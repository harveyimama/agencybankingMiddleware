package com.boat.bp.middleware.mifos.value.request;

import java.util.Date;
import com.boat.bp.middleware.mifos.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

@Value
public class NewAccountRequestValue {
    private String clientId;
    private String locale;
    private String dateFormat;
    private Integer productId;

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = Constant.TIMESTAMP_FORMAT
    )
    private Date submittedOnDate;
}
