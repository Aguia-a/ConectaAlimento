package com.aep.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aep.entity.Endereco;
import com.aep.entity.Instituicao;
import com.aep.repository.DoacaoRepository;
import com.aep.repository.InstituicaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstituicaoService {

    private final InstituicaoRepository instituicaoRepository;
    private final DoacaoRepository doacaoRepository;

    public Instituicao salvar(Instituicao instituicao) {
        validar(instituicao);
        if (instituicao.getId() == null) {
            instituicao.setId(UUID.randomUUID().toString());
        }
        return instituicaoRepository.save(instituicao);
    }

    public List<Instituicao> listar() {
        return instituicaoRepository.findAll();
    }

    public Instituicao buscarPorId(String id) {
        return instituicaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));
    }

    public List<Instituicao> buscarPorNome(String nome) {
        return instituicaoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Instituicao atualizar(String id, Instituicao dados) {
        Instituicao existente = buscarPorId(id);
        validar(dados);
        existente.setNome(dados.getNome());
        existente.setEmail(dados.getEmail());
        existente.setTelefone(dados.getTelefone());
        existente.setEndereco(dados.getEndereco());
        return instituicaoRepository.save(existente);
    }

    public void excluir(String id) {
        Instituicao instituicao = buscarPorId(id);
        if (doacaoRepository.existsByInstituicaoId(id)) {
            throw new IllegalStateException("Instituição possui doações relacionadas");
        }
        instituicaoRepository.delete(instituicao);
    }

    private void validar(Instituicao instituicao) {
        if (instituicao == null || vazio(instituicao.getNome())) {
            throw new IllegalArgumentException("Nome da instituição é obrigatório");
        }
        Endereco endereco = instituicao.getEndereco();
        if (endereco == null || vazio(endereco.getCidade()) || vazio(endereco.getEstado())) {
            throw new IllegalArgumentException("Cidade e estado do endereço são obrigatórios");
        }
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
