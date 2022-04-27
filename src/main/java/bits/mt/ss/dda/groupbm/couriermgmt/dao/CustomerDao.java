package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import bits.mt.ss.dda.groupbm.couriermgmt.model.Customer;

@Repository
public class CustomerDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDao.class);

  @Autowired JdbcTemplate jdbcTemplate;

  private static final String SELECT_CUSTOMER_BY_PK =
      "SELECT * FROM customer c, mv_pincode_city_state mv "
          + "WHERE c.pincode = mv.pincode "
          + "and c.contact_num = ?";

  private static final String INSERT_INTO_CUSTOMER =
      "INSERT INTO customer (contact_num,name,address_line,pincode) VALUES (?,?,?,?)";

  private static final String UPDATE_CUSTOMER_BY_ID =
      "UPDATE customer SET name=?,address_line=?,pincode=? WHERE contact_num=?";

  @Transactional
  public void upsertCustomer(Customer customer, boolean insert) {
    if (insert) {
      jdbcTemplate.update(
          INSERT_INTO_CUSTOMER,
          customer.getContactNumber(),
          customer.getName(),
          customer.getAddressLine(),
          customer.getPincode());
    } else {
      jdbcTemplate.update(
          UPDATE_CUSTOMER_BY_ID,
          customer.getName(),
          customer.getAddressLine(),
          customer.getPincode(),
          customer.getContactNumber());
    }
  }

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
