package se.danielmartensson.CSV2MySQL.components;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.apache.commons.io.IOUtils;
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
	
	@Value("${ftp.folderPath}")
	private String folderPath;
	
	private FTPClient client;
	
	public void scannFolderOverFTP(String downloadPath) {

		client = new FTPClient();

		try {
			// Connect
			client.connect(connectionPath);
			client.login(username, password);
			client.enterLocalPassiveMode();
			client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
			client.setFileTransferMode(FTP.BINARY_FILE_TYPE);

			// Download
			if (client.isConnected()) {
				FTPFile[] ftpFiles = client.listFiles(folderPath);
				for (FTPFile fileFTP : ftpFiles) {
					String localFile = "";
					if(System.getProperty("os.name").contains("Win") == true) {
						localFile = downloadPath + "\\" + fileFTP.getName(); // Windows
					}else {
						 localFile = downloadPath + "/" + fileFTP.getName(); // Unix
					}
					String remoteFile = folderPath + "/" + fileFTP.getName(); 
					OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFile));
		            client.retrieveFile(remoteFile, outputStream);
		            outputStream.flush();
		            outputStream.close();
					client.deleteFile(folderPath + "/" + fileFTP.getName());
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
