package TheOdds;

import javax.sound.midi.Sequencer;

public class MainActivity {
	public static void main(String[] argv){

		try{
			
			System.out.print("Resolving the files in the folder data.\n");
			FileNameResolver fnr = new FileNameResolver("data");
			SingleTracker singleTracker = new SingleTracker(fnr.getFiles());
			System.out.print("Generating the track...");
			Sequencer sequencer = singleTracker.produceTrack();
			System.out.print("Playing...");
			sequencer.start();
		
		}catch(Exception e){
			System.out.print("can't do it \n");
		}
	}
}
