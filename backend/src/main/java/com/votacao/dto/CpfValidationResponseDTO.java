package com.votacao.dto;

import com.votacao.enums.StatusCpf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CpfValidationResponseDTO {
    private StatusCpf status;
}
