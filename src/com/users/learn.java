package com.users;

import java.util.*;
import java.text.*;
  
public class learn {

    public static boolean validate(Date start1,Date end1,Date start2,Date end2)
    {
    	boolean canExist = false;
    	//start times are lesser than end times
    	if(start1.getTime()<end1.getTime() && start2.getTime()<end2.getTime())
    	{
    		if(start1.getTime()>=end2.getTime() || end1.getTime()<=start2.getTime())
    		{
    			canExist = true;
    		}
    	}
    	return canExist;
    }
    
	public static void main(String args[]) {
      SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
      SimpleDateFormat ft1 = new SimpleDateFormat ("HH:mm:ss");
      String startin = "2017-07-10 22:30:00";
      String endin = "2017-07-10 22:40:00";
      String start = "2017-07-10 22:00:00";
      String end = "2017-07-10 22:30:00";
       
      Date tin,tout,tstart,tend;
      try {
         tin = ft.parse(startin);
         tout = ft.parse(endin);
         tstart = ft.parse(start);
         tend = ft.parse(end);
         System.out.println(ft1.format(tin));
         System.out.println(validate(tin,tout,tstart,tend)); 
      }catch (ParseException e) { 
         System.out.println("Unparseable using " + ft); 
      }
   }
}