package pe.com.apptama.apptamapedidos.presenter.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class CarritoModelHashTable {

    private static Hashtable<Integer, List<CarritoModel>> myTable = new Hashtable<>();

    public static void agregaLinea(Integer key, List<CarritoModel> carritoModels) {
        myTable.put(key, carritoModels);
    }

    public static void quitarLineas() {
        myTable.clear();
    }

    public static List<CarritoModel> getCarritoModelAll() {

        List<CarritoModel>  carritoModels = new ArrayList<>();
        Enumeration<List<CarritoModel>> elements = myTable.elements();
        while (elements.hasMoreElements()) {
            carritoModels.addAll(elements.nextElement());
        }

        return carritoModels;
    }

}
