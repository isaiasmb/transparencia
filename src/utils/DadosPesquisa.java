package utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author isaias
 */
public class DadosPesquisa {

    private Map<String, String> elementoDespesaMap;

    public Map getElementoDespesaMap() {
        elementoDespesaMap = new LinkedHashMap<>();
        elementoDespesaMap.put("TOD", "Todos");
        elementoDespesaMap.put("01", "01 - APOSENT.RPPS, RESER. REMUNER. E REFOR.MILITAR");
        elementoDespesaMap.put("53", "53 - APOSENTADORIAS DO RGPS - AREA RURAL");
        elementoDespesaMap.put("54", "54 - APOSENTADORIAS DO RGPS - AREA URBANA");
        elementoDespesaMap.put("62", "62 - AQUISICAO DE BENS PARA REVENDA");
        elementoDespesaMap.put("61", "61 - AQUISICAO DE IMOVEIS");
        elementoDespesaMap.put("63", "63 - AQUISICAO DE TITULOS DE CREDITO");
        elementoDespesaMap.put("64", "64 - AQUISICAO TIT.REPRES.DE CAP. JA INTEGRALIZADO");
        elementoDespesaMap.put("38", "38 - ARRENDAMENTO MERCANTIL");
        elementoDespesaMap.put("18", "18 - AUXILIO FINANCEIRO A ESTUDANTES");
        elementoDespesaMap.put("20", "20 - AUXILIO FINANCEIRO A PESQUISADORES");
        elementoDespesaMap.put("46", "46 - AUXILIO-ALIMENTACAO");
        elementoDespesaMap.put("19", "19 - AUXILIO-FARDAMENTO");
        elementoDespesaMap.put("42", "42 - AUXILIOS – FUNDO A FUNDO");
        elementoDespesaMap.put("49", "49 - AUXILIO-TRANSPORTE");
        elementoDespesaMap.put("06", "06 - BENEFICIO MENSAL AO DEFICIENTE E AO IDOSO");
        elementoDespesaMap.put("73", "73 - CM OU CAMBIAL DA DIVIDA CONTRATUAL RESGATADA");
        elementoDespesaMap.put("98", "98 - COMPENSACOES AO RGPS ");
        elementoDespesaMap.put("66", "66 - CONCESSAO DE EMPRESTIMOS E FINANCIAMENTOS");
        elementoDespesaMap.put("65", "65 - CONSTIT. OU AUMENTO DE CAPITAL DE EMPRESAS");
        elementoDespesaMap.put("04", "04 - CONTRATACAO POR TEMPO DETERMINADO");
        elementoDespesaMap.put("07", "07 - CONTRIB. A ENTIDADES FECHADAS DE PREVIDENCIA");
        elementoDespesaMap.put("41", "41 - CONTRIBUICOES");
        elementoDespesaMap.put("74", "74 - COR.MONET.E CAMBIAL DA DIV.MOBIL.RESGATADA");
        elementoDespesaMap.put("75", "75 - COR.MONET.OPER.DE CRED.POR ANTEC.DA RECEITA");
        elementoDespesaMap.put("67", "67 - DEPOSITOS COMPULSORIOS");
        elementoDespesaMap.put("92", "92 - DESPESAS DE EXERCICIOS ANTERIORES");
        elementoDespesaMap.put("14", "14 - DIARIAS - CIVIL");
        elementoDespesaMap.put("15", "15 - DIARIAS -	PESSOAL MILITAR");
        elementoDespesaMap.put("81", "81 - DISTRIBUICAO DE RECEITAS");
        elementoDespesaMap.put("29", "29 - DIVIDENDOS - EMPRESAS ESTATAIS DEPENDENTES");
        elementoDespesaMap.put("27", "27 - ENC.P/ HONRA DE AVAIS, GARANT.,SEGUROS E SIM.");
        elementoDespesaMap.put("25", "25 - ENC.SOBRE OPER.DE CRED.POR ANTEC. DA RECEITA");
        elementoDespesaMap.put("52", "52 - EQUIPAMENTOS E MATERIAL PERMANENTE");
        elementoDespesaMap.put("95", "95 - INDENIZACAO PELA EXECUCAO TRABALHOS DE CAMPO");
        elementoDespesaMap.put("93", "93 - INDENIZACOES E RESTITUICOES");
        elementoDespesaMap.put("94", "94 - INDENIZACOES TRABALHISTAS");
        elementoDespesaMap.put("21", "21 - JUROS SOBRE A DIVIDA POR CONTRATO – LC141/12");
        elementoDespesaMap.put("23", "23 - JUROS,DESAGIOS E DESCONTOS DA DIV. MOBILIARIA");
        elementoDespesaMap.put("37", "37 - LOCACAO DE MAO-DE-OBRA");
        elementoDespesaMap.put("30", "30 - MATERIAL DE CONSUMO");
        elementoDespesaMap.put("32", "32 - MATERIAL, BEM OU SERVICO P/ DISTRIB. GRATUITA");
        elementoDespesaMap.put("51", "51 - OBRAS E INSTALACOES");
        elementoDespesaMap.put("47", "47 - OBRIGACOES  TRIBUTARIAS E  CONTRIBUTIVAS");
        elementoDespesaMap.put("26", "26 - OBRIGAÇÕES DECORRENTES DE POLÍTICA MONETÁRIA");
        elementoDespesaMap.put("13", "13 - OBRIGACOES PATRONAIS");
        elementoDespesaMap.put("34", "34 - OUTRAS DESPESAS DE PESSOAL - TERCEIRIZACAO");
        elementoDespesaMap.put("16", "16 - OUTRAS DESPESAS VARIAVEIS - PESSOAL CIVIL");
        elementoDespesaMap.put("17", "17 - OUTRAS DESPESAS VARIAVEIS - PESSOAL MILITAR");
        elementoDespesaMap.put("48", "48 - OUTROS AUXILIOS FINANCEIROS A PESSOA FISICA");
        elementoDespesaMap.put("08", "08 - OUTROS BENEF.ASSIST.DO SERVIDOR E DO MILITAR");
        elementoDespesaMap.put("57", "57 - OUTROS BENEFICIOS DO RGPS - AREA RURAL");
        elementoDespesaMap.put("58", "58 - OUTROS BENEFICIOS DO RGPS - AREA URBANA");
        elementoDespesaMap.put("05", "05 - OUTROS BENEFICIOS PREVIDENCIARIOS DO RPPS");
        elementoDespesaMap.put("24", "24 - OUTROS ENCARGOS SOBRE A DIVIDA MOBILIARIA");
        elementoDespesaMap.put("22", "22 - OUTROS ENCARGOS SOBRE A DIVIDA POR CONTRATO");
        elementoDespesaMap.put("36", "36 - OUTROS SERVICOS DE TERCEIROS - PESSOA FISICA");
        elementoDespesaMap.put("39", "39 - OUTROS SERVICOS DE TERCEIROS-PESSOA JURIDICA");
        elementoDespesaMap.put("33", "33 - PASSAGENS E DESPESAS COM LOCOMOCAO");
        elementoDespesaMap.put("55", "55 - PENSOES DO RGPS - AREA RURAL");
        elementoDespesaMap.put("56", "56 - PENSOES DO RGPS - AREA URBANA");
        elementoDespesaMap.put("59", "59 - PENSOES ESPECIAIS");
        elementoDespesaMap.put("03", "03 - PENSOES, EXCLUSIVE DO RGPS");
        elementoDespesaMap.put("31", "31 - PREMIACOES CULTURAIS, ARTISTICAS, CIENTIFICAS");
        elementoDespesaMap.put("77", "77 - PRINCIPAL CORRIGIDO DIVIDA CONTRATUAL RESG.");
        elementoDespesaMap.put("71", "71 - PRINCIPAL DA DIVIDA CONTRATUAL RESGATADO");
        elementoDespesaMap.put("76", "76 - PRINCIPAL DA DIVIDA MOBILIARIA REFINANCIADA");
        elementoDespesaMap.put("72", "72 - PRINCIPAL DA DIVIDA MOBILIARIA RESGATADA");
        elementoDespesaMap.put("70", "70 - RATEIO PELA PARTICIPACAO EM CONSORCIO PUBLICO");
        elementoDespesaMap.put("99", "99 - REGIME DE EXECUCAO ESPECIAL");
        elementoDespesaMap.put("28", "28 - REMUNERACAO DE COTAS DE FUNDOS AUTARQUICOS");
        elementoDespesaMap.put("96", "96 - RESSARC. DE DESPESAS DE PESSOAL REQUISITADO");
        elementoDespesaMap.put("09", "09 - SALARIO-FAMILIA");
        elementoDespesaMap.put("10", "10 - SEGURO DESEMPREGO E ABONO SALARIAL");
        elementoDespesaMap.put("91", "91 - SENTENCAS JUDICIAIS");
        elementoDespesaMap.put("35", "35 - SERVICOS DE CONSULTORIA");
        elementoDespesaMap.put("45", "45 - SUBVENCOES ECONOMICAS");
        elementoDespesaMap.put("43", "43 - SUBVENCOES SOCIAIS");
        elementoDespesaMap.put("12", "12 - VENCIMENTOS E VANTAGENS FIXAS - PES. MILITAR");
        elementoDespesaMap.put("11", "11 - VENCIMENTOS E VANTAGENS FIXAS - PESSOAL CIVIL");

        return elementoDespesaMap;
    }
}
