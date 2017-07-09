package com.testRest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.json.*;
import com.users.User;
import com.data.Validate;
import com.jwt_pack.Constants;
import com.jwt_pack.JwtManager;

@Path("/login")
public class Login {

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response validate(String data) throws JSONException, ClassNotFoundException
	{
		JSONObject jsoninput = new JSONObject(data);
		JSONObject json = new JSONObject();
		Validate validate = new Validate();
		String authenticated = "";
		User user = new User(jsoninput.get("username").toString(),jsoninput.get("password").toString());
		
		validate.check(user);
		
		if(user.isValid)
		{
			authenticated = "yes";
			json.put("username", jsoninput.get("username").toString());
			json.put("role", user.role);
			json.put("team", user.team);
			json.put("accesstoken",JwtManager.createJWT(jsoninput.get("username").toString(),Constants.ISSUER));
		}
		else
		{
			authenticated = "Invalid credentials";
		}
		json.put("authenticated", authenticated);
		String result = json + "";
		
		return Response.ok()
				.entity(result)
				.build();
	}
}
