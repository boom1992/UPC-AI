

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
		// Operators: MOVE, SWAP, EXCHANGE
		// TODO Auto-generated method stub
		return null;
	}

}
