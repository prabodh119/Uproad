package dao;

public class VehicleDetail {
	String username;
	String make;
	String model;
	String year;
	String vehicleCategory;
	
	public VehicleDetail(String username, String make, String model, String year, String vehicleCategory) {
		super();
		this.username = username;
		this.make = make;
		this.model = model;
		this.year = year;
		this.vehicleCategory = vehicleCategory;
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
	
	
}
