package com.dynamic_scheduler_automation.dy_sch_au.shared.ports;

import com.dynamic_scheduler_automation.dy_sch_au.shared.dtos.HistoryRecordDTO;

public interface HistoryApi {

    void createHistory(HistoryRecordDTO history);

}