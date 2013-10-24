package browser.util;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import browser.graph.Graph;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class ApplicationSettings {
	
	public static final String PROJECT_EXT = ".prj";
	
	private static ApplicationSettings self = null;
	
	private Log log = LogFactory.getLog(ApplicationSettings.class);
	private Settings settings = null;
		
	/**
	 * 
	 */
	private ApplicationSettings() {	
		this.settings = new Settings();
		load();
	}
		
	/**
	 * 
	 * @return
	 */
	public String getFilepath()
	{
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
	protected String getFilename(String name)
	{
		StringBuilder path = new StringBuilder();
		path.append(getFilepath());
		path.append(File.separator);
		if ( name == null ) {
	 	   path.append(this.getClass().getName());
		   path.append(".settings"); 
		} else {
		   path.append(name);
		   path.append(ApplicationSettings.PROJECT_EXT); 			
		}
		return path.toString();
	}
	
	/**
	 * 
	 */
	protected void load()
	{
		File dir = new File(getFilename(null));
		if ( dir.exists()) {
			try {				
				XStream xstream = new XStream(new JettisonMappedXmlDriver());
				
				this.settings = Settings.class.cast(xstream.fromXML(dir));
			} catch (Exception e) {
				log.error(this.getClass().getName(), e);
			}
		}

	}
	
	/**
	 * 
	 * @return
	 */
	public Project loadCurrentProject() {
		return this.loadProject(this.getSettings().getCurrentProject());
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public Project loadProject(String name)
	{
		Project rtn = null;
		File dir = new File(getFilename(name));
		if ( dir.exists()) {
			try {				
				XStream xstream = new XStream(new JettisonMappedXmlDriver());
				
				rtn = Project.class.cast(xstream.fromXML(dir));
			} catch (Exception e) {
				log.error(this.getClass().getName(), e);
			}
		}
		return rtn;
	}
	
	public Project loadProject(File file)
	{
		Project rtn = null;
		if ( file.exists()) {
			try {				
				XStream xstream = new XStream(new JettisonMappedXmlDriver());
				
				rtn = Project.class.cast(xstream.fromXML(file));
			} catch (Exception e) {
				log.error(this.getClass().getName(), e);
			}
		}
		return rtn;
	}
	
	/**
	 * 
	 */
	public void save()
	{
		File dir = new File(getFilepath());
		
		if ( ! dir.exists()) {
			dir.mkdirs();
		}
		dir = new File(getFilename(null));
		try {
			FileOutputStream stream = new FileOutputStream( dir );

			XStream xstream = new XStream(new JettisonMappedXmlDriver());
			xstream.toXML(this.getSettings(), stream);
			stream.close();
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	/**
	 * 
	 * @param project
	 */
	public void saveProject(Project project)
	{
		File dir = new File(getFilepath());
		
		if ( ! dir.exists()) {
			dir.mkdirs();
		}
		dir = new File(getFilename(project.getName()));
		try {
			FileOutputStream stream = new FileOutputStream( dir );

			XStream xstream = new XStream(new JettisonMappedXmlDriver());
			xstream.toXML(project, stream);
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
		
		private String classBuildPath = "build/classes;build;bin";
		private String srcPath = "src;src/src";
		private String graphHome = null;
		private String graphProgram = Graph.CMD.dot.name();
		private String graphOutput = Graph.OUTPUT.svg.name();
		private String graphDepth = Graph.DEPTH.ALL.name();
		private String graphNodeColor = Graph.COLOR.tan.name();
		private String graphFontColor = Graph.COLOR.black.name();
		private String previewCommand = null;
		private String currentProject = null;
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
			}
			
			graphOutputDir = System.getProperty("user.home");
		}
		
		/**
		 * @return the classBuildPath
		 */
		public String getClassBuildPath() {
			return classBuildPath;
		}
	
		/**
		 * @param classBuildPath the classBuildPath to set
		 */
		public void setClassBuildPath(String classBuildPath) {
			this.classBuildPath = classBuildPath;
			ApplicationSettings.instance().save();
		}
	
		/**
		 * @return the srcPath
		 */
		public String getSrcPath() {
			return srcPath;
		}
	
		/**
		 * @param srcPath the srcPath to set
		 */
		public void setSrcPath(String srcPath) {
			this.srcPath = srcPath;
			ApplicationSettings.instance().save();
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
			ApplicationSettings.instance().save();
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
			ApplicationSettings.instance().save();
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
			ApplicationSettings.instance().save();
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
			ApplicationSettings.instance().save();
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
			ApplicationSettings.instance().save();
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
			ApplicationSettings.instance().save();
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
			ApplicationSettings.instance().save();
		}
	
		/**
		 * @return the currentProject
		 */
		public String getCurrentProject() {
			return currentProject;
		}
	
		/**
		 * @param currentProject the currentProject to set
		 */
		public void setCurrentProject(String currentProject) {
			this.currentProject = currentProject;
			ApplicationSettings.instance().save();
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
			ApplicationSettings.instance().save();
		}
		
	}
	
}
