package browser.scanner;

import java.io.File;

public class ClassScanner extends FileScanner {

	/*
	 * (non-Javadoc)
	 * @see browser.scanner.FileScanner#isValid(java.lang.String)
	 */
	@Override
	protected boolean isValid(File file) {
		
		if ( file.toString().endsWith(".class") ) {
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
		ClassScanner cs = new ClassScanner();
		
		cs.scan("/Users/markeverline/Perforce/CIRAS_IR/trunk");
		for ( String file : cs.getFiles() ) {
			System.out.println(file);
		}
	}

}
