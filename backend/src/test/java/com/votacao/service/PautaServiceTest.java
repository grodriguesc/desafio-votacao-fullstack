package com.votacao.service;

import com.votacao.dto.PautaRequestDTO;
import com.votacao.dto.PautaResponseDTO;
import com.votacao.entity.Pauta;
import com.votacao.exception.ResourceNotFoundException;
import com.votacao.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    private Pauta pauta;
    private PautaRequestDTO pautaRequest;

    @BeforeEach
    void setUp() {
        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Pauta Teste");
        pauta.setDescricao("Descrição da pauta teste");
        pauta.setDataCriacao(LocalDateTime.now());

        pautaRequest = new PautaRequestDTO();
        pautaRequest.setTitulo("Pauta Teste");
        pautaRequest.setDescricao("Descrição da pauta teste");
    }

    @Test
    void deveCriarPautaComSucesso() {
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        PautaResponseDTO response = pautaService.criar(pautaRequest);

        assertNotNull(response);
        assertEquals("Pauta Teste", response.getTitulo());
        assertEquals("Descrição da pauta teste", response.getDescricao());
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    void deveBuscarPautaPorIdComSucesso() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        PautaResponseDTO response = pautaService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Pauta Teste", response.getTitulo());
        verify(pautaRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoPautaNaoEncontrada() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            pautaService.buscarPorId(1L);
        });

        verify(pautaRepository, times(1)).findById(1L);
    }

    @Test
    void deveListarTodasAsPautas() {
        Pauta pauta2 = new Pauta();
        pauta2.setId(2L);
        pauta2.setTitulo("Pauta 2");
        pauta2.setDataCriacao(LocalDateTime.now());

        when(pautaRepository.findAll()).thenReturn(Arrays.asList(pauta, pauta2));

        List<PautaResponseDTO> responses = pautaService.listarTodas();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(pautaRepository, times(1)).findAll();
    }
}
