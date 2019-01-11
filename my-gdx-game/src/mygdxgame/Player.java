package mygdxgame;

import interfaces.Controllable;
import interfaces.DrawableEntity;
import interfaces.Targetable;
import interfaces.Updateable;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public abstract class Player {
	public ArrayList<Controllable> controlList;
	protected UnitFactory factory;
	public int type;
	
	public static final int HUMAN = 0;
	public static final int AI = 1;
	
	public Player(ArrayList<Targetable> targetList, ArrayList<DrawableEntity> drawList, ArrayList<Updateable> updateList,
			Pool<Projectile> bPool, AssetManager manager, Pathfinder pathfinder) {
		controlList = new ArrayList<Controllable>();
		factory = new UnitFactory(controlList, targetList, drawList, updateList, bPool, manager, pathfinder, this);
	}
	public void setup() {
		factory.createUnit(new Vector2(50, 50), 9);
	}
}
