package ut.university.projectAI.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Frame {
	
	private final Queue<SentenceUnit<Tokens>> queue = new LinkedList<>();
	
	public boolean hasFrames(){
		return !queue.isEmpty();
	}
	
	public boolean isSuitable(SentenceUnit<Tokens> unit){
		SentenceUnit<Tokens> queued = queue.peek();
		if(queued != null){
			if(queued.getMovies().isEmpty() && !unit.getMovies().isEmpty()){
				return true;
			}
			if(queued.getWords().isEmpty() && !unit.getWords().isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	public SentenceUnit<Tokens> combine(SentenceUnit<Tokens> unit){
		if(isSuitable(unit)){
			SentenceUnit<Tokens> queued = queue.poll();
			queued.getMovies().addAll(unit.getMovies());
			queued.getWords().addAll(unit.getWords());
			return queued;
		} else {
			return null;
		}
	}
	
	public SentenceUnit<Tokens> getFirst() {
		return queue.peek();
	}
	
	public void addToFrame(List<SentenceUnit<Tokens>> sentUnits){
		for(SentenceUnit<Tokens> unit : sentUnits){
			queue.offer(unit);
		}
	}
}
