package com.aep.entity;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.aep.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "doacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doacao
{
    @Id
    private UUID id;

    @Field("nome_doador")
    private String nomeDoador;

    @Field("descricao_alimento")
    private String descricaoAlimento;

    @Field("quantidade")
    private Integer quantidade;

    @Field("unidade_de_medida")
    private String unidadeDeMedida;

    @Field("data_de_validade")
    private LocalDate dataDeValidade;

    @Field("status")
    private Status status;
}