package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import bits.mt.ss.dda.groupbm.couriermgmt.exception.CommonErrors;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.DaoException;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Branch;

@Repository
public class BranchDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(BranchDao.class);

  @Autowired JdbcTemplate jdbcTemplate;

  private static final String SELECT_BRANCH_BY_BRANCH_CODE =
      "SELECT * FROM branch where branch_code = ?";

  private static final String SELECT_BRANCH_CODE_BY_PINCODE =
      "SELECT branch_code FROM service_pincode where pincode = ?";

  public Branch getBranchByBranchCode(String branchCode) {

    Branch branch = null;
    try {
      branch =
          jdbcTemplate.queryForObject(
              SELECT_BRANCH_BY_BRANCH_CODE, RowMapper::branchRowMapper, branchCode);
    } catch (EmptyResultDataAccessException e) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("No data found {}", e.getMessage());
      }
    }
    return branch;
  }

  public String getBranchCodeByPincode(long pincode) {
    return jdbcTemplate.queryForObject(SELECT_BRANCH_CODE_BY_PINCODE, String.class, pincode);
  }

  public String upsertBranch(Branch createBranchRequest, boolean isInsert) {

    try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
      try (CallableStatement callableStatement =
          connection.prepareCall("call upsert_branch(?,?,?,?,?,?,?,?)")) {
        callableStatement.setString(1, null);
        callableStatement.registerOutParameter(1, Types.VARCHAR);
        callableStatement.setBoolean(2, isInsert);
        callableStatement.setString(3, isInsert ? null : createBranchRequest.getBranchCode());
        callableStatement.registerOutParameter(3, Types.VARCHAR);
        callableStatement.setString(4, createBranchRequest.getBranchName());
        callableStatement.setString(5, createBranchRequest.getAddressLine());
        callableStatement.setLong(6, createBranchRequest.getPincode());
        callableStatement.setString(7, createBranchRequest.getCity());
        callableStatement.setString(8, createBranchRequest.getState());

        callableStatement.execute();

        if (null != callableStatement.getObject(1)) {
          String error = (String) callableStatement.getObject(1);
          throw new DaoException(CommonErrors.FIELD_ERROR, "SP Execution Error", error);
        }
        return (String) callableStatement.getObject(3);
      }
    } catch (SQLException e) {
      throw new DaoException(CommonErrors.GLOBAL_ERROR, "SP Execution Error", e.getMessage());
    }
  }
}
