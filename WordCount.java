/**
*Author: Ryan Choi , PJ Sangvong
*/

/**
* class to store the word data and its frequency count in object
*/
public class WordCount{
  String word;
  int count;

  /**
  *Constructor class that instantiates word and count
  *@param word is the new word to be processed into an object
  */
  public WordCount(String word){
    this.word = word;
    this.count = 1;
  }

  /**
  * Updates the count
  * @param addInt can either be positive or negative
  */
  public void addCount(int addInt){
    this.count += addInt;
  }

  /**
  * Updates the count
  * @param newInt
  */
  public void setCount(int newInt){
    this.count = newInt;
  }

  /**
  * Gets the word stored by this WordCount;
  */
  public String getWord(){
    return this.word;
  }

  /**
  * Gets the count stored by this WordCount
  */
  public int getCount(){
    return this.count;
  }
}
