package me.factory;

import me.transit.dao.hibernate.HibernateDao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class DaoBeanFactory {
	
	private static DaoBeanFactory theOne = null;
	private ApplicationContext context_;
	
	/**
	 * 
	 * @param beanXmlFile
	 */
	private DaoBeanFactory(String beanXmlFile)
	{
		context_ = new FileSystemXmlApplicationContext(beanXmlFile) ;
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized DaoBeanFactory initilize() 
	{
		if (theOne == null) {
		    theOne = new DaoBeanFactory("config\\spring\\applicationContext.xml");
		}
		return theOne;
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized DaoBeanFactory create() 
	{
		if (theOne == null) {
		   throw new IllegalStateException("DaoBeanFactory not Initilized");
		}
		return theOne;
	}
	
	/**
	 * 
	 * @param beanClass
	 * @return
	 */
	public HibernateDao getDaoBean(Class<?> beanClass)
	{
		StringBuilder beanName = new StringBuilder();
		
		beanName.append( beanClass.getSimpleName().substring(0,1).toLowerCase());
		beanName.append( beanClass.getSimpleName().substring(1));
		return (HibernateDao) context_.getBean(beanName.toString());
	}
	
	/**
	 * 
	 * @param beanClass
	 * @return
	 */
	public Object getBean(Class<?> beanClass)
	{
		StringBuilder beanName = new StringBuilder();
		
		beanName.append( beanClass.getSimpleName().substring(0,1).toLowerCase());
		beanName.append( beanClass.getSimpleName().substring(1));
		return context_.getBean(beanName.toString());
	}
	
	
}
