package com.puppyrush.buzzcloud.controller.form;

import org.springframework.web.multipart.MultipartFile;

public class FileForm {

	private String		attach_path;
	private MultipartFile	file;
	private String		filename;
	private String		CKEditorFuncNum;

	public String getAttach_path() {
		return this.attach_path;
	}

	public void setAttach_path(String attach_path) {
		this.attach_path = attach_path;
	}

	public MultipartFile getUpload() {
		return file;
	}

	public void setUpload(MultipartFile upload) {
		this.file = upload;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getCKEditorFuncNum() {
		return this.CKEditorFuncNum;
	}

	public void setCKEditorFuncNum(String CKEditorFuncNum) { this.CKEditorFuncNum = CKEditorFuncNum; }
}
