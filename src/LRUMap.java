import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class LRUMap<K,V> extends LinkedHashMap<K,V> {
    private int max_cap;

    public LRUMap(int initial_cap, int max_cap) {
        super(initial_cap, 0.75f, true);
        this.max_cap = max_cap;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
    	return size() > this.max_cap;
    }
    
    public V getEldestEntry() {
    	Set<Map.Entry<K, V>> entrySet = this.entrySet();
		Iterator<Map.Entry<K, V>> it = entrySet.iterator();
		if (it.hasNext())
			return it.next().getValue();
		return null;
    }
}