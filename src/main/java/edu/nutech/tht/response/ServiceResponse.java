package edu.nutech.tht.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.nutech.tht.model.Service;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class ServiceResponse {
    private Integer status;
    private String message;
    private List<Service> data;

    public ServiceResponse() {
        data = new ArrayList<>();
    }
}
