package mygdxgame;

import interfaces.DrawableEntity;
import interfaces.Targetable;
import interfaces.Updateable;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class ComputerPlayer extends Player {
	private TacticalAi combatAi;
	
	public ComputerPlayer(ArrayList<Targetable> targetList, ArrayList<DrawableEntity> drawList, ArrayList<Updateable> updateList,
			Pool<Projectile> bPool, AssetManager manager, Pathfinder pathfinder) {
		super(targetList, drawList, updateList, bPool, manager, pathfinder);
		combatAi = new TacticalAi(controlList);
		type = AI;
	}
	@Override
	public void setup() {
		factory.createUnit(new Vector2(750, 550), 0);
		combatAi.giveOrder(controlList);
	}
}
