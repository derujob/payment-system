package edu.nutech.tht.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.nutech.tht.model.Data;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class UserRegisResponse {
    private Integer status;
    private String message;
    private Data data;
}
