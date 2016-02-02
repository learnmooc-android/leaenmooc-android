package com.upc.learnmooc.domain;

/**
 * 用户返回信息
 * Created by Explorer on 2016/2/1.
 */
public class User {
	public String result;//注册的返回结果 eg:"注册成功"
	public String password;
	public String mail;
	public String nickname;//昵称
	public int id;
	public String avatar;//头像url
	public int roleType;//用户身份 0：学生 1:老师

	@Override
	public String toString() {
		return "User{" +
				"result='" + result + '\'' +
				", password='" + password + '\'' +
				", mail='" + mail + '\'' +
				", nickname='" + nickname + '\'' +
				", id=" + id +
				", avatar='" + avatar + '\'' +
				", roleType=" + roleType +
				'}';
	}

	public String getResult() {
		return result;
	}

	public String getPassword() {
		return password;
	}

	public String getMail() {
		return mail;
	}

	public String getNickname() {
		return nickname;
	}

	public int getId() {
		return id;
	}

	public String getAvatar() {
		return avatar;
	}

	public int getRoleType() {
		return roleType;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}
}
