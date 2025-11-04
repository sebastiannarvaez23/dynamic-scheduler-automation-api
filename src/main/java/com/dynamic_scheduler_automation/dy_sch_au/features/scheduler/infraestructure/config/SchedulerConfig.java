package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.infraestructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
public class SchedulerConfig {

    private final Map<String, String> cronExpresiones = new ConcurrentHashMap<>();

    public void updateCron(String procesoId, List<String> companies, String nuevoCron) {
        if (procesoId == null || companies == null || companies.isEmpty() || nuevoCron == null) {
            log.warn("❌ No se puede actualizar CRON: datos incompletos -> procesoId={}, companies={}, cron={}",
                    procesoId, companies, nuevoCron);
            return;
        }

        for (String companyId : companies) {
            if (companyId == null) continue;

            String clave = procesoId + "_" + companyId;
            cronExpresiones.put(clave, nuevoCron);

            log.info("✅ CRON actualizado para proceso={} empresa={} -> {}", procesoId, companyId, nuevoCron);
        }
    }

    public String getCronExpresion(String procesoId, String empresaId) {
        if (procesoId == null || empresaId == null) return null;
        return cronExpresiones.get(procesoId + "_" + empresaId);
    }

}
