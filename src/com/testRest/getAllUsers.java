package com.testRest;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import com.data.Validate;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/getAllUsers")
public class getAllUsers {
		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(){
		Validate v = new Validate();
		Connection conn = null;
		String users = null;
		JSONObject jsonoutput = new JSONObject();
		JSONArray usersList = new JSONArray();
		
		try{
			conn = (Connection) v.getConnection();
			String sql = "SELECT USERNAME,ROLE,TEAM FROM USERS WHERE ROLE='EMPLOYEE'";
			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			usersList  = GetAllProjects.convert(rs);
			jsonoutput.put("users", usersList);
			
			users = jsonoutput + "";
			ps.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
	       //System.out.println("Closing the connection.");
	       if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
	       users = jsonoutput + "";
	    }
		
		return Response.ok()
				.entity(users)
				.build();
	}
	
}