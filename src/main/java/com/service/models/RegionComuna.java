package com.service.models;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "region_comuna")
public class RegionComuna {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "region")
    private String region;
    @ElementCollection
    @CollectionTable(name = "region_comuna_comunas", joinColumns = @JoinColumn(name = "region_id"))
    @Column(name = "comuna")
    private List<String> comunas = new ArrayList<>();

    public RegionComuna() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public List<String> getComunas() { return comunas; }
    public void setComunas(List<String> comunas) { this.comunas = comunas; }
}
