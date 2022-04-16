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
      "SELECT * FROM customer c WHERE c.contact_num = ?";

  private static final String INSERT_INTO_CUSTOMER =
      "INSERT INTO customer (contact_num,name,add_line,pincode,city,state) VALUES (?,?,?,?,?,?)";

  private static final String UPDATE_CUSTOMER_BY_ID =
      "UPDATE customer SET name=?,add_line=?,pincode=?,city=?,state=? WHERE contact_num=?";

  @Transactional
  public void upsertCustomer(Customer customer, boolean insert) {
    if (insert) {
      jdbcTemplate.update(
          INSERT_INTO_CUSTOMER,
          customer.getContactNumber(),
          customer.getName(),
          customer.getAddressLine(),
          customer.getPincode(),
          customer.getCity(),
          customer.getState());
    } else {
      jdbcTemplate.update(
          UPDATE_CUSTOMER_BY_ID,
          customer.getName(),
          customer.getAddressLine(),
          customer.getPincode(),
          customer.getCity(),
          customer.getState(),
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
