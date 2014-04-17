package TheOdds;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class OneSongTrainer {
	
	Sequence sequence;
	long tickLength;
	
	public OneSongTrainer(File f){
		try{
			
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			InputStream is = new BufferedInputStream(new FileInputStream(f));
			sequencer.setSequence(is);
			sequence = sequencer.getSequence();
			tickLength = sequence.getTickLength();
			
		}catch(Exception e){
			System.out.print("Can't get the sequence object from one song.");
			e.printStackTrace();
		}
	}
	
	
}
