package cn.itcast.travel.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 发邮件工具类
 */
public final class SendEamil {
    private static final String USER = "yy285790027@163.com"; // 发件人称号，同邮箱地址
    private static final String PASSWORD = "yangyi122"; // 如果是qq邮箱可以使户端授权码，或者登录密码
    // QQ邮箱的 SMTP 服务器地址为: smtp.qq.com
    private static String myEmailSMTPHost = "smtp.qq.com";

    //发件人邮箱
    private static String myEmailAccount = "发件人邮箱";

    //发件人邮箱密码（授权码）
    //在开启SMTP服务时会获取到一个授权码，把授权码填在这里
    private static String myEmailPassword = "pubb rjts jcqq bjad";
    SendEamil sendEamil=new SendEamil();
    public static void main(String[] args) throws Exception {

        String verifyCode="验证码";
        String toEmailAddress="3328263295@qq.com";
        //邮件主题
        String emailTitle = "【xxx】邮箱验证";
        //邮件内容
        String emailContent = "您正在【xxx】进行邮箱验证，您的验证码为：" + verifyCode + "，请于2分钟内完成验证！";
        //发送邮件
        sendEmail( toEmailAddress,emailTitle, emailContent);
    }

    public static void sendEmail(String toEmailAddress, String emailTitle, String emailContent) throws Exception {

        Properties props = new Properties();

        // 开启debug调试
        props.setProperty("mail.debug", "true");

        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");

        // 端口号465/587
        props.put("mail.smtp.port", 465);

        // 设置邮件服务器主机名
        props.setProperty("mail.smtp.host", myEmailSMTPHost);

        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        //SSL认证，腾讯邮箱是基于SSL加密的，所以需要开启才可以使用
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);

        //设置是否使用ssl安全连接（一般都使用）
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        //创建会话
        Session session = Session.getInstance(props);

        //获取邮件对象
        //发送的消息，基于观察者模式进行设计的
        Message msg = new MimeMessage(session);

        //设置邮件标题
        msg.setSubject(emailTitle);

        //设置邮件内容
        //使用StringBuilder，因为StringBuilder加载速度会比String快，而且线程安全性也不错
        StringBuilder builder = new StringBuilder();

        //写入内容
        builder.append("\n" + emailContent);

        //写入我的官网
        builder.append("\n官网：" + "https://www.baidu.com");

        //定义要输出日期字符串的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //在内容后加入邮件发送的时间
        builder.append("\n时间：" + sdf.format(new Date()));

        //设置显示的发件时间
        msg.setSentDate(new Date());

        //设置邮件内容
        msg.setText(builder.toString());

        //设置发件人邮箱
        // InternetAddress 的三个参数分别为: 发件人邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
        msg.setFrom(new InternetAddress(myEmailAccount, "发件人昵称", "UTF-8"));

        //得到邮差对象
        Transport transport = session.getTransport();

        //连接自己的邮箱账户
        //密码不是自己QQ邮箱的密码，而是在开启SMTP服务时所获取到的授权码
        //connect(host, user, password)
        transport.connect(myEmailSMTPHost, myEmailAccount, myEmailPassword);

        //发送邮件
        transport.sendMessage(msg, new Address[]{new InternetAddress(toEmailAddress)});

        //将该邮件保存到本地
//        OutputStream out = new FileOutputStream("MyEmail.eml");
//        msg.writeTo(out);
//        out.flush();
//        out.close();

        transport.close();
    }




    /**
     *
     * @param to 收件人邮箱
     * @param text 邮件正文
     * @param title 标题
     */
    /* 发送验证信息的邮件 */
    public static boolean sendMail(String to, String text, String title){
        try {
            final Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", "smtp.qq.com");

            // 发件人的账号
            props.put("mail.user", USER);
            //发件人的密码
            props.put("mail.password", PASSWORD);

            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            String username = props.getProperty("mail.user");
            InternetAddress form = new InternetAddress(username);
            message.setFrom(form);

            // 设置收件人
            InternetAddress toAddress = new InternetAddress(to);
            message.setRecipient(Message.RecipientType.TO, toAddress);

            // 设置邮件标题
            message.setSubject(title);

            // 设置邮件的内容体
            message.setContent(text, "text/html;charset=UTF-8");
            // 发送邮件
            Transport.send(message);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

//    public static void main(String[] args) throws Exception { // 做测试用
//        MailUtils.sendMail("285790027@qq.com","你好，这是一封测试邮件，无需回复。","测试邮件");
//        System.out.println("发送成功");
//    }
}
