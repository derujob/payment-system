package edu.nutech.tht.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Entity
@Table(
        indexes = {
                @Index(columnList = "id"),
                @Index(columnList = "serviceCode"),
                @Index(columnList = "serviceName"),
                @Index(columnList = "serviceIcon"),
                @Index(columnList = "serviceTariff")
        })
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    protected long id;

    @JsonProperty("banner_name")
    protected String serviceCode;

    @JsonProperty("banner_image")
    protected String serviceName;

    @JsonProperty("service_icon")
    protected String serviceIcon;

    @JsonProperty("service_tariff")
    protected Long serviceTariff;

    public Service() {

    }
}
