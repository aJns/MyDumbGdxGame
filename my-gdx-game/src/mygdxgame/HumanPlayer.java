package mygdxgame;

import interfaces.DrawableEntity;
import interfaces.Targetable;
import interfaces.Updateable;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Pool;

public class HumanPlayer extends Player {
	private OrderGenerator orderGenerator;

	public HumanPlayer(ShapeRenderer shaper, ArrayList<Targetable> targetList, ArrayList<DrawableEntity> drawList, ArrayList<Updateable> updateList,
			Pool<Projectile> bPool, AssetManager manager, Pathfinder pathfinder) {
		super(targetList, drawList, updateList, bPool, manager, pathfinder);
		orderGenerator = new OrderGenerator(controlList, targetList, shaper);
		orderGenerator.rightButton(50, 50);
		type = HUMAN;
	}
	public OrderGenerator getOrderGenerator() {
		return orderGenerator;
	}
	public void drawShapes(Matrix4 projectionMatrix)  {
		orderGenerator.drawShapes(projectionMatrix);
	}
}
