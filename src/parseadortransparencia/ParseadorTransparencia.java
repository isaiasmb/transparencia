/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parseadortransparencia;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author bruno
 */
public class ParseadorTransparencia {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        
        BasicCookieStore cookieStore = new BasicCookieStore();
        HttpContext httpContext = new BasicHttpContext();
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();

        String conteudoPagina = realizarFiltro(httpClient, httpContext);
        Integer ultimaPaginaResultado = encontrarUltimaPagina(conteudoPagina);
        /*
        List<String> listaLinksDocumentos = new ArrayList<>();
        for (int i=1; i <= ultimaPaginaResultado; i++) {
            System.out.println("---> " + i);
            listaLinksDocumentos = buscarDocumentosDaPagina(httpClient, httpContext, i);
            
            for (String linkDocumento : listaLinksDocumentos) {
               parsearDetalhesDocumento(httpClient, httpContext, linkDocumento);  
            }
            //parsearDetalhesDocumento(httpClient, httpContext, "http://www.portaltransparencia.gov.br/despesasdiarias/empenho?documento=153045152242016NE000681");
        */
        
        
        String linha = parsearDetalhesDocumento(httpClient, httpContext, "http://www.portaltransparencia.gov.br/despesasdiarias/empenho?documento=153045152242016NE000681");
        System.out.println(linha);
        
    }
    
    public static String realizarFiltro(CloseableHttpClient httpClient, HttpContext httpContext) throws Exception {
        String urlNumPaginas = "http://www.portaltransparencia.gov.br/despesasdiarias/resultado";
        String fontePagina = "";
        HttpGet httpGet = null;
        
        try {
            
            URIBuilder builder = new URIBuilder();
            builder.setScheme("http").setHost("www.portaltransparencia.gov.br").setPath("/despesasdiarias/resultado")
                .setParameter("consulta", "avancada")
                .setParameter("periodoInicio", "01/07/2016")
                .setParameter("periodoFim", "30/07/2016")
                .setParameter("fase", "EMP")
                .setParameter("codigoOS", "52000")
                .setParameter("codigoOrgao", "52121")
                .setParameter("codigoUG", "160450")
                .setParameter("codigoED", "TOD")
                .setParameter("codigoFavorecido", "");
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
    
    public static Integer encontrarUltimaPagina(String fontePagina) throws Exception {
        Document doc =  Jsoup.parse(fontePagina);

        Element content = doc.getElementsByClass("ultimaPagina").first();
        Element link = content.getElementsByTag("a").first();
        
        String linkUltimaPg = link.attr("href");
        int posInicial = linkUltimaPg.indexOf("=");
        int posFinal = linkUltimaPg.indexOf("#");
        
        String numMaxPaginaStr = linkUltimaPg.substring(posInicial + 1, posFinal);
        Integer numMaxPagina = 0;
        try {
            numMaxPagina = Integer.parseInt(numMaxPaginaStr);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        
        return numMaxPagina;
    }
    
   
    
  public static List<String> buscarDocumentosDaPagina(CloseableHttpClient httpClient, HttpContext httpContext, Integer pagina) throws Exception {
        
        String linkComum = "http://www.portaltransparencia.gov.br/despesasdiarias/";
        HttpGet httpGet = null;
        List<String> linksDocumentos = new ArrayList<>();
        try {
            httpGet = new HttpGet("http://www.portaltransparencia.gov.br/despesasdiarias//resultado?pagina=" + pagina + "#paginacao");
            CloseableHttpResponse respostaDocs = httpClient.execute(httpGet, httpContext);
            String fontePagina = EntityUtils.toString(respostaDocs.getEntity());
            
            
            Document doc =  Jsoup.parse(fontePagina);

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
  
  
  public static String parsearDetalhesDocumento(CloseableHttpClient httpClient, HttpContext httpContext, String linkDocumento) throws Exception {
      
    HttpGet httpGet = null;
    String linha = "";
    try {
        httpGet = new HttpGet(linkDocumento);
        CloseableHttpResponse respostaDocs = httpClient.execute(httpGet, httpContext);
        String fontePagina = EntityUtils.toString(respostaDocs.getEntity());

        Document doc =  Jsoup.parse(fontePagina);
  
        Element subtabela = doc.getElementsByClass("subtabela").first();
        
        //Elements titulos = subtabela.getElementsByTag("th");
        //linha = buscarTituloSubitens(titulos);
        
        Elements campos = subtabela.getElementsByTag("td");
        
        
        int i=0;
        for (Element campo : campos) {
            if (i % 5 == 0) {
                linha = linha.trim();
                if (linha.endsWith(",")) {
                    linha = linha.substring(0, linha.length() -1);
                }
                linha += "\n\r";
            }
            linha += StringEscapeUtils.unescapeHtml4("\"" + campo.html() + "\"") + ",";
            i++;
        }
        if (linha.endsWith(",")) {
            linha = linha.substring(0, linha.length() -1);
        }  
    } finally {
        if (httpGet != null) {
            httpGet.releaseConnection();
        }
    }
      
    return linha;
  }
  
  public static String buscarTituloSubitens(Elements titulos) {
    String linhaTitulo = "";
      
    int i=0;
    for (Element titulo : titulos) {
        linhaTitulo += StringEscapeUtils.unescapeHtml4("\"" + titulo.html() + "\"") + ",";
    }
    linhaTitulo = linhaTitulo.substring(0, linhaTitulo.length() -1);

    return linhaTitulo;
  }
}
