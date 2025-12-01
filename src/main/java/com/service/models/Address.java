package com.service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "address")
    private String address;
    @Column(name = "region")
    private String region;
    @Column(name = "comuna")
    private String comuna;
    
    @ManyToOne
    @JoinColumn(name = "user_run")
    private User user;

    public Address() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getComuna() { return comuna; }
    public void setComuna(String comuna) { this.comuna = comuna; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
