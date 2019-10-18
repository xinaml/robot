package com.xinaml.robot.common.webscoket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: [lgq]
 * @Date: [19-7-2 上午9:03]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
//@ServerEndpoint("/websocket/{userId}")
//@Component
public class WebSocketServer {

    static Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);
    private static int onlineCount = 0;
    private static ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap();

    //接收sid
    private String userId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        webSocketMap.put(userId, session);     //加入set中
        addOnlineCount();           //在线数加1
        LOG.info("有新窗口开始监听:" + userId + ",当前在线人数为" + getOnlineCount());
        this.userId = userId;
        sendMessage(userId, "连接成功");

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketMap.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        LOG.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        LOG.info("收到来自窗口" + userId + "的信息:" + message);
        sendMessage(userId, message);

    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        LOG.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public static void sendMessage(String userId, String message) {
        try {
            Session session = webSocketMap.get(userId);
            if (null != session) {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(message);
                }
            }
        } catch (IOException e) {
            LOG.error("websocket IO异常");
        }
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) {
        sendMessage(userId, message);

    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
