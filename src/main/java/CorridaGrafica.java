import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CorridaGrafica {
    public static void main(String[] args) {
        Application.launch(Tela.class, args);
    }

    public static class Tela extends Application {
        private Corrida corrida;
        private ProgressBar[] barras = new ProgressBar[Corrida.NUMERO_DE_CARROS];
        private Label[] estados = new Label[Corrida.NUMERO_DE_CARROS];
        private Label resultado = new Label("Clique em Iniciar corrida.");
        private Button iniciar = new Button("Iniciar corrida");
        private Timeline atualizacao;

        @Override
        public void start(Stage janela) {
            VBox painel = new VBox(12);
            painel.setPadding(new Insets(24));
            painel.getChildren().add(new Label("Corrida de carros - 300 metros"));

            for (int i = 0; i < barras.length; i++) {
                estados[i] = new Label("Carro " + (i + 1) + " - Aguardando");
                barras[i] = new ProgressBar(0);
                barras[i].setMaxWidth(Double.MAX_VALUE);
                painel.getChildren().addAll(estados[i], barras[i]);
            }

            painel.getChildren().addAll(iniciar, resultado);
            atualizacao = new Timeline(new KeyFrame(Duration.millis(100), evento -> atualizarTela()));
            atualizacao.setCycleCount(Timeline.INDEFINITE);
            iniciar.setOnAction(evento -> iniciarCorrida());
            janela.setTitle("Simulador de corrida");
            janela.setScene(new Scene(painel, 520, 440));
            janela.show();
        }

        private void iniciarCorrida() {
            corrida = new Corrida();
            for (int i = 0; i < barras.length; i++) {
                barras[i].setProgress(0);
                estados[i].setText("Carro " + (i + 1) + " - Correndo");
            }
            resultado.setText("Corrida em andamento...");
            iniciar.setDisable(true);
            corrida.iniciar();
            atualizacao.playFromStart();
        }

        private void atualizarTela() {
            boolean terminou = corrida.terminou();
            for (int i = 0; i < barras.length; i++) {
                Carro carro = corrida.getCarro(i);
                barras[i].setProgress(carro.getProgresso());
                estados[i].setText(carro.getNome() + " - " + carro.getStatus());
            }
            String podio = corrida.getPodio();
            if (!podio.isEmpty()) {
                resultado.setText("Podio\n" + podio);
            }
            if (terminou) {
                atualizacao.stop();
                iniciar.setText("Correr novamente");
                iniciar.setDisable(false);
            }
        }

        @Override
        public void stop() {
            if (atualizacao != null) {
                atualizacao.stop();
            }
            if (corrida != null) {
                corrida.interromper();
            }
        }

    }
}
