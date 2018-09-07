package mgt.inventory.pharmacy;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mgt.inventory.pharmacy.database.MongoDB;

/**
 * Application Lifecycle Listener implementation class ApplicationListener
 *
 */
@WebListener
public class ApplicationListener implements ServletContextListener {

	static Logger logger = LoggerFactory.getLogger(ApplicationListener.class);

	public ApplicationListener() {
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("**************** contextDestroyed ****************");
		MongoDB.stopDB();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("**************** contextInitialized ****************");
		MongoDB.startDB();
	}

}
