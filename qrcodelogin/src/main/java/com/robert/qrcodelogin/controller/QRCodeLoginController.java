package com.robert.qrcodelogin.controller;


import com.robert.qrcodelogin.bean.LoginResult;
import com.robert.qrcodelogin.bean.QRCode;
import com.robert.qrcodelogin.bean.QRCodeToken;
import com.robert.qrcodelogin.bean.QRCodeUser;
import com.robert.qrcodelogin.bean.User;
import com.robert.qrcodelogin.common.EmptyUtils;
import com.robert.qrcodelogin.common.QRCodeUtil;
import com.robert.qrcodelogin.service.UserLoginService;
import com.robert.qrcodelogin.websocket.QRCodeLogin;
import com.sun.jersey.client.impl.CopyOnWriteHashMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public static CopyOnWriteHashMap<String,QRCodeUser> loginUsers = new CopyOnWriteHashMap<String,QRCodeUser>();
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
    public String scanQRCode(HttpServletRequest request, HttpServletResponse response, @PathVariable("token") String token) throws Exception {
        try {
            String client = request.getHeader("lbclient");
            if (EmptyUtils.isEmpty(client)) {//非myluban app扫描
                String userAgent = request.getHeader("user-agent").toLowerCase();
                if (EmptyUtils.isEmpty(userAgent)) {
                    throw new Exception("非法扫描!");
                }
                if(userAgent.indexOf("android") != -1){//安卓
                    String thirdQrcodeLoginUrl = " https://www.pgyer.com/MylubanApp_Android";
                    response.sendRedirect(thirdQrcodeLoginUrl);
                } else if(userAgent.indexOf("iphone") != -1 || userAgent.indexOf("ipad") != -1 || userAgent.indexOf("ipod") != -1){//苹果
                    String thirdQrcodeLoginUrl = "https://itunes.apple.com/cn/app/myluban/id1037676936";
                    response.sendRedirect(thirdQrcodeLoginUrl);
                } else {
                    throw new Exception("非法扫描!");
                }
                return null;
            }
            if (isToken(token)) {
                QRCodeLogin qrcodeLogin = QRCodeLogin.getWebSocketMap().get(token);
                if (isQRCodeExpired(token) && qrcodeLogin != null){
                    qrcodeLogin.sendMessage("203");
                    qrcodeLogin.setPushed(Boolean.TRUE);
                    setQRCodeExpired(token);
                    throw  new Exception("二维码失效");
                }
                //1.扫描二维码与token进行绑定
                QRCodeUser qrCodeUser = new QRCodeUser();
                String username = "username";
                qrCodeUser.setUsername(username);
                qrCodeUser.setAuthorize(false);
                loginUsers.put(token,qrCodeUser);
                // 5.通知web端扫码完成
                if (qrcodeLogin != null) {
                    qrcodeLogin.sendMessage("202");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("扫描二维码失败!");
        }
        String authUrl = "http://localhost:8080/myluban"+"/"+"rest/qrcodelogin/qrcode/authorize/" + token;
        return authUrl;
    }

    /**
     * @Author: zhangyapo
     * @Date: 2018/06/27 0010 18:00
     * @Description: app扫描二维码后点击取消
     * @param:
     * @return:
     */
    @ApiOperation(value = "app扫描二维码后点击取消", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/qrcode/cancle/{token}")
    public Boolean cancleQRCodeLogin(HttpServletRequest request, @PathVariable("token") String token) throws Exception {
        try {
            if (isToken(token)) {
                QRCodeLogin qrcodeLogin = QRCodeLogin.getWebSocketMap().get(token);
                loginUsers.remove(token);
                if (qrcodeLogin != null) {
                    qrcodeLogin.sendMessage("203");
                    qrcodeLogin.setPushed(Boolean.TRUE);
                    setQRCodeExpired(token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("app取消用户登录失败!");
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
                if (isQRCodeExpired(token) && qrcodeLogin != null){
                    if (qrcodeLogin.getSession().isOpen()) {
                        qrcodeLogin.sendMessage("203");
                        qrcodeLogin.setPushed(Boolean.TRUE);
                        setQRCodeExpired(token);
                        throw  new Exception("二维码失效");
                    } else {
                        throw  new Exception("二维码失效");
                    }
                }
                // 3.app对该用户授权
                QRCodeUser qrCodeUser = new QRCodeUser();
                String username = "username";
                qrCodeUser.setUsername(username);
                qrCodeUser.setAuthorize(true);
                loginUsers.put(token,qrCodeUser);
                // 5.通知web端授权成功
                if (isQRCodeExpired(token) && qrcodeLogin != null){
                    qrcodeLogin.sendMessage("201");
                    //授权成功,取消二维码失效定时器
                    qrcodeLogin.getTimer().cancel();
                    qrcodeLogin.setTimer(null);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new Exception("app授权用户登录失败!");
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
     * @Description: 兼容不支持websocket浏览器的长轮询方式
     * @param:
     * @return:
     */
    @ApiOperation(value = "兼容不支持websocket浏览器的长轮询方式", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/qrcode/longpolling/{token}", method = RequestMethod.GET)
    public int longpolling(@PathVariable String token) throws Exception {
        int code = 0;
        try {
            Thread.sleep(5000);
            //201:app授权成功，202：扫码完成(提示app确认)，203：二维码失效
            //1.判断二维码是否失效
            if (isQRCodeExpired(token)) {
                code = 203;//二维码失效
                return code;
            }
            //2.判断是否扫码完成
            QRCodeUser user = loginUsers.get(token);
            if (user == null) {
                code = 408;
                return code;
            }
            if (user != null) {
                String username = user.getUsername();
                if (username != null && !username.equals("") && !user.getAuthorize()) {
                    code = 202;//扫码完成
                    return code;
                }
            }
            //3.判断是否授权成功
            if (user != null) {
                String username = user.getUsername();
                if (username != null && !username.equals("") && user.getAuthorize()) {
                    code = 201;//授权成功
                    return code;
                }
            }
        } catch (Exception e) {
            logger.error("生成二维码失败!");
            throw new RuntimeException("生成二维码失败!");
        }
        return code;
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

    /**
     * @Author: zhangyapo
     * @Date: 2018/06/27 0010 18:00
     * @Description: 根据token查找对应的二维码
     * @param:token
     * @return:boolean
     */
    private Boolean setQRCodeExpired(String token) {
        Boolean result = Boolean.FALSE;
        for (QRCodeToken code : QRCodeLoginController.tokens) {
            String otoken = code.getToken();
            if (otoken.equals(token)) {
                code.setExpireTime(System.currentTimeMillis()-1);
                result = Boolean.TRUE;
                break;
            }
        }
        return result;
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
