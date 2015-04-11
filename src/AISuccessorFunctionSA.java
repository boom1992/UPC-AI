import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class AISuccessorFunctionSA implements SuccessorFunction {

	@Override
	public List getSuccessors(Object obj) {
		Random myRandom = new Random();
		ArrayList retVal = new ArrayList();
		AIState state = (AIState)obj;
		AIHeuristicFunction heuristic = new AIHeuristicFunction();
		System.out.println("State heuristic: " + heuristic.getHeuristicValue(state));
		int op,i,j,k,l;
		boolean valid,opfound = false;
				
		
	while (!opfound){
		//Pick an operator at random (0 - Swap, 1 - Exchange, 2 - Move)
		op = myRandom.nextInt(3);
		
		if (op == 0){	
			//SWAP	
				
			//valid = false;
				if (state.drives.size()>0)
					i = myRandom.nextInt(state.drives.size());
				else {
					System.out.println("Error: There are no drives");
					continue;
				}	
				AIState.Drive d = state.drives.get(i);
				
				if (d.actions.size() - 2 > 0)
					j = 1 + myRandom.nextInt(d.actions.size() - 2);
				else {
					System.out.println("invalid1");
					continue;
				}
				
				if (d.actions.size() - 4 > 0){
					k = 1 + myRandom.nextInt(d.actions.size() - 2);
					//System.out.println("here k = "+ k);	
				}
				else{
					System.out.println("invalid2");
					continue;
				}
				if (k ==j) continue;
				
				AIState newState = new AIState(state);
				newState.swap(i, j, k);
				
				if (newState.actionRestrictionsValid(i)) {
            		String S = AIState.SWAP + " " + newState.toString();
            		retVal.add(new Successor(S, newState));
            		//valid = true;
            		opfound = true;
				}
				else continue;
			
		}
		else if (op == 1){
			//EXCHANGE
			//valid = false;
			
				if (state.drives.size()>0)
					i = myRandom.nextInt(state.drives.size());
				else {
					System.out.println("invalid3");
					continue;
				}
				AIState.Drive d1 = state.drives.get(i);
				if (state.drives.size()>0)
					j = myRandom.nextInt(state.drives.size());
				else {
					System.out.println("invalid4");
					continue;
				}
				if (i == j) continue;
				
				AIState.Drive d2 = state.drives.get(j);
				if (d1.actions.size()>0)
					k = myRandom.nextInt(d1.actions.size());
				else {
					System.out.println("invalid5");
					continue;
				}
				int id1 = d1.actions.get(k);
				if (id1 < 0)
					continue;
				if (d2.actions.size()>0)
					l = myRandom.nextInt(d2.actions.size());
				else {
					System.out.println("invalid6");
					continue;
				}
				int id2 = d2.actions.get(l);
				if (id2 < 0)
					continue;
				
				if(((k==0)&&(!state.Users.get(id2-1).isConductor()))||((l==0)&&(!state.Users.get(id1-1).isConductor())))
					continue;
				else {
					AIState newState = new AIState(state);
					newState.exchange(i, j, id1, id2);
					String S = AIState.EXCHANGE + " " + newState.toString();
					retVal.add(new Successor(S, newState));
					//valid = true;
					opfound = true;
				}
			
		}
		else {
			//MOVE
			if (state.drives.size()>0)
				i = myRandom.nextInt(state.drives.size());
			else
				continue;
			AIState.Drive d1 = state.drives.get(i);
			
			if (state.drives.size()>0)
				j = myRandom.nextInt(state.drives.size());
			else
				continue;
			if (i == j)
				continue;
			AIState.Drive d2 = state.drives.get(j);
			
			if (d1.actions.size()>0)
				k = myRandom.nextInt(d1.actions.size());
			else
				continue;
			int action = d1.actions.get(k);
			if (action<0)
				continue;
			//Don't move out the driver unless the driver is the only one left
			if ((k==0)&&(d1.actions.size()!=2))
				continue;
			
			//if the user is a conductor he can also be moved to a new drive
			int p = myRandom.nextInt(2);
			if (p == 0){
				//Determine the positions to be moved to
				int ind1,ind2;
				if (d2.actions.size()-2>0)
					ind1 = 1 + myRandom.nextInt(d2.actions.size()-2); 
				else
					continue;
				if (d2.actions.size()-2>0)
					ind2 = 1 + myRandom.nextInt(d2.actions.size()-2); 
				else
					continue;
				if (ind1>=ind2)
					continue;
				AIState newState = new AIState(state);
				newState.move(i,j,action,ind1,ind2);
				if (newState.actionRestrictionsValid()){
					String S = AIState.MOVE + " i: " + i + ", j: " + j + ", action: " + action + ", ind1: " + ind1 + ", ind2: " + ind2 + ": " + newState.toString();
					retVal.add(new Successor(S, newState));
				}
				else 
					continue;
			} else if (p == 1){//move user - conductor to a new drive
				AIState newState2 = new AIState(state);
				newState2.move(i, action);
				String S = AIState.MOVE + " i: " + i + " action: " + action + "\n " + newState2.toString();
                retVal.add(new Successor(S, newState2));
			}
			
		}
	}
		
		
		System.out.println("Found " + retVal.size());
		for (Object o : retVal) {
			Successor suc = (Successor)o;
			System.out.println("Successor-heuristic: " + heuristic.getHeuristicValue(suc.getState()));
			System.out.println(suc.getAction());
		} 
		return retVal;
	}


}
