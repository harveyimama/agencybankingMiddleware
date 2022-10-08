package com.boat.bp.middleware.mifos;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class CreateClientValue {
	private Integer officeId;
	private String firstname;
	private String lastname;
	private String externalId;
	private String dateFormat;
	private String locale;
	private boolean active;

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = Constant.TIMESTAMP_FORMAT
    )
	private Date activationDate;

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = Constant.TIMESTAMP_FORMAT
    )
	private Date submittedOnDate;
	private Integer savingsProductId;
    private String mobileNo;
    private Integer clientTypeId;
}
