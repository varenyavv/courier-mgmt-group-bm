package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.ObjectUtils;

import bits.mt.ss.dda.groupbm.couriermgmt.enums.Status;
import bits.mt.ss.dda.groupbm.couriermgmt.enums.TransportMode;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Agent;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Branch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Customer;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Distance;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Employee;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Hop;
import bits.mt.ss.dda.groupbm.couriermgmt.model.PincodeBranch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.RateCard;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Shipment;
import bits.mt.ss.dda.groupbm.couriermgmt.model.ShipmentTracker;

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

  static Shipment shipmentRowMapper(ResultSet rs, int rowNum) throws SQLException {
    return new Shipment()
        .setShipmentId(rs.getLong("shipment_id"))
        .setConsignmentNumber(rs.getString("consignment_num"))
        .setSourcePincode(rs.getLong("source_pincode"))
        .setDestPincode(rs.getLong("dest_pincode"))
        .setStatus(Status.valueOf(rs.getString("status")));
  }

  static ShipmentTracker shipmentTrackerRowMapper(ResultSet rs, int rowNum) throws SQLException {

    ShipmentTracker shipmentTracker =
        new ShipmentTracker()
            .setShipment(
                new Shipment()
                    .setConsignmentNumber(rs.getString("consignment_num"))
                    .setShipmentId(rs.getLong("shipment_id")))
            .setStatus(Status.valueOf(rs.getString("status")))
            .setStatusRemarks(rs.getString("status_remarks"))
            .setCreationDateTime(rs.getTimestamp("creation_datetime").toLocalDateTime());

    if (ObjectUtils.isNotEmpty(rs.getString("current_branch_code"))) {
      shipmentTracker.setCurrentBranch(
          new Branch(
              rs.getString("current_branch_code"),
              rs.getString("current_branch_name"),
              null,
              rs.getLong("current_pincode"),
              rs.getString("current_city"),
              rs.getString("current_state")));
    }

    if (ObjectUtils.isNotEmpty(rs.getString("next_branch_code"))) {
      shipmentTracker.setNexBranch(
          new Branch(
              rs.getString("next_branch_code"),
              rs.getString("next_branch_name"),
              null,
              rs.getLong("next_pincode"),
              rs.getString("next_city"),
              rs.getString("next_state")));
    }

    if (ObjectUtils.isNotEmpty(rs.getLong("employee_id"))) {
      shipmentTracker.setEmployee(
          new Employee()
              .setEmployeeId(rs.getLong("employee_id"))
              .setContactNumber(rs.getLong("emp_contact"))
              .setName(rs.getString("emp_name")));
    }

    if (ObjectUtils.isNotEmpty(rs.getLong("agent_contact"))) {
      shipmentTracker.setEmployee(
          new Employee()
              .setContactNumber(rs.getLong("agent_contact"))
              .setName(rs.getString("agent_name")));
    }

    return shipmentTracker;
  }
}
