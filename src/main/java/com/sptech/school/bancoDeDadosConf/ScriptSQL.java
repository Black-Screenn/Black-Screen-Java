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

    public Long buscarParametroPorNome(String nomeComponente, int idComputador) throws SQLException {
        String query = String.format(
                "SELECT p.Valor_Parametrizado " +
                        "FROM Parametros p " +
                        "INNER JOIN Componentes c ON p.Fk_Componente = c.Id_Componente " +
                        "WHERE c.Nome_Componente = '%s' AND c.Fk_Computador = %d;",
               nomeComponente, idComputador
        );

        ResultSet rs = this.statement.executeQuery(query);
        if (rs.next()) {
            return rs.getLong("Valor_Parametrizado");
        }
        return null;
    }

    public long buscarParametrosComponentes(Componentes componente) throws SQLException {
        String query = String.format(
                "SELECT Valor_Parametrizado FROM Parametros WHERE Fk_Componente = %d;",
                componente.getIdComponente()
        );

        ResultSet rs = this.statement.executeQuery(query);
        if (rs.next()) {
            return rs.getLong("Valor_Parametrizado");
        }
        return 0;
    }

    public String buscarComputadores(Computador computador) throws SQLException {
        String query = String.format(
                "SELECT Nome_Maquina FROM Computador WHERE Nome_Maquina = '%s';",
                computador.getNomeMaquina()
        );

        ResultSet rs = this.statement.executeQuery(query);
        if (rs.next()) {
            return rs.getString("Nome_Maquina");
        }
        return null;
    }

    public void inicializarComputadorPorNome(String nomeMaquina) throws SQLException {
        if (this.statement == null) {
            throw new IllegalStateException("O Statement não foi inicializado. Chame setConnection() primeiro.");
        }

        String query = String.format(
                "SELECT Id_Computador, Nome_Maquina FROM Computador WHERE Nome_Maquina = '%s';",
                nomeMaquina
        );

        ResultSet rs = this.statement.executeQuery(query);

        if (rs.next()) {
            int id = rs.getInt("Id_Computador");
            String nome = rs.getString("Nome_Maquina");

            Computador comp = new Computador();
            comp.setIdComputador(id);
            comp.setNomeMaquina(nome);

            this.setComputador(comp);
            System.out.println("INFO: Computador carregado com sucesso! ID: " + id);
        } else {
            this.setComputador(null);
            System.err.println("ALERTA: Máquina '" + nomeMaquina + "' não encontrada no cadastro.");
        }
    }

}
