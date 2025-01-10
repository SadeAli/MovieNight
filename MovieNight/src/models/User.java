package models;

public class User {
	private int id;
	private String fname;
	private String lname;
	private String username;
	private String password;
	private String createdAt;

	public User(int id, String fname, String lname, String username, String password, String createdAt)
	{
		this.id = id;
		this.fname = fname;
		this.lname = lname;
		this.username = username;
		this.password = password;
		this.createdAt = createdAt;
	}
	
	public User(String fname, String lname, String username, String password, String createdAt)
	{
		this.id = 0;
		this.fname = fname;
		this.lname = lname;
		this.username = username;
		this.password = password;
		this.createdAt = createdAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
}
