import java.util.*;

public class ConnectFour {
	public final static int WIDTH  = 7;
	public final static int HEIGHT = 6;
	public static int nodes = 0;
	public static int last_path = 0;
	public static boolean timeout = false;
	public static int path = 0;
	public static int ordered[] = new int[WIDTH];
	public static long startTime = System.currentTimeMillis();
	
	public static int alphaBeta(State s, int alpha, int beta) {
		
	    nodes++;
	    if(s.isLastLevel()){
	        return s.evaluation();
	    }
	    
	    //when the current player is winning
	    for(int i = 0; i < WIDTH; i++) {
	    	if(s.isValid(i) && s.isWinning(i)) {
                nodes++;
                if(s.isFirstLevel() && !s.alreadyWon(alpha, beta)){
                    path = i;
                }
                s.play(i);
                return s.evaluation();
	    	}
	    }
	    
	    //when the opponent is winning
	    if(s.isFirstLevel()) {
		    State so = s.copy();
	    	so.current_player *= -1;
		    for(int i = 0; i < WIDTH; i++) {
		    	if(so.isValid(i) && so.isWinning(i)) {
	            	nodes++;
	            	path = i;
	            	s.play(i);
	                return s.evaluation();
		    	}
		    }
	    }
	    
	    for(int move = 0; move < WIDTH; move++){
	    	if(System.currentTimeMillis() - startTime > 980) {
	    		timeout = true;
				break;
			}
	        if(s.isValid(move)) {
	            State s2 = s.copy();
	            s2.play(move);
	            s2.last_move = move;
	            s2.swap_move();
	            //s2.print();
	            
	            if(s2.isSecondLevel()) {
	            	State so = s2.copy();
	            	boolean op_win = false;
	            	for(int i = 0; i < WIDTH; i++) {
	    		    	if(so.isValid(i) && so.isWinning(i)) {
	    	            	op_win = true;
	    	            	break;
	    		    	}
	    		    }
	            	if(op_win) {
	            		continue;
	            	}
	            }
	            int score = alphaBeta(s2, alpha, beta);
	            
	            if(s.isRedTurn()){
	                if(s.better(score, alpha)){
	                    if(s.isFirstLevel() && !s.alreadyWon(alpha, beta)){
	                        path = move;
	                    }
	                    alpha = score;
	                }
	                if(beta <= alpha){
	                    return alpha;
	                }
	            }else{
	                if(s.better(score, beta)){
	                    if(s.isFirstLevel() && !s.alreadyWon(alpha, beta)){
	                        path = move;
	                    }
	                    beta = score;
	                }
	                if(beta <= alpha){
	                    return beta;
	                }
	            }
	        }
	    }
	    if(s.isRedTurn()){
	        return alpha;
	    }else{
	        return beta;
	    }
	}
	
	public static void main(String [] args) {
		
	    int bit = 1;
	    for(int i = 0; i < WIDTH; i++){
	        int s = WIDTH / 2 + bit*(i+1)/2;
	        ordered[i] = s;
	        bit *= -1;
	    }
	    
	    // For tournament only
	    // int i = 0;
	    
	    // while(System.currentTimeMillis() - startTime < 980) {
	    // 	State state = new State(args[0], args[1], i);
	    // 	alphaBeta(state, Integer.MIN_VALUE, Integer.MAX_VALUE);
	    // 	if(timeout) {
	    // 		path = last_path;
	    // 	}else {
	    // 		last_path = path;
	    // 	}
	    	
	    // 	i++;
	    // }
	    

	    // System.out.println(path);
	    // System.out.println("depth: " + (i - 1));
	    // System.out.println("path: " + path);
	    // System.out.println("explored nodes: " + nodes);
	    // long endTime = System.currentTimeMillis();
	    // System.out.println("time: " + (endTime - startTime + "ms"));
	    // System.out.println(args[1] + "'s turn!");
	    
	    
	    State state2 = new State(args[0], args[1], 0);
	    state2.print();
		System.out.println((state2.current_player == 1 ? "Red" : "Yellow") + "'s turn!");
	    Scanner sc = new Scanner(System.in);
	    
	    while(sc.hasNext()) {
	    	String command = sc.nextLine();
	    	System.out.println();
	    	String[] c = command.split(" ");
	    	state2.play(Integer.parseInt(c[0]));
	    	state2.print();
	    	startTime = System.currentTimeMillis();
	    	int j = 0;
	    	nodes = 0;
	    	path = 0;
	    	last_path = 0;
	    	timeout = false;
	    	State state3 = state2.copy();
	    	state3.current_depth = 0;
	    	while(System.currentTimeMillis() - startTime < 995) {
		    	state3.max_depth = j;
		    	alphaBeta(state3, Integer.MIN_VALUE, Integer.MAX_VALUE);
		    	if(timeout) {
		    		path = last_path;
		    	}else {
		    		last_path = path;
		    		j++;
		    	}
		    }
	    	
	    	// System.out.println("Explored Depth: " + j);
	    	System.out.println((state2.current_player == 1 ? "Red" : "Yellow") + "'s turn!");
		    System.out.println("Best Move: " + path);
		    // System.out.println("explored nodes: " + nodes);
		    // long endTime = System.currentTimeMillis();
		    // System.out.println("Evaluation Time: " + (endTime - startTime + "ms"));
	    }
	}
}
