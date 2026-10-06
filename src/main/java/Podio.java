import java.util.ArrayList;
import java.util.List;

public class Podio {
    private List<String> vencedores = new ArrayList<>();

    public int registrar(String nome) {
        synchronized (vencedores) {
            if (!vencedores.contains(nome)) {
                vencedores.add(nome);
            }
            return vencedores.indexOf(nome) + 1;
        }
    }

    public String getResultado() {
        synchronized (vencedores) {
            String resultado = "";
            for (int i = 0; i < vencedores.size() && i < 3; i++) {
                resultado += (i + 1) + "º lugar: " + vencedores.get(i) + "\n";
            }
            return resultado;
        }
    }
}
