package com.sptech.school;

import com.sptech.school.Lendo_CSV.ETL;
import com.sptech.school.Lendo_CSV.LerCsv;
import com.sptech.school.bancoDeDadosConf.ConexcaoBD;
import com.sptech.school.bancoDeDadosConf.ScriptSQL;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    private static final String CAMINHO_CSV_LOCAL = "src/df_capturaMaquina.csv";

    public static void main(String[] args) throws Exception {
        System.out.println("--- INICIANDO PROCESSO DE ETL ---");

        LerCsv leitorEscritor = new LerCsv();
        ConexcaoBD conexaoBD = new ConexcaoBD();
        ScriptSQL scriptSQL = new ScriptSQL();
        ETL etl = new ETL(leitorEscritor, scriptSQL, conexaoBD);

        System.out.println("\nPASSO 1: Lendo e limpando dados de " + CAMINHO_CSV_LOCAL);
        List<String[]> dadosLimpos = etl.limparDadosDoCsv(CAMINHO_CSV_LOCAL);
        if(dadosLimpos.isEmpty()) {
            System.out.println("Arquivo CSV está vazio ou não foi encontrado. Finalizando processo.");
            return;
        }
        System.out.println("-> Dados limpos e carregados na memória.");

        System.out.println("\nPASSO 2: Ordenando por data e formatando timestamps...");
        List<String[]> dadosProcessados = etl.organizarEFormatarDados(dadosLimpos);
        String arquivoOrganizado = "dados_organizados_por_data.csv";
        leitorEscritor.escreverCsvSobrescrevendo(arquivoOrganizado, dadosProcessados);
        System.out.println("-> Arquivo com dados organizados e formatados foi criado: " + arquivoOrganizado);

        System.out.println("\nPASSO 3: Gerando relatórios de alertas...");
        Set<Integer> usuariosNoCsv = extrairUsuariosUnicos(dadosLimpos);
        System.out.println("Usuários encontrados no CSV para verificação: " + usuariosNoCsv);

        for (Integer idUsuario : usuariosNoCsv) {
            etl.gerarAlertas(dadosLimpos, idUsuario);
        }
        System.out.println("-> Processo de alertas concluído. Verifique os arquivos 'alertas_*.csv'.");

        System.out.println("\n--- PROCESSO DE ETL CONCLUÍDO ---");
    }

    public static Set<Integer> extrairUsuariosUnicos(List<String[]> dados) {
        Set<Integer> usuarios = new HashSet<>();
        if (dados.isEmpty()) return usuarios;
        String[] cabecalho = dados.get(0);
        int indiceUsuario = -1;
        for (int i = 0; i < cabecalho.length; i++) {
            if (cabecalho[i].equalsIgnoreCase("usuario")) {
                indiceUsuario = i;
                break;
            }
        }
        if (indiceUsuario == -1) return usuarios;

        for (int i = 1; i < dados.size(); i++) {
            try {
                usuarios.add(Integer.parseInt(dados.get(i)[indiceUsuario]));
            } catch (NumberFormatException e) {}
        }
        return usuarios;
    }
}