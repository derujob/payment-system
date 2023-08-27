package edu.nutech.tht.model;

import java.io.Serializable;
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
                @Index(columnList = "bannerName"),
                @Index(columnList = "bannerImage"),
                @Index(columnList = "description")
        })
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class Banner implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    protected long id;

    @JsonProperty("banner_name")
    protected String bannerName;

    @JsonProperty("banner_image")
    protected String bannerImage;

    protected String description;

    public Banner() {

    }
}
