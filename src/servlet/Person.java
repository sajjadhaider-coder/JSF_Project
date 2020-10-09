package servlet;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name="user")
@RequestScoped
public class Person {
	
	private int uId;
	private String uName;
	private String uProfession;
	private int uSalary;
	
	public Person(int uId, String uName, String uProfession, int uSalary) {
		super();
		this.uId = uId;
		this.uName = uName;
		this.uProfession = uProfession;
		this.uSalary = uSalary;
	}
	public Person() {
		
	}
	public int getuId() {
		return uId;
	}
	public void setuId(int uId) {
		this.uId = uId;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public String getuProfession() {
		return uProfession;
	}
	public void setuProfession(String uProfession) {
		this.uProfession = uProfession;
	}
	public int getuSalary() {
		return uSalary;
	}
	public void setuSalary(int uSalary) {
		this.uSalary = uSalary;
	}
}
