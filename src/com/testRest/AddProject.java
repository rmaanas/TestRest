package com.testRest;

import java.sql.ResultSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONObject;

import com.data.Validate;

import com.mysql.fabric.Response;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@Path("/addProject")
public class AddProject {

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addproject(String data) throws Exception
	{
		String status = null;
		try{
			JSONObject jsoninput = new JSONObject(data);
			JSONObject json = new JSONObject();
			Validate validate = new Validate();
			
			Connection conn = (Connection) validate.getConnection();
			
			String sql = "INSERT INTO PROJECT (PROJECTNAME, ORGANISATION, CLIENTHEAD) VALUES (?,?,?)";
			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, jsoninput.getString("projectname").toString());
			ps.setString(2, jsoninput.getString("organisation").toString());
			ps.setString(3, jsoninput.getString("clienthead").toString());
			
			boolean b = ps.execute();
			
			if(b==true)
			{
				status = "inserted";
			}
			else
			{
				status = "error";
			}
			
			json.put("status", status);
			
			conn.close();
			ps.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return Response.ok.entity(status).build();
	}
}
