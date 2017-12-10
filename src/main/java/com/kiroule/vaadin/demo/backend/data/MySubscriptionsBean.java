/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kiroule.vaadin.demo.backend.data;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.validation.constraints.Size;

/**
 *
 * @author Rajkiran
 */
public class MySubscriptionsBean 
{
    
    private String name;
    private Long phone;
    private String vehicleBrand;
    private String vehicleNumber;
    private LocalDate fromDate;    
    private LocalDate toDate;
    private String startPoint;
    private LocalTime startTime;
    private String endPoint;
    private Boolean chargeable;    
    private LocalDate inactiveStDt;
    private LocalDate inactiveEndDt;    
    private String status;    
    private Long listingId;
    private Long subscriptionId;
        

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the phone
     */
    public Long getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(Long phone) {
        this.phone = phone;
    }

    /**
     * @return the vehicleBrand
     */
    public String getVehicleBrand() {
        return vehicleBrand;
    }

    /**
     * @param vehicleBrand the vehicleBrand to set
     */
    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    /**
     * @return the startPoint
     */
    public String getStartPoint() {
        return startPoint;
    }

    /**
     * @param startPoint the startPoint to set
     */
    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    /**
     * @return the startTime
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endPoint
     */
    public String getEndPoint() {
        return endPoint;
    }

    /**
     * @param endPoint the endPoint to set
     */
    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    /**
     * @return the chargeable
     */
    public Boolean getChargeable() {
        return chargeable;
    }

    /**
     * @param chargeable the chargeable to set
     */
    public void setChargeable(Boolean chargeable) {
        this.chargeable = chargeable;
    }

    /**
     * @return the vehicleNumber
     */
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    /**
     * @param vehicleNumber the vehicleNumber to set
     */
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    /**
     * @return the inactiveStDt
     */
    public LocalDate getInactiveStDt() {
        return inactiveStDt;
    }

    /**
     * @param inactiveStDt the inactiveStDt to set
     */
    public void setInactiveStDt(LocalDate inactiveStDt) {
        this.inactiveStDt = inactiveStDt;
    }

    /**
     * @return the inactiveEndDt
     */
    public LocalDate getInactiveEndDt() {
        return inactiveEndDt;
    }

    /**
     * @param inactiveEndDt the inactiveEndDt to set
     */
    public void setInactiveEndDt(LocalDate inactiveEndDt) {
        this.inactiveEndDt = inactiveEndDt;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the fromDate
     */
    public LocalDate getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public LocalDate getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the listingId
     */
    public Long getListingId() {
        return listingId;
    }

    /**
     * @param listingId the listingId to set
     */
    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    /**
     * @return the subscriptionId
     */
    public Long getSubscriptionId() {
        return subscriptionId;
    }

    /**
     * @param subscriptionId the subscriptionId to set
     */
    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
    
    
    
}
