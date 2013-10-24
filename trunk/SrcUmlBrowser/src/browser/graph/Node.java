package browser.graph;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import browser.loader.ClassReflectionData;
import browser.util.ApplicationSettings;


public class Node {

	public enum SHAPE { box, polygon, ellipse, circle, point, egg, oval,
						diamond, trapezium, parallelogram, house, pentagon,
						hexagon, septagon, octagon, doublecircle, doubleoctagon,
						tripleoctagon, invtriangle, invtrapezium, invhouse, Mdiamond,
						Msquare, Mcircle, rect, rectangle, square, none, note, tab,
						folder, box3e, component;
	
		public static SHAPE fromClass(ClassReflectionData cls) {
			if ( cls != null && cls.isInterface() ) {
				return SHAPE.oval;
			} else if ( cls != null && cls.isEnum() ) {
				return SHAPE.doubleoctagon;
			}
			return SHAPE.rect;
		}
	}
	
	private String name;
	private SHAPE shape = SHAPE.box;
	private Map<String, Edge> edge = new HashMap<String,Edge>();
	private int level = 0;
	
	/**
	 * 
	 * @param aName
	 * @param aShape
	 */
	public Node(String aName, Node.SHAPE aShape)
	{
		this.setName(aName);
		this.setShape(aShape);
	}
	
	/**
	 * 
	 * @param aName
	 * @param aShape
	 */
	public Node(String aName, Node.SHAPE aShape, int level)
	{
		this.setName(aName);
		this.setShape(aShape);
		this.level = level;
	}
	
	/**
	 * 
	 * @param edge
	 */
	public void addEdge( Edge edge)
	{
	    if ( edge != null && edge.getEdgeTo().getName().compareTo(getName()) != 0 ) {
	    	if ( this.edge.containsKey(edge.getEdgeTo().getName()) ) {
	    		Edge item = this.edge.get(edge.getEdgeTo().getName());
	    		if ( edge.getEdgeType().ordinal() > item.getEdgeType().ordinal() ) {
	    			item.setEdgeType( edge.getEdgeType());
	    		}
	    	} else {
	    		this.edge.put(edge.getEdgeTo().getName(), edge);
	    	}
	    }
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the shap
	 */
	public SHAPE getShape() {
		return shape;
	}
	
	/**
	 * @param shap the shap to set
	 */
	public void setShape(SHAPE shap) {
		this.shape = shap;
	}
	
	/**
	 * @return the edge
	 */
	public Collection<Edge> getEdge() {
		return edge.values();
	}
	
	/**
	 * @param edge the edge to set
	 */
	public void setEdge(List<Edge> edge) {
		this.edge.clear();
		for ( Edge e : edge ) {
			this.edge.put(e.getEdgeTo().getName(), e);
		}
	}
	
	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * 
	 * @param writer
	 */
	public void write(PrintWriter writer) {
		
		ApplicationSettings settings = ApplicationSettings.instance();
		
		String nodeName = getName().replace(".", "_").replace("$", "_");
		
		writer.print(nodeName);
		writer.print(" [");
		writer.print("label=\"");
		writer.print(getName());
		writer.print("\" shape=\"");
		writer.print(getShape().name());
		writer.print("\" ");
		if ( settings.getSettings().getGraphNodeColor() != null) {
			writer.print(" color=\"");
			writer.print( settings.getSettings().getGraphNodeColor() );
			writer.print("\" ");
			writer.print(" fillcolor=\"");
			writer.print( settings.getSettings().getGraphNodeColor() );
			writer.print("\" ");
			writer.print(" style = \"filled\"");
		}
		if ( settings.getSettings().getGraphFontColor() != null) {
			writer.print("labelfontcolor=\"");
			writer.print( settings.getSettings().getGraphFontColor());
			writer.print("\" ");
		}
		writer.println("];");
	}
	
	/**
	 * 
	 * @param writer
	 */
	public void writeEdges(PrintWriter writer)
	{
		for ( Edge e : getEdge()) {
			writer.print("\t");
			e.write(this, writer);
		}
	}

	
}
