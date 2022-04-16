package bits.mt.ss.dda.groupbm.couriermgmt.controller;

import static org.hibernate.validator.internal.util.Contracts.assertNotEmpty;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Hop;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Route;
import bits.mt.ss.dda.groupbm.couriermgmt.model.base.BaseResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.model.base.Links;
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.BookShipmentRequest;
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.ForwardShipmentRequest;
import bits.mt.ss.dda.groupbm.couriermgmt.model.response.BookShipmentResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.model.response.ForwardShipmentResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.service.ShipmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "APIs related to Shipment lifecycle")
@RequestMapping(path = ApplicationConstants.Shipment.SHIPMENT_RESOURCE_BASE_URI)
public class ShipmentController {

  @Autowired ShipmentService shipmentService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "400", description = ApplicationConstants.HTTP_400_BAD_REQUEST),
        @ApiResponse(responseCode = "401", description = ApplicationConstants.HTTP_401_UNAUTHORIZED)
      })
  @Operation(
      summary = "Api to book a shipment.",
      description =
          "Agents use this API to book a shipment after receiving the booking amount from the customer. "
              + "Once booked, it returns a shipment Id and a consignment number "
              + "using which customers can track this shipment.")
  public ResponseEntity<BaseResponse<BookShipmentResponse>> bookShipment(
      @RequestHeader
          @Parameter(
              description =
                  "Contact number of the agent who is booking the shipment. He should belong to the source branch")
          long agentContactNumber,
      @RequestBody @Valid BookShipmentRequest bookShipmentRequest) {

    BaseResponse<BookShipmentResponse> response = new BaseResponse<>();

    Route routeToFollow =
        shipmentService.validateBookShipmentRequest(agentContactNumber, bookShipmentRequest);

    BookShipmentResponse bookShipmentResponse =
        shipmentService.bookShipment(agentContactNumber, bookShipmentRequest, routeToFollow);

    response.setData(bookShipmentResponse);

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(path = ApplicationConstants.Shipment.FORWARD_SHIPMENT_URI)
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "404", description = ApplicationConstants.HTTP_404_NOT_FOUND),
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "403", description = ApplicationConstants.HTTP_403_FORBIDDEN)
      })
  @Operation(
      summary =
          "Api to forward a shipment either to another branch or to the delivery agent of the destination branch.",
      description =
          "Once a shipment is received at a branch, employees use this API to either forward it to another branch or "
              + "to move to the bucket of delivery agents if the receiving branch is the destination branch of the shipment. "
              + "Route table is used to determine the next hop of the shipment.")
  public ResponseEntity<BaseResponse<ForwardShipmentResponse>> receiveAndForwardShipment(
      @RequestHeader
          @Parameter(
              description =
                  "Employee Id of the employee who is receiving the shipment at a branch. "
                      + "Employee should belong to the same branch where the shipment is being received at.")
          long employeeId,
      @PathVariable("consignment_num") String consignmentNumber,
      @RequestBody @Valid ForwardShipmentRequest forwardShipmentRequest) {

    BaseResponse<ForwardShipmentResponse> response = new BaseResponse<>();

    forwardShipmentRequest.setConsignmentNumber(consignmentNumber);

    List<Hop> routeToFollow =
        shipmentService.validateForwardShipmentRequest(employeeId, forwardShipmentRequest);

    assertNotEmpty(routeToFollow, "Routes must never be empty during this flow execution.");

    ForwardShipmentResponse forwardShipmentResponse =
        shipmentService.forwardShipment(employeeId, forwardShipmentRequest, routeToFollow);
    response.setData(forwardShipmentResponse);

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
