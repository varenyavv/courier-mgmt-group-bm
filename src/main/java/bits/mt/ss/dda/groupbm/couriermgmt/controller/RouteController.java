package bits.mt.ss.dda.groupbm.couriermgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Route;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Shipment;
import bits.mt.ss.dda.groupbm.couriermgmt.model.base.BaseResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.model.base.Links;
import bits.mt.ss.dda.groupbm.couriermgmt.service.RandomRouteAllocator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = ApplicationConstants.Route.RESOURCE_ROOT_URI)
@Tag(name = "Routing Controller API")
public class RouteController {

  @Autowired RandomRouteAllocator randomRouteAllocator;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
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
  @Operation(summary = "Api to find route between source and destination pin codes")
  public ResponseEntity<BaseResponse<Route>> findRoute(@RequestBody Shipment shipment) {

    BaseResponse<Route> response = new BaseResponse<>();

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    response.setData(randomRouteAllocator.findRoute(shipment));

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
