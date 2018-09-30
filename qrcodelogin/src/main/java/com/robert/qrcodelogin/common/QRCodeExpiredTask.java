package com.robert.qrcodelogin.common;

import com.robert.qrcodelogin.bean.QRCodeToken;
import com.robert.qrcodelogin.controller.QRCodeLoginController;
import com.robert.qrcodelogin.websocket.QRCodeLogin;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.TimerTask;

/**
 * @创建人: zhangyapo
 * @创建时间: 2018/7/13 12:36
 * @描述: 二维码失效task
 */
public class QRCodeExpiredTask extends TimerTask implements Cloneable {
    private Logger logger = Logger.getLogger(QRCodeExpiredTask.class);
    private String token;
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void run() {
        if(isQRCodeExpired(token)) {//二维码失效
            QRCodeLogin qrcodeLogin = QRCodeLogin.getWebSocketMap().get(token);
            try {
                //发送一次就不再发送失效通知
                if (qrcodeLogin != null && !qrcodeLogin.getPushed()){
                    qrcodeLogin.sendMessage("203");
                    qrcodeLogin.setPushed(Boolean.TRUE);
                    QRCodeLoginController.tokens.remove(token);
                }
            } catch (IOException e) {
                logger.error("通知二维码失效失败!");
            }
        }
    }

    /**
     * @Author: zhangyapo
     * @Date: 2018/06/27 0010 18:00
     * @Description: 验证二维码是否过期
     * @param:token
     * @return:boolean
     */
    private Boolean isQRCodeExpired(String token) {
        Boolean result = Boolean.FALSE;
        for (QRCodeToken code : QRCodeLoginController.tokens) {
            String otoken = code.getToken();
            if (otoken.equals(token)) {
                long expireTime = code.getExpireTime();
                if (expireTime < System.currentTimeMillis()) {
                    result = Boolean.TRUE;
                }
            }
        }
        return result;
    }

}
