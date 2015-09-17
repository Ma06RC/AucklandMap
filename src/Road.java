import java.util.*;
import java.awt.Graphics;
import java.awt.Color;


public class Road {
	private List<Segment> edges = new ArrayList<Segment>();
	private int roadId;
	private String streetName;
	private String city;
	private boolean oneWay; 
	private int speed;
	private int roadClass;
	private boolean notForCar;
	private boolean notForPede;
	private boolean notForBicy;
	private boolean selected; 
	
	public Road (String line){
		String[] values = line.split("\t");
		roadId = Integer.parseInt(values[0]);
		streetName = values[2];
		city = values[3];
		if(city.equals("-")){ //deletes if the city is "-"
			city = "";
		}
		oneWay = values[4].equals("1");
		speed = Integer.parseInt(values[5]);
		roadClass = Integer.parseInt(values[6]);
		notForCar = values[7].equals("1");
		notForPede = values[8].equals("1");
		notForBicy = values[9].equals("1");
	}
	
	public void getSelect(){
		this.selected = true; 
	}

	public void getUnSelect(){
		this.selected = false;
	}
	
	public int getId(){
		return this.roadId;
	}
	
	public String getStreetName(){
		return this.streetName;
	}
	
	public String getCity(){
		return this.city;
	}

	public String getStreetFullName(){
		if(this.city == ""){
			return this.streetName;
		}
		return (this.streetName+" "+ this.city);
	}
	
	public String toString(){
		return "Road:"+" "+this.getStreetFullName();
	}

	public boolean oneWay(){
		return this.oneWay;
	}

	public void addSegments(Segment e){
		this.edges.add(e);
	}
	
	public void draw(Graphics g, Location origin, double scale){
		for(Segment s: this.edges){
			s.draw(g, origin, scale, selected);
		}
	}

}
