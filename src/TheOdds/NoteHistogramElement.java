package TheOdds;

public class NoteHistogramElement{
	
	int note;
	public long count;
	
	public NoteHistogramElement(int n){
		note = n;
		count =1;
	}
	
	public void incrementCount(){
		count++;
	}
}