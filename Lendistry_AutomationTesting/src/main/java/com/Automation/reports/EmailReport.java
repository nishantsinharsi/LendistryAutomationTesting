package com.Automation.reports;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.Automation.Keywords.GenericKeywords;

public class EmailReport {
	public static Properties envProp;
	Properties emailProperties;
	Session mailSession;
	MimeMessage emailMessage;

	

	public void setEnvProp(Properties envProp) {
		EmailReport.envProp = envProp;
	}

	public Properties getEnvProp() {
		return envProp;
	}

	public void SendEmail() throws MessagingException, MessagingException {
		GenericKeywords GK = new GenericKeywords();
		GK.zipReportsFolder();
		String[] ToEmails = { "nishant.sinha@rsystems.com" };
		// ,"anjali.sharma@rsystems.com" "seema.kundu@rsystems.com",
		// "kartik.chauhan@rsystems.com
		//String ToEmails2 = envProp.getProperty("MailSendTo2");
		String From = envProp.getProperty("MailSendFrom");
		String Subject = envProp.getProperty("MailSubject");
		String Body = envProp.getProperty("MailBody");
		String host = envProp.getProperty("SMTPHost");
		String socketFactoryPort = envProp.getProperty("socketFactoryport");
		String port = envProp.getProperty("SMTPport");
		//String auth = envProp.getProperty("SendMail");
		String socketFactoryCls = envProp.getProperty("socketFactoryClass");

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.socketFactory.port", socketFactoryPort);
		properties.setProperty("mail.smtp.socketFactory.class", socketFactoryCls);
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.port", port);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties,

				new javax.mail.Authenticator() {

					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("Nishant.Sinha@Rsystems.com", "Realme@12601");
					}

				});

		try {
			// Create a default MimeMessage object.

			Message message = new MimeMessage(session);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(From));

			// Set To: header field of the header.
			// message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(ToEmails2));
			// message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(ToEmails1));
			for (int i = 0; i < ToEmails.length; i++) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(ToEmails[i]));
			}
			// Set Subject: header field
			message.setSubject(Subject);

			// Now set the actual message
			BodyPart objMessageBodyPart = new MimeBodyPart();
			
			objMessageBodyPart.setText(Body);

			// Create another object to add another content
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();
			String zipFileName = ExtentManager.ReportsZipName;

			// Create data source and pass the filename
			DataSource source = new FileDataSource(zipFileName);

			// set the handler
			messageBodyPart2.setDataHandler(new DataHandler(source));

			// set the file
			messageBodyPart2.setFileName("report.zip");

			// Create object of MimeMultipart class
			MimeMultipart multipart = new MimeMultipart();

			// add body part 1
			multipart.addBodyPart(objMessageBodyPart);

			// add body part 2
			multipart.addBodyPart(messageBodyPart2);

			message.setContent(multipart);

			Transport transport = session.getTransport("smtp");

			transport.connect();

			transport.sendMessage(message, message.getAllRecipients());
			// Transport.send(message);
			transport.close();

			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

	}
}
