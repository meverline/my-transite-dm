package browser.scanner;

import java.io.File;

public class JarScanner extends FileScanner {

	/*
	 * (non-Javadoc)
	 * @see browser.scanner.FileScanner#isValid(java.lang.String)
	 */
	@Override
	protected boolean isValid(File file) {
		
		if (file.toString().endsWith(".jar") ) {
			if ( file.getPath().contains("war") || 
				 file.getPath().contains("WEB-INF") || 
				 file.getPath().contains(".metadata")) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String [ ] args)
	{
		JarScanner cs = new JarScanner();
		
		cs.scan("/Users/markeverline/Perforce/CIRAS_IR/trunk");
		for ( String file : cs.getFiles() ) {
			System.out.println(file);
		}
	}

}
