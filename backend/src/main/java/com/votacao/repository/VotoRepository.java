package com.votacao.repository;

import com.votacao.entity.Voto;
import com.votacao.enums.OpcaoVoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsBySessaoIdAndAssociadoId(Long sessaoId, Long associadoId);

    @Query("SELECT COUNT(v) FROM Voto v WHERE v.sessao.id = :sessaoId AND v.opcao = :opcao")
    Long countVotosBySessaoAndOpcao(@Param("sessaoId") Long sessaoId, @Param("opcao") OpcaoVoto opcao);
}
