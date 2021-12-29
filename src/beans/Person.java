package beans;

import java.util.Date;

public class Person {
	private String name;
	private String surname;
	private Date dateOfBirth;
	private Gender gender;
	private String username;
	private String password;
	private String email;
	
	public Person(String name, String surname, Date dateOfBirth, Gender gender, String username, String password,
			String email) {
		this.name = name;
		this.surname = surname;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.username = username;
		this.password = password;
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
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
