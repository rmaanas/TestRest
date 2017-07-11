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

@Path("/getEvents")
public class GetEvents {	
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response getCurrVisits(String data) throws Exception
	{
		Validate v = new Validate();
		Connection conn = null;
		String events = null;
		JSONObject jsonoutput = new JSONObject();
		JSONArray eventsList = new JSONArray();
		JSONObject jsoninput = new JSONObject(data);
		int visitID = jsoninput.getInt("visitid");
		
		try{
			conn = (Connection) v.getConnection();
			String sql = "SELECT * FROM EVENT E WHERE E.VISITID = " + visitID ;
			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			eventsList  = getCurrentVisits.convert(rs);
			jsonoutput.put("events", eventsList);
			
			events = jsonoutput + "";
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
	    }
		
		return Response.ok()
				.entity(events)
				.build();
	}
	
}