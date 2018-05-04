package com.newaim.purchase.desktop.email.service;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.FileUtil;
import com.newaim.core.utils.MD5Util;
import com.newaim.core.utils.SessionUtils;
import com.newaim.core.utils.mail.EmailUtils;
import com.newaim.core.utils.mail.HtmlUtils;
import com.newaim.core.utils.mail.api.AttacheHandler;
import com.newaim.core.utils.mail.api.HtmlHandler;
import com.newaim.core.utils.mail.model.Mail;
import com.newaim.core.utils.mail.model.MailAttachment;
import com.newaim.core.utils.mail.model.MailSeting;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.entity.MyDocument;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.admin.system.service.MyDocumentCategoryService;
import com.newaim.purchase.admin.system.service.MyDocumentService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.desktop.email.dao.EmailDao;
import com.newaim.purchase.desktop.email.entity.Email;
import com.newaim.purchase.desktop.email.entity.EmailBox;
import com.newaim.purchase.desktop.email.entity.EmailSetting;
import com.newaim.purchase.desktop.email.vo.EmailVo;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletRequest;
import java.util.*;

@Service
@Transactional(readOnly = true)

public class EmailService extends ServiceBase {

    private static final String FILE_MODULE_NAME = ConstantsAttachment.Status.Email.code;

    @Autowired
    private EmailDao emailDao;

    @Autowired
    private EmailSettingService emailSettingService;

    @Autowired
    private EmailBoxService emailboxService;

    @Autowired
    private MyDocumentService myDocumentService;

    @Autowired
    private MyDocumentCategoryService myDocumentCategoryService;

    @Autowired
    private AttachmentService attachmentService;

    private ServletRequest request = null;

    public ServletRequest getRequest() {
        return request;
    }

    public void setRequest(ServletRequest request) {
        this.request = request;
    }

    public Page<EmailVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort) {

        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<Email> spec = buildSpecification(params);
        Page<Email> p = emailDao.findAll(spec, pageRequest);
        Page<EmailVo> page = p.map(new Converter<Email, EmailVo>() {
            @Override
            public EmailVo convert(Email dv) {
                return BeanMapper.map(dv, EmailVo.class);
            }
        });

        return page;
    }

    public EmailVo get(String id) {
        Email row = emailDao.findEmailById(id);
        EmailVo o = BeanMapper.map(row, EmailVo.class);

        return o;
    }

    public EmailVo get(String id, String[] params) {
        EmailVo o = get(id);

        //附件文件
        if (ArrayUtils.contains(params, "attachment")) {
            o.setAttachments(attachmentService.listByBusinessIdAndModuleName(id, FILE_MODULE_NAME));
        }

        return o;
    }

    public Email getEmail(String id) {
        Email o = emailDao.findEmailById(id);
        return o;
    }

    /**
     * 新建邮件
     *
     * @param o
     * @param attachments
     * @return
     */
    @Transactional
    public Email add(Email o, List<Attachment> attachments) {

        UserVo user = SessionUtils.currentUserVo();
        o.setCreatorId(user.getId());
        o.setCreatorCnName(user.getCnName());
        o.setCreatorEnName(user.getEnName());
        o.setDepartmentId(user.getDepartmentId());
        o.setDepartmentCnName(user.getDepartmentCnName());
        o.setDepartmentEnName(user.getDepartmentEnName());

        o.setId(null);
        o.setStatus(1);
        if (null == o.getBoxType()) {
            o.setBoxType(0);
        }
        o.setIsHtml(1);
        o.setCreatedAt(new Date());

        if(o.getSendEmail() == null || StringUtils.isEmpty(o.getSendEmail())) {
            EmailSetting myEmailSet = emailSettingService.getEmailSetting(o.getEmailSettingId());
            o.setSendEmail(myEmailSet.getEmail());
            o.setSendName(myEmailSet.getSendName());
        }

        o = emailDao.save(o);

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, o.getId());

        return o;
    }

    @Transactional
    public Email saveAs(Email o, List<Attachment> attachments) {
        emailDao.clear();
        o.setStatus(1);
        o.setBoxType(0);

        return add(o, attachments);
    }

    public void clear() {
        emailDao.clear();
    }

    /**
     * 保存邮件内容
     *
     * @param o
     * @param attachments
     * @return
     */
    @Transactional
    public Email save(Email o, List<Attachment> attachments) {

        EmailSetting myEmailSet = emailSettingService.getEmailSetting(o.getEmailSettingId());
        o.setSendEmail(myEmailSet.getEmail());
        o.setSendName(myEmailSet.getSendName());

        o.setUpdatedAt(new Date());
        o = emailDao.save(o);

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, o.getId());

        return o;
    }

    @Transactional
    public void moveToDustbuin(String ids){
        Email o = getEmail(ids);
        o.setBoxType(3);
        emailDao.save(o);

        //重新统计mail box 的数量及Read数量

    }

    @Transactional
    public void reSetNumberAndRead(String emailSettingId, Integer boxType){
        emailDao.reSetNumberAndRead(emailSettingId, boxType);
    }

    @Transactional
    public void delete(String id) {
        //删除全部关联附件
        attachmentService.deleteByBusinessId(id);
        emailDao.delete(id);
    }

    @Transactional
    public void deleteByEmailSettingId(String emailSettingId) {
        //@TODO 需要根据账号删除全部邮件、附件及邮箱设置
        emailDao.deleteByEmailSettingId(emailSettingId);
    }

    @Transactional
    public void deleteByBoxType(String boxType) {
        //@TODO 需要根据邮箱ID删除全部邮件、附件
        emailDao.deleteByBoxType(boxType);
    }

    @Transactional
    public void setDelete(String id) {
        Email o = getEmail(id);
        o.setStatus(3);
        emailDao.save(o);
    }

    @Transactional
    public void setRead(String id){
        Email o= getEmail(id);
        o.setIsRead(1);
        emailDao.save(o);
    }

    public Integer countEmailNew(){
        Integer result =0;
        UserVo user = SessionUtils.currentUserVo();
        List<EmailSetting> emailSettinies = emailSettingService.listEmailSettingsByCreatorId(user);
        if(emailSettinies.size()>0){
            for (EmailSetting emailSet: emailSettinies){
                result += emailDao.countByEmailSettingIdEqualsAndBoxTypeAndIsRead(emailSet.getId(), 1, 2);
            }
        }

        return result;
    }

    //===========================================================

    /**
     * 发送邮件及保存
     *
     * @param o
     * @param attachments
     * @return
     */
    @Transactional
    public Boolean send(Email o, List<Attachment> attachments) {
        Boolean result =false;
        EmailSetting myEmailSet = emailSettingService.getEmailSetting(o.getEmailSettingId());
        Date sendTime = new Date();
        Integer attachmentCount = 0;
        String oldContent = "";

        try {

            //组合附件
            List<MailAttachment> attcList = Lists.newArrayList();
            if (!attachments.isEmpty()) {
                for (int i = 0; i < attachments.size(); i++) {
                    MailAttachment ma = new MailAttachment();
                    attachments.get(i).setDocument(myDocumentService.getMyDocument(attachments.get(i).getDocumentId()));
                    ma.setFileName(attachments.get(i).getDocument().getName());
                    String filePath = this.getRequest().getRealPath("/"+attachments.get(i).getDocument().getPath());
                    ma.setFilePath(filePath);
                    ma.setFileSize(attachments.get(i).getDocument().getBytes());
                    attcList.add(ma);
                    attachmentCount++;
                }
            }

            //替换正文图片
            oldContent = o.getContent();
            o.setContent(
                    HtmlUtils.replaceHtmlTag(new HtmlHandler() {
                    @Override
                    public String getPartContent(String part) {
                        String docPath = part.replace(getRequest().getServletContext().getContextPath() + "/","").replace("//","");
                        MyDocument myDoc = myDocumentService.getMyDocumentByPath(docPath);

                        if(null != myDoc && !myDoc.getId().isEmpty()) {
                            MailAttachment ma = new MailAttachment();
                            String cid = MD5Util.md5(myDoc.getId());
                            ma.setFileName(myDoc.getName());
                            String filePath = getRequest().getRealPath("/" + myDoc.getPath());
                            ma.setFilePath(filePath);
                            ma.setFileSize(myDoc.getBytes());
                            ma.setCid(cid);
                            attcList.add(ma);
                            return "cid:" + cid;
                        }

                        return null;
                    }
                },o.getContent(), "img", "src", "src=\"", "\"")
            );

            //保存邮件更改
            o.setSendName(myEmailSet.getSendName());
            o.setSendEmail(myEmailSet.getEmail());
            o.setIsHtml(1);
            o.setAttachmentCount(attachmentCount);
            o.setUpdatedAt(sendTime);
            if(null == o.getId() && StringUtils.isEmpty(o.getId())){
                o = this.saveAs(o, attachments);
            }else{
                o = this.save(o, attachments);
            }
            //保存附件
            o = emailDao.save(o);

            //创建邮件内容
            Mail m = new Mail(o);
            if (attcList.size() > 0) m.setMailAttachments(attcList);

            EmailUtils mu = new EmailUtils(new MailSeting(myEmailSet));
            mu.send(m);

            //发送成功后移到已发邮件箱
            o.setContent(oldContent);
            o.setBoxType(2);
            o.setSendTime(sendTime);
            o = emailDao.save(o);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 收新邮件
     * @return
     */
    @Transactional
    public Integer receive(String sid){
        Integer mailTotalNew =0;
        UserVo user = SessionUtils.currentUserVo();

        String myDocCateId = myDocumentCategoryService.getByPath("email").getId();

        List<EmailSetting> mySetting = Lists.newArrayList();
        if(null != sid && StringUtils.isNotEmpty(sid)){
            mySetting.add(emailSettingService.getEmailSetting(sid));
        }else{
            mySetting = emailSettingService.listEmailSettingsByCreatorId(user);
        }

        if (mySetting.size() > 0) {
            for (EmailSetting emailSetting : mySetting) {
                //邮箱账号设置
                final String mailAddress = emailSetting.getEmail();

                //邮箱设置(1收件箱)
                EmailBox emailBox = emailboxService.getEmailBoxByEmailSettingIdAndBoxType(emailSetting.getId(), 1);
                String lastMessageId = emailBox.getLastUid();
                EmailUtils mu = new EmailUtils(new MailSeting(emailSetting, "1"));

                try {
                    mu.receive(new AttacheHandler() {
                        @Override
                        public Boolean checkExist(String messageId) {
                            //根据需要判断是
                            int isExist =emailDao.countByEmailSettingIdEqualsAndBoxTypeAndUid(emailBox.getEmailSettingId(), emailBox.getBoxType(), messageId);
                            if((lastMessageId == null || StringUtils.isBlank(lastMessageId)) && isExist>0) {
                                emailBox.setLastUid(messageId);
                                emailboxService.save(emailBox);
                                logger.debug("补存邮件标记");
                            }
                            return  isExist>0;
                        }
                        @Override
                        public Boolean saveContent(Mail mail, String messageId) {
                            Email email = new Email(mail);
                            //接收邮件中的附件
                            List<MailAttachment> mailAttachments = mail.getMailAttachments();
                            List<Attachment> attachs = Lists.newArrayList();
                            if (mailAttachments != null && mailAttachments.size() > 0) {
                                for (MailAttachment mailAttachment : mailAttachments) {
                                    MyDocument mydoc = getMailAttach(mailAttachment, emailSetting.getShared(), myDocCateId);
                                    if(null != mydoc && StringUtils.isNotEmpty(mydoc.getId())) {
                                        Attachment attac = new Attachment(mydoc);
                                        attac.setModuleName(ConstantsAttachment.Status.Email.code);
                                        attachs.add(attac);
                                    }else{
                                        continue;
                                    }
                                }
                                email.setAttachmentCount(attachs.size());
                            }
                            email.setEmailSettingId(emailBox.getEmailSettingId());
                            email.setBoxType(emailBox.getBoxType());
                            email.setOnlyHead(1);
                            email.setUid(messageId);
                            add(email, attachs);

                            //保存邮箱标记
                            emailBox.setLastUid(mail.getUID());
                            emailboxService.save(emailBox);
                            logger.debug("保存邮件内容");

                            return true;
                        }
                        @Override
                        public void saveAll(Part part, Mail mail) {
                            try {
                                saveAttach(part, mail);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, lastMessageId);

                    mailTotalNew += mu.getMailTotalNew();
                    //重记邮箱的邮件数量
                    this.reSetNumberAndRead(emailSetting.getId(), emailBox.getBoxType());

                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return mailTotalNew;
    }

    /**
     * 获取收到邮件中的附件并且保存
     *
     * @param attachment
     * @param shared
     * @param myDocCateId
     * @return
     */
    private MyDocument getMailAttach(MailAttachment attachment, Integer shared, String myDocCateId) {

        MyDocument mydoc = new MyDocument();
        mydoc.setBytes(attachment.getFileSize());
        mydoc.setExtension(FileUtil.getFileExt(attachment.getFileName()));
        mydoc.setName(attachment.getFileName());
        mydoc.setPath(attachment.getFilePath().substring(1));
        mydoc.setNote(FileUtil.getStrFileSize(attachment.getFileSize()));
        mydoc.setStatus(1);
        mydoc.setShared(shared);
        mydoc.setCategoryId(myDocCateId);
        mydoc.setType(FileUtil.getFileCatByExt(mydoc.getExtension()));
        mydoc.setEmailCid(attachment.getCid());

        mydoc = myDocumentService.add(mydoc);

        return mydoc;
    }

    /**
     * 将邮件中的附件保存在本地指定目录下
     *
     * @return
     */
    private void saveAttach(Part message, Mail mail) throws Exception {
        String filename = MimeUtility.decodeText(message.getFileName());
        Calendar cal = Calendar.getInstance();//使用日历类
        int year = cal.get(Calendar.YEAR);//得到年
        int month = cal.get(Calendar.MONTH) + 1;//得到月，因为从0开始的，所以要加1
        int day = cal.get(Calendar.DAY_OF_MONTH);//得到天
        int hour = cal.get(Calendar.HOUR);//得到小时
        int minute = cal.get(Calendar.MINUTE);//得到分钟
        int second = cal.get(Calendar.SECOND);//得到秒
        UserVo user = SessionUtils.currentUserVo();

        //检查重复性
        String[] cidName = message.getHeader("Content-ID");
        if(null != cidName && cidName.length>0){
            for(MailAttachment mailAttac : mail.getMailAttachments()){
                if (null != mailAttac.getCid() && mailAttac.getCid().equals(cidName[0])){
                    return;
                }
            }
        }

        Random rand = new Random();
        String fileNameNew = "" + year + month + day + hour + minute + second + "_" + (rand.nextInt(1000000) + 1000000);

        String relateFilePath = "/var/upload/" + user.getId() + "/email/" + year + month + "/" + fileNameNew + "." + FileUtil.getFileExt(filename);

        String filePath = this.getRequest().getRealPath(relateFilePath);
        FileUtil.createFolderFile(filePath);
        FileUtil.writeFile(filePath, message.getInputStream());
        Long fileSize = FileUtil.getFileSize(filePath);

        MailAttachment ma = new MailAttachment(filename, relateFilePath, fileSize);
        if(null != cidName && cidName.length>0){
            ma.setCid(cidName[0]);
            ma.setApplyType(2);
        }

        mail.getMailAttachments().add(ma);

    }

    private Specification<Email> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Email> spec = DynamicSpecifications.bySearchFilter(filters.values(), Email.class);
        return spec;
    }

}
