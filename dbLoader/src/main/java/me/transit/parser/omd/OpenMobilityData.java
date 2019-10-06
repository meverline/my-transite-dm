package me.transit.parser.omd;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenMobilityData {

	private final static String API_KEY = "84b0aa80-1386-4f14-a026-4a7aec021430";
	private final static String BASE_URL = "https://api.transitfeeds.com/v1";
	private Log log = LogFactory.getLog(getClass().getName());

	/**
	 * 
	 * @param url
	 */
	public OpenMobilityData() {
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
		StringBuffer api = new StringBuffer(OpenMobilityData.BASE_URL);
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

	/**
	 * 
	 * @param parentId
	 * @return
	 */
	public List<Feed> getFeeds(String parentId, int page) {
		List<Feed> rtn = new ArrayList<>();
		StringBuffer api = new StringBuffer(OpenMobilityData.BASE_URL);
		api.append("/getFeeds?key=");
		api.append(OpenMobilityData.API_KEY);
		api.append("&location=");
		api.append(parentId);
		api.append("&descendants=1&page=");
		api.append(page);
		api.append("&limit=20");

		String response = this.getResponse(api.toString());
		try {
			FeedsResponse itemWithOwner = new ObjectMapper().readValue(response, FeedsResponse.class);
			rtn = itemWithOwner.getResults().getFeeds();
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}

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
	 * @param file
	 */
	public void unzip(String fileZip) {
		File file = new File(fileZip);

		ZipInputStream zis = null;
		FileOutputStream fos = null;

		try {

			zis = new ZipInputStream(new FileInputStream(file));
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				File newFile = newFile(file.getAbsoluteFile(), zipEntry);
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
	 * @param url
	 */
	public String download(Feed feed) {

		if (feed.getType() != "gtfs") {
			throw new IllegalArgumentException("Only type of gtfs is supported not: " + feed.getType());
		}

		int ndx = feed.getId().indexOf("/");
		String feedName = feed.getId().substring(ndx + 1);
		StringBuffer fileName = new StringBuffer(System.getProperty("java.io.tmpdir"));
		fileName.append("GTFS");
		fileName.append(File.separator);
		fileName.append(feedName);
		fileName.append(File.separator);
		fileName.append("GTFS.zip");

		File file = new File(fileName.toString());
		file.mkdirs();

		BufferedInputStream inputStream = null;
		FileOutputStream fileOS = null;
		try {

			inputStream = new BufferedInputStream(new URL(feed.getUrl().getGtfsUrl()).openStream());
			fileOS = new FileOutputStream(file);
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
