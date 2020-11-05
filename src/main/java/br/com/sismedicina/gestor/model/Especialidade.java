package br.com.sismedicina.gestor.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Especialidade {

    @Id
    private Integer id;
    private String descricao;

}
