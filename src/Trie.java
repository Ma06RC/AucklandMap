import java.util.*;


public class Trie {
	private HashMap<Character, Trie> children = new HashMap<Character, Trie>();
	private ArrayList<Road> roads = new ArrayList<Road>();
	
	public Trie() {
	}
	
	/**
	 * Takes a char parameter and adds a new TrieNode to the children
	 * HashMap as a Character/TrieNode key/value pair. This allows
	 * access to a TrieNode by its Character later on.
	 * 
	 * @param Character character
	 * @return TrieNode node
	 */
	public Trie addNode(Character character, Trie node) {
		Trie newNode = new Trie();
		node.children.put(character, newNode);
		return newNode;
	}
	
	/**
	 * Adds a Road to the Trie iteratively.
	 * Loops through each character in the roads name, creating a new TrieNode
	 * each time it finds a character that doesn't yet exist at the needed
	 * point in the Trie structure.
	 * 
	 * @param Road road
	 */
	public void addRoad(Road road) {
		String name = road.getStreetName();
		Trie node = this;
		for(int i = 0; i < name.length(); i++) {
			Character character = name.charAt(i);
			if(node.children.containsKey(character)) {
				node = node.children.get(character);
			} else {
				node = addNode(character, node);
			}
		}
		node.roads.add(road);
	}
	
	/**
	 * Checks if the Trie contains a road that matches the given string
	 * by looping through each character of the string and getting a 
	 * child TrieNode that matches the character. If one doesn't exist,
	 * the string doesn't exist either!
	 * 
	 * @param String name
	 * @return boolean
	 */
	public Trie containsRoad(String name) {
		Trie node = this;
		
		for(int i = 0; i < name.length(); i++) {
			Character character = name.charAt(i);
			if(node.children.containsKey(character)) {
				node = node.children.get(character);
			} else {
				return null;
			}
		}
		return node;
	}
	
	/**
	 * Recursively creates a list of all Roads that belong to this TrieNode
	 * and all of its children TrieNodes.
	 * 
	 * @return ArrayList<Road> roads
	 */
	public ArrayList<Road> getRoads() {
		ArrayList<Road> roads = this.roads;
		for(Trie childNode : this.children.values()) {
			roads.addAll(childNode.getRoads());
		}
		
		return roads;
	}
}
