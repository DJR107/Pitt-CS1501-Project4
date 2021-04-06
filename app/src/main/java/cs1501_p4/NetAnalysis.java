/**
 * Network Analysis specification interface for CS1501 Project 4
 * @author	David Roberts
 */
package cs1501_p4;

import java.util.*;
import java.io.*;
import java.math.*;

public class NetAnalysis implements NetAnalysis_Inter
{
	/**
	 * Adjacency List of edges
	 */
	private EdgeNode[] graph;

	/**
	 * Number of vertices in graph
	 */
	private int numOfVertices;

	/**
	 * Basic constructor
	 */
	public NetAnalysis(String fileName)
	{
		try
		{
			File file = new File(fileName);
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine())
			{
				String newEdge = scan.nextLine();
				try
				{
					int length = Integer.parseInt(newEdge);
					graph = new EdgeNode[length];
					numOfVertices = length;
				}
				catch (Exception e)
				{
					String[] newEdgeData = newEdge.split(" ");

					int origin = Integer.parseInt(newEdgeData[0]);
					int destination = Integer.parseInt(newEdgeData[1]);
					String type = newEdgeData[2];
					int bandwidth = Integer.parseInt(newEdgeData[3]);
					int length = Integer.parseInt(newEdgeData[4]);
					int latency;
					if (type.compareTo("copper") == 0)
						latency = length*20;
					else
						latency = length*23;

					if (graph[origin] == null)
					{
						graph[origin] = new EdgeNode(new STE(origin, destination), type, bandwidth, length, latency);
					}
					else
					{
						EdgeNode curr = graph[origin];
						while(curr != null && curr.next != null)
							curr = curr.next;
						curr.setNext(new EdgeNode(new STE(origin, destination), type, bandwidth, length, latency));
					}

					if (graph[destination] == null)
					{
						graph[destination] = new EdgeNode(new STE(destination, origin), type, bandwidth, length, latency);
					}
					else
					{
						EdgeNode curr = graph[destination];
						while(curr != null && curr.next != null)
							curr = curr.next;
						curr.setNext(new EdgeNode(new STE(destination, origin), type, bandwidth, length, latency));
					}
				}
			}
			scan.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Find the lowest latency path from vertex `u` to vertex `w` in the graph
	 * IS DIJKSTRA
	 *
	 * @param	u Starting vertex
	 * @param	w Destination vertex
	 *
	 * @return	ArrayList<Integer> A list of the vertex id's representing the
	 * 			path (should start with `u` and end with `w`)
	 * 			Return `null` if no path exists
	 */
	public ArrayList<Integer> lowestLatencyPath(int u, int w)
	{
		int[] distance = new int[numOfVertices];
		for (int i=0; i<numOfVertices; i++)
			distance[i] = Integer.MAX_VALUE;

		StringBuilder[] prev = new StringBuilder[numOfVertices];
		for (int i=0; i<numOfVertices; i++)
			prev[i] = new StringBuilder();

		boolean[] visited = new boolean[numOfVertices];
		visited[u] = true;

		distance[u] = 0;

		VertexQ q = new VertexQ(numOfVertices);
		q.add(u);

		int currNode = u;

		while (q.hasStuff())
		{
			//System.out.println("Q: ");
			//q.print();
			currNode = q.getRoot();
			visited[currNode] = true;
			//System.out.println("Current Node: "+currNode);

			EdgeNode curr = graph[currNode];
			while (curr != null)
			{
				if (!visited[curr.ste.w])
				{
					//System.out.println("Found Edge: ("+curr.ste.u+","+curr.ste.w+"): "+curr.latency);
					if (!q.contains(curr.ste.w))
						q.add(curr.ste.w);

					int dist = distance[curr.ste.u] + curr.latency;
					if (dist < distance[curr.ste.w])
					{
						distance[curr.ste.w] = dist;
						if (prev[curr.ste.w] == null)
						{
							prev[curr.ste.w].append(prev[curr.ste.u]);
							prev[curr.ste.w].append(curr.ste.u+" ");
						}
						else
							prev[curr.ste.w] = new StringBuilder(prev[curr.ste.u]+" "+curr.ste.u+" ");
					}
				}
				curr = curr.next;
			}
		}

		ArrayList<Integer> list = new ArrayList<Integer>();

		StringBuilder path = prev[w];
		if (path == null)
			return null;

		for (int i=0; i<path.length(); i++)
		{
			try
			{
				list.add(Integer.parseInt(String.valueOf(path.charAt(i))));
			}
			catch (NumberFormatException ignored) {}
		}
		list.add(w);

		return list;
	}

	/**
	 * Find the bandwidth available along a given path through the graph
	 * (the minimum bandwidth of any edge in the path). Should throw an
	 * `IllegalArgumentException` if the specified path is not valid for
	 * the graph.
	 *
	 * @param	ArrayList<Integer> A list of the vertex id's representing the
	 * 			path
	 *
	 * @return	int The bandwidth available along the specified path
	 */
	public int bandwidthAlongPath(ArrayList<Integer> p) throws IllegalArgumentException
	{
		int bandwidth = 0;

		for (int i=0; i<p.size()-1; i++)
		{
			EdgeNode curr = graph[p.get(i)];
			while (curr.ste.w != p.get(i+1))
				curr = curr.next;
			
			int b = curr.bandwidth;
			bandwidth = bandwidth + b;
		}

		return bandwidth;
	}

	/**
	 * Return `true` if the graph is connected considering only copper links
	 * `false` otherwise
	 *
	 * @return	boolean Whether the graph is copper-only connected
	 */
	public boolean copperOnlyConnected()
	{
		int[] idArray = new int[numOfVertices];
		for (int i=0; i<numOfVertices; i++)
		{
			idArray[i] = i;
		}

		for (int i=0; i<numOfVertices; i++)
		{
			EdgeNode curr = graph[i];
			while(curr != null)
			{
				if (curr.cableType.compareTo("copper") == 0)
				{
					int id = idArray[curr.ste.u];
					for (int j=0; j<numOfVertices; j++)
					{
						if (idArray[j] == id)
							idArray[j] = curr.ste.w;
					}
					idArray[curr.ste.u] = curr.ste.w;
					idArray[curr.ste.w] = curr.ste.w;
				}
				curr = curr.next;
			}
		}

		for (int i=0; i<numOfVertices; i++)
		{
			int id = idArray[i];
			for (int j=0; j<numOfVertices; j++)
			{
				if (idArray[j] != id)
					return false;
			}
		}
		return true;
	}

	/**
	 * Return `true` if the graph would remain connected if any two vertices in
	 * the graph would fail, `false` otherwise
	 *
	 * @return	boolean Whether the graph would remain connected for any two
	 * 			failed vertices
	 */
	public boolean connectedTwoVertFail()
	{
		boolean[] visited = new boolean[numOfVertices];
		int[] num = new int[numOfVertices];
		int[] low = new int[numOfVertices];
		int[] parent = new int[numOfVertices];

		int start = 0;
		for (int i=0; i<numOfVertices; i++)
		{
			if (graph[i] != null)
			{
				start = graph[i].ste.u;
				parent[start] = -1;
				break;
			}
		}

		int articulationPoints = dfsRec(start, visited, num, low, parent, 0, 0);
		//System.out.println("Number of Articulation Points: "+articulationPoints);

		if (articulationPoints >= 2)
			return false;
		else
			return true;
	}

	/**
	 * DFS helper function for connectedTwoVertFail()
	 *
	 * @param	v current vertex
	 * @param 	visited boolean array of visited nodes
	 * @param 	num int array of preorder traversal order
	 * @param 	low int array of lowest-numbered vertex reachable
	 * @param 	parent int array of parent nodes for each vertex
	 * @param 	numSize count of preorder traversal order
	 * @param 	numOfArticPoints count of articulation points
	 *
	 * @return	number of articulation points
	 */
	private int dfsRec(int v, boolean[] visited, int[] num, int[] low, int[] parent, int numSize, int numOfArticPoints)
	{
		//System.out.println("CurrNode: "+v+" w/ numSize: "+numSize);
		visited[v] = true;
		num[v] = numSize;
		low[v] = numSize;

		int children = 0;

		EdgeNode curr = graph[v];
		while (curr != null)
		{
			if (!visited[curr.ste.w])
			{
				//System.out.println("Found Edge: ("+curr.ste.u+"->"+curr.ste.w+")");
				parent[curr.ste.w] = v;
				children++;
				numOfArticPoints = dfsRec(curr.ste.w, visited, num, low, parent, numSize+1, numOfArticPoints);

				low[v] = Math.min(low[v], low[curr.ste.w]);

				if (parent[v] == -1 && children > 1)
				{
					//System.out.println("Found Articulation Point: "+v+" via 1st test");
					numOfArticPoints++;
				}

				if (parent[v] != -1 && low[curr.ste.w] >= num[v])
				{
					//System.out.println("Found Articulation Point: "+v+" via 2nd test");
					numOfArticPoints++;
				}
			}
			else if (curr.ste.w != parent[v])
			{
				low[v] = Math.min(low[v], num[curr.ste.w]);
			}
			curr = curr.next;
		}
		//System.out.println("Popped");
		return numOfArticPoints;
	}

	/**
	 * Find the lowest average (mean) latency spanning tree for the graph
	 * (i.e., a spanning tree with the lowest average latency per edge). Return
	 * it as an ArrayList of STE edges.
	 *
	 * Note that you do not need to use the STE class to represent your graph
	 * internally, you only need to use it to construct return values for this
	 * method.
	 *
	 * @return	ArrayList<STE> A list of STE objects representing the lowest
	 * 			average latency spanning tree
	 * 			Return `null` if the graph is not connected
	 */
	public ArrayList<STE> lowestAvgLatST()
	{
		EdgePQ pq = new EdgePQ();

		for (int i=0; i<numOfVertices; i++)
		{
			EdgeNode curr = graph[i];
			while(curr != null)
			{
				pq.add(curr);
				curr = curr.next;
			}
		}

		ArrayList<STE> mst = new ArrayList<STE>();
		int[] idArray = new int[numOfVertices];
		for (int i=0; i<numOfVertices; i++)
		{
			idArray[i] = i;
		}

		for (int i=0; i<numOfVertices-1; i++)
		{
			EdgeNode root = pq.getRoot();
			//System.out.println("Root: ("+root.ste.u+","+root.ste.w+")");
			if (idArray[root.ste.u] != idArray[root.ste.w])
			{
				mst.add(root.ste);

				int id = idArray[root.ste.u];
				for (int j=0; j<numOfVertices; j++)
				{
					if (idArray[j] == id)
						idArray[j] = root.ste.w;
				}
				idArray[root.ste.u] = root.ste.w;
				idArray[root.ste.w] = root.ste.w;
			}
			else
				i--;
		}

		return mst;
	}

	public void print()
	{
		for (int i=0; i<numOfVertices; i++)
		{
			EdgeNode curr = graph[i];
			while(curr != null)
			{
				System.out.print(curr.ste.u+"->"+curr.ste.w);
				if (curr.next != null)
					System.out.print(", ");
				curr = curr.next;
			}
			System.out.println();
		}
	}
}