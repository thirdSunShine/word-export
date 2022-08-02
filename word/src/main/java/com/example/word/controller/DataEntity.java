package com.example.word.controller;

/**
 * @Description:
 * @author: kevin.fang
 * @date: 2022/8/2 13:45
 **/
public class DataEntity {

    private String dataIp;

    private String clientName;

    private Long auditTotal;

    private Integer clientIpTotal;

    private Long subAuditTotal;

    public Integer getClientIpTotal() {
        return clientIpTotal;
    }

    public void setClientIpTotal(Integer clientIpTotal) {
        this.clientIpTotal = clientIpTotal;
    }

    public String getDataIp() {
        return dataIp;
    }

    public void setDataIp(String dataIp) {
        this.dataIp = dataIp;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getAuditTotal() {
        return auditTotal;
    }

    public void setAuditTotal(Long auditTotal) {
        this.auditTotal = auditTotal;
    }


    public Long getSubAuditTotal() {
        return subAuditTotal;
    }

    public void setSubAuditTotal(Long subAuditTotal) {
        this.subAuditTotal = subAuditTotal;
    }
}
