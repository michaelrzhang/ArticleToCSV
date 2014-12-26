import com.fiskkit.Fetcher;
import java.util.*;
import java.io.*;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import com.csvreader.CsvWriter;


public class ScrapeArticleToCSV{
	public static void main(String[] args){
		//Take HTTP url as inputs
		Scanner inp = new Scanner(System.in);
		String outputFile = "sentences.csv";
		String link_target = "";
		List<String> text; 
		SentenceDetectorME sentenceDetector = null;
		List<String[]> sentences = new ArrayList<String[]>();

		while (true){
			System.out.print("Enter an url for a news article. Enter . to stop: ");
			link_target = inp.next();
			if (link_target.equals("."))
				break;
			
			//Creates a model for sentence detector if needed
			if (sentenceDetector == null){
				sentenceDetector = createSentenceDetector();
				if (sentenceDetector == null){
					break;
				}
			}

			//Stores article into a List of string arrays, with each array a sentence
			text = Fetcher.pullAndExtract(link_target);
			for (int i=0;i<text.size();i++){
				sentences.add(sentenceDetector.sentDetect(text.get(i)));
			}
			sentences.add(sentenceDetector.sentDetect("-----End of article.")); //Seperates each article in output file
		}		
		//Write sentences into CSV format
		writeCsvFile(outputFile, sentences);
		if (sentenceDetector == null){
			System.out.println("Sentence detector not created.");
			}
		else{
			System.out.println(". detected. Stopping program");
		}
	}

	public static SentenceDetectorME createSentenceDetector(){
	/* Creates a sentence detector object with method sentDetect. sentDetect 
	detects sentences in an input stream. Returns null if something goes wrong.
	*/
		try{
			InputStream modelInput = new FileInputStream("en-sent.bin");
			SentenceModel model = new SentenceModel(modelInput);
			SentenceDetectorME newSentenceDetector = new SentenceDetectorME(model);
			System.out.println("Created SentenceDetector");
			modelInput.close();
			return newSentenceDetector;
		}
		catch (IOException e) {
			e.printStackTrace();
			}
		return null;
	}

	public static void writeCsvFile(String filename, List<String[]> content){
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
