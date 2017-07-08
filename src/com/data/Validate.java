package com.data;

import java.sql.*;
import java.util.*;

import com.users.User;

public class Validate {
	  //String dbName = "mydatabase";
	  String dbName = "mydatabase";
	  String userName = "root";
	  //String password="password"; //on Amazon
	  String password = "admin";
	  //String hostname = "firstdb.cmdd3pmg7orp.us-west-2.rds.amazonaws.com";
	  String hostname = "localhost";
	  //String port = "3400"; //on amazon
	  String port = "3306"; //my local MySql port number
	  //String url = "jdbc:mysql://firstdb.cmdd3pmg7orp.us-west-2.rds.amazonaws.com:3400/mydatabase?useSSL=false";
	  String url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?useSSL=false";
	  
	  String driver = "com.mysql.jdbc.Driver";
	  
	  public Connection getConnection() throws ClassNotFoundException{
		  
		  Connection conn = null;
		  try{
			  Class.forName(driver);
			  conn = DriverManager.getConnection(url,userName,password);
		  }
		  catch(ClassNotFoundException ce)
		  {
			  ce.printStackTrace();
		  }
		  catch(Exception e)
		  {
			  throw new RuntimeException("Error!",e);
		  }
		  return conn;
	  }
	  
	  public void check(User user) throws ClassNotFoundException
	  {
		 
		  Connection conn = null;
		  String sql = "SELECT * FROM users where username='" + user.username + "' and password='"+ user.password +"';";
		  ResultSet rs = null;
		  
		  try 
		  {
			  conn = getConnection();
			  PreparedStatement ps = conn.prepareStatement(sql);
			  rs = ps.executeQuery();
			  while(rs.next())
			  {
				  user.isValid = true;
				  user.role = rs.getString("role");
				  user.team = rs.getString("team");
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
	  }
}
