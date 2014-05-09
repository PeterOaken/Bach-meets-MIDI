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
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class ComplexAnalyzer extends Analyzer{
	
	ArrayList<File> files = new ArrayList<File>();
	int numFiles;
	
	HashMap<String, HashMap<String, Integer>> histogram;
	HashMap<String, Chord> chordList;
	
	Sequencer trainSequencer;
	
	public ComplexAnalyzer(ArrayList<File> f){
		try{
			files = f;
			numFiles = f.size();
			histogram = new HashMap<String, HashMap<String, Integer>>();
			chordList = new HashMap<String, Chord>();
			trainSequencer = MidiSystem.getSequencer();
			trainSequencer.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public void  analyze(){			
		try {			

			for(int i=0; i<numFiles; i++){
				File f = files.get(i);
				InputStream traingInputStrem = new BufferedInputStream(new FileInputStream(f.getPath()));
				trainSequencer.setSequence(traingInputStrem);
				Sequence trainSequence = trainSequencer.getSequence();
				Track[] trainTracks = trainSequence.getTracks();
								
				for (int j=0; j<trainTracks.length;j++){
					Track singleTrainTrack = trainTracks[j];
					ArrayList<Chord> noteLists = trackAnalyze(singleTrainTrack);
					updateHistogram(noteLists);
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
	
	private ArrayList<Chord> trackAnalyze(Track track){
		ArrayList<Chord> result = new ArrayList<Chord>();

		long tick = -1;
		int t = 0;
		
		while(t<track.size()-1){
			try{
				ShortMessage tmp = (ShortMessage) track.get(t).getMessage();
				long newTick = track.get(t).getTick();
				if (tmp.getCommand()>=144 && tmp.getCommand()<=159 && newTick != tick){
					Chord newChord = new Chord(tick);
					tick = newTick;
					newChord.addMessage(tmp);
					int size = result.size();
					if(size != 0){
						result.get(size-1).setEnd(tick);
					}
					result.add(newChord);
				}else if (tmp.getCommand()>=144 && tmp.getCommand()<=159 && newTick == tick){
					result.get(result.size()-1).addMessage(tmp);
				}
			}catch(Exception e){}
			t++;
		}		
		return result;
	}

	private void updateHistogram(ArrayList<Chord> chords) {
		Chord prev, next;	
		for(int i=0; i<chords.size()-1; i++){
			prev = chords.get(i);
			next = chords.get(i+1);
			String previousChord = prev.toString();
			String nextChord = next.toString();
			if(!chordList.containsKey(previousChord)){
				chordList.put(previousChord, prev);
			}
			if(!chordList.containsKey(nextChord)){
				chordList.put(nextChord, next);
			}
			if(histogram.containsKey(previousChord)){
				if(histogram.get(previousChord).containsKey(nextChord)){
					int count = histogram.get(previousChord).get(nextChord);
					histogram.get(previousChord).put(nextChord, count+1);
				}else{
					histogram.get(previousChord).put(nextChord, 1);
				}
			}else{
				HashMap<String, Integer> notesAfter = new HashMap<String, Integer>();
				histogram.put(previousChord, notesAfter);
			}
		}
	}
	
	private void printHistogram(){
		for(String s: histogram.keySet()){
			System.out.print(s +" : {");
			HashMap<String, Integer> hashValue = histogram.get(s);
			for(String j: hashValue.keySet()){
				System.out.print(j+" : "+hashValue.get(j)+", ");
			}
			System.out.print("} \n");
		}
	}
	
	public Chord pickRandomWeighted(Chord chord){
		String next ="";
		double total = 0;
		String chordString = chord.toString();
		if(histogram.get(chordString).isEmpty()){
			return null;
		}
		for(Integer i: histogram.get(chordString).values()){
			total+=i;
		}
		
		int random = (int) (Math.random()*(total+1));

		for (Entry<String, Integer> entry : histogram.get(chordString).entrySet()){
		    random -= entry.getValue();
		    if (random <= 0.0d){
		        next = entry.getKey();
		        break;
		    }
		}
		if(next.equals("")){
			return null;
		}
		return chordList.get(next);
	}
	
	public Chord randomChord(){
		int random = (int)(Math.random()*chordList.size());
		ArrayList<String> allchords = new ArrayList<String>(chordList.keySet());
		return chordList.get(allchords.get(random));
	}
}
