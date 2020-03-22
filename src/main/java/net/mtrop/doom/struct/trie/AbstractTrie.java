/*******************************************************************************
 * Copyright (c) 2015-2020 Matt Tropiano
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package net.mtrop.doom.struct.trie;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * A trie is a data structure that contains objects, using a path
 * of objects derived from the stored value. This structure is not thread-safe - wrap calls
 * with synchronized blocks if necessary.
 * @author Matthew Tropiano
 * @param <V> the value type that this holds.
 * @param <S> the type of the split segments used for searching.
 */
public abstract class AbstractTrie<V extends Object, S extends Object> implements Iterable<V>
{
	/** Root Node. */
	protected Node<V, S> root;
	
	/** Current size. */
	private int size;

	/**
	 * Creates a new trie.
	 */
	public AbstractTrie()
	{
		root = new Node<V, S>();
		size = 0;
	}

	/**
	 * Creates the segments necessary to find/store values.
	 * This should always create the same segments for the same value.
	 * @param value the value to generate significant segments for.
	 * @return the list of segments for the value.
	 */
	protected abstract S[] getSegments(V value);

	/**
	 * Returns all values in the order that they are found on the way through the Trie searching for a
	 * particular value. Result may include the value searched for.
	 * <p>The results are added to the end of the list.
	 * @param value the value to search for.
	 * @param out the output list.
	 * @return the amount of items returned into the list.
	 */
	public int getBefore(V value, List<V> out)
	{
		return getBefore(value, out, out.size());
	}
	
	/**
	 * Returns all values in the order that they are found on the way through the Trie searching for a
	 * particular value. Result may include the value searched for.
	 * <p>The results are set in the output list provided by the user - an offset before
	 * the end of the list replaces, not adds!
	 * @param value the value to search for.
	 * @param out the output list.
	 * @param startOffset the starting offset into the list to set values.
	 * @return the amount of items returned into the list.
	 */
	public int getBefore(V value, List<V> out, int startOffset)
	{
		Result<V, S> result = search(value, true, false);
		int added = 0;
		for (V obj : result.encounteredValues)
			out.set(startOffset + (added++), obj);
		return added;
	}
	
	/**
	 * Returns all values descending from the end of a search for a
	 * particular value. Result may include the value searched for. 
	 * <p>The values returned may not be returned in any consistent or stable order.
	 * <p>The results are added to the end of the list.
	 * @param value the value to search for.
	 * @param out the output list.
	 * @return the amount of items returned into the list.
	 */
	public int getAfter(V value, List<V> out)
	{
		return getAfter(value, out, out.size());
	}

	/**
	 * Returns all values descending from the end of a search for a
	 * particular value. Result may include the value searched for. 
	 * <p>The values returned may not be returned in any consistent or stable order.
	 * <p>The results are set in the output list provided by the user - an offset before
	 * the end of the list replaces, not adds!
	 * @param value the value to search for.
	 * @param out the output list.
	 * @param startOffset the starting offset into the list to set values.
	 * @return the amount of items returned into the list.
	 */
	public int getAfter(V value, List<V> out, int startOffset)
	{
		Result<V, S> result = search(value, false, true);
		int added = 0;
		for (V obj : result.remainderValues)
			out.set(startOffset + (added++), obj);
		return added;
	}
	
	/**
	 * Returns the last-encountered value down a trie search, plus
	 * the remainder of the segments generated by the key from the last-matched
	 * segment.
	 * @param value the value to search for.
	 * @param out the output list.
	 * @return the last-encountered value.
	 */
	public V getWithRemainder(V value, List<S> out)
	{
		return getWithRemainder(value, out, 0);
	}

	/**
	 * Returns the last-encountered value down a trie search, plus
	 * the remainder of the segments generated by the key from the last-matched
	 * segment.
	 * @param value the value to search for.
	 * @param out the output list.
	 * @param startOffset the starting offset into the list to set values.
	 * @return the last-encountered value, or null if none encountered.
	 */
	public V getWithRemainder(V value, List<S> out, int startOffset)
	{
		Result<V, S> result = search(value, true, false);
		
		if (result.foundValue != null)
		{
			return result.foundValue;
		}
		else
		{
			for (int i = result.movesToLastEncounter; i < result.segments.length; i++)
				out.set(startOffset + (i - result.movesToLastEncounter), result.segments[i]);
			
			if (!result.encounteredValues.isEmpty())
				return result.encounteredValues.get(result.encounteredValues.size() - 1);
			else
				return null;
		}
	}

	/**
	 * Adds a value to this structure, but only if it does not exist.
	 * If the value is already in the set, this does nothing. Else, it is added.
	 * @param value the value to add.
	 * @see AbstractTrie#equalityMethod(Object, Object)
	 */
	public void add(V value)
	{
		if (value == null)
			throw new IllegalArgumentException("Value cannot be null.");
		
		S[] segments = getSegments(value);
		int segindex = 0;
		
		Node<V, S> current = root;
		Node<V, S> next = null;
		while (segindex < segments.length)
		{
			if ((next = current.edgeMap.get(segments[segindex])) == null)
				current.edgeMap.put(segments[segindex], next = new Node<V, S>());
			
			current = next;
			segindex++;
		}
		
		V prevval = current.value;
		current.value = value;
		if (prevval == null)
			size++;
	}

	/**
	 * Checks if an object (by equality) is present in the structure.
	 * @param object the object to use for checking presence.
	 * @return true if it is in the structure, false otherwise.
	 * @see AbstractTrie#equalityMethod(Object, Object)
	 */
	public boolean contains(V object)
	{
		return equalityMethod(object, search(object, false, false).foundValue);
	}

	/**
	 * Removes an object from this structure.
	 * @param object the object to use for checking presence.
	 * @return true if it was removed from the structure, false otherwise.
	 */
	public boolean remove(V object)
	{
		S[] segments = getSegments(object);
		return removeRecurse(object, root, segments, 0) != null; 
	}
	
	/**
	 * Determines if the objects are equal. This can be implemented differently
	 * in case a data structure has a different concept of what is considered equal.
	 * @param object1 the first object.
	 * @param object2 the second object.
	 * @return true if the keys are considered equal, false otherwise.
	 */
	protected boolean equalityMethod(V object1, V object2)
	{
		if (object1 == null)
			return object2 == null;
		else if (object2 == null)
			return false;
		else 
			return object1.equals(object2);
	}

	/**
	 * @return the amount of elements that this trie holds.
	 */
	public int size()
	{
		return size;
	}

	/**
	 * @return true if this trie is empty, false if not.
	 */
	public boolean isEmpty()
	{
		return size() == 0;
	}

	/**
	 * Puts the contents of this trie into an array.
	 * If the array is too small for the contents of this trie, this will throw an {@link ArrayIndexOutOfBoundsException}.
	 * @param out the output array.
	 * @throws ArrayIndexOutOfBoundsException if the array is too small to fit every element.
	 */
	public void toArray(V[] out)
	{
		int i = 0;
		for (V value : this)
			out[i++] = value;
	}
	
	/**
	 * Removes all objects from the trie.
	 */
	public void clear()
	{
		root = null;
		size = 0;
	}
	
	@Override
	public TrieIterator<V, S> iterator()
	{
		return new TrieIterator<>(this);
	}

	/**
	 * Returns a search result generated from walking the edges of the trie looking for
	 * a particular value.
	 * @param value the value to search for.
	 * @param includeEncountered if true, includes encountered (on the way) during the search.
	 * @param includeDescendants if true, includes descendants (remainder) after the search.
	 * @return a trie {@link Result}. The result contents describe matches, encounters, and remainder, plus hops.
	 */
	protected Result<V, S> search(V value, boolean includeEncountered, boolean includeDescendants)
	{
		S[] segments = getSegments(value);
		List<V> encountered = includeEncountered ? new ArrayList<V>() : null;
		List<V> descending = includeDescendants ? new ArrayList<V>() : null;
		int encIndex = 0;
		int segIndex = 0;
		
		Node<V, S> current = root;
		
		if (encountered != null && current.value != null)
		{
			encountered.add(current.value);
			encIndex = segIndex;
		}

		while (segIndex < segments.length && current != null && current.hasEdges())
		{
			if (current.edgeMap.containsKey(segments[segIndex]))
			{
				current = current.edgeMap.get(segments[segIndex]);
				segIndex++;
			}
			else
			{
				current = null;
				break;
			}

			if (encountered != null && current.value != null)
			{
				encountered.add(current.value);
				encIndex = segIndex;
			}
				
		}
		
		if (current == null)
		{
			// don't get descendants. there won't be any.
			return new Result<V, S>(
				null,
				encountered,
				descending,
				segments,
				encIndex,
				segIndex
			);
		}
		// terminated due to end of segments?
		else if (segIndex == segments.length)
		{
			// get descendants if necessary.
			if (descending != null)
				getDescendantsRecurse(current, descending);
			
			return new Result<V, S>(
				equalityMethod(value, current.value) ? current.value : null,
				encountered,
				descending,
				segments,
				encIndex,
				segIndex
			);
		}
		else
		{
			return new Result<V, S>(
				null,
				encountered,
				descending,
				segments,
				encIndex,
				segIndex
			);
		}
		
	}

	/**
	 * Recurses through the trie for an object and removes it, cleaning up empty
	 * nodes in the way back. 
	 * @param object the object to look for.
	 * @param node the starting node.
	 * @return true if any removal occurred down the tree, false if not.
	 */
	V removeRecurse(V object, Node<V, S> node, S[] segments, int sidx)
	{
		V out = null;
	
		if (equalityMethod(node.value, object))
		{
			out = node.value;
			node.value = null;
			size--;
			return out;
		}
		
		Node<V, S> next = node.edgeMap.get(segments[sidx]);
		if (next == null)
			return null;
		
		if ((out = removeRecurse(object, next, segments, sidx + 1)) == null)
		{
			if (next != root && next.isExpired())
				node.edgeMap.remove(segments[sidx]);
			return out;
		}
		else
			return null;
	}

	/**
	 * Returns all possible values that can be used based on an input key.
	 */
	private void getDescendantsRecurse(Node<V, S> start, List<V> accum)
	{
		V value = start.getValue();
		if (value != null)
			accum.add(value);
		
		for (Map.Entry<S, Node<V, S>> pair : start.edgeMap.entrySet())
			getDescendantsRecurse(pair.getValue(), accum);
	}

	/**
	 * Iterator for this Trie.
	 * @param <V> the value type that this holds.
	 * @param <S> the type of the split segments used for searching.
	 */
	protected static class TrieIterator<V, S> implements Iterator<V>
	{
		private AbstractTrie<V, S> self;
		private Queue<Node<V, S>> edgeQueue;
		
		private TrieIterator(AbstractTrie<V, S> trie)
		{
			this.self = trie;
			reset();
		}
	
		@Override
		public boolean hasNext()
		{
			return !edgeQueue.isEmpty();
		}
	
		@Override
		public V next()
		{
			while (!edgeQueue.isEmpty())
			{
				Node<V, S> node = seekForQueue();
				if (node.value != null)
					return node.value;
			}
			return null;
		}
	
		/**
		 * Resets this iterator.
		 */
		public void reset()
		{
			this.edgeQueue = new LinkedList<Node<V, S>>();
			this.edgeQueue.add(self.root);
			seekForQueue();
		}
		
		private Node<V, S> seekForQueue()
		{
			Node<V, S> deq = edgeQueue.poll();
			for (Map.Entry<S, Node<V, S>> pair : deq.edgeMap.entrySet())
				edgeQueue.add(pair.getValue());
			return deq;
		}
		
	}

	/**
	 * A result of a passive search on a trie.
	 * @param <V> the value type that this holds.
	 * @param <S> the type of the split segments used for searching.
	 */
	protected static class Result<V, S>
	{
		private V foundValue;
		private List<V> encounteredValues;
		private List<V> remainderValues;
		private S[] segments;
		private int movesToLastEncounter;
		private int moves;
		
		Result(V found, List<V> encountered, List<V> remainder, S[] segments, int movesToLastEncounter, int moves)
		{
			this.foundValue = found;
			this.encounteredValues = encountered;
			this.remainderValues = remainder;
			this.segments = segments;
			this.movesToLastEncounter = movesToLastEncounter;
			this.moves = moves;
		}
		
		/**
		 * @return the value on the result, if something was found.
		 */
		public V getFoundValue() 
		{
			return foundValue;
		}
		
		/**
		 * @return the list of values found along the way of a search.
		 */
		public List<V> getEncounteredValues()
		{
			return encounteredValues;
		}
		
		/**
		 * @return the list of values descending from the endpoint of a search.
		 */
		public List<V> getDescendantValues()
		{
			return remainderValues;
		}
		
		/**
		 * @return the segments generated by the input value.
		 */
		public S[] getSegments()
		{
			return segments;
		}
		
		/**
		 * @return how many moves it took to find the last encountered object in a search.
		 */
		public int getMovesToLastEncounter()
		{
			return movesToLastEncounter;
		}
		
		/**
		 * @return how many edge hops that this performed in order to reach the result.
		 */
		public int getMoveCount() 
		{
			return moves;
		}
		
		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Found: ").append(foundValue).append('\n');
			sb.append("Encountered: ").append(String.valueOf(encounteredValues)).append('\n');
			sb.append("Remainder: ").append(String.valueOf(remainderValues)).append('\n');
			sb.append("Moves: ").append(moves);
			return sb.toString();
		}
		
	}

	/**
	 * A single node in the Trie.
	 * @param <V> the value type that this holds.
	 * @param <S> the type of the split segments used for searching.
	 */
	protected static class Node<V, S>
	{
		/** Edge map. */
		private AbstractMap<S, Node<V, S>> edgeMap;
		/** Value stored at this node. Can be null. */
		private V value;
		
		protected Node()
		{
			edgeMap = new HashMap<S, Node<V, S>>(2, 1f);
			value = null;
		}
		
		void deleteValue()
		{
			this.value = null;
		}
		
		/**
		 * @return this node's value.
		 */
		V getValue()
		{
			return value;
		}
		
		/**
		 * @return the reference to edge map.
		 */
		Map<S, Node<V, S>> getEdgeMap()
		{
			return edgeMap;
		}
		
		/**
		 * @return if this node can be cleaned up.
		 */
		boolean isExpired()
		{
			return edgeMap.isEmpty() && value == null;
		}
		
		/**
		 * @return if the node has possible paths.
		 */
		boolean hasEdges()
		{
			return !edgeMap.isEmpty();
		}
		
	}

}


