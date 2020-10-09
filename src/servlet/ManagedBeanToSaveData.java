package servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "saveData")
@SessionScoped
public class ManagedBeanToSaveData {

	private String userName;
	String response = "";
	private List<Person> person;

	private String profession;
	private int salary;
	private int userId;
	private String userDetails;
	private int userSearchId;
	Connection con;
	private int userDeleteId;
	Person selectedEmployee;

	public Person getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(Person selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public List<Person> getPerson() {
		return person;
	}

	public void setPerson(List<Person> person) {
		this.person = person;
	}

	public int getUserDeleteId() {
		return userDeleteId;
	}

	public void setUserDeleteId(int userDeleteId) {
		this.userDeleteId = userDeleteId;
	}

	public int getUserSearchId() {
		return userSearchId;
	}

	public void setUserSearchId(int userSearchId) {
		this.userSearchId = userSearchId;
	}

	public String getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(String userDetails) {
		this.userDetails = userDetails;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	@PostConstruct
	public void init() {
		selectedEmployee = new Person();
	}

	public String saveUser() {

		System.out.println(userName + "-" + profession + "-" + salary);
		int result = 0;
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage fm = null;

		userName = selectedEmployee.getuName();
		salary = selectedEmployee.getuSalary();
		profession = selectedEmployee.getuProfession();

		// saving user data
		try {
			if (userName != null && profession != null && salary > 0) {
				ManagedBeanToSaveData o = new ManagedBeanToSaveData();
				Connection con = o.connection();
				PreparedStatement stmt = con
						.prepareStatement("INSERT INTO employee (Salary, UserName, Profession) VALUES(?, ?, ?)");
				stmt.setInt(1, salary);
				stmt.setString(2, userName);
				stmt.setString(3, profession);
				if (salary > 0 || userName != null || profession != null) {
					result = stmt.executeUpdate();
				}

				System.out.println("user saved successfully");
				con.close();
				stmt.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		if (result > 0 && userName != null && salary > 0 && profession != null) {
			fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "user saved successfully", "");
		} else {
			fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "couldn't save the user", "");
		}
		
		// reseting inputText in AddandEdit.xhtml page
		selectedEmployee = new Person();
		
		context.addMessage(null, fm);
		return response;
	}

	public List<Person> findUser() throws SQLException {
		Person p = null;
		List<Person> list = new ArrayList<Person>();

		ManagedBeanToSaveData mbsd = new ManagedBeanToSaveData();
		Connection con = mbsd.connection();
		Statement stmt = con.createStatement();

		try {

			ResultSet rs = null;
			if (userSearchId > 0) {

				rs = stmt.executeQuery("select * from employee where UserID =" + userSearchId);
				while (rs.next()) {
					p = new Person();
					p.setuId(rs.getInt(1));
					p.setuSalary(rs.getInt(2));
					p.setuName(rs.getString(3));
					p.setuProfession(rs.getString(4));
					list.add(p);
				}
				con.close();
			} else {
				con = mbsd.connection();
				stmt = con.createStatement();

				rs = stmt.executeQuery("select * from employee ");
				while (rs.next()) {
					p = new Person();
					p.setuId(rs.getInt(1));
					p.setuSalary(rs.getInt(2));
					p.setuName(rs.getString(3));
					p.setuProfession(rs.getString(4));
					list.add(p);
				}

				userId = p.getuId();
				userName = p.getuName();
				profession = p.getuProfession();
				salary = p.getuSalary();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		con.close();
		return list;
	}

	public void populateInputTextForUpdate(Person p) {

		selectedEmployee = new Person();
		selectedEmployee = p;
	}

	public String updateUser() throws SQLException {

		// Updating user data
		int result = 0;
		ManagedBeanToSaveData o = new ManagedBeanToSaveData();
		Connection con = o.connection();
		
		userName = selectedEmployee.getuName();
		salary = selectedEmployee.getuSalary();
		userId = selectedEmployee.getuId();
		profession = selectedEmployee.getuProfession();

		try {
			if (userName != null || profession != null || salary > 0 && userId > 0) {
				
				PreparedStatement stmt = con.prepareStatement("UPDATE employee SET Salary=" + salary + ", UserName='"
						+ userName + "', Profession='" + profession + "' WHERE UserID=" + userId);

				result = stmt.executeUpdate();
				con.close();

				// reseting inputText in xhtml page
				selectedEmployee = new Person();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		con.close();
		if (result > 0) {
			response = "user updated successfully";
			FacesContext.getCurrentInstance().addMessage(null, new javax.faces.application.FacesMessage(response));
		} else {
			response = "couldn't update the user";
			FacesContext.getCurrentInstance().addMessage(null, new javax.faces.application.FacesMessage(response));
		}
		return null;
	}

	public String deleteUser(Person p) throws SQLException {

		String msg = "";
		int result = 0;
		ManagedBeanToSaveData o = new ManagedBeanToSaveData();
		Connection con = o.connection();
		userDeleteId = p.getuId();
		try {
			if (userDeleteId > 0) {
				PreparedStatement stmt = con.prepareStatement("DELETE FROM employee WHERE UserID=" + userDeleteId);
				msg = "user deleted successfully";
				result = stmt.executeUpdate();
				con.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		FacesMessage message = null;
		if (result > 0) {
			msg = "Record has been deleted.";
			message = new FacesMessage(msg);
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage(null, message);
		} else {
			msg = "could't delete user";
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage(null, message);
		}
		con.close();
		return msg;
	}
	
	public void resetForm() {
		selectedEmployee = new Person();
	}

	public Connection connection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/lmktsajjad?characterEncoding=latin1", "root",
					"root");
		} catch (Exception e) {
			System.out.println(e);
		}
		return con;
	}
}
