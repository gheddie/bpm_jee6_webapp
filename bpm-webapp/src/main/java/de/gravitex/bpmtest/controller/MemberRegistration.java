package de.gravitex.bpmtest.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.ProcessDefinition;

import de.gravitex.bpmtest.model.Member;

// The @Stateful annotation eliminates the need for manual transaction demarcation
@Stateful
// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class MemberRegistration {

   @Inject
   private Logger log;

   @Inject
   private EntityManager em;

   @Inject
   private Event<Member> memberEventSrc;

   private Member newMember;
   
   @Resource(mappedName = "java:global/camunda-bpm-platform/process-engine/default")
   private ProcessEngine processEngine;	   

   @Produces
   @Named
   public Member getNewMember() {
      return newMember;
   }

   public void register() throws Exception {
      log.info("Registering " + newMember.getName());
      em.persist(newMember);
      memberEventSrc.fire(newMember);
      initNewMember();
      for (ProcessDefinition definition: processEngine.getRepositoryService().createProcessDefinitionQuery().list()) {
    	  System.out.println(" ### " + definition.getName() + " [KEY="+definition.getKey()+"]");
      }
      processEngine.getRuntimeService().startProcessInstanceByKey("calledProcess");
   }

   @PostConstruct
   public void initNewMember() {
      newMember = new Member();
   }
}
