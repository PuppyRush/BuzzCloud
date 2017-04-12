package com.puppyrush.buzzcloud.controller.form;

import java.sql.Timestamp;

public class RequestedJoinForm {

	private int memberId;
	private int bandId;
	
	private boolean isAccept;
	
	public boolean isAccept() {
		return isAccept;
	}
	public void setAccept(boolean isAccept) {
		this.isAccept = isAccept;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public int getBandId() {
		return bandId;
	}
	public void setBandId(int bandId) {
		this.bandId = bandId;
	}

}
