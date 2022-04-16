package bits.mt.ss.dda.groupbm.couriermgmt.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Route;
import bits.mt.ss.dda.groupbm.couriermgmt.model.base.BaseResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.model.base.Links;
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.BookShipmentRequest;
import bits.mt.ss.dda.groupbm.couriermgmt.model.response.BookShipmentResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.service.ShipmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "APIs related to Shipment lifecycle")
@RequestMapping(path = ApplicationConstants.Shipment.SHIPMENT_RESOURCE_BASE_URI)
public class ShipmentController {

  @Autowired ShipmentService shipmentService;

  @ResponseStatus(HttpStatus.OK)
  @PostMapping(path = ApplicationConstants.Shipment.BOOK_SHIPMENT_URI)
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "400", description = ApplicationConstants.HTTP_400_BAD_REQUEST),
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "403", description = ApplicationConstants.HTTP_403_FORBIDDEN),
        @ApiResponse(
            responseCode = "409",
            description = ApplicationConstants.HTTP_422_UNPROCESSABLE_ENTITY)
      })
  @Operation(
      summary = "Api to book a shipment.",
      description =
          "Agents use this API to book a shipment after receiving the booking amount from the customer. "
              + "Once booked, it returns a shipment Id and a consignment number "
              + "using which customers can track this shipment.")
  public ResponseEntity<BaseResponse<BookShipmentResponse>> bookShipment(
      @RequestHeader long agentContactNumber,
      @RequestBody @Valid BookShipmentRequest bookShipmentRequest) {

    BaseResponse<BookShipmentResponse> response = new BaseResponse<>();

    Route routeToFollow =
        shipmentService.validateBookShipmentRequest(agentContactNumber, bookShipmentRequest);

    BookShipmentResponse bookShipmentResponse =
        shipmentService.bookShipment(agentContactNumber, bookShipmentRequest, routeToFollow);

    response.setData(bookShipmentResponse);

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
