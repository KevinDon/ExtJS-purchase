package com.newaim.core.utils.mail.model;

import java.io.Serializable;

public class MailAttachment implements Serializable{

	/**
	 *  文件名
	 */
	protected String fileName;
	/**
	 *  文件路径
	 */
	protected String filePath;

	/**
	 * 文件数据流
	 */
	protected byte[] fileBlob;
	
	private Long fileSize;

    /**
     * 附件使用类型：1、纯附件；2正文中的媒体
     */
    protected int applyType;

    /**
     * 正文中的媒体类型时，必需有Cid对应
     */
    protected String cid;


	/**
	 * 邮件附件类构造方法
	 */
	public MailAttachment(){}
	
	/**
	 * 邮件附件类构造方法
	 * @param fileName	文件名
	 * @param filePath	文件全路径
	 */
	public MailAttachment(String fileName, String filePath) {
		this.fileName = fileName;
		this.filePath = filePath;
	}
	
	/**
	 * 邮件附件类构造方法
	 * @param fileName	文件名
	 * @param filePath	文件全路径
	 */
	public MailAttachment(String fileName, String filePath, Long fileSize) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileSize = fileSize;
	}
	
	/**
	 * 邮件附件类构造方法
	 * @param fileName	文件名
	 * @param fileBlob	文件字节流
	 */
	public MailAttachment(String fileName, byte[] fileBlob) {
		this.fileName = fileName;
		this.fileBlob = fileBlob;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public byte[] getFileBlob() {
		return fileBlob;
	}

	public void setFileBlob(byte[] fileBlob) {
		this.fileBlob = fileBlob;
	}
	
	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

    public int getApplyType() {
        return applyType;
    }

    public void setApplyType(int applyType) {
        this.applyType = applyType;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
