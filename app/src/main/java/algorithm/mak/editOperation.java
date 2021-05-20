package algorithm.mak;

public class editOperation  {

	public int operation; // 1 = Insert , 2 = Delete , 3 = Update
	public char value;
	public int source,//Index in string B
			destination; // Index in string A
	
	
	public editOperation(int operation, int source, int destination, char value) {
		this.operation = operation;
		this.source = source;
		this.destination = destination;
		this.value = value;
	}
	
	public editOperation( int destination, char value) { // Delete
		this.operation = 2;
		this.destination = destination;
		this.value = value;
	}

	public String toString() {
		if(operation == 1) return "Ins(A" + destination + ", " + value + ")" ; // Insert
		else if(operation == 2) return "Del(A" + destination + ", " + value +")"; // Delete
		else return "Upd(A" + destination + ", B" + source + ", " + value +")";	// Update
	}



}
