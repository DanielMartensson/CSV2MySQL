# CSV2MySQL

This is a tool that can be used if you need to schedule a task that download CSV files from an FTP server, then import then delete these files on the FTP server. Then read the downloaded CSV files and import these into a MySQL/MariaDB database. Then delete these CSV files as well. 

Just open the project and go to the .properties files and set your configuration.
I have been used this for lots of projects when it comes to CodeSys logging. 

This tool is using the awesome Spring Framework.

## How to use

Step 1: Install at least OpenJDK 8 from AdoptOpenJDK
Step 2: Install MySQL or MariaDB
Step 3: Go to `application.properties` and change to your database configuration and username

```
# MySQL
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/CSV2MySQL?createDatabaseIfNotExist=true
spring.datasource.username=myUser
spring.datasource.password=myPassword

# Hibernate
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
hibernate.format_sql=true

# Server
server.port = 8081
```

Step 4: Gto to `ftp.properties` and change to your password and username for the FTP server
```
# Enter connections
ftp.username = ITH
ftp.password = ITH
ftp.connectionPath = 192.168.0.52
ftp.folderPath = /FTP
```

Step5: Go to `schedule.properties` and change your scheduling configuration as well. The key `downloadCSVFiles` is used of you want to download files from FTP.
```
# Global enable
schedule.enable = true
schedule.downloadCSVFiles = true
schedule.enableMail = true

# Schedule lists
schedule.ITHBAEPulsBenchIXPanelPathCSVFolder = C:\\Users\\Lab4\\Documents\\FTP
schedule.ITHBAEPulsBenchIXPanelIntervall = 5000
```

Step6: Go to 'mail.properties' and change your configurations
```
# Mail - Transmitter
mail.host=smtp.gmail.com
mail.port=587
mail.username=yourGmail@gmail.com
mail.password=yourPassword
mail.properties.mail.smtp.auth=true
mail.properties.mail.smtp.starttls.enable=true

# Mail - Reciever
mail.destination = test@test.se
mail.subject = Epost ITH
mail.message = Det fungerar
```

