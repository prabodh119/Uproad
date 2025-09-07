package dao;

public class Garage2 {
	int index;
	String name;
	String address;
	String contactNo;
	String contactNo2;
	String contactNo3;
	String nearCity;
	String road;
	String website;
	String vehicle_category;
	String services;
	String Field12;
	
	public Garage2(int index, String name, String address, String contactNo, String contactNo2, String contactNo3,
			String nearCity, String road, String website, String vehicle_category, String services, String field12) {
		super();
		this.index = index;
		this.name = name;
		this.address = address;
		this.contactNo = contactNo;
		this.contactNo2 = contactNo2;
		this.contactNo3 = contactNo3;
		this.nearCity = nearCity;
		this.road = road;
		this.website = website;
		this.vehicle_category = vehicle_category;
		this.services = services;
		this.Field12 = field12;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getContactNo2() {
		return contactNo2;
	}

	public void setContactNo2(String contactNo2) {
		this.contactNo2 = contactNo2;
	}

	public String getContactNo3() {
		return contactNo3;
	}

	public void setContactNo3(String contactNo3) {
		this.contactNo3 = contactNo3;
	}

	public String getNearCity() {
		return nearCity;
	}

	public void setNearCity(String nearCity) {
		this.nearCity = nearCity;
	}

	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getVehicle_category() {
		return vehicle_category;
	}

	public void setVehicle_category(String vehicle_category) {
		this.vehicle_category = vehicle_category;
	}

	public String getServices() {
		return services;
	}

	public void setServices(String services) {
		this.services = services;
	}

	public String getField12() {
		return Field12;
	}

	public void setField12(String field12) {
		Field12 = field12;
	}
	
}
