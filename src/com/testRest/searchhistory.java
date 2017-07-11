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
		//JSONObject json = new JSONObject();
		
		String dbName = "mydatabase";
		  //String dbName = "mydatabase";
		  String userName = "root";
		  //String password="password"; //on Amazon
		  String password = "root";
		  //String hostname = "firstdb.cmdd3pmg7orp.us-west-2.rds.amazonaws.com";
		  String hostname = "localhost";
		  //String port = "3400"; //on amazon
		  String port = "3306"; //my local MySql port number
		  //String url = "jdbc:mysql://firstdb.cmdd3pmg7orp.us-west-2.rds.amazonaws.com:3400/mydatabase?useSSL=false";
		  String url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?useSSL=false";
		  
		  String driver = "com.mysql.jdbc.Driver";
		  String sql = null;
		  try 
		  {
			    System.out.println("Loading driver...");
			    Class.forName("com.mysql.jdbc.Driver");
			    System.out.println("Driver loaded!");
		  } 
		  catch (ClassNotFoundException e) 
		  {
			    throw new RuntimeException("Cannot find the driver in the classpath!", e);
		  }
		  
	  	cname=jsoninput.get("cname").toString();
		pname=jsoninput.get("pname").toString();
		fdate=jsoninput.get("fdate").toString();
		tdate=jsoninput.get("tdate").toString();
		System.out.println(fdate);
		  
		  //0
		  if(cname.equals("") && pname.equals("") && fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME=p.NAME and p.projectid = v.projectid;";
		  }
		  
		  //1
		  if(cname.equals("") && pname.equals("") && fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME=p.NAME and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  
		  if(cname.equals("") && pname.equals("") && !fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME=p.NAME and v.visitdate >= '" + fdate + "' and p.projectid = v.projectid;";
		  }
		  
		  if(cname.equals("") && !pname.equals("") && fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME='"+ pname +"' and p.projectid = v.projectid;";
		  }
		  
		  if(!cname.equals("") && pname.equals("") && fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD='" + cname + "' and p.NAME=p.NAME and p.projectid = v.projectid;";
		  }
		  
		//2
		  if(cname.equals("") && pname.equals("") && !fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME=p.NAME and v.visitdate >= '" + fdate + "' and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  
		  if(cname.equals("") && !pname.equals("") && fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME='"+ pname +"' and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  
		  if(!cname.equals("") && pname.equals("") && fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD='" + cname + "' and p.NAME=p.NAME and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  
		  if(cname.equals("") && !pname.equals("") && !fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME='"+ pname +"' and v.visitdate >= '" + fdate + "' and p.projectid = v.projectid;";
		  }
		  
		  if(!cname.equals("") && pname.equals("") && !fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD='" + cname + "' and p.NAME=p.NAME and v.visitdate >= '" + fdate + "' and p.projectid = v.projectid;";
		  }
		  
		  if(!cname.equals("") && !pname.equals("") && fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD='" + cname + "' and p.NAME='"+ pname +"' and p.projectid = v.projectid;";
		  }
		  
		//3
		  if(cname.equals("") && !pname.equals("") && !fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD=p.CLIENTHEAD and p.NAME='"+ pname +"' and v.visitdate >= '" + fdate + "' and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  if(!cname.equals("") && pname.equals("") && !fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD='" + cname + "' and p.NAME=p.NAME and v.visitdate >= '" + fdate + "' and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  if(!cname.equals("") && !pname.equals("") && !fdate.equals("") && tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD='" + cname + "' and p.NAME='"+ pname +"' and v.visitdate >= '" + fdate + "' and p.projectid = v.projectid;";
		  }
		  if(!cname.equals("") && !pname.equals("") && fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD='" + cname + "' and p.NAME='"+ pname +"' and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  
		  //4
		  if(!cname.equals("") && !pname.equals("") && !fdate.equals("") && !tdate.equals(""))
		  {
			  sql = "select p.name as NAME,p.clienthead AS CLIENTHEAD,p.organisation AS ORGANISATION,p.clientemail AS CLIENTEMAIL,p.manager AS MANAGER,v.visitdate AS VISITDATE,v.venue AS VENUE from project p,visit v where p.CLIENTHEAD='" + cname + "' and p.NAME='"+ pname +"' and v.visitdate >= '" + fdate + "' and v.visitdate <= '" + tdate + "' and p.projectid = v.projectid;";
		  }
		  
		  System.out.println(sql);
		
		try{
			conn = DriverManager.getConnection(url, userName, password);
			  PreparedStatement ps = conn.prepareStatement(sql);
			  rs = ps.executeQuery();
			
			  histdetails  = searchhistory.convert(rs);
			jsonoutput.put("visits", histdetails);
			
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
