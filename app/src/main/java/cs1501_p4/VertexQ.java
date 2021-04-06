/**
 * Queue for Vertices used in lowestLatencyPath()
 * @author	David Roberts
 */
package cs1501_p4;

public class VertexQ
{
	/**
	 * Array representation of the heap
	 */
	private int[] nodeArray;

	/**
	 * Number of nodes in VertexPQ
	 */
	private int size;

	/**
	 * Basic constructor
	 */
	public VertexQ(int size)
	{
		nodeArray = new int[size];
		size = 0;
	}

	/**
	 * Adds vertices to queue, keeping first in first out order
	 * 
	 * @param 	vertex index of vertex to add to queue
	 */
	public void add(int vertex)
	{
		nodeArray[size] = vertex;
		size++;
	}

	/**
	 * Removes next vertex from queue and moves each vertex ups
	 */
	public int getRoot()
	{
		int temp = nodeArray[0];

		for (int i=0; i<size-1; i++)
		{
			nodeArray[i] = nodeArray[i+1];
		}
		size--;

		return temp;
	}

	/**
	 * Checks to see if a certain vertex is in the queue
	 * 
	 * @param 	v vertex of interest
	 */
	public boolean contains(int v)
	{
		for (int i=0; i<size-1; i++)
		{
			if (nodeArray[i] == v)
				return true;
		}
		return false;
	}

	/**
	 * @return 	true if queue has elements, false if queue is empty
	 */
	public boolean hasStuff()
	{
		if (size > 0)
			return true;
		else
			return false;
	}

	public void print()
	{
		for (int i=0; i<size; i++)
		{
			System.out.println(nodeArray[i]);
		}
	}
}