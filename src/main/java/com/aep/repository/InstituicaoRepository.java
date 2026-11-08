package com.aep.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aep.entity.Instituicao;

public interface InstituicaoRepository extends MongoRepository<Instituicao, String> {

    List<Instituicao> findByNomeContainingIgnoreCase(String nome);
}
