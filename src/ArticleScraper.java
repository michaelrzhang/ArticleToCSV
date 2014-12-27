import com.fiskkit.Fetcher;
import java.util.*;
import java.io.*;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import com.csvreader.CsvWriter;

public class ArticleScraper{
	private boolean multiple_urls;
	private List<String> input_urls;
	private String input_url;
	private static String outputFile;
	private static SentenceDetectorME sentenceDetector = null;
	private static List<String> text; 
	private static List<String[]> sentences = new ArrayList<String[]>();

	public ArticleScraper(String name, String url){
		outputFile = name;
		input_url = url;
		multiple_urls = false;
		
	}

	public ArticleScraper(String name, List<String> urls){
		outputFile = name;
		input_urls = urls;
		multiple_urls = true;
		
	}

	public static void scrapeArticles(ArticleScraper x){
	/* Static method that takes ArticleScraper objects and write to designated
	CSV output file.
	*/
		if (sentenceDetector == null){
			sentenceDetector = createSentenceDetector();
		}
		if (x.multiple_urls == false){
			text = Fetcher.pullAndExtract(x.input_url);
			parseSentences(text);
		}
		else{
			for(int i=0; i < x.input_urls.size();i++){
				text = Fetcher.pullAndExtract(x.input_urls.get(i));
				parseSentences(text);
			}
		}
		writeCsvFile(outputFile, sentences);
		//After writing out to file, reset sentences to empty.
		sentences.clear();  
	}

	public static void parseSentences(List<String> article){
		for (int i=0;i<article.size();i++){
			sentences.add(sentenceDetector.sentDetect(text.get(i)));
		}
		sentences.add(sentenceDetector.sentDetect("-----End of article."));
	}

	private static SentenceDetectorME createSentenceDetector(){
	/* Creates a sentence detector object with method sentDetect. sentDetect 
	detects sentences in an input stream. Returns null if something goes wrong.
	*/
	    System.out.println("Created SentenceDetector");
		try{
			InputStream modelInput = new FileInputStream("en-sent.bin");
			SentenceModel model = new SentenceModel(modelInput);
			SentenceDetectorME newSentenceDetector = new SentenceDetectorME(model);
			
			modelInput.close();
			return newSentenceDetector;
		}
		catch (IOException e) {
			e.printStackTrace();
			}
		return null;
	}

	private static void writeCsvFile(String filename, List<String[]> content){
	/* Takes in a list of String arrays CONTENT and writes to FILENAME 
	*/
		CsvWriter csvOutput = null;
		try{
			csvOutput = new CsvWriter(new FileWriter(filename, true), ',');
			for (int i=0; i < content.size(); i++){
				for(int j=0; j < content.get(i).length; j++){
					// System.out.println(sentences.get(i)[j]);  // For debugging
					csvOutput.write(content.get(i)[j]);
					csvOutput.endRecord();  // new line
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			}
		finally{
			if (csvOutput != null) {
		  		csvOutput.close();
		  	}		
		}
	}
	

}
