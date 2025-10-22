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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Integer> buscarPorUsuario(Integer idUsuario) throws SQLException {
        List<Integer> idsComputadores = new ArrayList<>();

        String query = String.format(
                "SELECT c.Id_Computador " +
                        "FROM Computador c " +
                        "INNER JOIN Usuario u ON c.Fk_Empresa = u.Fk_Empresa " +
                        "WHERE u.Id_Usuario = %d;",
                idUsuario
        );

        ResultSet rs = this.statement.executeQuery(query);

        while (rs.next()) {
            idsComputadores.add(rs.getInt("Id_Computador"));
        }

        return idsComputadores;
    }
    public java.util.List<String> buscarNomesComponentes(int idComputador) throws SQLException {
        java.util.List<String> nomesComponentes = new java.util.ArrayList<>();
        String query = String.format(
                "select p.Valor_Parametrizado , c.Nome_Componente \n" +
                        "\t\tfrom Parametros AS p \n" +
                        "\t\tinner join Componentes as c on c.Id_Componente = p.Fk_Componente \n" +
                        "\t\tinner join Computador as comp on comp.Id_Computador = c.Fk_Computador;",
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
    public Map<String, Double> buscarTodosParametrosPorUsuario(Integer idUsuario) throws SQLException {
        Map<String, Double> parametros = new HashMap<>();

        String query = String.format(
                "SELECT c.Nome_Componente, p.Valor_Parametrizado " +
                        "FROM Usuario u " +
                        "JOIN Computador comp ON u.Fk_Empresa = comp.Fk_Empresa " +
                        "JOIN Componentes c ON comp.Id_Computador = c.Fk_Computador " +
                        "JOIN Parametros p ON c.Id_Componente = p.Fk_Componente " +
                        "WHERE u.Id_Usuario = %d;",
                idUsuario
        );

        ResultSet rs = this.statement.executeQuery(query);
        while (rs.next()) {
            String nomeComponente = rs.getString("Nome_Componente").toLowerCase();
            Double valorParametro = rs.getDouble("Valor_Parametrizado");

            parametros.put(nomeComponente, valorParametro);
        }

        return parametros;
    }


}
