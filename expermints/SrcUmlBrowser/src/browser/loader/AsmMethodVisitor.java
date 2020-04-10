package browser.loader;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AsmMethodVisitor extends MethodVisitor {

	private MethodData data = null;

	public AsmMethodVisitor(MethodData data) {
		super(Opcodes.ASM4);
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see org.objectweb.asm.MethodVisitor#visitLocalVariable(java.lang.String, java.lang.String, java.lang.String, org.objectweb.asm.Label, org.objectweb.asm.Label, int)
	 */
	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		
	    if ( signature != null ) {
	       int gstart = signature.indexOf("<");
	       int gend = signature.indexOf(">");
	       
	       if ( gend != -1 && gstart != - 1) {
	    	   String parm = signature.substring(gstart+1, gend);
		       List<ParamaterData> parms = AsmClassVisitor.getParametres(parm);
		       for ( ParamaterData item : parms ) {
		    	   this.data.addLocal(item.getType());
		    	   if ( item.isGeneric() ) {
		    		   for (String type : item.getGenericTypes() ) {
		    			   this.data.addLocal(type);
		    		   }
		    	   }
		       }
	       }
	    }
	    
		this.data.addLocal(AsmClassVisitor.toType(desc));
	}

}
