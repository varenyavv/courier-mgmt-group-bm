package bits.mt.ss.dda.groupbm.couriermgmt.dao;

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
import bits.mt.ss.dda.groupbm.couriermgmt.model.Agent;
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.AgentRequest;

@Repository
public class AgentDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(AgentDao.class);

  @Autowired JdbcTemplate jdbcTemplate;

  private static final String SELECT_AGENT_BY_PK =
      "SELECT * FROM agent a INNER JOIN branch b ON a.branch_code = b.branch_code where a.contact_num = ?";

  public Agent getAgentById(long contactNum) {

    Agent agent = null;
    try {
      agent =
          jdbcTemplate.queryForObject(SELECT_AGENT_BY_PK, RowMapper::agentRowMapper, contactNum);
    } catch (EmptyResultDataAccessException e) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("No data found {}", e.getMessage());
      }
    }
    return agent;
  }

  public void upsertAgent(AgentRequest agentRequest, boolean isInsert) {

    try (Connection connection =
        Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
      try (CallableStatement callableStatement =
          connection.prepareCall("call upsert_agent(?,?,?,?,?,?,?,?,?)")) {
        callableStatement.setString(1, null);
        callableStatement.registerOutParameter(1, Types.VARCHAR);
        callableStatement.setBoolean(2, isInsert);
        callableStatement.setLong(3, agentRequest.getContactNumber());
        callableStatement.setString(4, agentRequest.getName());
        callableStatement.setString(5, agentRequest.getBranchCode());
        callableStatement.setString(6, agentRequest.getAddressLine());
        callableStatement.setLong(7, agentRequest.getPincode());
        callableStatement.setString(8, agentRequest.getCity());
        callableStatement.setString(9, agentRequest.getState());

        callableStatement.execute();

        if (null != callableStatement.getObject(1)) {
          String error = (String) callableStatement.getObject(1);
          throw new DaoException(CommonErrors.FIELD_ERROR, "SP Execution Error", error);
        }
      }
    } catch (SQLException e) {
      throw new DaoException(CommonErrors.GLOBAL_ERROR, "SP Execution Error", e.getMessage());
    }
  }
}
