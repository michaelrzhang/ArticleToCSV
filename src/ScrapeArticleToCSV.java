import com.fiskkit.Fetcher;
import java.util.*;
import java.io.*;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import com.csvreader.CsvWriter;


public class ScrapeArticleToCSV{
	public static void main(String[] args){
		//Take a HTTP url as input
		Scanner inp = new Scanner(System.in);
		String outputFile = "sentences.csv";
		String link_target = "";
		List<String> text; 
		InputStream modelInput = null;
		SentenceModel model = null;
		SentenceDetectorME sentenceDetector = null;
		List<String[]> sentences = new ArrayList<String[]>();

		while (true){
			System.out.print("Enter an url for a news article. Enter . to stop: ");
			link_target = inp.next();
			if (link_target.equals("."))
				break;
			try{
				//Creates a model for sentence detector
				if (modelInput == null){
					modelInput = new FileInputStream("en-sent.bin");
					model = new SentenceModel(modelInput);
					sentenceDetector = new SentenceDetectorME(model);
					System.out.println("initilized modelInput");
				}

				//Stores article into a List of string arrays, with each array a sentence
				text = Fetcher.pullAndExtract(link_target);
				for (int i=0;i<text.size();i++){
					sentences.add(sentenceDetector.sentDetect(text.get(i)));
				}
				sentences.add(sentenceDetector.sentDetect("Next Article.")); // Seperates each article in output file
			}
			catch (IOException e) {
			 	e.printStackTrace();
			}
			finally {
			  	if (modelInput != null) {
					try {
				  		modelInput.close();
					}
				catch (IOException e) {
					}
			  	}
			}
		}
		
		//Write sentences into CSV format
		try{
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
			for (int i=0; i < sentences.size(); i++){
				for(int j=0; j < sentences.get(i).length; j++){
					// System.out.println(sentences.get(i)[j]);  // For debugging
					csvOutput.write(sentences.get(i)[j]);
					csvOutput.endRecord();  // new line
				}
			}
			csvOutput.close();
			System.out.println(". detected. Stopping program");
		}
		catch (IOException e) {
			 	e.printStackTrace();
			}
		
	}

	

}
