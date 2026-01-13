package dao;

public class VehicleDetail {
	int id;
	String username;
	String make;
	String model;
	String year;
	String vehicleCategory;
	String nickname;
	
	public VehicleDetail(int id, String username, String make, String model, String year, String vehicleCategory, String nickname) {
		super();
		this.id = id;
		this.username = username;
		this.make = make;
		this.model = model;
		this.year = year;
		this.vehicleCategory = vehicleCategory;
		this.nickname = nickname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getVehicleCategory() {
		return vehicleCategory;
	}

	public void setVehicleCategory(String vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
