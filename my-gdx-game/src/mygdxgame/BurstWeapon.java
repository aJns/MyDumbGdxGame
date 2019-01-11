package mygdxgame;

import interfaces.Targetable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class BurstWeapon extends ProjectileWeapon {
	private int burstLenght = 3;
	private int shots = 0;
	private int burstFrequency = 3;
	private int burstTimer = 0;
	
	protected BurstWeapon(Vector2 position, Pool<Projectile> bPool, Unit owner) {
		super(position, bPool, owner);
		timeToReload = 40;
	}
	@Override
	public void shoot(Targetable target) {
		if(target!=null && reloadTimer>timeToReload) {
			if(shots>=burstLenght) {
				shots = 0;
				reloadTimer = 0;
			} else if(burstTimer>burstFrequency) {
				Projectile bullet = bulletPool.obtain();
				bullet.init(user.getOwner(), pos, target, accuracy);
				shots++;
				burstTimer = 0;
			} else {
				burstTimer++;
			}
		}
	}
}
