package View;

import Model.Marca;
import java.util.List;

public class VistaMarca {
    public void listar(List<Marca> lista){
      lista.forEach(System.out::println);
    }
}
