package dao;

public class VehicleCategory {
	int index;
	String vehicleCategory;
	
	public VehicleCategory(int index, String vehicleCategory) {
		super();
		this.index = index;
		this.vehicleCategory = vehicleCategory;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getVehicleCategory() {
		return vehicleCategory;
	}

	public void setVehicleCategory(String vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}
	
	

}
