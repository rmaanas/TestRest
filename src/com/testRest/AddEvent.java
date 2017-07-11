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

@Path("/addEvent")
public class AddEvent {

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addevent(String data) throws Exception
	{
		String status = "not inserted";
		String output = null;
		JSONObject jsoninput = new JSONObject(data);
		JSONObject jsonoutput = new JSONObject();
		Validate validate = new Validate();
		String database = "mydatabase";
		String table_name = "event";
		
		try{
			
			Connection conn = (Connection) validate.getConnection();
			
			String sql = "INSERT INTO EVENT (VISITID, NAME, STARTTIME, ENDTIME, OWNER, DUEDATE, VENUE, STATUS) VALUES (?,?,?,?,?,?,?,?)";
			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setInt(1, jsoninput.getInt("visitid"));
			ps.setString(2, jsoninput.getString("name").toString());
			ps.setString(3, jsoninput.getString("starttime").toString());
			ps.setString(4, jsoninput.getString("endtime").toString());
			ps.setString(5, jsoninput.getString("owner").toString());
			ps.setString(6, jsoninput.getString("duedate").toString());
			ps.setString(7, jsoninput.getString("venue").toString());
			ps.setString(8, jsoninput.getString("status").toString());
			
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
			jsonoutput.put("status", status);
			output = jsonoutput + "";
		}
		return Response.ok()
				.entity(output)
				.build();
	}
}
