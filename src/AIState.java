import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import IA.Comparticion.Usuarios;
import IA.Comparticion.Usuario;

public class AIState {
	
	public static int numberOfUsers;
	public static int numberOfDrivers;
	
	public static String MERGE = "Merge";
	public static String MOVE = "Move";
	public static String SWAP = "Swap";
			
	public class Drive {
		int driverId;
		int distance;
		List<Integer> actions;
	}
	

	ArrayList<Drive> drives;
	ArrayList<Usuario> Users;
	
	int totalKilometers;
	int totalActiveDrivers;
	
	/* Constructor
	 * Generates a new instance of the problem with N users
	 * and M drivers
	 */
	public AIState(int N, int M, int seed){
		
		numberOfUsers = N;
		numberOfDrivers = M;
		
		Users = new Usuarios(N, M, seed);	
		
		/* Generation of an initial solution of type 1
		 * Assign passengers to the 1st driver until we reach the 30km limit
		 * then move on to the 2nd one, etc.
		 */
		
		
		
	}
	
	
	
}
