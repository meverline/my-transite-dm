package browser.gui.models;

import browser.loader.ClassReflectionData;

public class NodeData {
	String path = null;
	String label = null;
	ClassReflectionData cls = null;
	
	public NodeData(String aPath, String label, ClassReflectionData cls) {
		this.path = aPath;
		this.label = label;
		this.cls = cls;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getLabel();
	}

	public ClassReflectionData getCls() {
		return cls;
	}

	public void setCls(ClassReflectionData cls) {
		this.cls = cls;
	}
	
}
