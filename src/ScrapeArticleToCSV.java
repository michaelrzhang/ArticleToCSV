import com.fiskkit.Fetcher;
import java.util.*;
import java.io.*;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import com.csvreader.CsvWriter;


public class ScrapeArticleToCSV{
	public static void main(String[] args) {
		//Take a HTTP url as input
		Scanner inp = new Scanner(System.in);
		String outputFile = "sentences.csv";
		System.out.print("Enter a url for a news article: ");
		String link_target = inp.next();
		List<String> text = Fetcher.pullAndExtract(link_target);
		InputStream modelInput = null;
		SentenceModel model;
		try{
			//Creates a model for sentence detector
			modelInput = new FileInputStream("en-sent.bin");
			model = new SentenceModel(modelInput);
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);

			//Stores article into a List of string arrays, with each array a sentence
			List<String[]> sentences = new ArrayList<String[]>();
			for (int i=0;i<text.size();i++){
				sentences.add(sentenceDetector.sentDetect(text.get(i)));
			}

			CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
			for (int i=0; i < sentences.size(); i++){
				for(int j = 0; j < sentences.get(i).length; j++){
					// System.out.println(sentences.get(i)[j]);  // For debugging
					csvOutput.write(sentences.get(i)[j]);
					csvOutput.endRecord();  // new line
				}
			}
			csvOutput.close();
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

	

}
