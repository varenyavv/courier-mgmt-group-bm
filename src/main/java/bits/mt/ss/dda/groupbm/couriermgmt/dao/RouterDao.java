package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import bits.mt.ss.dda.groupbm.couriermgmt.model.Branch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.PincodeBranch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.RateCard;

@Repository
public class RouterDao {

  @Autowired JdbcTemplate jdbcTemplate;

  private static final String SELECT_RATE_CARD_BY_DISTANCE_QUERY =
      "SELECT base_rate, express_mode_factor, extra_weight_factor_per_kg FROM rate_card "
          + "WHERE distance_from_km = ? AND distance_to_km = ?";

  private static final String SELECT_BRANCH_CODE_BY_PINCODES =
      "SELECT pincode, branch_code FROM service_pincode WHERE pincode IN (?,?)";

  private static final String SELECT_RANDOM_N_INTERMEDIATE_BRANCHES =
      "SELECT * FROM branch where branch_code NOT IN (?,?) LIMIT ?";

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
}
