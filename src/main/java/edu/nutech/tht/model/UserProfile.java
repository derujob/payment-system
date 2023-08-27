package edu.nutech.tht.model;

import javax.persistence.*;
import org.hibernate.annotations.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Entity
@Table
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class UserProfile {

    @Id
    private String email;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String pictureLink;

    public UserProfile() {

    }
}
