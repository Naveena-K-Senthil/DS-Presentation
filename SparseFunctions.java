package sparsesimilarity;

import java.util.*;

public class SparseFunctions<K, V> 
{
	private HashMap<K, ArrayList<V>> default_map = new HashMap<K, ArrayList<V>>();
	
	/* Insert an item into the hashmap if it is not already present */
	public void put(K key, V item) 
	{
		if (!default_map.containsKey(key)) 
		{
			default_map.put(key, new ArrayList<V>());
		}
		default_map.get(key).add(item);
	}
	
	/* Get an item from the hashmap using the key values */
	public ArrayList<V> get(K key) 
	{
		return default_map.get(key);
	}

	/* Get the key values in the form of a set */
	public Set<K> keySet() 
	{
		return default_map.keySet();
	}
	
	@Override
	public String toString() 
	{
		return default_map.toString();
	}
	
       
}