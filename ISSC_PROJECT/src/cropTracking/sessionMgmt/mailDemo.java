package cropTracking.sessionMgmt;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;    
class Mailer{  
    public static void send(String from,String password,String to,String sub,String msg){  
          //Get properties object    
          Properties props = new Properties();    
          props.put("mail.smtp.host", "smtp.gmail.com");    
          props.put("mail.smtp.socketFactory.port", "465");    
          props.put("mail.smtp.socketFactory.class",    
                    "javax.net.ssl.SSLSocketFactory");    
          props.put("mail.smtp.auth", "true");    
          props.put("mail.smtp.port", "465");    
          //get Session   
          Session session = Session.getInstance(props, new javax.mail.Authenticator() {
        	    protected PasswordAuthentication getPasswordAuthentication() {
        	        return new PasswordAuthentication(from, password);
        	    }
        	});    
          //compose message    
          try {    
           MimeMessage message = new MimeMessage(session);    
           message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
           message.setSubject(sub);    
           message.setText(msg);    
           //send message  
           Transport.send(message);    
           System.out.println("message sent successfully");    
          } catch (MessagingException e) {throw new RuntimeException(e);}    
             
    }  
}  
public class mailDemo{    
 public static void main(String[] args) {    
     //from,password,to,subject,message  
     Mailer.send("pritam.aug31@gmail.com","Pulsar150","rajshrishinde77@gmail.com","Java Mail Api Test {Crop Tracking and Information}","Hello Team CropTracking!!!");  
     //change from, password and to  
 }    
}   

/*
https://www.javatpoint.com/example-of-sending-email-using-java-mail-api-through-gmail-server
import java.util.Properties;    
import javax.mail.*;    
import javax.mail.internet.*;    
class Mailer{  
   public static void send(String from,String password,String to,String sub,String msg){  
         //Get properties object    
         Properties props = new Properties();    
         props.put("mail.smtp.host", "smtp.gmail.com");    
         props.put("mail.smtp.socketFactory.port", "465");    
         props.put("mail.smtp.socketFactory.class",    
                   "javax.net.ssl.SSLSocketFactory");    
         props.put("mail.smtp.auth", "true");    
         props.put("mail.smtp.port", "465");    
         //get Session   
         Session session = Session.getDefaultInstance(props,    
          new javax.mail.Authenticator() {    
          protected PasswordAuthentication getPasswordAuthentication() {    
          return new PasswordAuthentication(from,password);  
          }    
         });    
         //compose message    
         try {    
          MimeMessage message = new MimeMessage(session);    
          message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
          message.setSubject(sub);    
          message.setText(msg);    
          //send message  
          Transport.send(message);    
          System.out.println("message sent successfully");    
         } catch (MessagingException e) {throw new RuntimeException(e);}    
            
   }  
}  
public class SendMailSSL{    
public static void main(String[] args) {    
    //from,password,to,subject,message  
    Mailer.send("from@gmail.com","xxxxx","to@gmail.com","subject","content");  
    //change from, password and to  
}    
}    

*/

