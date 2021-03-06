package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import bits.mt.ss.dda.groupbm.couriermgmt.exception.CommonErrors;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.DaoException;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Branch;

@Repository
public class BranchDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(BranchDao.class);

  @Autowired JdbcTemplate jdbcTemplate;

  private static final String SELECT_BRANCH_BY_BRANCH_CODE =
      "SELECT * FROM branch, mv_pincode_city_state mv "
          + "where branch.pincode = mv.pincode "
          + "and branch_code = ?";

  private static final String SELECT_BRANCH_CODE_BY_PINCODE =
      "SELECT branch_code FROM service_pincode where pincode = ?";

  private static final String INSERT_INTO_SERVICE_PINCODE =
      "INSERT INTO service_pincode (pincode, branch_code) VALUES (?, ?)";

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

  @Transactional
  public void addServicePincodes(List<Long> servicePincodes, String branchCode) {
    try {
      servicePincodes.forEach(
          servicePincode ->
              jdbcTemplate.update(INSERT_INTO_SERVICE_PINCODE, servicePincode, branchCode));
    } catch (Exception e) {
      throw new DaoException(CommonErrors.GLOBAL_ERROR, "DB Error", e.getMessage());
    }
  }

  public String upsertBranch(Branch createBranchRequest, boolean isInsert) {

    try (Connection connection =
        Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
      try (CallableStatement callableStatement =
          connection.prepareCall("call upsert_branch(?,?,?,?,?,?)")) {
        callableStatement.setString(1, null);
        callableStatement.registerOutParameter(1, Types.VARCHAR);
        callableStatement.setBoolean(2, isInsert);
        callableStatement.setString(3, isInsert ? null : createBranchRequest.getBranchCode());
        callableStatement.registerOutParameter(3, Types.VARCHAR);
        callableStatement.setString(4, createBranchRequest.getBranchName());
        callableStatement.setString(5, createBranchRequest.getAddressLine());
        callableStatement.setLong(6, createBranchRequest.getPincode());

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
