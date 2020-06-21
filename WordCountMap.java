import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EmptyStackException;

/**
*Author: Ryan Choi , PJ Sangvong
*/

public class WordCountMap{
  private Node root;
  /**
  * Constructor that initializes the root node
  */
  public WordCountMap(){
    root = new Node(' ');
  }
  /**
  * Adds 1 to the existing count for word, or adds word to the WordCountMap
  * with a count of 1 if it was not already present.
  * Implementation must be recursive, not iterative.
  */
  public void incrementCount(String word){
    incrementCountHelper(word, 0, root);
  }

  /**
  * method to add new words to the tree
  *@param word is the word to be added to the tree
  *@param index is the index of the character of the word
  *@param currentNode is the node where the child will be added to
  */
  private void incrementCountHelper(String word, int index, Node currentNode){
    //base case that will add the count to the last node of the string
    if (index == word.length()){
      currentNode.addCount(1);
    }
    // recursive case where if current index exists in the childNode
    else if (checker(word.charAt(index), currentNode)){
      int nextIndex = index + 1;
      incrementCountHelper(word, nextIndex, currentNode.getDataMap().get(word.charAt(index)));
    }
    //Construct a new node as a leaf node if the char node does not exist
    else{
      int nextIndex = index + 1;
      currentNode.addChild(word.charAt(index), currentNode);
      incrementCountHelper(word, nextIndex, currentNode.getDataMap().get(word.charAt(index)));
    }
  }

  /**
  * Remove 1 to the existing count for word. If word is not present, does
  * nothing. If word is present and this decreases its count to 0, removes
  * any nodes in the tree that are no longer necessary to represent the
  * remaining words.
  */
  public void decrementCount(String word){
    decrementCountHelper(word, 0, root);
  }

  /**
  * method to delete word from the tree
  *@param word is the word to be deleted from the tree
  *@param index is the index of the character of the word
  *@param currentNode is the node that the method is looking at
  */
  private void decrementCountHelper(String word, int index, Node currentNode){
    //base case: decreases the count of the last char node of the String
    if (index == word.length()){
      currentNode.addCount(-1);
      removeParent(currentNode);//calls removeParent Method
    }
    // recursive case where if current index exists in the childNode
    else if (checker(word.charAt(index), currentNode)){
      int nextIndex = index + 1;
      decrementCountHelper(word, nextIndex, currentNode.getDataMap().get(word.charAt(index)));
    }
    else{//the decrementing word does not exist in the tree
      System.err.println("Cannot Decrement Error: " + word + " does not exist in the tree.");
    }
  }

  //helper method for decrementCountHelper to remove parent Nodes
  public void removeParent(Node currentNode){
    //call helper if the leaf node has no child or the count of leaf node is 0
    if (currentNode.getCount() == 0 && currentNode.hasNoChild()){
      Node parentNode = currentNode.getParent();
      removeParentHelper(currentNode, parentNode);
    }
  }

  //helper method for the removeParent method
  public void removeParentHelper(Node currentNode, Node parentNode){
    parentNode.getDataMap().remove(currentNode.getData());
    //base case: traverses up to root
    if (parentNode == root){
      ;
    }
    //base case: traverses up until parent node has child or has count
    if (!parentNode.hasNoChild() || parentNode.getCount() > 0){
      ;
    }
    //does recursion call
    else{
      removeParentHelper(parentNode, parentNode.getParent());
    }
  }

  /**
  * Returns true if word is stored in this WordCountMap with
  * a count greater than 0, and false otherwise.
  * Implementation must be recursive, not iterative.
  */
  public boolean contains(String word){
    return containsHelper(word, 0, root);
  }

  /**
  * helper method for contains methods
  * @param word is the input word to check whether it is in the list or not
  *@param index is the index of the word that is being looked at
  *@param currentNode is the current node that is being looked at
  */
  public boolean containsHelper(String word, int index, Node currentNode){
    //base case: when the index reaches the length of the word
    if (index == word.length()){
      return true;
    }
    //if the char of index word exists, increase the index by 1
    else if (checker(word.charAt(index), currentNode)){
      int nextIndex = index + 1;
      return containsHelper(word, nextIndex, currentNode.getDataMap().get(word.charAt(index)));
    }
    //if no matched word found, returns false
    else{
      return false;
    }
  }


  /**
  * Returns the count of word, or -1 if word is not in the WordCountMap.
  * Implementation must be recursive, not iterative.
  */
  public int getCount(String word){
    return getCountHelper(word, 0, root);
  }

  /**
  * helper method for getCount method
  * @param word is the input word to check whether it is in the list or not
  *@param index is the index of the word that is being looked at
  *@param currentNode is the current node that is being looked at
  */
  public int getCountHelper(String word, int index, Node currentNode){
    //base case: when the index reaches the length of the word, return the count
    if (index == word.length()){
      return currentNode.getCount();
    }
    //traverses recursively to the next index
    else if (checker(word.charAt(index), currentNode)){
      int nextIndex = index + 1;
      return getCountHelper(word, nextIndex, currentNode.getDataMap().get(word.charAt(index)));
    }
    //if no word exits, return -1
    else{
      return -1;
    }
  }


  /**
  * Returns a list of WordCount objects, one per word stored in this
  * WordCountMap, sorted in decreasing order by count.
  */
  public List<WordCount> getWordCountsByCount(){
    /*
    * traverses through the entire tree to construct the WordCount
    * and add it to the wordCountList
    */
    List<WordCount> wordCountList = new ArrayList<WordCount>();
    Stack<Node> nodeStack = new Stack<Node>();
    nodeStack.add(this.root); //puts root into the stack

    while (!nodeStack.isEmpty()){
      Node currentNode = nodeStack.pop();

      /*
      * checks whether the current node contains value or not
      * if yes, travese backwards to get the entire word,
      * construct the WordCount and add to the wordCountList
      */
      if (currentNode != this.root){

        //checks the count of the popped node
        if (currentNode.getCount() > 0){
          int countOfWord = currentNode.getCount();
          String curWord = String.valueOf(currentNode.getData());
          Node parentNodeToGetWord = currentNode.getParent();

          //gets the char from the parents until it hits the root
          while(parentNodeToGetWord != this.root){
            String wordFromBack = String.valueOf(parentNodeToGetWord.getData());
            curWord = wordFromBack.concat(curWord);
            parentNodeToGetWord = parentNodeToGetWord.getParent();
          }

          //constructs the wordCount and adds to the wordCountList
          WordCount wordCountToAdd = new WordCount(curWord);
          wordCountToAdd.setCount(countOfWord);
          wordCountList.add(wordCountToAdd);
        }
      }
      //if the curretNode has child, push every chid to the stack
      if (!currentNode.hasNoChild()){
        Collection<Node> nodeCollection = currentNode.getDataMap().values();
        //adds every child to the Stack
        for (Node curChild: nodeCollection){
          nodeStack.add(curChild);
        }
      }
    }

    //sorting
    for (int i = 0; i < wordCountList.size(); i++){
      for (int j = i + 1; j < wordCountList.size(); j++){
        if(wordCountList.get(i).getCount() < wordCountList.get(j).getCount()){
          WordCount tmp = wordCountList.get(j);
          wordCountList.set(j, wordCountList.get(i));
          wordCountList.set(i, tmp);
        }
      }
    }
    return wordCountList;
  }

  /**
  * Returns a count of the total number of nodes in the tree.
  * A tree with only a root is a tree with one node; it is an acceptable
  * implementation to have a tree that represents no words have either
  * 1 node (the root) or 0 nodes.
  * Implementation must be recursive, not iterative.
  */
  public int getNodeCount(){
    Stack<Node> nodeStack = new Stack<Node>();
    List<Node> visitedNodeList = new ArrayList<Node>();
    //runs recursion if root has child nodes
    if (root.hasNoChild()){
      return 0;
    }
    else{
      nodeStack.push(this.root);
      return getNodeCountHelper(nodeStack, 0);
    }
  }

  /**
  * helper method for getNodeCount
  *@param nodeStack is the Stack that is used to help travese through the tree
  *@param count is the count of the nodes
  */
  public int getNodeCountHelper(Stack<Node> nodeStack, int count){
    Node currentNode = nodeStack.pop();
    //if the curretNode has child, push every chid into the stack
    if (!currentNode.hasNoChild()){
      Collection<Node> nodeCollection = currentNode.getDataMap().values();
      //adds every child to the Stack
      for (Node curChild: nodeCollection){
        nodeStack.add(curChild);
        count ++;
      }
    } //if has no child, do nothing
    else{
      ;
    }
    //base case: return count when the nodeStack is empty
    if (nodeStack.isEmpty()){
      return count;
    }
    //does the recursion with the top node from the nodeStack
    else{
      return getNodeCountHelper(nodeStack, count);
    }
  }

  /**
  * helper method for lots of places to check whether the current Node has
  * targetedChar as its child node
  * @param targetedChar is the char that checks whether it is a child of curretNode or not
  * @param currentNode is the node that wants to check for a child
  */
  private boolean checker(char targetedChar,Node currentNode){
    return currentNode.getDataMap().containsKey(targetedChar);
  }

  /**
  * Node sub-class that represents each node in the tree
  * each node contains Character data and int count
  */
  public class Node{
    private Character data;
    private int count;
    private Node parentNode;
    //Map stores children nodes as values and their characters as keys
    private Map<Character, Node> dataMap;

    /**
    * Constructor method to construct the node and initialize the count and map
    * @param data is a char that would be stored in the new node
    */
    public Node(char data){
      this.data = data;
      this.count = 0;
      this.dataMap = new HashMap<Character, Node>();
    }

    /**
    *@return the character that is stored in the node
    */
    public Character getData(){
      return this.data;
    }

    /**
    *@param change the int that would be added to the count
    */
    public void addCount(int change){
      this.count += change;
    }

    /**
    *@return the HashMap of the Node
    */
    public Map<Character, Node> getDataMap(){
      return this.dataMap;
    }

    /**
    *Method that adds the new node and creates the link for its parent node
    *@param child is the newly added child node
    *@param currentNode is the to-be parentNode
    */
    public void addChild(char child, Node currentNode){
      Node newNode = new Node(child);
      newNode.parentNode = currentNode;
      this.dataMap.put(child, newNode);
    }

    /**
    *@return the count of the Node
    */
    public int getCount(){
      return this.count;
    }

    /**
    *@return the parent node
    */
    public Node getParent(){
      return this.parentNode;
    }

    /**
    *@return boolean of whether it has no child or not
    */
    public boolean hasNoChild(){
      return this.dataMap.isEmpty();
    }
  }

  public static void main(String[] args){
    WordCountMap x = new WordCountMap();
    // incrementCount(String word);
    // decrementCount(String word);
    // contains(String word);
    // getCount(String word);
    // getWordCountsByCount();
    // getNodeCount();
    System.out.println();
    System.out.println("Root node created " + x.root.getData());
    System.out.println("Root node should have no Child\n" +
                        "Does the root node have no child node? (should be true)--> "  + x.root.hasNoChild());
    System.out.println();
    System.out.println("Testing incrementCount...");
    System.out.println("incrementing aa 2 times, Aa once, and ab once...");
    x.incrementCount("Aa");
    x.incrementCount("aa");
    x.incrementCount("aa");
    x.incrementCount("ab");
    System.out.println("incrementing rrrr 3 times, rere once, rozzz once...");
    x.incrementCount("rrrr");
    x.incrementCount("rrrr");
    x.incrementCount("rrrr");
    x.incrementCount("rere");
    x.incrementCount("rozzz");
    System.out.println("Root node should have 3 children nodes.\n" +
                      "Root node's children nodes --> " + x.root.getDataMap().size());
    System.out.println("Node 'a' should have 2 children nodes.\n" +
                        "Node 'a's children nodes --> " + x.root.getDataMap().get('a').getDataMap().size());
    System.out.println();
    System.out.println("Testing decrementCount...");
    System.out.println("decrementing 'ab' from the tree");
    x.decrementCount("ab");
    System.out.println("Node 'a' (the child of the root node) should have only 1 child left");
    System.out.println("Node 'a's child node --> " + x.root.getDataMap().get('a').getDataMap().size());
    System.out.println("Testing decrementCount with something that is not in the tree...");
    System.out.println("decrementing '88' from the tree \nShould print out error message:");
    x.decrementCount("88");
    System.out.println();
    System.out.println("Testing Contains...");
    System.out.println("Does it contain rrrr? (should be true) --> " + x.contains("rrrr"));
    System.out.println("Does it contain 88? (should be false) --> " + x.contains("88"));
    System.out.println();
    System.out.println("Testing getCount...");
    System.out.println("Casting getCount on rrrr (should be 3) --> " + x.getCount("rrrr"));
    System.out.println("Casting getCount on 88 (doesn't exist)(should be -1) --> " + x.getCount("88"));
    System.out.println();
    System.out.println("Testing getWordCountsByCount...");
    List<WordCount> k = x.getWordCountsByCount();
    System.out.println("Should return List<WordCount>");
    System.out.println("Running for loop for each element in the List...");
    System.out.println("(Testing WordCount getWord and getCount methods as well)...");
    System.out.println("Printing WordCount inside the List (should match the incremented words without"+
                        " the word ab)...");
    for (int i = 0; i < k.size(); i++){
      System.out.println(k.get(i).getWord() + ":" + k.get(i).getCount());
    }
    System.out.println();
    System.out.println("Testing getNodeCount...");
    System.out.println("Calling getNodeCount (Should be 15) Note:(ab was removed)...");
    System.out.println("getNodeCount result --> " + x.getNodeCount());

    System.out.println();
    System.out.println("Done Testing");
    System.out.println();
  }
}
