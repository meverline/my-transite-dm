package browser.util;

public class Project {
		
	private String name = null;
	private String packageRoot = null;
	private String loadPath = null;
	private String scanClassPath = null;
	private String scanJarPath = null;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLoadPath() {
		return loadPath;
	}
	
	/**
	 * 
	 * @param loadPath
	 */
	public void setLoadPath(String loadPath) {
		this.loadPath = loadPath;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getScanClassPath() {
		return scanClassPath;
	}
	
	/**
	 * 
	 * @param scanClassPath
	 */
	public void setScanClassPath(String scanClassPath) {
		this.scanClassPath = scanClassPath;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getScanJarPath() {
		return scanJarPath;
	}
	
	/**
	 * 
	 * @param scanJarPath
	 */
	public void setScanJarPath(String scanJarPath) {
		this.scanJarPath = scanJarPath;
	}

	/**
	 * @return the packageRoot
	 */
	public String getPackageRoot() {
		return packageRoot;
	}

	/**
	 * @param packageRoot the packageRoot to set
	 */
	public void setPackageRoot(String packageRoot) {
		this.packageRoot = packageRoot;
	}
		
}
