public class MainMemory {
	private int numPages;
	private LRUMap<Page, Integer> lru;
	
	MainMemory(int num) {
		numPages = num;
		lru = new LRUMap<Page, Integer>(0, numPages);
	}
	
	public String getPage(Page pg) {
		if(lru.containsKey(pg)) {
			lru.get(pg);
			 return "HIT";
		}
		else {
			int key = insertPage(pg);
			return "MISS " + key;
		}
	}
	
	protected int insertPage(Page pg) {
		int key = -1;
		if(lru.size() < numPages) {
			key = lru.size();
			lru.put(pg, key);
		}
		else {
			key = lru.getEldestEntry();
			lru.put(pg, key);
		}
		return key;
	}
}
