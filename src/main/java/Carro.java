import java.util.Random;

public class Carro implements Runnable {
    private String nome;
    private double distanciaTotalCorrida;
    private double distanciaPercorrida = 0;
    private Corrida corrida;
    private String status = "Aguardando";
    private boolean fezPitStop = false;

    public Carro(String nome, double distanciaTotalCorrida, Corrida corrida) {
        this.nome = nome;
        this.distanciaTotalCorrida = distanciaTotalCorrida;
        this.corrida = corrida;
    }

    public String getNome() {
        return nome;
    }

    public double getProgresso() {
        synchronized (corrida) {
            return distanciaPercorrida / distanciaTotalCorrida;
        }
    }

    public String getStatus() {
        synchronized (corrida) {
            return status;
        }
    }

    @Override
    public void run() {
        Random random = new Random();
        try {
            while (distanciaPercorrida < distanciaTotalCorrida) {
                if (!fezPitStop && distanciaPercorrida >= distanciaTotalCorrida / 2) {
                    synchronized (corrida) {
                        status = "No pit stop";
                        fezPitStop = true;
                        System.out.println("[PIT STOP] " + nome + " parou por 1,5 segundo.");
                    }
                    Thread.sleep(1500);
                    System.out.println("[RETORNO] " + nome + " voltou para a corrida.");
                }

                synchronized (corrida) {
                    status = "Correndo";
                }
                Thread.sleep(random.nextInt(401) + 100);

                synchronized (corrida) {
                    double avanco = random.nextInt(41) + 10;
                    if (distanciaPercorrida + avanco > distanciaTotalCorrida) {
                        avanco = distanciaTotalCorrida - distanciaPercorrida;
                    }
                    distanciaPercorrida += avanco;
                    System.out.println(nome + " andou " + avanco
                            + " metros e ja percorreu " + distanciaPercorrida
                            + " de " + distanciaTotalCorrida + " metros.");

                    if (distanciaPercorrida >= distanciaTotalCorrida) {
                        status = "Chegou";
                        corrida.registrarChegada(nome);
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            synchronized (corrida) {
                status = "Interrompido";
            }
            System.out.println(nome + " teve a corrida interrompida.");
        }
    }
}
