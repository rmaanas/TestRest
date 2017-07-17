package com.testRest;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.data.Validate;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@Path("/modifychecklist")
public class ModifyChecklist {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createevent(String data) throws ClassNotFoundException, SQLException
	{
		Validate v = new Validate();
		Connection conn=null;
		JSONObject in = new JSONObject(data);
		String status = "notupdated";
		PreparedStatement ps = null;
		JSONObject jsonoutput = new JSONObject();
		String output = null;
		
		try{
			conn = (Connection) v.getConnection();
			
			String sql = "UPDATE CHECKLIST SET NAME = ? WHERE NAME = ?";
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, in.getString("eventname").toString());
			ps.setString(2, in.getString("prevname").toString());
			
			if(ps.executeUpdate()==1)
			{
				status = "updated";
			}
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
		
		jsonoutput.put("status", status);
		output = jsonoutput + "";
	
		return Response.ok().entity(output).build();
	}
	
}
