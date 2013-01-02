package com.iai.ant.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class EcllipseProject {

	public static String projectTemplate[] = {
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
			"<projectDescription>",
			"	<name>#PROJECT#</name>",
			"	<comment></comment>",
			"	<projects>",
			"	</projects>",
			"	<buildSpec>",
			"		<buildCommand>",
			"			<name>org.eclipse.jdt.core.javabuilder</name>",
			"			<arguments>",
			"			</arguments>",
			"		</buildCommand>",
			"	</buildSpec>",
			"	<natures>",
			"		<nature>org.eclipse.jdt.core.javanature</nature>",
			"	</natures>",
			"</projectDescription>"
	};
	
	public static String properties[] = {
			"#Wed Jul 06 14:55:40 EDT 2011",
			"eclipse.preferences.version=1",
			"org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=enabled",
			"org.eclipse.jdt.core.compiler.codegen.targetPlatform=1.6",
			"org.eclipse.jdt.core.compiler.codegen.unusedLocal=preserve",
			"org.eclipse.jdt.core.compiler.compliance=1.6",
			"org.eclipse.jdt.core.compiler.debug.lineNumber=generate",
			"org.eclipse.jdt.core.compiler.debug.localVariable=generate",
			"org.eclipse.jdt.core.compiler.debug.sourceFile=generate",
			"org.eclipse.jdt.core.compiler.problem.assertIdentifier=error",
			"org.eclipse.jdt.core.compiler.problem.enumIdentifier=error",
			"org.eclipse.jdt.core.compiler.source=1.6",
	};
	
	/**
	 * 
	 * @param projectName
	 */
	public EcllipseProject(String projectName)
	{
	}
	
	/**
	 * 
	 * @param projectName
	 */
	public void writeProjectFile(String projectName)
	{		
		try {
			PrintWriter out = new PrintWriter(".project");
			
			for ( String line : EcllipseProject.projectTemplate ) {
				out.println( line.replace("#PROJECT#", projectName));
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param projectName
	 */
	public void writeSettingsFile(String projectName)
	{		
		File fp = new File(".settings");
		
		if ( ! fp.exists()) {
			fp.mkdir();
		}
		
		try {
			PrintWriter out = new PrintWriter(".settings/org.eclipse.jdt.core.prefs");
			
			for ( String line : EcllipseProject.properties ) {
				out.println(line);
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Help message.
	 */
	public static void help()
	{
		System.out.println(EcllipseClassPath.class.getName());
		System.out.println("\tproject:<path>    project name");
		System.exit(0);	
	}
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String project = null;
			
		for ( int ndx = 0; ndx < args.length; ndx++) {
			if ( args[ndx].startsWith("project:")) {
				
			   int index = args[ndx].indexOf(":");
			   project = args[ndx].substring(index+1);
					
			} else if ( args[ndx].startsWith("help")) {
				help();	
			}
		}
		
		EcllipseProject out = new EcllipseProject(project);
		
		out.writeProjectFile(project);
		out.writeSettingsFile(project);
		
	}

}
