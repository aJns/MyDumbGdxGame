package util;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Util {
	public static void draw(Sprite sprite, float x, float y, float rotation, SpriteBatch batch) {
		float drawY = y - (sprite.getHeight() / 2);
		float drawX = x - (sprite.getWidth() / 2);
		
		sprite.setPosition(drawX, drawY);
		sprite.setRotation(rotation);
		sprite.draw(batch);
	}
	public static float getPosRotationDiff(float rot1, float rot2) {
		float diff = 0;
		diff = rot1 - rot2;
		if(diff<0) diff *= -1;
		return diff;
	}public static float getRotationDiff(float rot1, float rot2) {
		float diff = 0;
		diff = rot1 - rot2;
		return diff;
	}
}
