import java.util.ArrayList;
import java.util.List;

public class Corrida {
    public static final double DISTANCIA_TOTAL = 300;
    public static final int NUMERO_DE_CARROS = 3;
    private Podio podio = new Podio();
    private List<Carro> carros = new ArrayList<>();
    private List<Thread> threads = new ArrayList<>();

    public Corrida() {
        for (int i = 1; i <= NUMERO_DE_CARROS; i++) {
            Carro carro = new Carro("Carro " + i, DISTANCIA_TOTAL, this);
            carros.add(carro);
            threads.add(new Thread(carro));
        }
    }

    public Carro getCarro(int indice) {
        return carros.get(indice);
    }

    public String getPodio() {
        return podio.getResultado();
    }

    public synchronized void registrarChegada(String nome) {
        int posicao = podio.registrar(nome);
        System.out.println("[CHEGADA] O " + nome + " cruzou a linha de chegada!");
        System.out.println(posicao + "º lugar: " + nome);
        if (posicao == 1) {
            System.out.println("[VENCEDOR] " + nome + " venceu a corrida!");
        }
    }

    public void iniciar() {
        System.out.println("Largada! Distancia: " + DISTANCIA_TOTAL + " metros.");
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public boolean terminou() {
        for (Thread thread : threads) {
            if (thread.isAlive()) {
                return false;
            }
        }
        return true;
    }

    public void interromper() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    public void esperarTermino() throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
    }

    public static void main(String[] args) {
        Corrida corrida = new Corrida();
        corrida.iniciar();
        try {
            corrida.esperarTermino();
        } catch (InterruptedException e) {
            corrida.interromper();
            Thread.currentThread().interrupt();
            System.out.println("Corrida interrompida.");
            return;
        }
        System.out.println("\nPODIO\n" + corrida.getPodio());
        System.out.println("Todos os carros terminaram. Fim da corrida!");
    }
}
