package com.testRest;

import java.sql.ResultSet;
import java.sql.Time;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.JSONObject;

import com.itextpdf.*;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.data.*;

@Path("/getpdf")
public class PrintSchedule {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    
    @POST
    @Consumes("application/json")
    @Produces("application/pdf")
    public Response sendpdf(String data){
    	
    	JSONObject in = new JSONObject(data);
    	ResponseBuilder responseBuilder;
    	Document document = new Document();
    	
    	try 
    	{
    		String projectname = in.getString("projectname").toString();
    		String clienthead = in.getString("clienthead").toString();
    		String organisation = in.getString("organisation").toString();
    		String clientemail = in.getString("clientemail").toString();
    		String visitdate = in.getString("visitdate").toString();
    		
	        PdfWriter.getInstance(document, new FileOutputStream(projectname+".pdf"));
	        document.open();
	        
	        Anchor anchorTarget = new Anchor("Visit Details");
	        anchorTarget.setName("top");
	        Paragraph paragraph1 = new Paragraph();
	   
	        paragraph1.setSpacingBefore(50);
	   
	        paragraph1.add(anchorTarget);
	        document.add(paragraph1);
	   
			document.add(new Paragraph("Projectname:  " + projectname),this.subFont);
			document.add(new Paragraph("Client Head:  " + clienthead),this.subFont);
			document.add(new Paragraph("Organisation:  " + organisation),this.subFont);
			document.add(new Paragraph("Client Email:  " + clientemail),this.subFont);
			document.add(new Paragraph("Visit Date:  " + visitdate),this.subFont);
			
			paragraph1.setSpacingBefore(30);
	        paragraph1.add(anchorTarget);
	        document.add(paragraph1);
	        
	        document.add(new Paragraph("Event Schedule:  ",this.subFont));
			
	        PdfPTable table = new PdfPTable(4);
	        PdfPCell c1 = new PdfPCell(new Phrase("Agenda"));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);

	        c1 = new PdfPCell(new Phrase("Start Time"));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);

	        c1 = new PdfPCell(new Phrase("End Time"));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        
	        c1 = new PdfPCell(new Phrase("Venue"));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        table.setHeaderRows(1);
	        
	        Validate v = new Validate();
	        Connection conn = (Connection) (v.getConnection());
	        
	        String sql = "SELECT NAME, STARTTIME,ENDTIME,VENUE FROM EVENT WHERE VISITID=?";
			
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setInt(1, in.getInt("visitid"));
			ResultSet rs = ps.executeQuery();
			
			PdfPCell table_cell;
			
			while(rs.next())
			{
				 String agenda = rs.getString("name");
                 table_cell=new PdfPCell(new Phrase(agenda));
                 table.addCell(table_cell);
                 
                 String dept_name=rs.getString("venue");
                 table_cell=new PdfPCell(new Phrase(dept_name));
                 table.addCell(table_cell);
                 
                 Time starttime=rs.getTime("starttime");
                 table_cell=new PdfPCell(new Phrase(starttime));
                 table.addCell(table_cell);
                 
                 Time endtime=rs.getString("endtime");
                 table_cell=new PdfPCell(new Phrase(endtime));
                 table.addCell(table_cell);
			}
			
			document.add(table);
			
			responseBuilder = Response.ok((Object) document);
		    responseBuilder.type("application/pdf");
		    responseBuilder.header("Content-Disposition", "filename="+ projectname +"-visit.pdf");
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return responseBuilder.build();
    }
}
