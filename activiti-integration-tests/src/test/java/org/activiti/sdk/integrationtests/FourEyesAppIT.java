package org.activiti.sdk.integrationtests;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.activiti.community.swagger.handler.ApiClient;
import org.activiti.community.swagger.handler.ApiException;
import org.activiti.community.swagger.handler.ProcessDefinitionsApi;
import org.activiti.community.swagger.handler.ProcessInstancesApi;
import org.activiti.community.swagger.handler.TasksApi;
import org.activiti.community.swagger.handler.UsersApi;
import org.activiti.community.swagger.model.DataResponse;
import org.activiti.community.swagger.model.ProcessDefinitionResponse;
import org.activiti.community.swagger.model.ProcessInstanceCreateRequest;
import org.activiti.community.swagger.model.ProcessInstanceResponse;
import org.activiti.community.swagger.model.TaskActionRequest;
import org.junit.jupiter.api.Order;

import com.google.gson.internal.LinkedTreeMap;

/**
 * This test requires that the FourEyes process definition, or any other similar
 * processes is installed in the Activiti Engine. Here we are assuming to have a
 * process definition that allows to complete the entire process instance with
 * just two submissions of two user tasks
 */
public class FourEyesAppIT {

	protected static final String ACTIVITI_REST_USERNAME = "admin";
	protected static final String ACTIVITI_REST_PASSWORD = "test";
	protected static final String BASE_PATH_PROTOCOL = "http";
	protected static final String BASE_PATH_HOSTNAME = "localhost";
	protected static final int BASE_PATH_PORT = 8081;

	protected static final String ACTIVITI_REST_BASE_PATH = BASE_PATH_PROTOCOL + "://" + BASE_PATH_HOSTNAME + ":"
			+ BASE_PATH_PORT + "/activiti-rest/service";

	ApiClient activitiClient = null;

	ProcessDefinitionsApi processDefApi = null;
	TasksApi tasksApi = null;
	ProcessInstancesApi processInstancesApi = null;
	UsersApi usersApi = null;

	@SuppressWarnings("unchecked")
	// @Test
	@Order(1)
	public void testActivitiSwaggerApi() {

		activitiClient = new ApiClient();
		activitiClient.setBasePath(ACTIVITI_REST_BASE_PATH);
		activitiClient.setUsername(ACTIVITI_REST_USERNAME);
		activitiClient.setPassword(ACTIVITI_REST_PASSWORD);

		processDefApi = new ProcessDefinitionsApi(activitiClient);
		tasksApi = new TasksApi(activitiClient);
		processInstancesApi = new ProcessInstancesApi(activitiClient);

		try {

			// Get all the process definition
			DataResponse dataResponse = processDefApi.getProcessDefinitions(null, null, null, null, null, null, null,
					null, null, null, null, null, null, null, null);

			List<LinkedTreeMap<String, String>> processDefsList = (List<LinkedTreeMap<String, String>>) dataResponse
					.getData();

			// Get the first processDefinitionId available in the engine
			String processDefinitionId = processDefsList.get(0).get("id");

			// Get the process definition
			ProcessDefinitionResponse processDefResponse = processDefApi.getProcessDefinition(processDefinitionId);

			// Get the process definition key and creates the createRequest for creating a
			// new workflow instance
			ProcessInstanceCreateRequest createRequest = new ProcessInstanceCreateRequest();
			createRequest.setProcessDefinitionId(processDefinitionId);
			createRequest.setBusinessKey(processDefResponse.getKey());

			// Create a new workflow instance
			ProcessInstanceResponse processInstanceResponse = processInstancesApi.createProcessInstance(createRequest);
			String processInstanceId = processInstanceResponse.getId();

			// Get the current active tasks for this specific process instance
			dataResponse = tasksApi.getTasks(null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null, processInstanceId, null, null, null, null, null, null, null,
					null, null, null, null, null, null, null, null, null, Boolean.TRUE, null, null, null, null, null,
					null, null);

			List<LinkedTreeMap<String, String>> tasksList = (List<LinkedTreeMap<String, String>>) dataResponse
					.getData();
			String taskId = tasksList.get(0).get("id");

			// Execute the submit for the first user task
			TaskActionRequest taskActionRequest = new TaskActionRequest();
			taskActionRequest.setAction("complete");
			tasksApi.executeTaskAction(taskId, taskActionRequest);

			// Get the current active tasks again after the submission
			dataResponse = tasksApi.getTasks(null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null, processInstanceId, null, null, null, null, null, null, null,
					null, null, null, null, null, null, null, null, null, Boolean.TRUE, null, null, null, null, null,
					null, null);

			tasksList = (List<LinkedTreeMap<String, String>>) dataResponse.getData();
			taskId = tasksList.get(0).get("id");

			// Execute the submit for the second user task
			taskActionRequest.setAction("complete");
			tasksApi.executeTaskAction(taskId, taskActionRequest);

			dataResponse = tasksApi.getTasks(null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null, processInstanceId, null, null, null, null, null, null, null,
					null, null, null, null, null, null, null, null, null, Boolean.TRUE, null, null, null, null, null,
					null, null);
			tasksList = (List<LinkedTreeMap<String, String>>) dataResponse.getData();

			// Checking if the process instance is completed
			assertEquals(tasksList.size(), 0);

		} catch (ApiException e) {
			fail(e.getMessage());
		}
	}

}
