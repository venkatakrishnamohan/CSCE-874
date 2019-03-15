import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
/* Assignment 03 for CSCE 874 - Data Mining Course at UNL by Krishnamohan Sunkara and Bill Mutabazi*/
/* Clustering is the task of Finding groups of objects such that the objects in a group will be 
 * similar to one another and different from the objects in other groups.
 * Our program takes 3 inputs from the user:
 * 1. Number of Clusters to be formed: k
 * 2. Stopping Criteria : Max number of iterations allowed
 * 3. Stopping Criteria : Value of epsilon
 * Here we Implement the K-Means Algorithm with only numeric data as:
 * Read the data from an arff file
 * Normalize the instances of the data.
 * Form the Initial k-centroids at random
 * Repeat
 * 		Form K clusters by assigning all points to the nearest centroid.
 * 		Recompute the centroid of each cluster
 * until one of the stopping criteria is met.
 */

public class Kmeans {
	// An Array List of Lists to store the instances read from a file.
	public static List<List<Double>> db = new ArrayList<>();
	// An array List of Lists to store the centroids formed in every iteration
	public static List<List<Double>> cc = new ArrayList<>();
	public static Map<Integer,List<Integer>> clusters = new HashMap<>();
	//public static List<List<Integer>> assg = new ArrayList<>();
	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		System.out.println("Enter the full path of the dataset:");
		String path = reader.next();
		System.out.println("Enter the number of clusters k:");
		int k = reader.nextInt();
		System.out.println("Enter the epsilon value in decimal values between 0 and 1:");
		double e = reader.nextDouble();
		System.out.println("Enter the maximum number of iteration allowed:");
		int iterations = reader.nextInt();
		reader.close();
		try {
		// Provide the path for the dataset
		BufferedReader br =new BufferedReader(new FileReader(path));
		saveInstances(br);
		// Normalize the data
		db = normalizeData();
		System.out.println(db.size());
		long starttime = System.nanoTime();
		System.out.println("Database instances after normalization:");
		System.out.println(db);
		cc = initialCentroids(k);
		System.out.println("Initial Centroids:");
		System.out.println(cc);
		double ce = Double.POSITIVE_INFINITY;
		int ci = 0;
		// K-means Algorithm
		while(ce > e && ci < iterations) {
			List<List<Double>> dist = calcDist(cc);
			List<Integer> ptassg = assignCluster(dist);
			clusters = ptsToClusters(ptassg,k);
			List<List<Double>> nc = recomputeCentroids(clusters,cc);
			ce = calcEpsilon(cc, nc);
			cc = nc;
			ci++;
		}
		long endtime = System.nanoTime();
		System.out.println("Total number of points in the database:"+ db.size());
		System.out.println("Clusters formed are:");
		for(int i=0;i<clusters.size();i++) {
			if(clusters.get(i) != null) {
			System.out.println(i+"     :     "+clusters.get(i).size()+"     "+ ((clusters.get(i).size()/(double)db.size())*100));
			}
			else {
				System.out.println(i+"     :     "+"0"+"     "+ "0");
			}
		}
		for(int i=0;i<clusters.size();i++) {
			System.out.println(i+"	  : 		"+ clusters.get(i));
		}
		double sse = calcSSE(cc, clusters);
		System.out.println("The Sum squared error for this clustering is:");
		System.out.println(sse);
		System.out.println((endtime-starttime));
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	// A function to store the data instances from an .arff file. 
	public static void saveInstances(BufferedReader br){
		try {
			db.clear();
			String st;
            while((st = br.readLine())!=null) {
                if(!st.startsWith("@attribute") && !st.startsWith("@relation") && !st.startsWith("@data") && !st.isEmpty()) {
                   List<Double> instance = new ArrayList<>();
                	   String[] sti = st.split(",");
                   for(int i=0;i<sti.length;i++) {
                	   	instance.add(Double.parseDouble(sti[i]));
                   }
                   db.add(instance);
                }
            }
        }
        catch(IOException e) {

        }
	
	}
	// A function to compute the initial centroids. We want the centroids to be with in the range of instances and are generated at random. 
	public static List<List<Double>> initialCentroids(int k){
		List<List<Double>> ic = new ArrayList<>();
		List<List<Double>> transposed = transpose(db);
		List<List<Double>> minmax = minsMaxs(transposed);
		Random generator = new Random(10);
 		for(int i=0;i<k;i++) {
			List<Double> kpt = new ArrayList<>();
			for(int j=0;j<db.get(0).size();j++) {
				kpt.add(minmax.get(j).get(0)+(generator.nextDouble()*(minmax.get(j).get(1) - minmax.get(j).get(0))));
			}
			ic.add(kpt);
		}
		return ic;
	}
	// A function to normalize the data.
	public static List<List<Double>> normalizeData(){
		List<List<Double>> dbnew = new ArrayList<>();
		List<List<Double>> transposed = transpose(db);
		List<List<Double>> minmaxs = minsMaxs(transposed);
		for(int i=0;i<db.size();i++) {
			List<Double> dbrow = new ArrayList<>();
			for(int j=0;j<db.get(i).size();j++) {
				dbrow.add((db.get(i).get(j) - minmaxs.get(j).get(0))/(minmaxs.get(j).get(1) - minmaxs.get(j).get(0)));
			}
			dbnew.add(dbrow);
		}
		return dbnew;
	}
	// A function to find the minimums and maximums for each attribute
	public static List<List<Double>> minsMaxs(List<List<Double>> matrixIn){
		 List<List<Double>> matrixOut = new ArrayList<List<Double>>();
		 double min=0;
		 double max=0;
		 for(int i =0;i<matrixIn.size();i++) {
			 List<Double> minmax = new ArrayList<>();
			min = Collections.min(matrixIn.get(i));
			max = Collections.max(matrixIn.get(i));
			minmax.add(min);
			minmax.add(max);
			matrixOut.add(minmax);
		 }
		 return matrixOut;
	}
	// A function used to transpose the data. It is used for obtaining all the values corresponding to an attribute
	// in a single row.
	public static List<List<Double>> transpose(List<List<Double>> matrixIn) {
	    List<List<Double>> matrixOut = new ArrayList<List<Double>>();
	    if (!matrixIn.isEmpty()) {
	        int noOfElementsInList = matrixIn.get(0).size();
	        for (int i = 0; i < noOfElementsInList; i++) {
	            List<Double> col = new ArrayList<Double>();
	            for (List<Double> row : matrixIn) {
	                col.add(row.get(i));
	            }
	            matrixOut.add(col);
	        }
	    }

	    return matrixOut;
	}
	// A function used to calculate the Eucledian distance between each centroid and every point in the dataset.
	public static List<List<Double>> calcDist( List<List<Double>> cc) {
		List<List<Double>> dist = new ArrayList<>();
		for(int i=0; i<db.size();i++) {
			List<Double> distc = new ArrayList<>();
			for(int j=0;j<cc.size();j++) {
				double distance = 0;
				for(int k=0;k<db.get(i).size();k++) {
					distance += Math.pow((cc.get(j).get(k) - db.get(i).get(k)),2);
				}
				distc.add(Math.sqrt(distance));
			}
			dist.add(distc);
		}
		return dist;
	}
	// A function used to assign a point to a cluster based on its distance to every cluster's centroid. 
	public static List<Integer> assignCluster(List<List<Double>> dist){
		List<Integer> ptassg = new ArrayList<>();
		for(int i=0;i<dist.size();i++) {
			ptassg.add(minIndex(dist.get(i)));
		}
		return ptassg;
	}
	// A function used to recompute the centroids after re-assignment of points to a cluster.
	public static List<List<Double>> recomputeCentroids(Map<Integer,List<Integer>> clusterassg,List<List<Double>> cc) {
		List<List<Double>> nc = new ArrayList<>();
		for(int i=0;i<clusterassg.size();i++) {
			if(clusterassg.get(i) != null) {
			List<Double> val = new ArrayList<Double>();
			for(int p=0;p<db.get(0).size();p++) {
				val.add(p, (double) 0);
			}
			for(int j=0;j<clusterassg.get(i).size();j++) {
				for(int k=0;k<db.size();k++) {
					if(k == clusterassg.get(i).get(j)) {
						List<Double> cval = db.get(k);
						for(int ind = 0;ind<cval.size();ind++) {
							val.set(ind,val.get(ind)+cval.get(ind));
						}
					}
				}
			}
			List<Double> newval = new ArrayList<>();
			for(int p=0;p<db.get(0).size();p++) {
				newval.add(val.get(p)/clusterassg.get(i).size());
			}
			nc.add(newval);
		}
			else {
				nc.add(cc.get(i));
			}
		}
		return nc;
	}
	// A function used to calculate the epsilon which is the SSD (sum of squared distance) between centroids and is
	// used as stopping criterion.
	public static Double calcEpsilon(List<List<Double>> cc,List<List<Double>> nc) {
		Double ep = (double) 0;
		for(int i=0;i<cc.size();i++) {
			double singledist = 0;
			for(int j=0;j<cc.get(i).size();j++) {
				singledist+= Math.pow((cc.get(i).get(j) - nc.get(i).get(j)),2);
			}
			ep+=singledist;
		}
		return ep;
	}
	// A function used to calculate the goodness of clustering based on the Sum squared errors.
	public static double calcSSE(List<List<Double>> cc,Map<Integer,List<Integer>> clusterpts ) {
		double sse =0;
		for(int i=0;i<clusterpts.size();i++) {
			double clustersum = 0;
			if(clusterpts.get(i) !=null) {
			for(int j=0;j<clusterpts.get(i).size();j++) {
				double attrsum = 0;
				for(int k=0;k<cc.get(i).size();k++) {
					attrsum+= Math.pow(cc.get(i).get(k) - db.get(clusterpts.get(i).get(j)).get(k),2);
				}
				clustersum+=attrsum;
			}
			}
			else {
				clustersum+=0;
			}
			sse+=clustersum;
		}
		return sse;
	}
	// A helper function which is used to convert point assignments to a cluster, to a grouping of points to each cluster.
	// It can thought as a grouping by clusters.
	public static Map<Integer,List<Integer>> ptsToClusters(List<Integer> assg, int k){
		Map<Integer,List<Integer>> clusterassg = new HashMap<>();
		for(int i=0;i<k;i++) {
			clusterassg.put(i, null);
		}
		for(int i = 0; i<assg.size();i++) {
			List<Integer> li = clusterassg.get(assg.get(i));
			if(li == null) {
				li = new ArrayList<>();
				clusterassg.put(assg.get(i), li);
			}
			li.add(i);
		}
		return clusterassg;
	}
	// A helper function used to get the minimum index (point number) from a list
	// It is used to determine the closest cluster for a point.
	public static int minIndex(List<Double> pd) {
		return pd.indexOf(Collections.min(pd));
	}
}
