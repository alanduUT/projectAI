package ut.university.projectAI.decoding;

import java.util.List;

import ut.university.projectAI.model.SentenceUnit;
import ut.university.projectAI.model.Tokens;

public interface DSAnalyzer {
	
	List<SentenceUnit<Tokens>> analyze() throws Exception;

}
