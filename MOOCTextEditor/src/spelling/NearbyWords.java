package spelling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


/**
 * @author UC San Diego Intermediate MOOC team
 *
 */
public class NearbyWords implements SpellingSuggest
{
	public static final int BEGIN = 0;

	public NearbyWords (Dictionary dict) 
	{
		this.dict = dict;
	}

	/** Return the list of Strings that are one modification away
	 * from the input string.  
	 * @param s The original String
	 * @param wordsOnly controls whether to return only words or any String
	 * @return list of Strings which are nearby the original string
	 */
	public List<String> distanceOne(String s, boolean wordsOnly )  {
		   List<String> retList = new ArrayList<String>();
		   insertions(s, retList, wordsOnly);
		   subsitution(s, retList, wordsOnly);
		   deletions(s, retList, wordsOnly);
		   return retList;
	}

	
	/** Add to the currentList Strings that are one character mutation away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 */
	public void subsitution(String s, List<String> currentList, boolean wordsOnly) {
		// for each letter in the s and for all possible replacement characters
		for(int index = BEGIN; index < s.length(); index++){
			for(int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {
				// use StringBuffer for an easy interface to permuting the 
				// letters in the String
				StringBuffer sb = new StringBuffer(s);
				sb.setCharAt(index, (char)charCode);

				// if the item isn't in the list, isn't the original string, and
				// (if wordsOnly is true) is a real word, add to the list
				if(!currentList.contains(sb.toString()) && 
						(!wordsOnly||dict.isWord(sb.toString())) &&
						!s.equals(sb.toString())) {
					currentList.add(sb.toString());
				}
			}
		}
	}
	
	/** Add to the currentList Strings that are one character insertion away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 */
	public void insertions(String s, List<String> currentList, boolean wordsOnly)
	{
		if(s.isEmpty()) return ;
		if(currentList == null) throw new NullPointerException();

		int end = s.length();

		for(int it = BEGIN; it <= end; ++it)
		{
			for(int ch = (int) 'a'; ch <= (int) 'z'; ++ch)
			{
				StringBuilder temp = new StringBuilder(s);
				temp.insert(it, (char)ch);

				if(!currentList.contains(temp.toString()))
				{
					if (wordsOnly)
					{
						if (dict.isWord(temp.toString())) currentList.add(temp.toString());
					}
				    else currentList.add(temp.toString());
				}
			}
		}
	}

	/** Add to the currentList Strings that are one character deletion away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 */
	public void deletions(String s, List<String> currentList, boolean wordsOnly)
	{
		if(s.isEmpty()) return ;
		if(currentList == null) throw new NullPointerException();

		int end = s.length();

		for(int it = BEGIN; it != end; ++it)
		{
			StringBuilder temp = new StringBuilder(s);
			temp.deleteCharAt(it);

			if(!currentList.contains(temp.toString()))
			{
				if (wordsOnly)
				{
					if (dict.isWord(temp.toString())) currentList.add(temp.toString());
				}
				else currentList.add(temp.toString());
			}
		}
	}

	/** Add to the currentList Strings that are one character deletion away
	 * from the input string.  
	 * @param word The misspelled word
	 * @param numSuggestions is the maximum number of suggestions to return 
	 * @return the list of spelling suggestions
	 */
	@Override
	public List<String> suggestions(String word, int numSuggestions)
	{

		// initial variables
		List<String> queue = new LinkedList<String>();     // String to explore
		HashSet<String> visited = new HashSet<String>();   // to avoid exploring the same  
														   // string multiple times
		List<String> retList = new LinkedList<String>();   // words to return

		// insert first node
		queue.add(word);
		//visited.add(word);
					
		while(!queue.isEmpty())
		{
			String temp_word = queue.remove(queue.size()-1);
			if(!visited.contains(temp_word))
			{
				List<String> temp_list = distanceOne(temp_word, true);
				for(String str : temp_list)
					if(!retList.contains(str)) retList.add(str);

				numSuggestions -= temp_list.size();

				if(numSuggestions <= 0 || visited.size() > THRESHOLD) break;

				queue.addAll(temp_list);
				visited.add(temp_word);
			}
		}
		
		return retList;

	}

	// THRESHOLD to determine how many words to look through when looking
	// for spelling suggestions (stops prohibitively long searching)
	// For use in the Optional Optimization in Part 2.
	private static final int THRESHOLD = 500;

	Dictionary dict;
}
