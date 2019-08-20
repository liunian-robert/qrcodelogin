<!DOCTYPE html>
<html>
<head>
    <title>二维码扫码登录</title>
    　　　<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport' />
    　　　<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<body>
<input id="token" type="hidden" value="" />
<img id="qrcode" width="280" style="margin-left:750px;margin-top:280px;" height="280" src="" />
</body>
</body>
<script type="text/javascript" src="http://libs.baidu.com/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        $.get("http://localhost:8080/qrcodelogin/rest/qrcodelogin/qrcode", function(data, status) {
            $("#qrcode").attr("src","data:image/png;base64,"+data.base64Qrcode);
            if ('WebSocket' in window) {//websocket方式
                startWebsocket(data.token);
            } else {//如果不支持采用长轮询方式
                startLongPolling(data.token);
            }
        });
    });
    function startWebsocket(token) {
        var websocket = new WebSocket("ws://localhost:8000/qrcodelogin/loginpage?token="+token);
        websocket.a
        //连接发生错误的回调方法
        websocket.onerror = function () {
            alert("WebSocket连接发生错误");
        };

        //连接成功建立的回调方法
        websocket.onopen = function () {
            console.log("WebSocket连接成功");
        }

        //接收到消息的回调方法
        websocket.onmessage = function (event) {
            alert(event.data);
            if(event.data =='201'){
                weblogin(token,true);
            }
            if(event.data=='202') {
                alert("二维码扫描成功，提示app确认");
            }
            if(event.data=='203') {
                alert("二维码失效");
                //失效后关闭当前websocket连接(此处不能后端关闭，因为在关闭前需要页面显示失效通知信息)
                websocket.send("close#"+token);
                $.get("http://localhost:8080/qrcodelogin/rest/qrcodelogin/qrcode", function(data, status) {
                    $("#qrcode").attr("src","data:image/png;base64,"+data.base64Qrcode);
                    if ('WebSocket' in window) {//websocket方式
                        startWebsocket(data.token);
                    } else {//如果不支持采用长轮询方式
                        startLongPolling(data.token);
                    }
                });
            }
        }

        //连接关闭的回调方法
        websocket.onclose = function () {
            console.log("WebSocket连接关闭");
        }

        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function () {
            websocket.close();
        }
    }

    //长轮询方式二维码登录
    function startLongPolling(token) {
        console.log("定时任务执行中!");
        $.get("http://localhost:8080/qrcodelogin/rest/qrcodelogin/qrcode/longpolling/"+token, function(data, status) {
            if(data =='201'){
                weblogin(token,false);
            } else if(data=='202') {
                alert("二维码扫描成功，提示app确认");
                startLongPolling(token);
            }else if(data=='203') {
                alert("二维码失效");
                startLongPolling(token);
            } else if(data == '408') {//继续轮询
                console.log("定时任务执行中!");
                startLongPolling(token);
            } else {
                alert("服务器异常!");
                startLongPolling(token);
            }
        });
    }
    function weblogin(token,isWebsocket) {
        $.ajax({
            url: "http://localhost:8080/qrcodelogin/rest/qrcodelogin/weblogin/"+token,
            type: "GET",
            xhrFields: {
                withCredentials: true
            },
            success: function(data) {
                if (!isWebsocket) {
                    windown.clearInterval();
                }
                if (data != null) {
                    alert("二维码扫描登录成功!");
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {

            }
        });
    }
</script>
</html>