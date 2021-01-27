package com.wenda.communicationsystem.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;


/**
 * @Author Liguangzhe
 * @Date created in 20:22 2020/6/9
 */
@Service
public class MailSender implements InitializingBean {
    @Autowired
    private TemplateEngine templateEngine;

    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    public boolean sendWithHTMLTemplate(String to, String subject, String template,
                                        Map<String, Object> model) {
        try {
            String nick = MimeUtility.encodeText("lgz");
            InternetAddress from = new InternetAddress(nick + "<360238076@qq.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            Context context = new Context();
            for (Map.Entry<String, Object> entry: model.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
            String result = templateEngine.process(template, context);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setText(result, true);
            mimeMessageHelper.setSubject(subject);
            mailSender.send(mimeMessage);
            return true;
        } catch(Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("360238076@qq.com");
        mailSender.setPassword("wbmkslcbxbgecadb");
        mailSender.setHost("smtp.qq.com");
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties properties = new Properties();
        properties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(properties);
    }
}
