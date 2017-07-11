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

@Path("/getCurrentVisits")
public class getCurrentVisits {
	
	public static JSONArray convert( ResultSet rs ) throws SQLException, JSONException
		  {
		    JSONArray json = new JSONArray();
		    ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();

		    while(rs.next()) {
		      int numColumns = rsmd.getColumnCount();
		      JSONObject obj = new JSONObject();
		      
		      for (int i=1; i<numColumns+1; i++) {
		        String column_name = rsmd.getColumnName(i);

		        if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
		         obj.put(column_name, rs.getArray(column_name));
		        }
		        else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
		         obj.put(column_name, rs.getInt(column_name));
		        }
		        else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
		         obj.put(column_name, rs.getBoolean(column_name));
		        }
		        else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
		         obj.put(column_name, rs.getBlob(column_name));
		        }
		        else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
		         obj.put(column_name, rs.getDouble(column_name)); 
		        }
		        else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
		         obj.put(column_name, rs.getFloat(column_name));
		        }
		        else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
		         obj.put(column_name, rs.getInt(column_name));
		        }
		        else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
		         obj.put(column_name, rs.getNString(column_name));
		        }
		        else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
		         obj.put(column_name, rs.getString(column_name));
		        }
		        else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
		         obj.put(column_name, rs.getInt(column_name));
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
		        else{
		         obj.put(column_name, rs.getObject(column_name));
		        }
		      }
		      json.put(obj);
		    }
		    return json;
		  }
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response getCurrVisits(String data) throws Exception
	{
		Validate v = new Validate();
		Connection conn = null;
		String visits = null;
		JSONObject jsonoutput = new JSONObject();
		JSONArray visitsList = new JSONArray();
		JSONObject jsoninput = new JSONObject(data);
		String currDate = jsoninput.getString("currdate");
		
		try{
			conn = (Connection) v.getConnection();
			String sql = "SELECT P.PROJECTID AS PROJECTID,P.NAME AS NAME,P.CLIENTHEAD AS CLIENTHEAD,P.ORGANISATION AS ORGANISATION,P.CLIENTEMAIL AS CLIENTEMAIL,V.VISITID AS VISITID,V.VISITDATE AS VISITDATE,V.VENUE AS VENUE FROM PROJECT P, VISIT V WHERE P.PROJECTID = V.PROJECTID AND V.VISITDATE>='" + currDate+"'";
			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			visitsList  = getCurrentVisits.convert(rs);
			jsonoutput.put("visits", visitsList);
			
			visits = jsonoutput + "";
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
				.entity(visits)
				.build();
	}
	
}