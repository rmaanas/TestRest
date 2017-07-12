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

@Path("/editEvent")
public class EditEvent {

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response editproject(String data) throws Exception
	{
		String status = "not updated";
		String output = null;
		JSONObject jsoninput = new JSONObject(data);
		JSONArray events = jsoninput.getJSONArray("events");
		int i=0 ,len = events.length();
		JSONObject jsonoutput = new JSONObject();
		Validate validate = new Validate();
		String database = "mydatabase";
		String table_name = "event";
		
		try{
			
			Connection conn = (Connection) validate.getConnection();
			String sql = "UPDATE EVENT SET NAME = ?, STARTTIME = ?, ENDTIME = ?, OWNER = ?, DUEDATE = ?, VENUE = ?, STATUS = ? WHERE EVENTID = ?";
			PreparedStatement ps=null;
			int b;
			for(i=0;i<len;i++)
			{
				ps = (PreparedStatement) conn.prepareStatement(sql);
				ps.setString(1, events.getJSONObject(i).getString("NAME"));
				ps.setString(2, events.getJSONObject(i).getString("STARTTIME"));
				ps.setString(3, events.getJSONObject(i).getString("ENDTIME"));
				ps.setString(4, events.getJSONObject(i).getString("OWNER"));
				ps.setString(5, events.getJSONObject(i).getString("DUEDATE"));
				ps.setString(6, events.getJSONObject(i).getString("VENUE"));
				ps.setString(7, events.getJSONObject(i).getString("STATUS"));
				ps.setInt(8, events.getJSONObject(i).getInt("EVENTID"));
				
				b = ps.executeUpdate();
				
				if(b>0)
				{	
					status = "updated"; 
				}				
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
