package com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.factory;

import com.dynamic_scheduler_automation.dy_sch_au.features.scheduler.base.AbstractSchedule;

@FunctionalInterface
public interface ScheduleFactory {
	
    AbstractSchedule create(Long empresaId);
    
}