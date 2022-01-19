package dao.picture;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import beans.Picture;

public class PictureDAO {
	static final String CSV_FILE = "pictures.csv";
	private Map<String, Picture> pictures = new HashMap<>();
	private String path;
	
	public PictureDAO(String contextPath) {
		this.path = contextPath;
		readFile();
	}
	
	public Collection<Picture> findAll() {
		return pictures.values();
	}

	public Picture findById(String id) {
		for (Picture picture : findAll()) {
			if (picture.getId().equals(id)) {
				return picture;
			}
		}
		return null;
	}
	
	void readFile() {
		try(CSVReader csvr = new CSVReader(new FileReader(this.path + "/resources/" + CSV_FILE), ';', CSVWriter.NO_QUOTE_CHARACTER, 1)) {
			ColumnPositionMappingStrategy<PictureRepo> strategy = new ColumnPositionMappingStrategy<>();
			strategy.setType(PictureRepo.class);
			String[] columns = new String[] {"id","authorID","picture","description","posted","commentIDs","deleted"};
			strategy.setColumnMapping(columns);
			
			CsvToBean<PictureRepo> csv = new CsvToBean<>();
			List<PictureRepo> tempPictures = csv.parse(strategy, csvr);
			
			for (PictureRepo temp : tempPictures) {
				LocalDateTime posted = getDateTime(temp.getPosted());
				ArrayList<String> commentIDs = getList(temp.getCommentIDs());
				Picture picture = new Picture(temp.getId(), temp.getAuthorID(), temp.getPicture(), temp.getDescription(), posted, commentIDs, temp.isDeleted());
				pictures.put(temp.getId(), picture);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private ArrayList<String> getList(String s) {
		ArrayList<String> elems = new ArrayList<String>();
		for (String elem : s.split("\\|")) {
			elems.add(elem);
		}
		return elems;
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
