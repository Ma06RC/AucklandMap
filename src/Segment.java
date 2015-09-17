import java.awt.Graphics;
import java.awt.Point;
import java.util.*;
import java.awt.Color;


public class Segment {
	private Road road;
	private Node startNode;
	private Node endNode;
	private int roadId;
	private double length;
	private List <Location> loc = new ArrayList<Location>();
	//private Location location;

	public Segment(Road r, double l, Node s, Node e){
		this.road = r; 
		this.length = l;
		this.startNode = s;
		this.endNode = e;

	}
	public Segment (String line, Map<Integer, Road> roads, Map<Integer, Node> nodes){
		String[] values = line.split("\t");
		road = roads.get(Integer.parseInt(values[0]));
		length = Double.parseDouble(values[1]);
		startNode = nodes.get(Integer.parseInt(values[2]));
		endNode = nodes.get(Integer.parseInt(values[3]));

		for(int i = 4; i < values.length; i += 2){
			double lat = Double.parseDouble(values[i]);
			double lon = Double.parseDouble(values[i+1]);
			loc.add(Location.newFromLatLon(lat, lon));
		}
	}

	public Road getRoad(){
		return this.road;
	}

	public Node getStartNode(){
		return this.startNode;

	}

	public Node getEndNode(){
		return this.endNode;
	}

	public void setCoord(List<Location> l){
		this.loc = l;
	}
	
	public double getLength(){
		return this.length;
	}

//	public Segments reverse(){
//		Segments e = new Segments(road, length, endNode, startNode);
//		setCoord(this.loc);
//		return e;
//	}

	public void draw(Graphics g, Location origin, double scale, boolean selected){
		if(!loc.isEmpty()){
			if(selected){
				g.setColor(Color.YELLOW);
			}
			else{
				g.setColor(Color.BLACK);
			}
			Point p1 = loc.get(0).asPoint(origin, scale);
			for(int i = 1; i < loc.size(); i++){
				Point p2 = loc.get(i).asPoint(origin, scale);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
				p1=p2;
			}
		}
	}
}

