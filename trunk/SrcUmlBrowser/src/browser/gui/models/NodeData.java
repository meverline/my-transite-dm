package browser.gui.models;

public class NodeData {
	String path = null;
	String label = null;
	
	public NodeData(String aPath, String label) {
		this.path = aPath;
		this.label = label;
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
	
}
