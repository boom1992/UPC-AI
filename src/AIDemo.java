
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import java.lang.System;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;


public class AIDemo {
    
    public static void main(String[] args){
    	// What value to use as a seed??
        AIState State = new AIState(10,6,1,5);
        AIHillClimbingSearch(State);
        //AISimulatedAnnealingSearch(State);
    }
   
 
    private static void AIHillClimbingSearch(AIState State) {
        System.out.println("\nAI HillClimbing  -->");
        try {
            Problem problem =  new Problem(State,new AISuccessorFunction(), new AIGoalTest(),new AIHeuristicFunction());
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);
            
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void AISimulatedAnnealingSearch(AIState State) {
        System.out.println("\nAI Simulated Annealing  -->");
        try {
            Problem problem =  new Problem(State,new AISuccessorFunctionSA(), new AIGoalTest(),new AIHeuristicFunction());
            //Check Simulated Annealing Parameters 
            SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(2000,100,5,0.001);
            //search.traceOn();
            SearchAgent agent = new SearchAgent(problem,search);
            
            System.out.println("PRINT ACTIONS:");
            for (Object o : agent.getActions()) {
            	System.out.println(o.toString());
            }
            //printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
        
    }
    
    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }
    
    
}

