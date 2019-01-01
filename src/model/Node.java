package model;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class Node {
	private long Id;
	private BufferedReader fromNode;
	private PrintWriter toNode;
	
	public Node(long Id) {
		this.Id=Id;
	}

	public Node(long id, BufferedReader fromNode, PrintWriter toNode) {
		super();
		Id = id;
		this.fromNode = fromNode;
		this.toNode = toNode;
	}
	
}
