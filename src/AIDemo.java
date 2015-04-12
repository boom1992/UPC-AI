

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.System;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;


public class AIDemo {
    
    public static void main(String[] args){
    	
    	PrintWriter writer = null;
		try {
			writer = new PrintWriter("/home/georgia/Desktop/AI/results.txt", "UTF-8");
			writer.println("The first line");
	    	writer.println("The second line");
	    	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	for (int s=1; s<11; s++ ){
    		long startTime = System.nanoTime();
    		// What value to use as a seed?
    		AIState State = new AIState(10,4,1,1);
    		AIHillClimbingSearch(State);
    		//AISimulatedAnnealingSearch(State);
    		long endTime = System.nanoTime();
    		long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds
    		System.out.println("\n EXECUTION TIME: 	" + duration/1000000 + "ms = " + duration/1000000000 + "sec");
    		writer.println("\n EXECUTION TIME: 	" + duration/1000000 + "ms = " + duration/1000000000 + "sec");
    	}	
    	writer.close();
    }
   
 
    private static void AIHillClimbingSearch(AIState State) {
        System.out.println("\nAI HillClimbing  -->");
        try {
            Problem problem =  new Problem(State,new AISuccessorFunction(), new AIGoalTest(),new AIHeuristicFunction());
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);
            
            System.out.println("INITIAL STATE: " + State.toString());
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

