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
		String status = "not inserted";
		String output = null;
		JSONObject jsoninput = new JSONObject(data);
		JSONObject jsonoutput = new JSONObject();
		Validate validate = new Validate();
		String database = "mydatabase";
		String table_name = "project";
		Connection conn = null;
		
		try{
			
			conn = (Connection) validate.getConnection();
			
			String sql = "INSERT INTO PROJECT (NAME, CLIENTHEAD, ORGANISATION, CLIENTEMAIL, MANAGER) VALUES (?,?,?,?,?)";
			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, jsoninput.getString("name").toString());
			ps.setString(2, jsoninput.getString("clienthead").toString());
			ps.setString(3, jsoninput.getString("organisation").toString());
			ps.setString(4, jsoninput.getString("clientemail").toString());
			ps.setString(5, jsoninput.getString("manager").toString());
			
			int b = ps.executeUpdate();
			
			if(b>0)
			{/*
				String sql1 ="SELECT  AUTO_INCREMENT "
						+ "FROM information_schema.tables "
						+ "WHERE "
						+ "Table_SCHEMA =? AND table_name = ?";
				
				PreparedStatement ps1 = (PreparedStatement) conn.prepareStatement(sql1);
				ps1.setString(1, database);
				ps1.setString(2, table_name);
				
				ResultSet rs = ps1.executeQuery();*/
				
				
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
			if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			jsonoutput.put("status", status);
			output = jsonoutput + "";
		}
		return Response.ok()
				.entity(output)
				.build();
	}
}
