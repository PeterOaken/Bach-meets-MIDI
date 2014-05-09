package TheOdds;

import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.midi.ShortMessage;

public class Chord{
	
	ArrayList<ShortMessage> chord;
	long start;
	long end;

	
	public Chord(long tick){
		chord = new ArrayList<ShortMessage>();
		start = tick;
		end = 0;
	}
	
	public void addMessage(ShortMessage sm){
		chord.add(sm);
	}
	
	public long duration(){
		return end-start;
	}
	
	public String toString(){
		int l = chord.size();
		int[] notes = new int[l];
		for(int i=0; i<l; i++){
			notes[i] = chord.get(i).getData1();
		}
		Arrays.sort(notes);
		String result = "[";
		for(int j=0; j<l-1; j++){
			result = result + notes[j] +", ";
		}
		result = result +notes[l-1] +"]";
		return result;
	}
	
	public int size(){
		return chord.size();
	}
	
	public void setEnd(long tick){
		end = tick;
	}
	
	public ArrayList<ShortMessage> getChord(){
		return chord;
	}
}