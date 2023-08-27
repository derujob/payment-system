package edu.nutech.tht.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTransactionFilteredResponse {

    @JsonProperty(value = "invoice_number")
    private long invoiceNumber;

    @JsonProperty(value = "transaction_type")
    private String transactionType;

    private String description;

    @JsonProperty(value = "total_amount")
    private Long totalAmount;

    @JsonProperty(value = "created_on")
    private String createdOn;
}
