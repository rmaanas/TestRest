package com.testRest;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.data.Validate;


@Path("/getchecklist")
public class GetChecklist {

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
			
			String sql = "SELECT * FROM CHECKLIST ORDER BY STARTTIME";
			
			ps = (PreparedStatement) conn.prepareStatement(sql);
			
			rs = ps.executeQuery(sql);
			
			checklist  = GetAllProjects.convert(rs);
			
			jsonoutput.put("checklist", checklist);
			jsonoutput.put("count", checklist.length());
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		finally{
			ps.close();
			conn.close();
			rs.close();
		}
		
		output = jsonoutput + "";
	
		return Response.ok().entity(output).build();
	}
	
}
