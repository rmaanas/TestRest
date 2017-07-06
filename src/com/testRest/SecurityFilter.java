package com.testRest;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.json.JSONObject;

import com.jwt_pack.Constants;
import com.jwt_pack.JwtManager;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

@Provider
public class SecurityFilter implements ContainerRequestFilter{


	@Override
	public ContainerRequest filter(ContainerRequest requestContext) {
		// TODO Auto-generated method stub
		System.out.println("TRUTH: " + requestContext.getRequestUri().getPath().contains("login"));
		String username = requestContext.getHeaderValue("username");
		String accesstoken = requestContext.getHeaderValue("accesstoken");
		if(!requestContext.getRequestUri().getPath().contains("login"))
		{
			if(username != null && accesstoken != null)
			{
				if (JwtManager.parseJwt(accesstoken, username)) 
				{	
					System.out.println("Valid Credentials");
					return requestContext;
				}
			}
		}
		
		System.out.println("Invalid credentials");

	/*	
		if (!requestContext.getRequestUri().getPath().contains("login")) {
			List<String> authHeaders = requestContext.getRequestHeader(Constants.AUTHORIZATION_HEADER_KEY);
			List<String> usernameHeaders = requestContext.getRequestHeader(Constants.USERNAME_KEY);
			String accessToken = "";
			String username = "";
			Map<String, List<String>> reqheaders = requestContext.getRequestHeaders();
			
			System.out.println("authorization: " + reqheaders.get("authorization"));
			for (Entry<String, List<String>> entry : reqheaders.entrySet())
			{
			    System.out.println(entry.getKey() + "/" + entry.getValue());
			}
			
			if(authHeaders!= null && usernameHeaders!=null)
			{
				if (authHeaders.size() > 0 && usernameHeaders.size() > 0) {
					accessToken = authHeaders.get(0);
					username = usernameHeaders.get(0);
					if (JwtManager.parseJwt(accessToken, username)) {
						
						return requestContext;
					}
				}
			}
			else
			{
				System.out.println("something is null");
			}
			

					
			
			JSONObject json = new JSONObject();
			json.put("error", "User does not have access");
			String myresponse = json + "";
			System.out.println(Response.Status.UNAUTHORIZED);
			Response response = Response
								.status(Response.Status.UNAUTHORIZED)
								.header("Content-Type", "application/json")
								.entity(myresponse)
								.build();
			throw new WebApplicationException(response);
		}
	*/
		return requestContext;
	}

}
