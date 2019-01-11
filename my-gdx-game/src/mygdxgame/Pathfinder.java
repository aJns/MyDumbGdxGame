package mygdxgame;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Pathfinder {
	private ArrayList<Node> nodeList;
	private ArrayList<Node> openSet;
	private ArrayList<Node> closedSet;
	private Node currentNode;
	private int nodeDistance = 40;
	private float tentativeGScore = 0;
	
	public Pathfinder(int width, int height) {
		if(nodeList == null) {
			createNodeList(width, height);
		}
		openSet = new ArrayList<Node>();
		closedSet = new ArrayList<Node>();
		currentNode = new Node(0, 0);
	}
	public void createNodeList(int width, int height) {
		int nodeDist = 10;
		int w = width / nodeDist;
		int h = height / nodeDist;
		nodeList = new ArrayList<Node>();
		for(int i=0; i<=w ;i++) {
			for(int j=0; j<=h; j++) {
				int x = i * nodeDist;
				int y = j * nodeDist;
				Node node = new Node(x,y);
				nodeList.add(node);
			}
		}
		setNeighbors();
	}
	private void setNeighbors() {
		for(Node aNode : nodeList) {
			for(Node bNode : nodeList) {
				if(aNode != bNode) {
					aNode.checkNeighbor(bNode, nodeDistance);
				}
			}
		}
	}
	private Node calculateStartNode(Vector2 start) {
		Node startNode = nodeList.get(0);
		float currentDistance = 0;
		float newDistance = 0;
		currentDistance = startNode.distanceTo(start);
		for(Node node : nodeList) {
			newDistance = node.distanceTo(start);
			if(newDistance < currentDistance && !node.isBlocked) {
				startNode = node;
				currentDistance = newDistance;
			}
		}
		startNode.isStart = true;
		return startNode;
	} 
	private Node calculateEndNode(Vector2 finish) {
		Node endNode = nodeList.get(0);
		float currentDistance = 0;
		float newDistance = 0;
		currentDistance = endNode.distanceTo(finish);
		for(Node node : nodeList) {
			newDistance = node.distanceTo(finish);
			if(newDistance < currentDistance && !node.isBlocked) {
				endNode = node;
				currentDistance = newDistance;
			}
		}
		endNode.isFinish = true;
		return endNode;
	}
	private Node getClosestNode(ArrayList<Node> list) {
		Node closestNode = list.get(0);
		float leastDistance = closestNode.estimatedCost;
		for(Node node : list) {
			if(node.estimatedCost<leastDistance) {
				closestNode = node;
				leastDistance = node.estimatedCost;
			}
		}
		return closestNode;
	}
	private boolean findPath(Node endNode, Node startNode) { //returns true if path found
		endNode.travelledDistance = 0;
		endNode.setEstimatedCost(startNode, endNode);
		openSet.add(endNode);
		while(!openSet.isEmpty()) {
			currentNode = getClosestNode(openSet);
			if(currentNode == startNode) {
				return true;
			}
			closedSet.add(currentNode);
			openSet.remove(currentNode);
			for(Node neighbor : currentNode.neighborNodes) {
				if(neighbor.isBlocked) {
					continue;
				}
				tentativeGScore = currentNode.travelledDistance + currentNode.distanceTo(neighbor);
				if(closedSet.contains(neighbor) && tentativeGScore >= neighbor.travelledDistance) {
					continue;
				}
				if(!openSet.contains(neighbor) || tentativeGScore < neighbor.travelledDistance) {
					neighbor.setEstimatedCost(startNode, currentNode);
					if(!openSet.contains(neighbor)) {
						openSet.add(neighbor);
					}
				}
			}
		}
		return false;
	}
	private ArrayList<Vector2> setRoute(Vector2 finish, Vector2 start, 
			Node endNode, Node startNode, ArrayList<Vector2> route) {
		Node currentNode = startNode;
		Node nextNode;
		route.add(start);
		route.add(startNode.pos());
		while(currentNode != endNode) {
			currentNode.isRoute = true;
			nextNode = currentNode.nextNode();
			route.add(nextNode.pos());
			currentNode = nextNode;
		}
		endNode.isRoute = true;
		return route;
	}
	public ArrayList<Vector2> calculateRoute(Vector2 finish, Vector2 start) {
		ArrayList<Vector2> route = new ArrayList<Vector2>();
		resetNodes();
		Node endNode = new Node(0, 0);
		Node startNode = new Node(0, 0);
		startNode = calculateStartNode(start);
		endNode = calculateEndNode(finish);
		if(findPath(endNode, startNode)) {
			route = setRoute(finish, start, endNode, startNode, route);
		} else {
			route.add(start);
		}
		return route;
	}
	private void resetNodes() {
		currentNode.reset();
		for(Node node : nodeList) {
			node.reset();
		}
		openSet.clear();
		closedSet.clear();
	}
	public ArrayList<Node> getNodeList() {
		return nodeList;
	}
}
