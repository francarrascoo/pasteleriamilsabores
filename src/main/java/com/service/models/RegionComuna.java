package com.service.models;

import java.util.List;

public class RegionComuna {
    private String id;
    private String region;
    private List<String> comunas;

    public RegionComuna() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public List<String> getComunas() { return comunas; }
    public void setComunas(List<String> comunas) { this.comunas = comunas; }
}
