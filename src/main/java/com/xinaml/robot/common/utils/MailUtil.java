package com.xinaml.robot.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class MailUtil {
    private static Logger LOG = LoggerFactory.getLogger(MailUtil.class);
    public static String SERVER_MAIL = "xinaml@163.com";
    public static String SERVER_PASSWORD = "liguiqin123";
    public static String SMTP_HOST = "smtp.163.com";

    public static void send(String mail, String subject, String content) {
        if (StringUtils.isNotBlank(mail) && StringUtils.isNotBlank(subject) && StringUtils.isNotBlank(content)) {
            Session session = getSession();
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(SERVER_MAIL, "ROBOT服务", "utf-8"));
                // 代表收件人
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(mail, "李四", "utf-8"));
                // 设置邮件主题
                message.setSubject(subject);
                // 设置邮件内容
                message.setContent(content, "text/html;charset=utf-8");
                // 设置发送时间
                message.setSentDate(new Date());
                // 保存上面的编辑内容
                message.saveChanges();
                Transport trans = session.getTransport();
                // 链接邮件服务器
                trans.connect(SERVER_MAIL, SERVER_PASSWORD);
                // 发送信息
                trans.sendMessage(message, message.getAllRecipients());
                // 关闭链接
                trans.close();
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }else {
            LOG.error("邮件发送失败：mail="+mail+", subject="+subject+", content="+content);
        }
    }
    private static Session getSession() {
        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.host", SMTP_HOST);
        prop.setProperty("mail.smtp.auth", "true");
        Session session = Session.getInstance(prop);
        return session;
    }

    public static void main(String[] args) throws Exception {
        MailUtil.send("xinaml@qq.com","今天回家吃饭好吗","写代码没出息的！");
    }

}
