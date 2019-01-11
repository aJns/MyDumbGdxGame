package mygdxgame;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Formation {
	private Vector2 pos;
	private ArrayList<Position> positions;
	private ArrayList<Position> takenPositions;
	private ArrayList<Unit> users;
	private int userSize = 0;
	private int distance = 40;
	private float sideWidth = 0;
	private float sideHeight = 0;
	
	public Formation() {
		pos = new Vector2();
		positions = new ArrayList<Formation.Position>();
		takenPositions = new ArrayList<Formation.Position>();
		users = new ArrayList<Unit>();
	}
	private class Position {
		public Unit owner;
		public Vector2 formationPos;
		private Vector2 realPos;
		public Position() {
			formationPos = new Vector2();
			realPos = new Vector2();
		}
		public Position(Vector2 fp, Vector2 rp) {
			formationPos = fp;
			realPos = rp;
		}
		public Vector2 getPos() {
			realPos.set(formationPos).add(pos);
			return realPos;
		}
		public void set(float x, float y) {
			formationPos.set(x, y);
		}
		public Position cpy() {
			return new Position(formationPos.cpy(), realPos.cpy());
		}
	}
	private void reset() {
		positions.clear();
		takenPositions.clear();
		users.clear();
	}
	public void createFormation(int size, Vector2 centerPosition) {
		userSize = (int) size;
		sideWidth = 0;
		sideHeight = 0;
		reset();
		float side = 0;
		side = (float) Math.sqrt(size);
		Formation.Position open = new Position();
		for(int i = 0; i<side; i++) {
			for(int j = 0; j<side; j++) {
				open.set(j*distance, i*distance);
				positions.add(open.cpy());
				if(j>sideWidth) sideWidth = j;
			}
			sideHeight = i;
		}
		sideWidth *= distance/2;
		sideHeight *= distance/2;
		float x = sideWidth;
		float y = sideHeight;
		pos = centerPosition.cpy();
		pos.sub(x, y);
	}
	public Vector2 getPosition(Unit user) {
		Position returnPosition = null;
		for(Position position : positions) {
			if(user==position.owner) return position.getPos().cpy();
		}
		for(Position position : positions) {
			if(position.owner==null) {
				returnPosition = position;
				position.owner = user;
				break;
			}
		}
		return returnPosition.getPos();
	}
	public boolean inFormation(Unit user) { //checks if user already has a position
		for(Position position : positions) {
			if(user==position.owner) return true;
		}
		return false;
	}
	public void freePosition(Formation.Position position, Unit user) {
		users.remove(user);
		takenPositions.remove(position);
		positions.add(position);
	}
	public void update(Vector2 position, int users) {
		if(users!=userSize) {
			createFormation(users, position);
			userSize = users;
		} else {
			pos.set(position);
			pos.sub(sideWidth, sideHeight);
		}
	}
}
