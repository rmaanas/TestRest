package com.testRest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.json.*;



@Path("/ftoc")
public class FtoC {
	
	@GET 
	@Produces("application/json")
	public Response convertFtoC() throws JSONException
	{
		JSONObject json = new JSONObject();
		double f = 98.4;
		double c;
		c = (f-32)*5/9;
		json.put("f", f);
		json.put("c", c);
		
		String result = json + "";
		
		return Response.ok() //200
				.entity(result)
				.build();
				//Response.status(200).entity(result).build();
	}
	
	@Path("{f}")
	@GET 
	@Produces("application/json")
	public Response convertFtoCInput(@PathParam("f") float f) throws JSONException
	{
		JSONObject json = new JSONObject();
		double c;
		c = (f-32)*5/9;
		json.put("f", f);
		json.put("c", c);
		
		String result = json + "";
		
		return Response.ok() //200
				.entity(result)
				.build();
	}

	 @POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response convertFtoC(String data) throws JSONException
	{
		JSONObject jsoninput = new JSONObject(data);
		JSONObject json = new JSONObject();
		double f = Double.parseDouble(jsoninput.get("f").toString());
		double c;
		c = (f-32)*5/9;
		json.put("f", f);
		json.put("c", c);
		
		String result = json + "";
		
		return Response.ok() //200
				.entity(result)
				.build();
				//Response.status(200).entity(result).build();
	}

}
