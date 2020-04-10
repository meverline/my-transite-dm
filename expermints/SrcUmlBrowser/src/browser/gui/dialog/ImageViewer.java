package browser.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Graph;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;

@SuppressWarnings("serial")
public class ImageViewer extends JPanel {

	private static final String graph = "graph";
	private static final String nodes = "graph.nodes";
	private static final String edges = "graph.edges";

	private Visualization m_vis;

    public ImageViewer(Graph g, String label)   {
       super(new BorderLayout());
       // create a new, empty visualization for our data
       m_vis = new Visualization();
       m_vis.addGraph(label, g);

       // --------------------------------------------------------------------
       // set up the renderers
       
       LabelRenderer tr = new LabelRenderer();
       tr.setRoundedCorner(8, 8);
       m_vis.setRendererFactory(new DefaultRendererFactory(tr));

       // --------------------------------------------------------------------
       // register the data with a visualization
       
       // adds graph to visualization and sets renderer label field
       //setGraph(g, label);
            
       // --------------------------------------------------------------------
       // create actions to process the visual data

       int hops = 30;
       final GraphDistanceFilter filter = new GraphDistanceFilter(graph, hops);

       ColorAction fill = new ColorAction(nodes, 
               VisualItem.FILLCOLOR, ColorLib.rgb(200,200,255));
       fill.add(VisualItem.FIXED, ColorLib.rgb(255,100,100));
       fill.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255,200,125));
       
       ActionList draw = new ActionList();
       draw.add(filter);
       draw.add(fill);
       draw.add(new ColorAction(nodes, VisualItem.FILLCOLOR, 0));
       draw.add(new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0)));
       draw.add(new ColorAction(edges, VisualItem.FILLCOLOR, ColorLib.gray(200)));
       draw.add(new ColorAction(edges, VisualItem.STROKECOLOR, ColorLib.gray(200)));
       
       ActionList animate = new ActionList(Activity.INFINITY);
       animate.add(new ForceDirectedLayout(graph));
       animate.add(fill);
       animate.add(new RepaintAction());
       
       // finally, we register our ActionList with the Visualization.
       // we can later execute our Actions by invoking a method on our
       // Visualization, using the name we've chosen below.
       m_vis.putAction("draw", draw);
       m_vis.putAction("layout", animate);
       m_vis.runAfter("draw", "layout");
       
       
       // --------------------------------------------------------------------
       // set up a display to show the visualization
       
       Display display = new Display(m_vis);
       display.setSize(700,700);
       display.pan(350, 350);
       display.setForeground(Color.GRAY);
       display.setBackground(Color.WHITE);
       
       // main display controls
       display.addControlListener(new DragControl());
       display.addControlListener(new PanControl());
       display.addControlListener(new ZoomControl());
       display.addControlListener(new WheelZoomControl());
       
       display.setForeground(Color.GRAY);
       display.setBackground(Color.WHITE);
       
       // --------------------------------------------------------------------        
       // launch the visualization
       // now we run our action list
       m_vis.run("draw");
       
   }
   
   public void setGraph(Graph g, String label) {
       // update labeling
       DefaultRendererFactory drf = (DefaultRendererFactory)
                                               m_vis.getRendererFactory();
       ((LabelRenderer)drf.getDefaultRenderer()).setTextField(label);
       
       // update graph
       m_vis.removeGroup(graph);
       VisualGraph vg = m_vis.addGraph(graph, g);
       m_vis.setValue(edges, null, VisualItem.INTERACTIVE, Boolean.FALSE);
       VisualItem f = (VisualItem)vg.getNode(0);
       m_vis.getGroup(Visualization.FOCUS_ITEMS).setTuple(f);
       f.setFixed(false);
   }
   
   @Override
   protected void paintComponent(Graphics g) {
       super.paintComponent(g);
       m_vis.run("draw");         
   }

}
