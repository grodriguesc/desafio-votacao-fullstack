package com.votacao.controller;

import com.votacao.dto.AssociadoRequestDTO;
import com.votacao.dto.AssociadoResponseDTO;
import com.votacao.service.AssociadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/associados")
@RequiredArgsConstructor
@Tag(name = "Associados", description = "Gerenciamento de associados")
public class AssociadoController {

    private final AssociadoService associadoService;

    @PostMapping
    @Operation(summary = "Criar associado", description = "Cadastra um novo associado no sistema")
    public ResponseEntity<AssociadoResponseDTO> criar(@Valid @RequestBody AssociadoRequestDTO request) {
        AssociadoResponseDTO response = associadoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar associado por CPF", description = "Retorna os dados de um associado específico")
    public ResponseEntity<AssociadoResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        AssociadoResponseDTO response = associadoService.buscarPorCpf(cpf);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os associados", description = "Retorna a lista de todos os associados")
    public ResponseEntity<List<AssociadoResponseDTO>> listarTodos() {
        List<AssociadoResponseDTO> response = associadoService.listarTodos();
        return ResponseEntity.ok(response);
    }
}
