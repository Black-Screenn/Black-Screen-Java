package com.sptech.school.Lendo_CSV;

import ErrosETL.ErrosETL;
import com.sptech.school.Service.Client;
import com.sptech.school.bancoDeDadosConf.ConexcaoBD;
import com.sptech.school.bancoDeDadosConf.ScriptSQL;


import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class ETL extends Thread{
    public LerCsv lerCsv;
    public ScriptSQL scriptSQL;
    public ConexcaoBD conexcaoBD;

    Client meuClienteJira = new Client(
            "https://chamadosblackscreen.atlassian.net",
            "lukas.souza@sptech.school",
            "ATATT3xFfGF0urTKaF9jA2fJmF0a2ACSSzwz4Y14Cmk0-6NzvORIz4dwcJHY6-NRmYAZGnrJ8GX2Xfd>"
    );


    public ETL(LerCsv lerCsv, ScriptSQL scriptSQL, ConexcaoBD conexcaoBD) {
        this.lerCsv = lerCsv;
        this.scriptSQL = scriptSQL;
        this.conexcaoBD = conexcaoBD;
    }

    public List<String[]> limparDadosDoCsv(String caminhoCsv) {
        List<String[]> dadosOriginais = lerCsv.leituraCsv(caminhoCsv);
        if (dadosOriginais.isEmpty()) {
            return new ArrayList<>();
        }

        List<String[]> dadosLimpos = new ArrayList<>();

        String[] cabecalho = dadosOriginais.get(0);
        String[] cabecalhoLimpoArray = new String[cabecalho.length];
        for (int i = 0; i < cabecalho.length; i++) {
            cabecalhoLimpoArray[i] = cabecalho[i].trim().replace("\"", "");
        }
        dadosLimpos.add(cabecalhoLimpoArray);

        for (int i = 1; i < dadosOriginais.size(); i++) {
            String[] linhaOriginal = dadosOriginais.get(i);
            String[] linhaLimpa = new String[linhaOriginal.length];
            for (int j = 0; j < linhaOriginal.length; j++) {
                linhaLimpa[j] = linhaOriginal[j].trim().replace("\"", "");
            }
            dadosLimpos.add(linhaLimpa);
        }
        return dadosLimpos;
    }
    public List<String[]> organizarEFormatarDados(List<String[]> dadosLimpos) {
        List<String[]> dadosParaOrdenar = new ArrayList<>(dadosLimpos.subList(1, dadosLimpos.size()));

        final int indiceTimestamp = 1;
        int n = dadosParaOrdenar.size();
        DateTimeFormatter formatadorEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                try {
                    String dataAtualStr = dadosParaOrdenar.get(j)[indiceTimestamp];
                    String proximaDataStr = dadosParaOrdenar.get(j + 1)[indiceTimestamp];
                    LocalDateTime dataAtual = LocalDateTime.parse(dataAtualStr, formatadorEntrada);
                    LocalDateTime proximaData = LocalDateTime.parse(proximaDataStr, formatadorEntrada);

                    if (dataAtual.isBefore(proximaData)) {
                        String[] temp = dadosParaOrdenar.get(j);
                        dadosParaOrdenar.set(j, dadosParaOrdenar.get(j + 1));
                        dadosParaOrdenar.set(j + 1, temp);
                    }
                } catch (DateTimeParseException e) {
                    System.err.println("Aviso: Data em formato inválido na ordenação - " + e.getMessage());
                }
            }
        }

        DateTimeFormatter formatadorSaida = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<String[]> dadosProcessados = new ArrayList<>();
        dadosProcessados.add(dadosLimpos.get(0));

        for(String[] linhaOrdenada : dadosParaOrdenar) {
            try {
                LocalDateTime data = LocalDateTime.parse(linhaOrdenada[indiceTimestamp], formatadorEntrada);
                linhaOrdenada[indiceTimestamp] = data.format(formatadorSaida);
                dadosProcessados.add(linhaOrdenada);
            } catch (DateTimeParseException e) {
                dadosProcessados.add(linhaOrdenada);
            }
        }

        return dadosProcessados;
    }
    private int indice(String[] header, String columnName) {
        for (int i = 0; i < header.length; i++) {
            if (header[i].equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }
    private void verificarECriarAlertas(String[] linha, int indiceComponente, Double parametro, List<String[]> listaAlertas, int indiceUsuario, int indiceTimestamp) {
        if (indiceComponente != -1 && parametro != null) {
            try {
                double valorAtual = Double.parseDouble(linha[indiceComponente]);
                if (valorAtual > parametro) {
                    listaAlertas.add(new String[]{
                            linha[indiceUsuario],
                            linha[indiceTimestamp],
                            String.valueOf(valorAtual)
                    });
                }
            } catch (NumberFormatException e) {}
        }
    }
    public List<String[]> ordenarPorData(List<String[]> dadosLimpos) {
        final int indiceColuna = 1;
        int n = dadosLimpos.size();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (int i = 1; i < n - 1; i++) {
            boolean trocou = false;
            for (int j = 1; j < n - 1 - (i - 1); j++) {
                String dataAtualStr = null;
                String proximaDataStr = null;
                try {
                    String[] linhaAtual = dadosLimpos.get(j);
                    String[] proximaLinha = dadosLimpos.get(j + 1);
                    dataAtualStr = linhaAtual[indiceColuna].trim();
                    proximaDataStr = proximaLinha[indiceColuna].trim();
                    LocalDateTime dataAtual = LocalDateTime.parse(dataAtualStr, formatador);
                    LocalDateTime proximaData = LocalDateTime.parse(proximaDataStr, formatador);
                    if (dataAtual.isBefore(proximaData)) {
                        String[] temp = dadosLimpos.get(j);
                        dadosLimpos.set(j, dadosLimpos.get(j + 1));
                        dadosLimpos.set(j + 1, temp);
                        trocou = true;
                    }
                } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Erro durante a ordenação por data: " + e.getMessage());
                }
            }
            if (!trocou) {
                break;
            }
        }
        return dadosLimpos;
    }
    private void ordenarEsalvarAlertas(List<String[]> listaAlertas, String componente, int idUsuario) {
        if (listaAlertas.size() > 1) {
            List<String[]> alertasOrdenados = ordenarPorData(listaAlertas);

            String nomeArquivo = String.format("alertas_%s_usuario_%d.csv", componente, idUsuario);
            lerCsv.escreverCsvSobrescrevendo(nomeArquivo, alertasOrdenados);
        }
    }

    
    public void gerarAlertas (List<String[]> dadosLimpos, Integer idUsuario) throws SQLException, ErrosETL {

               scriptSQL.setConnection();

           Map<String, Double> parametros = null;
               parametros = scriptSQL.buscarTodosParametrosPorUsuario(idUsuario);
           if (parametros.isEmpty()) {
            System.out.println("Nenhum parâmetro de alerta encontrado para o usuário: " + idUsuario);
            return;
        }

        String[] cabecalhoOriginal = dadosLimpos.get(0);
        int indiceUsuario = indice(cabecalhoOriginal, "usuario");
        int indiceTimestamp = indice(cabecalhoOriginal, "timestamp");
        int indiceCpu = indice(cabecalhoOriginal, "cpu");
        int indiceRam = indice(cabecalhoOriginal, "ram");
        int indiceDisco = indice(cabecalhoOriginal, "disco");

        List<String[]> linhasAlertaCpu = new ArrayList<>();
        List<String[]> linhasAlertaRam = new ArrayList<>();
        List<String[]> linhasAlertaDisco = new ArrayList<>();

        linhasAlertaCpu.add(new String[]{"usuario", "timestamp", "AlertasCPU"});
        linhasAlertaRam.add(new String[]{"usuario", "timestamp", "AlertasRAM"});
        linhasAlertaDisco.add(new String[]{"usuario", "timestamp", "AlertasDISCO"});

        for (int i = 1; i < dadosLimpos.size(); i++) {
            String[] linhaAtual = dadosLimpos.get(i);
            if (Integer.parseInt(linhaAtual[indiceUsuario]) != idUsuario) {
                continue;
            }

            verificarECriarAlertas(linhaAtual, indiceCpu, parametros.get("cpu"), linhasAlertaCpu, indiceUsuario, indiceTimestamp);
            verificarECriarAlertas(linhaAtual, indiceRam, parametros.get("ram"), linhasAlertaRam, indiceUsuario, indiceTimestamp);
            verificarECriarAlertas(linhaAtual, indiceDisco, parametros.get("disco"), linhasAlertaDisco, indiceUsuario, indiceTimestamp);
        }


        ordenarEsalvarAlertas(linhasAlertaCpu, "cpu", idUsuario);
        ordenarEsalvarAlertas(linhasAlertaRam, "ram", idUsuario);
        ordenarEsalvarAlertas(linhasAlertaDisco, "disco", idUsuario);



    }
}
