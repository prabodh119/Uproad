package dbc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import dao.Garage;
import dao.ServiceType;
import dao.User;
import dao.VehicleCategory;
import dao.VehicleDetail;

public class DBConnection {
	
	public Connection connect() {
       String url = "jdbc:sqlite:C:/sqlite/db/restdb.db";
       //String url = "jdbc:sqlite:opt/tomcat/uproad/db/testdb.db";

        try {
        	Class.forName("org.sqlite.JDBC");
        	Connection conn = DriverManager.getConnection(url);
            
            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
        
    }
	
	public User isValidUser(String uName, String pwd) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		
		try {
			con = connect();
			String sql = "SELECT * FROM user WHERE username = ?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, uName);		
			rs = pstmt.executeQuery();
			
			User loggedInUser = null;
			
			if(rs.next()) {
				String hashedPassword = rs.getString("password");
				if (BCrypt.checkpw(pwd, hashedPassword)) {
					
					loggedInUser = new User(rs.getString("username"),
                    		rs.getString("password"),
                    		rs.getString("registered_date"),
                    		rs.getInt("is_enabled"),
                    		rs.getInt("is_admin"),
                    		rs.getString("name"),
                    		getVehicleByUser(uName)
                    		);
                }
				return loggedInUser;
			}
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (pstmt != null) pstmt.close();
            	if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
		}
		return null;
	}
	
	
	public User getUserByUsername(String uName) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = connect();
			String sql = "SELECT * FROM user WHERE username=?";
			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, uName);		
			rs = pstmt.executeQuery();

			User loggedInUser = null;
			
			if (rs.next()) {
				loggedInUser = new User(rs.getString("username"),
                		rs.getString("password"),
                		rs.getString("registered_date"),
                		rs.getInt("is_enabled"),
                		rs.getInt("is_admin"),
                		rs.getString("name"),
                		getVehicleByUser(uName)
                		);	
			}
			return loggedInUser;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (pstmt != null) pstmt.close();
            	if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
		}
		return null;
	}
	
	public int insertUser(String username, String password, String telephone, String name, String make, String model, String year, String vehicleCategory) {
		int result = 0;
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = connect();
			String sql = "SELECT COUNT(*) AS rows FROM user";
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			int rows = 0;
			if(rs.next())
				rows = rs.getInt("rows");
			
			con.setAutoCommit(false);
			
			sql = "INSERT INTO user ([index], username, password, telephone, is_enabled, is_admin, registered_date, name) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, rows+1);
			pstmt.setString(2, username);
			pstmt.setString(3, password);
			pstmt.setString(4, telephone);
			pstmt.setInt(5, 1);
			pstmt.setInt(6, 0);
			pstmt.setString(7, LocalDateTime.now().toString());
			pstmt.setString(8, name);
			
			pstmt.executeUpdate();
			if (pstmt != null) pstmt.close();
			
			sql = "INSERT INTO vehicle_details ([index], username, make, model, year, vehicle_category) VALUES (?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, rows+1);
			pstmt.setString(2, username);
			pstmt.setString(3, make);
			pstmt.setString(4, model);
			pstmt.setString(5, year);
			pstmt.setString(6, vehicleCategory);
			
			pstmt.executeUpdate();
			
			con.commit();
			result = 1;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (stmt != null) stmt.close();
            	if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		
		return result;
	}
	
	public ArrayList<Garage> getGarageByCity(String city) {
		ArrayList<Garage> garageList = new ArrayList<Garage>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = connect();
			String sql = "SELECT * FROM garage WHERE near_city LIKE ?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, city);
			
			rs = pstmt.executeQuery();
			
			Garage garage;
			while(rs.next()) {
				garage = new Garage(
						rs.getInt("index"), 
						rs.getString("name"),
						rs.getString("address"),
						rs.getString("contact_no"),
						rs.getString("contact_2"),
						rs.getString("contact_2"),
						rs.getString("near_city"),
						rs.getString("highway"),
						rs.getString("website"),
						rs.getString("vehicle_category"),
						rs.getString("services"),
						rs.getString("Field12"),
						rs.getString("Field13"),
						rs.getString("Field14"),
						rs.getString("Field15")
						);
				garageList.add(garage);	
			}
			return garageList; 
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (pstmt != null) pstmt.close();
            	if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		
		return null;
	}
	
	public ArrayList<Garage> getGarageByName(String name) {
		ArrayList<Garage> garageList = new ArrayList<Garage>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = connect();
			String sql = "SELECT * FROM garage WHERE name LIKE CONCAT('%', ?,'%')";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, name);
			
			rs = pstmt.executeQuery();
			
			Garage garage;
			while(rs.next()) {
				garage = new Garage(
						rs.getInt("index"), 
						rs.getString("name"),
						rs.getString("address"),
						rs.getString("contact_no"),
						rs.getString("contact_2"),
						rs.getString("contact_2"),
						rs.getString("near_city"),
						rs.getString("highway"),
						rs.getString("website"),
						rs.getString("vehicle_category"),
						rs.getString("services"),
						rs.getString("Field12"),
						rs.getString("Field13"),
						rs.getString("Field14"),
						rs.getString("Field15")
						);
				garageList.add(garage);	
			}
			return garageList; 
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (pstmt != null) pstmt.close();
            	if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		
		return null;
	}
	
	public ArrayList<Garage> getGarageByHighway(String highway) {
		ArrayList<Garage> garageList = new ArrayList<Garage>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = connect();
			String sql = "SELECT * FROM garage WHERE highway LIKE ?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, highway);
			
			rs = pstmt.executeQuery();
			
			Garage garage;
			while(rs.next()) {
				garage = new Garage(
						rs.getInt("index"), 
						rs.getString("name"),
						rs.getString("address"),
						rs.getString("contact_no"),
						rs.getString("contact_2"),
						rs.getString("contact_2"),
						rs.getString("near_city"),
						rs.getString("highway"),
						rs.getString("website"),
						rs.getString("vehicle_category"),
						rs.getString("services"),
						rs.getString("Field12"),
						rs.getString("Field13"),
						rs.getString("Field14"),
						rs.getString("Field15")
						);
				garageList.add(garage);	
			}
			return garageList; 
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (pstmt != null) pstmt.close();
            	if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		
		return null;
	}
	
	public ArrayList<Garage> getAllGarage() {
		ArrayList<Garage> garageList = new ArrayList<Garage>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = connect();
			String sql = "SELECT * FROM garage";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			Garage garage;
			while(rs.next()) {
				garage = new Garage(
						rs.getInt("index"), 
						rs.getString("name"),
						rs.getString("address"),
						rs.getString("contact_no"),
						rs.getString("contact_2"),
						rs.getString("contact_3"),
						rs.getString("near_city"),
						rs.getString("highway"),
						rs.getString("website"),
						rs.getString("vehicle_category"),
						rs.getString("services"),
						rs.getString("Field12"),
						rs.getString("Field13"),
						rs.getString("Field14"),
						rs.getString("Field15")
						);
				garageList.add(garage);	
			}
			return garageList; 
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (stmt != null) stmt.close();
            	if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		
		return null;
	}
	
	public List<Garage> getGaragebyServiceAndcity(String username, String vehicleCategory, String services, String city) {
		ArrayList<Garage> garageList = new ArrayList<Garage>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = connect();
			String sql = "SELECT * FROM garage WHERE vehicle_category LIKE CONCAT('%', ?,'%') AND services LIKE CONCAT('%', ?,'%') AND near_city LIKE ?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, vehicleCategory);
			pstmt.setString(2, services); 
			pstmt.setString(3, city);
			 
			rs = pstmt.executeQuery();
			
			Garage garage;
			while(rs.next()) {
				garage = new Garage(
						rs.getInt("index"), 
						rs.getString("name"),
						rs.getString("address"),
						rs.getString("contact_no"),
						rs.getString("contact_2"),
						rs.getString("contact_2"),
						rs.getString("near_city"),
						rs.getString("highway"),
						rs.getString("website"),
						rs.getString("vehicle_category"),
						rs.getString("services"),
						rs.getString("Field12"),
						rs.getString("Field13"),
						rs.getString("Field14"),
						rs.getString("Field15")
						);
				garageList.add(garage);	
			}
			return garageList; 
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (pstmt != null) pstmt.close();
            	if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		
		return null;
	}
	
	public List<Garage> getGaragebyServiceAndcity(String username, String vehicleCategory, String[] services, String city) {
		ArrayList<Garage> garageList = new ArrayList<Garage>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = connect();
			
			StringBuilder sql = new StringBuilder("SELECT * FROM garage WHERE vehicle_category LIKE CONCAT('%', ?, '%') AND near_city LIKE ?");
			if (services != null && services.length > 0) {
			    sql.append(" AND (");
			    for (int i = 0; i < services.length; i++) {
			        sql.append("services LIKE CONCAT('%', ?, '%')");
			        if (i < services.length - 1) {
			            sql.append(" OR ");
			        }
			    }
			    sql.append(")");
			}
			
			pstmt = con.prepareStatement(sql.toString());
			
			int index = 1;
			pstmt.setString(index++, vehicleCategory);
			pstmt.setString(index++, city);
			for (String serviceType : services) {
				pstmt.setString(index++, serviceType);
			}
			
			rs = pstmt.executeQuery();
			
			Garage garage;
			while(rs.next()) {
				garage = new Garage(
						rs.getInt("index"), 
						rs.getString("name"),
						rs.getString("address"),
						rs.getString("contact_no"),
						rs.getString("contact_2"),
						rs.getString("contact_3"),
						rs.getString("near_city"),
						rs.getString("highway"),
						rs.getString("website"),
						rs.getString("vehicle_category"),
						rs.getString("services"),
						rs.getString("Field12"),
						rs.getString("Field13"),
						rs.getString("Field14"),
						rs.getString("Field15")
						);
				garageList.add(garage);	
			}
			return garageList; 
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (pstmt != null) pstmt.close();
            	if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		
		return null;
	}
	
	public int insertGarageData(String name, String address, String contact_no, String contact_2, String contact_3, String near_city, String highway, String website, String vehicle, String services, String field12, String field13, String field14, String field15) {
		int result = 0;
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		
		try {
			con = connect();
			//String sql = "SELECT COUNT(*) AS rows FROM garage";
			String sql = "SELECT [index] FROM garage WHERE [index]=(SELECT max([index]) FROM garage)";
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			int rows = 0;
			if(rs.next())
				rows = rs.getInt("index");
			
			sql = "INSERT INTO garage ([index], name, address, contact_no, contact_2, contact_3, near_city, highway, website, vehicle_category, services, field12, field13, field14, field15) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, rows+1);
			pstmt.setString(2, name);
			pstmt.setString(3, address);
			pstmt.setString(4, contact_no);
			pstmt.setString(5, contact_2);
			pstmt.setString(6, contact_3);
			pstmt.setString(7, near_city);
			pstmt.setString(8, highway);
			pstmt.setString(9, website);
			pstmt.setString(10, vehicle);
			pstmt.setString(11, services);
			pstmt.setString(12, field12);
			pstmt.setString(13, field13);
			pstmt.setString(14, field14);
			pstmt.setString(15, field15);
			
			result = pstmt.executeUpdate();	
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (stmt != null) stmt.close();
            	if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		
		return result;
	}
	
	public int editRecord(int index, String name, String address, String contact_no, String contact_2, String contact_3, String near_city, String highway, String website, String vehicle, String services, String field12, String field13, String field14, String field15) {
		int result = 0;
		PreparedStatement pstmt = null;
		Connection con = null;
		
		try {
			con = connect();
			String sql = "UPDATE garage SET name=?, address=?, contact_no=?, contact_2=?, contact_3=?, near_city=?, highway=?, website=?, vehicle_category=?, services=?, field12=?, field13=?, field14=?, field15=? WHERE [index]=?";
			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, name);
			pstmt.setString(2, address);
			pstmt.setString(3, contact_no);
			pstmt.setString(4, contact_2);
			pstmt.setString(5, contact_3);
			pstmt.setString(6, near_city);
			pstmt.setString(7, highway);
			pstmt.setString(8, website);
			pstmt.setString(9, vehicle);
			pstmt.setString(10, services);
			pstmt.setString(11, field12);
			pstmt.setString(12, field13);
			pstmt.setString(13, field14);
			pstmt.setString(14, field15);
			pstmt.setInt(15, index);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		return result;
	}

	public boolean deleteRecord(int index) {
		int result = 0;
		PreparedStatement pstmt = null;
		Connection con = null;
		
		try {
			con = connect();
			String sql = "DELETE FROM garage WHERE [index]=?";
			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, index);
			
			result = pstmt.executeUpdate();
			
			if(result!=0)
				return true;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		return false;
	}
	
	public int insertVehicleDetail(String username, String make, String model, String year, String vehicle_category) {
		int result = 0;
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = connect();
			String sql = "SELECT COUNT(*) AS rows FROM vehicle_details";
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			int rows = 0;
			if(rs.next())
				rows = rs.getInt("rows");
			
			sql = "INSERT INTO vehicle_details ([index], username, make, model, year, vehicle_category) VALUES (?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, rows+1);
			pstmt.setString(2, username);
			pstmt.setString(3, make);
			pstmt.setString(4, model);
			pstmt.setString(5, year);
			pstmt.setString(6, vehicle_category);
			
			result = pstmt.executeUpdate();	
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (stmt != null) stmt.close();
            	if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		
		return result;
	}
	
	public ArrayList<VehicleDetail> getVehicleByUser(String username) {
		ArrayList<VehicleDetail> vechcleList = new ArrayList<VehicleDetail>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		
		try {
			con = connect();
			String sql = "SELECT * FROM vehicle_details WHERE username=?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			
			VehicleDetail vehicle;
			while (rs.next()) {
				vehicle = new VehicleDetail(
						username, 
						rs.getString("make"), 
						rs.getString("model"), 
						rs.getString("year"),
						rs.getString("vehicle_category"));
				vechcleList.add(vehicle);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (pstmt != null) pstmt.close();
            	if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		return vechcleList;
	}

	public ArrayList<VehicleCategory> getVehicleCategory() {
		ArrayList<VehicleCategory> catList = new ArrayList<VehicleCategory>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		
		try {
			con = connect();
			String sql = "SELECT * FROM vehicle_category";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			VehicleCategory category;
			while(rs.next()) {
				category = new VehicleCategory(
						rs.getInt("index"), 
						rs.getString("vehicle_category")
						);
				catList.add(category);	
			}
			return catList; 
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (stmt != null) stmt.close();
            	if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		
		return null;
	}
	
	public ArrayList<ServiceType> getServiceTypes() {
		ArrayList<ServiceType> serviceList = new ArrayList<ServiceType>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		
		try {
			con = connect();
			String sql = "SELECT * FROM service_type";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			ServiceType service;
			while(rs.next()) {
				service = new ServiceType(
						rs.getInt("index"), 
						rs.getString("service_type")
						);
				serviceList.add(service);	
			}
			return serviceList; 
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (stmt != null) stmt.close();
            	if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		
		return null;
	}
	
	public ArrayList<String> getAllCities() {
		ArrayList<String> cities = new ArrayList<String>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		
		try {
			con = connect();
			String sql = "SELECT DISTINCT near_city from garage;";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				cities.add(rs.getString("near_city"));	
			}
			return cities; 
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (stmt != null) stmt.close();
            	if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		
		return null;
	}
	
	public ArrayList<String> getAllHighways() {
		ArrayList<String> highways = new ArrayList<String>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		
		try {
			con = connect();
			String sql = "SELECT DISTINCT highway from garage;";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				highways.add(rs.getString("highway"));	
			}
			return highways; 
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
            try {
            	if (stmt != null) stmt.close();
            	if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		
		return null;
	}
}
