package pe.com.apptama.apptamapedidos.presenter.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class PedidoModelHashTable {

    private static Hashtable<Integer, PedidoModel> myTable = new Hashtable<>();

    public static void agregaLinea(Integer key, PedidoModel pedidoModel) {
        myTable.put(key, pedidoModel);
    }

    public static void quitarLineas() {
        myTable.clear();
    }


    public static PedidoModel getPedidoModelAll() {

        PedidoModel pedidoModel = new PedidoModel();
        Enumeration<PedidoModel> elements = myTable.elements();
        while (elements.hasMoreElements()) {
            pedidoModel =(elements.nextElement());
        }

        return pedidoModel;
    }


}
