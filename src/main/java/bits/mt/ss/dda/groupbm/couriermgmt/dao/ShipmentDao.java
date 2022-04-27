package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import bits.mt.ss.dda.groupbm.couriermgmt.enums.Status;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.CommonErrors;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.DaoException;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Shipment;
import bits.mt.ss.dda.groupbm.couriermgmt.model.ShipmentTracker;

@Repository
public class ShipmentDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentDao.class);

  @Autowired JdbcTemplate jdbcTemplate;

  private static final String INSERT_INTO_SHIPMENT =
      "INSERT INTO shipment (shipment_id,consignment_num,customer_id,"
          + "weight_gm,length_cm,width_cm,height_cm,source_pincode,"
          + "dest_address_line,dest_pincode,"
          + "distance_km,booking_amount,"
          + "status) "
          + "VALUES (nextval('seq_shipment_id'),'IN'||lpad(cast (nextval('seq_consignment_num') as varchar),6,'0')||'BM',?,"
          + "?,?,?,?,?,"
          + "?,?,"
          + "?,?,"
          + "?::status)";

  private static final String INSERT_INTO_SHIPMENT_TRACKER =
      "INSERT INTO shipment_tracker (shipment_id,current_branch,next_branch,transport_mode,"
          + "status,status_remarks,creation_datetime,"
          + "employee_id,agent_id) "
          + "VALUES (?,?,?,?::transport_mode,?::status,?,?,?,?)";

  private static final String UPDATE_SHIPMENT_STATUS =
      "UPDATE shipment set status = ?::status where shipment_id = ?";

  private static final String SELECT_SHIPMENT_BY_CONSIGNMENT_NUM =
      "SELECT s.*, mv.city as dest_city, mv.state as dest_state "
          + "FROM shipment s, mv_pincode_city_state mv "
          + "WHERE mv.pincode = s.dest_pincode "
          + "and s.consignment_num = ?";

  private static final String SELECT_SHIPMENT_HISTORY =
      "SELECT s.consignment_num, s.shipment_id, "
          + "b1.branch_code AS current_branch_code, "
          + "b1.branch_name AS current_branch_name, "
          + "b1.pincode AS current_pincode, "
          + "mv1.city AS current_city, "
          + "mv1.state AS current_state, "
          + "b2.branch_code AS next_branch_code, "
          + "b2.branch_name AS next_branch_name, "
          + "b2.pincode AS next_pincode, "
          + "mv2.city AS next_city, "
          + "mv2.state AS next_state, "
          + "st.status, st.status_remarks, st.creation_datetime, "
          + "e.employee_id, e.contact_num AS emp_contact, e.name AS emp_name, "
          + "a.contact_num AS agent_contact, a.name AS agent_name "
          + "FROM shipment_tracker st "
          + "INNER JOIN shipment s "
          + "ON st.shipment_id = s.shipment_id "
          + "LEFT OUTER JOIN branch b1 "
          + "ON st.current_branch = b1.branch_code "
          + "LEFT OUTER JOIN mv_pincode_city_state mv1 "
          + "ON b1.pincode = mv1.pincode "
          + "LEFT OUTER JOIN branch b2 "
          + "ON st.next_branch = b2.branch_code "
          + "LEFT OUTER JOIN mv_pincode_city_state mv2 "
          + "ON b2.pincode = mv2.pincode "
          + "LEFT OUTER JOIN employee e "
          + "ON st.employee_id = e.employee_id "
          + "LEFT OUTER JOIN agent a "
          + "ON st.agent_id = a.contact_num "
          + "WHERE s.consignment_num = ? "
          + "ORDER BY st.shipment_tracker_id DESC";

  @Transactional
  public Shipment createShipment(ShipmentTracker shipmentTracker) {

    Shipment shipment = shipmentTracker.getShipment();

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        con -> {
          PreparedStatement ps =
              con.prepareStatement(
                  INSERT_INTO_SHIPMENT, new String[] {"shipment_id", "consignment_num"});
          ps.setLong(1, shipment.getCustomer().getContactNumber());
          ps.setInt(2, shipment.getWeightInGram());
          ps.setInt(3, shipment.getLengthInCm());
          ps.setInt(4, shipment.getWidthInCm());
          ps.setInt(5, shipment.getHeightInCm());
          ps.setLong(6, shipment.getSourcePincode());
          ps.setString(7, shipment.getDestAddressLine());
          ps.setLong(8, shipment.getDestPincode());
          ps.setLong(9, shipment.getDistanceInKm());
          ps.setDouble(10, shipment.getBookingAmount());
          ps.setString(11, Status.BOOKED.name());
          return ps;
        },
        keyHolder);

    Map<String, Object> generatedKeys = keyHolder.getKeys();

    if (null == generatedKeys) {
      LOGGER.error("shipment_id and consignment_num not present in the key holder map");
      throw new DaoException(CommonErrors.UNHANDLED_EXCEPTION);
    }

    shipment.setShipmentId(((BigDecimal) generatedKeys.get("shipment_id")).longValue());
    shipment.setConsignmentNumber((String) generatedKeys.get("consignment_num"));

    saveShipmentTrackerRecord(shipmentTracker);

    return shipment;
  }

  @Transactional
  public void updateShipmentStatus(ShipmentTracker shipmentTracker) {
    jdbcTemplate.update(
        UPDATE_SHIPMENT_STATUS,
        shipmentTracker.getStatus().name(),
        shipmentTracker.getShipment().getShipmentId());

    saveShipmentTrackerRecord(shipmentTracker);
  }

  public void saveShipmentTrackerRecord(ShipmentTracker shipmentTracker) {
    jdbcTemplate.update(
        INSERT_INTO_SHIPMENT_TRACKER,
        shipmentTracker.getShipment().getShipmentId(),
        null != shipmentTracker.getCurrentBranch()
            ? shipmentTracker.getCurrentBranch().getBranchCode()
            : null,
        null != shipmentTracker.getNexBranch()
            ? shipmentTracker.getNexBranch().getBranchCode()
            : null,
        null != shipmentTracker.getTransportMode()
            ? shipmentTracker.getTransportMode().name()
            : null,
        shipmentTracker.getStatus().name(),
        shipmentTracker.getStatusRemarks(),
        shipmentTracker.getCreationDateTime(),
        null != shipmentTracker.getEmployee()
            ? shipmentTracker.getEmployee().getEmployeeId()
            : null,
        null != shipmentTracker.getAgent() ? shipmentTracker.getAgent().getContactNumber() : null);
  }

  public Shipment getShipmentByConsignmentNumber(String consignmentNum) {

    Shipment shipment = null;
    try {
      shipment =
          jdbcTemplate.queryForObject(
              SELECT_SHIPMENT_BY_CONSIGNMENT_NUM, RowMapper::shipmentRowMapper, consignmentNum);
    } catch (EmptyResultDataAccessException e) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("No data found {}", e.getMessage());
      }
    }
    return shipment;
  }

  public List<ShipmentTracker> getShipmentHistoryByConsignmentNumber(String consignmentNum) {
    return jdbcTemplate.query(
        SELECT_SHIPMENT_HISTORY, RowMapper::shipmentTrackerRowMapper, consignmentNum);
  }
}
