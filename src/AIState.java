import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashSet;

import java.lang.Math;

import IA.Comparticion.Usuarios;
import IA.Comparticion.Usuario;

public class AIState {
	
	public static int numberOfUsers;
	public static int numberOfDrivers;
	
	public static String MOVE = "Move";
	public static String EXCHANGE = "Exchange";
	public static String SWAP = "Swap";
			
	public class Drive {
		public Drive(Drive d) {
			driverId = d.driverId;
			distance = d.distance;
			//FIXME: Check if this is working
			actions = new ArrayList<Integer>(d.actions);
			//actions = (ArrayList<Integer>)d.actions.clone();
			currentCoords = new Coords(d.currentCoords);
		}
		public Drive(int dId) {
			driverId = dId;
			actions = new ArrayList<Integer>();
			currentCoords = new Coords();
		}
		int driverId; //index in Users ArrayList
		int distance;
		ArrayList<Integer> actions;
		Coords currentCoords;
	}
	
	public class Coords{
		public Coords() {
			
		}
		public Coords(Coords c) {
			x = c.x;
			y = c.y;
		}
		public Coords(int p_x, int p_y) {
			x = p_x;
			y = p_y;
		}
		int x;
		int y;
	}
	
	ArrayList<Drive> drives;
	static ArrayList<Usuario> Users;
	
	int totalDistance;
	int totalActiveDrivers;
	
	int above30cnt;
	
	//copy constructor
	public AIState(AIState state) {
		//numberOfUsers = state.numberOfUsers;
		//numberOfDrivers = state.numberOfDrivers;
		drives = new ArrayList<Drive>();
		for (Drive d : state.drives) {
			drives.add(new Drive(d));
		}
		//Users = state.Users;
		//FIXME: adjust the metrics according to the operators that are applied
		//totalDistance = state.totalDistance;
		//totalActiveDrivers = state.totalActiveDrivers;
	}
	
	/* Constructor
	 * Generates a new instance of the problem with N users
	 * and M drivers
	 */
	public AIState(int N, int M, int seed){
		
		int j=0, k=0;
		boolean limitExceeded = false, firstRound = true;
		
		numberOfUsers = N;
		numberOfDrivers = M;
		Users = new Usuarios(numberOfUsers, numberOfDrivers, seed);	
		
		totalDistance = 0;
		totalActiveDrivers = 0;
		above30cnt = 0;
		
		/* Generation of an initial state of type 1
		 * Assign passengers to the 1st driver until we reach the 30km limit
		 * then move on to the 2nd one, etc.
		 * If the 30km limit is reached for every driver then we start again from the 1st.
		 * The initial state is not necessarily  a solution.
		 */
		drives = new ArrayList<Drive>(numberOfDrivers);
		//drives.get(j).distance = 0;
		
		for (int i=0; i<Users.size(); i++) {
			if (Users.get(i).isConductor()) {
				Drive d = new Drive(i + 1);
				d.actions.add(i+1);
				d.driverId = i + 1;
				d.distance =0;
				d.currentCoords = new Coords();
				d.currentCoords.x = Users.get(i).getCoordOrigenX();
				d.currentCoords.y = Users.get(i).getCoordOrigenY();
				drives.add(d);
			}
		}	
		//k=0; 
		
		/*for (int i=0; i<Users.size(); i++) {
			
			if (firstRound) {
				if (limitExceeded) {
					while (!Users.get(j).isConductor() && (j<Users.size())) 
						j++;
					drives.get(k).driverId = j;
					k++;
					if(j>=Users.size()) {
						firstRound = false;
						j=0;
					}
				}
				//if (k == drives.size())
				//	k = 0;
				
				if (!Users.get(i).isConductor()) {
					//what happens with user 0? (+1??)
					drives.get(k).actions.add(i + 1);
					drives.get(k).actions.add(-(i + 1));
					//FIXME: Why += and not = ? distance has to be recalculated wholly
					drives.get(k).distance += calculateDistance(drives.get(k), Users.get(i));
					drives.get(k).currentCoords.x = Users.get(i).getCoordDestinoX();
					drives.get(k).currentCoords.y = Users.get(i).getCoordDestinoY();
			
					if (drives.get(k).distance>=30) {
						limitExceeded = true;
					}
					++k;
				}	
			} else {
				
				drives.get(j).actions.add(i+1);
				drives.get(j).actions.add(-(i+1));
				drives.get(j).distance += calculateDistance(drives.get(j), Users.get(i));
				drives.get(j).currentCoords.x = Users.get(i).getCoordDestinoX();
				drives.get(j).currentCoords.y = Users.get(i).getCoordDestinoY();
				j++;
				
				if (j>=drives.size()) j=0;
				
			}
			
			
		}*/
		k = 0;
		for (int i = 0; i != Users.size(); ++i) {
			if (!Users.get(i).isConductor()) {
				drives.get(k).actions.add(i+1);
				drives.get(k).actions.add(-(i+1));
				drives.get(k).distance += calculateDistance(drives.get(k), Users.get(i));
				drives.get(k).currentCoords.x = Users.get(i).getCoordDestinoX();
				drives.get(k).currentCoords.y = Users.get(i).getCoordDestinoY();
				k++;
				if (k == drives.size())
					k = 0;
			}
		}
		
		//adding the last negative element to the actions lists
		for (int i = 0; i<drives.size();  i++) {
			drives.get(i).actions.add(-drives.get(i).actions.get(0));
			drives.get(i).distance += Math.abs(drives.get(i).currentCoords.x - Users.get(drives.get(i).actions.get(0)-1).getCoordDestinoX()) +
					Math.abs(drives.get(i).currentCoords.y - Users.get(drives.get(i).actions.get(0)-1).getCoordDestinoY());
		}
		
		
		for (int i = 0; i<drives.size();  i++) {
			totalDistance += drives.get(i).distance;
			if (drives.get(i).distance > 300) above30cnt += drives.get(i).distance - 300;
		}
		totalActiveDrivers = numberOfDrivers;

	    recalculateMetrics();	
	}
	
	@Override
	public String toString() {
		String result = "\n";
		result += "Total distance: " + totalDistance + "\n";
		result += "Total drivers: " + totalActiveDrivers + "\n";
		result += "Drives:\n";
		for (Drive d : drives) {
			result += "Conductor: " + d.driverId;
			result += ", Actions: ";
			for (int a : d.actions) {
				if (a > 0)
					result += ", Pick up " + a + " at " + Users.get(a - 1).getCoordOrigenX() + ", " + Users.get(a - 1).getCoordOrigenY();
				else 
					result += ", Drop off " + a + " at " + Users.get((-1)*a - 1).getCoordDestinoX() + ", " + Users.get((-1)*a - 1).getCoordDestinoY();
			}
			result += "; \n";
		}
		result += "\n";
		return result;
	}
	
	public void recalculateMetrics() {
		for (int i = 0; i<drives.size();  i++) {
			Drive d = drives.get(i);
			Usuario cond = Users.get(d.driverId-1); //
			d.currentCoords = new Coords(cond.getCoordOrigenX(), cond.getCoordOrigenY());
			d.distance = 0;
			for (int j = 1; j != drives.get(i).actions.size(); j++) {
				if (d.actions.get(j) < 0){
					d.distance += Math.abs(d.currentCoords.x - Users.get((-1)*d.actions.get(j)-1).getCoordDestinoX()) +
									Math.abs(d.currentCoords.y - Users.get((-1)*d.actions.get(j)-1).getCoordDestinoY());
					
					d.currentCoords.x = Users.get((-1)*d.actions.get(j)-1).getCoordDestinoX();
					d.currentCoords.y = Users.get((-1)*d.actions.get(j)-1).getCoordDestinoY();
				}
				else {
					d.distance += Math.abs(d.currentCoords.x - Users.get(d.actions.get(j)-1).getCoordOrigenX()) +
					Math.abs(d.currentCoords.y - Users.get(d.actions.get(j)-1).getCoordOrigenY());
					
					d.currentCoords.x = Users.get(d.actions.get(j)-1).getCoordOrigenX();
					d.currentCoords.y = Users.get(d.actions.get(j)-1).getCoordOrigenY();
				}
			}
			//drives.get(i).distance += Math.abs(drives.get(i).currentCoords.x - Users.get(drives.get(i).actions.get(0)-1).getCoordDestinoX()) +
			//		Math.abs(drives.get(i).currentCoords.y - Users.get(drives.get(i).actions.get(0)-1).getCoordDestinoY());
		}
		
		totalDistance = 0;
		totalActiveDrivers = 0;
		above30cnt = 0;
		for (int i = 0; i<drives.size();  i++) {
			totalDistance += drives.get(i).distance;
			if (drives.get(i).distance > 300) above30cnt += drives.get(i).distance - 300;
			//if (drives.get(i).actions.size()!=0)
				//totalActiveDrivers++;
		}	
		totalActiveDrivers = drives.size();
	}
	
	//Calculates the distance of the route that picks up and drops off a user
	public int calculateDistance(Drive drive , Usuario user){
		int retVal = 0;
		//Distance to pick up
		retVal += Math.abs(drive.currentCoords.x - user.getCoordOrigenX()) + Math.abs(drive.currentCoords.y - user.getCoordOrigenY());
		//Distance to drop off
		retVal += Math.abs(user.getCoordOrigenX() - user.getCoordDestinoX()) + Math.abs(user.getCoordOrigenY() - user.getCoordDestinoY());	
		return retVal;
	}
	
	/* OPERATORS */
	
	public boolean actionRestrictionsValid(int driveId) {
		Drive d = drives.get(driveId);
		int numPpl = 0;
		HashSet<Integer> usuarios = new HashSet<Integer>();
		for (int val : d.actions) {
			if (val > 0) {
				numPpl++;
				if (numPpl > 3)
					return false;
				usuarios.add(val);
			} else {
				if (!usuarios.contains(-val))
					return false;
				numPpl--;
			}
		}
		return true;
	}
	
	//swap two actions in the same drive (actions list)
	public void swap(int driveId, int action1, int action2) {
	    Drive d = drives.get(driveId);
	    int temp = d.actions.get(action1);
	    d.actions.set(action1, d.actions.get(action2));
	    d.actions.set(action2, temp);
	    
	    recalculateMetrics();
	}
	//exchange two passengers between two drives
	public void exchange(int driveId1, int driveId2, int action1, int action2) {
	    Drive d1 = drives.get(driveId1);
	    Drive d2 = drives.get(driveId2);
	    for (int i = 0; i != d1.actions.size(); ++i) {
	    	if (d1.actions.get(i).equals(action1)){
	    		d1.actions.set(i, action2);
	    		if (i==0)
	    			d1.driverId = action2;
	    	}
	    	else if (d1.actions.get(i).equals(action1 * (-1)))
	    		d1.actions.set(i, action2 * (-1));
	    }
	    
	    for (int i = 0; i != d2.actions.size(); ++i) {
	    	if (d2.actions.get(i).equals(action2)){
	    		d2.actions.set(i, action1);
	    		if (i==0)
	    			d2.driverId = action1;
	    	}	
	    	else if (d2.actions.get(i).equals(action2 * (-1)))
	    		d2.actions.set(i, action1 * (-1));
	    }
	    recalculateMetrics();
	}
	
	public void move(int driveId1, int driveId2, int action, int index1, int index2) {
	    Drive d1 = drives.get(driveId1);
	    Drive d2 = drives.get(driveId2);
	    d1.actions.remove((Object)action);
	    d1.actions.remove((Object)((-1) * action));
	    
	    if (d1.actions.size() == 0)
	    	drives.remove(d1);
	    	
	    //
	    d2.actions.add(index2, action * (-1));
	    d2.actions.add(index1, action);
	    
	    recalculateMetrics();
	}
	
	public void move(int driveId, int action) {
		Drive d = drives.get(driveId);
		d.actions.remove((Object)action);
		d.actions.remove((Object)((-1) * action));
		
		if (d.actions.size() == 0)
			drives.remove(d);
		
		Drive additionalDrive = new Drive(action);
		additionalDrive.actions.add(action);
		additionalDrive.actions.add((-1) * action);

		drives.add(additionalDrive);
		
		recalculateMetrics();
	}
	
	
	public int getTotalDistance(){ return totalDistance; }
	
	public int getTotalActiveDrivers(){return totalActiveDrivers;}
	
	public int getTotalKilometresAbove30(){return above30cnt;}
	
	public int getNumberOfUsers(){return numberOfUsers;}
	
	public int getNumberOfDrivers(){return numberOfDrivers;}
	
	public void printState() {
		
		int j = 0;
		
		System.out.println("TOTAL DISTANCE: " + Integer.toString(totalDistance));
		System.out.println("ACTIVE DRIVERS: " + Integer.toString(totalActiveDrivers));
		System.out.println("DRIVES: ");

		Drive d;
		Iterator<Drive> iterator = drives.iterator();
		while (iterator.hasNext()) {
			d = iterator.next();
			System.out.println("DRIVER "+ Integer.toString(j) + "(USER " + Integer.toString(d.driverId) +"): ");
			
			Iterator<Integer> iterator2 = d.actions.iterator();
			while (iterator2.hasNext())
				System.out.print(Integer.toString(iterator2.next()) + "");
			
			j++;
		}
	}
	
	
	
}
