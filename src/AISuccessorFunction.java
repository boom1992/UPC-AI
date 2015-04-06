

import java.util.List;
import java.util.ArrayList;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class AISuccessorFunction implements SuccessorFunction {

	@Override
	public List getSuccessors(Object s) {
		ArrayList retVal = new ArrayList();
		AIState state = (AIState)s;
		AIHeuristicFunction heuristic = new AIHeuristicFunction();
		
		//SWAP
		for (int i = 0; i != state.drives.size(); ++i) {
			AIState.Drive d = state.drives.get(i);
			for (int j = 1; j != d.actions.size() - 1; ++j) {
		    	int action1 = d.actions.get(j);
		        for (int k = j + 1; k != d.actions.size() - 1; ++k) {
	            	int action2 = d.actions.get(k);
	            	AIState newState = new AIState(state);
	            	newState.swap(i, action1, action2);
	            	if (newState.actionRestrictionsValid(i)) {
	            		String S = AIState.SWAP + " " + newState.toString();
	            		retVal.add(new Successor(S, newState));
	            	}
		        }
			}
		}
		//EXCHANGE
		for (int i = 0; i != state.drives.size(); ++i) {
			AIState.Drive d1 = state.drives.get(i);
			for (int j = i + 1; j != state.drives.size(); ++j) {
				AIState.Drive d2 = state.drives.get(j);
				//Don't swap out the driver
				for (int k = 1; k != d1.actions.size() - 1; ++k) {
					int id1 = d1.actions.get(k);
					if (id1 < 0)
						continue;
					for (int l = 1; l != d2.actions.size() - 1; ++l) {
						int id2 = d2.actions.get(l);
						if (id2 < 0)
							continue;
		            	AIState newState = new AIState(state);
		            	newState.swap(i, j, id1, id2);
		                String S = AIState.EXCHANGE + " " + newState.toString();
		                retVal.add(new Successor(S, newState));
					}
				}
			}
		}
		//MOVE
		for (int i = 0; i != state.drives.size(); ++i) {
			AIState.Drive d1 = state.drives.get(i);
			for (int j = 0; j != state.drives.size(); ++j) {
				if (i == j)
					continue;
				//Don't swap out the driver
				for (int k = 1; k != d1.actions.size() - 1; ++k) {
					int action = d1.actions.get(k);

	            	AIState newState = new AIState(state);
	            	newState.move(i, j, action);
	                String S = AIState.MOVE + " " + newState.toString();
	                retVal.add(new Successor(S, newState));
				}
			}
		}
		return retVal;
	}

}
