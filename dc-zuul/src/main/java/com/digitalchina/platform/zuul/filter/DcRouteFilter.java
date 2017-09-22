package com.digitalchina.platform.zuul.filter;

import com.digitalchina.invoketrace.client.InvokeTrace;
import com.digitalchina.resttemplate.ribbon.retryable.RetryableLoadbalancedRestTemplateUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ROUTE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;

/**
 * @author liuyd
 * Date: 2017/9/1.
 * Route filters are used to make requests to other services.
 */
public class DcRouteFilter extends ZuulFilter {
    private static final int BUFFER_SIZE = 1024;
    public static final int order = 1;

    @Autowired
    private ProxyRequestHelper helper;
    @Autowired
    RetryableLoadbalancedRestTemplateUtil restTemplate;


    @Override
    public String filterType() {
        return ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return order;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (ctx.getRouteHost() == null && ctx.get(SERVICE_ID_KEY) != null
                && ctx.sendZuulResponse());
    }

    @Override
    public Object run() {
        // if you need prevent some request header from read by other service , run this function
        // this.helper.addIgnoredHeaders();
        RequestContext context = RequestContext.getCurrentContext();

        try {
            forward(context);
            // prevent SimpleHostRoutingFilter from running
            context.setRouteHost(null);
            return null;
        } catch (Exception e) {
            throw new ZuulRuntimeException(e);
        }
    }

    private ClientHttpResponse forward(RequestContext context) throws UnsupportedEncodingException{

        HttpServletRequest request = context.getRequest();
        HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());

        String uri = this.helper.buildZuulRequestURI(request);
        // remove double slashes
        uri = uri.replace("//", "/");
        String queryString = URLDecoder.decode(request.getQueryString(),"UTF-8");
        if (!StringUtils.isEmpty(queryString))
            uri = uri + "?" + queryString;

        String appName = (String) context.get(SERVICE_ID_KEY);

        RouteRequestCallback requestCallback = new RouteRequestCallback(context);
        RouteResponseExtractor responseExtractor = new RouteResponseExtractor();

        return (ClientHttpResponse) restTemplate.execute(appName, uri, httpMethod, requestCallback, responseExtractor);
    }

    private void setResponse(ClientHttpResponse response) throws IOException {
        // this response will closed ,there are no means to set ZuulResponse
        //  RequestContext.getCurrentContext().set("zuulResponse", response);
        this.helper.setResponse(response.getStatusCode().value(),
                copyResponseBody(response.getBody()) , response.getHeaders());

    }

    private InputStream copyResponseBody(InputStream responseBody) throws IOException{
        if (responseBody == null)
            return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        while ((len = responseBody.read(buffer)) > -1 ) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.flush();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private class RouteRequestCallback implements RequestCallback, InvokeTrace {
        private RequestContext requestContext;

        public RouteRequestCallback(RequestContext requestContext) {
            this.requestContext = requestContext;
        }

        @Override
        public void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException {
            HttpServletRequest request = requestContext.getRequest();

            //add headers
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                Enumeration<String> values = request.getHeaders(name);

                while (values.hasMoreElements()) {
                    String value = values.nextElement();
                    clientHttpRequest.getHeaders().add(name, value);
                }
            }
            //request body
            String method = request.getMethod().toUpperCase();
            if (!HttpMethod.POST.toString().equals(method)
                    && !HttpMethod.PUT.toString().equals(method)
                    && !HttpMethod.DELETE.toString().equals(method)
                    ) {
                return;
            }
            ServletInputStream sin = null;
            OutputStream out = null;
            try {
                sin = request.getInputStream();
                out = clientHttpRequest.getBody();
                byte[] b = new byte[BUFFER_SIZE];

                int i = sin.read(b);
                while (i != -1) {
                    out.write(b, 0, i);
                    i = sin.read(b);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                try {
                    if (sin != null)
                        sin.close();
                    if (out != null)
                        out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public byte[] getBytes() {
            HttpServletRequest request = requestContext.getRequest();
            if (!request.getHeaders("Content-Type").equals(MediaType.MULTIPART_FORM_DATA)) {

                Map<String, String[]> params = request.getParameterMap();
                String queryString = "";
                for (String key : params.keySet()) {
                    String[] values = params.get(key);
                    for (int i = 0; i < values.length; i++) {
                        String value = values[i];
                        queryString += key + "=" + value + "&";
                    }
                }

                if (!StringUtils.isEmpty(queryString)) {
                    queryString = queryString.substring(0, queryString.length() - 1);
                    return queryString.getBytes();
                }
            }
            return null;
        }

        @Override
        public HttpHeaders getHeaders() {
            HttpServletRequest request = requestContext.getRequest();
            HttpHeaders headers = new HttpHeaders();
            Enumeration<String> enu = request.getHeaderNames();
            if (enu != null) {
                while (enu.hasMoreElements()) {
                    String key = enu.nextElement();
                    String value = request.getHeader(key);
                    headers.add(key, value);
                }
            }

            headers.add("Host", (String) requestContext.get(SERVICE_ID_KEY));

            return headers;
        }
    }

    private class RouteResponseExtractor implements ResponseExtractor<Object> {
        @Override
        public Object extractData(ClientHttpResponse clientHttpResponse) throws IOException {
            setResponse(clientHttpResponse);
            return null;
        }
    }

}
