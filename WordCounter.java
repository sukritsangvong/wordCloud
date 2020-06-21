import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
* Author: Ryan Choi , PJ Sangvong
* WordCounter class to read files and create the output html file
* that will create the word cloud based on the frequency of the words
* in the text file.
*/
public class WordCounter{
  private Scanner scan;
  private Set<String> stopWordsSet;
  private List<String> textFileWordsList;
  private static Character[] punctuation = {',','.','?',':',';','\"','\'','!','~','/','{','}','[',']',
                                           '(',')','#','%','^','-', '*', '$', '_'};
  private List<Character> punctuationList = new ArrayList<Character>();

  /**
  * A constructor method to open and store the content of the file
  * the construtor will initialize the stopWordsSet and the punctuationList
  *@param textFileName is the name of the file that will be processed
  */
  public WordCounter(String textFileName){
    this.stopWordsSet = new HashSet<String>();
    this.textFileWordsList = new ArrayList<String>();
    String line;
    //loads the StopWords file
    Scanner stopScanner = loadFile("StopWords.txt");

    //creates punctuationList to filter out the punctuation attatched to the words
    for (Character eachPunc: this.punctuation){
      punctuationList.add(eachPunc);
    }
    //opens the StopWords.txt
    if(stopScanner == null){//checks for error
      System.err.println("StopWords file does not exist.");
    }else{
      while(stopScanner.hasNext()){
        line = stopScanner.nextLine();
        stopWordsSet.add(line.toLowerCase());
      }
    }

    //loads the textFileName file
    Scanner textFileScanner = loadFile(textFileName);
    if(textFileScanner == null){//checks for error
      System.err.println("textFileName does not exist.");
    }else{
      //reads lines from the textFileName file
      while(textFileScanner.hasNext()){
        String lineTextFile = textFileScanner.nextLine();
        String[] sentence = lineTextFile.split(" ");
        //gets each of the word from the sentence
        for(int i = 0; i < sentence.length; i++){
          String currentSentence = sentence[i].toLowerCase();
          //filters the StopWords and the empty line
          if(!stopWordsSet.contains(currentSentence) &&
              !currentSentence.equals("")){
            //filers the word with length 1 out to else case
            if (currentSentence.length() > 1){
              String tmpString = currentSentence;
              //variables that will be used to cut punctuations attatched to words
              int idxFront = 0;
              int idxBack = currentSentence.length() - 1;

              //finds the back index that excludes the punctuations
              while(punctuationList.contains(tmpString.charAt(idxBack)) && idxBack > 0){
                idxBack--;
              }

              //cuts the punctuations on the back
              tmpString = tmpString.substring(0, idxBack + 1);

              /*
              * trims back will never trim the zeroth index,
              * filers the word with length 1 out to else case again
              */
              if(tmpString.length() > 1){

                //finds the back index that excludes the punctuations
                while(punctuationList.contains(tmpString.charAt(idxFront)) && idxFront < tmpString.length()){
                  idxFront++;
                }

                //cuts the punctuations on the back
                tmpString = tmpString.substring(idxFront, tmpString.length());

                //checks for the StopWords again
                if(!stopWordsSet.contains(tmpString)){
                  textFileWordsList.add(tmpString);//adds to the returning List
                }
              } else{//deals with word with length 1 (inner if else case)
                if(!stopWordsSet.contains(tmpString)){//checks again for case like 'A
                  textFileWordsList.add(tmpString);//adds to the returning List
                }
              }
            }
            //deals with word with length 1 (outer if else case)
            else if (!punctuationList.contains(currentSentence.charAt(0))) {
              textFileWordsList.add(currentSentence);//adds to the returning List
            }
          }
        }
      }
    }
  }//end construtor

  /**
  *helper method for it to be used to load the file in the constructor method.
  *@param fileName is the name of the file to be processed
  *@return scan is the Scanner object holding the file's data
  */
  public Scanner loadFile(String fileName){
    File uploadingFile = new File(fileName);
    if(uploadingFile.exists()){
      try{
        scan = new Scanner(uploadingFile);
      }catch(Exception e){
        e.printStackTrace();
      }
      return scan;
    }else{
      return null;
    }
  }

  /**
  * Main method that takes 1 or 3 parameters.
  * Inputing 1 parameter (the fileName) would print out the words and their frequency
  * in the terminal
  *
  * Inputing 3 parameters (fileName, numberOfWordsToInclude, outFileName) would
  * create a wordCloud html file under the name outFileName with top numberOfWordsToInclude
  * words ranked by their frequency from fileName
  */
  public static void main(String[] args){
    int argsCount = args.length;
    int numberOfWordsToInclude = 0;

    //checks argsCount for the first time
    if (argsCount >= 1){
      //if argsCount is 3, checks whether the second parameter can be converted to integer
      if (argsCount == 3){
        try{
           numberOfWordsToInclude = Integer.parseInt(args[1]);
        } catch (NumberFormatException e){
            System.err.println("Type mismatch. Enter an integer only as a second parameter.");
        }
      }//end checks for Integer

      String inputFileName = args[0];
      WordCounter x = new WordCounter(inputFileName);
      WordCountMap y = new WordCountMap();

      //increments words into the WordCountMap
      for (int i = 0; i < x.textFileWordsList.size(); i++){
        y.incrementCount(x.textFileWordsList.get(i));
      }
      //gets a WordCount List
      List<WordCount> printingList = y.getWordCountsByCount();

      //displays the words and their frequency on the terminal
      if (argsCount == 1){
        for(int i=0; i < printingList.size(); i++){
          System.out.print(printingList.get(i).getWord() + ":");
          System.out.print(printingList.get(i).getCount() + "\n");
        }
      }
      else if (argsCount == 3){ //creats html file
        String outPutFileName = args[2];

        List<WordCount> cloudList = new ArrayList<WordCount>();

        //if the numberOfWordsToInclude is greater than the size of the list, use everything
        if (printingList.size() < numberOfWordsToInclude){
          cloudList = printingList;
        }
        else{//else, use numberOfWordsToInclude
          for (int i = 0; i < numberOfWordsToInclude; i++){
            cloudList.add(printingList.get(i));
          }
        }

        //creates file with fileName: outPutFileName
        WordCloudMaker z = new WordCloudMaker();
        String htmlWord = z.getWordCloudHTML(outPutFileName, cloudList);
        try{
          outPutFileName = outPutFileName.concat(".html");
          PrintWriter pw = new PrintWriter(outPutFileName);
          pw.println(htmlWord);
          pw.close();
        } catch(FileNotFoundException e){
          System.err.println(e);
        }
      }
      else{//inner else if to check for argsCount
        System.err.println("Error: Invalid input (length of input should be 1 or 3)");
      }
    } else{//outer else if to check for argsCount
      System.err.println("Error: Invalid input (length of input should be 1 or 3)");
    }
  }
}
