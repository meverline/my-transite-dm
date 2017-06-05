package browser.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import browser.gui.dialog.SearchDialog;
import browser.loader.ClassReflectionData;
import browser.util.ApplicationSettings;
import browser.util.ClassXRef;
import browser.util.Project;

public class GraphBuilder {

	public enum TEXT {
		CONTAINS {
			public boolean matchs(String name, String text) {
				return name.contains(text);
			}
		},
		REGEXP {
			public boolean matchs(String name, String text) {
				Pattern p = Pattern.compile(text);
				Matcher m = p.matcher(name);
				return m.matches();
			}
		},
		STARTS_WITH {
			public boolean matchs(String name, String text) {
				return name.startsWith(text);
			}
		};

		public abstract boolean matchs(String lhs, String rhs);
	};

	private Project project = null;
	private ClassXRef xref = null;
	private boolean keepPackageName = true;
	private String matchString = "";
	private TEXT matchType = TEXT.CONTAINS;
	private String filterSearchText = null;
	private int maxPackageDepth = Integer.MAX_VALUE;

	/**
	 * 
	 * @param project
	 * @param xref
	 */
	public GraphBuilder(Project project, ClassXRef xref) {

		this.project = project;
		this.xref = xref;
	}
	
	/**
	 * 
	 * @param project
	 * @param xref
	 */
	public GraphBuilder(Project project, ClassXRef xref, SearchDialog dialog) {

		this(project, xref);
		this.setMatchString(dialog.getSearchText());
		this.setMatchType(dialog.getMatchType());
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxPackageDepth() {
		return maxPackageDepth;
	}

	/**
	 * 
	 * @param maxPackageDepth
	 */
	public void setMaxPackageDepth(int maxPackageDepth) {
		this.maxPackageDepth = maxPackageDepth;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isKeepPackageName() {
		return keepPackageName;
	}

	/**
	 * 
	 * @param keepPackageName
	 */
	public void setKeepPackageName(boolean keepPackageName) {
		this.keepPackageName = keepPackageName;
	}

	/**
	 * 
	 * @return
	 */
	public String getMatchString() {
		return matchString;
	}

	/**
	 * 
	 * @param matchString
	 */
	protected void setMatchString(String matchString) {
		this.matchString = matchString;
	}

	/**
	 * 
	 * @return
	 */
	public TEXT getMatchType() {
		return matchType;
	}

	/**
	 * 
	 * @param matchType
	 */
	protected void setMatchType(TEXT matchType) {
		this.matchType = matchType;
	}

	/**
	 * 
	 * @return
	 */
	public String getFilterSearchText() {
		return filterSearchText;
	}

	/**
	 * 
	 * @param filterSearchText
	 */
	protected void setFilterSearchText(String filterSearchText) {
		this.filterSearchText = filterSearchText;
	}
	
	/**
	 * 
	 * @param baseName
	 * @return
	 */
	public String getGraphName(String baseName) {
		
		StringBuilder rtn = new StringBuilder(baseName);
		
		rtn.append("_mt_");
		rtn.append(this.getMatchType().name());
		if ( this.getMaxPackageDepth() != Integer.MAX_VALUE) {
			rtn.append("_md_");
			rtn.append( this.getMaxPackageDepth());
		}
		if ( this.getMatchString() != null ) {
			rtn.append("_ms_");
			rtn.append( this.getMatchString());
		}
		if ( this.getFilterSearchText() != null ) {
			rtn.append("_fst_");
			rtn.append( this.getFilterSearchText());
		}

		rtn.append("_kpn_");
		rtn.append( this.isKeepPackageName());
	
		return rtn.toString();
	}

	/**
	 * 
	 * @param className
	 * @return
	 */
	public String stripPackage(String className) {
		String rtn = className;

		if (!this.keepPackageName && className.startsWith(matchString)) {
			matchString = matchString.substring(0, matchString.lastIndexOf("."));
			rtn = className.substring(matchString.length() + 1);
		}
		return rtn;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected boolean showClass(String name) {
		if (project.getPackageRoot() == null) {
			if (name.startsWith("java") || name.startsWith("javax")) {
				return false;
			}
			return true;
		}

		String allowed[] = project.getPackageRoot().split(";");

		for (String packageRoot : allowed) {
			if (name.startsWith(packageRoot)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param className
	 * @return
	 */
	public boolean filter(String className) {
		boolean rtn = false;
		if (filterSearchText != null) {
			rtn = true;
			if ((!matchString.isEmpty()) && matchType.matchs(className, matchString)) {
				rtn = false;
			} else {
				rtn = true;
			}
		}
		return rtn;
	}

	/**
	 * 
	 * @param set
	 * @param depth
	 * @param g
	 * @param root
	 * @param visted
	 */
	public void addToClassInteractionGraph(Set<ClassXRef.Association> set, int depth, GraphVizGraph g,
										   GraphVizNode root, HashSet<String> visted) {
		if (set == null) {
			return;
		}

		ApplicationSettings settings = ApplicationSettings.instance();
		GraphVizGraph.DEPTH maxDepth = GraphVizGraph.DEPTH.valueOf(settings.getSettings().getGraphDepth());

		if (depth > maxDepth.depth()) {
			return;
		}

		for (ClassXRef.Association item : set) {
			if (this.showClass(item.toString())) {
				GraphVizNode node = g.getNodeByName(item.toString());
				if (node == null) {
					node = new GraphVizNode(item.toString(), GraphVizNode.SHAPE.fromClass(item.getTheClass()), 0);
					g.add(node);
				}

				if (item.isField()) {
					root.addEdge(new GraphVizEdge(node, GraphVizEdge.TYPE.none));
				} else {
					root.addEdge(new GraphVizEdge(node, GraphVizEdge.TYPE.dot));
				}
				if (!visted.contains(item.toString())) {
					visted.add(item.toString());
					this.addToClassInteractionGraph(xref.getUsesClasstRef(item.getTheClass()), depth + 1, g, node,
													visted);
				}
			}
		}
	}

	/**
	 * 
	 * @param set
	 * @param depth
	 * @param g
	 * @param root
	 * @param visted
	 * @param invert
	 */
	public void addToInheritanceGraph(Collection<ClassReflectionData> set, int depth, GraphVizGraph g,
									  GraphVizNode root, HashSet<String> visted, boolean invert) {
		if (set == null) {
			return;
		}

		ApplicationSettings settings = ApplicationSettings.instance();
		GraphVizGraph.DEPTH maxDepth = GraphVizGraph.DEPTH.valueOf(settings.getSettings().getGraphDepth());

		if (depth > maxDepth.depth()) {
			return;
		}

		for (ClassReflectionData item : set) {
			if (this.showClass(item.getName())) {
				String name = this.stripPackage(item.getName());
				GraphVizNode node = g.getNodeByName(name);
				if (node == null) {
					node = new GraphVizNode(name, GraphVizNode.SHAPE.fromClass(item), depth);
					g.add(node);
				}
				if (invert) {
					node.addEdge(new GraphVizEdge(root, GraphVizEdge.TYPE.normal));
				} else {
					node.addEdge(new GraphVizEdge(root, GraphVizEdge.TYPE.normal));
				}
				if (!visted.contains(item.getName())) {
					visted.add(item.getName());
					this.addToInheritanceGraph(xref.getInheritRef(item), depth + 1, g, node, visted, invert);
				}
			}
		}
	}

	/**
	 * 
	 * @param set
	 * @param depth
	 * @param g
	 * @param root
	 * @param visted
	 */
	public void addToPackageGraph(Set<ClassXRef.PackageAssociation> set, int depth, GraphVizGraph g, GraphVizNode root,
								  HashSet<String> visted) {
		if (set == null) {
			return;
		}

		ApplicationSettings setting = ApplicationSettings.instance();
		GraphVizGraph.DEPTH maxDepth = GraphVizGraph.DEPTH.valueOf(setting.getSettings().getGraphDepth());

		if (depth > maxDepth.depth()) {
			return;
		}

		for (ClassXRef.PackageAssociation item : set) {
			String name = this.stripPackage(item.getPackageName());
			if (this.showClass(item.getPackageName()) && (!filter(item.getPackageName()))) {
				GraphVizNode node = g.getNodeByName(name);
				if (node == null) {
					node = new GraphVizNode(name, GraphVizNode.SHAPE.folder, depth);
					g.add(node);
				}
				if (item.isInheritance() && (!item.isRelation())) {
					root.addEdge(new GraphVizEdge(node, GraphVizEdge.TYPE.normal));
				} else if ((!item.isInheritance()) && item.isRelation()) {
					node.addEdge(new GraphVizEdge(root, GraphVizEdge.TYPE.dot));
				} else if ((!item.isInheritance()) && (!item.isRelation())) {
					root.addEdge(new GraphVizEdge(node, GraphVizEdge.TYPE.none));
				} else {
					root.addEdge(new GraphVizEdge(node, GraphVizEdge.TYPE.normal, GraphVizGraph.COLOR.red));
				}
				if (!visted.contains(name)) {
					visted.add(name);
					this.addToPackageGraph(xref.getPackRef(item.getPackageName()), depth + 1, g, node, visted);
				}
			}
		}
	}

}
