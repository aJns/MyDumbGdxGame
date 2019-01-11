package mygdxgame;

import java.util.ArrayList;

import interfaces.Controllable;
import interfaces.Targetable;

import com.badlogic.gdx.math.Vector2;


public class Order {
	public static final int MOVE_ORDER = 0;
	public static final int TARGET_ORDER = 1;
	
	private int type;
	private Vector2 moveGoal;
	private Targetable target;
	private ArrayList<Controllable> followers;
	private Formation box;
	
	public Order(ArrayList<Controllable> subordinates, Vector2 goal) {
		followers = subordinates;
		box = new Formation();
		type = MOVE_ORDER;
		moveGoal = goal;
		box.createFormation(followers.size(), moveGoal);
	}
	public Order(ArrayList<Controllable> subordinates, Targetable target) {
		followers = subordinates;
		box = new Formation();
		type = TARGET_ORDER;
		this.target = target;
		moveGoal = target.getPos();
		box.createFormation(followers.size(), moveGoal);
	}
	public int type() {
		return type;
	}
	public Vector2 getMoveGoal(Unit user) {
		for(int i=0;i<followers.size();i++) {
			Controllable object = followers.get(i);
			if(!object.isAlive()) {
				followers.remove(object);
			}
		}
		box.update(moveGoal, followers.size());
		if(type==MOVE_ORDER) return box.getPosition(user);
		else if(type==TARGET_ORDER) {
			moveGoal = target.getPos();
			Vector2 point = box.getPosition(user);
			return point;
		} else return null;
	}
	public Targetable target() {
		if(type==TARGET_ORDER) return target;
		else return null;
	}
	public boolean inFormation(Unit user) {
		return box.inFormation(user);
	}
	public void addFollower(Controllable follower) {
		if(!followers.contains(follower)) {
			followers.add(follower);
		}
	}
}
