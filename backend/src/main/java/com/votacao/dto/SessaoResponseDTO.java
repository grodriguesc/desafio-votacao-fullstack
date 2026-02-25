package com.votacao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessaoResponseDTO {
    private Long id;
    private Long pautaId;
    private String pautaTitulo;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private Boolean aberta;
}
