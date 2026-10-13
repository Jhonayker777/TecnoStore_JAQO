package com.jhon.proyecto_jaql;

import Control.CelularesControl;
import Control.ClienteControl;
import Control.TiendaControl;
import Control.LoginControl;
import Dao.CelularDao;
import Dao.ClientesDao;
import Dao.PreciosDao;
import Model.CategoriaGama;
import Model.Celular;
import Model.Marca;
import Model.Precio;
import Model.SistemasOperativos;

public class Main {

    public static void main(String[] args) {
//        ClientesDao cliente = new ClientesDao();
//        LoginControl l = new LoginControl();
//        l.menu();

        ClienteControl cc = new ClienteControl();

        cc.administrar();
//        PreciosDao pd = new PreciosDao();

//        pd.eliminar(2);
//        pd.agregar(new Precio(2, "Por que si", 55555));
    }

}
