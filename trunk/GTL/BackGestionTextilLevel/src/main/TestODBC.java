package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestODBC {

	/**
	 * NO ANDA!!
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	
	public TestODBC() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
		Connection c = DriverManager.getConnection("jdbc:odbc:test-diego","","");
		//String URL = "jdbc:odbc:gtl-test";
	    //Connection c = DriverManager.getConnection(URL, "nacho", "nacho"); 
		Statement st = c.createStatement();
		ResultSet executeQuery = st.executeQuery("SELECT * FROM clientes");
		while(executeQuery.next()){
			System.out.println(executeQuery.getString(2));
		}
		executeQuery.close();
		st.close();
		c.close();
	}

	public static void main(String[] args) {
		try {
			new TestODBC();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
