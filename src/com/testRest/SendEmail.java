package com.testRest;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.data.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@Path("mail")
public class SendEmail {
	
	private String generatePassword(){
		StringBuilder sb = new StringBuilder();
		char[] characters = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
		char[] specialcharacters = {'!','@','#','$','%','&','*','-','+'};
		
		Random rn = new Random();
			
		int i = 0;
		char c = 'a';
		while(i<15)
		{
			if(i%3==0)
			{
				c = characters[rn.nextInt(characters.length)];
			}
			else if(i%3==1)
			{
				c = specialcharacters[rn.nextInt(specialcharacters.length)];
			}
			else
			{
				c = (char) ('0' + rn.nextInt()%10); 
			}
			sb.append(c);
			i++;
		}
		return sb.toString();
	}
	
	public static void sendmail(String tomail,String newpassword) {

	      // Sender's email ID needs to be mentioned
	      String from = "cvmsaffron@gmail.com";//change accordingly
	      final String username = "cvmsaffron@gmail.com";//change accordingly
	      final String password = "cvmsaffron2017";//change accordingly

	      Properties props = new Properties();
	      
	      props.put("mail.smtp.host", "smtp.gmail.com");    
        props.put("mail.smtp.socketFactory.port", "465");    
        props.put("mail.smtp.socketFactory.class",    
                  "javax.net.ssl.SSLSocketFactory");    
        props.put("mail.smtp.auth", "true");    
        props.put("mail.smtp.port", "587");    

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	      new javax.mail.Authenticator() {
	         protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	         }
	      });

	      try {
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.setRecipients(Message.RecipientType.TO,
	         InternetAddress.parse(tomail));

	         // Set Subject: header field
	         message.setSubject("Request for change of password for Customer Visit Management Software");

	         // Now set the actual message
	         message.setContent("<p style="+"font-size="+"18px"+">Hello, we have changed your password as requested by you!"
	         		+ "This is a system generated password. So please do not respond to this mail."
	         		+ "Your new password is " + newpassword+"</p>","text/html");

	         Transport.send(message);

	         System.out.println("Sent message successfully....");

	      } catch (MessagingException e) {
	            throw new RuntimeException(e);
	      }
	   }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response sendmail(String data) throws ClassNotFoundException{

		Validate v = new Validate();
		JSONObject jsoninput = new JSONObject(data);
		String tomail = jsoninput.getString("tomail");
		String status ="not changed";
		
		String newpassword = generatePassword();
		System.out.println(newpassword);
		try{
			Connection conn = (Connection) v.getConnection();
			
			String sql = "UPDATE USERS SET PASSWORD = ? WHERE USERNAME = ?";
			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			
			ps.setString(1, newpassword);
			ps.setString(2, jsoninput.getString("username").toString());
			
			int b = ps.executeUpdate();
			
			if(b==1)
			{
				status = "changed";
				SendEmail.sendmail(tomail, newpassword);
			}
			
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		return Response.ok().entity(status).build();
	}
}
