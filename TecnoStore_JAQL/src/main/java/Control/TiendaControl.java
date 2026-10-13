package Control;

import Dao.CelularDao;
import Dao.ClientesDao;
import Model.Cliente;
import View.VistaCelulares;
import View.VistaClientes;
import View.VistaTienda;

public class TiendaControl {

    ClientesDao clienteDao = new ClientesDao();
    CelularDao celularDao = new CelularDao();

    VistaTienda tiendaVista = new VistaTienda();
    VistaClientes clienteVista = new VistaClientes();
    VistaCelulares celularVista = new VistaCelulares();

    CelularesControl celular = new CelularesControl();
    private Cliente cliente;

    public TiendaControl(Cliente cliente) {
        this.cliente = cliente;
    }

    public void menu() {
        int op;
        do {
            op = tiendaVista.principal();
            switch (op) {
                case 1 -> {
                    celular.catalogo();
                }
                case 2 -> {
                    buscarCelular();
                }
                case 3 -> {
                    filtrarMarca();
                }
                case 4 -> {
                    filtrarGama();
                }
                case 5 -> {
                    System.out.println("Mi carrito");
                }
                case 6 -> {
                    System.out.println("Mis compras");
                }
                case 7 -> {
                    System.out.println(cliente);
                }
                case 8 -> {
                    cambiarContraseña();
                }
                case 0 -> {
                }
                default -> {
                    System.out.println("Opcion no encontrada!");
                }
            }
        } while (op != 0);
    }

    protected void cambiarContraseña() {
        String nueva = clienteVista.contraseñaValida(cliente.getContraseña());
        if (nueva != null) {
            clienteDao.Contraseña(cliente.getId(), nueva);
        } else {
            System.out.println("Su contraseña es incorrecta");
        }
    }

    protected void buscarCelular() {
        celularVista.FiltrarNombre(celularDao.listar());
    }

    protected void filtrarGama() {
        celularVista.filtrarGama(celularDao.listar());
    }

    protected void filtrarMarca() {
        celularVista.filtrarMarca(celularDao.listar());
    }
}
