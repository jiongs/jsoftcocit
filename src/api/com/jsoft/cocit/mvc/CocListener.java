package com.jsoft.cocit.mvc;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

import com.jsoft.cocimpl.CocitImpl;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.util.LogUtil;

/**
 * 
 * @preserve public
 * @author Ji Yongshan
 * 
 */
public class CocListener implements ServletContextListener, HttpSessionActivationListener {

	public void contextInitialized(ServletContextEvent event) {
		System.out.println("Cocit starting .....");
		LogUtil.info("Cocit starting .....");

		CocitImpl.init(event.getServletContext());
	}

	public void contextDestroyed(ServletContextEvent event) {
		LogUtil.info("Cocit destroying.....");

		Cocit.me().destroy(event.getServletContext());
	}

	public void sessionDidActivate(HttpSessionEvent event) {

	}

	public void sessionWillPassivate(HttpSessionEvent event) {

	}
}
