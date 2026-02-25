package com.votacao.dto;

import com.votacao.enums.OpcaoVoto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotoResponseDTO {
    private Long id;
    private Long sessaoId;
    private String cpf;
    private OpcaoVoto opcao;
    private LocalDateTime dataVoto;
}
