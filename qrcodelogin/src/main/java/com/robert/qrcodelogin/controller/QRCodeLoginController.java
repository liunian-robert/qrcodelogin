package com.robert.qrcodelogin.controller;


import com.robert.qrcodelogin.bean.LoginResult;
import com.robert.qrcodelogin.bean.QRCode;
import com.robert.qrcodelogin.bean.QRCodeToken;
import com.robert.qrcodelogin.bean.QRCodeUser;
import com.robert.qrcodelogin.bean.User;
import com.robert.qrcodelogin.common.QRCodeUtil;
import com.robert.qrcodelogin.service.UserLoginService;
import com.robert.qrcodelogin.websocket.QRCodeLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @创建人: zhangyapo
 * @创建时间: 2018/6/27 13:35
 * @描述:
 */
@RestController
@RequestMapping("/rest/qrcodelogin")
@Api(value = "/rest/qrcodelogin", description = "二维码扫描登录接口")
public class QRCodeLoginController {

    //存储二维码维一标识
    private Logger logger = Logger.getLogger(QRCodeLoginController.class);
    private static final Integer EXPIRED_TIME = 1;
    public static CopyOnWriteArraySet<QRCodeToken> tokens = new CopyOnWriteArraySet<QRCodeToken>();
    //存储toke绑定的用户
    public static ConcurrentHashMap<String,QRCodeUser> loginUsers = new ConcurrentHashMap<String,QRCodeUser>();
    @Resource
    private UserLoginService userLoginService;
    /**
     * @Author: zhangyapo
     * @Date: 2018/06/27 0010 18:00
     * @Description: 获取二维码和唯一标识
     * @param:
     * @return:
     */
    @ApiOperation(value = "获取二维码和唯一标识", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    public QRCode getQRCode() throws Exception {
        try {
            String uuid = UUID.randomUUID().toString();
            QRCodeToken token = new QRCodeToken();
            token.setToken(uuid);
            //二维码失效时间
            token.setExpireTime(System.currentTimeMillis()+ EXPIRED_TIME*60*1000);
            tokens.add(token);
            String mylubanurl = "http://localhost:8080/qrcodelogin/rest/qrcodelogin/qrcode/authorize/";
            String data = mylubanurl+uuid;
            String base64 = QRCodeUtil.toBase64WithLogo(data, 280, 280);
            QRCode qrcode = new QRCode();
            qrcode.setToken(uuid);
            qrcode.setBase64Qrcode(base64);
            return qrcode;
        } catch (Exception e) {
            logger.error("生成二维码失败!");
            throw new RuntimeException("生成二维码失败!");
        }
    }

    /**
     * @Author: zhangyapo
     * @Date: 2018/06/27 0010 18:00
     * @Description: app扫描二维码
     * @param:
     * @return:
     */
    @ApiOperation(value = "app扫描二维码", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/qrcode/scan/{token}")
    public Boolean scanQRCode(HttpServletRequest request, @PathVariable("token") String token) throws Exception {
        try {
            if (isToken(token)) {
                QRCodeLogin qrcodeLogin = QRCodeLogin.getWebSocketMap().get(token);
                //1.扫描二维码与token进行绑定
                QRCodeUser qrCodeUser = new QRCodeUser();
                String username = (String)request.getSession().getAttribute("username");
                qrCodeUser.setUsername(username);
                qrCodeUser.setAuthorize(false);
                loginUsers.put(token,qrCodeUser);
                // 5.通知web端扫码完成
                if (qrcodeLogin != null) {
                    qrcodeLogin.sendMessage("202");
                }
            }
        } catch (Exception e) {
            logger.error("扫描二维码失败!");
        }
        return Boolean.TRUE;
    }

    /**
     * @Author: zhangyapo
     * @Date: 2018/06/27 0010 18:00
     * @Description: app扫描二维码点击确定进行用户授权
     * @param:
     * @return:
     */
    @ApiOperation(value = "app扫描二维码点击确定进行用户授权", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/qrcode/authorize/{token}")
    public Boolean qrCodeAuthorize(HttpServletRequest request, @PathVariable("token") String token) throws Exception {
        try {
            if (isToken(token)) {
                QRCodeLogin qrcodeLogin = QRCodeLogin.getWebSocketMap().get(token);
                // 3.app对该用户授权
                QRCodeUser qrCodeUser = new QRCodeUser();
                String username = (String)request.getSession().getAttribute("username");
                qrCodeUser.setUsername(username);
                qrCodeUser.setAuthorize(true);
                loginUsers.put(token,qrCodeUser);
                // 5.通知web端授权成功
                if (qrcodeLogin != null) {
                    qrcodeLogin.sendMessage("201");
                    //授权成功,取消二维码失效定时器
                    qrcodeLogin.getTimer().cancel();
                    qrcodeLogin.setTimer(null);
                }
            }
        } catch (Exception e) {
            logger.error("app授权用户登录失败!");
        }
        return Boolean.TRUE;
    }

    /**
     * @Author: zhangyapo
     * @Date: 2018/06/27 0010 18:00
     * @Description: 二维码扫描无密码登录(web端与服务器建立cookie关系)
     * @param:
     * @return:
     */
    @ApiOperation(value = "二维码扫描无密码登录(web端与服务器建立cookie关系)", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/weblogin/{token}", method = RequestMethod.GET)
    public LoginResult send(HttpServletRequest request,@PathVariable String token) throws Exception {
        LoginResult loginResult = null;
        try {
            QRCodeUser qrCodeUser = loginUsers.get(token);
            if (qrCodeUser != null) {
                String username =(String)request.getSession().getAttribute("username");
                loginResult = new LoginResult();
                User user = new User();
                user.setUsername(qrCodeUser.getUsername());
                user.setType(User.QRCODE);
                List<Integer> organizationList = userLoginService.login(user);
                loginResult.setEnterpriseIdList(organizationList);
                loginResult.setPassport(username);
            }
        } catch (Exception e) {
            logger.error("扫码登录失败!");
        }
        return loginResult;
    }
    /**
     * @Author: zhangyapo
     * @Date: 2018/06/27 0010 18:00
     * @Description: 验证token合法性
     * @param:token
     * @return:boolean
     */
    private Boolean isToken(String token) {
        Boolean result = Boolean.FALSE;
        for (QRCodeToken code : tokens) {
            String otoken = code.getToken();
            if (otoken.equals(token)) {
                result = Boolean.TRUE;
            }
        }
        return result;
    }
}