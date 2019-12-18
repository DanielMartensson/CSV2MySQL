package se.danielmartensson.CSV2MySQL.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import se.danielmartensson.CSV2MySQL.entities.ITHBAEPulsBench;
import se.danielmartensson.CSV2MySQL.repositories.ITHBAEPulsBenchRepository;

@Component
@PropertySource("classpath:schedule.properties")
public class Schedule {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${schedule.ITHBAEPulsBenchIXPanelPathCSVFolder}")
	private String ITHBAEPulsBenchIXPanelPathCSVFolder;

	@Value("${schedule.downloadCSVFiles}")
	private boolean downloadCSVFiles;
	
	@Value("${schedule.enableMail}")
	private boolean enableMail;

	@Autowired
	private ITHBAEPulsBenchRepository iTHBAEPulsBenchRepository;

	@Autowired
	private FTPConnection fTPConnection;
	
	@Autowired
	private SendMail sendMail;

	/**
	 * Call this method for every minute
	 */
	@Scheduled(fixedDelayString = "${schedule.ITHBAEPulsBenchIXPanelIntervall}")
	public void ITHBAEPulsBenchLoadDatabase() {

		// Download the csv files
		if (downloadCSVFiles == true) {
			logger.info("Download files over FTP");
			fTPConnection.scannFolderOverFTP(ITHBAEPulsBenchIXPanelPathCSVFolder);
		}

		// Scan folder of all .csv files
		File folderPath = new File(ITHBAEPulsBenchIXPanelPathCSVFolder);

		// Only .csv files
		folderPath.list(takeOnlyFilesThatEndsWith(".csv"));

		// Create list
		File[] listedCSVFiles = folderPath.listFiles();

		// Check
		if (listedCSVFiles == null) {
			logger.info("Error: Cannot read file path to the CSV files!");
			return;
		}

		if (listedCSVFiles.length == 0) {
			logger.info("No CSV files where detected. Just continue");
			return;
		}

		// Sort on last modified
		Arrays.sort(listedCSVFiles, (f1, f2) -> {
			return new Date(f1.lastModified()).compareTo(new Date(f2.lastModified()));
		});

		// Load to the database
		logger.info("Loading files to the ITHBAEPulshBench");
		for (File CSVFile : listedCSVFiles) {
			// Begin to read each row
			try {
				BufferedReader br = new BufferedReader(new FileReader(CSVFile));
				String csvRow = br.readLine();
				
				while (csvRow != null) {
					ITHBAEPulsBench iTHBAEPulsBench = new ITHBAEPulsBench();
					boolean sendAlarm = iTHBAEPulsBench.fillFields(csvRow);
					if(sendAlarm == true && enableMail == true)
						sendMail.sendAlarmMail();
					iTHBAEPulsBenchRepository.save(iTHBAEPulsBench);
					csvRow = br.readLine(); // Read new row
				}

				br.close();
				CSVFile.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("Done");
		sendMail.setSendMailDate(""); // Reset for each CSV Batch

	}

	/**
	 * This returns a filter that ends with endPart
	 * 
	 * @param endPart
	 * @return
	 */
	private FilenameFilter takeOnlyFilesThatEndsWith(String endPart) {
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File f, String name) {
				return name.endsWith(endPart);
			}
		};
		return filter;
	}
}
