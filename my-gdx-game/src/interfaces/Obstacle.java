package interfaces;

import java.util.ArrayList;

import mygdxgame.Node;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Obstacle {
	public boolean blocksNode(Node node);
	public boolean contains(Vector2 pos);
	public boolean intersects(Rectangle rect);
	public void findBlockedNodes(ArrayList<Node> nodeList);
	public Vector2 getPos();
}