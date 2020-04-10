package browser.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import browser.loader.ClassReflectionData;
import browser.loader.FieldData;
import browser.loader.MethodData;
import browser.loader.ParamaterData;
import browser.loader.ScannedClassLoader;

public class ClassXRef {
	
	private static final String DUMPFILE = "/classXRef.txt";
	
	private Log log = LogFactory.getLog(ClassXRef.class);

	private Map<String,Set<PackageAssociation>> packRef = new HashMap<String,Set<PackageAssociation>>();
	private Map<String,Set<Association>> usesRef = new HashMap<String,Set<Association>>();
	private Map<String,Set<ClassReflectionData>> inheritRef = new HashMap<String,Set<ClassReflectionData>>();
	private ScannedClassLoader _loader = null;
	
	/**
	 * 
	 * @param loader
	 */
	public ClassXRef(ScannedClassLoader loader) {
		this.init(loader, loader.getClassList());
	}
	
	/**
	 * @return the packRef
	 */
	public Map<String, Set<PackageAssociation>> getPackRef() {
		return packRef;
	}

	/**
	 * @return the packRef
	 */
	public Set<PackageAssociation> getPackRef(String name) {
		if ( getPackRef().containsKey(name )) {
			return getPackRef().get(name);
		}
		return null;
	}
	
	/**
	 * @return the usesRef
	 */
	public Map<String, Set<ClassXRef.Association>> getUsesRef() {
		return usesRef;
	}

	/**
	 * @return the inheritRef
	 */
	public Map<String, Set<ClassReflectionData>> getInheritRef() {
		return inheritRef;
	}
	
	public Set<ClassReflectionData> getInheritRef(ClassReflectionData cls) 
	{
		if ( getInheritRef().containsKey(cls.getName() )) {
			return getInheritRef().get(cls.getName());
		}
		return null;
	}
	
	/**
	 * 
	 * @param cls
	 * @return
	 */
	public Set<ClassXRef.Association> getUsesClasstRef(ClassReflectionData cls) 
	{
		if ( getUsesRef().containsKey(cls.getName() )) {
			return getUsesRef().get(cls.getName());
		}
		return null;
	}
	
	/**
	 * 
	 * @param loader
	 * @param classList
	 */
	protected void init(ScannedClassLoader loader, List<String> classList)
	{
		this._loader = loader;
		for ( String name : classList )  {
			try {
				ClassReflectionData cls = loader.load(name);
				processClass(cls);
			} catch (Exception e) {
				log.error("Unable to load class: " + name + " " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		this.dump(System.getProperty("java.io.tmpdir") + DUMPFILE);
			
	}
	
	public void dump(String fileName) {
		
		try {
			PrintWriter writer = new PrintWriter(fileName);
			
			writer.println("Package XREF:");
			writer.println("----------------------------");
			writer.println();
			
			for ( Entry<String,Set<PackageAssociation>> entry : packRef.entrySet()) {
				
				writer.println(entry.getKey() + " -----> ");
				for ( PackageAssociation item : entry.getValue()) {
					writer.println("\t\t" + item.dumpString());
				}
			}
			
			writer.println();
			writer.println("----------------------------");
			writer.println("----------------------------");
			writer.println("----------------------------");
			writer.println("Association XREF:");
			writer.println("----------------------------");
			writer.println();
			
			
			for ( Entry<String,Set<Association>> entry : usesRef.entrySet()) {
				
				writer.println(entry.getKey() + " -----> ");
				for ( Association item : entry.getValue()) {
					writer.println("\t\t" + item.dumpString());
				}
			}
			
			writer.println();
			writer.println("----------------------------");
			writer.println("----------------------------");
			writer.println("----------------------------");
			writer.println("Inherit XREF:");
			writer.println("----------------------------");
			writer.println();
			
			
			for ( Entry<String,Set<ClassReflectionData>> entry : this.inheritRef.entrySet()) {
				
				writer.println(entry.getKey() + " -----> ");
				for ( ClassReflectionData item : entry.getValue()) {
					writer.println("\t\t" + item.getName());
				}
			}
			
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param aClass
	 * @return
	 */
	protected String getPackageName(Class<?> aClass)
	{		
		String className = aClass.getName();
		int index = className.lastIndexOf(".");
		return className.substring(0, index);
	}
	
	/**
	 * 
	 * @param fromClass
	 * @param toClass
	 */
	protected void addPackageRef(ClassReflectionData fromClass, ClassReflectionData toClass, boolean inherit, boolean use )
	{
		String formPackage = fromClass.getPackagePath();
		String toPackage = toClass.getPackagePath();
		
		if ( ! getPackRef().containsKey(toPackage) ) {
			getPackRef().put(toPackage, new HashSet<PackageAssociation>() );
		}
		
		Set<PackageAssociation> map = getPackRef().get(toPackage);
		
		boolean found = false;
	    for ( PackageAssociation assoc : map ) {
			if ( assoc.getPackageName().compareTo(formPackage) == 0) {
				assoc.setInheritance(assoc.isInheritance() || inherit);
				assoc.setRelation(assoc.isRelation() || use);
				found = true;
			}
		}
	    
	    if ( ! found ) {
	    	getPackRef().get(toPackage).add(new PackageAssociation(formPackage, inherit, use));
	    }
	}
	
	/**
	 * 
	 * @param aClass
	 * @return
	 */
	protected boolean keepReference(ClassReflectionData aClass)
	{	
	    if ( aClass.getName().startsWith("$Proxy") ) {
			return false;
		} else if ( aClass.getName().startsWith("java") ) {
			return false;
		}
		return true;
	}
	
	private boolean hasReferance(ClassReflectionData aClass, Set<ClassXRef.Association> set)
	{
		for (ClassXRef.Association ref : set ) {
			if ( ref.getClass().getName().compareTo(aClass.getName()) == 0 ) {
				return true;
			}
		}
		return false;
	}
			
	/**
	 * 
	 * @param fromClass
	 * @param toClass
	 */
	protected void addReference(ClassReflectionData fromClass, List<ClassReflectionData> toClassList, boolean isField)
	{
		if ( ! this.getUsesRef().containsKey(fromClass.getName())) {
			getUsesRef().put(fromClass.getName(), new HashSet<ClassXRef.Association>() );
		}
		
		for ( ClassReflectionData toClass : toClassList ) {
			 if ( this.keepReference(toClass) ) {
 				if ( toClass.getName().startsWith("[") ) {
 					log.info("single adding: " + toClass.getName() + " " + toClass.toString());
 				}
 				
 				if ( ! this.hasReferance(toClass, getUsesRef().get(fromClass.getName()))) {
				   getUsesRef().get(fromClass.getName()).add( new Association(toClass,isField));
				   this.addPackageRef(toClass, fromClass, false, true);
 				}
			}
		}
	}
	
	/**
	 * 
	 * @param fromClass
	 * @param toClassList
	 */
	protected void addInherit(ClassReflectionData fromClass, ClassReflectionData toClass)
	{
		if ( ! this.getInheritRef().containsKey(fromClass.getName())) {
			getInheritRef().put(fromClass.getName(), new HashSet<ClassReflectionData>() );
		}
		
		getInheritRef().get(fromClass.getName()).add(toClass);
		this.addPackageRef(fromClass, toClass, true, false);
	}
	
	/**
	 * 
	 */
	private Set<String> error = new HashSet<String>();
	
	private ClassReflectionData getClassRef(String parm) {
		
		if ( parm == null )  {
			return null;
		}
		
		ClassReflectionData ref = this._loader.load(parm);
		if ( ref == null && (! parm.startsWith("java")) && (! error.contains(parm)) ) {
			System.err.println("null ref: " + parm);
			error.add(parm);
		}
		return ref;
	}
		
	/**
	 * Process a class for its package, class and inheritance references.
	 * @param cls
	 */
	protected void processClass(ClassReflectionData cls)
	{
		/**
		 * Discover class reference from fields
		 */
		
		List<ClassReflectionData> aList = new ArrayList<ClassReflectionData>();
		ClassReflectionData ref = null;
		
		for ( FieldData field : cls.getFields() ) {
			ref = this.getClassRef(field.getType());
			if ( ref != null  && (! aList.contains(ref)) ) {
				aList.add(ref);
			} 
			
			if ( field.isGeneric() ) {
				for ( String types : field.getGenericTypes() ) {
					ref = this.getClassRef(types);
					if ( ref != null  && (! aList.contains(ref)) ) {
						aList.add(ref);
					} 
				}
			}
		}
	
		/**
		 * Discover class reference from methods
		 */
		for ( MethodData method : cls.getMethods()) { 
			
			ref = this.getClassRef(method.getRetrunType().getType());
			if ( ref != null && (! aList.contains(ref)) ) {
				aList.add(ref);
			} 
			
			if ( method.getRetrunType().isGeneric() ) {
				for ( String item : method.getRetrunType().getGenericTypes()) {
					ref = this.getClassRef(item);
					if ( ref != null  && (! aList.contains(ref)) ) {
						aList.add(ref);
					} 
				}
			}
			
			for ( ParamaterData parm : method.getParameters() ) {
				ref = this.getClassRef(parm.getType());
				if ( ref != null  && (! aList.contains(ref)) ) {
					aList.add(ref);
				} 
				if ( parm.isGeneric() ) {
					for ( String item : parm.getGenericTypes()) {
						ref = this.getClassRef(item);
						if ( ref != null  && (! aList.contains(ref)) ) {
							aList.add(ref);
						} 
					}
				}
			}
			
			for( String var : method.getLocalvarsType()) {
				ref = this.getClassRef(var);
				if ( ref != null  && (! aList.contains(ref)) ) {
					aList.add(ref);
				} 
			}			
		}
		
		addReference(cls, aList, true);
				 
		if ( cls.getAnnotations().size() > 0 ) {
			aList.clear();
			for ( String item : cls.getAnnotations()) {
				ref = this.getClassRef(item);
				if ( ref != null  && (! aList.contains(ref)) ) {
					aList.add(ref);
				} 
			}
			if ( ! aList.isEmpty() ) {
			   addReference(cls, aList, false);
			}
		}
		
		
		/**  
		 * The Inheritance/interfaces
		 */
		if ( cls.getSuperClass() != null ) {
			ClassReflectionData sc = this._loader.load(cls.getSuperClass());
			if (sc != null ) {
				this.addInherit(sc, cls);
			}
		}
		
		for ( String implment : cls.getInterfaces()) {
			ClassReflectionData sc = this._loader.load(implment);
			if ( sc != null ) {
				this.addInherit(sc, cls);
			}
		}
	
		return;
	}
		
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////

	public class Association {
		
		private ClassReflectionData theClass = null;
		private boolean isField = false;
		
		public Association(ClassReflectionData aClass, boolean field) {
			theClass = aClass;
			isField = field;
		}

		/**
		 * @return the theClass
		 */
		public ClassReflectionData getTheClass() {
			return theClass;
		}

		/**
		 * @return the isField
		 */
		public boolean isField() {
			return isField;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			return this.toString().equals(obj);
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return this.toString().hashCode();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.getTheClass().getName();
		}
		
		/**
		 * 
		 * @return
		 */
		public String dumpString() {
			return this.toString() + " FLD: " + this.isField();
		}
			
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////

	public class PackageAssociation {
		
		private String packageName = null;
		private boolean using = false;
		private boolean inheritance = false;
		private boolean relation = false;
		
		public PackageAssociation(String aClass, boolean inherit, boolean use) {
			packageName = aClass;
			inheritance = inherit;
			using = use;
		}

		/**
		 * @return the theClass
		 */
		public String getPackageName() {
			return packageName;
		}
		
		/**
		 * @return the using
		 */
		public boolean isUsing() {
			return using;
		}

		/**
		 * @param using the using to set
		 */
		public void setUsing(boolean using) {
			this.using = using;
		}

		/**
		 * @return the inheritance
		 */
		public boolean isInheritance() {
			return inheritance;
		}

		/**
		 * @param inheritance the inheritance to set
		 */
		public void setInheritance(boolean inheritance) {
			this.inheritance = inheritance;
		}

		/**
		 * @return the relation
		 */
		public boolean isRelation() {
			return relation;
		}

		/**
		 * @param relation the relation to set
		 */
		public void setRelation(boolean relation) {
			this.relation = relation;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			return this.toString().equals(obj);
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return this.toString().hashCode();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.getPackageName();
		}
		
		public String dumpString() {
			return this.toString() + " SC: " + this.isInheritance() + " USE: " + this.isRelation();
		}
			
	}


}
