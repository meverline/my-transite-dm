package browser.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class FileScanner {
	
	private List<String> files = new ArrayList<String>();
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	protected abstract boolean isValid(File file);
	
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
			} else if ( isValid(afile) ){
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
	
	
}
