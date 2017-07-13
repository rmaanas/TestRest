package com.testRest;

import java.security.SecureRandom;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
   /*public static void main(String[] args) {
	   
	sendm();   
   }*/
   public static void sendm(String name,String rece,String pass,String role,String team)
   {
	   final String username = "pranavbhoyar9@gmail.com";
       final String password = "pnb@aarohi@pnb";

     Properties props = new Properties();
     props.put("mail.smtp.auth", "true");
     props.put("mail.smtp.starttls.enable", "true");
     props.put("mail.smtp.host", "smtp.gmail.com");
     props.put("mail.smtp.port", "587");

     Session session = Session.getInstance(props,
             new javax.mail.Authenticator() {
                 protected PasswordAuthentication getPasswordAuthentication() {
                     return new PasswordAuthentication(username, password);
                 }
             });

     try {

         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress("pranavbhoyar9@gmail.com"));
         message.setRecipients(Message.RecipientType.TO,
                 InternetAddress.parse(rece));
         message.setSubject("You are appointed");
         message.setText("Hi "+name+ "!! Your role is " + role + " and team is "+team+" and password is "+pass);

         Transport.send(message);

         System.out.println("Mail sent succesfully!");

     } catch (MessagingException e) {
         throw new RuntimeException(e);
     }
	
   }

}