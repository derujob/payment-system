package edu.nutech.tht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(
        indexes = {
                @Index(columnList = "invoiceNumber"),
                @Index(columnList = "serviceCode"),
                @Index(columnList = "serviceName"),
                @Index(columnList = "transactionType"),
                @Index(columnList = "totalAmount"),
                @Index(columnList = "createdOn")
        })
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class UserTransaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long invoiceNumber;

    @JsonProperty(value = "service_code")
    private String serviceCode;

    @JsonProperty(value = "service_name")
    private String serviceName;

    @JsonProperty(value = "transaction_type")
    private String transactionType;

    @JsonProperty(value = "total_amount")
    private Long totalAmount;

    @JsonIgnore
    private String createdOn;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", referencedColumnName = "email")
    private User users;

    public UserTransaction() {
        createdOn = getCreatedOn();
    }

    @JsonProperty(value = "created_on")
    public String getCreatedOn(){
        long dateInUnixTimestamp = new Date().getTime();
        Instant i = Instant.ofEpochMilli(dateInUnixTimestamp);
        LocalDateTime ldt = LocalDateTime.ofInstant(i, ZoneId.systemDefault());

        String dateInHuman = ldt.toString();

        return dateInHuman;
    }
}
