package com.aep.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aep.entity.Doacao;
import com.aep.entity.ItemDoacao;
import com.aep.enums.Status;
import com.aep.repository.DoacaoRepository;
import com.aep.repository.InstituicaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoacaoService 
{
    private final DoacaoRepository doacaoRepository;
    private final InstituicaoRepository instituicaoRepository;

    public Doacao salvarDoacao(Doacao doacao)
    {
        validar(doacao);
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

    public List<Doacao> listarDoacoesPorInstituicao(String instituicaoId)
    {
        return doacaoRepository.findByInstituicaoId(instituicaoId);
    }

    public Doacao atualizarDoacao(String id, Doacao doacao)
    {
        validar(doacao);
        Doacao doacaoExistente = buscarDoacaoPorId(id);

        doacaoExistente.setNomeDoador(doacao.getNomeDoador());
        doacaoExistente.setDescricaoAlimento(doacao.getDescricaoAlimento());
        doacaoExistente.setQuantidade(doacao.getQuantidade());
        doacaoExistente.setUnidadeDeMedida(doacao.getUnidadeDeMedida());
        doacaoExistente.setDataDeValidade(doacao.getDataDeValidade());
        doacaoExistente.setStatus(doacao.getStatus());
        doacaoExistente.setInstituicaoId(doacao.getInstituicaoId());
        doacaoExistente.setItens(doacao.getItens());

        return doacaoRepository.save(doacaoExistente);
    }

    public void deletarDoacao(String id)
    {
        Doacao doacao = buscarDoacaoPorId(id);
        doacaoRepository.delete(doacao);
    }

    private void validar(Doacao doacao)
    {
        if (doacao == null || vazio(doacao.getNomeDoador())
            || vazio(doacao.getDescricaoAlimento())
            || vazio(doacao.getUnidadeDeMedida())
            || doacao.getQuantidade() == null || doacao.getQuantidade() <= 0
            || doacao.getDataDeValidade() == null || doacao.getStatus() == null)
        {
            throw new IllegalArgumentException("Dados da doação são inválidos");
        }

        if (doacao.getItens() != null)
        {
            for (ItemDoacao item : doacao.getItens())
            {
                if (item == null || vazio(item.getDescricao())
                    || item.getQuantidade() == null || item.getQuantidade() <= 0
                    || vazio(item.getUnidadeDeMedida()) || item.getDataDeValidade() == null)
                {
                    throw new IllegalArgumentException("Itens da doação são inválidos");
                }
            }

            if (!vazio(doacao.getInstituicaoId())
                && !instituicaoRepository.existsById(doacao.getInstituicaoId()))
            {
                throw new IllegalArgumentException("Instituição relacionada não encontrada");
            }
        }
    }

    private boolean vazio(String valor)
    {
        return valor == null || valor.isBlank();
    }
}
