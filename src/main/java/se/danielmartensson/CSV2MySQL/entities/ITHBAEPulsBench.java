package se.danielmartensson.CSV2MySQL.entities;

import java.lang.reflect.Field;

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
	
	private String ProvObjekt;
	private String TemperaturTank;
	private String Styrtryck;
	private String ArbetstryckPump;
	private String DistansProvObjekt;
	// Add more here, notice that dataLine need to have the same size as the filelds minus id field
	
	/**
	 * This method will fill all the fields, except the id field
	 * @param CSVFile
	 */
	public void fillFields(String csvRow) {
		try {
			Field[] fields = ITHBAEPulsBench.class.getDeclaredFields();
			if(csvRow != null) {
				String[] dataLine = csvRow.split(",");
				for(int i = 1; i < fields.length; i++) {
					fields[i].setAccessible(true); // So we can write to the field
					fields[i].set(this, dataLine[i-1]); // Important with -1
				}
			}
		} catch ( IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}
