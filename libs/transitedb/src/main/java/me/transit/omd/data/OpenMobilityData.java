package me.transit.omd.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenMobilityData {

	private static final int BUFFER_SIZE = 4096;

	private final static String API_KEY = "84b0aa80-1386-4f14-a026-4a7aec021430";
	private final static String BASE_URL = "http://api.transitfeeds.com/v1";
	private Log log = LogFactory.getLog(getClass().getName());
	
	private Map<String, Feed>feedCache = null;
	private int lastPid = -1;

	/**
	 *
	 */
	public OpenMobilityData() {
	}
	
	private Map<String, Feed> getFeedCache() {
		return feedCache;
	}

	private void setFeedCache(Map<String, Feed> feedCache) {
		this.feedCache = feedCache;
	}

	private int getLastPid() {
		return lastPid;
	}

	private void setLastPid(int lastPid) {
		this.lastPid = lastPid;
	}

	/**
	 * 
	 * @param theUrl
	 * @return
	 */
	private String getResponse(String theUrl) {
		StringBuilder content = new StringBuilder();

		// many of these calls can throw exceptions, so i've just
		// wrapped them all in one try/catch statement.
		log.info(theUrl);
		try {
			// create a url object
			URL url = new URL(theUrl);

			// create a urlconnection object
			URLConnection urlConnection = url.openConnection();

			// wrap the urlconnection in a bufferedreader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			String line;

			// read from the urlconnection via the bufferedreader
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return content.toString();
	}

	/**
	 * 
	 * @return
	 */
	public List<Location> getLocations() {
		List<Location> rtn = new ArrayList<>();
		StringBuilder api = new StringBuilder(OpenMobilityData.BASE_URL);
		api.append("/getLocations?key=");
		api.append(OpenMobilityData.API_KEY);

		String response = this.getResponse(api.toString());

		try {
			LocationsResponse itemWithOwner = new ObjectMapper().readValue(response, LocationsResponse.class);
			rtn = itemWithOwner.getResults().getLocations();
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}

		return rtn;
	}
	
	private String pageFeed(int parentId, int page) {
		StringBuilder api = new StringBuilder(OpenMobilityData.BASE_URL);
		api.append("/getFeeds?key=");
		api.append(OpenMobilityData.API_KEY);
		api.append("&location=");
		api.append(parentId);
		api.append("&descendants=1&page=");
		api.append(page);
		api.append("&limit=20");
		
		return api.toString();
	}

	/**
	 * 
	 * 
	 * @param parentId
	 * @return
	 */
	public Map<String, Feed> getFeeds(int parentId) {
		Map<String, Feed> rtn = new HashMap<>();
		
		if ( parentId == this.getLastPid() ) {
			return this.getFeedCache();
		}
		
		try {
			
			int page = 0;
			boolean done = false;
			
			while ( ! done ) {
				String response = this.getResponse(this.pageFeed(parentId, page));
				if ( response.contains("No Content")) {
					return rtn;
				}
				
				String val = response.replaceAll("\"u\":\\[\\]", "\"u\":{}").replaceAll("\\/", "/");
				
				FeedsResponse fr = new ObjectMapper().readValue(val, FeedsResponse.class);
				
				for ( Feed f : fr.getResults().getFeeds()) {
					rtn.put( f.getAgencyName(), f);
				}
				if ( fr.getResults().getPage() == fr.getResults().getNumpages()) {
					done = true;
				}
				page++;
			}
			
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		
		this.setLastPid(parentId);
		this.setFeedCache(rtn);

		return rtn;
	}

	/**
	 * 
	 * @param destinationDir
	 * @param zipEntry
	 * @return
	 * @throws IOException
	 */
	protected static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}

	/**
	 * 
	 * @param fileZip
	 */
	public void unzip(String dir, String fileZip) {
		File file = new File(fileZip);

		ZipInputStream zis = null;
		FileOutputStream fos = null;

		try {
			File path = new File(dir);
			zis = new ZipInputStream(new FileInputStream(file));
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				File newFile = newFile(path, zipEntry);
				fos = new FileOutputStream(newFile);

				byte[] buffer = new byte[1024];
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				zipEntry = zis.getNextEntry();
			}

		} catch (Exception e) {
			log.error("Unable to read zip file: " + e.getLocalizedMessage(), e);
		} finally {
			if (zis != null) {
				try {
					if (fos != null) {
						fos.close();
					}
					zis.closeEntry();
					zis.close();
				} catch (Exception e) {
					log.error("Unable to close zip file: " + e.getLocalizedMessage(), e);
				}
			}
		}

	}

	/**
	 *
	 * @param responseCode
	 * @param connection
	 * @return
	 */
	private HttpURLConnection isRedicrect(int responseCode, HttpURLConnection connection) throws IOException {
		HttpURLConnection rtn = null;
		if ( responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
				responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
		        responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
			String newUrl = connection.getHeaderField("Location");
			log.info("Redirect to url: " + newUrl);
			rtn = (HttpURLConnection) new URL(newUrl).openConnection();

		}
		return rtn;
	}

	/**
	 *
	 * @param fileURL
	 * @param saveDir
	 * @param httpConn
	 * @return
	 * @throws IOException
	 */
	private String downloadFile(String fileURL,  String saveDir, HttpURLConnection httpConn) throws IOException {

		String fileName = "";
		String disposition = httpConn.getHeaderField("Content-Disposition");

		if (disposition != null) {
			// extracts file name from header field
			int index = disposition.indexOf("filename=");
			if (index != -1) {
				fileName = disposition.substring(index + 10,
						disposition.length() - 1);
			}
		} else {
			// extracts file name from URL
			fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1);
		}

		// opens input stream from the HTTP connection
		InputStream inputStream = httpConn.getInputStream();
		String  saveFilePath = saveDir + File.separator + fileName.replace(' ', '_');

		// opens an output stream to save into file
		FileOutputStream outputStream = new FileOutputStream(saveFilePath);

		int bytesRead = -1;
		long total = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
			total += bytesRead;
		}

		outputStream.close();
		inputStream.close();

		log.info(String.format("File downloaded: %s size %d bytes",saveFilePath, total));

		return saveFilePath;
	}

	/**
	 *
	 * @param fileURL
	 * @param saveDir
	 * @return
	 */
	private String downloadFile(String fileURL, String saveDir) {
		
		String saveFilePath = null;
        HttpURLConnection httpConn = null;
        
        try {
            URL url = new URL(fileURL);

            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setInstanceFollowRedirects(true);
	        int responseCode = httpConn.getResponseCode();
	 
	        // always check HTTP response code first
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	        	saveFilePath = this.downloadFile(fileURL, saveDir, httpConn);
	        } else {

				log.info("No file to download. Server replied HTTP code: " + responseCode);
				HttpURLConnection redirectConn = this.isRedicrect(responseCode, httpConn);
				if ( redirectConn != null ) {
					saveFilePath = this.downloadFile(fileURL, saveDir, redirectConn);
				}

	        }
        } catch (IOException ex ) {
        	log.error(ex.getLocalizedMessage());
        } finally {
        	if (httpConn != null ) {
        		httpConn.disconnect();
        	}
        }
        return saveFilePath;
    }

	/**
	 * 
	 * @param feed
	 */
	public String download(Feed feed) {

		if ( ! feed.getType().equals("gtfs") ) {
			throw new IllegalArgumentException("Only type of gtfs is supported not: " + feed.getType());
		}

		String feedName = feed.getId().substring(feed.getId().indexOf("/") + 1);
		StringBuffer fileName = new StringBuffer(System.getProperty("java.io.tmpdir"));
		fileName.append("GTFS");
		fileName.append(File.separator);
		fileName.append(feedName);
		
		File file = new File(fileName.toString());
		if (file.exists()) {
			file.delete();
		}
		file.mkdirs();
		//file.deleteOnExit();
		
		log.info("Dir: " + fileName.toString() );
					
		String zipFile = this.downloadFile(feed.getUrl().getGtfsUrl(), fileName.toString());
		if ( zipFile != null ) {
			this.unzip(file.toString(), zipFile);
			File fp = new File(zipFile);
			fp.delete();
		}
		return file.getAbsolutePath();
		
	}

}
