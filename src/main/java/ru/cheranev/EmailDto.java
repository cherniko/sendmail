package ru.cheranev;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cheranev N.
 *         created on 27.02.2017.
 */
@Getter
@Setter
public class EmailDto {
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String text;
    private List<Attachment> attachments = new ArrayList<>();

//    @Getter
//    @Setter
//    class Attachment {
//        private String filename;
//        private String content;
//    }
}
