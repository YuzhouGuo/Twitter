//This is the Tweet class.

//Student Name: Yuzhou Guo
//Student ID: 260715042

import java.io.*;
import java.util.HashSet;
public class Tweet {

    private String userAccount;
    private String date;
    private String time;
    private String message;
    private static HashSet<String> stopWords;
    //Here are all the attributes, notice that the HashSet is private so we can only use in this class.
  
    public Tweet (String userAccount, String date, String time, String message){
    
      this.userAccount = userAccount;
      this.date = date;
      this.time = time;
      this.message = message;  

    }

    public boolean checkMessage (String maybeAValidMessage){

      if(stopWords==null){
        throw new NullPointerException ("The HashSet has not been initialized!");
      }
      //First we will check if the HashSet is initialized or not, if so then we can automatically stop the program.
            
      String[] mToWords = maybeAValidMessage.split(" ");
      String[] stopWordsArray = new String[stopWords.size()];
      stopWords.toArray(stopWordsArray);
      //From the input we receive a String and now we split it to a String array.
      //Similarly we did this to the stopWord HashSet, it would be more convenient to loop through two String arrays.
      
      int counter = mToWords.length;
      //The idea is that firstly we assume there's no stop word in the message, every time we find one stop word, we
      //reduce the "actual length" of the message by one.
      
      for(int i = 0; i<mToWords.length; i++){
        for(int j = 0; j<stopWordsArray.length; j++){
          if ((mToWords[i]).equalsIgnoreCase(stopWordsArray[j])){
            counter--; //We should ignore the lower and upper cases.
          }
          boolean punctuation1 = (mToWords[i]).equalsIgnoreCase(stopWordsArray[j]+',');
          boolean punctuation2 = (mToWords[i]).equalsIgnoreCase(stopWordsArray[j]+'.');
          boolean punctuation3 = (mToWords[i]).equalsIgnoreCase(stopWordsArray[j]+';');
          boolean punctuation4 = (mToWords[i]).equalsIgnoreCase(stopWordsArray[j]+':');
          
          if(punctuation1||punctuation2||punctuation3||punctuation4){
            counter--; //We should also consider the situations that "stop word + punctuations".
          }
        }
      }
      //We used nested loop here, so that we take one word from the message, loop it all through the stop word array
      //and make sure it is not a stop word(if it is then counter-1), then we go for the next word.
      
      boolean sizeLargeE = (counter>0);
      boolean sizeSmallE = (counter<16);
      
      if(sizeLargeE && sizeSmallE){
          return true;  //If the actual length of the message is between 1 and 15 after getting out the stop word.
      }
      else{
          return false;
      }

    }
  
    public String getDate (){
      return this.date;
    }
    
    public String getTime (){
      return this.time;
    }
    
    public String getMessage (){
      return this.message;
    }
    
    public String getUserAccount (){
      return this.userAccount;
    } //here are just four simple get methods.
    
    public String toString (){
      return (this.userAccount+"\t"+this.date+"\t"+this.time+"\t"+this.message);
    } //A little problem here I notice is that, according to the words we have, the tab space lengths may be different.
    //When we test the program and try to print the tweet, the space between date and time is very close, but if you 
    //look closely there is a tab space there...
    
    //But we have to keep it like this, since we have to write on another file, if we change the format it would be 
    //difficult for the reader to read, so I guess I will keep the "one tab between" format.
    
    public boolean isBefore (Tweet t){
    
      String[] thisDateSplit = this.date.split("-");
      String[] inputDateSplit = t.getDate().split("-");
      String[] thisTimeSplit = this.time.split(":");
      String[] inputTimeSplit = t.getTime().split(":");
      //First we split eveything.
      
      int thisYear = Integer.parseInt(thisDateSplit[0]);
      int inputYear = Integer.parseInt(inputDateSplit[0]);
      int thisMonth = Integer.parseInt(thisDateSplit[1]);
      int inputMonth = Integer.parseInt(inputDateSplit[1]);
      int thisDay = Integer.parseInt(thisDateSplit[2]);
      int inputDay = Integer.parseInt(inputDateSplit[2]);
      
      int thisHour = Integer.parseInt(thisTimeSplit[0]);
      int inputHour = Integer.parseInt(inputTimeSplit[0]);
      int thisMinute = Integer.parseInt(thisTimeSplit[1]);
      int inputMinute = Integer.parseInt(inputTimeSplit[1]);
      int thisSecond = Integer.parseInt(thisTimeSplit[2]);
      int inputSecond = Integer.parseInt(inputTimeSplit[2]);
      //Seem long but they do help cleaning up, in the following "if" conditions we can clearly see the order we compare.
      //Another advantage of doing this is that we can just use "<", ">" and "==", much easier.
      
      if(thisYear<inputYear){
        return true;
      }
      else if (thisYear>inputYear){
        return false;
      }
      else if (thisYear==inputYear){
        if(thisMonth<inputMonth){
          return true;
        }
        else if(thisMonth>inputMonth){
          return false;
        }
        else if (thisMonth==inputMonth){
          if (thisDay<inputDay){
            return true;
          }
          else if (thisDay>inputDay){
            return false;
          }
          else if (thisDay==inputDay){
            if (thisHour<inputHour){
              return true;
            }
            else if (thisHour>inputHour){
              return false;
            }
            else if (thisHour==inputHour){
              if (thisMinute<inputMinute){
                return true;
              }
              else if (thisMinute>inputMinute){
                return false;
              }
              else if (thisMinute==inputMinute){
                if (thisSecond<inputSecond){
                  return true;
                }
                else if (thisSecond>inputSecond){
                  return false;
                }
              }
            }
          }
        }
      }
      return false;
    }
    //We go from year all the way to second, if any larger unit can tell the order, we return.
    
    public static void loadStopWords(String fileName){
      
      stopWords = new HashSet<String>();//Here we build up the object.
      
      try{
        //Remember to use the try and catch method since it might throw exception.
      
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
      
        String currentLine = br.readLine();
      
        while (currentLine != null){
          stopWords.add(currentLine);//We add every line to the HashSet in the Tweet class.
          currentLine = br.readLine();
        }
        br.close();
        fr.close();//Remember to close the file.
      }
      catch(Exception e){
        System.out.println("Failed to read the file "+fileName);
      }
    }
    
}