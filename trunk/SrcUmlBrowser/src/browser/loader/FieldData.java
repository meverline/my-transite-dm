package browser.loader;

import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;

public class FieldData extends ParamaterData {
	
	private int accessFlags = 0;
	private String name;
	
	public FieldData(int access, String name, ParamaterData data) {
		super( data.getType(), data.getArrayDimension(), data.isPrmative(), data.getGenericTypes());
		this.name = name;
		this.accessFlags = access;
	}


	public String getName() {
		return name;
	}
	
	public String toString() {
		return name + super.toString();
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

}
