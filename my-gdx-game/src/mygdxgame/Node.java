package mygdxgame;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class Node {
	private Vector2 pos;
	private Node nextNode;
	private float distanceToGo = -1; //estimated distance to start, e.g. a straight line
	public ArrayList<Node> neighborNodes;
	public float travelledDistance = -1;
	public float estimatedCost = -1;
	public boolean isBlocked = false;
	public boolean isStart = false;
	public boolean isFinish = false;
	public boolean isRoute = false;
	
	public Node(int x, int y) {
		pos = new Vector2(x, y);
		neighborNodes = new ArrayList<Node>();
	}
	public float x() {
		return pos.x;
	}
	public float y() {
		return pos.y;
	}
	public Vector2 pos() {
		return pos;
	}
	public Node nextNode() {
		return nextNode;
	}
	public void block() {
		isBlocked = true;
	}
	public float distanceTo(Node b) {
		float distance = this.pos.dst(b.pos);
		return distance;
	}
	public float distanceTo(Vector2 b) {
		float distance = this.pos.dst(b);
		return distance;
	}
	public void reset() {
		distanceToGo = -1;
		travelledDistance = -1;
		estimatedCost = -1;
		isStart = false;
		isFinish = false;
		isRoute = false;
		nextNode = null;
	}
	public void setEstimatedCost(Node startNode, Node nextNode) {
		distanceToGo = this.distanceTo(startNode);
		travelledDistance = nextNode.travelledDistance + this.distanceTo(nextNode);
		estimatedCost = distanceToGo + travelledDistance;
		this.nextNode = nextNode;
	}
	public void checkNeighbor(Node node, float nodeDistance) {
		if(distanceTo(node)<nodeDistance) {
			neighborNodes.add(node);
			if(node.isBlocked) {
				System.out.println(node.x() + ", " + node.y());
			}
		}
	}
	public void print() {
		System.out.println(pos.x + ", " + pos.y);
	}
	public void drawShapes(Matrix4 projectionMatrix, ShapeRenderer shaper) {
		if(isBlocked) {
			shaper.filledCircle(pos.x, pos.y, 2);
		}
	}
}
