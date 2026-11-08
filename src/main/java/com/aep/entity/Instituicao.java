package com.aep.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "instituicoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instituicao {

    @Id
    private String id;

    private String nome;
    private String email;
    private String telefone;

    @Field("endereco")
    private Endereco endereco;
}
