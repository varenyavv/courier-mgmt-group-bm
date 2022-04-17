package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import bits.mt.ss.dda.groupbm.couriermgmt.model.Branch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Distance;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Hop;
import bits.mt.ss.dda.groupbm.couriermgmt.model.PincodeBranch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.RateCard;

@Repository
public class RouterDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(RouterDao.class);

  @Autowired JdbcTemplate jdbcTemplate;

  private static final String SELECT_RATE_CARD_BY_DISTANCE_QUERY =
      "SELECT base_rate, extra_weight_factor FROM rate_card "
          + "WHERE distance_from_km = ? AND distance_to_km = ?";

  private static final String SELECT_BRANCH_CODE_BY_PINCODES =
      "SELECT pincode, branch_code FROM service_pincode WHERE pincode IN (?,?)";

  private static final String SELECT_RANDOM_N_INTERMEDIATE_BRANCHES =
      "SELECT * FROM branch where branch_code NOT IN (?,?) LIMIT ?";

  private static final String INSERT_INTO_DISTANCE =
      "INSERT INTO distance (source_pincode,dest_pincode,distance_km) VALUES (?,?,?)";

  private static final String INSERT_INTO_ROUTE =
      "INSERT INTO route (source_pincode,dest_pincode,hop_counter,next_hop,transport_mode) "
          + "VALUES (?,?,?,?,?::transport_mode)";

  private static final String SELECT_DISTANCE_BETWEEN_SOURCE_AND_DESTINATION =
      "SELECT d.distance_km, d.source_pincode, source.branch_code as source_branch, "
          + "d.dest_pincode, dest.branch_code as dest_branch "
          + "FROM distance d "
          + "INNER JOIN service_pincode source "
          + "ON d.source_pincode=source.pincode "
          + "INNER JOIN service_pincode dest "
          + "ON d.dest_pincode=dest.pincode "
          + "where d.source_pincode=? and  d.dest_pincode=?";

  private static final String SELECT_DISTANCE_BETWEEN_SOURCE_AND_DESTINATION_NO_JOIN =
      "SELECT d.distance_km "
          + "FROM distance d "
          + "where d.source_pincode=? and  d.dest_pincode=?";

  private static final String SELECT_ROUTE_BETWEEN_SOURCE_AND_DESTINATION =
      "SELECT b.branch_code,b.branch_name,"
          + "b.add_line,b.pincode,b.city,b.state,"
          + "r.transport_mode,r.hop_counter "
          + "FROM route r "
          + "INNER JOIN branch b "
          + "ON b.branch_code = r.next_hop "
          + "where r.source_pincode=? "
          + "and  r.dest_pincode=? "
          + "order by r.hop_counter";

  public RateCard getRateCardByDistance(double distanceInKm) {

    if (distanceInKm >= 0 && distanceInKm <= 50) {
      return jdbcTemplate.queryForObject(
          SELECT_RATE_CARD_BY_DISTANCE_QUERY, RowMapper::rateCardRowMapper, 0, 50);
    } else if (distanceInKm > 50 && distanceInKm <= 100) {
      return jdbcTemplate.queryForObject(
          SELECT_RATE_CARD_BY_DISTANCE_QUERY, RowMapper::rateCardRowMapper, 51, 100);
    } else if (distanceInKm > 100 && distanceInKm <= 500) {
      return jdbcTemplate.queryForObject(
          SELECT_RATE_CARD_BY_DISTANCE_QUERY, RowMapper::rateCardRowMapper, 101, 500);
    } else {
      return jdbcTemplate.queryForObject(
          SELECT_RATE_CARD_BY_DISTANCE_QUERY, RowMapper::rateCardRowMapper, 501, 10000);
    }
  }

  public List<PincodeBranch> getSourceAndDestinationBranchByPincodes(
      long sourcePincode, long destPincode) {
    return jdbcTemplate.query(
        SELECT_BRANCH_CODE_BY_PINCODES,
        RowMapper::pincodeBranchRowMapper,
        sourcePincode,
        destPincode);
  }

  public List<Branch> getNRandomIntermediateBranchFromSourceToDest(
      String sourceBranch, String destBranch, int randomCount) {
    return jdbcTemplate.query(
        SELECT_RANDOM_N_INTERMEDIATE_BRANCHES,
        RowMapper::branchRowMapper,
        sourceBranch,
        destBranch,
        randomCount);
  }

  public List<Hop> getRouteBetweenSourceAndDestination(
      long sourcePincode, long destinationPincode) {
    return jdbcTemplate.query(
        SELECT_ROUTE_BETWEEN_SOURCE_AND_DESTINATION,
        RowMapper::hopRowMapper,
        sourcePincode,
        destinationPincode);
  }

  public Distance getDistanceBetweenSourceAndDestination(
      long sourcePincode, long destinationPincode) {

    Distance distance = null;
    try {
      distance =
          jdbcTemplate.queryForObject(
              SELECT_DISTANCE_BETWEEN_SOURCE_AND_DESTINATION,
              RowMapper::distanceRowMapper,
              sourcePincode,
              destinationPincode);
    } catch (EmptyResultDataAccessException e) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("No data found {}", e.getMessage());
      }
    }
    return distance;
  }

  public Long getDistanceInKmBetweenSourceAndDestination(
      long sourcePincode, long destinationPincode) {

    Long distance = null;
    try {
      distance =
          jdbcTemplate.queryForObject(
              SELECT_DISTANCE_BETWEEN_SOURCE_AND_DESTINATION_NO_JOIN,
              Long.class,
              sourcePincode,
              destinationPincode);
    } catch (EmptyResultDataAccessException e) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("No data found {}", e.getMessage());
      }
    }
    return distance;
  }

  @Transactional
  public void saveDistance(long sourcePincode, long destinationPincode, long distanceInKm) {
    jdbcTemplate.update(INSERT_INTO_DISTANCE, sourcePincode, destinationPincode, distanceInKm);
  }

  @Transactional
  public void saveRoute(long sourcePincode, long destinationPincode, List<Hop> hops) {

    hops.forEach(
        hop ->
            jdbcTemplate.update(
                INSERT_INTO_ROUTE,
                sourcePincode,
                destinationPincode,
                hop.getHopCounter(),
                hop.getBranch().getBranchCode(),
                null != hop.getShipVia() ? hop.getShipVia().name() : null));
  }
}
