import aima.search.framework.HeuristicFunction;


public class AIHeuristicFunction2 implements HeuristicFunction {

	@Override
	public double getHeuristicValue(Object arg0) {
		AIState state = (AIState)arg0;
		return state.totalKilometers + state.totalDrivers;
	}

}
