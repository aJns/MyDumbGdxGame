package mygdxgame;

import interfaces.Controllable;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class TacticalAi {
	private ArrayList<Controllable> selectedUnits;
	
	public TacticalAi(ArrayList<Controllable> controlList) {
		selectedUnits = controlList;
	}
	public void giveOrder(Controllable subordinate) {
		Vector2 coordinate = new Vector2(700, 500);
		Order order = new Order(selectedUnits, coordinate);
		subordinate.setOrder(order);
	}
	public void giveOrder(ArrayList<Controllable> subordinates) {
		Vector2 coordinate = new Vector2(600, 500);
		Order order = new Order(selectedUnits, coordinate);
		for(Controllable object : selectedUnits) {
			object.setOrder(order);
		}
	}
}
