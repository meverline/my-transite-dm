package me.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import me.database.hibernate.HibernateDao;

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
			String paths[] = {"me.config"};
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
	public HibernateDao<?> getDaoBean(Class<?> beanClass)
	{
		StringBuilder beanName = new StringBuilder();
		
		beanName.append( beanClass.getSimpleName().substring(0,1).toLowerCase());
		beanName.append( beanClass.getSimpleName().substring(1));
		return (HibernateDao<?>) context_.getBean(beanName.toString());
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
