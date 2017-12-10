/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kiroule.vaadin.demo.backend.data.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author sonderaj
 */
@Entity(name = "subscriptions")
//@NamedQueries({
//    @NamedQuery(name = "Subscriptions.findAll", query = "SELECT s FROM Subscriptions s")})
@NamedEntityGraphs({ @NamedEntityGraph(name = "Subscriptions.getSubscriptionsData")})
public class Subscriptions  extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Size(max = 200)
    @Column(name = "name")
    private String name;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "email")
    private String email;
    
    @Column(name = "phone")
    private Long phone;
    @Column(name = "s_date")
    
    private LocalDate sDate;
    @Column(name = "No_Seats")
    private Short noSeats;
    
    @Size(max = 50)
    @Column(name = "Status")
    private String status;
    
    @Column(name = "from_date")    
    private LocalDate fromDate;
    
    @Column(name = "to_date")       
    private LocalDate toDate;
    
    
//    @JoinColumn(name = "listing_id", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.EAGER)
//    private Order orderID;
    
    @Column(name = "listing_id")
    private Long listingId;

    public Subscriptions() {
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public LocalDate getSDate() {
        return sDate;
    }

    public void setSDate(LocalDate sDate) {
        this.sDate = sDate;
    }

    public Short getNoSeats() {
        return noSeats;
    }

    public void setNoSeats(Short noSeats) {
        this.noSeats = noSeats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subscriptions)) {
            return false;
        }
        Subscriptions other = (Subscriptions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kiroule.vaadin.demo.backend.data.entity.Subscriptions[ id=" + id + " ]";
    }

    /**
     * @return the orderID
     */
//    public Order getOrderID() {
//        return orderID;
//    }
//
//    /**
//     * @param orderID the orderID to set
//     */
//    public void setOrderID(Order orderID) {
//        this.orderID = orderID;
//    }

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
    
}
