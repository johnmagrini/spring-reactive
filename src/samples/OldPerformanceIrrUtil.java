package com.cohesion.engine.util;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.cohesion.engine.model.InvestmentNodeKey;
import com.cohesion.engine.model.PerformanceIrrStatus;
import com.cohesion.engine.model.ReportingKey;
import com.cohesion.persistence.beans.PerformanceIRR;


public class PerformanceIrrUtil {

  public static PerformanceIRR findRecord(ReportingKey reportingKey, InvestmentNodeKey nodeKey,
      Date date, List<PerformanceIRR> returns) {
    for (PerformanceIRR performanceIrr : returns) {
      if (performanceIrr.getReportingKey().equals(reportingKey)
          && performanceIrr.getInvestmentNodeKey().equals(nodeKey)
          && new Date(performanceIrr.getEff_date().getTime()).equals(date)) {
        return performanceIrr;
      }
    }
    return null;
  }

  public static List<PerformanceIRR> filterRecords(Date from, Date to, List<PerformanceIRR> performanceIrrs) {
    
    if (from == null || to == null || performanceIrrs == null) {
      return null;
    }
    
    List<PerformanceIRR> results = new LinkedList<PerformanceIRR>();
    
    for (PerformanceIRR performanceIrr : performanceIrrs) {
      Date recordDate = performanceIrr.getEff_date();
      if ((from.before(recordDate) || from.equals(recordDate))
          && to.after(recordDate) || to.equals(recordDate)) {
        results.add(performanceIrr);
      }
    }

    return results;
  }

  public static List<PerformanceIRR> filterOutExceptionRecords(List<PerformanceIRR> periodIrrs) {
    List<PerformanceIRR> results = new LinkedList<PerformanceIRR>();
    for (PerformanceIRR irr : periodIrrs) {
      if (irr.getIrr() != null 
          && ( irr.getIrrStatus().equals(PerformanceIrrStatus.OK)
              || irr.getIrrStatus().equals(PerformanceIrrStatus.APPR))) {
        results.add(irr);
      }
    }
    return results;
  }

}
