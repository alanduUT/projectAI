package ut.university.projectAI.decoding;

import ut.university.projectAI.model.SentenceUnit;

public interface DSGeneralAnalyzer<T> {
	void analyze();
	boolean hasMore();
	SentenceUnit<T> getNext();
}
