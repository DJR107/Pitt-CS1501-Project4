/**
 * EdgeNode class that includes all necesary data for edge
 * @author	David Roberts
 */
package cs1501_p4;

import java.math.*;

public class EdgeNode
{
	/**
	 * Spanning Tree Edge for the node
	 */
	protected STE ste;

	/**
	 * The type of cable (i.e. copper, optical)
	 */
	protected String cableType;

	/**
	 * Bandwidth of cable/edge
	 */
	protected int bandwidth;

	/**
	 * Length of edge in meters
	 */
	protected int length;

	/**
	 * Latency of edge
	 */
	protected int latency;

	/**
	 * Next edge node in adjacency list
	 */
	protected EdgeNode next;

	/**
	 * Basic constructor
	 */
	public EdgeNode(STE s, String c, int b, int l, int l1)
	{
		ste = s;
		cableType = c;
		bandwidth = b;
		length = l;
		latency = l1;
	}

	/**
	 * Set next node to n
	 */
	public void setNext(EdgeNode n)
	{
		next = n;
	}
}