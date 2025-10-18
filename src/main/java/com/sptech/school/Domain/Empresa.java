package com.sptech.school.Domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Empresa {

  private int idEmpresa;
  private String nome;
  private String cnpj;
  private String razaoSocial;
  private String telefone;
  private int fkEndereco;

    public Empresa(int idEmpresa,String nome, String cnpj, String razaoSocial, String telefone, int fkEndereco){
    this.idEmpresa=idEmpresa;
    this.nome=nome;
    this.cnpj=cnpj;
    this.razaoSocial=razaoSocial;
    this.telefone=telefone;
    this.fkEndereco=fkEndereco;
    }
}
