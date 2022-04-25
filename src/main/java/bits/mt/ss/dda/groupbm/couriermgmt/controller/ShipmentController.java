package bits.mt.ss.dda.groupbm.couriermgmt.controller;

import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.BOOK_SHIPMENT_DESC;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.BOOK_SHIPMENT_SUMMARY;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.DELIVER_SHIPMENT_DESC;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.DELIVER_SHIPMENT_SUMMARY;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.FORWARD_SHIPMENT_SUMMARY;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.FORWARD_SHIPMET_DESC;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.SHIPMENT_HISTORY_API_DESC;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.SHIPMENT_HISTORY_API_SUMMARY;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.TAG_SHIPMENT_HISTORY;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.TAG_SHIPMENT_LIFECYCLE;
import static org.hibernate.validator.internal.util.Contracts.assertNotEmpty;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.DeliverShipmentRequest;
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.ForwardShipmentRequest;
import bits.mt.ss.dda.groupbm.couriermgmt.model.response.BookShipmentResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.model.response.DeliverShipmentResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.model.response.ForwardShipmentResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.model.response.TrackShipmentResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.service.ShipmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
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
      tags = {TAG_SHIPMENT_LIFECYCLE},
      summary = BOOK_SHIPMENT_SUMMARY,
      description = BOOK_SHIPMENT_DESC)
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
      tags = {TAG_SHIPMENT_LIFECYCLE},
      summary = FORWARD_SHIPMENT_SUMMARY,
      description = FORWARD_SHIPMET_DESC)
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

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(path = ApplicationConstants.Shipment.DELIVER_SHIPMENT_URI)
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "404", description = ApplicationConstants.HTTP_404_NOT_FOUND),
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "403", description = ApplicationConstants.HTTP_403_FORBIDDEN)
      })
  @Operation(
      tags = {TAG_SHIPMENT_LIFECYCLE},
      summary = DELIVER_SHIPMENT_SUMMARY,
      description = DELIVER_SHIPMENT_DESC)
  public ResponseEntity<BaseResponse<DeliverShipmentResponse>> deliverShipment(
      @RequestHeader
          @Parameter(
              description =
                  "Contact number of the agent who is going to attempt the shipment's delivery. "
                      + "He should belong to the destination branch.")
          long agentContactNumber,
      @PathVariable("consignment_num") String consignmentNumber,
      @RequestBody @Valid DeliverShipmentRequest deliverShipmentRequest) {

    BaseResponse<DeliverShipmentResponse> response = new BaseResponse<>();

    deliverShipmentRequest.setConsignmentNumber(consignmentNumber);

    shipmentService.validateDeliverShipmentRequest(agentContactNumber, deliverShipmentRequest);

    DeliverShipmentResponse deliverShipmentResponse =
        shipmentService.deliverShipment(agentContactNumber, deliverShipmentRequest);

    response.setData(deliverShipmentResponse);

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = ApplicationConstants.Shipment.TRACK_SHIPMENT_URI)
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "404", description = ApplicationConstants.HTTP_404_NOT_FOUND),
        @ApiResponse(responseCode = "401", description = ApplicationConstants.HTTP_401_UNAUTHORIZED)
      })
  @Operation(
      tags = {TAG_SHIPMENT_HISTORY},
      summary = SHIPMENT_HISTORY_API_SUMMARY,
      description = SHIPMENT_HISTORY_API_DESC)
  public ResponseEntity<BaseResponse<TrackShipmentResponse>> trackShipment(
      @PathVariable("consignment_num") String consignmentNumber) {

    BaseResponse<TrackShipmentResponse> response = new BaseResponse<>();

    TrackShipmentResponse trackShipmentResponse = shipmentService.trackShipment(consignmentNumber);

    response.setData(trackShipmentResponse);

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
