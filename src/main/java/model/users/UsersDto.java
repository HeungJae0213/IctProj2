package model.users;
import java.sql.Date;

public class UsersDto {
	private long id;
	private String username;
	private String password;
	private String name;
	private String gender;
	private String interest;
	private String grade;
	private String intro;
	private String phone;
    private Date regDate;

	//생성자]
	public UsersDto() {}
	public UsersDto(long id, String username, String password, String name, String gender, String interest, String grade,
			String intro, String phone, Date regDate) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.gender = gender;
		this.interest = interest;
		this.grade = grade;
		this.intro = intro;
		this.phone = phone;
		this.regDate = regDate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	
	
	
}
