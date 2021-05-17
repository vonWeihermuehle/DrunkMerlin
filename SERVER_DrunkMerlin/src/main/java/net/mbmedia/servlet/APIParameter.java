package net.mbmedia.servlet;

public enum APIParameter {

    hash("hash"),
    username("username"),
    password("pw"),
    promille("promille"),
    nutzerid("nutzerid"),
    freundid("freundid"),
    text("text"),
    id("id"),
    name("name"),
    menge("menge"),
    prozent("prozent"),
    zeitpunkt("zeitpunkt"),
    latlng("latlng"),
    drinkid("drinkid");

    private String web;
    APIParameter(String web){
        this.web = web;
    }

    public String getWeb(){
        return web;
    }
}
