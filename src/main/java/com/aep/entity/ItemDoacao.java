package com.aep.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDoacao {

    private String descricao;
    private Integer quantidade;
    private String unidadeDeMedida;
    private LocalDate dataDeValidade;
}
