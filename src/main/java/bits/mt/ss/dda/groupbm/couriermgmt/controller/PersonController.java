package bits.mt.ss.dda.groupbm.couriermgmt.controller;

import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.OPERATION_API_DESC;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.OPERATION_API_TAG;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants;
import bits.mt.ss.dda.groupbm.couriermgmt.dao.AgentDao;
import bits.mt.ss.dda.groupbm.couriermgmt.dao.CustomerDao;
import bits.mt.ss.dda.groupbm.couriermgmt.dao.EmployeeDao;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.CommonErrors;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.EntityNotFoundException;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Agent;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Customer;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Employee;
import bits.mt.ss.dda.groupbm.couriermgmt.model.base.BaseResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.model.base.Links;
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.AgentRequest;
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.EmployeeRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = OPERATION_API_TAG, description = OPERATION_API_DESC)
public class PersonController {

  @Autowired CustomerDao customerDao;
  @Autowired AgentDao agentDao;
  @Autowired EmployeeDao employeeDao;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = ApplicationConstants.Person.CUSTOMER_RESOURCE_URI + "/{contact-number}")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = ApplicationConstants.HTTP_404_NOT_FOUND)
      })
  @Operation(summary = "Api to get details of customer by id (contact number)")
  public ResponseEntity<BaseResponse<Customer>> getCustomer(
      @PathVariable("contact-number") long contactNum) {

    BaseResponse<Customer> response = new BaseResponse<>();

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    Customer customer = customerDao.getCustomerById(contactNum);

    if (null == customer) {
      throw new EntityNotFoundException(
          CommonErrors.ENTITY_NOT_FOUND, "Customer", "contact number", contactNum);
    }
    response.setData(customer);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = ApplicationConstants.Person.AGENT_RESOURCE_URI + "/{contact-number}")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = ApplicationConstants.HTTP_404_NOT_FOUND)
      })
  @Operation(summary = "Api to get details of agent by id (contact number)")
  public ResponseEntity<BaseResponse<Agent>> getAgent(
      @PathVariable("contact-number") long contactNum) {

    BaseResponse<Agent> response = new BaseResponse<>();

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    Agent agent = agentDao.getAgentById(contactNum);

    if (null == agent) {
      throw new EntityNotFoundException(
          CommonErrors.ENTITY_NOT_FOUND, "Agent", "contact number", contactNum);
    }
    response.setData(agent);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(path = ApplicationConstants.Person.AGENT_RESOURCE_URI)
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "400", description = ApplicationConstants.HTTP_400_BAD_REQUEST)
      })
  @Operation(summary = "Api to add a new agent")
  public ResponseEntity<BaseResponse<AgentRequest>> addAgent(
      @RequestBody AgentRequest addAgentRequest) {

    BaseResponse<AgentRequest> response = new BaseResponse<>();

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    agentDao.upsertAgent(addAgentRequest, true);
    response.setData(addAgentRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(path = ApplicationConstants.Person.AGENT_RESOURCE_URI + "/{contact-number}")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "400", description = ApplicationConstants.HTTP_400_BAD_REQUEST)
      })
  @Operation(summary = "Api to update agent details")
  public ResponseEntity<BaseResponse<AgentRequest>> updateAgent(
      @PathVariable("contact-number") Long contactNumber,
      @RequestBody AgentRequest updateAgentRequest) {

    BaseResponse<AgentRequest> response = new BaseResponse<>();

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    updateAgentRequest.setContactNumber(contactNumber);

    if (null == agentDao.getAgentById(updateAgentRequest.getContactNumber())) {
      throw new EntityNotFoundException(
          CommonErrors.ENTITY_NOT_FOUND,
          "Agent",
          "contact number",
          updateAgentRequest.getContactNumber());
    }

    agentDao.upsertAgent(updateAgentRequest, false);
    response.setData(updateAgentRequest);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = ApplicationConstants.Person.EMPLOYEE_RESOURCE_URI + "/{id}")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = ApplicationConstants.HTTP_404_NOT_FOUND)
      })
  @Operation(summary = "Api to get details of employee by employee Id")
  public ResponseEntity<BaseResponse<Employee>> getEmployee(@PathVariable("id") long id) {

    BaseResponse<Employee> response = new BaseResponse<>();

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    Employee employee = employeeDao.getEmployeeById(id);

    if (null == employee) {
      throw new EntityNotFoundException(CommonErrors.ENTITY_NOT_FOUND, "Employee", "id", id);
    }
    response.setData(employee);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(path = ApplicationConstants.Person.EMPLOYEE_RESOURCE_URI)
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "400", description = ApplicationConstants.HTTP_400_BAD_REQUEST)
      })
  @Operation(summary = "Api to add a new employee")
  public ResponseEntity<BaseResponse<EmployeeRequest>> addEmployee(
      @RequestBody EmployeeRequest addEmployeeRequest) {

    BaseResponse<EmployeeRequest> response = new BaseResponse<>();

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    addEmployeeRequest.setEmployeeId(employeeDao.upsertEmployee(addEmployeeRequest, true));
    response.setData(addEmployeeRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(path = ApplicationConstants.Person.EMPLOYEE_RESOURCE_URI + "/{id}")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "400", description = ApplicationConstants.HTTP_400_BAD_REQUEST)
      })
  @Operation(summary = "Api to update employee details")
  public ResponseEntity<BaseResponse<EmployeeRequest>> updateEmployee(
      @PathVariable("id") Long employeeId, @RequestBody EmployeeRequest updateEmployeeRequest) {

    BaseResponse<EmployeeRequest> response = new BaseResponse<>();

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    updateEmployeeRequest.setEmployeeId(employeeId);

    if (null == employeeDao.getEmployeeById(employeeId)) {
      throw new EntityNotFoundException(
          CommonErrors.ENTITY_NOT_FOUND, "Employee", "id", employeeId);
    }

    employeeDao.upsertEmployee(updateEmployeeRequest, false);
    response.setData(updateEmployeeRequest);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
