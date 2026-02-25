package com.votacao.controller;

import com.votacao.dto.PautaRequestDTO;
import com.votacao.dto.PautaResponseDTO;
import com.votacao.service.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
@Tag(name = "Pautas", description = "Gerenciamento de pautas")
public class PautaController {

    private final PautaService pautaService;

    @PostMapping
    @Operation(summary = "Criar nova pauta", description = "Cadastra uma nova pauta no sistema")
    public ResponseEntity<PautaResponseDTO> criar(@Valid @RequestBody PautaRequestDTO request) {
        PautaResponseDTO response = pautaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pauta por ID", description = "Retorna os dados de uma pauta específica")
    public ResponseEntity<PautaResponseDTO> buscarPorId(@PathVariable Long id) {
        PautaResponseDTO response = pautaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas as pautas", description = "Retorna a lista de todas as pautas cadastradas")
    public ResponseEntity<List<PautaResponseDTO>> listarTodas() {
        List<PautaResponseDTO> response = pautaService.listarTodas();
        return ResponseEntity.ok(response);
    }
}
