package dao.dm;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import beans.DM;

public class DmDAO {
	static final String CSV_FILE = "dms.csv";
	private Map<String, DM> dms = new HashMap<>();
	private String path;
	
	public DmDAO(String contextPath) {
		this.path = contextPath;
		readFile();
	}
	
	public Collection<DM> findAll() {
		return dms.values();
	}

	public DM findById(String id) {
		for (DM dm : findAll()) {
			if (dm.getId().equals(id)) {
				return dm;
			}
		}
		return null;
	}
	
	void readFile() {
		try(CSVReader csvr = new CSVReader(new FileReader(this.path + "/resources/" + CSV_FILE), ';', CSVWriter.NO_QUOTE_CHARACTER, 1)) {
			ColumnPositionMappingStrategy<DmRepo> strategy = new ColumnPositionMappingStrategy<>();
			strategy.setType(DmRepo.class);
			String[] columns = new String[] {"id","content","dateTime","senderID","recieverID"};
			strategy.setColumnMapping(columns);
			CsvToBean<DmRepo> csv = new CsvToBean<>();
			List<DmRepo> tempDms = csv.parse(strategy, csvr);
			
			for (DmRepo tempDm : tempDms) {
				LocalDateTime dateTime = getDateTime(tempDm.getDateTime());
				DM dm = new DM(tempDm.getId(), tempDm.getContent(), dateTime, tempDm.getSenderID(), tempDm.getRecieverID());
				dms.put(dm.getId(), dm);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private LocalDateTime getDateTime(String s) {
		String date = s.split(" ")[0];
		String time = s.split(" ")[1];
		return LocalDateTime.of(Integer.parseInt(date.split("-")[0]), 
								Integer.parseInt(date.split("-")[1]), 
								Integer.parseInt(date.split("-")[2]), 
								Integer.parseInt(time.split(":")[0]), 
								Integer.parseInt(time.split(":")[1]), 
								Integer.parseInt(time.split(":")[2]));
		
	}
}
