package com.droidbots.augmentedARea.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

@SerializedName("type")
@Expose
private String type;
@SerializedName("id")
@Expose
private String id;
@SerializedName("score")
@Expose
private Integer score;
@SerializedName("dist")
@Expose
private Double dist;
@SerializedName("info")
@Expose
private String info;
@SerializedName("poi")
@Expose
private Poi poi;
@SerializedName("address")
@Expose
private Address address;
@SerializedName("position")
@Expose
private Position position;
@SerializedName("viewport")
@Expose
private Viewport viewport;
@SerializedName("entryPoints")
@Expose
private List<EntryPoint> entryPoints = null;

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public Integer getScore() {
return score;
}

public void setScore(Integer score) {
this.score = score;
}

public Double getDist() {
return dist;
}

public void setDist(Double dist) {
this.dist = dist;
}

public String getInfo() {
return info;
}

public void setInfo(String info) {
this.info = info;
}

public Poi getPoi() {
return poi;
}

public void setPoi(Poi poi) {
this.poi = poi;
}

public Address getAddress() {
return address;
}

public void setAddress(Address address) {
this.address = address;
}

public Position getPosition() {
return position;
}

public void setPosition(Position position) {
this.position = position;
}

public Viewport getViewport() {
return viewport;
}

public void setViewport(Viewport viewport) {
this.viewport = viewport;
}

public List<EntryPoint> getEntryPoints() {
return entryPoints;
}

public void setEntryPoints(List<EntryPoint> entryPoints) {
this.entryPoints = entryPoints;
}

}