package com.votacao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PautaResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataCriacao;
}
