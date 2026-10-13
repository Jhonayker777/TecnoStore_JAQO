package View;

import Model.SistemasOperativos;
import java.util.List;

public class VistaSistemaOperativo {
    
    public void listar(List<SistemasOperativos> lista) {
        lista.forEach(System.out::println);
    }
}
