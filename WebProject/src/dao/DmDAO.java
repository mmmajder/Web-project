package dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import beans.DM;

public class DmDAO {
	static final String CSV_FILE = "dms.csv";
	private Map<String, DM> dms = new HashMap<>();
	private String path;

	public static void main(String[] args) {
		DmDAO dao = new DmDAO("src");
		dao.addDM(new DM(dao.generateId(), "Cao", LocalDateTime.now(), "prvi", "drugi"));
		// dao.writeFile();
	}

	public DmDAO(String contextPath) {
		this.path = contextPath;
		readFile();
	}

	public DmDAO() {
		this.path = "";
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

	public void addDM(DM dm) {
		dms.put(dm.getId(), dm);
		writeFile();
	}

	// DM000000001
	public String generateId() {
		StringBuilder sb = new StringBuilder();
		String number = String.format("%09d", findAll().size() + 1);
		sb.append("DM");
		sb.append(number);
		return sb.toString();
	}

	void writeFile() {
		try {
			System.out.println("stigao");
			OutputStream os = new FileOutputStream(this.path + "/resources/" + CSV_FILE);
			CSVWriter writer = new CSVWriter(new PrintWriter(new OutputStreamWriter(os, "UTF-8")), ';',
					CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
			List<String[]> data = new ArrayList<String[]>();
			data.add(new String[] { "id", "content", "dateTime", "senderID", "recieverID" });
			for (DM dm : findAll()) {
				data.add(new String[] { dm.getId(), dm.getContent(), dm.getDateTime().toString().replace('T', ' '),
						dm.getSender(), dm.getReciever() });
				System.out.println(dm);
			}
			writer.writeAll(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void readFile() {
		try (CSVReader csvr = new CSVReader(
				new InputStreamReader(new FileInputStream(this.path + "/resources/" + CSV_FILE), "UTF-8"), ';', '\'',
				1);) {
			String[] nextLine;
			// String[] columns = new String[]
			// {"id","content","dateTime","senderID","recieverID"};
			while ((nextLine = csvr.readNext()) != null) {
				LocalDateTime dateTime = getDateTime(nextLine[2]);
				DM dm = new DM(nextLine[0], nextLine[1], dateTime, nextLine[3], nextLine[4]);
				System.out.println(dm);
				dms.put(dm.getId(), dm);
			}
			csvr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private LocalDateTime getDateTime(String s) {
		String date = s.split(" ")[0];
		String time = s.split(" ")[1];
		return LocalDateTime.of(Integer.parseInt(date.split("-")[0]), Integer.parseInt(date.split("-")[1]),
				Integer.parseInt(date.split("-")[2]), Integer.parseInt(time.split(":")[0]),
				Integer.parseInt(time.split(":")[1]));

	}
}
