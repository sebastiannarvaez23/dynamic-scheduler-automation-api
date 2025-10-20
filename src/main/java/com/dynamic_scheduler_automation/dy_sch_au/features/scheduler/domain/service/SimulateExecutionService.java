package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.service;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.model.Execution;
import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.model.Status;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@Component
public class SimulateExecutionService {
    public void simulate(Execution execution) {
        try {
            long delay = ThreadLocalRandom.current().nextLong(1000L, 15000L + 1);
            Thread.sleep(delay);

            boolean isSuccess = ThreadLocalRandom.current().nextDouble() < 0.6;
            execution.setStatus(isSuccess ? Status.COMPLETED : Status.FAILED);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("La ejecución del proceso {} fue interrumpida durante el delay", execution.getProcess());
        }
    }
}
