package com.votacao.client;

import com.votacao.dto.CpfValidationResponseDTO;
import com.votacao.enums.StatusCpf;
import com.votacao.exception.CpfInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CpfValidationClientTest {

    @InjectMocks
    private CpfValidationClient cpfValidationClient;

    @Test
    void deveValidarCpfValido() {
        CpfValidationResponseDTO response = cpfValidationClient.validarCpf("11144477735");

        assertNotNull(response);
        assertNotNull(response.getStatus());
        assertTrue(response.getStatus() == StatusCpf.ABLE_TO_VOTE ||
                response.getStatus() == StatusCpf.UNABLE_TO_VOTE);
    }

    @Test
    void deveValidarOutroCpfValido() {
        CpfValidationResponseDTO response = cpfValidationClient.validarCpf("38119606019");

        assertNotNull(response);
        assertNotNull(response.getStatus());
    }

    @Test
    void deveLancarExcecaoParaCpfComDigitosIguais() {
        assertThrows(CpfInvalidoException.class, () -> {
            cpfValidationClient.validarCpf("11111111111");
        });

        assertThrows(CpfInvalidoException.class, () -> {
            cpfValidationClient.validarCpf("00000000000");
        });

        assertThrows(CpfInvalidoException.class, () -> {
            cpfValidationClient.validarCpf("99999999999");
        });
    }

    @Test
    void deveLancarExcecaoParaCpfComTamanhoInvalido() {
        assertThrows(CpfInvalidoException.class, () -> {
            cpfValidationClient.validarCpf("123456789");
        });

        assertThrows(CpfInvalidoException.class, () -> {
            cpfValidationClient.validarCpf("123456789012");
        });

        assertThrows(CpfInvalidoException.class, () -> {
            cpfValidationClient.validarCpf("");
        });
    }

    @Test
    void deveLancarExcecaoParaCpfComDigitoVerificadorIncorreto() {
        assertThrows(CpfInvalidoException.class, () -> {
            cpfValidationClient.validarCpf("11144477799");
        });

        assertThrows(CpfInvalidoException.class, () -> {
            cpfValidationClient.validarCpf("11144477745");
        });
    }

    @Test
    void deveValidarCpfComMascara() {
        CpfValidationResponseDTO response = cpfValidationClient.validarCpf("111.444.777-35");

        assertNotNull(response);
        assertNotNull(response.getStatus());
    }

    @Test
    void deveValidarMultiplosCpfsValidos() {
        // Lista de CPFs válidos verificados com algoritmo oficial
        String[] cpfsValidos = {
                "11144477735",  // CPF válido
                "38119606019",  // CPF válido
                "52998224725"   // CPF válido
        };

        for (String cpf : cpfsValidos) {
            assertDoesNotThrow(() -> {
                CpfValidationResponseDTO response = cpfValidationClient.validarCpf(cpf);
                assertNotNull(response);
                assertNotNull(response.getStatus());
            }, "CPF " + cpf + " deveria ser válido");
        }
    }

    @Test
    void deveLancarExcecaoParaCpfsInvalidos() {
        String[] cpfsInvalidos = {
                "12345678901",
                "11111111111",
                "123",
                "99999999999",
                "12345678900"
        };

        for (String cpf : cpfsInvalidos) {
            assertThrows(CpfInvalidoException.class, () -> {
                cpfValidationClient.validarCpf(cpf);
            }, "CPF " + cpf + " deveria ser inválido");
        }
    }
}
