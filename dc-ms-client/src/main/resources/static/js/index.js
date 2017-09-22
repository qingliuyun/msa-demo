var index = function () {
    var status = "sleep";
    var newsetInterval = '',
        account = 0;
    var invokeCount = 0;

    setInterval(run, 5000);

    function run() {
        if ("sleep" == status) {
            return;
        }
        if(invokeCount > 5){
            return;
        }

        $.ajax(
            {
                url: contextPath + "/invoke.do",
                success: function () {
                    invokeCount++;
                }
            });
    }

    function start() {
        $('.img-bg').find('.fl')
            .transition({ width: '580px'}, 2000, 'linear');
        $('.img-bg').find('.fr').transition({ width: '0'}, 2000, 'linear');
        account ++;
        if(newsetInterval){
            clearInterval(newsetInterval);
        }
        newsetInterval = setInterval(changeFloat,2000);
        $("#start-btn").css('display','none');
        $("#pause-btn").css('display','block');
        status = "run";
        run();
    }

    function pause() {
        clearInterval(newsetInterval);
        account = 0;
        $("#pause-btn").css('display','none');
        $("#start-btn").css('display','block');
        status = "sleep";
    }

    function bindEvents() {
        $('body').undelegate()
            .delegate('#start-btn', 'click', start)
            .delegate('#pause-btn', 'click', pause)

    }

    function changeFloat(){
        if( $('.bg-div1').hasClass('fl')){
            $('.bg-div1').addClass('fr').removeClass('fl')
                .siblings('div').addClass('fl').removeClass('fr');
        }
        else {
            $('.bg-div1').addClass('fl').removeClass('fr')
                .siblings('div').addClass('fr').removeClass('fl');
        }
        changeWidth();
    }
    function changeWidth(){
        if(account % 2 == 0){
            $('.img-bg').find('.fl')
                .transition({ width: '580px'}, 2000, 'linear');
            $('.img-bg').find('.fr').transition({ width: '0'}, 2000, 'linear');
        }
        else {
            $('.img-bg').find('.fl').css('width', '580px');
            $('.img-bg').find('.fr').css('width', '0');
        }
        account ++;

    }

    function initialize() {
        $('.hide').removeClass('hide');
        bindEvents();
    }

    initialize();
};