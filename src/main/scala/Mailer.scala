trait Mailer {
  def sendMail(from: String, to: String, subject: String, message: String): Unit
}
