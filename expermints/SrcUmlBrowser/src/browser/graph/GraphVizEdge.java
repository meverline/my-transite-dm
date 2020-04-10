package browser.graph;


import java.io.PrintWriter;

public class GraphVizEdge {

	public enum TYPE { none, normal, tee, vee, box, crow, diamond, dot, inv };
	
	private TYPE edgeType = TYPE.none;
	private GraphVizNode edgeTo = null;
	private GraphVizGraph.COLOR color = null;
	
	public GraphVizEdge( GraphVizNode to, GraphVizEdge.TYPE type)
	{
		this.setEdgeTo(to);
		this.setEdgeType(type);
	}
	
	public GraphVizEdge( GraphVizNode to, GraphVizEdge.TYPE type, GraphVizGraph.COLOR color)
	{
		this.setEdgeTo(to);
		this.setEdgeType(type);
		this.color = color;
	}
	
	/**
	 * @return the edgeType
	 */
	public TYPE getEdgeType() {
		return edgeType;
	}
	
	/**
	 * @param edgeType the edgeType to set
	 */
	public void setEdgeType(TYPE edgeType) {
		this.edgeType = edgeType;
	}
	
	/**
	 * @return the edgeTo
	 */
	public GraphVizNode getEdgeTo() {
		return edgeTo;
	}
	
	/**
	 * @param edgeTo the edgeTo to set
	 */
	public void setEdgeTo(GraphVizNode edgeTo) {
		this.edgeTo = edgeTo;
	}
	
	/**
	 * 
	 * @param fromNode
	 * @param writer
	 */
	public void write(GraphVizNode fromNode, PrintWriter writer) {
		
		String fromName = fromNode.getName().replace(".", "_").replace("$", "_");
		String toName = getEdgeTo().getName().replace(".", "_").replace("$", "_");
		
		writer.print(fromName);
		writer.print(" -> ");
		writer.print( toName);
		writer.print(" [ arrowhead=");
		writer.print( getEdgeType().name());
		if ( color != null ) {
			writer.print(" color=");
			writer.print( this.color.name() );
		}
		writer.println( "];");

    }

}
