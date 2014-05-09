package TheOdds;

import java.io.File;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiFileFormat;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MainActivity {
	
	public static void main(String[] argv){
		FileNameResolver fnr = new FileNameResolver("data");
		
		SimpleAnalyzer analyzer = new SimpleAnalyzer(fnr.getFiles());
		analyzer.analyze();
		
		ComplexAnalyzer complexAnalyzer = new ComplexAnalyzer(fnr.getFiles());
		complexAnalyzer.analyze();
		
		MainActivity mini = new MainActivity();
		
		//mini.play(0);
		//mini.play(0, analyzer);
		mini.play(0, complexAnalyzer);
		
	}
	
	
	public void play(int instrument, SimpleAnalyzer analyzer){
		try{
			Sequencer player = MidiSystem.getSequencer();
			player.open();
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
						
			ShortMessage first = new ShortMessage();
			first.setMessage(192, 1, instrument, 0);
			MidiEvent changeInstrument = new MidiEvent(first, 1);
			track.add(changeInstrument);

			simpleCompose(track, analyzer);
			
			player.setSequence(seq);
			player.start();

//			File f = new File("simpleBach.midi");
//			MidiSystem.write(seq, 1, f);
						
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void play(int instrument){
		try{
			Sequencer player = MidiSystem.getSequencer();
			player.open();
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
						
			ShortMessage first = new ShortMessage();
			first.setMessage(192, 1, instrument, 0);
			MidiEvent changeInstrument = new MidiEvent(first, 1);
			track.add(changeInstrument);

			randomCompose(track);
			
			player.setSequence(seq);
			player.start();

//			File f = new File("random.midi");
//			MidiSystem.write(seq, 1, f);	
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void play(int instrument, ComplexAnalyzer analyzer){
		try{
			Sequencer player = MidiSystem.getSequencer();
			player.open();
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
						
			ShortMessage first = new ShortMessage();
			first.setMessage(192, 1, instrument, 0);
			MidiEvent changeInstrument = new MidiEvent(first, 1);
			track.add(changeInstrument);

			complexCompose(track, analyzer);
			
			player.setSequence(seq);
			player.start();
			
//			File f = new File("complexBach.midi");
//			MidiSystem.write(seq, 1, f);
			
						
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void simpleCompose(Track track, SimpleAnalyzer analyzer){
		try{
			ShortMessage a = null;
			ShortMessage b = null;
			MidiEvent noteOn = null;
			MidiEvent noteOff = null;
			int tick = 0;
			
			int note = (int) (Math.random()*128);
			a = new ShortMessage();
			a.setMessage(144, 1, note, 100);
			noteOn = new MidiEvent(a, tick);
			track.add(noteOn);
			
			b = new ShortMessage();
			b.setMessage(128,1,note,100);
			noteOff = new MidiEvent(b, tick+1);
			track.add(noteOff);
			tick++;
			
			while(tick < 200){
	
				note = analyzer.pickRandomWeighted(note);
				a = new ShortMessage();
				a.setMessage(144, 1, note, 100);
				noteOn = new MidiEvent(a, tick);
				track.add(noteOn);
				
				b = new ShortMessage();
				b.setMessage(128,1,note,100);
				noteOff = new MidiEvent(b, tick+1);
				track.add(noteOff);
				tick+=2;
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	public void randomCompose(Track track){		
		try {
			ShortMessage a = null;
			ShortMessage b = null;
			MidiEvent noteOn = null;
			MidiEvent noteOff = null;
			int tick = 0;
			int note;
			while(tick < 200){
				
				note = (int) (Math.random()*128);
				a = new ShortMessage();
				a.setMessage(144, 1, note, 100);
				noteOn = new MidiEvent(a, tick);
				track.add(noteOn);
				
				b = new ShortMessage();
				b.setMessage(128,1,note,100);
				noteOff = new MidiEvent(b, tick+1);
				track.add(noteOff);
				tick+=2;
			}
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	public void complexCompose(Track track, ComplexAnalyzer analyzer){
		try{
			long tick = 1;
			Chord chord = analyzer.randomChord();
			ShortMessage a, b;
			MidiEvent noteOn, noteOff;
			while(tick < 1000){	
				ArrayList<ShortMessage> notes = chord.getChord();
				
				for(int i=0; i<notes.size(); i++){
					a = notes.get(i);
					noteOn = new MidiEvent(a, tick);
					track.add(noteOn);
					
					b = new ShortMessage();
					b.setMessage(a.getCommand()-16, a.getChannel(), a.getData1(), a.getData2());
					noteOff = new MidiEvent(b, tick+chord.duration());
					track.add(noteOff);
				}
				
				tick = tick+=2;
				chord = analyzer.pickRandomWeighted(chord);
				if(chord == null){
					System.out.println("no chords after");
					chord = analyzer.randomChord();
				}

			}
		}catch(Exception e){e.printStackTrace();}
	}
}
