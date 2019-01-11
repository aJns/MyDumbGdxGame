package mygdxgame;

import interfaces.Targetable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class ProjectileWeapon {
	public final static int MACHINE_GUN = 0;
	public final static int BURST_GUN = 1;
	
	protected Vector2 pos;
	protected Pool<Projectile> bulletPool;
	protected float accuracy = 1;
	protected int reloadTimer = 0;
	protected int timeToReload = 15;
	protected float maxRange = 200;
	protected Unit user;
	
	public static ProjectileWeapon getWeapon(Vector2 position, Pool<Projectile> bPool, Unit owner, int type) {
		if(type==MACHINE_GUN) {
			ProjectileWeapon gun = new ProjectileWeapon(position, bPool, owner);
			return gun;
		} else if(type==BURST_GUN) {
			ProjectileWeapon gun = new BurstWeapon(position, bPool, owner);
			return gun;
		} else {
			return new ProjectileWeapon(position, bPool, owner);
		}
	}
	
	protected ProjectileWeapon(Vector2 position, Pool<Projectile> bPool, Unit owner) {
		user = owner;
		pos = new Vector2(position);
		bulletPool = bPool;
	}
	public void update(Vector2 position) {
		pos.set(position);
		if(reloadTimer<600) reloadTimer++;
	}
	public void shoot(Targetable target) {
		if(target!=null && reloadTimer>timeToReload) {
			Projectile bullet = bulletPool.obtain();
			bullet.init(user.getOwner(), pos, target, accuracy);
			reloadTimer = 0;
		}
	}
	public float range() {
		return maxRange;
	}
}
