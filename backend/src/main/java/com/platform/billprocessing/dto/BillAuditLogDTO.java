package com.platform.billprocessing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillAuditLogDTO {
    private Long id;
    private String action;
    private Long performedByUserId;
    private String performedByUserName;
    private String comment;
    private LocalDateTime timestamp;
}

