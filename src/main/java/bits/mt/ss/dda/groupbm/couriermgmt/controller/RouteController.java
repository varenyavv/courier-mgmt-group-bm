package bits.mt.ss.dda.groupbm.couriermgmt.controller;

import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.GET_QUOTE_DESC;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.GET_QUOTE_SUMMARY;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.GET_ROUTE_DESC;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.GET_ROUTE_SUMMARY;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.TAG_QUOTE;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.TAG_ROUTE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Route;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Shipment;
import bits.mt.ss.dda.groupbm.couriermgmt.model.base.BaseResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.model.base.Links;
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.GetRouteRequest;
import bits.mt.ss.dda.groupbm.couriermgmt.model.response.GetQuoteResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.service.RandomRouteAllocator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class RouteController {

  @Autowired RandomRouteAllocator randomRouteAllocator;

  @ResponseStatus(HttpStatus.OK)
  @PostMapping(path = ApplicationConstants.Quote.RESOURCE_ROOT_URI)
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
      tags = {TAG_QUOTE},
      summary = GET_QUOTE_SUMMARY,
      description = GET_QUOTE_DESC)
  public ResponseEntity<BaseResponse<GetQuoteResponse>> getQuote(
      @RequestBody GetRouteRequest getQuoteRequest) {

    BaseResponse<GetQuoteResponse> response = new BaseResponse<>();

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    Route route = randomRouteAllocator.findRoute(getShipment(getQuoteRequest));

    response.setData(new GetQuoteResponse(route.getDistance().getDistanceInKm(), route.getCost()));

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping(path = ApplicationConstants.Route.RESOURCE_ROOT_URI)
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
      tags = {TAG_ROUTE},
      summary = GET_ROUTE_SUMMARY,
      description = GET_ROUTE_DESC)
  public ResponseEntity<BaseResponse<Route>> findRoute(
      @RequestBody GetRouteRequest getRouteRequest) {

    BaseResponse<Route> response = new BaseResponse<>();

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    response.setData(randomRouteAllocator.findRoute(getShipment(getRouteRequest)));

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  private Shipment getShipment(GetRouteRequest request) {
    return new Shipment()
        .setSourcePincode(request.getSourcePincode())
        .setDestPincode(request.getDestPincode())
        .setWeightInGram(request.getWeightInGram())
        .setLengthInCm(request.getLengthInCm())
        .setWidthInCm(request.getWidthInCm())
        .setHeightInCm(request.getHeightInCm());
  }
}
