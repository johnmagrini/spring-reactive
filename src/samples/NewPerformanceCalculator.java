package com.cohesion.engine.drone.calcs;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

      List<PerformanceIRR> periodIrrs = performanceIrrs.stream()
            .filter(r -> r.getEff_date().compareTo(periodStart) >= 0)  //filter records before window
            .filter(r -> r.getEff_date().compareTo(snapShot.getEff_date()) <= 0) //filter records after window
            .filter(r -> r.getIrr() != null)  //filter null
            .filter(r -> r.getIrrStatus().equals(PerformanceIrrStatus.OK) || r.getIrrStatus().equals(PerformanceIrrStatus.APPR))  //only approved records
            .collect(Collectors.toList());

      BigDecimal startingMv = getMvOnDate(periodStart, periodIrrs);
      snapShot.setPeriodStartMv(period, startingMv);
      
      BigDecimal netFlows = getNetFlows(periodIrrs);
      snapShot.setPeriodNetFlows(period, netFlows);

      BigDecimal linkedReturn = linkReturns(periodIrrs);
      
      if (PerformanceIRR.MAX_IRR.compareTo(linkedReturn) >= 0) {
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
    return periodIrrs.stream()
            .map( r -> r.getIrr())
            .reduce(BigDecimal.ONE, BigDecimal::multiply);

  }

  protected BigDecimal getNetFlows(List<PerformanceIRR> periodIrrs) {
      return periodIrrs.stream()
              .map( r -> r.getFlows())
              .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  protected BigDecimal getMvOnDate(Date date, List<PerformanceIRR> irrList) {
      return irrList.stream()
              .filter(r -> r.getEff_date().compareTo(date) == 0)
              .map(r -> r.getMarket_value())
              .findFirst().orElse(BigDecimal.ZERO);
  }

}
