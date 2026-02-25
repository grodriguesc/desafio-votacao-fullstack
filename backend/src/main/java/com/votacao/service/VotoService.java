package com.votacao.service;

import com.votacao.client.CpfValidationClient;
import com.votacao.dto.CpfValidationResponseDTO;
import com.votacao.dto.ResultadoVotacaoResponseDTO;
import com.votacao.enums.ResultadoVotacaoEnum;
import com.votacao.dto.VotoRequestDTO;
import com.votacao.dto.VotoResponseDTO;
import com.votacao.entity.Associado;
import com.votacao.entity.Sessao;
import com.votacao.entity.Voto;
import com.votacao.enums.OpcaoVoto;
import com.votacao.enums.StatusCpf;
import com.votacao.exception.BusinessException;
import com.votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotoService {

    private final VotoRepository votoRepository;
    private final SessaoService sessaoService;
    private final AssociadoService associadoService;
    private final CpfValidationClient cpfValidationClient;

    @Transactional
    public VotoResponseDTO votar(VotoRequestDTO request) {
        log.info("Processando voto - Sessão: {}, CPF: {}, Opção: {}",
                request.getSessaoId(), request.getCpf(), request.getOpcao());

        // Validar CPF com sistema externo (Bônus 1)
        CpfValidationResponseDTO cpfValidation = cpfValidationClient.validarCpf(request.getCpf());
        if (cpfValidation.getStatus() == StatusCpf.UNABLE_TO_VOTE) {
            throw new BusinessException("CPF não está habilitado para votar");
        }

        Sessao sessao = sessaoService.buscarEntidadePorId(request.getSessaoId());

        // Verificar se a sessão está aberta
        if (!sessao.isAberta()) {
            throw new BusinessException("A sessão de votação está fechada");
        }

        Associado associado = associadoService.buscarOuCriarPorCpf(request.getCpf());

        // Verificar se o associado já votou nesta sessão
        if (votoRepository.existsBySessaoIdAndAssociadoId(sessao.getId(), associado.getId())) {
            throw new BusinessException("Este associado já votou nesta sessão");
        }

        Voto voto = new Voto();
        voto.setSessao(sessao);
        voto.setAssociado(associado);
        voto.setOpcao(request.getOpcao());

        Voto votoSalvo = votoRepository.save(voto);
        log.info("Voto registrado com ID: {}", votoSalvo.getId());

        return toResponse(votoSalvo);
    }

    @Transactional(readOnly = true)
    public ResultadoVotacaoResponseDTO contabilizarVotos(Long sessaoId) {
        log.info("Contabilizando votos da sessão: {}", sessaoId);

        Sessao sessao = sessaoService.buscarEntidadePorId(sessaoId);

        Long votosSim = votoRepository.countVotosBySessaoAndOpcao(sessaoId, OpcaoVoto.SIM);
        Long votosNao = votoRepository.countVotosBySessaoAndOpcao(sessaoId, OpcaoVoto.NAO);
        Long totalVotos = votosSim + votosNao;

        ResultadoVotacaoEnum resultado;
        if (votosSim > votosNao) {
            resultado = ResultadoVotacaoEnum.APROVADA;
        } else if (votosNao > votosSim) {
            resultado = ResultadoVotacaoEnum.REJEITADA;
        } else {
            resultado = ResultadoVotacaoEnum.EMPATE;
        }

        log.info("Resultado da sessão {} - Sim: {}, Não: {}, Total: {}, Resultado: {}",
                sessaoId, votosSim, votosNao, totalVotos, resultado);

        return new ResultadoVotacaoResponseDTO(
                sessaoId,
                sessao.getPauta().getId(),
                sessao.getPauta().getTitulo(),
                totalVotos,
                votosSim,
                votosNao,
                resultado);
    }

    private VotoResponseDTO toResponse(Voto voto) {
        return new VotoResponseDTO(
                voto.getId(),
                voto.getSessao().getId(),
                voto.getAssociado().getCpf(),
                voto.getOpcao(),
                voto.getDataVoto());
    }
}
