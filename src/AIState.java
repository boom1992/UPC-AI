import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashSet;

import java.lang.Math;

import IA.Comparticion.Usuarios;
import IA.Comparticion.Usuario;

public class AIState {
	
	public int numberOfUsers;
	public int numberOfDrivers;
	
	public static String MOVE = "Move";
	public static String EXCHANGE = "Exchange";
	public static String SWAP = "Swap";
			
	public class Drive {
		public Drive(Drive d) {
			driverId = d.driverId;
			distance = d.distance;
			//FIXME: Check if this is working
			actions = (ArrayList<Integer>)d.actions.clone();
			currentCoords.x = d.currentCoords.x;
			currentCoords.y = d.currentCoords.y;
		}
		int driverId; //index in Users ArrayList
		int distance;
		ArrayList<Integer> actions;
		Coords currentCoords;
	}
	
	public class Coords{
		int x;
		int y;
	}
	
	ArrayList<Drive> drives;
	ArrayList<Usuario> Users;
	
	int totalDistance;
	int totalActiveDrivers;
	
	public AIState(AIState state) {
		numberOfUsers = state.numberOfUsers;
		numberOfDrivers = state.numberOfDrivers;
		for (Drive d : state.drives) {
			drives.add(new Drive(d));
		}
		Users = state.Users;
		totalDistance = state.totalDistance;
		totalActiveDrivers = state.totalActiveDrivers;
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
		Users = new Usuarios(N, M, seed);	
		
		totalDistance = 0;
		totalActiveDrivers = 0;
		
		/* Generation of an initial state of type 1
		 * Assign passengers to the 1st driver until we reach the 30km limit
		 * then move on to the 2nd one, etc.
		 * If the 30km limit is reached for every driver then we start again from the 1st.
		 * The initial state is not necessarily  a solution.
		 */
		
		
		
		drives = new ArrayList<Drive>(M);
		drives.get(j).distance = 0;
		
		for (int i=0; i<Users.size(); i++){
			if (Users.get(i).isConductor()) {
				drives.get(k).actions.add(i);
				drives.get(k).currentCoords = new Coords();
				drives.get(k).currentCoords.x = Users.get(i).getCoordOrigenX();
				drives.get(k).currentCoords.y = Users.get(i).getCoordOrigenY();
				k++;
			}
		}	
		k=0; 
		
		for (int i=0; i<Users.size(); i++) {
			
			if (firstRound) {
				if (limitExceeded) {
					while (!Users.get(j).isConductor()&&(j<Users.size())) 
						j++;
					k++;
					drives.get(k).driverId = j;
					if(j>=Users.size()){ 
						firstRound = false;
						j=0;
					}
				}
				
				if (!Users.get(i).isConductor()){
						
					//what happens with user 0? (+1??)
					drives.get(k).actions.add(i);
					drives.get(k).actions.add(-i);
					drives.get(k).distance += calculateDistance(drives.get(k), Users.get(i));
					drives.get(k).currentCoords.x = Users.get(i).getCoordDestinoX();
					drives.get(k).currentCoords.y = Users.get(i).getCoordDestinoY();
			
					if (drives.get(k).distance>=30) {
						limitExceeded = true;
					}
				}	
			} else {
				
				drives.get(j).actions.add(i);
				drives.get(j).actions.add(-i);
				drives.get(j).distance += calculateDistance(drives.get(j), Users.get(i));
				drives.get(j).currentCoords.x = Users.get(i).getCoordDestinoX();
				drives.get(j).currentCoords.y = Users.get(i).getCoordDestinoY();
				j++;
				
				if (j>=drives.size()) j=0;
				
			}
			
			
		}
		
		for (int i = 0; i<drives.size();  i++) 
			totalDistance += drives.get(i).distance;
		totalActiveDrivers = M;
		
	}
	
	//Calculates the distance of the route that picks up and drops off user
	public int calculateDistance(Drive drive , Usuario user){
		int retVal = 0;
		//Distance to pick up
		retVal += Math.abs(drive.currentCoords.x - user.getCoordOrigenX()) + Math.abs(drive.currentCoords.y - user.getCoordOrigenY());
		//Distance to drop off
		retVal += Math.abs(user.getCoordOrigenX() - user.getCoordDestinoX()) + Math.abs(user.getCoordOrigenY() - user.getCoordDestinoY());	
		return retVal;
	}
	
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
				if (!usuarios.contains(val))
					return false;
				numPpl--;
			}
		}
		return true;
	}
	
	public void swap(int driveId, int action1, int action2) {
	    Drive d = drives.get(driveId);
	    for (int i = 0; i != d.actions.size(); ++i) {
	    	if (d.actions.get(i).equals(action1))
	    		d.actions.set(i, action2);
	    	else if (d.actions.get(i).equals(action2))
	    		d.actions.set(i, action1);
	    }
	}
	
	
	public int getTotalDistance(){ return totalDistance; }
	
	public int getTotalActiveDrivers(){return totalActiveDrivers;}
	
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
	
	/* OPERATORS */
	
}
