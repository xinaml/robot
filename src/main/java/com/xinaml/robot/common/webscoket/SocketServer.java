package com.xinaml.robot.common.webscoket;

import com.xinaml.robot.common.utils.StringUtil;
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
 * @Description: fileId 由前端生成唯一的文件uuid
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
@ServerEndpoint("/websocket/{fileId}")
@Component
public class SocketServer {

    static Logger LOG = LoggerFactory.getLogger(SocketServer.class);
    private static ConcurrentHashMap<String, Session> fileSessionMap = new ConcurrentHashMap();//文件会话


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("fileId") String fileId) {
        fileSessionMap.put(fileId, session);     //把文件加入会话列表中

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        fileSessionMap.remove(this);  //从会话列表中删除
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
    }

    /**
     * 发生错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
        close(session);
        fileSessionMap.remove(this);  //从列表中删除

    }

    /**
     * 实现服务器主动推送
     */
    public static void sendMessage(String fileId, String message) {

        Session session = fileSessionMap.get(fileId);
        if (null != session) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    LOG.error("websocket IO异常");
                }
            } else {//如果已经关闭，直接从session移除
                fileSessionMap.remove(fileId);
            }
        }

    }

    /**
     * 推送文件上传进度
     *
     * @param fileId 文件id
     * @param index  当前索引
     * @param count  总数据量
     */
    public static void sendProgress(String fileId, int index, int count) {
        Session session = fileSessionMap.get(fileId);
        if (null != session) {
            if (session.isOpen()) {//是否为打开状态
                String progress = null;
                if (count >= index) { //总数据量>=当前索引
                    //计算进度
                    progress = index > 0 ? StringUtil.formatDoubleStr(String.valueOf((index + 0.0) / count * 100)) : "0";
                } else {//上传完毕
                    progress = "100";
                }
                try {
                    session.getBasicRemote().sendText(progress);//发送进度
                    if ("100".equals(progress)) {//服务端主动关闭
                        fileSessionMap.remove(fileId);
                        close(session);
                    }
                } catch (IOException e) {
                    LOG.error("websocket IO异常");
                }
            } else { //如果已经关闭，直接从session移除
                fileSessionMap.remove(fileId);
            }
        }
    }

    private static void close(Session session) {
        try {
            if (session.isOpen()) {
                session.close();
                fileSessionMap.remove(session);  //从列表中删除
            }
        } catch (IOException e) {
            LOG.error("关闭session发生错误");
        }
    }


}
