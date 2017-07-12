package com.testRest;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.testRest.GetAllProjects;
import com.data.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@Path("/getemployeeevents")
public class GetEmployeeEvents {

		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getevents(String data){
		
		Validate v = new Validate();
		JSONObject in = new JSONObject(data);
		JSONObject out = new JSONObject();
		Connection conn = null;
		JSONArray eventlist = new JSONArray();
		String events = null;
		try{
			conn = (Connection) v.getConnection();
			
			String sql = "SELECT EVENT.EVENTID AS EVENTID, EVENT.VISITID AS VISITID, EVENT.NAME as EVENTNAME, EVENT.STARTTIME AS STARTTIME,EVENT.ENDTIME AS ENDTIME,"
					+ "EVENT.DUEDATE AS DUEDATE,EVENT.VENUE AS EVENTVENUE,EVENT.STATUS AS STATUS, "
					+ "VISIT.PROJECTID, VISIT.VISITDATE AS VISITDATE, "
					+ "PROJECT.NAME AS PROJECTNAME,PROJECT.MANAGER AS MANAGER,PROJECT.CLIENTHEAD AS CLIENTHEAD,PROJECT.ORGANISATION AS ORGANISATION FROM EVENT,VISIT,PROJECT "
					+ "WHERE EVENT.OWNER = ? AND"
					+ "EVENT.VISITID = VISIT.VISITID AND"
					+ "VISIT.VISITDATE > ?"
					+ "VISIT.PROJECTID = PROJECT.PROJECTID;";
			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			
			ps.setString(1, in.getString("username").toString());
			ps.setString(2, in.getString("currdate").toString());
		
			ResultSet rs = ps.executeQuery();
			
			eventlist  = GetAllProjects.convert(rs);
			out.put("events", eventlist);
			
			events = out + "";
			ps.close();
			rs.close();
			conn.close();
		}
		catch(JSONException json)
		{
			json.printStackTrace();
			json.getMessage();
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		return Response.ok().entity(events).build();
	}
	
}
