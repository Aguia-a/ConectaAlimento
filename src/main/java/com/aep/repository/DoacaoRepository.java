package com.aep.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aep.entity.Doacao;
import java.util.List;
import com.aep.enums.Status;


public interface DoacaoRepository extends MongoRepository<Doacao, UUID>
{
    List<Doacao> findByStatus(Status status);

    List<Doacao> findByNomeDoadorContainingIgnoreCase(String nomeDoador);

    List<Doacao> findByDescricaoAlimentoContainingIgnoreCase(String descricao);
}
