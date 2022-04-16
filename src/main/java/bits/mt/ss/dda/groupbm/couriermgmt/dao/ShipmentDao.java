package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
          + "weight_gm,length_cm,width_cm,height_cm,"
          + "dest_add_line,dest_pincode,dest_city,dest_state,"
          + "distance_km,booking_amount,"
          + "status) "
          + "VALUES (nextval('shipment_id'),'IN'||lpad(cast (nextval('consignment_num') as varchar),6,'0')||'BM',?,"
          + "?,?,?,?,"
          + "?,?,?,?,"
          + "?,?,"
          + "?::status)";

  private static final String INSERT_INTO_SHIPMENT_TRACKER =
      "INSERT INTO shipment_tracker (shipment_id,current_branch,next_branch,transport_mode,"
          + "status,status_remarks,creation_datetime,"
          + "employee_id,agent_id) "
          + "VALUES (?,?,?,?::transport_mode,?::status,?,?,?,?)";

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
          ps.setString(6, shipment.getDestAddressLine());
          ps.setLong(7, shipment.getDestPincode());
          ps.setString(8, shipment.getDestCity());
          ps.setString(9, shipment.getDestState());
          ps.setLong(10, shipment.getDistanceInKm());
          ps.setDouble(11, shipment.getBookingAmount());
          ps.setString(12, Status.BOOKED.name());
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
}
