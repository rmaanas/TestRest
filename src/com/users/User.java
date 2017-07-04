package com.users;

public class User {
	
	public String username;
	public String password;
	public String role;
	public String team;
	public boolean isValid;
	public User(String username,String password)
	{
		this.username = username;
		this.password = password;
		this.role = "";
		this.team = "";
		this.isValid = false;
	}
}
