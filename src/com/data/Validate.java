package com.data;

import java.sql.*;
import java.util.*;

public class Validate {
	  String dbName = "firstdb";
	  String userName = "root";
	  String password = "password";
	  String hostname = "firstdb.cmdd3pmg7orp.us-west-2.rds.amazonaws.com";
	  String port = "3400";
	  String url = "jdbc:mysql://firstdb.cmdd3pmg7orp.us-west-2.rds.amazonaws.com:3400/mydatabase?useSSL=false";

	  public boolean check(String username,String pass)
	  {
		  boolean exists = false;
		  try 
		  {
			    System.out.println("Loading driver...");
			    Class.forName("com.mysql.jdbc.Driver");
			    System.out.println("Driver loaded!");
		  } 
		  catch (ClassNotFoundException e) 
		  {
			    throw new RuntimeException("Cannot find the driver in the classpath!", e);
		  }

		  Connection conn = null;
		  String sql = "SELECT * FROM user where username='" + username + "' and password='"+ pass +"';";
		  ResultSet rs = null;
		  int i=1;
		  int number = 0;
		  String name = null;
		  try 
		  {
			  conn = DriverManager.getConnection(url, userName, password);
			  PreparedStatement ps = conn.prepareStatement(sql);
			  rs = ps.executeQuery();
			  while(rs.next())
			  {
				  exists = true;
			  }
		    rs.close();
		    ps.close();
		    conn.close();

		  } 
		  catch (SQLException ex) {
		    // Handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		  } 
		  finally {
		       System.out.println("Closing the connection.");
		      if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		  }
		  return exists;
	  }
}
