package browser.gui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import browser.gui.AppMainWindow;
import browser.gui.commands.CloseDialogAction;
import browser.gui.commands.FileSelectionCommand;
import browser.gui.layout.SpringLayoutUtilities;
import browser.util.ApplicationSettings;
import browser.util.Project;

@SuppressWarnings("serial")
public class NewProjectDialog extends JDialog {

	private Log log = LogFactory.getLog(NewProjectDialog.class);
	
	private JTextField name = null;
	private JTextField loadPath = null;
	private JTextField srcPath = null;
	private JTextField jarPath = null;
	private AppMainWindow mainWindow = null;
	
	public NewProjectDialog(AppMainWindow window)
	{
		super(window);
		build();
		setSize(700,200);
		setTitle("New Project");
		this.setLocationByPlatform(true);
		this.setVisible(true);
		mainWindow = window;
	}
	
	private void build()
	{
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		panel.add(buildInputPanel(), BorderLayout.CENTER);
		
		JPanel buttonBox = new JPanel();
		
		JButton applyButton = new JButton("Save");
		buttonBox.add(applyButton);
		applyButton.addActionListener( new ApplyDialogAction());
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
	private JPanel buildInputPanel()
	{
		BufferedImage icon = null;
		try {
			File fp = new File("icons/folder_find.png");
			icon = ImageIO.read(fp);
		} catch (IOException e) {
			log.error(e);
		}

		JPanel panel = new JPanel();
		this.add(panel);
		panel.setLayout(new SpringLayout());
		
		panel.add(new JLabel("Project Name"));
		name = new JTextField();
		name.setColumns(40);
		panel.add(name);
		panel.add(new JLabel(""));
		
		panel.add(new JLabel("Load Path"));
		loadPath = new JTextField();
		loadPath.setColumns(40);
		panel.add(loadPath);

		JButton button;
		if (icon != null) {
			button = new JButton(new ImageIcon(icon));
		} else {
			button = new JButton("Browse...");
		}
		button.addActionListener(new FileSelectionCommand(loadPath, this, true, new CopyAction()));
		panel.add(button);

		panel.add(new JLabel("Src Path"));
		srcPath = new JTextField();
		srcPath.setColumns(40);
		panel.add(srcPath);

		if (icon != null) {
			button = new JButton(new ImageIcon(icon));
		} else {
			button = new JButton("Browse...");
		}
		button.addActionListener(new FileSelectionCommand(loadPath, this, true));
		panel.add(button);

		panel.add(new JLabel("Jar Path"));
		jarPath = new JTextField();
		jarPath.setColumns(40);
		panel.add(jarPath);

		if (icon != null) {
			button = new JButton(new ImageIcon(icon));
		} else {
			button = new JButton("Browse...");
		}
		button.addActionListener(new FileSelectionCommand(loadPath, this, true));
		panel.add(button);

		SpringLayoutUtilities.makeCompactGrid(panel, 4, 3, 2, 2, 5, 5);

		return panel;
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////
	
	public class CopyAction implements ActionListener {
				
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			srcPath.setText( loadPath.getText());
			jarPath.setText( loadPath.getText());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////
	
	public class ApplyDialogAction implements ActionListener {
		
		public String fixPath(String path) {
			return path.toString().replace("\\", "/");
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			Project project = new Project();
			
			project.setName(name.getText());
			project.setLoadPath(fixPath(loadPath.getText()));
			project.setScanClassPath(fixPath(srcPath.getText()));
			project.setScanJarPath(fixPath(jarPath.getText()));
			
			ApplicationSettings.instance().saveProject(project);
			mainWindow.loadProject(project);
		}

	}

}
