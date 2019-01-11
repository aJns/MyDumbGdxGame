package interfaces;

import mygdxgame.Order;

import com.badlogic.gdx.math.Vector2;

public interface Controllable {
	public void setOrder(Order order);
	public boolean withinBounds(Vector2 coordinate);
	public Vector2 getPos();
	public boolean isAlive();
}
