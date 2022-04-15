package com.dxhy.order.util;

import com.alibaba.fastjson.JSONObject;
import com.dxhy.order.model.EmailContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * @ClassName SendMailUtil
 * @Description TODO
 * @Author wangruwei
 * @Date 2020-08-29 19:06
 * @Version 1.0
 */
@Slf4j
@Component
public class SendMailUtil {

    private String messageType = "text/html;charset=UTF-8";//相应内容类型，编码类型

    private String sucCode = "0000";

    private String failCode = "9999";


    public static String smtpHost;


    public static String hostPort;


    public static String smtpName;


    public static String logoPath;

    public static String addressPass;

    @Value("${mail.qq.smtphost}")
    public void setSmtpHost(String smtpHost) {
        SendMailUtil.smtpHost = smtpHost;
    }

    @Value("${mail.qq.hostport}")
    public void setHostPort(String hostPort) {
        SendMailUtil.hostPort = hostPort;
    }

    @Value("${mail.qq.smtpname}")
    public void setSmtpName(String smtpName) {
        SendMailUtil.smtpName = smtpName;
    }


    @Value("${mail.logoPath}")
    public void setLogoPath(String logoPath) {
        SendMailUtil.logoPath = logoPath;
    }


    @Value("${mail.addressPass}")
    public void setAddressPass(String addressPass)
    {
        SendMailUtil.addressPass = addressPass;
    }



    /**
     *
     * @param ifNeedAccount    是否需要传账号密码，false不传，true传
     * @param fromEmail    发送人邮箱地址，根据ifNeedAccount来决定是否可以为空
     * @param password    发送人邮箱密码址，根据ifNeedAccount来决定是否可以为空
     * @param emailTitle 邮件标题
     * @param content    邮件内容
     * @param senderName    发送人显示名称
     * @param contactEmails    收件人地址
     * @param ccEmails    抄送人地址
     * @param filesList    附件
     * @param pics
     * @return
     * TODO
     * ApiSendEmailServiceImpl.java
     * author wangruwei
     * 2019年6月28日
     * @throws Exception
     */
    private Map<String,String> sendEmail(boolean ifNeedAccount
            , String fromEmail
            , String password
            , String emailTitle
            , String content
            , String senderName
            , String[] contactEmails
            , String[] ccEmails
            , List<File> filesList
            , boolean showLogo) throws Exception {
        try {
            //第一步：配置javax.mail.Session对象
            Properties props = new Properties();   // 创建Properties 类用于记录邮箱的一些属性
            //props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
            InputStream io = null;

            log.info("发送邮件：发件人地址{},发件人密码{},收件人地址{}",fromEmail,password,contactEmails);
            if(!EmailUtils.validateEmail(fromEmail)){
                return getFailRtn("发件人邮箱格式不正确");
            }
            //根据邮箱地址判断邮箱服务器是哪个
            String mailSource = fromEmail.substring(fromEmail.lastIndexOf("@") + 1);
            mailSource = mailSource.substring(0, mailSource.indexOf("."));
//            String mailSource = fromEmail.substring(fromEmail.lastIndexOf("@")+1,fromEmail.lastIndexOf("."));;

            /**
             props.put("mail.smtp.host", smtpHost);  //此处填写SMTP服务器
             props.put("mail.smtp.starttls.enable", "true");//使用 STARTTLS安全连接
             props.put("mail.smtp.ssl.enable", "true");
             props.put("mail.smtp.port", hostPort);             //google使用465或587端口
             props.put("mail.smtp.auth", auth);       // 表示SMTP发送邮件，必须进行身份验证
             props.put("mail.debug", "true");      //开启调试模式
             props.put("mail.transport.protocol", smtpName);     // 发送邮件协议名称
             props.setProperty("mail.smtp.socketFactory.port", hostPort);
             */
            //props.put("mail.smtp.ssl.enable", "true");
            props.setProperty("mail.smtp.host", smtpHost);  //此处填写SMTP服务器
            props.setProperty("mail.smtp.port", hostPort);             //google使用465或587端口
            props.put("mail.smtp.auth", true);       // 表示SMTP发送邮件，必须进行身份验证
            props.put("mail.info", "true");      //开启调试模式
            props.put("mail.transport.protocol", smtpName);     // 发送邮件协议名称
            //props.setProperty("mail.smtp.socketFactory.port", "465");
            //props.put("mail.smtp.ssl.socketFactory", sf);
            Session mailSession = Session.getInstance(props, new MyAuthenticator(fromEmail, password));//此处填写你的账号和口令(16位口令)
            //第二步：编写消息
            //InternetAddress toAddress = new InternetAddress(to); // 设置收件人的邮箱
            MimeMessage message = new MimeMessage(mailSession);
            //防止成为垃圾邮件，披上outlook的马甲
            message.addHeader("X-Mailer","Microsoft Outlook Express 6.00.2900.2869");
            if(StringUtils.isEmpty(senderName)){
                senderName = fromEmail;
            }
            message.setFrom(new InternetAddress(fromEmail, senderName, "UTF-8"));
            //因为有的邮箱是把主题作为显示位的，所以主题跟标题弄成一样的。
            message.setSubject(emailTitle);
            int index = contactEmails.length;
            InternetAddress[] sendTo = new InternetAddress[index];
            for (int i = 0 ;i < index; i++) {
                if(!StringUtils.isEmpty(contactEmails[i])){
                    sendTo[i] = new InternetAddress(contactEmails[i]);
                }
            }
            //message.setRecipient(RecipientType.TO, new String[moreUsers.size()]);
            //收件人
            message.setRecipients(Message.RecipientType.TO, sendTo);

            //抄送人
            int ccindex = ccEmails.length;
            InternetAddress[] ccTo = new InternetAddress[ccindex];
            for (int i = 0; i < ccindex; i++) {
                if(!StringUtils.isEmpty(ccEmails[i])){
                    ccTo[i] = new InternetAddress(ccEmails[i]);
                }
            }
            if(ccTo.length>0){
                message.setRecipients(Message.RecipientType.CC, ccTo);
            }

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();

            /*
            * @Description 要在正文中添加图片，需要先写图片元素，然后在下面把id赋值进去，否则会进附件且乱码
            **/

            //如果展示logo，内容里加logo位置和元素id
            String showLogoStr = "";
            if(showLogo){
                showLogoStr = "<div><img src='cid:logo0' style='height:150px;width:700px;'/></div>";
                // 正文的图片部分
                MimeBodyPart jpgBody = new MimeBodyPart();
                //FileDataSource fds = new FileDataSource("E:\\workspace\\xxddutil\\src\\main\\resources\\logo.jpg");
                FileDataSource fds = new FileDataSource(FileUtil.getResourcePath()+"logo.jpg");
                jpgBody.setDataHandler(new DataHandler(fds));
                jpgBody.setContentID("logo0");
                multipart.addBodyPart(jpgBody);
            }


            // 添加邮件正文
            BodyPart textPart = new MimeBodyPart();
            textPart.setContent(content + showLogoStr, messageType);
            multipart.addBodyPart(textPart);

            //邮件附件
            if (filesList != null&&filesList.size()>0) {
                for (File attachment:filesList) {
                    BodyPart attachmentBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(attachment);
                    attachmentBodyPart.setDataHandler(new DataHandler(source));

                    // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                    // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                    //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                    //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");

                    //MimeUtility.encodeWord可以避免文件名乱码
                    attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                    multipart.addBodyPart(attachmentBodyPart);
                }
            }



            message.setSentDate(Calendar.getInstance().getTime());
            message.setSubject(emailTitle);   // 设置邮件标题

            // 将multipart对象放到message中
            message.setContent(multipart);

            // 第三步：发送消息

            Transport.send(message);
            return getSussRtn(null, "发送邮件服务器成功");
        } catch (Exception e) {
            log.error("邮件发送错误:{}",e);
            return getFailRtn("邮件发送错误"+e.getMessage());
//            throw new Exception("邮件发送错误", e);
        }
    }

    /**
     * 获取成功的返回内容
     *
     * @param data
     * @author chenrui
     * @return
     */
    protected Map<String, String> getSussRtn(String data, String msg) {
        Map<String, String> rtn = new HashMap<String, String>();
        rtn.put("code", sucCode);
        rtn.put("msg", msg);
        rtn.put("data", data);
        return rtn;
    }

    @PostConstruct
    private void init() {
        // 默认java关于邮箱的参数splitlongparameters为true, 即太长会处理为.bin
        // 我们手动关闭即可
        System.setProperty("mail.mime.splitlongparameters", "false");
        System.setProperty("mail.mime.charset", "UTF-8");
    }

    /**
     * 获取失败的返回内容
     *
     * @param msg
     * @author chenrui
     * @return
     */
    protected Map<String, String> getFailRtn(String msg) {
        Map<String, String> rtn = new HashMap<String, String>();
        rtn.put("code", failCode);
        rtn.put("msg", msg);
        rtn.put("data", null);
        return rtn;
    }

    public Map sendMail(EmailContent content) throws Exception  {
        JSONObject.toJSONString(content);
        if(null==content.getSubjects()||"".equals(content.getSubjects())){
            return getFailRtn("邮件标题(主题)不能为空");
        }
        if(null==content.getTo()||content.getTo().length==0){
            return getFailRtn("收件人信息不能为空");
        }
        if(null==content.getCC()||content.getCC().length==0){
            content.setCC(null);
        }

        //获取内容
        String mailcontent = content.getContents();
        //邮件标题
        String Subjects = content.getSubjects();

        String[] zhPass = addressPass.split("~~");
        if(zhPass.length!=2){
            return getFailRtn("请先配置账号密码");
        }

        //处理附件
        //附件列表
        List<File> filesList = new LinkedList<File>();
        //如果有附件,此处例子2个，第一个jpg，第二个txt
        filesList.add(new File(FileUtil.getResourcePath()+"temp/图片附件.jpg"));
        filesList.add(new File(FileUtil.getResourcePath()+"temp/txt文本.txt"));
        //从传来的值里面获取服务器位置上的文件
        if(!CollectionUtils.isEmpty(content.getFjFiles())){
            for (int i = 0; i < content.getFjFiles().size() ; i++) {
                filesList.add(new File(content.getFjFiles().get(i)));
            }
        }


        Map<String, String> sendEmail = sendEmail(content.isIF_NEED_ACCOUNT(),
                zhPass[0],zhPass[1],Subjects,
                mailcontent,content.getSenderName(),content.getTo(),
                content.getCC()==null?new String[]{}:content.getCC(),
                filesList,
                content.isShowlogo());

        //发送完了把创建的附件删除
        /*for (File file:filesList) {
            file.delete();
        }
*/
        return sendEmail;
    }



}

