import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler_TimesofIndia {
	 public static void main(String[] args) throws IOException {
		 
	        // Construct arrays with months and days
	        int[] months = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
	        int[] days = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	        int year = 2017;
	        //This number is the first number for a year. Open the times of India Archive and look at 01-01-year acrhive,
	        //you can find a number of the url **number.cns. Insert that number in the index.
	        int index = 43100;
	        while (year <= 2017) {
	            int monthIndx = 11;
	            int dayIndx = 11;
	 
	            while (months[monthIndx] <= 12) {
	                int day = 31;
	 
	                while (day <= days[dayIndx]) {
	                    final ArrayList<String> fullLinks = new ArrayList<String>();
	                    String date_dir = Integer.toString(months[monthIndx])+"-"+Integer.toString(day)+"-"+Integer.toString(year);
	                    // Pull HTML from archive page, Create doc from HTMl
	                    String html = "<html><head></head>" + "<body><p>Parsed HTML into a doc." + "</p></body></html>";
	                    Document doc = Jsoup.connect("https://timesofindia.indiatimes.com/" + year + "/"
	                            + months[monthIndx] + "/" + day + "/"+"archivelist/"+"year-"+year+",month-"+months[monthIndx]+",starttime-"+index+".cms").timeout(100 * 10000).get();
	                    	System.out.println(doc.location());
	                    // Extract all links from the page, add them to arrayList
	                    Elements links = doc.select("a[href*=http://timesofindia.indiatimes.com//]");
	                    for (Element link : links) {
	                    	links.wrap("<div class=\"news_head\"><ul><li></ul></li></div>");
	                        fullLinks.add(link.absUrl("href"));
	                    }
	                    // Result arrayList contains all of the articles on the
	                    // page.
	                    // Now we send the loops to extract all of the data.
	                    // And save them to files.
	 
	                    for (int i = 000; i < fullLinks.size(); i += 1) {
	                        // Error handling for dead links / archives that don't
	                        // have files
	 
	                        try {
	 
	                            // Connect to the links on the page / create a file
	                            // and writer for each
	                        	System.out.println(fullLinks.get(i));
	                            doc = Jsoup.connect(fullLinks.get(i)).get();
	                            // Relocates files to new folder instead of pack
	                            		File file = new File(
	                            				"/Users/venkatakrishnamohansunkara/Desktop/news_articles/" + date_dir + "_article" + i + ".txt");
	                            FileWriter writer = new FileWriter(file);
	                          
	                            // Get title
	                            Element t = doc.select("h1").first();
								t.wrap("<h1></h1>");
								String title = t.text();
								writer.append(title);
								writer.write("\r\n");
								System.out.println(title);
	                            // Get Date with City
	                            writer.append(Integer.toString(year)+"-"+Integer.toString(months[monthIndx])+"-"+Integer.toString(day) + "\n");
	                            writer.write("\r\n");
	                            writer.write(doc.getElementsByClass("Normal").text());
	                            // Get body
	                            //Elements paragraphs = doc.select("p");
	                            //for (Element p : paragraphs) {
	                             //   writer.write(p.text());
	                            //}
	                            index++;
	                            writer.close();
	                        } catch (Exception E) {
	                        		E.printStackTrace();
	                        } finally {
	 
	                        }
	 
	                    }
	                    day++;
	 
	                }
	                monthIndx++;
	                dayIndx++;
	 
	            }
	 
	        }
	        year++;
	    }
	 
	}

