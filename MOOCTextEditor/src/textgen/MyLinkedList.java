package textgen;

import java.util.AbstractList;


/** A class that implements a doubly linked list
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 * @param <E> The type of the elements stored in the list
 */
public class MyLinkedList<E> extends AbstractList<E> {
	LLNode<E> head;
	LLNode<E> tail;
	int size;

	/** Create a new empty LinkedList */
	public MyLinkedList()
	{
		size = 0;

		head = new LLNode<>(null);
		tail = new LLNode<>(null);

		head.next = tail;
		tail.prev = head;
	}

	/**
	 * Appends an element to the end of the list
	 * @param element The element to add
	 */
	public boolean add(E element)
	{
		LLNode<E> node = new LLNode<>(element);
		LLNode<E> temp = tail.prev;

		node.next = tail;
		node.prev = temp;

		temp.next = node;
		tail.prev = node;

		++size;

		return true;
	}

	/** Get the element at position index 
	 * @throws IndexOutOfBoundsException if the index is out of bounds. */
	public E get(int index) 
	{
		if(IsEmpty())                  throw new IndexOutOfBoundsException("Can't access an element from an empty list.");
		if(index < 0 || index >= size) throw new IndexOutOfBoundsException("The index must have a value from 0 to the list size.");
		else
		{
			if(index == 0)             return head.next.data;
			else if(index == size - 1) return tail.prev.data;
			else
			{
				LLNode<E> temp = head.next;
				for(int it = 0; it != index; ++it) temp = temp.next;

				return temp.data;
			}
		}
	}

	/**
	 * Add an element to the list at the specified index
	 * @param index The index where the element should be added
	 * @param element The element to add
	 */
	public void add(int index, E element ) 
	{
		if(index < 0 || index > size) throw new IndexOutOfBoundsException("The index must have a value from 0 to the list size.");
		if(element == null)           throw new NullPointerException("The element can not be null.");

		LLNode<E> node = new LLNode<>(element);

		if(index == 0)
		{
			LLNode<E> temp = head.next;

			node.next = temp;
			node.prev = head;

			temp.prev = node;
			head.next = node;
		}
		else if(index == size-1)
		{
			LLNode<E> temp = tail.prev;

			node.next = tail;
			node.prev = temp;

			temp.next = node;
			tail.prev = node;
		}
		else
		{
			LLNode<E> temp = head;

			for(int it = 0; it != index; ++it) temp = temp.next;

			LLNode<E> next_to_node = temp.next;

			node.next = temp.next;
			temp.next = node;
			node.prev = temp;
			next_to_node.prev = node;
		}

		++size;
	}


	/** Return the size of the list */
	public int size() { return size; }

	/** Remove a node at the specified index and return its data element.
	 * @param index The index of the element to remove
	 * @return The data element removed
	 * @throws IndexOutOfBoundsException If index is outside the bounds of the list
	 * 
	 */
	public E remove(int index) 
	{
		if(index < 0 || index >= size) throw new IndexOutOfBoundsException("The index must have a value from 0 to the list size.");
		if(IsEmpty())                  throw new IndexOutOfBoundsException("Can't remove an element from an empty list.");
		else
		{
			if(index == 0)
			{
				LLNode<E> temp = head.next;

				head.next      = temp.next;
				temp.next.prev = head;

				--size;

				return temp.data;
			}
			else if(index == size - 1)
			{
				LLNode<E> temp = tail.prev;

				tail.prev      = temp.prev;
				temp.prev.next = tail;

				--size;

				return temp.data;
			}
			else
			{
				LLNode<E> temp = head.next;

				for(int it = 0; it != index; ++it) temp = temp.next;

				LLNode<E> temp_next = temp.next;
				LLNode<E> temp_prev = temp.prev;

				temp_prev.next = temp.next;
				temp_next.prev = temp.prev;

				--size;

				return temp.data;
			}
		}
	}

	/**
	 * Set an index position in the list to a new element
	 * @param index The index of the element to change
	 * @param element The new element
	 * @return The element that was replaced
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 */
	public E set(int index, E element) 
	{
		if(element == null)            throw new NullPointerException("The element can not be null.");
		if(index < 0 || index >= size) throw new IndexOutOfBoundsException("The index must have a value from 0 to the list size.");

		if(index == 0)           return GetElement(head.next, element);
		else if(index == size-1) return GetElement(tail.prev, element);
		else
		{
			LLNode<E> temp = head.next;

			for(int it = 0; it != index; ++it) temp = temp.next;

			return GetElement(temp, element);
		}
	}

	public boolean IsEmpty() { return size == 0; }

	private E GetElement(LLNode<E> node, E new_element)
	{
		if(node == null) throw new NullPointerException();

		E old_element = node.data;
		node.data = new_element;

		return old_element;
	}
}

class LLNode<E> 
{
	LLNode<E> prev;
	LLNode<E> next;
	E data;

	public LLNode(E e)
	{
		this.data = e;
		this.prev = null;
		this.next = null;
	}
}
