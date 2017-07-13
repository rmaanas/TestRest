package com.testRest;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import com.mysql.jdbc.ResultSetMetaData;

@Path("/getemployeeevents")
public class GetEmployeeEvents {
	
	public static JSONArray convert( ResultSet rs ) throws SQLException, JSONException
	  {
	    JSONArray json = new JSONArray();
	    ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();

	    while(rs.next()) {
	      int numColumns = rsmd.getColumnCount();
	      JSONObject obj = new JSONObject();
	      
	      for (int i=1; i<numColumns+1; i++) {
	        String column_name = rsmd.getColumnLabel(i);
	        String column_alias = rsmd.getColumnLabel(i);

	        if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
	         obj.put(column_alias, rs.getArray(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
	         obj.put(column_alias, rs.getInt(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
	         obj.put(column_alias, rs.getBoolean(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
	         obj.put(column_alias, rs.getBlob(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
	         obj.put(column_alias, rs.getDouble(column_name)); 
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
	         obj.put(column_alias, rs.getFloat(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
	         obj.put(column_alias, rs.getInt(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
	         obj.put(column_alias, rs.getNString(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
	         obj.put(column_alias, rs.getString(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
	         obj.put(column_alias, rs.getInt(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
	         obj.put(column_name, rs.getInt(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
	         obj.put(column_name, rs.getDate(column_name));
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
	        obj.put(column_name, rs.getTimestamp(column_name));   
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.TIME){
		        obj.put(column_name, rs.getTime(column_name));   
		    }
	        else{
	         obj.put(column_name, rs.getObject(column_name));
	        }
	      }
	      json.put(obj);
	    }
	    return json;
	  }
		
	@POST
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
			
			String sql = "SELECT E.EVENTID AS EVENTID, E.VISITID AS VISITID, E.NAME as EVENTNAME, E.STARTTIME AS STARTTIME,E.ENDTIME AS ENDTIME,"
					+ "E.DUEDATE AS DUEDATE,E.VENUE AS EVENTVENUE,E.STATUS AS STATUS, "
					+ "V.PROJECTID AS PROJECTID, V.VISITDATE AS VISITDATE, V.VENUE AS VENUE,"
					+ "P.NAME AS NAME,P.CLIENTEMAIL AS CLIENTEMAIL,P.CLIENTHEAD AS CLIENTHEAD,P.ORGANISATION AS ORGANISATION FROM EVENT E,VISIT V,PROJECT P "
					+ "WHERE E.OWNER = ? AND "
					+ "E.VISITID = V.VISITID AND "
					+ "V.VISITDATE >= ? AND "
					+ "V.PROJECTID = P.PROJECTID ORDER BY V.VISITDATE,E.STARTTIME";
			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			
			ps.setString(1, in.getString("username").toString());
			ps.setString(2, in.getString("currdate").toString());
		
			ResultSet rs = ps.executeQuery();
			
			eventlist  = GetEmployeeEvents.convert(rs);
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
		finally
		{
		       //System.out.println("Closing the connection.");
		       if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		}
		return Response.ok().entity(events).build();
	}
	
}
