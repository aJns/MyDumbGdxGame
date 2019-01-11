package interfaces;

import java.util.ArrayList;



public interface Updateable {
	public void update();
	public boolean isAlive();
	public void die(ArrayList<Updateable> uList, ArrayList<Targetable> tList);
}
