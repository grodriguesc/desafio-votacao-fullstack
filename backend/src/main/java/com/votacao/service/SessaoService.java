package com.votacao.service;

import com.votacao.dto.SessaoRequestDTO;
import com.votacao.dto.SessaoResponseDTO;
import com.votacao.entity.Pauta;
import com.votacao.entity.Sessao;
import com.votacao.exception.BusinessException;
import com.votacao.exception.ResourceNotFoundException;
import com.votacao.repository.SessaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final PautaService pautaService;

    @Transactional
    public SessaoResponseDTO abrir(SessaoRequestDTO request) {
        log.info("Abrindo sessão para pauta ID: {}", request.getPautaId());

        Pauta pauta = pautaService.buscarEntidadePorId(request.getPautaId());

        // Verificar se já existe sessão aberta para esta pauta
        sessaoRepository.findByPautaIdAndAbertaTrue(request.getPautaId())
                .ifPresent(s -> {
                    throw new BusinessException("Já existe uma sessão aberta para esta pauta");
                });

        Integer duracao = request.getDuracaoMinutos() != null ? request.getDuracaoMinutos() : 1;
        LocalDateTime dataFechamento = LocalDateTime.now().plusMinutes(duracao);

        Sessao sessao = new Sessao();
        sessao.setPauta(pauta);
        sessao.setDataFechamento(dataFechamento);

        Sessao sessaoSalva = sessaoRepository.save(sessao);
        log.info("Sessão criada com ID: {} - Duração: {} minutos", sessaoSalva.getId(), duracao);

        return toResponse(sessaoSalva);
    }

    @Transactional(readOnly = true)
    public SessaoResponseDTO buscarPorId(Long id) {
        log.info("Buscando sessão com ID: {}", id);
        Sessao sessao = sessaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sessão não encontrada com ID: " + id));
        return toResponse(sessao);
    }

    @Transactional(readOnly = true)
    public List<SessaoResponseDTO> listarTodas() {
        log.info("Listando todas as sessões");
        return sessaoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Sessao buscarEntidadePorId(Long id) {
        return sessaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sessão não encontrada com ID: " + id));
    }

    private SessaoResponseDTO toResponse(Sessao sessao) {
        return new SessaoResponseDTO(
                sessao.getId(),
                sessao.getPauta().getId(),
                sessao.getPauta().getTitulo(),
                sessao.getDataAbertura(),
                sessao.getDataFechamento(),
                sessao.isAberta());
    }
}
