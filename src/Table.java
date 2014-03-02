import java.util.ArrayList;

public class Table {
	private String name;
	private int numPages, numRecords;
	private ArrayList<Page> pages = new ArrayList<Page>();
	private ArrayList<Attribute> attr = new ArrayList<Attribute>();
	
	Table (String tablename) {
		name = tablename;
		numPages = 0;
		numRecords = 0;
		//System.out.println("Table name " + name);
	}
	
	public String getName() {
		return name;
	}
	
	public void addPage(int start, int size) {
		pages.add(new Page(name, start, size));
		numPages++;
	}
	
	public Page getPage(int recordId) {
		int index = -1;
		for (Page pg : pages) {
			if (recordId >= pg.getStartId() && recordId <= pg.getEndId()) {
				index = pages.indexOf(pg);
				break;
			}
		}
		return pages.get(index);
	}
	
	public Page getLastPage() {
		return pages.get(pages.size() - 1);
	}
	
	public void addAttr(Attribute a) {
		attr.add(a);
	}

	public int isColumn(String name) {
		for (Attribute a : attr) {
			if (name.equalsIgnoreCase(a.getName())) {
				return attr.indexOf(a);
			}
		}
		return -1;
	}
	
	public Attribute getColumn (String name) {
		for (Attribute a : attr) {
			if (name.equalsIgnoreCase(a.getName())) {
				return a;
			}
		}
		return null;
	}
	
	public Attribute getColumn (int i) {
		if(i >= attr.size())
			return null;
		return attr.get(i);
	}
	
	public int numColumns () {
		return attr.size();
	}

	
	public int getNumPages() {
		return numPages;
	}

	public void addRecord (String record, int pageSize, MainMemory m) {
		Page pg = getLastPage();
		if(record.length() >= pg.getFreeSpace()) {
			addPage(pg.getEndId() + 1, pageSize);
			pg = getLastPage();
		}
		m.getPage(pg);
		pg.addRecord(record);
		numRecords++;
	}
	
	public int getNumRecords() {
		return numRecords;
	}

	public String getRecord(int recordId, MainMemory m) {
		Page pg = getPage(recordId);
		//System.out.println(m.getPage(pg));
		m.getPage(pg);
		return pg.getRecord(recordId);
	}
	
	public void setNumRecords(int num) {
		numRecords = num;
	}
}
