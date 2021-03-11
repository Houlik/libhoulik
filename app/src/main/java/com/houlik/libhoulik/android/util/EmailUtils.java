package com.houlik.libhoulik.android.util;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Houlik on 2017/9/24.
 * 这是调用第三方邮箱工具发送到指定的邮箱地址
 */

public class EmailUtils {

    /**
     * 1795923974 adfoemqhjbkydgab
     * 422043828 gmmhioindgqsbibh
     */

    private Properties properties;
    private Session session;
    private Message message;
    private MimeMultipart multipart;

    private int countOfEmailSent;

    private enum MAIL_COMPANY {QQ,OTS,HOTMAIL,GMAIL,YAHOO,SINA,NONE}
    private MAIL_COMPANY mail_company = MAIL_COMPANY.NONE;

    //要发送Email地址
    private String[] mailTo = new String[countOfEmailSent];
    //邮件发送来源地址 xxxxxx@qq.com
    private String mailFrom;
    //SMTP主机地址 smtp.qq.com
    private String smtpHost;
    //端口 465 / 587 / 25
    private int port;
    //发送者邮箱账号 xxxxxx@qq.com
    private String emailAddress;
    //SMTP 验证码
    private String emailSMTPPassword;
    //头文件名
    private String title;
    //文件内容
    private String content;

    public static EmailUtils emailUtils = new EmailUtils();

    private EmailUtils() {
        super();
        this.properties = new Properties();
    }

    public static EmailUtils getInstance(){

        if(emailUtils == null){
            new EmailUtils();
        }
        return emailUtils;
    }

    /**
     * 这是设置需要发给多少个用户
     * @param countOfEmailSent
     */
    public void setCountOfEmailSent(int countOfEmailSent){
        this.countOfEmailSent = countOfEmailSent;
    }

    public int getCountOfEmailSent(){
        return countOfEmailSent;
    }

    /**
     * 输入文件主题
     * @param title
     */
    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    /**
     * 输入文件内容
     * @param content
     */
    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }

    /**
     * 要发送Email地址
     * @param mailTo
     */
    public void setMailTo(String[] mailTo) {
        this.mailTo = mailTo;
    }

    public String[] getMailTo(){
        return mailTo;
    }

    /**
     * 邮件发送来源地址
     * @param mailFrom
     */
    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailFrom(){
        return mailFrom;
    }

    /**
     * SMTP主机地址
     * @param smtpHost
     */
    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpHost(){
        return smtpHost;
    }

    /**
     * /端口
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    public int getPort(){
        return port;
    }

    /**
     * 发送者邮箱账号
     * @param emailAddress
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    /**
     * SMTP 验证码
     * @param emailSMTPPassword
     */
    public void setEmailSMTPPassword(String emailSMTPPassword) {
        this.emailSMTPPassword = emailSMTPPassword;
    }

    public String getEmailSMTPPassword(){
        return emailSMTPPassword;
    }

    private void setProperties(String host, String post){
        //地址
        this.properties.put("mail.smtp.host",host);
        //端口号
        this.properties.put("mail.smtp.post",post);
        //是否验证
        this.properties.put("mail.smtp.auth",true);
        this.session=Session.getInstance(properties);
        this.message = new MimeMessage(session);
        this.multipart = new MimeMultipart("mixed");
    }
    /**
     * 设置收件人
     * @param receiver
     * @throws MessagingException
     */
    private void setReceiver(String[] receiver) throws MessagingException{
        Address[] address = new InternetAddress[receiver.length];
        for(int i=0;i<receiver.length;i++){
            address[i] = new InternetAddress(receiver[i]);
        }
        this.message.setRecipients(Message.RecipientType.TO, address);
    }
    /**
     * 设置邮件
     * @param from 来源
     * @param title 标题
     * @param content 内容
     * @throws AddressException
     * @throws MessagingException
     */
    private void setMessage(String from,String title,String content) throws AddressException, MessagingException{
        this.message.setFrom(new InternetAddress(from));
        this.message.setSubject(title);
        //纯文本的话用setText()就行，不过有附件就显示不出来内容了
        MimeBodyPart textBody = new MimeBodyPart();
        textBody.setContent(content,"text/html;charset=gbk");
        this.multipart.addBodyPart(textBody);
    }
    /**
     * 添加附件
     * @param filePath 文件路径
     * @throws MessagingException
     */
    private void addAttachment(String filePath) throws MessagingException{
        FileDataSource fileDataSource = new FileDataSource(new File(filePath));
        DataHandler dataHandler = new DataHandler(fileDataSource);
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setDataHandler(dataHandler);
        mimeBodyPart.setFileName(fileDataSource.getName());
        this.multipart.addBodyPart(mimeBodyPart);
    }
    /**
     * 发送邮件
     * @param host 地址
     * @param emailAccount 账户名
     * @param emailPassword 密码
     * @throws MessagingException
     */
    private void sendEmail(String host,String emailAccount,String emailPassword) throws MessagingException{
        //发送时间
        this.message.setSentDate(new Date());
        //发送的内容，文本和附件
        this.message.setContent(this.multipart);
        this.message.saveChanges();
        //创建邮件发送对象，并指定其使用SMTP协议发送邮件
        Transport transport=session.getTransport("smtp");
        //登录邮箱
        transport.connect(host, port, emailAccount, emailPassword);
        //发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        //关闭连接
        transport.close();
    }

    /**
     * 发送邮件
     */
    public void sendingEmail(){
        setProperties(smtpHost, port+"");
        try {
            setReceiver(mailTo);
            setMessage(mailFrom,title,content);
            sendEmail(smtpHost,emailAddress,emailSMTPPassword);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当前所需的变量值是否为空
     * @return
     */
    private boolean  isAllVariableEmpty(){
        if(mailTo != null && mailFrom != null && smtpHost != null && port != 0 && emailAddress != null && emailSMTPPassword != null){
            return false;
        }
        return true;
    }

    //使用系统邮件，发送邮件到指定的邮箱
    public void sendingEmailByActionSend(Context context){
        Intent i = new Intent(Intent.ACTION_SEND);
        // i.setType("text/plain"); //模拟器请使用这行
        i.setType("message/rfc822"); // 真机上使用这行
        i.putExtra(Intent.EXTRA_EMAIL,
                new String[] { "houlik@126.com" });
        i.putExtra(Intent.EXTRA_SUBJECT, "建议主题");
        i.putExtra(Intent.EXTRA_TEXT, "请在此处输入您宝贵的建议！！！");
        context.startActivity(Intent.createChooser(i, "Select email application."));
    }
}
