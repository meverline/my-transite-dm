package org.dm.transit.controller.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonRootName("SearchRequest")
public class SearchRequest {

    private String name;
    private String searchId;

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonGetter("searchId")
    public String getSearchId() {
        return searchId;
    }

    @JsonSetter("searchId")
    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }
}
