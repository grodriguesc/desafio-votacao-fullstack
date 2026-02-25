package com.votacao.service;

import com.votacao.client.CpfValidationClient;
import com.votacao.dto.CpfValidationResponseDTO;
import com.votacao.dto.VotoRequestDTO;
import com.votacao.dto.VotoResponseDTO;
import com.votacao.entity.Associado;
import com.votacao.entity.Pauta;
import com.votacao.entity.Sessao;
import com.votacao.entity.Voto;
import com.votacao.enums.OpcaoVoto;
import com.votacao.enums.StatusCpf;
import com.votacao.exception.BusinessException;
import com.votacao.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoService sessaoService;

    @Mock
    private AssociadoService associadoService;

    @Mock
    private CpfValidationClient cpfValidationClient;

    @InjectMocks
    private VotoService votoService;

    private Pauta pauta;
    private Sessao sessao;
    private Associado associado;
    private Voto voto;
    private VotoRequestDTO votoRequest;

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
        sessao.setDataFechamento(LocalDateTime.now().plusMinutes(10));
        sessao.setAberta(true);

        associado = new Associado();
        associado.setId(1L);
        associado.setCpf("11144477735");
        associado.setNome("Associado Teste");

        voto = new Voto();
        voto.setId(1L);
        voto.setSessao(sessao);
        voto.setAssociado(associado);
        voto.setOpcao(OpcaoVoto.SIM);
        voto.setDataVoto(LocalDateTime.now());

        votoRequest = new VotoRequestDTO();
        votoRequest.setSessaoId(1L);
        votoRequest.setCpf("11144477735");
        votoRequest.setOpcao(OpcaoVoto.SIM);
    }

    @Test
    void deveRegistrarVotoComSucesso() {
        CpfValidationResponseDTO cpfValidation = new CpfValidationResponseDTO(StatusCpf.ABLE_TO_VOTE);

        when(cpfValidationClient.validarCpf("11144477735")).thenReturn(cpfValidation);
        when(sessaoService.buscarEntidadePorId(1L)).thenReturn(sessao);
        when(associadoService.buscarOuCriarPorCpf("11144477735")).thenReturn(associado);
        when(votoRepository.existsBySessaoIdAndAssociadoId(1L, 1L)).thenReturn(false);
        when(votoRepository.save(any(Voto.class))).thenReturn(voto);

        VotoResponseDTO response = votoService.votar(votoRequest);

        assertNotNull(response);
        assertEquals(1L, response.getSessaoId());
        assertEquals("11144477735", response.getCpf());
        assertEquals(OpcaoVoto.SIM, response.getOpcao());
        verify(votoRepository, times(1)).save(any(Voto.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfNaoHabilitado() {
        CpfValidationResponseDTO cpfValidation = new CpfValidationResponseDTO(StatusCpf.UNABLE_TO_VOTE);

        when(cpfValidationClient.validarCpf("11144477735")).thenReturn(cpfValidation);

        assertThrows(BusinessException.class, () -> {
            votoService.votar(votoRequest);
        });

        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    void deveLancarExcecaoQuandoSessaoFechada() {
        sessao.setAberta(false);
        CpfValidationResponseDTO cpfValidation = new CpfValidationResponseDTO(StatusCpf.ABLE_TO_VOTE);

        when(cpfValidationClient.validarCpf("11144477735")).thenReturn(cpfValidation);
        when(sessaoService.buscarEntidadePorId(1L)).thenReturn(sessao);

        assertThrows(BusinessException.class, () -> {
            votoService.votar(votoRequest);
        });

        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoJaVotou() {
        CpfValidationResponseDTO cpfValidation = new CpfValidationResponseDTO(StatusCpf.ABLE_TO_VOTE);

        when(cpfValidationClient.validarCpf("11144477735")).thenReturn(cpfValidation);
        when(sessaoService.buscarEntidadePorId(1L)).thenReturn(sessao);
        when(associadoService.buscarOuCriarPorCpf("11144477735")).thenReturn(associado);
        when(votoRepository.existsBySessaoIdAndAssociadoId(1L, 1L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> {
            votoService.votar(votoRequest);
        });

        verify(votoRepository, never()).save(any(Voto.class));
    }
}
