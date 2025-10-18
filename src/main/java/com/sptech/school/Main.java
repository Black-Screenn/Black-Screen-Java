package com.sptech.school;

import com.sptech.school.Lendo_CSV.ETL;
import com.sptech.school.Lendo_CSV.LerCsv;
import com.sptech.school.bancoDeDadosConf.ScriptSQL;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        ScheduledExecutorService agendador = Executors.newScheduledThreadPool(1);

        Runnable tarefa = () -> {
            try {
                LerCsv lerCsv = new LerCsv();
                ScriptSQL scriptSQL = new ScriptSQL();
                scriptSQL.setConnection();
                ETL etl = new ETL();

                String nomeComponente = "cpu";
                String nomeCsvAlerta = "alertas.csv";
                String idUsuario = "7";
                int quantidadeAlertas = etl.compararValoresprocessarEGerarCsvAlerta(
                        lerCsv,
                        scriptSQL,
                        nomeComponente,
                        nomeCsvAlerta,
                        idUsuario
                );
                System.out.println("Quantidade de alertas gerados: " + quantidadeAlertas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        agendador.scheduleAtFixedRate(tarefa, 0, 10, TimeUnit.SECONDS);
    }
}