package com.testRest;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

@Path("/getEditEventData")
public class getEditEventData {	
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response getData(String data) throws Exception
	{
		Validate v = new Validate();
		Connection conn = null;
		String events = null;
		JSONObject jsonoutput = new JSONObject();
		JSONArray eventsList = new JSONArray();
		JSONArray usersList = new JSONArray();
		JSONObject jsoninput = new JSONObject(data);
		int visitID = jsoninput.getInt("visitid");
		
		try{
			conn = (Connection) v.getConnection();
			String sql = "SELECT * FROM EVENT E WHERE E.VISITID = " + visitID + " ORDER BY E.STARTTIME";
			String sql1 = "SELECT USERNAME,ROLE,TEAM FROM USERS WHERE ROLE='EMPLOYEE'";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			PreparedStatement ps1 = (PreparedStatement) conn.prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();
			ResultSet rs1 = ps1.executeQuery();
			eventsList  = getCurrentVisits.convert(rs);
			usersList = getCurrentVisits.convert(rs1);
			jsonoutput.put("events", eventsList);
			jsonoutput.put("users" , usersList);
			
			events = jsonoutput + "";
			ps.close();
			rs.close();
			ps1.close();
			rs1.close();
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
	    }
		
		return Response.ok()
				.entity(events)
				.build();
	}
	
}