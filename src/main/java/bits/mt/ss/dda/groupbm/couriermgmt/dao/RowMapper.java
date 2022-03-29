package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import bits.mt.ss.dda.groupbm.couriermgmt.model.Branch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.PincodeBranch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.RateCard;

public class RowMapper {

  private RowMapper() {}

  static RateCard rateCardRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new RateCard(
        rs.getDouble("base_rate"),
        rs.getDouble("express_mode_factor"),
        rs.getDouble("extra_weight_factor_per_kg"));
  }

  static PincodeBranch pincodeBranchRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new PincodeBranch(rs.getLong("pincode"), rs.getString("branch_code"));
  }

  static Branch branchRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new Branch(
        rs.getString("branch_code"), rs.getString("branch_name"), rs.getString("branch_address"));
  }
}
