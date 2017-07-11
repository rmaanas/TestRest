package com.testRest;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.data.Validate;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@Path("/addVisit")
public class AddVisit {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addvisit(String data)
	{
		String status = "notinserted";
		String output = null;
		JSONObject jsoninput = new JSONObject(data);
		JSONObject jsonoutput = new JSONObject();
		Validate validate = new Validate();
		
		try{
			
			Connection conn = (Connection) validate.getConnection();
				
			String sql = "INSERT INTO visit ()";
			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setInt(1,jsoninput.getInt("projectid"));
			ps.setString(3, jsoninput.getString("visitdate").toString());
			ps.setString(4, jsoninput.getString("venue").toString());
			ps.setString(5, jsoninput.getString("projectname").toString());
			ps.setString(6, jsoninput.getString("organisation").toString());
			
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
