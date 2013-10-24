/**
 * 
 */
package browser.loader;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

/**
 * @author meverline
 *
 */
public class AsmSignatureVisitor extends SignatureVisitor {

	private int array = 0;
	private List<ParamaterData> parmeters = new ArrayList<ParamaterData>();
	private List<String> genericArgs = new ArrayList<String>();
	private String type = null;
	private boolean primative = false;
	private ParamaterData rt = null;
	private boolean classArgs = false;
	private boolean inParameters = false;
	
	public AsmSignatureVisitor(boolean isClassArgs) {
		super(Opcodes.ASM4);
		classArgs = isClassArgs;
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.signature.SignatureVisitor#visitArrayType()
	 */
	@Override
	public SignatureVisitor visitArrayType() {
		this.array++;
		return this;
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.signature.SignatureVisitor#visitBaseType(char)
	 */
	@Override
	public void visitBaseType(char descriptor) {
		String type = AsmClassVisitor.toType(Character.toString(descriptor));
		if ( this.type == null ) {
			this.type = type;
			this.primative = true;
		} else {
			this.genericArgs.add(type);
		}
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.signature.SignatureVisitor#visitClassType(java.lang.String)
	 */
	@Override
	public void visitClassType(String name) {
		String type = name.replace("/", ".").trim();
		if ( this.classArgs ) {
			this.genericArgs.add(type);
		} else if ( this.type == null ) {
			this.type = type;
		} else {
			this.genericArgs.add(type);
		}
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.signature.SignatureVisitor#visitFormalTypeParameter(java.lang.String)
	 */
	@Override
	public void visitFormalTypeParameter(String name) {
		this.genericArgs.add(name);
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.signature.SignatureVisitor#visitEnd()
	 */
	@Override
	public void visitEnd() {
		if ( ! this.classArgs ) {
			if ( this.inParameters) {
				this.addParameter();
			} else {
				this.rt = new ParamaterData(type, array, primative, this.genericArgs );
			}
		}
	}
	
	private void addParameter()
	{
		if ( this.type != null ) {
	    	this.parmeters.add( new ParamaterData(type, array, primative, this.genericArgs ));
		}
		this.type = null;
		this.array = 0;
		this.primative = false;
		this.genericArgs = new ArrayList<String>();
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.signature.SignatureVisitor#visitParameterType()
	 */
	@Override
	public SignatureVisitor visitParameterType() {
		this.inParameters = true;
		return this;
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.signature.SignatureVisitor#visitReturnType()
	 */
	@Override
	public SignatureVisitor visitReturnType() {
		this.inParameters = false;
		return this;
	}

	public ParamaterData getReturnType() {
		if ( this.rt == null ) {
			return new ParamaterData("void", 0, true, null);
		}
		return this.rt;
	}
	
	public List<ParamaterData> getParamterData() {
		if ( this.parmeters.isEmpty() ) {
			this.parmeters.add(new ParamaterData("void", 0, true, null));
		}
		return this.parmeters;
	}
	
	public List<String> getGenericArgs() {
		return this.genericArgs;
	}
		
}
