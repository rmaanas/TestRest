package com.testRest;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.data.Validate;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@Path("/addVisit")
public class AddVisit {

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addproject(String data) throws Exception
	{
		String status = "not inserted";
		String output = null;
		JSONObject jsoninput = new JSONObject(data);
		JSONArray visitsList = new JSONArray();
		JSONObject jsonoutput = new JSONObject();
		Validate validate = new Validate();
		String database = "mydatabase";
		String table_name = "visit";
		Connection conn=null;
		
		try{
			
			conn = (Connection) validate.getConnection();
			
			String sql = "INSERT INTO VISIT (PROJECTID, VISITDATE, VENUE) VALUES (?,?,?)";
			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setInt(1, jsoninput.getInt("projectid"));
			ps.setString(2, jsoninput.getString("visitdate").toString());
			ps.setString(3, jsoninput.getString("venue").toString());
			
			int b = ps.executeUpdate();
			
			if(b>0)
			{
				String sql1 ="SELECT * FROM VISIT where VISITID >= ALL(SELECT VISITID FROM VISIT)";
				
				PreparedStatement ps1 = (PreparedStatement) conn.prepareStatement(sql1);
				
				ResultSet rs = ps1.executeQuery();
				visitsList  = GetAllProjects.convert(rs);
				rs.close();
				jsonoutput.put("currVisit", visitsList.get(0));
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
