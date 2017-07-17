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

@Path("/addEvents")
public class AddEvents {

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addEvents(String data) throws Exception
	{
		String status = "not inserted";
		String output = null;
		JSONObject jsoninput = new JSONObject(data);
		JSONArray events = jsoninput.getJSONArray("events");
		int i=0 ,len = events.length();
		JSONObject jsonoutput = new JSONObject();
		Validate validate = new Validate();
		String database = "mydatabase";
		String table_name = "event";
		Connection conn=null;
		
		if(len==0)
		{
			status = "inserted";
		}
		
		try{
			
			conn = (Connection) validate.getConnection();
			
			String sql = "INSERT INTO EVENT (VISITID, NAME, STARTTIME, ENDTIME, OWNER, DUEDATE, VENUE, STATUS) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement ps=null;
			int b;
			for(i=0;i<len;i++)
			{
				ps = (PreparedStatement) conn.prepareStatement(sql);
				ps.setInt(1, events.getJSONObject(i).getInt("visitid"));
				ps.setString(2, events.getJSONObject(i).getString("name"));
				ps.setString(3, events.getJSONObject(i).getString("starttime"));
				ps.setString(4, events.getJSONObject(i).getString("endtime"));
				ps.setString(5, events.getJSONObject(i).getString("owner"));
				ps.setString(6, events.getJSONObject(i).getString("duedate"));
				ps.setString(7, events.getJSONObject(i).getString("venue"));
				ps.setString(8, events.getJSONObject(i).getString("status"));
				
				b = ps.executeUpdate();
				
				if(b>0)
				{	
					status = "inserted"; 
				}				
			}
			ps.close();
			conn.close();
		}
		catch(SQLException se)
		{
			se.getSQLState();
			se.printStackTrace();
		}
		catch(Exception e){
			System.out.println("Exception");
			e.printStackTrace();
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