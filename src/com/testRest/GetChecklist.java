package com.testRest;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;
import com.data.Validate;


@Path("/getchecklist")
public class GetChecklist {
	
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
	        else if(rsmd.getColumnType(i)==java.sql.Types.TIME){
		        obj.put(column_name, rs.getTime(column_name));   
		    }
	        else{
	         obj.put(column_name, rs.getObject(column_name));
	        }
	      }
	      obj.put("checked",false);
	      json.put(obj);
	    }
	    return json;
	  }

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getchecklist(String data) throws ClassNotFoundException, SQLException
	{
		Validate v = new Validate();
		Connection conn = null;
		PreparedStatement ps = null;
		JSONObject jsonoutput = new JSONObject();
		String output = null;
		ResultSet rs = null;
		JSONArray checklist = new JSONArray();
		
		try{
			conn = (Connection) v.getConnection();
			
			String sql = "SELECT * FROM CHECKLIST";
			
			ps = (PreparedStatement) conn.prepareStatement(sql);
			
			rs = ps.executeQuery(sql);
			
			checklist  = GetChecklist.convert(rs);
			
			jsonoutput.put("checklist", checklist);
			
			rs.close();
			ps.close();
			conn.close();
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("exception occured");
		}
		finally
		{
			if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		}
		
		output = jsonoutput + "";
	
		return Response.ok().entity(output).build();
	}
	
}
