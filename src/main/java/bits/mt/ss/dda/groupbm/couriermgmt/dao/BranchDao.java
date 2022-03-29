package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import bits.mt.ss.dda.groupbm.couriermgmt.model.Branch;

@Repository
public class BranchDao {

  @Autowired JdbcTemplate jdbcTemplate;

  private static final String SELECT_BRANCH_BY_BRANCH_CODE =
      "SELECT * FROM branch where branch_code = ?";

  public Branch getBranchByBranchCode(String branchCode) {
    return jdbcTemplate.queryForObject(
        SELECT_BRANCH_BY_BRANCH_CODE, RowMapper::branchRowMapper, branchCode);
  }
}
