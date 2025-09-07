package dao;

public class ServiceType {
	int index;
	String serviceType;
	
	public ServiceType(int index, String serviceType) {
		super();
		this.index = index;
		this.serviceType = serviceType;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	

}
