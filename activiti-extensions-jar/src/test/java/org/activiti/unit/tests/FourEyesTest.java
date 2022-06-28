package org.activiti.unit.tests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiTestCase;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class FourEyesTest extends ActivitiTestCase {

	public String getTaskOutcomeVariable(Task task) {
		return "form" + task.getFormKey() + "outcome";
	}

	@Test
	public void test() {
		User currentUser = identityService.newUser("plucidi");
		currentUser.setEmail("plucidi@ziaconsulting.com");
		currentUser.setFirstName("Piergiorgio");
		currentUser.setLastName("Lucidi");
		currentUser.setPassword("plucidi");
		
		identityService.saveUser(currentUser);
		
		List<User> users = identityService.createUserQuery().userId("plucidi").list();
		currentUser = users.get(0);
		
		String tenantId = StringUtils.EMPTY;
		repositoryService.createDeployment()
				.addClasspathResource("apps/fourEyes/bpmn-models/4 Eyes Principle-9011.bpmn20.xml").tenantId(tenantId)
				.deploy().getId();

		Map<String, Object> processVars = getProcessInitVariables(currentUser.getId());
		Map<String, Object> taskVars = new HashMap<String, Object>();

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId("fourEyesPrinciple",
				processVars, tenantId);

		assertNotNull(processInstance);

		assertEquals(1, taskService.createTaskQuery().count());

		// Validate document - First approval
		Task task = taskService.createTaskQuery().singleResult();
		processVars.put(getTaskOutcomeVariable(task), "submit");
		taskService.complete(task.getId(), processVars);

		assertEquals(1, taskService.createTaskQuery().count());
		System.out.println("--- /Validate document - First approval ---");

		// Validate document - Second approval

		task = taskService.createTaskQuery().singleResult();
		taskVars = taskService.getVariables(task.getId());
		taskVars.put(getTaskOutcomeVariable(task), "submit");
		taskService.complete(task.getId(), taskVars);

		assertEquals(0, taskService.createTaskQuery().count());

		System.out.println("--- /Validate document - Second approval ---");

		System.out.println("--- /End ---");

	}

	private Map<String, Object> getProcessInitVariables(String userId) {
		Map<String, Object> processInitVariables = new HashMap<String, Object>();
		processInitVariables.put("initiator", userId);
		processInitVariables.put("assignee", userId);
		processInitVariables.put("reviewtitle", "Iterating against software");
		processInitVariables.put("description", "The quick brown fox jumps over the lazy dog.");
		return processInitVariables;
	}

}
