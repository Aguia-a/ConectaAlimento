package com.aep.ui;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.aep.entity.Doacao;
import com.aep.enums.Status;
import com.aep.service.DoacaoService;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor
public class AppUi 
{
    private final DoacaoService doacaoService;
    private final Scanner scanner = new Scanner(System.in);

    public void iniciar()
    {
        Integer opcao;

        do
        {
            System.out.println("\n=== Conecta Alimento ===");
            System.out.println("1 - Cadastrar doação");
            System.out.println("2 - Listar doações");
            System.out.println("3 - Buscar por status");
            System.out.println("4 - Atualizar doação");
            System.out.println("5 - Excluir doação");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> cadastrarDoacao();
                case 2 -> listarDoacoes();
                case 3 -> buscarDoacao();
                case 4 -> atualizarDoacao();
                case 5 -> deletarDoacao();
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
            System.out.println("0 - SAIR");

            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao)
            {
                case 1 -> buscarDoacaoPorId();
                case 2 -> buscarDoacaoPorStatus();
                case 3 -> buscarDoacaoPorDoador();
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
