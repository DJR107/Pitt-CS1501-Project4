/**
 * A driver for CS1501 Project 4
 * @author	Dr. Farnan
 */
package cs1501_p4;

import java.util.*;
import java.io.*;

public class App {
    public static void main(String[] args) {
        NetAnalysis na = new NetAnalysis("build/resources/main/network_data2.txt");

        na.print();

     	System.out.println();

     	if (na.connectedTwoVertFail())
     		System.out.println("TRUE: Graph does not have two articulation points");
     	else
     		System.out.println("FALSE: Graph has two articulation points");

     	System.out.println();

     	ArrayList<Integer> list = na.lowestLatencyPath(0, 5);
     	System.out.print("Shortest Path from 0 to 5: ");
     	for (int i : list)
     	{
     		System.out.print(i + " ");
     	}

     	System.out.println("\n");

     	boolean what = na.copperOnlyConnected();
     	if (what)
     		System.out.println("YES: Graph is copper only connected");
     	else
     		System.out.println("NO: Graph is not copper only connected");

     	System.out.println();

     	System.out.println("Lowest Average Latency Spanning Tree:");
     	ArrayList<STE> lowestAvgLatST = na.lowestAvgLatST();
     	for (STE ste : lowestAvgLatST)
     	{
     		System.out.println("("+ste.u+","+ste.w+")");
     	}

     	System.out.println();

        list = new ArrayList<Integer>();
        list.add(0);
        list.add(2);
        list.add(5);
        list.add(3);
        //list.add(5);
        System.out.println("Bandwidth along path: "+na.bandwidthAlongPath(list));
    }
}
