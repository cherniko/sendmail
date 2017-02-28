package ru.cheranev;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.mock_javamail.Mailbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.SharedByteArrayInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Component
public class SendmailApplicationTests {

    private static final String FROM = "from";
    private static final String TO = "to";
    private static final String CC = "cc";
    private static final String BCC = "bcc";
    private static final String SUBJECT = "subject";
    private static final String TEXT = "text";
    private static final Date DATE = new Date();
    private static final String CONTENT = "content";
    private static final String CONTENT_BASE64 = "Y29udGVudA==";
    private static final String FILENAME = "attachment.ext";

    @Autowired
    private EmailService emailService;

    @Test
    public void test() throws IOException, MessagingException {
        Assert.notNull(emailService);

        Mailbox.clearAll();

        EmailDto dto = new EmailDto();
        dto.setFrom(FROM);
        dto.setTo(TO);
        dto.setCc(CC);
        dto.setBcc(BCC);
        dto.setSubject(SUBJECT);
        dto.setText(TEXT);
        Attachment attachment = new Attachment();
        attachment.setContent(CONTENT_BASE64);
        attachment.setFilename(FILENAME);
        dto.getAttachments().add(attachment);
        emailService.sendEmail(dto);

        List<Message> inbox = Mailbox.get(TO);
        Assert.notNull(inbox);
        Assert.notEmpty(inbox);
        Message message = inbox.get(0);
        Assert.isTrue((message.getFrom()[0]).toString().equals(FROM));
        Assert.isTrue(message.getSubject().equals(SUBJECT));
        Assert.isTrue((message.getRecipients(Message.RecipientType.TO)[0]).toString().equals(TO));
        Assert.isTrue((message.getRecipients(Message.RecipientType.CC)[0]).toString().equals(CC));
        Assert.isTrue((message.getRecipients(Message.RecipientType.BCC)[0]).toString().equals(BCC));
        Assert.isTrue(message.getSentDate().after(DATE) & message.getSentDate().before(new Date()));

        MimeMultipart content = (MimeMultipart) message.getContent();
        Part part = content.getBodyPart(0);
        Assert.isTrue(part.getFileName().contains(FilenameUtils.getBaseName(FILENAME)));
        Assert.isTrue(part.getFileName().contains(FilenameUtils.getExtension(FILENAME)));
        Assert.isTrue(readContent((SharedByteArrayInputStream) part.getContent()).equals(CONTENT));
    }

    private String readContent(ByteArrayInputStream byteArrayInputStream) {
        Scanner scanner = new Scanner(byteArrayInputStream);
        scanner.useDelimiter("\\Z");
        String data = "";
        if (scanner.hasNext())
            data = scanner.next();
        return data;
    }

}
