package me.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import me.database.hibernate.AbstractHibernateDao;

public class DaoBeanFactory {
	
	private static DaoBeanFactory theOne = null;
	private ApplicationContext context_;
	
	/**
	 * 
	 * @param beanXmlFile
	 */
	private DaoBeanFactory(String[] configPackage)
	{
		context_ = new AnnotationConfigApplicationContext(configPackage) ;
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized DaoBeanFactory initilize() 
	{
		if (theOne == null) {
			String paths[] = {"me"};
		    theOne = new DaoBeanFactory(paths);
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
	public AbstractHibernateDao<?> getDaoBean(Class<?> beanClass)
	{
		StringBuilder beanName = new StringBuilder();
		
		beanName.append( beanClass.getSimpleName().substring(0,1).toLowerCase());
		beanName.append( beanClass.getSimpleName().substring(1));
		return (AbstractHibernateDao<?>) context_.getBean(beanName.toString());
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
