/**
 * 
 */
package browser.gui.dialog;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import browser.graph.GraphBuilder;
import browser.gui.AppMainWindow;
import browser.gui.commands.CloseDialogAction;
import browser.gui.commands.SearchActionCommand;

/**
 * @author markeverline
 *
 */
public class SearchDialog extends JDialog {
    
	public enum CLASSNAME { 
		   FULLNAME {
			   public String getClassName(String name) {
				   return name;
			   }
		   },
		   SHORTNAME {
			   public String getClassName(String name) {
				   int ndx = name.lastIndexOf('.');
				   return name.substring(ndx+1);
			   }    
		   };
		   
		   public abstract String getClassName(String name);
    };
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 5011439039769348845L;
	
	private AppMainWindow main = null;
	private JComboBox<GraphBuilder.TEXT> typeText = null;
	private JComboBox<CLASSNAME> typeName = null;
	private JTextField searchText = null;
	private JList<String> classList = null;
	private JButton applyButton = null;
	private JCheckBox filterBySearch = null;
	private DefaultListModel<String> model = null;
	private Map<String, List<String>> map = new HashMap<String, List<String>>();
	/**
	 * 
	 * @param frame
	 */
	public SearchDialog(AppMainWindow frame, SearchActionCommand command)
	{
		super(frame);
		main = frame;
		command.setDialog(this);
		
		for ( String cls : command.getListItems() ) {
			
			int ndx = cls.lastIndexOf(".");
			String letter = cls.substring(ndx+1,ndx+2).toLowerCase();
			
			if ( ! map.containsKey(letter) ) {
				map.put(letter, new ArrayList<String>());
			}
			
			map.get(letter).add(cls);
		}
				
		build(command);
		setSize(command.getWidth(), 300);
		setTitle(command.title());
		this.setLocationByPlatform(true);
		this.setVisible(true);
		setLocationRelativeTo(frame);
	}
	
	public void setTypeText(JComboBox<GraphBuilder.TEXT> typeText) {
		this.typeText = typeText;
	}

	public String getSearchText() {
		return this.searchText.getText();
	}
	
	public GraphBuilder.TEXT getMatchType() {
		return (GraphBuilder.TEXT) this.typeText.getSelectedItem();
	}

	/**
	 * 
	 */
	private void build(SearchActionCommand command)
	{
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout( new BorderLayout());
		
		JPanel combo = new JPanel();
		
		typeName = new JComboBox<CLASSNAME>( CLASSNAME.values());
		combo.add(typeName);
		
		typeText = new JComboBox<GraphBuilder.TEXT>( GraphBuilder.TEXT.values());
		combo.add(typeText);
		
		filterBySearch = new JCheckBox("Filter By Search");
		combo.add(filterBySearch);
		
		combo.add(command.getUI());

		textPanel.add(combo, BorderLayout.SOUTH);
		
		textPanel.add(new JLabel( command.itemsName() + " name"), BorderLayout.WEST);
	
		searchText = new JTextField();
		searchText.getDocument().addDocumentListener(new TextChanged(command));
		textPanel.add(searchText, BorderLayout.CENTER);
		
		panel.add(textPanel, BorderLayout.NORTH);
		
		model = new DefaultListModel<String>();
		
		for (String name : command.getListItems()) {
			model.addElement(name);
		}
		classList = new JList<String>(model);
		if ( command.isMultiSelect() ) {
			classList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} else {
			classList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			classList.addListSelectionListener(new SelectionListener());
		}
		panel.add( new JScrollPane(classList), BorderLayout.CENTER);
		 		
		JPanel buttonBox = new JPanel();
		
		applyButton = new JButton(command.actionButtonLabel());
		applyButton.addActionListener(command);
		if ( ! command.isMultiSelect() ) {
			applyButton.setEnabled(false);
		}
		buttonBox.add(applyButton);
		
		JButton close = new JButton("Cancel");
		close.addActionListener( new CloseDialogAction(this));
		buttonBox.add(close);
		
		panel.add(buttonBox, BorderLayout.SOUTH);
		
		this.setContentPane(panel);
	}

	/**
	 * 
	 * @return
	 */
	public boolean filterBySearchText() {
		return this.filterBySearch.isSelected();
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getSelected()
	{
		return classList.getSelectedValuesList();
	}
	
	/**
	 * @return the typeText
	 */
	public JComboBox<GraphBuilder.TEXT> getTypeText() {
		return typeText;
	}

	/**
	 * @return the typeName
	 */
	public JComboBox<CLASSNAME> getTypeName() {
		return typeName;
	}

	/**
	 * @return the searchText
	 */
	public JTextField getSearchTextField() {
		return searchText;
	}

	/**
	 * @return the model
	 */
	public DefaultListModel<String> getModel() {
		return model;
	}

	/**
	 * @return the map
	 */
	public Map<String, List<String>> getMap() {
		return map;
	}
	
	/**
	 * @return the main
	 */
	public AppMainWindow getMain() {
		return main;
	}
	
	/**
	 * 
	 * @param className
	 * @return
	 */
	public boolean filter(String className) 
	{
		String matchString = getSearchText();
		GraphBuilder.TEXT matchType = (GraphBuilder.TEXT) getTypeText().getSelectedItem();
		
		boolean rtn = false;
		if ( this.filterBySearchText() ) {
			rtn = true;
			if ( (!matchString.isEmpty()) && matchType.matchs(className, matchString ) ) {
				rtn = false;
			} else {
				rtn = true;
			}
		}
		return rtn;
	}
		
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	private class SelectionListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent arg0) {
			
			if ( ! classList.getSelectedValuesList().isEmpty() ) {
				applyButton.setEnabled(true);
			} else {
				applyButton.setEnabled(false);
			}
			
		}
		
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	private class TextChanged implements DocumentListener {
		
		SearchActionCommand command = null;
		
		public TextChanged(SearchActionCommand aCommand)
		{
			command = aCommand;
		}
		
		private void updateListBox()
		{
			String matchString = getSearchText();
			GraphBuilder.TEXT matchType = (GraphBuilder.TEXT) getTypeText().getSelectedItem();
			CLASSNAME clsType =(CLASSNAME) getTypeName().getSelectedItem();
			
			getModel().clear();
			if ( clsType == CLASSNAME.FULLNAME && (!matchString.isEmpty()) ) {
				for ( String name : command.getListItems() ) {
					if ( matchType.matchs(name, matchString )) {
						getModel().addElement(name);
					}
				}
			}
			else if ( matchType == GraphBuilder.TEXT.STARTS_WITH && (!matchString.isEmpty()) ) {
				String firstLetter = matchString.substring(0,1).toLowerCase();
				if ( getMap().containsKey(firstLetter) ) {
					for ( String name :  getMap().get(firstLetter) ) {
						if ( matchType.matchs( clsType.getClassName(name), matchString )) {
							getModel().addElement(name);
						}
					}
				}
				
			}
			else if ( matchString.isEmpty() ) {
				for (String name : command.getListItems() ) {
					model.addElement(name);
				}
			}
			else {
				for ( String name : command.getListItems() ) {
					if ( matchType.matchs(clsType.getClassName(name), matchString )) {
						getModel().addElement(name);
					}
				}				
			}
	
			return;	
		}

		public void changedUpdate(DocumentEvent event) {
			this.updateListBox();
		}

		public void insertUpdate(DocumentEvent event) {
			this.updateListBox();
		}

		public void removeUpdate(DocumentEvent event) {
			this.updateListBox();
		}
		
	}
}
