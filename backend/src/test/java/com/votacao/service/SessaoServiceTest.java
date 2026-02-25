package com.votacao.service;

import com.votacao.dto.SessaoRequestDTO;
import com.votacao.dto.SessaoResponseDTO;
import com.votacao.entity.Pauta;
import com.votacao.entity.Sessao;
import com.votacao.exception.BusinessException;
import com.votacao.exception.ResourceNotFoundException;
import com.votacao.repository.SessaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoServiceTest {

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private SessaoService sessaoService;

    private Pauta pauta;
    private Sessao sessao;
    private SessaoRequestDTO sessaoRequest;

    @BeforeEach
    void setUp() {
        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Pauta Teste");
        pauta.setDataCriacao(LocalDateTime.now());

        sessao = new Sessao();
        sessao.setId(1L);
        sessao.setPauta(pauta);
        sessao.setDataAbertura(LocalDateTime.now());
        sessao.setDataFechamento(LocalDateTime.now().plusMinutes(1));
        sessao.setAberta(true);

        sessaoRequest = new SessaoRequestDTO();
        sessaoRequest.setPautaId(1L);
        sessaoRequest.setDuracaoMinutos(1);
    }

    @Test
    void deveAbrirSessaoComSucesso() {
        when(pautaService.buscarEntidadePorId(1L)).thenReturn(pauta);
        when(sessaoRepository.findByPautaIdAndAbertaTrue(1L)).thenReturn(Optional.empty());
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

        SessaoResponseDTO response = sessaoService.abrir(sessaoRequest);

        assertNotNull(response);
        assertEquals(1L, response.getPautaId());
        assertTrue(response.getAberta());
        verify(sessaoRepository, times(1)).save(any(Sessao.class));
    }

    @Test
    void deveLancarExcecaoQuandoJaExisteSessaoAberta() {
        when(pautaService.buscarEntidadePorId(1L)).thenReturn(pauta);
        when(sessaoRepository.findByPautaIdAndAbertaTrue(1L)).thenReturn(Optional.of(sessao));

        assertThrows(BusinessException.class, () -> {
            sessaoService.abrir(sessaoRequest);
        });

        verify(sessaoRepository, never()).save(any(Sessao.class));
    }

    @Test
    void deveBuscarSessaoPorIdComSucesso() {
        when(sessaoRepository.findById(1L)).thenReturn(Optional.of(sessao));

        SessaoResponseDTO response = sessaoService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(sessaoRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoSessaoNaoEncontrada() {
        when(sessaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            sessaoService.buscarPorId(1L);
        });
    }
}
