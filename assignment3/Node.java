package assignment3;

public class Node {
	public String value;
	public Node parent;
	public Integer count = 0;
	public Node(String value, Node parent) {
		this.value = value;
		this.parent = parent;
		this.count = null;
	}	
	public Node(String word, Integer count, Node parent) {
		this.value = word;
		this.parent = parent;
		this.count = count;
	}
}
