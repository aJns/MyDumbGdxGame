package mygdxgame;

import interfaces.Controllable;
import interfaces.DrawableEntity;
import interfaces.Targetable;
import interfaces.Updateable;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class UnitFactory {
	private Player owner;
	private Pool<Projectile> bulletPool;
	private ArrayList<Controllable> controlList;
	private ArrayList<Targetable> targetList;
	private ArrayList<DrawableEntity> drawList;
	private ArrayList<Updateable> updateList;
	private AssetManager assetManager;
	private Pathfinder router;
	private int counter = 0;
	private int robotLimit = 25;
	
	public UnitFactory(ArrayList<Controllable> cList, ArrayList<Targetable> tList, ArrayList<DrawableEntity> dList, ArrayList<Updateable> uList, 
			Pool<Projectile> bPool, AssetManager aManager, Pathfinder pathfinder, Player player) {
		owner = player;
		router = pathfinder;
		controlList = cList;
		targetList = tList;
		drawList = dList;
		updateList = uList;
		assetManager = aManager;
		bulletPool = bPool;
	}
	public void createUnit(Vector2 spawn, int type) {
		while(assetManager.update() && counter < robotLimit) {
			Unit unit;
			if(type==0) {
				unit = new Unit(assetManager.get("data/baddie.png", Texture.class), 
						assetManager.get("data/robotBody.png", Texture.class), spawn, router, owner, bulletPool, targetList);
			} else {
				unit = new Unit(assetManager.get("data/robot.png", Texture.class), 
						assetManager.get("data/robotBody.png", Texture.class), spawn, router, owner, bulletPool, targetList);
			}
			controlList.add(unit);
			targetList.add(unit);
			drawList.add(unit);
			updateList.add(unit);
			counter++;
		}
	}
}
