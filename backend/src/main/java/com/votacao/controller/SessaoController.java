package com.votacao.controller;

import com.votacao.dto.SessaoRequestDTO;
import com.votacao.dto.SessaoResponseDTO;
import com.votacao.service.SessaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessoes")
@RequiredArgsConstructor
@Tag(name = "Sessões", description = "Gerenciamento de sessões de votação")
public class SessaoController {

    private final SessaoService sessaoService;

    @PostMapping
    @Operation(summary = "Abrir sessão de votação", description = "Abre uma nova sessão de votação para uma pauta")
    public ResponseEntity<SessaoResponseDTO> abrir(@Valid @RequestBody SessaoRequestDTO request) {
        SessaoResponseDTO response = sessaoService.abrir(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sessão por ID", description = "Retorna os dados de uma sessão específica")
    public ResponseEntity<SessaoResponseDTO> buscarPorId(@PathVariable Long id) {
        SessaoResponseDTO response = sessaoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas as sessões", description = "Retorna a lista de todas as sessões de votação")
    public ResponseEntity<List<SessaoResponseDTO>> listarTodas() {
        List<SessaoResponseDTO> response = sessaoService.listarTodas();
        return ResponseEntity.ok(response);
    }
}
