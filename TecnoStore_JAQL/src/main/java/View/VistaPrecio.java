package View;

import Model.Precio;
import java.util.List;

public class VistaPrecio {
    public void listar(List<Precio> lista){
      lista.forEach(System.out::println);
    }
}
