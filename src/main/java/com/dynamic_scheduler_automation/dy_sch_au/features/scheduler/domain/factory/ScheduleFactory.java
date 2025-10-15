package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.factory;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.domain.base.AbstractSchedule;

@FunctionalInterface
public interface ScheduleFactory {
	
    AbstractSchedule create(String empresaId);
    
}