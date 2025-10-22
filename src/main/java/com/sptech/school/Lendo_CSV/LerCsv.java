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
    public static void escreverCsv(String novoNomeCsv, List<String[]> linhas) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(novoNomeCsv,true), StandardCharsets.UTF_8))) {

            for (String[] linha: linhas) {
                    String linhaCsv = String.join(",",linha);
                    writer.write(linhaCsv);
                    writer.newLine();
            }
            System.out.println("Arquivo CSV escrito com sucesso: " + novoNomeCsv);
        } catch (IOException e) {
            System.out.println("Erro ao escrever o arquivo: " + e);
        }
    }


}




