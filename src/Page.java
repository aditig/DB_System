import java.util.ArrayList;

public class Page {
	private int startId, endId, freeSpace;
	private String tableName;
	private ArrayList<String> records = new ArrayList<String>();
	
	Page (String name, int start, int pageSize) {
		startId = start;
		endId = start - 1;
		tableName = name;
		freeSpace = pageSize;
	}
	
	public void addRecord(String rec) {
		records.add(rec);
		endId += 1;
		freeSpace -= rec.length();
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public int getStartId() {
		return startId;
	}
	
	public int getEndId() {
		return endId;
	}
	
	public int getFreeSpace() {
		return freeSpace;
	}
	
	public void updateEndId(int end) {
		endId = end; 
	}
	
	public void updateFreeSpace(int free) {
		freeSpace = free;
	}
	
	public String getRecord(int id) {
		return records.get(id - startId);
	}
}