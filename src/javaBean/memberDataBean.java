package javaBean;

import java.sql.Timestamp;

//User Table 참조
public class memberDataBean {

	private int id;
	private String nickname;
	private String password;
	private String idType;
	private String email;
	private Timestamp reg_date;
	
	public memberDataBean(){
	}
	
	public memberDataBean(memberDataBean mdb){
		id = mdb.id;
		nickname = mdb.nickname;
		password = mdb.password;
		idType = mdb.idType;
		reg_date = mdb.reg_date;
	}
	
	public memberDataBean(int id, String email, String name, String pw, String idType, Timestamp reg_date){
		this.id = id;
		this.email = email;
		this.nickname = name;
		this.password = pw;
		this.idType = idType;
		this.reg_date = reg_date;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String name) {
		this.nickname = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getReg_date() {
		return reg_date;
	}
	public void setReg_date(Timestamp reg_date) {
		this.reg_date = reg_date;
	}
	
	
	
	
}
