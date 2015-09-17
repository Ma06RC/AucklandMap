import java.awt.Color;
import java.awt.Graphics;
import java.util.*;
import java.awt.Point;


public class Node {
	private int nodeID;
	private double lat;
	private double lon;
	private Location loc;
	private List<Segment> outNeighbours = new ArrayList<Segment>();
	private List<Segment> inNeighbours = new ArrayList<Segment>();
	private boolean select;

	private boolean visited;
	private Node pathFrom;
	private double cost;
	

	//	public Node(int node, double latitude, double longtitude ){
	//		this.nodeID = node;
	//		//this.street = s;
	//		this.lat = latitude;
	//		this.lon = longtitude;
	//	}
	public void getSelect(){
		select = true; 
		
	}

	public void getUnSelect(){
		select = false;
		System.out.println("Node: false");
	}

	public Node(String line){
		String [] values = line.split("\t");
		nodeID = Integer.parseInt(values[0]);
		lat = Double.parseDouble(values[1]);
		lon = Double.parseDouble(values[2]);

	}

	public Node(int id, Location loc ){
		this.nodeID = id;
		this.loc = loc;
	}

	public int getNodeId(){
		return this.nodeID;
	}

	public double getLat(){
		return this.lat;
	}

	public double getLon(){
		return this.lon;
	}

	public void addOutNeighbours(Segment e){
		outNeighbours.add(e);
	}

	public void addInNeighbours(Segment e){
		inNeighbours.add(e);
	}

	public Location getLocation(){
		return this.loc;
	}

	public List<Segment> getOutNeighbours(){
		return this.outNeighbours;
	}

	public List<Segment> getInNeighbours(){
		return this.inNeighbours;
	}
	public Location getLoc(){
		return this.loc;
	}
	
	public double heuristic(Location otherLoc){
		return this.loc.distance(otherLoc);
	}
	
	public boolean visited(){
		return this.visited;
	}
	
	public boolean visit(boolean visited){
		return this.visited = visited;
	}
	
	public void pathFrom(Node from){
		this.pathFrom = from;
	}
	
	public void cost(double costToHere){
		this.cost = costToHere;
	}
	
	public Node getPathFrom(){
		return this.pathFrom;
	}
	
	public double getCost(){
		return this.cost;
	}
	


	public void draw(Graphics g, Location origin, double scale ){
		Point p = loc.asPoint(origin, scale);
		int lat = p.x;
		int lon = p.y;
		
		if(select){
			g.setColor(Color.yellow);
			g.fillOval(lat, lon, 3, 3);
		}else{
			g.setColor(Color.BLUE);
			g.fillOval(lat, lon, 3, 3);
		}
	}


}
