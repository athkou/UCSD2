package textgen;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/** 
 * An implementation of the MTG interface that uses a list of lists.
 * @author UC San Diego Intermediate Programming MOOC team 
 */
public class MarkovTextGeneratorLoL implements MarkovTextGenerator {

	public static final int BEGIN     = 0;
	public static final int NOT_FOUND = -1;

	// The list of words with their next words
	private List<ListNode> wordList; 
	
	// The starting "word"
	private String starter;
	
	// The random number generator
	private Random rnGenerator;
	
	public MarkovTextGeneratorLoL(Random generator)
	{
		wordList = new LinkedList<ListNode>();
		starter = "";
		rnGenerator = generator;
	}
	
	
	/** Train the generator by adding the sourceText */
	@Override
	public void train(String sourceText)
	{
		if(sourceText.isEmpty()) return ;

		String text[] = sourceText.split("[ ]+");
		starter       = text[BEGIN];
		wordList.add(new ListNode(starter));


		for(int it = BEGIN; it != text.length; ++it)
		{
			String temp = text[it];
			int index   = FoundWord(temp);

			if(index != NOT_FOUND) AddNextWordToNode(text, index, it);
			else
			{
				wordList.add(new ListNode(temp));
				AddNextWordToNode(text, wordList.size() - 1, it);
			}
		}
	}
	
	/** 
	 * Generate the number of words requested.
	 */
	@Override
	public String generateText(int numWords)
	{
		StringBuilder output = new StringBuilder();
		String current       = starter;

		output.append(current + " ");

		if(numWords == 0) return "";

		for(int it = BEGIN; it != numWords - 1; ++it)
		{
			for(ListNode node : wordList)
			{
				if(current.equals(node.getWord()))
				{
					current = node.getRandomNextWord(rnGenerator);
					output.append(current + " ");

					break;
				}
			}
		}

		return output.toString();
	}
	
	// Can be helpful for debugging
	@Override
	public String toString()
	{
		String toReturn = "";
		for (ListNode n : wordList)
		{
			toReturn += n.toString();
		}
		return toReturn;
	}
	
	/** Retrain the generator from scratch on the source text */
	@Override
	public void retrain(String sourceText)
	{
		wordList.clear();
		starter = "";
		train(sourceText);
	}
	
	/**
	 * The method tries to find if the linked list contains
	 * a node with a String "word"
	 * @param word A String variable we are looking for it the list.
	 * @return An integer variable. It has the value -1 if
	 * the linked list does'n contain the word, or otherwise
	 * it returns the index of the list that contains the word.
     */
	public int FoundWord(String word)
	{
		int index = NOT_FOUND;
		int end   = wordList.size();
		for(int it = BEGIN; it != end; ++it)
		{
			String temp = wordList.get(it).getWord();
			if(word.equals(temp)) return it;
		}

		return index;
	}

	/**
	 *  The method takes a node from the linked list. The node has a word
	 *  and a list of next words. A string is added from the string array
	 *  into the list of next words.
	 *
	 * @param words An array of String containing words.
	 * @param index An integer, which represents the index of the node in the linked list.
	 * @param position An integer, that is the index of the word from the array "words"
     */
	public void AddNextWordToNode(String words[], int index, int position)
	{
		ListNode node = wordList.get(index);
		int next_word = position + 1;

		if(next_word < words.length) node.addNextWord(words[next_word]);
		else                         node.addNextWord(starter);
	}
	
	/**
	 * This is a minimal set of tests.  Note that it can be difficult
	 * to test methods/classes with randomized behavior.   
	 * @param args
	 */
	public static void main(String[] args)
	{
		// feed the generator a fixed random value for repeatable behavior
		MarkovTextGeneratorLoL gen = new MarkovTextGeneratorLoL(new Random(42));
		String textString = "Hello.  Hello there.  This is a test.  Hello there.  Hello Bob.  Test again.";
		System.out.println(textString);
		gen.train(textString);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
		String textString2 = "You say yes, I say no, "+
				"You say stop, and I say go, go, go, "+
				"Oh no. You say goodbye and I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"I say high, you say low, "+
				"You say why, and I say I don't know. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"Why, why, why, why, why, why, "+
				"Do you say goodbye. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"You say yes, I say no, "+
				"You say stop and I say go, go, go. "+
				"Oh, oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello,";
		System.out.println(textString2);
		gen.retrain(textString2);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
	}

}

/** Links a word to the next words in the list 
 * You should use this class in your implementation. */
class ListNode
{
    // The word that is linking to the next words
	private String word;
	
	// The next words that could follow it
	private List<String> nextWords;
	
	ListNode(String word)
	{
		this.word = word;
		nextWords = new LinkedList<String>();
	}
	
	public String getWord()
	{
		return word;
	}

	public void addNextWord(String nextWord)
	{
		nextWords.add(nextWord);
	}
	
	public String getRandomNextWord(Random generator)
	{
	   	int index = generator.nextInt(nextWords.size());
		return nextWords.get(index);
	}

	public String toString()
	{
		String toReturn = word + ": ";
		for (String s : nextWords) {
			toReturn += s + "->";
		}
		toReturn += "\n";
		return toReturn;
	}
}


