package browser.loader;

import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;

public class MethodData {

	private int accessFlags = 0;
	private ParamaterData retrunType;
	private String name;
	private List<ParamaterData> parameters = new ArrayList<ParamaterData>();
	private List<String> exceptions = new ArrayList<String>();
	private List<String> localvarsType = new ArrayList<String>();
	
	public MethodData(int access, String name, List<ParamaterData> parameters, ParamaterData retrunType, String[] except) {
		this.accessFlags = access;
		this.name = name;
		this.retrunType = retrunType;
	    this.parameters = parameters;
		
	    if ( except != null ) {
			for (String item : except) {
				this.exceptions.add(item);
			}
	    }
	}
	
	public void addLocal(String localType)  {
		if ( localType != null ) {
			localvarsType.add(localType);
		}
	}

	/**
	 * @return the retrunType
	 */
	public ParamaterData getRetrunType() {
		return retrunType;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the parameters
	 */
	public List<ParamaterData> getParameters() {
		return parameters;
	}

	/**
	 * @return the exceptions
	 */
	public List<String> getExceptions() {
		return exceptions;
	}

	/**
	 * @return the localvarsType
	 */
	public List<String> getLocalvarsType() {
		return localvarsType;
	}
	
	private boolean checkFlag(int flag) {
		boolean rtn = false;
		if ( (accessFlags & flag) == flag) {
			rtn = true;
		}
		return rtn;
	}
	
	public boolean isStatic() {
		return checkFlag(Opcodes.ACC_STATIC);
	}
	
	public boolean isPublic() {
		return checkFlag(Opcodes.ACC_PUBLIC);
	}

	public boolean isPrivate() {
		return checkFlag(Opcodes.ACC_PRIVATE);
	}

	public boolean isProtected() {
		return checkFlag(Opcodes.ACC_PROTECTED);
	}

	public boolean isAbstract() {
		return checkFlag(Opcodes.ACC_ABSTRACT);
	}
	
	public boolean isThreadSafe() {
		return checkFlag(Opcodes.ACC_SYNCHRONIZED);
	}

	public String toString() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(this.name);
		buffer.append(") ");
		for ( ParamaterData g : this.parameters) {
		    buffer.append(g.toString());
		    buffer.append(" ");
		}
		buffer.append(") ");
		buffer.append(this.retrunType);
		buffer.append(" ");
		for ( String g : this.exceptions) {
		    buffer.append(g);
		    buffer.append(" ");
		}
		return buffer.toString();
		
	}

}
