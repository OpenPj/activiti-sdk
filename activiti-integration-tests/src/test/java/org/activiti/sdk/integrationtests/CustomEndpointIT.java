package org.activiti.sdk.integrationtests;

import org.activiti.sdk.integration.tests.utils.IntegrationTestUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class CustomEndpointIT {

	protected static final String ACTIVITI_APP_USERNAME = "admin";
	protected static final String ACTIVITI_APP_PASSWORD = "test";
	protected static final String BASE_PATH_PROTOCOL = "http";
	protected static final String BASE_PATH_HOSTNAME = "localhost";
	protected static final int BASE_PATH_PORT = 8081;

	protected static final String ACTIVITI_APP_BASE_PATH = BASE_PATH_PROTOCOL + "://" + BASE_PATH_HOSTNAME + ":"
			+ BASE_PATH_PORT;

	protected static final String PRIVATE_ENDPOINT = ACTIVITI_APP_BASE_PATH + "/activiti-rest/service/sdk/my-rest-endpoint";
	
	@Test
	@Order(1)
	public void testCustomPrivateRestEndpoint() {
		IntegrationTestUtils.executePrivateGETRequest(ACTIVITI_APP_USERNAME, ACTIVITI_APP_PASSWORD, BASE_PATH_PROTOCOL,
				BASE_PATH_HOSTNAME, BASE_PATH_PORT, PRIVATE_ENDPOINT);
	}

}
