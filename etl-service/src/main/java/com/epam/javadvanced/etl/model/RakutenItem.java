package com.epam.javadvanced.etl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RakutenItem {
    
    @JsonProperty("itemName")
    private String itemName;
    
    @JsonProperty("itemCaption")
    private String itemCaption;
    
    @JsonProperty("genreId")
    private String genreId;
}