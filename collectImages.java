
public class collectImages {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String downloadSite = "http://www.httprecipes.com/1/6/image.php";
		String fileDirectory = "/Users/acknapp/Desktop/test/";
		imageCollector page = new imageCollector(downloadSite, fileDirectory);
		page.fetchWebsite(downloadSite);
		page.downloadImages();
	}

}
