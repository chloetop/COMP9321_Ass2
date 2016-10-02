<%@ page import="java.util.*, javax.mail.*, javax.mail.internet.*" %>
<%
String host = "outlook.office365.com";
String user = "z5081713@ad.unsw.edu.au";
String pass = "";
String to = "svajiraya@icloud.com";
String from = "s.vajiraya@unsw.edu.au";
String subject = "Test subject";
String messageText = "Test body";
boolean sessionDebug = false;

Properties props = System.getProperties();
props.put("mail.host", host);
/* props.put("mail.transport.protocol", "smtp");
props.put("mail.smtp.auth", "true"); */
/* props.put("mail.smtp.port", 26); */
// Uncomment 5 SMTPS-related lines below and comment out 2 SMTP-related lines above and 1 below if you prefer to use SSL
props.put("mail.transport.protocol", "smtp");
props.put("mail.smts.auth", "true");
props.put("mail.smtp.port", 587);
props.put("mail.smtp.starttls.enable","true");
/* props.put("mail.smtps.ssl.trust", host); */
Session mailSession = Session.getDefaultInstance(props, null);
mailSession.setDebug(sessionDebug);
Message msg = new MimeMessage(mailSession);
msg.setFrom(new InternetAddress(from));
InternetAddress[] address = {new InternetAddress(to)};
msg.setRecipients(Message.RecipientType.TO, address);
msg.setSubject(subject);
msg.setSentDate(new Date());
msg.setText(messageText);
Transport transport = mailSession.getTransport("smtp");
// Transport transport = mailSession.getTransport("smtps");
transport.connect(host, user, pass);
transport.sendMessage(msg, msg.getAllRecipients());
transport.close();
%>