package parseadortransparencia;

import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parseador.http.HttpContainer;

/**
 *
 * @author bruno
 */
public class ParseadorTransparencia implements Runnable {

    private String periodoInicio;
    private String periodoFim;
    private String codigoOS;
    private String codigoOrgao;
    private String codigoUG;
    private String codigoED;
    private String codigoFavorecido;
    private HttpContainer httpContainer;
    private CloseableHttpClient httpClient;
    private HttpContext httpContext;
    private String nomeArquivo;
    private Integer contadorPeriodos = 0;
    private final Map<String, String> map;
    private final Map<Integer, String> dataInicialMap = new LinkedHashMap<>();
    private final Map<Integer, String> dataFinalMap = new LinkedHashMap<>();
    private final List<String> listaDespesasDocumento = new ArrayList<>();

    public ParseadorTransparencia(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public void run() {
        try {
            BasicCookieStore cookieStore = new BasicCookieStore();
            httpContext = new BasicHttpContext();
            httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
            httpClient = HttpClients.custom()
                    .setDefaultCookieStore(cookieStore)
                    .build();
            httpContainer = new HttpContainer(httpClient, httpContext);

            carregaCampos(map);

            String conteudoPagina = null;
            Integer dias;

            dias = processaData();
            if (dias > 30) {
                System.out.println("Quantidade de meses: " + dataInicialMap.size());
                for (int i = 0; i < dataInicialMap.size(); i++) {
                    if (dataInicialMap.get(i) != null) {
                        String periodoInicio = dataInicialMap.get(i);
                        String periodoFim = dataFinalMap.get(i);
                        conteudoPagina = realizarFiltro(httpContainer, periodoInicio, periodoFim);
                        Thread.sleep(30000);
                        geraArquivo(conteudoPagina, dataInicialMap.size());
                    }
                }
            } else {
                DateTimeFormatter patternDate = DateTimeFormat.forPattern("dd/MM/yyyy");
                DateTime dataInicial = new DateTime(new Date(periodoInicio));
                DateTime dataFim = new DateTime(new Date(periodoFim));
                String periodoInicio = dataInicial.toString(patternDate);
                String periodoFim = dataFim.toString(patternDate);
                conteudoPagina = realizarFiltro(httpContainer, periodoInicio, periodoFim);
                geraArquivo(conteudoPagina, 1);
            }
        } catch (ParseException ex) {
            Logger.getLogger(ParseadorTransparencia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ParseadorTransparencia.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String realizarFiltro(HttpContainer httpContainer, String periodoInicio, String periodoFim) throws Exception {
        String fontePagina = "";
        CloseableHttpClient httpClient = httpContainer.getHttpClient();
        HttpContext httpContext = httpContainer.getHttpContext();
        HttpGet httpGet = null;

        try {
            URIBuilder builder = new URIBuilder();
            builder.setScheme("http").setHost("www.portaltransparencia.gov.br").setPath("/despesasdiarias/resultado")
                    .setParameter("consulta", "avancada")
                    .setParameter("periodoInicio", periodoInicio)
                    .setParameter("periodoFim", periodoFim)
                    .setParameter("fase", "EMP")
                    .setParameter("codigoOS", codigoOS)
                    .setParameter("codigoOrgao", codigoOrgao)
                    .setParameter("codigoUG", codigoUG)
                    .setParameter("codigoED", codigoED)
                    .setParameter("codigoFavorecido", codigoFavorecido);
            URI uri = builder.build();
            httpGet = new HttpGet(uri);
            System.out.println(httpGet.getURI());
            CloseableHttpResponse respostaRequisicaoInicial = httpClient.execute(httpGet, httpContext);
            fontePagina = EntityUtils.toString(respostaRequisicaoInicial.getEntity());

        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        return fontePagina;
    }

    private Integer encontrarUltimaPagina(String fontePagina) throws Exception {
        Document doc = Jsoup.parse(fontePagina);
        Integer numMaxPagina;
        Element content = doc.getElementsByClass("paginaXdeN").first();

        if (content == null) {
            numMaxPagina = 1;
        } else {
            System.out.println("Elemento: " + content.toString());
            String texto = content.toString();
            int posInicial = texto.indexOf("Página 1 de");
            System.out.println("Índice 1: " + posInicial);
            int posFinal = texto.indexOf("</span>");
            System.out.println("Índice 2: " + posFinal);
            String numMaxPaginaStr = texto.substring(posInicial + 12, posFinal);
            System.out.println("QTD PÁGINAS: " + numMaxPaginaStr);
            try {
                numMaxPagina = Integer.parseInt(numMaxPaginaStr);
            } catch (NumberFormatException ex) {
                throw new Exception(ex);
            }
        }

        return numMaxPagina;
    }

    private List<String> buscarDocumentosDaPagina(CloseableHttpClient httpClient, HttpContext httpContext, Integer pagina) throws Exception {
        String linkComum = "http://www.portaltransparencia.gov.br/despesasdiarias/";
        HttpGet httpGet = null;
        List<String> linksDocumentos = new ArrayList<>();
        try {
            httpGet = new HttpGet("http://www.portaltransparencia.gov.br/despesasdiarias//resultado?pagina=" + pagina + "#paginacao");
            CloseableHttpResponse respostaDocs = httpClient.execute(httpGet, httpContext);
            String fontePagina = EntityUtils.toString(respostaDocs.getEntity());
            Document doc = Jsoup.parse(fontePagina);
            Elements elementosDaTabela = doc.getElementsByClass("tabela");

            for (Element elemento : elementosDaTabela) {
                Elements links = elemento.getElementsByTag("a");

                for (Element link : links) {
                    linksDocumentos.add(linkComum + link.attr("href"));
                }
            }
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        return linksDocumentos;
    }

    private String parsearDetalhesDocumento(HttpContainer httpContainer, String linkDocumento) throws Exception {
        CloseableHttpClient httpClient = httpContainer.getHttpClient();
        HttpContext httpContext = httpContainer.getHttpContext();
        HttpGet httpGet = null;

        String linha = "";
        try {
            httpGet = new HttpGet(linkDocumento);
            CloseableHttpResponse respostaDocs = httpClient.execute(httpGet, httpContext);
            String fontePagina = EntityUtils.toString(respostaDocs.getEntity());
            Document doc = Jsoup.parse(fontePagina);
            Element subtabela = doc.getElementsByClass("subtabela").first();
            Elements campos = subtabela.getElementsByTag("td");
            int i = 0;

            for (Element campo : campos) {
                if (i % 5 == 0) {
                    linha = linha.trim();
                    if (linha.endsWith(";")) {
                        linha = linha.substring(0, linha.length() - 1);
                    }
                    //linha += "\n\r";
                    if (i != 0) {
                        linha += "-###EOL###-";
                    }
                }
                if ((i + 5) % 5 == 1) {
                    Double campoDouble = Double.valueOf(campo.text());
                    campo.text(String.format("%.0f", campoDouble));
                } else if ((i + 5) % 5 == 2 || (i + 5) % 5 == 3) {
                    Double campoDouble = Double.valueOf(campo.text());
                    campo.text(String.format("%.2f", campoDouble));
                }
                linha += StringEscapeUtils.unescapeHtml4("\"" + campo.html() + "\"") + ";";
                i++;
            }
            if (linha.endsWith(";")) {
                linha = linha.substring(0, linha.length() - 1);
            }
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        return linha;
    }

    private static String buscarTituloSubitens(Elements titulos) {
        String linhaTitulo = "";
        int i = 0;

        for (Element titulo : titulos) {
            linhaTitulo += StringEscapeUtils.unescapeHtml4("\"" + titulo.html() + "\"") + ",";
        }
        linhaTitulo = linhaTitulo.substring(0, linhaTitulo.length() - 1);

        return linhaTitulo;
    }

    private void carregaCampos(Map<String, String> map) {
        periodoInicio = map.get("periodoInicio");
        periodoFim = map.get("periodoFim");
        codigoOS = (map.get("codigoOS") == null || map.get("codigoOS").equals("") ? "TOD" : map.get("codigoOS"));
        codigoOrgao = (map.get("codigoOrgao") == null || map.get("codigoOrgao").equals("") ? "TOD" : map.get("codigoOrgao"));
        codigoUG = (map.get("codigoUG") == null || map.get("codigoUG").equals("") ? "TOD" : map.get("codigoUG"));
        codigoED = map.get("codigoED");
        codigoFavorecido = (map.get("codigoFavorecido") == null ? "" : map.get("codigoFavorecido"));
        nomeArquivo = (map.get("nomeArquivo") == null || map.get("nomeArquivo").equals("") ? "novoArquivo" : map.get("nomeArquivo"));
    }

    private int processaData() throws ParseException {
        DateTime dataInicial = new DateTime(new Date(periodoInicio));
        DateTime dataFinal = new DateTime(new Date(periodoFim));
        DateTimeFormatter patternDate = DateTimeFormat.forPattern("dd/MM/yyyy");
        int dias = Days.daysBetween(dataInicial, dataFinal).getDays();

        if (dias > 30) {
            int diasMes = dias / 30;
            int diasMesResto = dias % 30;

            dataInicialMap.put(0, dataInicial.toString(patternDate));
            dataFinalMap.put(0, dataInicial.plusDays(30).toString(patternDate));

            for (int i = 1; i <= diasMes; i++) {

                if (i < diasMes) {
                    DateTime dataInicialItemMap = new DateTime(dataInicial.plusDays((30 * i) + 1));
                    String dataIcialFormatada = dataInicialItemMap.toString(patternDate);
                    dataInicialMap.put(i, dataIcialFormatada);

                    DateTime dataFinalItemMap = new DateTime(dataInicial.plusDays((30 * i) + 30));
                    String dataFinalFormatada = dataFinalItemMap.toString(patternDate);
                    dataFinalMap.put(i, dataFinalFormatada);

                } else if (diasMesResto > 0) {
                    DateTime dataInicialItemMap = new DateTime(dataInicial.plusDays((30 * i) + 1));
                    String dataIcialFormatada = dataInicialItemMap.toString(patternDate);
                    dataInicialMap.put(i, dataIcialFormatada);

                    DateTime dataFinalItemMap = new DateTime(dataInicial.plusDays((30 * i) + diasMesResto));
                    String dataFinalFormatada = dataFinalItemMap.toString(patternDate);
                    dataFinalMap.put(i, dataFinalFormatada);
                }
            }
        }
        return dias;
    }

    private void geraArquivo(String conteudoPagina, Integer qtdPeriodos) throws Exception {
        Integer ultimaPaginaResultado = encontrarUltimaPagina(conteudoPagina);
        System.out.println("Quantidade de páginas: " + encontrarUltimaPagina(conteudoPagina));
        List<String> listaLinksDocumentos;
        contadorPeriodos++;

        if (listaDespesasDocumento.isEmpty()) {
            listaDespesasDocumento.add("Subitem da Despesa;Quantidade;Valor Unitário (R$);Valor Total (R$);Descrição");
        }

        for (int i = 1; i <= ultimaPaginaResultado; i++) {
            listaLinksDocumentos = new ArrayList<>();
            System.out.println("---> " + i);
            listaLinksDocumentos.addAll(buscarDocumentosDaPagina(httpClient, httpContext, i));
            for (String linkDocumento : listaLinksDocumentos) {
                System.out.println("-----> " + linkDocumento);
                String linha = parsearDetalhesDocumento(httpContainer, linkDocumento);
                listaDespesasDocumento.addAll(Arrays.asList(linha.split("-###EOL###-")));
                Thread.sleep(2000);
            }

            if (contadorPeriodos == qtdPeriodos) {
                if (listaDespesasDocumento.size() == 1) {
                    JOptionPane.showMessageDialog(null, "Essa consulta não retornou nenhum resultado.", "Sem resultados", JOptionPane.INFORMATION_MESSAGE);
                } else if (listaDespesasDocumento.size() > 1 && i == ultimaPaginaResultado) {
                    for (String a : listaDespesasDocumento) {
                        System.out.println(a);
                    }
                    FileUtils.writeLines(new File(nomeArquivo + ".csv"), "ISO8859_1", listaDespesasDocumento, true);
                    JOptionPane.showMessageDialog(null, "Arquivo gerado com sucesso!", "Arquivo gerado", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
}
