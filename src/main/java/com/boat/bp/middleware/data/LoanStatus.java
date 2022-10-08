package com.boat.bp.middleware.data;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanStatus {

    private String code;
    private String value;
    private boolean active; 
    private boolean closedObligationsMet;
    private boolean closedWrittenOff;
    private boolean closedRescheduled;
    private boolean closed;
    private boolean overpaid;
    
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String isActive() {
        return ""+active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public String isClosedObligationsMet() {
        return ""+closedObligationsMet;
    }
    public void setClosedObligationsMet(boolean closedObligationsMet) {
        this.closedObligationsMet = closedObligationsMet;
    }
    public String isClosedWrittenOff() {
        return ""+closedWrittenOff;
    }
    public void setClosedWrittenOff(boolean closedWrittenOff) {
        this.closedWrittenOff = closedWrittenOff;
    }
    public String isClosedRescheduled() {
        return ""+closedRescheduled;
    }
    public void setClosedRescheduled(boolean closedRescheduled) {
        this.closedRescheduled = closedRescheduled;
    }
    public String isClosed() {
        return ""+closed;
    }
    public void setClosed(boolean closed) {
        this.closed = closed;
    }
    public String isOverpaid() {
        return ""+overpaid;
    }
    public void setOverpaid(boolean overpaid) {
        this.overpaid = overpaid;
    }

    


    
}
