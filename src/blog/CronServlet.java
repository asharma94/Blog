package blog;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.List;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.ObjectifyService;

import java.util.Collections;


@SuppressWarnings("serial")
public class CronServlet extends HttpServlet {

	
	private static final Logger _logger = Logger.getLogger(CronServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		// Recipient's email ID needs to be mentioned.

	      // Sender's email ID needs to be mentioned
	      String from = "admin@fast-kiln-122614.appspotmail.com";
	      
	      ObjectifyService.register(BlogPost.class);
	      List<BlogPost> blogposts = ObjectifyService.ofy().load().type(BlogPost.class).list();   
	      Collections.sort(blogposts);
	      
	      ObjectifyService.register(Subscribed.class);
	      List<Subscribed> sub = ObjectifyService.ofy().load().type(Subscribed.class).list();   

	      
	      List<BlogPost> sendBlogs = new ArrayList<BlogPost>();
	      Date date = new Date();
	      for(BlogPost b : blogposts){
	    	  if(b.compareDate(date)){
	    		  sendBlogs.add(b);
	    	  }
	      }
	      
	      String text = "";
	      for(BlogPost b : sendBlogs){
	    	  text += ("User: " + b.getUser() + "\n");
	    	  text += ("Title: " + b.getTitle() + "\n");
	    	  text += ("Message: " + b.getContent() + "\n");
	    	  text += "\n";
	    	  
	      }

	      // Assuming you are sending email from localhost
	      //String host = "smtp.google.com";
		
		// Get system properties
	      Properties properties =  new Properties();//System.getProperties();

	      // Setup mail server
	      //properties.setProperty("mail.smtp.host", host);

	      // Get the default Session object.
	      Session session = Session.getDefaultInstance(properties, null);
	      
	 if(!sendBlogs.isEmpty()){ 
		try {
			for(Subscribed s : sub){
			_logger.info("Cron Job has been executed");
				
			
			User to = s.getUser();
			
			  // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.toString()));

	         // Set Subject: header field
	         message.setSubject("Daily Blog Overview");

	         // Now set the actual message
	         message.setText(text);

	         // Send message
	         Transport.send(message);
			}
		}
		catch (MessagingException mex) {
			//Log any exceptions in your Cron Job
			_logger.info("Error occured: " + mex);
		}
	 }
}

@Override
public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
			doGet(req, resp);
		}
}