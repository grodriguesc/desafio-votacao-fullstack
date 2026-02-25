package com.votacao.service;

import com.votacao.dto.PautaRequestDTO;
import com.votacao.dto.PautaResponseDTO;
import com.votacao.entity.Pauta;
import com.votacao.exception.ResourceNotFoundException;
import com.votacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PautaService {

    private final PautaRepository pautaRepository;

    @Transactional
    public PautaResponseDTO criar(PautaRequestDTO request) {
        log.info("Criando nova pauta: {}", request.getTitulo());

        Pauta pauta = new Pauta();
        pauta.setTitulo(request.getTitulo());
        pauta.setDescricao(request.getDescricao());

        Pauta pautaSalva = pautaRepository.save(pauta);
        log.info("Pauta criada com ID: {}", pautaSalva.getId());

        return toResponse(pautaSalva);
    }

    @Transactional(readOnly = true)
    public PautaResponseDTO buscarPorId(Long id) {
        log.info("Buscando pauta com ID: {}", id);
        Pauta pauta = pautaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada com ID: " + id));
        return toResponse(pauta);
    }

    @Transactional(readOnly = true)
    public List<PautaResponseDTO> listarTodas() {
        log.info("Listando todas as pautas");
        return pautaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Pauta buscarEntidadePorId(Long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada com ID: " + id));
    }

    private PautaResponseDTO toResponse(Pauta pauta) {
        return new PautaResponseDTO(
                pauta.getId(),
                pauta.getTitulo(),
                pauta.getDescricao(),
                pauta.getDataCriacao());
    }
}
