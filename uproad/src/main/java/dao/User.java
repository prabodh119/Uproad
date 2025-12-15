package dao;

import java.util.ArrayList;

public class User {
	String username;
	String password;
	String regDate;
	int enabled;
	int admin;
	String telephone;
	String name;
	ArrayList<VehicleDetail> vehicles;
	
	public User(String username, String password, String regDate, int isEnabled, int isAdmin, String name, String telephone, ArrayList<VehicleDetail> vehicles) {
		super();
		this.username = username;
		this.password = password;
		this.regDate = regDate;
		this.enabled = isEnabled;
		this.admin = isAdmin;
		this.name = name;
		this.telephone = telephone;
		this.vehicles = vehicles;
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

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public int getAdmin() {
		return admin;
	}

	public void setAdmin(int admin) {
		this.admin = admin;
	}
	
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public ArrayList<VehicleDetail> getVehicles() {
		return vehicles;
	}

	public void setVehicles(ArrayList<VehicleDetail> vehicles) {
		this.vehicles = vehicles;
	}
	
}
