package mygdxgame;

import interfaces.Controllable;
import interfaces.Targetable;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class OrderGenerator {
	private ArrayList<Controllable> selectedUnits;
	private ArrayList<Controllable> controlList;
	private ArrayList<Targetable> targetList;
	private ShapeRenderer shaper;
	private Rectangle selectionBox;
	private boolean creatingSelectionBox;
	private Vector2 selectionBoxStart;
	private Targetable target;
	
	public OrderGenerator(ArrayList<Controllable> cList, ArrayList<Targetable> tList, ShapeRenderer shapeRenderer) {
		controlList = cList;
		selectedUnits = cList;
		targetList = tList;
		shaper = shapeRenderer;
		selectedUnits = new ArrayList<Controllable>();
		selectionBox = new Rectangle();
		creatingSelectionBox = false;
		selectionBoxStart = new Vector2();
	}
	public void rightButton(int x, int y) {
		if(!selectedUnits.isEmpty()) {
			if(withinTargetBounds(x, y)) {
				Order order = new Order(selectedUnits, target);
				for(Controllable object : selectedUnits) {
					object.setOrder(order);
				}
			} else {
				Vector2 coordinate = new Vector2(x, y);
				Order order = new Order(selectedUnits, coordinate);
				for(Controllable object : selectedUnits) {
					object.setOrder(order);
				}
			}
		}
	}
	public void leftButton(int x, int y) {
		selectedUnits.clear();
		Vector2 coordinate = new Vector2(x, y);
		for(Controllable object : controlList) {
			if(object.withinBounds(coordinate)) {
				selectedUnits.add(object);
				return;
			}
		}
	}
	public void createSelectionBox(int x, int y) {
		if(!creatingSelectionBox) {
			selectionBoxStart.set(x, y);
			selectionBox.setX(x);
			selectionBox.setY(y);
			creatingSelectionBox = true;
		} else {
			float width = x - selectionBoxStart.x;
			float height = y - selectionBoxStart.y;
			selectionBox.setWidth(width);
			selectionBox.setHeight(height);
		}
	}
	public void endSelectionBox(int button) {
		if(button==Input.Buttons.LEFT) {
			selectedUnits.clear();
			if(selectionBox.width<0) {
				float width = selectionBox.width;
				selectionBox.x += width;
				selectionBox.width = width * -1;
				
			}if(selectionBox.height<0) {
				float height = selectionBox.height;
				selectionBox.y += height;
				selectionBox.height = height * -1;
				
			}
			for(Controllable object : controlList) {
				float x = object.getPos().x;
				float y = object.getPos().y;
				if(selectionBox.contains(x, y)) {
					selectedUnits.add(object);
				}
			}
		}
		selectionBox.set(0, 0, 0, 0);
		creatingSelectionBox = false;
	}
	public void drawShapes(Matrix4 projectionMatrix) {
		float x = selectionBox.getX();
		float y = selectionBox.getY();
		float width = selectionBox.getWidth();
		float height = selectionBox.getHeight();
		shaper.setProjectionMatrix(projectionMatrix);
		shaper.begin(ShapeType.Rectangle);
		shaper.setColor(0, 1, 0, 0);
		shaper.rect(x, y, width, height);
		shaper.end();
		shaper.begin(ShapeType.Circle);
		shaper.setColor(0, 1, 0, 0);
		for(Controllable object : selectedUnits) {
			float cX = object.getPos().x;
			float cY = object.getPos().y;
			shaper.circle(cX, cY, 20);
		}
		shaper.end();
	}
	public boolean creatingSelectionBox() {
		return creatingSelectionBox;
	}
	private boolean withinTargetBounds(int x, int y) {
		Vector2 point = new Vector2(x, y);
		for(Targetable object : targetList) {
			if(object.withinBounds(point)) {
				target = object;
				return true;
			}
		}
		return false;
	}
}
