package com.droidbots.augmentedARea.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntryPoint {

@SerializedName("type")
@Expose
private String type;
@SerializedName("position")
@Expose
private Position_ position;

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public Position_ getPosition() {
return position;
}

public void setPosition(Position_ position) {
this.position = position;
}

}