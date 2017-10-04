import java.util.*;

class State implements Comparable<State>{
	int[] board;
	State parentPt;
	int depth;
	final int DEFAULT_SUCCESSORS = 4; // default value of array size

	public State(int[] arr) {
		this.board = Arrays.copyOf(arr, arr.length);
		this.parentPt = null;
		this.depth = 0;
	}

	public State[] getSuccessors() {
		// initialilze successors array
		State[] successors = new State[DEFAULT_SUCCESSORS];
		int indexSpace = 0;

		// finds the index of 0 or space in the 8-puzzle
		for(int i = 0; i < board.length; i++){
			if(board[i] == 0){
				indexSpace = i;
				break;
			}
		}

		// give every element in a row and column the same value
		int column = indexSpace % 3;
		int row = indexSpace / 3;
		State successor;

		// every element in the same column has the same function for
		// moving left and right in the puzzle
		// every new successor is then added to the successors array
		switch (column){
		case 0:
			for(int i = 0; i < board.length; i += 3){
				successor = new State(board);
				successor = swap(successor, indexSpace, indexSpace + 1);
				successors[0] = successor;

				successor = new State(board);
				successor = swap(successor, indexSpace, indexSpace + 2);
				successors[1] = successor;
			}
			break;

		case 1:
			for(int i = 0; i < board.length; i += 3){
				successor = new State(board);
				successor = swap(successor, indexSpace, indexSpace + 1);
				successors[0] = successor;

				successor = new State(board);
				successor = swap(successor, indexSpace, indexSpace - 1);
				successors[1] = successor;
			}
			break;

		case 2:
			for(int i = 0; i < board.length; i += 3){
				successor = new State(board);
				successor = swap(successor, indexSpace, indexSpace - 1);
				successors[0] = successor;

				successor = new State(board);
				successor = swap(successor, indexSpace, indexSpace - 2);
				successors[1] = successor;
			}
			break;
		}

		// every element in the same row has the same function for
		// moving up and down in the puzzle 
		// every new successor is then added to the successors array
		switch (row){
		case 0:
			for(int i = 0; i < 3; i++){
				successor = new State(board);
				successor = swap(successor, indexSpace, indexSpace + 3);
				successors[2] = successor;

				successor = new State(board);
				successor = swap(successor, indexSpace, indexSpace + 6);
				successors[3] = successor;
			}
			break;

		case 1:
			for(int i = 3; i < 6; i++){
				successor = new State(board);
				successor = swap(successor, indexSpace, indexSpace - 3);
				successors[2] = successor;

				successor = new State(board);
				successor = swap(successor, indexSpace, indexSpace + 3);
				successors[3] = successor;
			}
			break;

		case 2:
			for(int i = 6; i < 9; i++){
				successor = new State(board);
				successor = swap(successor, indexSpace, indexSpace - 3);
				successors[2] = successor;

				successor = new State(board);
				successor = swap(successor, indexSpace, indexSpace - 6);
				successors[3] = successor;
			}
			break;
		}

		// the parents and depth for each successor is set
		for(int i = 0; i < successors.length; i++){
			successors[i].parentPt = this;
			successors[i].depth = this.depth + 1;
		}

		// insertion sort to sort the successors in ascending order
		for(int i = 1; i < successors.length; i++){
			State key = successors[i];
			int j = i - 1;

			while (j >= 0 && successors[j].compareTo(key) > 0){
				successors[j + 1] = successors[j];
				j = j - 1;
			}
			successors[j + 1] = key;
		}

		return successors;
	}

	public void printState(int option) {
		// print statement if option 1 is chosen
		if(option == 1)
			System.out.println(this.getBoard());

		// print statement if option 2 is chosen
		else if(option == 2)
			System.out.println(this.getBoard());

		// print statement if option 3 is chosen
		else if(option == 3){
			// print statement if the parent is null
			if(this.parentPt == null)
				System.out.println(this.getBoard() 
						+ " parent 0 0 0 0 0 0 0 0 0");
			else
				System.out.println(this.getBoard() 
						+ " parent " + this.parentPt.getBoard());
		}
	}

	public String getBoard() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			builder.append(this.board[i]).append(" ");
		}
		return builder.toString().trim();
	}

	public boolean isGoalState() {
		for (int i = 0; i < 9; i++) {
			if (this.board[i] != (i + 1) % 9)
				return false;
		}
		return true;
	}

	public boolean equals(State src) {
		for (int i = 0; i < 9; i++) {
			if (this.board[i] != src.board[i])
				return false;
		}
		return true;
	}

	/**
	 * CompareTo method used to compare the values of two states
	 * 
	 * @param compareState the state being compared to the current state
	 */
	public int compareTo(State compareState) {
		// remove all spaces in the string
		String strState1 = this.getBoard().replaceAll("\\s","");
		String strState2 = compareState.getBoard().replaceAll("\\s","");

		// parse the strings as int types
		int state1 = Integer.parseInt(strState1);
		int state2 = Integer.parseInt(strState2);

		// check if the state being compared is greater, lesser or equal
		// to the current state, and return an appropriate value
		if(state1 < state2)
			return -1;
		else if(state1 > state2)
			return 1;
		else
			return 0;
	}

	/**
	 * Swap method used to swap the tiles to form the successor board
	 * 
	 * @param successor the state being edited
	 * @param swapIndex0 index of the space
	 * @param swapIndexNum index of the tile to be swapped
	 */
	private State swap(State successor, int swapIndex0, int swapIndexNum){
		int temp = successor.board[swapIndexNum];
		successor.board[swapIndexNum] = 0;
		successor.board[swapIndex0] = temp;

		return successor;
	}
}

public class Torus {
	public static void main(String args[]) {
		if (args.length < 10) {
			System.out.println("Invalid Input");
			return;
		}
		int flag = Integer.valueOf(args[0]);
		int[] board = new int[9];
		for (int i = 0; i < 9; i++) {
			board[i] = Integer.valueOf(args[i + 1]);
		}
		int option = flag / 100;
		int cutoff = flag % 100;
		if (option == 1) {
			State init = new State(board);
			State[] successors = init.getSuccessors();
			for (State successor : successors) {
				successor.printState(option);
			}
		} else {
			State init = new State(board);
			Stack<State> stack = new Stack<>();
			List<State> prefix = new ArrayList<>();
			int goalChecked = 0;
			int maxStackSize = Integer.MIN_VALUE;
			// to check that only correct prefix list is printed in the loop
			boolean checkPrint = false; 

			while (true) {
				// push initial board onto stack
				stack.push(init);
				while (!stack.isEmpty() && stack.peek().depth <= cutoff) {
					// pop the last state in the stack and add it to the prefix
					State statePopped = stack.pop();
					prefix.add(statePopped);

					// check if popped state is goal state
					// increment goalChecked
					goalChecked++;
					if(statePopped.isGoalState())
						break;

					switch(option){
					// print the states in the order of goal-test
					case 2: 
						statePopped.printState(2);
						break;

					// print the states in the order of goal-test with parent
					case 3:
						statePopped.printState(3);
						break;

					// print prefix states
					case 4:
						if (!checkPrint && statePopped.depth == cutoff){
							checkPrint = true;
							for(int i = 0; i < prefix.size(); i++)
								prefix.get(i).printState(2);
						}
						break;
					}

					// finds the parent of the current state and clears the list
					int parentIndex = prefix.indexOf(statePopped.parentPt);
					prefix = new ArrayList<>(prefix.subList(0, parentIndex + 1));

					// add the current state right after parent in prefix list
					prefix.add(statePopped);

					// pushes successors into stack only if their depth is 
					// less than the cutoff
					if(statePopped.depth < cutoff){
						State[] successors = statePopped.getSuccessors();

						for(int i = 0; i < successors.length; i++){
							boolean nodeExists = false;

							for(int j = 0; j < prefix.size(); j++){
								if(successors[i].equals(prefix.get(j)))
									nodeExists = true;
							}
							// push node to stack if it doesn't already exist
							// in the prefix list
							if(!nodeExists)
								stack.push(successors[i]);
						}
					}

					// checks if current stack size is the maximum
					if(maxStackSize < stack.size())
						maxStackSize = stack.size();
				}

				if (option != 5)
					break;
				else{
					// checks if the last state in the prefix is the goal state
					if(prefix.get(prefix.size() - 1).isGoalState()){
						// prints solution from the initial state to the goal state
						for(int i = 0; i < prefix.size(); i++)
							prefix.get(i).printState(2);

						// prints the number of times goal-checked was performed
						// prints maximum number of states in your DFS stack at the
						// moment of the search
						System.out.println("Goal-check " + goalChecked);
						System.out.println("Max-stack-size " + maxStackSize);

						break;
					}
					else{
						// increment cutoff so that while-loop goes on till 
						// goal-state is reached
						cutoff++;
					}
				}
			}
		}
	}
}