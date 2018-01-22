package com.cohesion.engine.drone.calcs;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cohesion.engine.ifc.Reportable;
import com.cohesion.engine.model.PerformanceIrrStatus;
import com.cohesion.engine.model.PerformancePeriod;
import com.cohesion.engine.service.ifc.DateService;
import com.cohesion.engine.service.ifc.PerformanceService;
import com.cohesion.engine.service.ifc.ReportableService;
import com.cohesion.engine.util.PerformanceIrrUtil;
import com.cohesion.persistence.beans.PerformanceIRR;
import com.cohesion.persistence.beans.PerformanceSnapShot;

@Service
public class PerformanceCalculator {
  
  private static final Logger logger = Logger.getLogger(PerformanceCalculator.class);

  
  @Autowired
  ReportableService reportableService;
  @Autowired
  PerformanceService performanceService;
  @Autowired
  DateService dateService;
    
 
  public PerformanceSnapShot calculateSnapShot(Reportable reportable, PerformanceSnapShot snapShot, 
      List<PerformanceIRR> performanceIrrs) {

    for (PerformancePeriod period : PerformancePeriod.values()) {
      
      Date periodStart = dateService.getPerfPeriodStart(snapShot.getEff_date(), reportable.getStartDate(), period);
      
      List<PerformanceIRR> periodIrrs = PerformanceIrrUtil.filterRecords(periodStart, 
          snapShot.getEff_date(), performanceIrrs);
      
      periodIrrs = PerformanceIrrUtil.filterOutExceptionRecords(periodIrrs);

      BigDecimal startingMv = getMvOnDate(periodStart, periodIrrs);
      snapShot.setPeriodStartMv(period, startingMv);
      
      BigDecimal netFlows = getNetFlows(periodIrrs);
      snapShot.setPeriodNetFlows(period, netFlows);

      BigDecimal linkedReturn = linkReturns(periodIrrs);
      
      if (PerformanceIRR.MAX_IRR.compareTo(linkedReturn) == 1) {
        snapShot.setPeriodReturn(period, linkedReturn);  
      } else {
        snapShot.setPeriodReturn(period, PerformanceIRR.MAX_IRR);
      }
      
      
    }
    
    BigDecimal endingMv = getMvOnDate(snapShot.getEff_date(), performanceIrrs);
    snapShot.setMarket_value(endingMv);
    
    snapShot.setLast_updated_time(new Timestamp(System.currentTimeMillis()));
    snapShot.setLast_updated_user("PerformanceCalculator");
    
    return snapShot;
  }

  protected BigDecimal linkReturns(List<PerformanceIRR> periodIrrs) {
    
    BigDecimal irr = BigDecimal.valueOf(1);
    for (PerformanceIRR performanceIrr : periodIrrs) {
      if (performanceIrr.getIrrStatus() != null
          && (performanceIrr.getIrrStatus().equals(PerformanceIrrStatus.OK)
          || performanceIrr.getIrrStatus().equals(PerformanceIrrStatus.APPR))) {
        irr = irr.multiply(performanceIrr.getIrr()); 
      }
    }
    return irr;
  }

  protected BigDecimal getNetFlows(List<PerformanceIRR> periodIrrs) {
    BigDecimal netflows = BigDecimal.valueOf(0);    
    for (PerformanceIRR performanceIrr : periodIrrs) {
      netflows = netflows.add(performanceIrr.getFlows());
    }
    return netflows;
  }

  protected BigDecimal getMvOnDate(Date date, List<PerformanceIRR> irrList) {
    List<PerformanceIRR> filteredRecordList = 
        PerformanceIrrUtil.filterRecords(date, date, irrList);
    if (filteredRecordList.size() == 1) {
      return filteredRecordList.get(0).getMarket_value();
    } else {
      return BigDecimal.ZERO;
    }
  }

}
