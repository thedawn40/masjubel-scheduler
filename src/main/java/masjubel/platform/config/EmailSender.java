package masjubel.platform.config;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String filename){
        // Email configuration settings
        final String username = "no-reply@masjubel.com";
        final String password = "Luthf!92"; // Replace with your actual password

        // SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "masjubel.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");

        // Start a mail session
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("alfarisi.luthfi@yahoo.com"));
            message.setSubject("Test Email with Generated Resource Attachment");

            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();

            // Part one is the text content
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("This is the email message body");
            multipart.addBodyPart(messageBodyPart);

            // Part two is the attachment from resource/generated
            messageBodyPart = new MimeBodyPart();
            // String filename = "new-20240810.png"; // Replace with the actual file name
            String filePath = EmailSender.class.getClassLoader().getResource("generated/" + filename).getPath();
            filePath = filePath.replace("%20", " "); // Decode any spaces in the path
            File file = new File(filePath);
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent email successfully with attachment");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
    }
}
