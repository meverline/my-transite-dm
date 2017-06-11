/**
 * 
 */
package browser.gui.dialog;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import browser.graph.GraphVizGraph;
import browser.gui.commands.CloseDialogAction;
import browser.gui.commands.FileSelectionCommand;
import browser.gui.layout.SpringLayoutUtilities;
import browser.util.ApplicationSettings;

/**
 * @author markeverline
 *
 */
public class SettingsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5011439039769348845L;
	
	private Log log = LogFactory.getLog(SettingsDialog.class);
	private List<SettingsTab> aList = new ArrayList<SettingsTab>();

	/**
	 * 
	 * @param frame
	 */
	public SettingsDialog(JFrame frame)
	{
		super(frame);
		build();
		setSize(750,300);
		setTitle("Application Settings");
		this.setLocationByPlatform(true);
		this.setVisible(true);
		setLocationRelativeTo(frame);
	}
	
	/**
	 * 
	 */
	private void build()
	{
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());
		
		JTabbedPane tabpane = new JTabbedPane();
		panel.add(tabpane, BorderLayout.CENTER);
		
		GraphVizSettings gzs = new GraphVizSettings(this);
		aList.add(gzs);
		tabpane.add( gzs, gzs.getLabel());
		
		ScanSettings ss = new ScanSettings(this);
		aList.add(ss);
		tabpane.add(ss, ss.getLabel());
		
		GraphColors gc = new GraphColors(this);
		aList.add(gc);
		tabpane.add(gc, gc.getLabel());
		
		JPanel buttonBox = new JPanel();
		
		JButton applyButton = new JButton("Apply");
		buttonBox.add(applyButton);
		applyButton.addActionListener( new SaveActionListener() );
		applyButton.addActionListener( new CloseDialogAction(this));
		
		JButton close = new JButton("Cancel");
		close.addActionListener( new CloseDialogAction(this));
		buttonBox.add(close);
		
		panel.add(buttonBox, BorderLayout.SOUTH);
		
		this.setContentPane(panel);
	}
	
	/**
	 * 
	 */
	public void save()
	{
		for ( SettingsTab tab : aList) {
			tab.save();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public class SaveActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			save();	
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	private interface SettingsTab {	
		public void save();
		public String getLabel();
	}
	

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	private class GraphColors extends JPanel implements SettingsTab {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JList<GraphVizGraph.COLOR> font = null;
		private JList<GraphVizGraph.COLOR> node = null;
		
		public GraphColors(JDialog dialog)
		{
			ApplicationSettings set = ApplicationSettings.instance();
			
			this.setLayout( new BorderLayout());
			JSplitPane pane = new JSplitPane();
			pane.setOrientation(JSplitPane.HORIZONTAL_SPLIT); 
			pane.setDividerLocation(0.5);
			this.add(pane, BorderLayout.CENTER);
			
			JPanel left = new JPanel();
			left.setLayout( new BorderLayout());
			left.add( new JLabel("Font"), BorderLayout.NORTH);
			
			font = new JList<GraphVizGraph.COLOR>( GraphVizGraph.COLOR.values());
			left.add( new JScrollPane(font), BorderLayout.CENTER);
			if ( set.getSettings().getGraphFontColor() != null) {
				GraphVizGraph.COLOR color = GraphVizGraph.COLOR.valueOf(set.getSettings().getGraphFontColor());
				font.setSelectedValue(color, true);
			}
			
			JPanel right = new JPanel();
			right.setLayout( new BorderLayout());
			right.add( new JLabel("Node"), BorderLayout.NORTH);
			
			node = new JList<GraphVizGraph.COLOR>( GraphVizGraph.COLOR.values());
			right.add( new JScrollPane(node), BorderLayout.CENTER);
			if ( set.getSettings().getGraphNodeColor() != null) {
				GraphVizGraph.COLOR color = GraphVizGraph.COLOR.valueOf(set.getSettings().getGraphNodeColor());
				node.setSelectedValue(color, true);
			}
				
			pane.setLeftComponent(left);
			pane.setRightComponent(right);
			
		}

		public String getLabel() {
			return "Graph Colors";
		}

		public void save() {
			ApplicationSettings set = ApplicationSettings.instance();

			set.getSettings().setGraphFontColor(font.getSelectedValue().toString());
			set.getSettings().setGraphNodeColor(node.getSelectedValue().toString());
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	private class GraphVizSettings extends JPanel implements SettingsTab {
		
		private static final long serialVersionUID = 1395083596310938199L;
		private JComboBox<GraphVizGraph.OUTPUT> formatBox = null;
		private JComboBox<GraphVizGraph.CMD> commandBox = null;
		private JComboBox<GraphVizGraph.DEPTH> depthBox = null;
		private JTextField homePath = null;
		
		public GraphVizSettings(JDialog dialog)
		{
			ApplicationSettings set = ApplicationSettings.instance();
			
			BufferedImage icon = null;
			try {
				File fp = new File("icons/folder_find.png");
				icon = ImageIO.read(fp);
			} catch (IOException e) {
				log.error(e);
			}
			
			JPanel panel = new JPanel();
			this.add(panel);
			
			panel.setLayout( new SpringLayout());
			
			panel.add( new JLabel("Graph Viz Home"));
			homePath = new JTextField();
			homePath.setColumns(40);
			
			if ( set.getSettings().getGraphHome() != null) {
				homePath.setText(set.getSettings().getGraphHome() );
			}
			panel.add(homePath);
			JButton button;
			if ( icon != null ) {
			   button = new JButton( new ImageIcon(icon));
			} else { 
			   button = new JButton("Browse...");
			}
			button.addActionListener( new FileSelectionCommand(homePath, dialog, true));
			panel.add(button);
			
			panel.add( new JLabel("Graph Viz Command"));
			commandBox = new JComboBox<GraphVizGraph.CMD>( GraphVizGraph.CMD.values());
			if ( set.getSettings().getGraphProgram() != null) {
				GraphVizGraph.CMD cmd = GraphVizGraph.CMD.valueOf(set.getSettings().getGraphProgram());
				commandBox.setSelectedItem(cmd);
			}
			panel.add(commandBox);
			panel.add( new JLabel(""));
			
			panel.add( new JLabel("Graph Viz Format"));
			formatBox = new JComboBox<GraphVizGraph.OUTPUT>( GraphVizGraph.OUTPUT.values());
			if ( set.getSettings().getGraphOutput() != null) {
				GraphVizGraph.OUTPUT cmd = GraphVizGraph.OUTPUT.valueOf(set.getSettings().getGraphOutput());
				formatBox.setSelectedItem(cmd);
			}
			panel.add(formatBox);
			panel.add( new JLabel(""));
			
			panel.add( new JLabel("Graph Depth"));
			depthBox = new JComboBox<GraphVizGraph.DEPTH>( GraphVizGraph.DEPTH.values());
			if ( set.getSettings().getGraphDepth() != null) {
				GraphVizGraph.DEPTH cmd = GraphVizGraph.DEPTH.valueOf(set.getSettings().getGraphDepth());
				depthBox.setSelectedItem(cmd);
			}
			panel.add(depthBox);
			panel.add( new JLabel(""));
						
			SpringLayoutUtilities.makeCompactGrid(panel, 4, 3, 2, 2, 5, 5);
			
		}
		
		public void save() {
			ApplicationSettings set = ApplicationSettings.instance();
			
			set.getSettings().setGraphHome(homePath.getText());
			set.getSettings().setGraphOutput(formatBox.getSelectedItem().toString());
			set.getSettings().setGraphProgram( ((GraphVizGraph.CMD)commandBox.getSelectedItem()).name() );
			set.getSettings().setGraphDepth( depthBox.getSelectedItem().toString());	
		}

		public String getLabel() {
			return "Graph Viz";
		}
		
	}
	
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	private class ScanSettings extends JPanel implements SettingsTab {
		
		private static final long serialVersionUID = 1395083596310938199L;
		private JTextField srcBuildPath = null;
		private JTextField classBuildPath = null;
	
		public ScanSettings(JDialog dialog)
		{
			ApplicationSettings set = ApplicationSettings.instance();
			
//			BufferedImage icon = null;
//			try {
//				File fp = new File("icons/folder_find.png");
//				icon = ImageIO.read(fp);
//			} catch (IOException e) {
//				log.error(e);
//			}
			
			JPanel panel = new JPanel();
			this.add(panel);
			
			panel.setLayout( new SpringLayout());
			
			panel.add( new JLabel("Src Path"));
			srcBuildPath = new JTextField();
			srcBuildPath.setColumns(40);
			if ( set.getSettings().getSrcPath() != null)
			{
				String path[] = set.getSettings().getSrcPath();
				StringBuilder bld = new StringBuilder();
				for (int ndx=0; ndx < path.length; ndx++) {
					if ( ndx != 0) { bld.append(";"); }
					bld.append(path[ndx]);
				}
				srcBuildPath.setText(bld.toString());
			}
			panel.add( srcBuildPath );
			panel.add( new JLabel() );
			
			panel.add( new JLabel("Build Path"));
			classBuildPath = new JTextField();
			classBuildPath.setColumns(40);
			if ( set.getSettings().getClassBuildPath() != null)
			{
				String path[] = set.getSettings().getClassBuildPath();
				StringBuilder bld = new StringBuilder();
				for (int ndx=0; ndx < path.length; ndx++) {
					if ( ndx != 0) { bld.append(";"); }
					bld.append(path[ndx]);
				}
				classBuildPath.setText(bld.toString());
			}
			panel.add( classBuildPath );
			panel.add( new JLabel() );
						
			SpringLayoutUtilities.makeCompactGrid(panel, 2, 3, 2, 2, 5, 5);
			
		}
		
		public void save() {
			ApplicationSettings set = ApplicationSettings.instance();
			
			set.getSettings().setClassBuildPath(classBuildPath.getText().split(";"));
			set.getSettings().setSrcPath(srcBuildPath.getText().split(";"));
		}

		public String getLabel() {
			return "Scan";
		}
		
	}
	
}
