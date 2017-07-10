package com.testRest;

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

@Path("/addProject")
public class AddProject {

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addproject(String data) throws Exception
	{
		String status = "notinserted";
		String output = null;
		JSONObject jsoninput = new JSONObject(data);
		JSONObject jsonoutput = new JSONObject();
		Validate validate = new Validate();
		
		try{
			
			Connection conn = (Connection) validate.getConnection();
			
			String sql = "INSERT INTO PROJECT (PROJECTNAME, ORGANISATION, CLIENTHEAD) VALUES (?,?,?)";
			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, jsoninput.getString("projectname").toString());
			ps.setString(2, jsoninput.getString("organisation").toString());
			ps.setString(3, jsoninput.getString("clienthead").toString());
			
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
			e.printStackTrace();
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
}
