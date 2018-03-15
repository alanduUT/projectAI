package ut.university.projectAI;

import java.io.BufferedInputStream;
import ut.university.projectAI.processor.MainProcessor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import ut.university.projectAI.networking.RequestPerformer;
import ut.university.projectAI.networking.RequestPerformerImpl;

public class Test {
	private static Tokenizer tokenizer;
	private static final String EN_TOKEN = "src/main/resources/en-token.bin";
	private static final String REGEX = "<strong>Lemma</strong>:[\\w\\s]+<br />";
	private static final String THESAURUS_API_KEY = "5c4684a65e8ab0b05ff5feebab78a22f";
	private static final String THESAURUS_URI = "http://words.bighugelabs.com/api/2/";
	public static void main(String[] args) {
		/*try(InputStream is = new BufferedInputStream(new FileInputStream(EN_TOKEN))) {
			tokenizer = new TokenizerME(new TokenizerModel(is));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Tokenizer could not be initialised");
		}
		System.out.println(Arrays.asList(tokenizer.tokenize("Kill Bill: Vol. 1")));*/
		/*RequestPerformer rp = RequestPerformerImpl.instance();
		System.out.println(rp.performRequest("http://morphological.org/?words=boys"));*/
		/*Pattern pattern = Pattern.compile(REGEX);
		Matcher m = pattern.matcher("<strong>Lemma</strong>: BOY<br />");		
		if(m.find()) {
			System.out.println(m.group(0));
		}
		System.out.println("<strong>Lemma</strong>: BOY<br />".substring("<strong>Lemma</strong>:".length(), "<strong>Lemma</strong>: BOY<br />".length() - "<br />".length()));
		System.out.println(REGEX.length());
		System.out.println("<br />".length());
		System.out.println("<strong>Lemma</strong>: BOY<br />".indexOf('b'));*/
		
		/*String text = "imdb movie title actors I would like to know \"Sixth Sense\". In addition naming date";
		while(true){
			DSAnalyzerImpl im = new DSAnalyzerImpl(text);
			try {
				im.analyze();
			} catch(Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException("NOT SO GOOD");
			}
			break;
		}*/
		String text1 = "imdb movie title metascore director and language with actors I would like to know \"The Sixth Sense\". In addition naming date with languages and actors \"Interstellar\", \"The Sixth Sense\". \"The Sixth Sense\" metascore.";
		String text2 = "imdb metascore director and language with performers I would like to know \"The Sixth Sense\". In addition naming date with languages and actors \"Interstellar\", \"The Sixth Sense\". \"The Sixth Sense\" metascore.";
		while(true){
			System.out.println(new MainProcessor().getResponse(text2));
			try {
				Thread.sleep(10_000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*for(String text : new String[]{text1, text2}){
			double x = System.currentTimeMillis();
			System.out.println(new MainProcessor().getResponse(text));
			double y = System.currentTimeMillis();
			System.out.println(y - x);
			try {
				Thread.sleep(10_000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		/*RequestPerformer rp = RequestPerformerImpl.instance();
		String res = rp.performRequest(THESAURUS_URI + THESAURUS_API_KEY + "/" + "love" + "/json");
		JSONObject json = new JSONObject(res);
		json = json.getJSONObject("noun");
		JSONArray jsonAr = json.getJSONArray("syn");
		System.out.println(jsonAr);*/
		/*RequestPerformer rp = RequestPerformerImpl.instance();
		String res = rp.performRequest("http://www.omdbapi.com/?t=Interstellar");
		System.out.println(res);*/
	}
}
