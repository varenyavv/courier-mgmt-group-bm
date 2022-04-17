package bits.mt.ss.dda.groupbm.couriermgmt.controller;

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
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = ApplicationConstants.Branch.BRANCH_RESOURCE_BASE_URI)
@Tag(name = "APIs to Add/Update/Get details of entities - Branch, Employee, Agent and Customer")
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
  @Operation(summary = "Api to add a new branch")
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
  @Operation(summary = "Api to update a branch")
  public ResponseEntity<BaseResponse<Branch>> addBranch(
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
}
