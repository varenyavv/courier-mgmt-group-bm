package bits.mt.ss.dda.groupbm.couriermgmt.controller;

import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.ADD_BRANCH_DESC;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.ADD_BRANCH_SUMMARY;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.SERVICE_PINCODE_DESC;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.SERVICE_PINCODE_SUMMARY;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.TAG_BRANCH;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.TAG_PINCODES;
import static bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants.Documentation.UPDATE_BRANCH_SUMMARY;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import bits.mt.ss.dda.groupbm.couriermgmt.constants.ApplicationConstants;
import bits.mt.ss.dda.groupbm.couriermgmt.dao.BranchDao;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.CommonErrors;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.EntityNotFoundException;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Branch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.base.BaseResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.model.base.Links;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(path = ApplicationConstants.Branch.BRANCH_RESOURCE_BASE_URI)
public class BranchController {

  @Autowired BranchDao branchDao;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "400", description = ApplicationConstants.HTTP_400_BAD_REQUEST)
      })
  @Operation(
      tags = {TAG_BRANCH},
      summary = ADD_BRANCH_SUMMARY,
      description = ADD_BRANCH_DESC)
  public ResponseEntity<BaseResponse<Branch>> addBranch(@RequestBody Branch createBranchRequest) {

    BaseResponse<Branch> response = new BaseResponse<>();

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    createBranchRequest.setBranchCode(branchDao.upsertBranch(createBranchRequest, true));
    response.setData(createBranchRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(path = "/{branch-code}")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "400", description = ApplicationConstants.HTTP_400_BAD_REQUEST)
      })
  @Operation(
      tags = {TAG_BRANCH},
      summary = UPDATE_BRANCH_SUMMARY)
  public ResponseEntity<BaseResponse<Branch>> updateBranch(
      @PathVariable("branch-code") String branchCode, @RequestBody Branch updateBranchRequest) {

    BaseResponse<Branch> response = new BaseResponse<>();

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));
    updateBranchRequest.setBranchCode(branchCode);

    if (null == branchDao.getBranchByBranchCode(branchCode)) {
      throw new EntityNotFoundException(
          CommonErrors.ENTITY_NOT_FOUND, "Branch", "branch code", branchCode);
    }

    branchDao.upsertBranch(updateBranchRequest, false);
    response.setData(updateBranchRequest);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/{branch-code}/pincode")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "401",
            description = ApplicationConstants.HTTP_401_UNAUTHORIZED),
        @ApiResponse(responseCode = "400", description = ApplicationConstants.HTTP_400_BAD_REQUEST)
      })
  @Operation(
      tags = {TAG_PINCODES},
      summary = SERVICE_PINCODE_SUMMARY,
      description = SERVICE_PINCODE_DESC)
  public ResponseEntity<BaseResponse<List<Long>>> addServicePincodes(
      @PathVariable("branch-code") String branchCode,
      @RequestBody @Valid
          List<
                  @NotEmpty
                  @Range(min = 100000L, max = 999999L, message = "Valid pincode is required.") Long>
              servicePincodes) {

    if (null == branchDao.getBranchByBranchCode(branchCode)) {
      throw new EntityNotFoundException(
          CommonErrors.ENTITY_NOT_FOUND, "Branch", "branch code", branchCode);
    }

    BaseResponse<List<Long>> response = new BaseResponse<>();

    response.setLinks(
        new Links(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath()));

    branchDao.addServicePincodes(servicePincodes, branchCode);
    response.setData(servicePincodes);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
