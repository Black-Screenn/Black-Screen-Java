package com.sptech.school.bancoDeDadosConf;

import com.sptech.school.Domain.Componentes;
import com.sptech.school.Domain.Computador;
import com.sptech.school.Domain.Parametros;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Getter
@Setter
@RequiredArgsConstructor
public class ScriptSQL {

    ConexcaoBD conexcaoBD = new ConexcaoBD();
    Statement statement;
    Componentes componentes;
    Computador computador;
    Parametros parametros;

    public void setConnection() throws SQLException {
        if (this.conexcaoBD == null) {
            this.conexcaoBD = new ConexcaoBD();
        }
        if (this.statement == null || this.statement.isClosed()) {
            this.statement = conexcaoBD.getConnection().createStatement();
            System.out.println("INFO: Conexão e Statement inicializados com sucesso!");
        }
    }

    public Integer buscarPorUsuario(Integer idUsuario) throws SQLException {
        String query = String.format(
                "SELECT c.Id_Computador " +
                        "FROM Computador c " +
                        "INNER JOIN Log_Acesso_Computador lac ON c.Id_Computador = lac.Id_Computador " +
                        "WHERE lac.Id_Usuario = %d;",
                idUsuario
        );

        ResultSet rs = this.statement.executeQuery(query);
        if (rs.next()) {
            return rs.getInt("Id_Computador");
        }
        return null;
    }

    public java.util.List<String> buscarNomesComponentes(int idComputador) throws SQLException {
        java.util.List<String> nomesComponentes = new java.util.ArrayList<>();
        String query = String.format(
                "SELECT Nome_Componente FROM Componentes WHERE Fk_Computador = %d;",
                idComputador
        );

        ResultSet rs = this.statement.executeQuery(query);
        while (rs.next()) {
            nomesComponentes.add(rs.getString("Nome_Componente"));
        }
        return nomesComponentes;
    }

    public Double buscarParametroPorNomeDoComponente(String nomeComponente, int idComputador) throws SQLException {
        String query = String.format(
                "SELECT p.Valor_Parametrizado " +
                        "FROM Parametros p " +
                        "INNER JOIN Componentes c ON p.Fk_Componente = c.Id_Componente " +
                        "WHERE c.Nome_Componente = '%s' AND c.Fk_Computador = %d;",
                nomeComponente, idComputador
        );

        ResultSet rs = this.statement.executeQuery(query);
        if (rs.next()) {
            return rs.getDouble("Valor_Parametrizado");
        }
        return null;
    }



}
