package mygdxgame;

import interfaces.DrawableEntity;
import interfaces.Obstacle;

import java.util.ArrayList;

import util.Util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BlockingTerrain implements Obstacle, DrawableEntity {
	private Vector2 pos;
	private Rectangle area;
	private Sprite sprite;
	
	public BlockingTerrain(float x, float y, float width, float height, Texture tex) {
		sprite = new Sprite(tex);
		pos = new Vector2(x, y);
		float rectX = x - (width/2);
		float rectY = y - (height/2);
		area = new Rectangle(rectX, rectY, width, height);
	}
	@Override
	public boolean blocksNode(Node node) {
		if(area.contains(node.x(), node.y())) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public void draw(SpriteBatch batch) {
		Util.draw(sprite, pos.x, pos.y, 0, batch);
	}
	@Override
	public void findBlockedNodes(ArrayList<Node> nodeList) {
		for(Node node : nodeList) {
			if(blocksNode(node)) {
				node.block();
			}
		}
	}
	@Override
	public boolean contains(Vector2 pos) {
		return area.contains(pos.x, pos.y);
	}
	@Override
	public Vector2 getPos() {
		return pos;
	}
	@Override
	public boolean intersects(Rectangle rect) {
		return area.contains(rect);
	}
}
