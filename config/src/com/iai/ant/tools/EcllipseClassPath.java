package com.iai.ant.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

/**
 * @author meverline
 *
 */
public class EcllipseClassPath {
	
	public static final String CLASSPATHENTRY ="classpathentry";
	public static final String KIND ="kind";
	public static final String PATH ="path";
	public static final String CLASSPATH ="classpath";
	
	private SAXParser  parser = null;
	private boolean    includeSource = false;
	private boolean    debug = false;
	
	public EcllipseClassPath(boolean incSrc, boolean debug)
	{
		this.includeSource = incSrc;
		this.debug = debug;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			setParser(factory.newSAXParser());
		} catch (ParserConfigurationException e) {
			System.out.println(e);
		} catch (SAXException e) {
			System.out.println(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	private SAXParser getParser() {
		return parser;
	}
	
	/**
	 * 
	 * @param parser
	 */
	private void setParser(SAXParser parser) {
		this.parser = parser;
	}
	
	/**
	 * 
	 * @param directory
	 * @return
	 */
	private void scanDir(String directory, List<List<Entry>> pathEntry) {
		
		File dirFile = new File(directory);
		if ( ! dirFile.exists() ) { return ; }
		for ( File fp : dirFile.listFiles()) {
			
			if ( fp.isFile() && fp.getName().endsWith("jar")) {
				
				boolean add = true;
				if ( fp.getName().contains("-source" ) ) {
				   add = this.includeSource;
				}
				
				if ( add ) {
					List<Entry> map = new ArrayList<Entry>();
					
					map.add( new Entry(KIND, "lib"));
					map.add( new Entry(PATH, fp.toString().replace('\\', '/')));
					
					pathEntry.add(map);
				}
			}
		}
		return;
		
	}
	
	/**
	 * Write the ecllispe path file.
	 * @param stream
	 * @param pathEntry
	 */
	private void write(PrintStream stream, List<List<Entry>> pathEntry)
	{
		stream.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		stream.print("<");
		stream.print(CLASSPATH);
		stream.println(">");
		for ( List<Entry> entry : pathEntry ) {
			stream.print("    <");
			stream.print(CLASSPATHENTRY);
			stream.print(" ");
			for ( Entry info : entry ) {
				stream.print(info.getKey());
				stream.print("=\"");
			    stream.print(info.getValue());
			    stream.print("\" ");
			}
			stream.println(" />");
		}
		stream.print("</");
	    stream.print(CLASSPATH);
		stream.println(">");
	}
	
	/**
	 * Fix the ecllipse .classpath file by update in the lib entries.
	 * @param classPath
	 * @param libPath
	 * @return
	 */
	public boolean fixPath(String classPath, String libPath, List<String> depends)
	{
		File file = new File(classPath);
		if ( file.exists() ) {
			try {
	
				Handler handler = new Handler();
				getParser().parse(new InputSource(new FileReader(classPath)), handler);
				scanDir(libPath, handler.getPathEntry());
				
				File fp = new File(classPath);
				PrintStream stream = new PrintStream(fp);
				
				if ( this.debug ) {
					write( System.out, handler.getPathEntry());
				} else {
					System.out.println("write " + classPath);
					write(stream, handler.getPathEntry());
				}
				
				stream.close();
		
			} catch (SAXException e) {
				System.out.println(e);
				return false;
			} catch (IOException e) {
				System.out.println(e);
				return false;
			}
		} else {
			
			List<List<Entry>> pathEntry = new ArrayList<List<Entry>>();
			
			List<Entry> item = new ArrayList<Entry>();
			item.add(new Entry("kind", "src"));
			item.add(new Entry("path", "src"));
			pathEntry.add(item);
			
			item = new ArrayList<Entry>();
			item.add(new Entry("kind", "con"));
			item.add(new Entry("path", "org.eclipse.jdt.junit.JUNIT_CONTAINER/4"));
			pathEntry.add(item);
			
			item = new ArrayList<Entry>();
			item.add(new Entry("kind", "con"));
			item.add(new Entry("path", "org.eclipse.jdt.launching.JRE_CONTAINER"));
			pathEntry.add(item);
			
			for ( String entry : depends ) {
				item = new ArrayList<Entry>();
				item.add(new Entry("combineaccessrules", "false"));
				item.add(new Entry("kind", "src"));
				item.add(new Entry("path", entry));
				pathEntry.add(item);
			}
			
			scanDir(libPath, pathEntry);
			
			if ( this.debug ) {
				write( System.out, pathEntry);
			} else {
				File fp = new File(classPath);
				PrintStream stream;
				try {
					stream = new PrintStream(fp);
					System.out.println("write " + classPath);
					write(stream, pathEntry);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			
		}
		return true;
	}

	/**
	 * Help message.
	 */
	public static void help()
	{
		System.out.println(EcllipseClassPath.class.getName());
		System.out.println("\tlib:<path>    The path to lib dir");
		System.out.println("\tfile:<path>    Ecllipse .classpath file to modify");
		System.out.println("\tdepends:<project>;<project> list of projects");
		System.out.println("\tsource:true/false include source jar files");
		System.out.println("\tnowrite don't write class path");
		System.out.println("\thelp    this message");
		System.exit(0);	
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String libPath = null;
		String classPathFile = null;
		List<String> depends = new ArrayList<String>();
		boolean includeSource = false;
		boolean debug = false;
		boolean nowrite = false;
		
		for ( int ndx = 0; ndx < args.length; ndx++) {
			if ( args[ndx].startsWith("lib:")) {
				
			   int index = args[ndx].indexOf(":");
			   libPath = args[ndx].substring(index+1).replace('\\', '/');
			
			} else if ( args[ndx].startsWith("file:")) {
				
			   int index = args[ndx].indexOf(":");
			   classPathFile = args[ndx].substring(index+1).replace('\\', '/');		
			   
			} else if ( args[ndx].startsWith("depends:")) {
				
			   int index = args[ndx].indexOf(":");
			   
			   String [] projs = args[ndx].substring(index+1).replace('\\', '/').split("%");
			   for (String p : projs) {
				   depends.add(p);
			   }

			} else if ( args[ndx].startsWith("source:")) {
				
				   int index = args[ndx].indexOf(":");			   
				   includeSource = Boolean.parseBoolean(args[ndx].substring(index+1));
					
			} else if ( args[ndx].startsWith("debug")) {
				debug = true;
			} else if ( args[ndx].startsWith("nowrite")) {
				nowrite = true;
			} else if ( args[ndx].startsWith("help")) {
				help();	
			}
		}
		
		if ( libPath == null || classPathFile == null ) {
			System.out.println("Missing lib path or class path file");
			help();
		}
		
		if ( nowrite ) {
			return;
		} else {
			EcllipseClassPath pathFixer = new EcllipseClassPath(includeSource, debug);
			if ( ! pathFixer.fixPath(classPathFile, libPath, depends)) {
				System.exit(-1);
			}
		}
		
		return;
	}
	
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////

	public class Entry {
		
		private String key = "";
		private String value = "";
		
		public Entry(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
		
	}
	
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	
	public class Handler extends DefaultHandler2 {
		
		private List<List<Entry>> pathEntry = new ArrayList<List<Entry>>();

		/**
		 * 
		 */
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			
			if ( qName.compareTo(CLASSPATHENTRY) == 0 ) {
				
				String kind = attributes.getValue(KIND);
				
				if ( kind.compareTo("lib") != 0 ) {
					List<Entry> map = new ArrayList<Entry>();
					
					for ( int ndx = 0; ndx < attributes.getLength(); ndx++) {
						map.add( new Entry(attributes.getQName(ndx), 
										   attributes.getValue(ndx).trim() ));
					}
					getPathEntry().add(map);
				}
				
			}
		}
		
		/**
		 * 
		 * @return
		 */
		public List<List<Entry>> getPathEntry()
		{
			return pathEntry;
		}
	
	}
	
}
