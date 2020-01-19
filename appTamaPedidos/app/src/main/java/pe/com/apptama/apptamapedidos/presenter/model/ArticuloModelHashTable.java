package pe.com.apptama.apptamapedidos.presenter.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class ArticuloModelHashTable {

    private static Hashtable<Integer, List<ArticuloModel>> myTable = new Hashtable<>();

    public static void agregaLinea(Integer key, List<ArticuloModel> articuloModels) {
        myTable.put(key, articuloModels);
    }

    public static void quitarLineas() {
        myTable.clear();
    }

    public static Hashtable<Integer, List<ArticuloModel>> getLineas() {
        return myTable;
    }

    public static List<ArticuloModel> getArticuloModelAll() {

        List<ArticuloModel> articuloModels = new ArrayList<>();
        Enumeration<List<ArticuloModel>> elements = myTable.elements();
        while (elements.hasMoreElements()) {
            articuloModels.addAll(elements.nextElement());
        }

        return articuloModels;
    }


}
