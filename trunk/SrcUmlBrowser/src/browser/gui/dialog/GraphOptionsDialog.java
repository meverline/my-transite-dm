/**
 * 
 */
package browser.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import browser.graph.GraphVizGraph;
import browser.gui.commands.CloseDialogAction;
import browser.gui.layout.SpringLayoutUtilities;
import browser.util.ApplicationSettings;

/**
 * @author markeverline
 *
 */
public class GraphOptionsDialog extends JDialog {

	/**
	 * 
	 */
	private Log log = LogFactory.getLog(GraphOptionsDialog.class);
	private static final long serialVersionUID = 1L;
	private JTextField textField = null;
	private JTextField fileNameField = null;
	private JComboBox graphType = null;
	private JComboBox graphCmd = null;
	private GraphVizGraph dataGraph = null;
	private Component parent = null;
	
	
	/**
	 * 
	 * @param frame
	 */
	public GraphOptionsDialog(JFrame frame, GraphVizGraph graph)
	{
		super(frame);
		parent = frame;
		init(graph);
	}
	
	
	/**
	 * 
	 * @param frame
	 */
	public GraphOptionsDialog(JDialog frame, GraphVizGraph graph)
	{
		super(frame);
		parent = frame;
		init(graph);
	}
	
	
	private void init(GraphVizGraph graph)
	{
		dataGraph = graph;
		build();
		setSize(500,220);
		setTitle("Graph Settings");
		this.setLocationByPlatform(true);
	}
	
	/**
	 * 
	 */
	private void build()
	{
		ApplicationSettings set = ApplicationSettings.instance();

		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());
		
		JPanel graph = new JPanel();
		graph.setLayout( new SpringLayout());
		
		JPanel outer = new JPanel();
		outer.add(graph);
		panel.add(outer, BorderLayout.CENTER);
		
		// 1 directory.
		graph.add( new JLabel("File:"));
		
		textField = new JTextField();
		textField.setColumns(40);
		textField.setText( ApplicationSettings.instance().getSettings().getGraphOutputDir());
		graph.add(textField);
		
		JButton button = new JButton("Browse ...");
		button.addActionListener( new BrowserCommand(this));
		graph.add(button);
		
		// 1 file Name.
		graph.add( new JLabel("Name:"));
		
		fileNameField = new JTextField();
		fileNameField.setColumns(40);
		fileNameField.setText( dataGraph.getName());
		graph.add(fileNameField);
		
		graph.add( new JLabel(""));
			
		// 2 output.
		graph.add( new JLabel("Output Format:"));
		
		graphType = new JComboBox( GraphVizGraph.OUTPUT.values());
		graph.add(graphType);
		
		if ( set.getSettings().getGraphOutput() != null ) {
			String value = set.getSettings().getGraphOutput();
			graphType.setSelectedItem( GraphVizGraph.OUTPUT.valueOf(value));
		}
		
		graph.add( new JLabel(""));
		
		// 3 command.
		graph.add( new JLabel("Command:"));
		
		graphCmd = new JComboBox( GraphVizGraph.CMD.values());
		graph.add(graphCmd);
		
		if ( set.getSettings().getGraphProgram() != null ) {
			String value = set.getSettings().getGraphProgram();
			graphCmd.setSelectedItem( GraphVizGraph.CMD.valueOf(value));
		}
		
		graph.add( new JLabel(""));
		
		SpringLayoutUtilities.makeCompactGrid(graph, 4, 3, 2, 2, 5, 5);

		//////////////////////////////////////////////////
		
		JPanel buttonBox = new JPanel();
		
		JButton applyButton = new JButton("Save");
		buttonBox.add(applyButton);
		applyButton.addActionListener( new SaveGraphCommand(this));
		applyButton.addActionListener( new CloseDialogAction(this));
		
		JButton close = new JButton("Cancel");
		close.addActionListener( new CloseDialogAction(this));
		buttonBox.add(close);
		
		panel.add(buttonBox, BorderLayout.SOUTH);
		
		this.setContentPane(panel);
	}
	
	/**
	 * @return the textField
	 */
	public String getTextField() {
		return textField.getText();
	}

	/**
	 * @param textField the textField to set
	 */
	public void setTextField(String value) {
		this.textField.setText(value);
	}

	/**
	 * @return the graphType
	 */
	public GraphVizGraph.OUTPUT getGraphType() {
		return (GraphVizGraph.OUTPUT) graphType.getSelectedItem();
	}

	/**
	 * @return the graphCmd
	 */
	public GraphVizGraph.CMD getGraphCmd() {
		return (GraphVizGraph.CMD) graphCmd.getSelectedItem();
	}

	/**
	 * @return the dataGraph
	 */
	public GraphVizGraph getDataGraph() {
		return dataGraph;
	}
	
	public String getFileNameField()
	{
		return fileNameField.getText();
	}
	
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	private class SaveGraphCommand implements ActionListener {
		
		GraphOptionsDialog dialog = null;
		
		public SaveGraphCommand(GraphOptionsDialog d) {
			dialog = d;
		}

		public void actionPerformed(ActionEvent arg0) {
			
			if ( dialog.getDataGraph() == null ) {
				log.info("Data Graph is null");
				return;
			}
			
			ApplicationSettings set = ApplicationSettings.instance();
			
			set.getSettings().setGraphOutputDir(dialog.getTextField());
			
			Cursor current = parent.getCursor();
			parent.setCursor( new Cursor(Cursor.WAIT_CURSOR));
			try {
				StringBuilder fileName = new StringBuilder();
				
				fileName.append(dialog.getTextField());
				fileName.append(File.separator);
				fileName.append(dialog.getFileNameField());
				fileName.append(".dot");
				
				PrintWriter writer = new PrintWriter( fileName.toString() );
				dialog.getDataGraph().write(writer);
				writer.close();
				
				List<String> cmd = new ArrayList<String>();
				
				StringBuilder exe = new StringBuilder();
				
				exe.append( set.getSettings().getGraphHome() );
				exe.append(File.separator);
				exe.append( dialog.getGraphCmd().name());
								
				File fp = new File(exe.toString().replace("\\", "/"));
				if ( ! fp.exists()) {
					cmd.add( dialog.getGraphCmd().name() );
				} else {
				    cmd.add(exe.toString());
				}
				
				cmd.add("-T" + dialog.getGraphType().name());
				
				String input = fileName.toString();
				fileName.delete(0, fileName.length());
				
				fileName.append(dialog.getTextField());
				fileName.append(File.separator);
				fileName.append(dialog.getFileNameField());
				fileName.append(".");
				fileName.append(dialog.getGraphType());
				cmd.add("-o" + fileName.toString());
				cmd.add(input);
				
				StringBuilder builder = new StringBuilder();
				for ( String arg : cmd) {
					builder.append(arg);
					builder.append(" ");
				}
				log.info(builder.toString());
				
				ProcessBuilder pb = new ProcessBuilder(cmd);
				pb.redirectErrorStream(true);
				Process p = pb.start();
				p.waitFor();
				
				if ( p.exitValue() != 0 ) {
					InputStream err = p.getErrorStream();
					
					StringBuilder errString = new StringBuilder();
					errString.append("exit value ");
					errString.append(p.exitValue());
					errString.append(" ");
					while( err.available() != 0 ) {
						errString.append( err.read() );
					}
				    log.error( p.exitValue() + errString.toString());	
					
				}
				
				if (set.getSettings().getPreviewCommand()  != null) {
					
					cmd.clear();
					cmd.add(set.getSettings().getPreviewCommand());
					cmd.add(fileName.toString());
					
					pb = new ProcessBuilder(cmd);
					p = pb.start();
				}
				
			} catch (Exception e) {
				log.error(e);
				@SuppressWarnings("unused")
				ExceptionDialog ed = new ExceptionDialog(dialog, e);
			}
			parent.setCursor( current);
			
		}
		
	}
	
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	private class BrowserCommand implements ActionListener {
		
		GraphOptionsDialog dialog = null;
		
		public BrowserCommand(GraphOptionsDialog d) {
			dialog = d;
		}

		public void actionPerformed(ActionEvent arg0) {
			JFileChooser chooser = new JFileChooser();
			
			if ( chooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION)  {
				setTextField( chooser.getSelectedFile().toString());
			}
			
		}
		
	}
	
}
