package service;

import java.io.IOException;
import java.io.InputStream;

import play.api.Play;

import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.auth.PropertiesCredentials;

public class AmazonEmailService {
	static final String FROM = "praneet.loke@centricconsulting.com";  // Replace with your "From" address. This address must be verified.
    static final String TO = "praneetloke@hotmail.com"; // Replace with a "To" address. If you have not yet requested
                                                      // production access, this address must be verified.
    static final String BODY = "";
    static final String SUBJECT = "CCD Merge Status";

    public static void sendEmail(String emailMessage) throws IOException {
    	InputStream is = Play.classloader(Play.current()).getResourceAsStream("AwsCredentials.properties");
        PropertiesCredentials credentials = new PropertiesCredentials(is);

        // Construct an object to contain the recipient address.
        Destination destination = new Destination().withToAddresses(new String[]{TO});
        
        // Create the subject and body of the message.
        Content subject = new Content().withData(SUBJECT);
        Content textBody = new Content().withData(emailMessage); 
        Body body = new Body().withText(textBody);
        
        // Create a message with the specified subject and body.
        Message message = new Message().withSubject(subject).withBody(body);
        
        // Assemble the email.
        SendEmailRequest request = new SendEmailRequest().withSource(FROM).withDestination(destination).withMessage(message);
        
        try {
            // Instantiate an Amazon SES client, which will make the service call with the supplied AWS credentials.
            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
       
            // Send the email.
            client.sendEmail(request);  
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }
}
