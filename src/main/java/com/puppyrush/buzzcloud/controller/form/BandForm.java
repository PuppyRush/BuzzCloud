package com.puppyrush.buzzcloud.controller.form;

import java.util.ArrayList;
import java.util.List;

public class BandForm {

	private String bandName;
	private String bandOwner;
	private String administrator;
	private int bandCapacity;
	private String bandContain;
	private int upperBand;
	private int exUpperBand;
	private List<Integer> members;
	private List<String> bandAuthority;
	private List<String> fileAuthority;
	private List<String> memberAuthority;


	private BandForm(){
		
		bandName = null;
		bandOwner = null;
		administrator = null;
		bandCapacity = -1;
		bandContain = null;
		upperBand = -1;
		members = new ArrayList<Integer>();
		bandAuthority = new ArrayList<String>();
		fileAuthority = new ArrayList<String>();
		memberAuthority = new ArrayList<String>();
	}
	
	public List<String> getMemberAuthority() {
		return memberAuthority;
	}

	public void setMemberAuthority(List<String> memberAuthority) {
		this.memberAuthority = memberAuthority;
	}
	public String getBandName() {
		return bandName;
	}
	public void setBandName(String bandName) {
		this.bandName = bandName;
	}
	public String getBandOwner() {
		return bandOwner;
	}
	public void setBandOwner(String bandOwner) {
		this.bandOwner = bandOwner;
	}
	public String getAdministrator() {
		return administrator;
	}
	public void setAdministrator(String administrator) {
		this.administrator = administrator;
	}
	public int getBandCapacity() {
		return bandCapacity;
	}
	public void setBandCapacity(int bandCapacity) {
		this.bandCapacity = bandCapacity;
	}
	public String getBandContain() {
		return bandContain;
	}
	public void setBandContain(String bandContain) {
		this.bandContain = bandContain;
	}
	public int getUpperBand() {
		return upperBand;
	}
	public void setUpperBand(int upperBand) {
		this.upperBand = upperBand;
	}
	public int getExUpperBand() {
		return exUpperBand;
	}
	public void setExUpperBand(int upperBand) {
		this.exUpperBand = upperBand;
	}
	public List<Integer> getMembers() {
		return members;
	}
	public void setMembers(List<Integer> members) {
		this.members = members;
	}
	public List<String> getBandAuthority() {
		return bandAuthority;
	}
	public void setBandAuthority(List<String> bandAuthority) {
		this.bandAuthority = bandAuthority;
	}
	public List<String> getFileAuthority() {
		return fileAuthority;
	}
	public void setFileAuthority(List<String> fileAuthority) {
		this.fileAuthority = fileAuthority;
	}
	
}
