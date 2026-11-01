package com.platform.billprocessing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {
    private Long id;
    private String name;
    private String code;
    private Long parentId;
    private String orgPath;
    private List<OrganizationDTO> children;
    private LocalDateTime createdAt;
}

