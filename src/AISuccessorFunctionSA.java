import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class AISuccessorFunctionSA implements SuccessorFunction {

	@Override
	public List getSuccessors(Object s) {
		Random myRandom = new Random();
		ArrayList retVal = new ArrayList();
		AIState state = (AIState)s;
		AIHeuristicFunction heuristic = new AIHeuristicFunction();
		System.out.println("State heuristic: " + heuristic.getHeuristicValue(state));
		int op,i,j,k,l;
		boolean valid;
		
		//Pick an operator at random (0 - Swap, 1 - Exchange, 2 - Move)
		op = myRandom.nextInt(3);
		
		if (op == 0){	
			//SWAP
			valid = false;
			while (!valid){
				if (state.drives.size()>0)
					i = myRandom.nextInt(state.drives.size());
				else 
					continue;
				AIState.Drive d = state.drives.get(i);
				if (d.actions.size() - 2 > 0)
					j = 1 + myRandom.nextInt(d.actions.size() - 2);
				else 
					continue;
				if (d.actions.size() - 1 > (j+1))
					k = j+1 + myRandom.nextInt(d.actions.size() - 1 - (j+1));
				else
					continue;
				AIState newState = new AIState(state);
				newState.swap(i, j, k);
				
				if (newState.actionRestrictionsValid(i)) {
            		String S = AIState.SWAP + " " + newState.toString();
            		retVal.add(new Successor(S, newState));
            		valid = true;
				}
			}
		}
		else if (op == 1){
			//EXCHANGE
			valid = false;
			while (!valid){
				if (state.drives.size()>0)
					i = myRandom.nextInt(state.drives.size());
				else 
					continue;
				AIState.Drive d1 = state.drives.get(i);
				if (state.drives.size() - (i+1) >0)
					j = i+1 + myRandom.nextInt(state.drives.size() - (i+1));
				else 
					continue;
				AIState.Drive d2 = state.drives.get(j);
				if (d1.actions.size()>0)
					k = myRandom.nextInt(d1.actions.size());
				else 
					continue;
				int id1 = d1.actions.get(k);
				if (id1 < 0)
					continue;
				if (d2.actions.size()>0)
					l = myRandom.nextInt(d2.actions.size());
				else 
					continue;
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
					valid = true;
				}
			}
		}
		else {
			//MOVE
			valid = false;
			while (!valid){
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
				
				AIState newState = new AIState(state);
            	newState.move(i, j, action); 	
                String S = AIState.MOVE + " " + newState.toString();
                retVal.add(new Successor(S, newState));
                valid = true;
			}
		}
		
		
		System.out.println("Found " + retVal.size());
		for (Object o : retVal) {
			Successor suc = (Successor)o;
			System.out.println("Successor-heuristic: " + heuristic.getHeuristicValue(suc.getState()));
		} 
		return retVal;
	}


}