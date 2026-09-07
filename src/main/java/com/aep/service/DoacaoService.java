package com.aep.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aep.entity.Doacao;
import com.aep.enums.Status;
import com.aep.repository.DoacaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoacaoService 
{
    private final DoacaoRepository doacaoRepository;

    public Doacao salvarDoacao(Doacao doacao)
    {
        if (doacao.getId() == null)
            doacao.setId(UUID.randomUUID().toString());

        return doacaoRepository.save(doacao);
    }

    public List<Doacao> listarDoacoes()
    {
        return doacaoRepository.findAll();
    }

    public Doacao buscarDoacaoPorId(String id)
    {
        Doacao doacaoExistente = doacaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Doação não Encontrada"));

        return doacaoExistente;
    }

    public List<Doacao> listarDoacoesPorStatus(Status status)
    {
        return doacaoRepository.findByStatus(status);
    }

    public List<Doacao> listarDoacoesPorDoador(String nomeDoador)
    {
        return doacaoRepository.findByNomeDoadorContainingIgnoreCase(nomeDoador);
    }

    public Doacao atualizarDoacao(String id, Doacao doacao)
    {
        Doacao doacaoExistente = buscarDoacaoPorId(id);

        doacaoExistente.setNomeDoador(doacao.getNomeDoador());
        doacaoExistente.setDescricaoAlimento(doacao.getDescricaoAlimento());
        doacaoExistente.setQuantidade(doacao.getQuantidade());
        doacaoExistente.setUnidadeDeMedida(doacao.getUnidadeDeMedida());
        doacaoExistente.setDataDeValidade(doacao.getDataDeValidade());
        doacaoExistente.setStatus(doacao.getStatus());

        return doacaoRepository.save(doacaoExistente);
    }

    public void deletarDoacao(String id)
    {
        Doacao doacao = buscarDoacaoPorId(id);
        doacaoRepository.delete(doacao);
    }
}
