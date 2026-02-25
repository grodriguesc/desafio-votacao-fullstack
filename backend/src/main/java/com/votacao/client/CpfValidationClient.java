package com.votacao.client;

import com.votacao.dto.CpfValidationResponseDTO;
import com.votacao.enums.StatusCpf;
import com.votacao.exception.CpfInvalidoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Client fake para validação de CPF (Tarefa Bônus 1)
 * Valida CPF usando algoritmo oficial e retorna aleatoriamente se pode votar
 */
@Component
@Slf4j
public class CpfValidationClient {

    private final Random random = new Random();

    public CpfValidationResponseDTO validarCpf(String cpf) {
        log.info("Validando CPF: {}", cpf);

        if (!isCpfValido(cpf)) {
            log.warn("CPF inválido: {}", cpf);
            throw new CpfInvalidoException("CPF não encontrado ou inválido");
        }

        StatusCpf status = random.nextBoolean() ? StatusCpf.ABLE_TO_VOTE : StatusCpf.UNABLE_TO_VOTE;
        log.info("CPF {} válido - Status: {}", cpf, status);

        return new CpfValidationResponseDTO(status);
    }

    /**
     * Valida CPF usando o algoritmo oficial brasileiro
     * 
     * @param cpf CPF com 11 dígitos (apenas números)
     * @return true se válido, false se inválido
     */
    private boolean isCpfValido(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int[] digitos = new int[11];
            for (int i = 0; i < 11; i++) {
                digitos[i] = Character.getNumericValue(cpf.charAt(i));
            }

            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += digitos[i] * (10 - i);
            }
            int resto = soma % 11;
            int dv1 = (resto < 2) ? 0 : (11 - resto);

            if (dv1 != digitos[9]) {
                return false;
            }

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += digitos[i] * (11 - i);
            }
            resto = soma % 11;
            int dv2 = (resto < 2) ? 0 : (11 - resto);

            return dv2 == digitos[10];

        } catch (Exception e) {
            log.error("Erro ao validar CPF: {}", cpf, e);
            return false;
        }
    }
}
