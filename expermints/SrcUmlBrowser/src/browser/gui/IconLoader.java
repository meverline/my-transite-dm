package browser.gui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import browser.loader.ClassReflectionData;

public final class IconLoader {

	private static Log log = LogFactory.getLog(AppMainWindow.class);
	private static IconLoader self = null;
	private Map<String, ImageIcon> cache = new HashMap<String, ImageIcon>();
	/**
	 * 
	 */
	private IconLoader() {	
		
		this.loadIcon("C_default", "/icons/class_default_obj.png");
		this.loadIcon("C", "/icons/class_obj.png");
		
		this.loadIcon("IC", "/icons/innerclass_default_obj.png");
		this.loadIcon("IC_protected", "/icons/innerclass_protected_obj.png");
		this.loadIcon("IC_private", "/icons/innerclass_private_obj.png");
		this.loadIcon("IC_public", "/icons/innerclass_public_obj.png");
		
		this.loadIcon("I", "/icons/int_obj.png");
		this.loadIcon("I_default", "/icons/int_default_obj.png");
		this.loadIcon("I_public", "/icons/innerinterface_public_obj.png");
		this.loadIcon("II_default", "/icons/innerinterface_default_obj.png");
		this.loadIcon("II_private", "/icons/innerinterface_private_obj.png");
		this.loadIcon("II_protected", "/icons/innerinterface_protected_obj.png");
		
		this.loadIcon("E", "/icons/enum_obj.png");
		this.loadIcon("E_default", "/icons/enum_default_obj.png");
		this.loadIcon("E_private", "/icons/enum_private_obj.png");
		this.loadIcon("E_protected", "/icons/enum_protected_obj.png");
		
		this.loadIcon("P", "/icons/package_obj.png");
		this.loadIcon("P_folder", "/icons/packagefolder_obj.png");
							
		this.loadIcon("M", "/icons/magnifier_zoom_in.png");
		this.loadIcon("F", "/icons/folder_find.png	");
				
	}
	
	/**
	 * 
	 * @param name
	 */
	private void loadIcon(String key, String name) {
		
		URL url =  IconLoader.class.getResource(name);
		if ( url != null ) {
			cache.put(key, new ImageIcon(url));
		} else {
			log.error("uanble to load: " + name);
		}
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized IconLoader instance()
	{
		if ( IconLoader.self == null ) {
			IconLoader.self = new IconLoader();
		}
		return IconLoader.self;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public ImageIcon getIcon(String name) {
		
		if (cache.containsKey(name)) {
			return cache.get(name);
		}
		return null;
	}
	
	public ImageIcon getIcon(ClassReflectionData fname) {
		
		ImageIcon rtn = null;
		
		if ( fname.isEnum() ) {
			rtn = this.getIcon("E");
		}
		else if ( fname.getName().contains("$") ) {
			if ( fname.isPrivate() ) {
				rtn = this.getIcon("IC_private");
			} else if ( fname.isProtected()) {
				rtn = this.getIcon("IC_protected");
			} else {
				rtn = this.getIcon("IC_public");
			}
		}
		else if ( fname.isInterface() ) {
			if ( fname.isPrivate() ) {
				rtn = this.getIcon("I_private");
			} else if ( fname.isProtected()) {
				rtn = this.getIcon("I_protected");
			} else {
				rtn = this.getIcon("I");
			}
		}
		else {
			rtn = this.getIcon("C");
		}
		
		return rtn;
	}
}
