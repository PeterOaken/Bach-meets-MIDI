package TheOdds;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;


public class Player{
	
	String path;
	Sequencer sequencer;
	
	public Player(String filePath){
		path = filePath;
		
		try{
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			InputStream is = new BufferedInputStream(new FileInputStream(new File(path)));
			sequencer.setSequence(is);
			
		}catch(Exception e){
			System.out.print("Can't play the midi song");
			e.printStackTrace();
		}
	}
	
	public void play(){
		sequencer.start();
	}
	
	public void stop(){
		sequencer.stop();
	}
	
}