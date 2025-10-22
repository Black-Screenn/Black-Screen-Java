package com.sptech.school.Lendo_CSV;

import com.sptech.school.bancoDeDadosConf.ConexcaoBD;
import com.sptech.school.bancoDeDadosConf.ScriptSQL;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

public class ETL {

    LerCsv lerCsv;
    ScriptSQL scriptSQL;
    ConexcaoBD conexcaoBD;

    public ETL(LerCsv lerCsv, ScriptSQL scriptSQL, ConexcaoBD conexcaoBD) {
        this.lerCsv = lerCsv;
        this.scriptSQL = scriptSQL;
        this.conexcaoBD = conexcaoBD;
    }
    public String csvMaquina = "src/df_capturaMaquina.csv";
    public String csvProcesso = "src/df_capturaProcesso.csv";
    public List<String[]> limparCabecalhoEValores(String nomedoCsv) {

        List<String[]> dadosOriginais = lerCsv.leituraCsv(csvMaquina);

        List<String[]> dadosLimpos = new ArrayList<>();

        String[] cabecalho = dadosOriginais.get(0);
        int indiceColuna = -1;

        for (int i = 0; i < cabecalho.length; i++) {
            String cabecalhoLimpo = cabecalho[i].trim().replace("\"", "");
        }
        dadosLimpos.add(cabecalho);

        for (int i = 1; i < dadosOriginais.size(); i++) {
            String[] linhaOriginal = dadosOriginais.get(i);
            String[] linhaLimpa = new String[linhaOriginal.length];
            for (int j = 0; j < linhaOriginal.length; j++) {
                linhaLimpa[j] = linhaOriginal[j].trim().replace("\"", "").replace(",", ".");
            }
            dadosLimpos.add(linhaLimpa);
        }
            LerCsv.escreverCsv("csvTrusted", dadosLimpos);
            return dadosLimpos;
    }
    public List<String[]> ordenarPorData(List<String[]> dadosLimpos, int indiceColuna){
    int inicioDados=1;
    int n=dadosLimpos.size();

        final DateTimeFormatter FORMATTER =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    for(int i = inicioDados; i < n - 1; i++){
        boolean Trocou = false;
        for(int j = inicioDados; j < n - 1  - (i - inicioDados); j++){
            String[]linhaAtual = dadosLimpos.get(j);
            String[] proximaLinha= dadosLimpos.get(j+1);
            try{
                LocalDateTime dataAtual =  LocalDateTime.parse(linhaAtual[indiceColuna], FORMATTER);
                LocalDateTime ProximaData = LocalDateTime.parse(proximaLinha[indiceColuna], FORMATTER);
                if(dataAtual.compareTo(ProximaData) > 0){
                    String[] temp = linhaAtual;
                    dadosLimpos.set(j,proximaLinha);
                    dadosLimpos.set(j + 1,temp);
                    Trocou = true;
                }
            }catch(Exception e){
                System.out.println("Erro na hora de ordenar a data e hora");
            }
        }
            if(!Trocou){
                break;
            }
    }
    return dadosLimpos;
    }
    public List<String[]> ordenacaoValores(List<String[]> dadosLimpos, int indiceColuna) {
            int inicioDados =1;
            int n = dadosLimpos.size();

        for(int i = inicioDados ;  i < n -1; i++){
            boolean Trocou = false;

            for(int j = inicioDados; j < n -1 - ( i - inicioDados); j++){
               String[] linhaAtual = dadosLimpos.get(j);
               String[] linhaProxima = dadosLimpos.get(j+1);
                try{
                    Double valorAtual = Double.parseDouble(linhaAtual[indiceColuna]);
                    Double valorProxima = Double.parseDouble(linhaProxima[indiceColuna]);
                    if(valorAtual.compareTo(valorProxima) > 0) {
                        String[] temp = linhaAtual;
                        dadosLimpos.set(j, linhaProxima);
                        dadosLimpos.set(j+1, temp);
                        Trocou = true;
                    }
                }catch (NumberFormatException e){
                    System.out.println("Valor invalido na encontrado na hora de ordenar os valores");
                }

            }
                if(!Trocou){
                    break;
                }
        }
        return dadosLimpos;
    }
    public List<String[]> compararCsvBd(ScriptSQL scriptSQL, LerCsv lerCsv, Integer idUsuario) throws SQLException{

        scriptSQL.setConnection();
        Integer idComputador = scriptSQL.buscarPorUsuario(idUsuario);

        List<String[]> dadosLimpos =  limparCabecalhoEValores(this.csvMaquina);
        List<String> nomesComponentes = scriptSQL.buscarNomesComponentes(idComputador);

        String[] cabecalho = dadosLimpos.get(0);

        if(nomesComponentes == null){
            System.out.println("Componente NULL");
            return new ArrayList<>();
        }if (nomesComponentes.isEmpty()){
            System.out.println("Nenhum componente encontrado");
        }

        List<String[]> linhasEmAlertas = new ArrayList<>();
        linhasEmAlertas.add(cabecalho);

        for (String nomeComponente : nomesComponentes) {
            int indiceColuna = -1;
            for(int i = 0; i < cabecalho.length; i++){
                if(cabecalho[i].equals(nomeComponente)){
                    indiceColuna = i;
                    break;
                }

            }
            Double valorParametro = scriptSQL.buscarParametroPorNomeDoComponente(nomeComponente, idComputador);
            if(valorParametro == null){
                System.out.println("Nenhum parametro encontrado para o componentne" +  nomeComponente);
                continue;
            }
            for (int i = 1; i < dadosLimpos.size(); i++){
                String[] linha = dadosLimpos.get(i);
                try{
                    Double valorAtual = Double.parseDouble(linha[indiceColuna]);
                    if (valorAtual  >  valorParametro) {
                        linhasEmAlertas.add(linha);
                    }
                }catch (Exception e){
                    System.out.println("Falha em transformar o valor em Double");
                }
            }

        }
        if(linhasEmAlertas.size() > 1){
            lerCsv.escreverCsv("/dadosEmAlertas/csv"+nomesComponentes,linhasEmAlertas);
        }
        return linhasEmAlertas;
    }


}