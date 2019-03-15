import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

//Include the Jsoup Jar files.
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler_IndianExpress {
	 public static void main(String[] args) throws IOException {
		 
	        // Construct arrays with months and days
	        int[] months = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
	        int[] days = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	        int year = 2014;
	 		
	        while (year <= 2017) {
	            int monthIndx = 0;
	            int dayIndx = 0;
	 
	            while (months[monthIndx] <= 12) {
	                int day = 1;
	 
	                while (day <= days[dayIndx]) {
	                    final ArrayList<String> fullLinks = new ArrayList<String>();
	                    String date_dir = Integer.toString(months[monthIndx])+"-"+Integer.toString(day)+"-"+Integer.toString(year);
	                    // Pull HTML from archive page, Create doc from HTMl
	                    String html = "<html><head></head>" + "<body><p>Parsed HTML into a doc." + "</p></body></html>";
	                    Document doc = Jsoup.connect("http://archive.indianexpress.com/archive/news/" + day + "/"
	                            + months[monthIndx] + "/" + year + "/").timeout(100 * 10000).get();
	                    	System.out.println(doc.location());
	                    // Extract all links from the page, add them to arrayList
	                    Elements links = doc.select("a[href*=/news/]");
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
	 
	                            // Get Date with City
								
								Element result = doc.getElementsByClass("story-date").get(0);
								System.out.println(result);
	                            String date = result.text();
	                            String [] date1 = date.split(":");
	                            writer.append(date1[1] + "\n");
	                            writer.write("\r\n");
	                            
	                            // Get body
	                            Elements paragraphs = doc.select("p");
	                            for (Element p : paragraphs) {
	                                writer.write(p.text());
	                            }
	 
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

