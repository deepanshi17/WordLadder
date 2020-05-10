package assignment3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class Adjacency {
	public static HashMap<String, LinkedList<String>> adjacencyLists = new HashMap<>();

	/**
	 * 
	 * @param dictSet set of words returned by makeDictionary() from word list file 
	 * @return Linked Lists for each word in the dictionary connecting each word to its off-by-one partners
	 */
	public static void createAdjacencyList(Set<String> dictSet){
		//Convert dictSet to an ArrayList so that we can iterate through it
		ArrayList<String> dictList = new ArrayList<String>(dictSet);
		int dictSize = dictList.size();
		//Create adjacency list of linked lists
		for(int i = 0; i < dictSize; i++) {
			LinkedList<String> neighbors = new LinkedList<>();
			//We create a linked list called neighbors to hold partners
			for(int j = 0; j < dictSize; j++) {
				//We iterate through dictList a second time seeking partners and adding them to neighbors when found.
				if(diffIsOne(dictList.get(i), dictList.get(j)))
					neighbors.add(0, dictList.get(j));
			}
			//Lastly we add neighbors to our adjacency list.
			adjacencyLists.put(dictList.get(i), neighbors);
		}
		
//		//TEST OF ADJACENCY LISTS, PRINTS FIRST 10 LINKED LISTS
//		System.out.println(adjacencyLists.size());
//		Iterator<Entry<String, LinkedList<String>>> j = adjacencyLists.entrySet().iterator();
//		for(int i = 1; i <= 10; i++)
//			System.out.println(i + ". " + j.next());
//		System.out.println();

	}
	
	/**
	 * 
	 * @param compare first String to be compared
	 * @param compareTo second String to be compared
	 * @return boolean, true if compare and compareTo differ by only one character
	 */
	public static boolean diffIsOne(String compare, String compareTo) {
		//Convert both strings to char arrays so we can iterate through both.
		char[] compareArray = compare.toCharArray();
		char[] compareToArray = compareTo.toCharArray();
		int diff = 0;
		//Look at each letter and increase difference count for each pair of different characters.
		//The "(diff <= 1)" statement keeps the loop from continuing further than necessary.
		//Once diff becomes 2 we have enough information. 
		for(int i = 0; i < compareTo.length(); i++) {
			if((diff <= 1) && (compareArray[i] != compareToArray[i]))
				diff++;
		}
		if(diff == 1)
			return true;
		else
			return false;	
	}
	
	/**
	 * 
	 * @param key should be the parent word being sought in the adjacency list
	 * @return Returns linked list in adjacencyLists corresponding to key.
	 */
	public static LinkedList<String> getNeighbors(String key){
		return adjacencyLists.get(key);
	}
}
