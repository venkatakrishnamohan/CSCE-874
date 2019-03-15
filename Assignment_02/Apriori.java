import java.io.*;
import java.util.*;
import java.lang.*;
public class Apriori {
    public static Map<Integer,String> d =  new HashMap<Integer,String>();
    public static List<String> freqs = new ArrayList<>();
    public static Map<String, Integer> cmap = new HashMap<>();
    public static Set<String> rulesets = new HashSet<>(); 
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
        	String location; 
        	location = args[0];
        	BufferedReader br =new BufferedReader(new FileReader(location));
    		BufferedReader br1 = new BufferedReader(new FileReader(location));
    		BufferedReader br2 = new BufferedReader(new FileReader(location));
    		PrintWriter output = new PrintWriter("output.txt");
            Double minsup1 = Double.parseDouble(args[1]);
            double minconf = Double.parseDouble(args[2]);
        
            List<String> instances = saveInstances(br);
            List<String> attributes = saveAttributes(br1);
            d = AttributetoInd(attributes);
            String[] cv = classValues(br2);
            long minsup = Math.round(minsup1*instances.size());
            //System.out.println(minsup);
            List<String> newinst = InstancetoInd(instances,cv);
            //System.out.println(newinst);
            Map<String,Integer> c1 = supportcountone(newinst);
            saveCandidateItems(c1);
            List<String> l1 = getFrequentItemSets(c1, minsup);
            List<String> li = l1;
            System.out.println("Frequent item sets size:1");
            System.out.println(li.size());
            //System.out.println(li);
            //System.out.println("--------------------------------------------------------------------------------------------------");
           // saveFrequentItemSets(l1);
            int ind=1;
            while(!li.isEmpty()) {
                ind++;
                Map<String,Integer> am = supportcount(li, newinst);
                //System.out.println("No.of Candidates:"+am.size());
                //System.out.println("Candidates size: "+ind);
                //System.out.println(am);
                saveCandidateItems(am);
                li = getFrequentItemSets(am, minsup);
                System.out.println("Frequent Items sets size: "+ind);
                System.out.println("No.of Frequent Itemsets:"+li.size());
                saveFrequentItemSets(li);
                generateRules(li,minconf,output);
                //System.out.println("--------------------------------------------------------------------------------------------");
            }
            System.out.println("Total No.of rules:" + rulesets.size());
            output.println("Total No.of rules:" + rulesets.size());
            output.close();
        }
        catch(IOException e) {

        }
    }
    public static void generateRules(List<String> li,double minconf, PrintWriter output) {
    		///////////// association rules
        for(int a = 0; a<li.size(); a++){//go through each frequent itemset
            String[] val = li.get(a).split(","); //used for generating subsets
            List<String> subset = new ArrayList<String>();
            for(long i = 1; i < (1<<val.length)-1; i++){//generate subsets of frequent itemset (uses bitshifting ex.: 001, 010, 100, 101, 110, etc.)
                String ans = "";
            		for(int j = 0; j < val.length; j++){
                    if(((i>>j) & 1) == 1) { // check if bit j is 1
                        ans = ans+val[j]+","; //itemset #a and string j
                    }
                }
            		subset.add(ans.substring(0, ans.length()-1));
            }
                List<String> test = new ArrayList<String>(); //used for taking difference of sets I-s
                test.add(li.get(a)); //set I
           for(int i=0;i< subset.size();i++) {
        	   String stri = "";
                //IF SUPP(I)/SUPP(S) >= MINCONF
                //I = li.get(a)
                //s = subset
                double confidence = 0.0;
                //if set.get.contains comma
                //multi item(compare each item using count
                //else, single item
                String[] aa;
                int sup = 0;
                if(subset.get(i).contains(",")) {
                  aa = subset.get(i).split(",");
                }
                else {
                		aa = new String[] {subset.get(i)};
                }
                    for(Map.Entry<String,Integer> k: cmap.entrySet()) {
                    		String b[];
                    if(k.getKey().contains(",")) {
                        b = k.getKey().split(",");
                    }
                    else {
                    	b = new String[] {k.getKey()};
                    }
                    if(aa.length == b.length){
                            int counter = 0;
                            for(int x =0; x<aa.length;x++){
                                for(int y =0; y<b.length;y++) {
                                    if (aa[x].equals(b[y])) {
                                        counter++;
                                        break;
                                    }
                                }
                            }
                            if(counter == aa.length) {
                                sup = k.getValue();
                                confidence = cmap.get(li.get(a)) * 1.0 / sup;
                                break;
                            }
                    }
                    }
                if (confidence >= minconf) {
                    //generate assoc. rule
                    List<String> c = new ArrayList<String>();
                    c.addAll(test); //Set I
                    String[] w = c.get(0).split(",");
                    List<String> ab = new ArrayList<String>(Arrays.asList(w));
                   // c.removeAll(subset);// I - s
                    String[] ww = subset.get(i).split(",");
                    List<String> aabb = new ArrayList<String>(Arrays.asList(ww));
                    ab.removeAll(aabb);
                    stri = stri+subset.get(i) + " -> " + String.join(",", ab);
                    if(!rulesets.contains(stri)) {
                    rulesets.add(stri);
                    }
                    output.println(stri + " conf = " + confidence + "  sup= " + sup);
                }
            }
        }
    }
    public static void saveCandidateItems(Map<String, Integer> a) {
        for(Map.Entry<String,Integer> entry: a.entrySet()) {
            cmap.put(entry.getKey(), entry.getValue());
        }
    }
    public static void saveFrequentItemSets(List<String> a){
        for(int i=0;i<a.size();i++) {
            freqs.add(a.get(i));
        }
    }
    public static List<String> getFrequentItemSets(Map<String,Integer> cs,long minsup){
        List<String> fs = new ArrayList<>();
        for(Map.Entry<String,Integer> entry: cs.entrySet()) {
            if(entry.getValue() >= minsup) {
                fs.add(entry.getKey());
            }
        }
        return fs;
    }
    public static Map<String,Integer> getoneFrequentItemSets(Map<String,Integer> cs,Integer minsup){
        Map<String,Integer> fs = new HashMap<>();
        for(Map.Entry<String,Integer> entry: cs.entrySet()) {
            if(entry.getValue() >= minsup) {
                fs.put(entry.getKey(), entry.getValue());
            }
        }
        return fs;
    }
    public static  List<String> saveAttributes(BufferedReader br) {
        List<String> attributes = new ArrayList<String>();
        try {
            for(String st = br.readLine();st != null; st = br.readLine()) {
                if(st.startsWith("@attribute")) {
                    attributes.add(st.split(" ")[1]);
                }
            }
        }
        catch(IOException e) {

        }
        return attributes;
    }
    public static  List<String> saveInstances(BufferedReader br2) {
        String st;
        List<String> Instances = new ArrayList<String>();
        try {
            while((st = br2.readLine())!=null) {
                if(!st.startsWith("@attribute") && !st.startsWith("@relation") && !st.startsWith("@data") && !st.isEmpty()) {
                    Instances.add(st);
                }
            }
        }
        catch(IOException e) {

        }
        return Instances;
    }
    public static List<String> InstancetoInd(List<String> instances,String[] cv) {
        List<String> newinst = new ArrayList<String>();
        for(int i=0;i<instances.size();i++) {
            String a[] = instances.get(i).split(",");
            String s = "";
            for(int j=0;j<a.length;j++) {
                if(a[j].charAt(0) == 'y') {
                    s+=d.get(j)+",";
                }
                else if(a[j].charAt(0) == 'n'){
                    s+=d.get(j+a.length)+",";
                }
                else if(cv!=null){
                if(a[j].equals( cv[0])) {
                    s+=d.get(j);
                }
                else if(a[j].equals(cv[1])) {
                    s+=d.get(j+a.length);
                }
                }
            }
            newinst.add(s);
        }
        return newinst;
    }
    public static Map<Integer,String> AttributetoInd(List<String> attributes) {
        for(int i=0;i<attributes.size()*2;i++) {
            if(i<attributes.size()) {
                d.put(i, attributes.get(i));
            }
            else {
                d.put(i, attributes.get(i-attributes.size())+"-n");
            }
        }
        return d;
    }
    public static String[] classValues(BufferedReader br){
        String[] cv = null;
        try {
            for(String st = br.readLine();st != null; st = br.readLine()) {
                if(st.contains("Class")) {
                    String a[] = st.split(" ");
                    String labels = a[a.length-1];
                    labels = labels.substring(1, labels.length()-1);
                    cv = labels.split(",");
                }
            }
        }
        catch(IOException e) {

        }
        return cv;
    }
    public static Map<String,Integer> supportcountone(List<String> newinst){
        Map<String,Integer> l1 = new HashMap<>();
        for(int i=0;i<d.size();i++) {
            l1.put(d.get(i),0);
        }
        for(int i=0;i<newinst.size();i++) {
            String a[] = newinst.get(i).split(",");
            for(int j=0;j<a.length;j++) {
                l1.put(a[j], l1.get(a[j])+1);
            }
        }
        return l1;
    }
    public static String getValue(Integer i) {
        return d.get(i);
    }
    public static void display(Map<Integer,String>d) {
        System.out.println(d.get(0));
    }
    /*Generate candidates of size k+1 from k-frequent item sets*/
    public static Map<String,Integer> supportcount(List<String> l,List<String> db){
        Set<String> ans1 = new HashSet<>();
        Map<String,Integer> l1 = new HashMap<>();
        for(int i=0;i<l.size();i++) {
            String[] a;
            if(l.get(i).contains(",")) {
                a = l.get(i).split(",");
            }
            else {
                a = new String[] {l.get(i)};
            }
            for(int j=i+1;j<l.size();j++) {
                String b[];
                if(l.get(i).contains(",")) {
                    b = l.get(j).split(",");
                }
                else {
                    b = new String[] {l.get(j)};
                }
                int c=0;
                for(int k=0;k<a.length-1;k++) {
                    if(a[k].equals(b[k])) {
                        c++;
                    }
                }
                if(c==a.length-1)
                {
                    ans1.add(l.get(i)+","+b[b.length-1]);
                }
            }
        }
        List<String> ans = new ArrayList<>(ans1);
        //System.out.println("No.of candidates generated before pruning:"+ ans.size());
        //System.out.println(ans);
			/*get subsets of size k-1 from size k*/
        Set<String> subsets = new HashSet<>();
        for(int i=0;i<ans.size();i++) {
            String[] a = ans.get(i).split(",");
            for(int j=0;j<a.length;j++) {
                String b = "";
                for(int k=0;k<a.length;k++) {
                    if(k!=j) {
                        b+=a[k]+",";
                    }
                }
                if(!subsets.contains(b))
                    subsets.add(b.substring(0, b.length()-1));
            }
        }
        List<String> sub = new ArrayList<>(subsets);
        //System.out.println("No of subsets generated:"+sub.size());
        //System.out.println(sub);
        //System.out.println(l);
		/*check if the subsets of size k-1 are in the previous frequent itemset*/
        for(int i=0;i<sub.size();i++) {
            int j=0;
            for(j=0;j<l.size();j++) {
                String [] a;
                if(l.get(j).contains(",")) {
                    a= l.get(j).split(",");
                }
                else {
                    a = new String[] {l.get(j)};
                }
                int count=0;
                for(int k=0;k<a.length;k++) {
                    if((sub.get(i)+",").contains(a[k]+",")) {
                        count++;
                    }
                }
                if(count == a.length) {
                    break;
                }
            }
            if(j==l.size()) {
                for(int k=0;k<ans.size();k++) {
                    if((ans.get(k)+",").contains(sub.get(i)+",")) {
                        ans.remove(k);
                        break;
                    }
                }
            }
        }
        //System.out.println("Size of candidates after pruning:"+ans.size());
		/*count the support for the final cand sets*/
        for(int i=0;i<ans.size();i++) {
            l1.put(ans.get(i), 0);
        }
        for(int k=0;k<db.size();k++) {
            for(int i=0;i<ans.size();i++) {
                int count=0;
                String[] a;
                if(ans.get(i).contains(",")) {
                    a = ans.get(i).split(",");
                }
                else {
                    a = new String[] {db.get(i)};
                }
                for(int j=0;j<a.length;j++) {
                    String st = "";
					/*if(a[j].contains("Class")) {
						st = a[j];
					}
					else {
						st = a[j]+",";
					}*/
                    st = a[j]+",";
                    if((db.get(k)+",").contains(st)) {
                        count++;
                    }
                }
                if(count == a.length) {
                    l1.put(ans.get(i), l1.get(ans.get(i))+1);
                }
            }
        }
        return l1;
    }

}
