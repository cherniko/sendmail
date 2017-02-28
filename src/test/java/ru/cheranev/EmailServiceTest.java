package ru.cheranev;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Cheranev N.
 *         created on 28.02.2017.
 */
@RunWith(SpringRunner.class)
public class EmailServiceTest {

    @Test
    public void sendEmail() throws Exception {

        EmailDto emailDto = new EmailDto();
        emailDto.setFrom("from");
        emailDto.setTo("to");
        emailDto.setSubject("subject");
        emailDto.setText("Hello");
        Attachment attachment = new Attachment();
        attachment.setFilename("filename.txt");
        attachment.setContent("SGVsbG8gZmlsZSBjb250ZW50");
        emailDto.getAttachments().add(attachment);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(emailDto);
        System.out.println(jsonString);
        System.out.println(jsonString);
    }

}