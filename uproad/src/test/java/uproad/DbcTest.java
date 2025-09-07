package uproad;

import static org.junit.Assert.*;

import org.junit.Test;

import dbc.DBConnection;

public class DbcTest {

	@Test
	public void testConnect() {
		DBConnection dbc = new DBConnection();
		dbc.connect();
	}

	@Test
	public void testIsValidUser() {
		DBConnection dbc = new DBConnection();
		System.out.println(dbc.isValidUser("praja", "nilu"));
		
	}

	@Test
	public void testGetUserByUsername() {
		fail("Not yet implemented");
	}

}
