package edu.nutech.tht.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.nutech.tht.model.Banner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class BannerResponse {
    private Integer status;
    private String message;
    private List<Banner> data;

    public BannerResponse() {
        data = new ArrayList<>();
    }
}