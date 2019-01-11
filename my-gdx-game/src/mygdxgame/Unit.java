package mygdxgame;

import interfaces.Controllable;
import interfaces.DrawableEntity;
import interfaces.Targetable;
import interfaces.Updateable;

import java.util.ArrayList;

import util.Util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Unit implements Controllable, Targetable, Updateable, DrawableEntity {
	private boolean isAlive = true;
	private boolean isMoving = false;
	private Player owner;
	private ArrayList<Order> orders;
	private Targetable activeTarget;
	private ArrayList<Targetable> targetList;
	private ProjectileWeapon gun;
	private Sprite turretSprite;
	private Sprite bodySprite;
	private Vector2 pos;
	private Vector2 waypoint;
	private float turretRotation = 0;
	private float bodyRotation = 0;
	private float targetTuRot = 0;
	private float targetBoRot = 0;
	private float tuRotSpeed = 20;
	private float boRotSpeed = 10;
	private int speed = 3;
	private Circle bounds;
	private ArrayList<Vector2> route;
	private int routeCounter = 0;
	private Pathfinder router;
	
	public Unit(Texture turretTex, Texture bodyTex, Vector2 spawn, Pathfinder pathfinder, Player player,
			Pool<Projectile> bPool, ArrayList<Targetable> tList) {
		owner = player;
		turretSprite = new Sprite(turretTex);
		bodySprite = new Sprite(bodyTex);
		pos = new Vector2(spawn);
		waypoint = new Vector2(spawn);;
		route = new ArrayList<Vector2>();
		router = pathfinder;
		bounds = new Circle(pos, turretSprite.getWidth()/2);
		targetList = tList;
		gun = ProjectileWeapon.getWeapon(pos, bPool, this, ProjectileWeapon.BURST_GUN);
		orders = new ArrayList<Order>();
	}
	private void rotate() {
		float xDiff = 0;
		float yDiff = 0;
		if(activeTarget!=null) {
			xDiff = (activeTarget.getPos().x - pos.x);
			yDiff = (activeTarget.getPos().y - pos.y);
		} else if(isMoving && pos.dst(waypoint)>1){
			xDiff = (waypoint.x - pos.x);
			yDiff = (waypoint.y - pos.y);
		}
		double radRotation;
		if(Math.abs(xDiff)>0 || Math.abs(yDiff)>0) {
			radRotation = Math.atan2(yDiff, xDiff);
			targetTuRot = (float) Math.toDegrees(radRotation);
		}
		xDiff = (waypoint.x - pos.x);
		yDiff = (waypoint.y - pos.y);
		if(Math.abs(xDiff)>0 || Math.abs(yDiff)>0) {
			radRotation = Math.atan2(yDiff, xDiff);
			targetBoRot = (float) Math.toDegrees(radRotation);
		}
		matchTurretRotation();
		matchBodyRotation();
	}
	private void matchTurretRotation() {
		if(turretRotation>180) turretRotation -= 360;
		else if(turretRotation<-180) turretRotation += 360;
		float speed = 0;
		float posDiff = Util.getPosRotationDiff(targetTuRot, turretRotation);
		float diff = Util.getRotationDiff(targetTuRot, turretRotation);
		if(posDiff<=tuRotSpeed) speed = posDiff;
		else speed = tuRotSpeed;
		if(posDiff>180) speed *= -1;
		if(diff<0) turretRotation -= speed;
		else turretRotation += speed;
	}
	private void matchBodyRotation() {
		if(bodyRotation>180) bodyRotation -= 360;
		else if(bodyRotation<-180) bodyRotation += 360;
		float speed = 0;
		float posDiff = Util.getPosRotationDiff(targetBoRot, bodyRotation);
		float diff = Util.getRotationDiff(targetBoRot, bodyRotation);
		if(posDiff<=boRotSpeed) speed = posDiff;
		else speed = boRotSpeed;
		if(posDiff>180) speed *= -1;
		if(diff<=0) bodyRotation -= speed;
		else bodyRotation += speed;
	}
	private void setRoute(Vector2 finish) {
		Vector2 start = pos;
		route = router.calculateRoute(finish, start);
		routeCounter = 0;
	}
	private boolean move() {
		if(Util.getPosRotationDiff(targetBoRot, bodyRotation)>boRotSpeed) {
			return false;
		}
		if(pos.dst(waypoint) < 1) {
			if(routeCounter < route.size()) {
				waypoint = route.get(routeCounter);
				routeCounter++;
			} else {
				return false;
			}
		}
		float xDiff = (waypoint.x - pos.x);
		float yDiff = (waypoint.y - pos.y);
		float distance = (float) Math.hypot(xDiff, yDiff);
		if (distance < speed) {
			pos.x = waypoint.x;
			pos.y = waypoint.y;
		} else {
			float xPlus = (xDiff / distance) * speed;
			float yPlus = (yDiff / distance) * speed;
			pos.x += xPlus;
			pos.y += yPlus;
		}
		bounds.x = pos.x;
		bounds.y = pos.y;
		return true;
	}
	private void parseOrders() {
		Order order = orders.get(0);
		if(order.type() == Order.MOVE_ORDER) {
			setRoute(order.getMoveGoal(this));
			orders.clear();
		} else if(order.type() == Order.TARGET_ORDER) {
			float dst = pos.dst(order.target().getPos());
			if(dst<gun.range()) {
				activeTarget = order.target();
				stopMoving();
				orders.clear();
			} else {
				if(!order.inFormation(this)) {
					setRoute(order.getMoveGoal(this));
				}
			}
		}
	}
	private void findTargets() {
		if(activeTarget!=null && activeTarget.isAlive()) {
			if(pos.dst(activeTarget.getPos())<gun.range() && activeTarget.getOwner()!=owner) return;
		}
		activeTarget = null;
		for(Targetable target : targetList) {
			if(pos.dst(target.getPos())<gun.range() && target.getOwner()!=owner) {
				activeTarget = target;
			}
		}
	}
	private void stopMoving() {
		route.clear();
	}
	@Override
	public boolean withinBounds(Vector2 pos) {
		if(bounds.contains(pos)) {
			return true;
		} else {
			return false;
		}
		
	}
	@Override
	public void setOrder(Order order) {
		orders.clear();
		orders.add(order);
	}
	@Override
	public void update() {
		if(!orders.isEmpty()) {
			parseOrders();
		}
		findTargets();
		rotate();
		gun.update(pos);
		if(Util.getPosRotationDiff(targetTuRot, turretRotation)<tuRotSpeed) gun.shoot(activeTarget);
		isMoving = move();
	}
	@Override
	public void draw(SpriteBatch batch) {
		Util.draw(bodySprite, pos.x, pos.y, bodyRotation, batch);
		Util.draw(turretSprite, pos.x, pos.y, turretRotation, batch);
	}
	@Override
	public Vector2 getPos() {
		return pos;
	}
	@Override
	public Player getOwner() {
		return owner;
	}
	@Override
	public void hit(Projectile projectile) {
		isAlive = false;
	}
	@Override
	public boolean isAlive() {
		return isAlive;
	}
	@Override
	public void die(ArrayList<Updateable> uList, ArrayList<Targetable> tList) {
		owner.controlList.remove(this);
		uList.remove(this);
		tList.remove(this);
	}
}
