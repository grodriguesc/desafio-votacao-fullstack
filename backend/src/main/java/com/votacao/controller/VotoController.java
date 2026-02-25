package com.votacao.controller;

import com.votacao.dto.ResultadoVotacaoResponseDTO;
import com.votacao.dto.VotoRequestDTO;
import com.votacao.dto.VotoResponseDTO;
import com.votacao.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/votos")
@RequiredArgsConstructor
@Tag(name = "Votos", description = "Gerenciamento de votos")
public class VotoController {

    private final VotoService votoService;

    @PostMapping
    @Operation(summary = "Registrar voto", description = "Registra o voto de um associado em uma sessão")
    public ResponseEntity<VotoResponseDTO> votar(@Valid @RequestBody VotoRequestDTO request) {
        VotoResponseDTO response = votoService.votar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/resultado/{sessaoId}")
    @Operation(summary = "Contabilizar votos", description = "Retorna o resultado da votação de uma sessão")
    public ResponseEntity<ResultadoVotacaoResponseDTO> contabilizar(@PathVariable Long sessaoId) {
        ResultadoVotacaoResponseDTO resultado = votoService.contabilizarVotos(sessaoId);
        return ResponseEntity.ok(resultado);
    }
}
