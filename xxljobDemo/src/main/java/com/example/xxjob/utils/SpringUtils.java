package com.example.xxjob.utils;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring工具
 */
@Component("springUtils")
public class SpringUtils implements ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public void setApplicationContext(ApplicationContext context) {
		applicationContext = context;
	}

	public void destroy() throws Exception {
		applicationContext = null;
	}

	/**
	 * 获取Bean实例
	 */
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	/**
	 * 获取Bean实例
	 */
	public static <T> T getBean(String name, Class<T> type) {
		return applicationContext.getBean(name, type);
	}

	/**
	 * 获取Bean实例
	 */
	public static <T> T getBean(Class<T> type) {
		return applicationContext.getBean(type);
	}

}