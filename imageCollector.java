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
 * @author Andrew Knapp
 * Given a url and a directory, downloads all the images of the URL's site to that directory
 */
public class imageCollector 
{
	private String siteURL;
	private String downloadDirectory;
	private Scanner webPage;
	
	/**
	 * 
	 * @param url
	 * @param directory
	 */
	public imageCollector(String url, String directory)
	{
		url = siteURL;
		directory = downloadDirectory;
	}
	
	/**
	 * 
	 * @param webSite
	 * @return
	 */
	public void fetchWebsite(String webSite)
	{
		try 
		{
			URLConnection siteConnection = new URL(webSite).openConnection();
			webPage = new Scanner(siteConnection.getInputStream());
			
			System.out.println("Website " + webSite + " opened!"); //debug
		
		} catch (IOException e) 
		{
			System.out.println(webSite + " is not a valid URL");
			e.printStackTrace();
		}	
	}
	
	/**
	 * 
	 * @param webPage
	 * @return
	 */
	private Set<String> identifyContent()
	{
		Set<String> imageDirectories= new HashSet<String>();	
		while (webPage.hasNextLine())
		{
			String line = webPage.nextLine();
			System.out.println(line);
			if (line.contains("img src"))
			{
				Scanner lineScan = new Scanner(line);
				while(lineScan.hasNext())
				{
					String linkCheck = lineScan.next();
					
					if(linkCheck.contains("img src="))
					{
						String imageLink = linkCheck.substring(8, linkCheck.lastIndexOf('"'));
						
						System.out.println("image link: " + imageLink); //debug
						
						if (!imageLink.contains(siteURL))
						{
							String baseURL = siteURL.substring(0, siteURL.lastIndexOf("/") + 1);
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
	 * 
	 * @param imageLinks
	 */
	public void downloadImages()
	{
		Set<String> imageDirectories = identifyContent();
		for(String imageURL : imageDirectories)
		{
			System.out.println("imageURL: " + imageURL);  //debug
			
			String fileName = imageURL.substring(imageURL.lastIndexOf("/") + 1);
			downloadDirectory.concat(fileName);
			
			try
			{
				InputStream siteImageStream = new URL(imageURL).openStream();
				OutputStream fileOutputStream = new FileOutputStream(fileName);
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
}
