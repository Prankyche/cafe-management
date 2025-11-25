//package com.internship.cafe_management.Util;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class Emailutils {
//    @Autowired
//    private JavaMailSender emailSender;
//
//    public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("cafeadmin123@gmail.com");
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        if (list != null && list.size() > 0) {
//            message.setCc(getCcArray(list));
//        }
//        emailSender.send(message);
//    }
//
//    private String[] getCcArray(List<String> ccList) {
//        String[] cc = new String[ccList.size()];
//        for (int i = 0; i < cc.length; i++) {
//            cc[i] = ccList.get(i);
//        }
//        return cc;
//    }
//}
