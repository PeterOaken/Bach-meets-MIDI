package TheOdds;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;

public class SingleTracker {
	
	ArrayList<File> files = new ArrayList<File>();
	int numFiles;
	
	public SingleTracker(ArrayList<File> f){
		files = f;
		numFiles = f.size();
	}
	
	
	public Sequencer produceTrack(){
		Sequencer sequencer = null;
		try {
			Sequence s = new Sequence(Sequence.PPQ, 1);
		
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			
			sequencer.setSequence(s);

			Track track= s.createTrack();
			
			int index = 0;
			MidiEvent me = null;
			
			Sequencer fileSequencer = MidiSystem.getSequencer();
			fileSequencer.open();
			
			//while (s.getMicrosecondLength()/1000 < 2){
				index = (int) (Math.random()*numFiles);
				File f = files.get(index);
				InputStream is = new BufferedInputStream(new FileInputStream(f.getPath()));
				sequencer.setSequence(is);
				Sequence fileSequence = sequencer.getSequence();
				Track[] tracks = fileSequence.getTracks();
								
				while(true){
					int tracknumber = (int) (Math.random()*tracks.length);
					Track t = tracks[tracknumber];
					if (me==null){
						int i = (int) (Math.random()*t.size());
						me = t.get(i);
						track.add(me);
						break;
					}else{
						MidiEvent next = nextMidiEvent(t, me);
						if(next != null){
							me = next;
							track.add(me);
							break;
						}
					}
				}
				System.out.print(me.getTick()+"\n");
			//}
			
			
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sequencer;
	}
	
	public MidiEvent nextMidiEvent(Track t, MidiEvent me){
		for(int j=0; j<t.size()-1; j++){
			if(t.get(j).equals(me)){
				return t.get(j+1);
			}
		}
		return null;
	}
	
}
