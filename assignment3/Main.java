/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Summer 2019
 */

package assignment3;

import java.util.*;
import java.io.*;

public class Main {

	// static variables and constants only here.

	public static void main(String[] args) throws Exception {
		initialize();

		Scanner kb; // input Scanner for commands
		PrintStream ps; // output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps); // redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out; // default output to Stdout
		}
		System.out.println("Input words: ");
		ArrayList<String> response = parse(kb);
		while (!response.isEmpty()) {
			ArrayList<String> ladder = getWordLadderDFS(response.get(0), response.get(1));
			printLadder(ladder);
			System.out.println(ladder.size());
			System.out.println("");
			System.out.println("Input words: ");
			response = parse(kb);
		}

	}

	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests. So call it
		// only once at the start of main.
		Adjacency.createAdjacencyList(makeDictionary());
	}

	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. If command
	 *         is /quit, return empty ArrayList.
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		// inputs is the ArrayList where the input words are stored
		ArrayList<String> inputs = new ArrayList<String>();

		// temporary place holders for the word ladder inputs
		String start, end;
		String command = keyboard.next();
		String next_command = keyboard.next();

		// if either word is the quit command, return an empty ArrayList
		if (command.equals("/quit") || next_command.equals("/quit")) {
			inputs.clear();
			return inputs;
		}

		// both words are valid inputs to word ladder, store in ArrayList consecutively
		else {
			start = command;
			end = next_command;
			inputs.add(0, start);
			inputs.add(1, end);
		}
		return inputs;
	}

	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		// Returned list should be ordered start to end. Include start and end.
		// If ladder is empty, return list with just start and end.

		// ladder is the final list of strings that make up the valid word ladder
		ArrayList<String> ladder = new ArrayList<String>();

		// creating a node for the root of the tree
		Node starting = new Node(start, null);

		// DFS keeps track of the path taken using stack (LIFO)
		Stack<Node> s = new Stack<Node>();

		// Set of strings that have already been visited to avoid loops
		Set<String> visited = new HashSet<String>();

		// find is a helper function that will handle the recursive calls
		// if is_Found returns true, there is a valid word ladder
		// pop values off stack and store in ladder (in reverse order since stack is
		// LIFO)
		// if is_Found returns false, simply store end and start word in ladder and
		// return
		boolean is_Found = find(starting, end, visited, s);
		if (is_Found) {
			ArrayList<String> result = new ArrayList<>();
			int i = s.size() - 1;
			while (!s.isEmpty()) {
				result.add(s.pop().value);
			}
			while(i >= 0) {
				ladder.add(result.get(i).toLowerCase());
				i--;
			}
			
		} else {
			ladder.add(start);
			ladder.add(end);
		}
		return ladder;
	}

	public static ArrayList<String> getWordLadderBFS(String end, String start) {
		start = start.toUpperCase();
		end = end.toUpperCase();
		// first index in string array is word, second is value
		Queue<Node> q = new LinkedList<Node>();
		Set<String> discovered = new HashSet<String>();
		Node startingWord = new Node(start, null);

		q.add(startingWord);
		discovered.add(startingWord.value);
		Node ladderNode = null;

		headloop: while (!q.isEmpty()) {
			Node head = q.poll();
			if (head.value.equals(end)) {
				ladderNode = q.peek();
				break;
			}
			LinkedList<String> neighbors = Adjacency.getNeighbors(head.value);
			for (int i = 0; neighbors != null && i < neighbors.size(); i++) {
				if (!discovered.contains(neighbors.get(i))) {
					Node next = new Node(neighbors.get(i), head);
					discovered.add(neighbors.get(i));
					q.add(next);
					if (neighbors.get(i).equals(end)) {
						ladderNode = next;
						break headloop;
					}
				}
			}
		}

		ArrayList<String> ladder = new ArrayList<String>();
		if (ladderNode == null) {
			ladder.add(end);
			ladder.add(start);
			return ladder;
		}
		while (ladderNode != null) {
			ladder.add(ladderNode.value);
			ladderNode = ladderNode.parent;
		}

		return ladder;
	}

	public static void printLadder(ArrayList<String> ladder) {
		String start = ladder.get(0).toLowerCase();
		String end = ladder.get(ladder.size() - 1).toLowerCase();
		if (ladder.size() == 2)
			System.out.println("no word ladder can be found between " + start + " and " + end);
		else {
			System.out
					.println("a " + (ladder.size() - 2) + "-word ladder exists between " + start + " and " + end + ".");
			for (String word : ladder)
				System.out.println(word);
		}
	}
	// TODO
	// Other private static methods here

	private static boolean find(Node start, String value, Set<String> visited, Stack<Node> stack) {

		// edge case
		if (start == null)
			return false;

		// add word to visited and push parent-child node to stack
		String startVal = start.value.toUpperCase();
		String endVal = value.toUpperCase();
		visited.add(startVal);
		stack.push(start);

		// end condition
		if (start.value.equals(endVal))
			return true;

		// traverse through unvisited neighbors, recursing through each branch until
		// last leaf
		else {
			LinkedList<String> neighbors = Adjacency.getNeighbors(startVal);
			ArrayList<Node> ordered_neighbors = getClosestNeighbors(neighbors, endVal);
			for (int i = 0; ordered_neighbors != null && i < ordered_neighbors.size(); i++) {
				if (!(visited.contains (ordered_neighbors.get(i).value))) {
					Node nextWord = new Node(ordered_neighbors.get(i).value, start);
					boolean is_found = find(nextWord, value, visited, stack);
					if (is_found)
						return true;
				}
			}

			// reached last leaf of path without reaching end word, so pop this node off
			// stack
			stack.pop();
			return false;
		}
	}

	private static ArrayList<Node> getClosestNeighbors(LinkedList<String> neighbors, String endVal) {
		ArrayList<Node> similarities = new ArrayList<>();
		char[] endChar = endVal.toCharArray();
		for (int i = 0; neighbors != null && i < neighbors.size(); i++) {
			char[] neighborChar = neighbors.get(i).toCharArray();
			int count = 0;
			for (int j = 0; j < neighbors.get(i).length(); j++) {
				if (neighborChar[j] == endChar[j])
					count++;
			}
			similarities.add(new Node(neighbors.get(i), count, null));
		}
		int j = 0;
		int key;
		for (int i = 0; similarities != null && i < similarities.size(); i++) {
			key = similarities.get(i).count;
			j = i - 1;
			while (j >= 0 && key < similarities.get(j).count) {
				similarities.set(j + 1, similarities.get(j));
				j -= 1;
			}
			similarities.set(j + 1, similarities.get(i));
		}
		
		return similarities;
	}

	/* Do not modify makeDictionary */
	public static Set<String> makeDictionary() {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner(new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
}
