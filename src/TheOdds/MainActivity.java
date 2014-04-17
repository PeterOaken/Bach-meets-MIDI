package TheOdds;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;

public class MainActivity {
	public static void main(String[] argv){
		//Player player = new Player("data/bjsbb21.mid");
		//player.play();
		try{
		FileNameResolver fnr = new FileNameResolver("data");
		ArrayList<File> array = fnr.getFiles();
		for(int i=0;i<array.size();i++){
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			InputStream is = new BufferedInputStream(new FileInputStream(array.get(i)));
			sequencer.setSequence(is);
			Sequence s = sequencer.getSequence();
			Track[] t = s.getTracks();
			System.out.print("length is "+t.length);
		}
		}catch(Exception e){
			System.out.print("can't do it \n");
		}
	}
}
