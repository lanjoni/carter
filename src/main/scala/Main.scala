object Main {
  def matchServer(server: String, from: String, to: String, subject: String, message: String) = server match {
    case "gmail" => new Gmailer().sendMail(from, to, subject, message)
    case _ => println("Unknown server")
  }

  def main(args: Array[String]) =
    if (args.length < 5) {
      println("Usage: sbt run '<server> <from> <to> <subject> <message>'")
      System.exit(1)
    }

    val Array(server, from, to, subject, message) = args
    matchServer(server, from, to, subject, message)
}