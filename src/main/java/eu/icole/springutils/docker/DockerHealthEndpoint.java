package eu.icole.springutils.docker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicatorRegistry;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

@Component
@RestControllerEndpoint(id = "docker")
public class DockerHealthEndpoint {

    @Value("${icole.docker.health.indicators ?: list}")
    String indicators;

    @Autowired
    HealthIndicatorRegistry registry;

    @GetMapping(value = "/health", produces = "text/plain")
    public @ResponseBody ResponseEntity dockerHealthEndpoint() {
System.out.println(indicators);
        List<String> selectedIndicators = new LinkedList<>();

        if (indicators == null || indicators.trim().equals("none"))
            return new ResponseEntity<>(HttpStatus.OK);
        else if (indicators.trim().equals("all")) {
            Map<String, HealthIndicator> allIndicators = registry.getAll();
            for (String i : allIndicators.keySet()) {
                selectedIndicators.add(i);
            }
        } else if (indicators.trim().equals("list")) {
            String list = "List of Spring health indicators:\n";
            Map<String, HealthIndicator> allIndicators = registry.getAll();
            for (String i : allIndicators.keySet()) {
                list += i + " - " + allIndicators.get(i).getClass().toString() + "\n";
            }
            return new ResponseEntity<>(list, HttpStatus.OK);

        } else {
            StringTokenizer st = new StringTokenizer(indicators, ",");
            while (st.hasMoreTokens())
                selectedIndicators.add(st.nextToken());
        }


        for (String check : selectedIndicators) {
            HealthIndicator hi = registry.get(check);
            if (hi != null) {
                String status = checkHealthStatus(check, hi);
                if (status != null) {
                    return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public String checkHealthStatus(String id, HealthIndicator hi) {

        Health health = hi.health();

        if (health.getStatus().equals(Status.UP))
            return null;
        String message = id + " - " + health.getStatus().getCode();
        if (health.getStatus().getDescription() != null && health.getStatus().getDescription().trim().equals(""))
            message += " : " + health.getStatus().getDescription();
        message += health.getDetails();
        return message;
    }
}
