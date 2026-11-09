package com.aep.ui;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aep.entity.Doacao;
import com.aep.entity.Endereco;
import com.aep.entity.Instituicao;
import com.aep.entity.ItemDoacao;
import com.aep.enums.Status;
import com.aep.service.DoacaoService;
import com.aep.service.InstituicaoService;

@Component 
public class AppUi 
{
    private final DoacaoService doacaoService;
    private final InstituicaoService instituicaoService;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public AppUi(DoacaoService doacaoService, InstituicaoService instituicaoService) {
        this.doacaoService = doacaoService;
        this.instituicaoService = instituicaoService;
    }

    public AppUi(DoacaoService doacaoService) {
        this(doacaoService, null);
    }

    public void iniciar()
    {
        Integer opcao;

        do
        {
            System.out.println("\n=== Conecta Alimento ===");
            System.out.println("1 - Cadastrar doação");
            System.out.println("2 - Listar doações");
            System.out.println("3 - Buscar doação");
            System.out.println("4 - Atualizar doação");
            System.out.println("5 - Excluir doação");
            System.out.println("6 - Gerenciar instituições");
            System.out.println("7 - Cadastrar doação com itens e instituição");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> cadastrarDoacao();
                case 2 -> listarDoacoes();
                case 3 -> buscarDoacao();
                case 4 -> atualizarDoacao();
                case 5 -> deletarDoacao();
                case 6 -> gerenciarInstituicoes();
                case 7 -> cadastrarDoacaoComplexa();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private void cadastrarDoacao()
    {
        Doacao doacao = new Doacao();

        doacao = setarDadosDoacao(doacao);

        Doacao salvo = doacaoService.salvarDoacao(doacao);
        System.out.println("Doação Cadastrada: " + salvo.getId());
    }

    private void cadastrarDoacaoComplexa()
    {
        Doacao doacao = setarDadosDoacao(new Doacao());
        System.out.print("ID da instituição relacionada (Enter para ignorar) > ");
        String instituicaoId = scanner.nextLine();
        if (!instituicaoId.isBlank())
        {
            if (instituicaoService == null)
            {
                System.out.println("Serviço de instituições indisponível.");
                return;
            }
            instituicaoService.buscarPorId(instituicaoId);
            doacao.setInstituicaoId(instituicaoId);
        }

        System.out.print("Quantidade de itens detalhados (0 para ignorar) > ");
        int quantidadeItens = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < quantidadeItens; i++)
        {
            System.out.print("Descrição do item " + (i + 1) + " > ");
            String descricao = scanner.nextLine();
            System.out.print("Quantidade > ");
            Integer quantidade = Integer.parseInt(scanner.nextLine());
            System.out.print("Unidade de medida > ");
            String unidade = scanner.nextLine();
            System.out.print("Data de validade (YYYY-MM-DD) > ");
            LocalDate validade = LocalDate.parse(scanner.nextLine());
            doacao.getItens().add(ItemDoacao.builder().descricao(descricao).quantidade(quantidade)
                .unidadeDeMedida(unidade).dataDeValidade(validade).build());
        }

        Doacao salvo = doacaoService.salvarDoacao(doacao);
        System.out.println("Doação complexa cadastrada: " + salvo.getId());
    }

    private Doacao setarDadosDoacao(Doacao doacao)
    {
        System.out.print("Nome do Doador > ");
        doacao.setNomeDoador(scanner.nextLine());

        System.out.print("Descrição do Alimento > ");
        doacao.setDescricaoAlimento(scanner.nextLine());

        System.out.print("Unidade de Medida (Ex: kg, un, ton) > ");
        doacao.setUnidadeDeMedida(scanner.nextLine());

        System.out.print("Quantidade (? " + doacao.getUnidadeDeMedida() + ") > ");
        doacao.setQuantidade(Integer.parseInt(scanner.nextLine()));

        System.out.print("Data de Validade (YYYY-MM-DD) > ");
        doacao.setDataDeValidade(LocalDate.parse(scanner.nextLine()));

        System.out.println("Status " + Arrays.toString(Status.values()) + " > ");
        doacao.setStatus(Status.valueOf(scanner.nextLine().toUpperCase()));

        return doacao;
    }

    private void gerenciarInstituicoes()
    {
        if (instituicaoService == null)
        {
            System.out.println("Serviço de instituições indisponível.");
            return;
        }

        Integer opcao;
        do
        {
            System.out.println("\n=== INSTITUIÇÕES ===");
            System.out.println("1 - Cadastrar instituição");
            System.out.println("2 - Listar instituições");
            System.out.println("3 - Buscar instituição por nome");
            System.out.println("0 - Voltar");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao)
            {
                case 1 -> cadastrarInstituicao();
                case 2 -> instituicaoService.listar().forEach(this::mostrarInstituicao);
                case 3 -> instituicaoService.buscarPorNome(scanner.nextLine()).forEach(this::mostrarInstituicao);
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private void cadastrarInstituicao()
    {
        System.out.print("Nome da instituição > ");
        String nome = scanner.nextLine();
        System.out.print("E-mail > ");
        String email = scanner.nextLine();
        System.out.print("Telefone > ");
        String telefone = scanner.nextLine();
        System.out.print("Rua > ");
        String rua = scanner.nextLine();
        System.out.print("Número > ");
        String numero = scanner.nextLine();
        System.out.print("Cidade > ");
        String cidade = scanner.nextLine();
        System.out.print("Estado > ");
        String estado = scanner.nextLine();
        System.out.print("CEP > ");
        String cep = scanner.nextLine();

        Instituicao instituicao = Instituicao.builder()
            .nome(nome)
            .email(email)
            .telefone(telefone)
            .endereco(Endereco.builder().rua(rua).numero(numero).cidade(cidade)
                .estado(estado).cep(cep).build())
            .build();

        System.out.println("Instituição cadastrada: " + instituicaoService.salvar(instituicao).getId());
    }

    private void mostrarInstituicao(Instituicao instituicao)
    {
        System.out.println("\nInstituição: " + instituicao.getNome());
        System.out.println("ID: " + instituicao.getId());
        System.out.println("Contato: " + instituicao.getEmail() + " | " + instituicao.getTelefone());
        if (instituicao.getEndereco() != null)
        {
            Endereco endereco = instituicao.getEndereco();
            System.out.println("Endereço: " + endereco.getRua() + ", " + endereco.getNumero()
                + " - " + endereco.getCidade() + "/" + endereco.getEstado());
        }
    }

    private void listarDoacoes()
    {
        List<Doacao> doacoes = doacaoService.listarDoacoes();
        mostrarDoacoes(doacoes);
    }

    private void mostrarDoacoes(List<Doacao> doacoes)
    {
        if (doacoes.isEmpty())
        {
            System.out.println("Nenhuma doação encontrada.");
            return;
        }

        doacoes.forEach(this::mostrarDoacao);
    }

    private void mostrarDoacao(Doacao doacao)
    {
        System.out.println("\n----------------------------------------");
        System.out.println("ID: " + doacao.getId());
        System.out.println("Doador: " + doacao.getNomeDoador());
        System.out.println("Alimento: " + doacao.getDescricaoAlimento());
        System.out.println("Quantidade: " + doacao.getQuantidade() + " " + doacao.getUnidadeDeMedida());
        System.out.println("Validade: " + doacao.getDataDeValidade());
        System.out.println("Status: " + doacao.getStatus());
        if (doacao.getInstituicaoId() != null)
            System.out.println("Instituição relacionada: " + doacao.getInstituicaoId());
        if (doacao.getItens() != null && !doacao.getItens().isEmpty())
            System.out.println("Itens detalhados: " + doacao.getItens().size());
        System.out.println("----------------------------------------");
    }

    private void buscarDoacao()
    {
        Integer opcao;

        do
        {
            System.out.println("=== BUSCAR DOACOES ===");
            System.out.println("1 - POR ID");
            System.out.println("2 - POR STATUS");
            System.out.println("3 - POR DOADOR");
            System.out.println("4 - POR INSTITUIÇÃO");
            System.out.println("0 - SAIR");

            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao)
            {
                case 1 -> buscarDoacaoPorId();
                case 2 -> buscarDoacaoPorStatus();
                case 3 -> buscarDoacaoPorDoador();
                case 4 -> buscarDoacaoPorInstituicao();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção Inválida");
            }

        } while (opcao != 0);

        
    }

    private void buscarDoacaoPorId()
    {
        System.out.print("Digite o ID da Doação > ");
        String id = scanner.nextLine();

        Doacao doacao = doacaoService.buscarDoacaoPorId(id);

        mostrarDoacao(doacao);
    }

    private void buscarDoacaoPorStatus()
    {
        System.out.print("Digite um Status " + Arrays.toString(Status.values()) + " > ");
        Status status = Status.valueOf( scanner.nextLine().toUpperCase() );

        List<Doacao> doacoes = doacaoService.listarDoacoesPorStatus(status);
        mostrarDoacoes(doacoes);
    }

    private void buscarDoacaoPorDoador()
    {
        System.out.print("Digite um Doador > ");
        String doador = scanner.nextLine();

        List<Doacao> doacoes = doacaoService.listarDoacoesPorDoador(doador);
        mostrarDoacoes(doacoes);
    }

    private void buscarDoacaoPorInstituicao()
    {
        System.out.print("Digite o ID da Instituição > ");
        String instituicaoId = scanner.nextLine();

        List<Doacao> doacoes = doacaoService.listarDoacoesPorInstituicao(instituicaoId);
        mostrarDoacoes(doacoes);
    }

    private void atualizarDoacao()
    {
        System.out.print("Digite o ID da Doação > ");
        String id = scanner.nextLine();

        Doacao doacao = doacaoService.buscarDoacaoPorId(id);

        doacao = setarDadosDoacao(doacao);

        doacaoService.atualizarDoacao(id, doacao);
        System.out.println("Doação ID " + id + " atualizada.");
    }

    private void deletarDoacao()
    {
        System.out.print("Digite o ID da Doação > ");
        String id = scanner.nextLine();

        doacaoService.deletarDoacao(id);
        System.out.println("Doação Deletada.");
    }
}
