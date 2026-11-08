package com.aep.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aep.entity.Endereco;
import com.aep.entity.Instituicao;
import com.aep.repository.InstituicaoRepository;

@ExtendWith(MockitoExtension.class)
class InstituicaoServiceTest {

    @Mock
    private InstituicaoRepository instituicaoRepository;

    private InstituicaoService instituicaoService;

    @BeforeEach
    void setUp() {
        instituicaoService = new InstituicaoService(instituicaoRepository);
    }

    @Test
    void deveGerarIdESalvarInstituicao() {
        Instituicao instituicao = instituicaoValida();
        when(instituicaoRepository.save(instituicao)).thenReturn(instituicao);

        Instituicao resultado = instituicaoService.salvar(instituicao);

        assertNotNull(resultado.getId());
        verify(instituicaoRepository).save(instituicao);
    }

    @Test
    void deveBuscarInstituicaoPorId() {
        Instituicao instituicao = instituicaoValida();
        instituicao.setId("id-1");
        when(instituicaoRepository.findById("id-1")).thenReturn(Optional.of(instituicao));

        assertEquals(instituicao, instituicaoService.buscarPorId("id-1"));
    }

    @Test
    void deveRejeitarInstituicaoSemCidade() {
        Instituicao instituicao = instituicaoValida();
        instituicao.getEndereco().setCidade("");

        assertThrows(IllegalArgumentException.class, () -> instituicaoService.salvar(instituicao));
    }

    @Test
    void deveListarEBuscarPorNome() {
        when(instituicaoRepository.findAll()).thenReturn(List.of(instituicaoValida()));
        when(instituicaoRepository.findByNomeContainingIgnoreCase("casa"))
            .thenReturn(List.of(instituicaoValida()));

        assertEquals(1, instituicaoService.listar().size());
        assertEquals(1, instituicaoService.buscarPorNome("casa").size());
    }

    @Test
    void deveAtualizarInstituicao() {
        Instituicao existente = instituicaoValida();
        existente.setId("id-1");
        Instituicao dados = instituicaoValida();
        dados.setNome("Novo nome");
        when(instituicaoRepository.findById("id-1")).thenReturn(Optional.of(existente));
        when(instituicaoRepository.save(existente)).thenReturn(existente);

        Instituicao resultado = instituicaoService.atualizar("id-1", dados);

        assertEquals("Novo nome", resultado.getNome());
        verify(instituicaoRepository).save(existente);
    }

    @Test
    void deveExcluirInstituicao() {
        Instituicao instituicao = instituicaoValida();
        when(instituicaoRepository.findById("id-1")).thenReturn(Optional.of(instituicao));

        instituicaoService.excluir("id-1");

        verify(instituicaoRepository).delete(instituicao);
    }

    @Test
    void naoDeveSalvarInstituicaoComNomeVazio() {
        Instituicao instituicao = instituicaoValida();
        instituicao.setNome(" ");

        assertThrows(IllegalArgumentException.class, () -> instituicaoService.salvar(instituicao));
        verify(instituicaoRepository, never()).save(instituicao);
    }

    private Instituicao instituicaoValida() {
        return Instituicao.builder()
            .nome("Casa Esperança")
            .email("contato@esperanca.org")
            .telefone("(44) 99999-9999")
            .endereco(Endereco.builder()
                .rua("Rua A").numero("100").cidade("Maringá").estado("PR").cep("87000-000").build())
            .build();
    }
}
