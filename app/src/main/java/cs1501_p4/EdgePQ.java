/**
 * Priority Queue for Edges used in lowestAvgLatST()
 * @author	David Roberts
 */
package cs1501_p4;

public class EdgePQ
{
	/**
	 * Array representation of the heap
	 */
	private EdgeNode[] edgeArray;

	/**
	 * Number of nodes in EdgePQ
	 */
	private int size;

	/**
	 * Basic Constructor
	 */
	public EdgePQ()
	{
		edgeArray = new EdgeNode[30];
		size = 0;
	}

	/**
	 * Add edges to priority queue, keeping edge with lowest latency on top
	 * 
	 * @param 	e Edge to add to queue
	 */
	public void add(EdgeNode e)
	{
		edgeArray[size] = e;

		int index = size;
		while (index > 0)
		{
			if (index % 2 == 1)
			{
				index = (index-1)/2;
				EdgeNode parent = edgeArray[index];
				if (e.latency < parent.latency)
				{
					edgeArray[(2*index)+1] = parent;
				}
				else
				{
					edgeArray[(2*index)+1] = e;
					break;
				}
			}
			else if (index % 2 == 0)
			{
				index = (index-2)/2;
				EdgeNode parent = edgeArray[index];
				if (e.latency < parent.latency)
				{
					edgeArray[(2*index)+2] = parent;
				}
				else
				{
					edgeArray[(2*index)+2] = e;
					break;
				}
			}
			if (index == 0)
			{
				edgeArray[index] = e;
			}
		}
		size++;
	}

	/**
	 * Returns edge with least latency and reorganizes queue after removal of root
	 * 
	 * @return 	EdgeNode with smallest latency
	 */
	public EdgeNode getRoot()
	{
		EdgeNode root = edgeArray[0];

		EdgeNode last = edgeArray[size-1];
		edgeArray[size-1] = null;

		edgeArray[0] = last;

		EdgeNode edgeToSwap = edgeArray[0];
		int index = 0;
		try
		{
			while (edgeArray[(2*index)+1] != null)
			{
				if (edgeArray[(2*index)+2] != null)
				{
					if (edgeArray[index].latency < edgeArray[(2*index)+1].latency && edgeArray[index].latency < edgeArray[(2*index)+2].latency)
						break;

					if (edgeArray[(2*index)+1].latency < edgeArray[(2*index)+2].latency)
					{
						EdgeNode tempEdge = edgeArray[(2*index)+1];
						edgeArray[(2*index)+1] = edgeToSwap;
						edgeArray[index] = tempEdge;

						index = (2*index)+1;
						edgeToSwap = edgeArray[index];					
					}
					else
					{
						EdgeNode tempEdge = edgeArray[(2*index)+2];
						edgeArray[(2*index)+2] = edgeToSwap;
						edgeArray[index] = tempEdge;

						index = (2*index)+2;
						edgeToSwap = edgeArray[index];	
					}
				}
				else
				{
					if (edgeArray[index].latency > edgeArray[(2*index)+1].latency)
					{
						EdgeNode tempEdge = edgeArray[(2*index)+1];
						edgeArray[(2*index)+1] = edgeToSwap;
						edgeArray[index] = tempEdge;

						index = (2*index)+1;
						edgeToSwap = edgeArray[index];
					}
					else
						break;
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException ignored) {}
		size--;

		return root;
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
			System.out.println("("+edgeArray[i].ste.u+","+edgeArray[i].ste.w+"): "+edgeArray[i].latency);
		}
	}
}