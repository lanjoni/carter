# Carter

Carter is a simple, lightweight, and fast project to send emails from the command line using OAuth2 authentication with multiple providers.

## Installation

Installation must be carried out directly from the official repository, preparing the ecosystem with the requirements presented below.

### Requirements

- [Scala 3](https://www.scala-lang.org/download/)
- [sbt](https://www.scala-sbt.org/download.html)
- [OAuth2](https://oauth.net/2/)
> For example: if you want to connect with Gmail, you must create a project in the [Google Cloud Platform](https://console.cloud.google.com/), enable the Gmail API, and create the OAuth2 credentials [here](https://console.cloud.google.com/apis/credentials).

## Usage

Clone the repository and put your credentials at `src/main/resources` with the name `credentials.json`.

The format can be different depending on the provider, but it must have the `credentials.json` name.

To run the project, execute the following command:

```bash
$ sbt "run <provider> <from> <to> <subject> <message>"
```
  
  Where:
  - `<provider>` is the name of the provider you want to use.
  - `<from>` is the email address of the sender.
  - `<to>` is the email address of the receiver.
  - `<subject>` is the subject of the email.
  - `<message>` is the message of the email.

Example:

```bash
$ sbt "run gmail from@gmail.com to@gmail.com 'Hello World' 'This is a test'"
```

## Providers

- [Gmail](https://www.google.com/gmail/)
  
## License

This program and the accompanying materials are made available under the
terms of the Apache License 2.0 which is available at 
https://www.apache.org/licenses/LICENSE-2.0.