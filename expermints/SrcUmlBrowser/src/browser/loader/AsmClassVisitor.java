package browser.loader;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;

public class AsmClassVisitor extends ClassVisitor {
	
	private ClassReflectionData refData = null;

	public AsmClassVisitor(ScannedClassLoader loader) {
		super(Opcodes.ASM4);
	}
	
	/**
	 * 
	 * @return
	 */
	public ClassReflectionData getClassData() {
		return refData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.ClassVisitor#visit(int, int, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
				
	    AsmSignatureVisitor visitor = null;
	    if ( signature != null ) {
		   SignatureReader reader = new SignatureReader(signature);
		   visitor = new AsmSignatureVisitor(true);
		   reader.accept(visitor);      
	    }
	    
		refData = new ClassReflectionData(access, name, superName, interfaces);
		if ( visitor != null ) {
			List<String> templArgs = visitor.getGenericArgs();
			templArgs.remove(templArgs.size()-1);
			refData.setTemplateArgs(templArgs);
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.ClassVisitor#visitAnnotation(java.lang.String,
	 * boolean)
	 */
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		refData.addAnnotation(AsmClassVisitor.toType(desc));
		return super.visitAnnotation(desc, visible);
	}
	
	/**
	 * 
	 * @param desc
	 * @return
	 */
	public static String toArrayType(String desc) {
		return AsmClassVisitor.toType(desc);
	}
	
	/**
	 * 
	 * @param desc
	 * @return
	 */
	public static String toType(String desc) {
		
		char type = desc.charAt(0);
		if ( type == 'Z') { return "boolean"; }
		if ( type == 'C') { return "char"; }
		if ( type == 'B') { return "byte"; }
		if ( type == 'S') { return "short"; }
		if ( type == 'I') { return "int"; }
		if ( type == 'F') { return "flot"; }
		if ( type == 'J') { return "long"; }
		if ( type == 'D') { return "double"; }
		if ( type == 'V') { return "void"; }
		if ( type == 'L') { return  desc.substring(1).replace("/", ".").replace(";", "").trim(); }
		if ( type == '[') { return AsmClassVisitor.toArrayType(desc.substring(1)).trim(); }
		return Object.class.getName();
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	public static List<ParamaterData> getParametres(String param)
	{
	    List<ParamaterData> parameters = new ArrayList<ParamaterData>();
	    boolean on = false;
	    int array = 0;
	    StringBuffer buffer = new StringBuffer();
	    
	    for ( int ndx = 0; ndx < param.length(); ndx++ ) {
	    	if ( param.charAt(ndx) == 'L' && (! on) ) {
	    		on = true;
	    		buffer.delete(0, buffer.length());
	    	} else if  ( param.charAt(ndx) == ';') {
	    		on = false;
	    		
	    		String type =  buffer.toString().replace("/", ".").trim();
	    		parameters.add( new ParamaterData(type, array, false, null));
	    		array = 0;
	    	} else if ( param.charAt(ndx) == '[' ) {
	    		array++;
	    	} else if ( param.charAt(ndx) == '?' || param.charAt(ndx) == '*' ) {
	    		parameters.add( new ParamaterData(Object.class.getName(), array, false, null));
	    	} else {
	    		if ( on ) { 
	    			buffer.append(param.charAt(ndx));
	    		} else {
	    			buffer.append(param.charAt(ndx));
	    			
	    			String type = toType( buffer.toString().trim());
	    			parameters.add( new ParamaterData( type, array, true, null ));
	    			buffer.delete(0, buffer.length());
	    			array = 0;
	    		}
	    	}
	    }
	    return parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.ClassVisitor#visitField(int, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		
		ParamaterData data = null;
		
	    if ( signature != null ) {
	    	int start = signature.indexOf("<");
	    	int end = signature.indexOf(">");
			data = AsmClassVisitor.getParametres(signature.substring(start+1, end)).get(0); 
			
			List<String> list = new ArrayList<String>();
			list.add(data.getType());
			data = new ParamaterData(AsmClassVisitor.toType(desc), 0, false, list);
			
	    }  else {
	    	
			int array = 0;
			boolean primative = true;
			
			String type = desc.trim();
			if  ( desc.charAt(0) == '[') {
				boolean atEnd = false;
				for ( int ndx = 0; ndx < desc.length() && (! atEnd); ndx++) {
				   if ( desc.charAt(ndx) == '[') {
				      array++;
				   } else {
					   atEnd = true;
				   }
				}
				type = desc.substring(array);
			} 
			
			if ( type.charAt(0) == 'L') {
				primative = false;
			}
			
			data = new ParamaterData(AsmClassVisitor.toType(type), array, primative, null);
	    }
	    
		FieldData fld = new FieldData(access, name, data);
		refData.addField(fld);
		return super.visitField(access, name, desc, signature, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.ClassVisitor#visitMethod(int, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		
		ParamaterData returnType = null;
		List<ParamaterData> sigParms = null;
	    if ( signature != null ) {
	       SignatureReader reader = new SignatureReader(signature);
	       AsmSignatureVisitor visitor = new AsmSignatureVisitor(false);
	       reader.accept(visitor);
	       
	       returnType = visitor.getReturnType();
	       sigParms = visitor.getParamterData();

	    } else {
	       int index = desc.indexOf(')');
	       
	       String parm = desc.substring(index+1);
	       returnType = AsmClassVisitor.getParametres(parm).get(0);
	       parm = desc.substring(1, index);
	       sigParms = AsmClassVisitor.getParametres(desc.substring(1, index));
	    }
	      
		MethodData mth = new MethodData(access, name, sigParms, returnType, exceptions);
		refData.addMethod(mth);
		return new AsmMethodVisitor(mth);
	}

}
