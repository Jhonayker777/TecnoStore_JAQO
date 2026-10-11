package com.jhon.proyecto_jaql;

import Control.LoginControl;
import Dao.CelularDao;
import Dao.PreciosDao;
import Model.entidad.CategoriaGama;
import Model.entidad.Celular;
import Model.entidad.Marca;
import Model.entidad.Precio;
import Model.entidad.SistemasOperativos;

public class Main {

    public static void main(String[] args) {
//        LoginControl l = new LoginControl();
//        l.menu();

//        CelularDao cd = new CelularDao();
//
//        Celular c = new Celular.Builder()
//                .marca(new Marca(1, "MOTOROLA"))
//                .modelo("Moto g85 5g")
//                .precio(new Precio(1, "Por mayor", 500000))
//                .sistemaOperativo(new SistemasOperativos(1, "Android 16"))
//                .gama(CategoriaGama.MEDIA)
//                .stock(20)
//                .stock_minimo(5)
//                .build();
//        
//        cd.agregar(c);
        PreciosDao pd = new PreciosDao();
        pd.eliminar(2);
//        pd.agregar(new Precio(2, "Por que si", 55555));

    }

}
