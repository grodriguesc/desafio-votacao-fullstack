package com.votacao.service;

import com.votacao.dto.AssociadoRequestDTO;
import com.votacao.dto.AssociadoResponseDTO;
import com.votacao.entity.Associado;
import com.votacao.exception.BusinessException;
import com.votacao.exception.ResourceNotFoundException;
import com.votacao.repository.AssociadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssociadoService {

    private final AssociadoRepository associadoRepository;

    @Transactional
    public AssociadoResponseDTO criar(AssociadoRequestDTO request) {
        log.info("Criando associado com CPF: {}", request.getCpf());

        if (associadoRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("Já existe um associado com este CPF");
        }

        Associado associado = new Associado();
        associado.setCpf(request.getCpf());
        associado.setNome(request.getNome());

        Associado associadoSalvo = associadoRepository.save(associado);
        log.info("Associado criado com ID: {}", associadoSalvo.getId());

        return toResponse(associadoSalvo);
    }

    @Transactional(readOnly = true)
    public AssociadoResponseDTO buscarPorCpf(String cpf) {
        log.info("Buscando associado com CPF: {}", cpf);
        Associado associado = associadoRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Associado não encontrado com CPF: " + cpf));
        return toResponse(associado);
    }

    @Transactional(readOnly = true)
    public List<AssociadoResponseDTO> listarTodos() {
        log.info("Listando todos os associados");
        return associadoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Associado buscarOuCriarPorCpf(String cpf) {
        return associadoRepository.findByCpf(cpf)
                .orElseGet(() -> {
                    Associado novoAssociado = new Associado();
                    novoAssociado.setCpf(cpf);
                    novoAssociado.setNome("Associado " + cpf);
                    return associadoRepository.save(novoAssociado);
                });
    }

    private AssociadoResponseDTO toResponse(Associado associado) {
        return new AssociadoResponseDTO(
                associado.getId(),
                associado.getCpf(),
                associado.getNome());
    }
}
