package interfaces;

import com.badlogic.gdx.math.Vector2;

import mygdxgame.Player;
import mygdxgame.Projectile;

public interface Targetable {
	public Player getOwner();
	public Vector2 getPos();
	public boolean withinBounds(Vector2 point);
	public boolean isAlive();
	public void hit(Projectile projectile);
}
