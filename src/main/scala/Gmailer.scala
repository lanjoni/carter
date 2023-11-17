import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.{GoogleAuthorizationCodeFlow, GoogleClientSecrets}
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonError
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.model.Message
import org.apache.commons.codec.binary.Base64

import javax.mail.Session
import javax.mail.internet.{InternetAddress, MimeMessage}
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.nio.file.Paths
import java.util.Properties
import scala.jdk.CollectionConverters.SetHasAsScala

import scala.jdk.CollectionConverters.SetHasAsJava
import com.google.api.services.gmail.GmailScopes.GMAIL_SEND
import javax.mail.Message.RecipientType.TO

class Gmailer {
  private val service: Gmail = {
    val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
    val jsonFactory = GsonFactory.getDefaultInstance
    new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
      .setApplicationName("Simple Gmailer")
      .build()
  }

  private def getCredentials(httpTransport: NetHttpTransport, jsonFactory: GsonFactory): Credential = {
    val clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(classOf[Gmailer].getResourceAsStream("credentials.json")))

    val flow = new GoogleAuthorizationCodeFlow.Builder(
      httpTransport, jsonFactory, clientSecrets, Set(GMAIL_SEND).asJava)
      .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile))
      .setAccessType("offline")
      .build()

    val receiver = new LocalServerReceiver.Builder().setPort(8888).build()
    new AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
  }

  def sendMail(from: String, to: String, subject: String, message: String): Unit = {
    val props = new Properties
    val session = Session.getDefaultInstance(props, null)
    val email = new MimeMessage(session)
    email.setFrom(new InternetAddress(from))
    email.addRecipient(TO, new InternetAddress(to))
    email.setSubject(subject)
    email.setText(message)

    val buffer = new ByteArrayOutputStream()
    email.writeTo(buffer)
    val rawMessageBytes = buffer.toByteArray
    val encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes)
    var msg = new Message()
    msg.setRaw(encodedEmail)

    try {
      msg = service.users().messages().send("me", msg).execute()
      println("Message id: " + msg.getId)
      println(msg.toPrettyString)
    } catch {
      case e: GoogleJsonResponseException =>
        val error = e.getDetails
        if (error.getCode == 403) {
          System.err.println("Unable to send message: " + e.getDetails)
        } else {
          throw e
        }
    }
  }
}