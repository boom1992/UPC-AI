import aima.search.framework.HeuristicFunction;


public class AIHeuristicFunction2 implements HeuristicFunction {

	@Override
	public double getHeuristicValue(Object arg0) {
		AIState state = (AIState)arg0;
		
		int penalty = 20;
		int dWeight = 10;
		
		return state.totalDistance + state.totalActiveDrivers*dWeight + state.above30cnt*penalty;
	}

}
