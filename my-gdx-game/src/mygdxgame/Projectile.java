package mygdxgame;

import interfaces.DrawableEntity;
import interfaces.Obstacle;
import interfaces.Targetable;
import interfaces.Updateable;

import java.util.ArrayList;

import util.Util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Projectile implements Updateable, DrawableEntity, Poolable {
	private Player owner;
	private ArrayList<Projectile> bulletList;
	private ArrayList<DrawableEntity> drawList;
	private ArrayList<Targetable> targetList;
	private ArrayList<Obstacle> obstacleList;
	private Sprite sprite;
	private Vector2 line;
	private Vector2 pos;
	private Vector2 direction;
	private float rotation = 0;
	private int speed = 5;
	private int timer = 0;
	public Projectile(Texture tex, ArrayList<Projectile> bList, ArrayList<DrawableEntity> dList, ArrayList<Targetable> tList, ArrayList<Obstacle> oList) {
		bulletList = bList;
		drawList = dList;
		targetList = tList;
		obstacleList = oList;
		sprite = new Sprite(tex);
		pos = new Vector2();
		direction = new Vector2();
		line = new Vector2();
	}
	private void randomizeTarget(float accuracy) {
		float range = 10 / accuracy;
		float x = MathUtils.random(-range, range);
		float y = MathUtils.random(-range, range);
		Vector2 random = new Vector2(x, y);
		direction.add(random);
	}
	private void targetHit(Targetable thing) {
		timer += 999;
		if(thing!=null) {
			thing.hit(this);
		}
	}
	public void init(Player shooter, Vector2 position, Targetable givenTarget, float accuracy) {
		owner = shooter;
		bulletList.add(this);
		drawList.add(this);
		pos.set(position);
		direction.set(givenTarget.getPos());
		randomizeTarget(accuracy);
		rotation = line.set(pos).sub(direction).nor().mul(speed).angle();
	}
	public boolean isAlive() {
		if(timer>60) {
			return false;
		} else {
			return true;
		}
	}
	@Override
	public void draw(SpriteBatch batch) {
		Util.draw(sprite, pos.x, pos.y, rotation, batch);
	}
	@Override
	public void update() {
		for(Targetable thing : targetList) {
			if(thing.getOwner()!=owner && pos.dst(thing.getPos())<10) {
				targetHit(thing);
			}
		}
		for(Obstacle thing : obstacleList) {
			if(thing.contains(pos)) {
				targetHit(null);
			}
		}
		pos.x -= line.x;
		pos.y -= line.y;
		timer++;
	}
	@Override
	public void reset() {
		bulletList.remove(this);
		drawList.remove(this);
		pos.set(0, 0);
		direction.set(pos);
		line.set(pos);
		rotation = 0;
		timer = 0;
	}
	@Override
	public void die(ArrayList<Updateable> uList, ArrayList<Targetable> tList) {
		// TODO Auto-generated method stub
		
	}
}
