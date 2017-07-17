package com.testRest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.data.Validate;
import com.mysql.jdbc.ResultSetMetaData;

@Path("/searchhistory")
public class searchhistory {
	
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
	public Response validate(String data) throws JSONException
	{
		String cname;
		String pname;
		String fdate;
		String tdate;
		JSONObject jsoninput = new JSONObject(data);
		Connection conn = null;
		ResultSet rs = null;
		JSONObject jsonoutput = new JSONObject();
		JSONArray histdetails = new JSONArray();
		String visits = null;
		Validate validate = new Validate();
		String sql = null;
		//JSONObject json = new JSONObject();
		
		  
	  	cname=jsoninput.get("cname").toString();
		pname=jsoninput.get("pname").toString();
		fdate=jsoninput.get("fdate").toString();
		tdate=jsoninput.get("tdate").toString();
		System.out.println(fdate);
		  
		  //0
		  if(cname.equals("") && pname.equals("") && fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME=p.NAME and p.projectid = v.projectid;";
		  }
		  
		  //1
		  if(cname.equals("") && pname.equals("") && fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME=p.NAME and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  
		  if(cname.equals("") && pname.equals("") && !fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME=p.NAME and v.visitdate >= '" + fdate + "' and p.projectid = v.projectid;";
		  }
		  
		  if(cname.equals("") && !pname.equals("") && fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME='"+ pname +"' and p.projectid = v.projectid;";
		  }
		  
		  if(!cname.equals("") && pname.equals("") && fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD='" + cname + "' and p.NAME=p.NAME and p.projectid = v.projectid;";
		  }
		  
		//2
		  if(cname.equals("") && pname.equals("") && !fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME=p.NAME and v.visitdate >= '" + fdate + "' and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  
		  if(cname.equals("") && !pname.equals("") && fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME='"+ pname +"' and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  
		  if(!cname.equals("") && pname.equals("") && fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD='" + cname + "' and p.NAME=p.NAME and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  
		  if(cname.equals("") && !pname.equals("") && !fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME='"+ pname +"' and v.visitdate >= '" + fdate + "' and p.projectid = v.projectid;";
		  }
		  
		  if(!cname.equals("") && pname.equals("") && !fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD='" + cname + "' and p.NAME=p.NAME and v.visitdate >= '" + fdate + "' and p.projectid = v.projectid;";
		  }
		  
		  if(!cname.equals("") && !pname.equals("") && fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD='" + cname + "' and p.NAME='"+ pname +"' and p.projectid = v.projectid;";
		  }
		  
		//3
		  if(cname.equals("") && !pname.equals("") && !fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME='"+ pname +"' and v.visitdate >= '" + fdate + "' and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  if(!cname.equals("") && pname.equals("") && !fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD='" + cname + "' and p.NAME=p.NAME and v.visitdate >= '" + fdate + "' and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  if(!cname.equals("") && !pname.equals("") && !fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD='" + cname + "' and p.NAME='"+ pname +"' and v.visitdate >= '" + fdate + "' and p.projectid = v.projectid;";
		  }
		  if(!cname.equals("") && !pname.equals("") && fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD='" + cname + "' and p.NAME='"+ pname +"' and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  
		  //4
		  if(!cname.equals("") && !pname.equals("") && !fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.VISITID AS VISITID,v.visitdate AS VISITDATE,v.venue AS VENUE from PROJECT p,VISIT v where p.CLIENTHEAD='" + cname + "' and p.NAME='"+ pname +"' and v.visitdate >= '" + fdate + "' and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  
//		  System.out.println(sql);
		
		try{
			conn =  validate.getConnection();  
			PreparedStatement ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			histdetails  = searchhistory.convert(rs);
			jsonoutput.put("visits", histdetails);
			
			visits = jsonoutput + "";
			ps.close();
			rs.close();
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
		finally
		{
			if (conn != null) try { conn.close(); } catch (SQLException ignore) {}

			visits = jsonoutput + "";
		}
		
		return Response.ok()
				.entity(visits)
				.build();	
	}

}
