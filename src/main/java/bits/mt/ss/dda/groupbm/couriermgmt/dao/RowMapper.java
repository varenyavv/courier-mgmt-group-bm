package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import bits.mt.ss.dda.groupbm.couriermgmt.enums.TransportMode;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Branch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Distance;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Hop;
import bits.mt.ss.dda.groupbm.couriermgmt.model.PincodeBranch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.RateCard;

public class RowMapper {

  private RowMapper() {}

  static RateCard rateCardRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new RateCard(rs.getDouble("base_rate"), rs.getDouble("extra_weight_factor_per_kg"));
  }

  static PincodeBranch pincodeBranchRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new PincodeBranch(rs.getLong("pincode"), rs.getString("branch_code"));
  }

  static Branch branchRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new Branch(
        rs.getString("branch_code"), rs.getString("branch_name"), rs.getString("branch_address"));
  }

  static Hop hopRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new Hop(
        new Branch(
            rs.getString("branch_code"),
            rs.getString("branch_name"),
            rs.getString("branch_address")),
        TransportMode.valueOf(rs.getString("transportation_mode")),
        rs.getInt(rs.getString("hop_counter")));
  }

  static Distance distanceRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new Distance(
        rs.getLong("source_pincode"),
        rs.getLong("destination_pincode"),
        rs.getString("source_branch"),
        rs.getString("dest_branch"),
        rs.getInt("distance_in_km"));
  }
}
