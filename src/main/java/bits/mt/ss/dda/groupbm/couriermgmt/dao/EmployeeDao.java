package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import bits.mt.ss.dda.groupbm.couriermgmt.exception.CommonErrors;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.DaoException;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Employee;
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.EmployeeRequest;

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

  public long upsertEmployee(EmployeeRequest employeeRequest, boolean isInsert) {

    try (Connection connection =
        Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
      try (CallableStatement callableStatement =
          connection.prepareCall("call upsert_employee(?,?,?,?,?,?,?,?,?,?)")) {
        callableStatement.setString(1, null);
        callableStatement.registerOutParameter(1, Types.VARCHAR);
        callableStatement.setBoolean(2, isInsert);
        callableStatement.setLong(3, employeeRequest.getEmployeeId());
        callableStatement.registerOutParameter(3, Types.NUMERIC);
        callableStatement.setLong(4, employeeRequest.getContactNumber());
        callableStatement.setString(5, employeeRequest.getName());
        callableStatement.setString(6, employeeRequest.getBranchCode());
        callableStatement.setString(7, employeeRequest.getAddressLine());
        callableStatement.setLong(8, employeeRequest.getPincode());
        callableStatement.setString(9, employeeRequest.getCity());
        callableStatement.setString(10, employeeRequest.getState());

        callableStatement.execute();

        if (null != callableStatement.getObject(1)) {
          String error = (String) callableStatement.getObject(1);
          throw new DaoException(CommonErrors.FIELD_ERROR, "SP Execution Error", error);
        }
        return ((BigDecimal) callableStatement.getObject(3)).longValue();
      }
    } catch (SQLException e) {
      throw new DaoException(CommonErrors.GLOBAL_ERROR, "SP Execution Error", e.getMessage());
    }
  }
}
