package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import bits.mt.ss.dda.groupbm.couriermgmt.model.Employee;

@Repository
public class EmployeeDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDao.class);

  @Autowired JdbcTemplate jdbcTemplate;

  private static final String SELECT_EMPLOYEE_BY_PK =
      "SELECT * FROM employee e INNER JOIN branch b ON e.branch_code = b.branch_code where e.employee_id = ?";

  public Employee getEmployeeById(long employeeId) {

    Employee employee = null;
    try {
      employee =
          jdbcTemplate.queryForObject(
              SELECT_EMPLOYEE_BY_PK, RowMapper::employeeRowMapper, employeeId);
    } catch (EmptyResultDataAccessException e) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("No data found {}", e.getMessage());
      }
    }
    return employee;
  }
}
