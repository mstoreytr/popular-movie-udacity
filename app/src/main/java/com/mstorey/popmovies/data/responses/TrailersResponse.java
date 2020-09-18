package com.mstorey.popmovies.data.responses;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;
@Keep
public class TrailersResponse implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private List<Trailer> results;

	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return id;
	}

	public void setResults(List<Trailer> results){
		this.results = results;
	}
	public List<Trailer> getResults(){
		return results;
	}
}