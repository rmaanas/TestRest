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
	public Response modifyevent(String data) throws ClassNotFoundException, SQLException
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
			
			String sql = "UPDATE CHECKLIST SET NAME = ? , STARTTIME = ? ,ENDTIME = ?"
					+ ",VENUE = ? WHERE NAME = ? AND VENUE = ?";
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, in.getString("eventname").toString());
			ps.setString(2, in.getString("starttime").toString());
			ps.setString(3, in.getString("endtime").toString());
			ps.setString(4, in.getString("venue").toString());
			ps.setString(5, in.getString("prevname").toString());
			ps.setString(6, in.getString("prevvenue").toString());
			
			if(ps.executeUpdate()>0)
			{
				status = "updated";
			}
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		finally{
			ps.close();
			conn.close();
		}
		
		jsonoutput.put("status", status);
		output = jsonoutput + "";
	
		return Response.ok().entity(output).build();
	}
	
}
