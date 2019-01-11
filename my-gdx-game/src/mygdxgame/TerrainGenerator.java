package mygdxgame;

import interfaces.DrawableEntity;
import interfaces.Obstacle;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class TerrainGenerator {
	private AssetManager manager;
	private ArrayList<Obstacle> obstacleList;
	private ArrayList<DrawableEntity> drawList;
	private ArrayList<Node> nodeList;
	private int terrainLimit = 20;
	private int terrainCounter = 0;
	
	public TerrainGenerator(ArrayList<Obstacle> oList, ArrayList<DrawableEntity> dList, ArrayList<Node> nList, AssetManager aManager) {
		obstacleList = oList;
		drawList = dList;
		nodeList = nList;
		manager = aManager;
	}
	public void createTerrain() {
		boolean flag = true;
		Vector2 pos = new Vector2();
		for(;terrainCounter<terrainLimit; terrainCounter++) {
			float x = 0;
			float y = 0;
			while(flag) {
				x = (float) Math.random() * 1600;
				y = (float) Math.random() * 1200;
				pos.set(x, y);
				flag = false;
				for(Obstacle rock : obstacleList) {
					if(rock.contains(pos)) {
						flag = true;
					}
				}
			}
			flag = true;
			BlockingTerrain terrain = new BlockingTerrain(x, y, 100, 100, manager.get("data/rock.png", Texture.class));
			terrain.findBlockedNodes(nodeList);
			obstacleList.add(terrain);
			drawList.add(terrain);
		}
	}
}
