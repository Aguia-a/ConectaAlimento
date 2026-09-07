package com.aep.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aep.entity.Doacao;
import com.aep.enums.Status;
import com.aep.repository.DoacaoRepository;

@ExtendWith(MockitoExtension.class)
class DoacaoServiceTest {

    @Mock
    private DoacaoRepository doacaoRepository;

    private DoacaoService doacaoService;

    @BeforeEach
    void setUp() {
        doacaoService = new DoacaoService(doacaoRepository);
    }

    @Test
    void deveGerarIdAoSalvarDoacaoNova() {
        Doacao doacao = criarDoacao(null);
        when(doacaoRepository.save(doacao)).thenReturn(doacao);

        Doacao resultado = doacaoService.salvarDoacao(doacao);

        assertSame(doacao, resultado);
        assertNotNull(doacao.getId());
        verify(doacaoRepository).save(doacao);
    }

    @Test
    void devePreservarIdAoSalvarDoacaoExistente() {
        Doacao doacao = criarDoacao("id-existente");
        when(doacaoRepository.save(doacao)).thenReturn(doacao);

        doacaoService.salvarDoacao(doacao);

        assertEquals("id-existente", doacao.getId());
        verify(doacaoRepository).save(doacao);
    }

    @Test
    void deveListarTodasAsDoacoes() {
        List<Doacao> doacoes = List.of(criarDoacao("id-1"));
        when(doacaoRepository.findAll()).thenReturn(doacoes);

        List<Doacao> resultado = doacaoService.listarDoacoes();

        assertSame(doacoes, resultado);
        verify(doacaoRepository).findAll();
    }

    @Test
    void deveBuscarDoacaoPorId() {
        Doacao doacao = criarDoacao("id-1");
        when(doacaoRepository.findById("id-1")).thenReturn(Optional.of(doacao));

        Doacao resultado = doacaoService.buscarDoacaoPorId("id-1");

        assertSame(doacao, resultado);
        verify(doacaoRepository).findById("id-1");
    }

    @Test
    void deveLancarExcecaoQuandoDoacaoNaoForEncontrada() {
        when(doacaoRepository.findById("inexistente")).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(
            RuntimeException.class,
            () -> doacaoService.buscarDoacaoPorId("inexistente")
        );

        assertEquals("Doação não Encontrada", excecao.getMessage());
        verify(doacaoRepository, never()).delete(any());
    }

    @Test
    void deveListarDoacoesPorStatus() {
        List<Doacao> doacoes = List.of(criarDoacao("id-1"));
        when(doacaoRepository.findByStatus(Status.DISPONIVEL)).thenReturn(doacoes);

        List<Doacao> resultado = doacaoService.listarDoacoesPorStatus(Status.DISPONIVEL);

        assertSame(doacoes, resultado);
        verify(doacaoRepository).findByStatus(Status.DISPONIVEL);
    }

    @Test
    void deveListarDoacoesPorDoador() {
        List<Doacao> doacoes = List.of(criarDoacao("id-1"));
        when(doacaoRepository.findByNomeDoadorContainingIgnoreCase("mercado"))
            .thenReturn(doacoes);

        List<Doacao> resultado = doacaoService.listarDoacoesPorDoador("mercado");

        assertSame(doacoes, resultado);
        verify(doacaoRepository).findByNomeDoadorContainingIgnoreCase("mercado");
    }

    @Test
    void deveAtualizarDoacaoExistente() {
        Doacao existente = criarDoacao("id-1");
        Doacao dadosAtualizados = Doacao.builder()
            .nomeDoador("Novo doador")
            .descricaoAlimento("Feijão")
            .quantidade(10)
            .unidadeDeMedida("kg")
            .dataDeValidade(LocalDate.of(2027, 1, 1))
            .status(Status.COLETADO)
            .build();

        when(doacaoRepository.findById("id-1")).thenReturn(Optional.of(existente));
        when(doacaoRepository.save(existente)).thenReturn(existente);

        Doacao resultado = doacaoService.atualizarDoacao("id-1", dadosAtualizados);

        assertSame(existente, resultado);
        assertEquals("id-1", existente.getId());
        assertEquals("Novo doador", existente.getNomeDoador());
        assertEquals("Feijão", existente.getDescricaoAlimento());
        assertEquals(10, existente.getQuantidade());
        assertEquals("kg", existente.getUnidadeDeMedida());
        assertEquals(LocalDate.of(2027, 1, 1), existente.getDataDeValidade());
        assertEquals(Status.COLETADO, existente.getStatus());
        verify(doacaoRepository).save(existente);
    }

    @Test
    void deveExcluirDoacaoExistente() {
        Doacao doacao = criarDoacao("id-1");
        when(doacaoRepository.findById("id-1")).thenReturn(Optional.of(doacao));

        doacaoService.deletarDoacao("id-1");

        verify(doacaoRepository).findById("id-1");
        verify(doacaoRepository).delete(doacao);
    }

    private Doacao criarDoacao(String id) {
        return Doacao.builder()
            .id(id)
            .nomeDoador("Doador teste")
            .descricaoAlimento("Arroz")
            .quantidade(5)
            .unidadeDeMedida("kg")
            .dataDeValidade(LocalDate.of(2026, 12, 31))
            .status(Status.DISPONIVEL)
            .build();
    }
}
