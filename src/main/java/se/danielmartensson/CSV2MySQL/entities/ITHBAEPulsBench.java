package se.danielmartensson.CSV2MySQL.entities;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ITHBAEPulsBench {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private float ProvobjektTryck_BAR;
	private float TemperaturTank_C;
	private float Styrtryck_BAR;
	private float ArbetstryckPump_BAR;
	private float DistansProvObjekt_MM;
	private float Tryckbegransare_BAR;
	private String Loggnummer;
	private int Samplingstid_10MS;
	private float Stigtid_MS;
	private float DifferanseStigtid_MS;
	private float DifferanseDistans_MM;
	private long GjordaPulser_;
	private boolean LarmProvning;
	private String Datum_YYYY_MM_DD_HH_MM_SS;
	
	// Add more here, notice that dataLine need to have the same size as the filelds minus id field
	
	/**
	 * This method will fill all the fields, except the id field
	 * @param CSVFile
	 */
	public boolean fillFields(String csvRow) {
		try {
			if(csvRow != null) {
				String[] dataRow = csvRow.split(",");
				SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				String time = spf.format(date);
				ProvobjektTryck_BAR = Float.parseFloat(dataRow[0]);
				TemperaturTank_C = Float.parseFloat(dataRow[1]);
				Styrtryck_BAR = Float.parseFloat(dataRow[2]);
				ArbetstryckPump_BAR = Float.parseFloat(dataRow[3]);
				DistansProvObjekt_MM = Float.parseFloat(dataRow[4]);
				Tryckbegransare_BAR = Float.parseFloat(dataRow[5]);
				
				Loggnummer = dataRow[6];
				
				Samplingstid_10MS = Integer.parseInt(dataRow[7]);
				
				Stigtid_MS = Float.parseFloat(dataRow[8]);
				DifferanseStigtid_MS = Float.parseFloat(dataRow[9]);
				DifferanseDistans_MM = Float.parseFloat(dataRow[10]);
				
				GjordaPulser_ = Long.parseLong(dataRow[11]);
				
				LarmProvning = Boolean.parseBoolean(dataRow[12]);
				
				Datum_YYYY_MM_DD_HH_MM_SS = time;
			}
			
		} catch ( IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		// Check if we are going to send a alarm
		if(LarmProvning == true)
			return true;
		else
			return false;
	}
	
}
