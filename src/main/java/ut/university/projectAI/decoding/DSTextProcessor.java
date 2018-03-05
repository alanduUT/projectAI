package ut.university.projectAI.decoding;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import ut.university.projectAI.model.SentenceUnit;

public class DSTextProcessor implements DSGeneralAnalyzer<String>{
	private final String text;
	private final Queue<Integer> queue = new LinkedList<>();
	private final Map<Integer, List<String>> extractedWordsBySentence = new HashMap<>();
	private final Map<Integer, List<String>> extractedMoviesBySentence = new HashMap<>();
	private final RegexParser regPar = new RegexParser();
	private static final Logger logger = Logger.getLogger(DSTextProcessor.class.getName());
	
	public DSTextProcessor(String text) {
		this.text = text.replace("\"", " \" ");
	}
	
	@Override
	public void analyze() {
		logger.log(Level.INFO, "************************************");
		logger.log(Level.INFO, "Start DSTextProcessorImpl");
		logger.log(Level.INFO, "************************************");
		extractStringTokensAndMovies();
	}
	
	private void extractStringTokensAndMovies() {
		boolean isMovieExtracting = false;
		List<String> movieParts = new ArrayList<>();
		int sentenceNum = 0;
		initNewSentence(sentenceNum);
		for(String strTok : regPar.getStringTokens(text)) {
			logger.log(Level.INFO, "************************************");
			logger.log(Level.INFO, "String token: " + strTok);
			strTok = strTok.toLowerCase();
			if(isMovieExtracting) {
				if(regPar.isDoubleQuote(strTok)) {
					extractedMoviesBySentence.get(sentenceNum).add(String.join(" ", movieParts));
					movieParts.clear();
					isMovieExtracting = false;
				} else {
					movieParts.add(strTok);
				}
			} else if(regPar.isDoubleQuote(strTok)) {
				isMovieExtracting = true;
			} else if(regPar.isSentenceSplitter(strTok)) {
				initNewSentence(++sentenceNum);
			} else if (regPar.isWord(strTok)) {
				extractedWordsBySentence.get(sentenceNum).add(strTok);
			}
			logger.log(Level.INFO, "************************************");
		}
		logger.log(Level.INFO, "************************************");
		logger.log(Level.INFO, "Extracted words: " + extractedWordsBySentence);
		logger.log(Level.INFO, "Extracted movies: " + extractedMoviesBySentence);
		logger.log(Level.INFO, "************************************");
	}
	
	private void initNewSentence(int sentenceNum) {
		extractedMoviesBySentence.put(sentenceNum, new ArrayList<>());
		extractedWordsBySentence.put(sentenceNum, new ArrayList<>());
		queue.offer(sentenceNum);
	}
	
	@Override
	public SentenceUnit<String> getNext() {
		Integer sentenceNum = queue.poll();
		if(sentenceNum == null) {
			throw new RuntimeException("#hasMore method should be used to avoid that.");
		}
		List<String> extractedMovies = extractedMoviesBySentence.remove(sentenceNum);
		List<String> extractedWords = extractedWordsBySentence.remove(sentenceNum);
		return new SentenceUnit<>(extractedMovies, extractedWords);
	}
	
	@Override
	public boolean hasMore() {
		return !queue.isEmpty();
	}
	
	private class RegexParser {
		private final Tokenizer tokenizer;
		private static final String EN_TOKEN = "src/main/resources/en-token.bin";
		
		{
			try(InputStream is = new BufferedInputStream(new FileInputStream(EN_TOKEN))) {
				tokenizer = new TokenizerME(new TokenizerModel(is));
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Tokenizer could not be initialised");
			}
		}
		
		private List<String> getStringTokens(String text) {
			return Arrays.asList(tokenizer.tokenize(text));
		}
		
		private boolean isSentenceSplitter(String str) {
			return ".".equals(str) || "!".equals(str) || "?".equals(str);
		}
		
		private boolean isWord(String str) {
			return Pattern.matches("\\w+", str); 
		}
		
		private boolean isDoubleQuote(String str) {
			return "\"".equals(str);
		}
	}
}
