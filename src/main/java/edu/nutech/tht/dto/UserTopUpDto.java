package edu.nutech.tht.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTopUpDto {
    @JsonProperty(value = "top_up_amount")
    private long topUpAmount;
}
