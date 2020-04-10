package browser.loader;

import java.util.List;

public class ParamaterData {
	private String type;
	private int array = 0;
	private boolean primative = false;
	private List<String> genericTypes = null;
	
	public ParamaterData(String type, int array, boolean primative, List<String> parms) {
		this.type = type;
		this.array = array;
		this.primative = primative;
		if ( parms != null) {
		   this.genericTypes = parms;
		}
	}

	public String getType() {
		return type;
	}
	
	public boolean isArray() {
		return (array != 0);
	}
	
	public int getArrayDimension() {
		return array;
	}
	
	public boolean isPrmative() {
		return primative;
	}
	
	public boolean isGeneric() {
		boolean rtn = false;
		if ( this.genericTypes != null && (! this.genericTypes.isEmpty()) ) {
			rtn = true;
		}
		return rtn;
	}
	
	public List<String> getGenericTypes() {
		return this.genericTypes;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(this.getType());
		buffer.append(" ");
		buffer.append(this.array);
		buffer.append(" ");
		buffer.append(this.primative);
		buffer.append(" ");
		for ( String g : this.genericTypes) {
		    buffer.append(g);
		    buffer.append(" ");
		}
		return buffer.toString();
	}
}
