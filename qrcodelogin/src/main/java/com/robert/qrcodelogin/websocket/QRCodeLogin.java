package com.robert.qrcodelogin.websocket;

/**
 * @创建人: zhangyapo
 * @创建时间: 2018/7/3 15:57
 * @描述:
 */
/**
 * @创建人: zhangyapo
 * @创建时间: 2018/7/2 14:15
 * @描述:检测app客户端是否授权用户登录
 */

import com.robert.qrcodelogin.common.QRCodeExpiredTask;
import com.sun.jersey.client.impl.CopyOnWriteHashMap;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.yeauty.annotation.*;
import org.yeauty.pojo.ParameterMap;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.Timer;

@ServerEndpoint(path = "/qrcodelogin/loginpage",host="0.0.0.0",port=8000,optionConnectTimeoutMillis=6000000)
@Component//如果是非springboot项目可去除
public class QRCodeLogin {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteHashMap<String, QRCodeLogin> webSocketMap = new CopyOnWriteHashMap<String, QRCodeLogin>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //判断二维码失效的定时器
    private Timer timer;
    //是否已经向前端推送了二维码失效通知
    private Boolean isPushed = Boolean.FALSE;
    private Logger logger = Logger.getLogger(QRCodeLogin.class);
    public static CopyOnWriteHashMap<String, QRCodeLogin> getWebSocketMap() {
        return webSocketMap;
    }
    //消息说明：201:app授权成功，202：扫码完成(提示app确认)，203：二维码失效

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public Boolean getPushed() {
        return isPushed;
    }

    public void setPushed(Boolean pushed) {
        isPushed = pushed;
    }

    /**
     *连接建立成功调用的方法
     *@param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap){
        String token = parameterMap.getParameter("token");
        this.session = session;
        //建立连接，创建二维码失效定时器 判断二维码是否失效，如果失效，则给web端发送websocket消息，通知二维码失效
        Timer timer = new Timer();
        QRCodeExpiredTask task = new QRCodeExpiredTask();
        task.setToken(token);
        timer.schedule(task,0,1000);//立即执行定时器，每隔1s执行一次
        this.setTimer(timer);
        webSocketMap.put(token,this);
        logger.info("有新连接加入！");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketMap.remove(this);  //从set中删除
        System.out.println("有一连接关闭！");
        logger.info("有一连接关闭！");
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("来自客户端的消息:" + message);
        if (!StringUtils.isEmpty(message)) {
            logger.info("收到消息：" + message);
            String active = message.split("#")[0];
            String token = message.split("#")[1];
            if (active.equals("close")) {//接收客户端发来的消息，断开连接
                QRCodeLogin qrcodeLogin = QRCodeLogin.webSocketMap.get(token);
                try {
                    if (qrcodeLogin != null) {
                        qrcodeLogin.getTimer().cancel();
                        qrcodeLogin.setPushed(Boolean.TRUE);
                        qrcodeLogin.getSession().close();
                    }
                    logger.info("连接关闭成功!");
                } catch (Exception e) {
                    logger.error("关闭websocket失败!");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 单对单发送消息
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        //this.session.getBasicRemote().sendText(message);
        this.session.sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 群发消息方法。
     * @param message
     * @throws IOException
     */
    public void sendMessageAll(String message) throws IOException{
        for(String token : webSocketMap.keySet()){
            QRCodeLogin qrCodeLogin = webSocketMap.get(token);
            qrCodeLogin.sendMessage(message);
        }
    }
    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        logger.error("发生错误");
    }


    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }
}
