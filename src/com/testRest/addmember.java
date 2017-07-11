package com.testRest;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.data.Validate;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@Path("/addmember")
public class addmember {

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addmemb(String data) throws Exception
	{
		String status = "not inserted";
		String output = null;
		String pass = null;
		JSONObject jsoninput = new JSONObject(data);
		JSONObject jsonoutput = new JSONObject();
		Validate validate = new Validate();
		
		try{
			
			Connection conn = (Connection) validate.getConnection();
			
			String sql = "INSERT INTO USERS (USERNAME,EMAIL,PASSWORD,ROLE,TEAM) VALUES (?,?,?,?,?)";
			
			pass = randomString(10);
			System.out.println(jsoninput.getString("team").toString());
			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, jsoninput.getString("name").toString());
			ps.setString(2, jsoninput.getString("email").toString());
			ps.setString(3, pass);
			ps.setString(4, jsoninput.getString("role").toString());
			ps.setString(5, jsoninput.getString("team").toString());
						
			int b = ps.executeUpdate();
			
			if(b>0)
			{
				status = "inserted"; 
			}
			conn.close();
			ps.close();
			
			
		}
		catch(SQLException se)
		{
			se.getSQLState();
			se.printStackTrace();
		}
		catch(Exception e){
			System.out.println("Exception");
		}
		finally{
			jsonoutput.put("status", status);
			output = jsonoutput + "";
		}
		return Response.ok()
				.entity(output)
				.build();
	}
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	String randomString( int len ){
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   return sb.toString();
	}
}
