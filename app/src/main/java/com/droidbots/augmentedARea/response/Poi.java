package com.droidbots.augmentedARea.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Poi {

@SerializedName("name")
@Expose
private String name;
@SerializedName("phone")
@Expose
private String phone;
@SerializedName("categories")
@Expose
private List<String> categories = null;
@SerializedName("classifications")
@Expose
private List<Classification> classifications = null;

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getPhone() {
return phone;
}

public void setPhone(String phone) {
this.phone = phone;
}

public List<String> getCategories() {
return categories;
}

public void setCategories(List<String> categories) {
this.categories = categories;
}

public List<Classification> getClassifications() {
return classifications;
}

public void setClassifications(List<Classification> classifications) {
this.classifications = classifications;
}

}