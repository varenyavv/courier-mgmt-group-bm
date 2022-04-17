package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
