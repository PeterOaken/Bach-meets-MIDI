package TheOdds;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class SimpleComposer extends Composer{

	Sequence seq;
	
	@Override
	public void play(int instrument, Analyzer an) {
		SimpleAnalyzer analyzer = (SimpleAnalyzer) an;
		try{
			Sequencer player = MidiSystem.getSequencer();
			player.open();
			seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
						
			ShortMessage first = new ShortMessage();
			first.setMessage(192, 1, instrument, 0);
			MidiEvent changeInstrument = new MidiEvent(first, 1);
			track.add(changeInstrument);

			compose(track, analyzer);
			
			player.setSequence(seq);
			player.start();
						
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void compose(Track track, Analyzer an) {
		SimpleAnalyzer analyzer = (SimpleAnalyzer) an;
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

	@Override
	public void write(String path) {
		
		try {
			File f = new File(path);
			MidiSystem.write(seq, 1, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}