package org.activiti.sdk.integrationtests;

import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.community.swagger.handler.ApiClient;
import org.activiti.community.swagger.handler.ApiException;
import org.activiti.community.swagger.handler.DeploymentApi;
import org.activiti.community.swagger.handler.ProcessDefinitionsApi;
import org.activiti.community.swagger.handler.ProcessInstancesApi;
import org.activiti.community.swagger.handler.TasksApi;
import org.activiti.community.swagger.handler.UsersApi;
import org.activiti.community.swagger.model.UserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.google.gson.internal.LinkedTreeMap;

public class ActivitiSwaggerClientIT {

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
	DeploymentApi deploymentApi = null;
	UsersApi usersApi = null;

	@BeforeEach
	public void init() {
		activitiClient = new ApiClient();
		activitiClient.setBasePath(ACTIVITI_REST_BASE_PATH);
		activitiClient.setUsername(ACTIVITI_REST_USERNAME);
		activitiClient.setPassword(ACTIVITI_REST_PASSWORD);

		processDefApi = new ProcessDefinitionsApi(activitiClient);
		tasksApi = new TasksApi(activitiClient);
		processInstancesApi = new ProcessInstancesApi(activitiClient);
		deploymentApi = new DeploymentApi(activitiClient);
		usersApi = new UsersApi(activitiClient);

	}

	@SuppressWarnings("unchecked")
	@Test
	@Order(1)
	public void testActivitiSwaggerApi() {
		try {
			// create a new user
			UserRequest userRequest = new UserRequest();
			userRequest.setId("plucidi");
			userRequest.setFirstName("Piergiorgio");
			userRequest.setLastName("Lucidi");
			userRequest.setEmail("plucidi@ziaconsulting.com");

			usersApi.createUser(userRequest);

			// get all the users
			List<LinkedTreeMap<String, String>> usersList = (List<LinkedTreeMap<String, String>>) usersApi
					.getUsers(null, null, null, null, null, null, null, null, null, null).getData();
			Iterator<LinkedTreeMap<String, String>> iteratorUsers = usersList.iterator();
			System.out.println("Get Users: ");
			while (iteratorUsers.hasNext()) {
				LinkedTreeMap<java.lang.String, java.lang.String> linkedTreeMap = (LinkedTreeMap<java.lang.String, java.lang.String>) iteratorUsers
						.next();
				Iterator<Entry<String, String>> props = linkedTreeMap.entrySet().iterator();
				while (props.hasNext()) {
					Map.Entry<java.lang.String, java.lang.String> entry = (Map.Entry<java.lang.String, java.lang.String>) props
							.next();
					System.out.println(entry.getKey() + " = " + entry.getValue());
				}
				System.out.println("--- --- --- ---");
			}

		} catch (ApiException e) {
			fail(e.getMessage() + " Response body:" + e.getResponseBody());
		}
	}

	@AfterEach
	public void clean() {
		try {
			usersApi.deleteUser("plucidi");
		} catch (ApiException e) {
			fail(e.getMessage() + " Response body:" + e.getResponseBody());
		}
	}

}
