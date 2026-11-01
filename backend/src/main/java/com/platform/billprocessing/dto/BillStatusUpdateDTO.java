package com.platform.billprocessing.dto;

import com.platform.billprocessing.model.BillStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillStatusUpdateDTO {
    @NotNull(message = "Target status is required")
    private BillStatus status;

    private String comment;
    private String rejectionReason;
}

