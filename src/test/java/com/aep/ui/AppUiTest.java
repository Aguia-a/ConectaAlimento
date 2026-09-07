package com.aep.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aep.entity.Doacao;
import com.aep.enums.Status;
import com.aep.service.DoacaoService;

@ExtendWith(MockitoExtension.class)
class AppUiTest {

    @Mock
    private DoacaoService doacaoService;

    private InputStream entradaOriginal;

    @BeforeEach
    void setUp() {
        entradaOriginal = System.in;
    }

    @AfterEach
    void tearDown() {
        System.setIn(entradaOriginal);
    }

    @Test
    void deveEncerrarQuandoUsuarioEscolherSair() {
        executar("0");
    }

    @Test
    void deveCadastrarDoacao() {
        Doacao doacao = criarDoacao(null);
        when(doacaoService.salvarDoacao(any(Doacao.class))).thenReturn(doacao);

        executar("1", "Doador teste", "Arroz", "kg", "5", "2026-12-31", "DISPONIVEL", "0");

        verify(doacaoService).salvarDoacao(any(Doacao.class));
    }

    @Test
    void deveListarDoacoes() {
        when(doacaoService.listarDoacoes()).thenReturn(List.of(criarDoacao("id-1")));

        executar("2", "0");

        verify(doacaoService).listarDoacoes();
    }

    @Test
    void deveExecutarTodasAsBuscas() {
        Doacao doacao = criarDoacao("id-1");
        when(doacaoService.buscarDoacaoPorId("id-1")).thenReturn(doacao);
        when(doacaoService.listarDoacoesPorStatus(Status.DISPONIVEL)).thenReturn(List.of(doacao));
        when(doacaoService.listarDoacoesPorDoador("Doador")).thenReturn(List.of(doacao));

        executar("3", "1", "id-1", "2", "DISPONIVEL", "3", "Doador", "0", "0");

        verify(doacaoService).buscarDoacaoPorId("id-1");
        verify(doacaoService).listarDoacoesPorStatus(Status.DISPONIVEL);
        verify(doacaoService).listarDoacoesPorDoador("Doador");
    }

    @Test
    void deveAtualizarDoacao() {
        Doacao doacao = criarDoacao("id-1");
        when(doacaoService.buscarDoacaoPorId("id-1")).thenReturn(doacao);
        when(doacaoService.atualizarDoacao("id-1", doacao)).thenReturn(doacao);

        executar("4", "id-1", "Novo doador", "Feijao", "kg", "10", "2027-01-01", "COLETADO", "0");

        verify(doacaoService).atualizarDoacao("id-1", doacao);
    }

    @Test
    void deveExcluirDoacao() {
        executar("5", "id-1", "0");

        verify(doacaoService).deletarDoacao("id-1");
    }

    private void executar(String... entradas) {
        String conteudo = String.join(System.lineSeparator(), entradas) + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(conteudo.getBytes()));
        new AppUi(doacaoService).iniciar();
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
