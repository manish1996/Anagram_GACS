package com.google.engedu.anagrams;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.HashSet;
import android.util.Log;
public class AnagramDictionary {
    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
     HashSet<String> wordset=new HashSet<String>();
    HashMap<String, ArrayList>  lettersToWord=new HashMap<>();
    HashMap<Integer, ArrayList<String>> sizeToWord=new HashMap<>();
    private ArrayList<String> wordlist=new ArrayList<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordlist.add(word);
            wordset.add(word);
            ArrayList<String> temp_words2 = new ArrayList<String>();
            ArrayList<String> tempWord1 = new ArrayList<String>();
            int l = word.length();
            if(sizeToWord.containsKey(l))
            {
                temp_words2 = sizeToWord.get(l);
                temp_words2.add(word);
                sizeToWord.put(l,temp_words2);
            }
            else
            {
                temp_words2.add(word);
                sizeToWord.put(l,temp_words2);
            }
            String sortWord;
            sortWord=alphabeticalOrder(word);
            if(lettersToWord.containsKey(sortWord)){
                tempWord1=lettersToWord.get(sortWord);
                tempWord1.add(word);
                lettersToWord.put(sortWord,tempWord1);
            }
            else{
                tempWord1.add(word);
                lettersToWord.put(sortWord,tempWord1);
            }

        }

    }
    public String alphabeticalOrder(String word) {
        char[] charArray=word.toCharArray();
        Arrays.sort(charArray);
        String newword=new String(charArray);
        return newword;

    }
    public boolean isGoodWord(String word, String base) {
      if(wordset.contains(word) && !word.contains(base))
        return true;
        else
          return false;
    }



    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String wordd;
        for(char i='a';i<='z';i++)
        {
           wordd= i+word;
            wordd=alphabeticalOrder(wordd);
            if(lettersToWord.containsKey(wordd)){
                result.addAll(lettersToWord.get(wordd));
            }
        }
        for(int i=0;i<result.size();i++){
            Log.d("AD list ", result.get(i));
            if(!isGoodWord(result.get(i),word)){
                result.remove(i);
                i--;
            }
        }
        return result;
    }



    public String pickGoodStarterWord() {
        String word = new String();
        int j;

        //taking arrayList as lengthWords
        ArrayList<String> lengthWords =  new ArrayList<>();

        //our aim is that the word length should never exceed 7
        //getting all words of a fixed size into lengthWords
        if(wordLength <= MAX_WORD_LENGTH){
            lengthWords = sizeToWord.get(wordLength);
        }

        //getting size of all possible words of a fixed size
        int i = random.nextInt(lengthWords.size());

        //now we will pick a random word from the arraylist
        for(j = i; j < lengthWords.size(); j++) {

            //Checking all the conditions and returning the word.
            if(getAnagramsWithOneMoreLetter(lengthWords.get(j)).size() >= MIN_NUM_ANAGRAMS)
            {
                //Log.d("word ",lengthWords.get(j));
                word = lengthWords.get(j);
                break;
            }
        }

        if(j == lengthWords.size() && word == null) {

            for (j = 0; j < i; j++) {
                if (getAnagramsWithOneMoreLetter(lengthWords.get(j)).size() >= MIN_NUM_ANAGRAMS) {
                    word = lengthWords.get(j);
                    break;
                }
            }

        }

        //Max wordLength can be & only.
        if(wordLength < MAX_WORD_LENGTH)
            wordLength++;

        return word;
    }
}


