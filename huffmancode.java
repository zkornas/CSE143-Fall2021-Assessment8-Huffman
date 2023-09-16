// Zacharia Kornas
// CSE 143 AE 
// TA: Kent Zeng
// HuffmanCode program can compress and decompress text files 
// given by the user using huffman encoding.
import java.util.*;
import java.io.*;

public class HuffmanCode {
   private HuffmanNode overallRoot;
   
   // Constructs a new HuffmanCode that takes in data of characters
   // and their frequency and stores the information.
   // Parameters:
   //    frequencies - an array of frequencies where frequencies[i] is 
   //                  the count of the character with ASCII value i.
   public HuffmanCode(int[] frequencies){
      PriorityQueue<HuffmanNode> freqQueue = new PriorityQueue<>();
      for(int i = 0; i < frequencies.length; i++){
         int frequency = frequencies[i];
         if(frequency > 0){
            freqQueue.add(new HuffmanNode(i, frequency, null, null));
         }
      }
      while(freqQueue.size() != 1){
         HuffmanNode smallNode = freqQueue.remove();
         HuffmanNode bigNode = freqQueue.remove();
         overallRoot = new HuffmanNode(0, (smallNode.frequency + bigNode.frequency), smallNode, bigNode);
         freqQueue.add(overallRoot);
      }
   }

   // Constructs a new HuffmanCode that takes in input from a text file
   // regarding characters and stores the data.
   // Parameters:
   //    input - scanner object that is reading from the input file. Input file
   //            should be in line pairs where first line is the character's ASCII value
   //            and the second line is the location information.
   public HuffmanCode(Scanner input){
      overallRoot = new HuffmanNode(0);
      while(input.hasNextLine()){
         int character = Integer.parseInt(input.nextLine());
         String position = input.nextLine();
         int counter = 0;
         overallRoot = constructorHelper(counter, position, overallRoot, character);
      }
   }

   // Private helper method for constructor that stores the given characters
   // in the appropriate location in the tree
   // Returns overallRoot of the tree once the character has been added to the 
   // appropriate HuffmanNode
   // Parameters:
   //    counter - keeps track of which character to look at in the position String
   //              to dictate if we should go to root.zero or root.one
   //    position - the position information of where the character data should go in the tree
   //    root - the HuffmanNode that the computer is evaluating while moving down the tree
   //    character - the ASCII value of the character that we are placing in the tree
   private HuffmanNode constructorHelper(int counter, String position, HuffmanNode root, int character){
      if(root == null){
         root = new HuffmanNode(0, 0, null, null);
      }
      if(counter == position.length()){
         root = new HuffmanNode(character, 0, null, null);
      } else {
         char curr = position.charAt(counter);
         if(curr == '0'){
            counter++;
            root.zero = constructorHelper(counter, position, root.zero, character);
         } else {
            counter++;
            root.one = constructorHelper(counter, position, root.one, character);
         }
      }
      return root;
   }

   // Saves the current information of the HuffmanCode object to a text file in
   // line pairs where the first line is the character's ASCII value and the second
   // line is the characters position in the HuffmanCode object.
   // Parameters:
   //    output - PrintStream object that prints to a text file.
   public void save(PrintStream output){
      String position = "";
      saveHelper(output, overallRoot, position);
   }

   // Private helper method that saves the HuffmanCode tree data to a text file
   // Parameters:
   //    output - PrintStream object used to print output to a text file
   //    root - the HuffmanNode that the computer is currently reading
   //    position - the string that holds information about the position of
   //               the current HuffmanNode in the tree
   private void saveHelper(PrintStream output, HuffmanNode root, String position){
      if(root != null){
         if(root.zero == null || root.one == null){
            output.println(root.data);
            output.println(position);
         }
         saveHelper(output, root.zero, position + "0");
         saveHelper(output, root.one, position + "1");
      }
   }

   // Translates compressed information into a readable format using 
   // huffman encoding. 
   // Parameters:
   //    input - BitInputStream object that reads information bit by bit.
   //    output - PrintStream object that prints to a text file
   public void translate(BitInputStream input, PrintStream output){
      HuffmanNode currNode = overallRoot;
      while(input.hasNextBit()){
         while(currNode.zero != null && currNode.one != null){
            int currBit = input.nextBit();
            if(currBit == 0){
               currNode = currNode.zero;
            } else {
               currNode = currNode.one;
            }
         }
         output.write((char)currNode.data);
         currNode = overallRoot; 
      }
   }
     
   // Class that represents a single QuestionsNode in the tree
   private static class HuffmanNode implements Comparable<HuffmanNode> {
      public int data;
      public int frequency;
      public HuffmanNode zero;
      public HuffmanNode one;
   
      // Constructs a leaf node with the given data
      public HuffmanNode(int data){
         this(data, 0, null, null);
      }
   
      // constructs a leaf or branch node with the given data and links to subtrees
      public HuffmanNode(int data, int frequency, HuffmanNode zero, HuffmanNode one){
         this.data = data;
         this.frequency = frequency;
         this.zero = zero;
         this.one = one;
      }
   
      // Compares HuffmanNodes and returns the one with the bigger frequency
      // >0 if this HuffmanNode is bigger and <0 if the other node is bigger
      // returns 0 if they have the same frequency
      // Parameters:
      //    other - HuffmanNode that will be compared to this HuffmanNode
      public int compareTo(HuffmanNode other){
         if(this.frequency != other.frequency){
            return this.frequency - other.frequency;
         }
         return 0;
      }
   }
}
