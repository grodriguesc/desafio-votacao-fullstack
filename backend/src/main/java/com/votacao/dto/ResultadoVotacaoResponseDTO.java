package com.votacao.dto;

import com.votacao.enums.ResultadoVotacaoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoVotacaoResponseDTO {
    private Long sessaoId;
    private Long pautaId;
    private String pautaTitulo;
    private Long totalVotos;
    private Long votosSim;
    private Long votosNao;
    private ResultadoVotacaoEnum resultado;
}
