package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import bits.mt.ss.dda.groupbm.couriermgmt.enums.TransportMode;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Agent;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Branch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Customer;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Distance;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Employee;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Hop;
import bits.mt.ss.dda.groupbm.couriermgmt.model.PincodeBranch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.RateCard;

public class RowMapper {

  private RowMapper() {}

  static RateCard rateCardRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new RateCard(rs.getDouble("base_rate"), rs.getDouble("extra_weight_factor"));
  }

  static PincodeBranch pincodeBranchRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new PincodeBranch(rs.getLong("pincode"), rs.getString("branch_code"));
  }

  static Branch branchRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new Branch(
        rs.getString("branch_code"),
        rs.getString("branch_name"),
        rs.getString("add_line"),
        rs.getLong("pincode"),
        rs.getString("city"),
        rs.getString("state"));
  }

  static Hop hopRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new Hop(
        new Branch(
            rs.getString("branch_code"),
            rs.getString("branch_name"),
            rs.getString("add_line"),
            rs.getLong("pincode"),
            rs.getString("city"),
            rs.getString("state")),
        null != rs.getString("transport_mode")
            ? TransportMode.valueOf(rs.getString("transport_mode"))
            : null,
        rs.getInt("hop_counter"));
  }

  static Distance distanceRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new Distance(
        rs.getLong("source_pincode"),
        rs.getLong("dest_pincode"),
        rs.getString("source_branch"),
        rs.getString("dest_branch"),
        rs.getInt("distance_km"));
  }

  static Agent agentRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new Agent(
        rs.getLong("contact_num"),
        rs.getString("name"),
        rs.getString("add_line"),
        rs.getLong("pincode"),
        rs.getString("city"),
        rs.getString("state"),
        new Branch(
            rs.getString("branch_code"),
            rs.getString("branch_name"),
            rs.getString("add_line"),
            rs.getLong("pincode"),
            rs.getString("city"),
            rs.getString("state")));
  }

  static Employee employeeRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new Employee(
        rs.getLong("employee_id"),
        rs.getLong("contact_num"),
        rs.getString("name"),
        rs.getString("add_line"),
        rs.getLong("pincode"),
        rs.getString("city"),
        rs.getString("state"),
        new Branch(
            rs.getString("branch_code"),
            rs.getString("branch_name"),
            rs.getString("add_line"),
            rs.getLong("pincode"),
            rs.getString("city"),
            rs.getString("state")));
  }

  static Customer customerRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new Customer(
        rs.getLong("contact_num"),
        rs.getString("name"),
        rs.getString("add_line"),
        rs.getLong("pincode"),
        rs.getString("city"),
        rs.getString("state"));
  }
}
