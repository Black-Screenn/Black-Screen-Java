package com.sptech.school.Lendo_CSV;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class LerCsv {


    public List<String[]> leituraCsv(String nomeCsv) {
        List<String[]> dados = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(nomeCsv), StandardCharsets.UTF_8))) {
            String linha;
            System.out.println("Conteúdo do arquivo: " + nomeCsv);

            while ((linha = reader.readLine()) != null) {
                String[] colunas = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (colunas.length > 0) {
                    colunas[0] = colunas[0].trim().replace("\"", "");
                }
                dados.add(colunas);
                System.out.println(colunas.length);
                System.out.println(linha);
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        return dados;
    }
    public static void escreverCsv(String csvRefatorado, List<String[]> linhas, int indiceColunaSelecionada) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(csvRefatorado,true), StandardCharsets.UTF_8))) {

            if(!linhas.isEmpty()){
                String[] cabecalho = linhas.get(0);
                if(indiceColunaSelecionada>=0 && indiceColunaSelecionada < cabecalho.length){
                    writer.write(cabecalho[indiceColunaSelecionada]);
                    writer.newLine();
                }
            }
            for (int i = 1; i < linhas.size(); i++) {
                String[] linha = linhas.get(i);
                if(indiceColunaSelecionada>=0 && indiceColunaSelecionada < linha.length){
                    String valorColuna = linha[indiceColunaSelecionada];
                    writer.write(valorColuna);
                    writer.newLine();
                }
            }
            System.out.println("Arquivo CSV escrito com sucesso: " + csvRefatorado);
        } catch (IOException e) {
            System.out.println("Erro ao escrever o arquivo: " + e);
        }
    }


}




