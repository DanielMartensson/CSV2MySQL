package se.danielmartensson.CSV2MySQL.components;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.apache.commons.net.ftp.FTP;

@Component
@PropertySource("classpath:ftp.properties")
public class FTPConnection {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${ftp.connectionPath}")
	private String connectionPath; // e.g ftp.example.org/folder/path
	
	@Value("${ftp.password}")
	private String password;
	
	@Value("${ftp.username}")
	private String username;
	
	public void scannFolderOverFTP(String downloadPath) {

		FTPClient client = new FTPClient();

		try {
			// Connect
			client.connect(connectionPath);
			client.login(username, password);
			client.enterLocalPassiveMode();
			client.setFileType(FTP.BINARY_FILE_TYPE);

			// Download
			if (client.isConnected()) {
				FTPFile[] ftpFiles = client.listFiles();
				for (FTPFile fileFTP : ftpFiles) {
					String remoteFile = fileFTP.getName();
					File downloadFile = new File(downloadPath);
					OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
					boolean success = client.retrieveFile(remoteFile, outputStream);
					if (success == true) {
						logger.info("File: " + remoteFile + " has been downloaded.");
					}
					outputStream.close();
					client.deleteFile(remoteFile);
				}
			}
			client.logout();
			client.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
