import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class DBSystem {
	private int pageSize, numPages;
	private String path;
	private ArrayList<Table> tables = new ArrayList<Table>();	
	private MainMemory m;
	
	public void readConfig(String configFilePath) {
		File file = new File(configFilePath);
		Scanner sc = null;
		String colName;
		
		try {
			sc = new Scanner(file);
			//TODO PAGESIZE or PAGE_SIZE??
			sc.next("PAGE_SIZE");
			pageSize = sc.nextInt();
			
			sc.next("NUM_PAGES");
			numPages = sc.nextInt();

			sc.next("PATH_FOR_DATA");
			path = sc.next();
			
			while(sc.hasNext("BEGIN")) {
				sc.next("BEGIN");
				Table t = new Table(sc.next());
				tables.add(t);
				while(!(sc.hasNext("END"))){
					colName = sc.next();
					//System.out.println(colName.substring(0,colName.length()-1));
					//TODO create <table>.data file
					t.addAttr(new Attribute(colName.substring(0, colName.length() - 1), sc.next()));
				}
				sc.next("END");
			}
			
			m = new MainMemory(numPages);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if (sc !=  null) {
				sc.close();
			}
		}
	}

	public void populateDBInfo() {
		File file;
		Scanner sc = null;
		int size, start, count;
		String l;
		
		for (Table t : tables) {
			size = 0;
			start = 0;
			count = 0;
			file = new File(path + t.getName() + ".csv");
			try {
				sc = new Scanner(file);
				while(sc.hasNextLine()) {
					l = sc.nextLine();
					if(count == 0 && l.length() > 0)
						t.addPage(start, pageSize);
					if(l.length() + size < pageSize) {
						size += l.length();
						t.getLastPage().addRecord(l);
					}
					else {
						//new page for table
						t.addPage(count, pageSize);
						t.getLastPage().addRecord(l);
						start = count;
						size = l.length();
					}
					count++;
				}
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			finally {
				if (sc !=  null) {
					sc.close();
				}
			}	
		}
	}

	
	public String getRecord (String tableName, int recordId) {
		int index = -1;
		for (Table t : tables) {
			if (t.getName().equals(tableName)) {
				index = tables.indexOf(t);
				break;
			}
		}
		
		Page pg = tables.get(index).getPage(recordId);
		//System.out.println(pg.getTableName() + " " + pg.getStartId());
		System.out.println(m.getPage(pg));
		return pg.getRecord(recordId);
	}
	
	
	public void insertRecord(String tableName, String record) throws IOException {
		int index = -1;
		Page pg;
		for (Table t : tables) {
			if(t.getName().equals(tableName)) {
				index = tables.indexOf(t);
				break;
			}
		}
		
		pg = tables.get(index).getLastPage();
		if(record.length() >= pg.getFreeSpace()) {
			tables.get(index).addPage(pg.getEndId() + 1, pageSize);
			pg = tables.get(index).getLastPage();
		}
		m.getPage(pg);
		pg.addRecord(record);
		FileWriter f = new FileWriter(path + tableName + ".csv",true);
        f.append(record + '\n');
        f.close();
	}
}
