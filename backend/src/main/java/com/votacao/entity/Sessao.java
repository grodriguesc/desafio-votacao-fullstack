package com.votacao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    @Column(nullable = false)
    private LocalDateTime dataAbertura;

    @Column(nullable = false)
    private LocalDateTime dataFechamento;

    @Column(nullable = false)
    private Boolean aberta;

    @PrePersist
    protected void onCreate() {
        if (dataAbertura == null) {
            dataAbertura = LocalDateTime.now();
        }
        if (aberta == null) {
            aberta = true;
        }
    }

    public boolean isAberta() {
        return aberta && LocalDateTime.now().isBefore(dataFechamento);
    }
}
