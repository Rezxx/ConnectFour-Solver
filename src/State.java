
public class State {
	
	public final int WIDTH  = 7;
	public final int HEIGHT = 6;
	public int[][] board = new int[HEIGHT][WIDTH];// r = 1, y = -1, . = 0
	public int[] height = new int[WIDTH];//number of stones in each coloumn
	public int current_player; // r = 1, y = -1
    public int goal_player;
    public int max_depth;
    public int current_depth;
    public int last_move;
    public int last_op_move;
    
    public void swap_move() {
    	int temp = last_op_move;
    	last_op_move = last_move;
    	last_move = temp;
    }
    
    public void print() {
    	for(int i = HEIGHT -1; i >=0; i--) {
			for(int j = 0; j < WIDTH; j++){
				String s = ".";
                if(board[i][j] == -1){
                    s = "y";
                }else if (board[i][j] == 1){
                    s = "r";
                }
				System.out.print(s + " ");
			}
			System.out.println();
	    }
    	String current = "yellow";
        if(current_player == -1){
            current = "red";
        }
        //System.out.println(current + ": " + evaluation());
    	
    }
    public State() {}
	public State(String fig, String player, int depth){
		String[] pos = fig.split(",");
		for(int i = 0; i < HEIGHT; i ++) {
			for(int j = 0; j < WIDTH; j++){
				int value = 0;
				if(pos[i].charAt(j) == 'r'){
					height[j]++;
					value = 1;
				}else if(pos[i].charAt(j) == 'y'){
					height[j]++;
					value = -1;
				}
				board[i][j] = value;
			}
		}
		if(player.equals("red")){
			current_player = 1;
			goal_player = 1;
		}else{
			current_player = -1;
			goal_player = -1;
		}
		max_depth = depth;
		current_depth = 0;
	}
	
	public State copy() {
		State s2 = new State();
		s2.current_player= this.current_player;
		s2.goal_player = this.goal_player;
		s2.max_depth = this.max_depth;
	    s2.current_depth = this.current_depth;
	    for(int i = 0; i < HEIGHT; i ++) {
			for(int j = 0; j < WIDTH; j++){
				s2.board[i][j] = this.board[i][j];
			}
	    }
	    for(int i = 0; i < WIDTH; i++) {
	    	s2.height[i] = this.height[i];
	    }
	    return s2;
	}
	
	public boolean isValid(int move){
		return height[move] < HEIGHT;
	}

	public boolean isWinning(int move) {
		if(height[move] >= 3 && board[height[move]-1][move] == current_player
				&& board[height[move]-2][move] == current_player && board[height[move]-3][move] == current_player){
			return true;
		}

		for(int dy = -1; dy <=1; dy++) {
			int n = 0;
			for(int dx = -1; dx <=1; dx += 2){
				for(int x = move+dx, y = height[move]+dx*dy; x >= 0 && x < WIDTH &&
					y >= 0 && y < HEIGHT && board[y][x] == current_player; n++) {
					x += dx;
					y += dx*dy;
				}
				if(n >= 3){
					return true;
				}
			}
		}
		return false;
	}

	public void play(int move){
        if(height[move] >= HEIGHT){
            System.out.println("Can't play here.\n");
            return;
        }
		board[height[move]][move] = current_player;
		height[move]++;
		current_depth++;
		current_player *= -1; 
    }

	public int[] a_row( String player){
    	int[] number = new int[4];
    	int player_n;
    	if(player.equals("red")){
    		player_n = 1;
    	}else{
    		player_n = -1;
    	}
    	for(int i = 0; i < HEIGHT; i++){
    		for(int j = 0; j < WIDTH; j++){
    			if(board[i][j] == player_n){
    				number[0] += 2;
    				// horizontal increment
    				int right_n = 1;
    				while(j + right_n < WIDTH && right_n < 4 && board[i][j + right_n] == player_n){
    					right_n++;
    				}
    				if(right_n == 4) {
    					number[right_n - 1]++;
    				}else if(right_n > 1){
    					int left_non = j;
    					boolean left_end = false;
    					while(true) {
    						if(left_non < 0 || board[i][left_non] == -player_n) {
        						left_end = true;
        						break;
        					}
    						if(board[i][left_non] == 0) {
    							break;
    						}
    						left_non--;
    					}
    					
    					int right_non = j + right_n;
    					boolean right_end = false;
    					if(right_non >= WIDTH || board[i][right_non] == -player_n) {
    						right_end = true;
    					}
    					
    					if(!right_end && !left_end) {
    						number[right_n - 1] += 2;
    					}else if((left_end && !right_end) || (!left_end && right_end)) {
    						number[right_n - 1]++;
    					}
    				}

    				//going top
    				int top_n = 1;
    				while(top_n < 4 && i + top_n < HEIGHT && board[i + top_n][j] == player_n){
    					top_n++;
    				}
    				if(top_n == 4) {
    					number[top_n - 1]++;
    				}else if(top_n > 1){
    					int bottom_non = i;
    					boolean bottom_end = false;
    					while(true) {
    						if(bottom_non < 0 || board[bottom_non][j] == -player_n) {
        						bottom_end = true;
        						break;
    						}
    						if(board[bottom_non][j] == 0) {
    							break;
    						}
    						bottom_non--;
    						
    					}
    					
    					int top_non = i + top_n;
    					boolean top_end = false;
    					if(top_non >= HEIGHT || board[top_non][j] == -player_n) {
    						top_end = true;
    					}
    					
    					if(!top_end && !bottom_end) {
    						number[top_n - 1] += 2;
    					}else if((top_end && !bottom_end) || (!top_end && bottom_end)) {
    						number[top_n - 1]++;
    					}
    				}

    				//top left
    				int top_left_n = 1;
    				while(top_left_n < 4 && i + top_left_n < HEIGHT && j - top_left_n >= 0
    						&& board[i + top_left_n][j - top_left_n] == player_n){
    					top_left_n++;
    				}
    				if(top_left_n == 4) {
    					number[top_left_n - 1]++;
    				}else if(top_left_n > 1){
    					int bottom_right_non = 1;
    					boolean bottom_right_end = false;
    					while(true) {
    						if(i - bottom_right_non < 0 || j + bottom_right_non >= WIDTH ||
        							board[i - bottom_right_non][j + bottom_right_non] == -1) {
        						bottom_right_end = true;
        						break;
        					}
    						if(board[i - bottom_right_non][j + bottom_right_non] == 0) {
    							break;
    						}
    						bottom_right_non++;
    					}
    					
    					int top_left_non = top_left_n;
    					boolean top_left_end = false;
    					if(i + top_left_non >= HEIGHT || j - top_left_non < 0 
    							|| board[i + top_left_non][j - top_left_non] == -player_n) {
    						top_left_end = true;
    					}
    					
    					if(!top_left_end && !bottom_right_end) {
    						number[top_left_n - 1] += 2;
    					}else if((top_left_end && !bottom_right_end) || (!top_left_end && bottom_right_end)) {
    						number[top_left_n - 1]++;
    					}
    				}

    				//top right
    				int top_right_n = 1;
    				while(top_right_n < 4 && i + top_right_n < HEIGHT && j + top_right_n < WIDTH
    						&& board[i + top_right_n][j + top_right_n] == player_n){
    					top_right_n++;
    					}
    				if(top_right_n > 1){
    					int bottom_left_non = 1;
    					boolean bottom_left_end = false;
    					while(true) {
    						if(i - bottom_left_non < 0 || j - bottom_left_non < 0 ||
        							board[i - bottom_left_non][j - bottom_left_non] == -player_n) {
        						bottom_left_end = true;
        						break;
        					}
    						if(board[i - bottom_left_non][j - bottom_left_non] == 0) {
    							break;
    						}
    						bottom_left_non++;
    					}
    					
    					int top_right_non = top_right_n;
    					boolean top_right_end = false;
    					if(i + top_right_non >= HEIGHT || j + top_right_non >= WIDTH || board[i + top_right_non][j + top_right_non] == -player_n) {
    						top_right_end = true;
    					}
    					
    					if(!top_right_end && !bottom_left_end) {
    						number[top_right_n - 1] += 2;
    					}else if((top_right_end && !bottom_left_end) || (!top_right_end && bottom_left_end)) {
    						number[top_right_n - 1]++;
    					}
    				}
    			}
    		}
    	}
    	//clear
    	number[1] -= number[2];
    	number[2] -= number[3];
    	return number;
    }

    int score(String player){
    	int[] number = a_row(player);    
//    	System.out.println(player);
//    	for(int i = 0; i < 4; i++) {
//    		System.out.println(number[i]);
//    	}
        int score = 0;
        int mul = 1;
        for(int i = 0; i < 4; i++){
            score += number[i]*mul;
            if(i == 2) {
            	mul*= 10;
            }
            mul *= 10;
        }
        return score;
     }

    int evaluation(){
    	int red_value = score("red");
    	int yellow_value = score("yellow");
    	if(red_value >= 10000){
    		return 10000;
    	}else if(yellow_value >= 10000){
    			return -10000;
    	}
    	//System.out.println(red_value);
    	return red_value - yellow_value;
    	}

    boolean isLastLevel(){
    	return current_depth == max_depth;
    }

    boolean isFirstLevel(){
    	return current_depth == 0;
    }

    boolean isSecondLevel() {
    	return current_depth == 1;
    }
    boolean better(int score, int other_score){
    	if(current_player == 1){
    		return score > other_score;
    	}else{
    		return score < other_score;
    	}
    }

    boolean isRedTurn(){
    	return current_player == 1;
    }

    boolean alreadyWon(int alpha, int beta){
    	if(goal_player == 1){
    		return alpha == 100000;
    	}else{
    		return beta == -100000;
    		}
    	}
	}
