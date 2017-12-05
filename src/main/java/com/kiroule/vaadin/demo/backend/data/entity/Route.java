/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kiroule.vaadin.demo.backend.data.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author sonderaj
 */
@Entity(name = "route")
//@NamedQueries({
//    @NamedQuery(name = "Route.findAll", query = "SELECT r FROM Route r")
//    , @NamedQuery(name = "Route.findById", query = "SELECT r FROM Route r WHERE r.id = :id")
//    , @NamedQuery(name = "Route.findBySource", query = "SELECT r FROM Route r WHERE r.source = :source")
//    , @NamedQuery(name = "Route.findByDestination", query = "SELECT r FROM Route r WHERE r.destination = :destination")
//    , @NamedQuery(name = "Route.findByVia", query = "SELECT r FROM Route r WHERE r.via = :via")
//    , @NamedQuery(name = "Route.findByLink", query = "SELECT r FROM Route r WHERE r.link = :link")
//    , @NamedQuery(name = "Route.findByRole", query = "SELECT r FROM Route r WHERE r.role = :role")
//    , @NamedQuery(name = "Route.findByVersion", query = "SELECT r FROM Route r WHERE r.version = :version")})
@NamedEntityGraphs({ @NamedEntityGraph(name = "Route.getRouteData")})
public class Route extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Size(max = 100)
    @Column(name = "source")
    private String source;
    
    @Size(max = 100)
    @Column(name = "destination")
    private String destination;
    
    @Size(max = 2000)
    @Column(name = "via")
    private String via;
    
    @Size(max = 2000)
    @Column(name = "link")
    private String link;
    
    @Size(max = 100)
    @Column(name = "role")
    private String role;
    
    public Route() {
    }
    
    public Route(Long id) {
        this.id=id;
    }
    
    public Route(String source, String destination, String via, String role) {
        this.source=source;
        this.destination=destination;
        this.via=via;
        this.role=role;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }        
}
