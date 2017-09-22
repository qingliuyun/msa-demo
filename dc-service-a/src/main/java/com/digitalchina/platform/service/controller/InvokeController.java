package com.digitalchina.platform.service.controller;

import com.digitalchina.platform.service.service.InvokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvokeController {
    @Autowired
    InvokeService invokeService;

    @RequestMapping(value = "/invoke.do", method = RequestMethod.GET)
    public Double plus(@RequestParam String service,@RequestParam String contextPath) {
        return invokeService.doPlus(service,"/".equals(contextPath)?"":contextPath);
    }

}
