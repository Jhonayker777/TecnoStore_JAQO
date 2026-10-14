package Control;

import Dao.CelularDao;
import Dao.ClientesDao;
import Model.Carrito;
import Model.Cliente;
import View.VistaCelulares;
import View.VistaClientes;
import View.VistaTienda;

public class TiendaControl {

    private Carrito carrito;
    private Cliente cliente;

    ClientesDao clienteDao = new ClientesDao();
    CelularDao celularDao = new CelularDao();

    VistaTienda tiendaVista;
    VistaClientes clienteVista = new VistaClientes();
    VistaCelulares celularVista = new VistaCelulares();

    CelularesControl celular = new CelularesControl();
    CarritoControl carritoControl = new CarritoControl(cliente, carrito);

    public TiendaControl(Cliente cliente, Carrito carrito) {
        this.cliente = cliente;
        this.carrito = carrito;
        this.tiendaVista = new VistaTienda(carrito);
    }

    public void menu() {
        int op;
        do {
            op = tiendaVista.principal();
            switch (op) {
                case 1 -> {
                    catalogo();
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
                    carrito();
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

    protected void catalogo() {
        tiendaVista.catalogo(celularDao.listar());
    }

    protected void carrito() {
        tiendaVista.verCarrito();
        carritoControl.menuCarrito();
    }
}
