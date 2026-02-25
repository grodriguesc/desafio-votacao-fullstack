package com.votacao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessaoRequestDTO {

    @NotNull(message = "ID da pauta é obrigatório")
    private Long pautaId;

    @Positive(message = "Duração deve ser um valor positivo")
    private Integer duracaoMinutos = 1; // Default: 1 minuto
}
