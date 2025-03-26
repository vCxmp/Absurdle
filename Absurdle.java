/* 
    11/5/2024
*/

import java.util.*;
import java.io.*;
/*
    This program simulates the Absurdle game, where the program minimizes the pruning of words
        to lengthen the gameplay time. The users have to guess words and then the game responds
            with patterns of colored tiles which show if user's guess letters are correct, 
                misplaced, or fully incorrect.
*/

public class Absurdle  {
    public static final String GREEN = "🟩";
    public static final String YELLOW = "🟨";
    public static final String GRAY = "⬜";

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to the game of Absurdle.");

        System.out.print("What dictionary would you like to use? ");
        String dictName = console.next();

        System.out.print("What length word would you like to guess? ");
        int wordLength = console.nextInt();

        List<String> contents = loadFile(new Scanner(new File(dictName)));
        Set<String> words = pruneDictionary(contents, wordLength);

        List<String> guessedPatterns = new ArrayList<>();
        while (!isFinished(guessedPatterns)) {
            System.out.print("> ");
            String guess = console.next();
            String pattern = recordGuess(guess, words, wordLength);
            guessedPatterns.add(pattern);
            System.out.println(": " + pattern);
            System.out.println();
        }
        System.out.println("Absurdle " + guessedPatterns.size() + "/∞");
        System.out.println();
        printPatterns(guessedPatterns);
    }

    // Prints out the given list of patterns.
    // - List<String> patterns: list of patterns from the game
    public static void printPatterns(List<String> patterns) {
        for (String pattern : patterns) {
            System.out.println(pattern);
        }
    }

    // Returns true if the game is finished, meaning the user guessed the word. Returns
    // false otherwise.
    // - List<String> patterns: list of patterns from the game
    public static boolean isFinished(List<String> patterns) {
        if (patterns.isEmpty()) {
            return false;
        }
        String lastPattern = patterns.get(patterns.size() - 1);
        return !lastPattern.contains("⬜") && !lastPattern.contains("🟨");
    }

    // Loads the contents of a given file Scanner into a List<String> and returns it.
    // - Scanner dictScan: contains file contents
    public static List<String> loadFile(Scanner dictScan) {
        List<String> contents = new ArrayList<>();
        while (dictScan.hasNext()) {
            contents.add(dictScan.next());
        }
        return contents;
    }

    /*
        This method narrows down the long list of words in a dictionary so that the only word 
            options available to guess for the user are the word letters length that they 
                specified when the game asked them in the beginning. 
        Parameters:
            - List<String> contents: the entire list of words in the dictionary 
            - int wordLength: the word length the user wants the possible guess target word to be
        Exceptions:
            - IllegalArgumentException(): gets thrown if user's word length choce is less than 1
        Return:
            - Set<String>: the refined word list containing only words with the 
                length of what the user desired
    */
    public static Set<String> pruneDictionary(List<String> contents, int wordLength) {
        if (wordLength < 1) {
            throw new IllegalArgumentException();
        }
        Set<String> prunedResult = new HashSet<>();
        for (String word: contents) {
            if (word.length() == wordLength) {
                prunedResult.add(word);
            }
        }
        return prunedResult;    
    }

    /*
        This method analyzes the user's word guess and sees for and picks the specific color tiles 
            pattern that is assosicated with the largest number of target words. 
        If there is a tie between two patterns for the amount of target words, the pattern that 
            appears first alphabetically gets selected. 
        In the end, the current word list is updated so that they only reflect the selected 
            color tiles pattern based on the two situations specified above.  
        Parameters:
            - String guess: the user's guessed word
            - Set<String> words: the refined list of possible words to guess 
            - int wordLength: the word length the user wants the possible guess target word to be
        Exceptions:
            - IllegalArgumentException(): gets thrown if the set of words is empty or the user's
                guess word length is different than the word length they
                    specified at the beginning.
        Return:
            - String: the color tiles pattern that is assosicated with the largest number
                of target words or in a tie, the one that comes first alphabetically
    */
    public static String recordGuess(String guess, Set<String> words, int wordLength) {
        if (words.isEmpty() || !(guess.length() == wordLength)) {
            throw new IllegalArgumentException();
        }
        Map<String, Set<String>> groups = new TreeMap<>();
        String tiles = "";
        for (String individualWord : words) {
            tiles = patternFor(individualWord, guess);
            if (groups.containsKey(tiles)) {
                groups.get(tiles).add((individualWord));
            }
            else {
                Set<String> addition = new HashSet<String>();
                addition.add(individualWord);
                groups.put(tiles, addition);
            }
        }
        String maxPattern = mostPattern(groups, words);
        return maxPattern;
    }

    /*
        This method produces a color tiles pattern for the user's word guess. If the user's 
            guess has a letter at the exact correct place as the word the program is looking for, 
                a green tile will replace that letter spot. Similarly, a yellow tile
                    will be placed for correct letter but incorrect position. Furthermore, a gray
                        tile is placed if a letter in the guess is fully incorrect.
        Parameters:
            - String word: the target word the program is looking for
            - String guess: the user's word guess 
        Return:
            - String: the color tile pattern regarding the user's guess
    */
    public static String patternFor(String word, String guess) {
        List<String> wordList = new ArrayList<String>();
        for (int i = 0; i < guess.length(); i++) {
            wordList.add("" + guess.charAt(i));
        }
        Map<Character, Integer> charCount = new TreeMap<>();
        for (int i = 0; i < word.length(); i++) {
            if (charCount.containsKey(word.charAt(i))) {
                charCount.put(word.charAt(i), charCount.get(word.charAt(i)) + 1);
            }
            else {
                charCount.put(word.charAt(i), 1);
            }
        }
        for (int i = 0; i < word.length(); i++) {
            if (guess.charAt(i) == word.charAt(i)) {
                wordList.set(i, GREEN);
                charCount.put(word.charAt(i), charCount.get(word.charAt(i)) - 1);
            }
        }
        for (int i = 0; i < word.length(); i++) {  
            if (!wordList.get(i).equals(GREEN)) {
                if (charCount.containsKey(guess.charAt(i)) && charCount.get(guess.charAt(i)) > 0) {
                    wordList.set(i, YELLOW);
                    charCount.put(guess.charAt(i), charCount.get(guess.charAt(i)) - 1);
                }
                else {
                    wordList.set(i, GRAY);
                }
            }
        } 

        String result = "";
        for (String individual: wordList) {
            result += individual;
        }
        return result;  
    }

    /*
        This method determines the pattern that is assosicated with the greatest amount of target 
            words. If there is a tie for the amount of words between two patterns, 
                then the pattern that comes first alphabetically gets selected.
        Parameters:
            - Map<String, Set<String>> groups: matches specfic patterns to their sets of words
            - Set<String> words: the set of possible words to guess, which is then updated
                    so that the target word set is the dictionary for the next call to method
    */
    public static String mostPattern(Map<String, Set<String>> groups, Set<String> words) {
        String chosenOne = "";
        int max = 0;
        String maxPattern = "";
        for (String pattern : groups.keySet()) {
            int size = groups.get(pattern).size();
            if (max < size) {
                max = size;
                maxPattern = pattern;
            }
        }
        words.clear();
        words.addAll(groups.get(maxPattern));
        return maxPattern;
    }
}
