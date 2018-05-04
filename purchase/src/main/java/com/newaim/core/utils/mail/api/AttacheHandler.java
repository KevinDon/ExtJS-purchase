package com.newaim.core.utils.mail.api;

import com.newaim.core.utils.mail.model.Mail;

import javax.mail.Part;

public interface AttacheHandler {

    /**
     * 邮件附件处理接口方法
     *
     * @param part
     * @param mail 邮件对象
     *             <p>示例：</p>
     *             <pre>
     *             public void handle(Part part, Mail mail){
     *             	//获得文件名
     *             String filename=MimeUtility.decodeText(part.getFileName());
     *             String basePath = System.getProperty("user.dir")+File.separator;
     *             String filePath = basePath + filename;
     *             System.out.println(filePath);
     *             File file = new File(basePath);
     *             if (!file.exists()) file.mkdirs();
     *             //将附件流保存到本地
     *             InputStream is = part.getInputStream() ;
     *             FileOutputStream fos = new FileOutputStream(filePath);
     *             byte[] bs = new byte[512];
     *             int n = 0;
     *             while ((n = is.read(bs)) != -1) {
     *             fos.write(bs, 0, n);
     *             }
     *             is.close();
     *             fos.close();
     *             //将附件的文件名及存放路径存入Mail对象
     *             mail.getMailAttachments().add(new MailAttachment(filename, filePath));
     *             }
     *             </pre>
     */
    public void saveAll(Part part, Mail mail);
    public Boolean saveContent(Mail mail, String messageId);
    public Boolean checkExist(String messageId);
}
