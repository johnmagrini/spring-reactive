package com.cohesion.engine.util;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cohesion.engine.model.InvestmentNodeKey;
import com.cohesion.engine.model.PerformanceIrrStatus;
import com.cohesion.engine.model.ReportingKey;
import com.cohesion.persistence.beans.PerformanceIRR;


public class PerformanceIrrUtil {

  public static Optional<PerformanceIRR> findRecord(ReportingKey reportingKey, InvestmentNodeKey nodeKey,
                                                    Date date, List<PerformanceIRR> returns) {
    return returns.stream()
            .filter(r -> r.getReportingKey().equals(reportingKey))
            .filter(r -> r.getInvestmentNodeKey().equals(nodeKey))
            .filter(r -> r.getEff_date().compareTo(date) == 0)
            .findAny();
  }

  public static List<PerformanceIRR> filterRecords(Date from, Date to, List<PerformanceIRR> performanceIrrs) {
    
    if (from == null || to == null || performanceIrrs == null) {
      return null;
    }

    return performanceIrrs.stream()
            .filter(r -> r.getEff_date().compareTo(from) >= 0)
            .filter(r -> r.getEff_date().compareTo(to) <= 0)
            .collect(Collectors.toList());
  }

  public static List<PerformanceIRR> filterOutExceptionRecords(List<PerformanceIRR> periodIrrs) {
    return periodIrrs.stream()
              .filter(r -> r.getIrr() != null)
              .filter(r -> r.getIrrStatus().equals(PerformanceIrrStatus.OK) || r.getIrrStatus().equals(PerformanceIrrStatus.APPR))
              .collect(Collectors.toList());
  }

}
