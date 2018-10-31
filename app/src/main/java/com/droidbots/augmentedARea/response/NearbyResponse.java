package com.droidbots.augmentedARea.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbyResponse {

@SerializedName("summary")
@Expose
private Summary summary;
@SerializedName("results")
@Expose
private List<Result> results = null;

public Summary getSummary() {
return summary;
}

public void setSummary(Summary summary) {
this.summary = summary;
}

public List<Result> getResults() {
return results;
}

public void setResults(List<Result> results) {
this.results = results;
}

}