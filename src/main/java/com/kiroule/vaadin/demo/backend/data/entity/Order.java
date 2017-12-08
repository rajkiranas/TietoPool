package com.kiroule.vaadin.demo.backend.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToOne;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import javax.persistence.OrderColumn;
import javax.xml.bind.annotation.XmlRootElement;


//@Entity(name = "OrderInfo") // "Order" is a reserved word
@Entity(name = "listing")
//@NamedEntityGraphs({ @NamedEntityGraph(name = "Order.gridData", attributeNodes = { @NamedAttributeNode("customer") }),
//		@NamedEntityGraph(name = "Order.allData", attributeNodes = { @NamedAttributeNode("customer"),
//				@NamedAttributeNode("items"), @NamedAttributeNode("history") }) })
@NamedEntityGraphs({ @NamedEntityGraph(name = "Order.gridData")})

@XmlRootElement

public class Order extends AbstractEntity {

    /**
     * @return the subscriptionsList
     */
    public List<Subscriptions> getSubscriptionsList() {
        return subscriptionsList;
    }

    /**
     * @param subscriptionsList the subscriptionsList to set
     */
    public void setSubscriptionsList(List<Subscriptions> subscriptionsList) {
        this.subscriptionsList = subscriptionsList;
    }

	
    @Size(max = 200)
    @Column(name = "name")
    private String name;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    
    @Size(max = 50)
    @Column(name = "email")
    private String email;
    
    @Column(name = "phone")
    private Long phone;
    
    @Column(name = "vehicleType")
    private Long vehicleType;
    
    @Size(max = 200)    
    @Column(name = "vehicleBrand")
    private String vehicleBrand;
    
    @Column(name = "validFrom")    
    private LocalDateTime validFrom;
    
    @Column(name = "validTo")    
    private LocalDate validTo;
    
    @Size(max = 200)
    @Column(name = "startPoint")
    private String startPoint;
    
    @Column(name = "startTime")    
    private LocalTime startTime;
    
    @Size(max = 200)
    @Column(name = "endPoint")
    private String endPoint;
    
    @Column(name = "endTime")    
    private LocalTime endTime;
    
    @Column(name = "isActive")
    private Boolean isActive;
    
//    @Column(name = "routeId")
//    private Long routeId;
    
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
//    @JoinColumn(name = "id", referencedColumnName = "id")
    private Route route;
    
    @Column(name = "chargeable")
    private Boolean chargeable;
    
    @Size(max = 50)
    @Column(name = "vehicleNumber")
    private String vehicleNumber;
    
    @Column(name = "inactiveStDt")    
    private LocalDate inactiveStDt;
    
    @Column(name = "inactiveEndDt")    
    private LocalDate inactiveEndDt;
    
    @Column(name = "no_seats")
    private int noSeats;
    
    @OneToMany(mappedBy = "listingId",cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    //@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Subscriptions> subscriptionsList;

    public Order() {
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

    public Long getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Long vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

//    public Long getRouteId() {
//        return routeId;
//    }
//
//    public void setRouteId(Long routeId) {
//        this.routeId = routeId;
//    }

    public Boolean getChargeable() {
        return chargeable;
    }

    public void setChargeable(Boolean chargeable) {
        this.chargeable = chargeable;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public LocalDate getInactiveStDt() {
        return inactiveStDt;
    }

    public void setInactiveStDt(LocalDate inactiveStDt) {
        this.inactiveStDt = inactiveStDt;
    }

    public LocalDate getInactiveEndDt() {
        return inactiveEndDt;
    }

    public void setInactiveEndDt(LocalDate inactiveEndDt) {
        this.inactiveEndDt = inactiveEndDt;
    }    

    /**
     * @return the noSeats
     */
    public int getNoSeats() {
        return noSeats;
    }

    /**
     * @param noSeats the noSeats to set
     */
    public void setNoSeats(int noSeats) {
        this.noSeats = noSeats;
    }

    /**
     * @return the route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public String toString() {
        return "Order{" + "name=" + name + ", email=" + email + ", phone=" + phone + ", vehicleType=" + vehicleType + ", vehicleBrand=" + vehicleBrand + ", validFrom=" + validFrom + ", validTo=" + validTo + ", startPoint=" + startPoint + ", startTime=" + startTime + ", endPoint=" + endPoint + ", endTime=" + endTime + ", isActive=" + isActive + ", route=" + route + ", chargeable=" + chargeable + ", vehicleNumber=" + vehicleNumber + ", inactiveStDt=" + inactiveStDt + ", inactiveEndDt=" + inactiveEndDt + ", noSeats=" + noSeats + '}';
    }
}
