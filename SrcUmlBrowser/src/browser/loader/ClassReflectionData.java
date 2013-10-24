package browser.loader;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Opcodes;

public class ClassReflectionData {

	private String name = null;
	private int accessFlags = 0;
	private List<FieldData> fields = new ArrayList<FieldData>();
	private List<MethodData> methods = new ArrayList<MethodData>();
	private String superClass = "java.lang.Object";
	private List<String> interfaces = new ArrayList<String>();
	private List<String> annotations  = new ArrayList<String>();
	private List<String> templateArgs = new ArrayList<String>();
	
	public ClassReflectionData(String name)
	{
		this.name = name.replace("/", ".").trim();
	}
	
	public ClassReflectionData(int access, String name, String superClass, String[] interfaces)
	{
		this.accessFlags = access;
		this.name = name.replace("/", ".").trim();
		this.superClass = superClass.replace("/", ".").trim();
		for ( String item : interfaces) {
			this.interfaces.add(item.replace("/", ".").trim());
		}
	}
	
	/**
	 * @return the templateArgs
	 */
	public List<String> getTemplateArgs() {
		return templateArgs;
	}

	/**
	 * @param templateArgs the templateArgs to set
	 */
	public void setTemplateArgs(List<String> templateArgs) {
		this.templateArgs = templateArgs;
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

	public boolean isInterface() {
		return checkFlag(Opcodes.ACC_INTERFACE);
	}
	
	public boolean isEnum() {
		return checkFlag(Opcodes.ACC_ENUM);
	}
	
	public boolean isFinal() {
		return checkFlag(Opcodes.ACC_FINAL);
	}
	
	public boolean isSynthetic() {
		return checkFlag(Opcodes.ACC_SYNTHETIC);
	}
	
	public boolean isAbstract() {
		return checkFlag(Opcodes.ACC_ABSTRACT);
	}
	
	public boolean isAnnotation() {
		return checkFlag(Opcodes.ACC_ANNOTATION);
	}
	
	public void addMethod(MethodData data) {
		if ( data != null ) {
			this.methods.add(data);
		}
	}
	
	public void addField(FieldData data) {
		if ( data != null ) {
			this.fields.add(data);
		}
	}
	
	public void addAnnotation(String data) {
		if ( data != null ) {
			this.annotations.add(data.replace("/", ".").trim());
		}
	}
		
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the packagePath
	 */
	public String getPackagePath() {
		String className = getName();
		int index = className.lastIndexOf(".");
		return className.substring(0, index);
	}

	/**
	 * @return the fields
	 */
	public List<FieldData> getFields() {
		return fields;
	}

	/**
	 * @return the methods
	 */
	public List<MethodData> getMethods() {
		return methods;
	}

	/**
	 * @return the superClass
	 */
	public String getSuperClass() {
		return superClass;
	}

	/**
	 * @return the interfaces
	 */
	public List<String> getInterfaces() {
		return interfaces;
	}
	
	/**
	 * @return the annotations
	 */
	public List<String> getAnnotations() {
		return annotations;
	}
	
	public String toString() {
		return getName();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getShortName() {
		String matchString = getName().substring(0, getName().lastIndexOf("."));
		return getName().substring(matchString.length()+1);
	}
		
}
