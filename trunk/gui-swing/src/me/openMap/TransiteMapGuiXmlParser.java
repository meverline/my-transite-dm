package me.openMap;

import java.awt.Component;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import me.openMap.command.ICommand;
import me.openMap.models.query.ISearchParameters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TransiteMapGuiXmlParser extends DefaultHandler {
	
	private static final String MENU = "menu";
	private static final String TOOLBAR = "toolbar";
	private static final String SIZE = "size";
	private static final String PARAMETER = "parameter";
	
	private static final String LABEL = "label";
	private static final String VALUE = "value";
	private static final String ACTION_LISTENER = "actionListener";
	private static final String SELECTED = "selected";
	private static final String ITEM = "item";
	private static final String PROPERTY = "property";
	
	private static final String SEARCH = "search";
	private static final String TAB = "tab";
	private static final String SAMPLE = "sample";
	

	private StringBuffer   buffer_ = new StringBuffer();
	private Stack<String>  tagStack = new Stack<String>();
	private SAXParser      parser_ = null;
	private OpenTransitMap app = null;
	private Stack<JMenu>   curentMenu = new Stack<JMenu>();
	private HashMap<String,String> toolBarMap = new HashMap<String,String>();
	private List<String>   itemList = new ArrayList<String>();
	private List<String>   sampleList = new ArrayList<String>();
	private List<String>   paramenters = new ArrayList<String>();
	private int			   width = 1000;
	private int            height = 600;
	private String		   tabValue = null;
	private ICommand       currentCommand = null;
	
	protected static Log log_ = LogFactory.getLog(TransiteMapGuiXmlParser.class);
	
	/**
	 * 
	 * @param application
	 */
	public TransiteMapGuiXmlParser(OpenTransitMap application)
	{
		this.app = application;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			parser_ = factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			TransiteMapGuiXmlParser.log_.error(e);
		} catch (SAXException e) {
			TransiteMapGuiXmlParser.log_.error(e);
		}
	}
	
	/**
	 * 
	 * @param commandClass
	 * @param app
	 * @return
	 */
	public static ICommand getCommand(String commandClass, OpenTransitMap app)
	{
		ICommand command = null;
		try {
			Class<?> cls = Class.forName(commandClass);
			command = ICommand.class.cast(cls.newInstance());
			command.initilize(app);
		} catch (Exception e) {
			TransiteMapGuiXmlParser.log_.error(e);
		} 
		return command;
	}
	
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * 
	 * @param app
	 */
	public void setSize(OpenTransitMap app)
	{
		app.setSize(this.getWidth(), this.getHeight());
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public boolean parse(String data) {

		boolean rtn = true;
		try {

			buffer_.delete(0, buffer_.length());
			parser_.parse(new InputSource(new FileReader(data)), this);

		} catch (SAXException e) {
			TransiteMapGuiXmlParser.log_.error(e,e);
			rtn = false;
		} catch (IOException e) {
			TransiteMapGuiXmlParser.log_.error(e,e);
			rtn = false;
		}
		return rtn;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		for (int ndx = 0; ndx < length; ndx++) {
			buffer_.append(ch[start + ndx]);
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param attributes
	 */
	private void processMenu(String name, Attributes attributes)
	{
		MenuEnumTypes item = MenuEnumTypes.valueOf(name);
		
		item.setApp(this.app);
		this.currentCommand = item.create(attributes, this.curentMenu.peek());
	}
	
	/**
	 * 
	 * @param name
	 * @param attributes
	 */
	private void processToolBarItem(String name, Attributes attributes)
	{
		if ( name.compareTo(ITEM) == 0) {
			this.itemList.add(attributes.getValue(VALUE).trim());
		} else {
			for ( int ndx = 0; ndx < attributes.getLength(); ndx++ ) {
				String key = attributes.getQName(ndx);
				String value = attributes.getValue(ndx);
				
				this.toolBarMap.put(key, value);
			}
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param attributes
	 */
	private void processSearch(String name, String label, List<String> sampleList)
	{
		if ( name.compareTo(TransiteMapGuiXmlParser.TAB) == 0 ) {			
			ISearchParameters command = null;
			
			try {
				Class<?> cls = Class.forName(label);
				command = ISearchParameters.class.cast(cls.newInstance());
				command.initilize(app, sampleList);
				
				app.getTabbedPane().addTab(command.getName(), (Component)command); 
				app.getSearchMethods().add(command);
				
			} catch (Exception e) {
				e.printStackTrace();
				TransiteMapGuiXmlParser.log_.error(e);
			} 
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		
		if ( name.compareTo(TransiteMapGuiXmlParser.MENU) == 0) {
			this.curentMenu.pop();
			tagStack.pop();

		} 
		else if (name.compareTo(TransiteMapGuiXmlParser.TOOLBAR) == 0  )
		{
			this.toolBarMap.clear();
			tagStack.pop();
		}
		else if (name.compareTo(TransiteMapGuiXmlParser.SEARCH) == 0  )
		{
			tagStack.pop();
		}
		else if ( ! tagStack.isEmpty() )
		{
			if ( tagStack.peek().compareTo(TransiteMapGuiXmlParser.SEARCH) == 0 &&
				 name.compareTo(TransiteMapGuiXmlParser.SAMPLE) != 0  ) {
				processSearch(name,this.tabValue, this.sampleList);
				sampleList.clear();
			}
			if ( tagStack.peek().compareTo(TransiteMapGuiXmlParser.MENU) == 0 &&
					 name.compareTo("menuItem") != 0 &&  this.currentCommand != null ) {
				this.currentCommand.paramenters(paramenters);
			}
			if ( tagStack.peek().compareTo(TransiteMapGuiXmlParser.TOOLBAR) == 0 &&
				 name.compareTo(TransiteMapGuiXmlParser.ITEM) != 0) {
				ToolBarTypes type = ToolBarTypes.valueOf(name);
				
				type.setApp(this.app);
				type.create(this.toolBarMap, this.itemList);
				this.toolBarMap.clear();
				this.itemList.clear();
			}
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String name,
							 Attributes attributes) throws SAXException {
		
		if ( name.compareTo(TransiteMapGuiXmlParser.MENU) == 0 ) {
			tagStack.push(name);
			
			String label  = attributes.getValue(TransiteMapGuiXmlParser.LABEL).trim();
			this.curentMenu.push( new JMenu(label)); 
			app.getApplicationMenuBar().add(this.curentMenu.peek());
			this.paramenters = new ArrayList<String>();
			
		}
		else if (name.compareTo(TransiteMapGuiXmlParser.TOOLBAR) == 0  )
		{
			tagStack.push(name);
			this.toolBarMap.clear();
		}
		else if (name.compareTo(TransiteMapGuiXmlParser.SEARCH) == 0  )
		{
			tagStack.push(name);
		}
		else if (name.compareTo(TransiteMapGuiXmlParser.PARAMETER) == 0  )
		{
			String value  = attributes.getValue(TransiteMapGuiXmlParser.VALUE).trim();
			this.paramenters.add(value);
		}
		else if (name.compareTo(TransiteMapGuiXmlParser.SAMPLE) == 0  )
		{
			String value  = attributes.getValue(TransiteMapGuiXmlParser.VALUE).trim();
			sampleList.add(value);
		}
		else if (name.compareTo(TransiteMapGuiXmlParser.SIZE) == 0  )
		{
			String width  = attributes.getValue("width").trim();
			String height  = attributes.getValue("height").trim();
			
			this.width = Integer.parseInt(width);
			this.height = Integer.parseInt(height);
		}
		else if ( ! tagStack.isEmpty() )
		{
			if ( tagStack.peek().compareTo(TransiteMapGuiXmlParser.MENU) == 0) {
				processMenu(name,attributes);
			}
			else if ( tagStack.peek().compareTo(TransiteMapGuiXmlParser.SEARCH) == 0) {
				this.tabValue  = attributes.getValue(TransiteMapGuiXmlParser.VALUE).trim();
			}else if ( tagStack.peek().compareTo(TransiteMapGuiXmlParser.TOOLBAR) == 0) {
				processToolBarItem(name,attributes);
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	
	private enum MenuEnumTypes {
		
		menuItem {
			public ICommand create(Attributes attributes, JMenu current)
			{
				String label  = attributes.getValue(TransiteMapGuiXmlParser.LABEL).trim();
				String alClass  = attributes.getValue(TransiteMapGuiXmlParser.ACTION_LISTENER).trim();
				
				JMenuItem button = new JMenuItem(label);
				current.add(button);
				ICommand command = TransiteMapGuiXmlParser.getCommand(alClass, getApp());
				if ( command != null ) {
					button.addActionListener(command);
				}
				return command;
			}
		},
		separator{
			public ICommand create(Attributes attributes, JMenu current)
			{
				current.add(new JSeparator());
				return null;
			}
		};
		
		private OpenTransitMap app = null;
		
		public abstract ICommand create(Attributes attributes, JMenu current);

		/**
		 * @return the app
		 */
		public OpenTransitMap getApp() {
			return app;
		}

		/**
		 * @param app the app to set
		 */
		public void setApp(OpenTransitMap app) {
			this.app = app;
		}
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////

	private enum ToolBarTypes {
		
		checkBox {
			public  void create(HashMap<String,String> attributes, List<String> itemList)
			{				
				JCheckBox button = new JCheckBox(attributes.get(TransiteMapGuiXmlParser.LABEL));
				getApp().getToolBar().add(button);
				if ( attributes.containsKey(TransiteMapGuiXmlParser.SELECTED)) {
					button.setSelected(true);
				} else {
					button.setSelected(false);
				}
				
				ICommand command = 
					TransiteMapGuiXmlParser.getCommand(attributes.get(TransiteMapGuiXmlParser.ACTION_LISTENER),
													   getApp());
				if ( command != null ) {
					button.addActionListener(command);
				}
				
				setProperty(attributes.get(TransiteMapGuiXmlParser.PROPERTY).toString(), button);
			}
		},
		comboBox {
			public  void create(HashMap<String,String> attributes, List<String> itemList)
			{
				List<Object> items = new ArrayList<Object>();
				String type = attributes.get("type");
				for (String value : itemList) {
					if ( type.compareTo(Double.class.getName()) == 0) {
						items.add( new Double(value));
					} else if ( type.compareTo(Integer.class.getName() ) == 0) {
						items.add( new Integer(value));
					} else if ( type.compareTo(Float.class.getName() ) == 0) {
						items.add( new Float(value));
					} else if ( type.compareTo(Short.class.getName() ) == 0) {
						items.add( new Float(value));
					} else {
						items.add( value);
					}
				}
				
				JComboBox box = new JComboBox(items.toArray());
				getApp().getToolBar().add(box);
				
				if ( attributes.containsKey("selectedItem") ) {
					int index = Integer.parseInt(attributes.get("selectedItem").toString());
					box.setSelectedItem( items.get(index) );
				}
				
				ICommand command = 
					TransiteMapGuiXmlParser.getCommand(attributes.get(TransiteMapGuiXmlParser.ACTION_LISTENER).toString(),
													   getApp());
				if ( command != null ) {
					box.addActionListener(command);
				}
				
				setProperty(attributes.get(TransiteMapGuiXmlParser.PROPERTY).toString(), box);

			}
		};
		
		private OpenTransitMap app = null;
		
		public abstract void create(HashMap<String,String> attributes, List<String> itemList);
		
		protected void setProperty(String property, JComponent value)
		{
			StringBuilder method = new StringBuilder();
			
			method.append("set");
			method.append(property.substring(0, 1).toUpperCase());
			method.append(property.substring(1));
			
			for ( Method meth : this.getApp().getClass().getMethods()) {
				if ( meth.getName().compareTo(method.toString()) == 0 ) {
					Object args[] = new Object[1];
					
					args[0] = value;
					
					try {
						meth.invoke(this.getApp(), args);
					} catch (Exception e) {
						TransiteMapGuiXmlParser.log_.error(e,e);
					}
					return;
				}
			}
		}

		/**
		 * @return the app
		 */
		public OpenTransitMap getApp() {
			return app;
		}

		/**
		 * @param app the app to set
		 */
		public void setApp(OpenTransitMap app) {
			this.app = app;
		}
	}
	
}
