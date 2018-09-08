//This is the twitter class.

//Student Name: Yuzhou Guo
//Student ID: 260715042

import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
//The three input classes we need.

public class Twitter {
    private ArrayList<Tweet> tweets;

    public Twitter(){
      tweets = new ArrayList<Tweet>();
    }
    
    public static void main (String args[]){
      Twitter example = new Twitter();
      Tweet.loadStopWords("stopWords.txt");
      example.loadDB("tweets.txt");
  
    }
    //Not included in the instruction, but we need a mian method to test the methods.
    
    public void loadDB (String fileName){
      try{
        //We need to keep the "fr, br" all in the try and catch block, since it may throw exceptions.
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        
        String currentLine = br.readLine();

        while (currentLine != null){
            String[] s = currentLine.split("\t"); //We first split the line into the four elements we need to build
                                                  //a tweet, then we add the tweet into the ArrayList.
            Tweet t = new Tweet(s[0],s[1],s[2],s[3]); 
            
            if(t.checkMessage(t.getMessage())==true){  
              tweets.add(t);
            }
            currentLine = br.readLine();
        }
        br.close();
        fr.close();//Remember to close the br and fr.
      }
      catch(NullPointerException e){
        //We have this catch block here so that we know specifically this is because the file is not initialized yet.
        System.out.println("Error checking the stopWords database: The file of stopWords has not been initialized yet.");
      }
      catch(Exception e){
        //We have a general exception catch block, just make sure the program won't crash.
        System.out.println("Fail to read the file "+fileName);
      }
      sortTwitter();
      //Finally we reorder the elements in the ArrayList so that they are listed according to the date and time.
    }
    
    public void sortTwitter(){
      
      for (int i = 0; i<tweets.size(); i++){
        for (int j = (i+1); j<tweets.size(); j++){
          if(tweets.get(j).isBefore(tweets.get(i))){
            Tweet temporary = tweets.get(i);
            tweets.set(i, tweets.get(j));
            tweets.set(j, temporary);
          }
        }
      }
    }
    //The idea is that for a sequence of elements, we take the first one out, compare it to all the elements behind to
    //make sure it is the earliest tweet. If any tweet behind it is posted ealier, then it will be set in the first place.
    //We did this to all the elements and set the first element, then we go for the second element and repeat the procedure.
    
    //Notice that when we do the "replace" job, or switching, we need a temporary variable.
    //If not, then the previous element info just disappears.
    
    public int getSizeTwitter(){
      return tweets.size();
    }
    
    public Tweet getTweet (int index){
      return tweets.get(index);
    }
    
    public String printDB(){
      String allElements = "";
      for (Tweet t: tweets){ //We must use the foreach loop to go through the elements in the ArrayList.
        allElements += t.toString()+"\n"; //Remember to change the line.
      }
      return allElements;
    }
    
    public ArrayList<Tweet> rangeTweets (Tweet tweet1, Tweet tweet2){
      ArrayList<Tweet> tweetsBetween = new ArrayList<Tweet>();
      //First we create a new ArrayList to store the new info later.
      if (tweet1.isBefore(tweet2)){
        //In the first case, the tweet one is posted ealier than tweet two.
        
        for (int i = 0; i<tweets.size(); i++){
          boolean afterTweet1 = (tweets.get(i).isBefore(tweet1)==false);
          boolean beforeTweet2 = (tweets.get(i).isBefore(tweet2)==true);
          
          if(afterTweet1 && beforeTweet2){  //If both condition are satisfied, then it is posted between two tweets.
            tweetsBetween.add(tweets.get(i));
          }
        }
        tweetsBetween.add(tweet2); //Don't forget to add tweet2 as well.
      }
      else {
        //In the secong case, the tweet two is posted ealier than tweet one.
        
        for (int i = 0; i<tweets.size(); i++){
          boolean beforeTweet1 = (tweets.get(i).isBefore(tweet1)==true);
          boolean afterTweet2 = (tweets.get(i).isBefore(tweet2)==false);
          
          if(beforeTweet1 && afterTweet2){
            tweetsBetween.add(tweets.get(i));        //The same idea as before.
          }
        }
        tweetsBetween.add(tweet1);
      }
      return tweetsBetween;
    }
    
    public void saveDB (String fileName){
       try{
      
        FileWriter fw = new FileWriter(fileName);
        BufferedWriter bw = new BufferedWriter(fw);
        
        String message = printDB();
        bw.write(message); //Here we just need to put eveything we have in "printDB" method, and put it to the String
                           //message.  
        bw.close();
        fw.close();
      }
      catch(Exception e){
        System.out.println("Failed to save the database");
      } 
    }
    
    public String trendingTopic(){
      String trendingTopic = "";
      HashMap<Integer, String> pairs = new HashMap<Integer, String>();
      //The reason why we use HashMap is that, we can set the "key" as the times that particualr word appears in total.
      //Another advantage is that if two words have the same appearing times, the later one will cover the previous one.
      //So eventually when we want to see which word is most popular, we just need to get the value associates with
      //the key.
      
      int appears = 1; //A word must appear once already(when we see it).
      
      //The idea is like this: 
      //We have lines of information include all the userAccount, date, time and message. But we
      //only care about message in this case. So our first job is spliting the words in the first line (the earliest 
      //post, the order doesn't really matter, we just need to make sure we iterate through everything).
      
      //Then we take the first word out, and every time we see it in the lines behind, variable "appears"+1.
      //When we finish counting the appearing times of the first word, we put the times as well as the word itself into
      //the HashMap"pairs", for example "5-elephant". And set the "appears" back to 1.
      
      //And then we go for the second word in the first line, when we finish the first line wo go for the second line...
      
      for (int i = 0; i<tweets.size(); i++){
        String[] t = tweets.get(i).getMessage().split(" ");
        
        for (int a = 0; a<t.length; a++){
          for (int j = (i+1); j<tweets.size(); j++){
            if(tweets.get(i).checkMessage(t[a])==false){
              break;
            }
            if(contains(tweets.get(j), t[a])==true){
               appears++; //This variable here works as the key in the HashMap.
            }
            if(j==(tweets.size()-1)){  
               pairs.put(appears, t[a]);
               appears = 1;
            }
          }//How many times the previous word appears.
        }
      }
      
      for(int i = tweets.size(); i>0; i--){
        if(pairs.containsKey(i)==true){  //Here the algorithem is simple, we know how many tweets in total, just guess
          trendingTopic = pairs.get(i);  //the number from large to small. If we get it, we get it.
          break;//Don't forget to escape from the loop as soon as we get the key.
        }
      }
      return trendingTopic;
    }
    
    //This is a helper method with the trending method, since the trending word is counted only once in every tweet.
    //we just need to consider if one post contains the word, or not. A simple method returns a boolean.
    private boolean contains (Tweet t, String word){
      String[] messageElements = t.getMessage().split(" ");
      for(int i = 0; i<messageElements.length; i++){
        if (messageElements[i].equals(word)){
          return true;
        }
      }
      return false;
    }
}
