package TheOdds;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class SimpleAnalyzer extends Analyzer{
	
	ArrayList<File> files = new ArrayList<File>();
	int numFiles;
	
	HashMap<Integer, HashMap<Integer, Integer>> histogram;
	
	Sequencer trainSequencer;
	
	public SimpleAnalyzer(ArrayList<File> f){
		try{
			files = f;
			numFiles = f.size();
			histogram = new HashMap<Integer, HashMap<Integer, Integer>>();
			trainSequencer = MidiSystem.getSequencer();
			trainSequencer.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public void  analyze(){			
		try {			

			ShortMessage trainMidiMessage = null;
			ShortMessage next =null; 
			for(int i=0; i<numFiles; i++){
				File f = files.get(i);
				InputStream traingInputStrem = new BufferedInputStream(new FileInputStream(f.getPath()));
				trainSequencer.setSequence(traingInputStrem);
				Sequence trainSequence = trainSequencer.getSequence();
				Track[] trainTracks = trainSequence.getTracks();
								
				for (int j=0; j<trainTracks.length;j++){
					Track singleTrainTrack = trainTracks[j];
					int trackSize = singleTrainTrack.size();
					int t =0;
					while(t<trackSize-1){
						if (trainMidiMessage == null){
							try{
								ShortMessage tmp = (ShortMessage) singleTrainTrack.get(t).getMessage();	
								if (tmp.getCommand()>=144 && tmp.getCommand()<=159){
									trainMidiMessage = tmp;
								}
							}catch(Exception e){}
							
						}else{
							try{
								next = (ShortMessage) singleTrainTrack.get(t).getMessage();
								if (next.getCommand()>=144 && next.getCommand()<=159){
									int note = trainMidiMessage.getData1();
									int nextNote = next.getData1();
									updateHistogram(note, nextNote);
									trainMidiMessage = next;
								}
							}catch(Exception e){}
							
						}	
						t++;
					}
				}

			}
			//printHistogram();
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}


	private void updateHistogram(int note, int nextNote) {
		if (histogram.containsKey(note) && histogram.get(note).containsKey(nextNote)){
			int count = histogram.get(note).get(nextNote);
			histogram.get(note).put(nextNote, count+1);
		}else if (histogram.containsKey(note)){
			histogram.get(note).put(nextNote, 1);
		}else{
			HashMap<Integer, Integer> empty = new HashMap<Integer, Integer>();
			histogram.put(note, empty);
		}		
	}
	
	private void printHistogram(){
		for(Integer i: histogram.keySet()){
			System.out.print(i +" : {");
			HashMap<Integer, Integer> hashValue = histogram.get(i);
			for(Integer j: hashValue.keySet()){
				System.out.print(j+" : "+hashValue.get(j)+", ");
			}
			System.out.print("} \n");
		}
	}
	
	public int pickRandomWeighted(int note){
		int result =0;
		double total = 0;
		for(Integer i: histogram.get(note).values()){
			total+=i;
		}
		int random = (int) (Math.random()*total+1);

		for (Entry<Integer, Integer> entry : histogram.get(note).entrySet()){
		    random -= entry.getValue();
		    if (random <= 0.0d){
		        result = entry.getKey();
		        break;
		    }
		}
		return result;
	}
}
