/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kiroule.vaadin.demo.ui.util;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author patilneh
 */
public class SendMailUtil {
                  public static void send(String to) {
                             String from = "tietopool@gmail.com";
                             String password = "tietopool@123";
                             String sub = "Tieto Pool";
                             int index = to.indexOf('.');
                             String Username= to.substring(0,index).toUpperCase();
                             String msg = "Dear "+Username+",\n \nYou have 1 subscription for carpool. \nFor more details sign in to your TietoPool account. \n\n\nRIDE SMART,\nTieto Pool";
                             
                             // Get properties object
                             //rohit.kapure@tieto.com
                             Properties props = new Properties();
                             props.put("mail.smtp.host", "smtp.gmail.com");
                             props.put("mail.smtp.socketFactory.port", "465");
                             props.put("mail.smtp.socketFactory.class",
                                                          "javax.net.ssl.SSLSocketFactory");
                             props.put("mail.smtp.auth", "true");
                             props.put("mail.smtp.port", "465");
                             // get Session
                             Session session;
                      session = Session.getDefaultInstance(props,
                              new javax.mail.Authenticator() {
                                  protected PasswordAuthentication getPasswordAuthentication() {
                                      return new PasswordAuthentication(from, password);
                                  }
                              });
                             // compose message
                             try {
                                           MimeMessage message = new MimeMessage(session);
                                           message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                                           message.setSubject(sub);
                                           message.setText(msg);
                                           // send message
                                           Transport.send(message);
                                           System.out.println("message sent successfully");
                             } catch (MessagingException e) {
                                           throw new RuntimeException(e);
                             }

              }
                  
                  public static void main(String[] args) {
        SendMailUtil.send("neha.patil@tieto.com");
       
    }

}
