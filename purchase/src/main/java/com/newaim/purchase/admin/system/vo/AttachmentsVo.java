package com.newaim.purchase.admin.system.vo;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.newaim.purchase.admin.system.entity.Attachment;

public class AttachmentsVo implements Serializable {

	private List<Attachment> attachments = Lists.newArrayList();

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	
}
