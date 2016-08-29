package telas;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import parseadortransparencia.ParseadorTransparencia;
import utils.DadosPesquisa;

/**
 *
 * @author isaias
 */
public class JanelaApp extends Application {
    private final DadosPesquisa dadosPesquisa = new DadosPesquisa();
    private final Map<String, String> elementosDespesaMap = dadosPesquisa.getElementoDespesaMap();
    private AnchorPane pane;
    private Label periodoLabel;
    private DatePicker periodoInicio;
    private DatePicker periodoFim;
    private Label orgaoSuperiorLabel;
    private TextField orgaoSuperior;
    private Label orgaoEntidadeVinculadaLabel;
    private TextField orgaoEntidadeVinculada;
    private Label unidadeGestoraLabel;
    private TextField unidadeGestora;
    private Label elementoDespesaLabel;
    private ComboBox<String> elementoDespesaCombo;
    private Label favorecidoLabel;
    private TextField favorecido;
    private Label nomeArquivoLabel;
    private TextField nomeArquivo;
    private Button consultar;
    private Button limparCampos;

    @Override
    public void start(Stage stage) throws Exception {
        initComponents();

        Scene scene = new Scene(pane);
        stage.setScene(scene);

        // Remove a opção de maximizar a tela
        stage.setResizable(false);
        // Dá um título para a tela
        stage.setTitle("Consulta - Portal transparência");
        stage.show();
        initLayout();  
        validaCampos();
    }

    private void initComponents() {
        pane = new AnchorPane();
        pane.setPrefSize(650, 400);

        periodoLabel = new Label("Período:");
        periodoInicio = new DatePicker();
        periodoInicio.setPromptText("Inicio");
        periodoFim = new DatePicker();
        periodoFim.setPromptText("Fim");

        orgaoSuperiorLabel = new Label("Órgão Superior:");
        orgaoSuperior = new TextField();

        orgaoEntidadeVinculadaLabel = new Label("Órgão / Entidade Vinculada:");
        orgaoEntidadeVinculada = new TextField();

        unidadeGestoraLabel = new Label("Unidade Gestora:");
        unidadeGestora = new TextField();

        elementoDespesaLabel = new Label("Elemento de Despesa:");
        elementoDespesaCombo = new ComboBox();
        elementoDespesaCombo.getItems().addAll(elementosDespesaMap.values());
        elementoDespesaCombo.getSelectionModel().select(elementosDespesaMap.get("TOD"));

        favorecidoLabel = new Label("Favorecido:");
        favorecido = new TextField();
        
        nomeArquivoLabel = new Label("Nome do arquivo: ");
        nomeArquivo = new TextField();
        nomeArquivo.setPromptText("Opcional");

        consultar = new Button("Consultar");
        consultar.setOnAction(e -> consultar());

        limparCampos = new Button("Limpar Campos");
        limparCampos.setOnAction(e -> limparCampos());

        pane.getChildren().addAll(periodoLabel, periodoInicio, periodoFim, orgaoSuperiorLabel, orgaoSuperior,
                orgaoEntidadeVinculadaLabel, orgaoEntidadeVinculada, unidadeGestora, unidadeGestoraLabel,
                elementoDespesaCombo, elementoDespesaLabel, favorecido, favorecidoLabel, nomeArquivoLabel, nomeArquivo, 
                consultar, limparCampos);
    }

    private void initLayout() {
        periodoLabel.setLayoutX(15);
        periodoLabel.setLayoutY(55);
        periodoInicio.setLayoutX(200);
        periodoInicio.setLayoutY(50);
        periodoFim.setLayoutX(periodoInicio.getLayoutX() + periodoInicio.getWidth() + 30);
        periodoFim.setLayoutY(50);

        orgaoSuperiorLabel.setLayoutX(periodoLabel.getLayoutX());
        orgaoSuperiorLabel.setLayoutY(periodoLabel.getLayoutY() + 40);
        orgaoSuperior.setLayoutX(periodoInicio.getLayoutX());
        orgaoSuperior.setLayoutY(periodoFim.getLayoutY() + 40);

        orgaoEntidadeVinculadaLabel.setLayoutX(orgaoSuperiorLabel.getLayoutX());
        orgaoEntidadeVinculadaLabel.setLayoutY(orgaoSuperiorLabel.getLayoutY() + 40);
        orgaoEntidadeVinculada.setLayoutX(orgaoSuperior.getLayoutX());
        orgaoEntidadeVinculada.setLayoutY(orgaoSuperior.getLayoutY() + 40);

        unidadeGestoraLabel.setLayoutX(orgaoEntidadeVinculadaLabel.getLayoutX());
        unidadeGestoraLabel.setLayoutY(orgaoEntidadeVinculadaLabel.getLayoutY() + 40);
        unidadeGestora.setLayoutX(orgaoEntidadeVinculada.getLayoutX());
        unidadeGestora.setLayoutY(orgaoEntidadeVinculada.getLayoutY() + 40);

        elementoDespesaLabel.setLayoutX(unidadeGestoraLabel.getLayoutX());
        elementoDespesaLabel.setLayoutY(unidadeGestoraLabel.getLayoutY() + 40);
        elementoDespesaCombo.setLayoutX(unidadeGestora.getLayoutX());
        elementoDespesaCombo.setLayoutY(unidadeGestora.getLayoutY() + 40);

        favorecidoLabel.setLayoutX(elementoDespesaLabel.getLayoutX());
        favorecidoLabel.setLayoutY(elementoDespesaLabel.getLayoutY() + 40);
        favorecido.setLayoutX(elementoDespesaCombo.getLayoutX());
        favorecido.setLayoutY(elementoDespesaCombo.getLayoutY() + 40);
        
        nomeArquivoLabel.setLayoutX(favorecidoLabel.getLayoutX());
        nomeArquivoLabel.setLayoutY(favorecidoLabel.getLayoutY() + 40);
        nomeArquivo.setLayoutX(favorecido.getLayoutX());
        nomeArquivo.setLayoutY(favorecido.getLayoutY() + 40);

        consultar.setLayoutX(nomeArquivo.getLayoutX());
        consultar.setLayoutY(nomeArquivo.getLayoutY() + 40);

        limparCampos.setLayoutX(consultar.getLayoutX() + consultar.getWidth() + 15);
        limparCampos.setLayoutY(consultar.getLayoutY());
    }

    private void consultar() {
        String patternDate = "MM/dd/yyyy";
        try {
            Map<String, String> map = new HashMap<>();
            map.put("periodoInicio", periodoInicio.getValue().format(DateTimeFormatter.ofPattern(patternDate)));
            map.put("periodoFim", periodoFim.getValue().format(DateTimeFormatter.ofPattern(patternDate)));
            map.put("codigoOS", orgaoSuperior.getText());
            map.put("codigoOrgao", orgaoEntidadeVinculada.getText());
            map.put("codigoUG", unidadeGestora.getText());
            map.put("codigoED", getValue(elementosDespesaMap, elementoDespesaCombo));
            map.put("codigoFavorecido", favorecido.getText());
            map.put("nomeArquivo", nomeArquivo.getText());
            System.out.println("Nome do favorecido antes de enviar: " + favorecido.getText());
            System.out.println("Nome do arquivo antes de enviar: " + nomeArquivo.getText());
           
            ParseadorTransparencia.execute(map);
        } catch (Exception ex) {
            Logger.getLogger(JanelaApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getValue(Map map, ComboBox<String> combo) {
        Iterator iterator = map.entrySet().iterator();
        String result = null;
        while (iterator.hasNext()) {
            Map.Entry pairs = (Map.Entry) iterator.next();
            if (((String) pairs.getValue()).equalsIgnoreCase(combo.getValue())) {
                result = (String) pairs.getKey();
            }
        }
        return result;
    }

    private void limparCampos() {
        periodoInicio.getEditor().clear();
        periodoFim.getEditor().clear();
        orgaoSuperior.clear();
        orgaoEntidadeVinculada.clear();
        unidadeGestora.clear();
        elementoDespesaCombo.getSelectionModel().select(elementosDespesaMap.get("TOD"));
        favorecido.clear();
    }
    
    private void validaCampos() {
//        if(orgaoSuperior.getText() == null || orgaoSuperior.getText().trim().equals("")){
//            orgaoEntidadeVinculada.setEditable(false);
//        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
