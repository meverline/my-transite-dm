package me.transit.parser.crawller;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class OpenMobilityData {

	private final static String BASE_URL = "https://transitfeeds.com/";
	private Log log = LogFactory.getLog(getClass().getName());
	private WebClient client;
	private HtmlPage page;

	/**
	 * 
	 * @param url
	 */
	public OpenMobilityData(String url) {
		client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		try {
			page = client.getPage(url);
		} catch (Exception e) {
			log.error("URL: " + url + " " + e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 
	 * @param url
	 */
	public void gotoPage(String url) {
		try {
			page = client.getPage(url);
		} catch (Exception e) {
			log.error("URL: " + url + " " + e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getDownLoadHref() {
		@SuppressWarnings("unchecked")
		List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//a[text='Download']");

		return OpenMobilityData.BASE_URL + items.get(0).getAttribute("href");
	}

	/**
	 * 
	 * @return
	 */
	public String getGfsPage() {
		@SuppressWarnings("unchecked")
		List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//a[@class='list-group-item']");

		return OpenMobilityData.BASE_URL + items.get(0).getAttribute("href");
	}

	/**
	 * 
	 * @param url
	 */
	public String download(String url) {
		int ndx = url.lastIndexOf("/");
		StringBuffer fileName = new StringBuffer(System.getProperty("java.io.tmpdir"));
		fileName.append(url.substring(ndx + 1));

		BufferedInputStream inputStream = null;
		FileOutputStream fileOS = null;
		try {
			
			inputStream = new BufferedInputStream(new URL(url).openStream());
			fileOS = new FileOutputStream(fileName.toString());
			byte data[] = new byte[1024];
			int byteContent;
			while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
				fileOS.write(data, 0, byteContent);
			}
			
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
			fileName = new StringBuffer();
		} finally {
			try {
				if (fileOS != null) {
					fileOS.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				log.error(e.getLocalizedMessage());
			}
		}

		return fileName.toString();
	}

}
