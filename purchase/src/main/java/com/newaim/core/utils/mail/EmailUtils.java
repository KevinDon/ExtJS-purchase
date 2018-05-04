package com.newaim.core.utils.mail;

import com.newaim.core.utils.mail.api.AttacheHandler;
import com.newaim.core.utils.mail.api.HtmlHandler;
import com.newaim.core.utils.mail.model.Mail;
import com.newaim.core.utils.mail.model.MailAddress;
import com.newaim.core.utils.mail.model.MailAttachment;
import com.newaim.core.utils.mail.model.MailSeting;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.*;

public class EmailUtils {
    private static Log logger = LogFactory.getLog(EmailUtils.class);

    private Integer mailTotal;
    private Integer mailTotalNew = 0;

    private Integer mailTotalDownload = 0;

    /**
     * 邮件设置实体类
     */
    private MailSeting mailSetting;
    /**
     * 附件处理接口
     */
    private AttacheHandler handler;

    public Integer getMailTotal() throws MessagingException {
        return mailTotal;
    }

    public void setMailTotal(Integer mailTotal) {
        this.mailTotal = mailTotal;
    }

    public Integer getMailTotalDownload() {
        return mailTotalDownload;
    }

    public void setMailTotalDownload(Integer mailTotalDownload) {
        this.mailTotalDownload = mailTotalDownload;
    }

    public Integer getMailTotalNew() {
        return mailTotalNew;
    }

    public void setMailTotalNew(Integer mailTotalNew) {
        this.mailTotalNew = mailTotalNew;
    }

    public MailSeting getMailSetting() {
        return mailSetting;
    }

    public void setMailSetting(MailSeting mailSetting) {
        this.mailSetting = mailSetting;
    }

    public AttacheHandler getHandler() {
        return handler;
    }

    public void setHandler(AttacheHandler handler) {
        this.handler = handler;
    }

    /**
     * 设置Mail smtp属性
     *
     * @param host     主机
     * @param protocol 协议 一般情况为 smtp
     * @param fromMail 发送者邮箱
     * @return
     */
    private static Properties setProperties(String host, String protocol, String fromMail) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", true); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        props.put("mail.from", fromMail);
        return props;
    }

    public EmailUtils(MailSeting mailSetting) {
        this.mailSetting = mailSetting;

        try {
            Store store = getConnectedStore();
            Folder folder = getFolder(store);
            if (!folder.isOpen())  folder = getFolder(folder.getStore());
            this.mailTotal = folder.getMessageCount();
        }catch (Exception e){

        }
    }

    /**
     * 测试发送邮件服务器和接收邮件服务器连接情况
     *
     * @throws Exception
     * @throws MessagingException
     */
    public void connectSmtpAndReceiver() throws Exception {
        connectSmtp();
        connectReciever();
    }

    /**
     * 测试发送邮件服务器连接情况
     *
     * @throws MessagingException
     */
    public void connectSmtp() throws Exception {
        // 取得通道session
        Session session = getMailSession(MailSeting.SMTP_PROTOCAL);
        // 创建smtp连接
        Transport transport = null;
        try {
            transport = session.getTransport(MailSeting.SMTP_PROTOCAL);
            transport.connect(mailSetting.getSendHost(), mailSetting.getMailAddress(), mailSetting.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
    }

    /**
     * 测试接收邮件服务器连接情况
     *
     * @throws MessagingException
     */
    public void connectReciever() throws MessagingException {
        Store store = null;
        try {
            Session session = getMailSession(mailSetting.getProtocal());
            // 创建连接
            URLName urln = new URLName(
                    mailSetting.getProtocal(),
                    mailSetting.getReceiveHost(),
                    Integer.parseInt(mailSetting.getReceivePort()),
                    null,
                    mailSetting.getMailAddress(),
                    mailSetting.getPassword());

            store = session.getStore(urln);
            store.connect();
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            store.close();
        }
    }

    /**
     * 根据协议，获取邮箱连接session
     *
     * @param protocal 有IMAP、SMTP、POP3
     * @return 邮箱连接session
     */
    private Session getMailSession(String protocal) {
        // Get a Properties object
        Properties props = getProperty(protocal);
        // 如果不要对服务器的ssl证书进行受信任检查，测添加以下语句
        // mailProps.setProperty("mail.smtp.ssl.trust","*");
        Session instance = null;
        if (MailSeting.IMAP_PROTOCAL.equals(protocal)) {
            instance = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailSetting.getMailAddress(), mailSetting.getPassword());
                }
            });
        } else {
            instance = Session.getInstance(props);
        }
        return instance;
    }

    /**
     * 根据传入的协议类型，返回Properties
     *
     * @param protocal 有IMAP、SMTP、POP3
     * @return Properties
     */
    private Properties getProperty(String protocal) {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Properties props = new Properties();
        if (mailSetting.getSSL()) {
            props.setProperty("mail." + protocal + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        props.setProperty("mail." + protocal + ".socketFactory.fallback", "false");

        if (MailSeting.SMTP_PROTOCAL.equals(protocal)) {
            String host = mailSetting.getSendHost();
            props.setProperty("mail.smtp.host", host);
            props.setProperty("mail.smtp.port", mailSetting.getSendPort());
            props.setProperty("mail.smtp.socketFactory.port", mailSetting.getSendPort());
            props.setProperty("mail.smtp.auth", String.valueOf(mailSetting.getValidate()));
            int gmail = host.indexOf("gmail");
            int live = host.indexOf("live");
            if (gmail != -1 || live != -1) {
                props.setProperty("mail.smtp.starttls.enable", "true");
            }
            if (!mailSetting.getSSL()) {
                props.setProperty("mail.smtp.socketFactory.class", "javax.net.SocketFactory");
            }
        } else {
            props.setProperty("mail." + protocal + ".host", mailSetting.getReceiveHost());
            props.setProperty("mail." + protocal + ".port", mailSetting.getReceivePort());
            props.setProperty("mail." + protocal + ".socketFactory.port", mailSetting.getReceivePort());
            if (MailSeting.POP3_PROTOCAL.equals(protocal)) {
                props.setProperty("mail.smtp.starttls.enable", "true");
            } else {
                props.setProperty("mail.store.protocol", MailSeting.IMAP_PROTOCAL);
            }
        }
        return props;
    }

    /**
     * 发送邮件
     *
     * @param mail 邮件信息实体
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @see Mail
     */
    public void send(Mail mail) throws UnsupportedEncodingException, MessagingException {
        Session session = getMailSession(MailSeting.SMTP_PROTOCAL);

        MimeMessage message = new MimeMessage(session);
        addAddressInfo(mail, message);

        BodyPart contentPart = new MimeBodyPart();// 内容
        Multipart multipart = new MimeMultipart();
        contentPart.setHeader("Content-Transfer-Encoding", "base64");

        // 邮件正文(第二部分邮件的内容及附件)
        contentPart.setContent(mail.getContent(), "text/html;charset=utf-8");
        message.setSubject(mail.getSubject(), "utf-8");
        message.setText("utf-8", "utf-8");
        message.setSentDate(new Date());
        multipart.addBodyPart(contentPart);// 邮件正文
        message.setContent(multipart);

        //加正文中的图片附件

        // 添加附件
        for (MailAttachment attachment : mail.getMailAttachments()) {
            String filePath = attachment.getFilePath();
            File attachFile = new File(filePath);
            if(attachFile.exists()) {
                BodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(new File(filePath));
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(MimeUtility.encodeWord(attachment.getFileName(), "UTF-8", "Q"));
                if(null != attachment.getCid() && attachment.getCid().length()>0){
                    messageBodyPart.setHeader("Content-ID",attachment.getCid());
                }
                multipart.addBodyPart(messageBodyPart);
            }
        }
        message.setContent(multipart);
        message.saveChanges();

        Transport transport = session.getTransport(MailSeting.SMTP_PROTOCAL);
        transport.connect(mailSetting.getSendHost(), mailSetting.getMailAddress(), mailSetting.getPassword());
        transport.sendMessage(message, message.getAllRecipients());
    }

    /**
     * 添加发件人、收件人、抄送人、暗送人地址信息
     *
     * @param mail
     * @param message
     * @throws UnsupportedEncodingException
     * @throws Exception
     * @throws MessagingException
     * @see Message
     * @see Mail
     */
    private void addAddressInfo(Mail mail, Message message) throws UnsupportedEncodingException, MessagingException {
        // 添加发件人
        InternetAddress senderAddress = toInternetAddress(mailSetting.getNickName(), mail.getSenderAddress());

        message.setFrom(senderAddress);
        // 收件人列表
        addAddressInfo(message, mail.getReceiverAddresses(), Message.RecipientType.TO);
        // 抄送人列表
        addAddressInfo(message, mail.getCopyToAddresses(), Message.RecipientType.CC);
        // 暗送人列表
        addAddressInfo(message, mail.getBcCAddresses(), Message.RecipientType.BCC);
    }

    /**
     * 根据传入的带,号的address，添加地址信息
     *
     * @param message
     * @param address
     * @param recipientType
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    private void addAddressInfo(Message message, String address, Message.RecipientType recipientType) throws UnsupportedEncodingException, MessagingException {
        MailAddress mailAddress = new MailAddress();
        Set<MailAddress> addresseSet = new HashSet<MailAddress>();
        if (address != null && !"".equals(address.trim())) {
            String[] addressArr = address.split(",");
            for (String addr : addressArr) {
                if (addr == null || "".equals(addr.trim())) continue;
                String[] addAndName = addr.trim().split(":");
                mailAddress = new MailAddress();
                mailAddress.setAddress(addAndName.length>1? addAndName[1]: addAndName[0]);
                mailAddress.setName(addAndName[0]);
                addresseSet.add(mailAddress);
            }
        }
        InternetAddress addressArr[] = toInternetAddress(addresseSet);
        if (addressArr != null)
            message.addRecipients(recipientType, addressArr);
    }

    /**
     * 将地地址址转化为 可输送的网络地址
     *
     * @param name    显示名称
     * @param address 邮件地址
     * @throws UnsupportedEncodingException
     * @throws AddressException
     */
    private InternetAddress toInternetAddress(String name, String address) throws UnsupportedEncodingException, AddressException {
        if (name != null && !name.trim().equals("")) {
            return new InternetAddress(address, MimeUtility.encodeWord(name, "utf-8", "Q"));
        }
        return new InternetAddress(address);
    }

    /**
     * 将地地址址转化为 可输送的网络地址
     * @param set
     * @return
     * @throws UnsupportedEncodingException
     * @throws AddressException
     */
    private InternetAddress[] toInternetAddress(Set<MailAddress> set) throws UnsupportedEncodingException, AddressException {
        if (set == null || set.size() < 1)  return null;
        InternetAddress address[] = new InternetAddress[set.size()];
        Iterator<MailAddress> it = set.iterator();
        int i = 0;
        while (it.hasNext()) {
            address[i++] = toInternetAddress(it.next());
        }
        return address;
    }

    /**
     * 将地地址址转化为 可输送的网络地址
     *
     * @param emailAddress MailAddress实体对象
     * @throws UnsupportedEncodingException
     * @throws AddressException
     * @see MailAddress
     */
    private InternetAddress toInternetAddress(MailAddress emailAddress) throws UnsupportedEncodingException, AddressException {
        return toInternetAddress(emailAddress.getName(), emailAddress.getAddress());
    }

    /**
     * 接收全部邮件头内容
     * @param handler
     * @param lastMessageId
     * @throws MessagingException
     */
    public void receive(AttacheHandler handler, String lastMessageId) throws MessagingException {
        receive(handler, lastMessageId,false);
    }

    /**
     * 接收邮件头,支持仅接收新邮件，但需要邮件服务器支持
     * @param handler
     * @param lastMessageId
     * @param getNew
     * @throws MessagingException
     */
    public void receive(AttacheHandler handler, String lastMessageId, Boolean getNew) throws MessagingException {
        this.handler = handler;
        Integer startNumber = 1;
        Store store = getConnectedStore();
        Folder folder = getFolder(store);

        try {
            this.mailTotal = folder.getMessageCount();
            this.mailTotalNew = 0;
            //检查接收起点
            int i = mailTotal;
            if( null != lastMessageId && StringUtils.isNotBlank(lastMessageId)) {
                while (i > 0) {
                    if (!folder.isOpen()) folder = getFolder(folder.getStore());
                    MimeMessage msg = (MimeMessage) folder.getMessage( i);
                    if (msg.getMessageID().equals(lastMessageId)){
                        startNumber = i + 1;
                        break;
                    }
                    i--;
                }
            }

            //收邮件
            i = startNumber;
            while (i <= mailTotal) {
                if (!folder.isOpen())  folder = getFolder(folder.getStore());
                MimeMessage msg = (MimeMessage) folder.getMessage(i);
                String messageId = msg.getMessageID();
                i++;

                //跳过已接收的邮件
                if(msg.getMessageNumber() < startNumber) continue;
                //检查是否已经下载
                if(handler.checkExist(messageId)) continue;

                //获取邮件内容
                Mail mail = getMail(msg);
                handler.saveContent(mail, messageId);

                //新接收邮件数
                this.mailTotalNew ++ ;

                // 设置已删除状态为true
                if (mailSetting.getIsDeleteRemote()) { msg.setFlag(Flags.Flag.DELETED, true); }
            }

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close(folder, store);
        }
    }

    /**
     * 根据已接收的最后一个 message id 获取邮件排位数量
     * @param lastMessageId
     * @return
     * @throws MessagingException
     */
    public Integer getMessageNumberForLastMessageId(String lastMessageId) throws MessagingException {
        Integer result =1;
        Store store = getConnectedStore();
        Folder folder = getFolder(store);
        this.mailTotal = folder.getMessageCount();

        int i = mailTotal;
        while (true && i > 0) {
            if (!folder.isOpen())  folder = getFolder(folder.getStore());
            MimeMessage msg = (MimeMessage) folder.getMessage(mailTotal-i);
            String messageId = msg.getMessageID();
            if(messageId == lastMessageId){
                result = msg.getMessageNumber();
                break;
            }
        }

        return result;
    }

    /**
     * 判断是否为新邮件,注不是所有邮箱服务器都支持
     * @param mimeMessage
     * @return
     * @throws MessagingException
     */
    public boolean isNew(Message mimeMessage) throws MessagingException {
        boolean isnew = false;
        Flags flags = mimeMessage.getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == Flags.Flag.SEEN) {
                isnew = true;
                break;
            }
        }
        return isnew;
    }

    /**
     * 根据MimeMessage获得Mail实体
     *
     * @param message
     * @return
     * @throws Exception
     * @see MimeMessage
     */
    private Mail getMail(MimeMessage message) throws Exception {
        Mail mail = new Mail();
        mail.setUID(message.getMessageID());
        Date sentDate = null;
        if (message.getSentDate() != null) {
            sentDate = message.getSentDate();
        } else {
            sentDate = new Date();
        }

        // 邮件发送时间
        mail.setSendDate(sentDate);
        mail.setSubject(MimeUtility.decodeText(message.getSubject()));
        // 取得邮件内容
        StringBuffer bodytext = new StringBuffer();
        getMailContent(message, bodytext, mail);
        mail.setContent(bodytext.toString());
        // 发件人
        MailAddress temp = getFrom(message);
        mail.setSenderAddress(temp.getAddress());
        mail.setSenderName(temp.getName());
        // 接受者
        temp = getMailAddress(Message.RecipientType.TO, message);
        mail.setReceiverAddresses(temp.getAddress());
        mail.setReceiverName(temp.getName());
        // 暗送者
        temp = getMailAddress(Message.RecipientType.BCC, message);
        mail.setBcCAddresses(temp.getAddress());
        // 抄送者
        temp = getMailAddress(Message.RecipientType.CC, message);
        mail.setCopyToAddresses(temp.getAddress());
        return mail;
    }

    /**
     * 获得发件人的地址和姓名
     *
     * @see MimeMessage
     */
    private MailAddress getFrom(MimeMessage mimeMessage) throws Exception {
        MailAddress mailAddress = new MailAddress();
        try {
            InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
            if (address == null || address.length == 0) return mailAddress;

            mailAddress.setAddress(address[0].getPersonal() +":"+address[0].getAddress());
            mailAddress.setName(address[0].getPersonal());
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            return mailAddress;
        }
    }

    /**
     * 根据RecipientType类型，获得邮件相应的收件人信息：邮箱地址,邮箱名称
     *
     * @param recipientType
     * @param mimeMessage
     * @return
     * @throws Exception
     * @see Message.RecipientType
     * @see MimeMessage
     */
    private MailAddress getMailAddress(Message.RecipientType recipientType, MimeMessage mimeMessage) throws Exception {

        MailAddress mailAddress = new MailAddress();
        try {
            InternetAddress[] address = (InternetAddress[]) mimeMessage.getRecipients(recipientType);
            if (address == null) return mailAddress;
            StringBuffer addresses = new StringBuffer("");
            StringBuffer name = new StringBuffer("");
            for (int i = 0; i < address.length; i++) {
                String email = address[i].getAddress();
                if (email == null) continue;
                String personal = address[i].getPersonal();
                if (personal == null) personal = email;
                switch (i) {
                    case 0:
                        if(MimeUtility.decodeText(personal).equals(MimeUtility.decodeText(email))){
                            addresses.append(MimeUtility.decodeText(email));
                        }else {
                            addresses.append(MimeUtility.decodeText(personal));
                            addresses.append(":").append(MimeUtility.decodeText(email));
                        }
                        name.append(MimeUtility.decodeText(personal));
                        break;
                    default:
                        if(MimeUtility.decodeText(personal).equals(MimeUtility.decodeText(email))){
                            addresses.append(",").append(MimeUtility.decodeText(email));
                        }else {
                            addresses.append(":").append(MimeUtility.decodeText(personal));
                            addresses.append(",").append(MimeUtility.decodeText(email));
                        }
                        name.append(",").append(MimeUtility.decodeText(personal));
                }
            }
            mailAddress.setAddress(addresses.toString());
            mailAddress.setName(name.toString());
        } catch (Exception e) {
			e.printStackTrace();
        } finally {
            return mailAddress;
        }
    }

    /**
     * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中， 解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
     *
     * @param message
     * @param bodyText
     * @param mail
     * @see Part
     * @see Mail
     */
    private void getMailContent(Part message, StringBuffer bodyText, Mail mail) throws Exception {
        String contentType = message.getContentType();
        int nameindex = contentType.indexOf("name");
        boolean conname = false;

        try {

            if (nameindex != -1) {
                conname = true;
            }
            if ((message.isMimeType("text/plain") || message.isMimeType("text/html")) && !conname) {
                bodyText.append((String) message.getContent());
            } else if (message.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) message.getContent();

                int count = multipart.getCount();
                Map<String, Part> partMap = new LinkedHashMap<String, Part>();

                boolean blnTxt = false;
                boolean blnHtml = false;
                for (int i = 0; i < count; i++) {
                    Part tmpPart = multipart.getBodyPart(i);
                    String partType = tmpPart.getContentType();
                    if (tmpPart.isMimeType("text/plain")) {
                        partMap.put("text/plain", tmpPart);
                        blnTxt = true;
                    } else if (tmpPart.isMimeType("text/html")) {
                        partMap.put("text/html", tmpPart);
                        blnHtml = true;
                    } else {
                        partMap.put(partType, tmpPart);
                    }
                }
                if (blnTxt && blnHtml) {
                    partMap.remove("text/plain");
                }
                Set<Map.Entry<String, Part>> set = partMap.entrySet();
                for (Iterator<Map.Entry<String, Part>> it = set.iterator(); it.hasNext(); ) {
                    getMailContent(it.next().getValue(), bodyText, mail);
                }

            } else if (message.isMimeType("message/rfc822")) {
                    getMailContent((Part) message.getContent(), bodyText, mail);

            } else if (message.isMimeType("application/octet-stream")
                    || message.isMimeType("image/*")
                    || message.isMimeType("application/*")) {
                String a =message.getContentType();
                Object b = message.getContent();

                if (mailSetting.getIsHandleAttach()) {
                    handler.saveAll(message, mail );
                } else {
                    // 不处理附件下载，则只记录下附件的文件名
                    String filename = MimeUtility.decodeText(message.getFileName());
                    mail.getMailAttachments().add(new MailAttachment(filename, ""));
                }
            }
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 创建远程邮箱链接
     * @param store
     * @return
     * @throws MessagingException
     */
    private Folder getFolder(Store store) throws MessagingException {
        if (!store.isConnected()) store = getConnectedStore();

        Folder folder = store.getFolder("INBOX");
        if (mailSetting.getIsDeleteRemote()) {// 需要删除远程邮件，则以读写方式打开
            folder.open(Folder.READ_WRITE);
        } else {
            folder.open(Folder.READ_ONLY);
        }
        return folder;
    }

    /**
     * 关闭邮箱连接，关闭时，根据MailSeting中设置的isDeleteRemote，决定是否删除远程邮件
     *
     * @param folder java.mail.Folder
     * @param store  javax.mail.Store
     * @throws UnsupportedEncodingException
     * @see Folder
     * @see Store
     */
    private void close(Folder folder, Store store) {
        try {
            if (folder != null && folder.isOpen()) {
                // 是否删除远程邮件
                folder.close(mailSetting.getIsDeleteRemote());
            }
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            folder = null;
            store = null;
        }
    }

    /**
     * 获取链接库
     * @return
     * @throws MessagingException
     */
    private Store getConnectedStore() throws MessagingException {

        Session session = getMailSession(mailSetting.getProtocal());
        URLName urln = new URLName(
                mailSetting.getProtocal(),
                mailSetting.getReceiveHost(),
                Integer.parseInt(mailSetting.getReceivePort()),
                null,
                mailSetting.getMailAddress(),
                mailSetting.getPassword()
        );
        // 创建连接
        Store store = session.getStore(urln);
        try {
            store.connect(mailSetting.getMailAddress(), mailSetting.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return store;
    }

    /**
     * 获得邮件的优先级
     * @param msg 邮件内容
     * @return 1(High):紧急  3:普通(Normal)  5:低(Low)
     * @throws MessagingException
     */
    public static int getPriority(MimeMessage msg) throws MessagingException {
        int priority = 3;
        String[] headers = msg.getHeader("X-Priority");
        if (headers != null) {
            String headerPriority = headers[0];
            if (headerPriority.indexOf("1") != -1 || headerPriority.indexOf("High") != -1)
                priority = 1;
            else if (headerPriority.indexOf("5") != -1 || headerPriority.indexOf("Low") != -1)
                priority = 2;
            else
                priority = 3;
        }
        return priority;
    }
}
