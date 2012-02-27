import java.net.URL;
import java.net.URLConnection;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Given a url and a directory, downloads all the images of the URL's site to that directory
 * @author Andrew Knapp
 * @version 1.0
 */
public class imageCollector 
{
	private String siteURL;
	private String downloadDirectory;
	private Scanner webPage;
	private Set<String> imageDirectories;
	
	/**
	 * Creates an imageCollector object given a URL and a Directory.  If no directory is given
	 * it will save to the directory of this program.
	 * @param url
	 * @param directory
	 */
	public imageCollector(String url, String directory)
	{
		siteURL = url;
		downloadDirectory = directory;
		imageDirectories = new HashSet<String>();
	}
	
	/**
	 * Given a website it gets the raw page and stores it in a Scanner object.
	 * @param webSite
	 */
	public void fetchWebsite(String webSite)
	{
		try 
		{
			URLConnection siteConnection = new URL(webSite).openConnection();
			webPage = new Scanner(siteConnection.getInputStream());
		} catch (IOException e) 
		{
			System.out.println(webSite + " is not a valid URL");
			e.printStackTrace();
		}	
	}
	
	/**
	 * Identifies the image links from a given web page.
	 * @return a set of found links.
	 */
	private Set<String> identifyContent()
	{
		this.imageDirectories= new HashSet<String>();	
		while (webPage.hasNextLine())
		{
			String line = webPage.nextLine();
				
			if (line.contains("img src"))
			{	
				Scanner lineScan = new Scanner(line);
				while(lineScan.hasNext())
				{
					String linkCheck = lineScan.next();
					if(linkCheck.contains("src="))
					{
						String imageLink = linkCheck.substring(6, linkCheck.lastIndexOf('"'));
						if (!imageLink.contains(siteURL))
						{
							String baseURL = siteURL.substring(0, siteURL.lastIndexOf("com/") + 4);
							String fullLink = baseURL.concat(imageLink);
							imageDirectories.add(fullLink);
						} else 
						{
							imageDirectories.add(imageLink);
						}
					}
				}
			}
		}
		return imageDirectories;
	}
	
	/**
	 * Downloads images from a collection of image links found on a website.
	 */
	public void downloadImages()
	{
		Set<String> imageDirectories = identifyContent();
		for(String imageURL : imageDirectories)
		{
			String fileName = imageURL.substring(imageURL.lastIndexOf("/") + 1);
			String fileLocation = downloadDirectory.concat(fileName);
			
			try
			{
				InputStream siteImageStream = new URL(imageURL).openStream();
				OutputStream fileOutputStream = new FileOutputStream(fileLocation);
				byte[] b = new byte[2048];
				int byteLength;
				
				while ((byteLength = siteImageStream.read(b)) != -1)
				{
					fileOutputStream.write(b, 0, byteLength);
				}
				
				siteImageStream.close();
				fileOutputStream.close();
				
			} catch (IOException e)
			{
				System.out.println(imageURL + " is not a valid URL");
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * Prints all the image URLs found on a given page.
	 */
	public void printImageURLs(){
		for (String link: this.imageDirectories){
			System.out.println(link);
		}
	}
}
