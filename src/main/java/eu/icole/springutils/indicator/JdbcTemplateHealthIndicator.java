package eu.icole.springutils.indicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicatorRegistry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class JdbcTemplateHealthIndicator implements HealthIndicator {

    @Autowired(required = false)
    JdbcTemplate template;

    public JdbcTemplateHealthIndicator(HealthIndicatorRegistry registry){
        registry.register("jdbcTemplate",this);
    }

    @Override
    public Health health() {

        if(template!=null)
        {
            try {
                Connection connection = template.getDataSource().getConnection();
                connection.close();
            }
            catch (Exception e)
            {
                return Health.down(e).status("Problem with JDBC Template").build();
            }
        }

        return Health.down().status("JDBC Profile not working!").build();
    }
}


