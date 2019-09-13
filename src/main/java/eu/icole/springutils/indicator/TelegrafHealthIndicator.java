package eu.icole.springutils.indicator;

import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.ProcessInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicatorRegistry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelegrafHealthIndicator implements HealthIndicator {

    public TelegrafHealthIndicator(@Autowired HealthIndicatorRegistry registry){
        registry.register("telegraf", this);
    }
    @Override
    public Health health() {

        List<ProcessInfo> processesList = JProcesses.getProcessList();
        for(ProcessInfo info: processesList){
            if(info.getName().toLowerCase().equals("telegraf"))
                return Health.up().build();
        }
        return Health.down().status("Telegraf not working").build();
    }
}
