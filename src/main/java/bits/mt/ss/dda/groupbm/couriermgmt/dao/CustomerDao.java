package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import bits.mt.ss.dda.groupbm.couriermgmt.model.Customer;

@Repository
public class CustomerDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDao.class);

  @Autowired JdbcTemplate jdbcTemplate;

  private static final String SELECT_CUSTOMER_BY_PK =
      "SELECT * FROM customer c WHERE c.contact_num = ?";

  public Customer getCustomerById(long contactNum) {

    Customer customer = null;
    try {
      customer =
          jdbcTemplate.queryForObject(
              SELECT_CUSTOMER_BY_PK, RowMapper::customerRowMapper, contactNum);
    } catch (EmptyResultDataAccessException e) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("No data found {}", e.getMessage());
      }
    }
    return customer;
  }
}
