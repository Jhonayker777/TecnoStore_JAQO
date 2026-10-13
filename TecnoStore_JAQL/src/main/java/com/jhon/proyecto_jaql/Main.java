package com.jhon.proyecto_jaql;

import Control.CelularesControl;
import Control.ClienteControl;
import Control.TiendaControl;
import Control.LoginControl;
import Dao.CelularDao;
import Dao.ClientesDao;
import Dao.PreciosDao;
import Model.Carrito;
import Model.CategoriaGama;
import Model.Celular;
import Model.Marca;
import Model.Precio;
import Model.SistemasOperativos;

public class Main {

    public static void main(String[] args) {
//        LoginControl l = new LoginControl();
//        l.menu();
        ClientesDao cd = new ClientesDao();
        TiendaControl tc = new TiendaControl(cd.buscarId(1),new Carrito());
        tc.menu();

    }

}
