package services.registration;

import enums.Gender;

public class RegisterUser {
	private String username;
	private String email;
	private String name;
	private String surname;
	private Gender gender;
	private String password;
	public RegisterUser(String username, String email, String name, String surname, Gender gender, String password) {
		super();
		this.username = username;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.gender = gender;
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
