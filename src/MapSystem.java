import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import javax.swing.JTextArea;
import javax.swing.JTextField;



public class MapSystem extends GUI { 

	private Location origin = Location.newFromLatLon(-34.8, 172.6); 

	private double scale = 1;

	private Map <Integer, Road> roadsMap = new HashMap<Integer, Road>();

	private Map<Integer, Node> nodesMap = new HashMap<Integer, Node>();

	//	private Set<Integer> setOfRoads = new HashSet<Integer>();

	//	private Node nodeSelected = null;

	private Road roadSelected = null;

	private List<Road> road = null;

	private int mouseX, mouseY;

	private Trie trie;

	//for A* 
	public static Node startNode = null;
	public static Node goalNode = null;
	public static List<Segment> findPath;
	private boolean activateAStar = false;

	@Override
	protected void astar() {
		if(activateAStar){
			activateAStar = false;
			findPath = null;
			startNode = null;
			goalNode = null;
			System.out.println("not activated");
		}else{
			this.activateAStar = true;
			findPath = AStar(startNode, goalNode);	
			System.out.println("activated");
		}
	}

	@Override
	protected void deselect(){
		activateAStar = false;
		findPath = null;
		startNode = null;
		goalNode = null;
		startNode.getUnSelect();
		goalNode.getUnSelect();
	}

	public void onLoad(File n, File r, File s, File p){
		loadNode(n); 
		loadRoad(r);
		loadSegment(s);
	}


	public void redraw(Graphics g){
		//g.setColor(Color.black);
		//for (Node node : nodesMap.values()){
		for (Road road : this.roadsMap.values()){ 
			road.draw(g,origin,scale);
		}
		//}
		for(Node node : nodesMap.values()){
			node.draw(g, origin, scale);
		}

		if(startNode != null){
			g.setColor(Color.YELLOW);
			startNode.draw(g, origin, scale);
		}

		if(goalNode != null){
			g.setColor(Color.RED);
			goalNode.draw(g, origin, scale);
		}

		if(findPath != null){
			for(Segment s : findPath){
				s.draw(g, origin, scale,true);
				System.out.println("drawing");
			}
		}
	}

	public void onClick(MouseEvent e){
		if(startNode==null){
			startNode = findNode(e.getPoint());

		}
		else {
			goalNode = findNode(e.getPoint());

		}


		//		Point p = e.getPoint();
		//		Location loc = Location.newFromPoint(p, origin, scale);
		//		JTextArea printText =  getTextOutputArea();
		//
		//		if(nodeSelected != null){ // resets the selected node if going to select another node
		//			nodeSelected.getUnSelect();
		//			nodeSelected = null;
		//			for(Integer r: this.setOfRoads){
		//				this.roadsMap.get(r.intValue()).getUnSelect();
		//			}
		//			setOfRoads.clear();
		//		}
		//		for(Node node: nodesMap.values()){
		//			if(loc.isClose(node.getLocation(), 1)){
		//				nodeSelected = node;
		//				nodeSelected.getSelect();
		//				for(Segment in : this.nodeSelected.getInNeighbours()){
		//					this.setOfRoads.add(in.getRoad().getId());
		//					printText.setText("RoadId: " + this.setOfRoads +"\n");
		//					printText.append("Road: "+ in.getRoad().getStreetName());
		//				}
		//				for(Segment out : this.nodeSelected.getOutNeighbours()){
		//					this.setOfRoads.add(out.getRoad().getId());
		//					printText.setText("RoadId: " + this.setOfRoads +"\n");
		//					printText.append("Road: "+ out.getRoad().getStreetName());
		//				}
		//				for(Integer r: this.setOfRoads){
		//					this.roadsMap.get(r.intValue()).getSelect();
		//				}
		//				break;
		//			}
		//		}
	}

	public void onDragged(MouseEvent e){
		origin = origin.moveBy(-(e.getX() - mouseX)/scale, -(mouseY - e.getY())/scale);
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}

	public void onPressed(MouseEvent e){
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}

	public void onSearch(){
		if(trie == null){
			return;
		}
		trie = new Trie();
		String query = getSearchBox().getText();
		// Check that the query isn't empty! Searching on an empty string
		// should clear the currently 'active' roads
		if(!query.equals("")) {
			Trie containsRoad = this.trie.containsRoad(query);
			if(containsRoad != null) {
				this.road = containsRoad.getRoads();
			}
		} else {
			this.road = null;
		}

		for(Road r : this.roadsMap.values()){
			if(r.getStreetName().contains((CharSequence) road))
				roadSelected = r;
			roadSelected.getSelect();
			System.out.println("TRUE");
		}

		//		JTextField text = getSearchBox();
		//		String getText = text.getText();
		//		if(roadSelected != null){
		//			roadSelected.getUnSelect();
		//			roadSelected = null;
		//		}
		//		for(Road r : this.roadsMap.values()){
		//			if(r.getStreetName().contains(getText)){
		//				roadSelected = r;
		//				roadSelected.getSelect();
		//			}
		//
		//		}
	}	



	public void onMove(Move m){
		switch (m){
		case ZOOM_IN:
			scale = scale * 2;
			break;
		case ZOOM_OUT:
			if(scale < 1) scale = 1;
			scale = scale/2;
			break;
		case NORTH:
			origin = origin.moveBy(0, 20/(scale));
			break;
		case SOUTH:
			origin = origin.moveBy(0, -20/(scale));
			break;
		case EAST:
			origin = origin.moveBy(-20/(scale), 0);
			break;
		case WEST:
			origin = origin.moveBy(20/(scale), 0);
			break;
		}	
	}


	public void loadNode(File n){
		try{
			BufferedReader nodesData = new BufferedReader(new FileReader(n));
			String lines;

			while((lines = nodesData.readLine()) != null ){
				String [] values = lines.split("\t");
				int nodeID = Integer.parseInt(values[0]);
				double lat = Double.parseDouble(values[1]);
				double lon = Double.parseDouble(values[2]);
				Location latlon = Location.newFromLatLon(lat, lon);
				Node nodeClass = new Node(nodeID, latlon);
				this.nodesMap.put(nodeClass.getNodeId(), nodeClass);
			}

		}catch(IOException e){ 
			e.printStackTrace();
			System.out.println("Error while reading database file: " + e.getMessage());
		}
	}

	public void loadRoad(File r){
		try{
			BufferedReader roadsData = new BufferedReader(new FileReader(r));
			roadsData.readLine(); // throws the first line
			String lines;
			while((lines = roadsData.readLine()) != null ){
				Road road = new Road(lines);
				roadsMap.put(road.getId(), road);
				//this.add(road.getStreetName());
			}

		}catch(IOException e){ 
			e.printStackTrace();
			System.out.println("Error while reading database file: " + e.getMessage());
		}
	}

	public void loadSegment(File s){
		try{
			BufferedReader segmentsData = new BufferedReader(new FileReader(s));
			segmentsData.readLine(); // throws the first line
			String lines;
			while((lines = segmentsData.readLine()) != null ){
				//String line = segmentsData.readLine();
				Segment segment = new Segment(lines, roadsMap, nodesMap);
				Node node1 = segment.getStartNode();
				Node node2 = segment.getEndNode();
				node1.addOutNeighbours(segment);
				node2.addInNeighbours(segment);
				Road road = segment.getRoad();
				road.addSegments(segment);

				//				if(!road.oneWay()){ //check if its not a one way road
				//					Segments revEdge = segment.reverse(); //reverse the start and node and also the coordinates
				//					node2.addOutNeighbours(revEdge); //this means node2 can go out
				//					node1.addInNeighbours(revEdge); // node1 can go in
				//				}
			}

		}catch(IOException e){ 
			e.printStackTrace();
			System.out.println("Error while reading database file: " + e.getMessage());
		}
	}

	private Node findNode(Point point) {
		Location mousePlace = Location.newFromPoint(point, origin, scale);
		Node closest = null;
		double mindist = Double.POSITIVE_INFINITY;
		for(Node node : nodesMap.values()){
			double dist = node.getLoc().distance(mousePlace);
			if(dist<mindist){
				mindist = dist;
				closest = node;
			}
		}
		return closest;
	}

	public List<Segment> AStar(Node startNode, Node goalNode){
		for(Node node : nodesMap.values()){
			node.visit(false);
			node.pathFrom(null);
		}
		List<Segment> thePath = new ArrayList<Segment>();
		PriorityQueue<AStar> fringe = new PriorityQueue<AStar>();
		fringe.offer(new AStar(startNode, null, 0, startNode.heuristic(goalNode.getLoc())));

		while(!fringe.isEmpty()){
			AStar aStarNode = fringe.poll(); //dequeue
			Node n = aStarNode.getStartNode();

			if(!n.visited()) {
				n.visit(true);
				n.pathFrom(aStarNode.getGoalNode());
				n.cost(aStarNode.getCostSoFar());
				if(n == goalNode){
					Node backTrack = goalNode;
					while(backTrack != startNode ){
						for(Segment s : backTrack.getInNeighbours()){
							if(s.getStartNode() == backTrack.getPathFrom()){
								backTrack = backTrack.getPathFrom();
								thePath.add(s);

							}
						}
					}
					break;
				}

				for(Segment edge : n.getOutNeighbours()){
					Node neigh = edge.getEndNode();
					if(!neigh.visited()){
						double costToNeigh = aStarNode.getCostSoFar() + edge.getLength();
						double estTotal = costToNeigh + neigh.heuristic(goalNode.getLoc());
						fringe.add(new AStar(neigh,n,costToNeigh,estTotal));
					}
				}
			}
		}

		//Collections.reverse(thePath);
		return thePath;
	}

	public static void main(String[] args) {
		new MapSystem();

	}

}
