import java.util.*;
import java.net.*;
import java.io.*;

/** This program was written to count and analyze all the websites under
 * a specific domain.
 * @author Andrew Knapp
 * @version 0.1
 */
public class CrawlerMain{
	public static final String TOPLEVELWEBSITE = "http://www.httprecipes.com/1/6/subpage.php"; //simple site
//	public static final String TOPLEVELWEBSITE = "http://0entrypoint.blogspot.com";	//complex site
	
	public static void main(String[] args){
		Set<String> links = new HashSet();
		long start = System.currentTimeMillis();
		
		fetchWebsite(TOPLEVELWEBSITE, links);
		printSet(links);
		
		long end = System.currentTimeMillis();
		System.out.println("Time to run: " + (end-start) + " ms");
		
	}
	
	public static void fetchWebsite(String topDomain, Set<String> links){
		try {
	         URLConnection connection = new URL(topDomain).openConnection();
	         Scanner site = new Scanner(connection.getInputStream());
	         while (site.hasNextLine()){
	        	 String line = site.nextLine();
//        		 System.out.println(line);	//debug	 
	        	 if (line.contains("href")){
	        		 Scanner lineScan = new Scanner(line);
	        		 while(lineScan.hasNext()){
	        			 String linkCheck = lineScan.next();
	        			 if (linkCheck.contains("href")){
	        				 String foundLink = linkCheck.substring(6, linkCheck.lastIndexOf('"'));
	        				 if (!foundLink.contains(topDomain)){
	        					 String ladd = topDomain.substring(0, topDomain.lastIndexOf("/") + 1);
	        					 ladd = ladd.concat(foundLink);
	        					 links.add(ladd);
	        				 } else {
	        					 links.add(foundLink);
	        				 }
	        			 }
	        		 }
	        	 }
	         }
	       } 
	       catch (IOException e) {
	         e.printStackTrace();
	         links.remove(topDomain);
	       }
	}
	
	public static void printSet(Set<String> link){
		for (String s: link){
			System.out.println(s);
		}
	}
}