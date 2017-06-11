package browser.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import browser.graph.GraphVizGraph;

public class ApplicationSettings {
	
	public static final String PROJECT_LAST = ".lastopened";
	
	private static ApplicationSettings self = null;
	
	private Log log = LogFactory.getLog(ApplicationSettings.class);
	private Settings settings = null;
		
	/**
	 * 
	 */
	private ApplicationSettings() {	
		this.settings = new Settings();
		this.loadCurrentProject();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDir() {
		StringBuilder path = new StringBuilder();
		path.append(System.getProperty("user.home"));
		path.append(File.separator);
		path.append(".browser");
		return path.toString();
	}
		
	/**
	 * 
	 * @return
	 */
	public String getFilename()
	{
		StringBuilder path = new StringBuilder(this.getDir());
		path.append(File.separator);
		path.append(ApplicationSettings.PROJECT_LAST);
		return path.toString();
	}
			
	/**
	 * 
	 * @return
	 */
	public Project loadCurrentProject() {
		return this.loadProject(this.getFilename());
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public Project loadProject(String name)
	{
		Project rtn = null;
		if ( name != null ) {
			File dir = new File(name);
			if ( dir.exists()) {
				try {				
					FileReader reader = new FileReader(dir);
					BufferedReader lineReader = new BufferedReader(reader);
					
					String line = lineReader.readLine();
					lineReader.close();
					if ( line.endsWith("\n")) {
						line = line.substring(0, line.length()-1);
					}
					
					rtn = new Project(line);
					this.settings.setCurrentProject(rtn);
				
				} catch (Exception e) {
					log.error(this.getClass().getName(), e);
				}
			}
		}
		return rtn;
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	public Project loadProject(File file)
	{
		Project rtn = null;
		if ( file.exists()) {
			try {
				
				rtn = new Project(file);
				this.saveProject(rtn);
				
			} catch (Exception e) {
				log.error(this.getClass().getName(), e);
			}
		}
		return rtn;
	}
	
	/**
	 * 
	 * @param project
	 */
	public void saveProject(Project project)
	{
		File dir = new File(this.getDir());
		
		if ( ! dir.exists()) {
			dir.mkdirs();
		}
		
		try {
			File fname = new File(this.getFilename());
			PrintWriter stream = new PrintWriter( fname );

			stream.print(project.getLoadPath());
			stream.close();
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	/**
	 * @return the settings
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * @param settings the settings to set
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * 
	 * @return
	 */
	public static synchronized ApplicationSettings instance()
	{
		if ( ApplicationSettings.self == null ) {
			ApplicationSettings.self = new ApplicationSettings();
		}
		return ApplicationSettings.self;
	}
	
	//////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////
	
	public static class Settings {
		
		private String classBuildPath = "build/classes;build;bin;target/classes";
		private String srcPath = "src/java;src;src/src";
		private String graphHome = null;
		private String graphProgram = GraphVizGraph.CMD.dot.name();
		private String graphOutput = GraphVizGraph.OUTPUT.svg.name();
		private String graphDepth = GraphVizGraph.DEPTH.ALL.name();
		private String graphNodeColor = GraphVizGraph.COLOR.tan.name();
		private String graphFontColor = GraphVizGraph.COLOR.black.name();
		private String previewCommand = null;
		private Project currentProject = null;
		private String graphOutputDir = null;
		
		public Settings()
		{
			init();
		}
		
		private void init()
		{
	        String os = String.class.cast(System.getProperty("os.name"));
			
			if ( os.compareTo("Mac OS X") == 0) {
				this.graphHome = "/usr/local/bin";
				this.previewCommand = "open";
			} else {
				this.graphHome = "C:/Program Files (x86)/Internet Explorer/iexplore.exe";
				this.previewCommand = "";
			}
			
			graphOutputDir = System.getProperty("user.home");
		}
		
		/**
		 * @return the classBuildPath
		 */
		public String[] getClassBuildPath() {
			return classBuildPath.split(";");
		}
	
		/**
		 * @param classBuildPath the classBuildPath to set
		 */
		public void setClassBuildPath(String[] classBuildPath) {
			StringBuilder bld = new StringBuilder();
			for (int ndx=0; ndx < classBuildPath.length; ndx++) {
				if ( ndx != 0) { bld.append(";"); }
				bld.append(classBuildPath[ndx]);
			}
			this.classBuildPath = bld.toString();
		}
	
		/**
		 * @return the srcPath
		 */
		public String[] getSrcPath() {
			return srcPath.split(";");
		}
	
		/**
		 * @param srcPath the srcPath to set
		 */
		public void setSrcPath(String[] srcPath) {
			
			StringBuilder bld = new StringBuilder();
			for (int ndx=0; ndx < srcPath.length; ndx++) {
				if ( ndx != 0) { bld.append(";"); }
				bld.append(srcPath[ndx]);
			}
			this.srcPath = bld.toString();
		}
	
		/**
		 * @return the graphHome
		 */
		public String getGraphHome() {
			return graphHome;
		}
	
		/**
		 * @param graphHome the graphHome to set
		 */
		public void setGraphHome(String graphHome) {
			this.graphHome = graphHome;
			
		}
	
		/**
		 * @return the graphProgram
		 */
		public String getGraphProgram() {
			return graphProgram;
		}
	
		/**
		 * @param graphProgram the graphProgram to set
		 */
		public void setGraphProgram(String graphProgram) {
			this.graphProgram = graphProgram;
			
		}
	
		/**
		 * @return the graphOutput
		 */
		public String getGraphOutput() {
			return graphOutput;
		}
	
		/**
		 * @param graphOutput the graphOutput to set
		 */
		public void setGraphOutput(String graphOutput) {
			this.graphOutput = graphOutput;
			
		}
	
		/**
		 * @return the graphDepth
		 */
		public String getGraphDepth() {
			return graphDepth;
		}
	
		/**
		 * @param graphDepth the graphDepth to set
		 */
		public void setGraphDepth(String graphDepth) {
			this.graphDepth = graphDepth;
			
		}
	
		/**
		 * @return the graphNodeColor
		 */
		public String getGraphNodeColor() {
			return graphNodeColor;
		}
	
		/**
		 * @param graphNodeColor the graphNodeColor to set
		 */
		public void setGraphNodeColor(String graphNodeColor) {
			this.graphNodeColor = graphNodeColor;
			
		}
	
		/**
		 * @return the graphFontColor
		 */
		public String getGraphFontColor() {
			return graphFontColor;
		}
	
		/**
		 * @param graphFontColor the graphFontColor to set
		 */
		public void setGraphFontColor(String graphFontColor) {
			this.graphFontColor = graphFontColor;
			
		}
	
		/**
		 * @return the previewCommand
		 */
		public String getPreviewCommand() {
			return previewCommand;
		}
	
		/**
		 * @param previewCommand the previewCommand to set
		 */
		public void setPreviewCommand(String previewCommand) {
			this.previewCommand = previewCommand;
			
		}
	
		/**
		 * @return the currentProject
		 */
		public Project getCurrentProject() {
			return currentProject;
		}
	
		/**
		 * @param currentProject the currentProject to set
		 */
		public void setCurrentProject(Project currentProject) {
			this.currentProject = currentProject;
			
		}

		/**
		 * @return the graphOutputDir
		 */
		public String getGraphOutputDir() {
			return graphOutputDir;
		}

		/**
		 * @param graphOutputDir the graphOutputDir to set
		 */
		public void setGraphOutputDir(String graphOutputDir) {
			this.graphOutputDir = graphOutputDir;
			
		}
		
	}
	
}
