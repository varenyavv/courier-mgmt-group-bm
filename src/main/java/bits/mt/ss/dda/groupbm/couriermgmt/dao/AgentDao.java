package bits.mt.ss.dda.groupbm.couriermgmt.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import bits.mt.ss.dda.groupbm.couriermgmt.model.Agent;

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
}
