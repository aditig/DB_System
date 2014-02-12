import java.util.ArrayList;

public class Table {
	private String name;
	private ArrayList<Page> pages = new ArrayList<Page>();
	private ArrayList<Attribute> attr = new ArrayList<Attribute>();
	
	Table (String tablename) {
		name = tablename;
		//System.out.println("Table name " + name);
	}
	
	public String getName() {
		return name;
	}
	
	public void addPage(int start, int size) {
		pages.add(new Page(name, start, size));
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

}
