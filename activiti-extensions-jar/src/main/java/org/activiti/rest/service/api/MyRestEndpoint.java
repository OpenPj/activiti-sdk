package org.activiti.rest.service.api;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.identity.Authentication;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;

/**
 * REST Service: http://localhost:8081/activiti-rest/service/sdk/my-rest-endpoint
 * @author pjlucidi
 *
 */
@RestController
@Api(tags = { "sdk" }, description = "SDK", authorizations = { @Authorization(value = "basicAuth") })
public class MyRestEndpoint {
	
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private IdentityService identityService;
    

    @RequestMapping(value = "/sdk/my-rest-endpoint", method = RequestMethod.GET, produces = "application/json")
    public MyRestEndpointResponse executeCustomLogic() {

    	String userId = Authentication.getAuthenticatedUserId();
        long taskCount = taskService.createTaskQuery().taskAssignee(userId).count();
        
        User currentUser = identityService.createUserQuery().userId(userId).list().get(0);
        String firstName = currentUser.getFirstName();
        String lastName = currentUser.getLastName();
        
        MyRestEndpointResponse myRestEndpointResponse = new MyRestEndpointResponse();
        myRestEndpointResponse.setFullName(firstName + " " + lastName);
        myRestEndpointResponse.setTaskCount(taskCount);
        return myRestEndpointResponse;

    }

    private static final class MyRestEndpointResponse {

    	private String fullName = StringUtils.EMPTY;
        private long taskCount = -1;
        private String infoKeys = StringUtils.EMPTY;
        
        @SuppressWarnings("unused")
		public String getFullName() {
			return fullName;
		}
		public void setFullName(String fullName) {
			this.fullName = fullName;
		}
		@SuppressWarnings("unused")
		public long getTaskCount() {
			return taskCount;
		}
		public void setTaskCount(long taskCount) {
			this.taskCount = taskCount;
		}
		@SuppressWarnings("unused")
		public String getInfoKeys() {
			return infoKeys;
		}
		@SuppressWarnings("unused")
		public void setInfoKeys(String infoKeys) {
			this.infoKeys = infoKeys;
		}

    }

}