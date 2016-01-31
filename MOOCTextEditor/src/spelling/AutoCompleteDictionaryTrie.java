package spelling;

import java.util.*;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author Athanasios Kourtzis
 *
 */
public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete
{
	public static final int EMPTY       = 0;
	public static final int FIRST_CHAR  = 0;
	public static final int SECOND_CHAR = 1;

    public AutoCompleteDictionaryTrie()
	{
		root = new TrieNode();
	}
	
	
	/** Insert a word into the trie.
	 * For the basic part of the assignment (part 2), you should ignore the word's case.
	 * That is, you should convert the string to all lower case as you insert it. */
	public boolean addWord(String word)
	{
		if(word.isEmpty()) return false;
		if(word == null) throw new NullPointerException("The String variable can't be a null object");

		String temp = word.toLowerCase();
		if(isWord(temp)) return false;
		else
		{
			root.insert(temp.charAt(FIRST_CHAR));

			TrieNode node;
			TrieNode child = root.getChild(temp.charAt(FIRST_CHAR));
			if(child == null) throw new NullPointerException("The child from the root can't be null");

			for(int it = SECOND_CHAR; it != temp.length(); ++it)
			{
				node = child.getChild(temp.charAt(it));
				if(node == null)
				{
					child.insert(temp.charAt(it));
					if(child.endsWord()) ; //Do nothing
					else                 child.setEndsWord(false);

					child = child.getChild(temp.charAt(it));
				}
				else child = node;
			}

			child.setEndsWord(true);
			++size;
		}

		return true;
	}
	
	/** 
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size() { return size; }
	
	/** Returns whether the string is a word in the trie */
	@Override
	public boolean isWord(String s) 
	{
		if(IsEmpty() || s.isEmpty()) return false;
		else
		{
			String temp = s.toLowerCase();

			TrieNode node;
			TrieNode child = root.getChild(temp.charAt(FIRST_CHAR));
			if(child == null) return false;

			for(int it = SECOND_CHAR; it != temp.length(); ++it)
			{
				node = child.getChild(temp.charAt(it));
				if(node == null)
				{
					if(child.endsWord()) return temp.equals(child.getText());
					else				 return false;

				}
				else child = node;
			}

			if(!child.endsWord()) return false;
			else return temp.equals(child.getText());
		}
	}

	/** 
	 * Returns up to the n "best" predictions, including the word itself,
     * in terms of length
     * If this string is not in the trie, it returns null.
     * @param prefix The text to use at the word stem
     * @param numCompletions The maximum number of predictions desired.
     * @return A list containing the up to n best predictions
	 *
	 * @throws IllegalArgumentException if numCopletions is a negative number.
     */
	@Override
    public List<String> predictCompletions(String prefix, int numCompletions)
    {
		 if(numCompletions < 0) throw new IllegalArgumentException("numCompletions must be a positive integer");

		 LinkedList<TrieNode> queue = new LinkedList<>();
		 List<String> completions   = new LinkedList<>();

		 if(prefix.isEmpty()) return completions;

		 TrieNode child = FindStem(prefix.toLowerCase());

		 if(child == null) return completions;

		 queue.add(child);

		 while(true)
		 {
			 if(numCompletions <= 0 || queue.isEmpty()) break;

			 child = queue.remove();

			 if(child.endsWord())
			 {
				 completions.add(child.getText());
				 --numCompletions;
			 }

			 PopulateQueue(child, queue);
		 }

		 return completions;
    }

	public boolean IsEmpty() {return size == EMPTY; }

 	// For debugging
 	public void printTree()
 	{
 		printNode(root);
 	}
 	
 	/** Do a pre-order traversal from this node down */
 	public void printNode(TrieNode curr)
 	{
 		if (curr == null) 
 			return;
 		
 		System.out.println(curr.getText());
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}

	private TrieNode FindStem(String prefix)
	{
		int end     = prefix.length();

		TrieNode child = root.getChild(prefix.charAt(FIRST_CHAR));
		if(child == null) return null;

		TrieNode node;

		for(int it = SECOND_CHAR; it != end; ++it)
		{
			node = child.getChild(prefix.charAt(it));

			if(node != null) child = node;
			else             return null;
		}

		return child;
	}

	private void PopulateQueue(TrieNode child, LinkedList<TrieNode> queue)
	{
		Set<Character> characters = new TreeSet<>(child.getValidNextCharacters());
		for(Character ch : characters)
		{
			TrieNode temp_node = child.getChild(ch);
			queue.add(temp_node);
		}
	}

	private TrieNode root;
	private int size;
}