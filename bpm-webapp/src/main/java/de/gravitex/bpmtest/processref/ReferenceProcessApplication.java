package de.gravitex.bpmtest.processref;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.application.ProcessApplicationInterface;
import org.camunda.bpm.application.impl.EjbProcessApplication;

@ProcessApplication("Reference Process Application")
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Local(ProcessApplicationInterface.class)
public class ReferenceProcessApplication extends EjbProcessApplication {

	@PostConstruct
	public void start() {		
		deploy();
		System.out.println(" ------------------ DEPLOYED REF BPMN APP ------------------- ");
	}

	@PreDestroy
	public void stop() {
		undeploy();
		System.out.println(" ------------------ UNDEPLOYED REF BPMN APP ------------------ ");
	}
}
