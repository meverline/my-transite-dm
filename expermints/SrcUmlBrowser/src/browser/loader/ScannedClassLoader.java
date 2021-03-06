/**
 * 
 */
package browser.loader;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.ClassReader;

import browser.util.ApplicationSettings;

/**
 * @author markeverline
 *
 */
public class ScannedClassLoader  {
	
	private Log log = LogFactory.getLog(ScannedClassLoader.class);

	private Map<String,String> classMap = new HashMap<String,String>();
	private Map<String,ClassReflectionData> loaded = new HashMap<String,ClassReflectionData>();
	private Map<String, String> srcFilesIndex = new HashMap<String, String>();


	/**
	 * 
	 * @param scanPath
	 * @param classFiles
	 */
	public ScannedClassLoader(List<String> classFiles, String loadPath, Map<String, String> srcFiles) {
		init(classFiles, loadPath);
		this.srcFilesIndex = srcFiles;
	}
	
	public List<String> getClassList()
	{
		List<String> rtn = new ArrayList<String>();
		
		rtn.addAll( getClassMap().keySet());
		Collections.sort(rtn);
		return rtn;
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> getClassMap() {
		return classMap;
	}

	/**
	 * 
	 * @param classMap
	 */
	public void setClassMap(Map<String, String> classMap) {
		this.classMap = classMap;
	}
	
	/**
	 * 
	 * @param cls
	 * @return
	 */
	public String getClassFilename(ClassReflectionData cls)
	{				
		String srcFile = cls.getName();
		if ( srcFile.contains("$") ) {
			int ndx = srcFile.lastIndexOf("$");
			srcFile = srcFile.substring(0, ndx);
		}
			
		if ( srcFilesIndex.containsKey(srcFile)) {
			return srcFilesIndex.get(srcFile);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param className
	 * @return
	 */
	private String stripBuildPath(String className)
	{
		String path[] = ApplicationSettings.instance().getSettings().getClassBuildPath(); 

		for ( String part : path ) {
			if ( className.contains(part)) {
				int start = className.indexOf(part);
				className = className.substring(start+part.length()+1);
				return className;
			}
		}
		return className;
	}

	/**
	 * 
	 * @param path
	 * @param classList
	 * @param jarFiles
	 */
	private void init(List<String> classList, String path)
	{
			
		for ( String name : classList ) {
			if ( name.startsWith(path) ) {
				
				String className = this.stripBuildPath(name.substring(path.length()+1));
				
				className = className.replaceAll( "/", ".");
			
				int ndx = className.lastIndexOf('.');
				String key = className.substring(0, ndx);
				
				//key = key.replace("$",".");
				classMap.put(key, name);
			}
		}
	}	

	/**
	 * 
	 * @param className
	 * @return
	 */
	public ClassReflectionData load(String className) {
		
		ClassReflectionData rtn = null;
		
		if ( this.getClassMap().containsKey(className) ) {
			
		   if ( this.loaded.containsKey(className) ) {
		      rtn = this.loaded.get(className);
		   } else {
				
				try {
					String fileName = this.classMap.get(className);
					
					FileInputStream stream = new FileInputStream(fileName);
					ClassReader reader = new ClassReader(stream);
					AsmClassVisitor vistor = new AsmClassVisitor(this);
					reader.accept(vistor, 0);
					
					rtn = vistor.getClassData();
					this.loaded.put(className, rtn);
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
				}
		   }
		} 
		else if ( className != null && (! className.startsWith("java")) && className.indexOf(".") != -1 ) {
			if ( this.loaded.containsKey(className) ) {
			      rtn = this.loaded.get(className);
			} else {
				rtn = new ClassReflectionData(className);
				this.loaded.put(className, rtn);
			}
		}
		return rtn;
	}
	
}
