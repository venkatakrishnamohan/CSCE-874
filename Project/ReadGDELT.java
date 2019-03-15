/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readgdelt;

//import dataloadermysql.DataLoaderMySQL;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;
/**
 *
 * @author sudbasnet
 */
public class ReadGDELT {
    public static String readGdeltCsv(String readDir, String writeDir) throws FileNotFoundException, IOException {
        String sourceUrl = null;
        String returnFilePath = null;
        BufferedReader br = null;				
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter out = null;
        //DateTimeFormatter ft = DateTimeFormat.forPattern("yyyyMMdd");
        try {
            String sCurrentLine;
            File file1 = new File(readDir);
            String name = file1.getName();
            int filenameLength = name.length();
            // only read .CSV file
            if (".CSV".equals(name.substring(filenameLength-4).toUpperCase())){
                br = new BufferedReader(new FileReader(file1));
                // create the output filename
                char[] chr = name.toCharArray();
                StringBuffer n = new StringBuffer();
                for(int c = 0; c < chr.length; c++){
                    if(chr[c] != '.'){
                        n.append(Character.toString(chr[c]));
                    } else
                        break;
                }
                // put the filename into the write directory
                returnFilePath = writeDir + n + ".xlsx";
                fw = new FileWriter(returnFilePath, true);
                bw = new BufferedWriter(fw);
                out = new PrintWriter(bw);
                
                System.out.println("Output directory: " + returnFilePath);
                
                while ((sCurrentLine = br.readLine()) != null) {
//                          String LineAll = sCurrentLine.replaceAll("\"\t",",");                                                        
//                          String LineAll2 = LineAll.replaceAll("\t\"",",");                                                   
//                          String[] attr = LineAll2.substring(2).split("\t");
                    String[] attr = sCurrentLine.replaceAll("\t\"",",").replaceAll("\"\t",",").replaceAll("\"",",").split("\t");
//                          System.out.println(attr[26]);
                    // choose only INDIA, PAKISTAN and BANGLADESH
                    if(attr[51].equals("IN") || attr[51].equals("PK") || attr[51].equals("BG")){
                        //if (DataLoaderMySQL.currentDay.isBefore(ft.parseDateTime("20130401"))){
                            // there is no sourceURL in data before 20130401
                           // sourceUrl = "no url";
                        //} else {
                         //   sourceUrl = attr[57];
                        //}
                            out.write(attr[0]  + "\t" + 
                                    attr[1].substring(6, 8)  + "\t" + 
                                    attr[1].substring(4, 6)  + "\t" + 
                                    attr[3]  + "\t" +
                                    attr[26] + "\t" +
                                    attr[34] + "\t" +
                                    attr[51] + "\t" + 
                                    attr[49] + "\t" + 
                                    attr[50] + "\t" +
                                    attr[53] + "\t" + 
                                    attr[54] + "\t" ); 
                                   
                    }
                } //end while
                out.close();
                
            }//end if CSV 
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
                if(bw != null) bw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return returnFilePath;
    }
    public static void deleteCSV(String csvDir){
        File f = new File(csvDir);
        f.delete();
    }
    public static void main(){
      ReadGDELT.readGdeltCsv("Users/venkatakrishnamohan/desktop/Data Mining/project/test","Users/venkatakrishnamohan/desktop/Data Mining/project/test_res");
    }
}
