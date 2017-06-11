package browser.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import browser.util.ApplicationSettings;

public abstract class FileScanner {
	
	private List<String> files = new ArrayList<String>();
	private Map<String, String> srcFiles = new HashMap<String, String>();
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	protected abstract boolean isValid(File file);
	
	
	protected boolean isSource(File file) {
		
		if ( file.toString().endsWith(".java") ) {
			return true;
		}
		return false;
		
	}
	
	/**
	 * 
	 * @param directory
	 * @return
	 */
	public void scan(String directory)
	{
		File dir = new File(directory);
		
		for ( File afile : dir.listFiles() ) {
			if ( afile.isDirectory() ) {
				scan(afile.toString());
			} else if ( this.isSource(afile) ) {
				
				String path[] = ApplicationSettings.instance().getSettings().getSrcPath();
				String fname = afile.toString();
				for ( int ndx = 0; ndx < path.length; ndx++) {
					if ( fname.contains(path[ndx]) ) {
						int loc = fname.indexOf(path[ndx]);
						fname = fname.substring(loc+path[ndx].length()+1, fname.length());
						loc = fname.lastIndexOf(".");
						fname = fname.substring(0, loc);
						this.srcFiles.put(fname.replace("\\", "/").replace("/", "."), afile.toString());
						break;
					}
				}
				
				
			}else if ( isValid(afile) ){
				files.add(afile.toString().replace("\\", "/"));
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getFiles() {
		return files;
	}

	/**
	 * 
	 * @param files
	 */
	protected void setFiles(List<String> files) {
		this.files = files;
	}


	public Map<String, String> getSrcFiles() {
		return srcFiles;
	}


	public void setSrcFiles(Map<String, String> srcFiles) {
		this.srcFiles = srcFiles;
	}
	
}
