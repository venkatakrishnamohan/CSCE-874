package aggregate;

/**
 * EventID
Day
Month
Year
CountryCode {IN, PK, BG}
SourceURL
EventCategory {Appeal, Demand, Threaten, Protest, Assault, Coerce, Fight, Engage in Unconventional Mass Violence (UMV)}
Latitude
Longitude
Count of Events

 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Aggregate {
    static ArrayList<String[]> cList = new ArrayList<String[]>();
    static BufferedReader br = null;
    static BufferedReader br1 = null;
    static FileWriter fw = null;
    static BufferedWriter bw = null;
    static PrintWriter out = null;
    
    public static String aggregateTXT(String readDir, String writeDir) throws FileNotFoundException, IOException{
        String aggregateFileDir = null;
        try{
            String sCurrentLine;
            String Line;
//            System.out.println(readDir);
            File file1 = new File(readDir);
            br = new BufferedReader(new FileReader(file1));
            String name = file1.getName();
            int filenameLength = name.length();
            if (".txt".equals(name.substring(filenameLength-4))){
                aggregateFileDir = writeDir + name;
                fw = new FileWriter(aggregateFileDir, true);
                bw = new BufferedWriter(fw);
                out = new PrintWriter(bw);			    
//                System.out.println("Aggregate file location: "+ aggregateFileDir);			  
                cList.clear();
                while ((sCurrentLine = br.readLine()) != null) {
                    String[] attr = sCurrentLine.split(",");
                    String lat = attr[9];
                    String lon = attr[10];

                    int countAppeal = 0;
                    int demand = 0;
                    int threaten = 0;
                    int protest = 0;
                    int coerce = 0;
                    int assault = 0;
                    int fight = 0;
                    int engage = 0;

                    String[] coord1 = {lat, lon};
                    if(!containsCoords(coord1)){
//                                  System.out.println("HERE! 1 pointer: " + lat + "\t" + lon);
                        cList.add(coord1);
                        br1 = new BufferedReader(new FileReader(file1));
                        while ((Line = br1.readLine()) != null) {
                            String[] attr1 = Line.split("\t");
                            String lat1 = attr1[9];
                            String lon1 = attr1[10];
                            if(lat1.equals(lat) && lon1.equals(lon)){
                                //System.out.println("HERE! 2 pointer" + lat1 + "\t" + lon1);
                                //020, 021, 0211, 0212, 0213, 0214, 022, 023, 0231, 0232, 0233, 0234, 024, 0241, 0242, 0243, 0244, 025, 0251, 0252, 0253, 0254, 0255, 0256, 026, 027, 028                                            
                                if(attr[4].equals("020") || attr[4].equals("021") ||attr[4].equals("0211")||attr[4].equals("0212")||
                                        attr[4].equals("0213")|| attr[4].equals("0214")||attr[4].equals("022")||
                                        attr[4].equals("023")||attr[4].equals("0231")||attr[4].equals("0232")||attr[4].equals("0233")||
                                        attr[4].equals("0234")||attr[4].equals("024")||attr[4].equals("0241")||attr[4].equals("0242")||
                                        attr[4].equals("0243")||attr[4].equals("0244")||attr[4].equals("025")||attr[4].equals("0251")||
                                        attr[4].equals("0252")||attr[4].equals("0253")||attr[4].equals("0254")||attr[4].equals("0255")||
                                        attr[4].equals("0256")||attr[4].equals("026")||attr[4].equals("027")||attr[4].equals("028")){
                                    countAppeal++;
                                }
                                //System.out.println("HERE! 3 pointer" );
                                else if(attr[4].equals("100") ||attr[4].equals("101") ||attr[4].equals("1011") ||attr[4].equals("1012") ||attr[4].equals("1013") ||
                                        attr[4].equals("1014") ||attr[4].equals("102") ||attr[4].equals("103") ||attr[4].equals("1031") ||attr[4].equals("1032") ||
                                        attr[4].equals("1033") ||attr[4].equals("1034") ||attr[4].equals("104") ||attr[4].equals("1041") ||attr[4].equals("1042") ||
                                        attr[4].equals("1043") ||attr[4].equals("1044") ||attr[4].equals("105") ||attr[4].equals("1051") ||
                                        attr[4].equals("1052") ||attr[4].equals("1052") ||attr[4].equals("1053") ||attr[4].equals("1054")||
                                        attr[4].equals("1055") ||attr[4].equals("1056") ||attr[4].equals("106") ||attr[4].equals("107") ||attr[4].equals("108") ){
                                    demand++;
                                }
                                else if(attr[4].equals("130") ||attr[4].equals("131") ||attr[4].equals("1311") ||attr[4].equals("1312") ||attr[4].equals("1313") ||
                                        attr[4].equals("132") ||attr[4].equals("1321") ||attr[4].equals("1322") ||attr[4].equals("1323") ||attr[4].equals("1324") ||
                                        attr[4].equals("133") ||attr[4].equals("134") ||attr[4].equals("135") ||attr[4].equals("136") ||attr[4].equals("137") ||
                                        attr[4].equals("138") ||attr[4].equals("1381") ||attr[4].equals("1382") ||attr[4].equals("1383") ||
                                        attr[4].equals("1384") ||attr[4].equals("1385") ||attr[4].equals("139")){
                                    threaten++;
                                }
                                else if(attr[4].equals("140") ||attr[4].equals("141") ||attr[4].equals("1411") ||attr[4].equals("1412") ||attr[4].equals("1413") ||
                                        attr[4].equals("1414") ||attr[4].equals("142") ||attr[4].equals("1421") ||attr[4].equals("1422") ||attr[4].equals("1423") ||
                                        attr[4].equals("1424") ||attr[4].equals("143") ||attr[4].equals("1431") ||attr[4].equals("1432") ||attr[4].equals("1433") ||
                                        attr[4].equals("1434") ||attr[4].equals("144") ||attr[4].equals("1441") ||attr[4].equals("1442") ||
                                        attr[4].equals("1443") ||attr[4].equals("1444") ||attr[4].equals("145") ||attr[4].equals("1451")||
                                        attr[4].equals("1452") ||attr[4].equals("1453") ||attr[4].equals("1454") ){
                                    protest++;
                                }
                                else if(attr[4].equals("170") ||attr[4].equals("171") ||attr[4].equals("1711") ||attr[4].equals("1712")||
                                        attr[4].equals("172") ||attr[4].equals("1721") ||attr[4].equals("1722") ||attr[4].equals("1723") ||
                                        attr[4].equals("1724") ||attr[4].equals("173") ||attr[4].equals("174") ||attr[4].equals("175") ||
                                        attr[4].equals("176") ){
                                    //missing 174
                                    coerce++;
                                }
                                else if(attr[4].equals("180") ||attr[4].equals("181") ||attr[4].equals("182") ||attr[4].equals("1821")||
                                        attr[4].equals("1822") ||attr[4].equals("1823") ||attr[4].equals("183") ||attr[4].equals("1831") ||
                                        attr[4].equals("1832") ||attr[4].equals("1833") ||attr[4].equals("1834") ||attr[4].equals("184") ||
                                        attr[4].equals("185") ||attr[4].equals("186")){
                                    assault++;
                                }
                                else if(attr[4].equals("190") ||attr[4].equals("191") ||attr[4].equals("192") ||attr[4].equals("193")||
                                        attr[4].equals("194") ||attr[4].equals("195") ||attr[4].equals("1951") ||attr[4].equals("1952") ||
                                        attr[4].equals("196") ){
                                    fight++;
                                }
                                else if(attr[4].equals("200") ||attr[4].equals("201") ||attr[4].equals("202") ||attr[4].equals("203")||
                                        attr[4].equals("204") ||attr[4].equals("2041") ||attr[4].equals("2042") ){
                                    engage++;
                                }
                            } // if
                        } // while
                        if(countAppeal > 0)
                            out.write(attr[0] + "\t" + attr[1] + "\t" +attr[2] + "\t" +attr[3] + "\t" + attr[6] + "\t" + attr[11]  + "\t" + "Appeal" + "\t" + lat + " \t" + lon + "\t" + countAppeal + "\n");
                        if(demand > 0)
                            out.write(attr[0] + "\t" + attr[1] + "\t" +attr[2] + "\t" +attr[3] + "\t" + attr[6] + "\t"+ attr[11]  + "\t" + "Demand" + "\t" + lat + " \t" + lon + "\t" + demand + "\n");
                        if(threaten > 0)
                            out.write(attr[0] + "\t" + attr[1] + "\t" +attr[2] + "\t" +attr[3] + "\t" + attr[6] + "\t"+ attr[11]  + "\t" + "Threaten" + "\t" + lat + " \t" + lon + "\t" + threaten + "\n");
                        if(protest > 0)
                            out.write(attr[0] + "\t" + attr[1] + "\t" +attr[2] + "\t" +attr[3] + "\t" + attr[6] + "\t"+ attr[11]  + "\t" + "Protest" + "\t" + lat + " \t" + lon + "\t" + protest + "\n");
                        if(coerce > 0)
                            out.write(attr[0] + "\t" + attr[1] + "\t" +attr[2] + "\t" +attr[3] + "\t" + attr[6] + "\t"+ attr[11]  + "\t" + "Coerce" + "\t" + lat + " \t" + lon + "\t" + coerce + "\n");
                        if(assault > 0)
                            out.write(attr[0] + "\t" + attr[1] + "\t" +attr[2] + "\t" +attr[3] + "\t" + attr[6] + "\t"+ attr[11]  + "\t" + "Assault" + "\t" + lat + " \t" + lon + "\t" + assault + "\n");
                        if(fight > 0)
                            out.write(attr[0] + "\t" + attr[1] + "\t" +attr[2] + "\t" +attr[3] + "\t" + attr[6] + "\t"+ attr[11]  + "\t" + "Fight" + "\t" + lat + " \t" + lon + "\t" + fight + "\n");
                        if(engage > 0)
                            out.write(attr[0] + "\t" + attr[1] + "\t" +attr[2] + "\t" +attr[3] + "\t" + attr[6] + "\t"+ attr[11]  + "\t" + "Engage in UMV" + "\t" + lat + " \t" + lon + "\t" + engage + "\n");
                    } //if
                }//main while
                out.close();
                br.close();
            } // if txt file
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
                if(bw != null) bw.close();
//                out.close();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return aggregateFileDir;
    }
    
    public static boolean containsCoords(String[] cds){
        for(int i = 0; i < cList.size(); i++){
            String[] cd = cList.get(i);
            if(cd[0].equals(cds[0]) && cd[1].equals(cds[1]))
                return true;
        }
        return false;
    }
    public static void main(String args[]) {
    		aggregateTXT("/Users/venkatakrishnamohansunkara/Desktop/test/testall", "/Users/venkatakrishnamohansunkara/Desktop/test/new");
    }
}
