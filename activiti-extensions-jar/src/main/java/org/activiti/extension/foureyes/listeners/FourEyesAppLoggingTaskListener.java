package org.activiti.extension.foureyes.listeners;

import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class FourEyesAppLoggingTaskListener implements TaskListener {

	private final Logger logger = LoggerFactory.getLogger(FourEyesAppLoggingTaskListener.class);

	@Override
	public void notify(DelegateTask delegateTask) {				
		//Process variables 
		logger.info("--- Task variables:");
		Map<String, Object> taskVars = delegateTask.getVariables();
		for (Map.Entry<String, Object> taskVar : taskVars.entrySet()) {
		 logger.info(" [" + taskVar.getKey() + " = " + taskVar.getValue() + "]");
		}
		
	}

}