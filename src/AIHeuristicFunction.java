

import aima.search.framework.HeuristicFunction;
import aima.search.framework.Successor;

public class AIHeuristicFunction implements HeuristicFunction {

	@Override
	public double getHeuristicValue(Object arg0) {
		int penalty = 10;
		AIState state = (AIState)arg0;
		//return state.totalDistance;
		return (state.totalDistance + state.above30cnt*penalty);
	}
}
