package ru.cheranev;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * @author Cheranev N.
 *         created on 27.02.2017.
 */
@Service
public class EmailService {

    public void sendEmail(EmailDto dto) throws IOException {

        final String address = "***";
        final String username = "***";
        final String password = "***";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
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
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(dto.getFrom()));
            msg.setRecipients(Message.RecipientType.TO, dto.getTo());
            msg.setRecipients(Message.RecipientType.CC, dto.getCc());
            msg.setRecipients(Message.RecipientType.BCC, dto.getBcc());
            msg.setSubject(dto.getSubject());
            msg.setSentDate(new Date());
            msg.setText(dto.getText());

            MimeBodyPart mbp = new MimeBodyPart();

            Attachment firstAttachment = dto.getAttachments().get(0);
            String ext = FilenameUtils.getExtension(firstAttachment.getFilename());
            File tempFile = File.createTempFile("attachment", "." + ext, null);
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(Base64.decode(firstAttachment.getContent()));
            fos.close();

            // attach the file to the message
            mbp.attachFile(tempFile);

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp);

            // add the Multipart to the message
            msg.setContent(mp);

            Transport.send(msg);

            String filename = tempFile.getCanonicalPath();
            System.out.println("temp file deleted: " + filename);

            boolean deleted = tempFile.delete();
            Assert.isTrue(deleted);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
