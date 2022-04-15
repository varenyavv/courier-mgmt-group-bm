package bits.mt.ss.dda.groupbm.couriermgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "APIs to get details of actors - Employee, Agent and Customer")
public class PersonController {

  @Autowired CustomerDao customerDao;
  @Autowired AgentDao agentDao;
  @Autowired EmployeeDao employeeDao;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = ApplicationConstants.Person.CUSTOMER_RESOURCE_URI + "/{id}")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = ApplicationConstants.HTTP_404_NOT_FOUND)
      })
  @Operation(summary = "Api to get details of customer by id (contact number)")
  public ResponseEntity<BaseResponse<Customer>> getCustomer(@PathVariable("id") long contactNum) {

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
  @GetMapping(path = ApplicationConstants.Person.AGENT_RESOURCE_URI + "/{id}")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = ApplicationConstants.HTTP_404_NOT_FOUND)
      })
  @Operation(summary = "Api to get details of agent by id (contact number)")
  public ResponseEntity<BaseResponse<Agent>> getAgent(@PathVariable("id") long contactNum) {

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

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = ApplicationConstants.Person.EMPLOYEE_RESOURCE_URI + "/{id}")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = ApplicationConstants.HTTP_404_NOT_FOUND)
      })
  @Operation(summary = "Api to get details of employee by id")
  public ResponseEntity<BaseResponse<Employee>> getEmployee(@PathVariable long id) {

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
}
