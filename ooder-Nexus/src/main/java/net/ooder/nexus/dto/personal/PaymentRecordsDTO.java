package net.ooder.nexus.dto.personal;

import net.ooder.nexus.domain.personal.model.PaymentRecord;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class PaymentRecordsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer total;
    private List<PaymentRecord> records;
    private PaymentStatisticsDTO statistics;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<PaymentRecord> getRecords() {
        return records;
    }

    public void setRecords(List<PaymentRecord> records) {
        this.records = records;
    }

    public PaymentStatisticsDTO getStatistics() {
        return statistics;
    }

    public void setStatistics(PaymentStatisticsDTO statistics) {
        this.statistics = statistics;
    }

    public static class PaymentStatisticsDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private BigDecimal totalIncome;
        private BigDecimal totalExpense;

        public BigDecimal getTotalIncome() {
            return totalIncome;
        }

        public void setTotalIncome(BigDecimal totalIncome) {
            this.totalIncome = totalIncome;
        }

        public BigDecimal getTotalExpense() {
            return totalExpense;
        }

        public void setTotalExpense(BigDecimal totalExpense) {
            this.totalExpense = totalExpense;
        }
    }
}
