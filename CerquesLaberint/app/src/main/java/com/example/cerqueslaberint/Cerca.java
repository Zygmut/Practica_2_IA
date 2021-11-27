package com.example.cerqueslaberint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

/**
 * Created by Ramon Mas on 10/10/21.
 * Classe que conté els diferents algorismes de cerca que s'han d'implementar
 */

/**
 * AUTORS:__________________________________________
 */
/* S'ha d'omplenar la següent taula amb els diferents valors del nodes visitats i la llargada del camí
 * per les diferents grandàries de laberints proposades i comentar breument els resultats obtinguts.
 ****************************************************************************************************************
 *                  Profunditat           Amplada          Manhattan         Euclidiana         Viatjant        *
 *  Laberint     Nodes   Llargada    Nodes   Llargada   Nodes   Llargada   Nodes   Llargada  Nodes   Llargada   *
 * **************************************************************************************************************
 *    Petit
 *    Mitjà
 *    Gran
 *
 * Comentari sobre els resultats obtinguts:
 *
 *
 *
 *
 *
 *
 */

public class Cerca {
    static final public int MANHATTAN = 2;
    static final public int EUCLIDEA = 3;

    Laberint laberint;      // laberint on es cerca
    int files, columnes;    // files i columnes del laberint

    public Cerca(Laberint l) {
        files = l.nFiles;
        columnes = l.nColumnes;
        laberint = l;
    }

    /*
     * Printea un NODO
     * !! LA X ES LA ALTURA !!
     */
    public String print_punt(Punt punto) {
        return "Punto\tx:" + punto.x + "\ty:" + punto.y;
    }

    /*
     * Añade al primer arraylist todos los elementos del segundo, evitando nodos repetidos
     */
    public void add_non_repeated(ArrayList<Punt> target, ArrayList<Punt> toAdd) {
        for (int i = 0; i < toAdd.size(); i++) {
            boolean found = false;
            for (int j = 0; j < target.size(); j++) {
                //System.out.println(print_punt(toAdd.get(i)) + "\t" + print_punt(target.get(j)));
                if (toAdd.get(i).equals(target.get(j))) {
                    //System.out.println("Found equal!");
                    found = true;
                    break;
                }
            }
            if (!found) {
                target.add(toAdd.get(i));
            }

        }
    }

    /*
     * Añade al Stack todos los elementos del ArrayList, evitando nodos repetidos
     */
    public void add_non_repeated(Stack<Punt> target, ArrayList<Punt> toAdd) {
        ArrayList<Punt> arr_target = new ArrayList<>();
        ArrayList<Punt> swap_order = new ArrayList<>();
        arr_target.addAll(target);

        for (int i = 0; i < toAdd.size(); i++) {
            boolean found = false;
            for (int j = 0; j < arr_target.size(); j++) {
                //System.out.println(print_punt(toAdd.get(i)) + "\t" + print_punt(target.get(j)));
                if (toAdd.get(i).equals(arr_target.get(j))) {
                    //System.out.println("Found equal!");
                    found = true;
                    break;
                }
            }
            if (!found) {
                swap_order.add(toAdd.get(i));
            }
        }

        // Swap order to get prefered order upon pop
        for (int i = swap_order.size()-1; i >= 0; i-- ){
            target.push(swap_order.get(i));
        }
    }

    /*
     * Dado un nodo, retorna la cantidad de nodos a los que podemos ir siguiendo un orden
     */
    public ArrayList<Punt> expand_node(Punt node) {
        ArrayList<Punt> expansion = new ArrayList<>();
        int[] orden = {laberint.ESQUERRA, laberint.AMUNT, laberint.DRETA, laberint.AVALL};
        for (int i = 0; i < orden.length; i++) {

            if (laberint.pucAnar(node.x, node.y, orden[i])) {
                switch (orden[i]) {
                    // No se que es este "val" pero nunca se usa
                    case Laberint.ESQUERRA:
                        //System.out.println("Puedo ir a la izquierda");
                        expansion.add(new Punt(node.x, node.y - 1, node, 0));
                        break;
                    case Laberint.AMUNT:
                        //System.out.println("Puedo ir a la arriba");
                        expansion.add(new Punt(node.x - 1, node.y, node, 0));
                        break;
                    case Laberint.DRETA:
                        //System.out.println("Puedo ir a la derecha");
                        expansion.add(new Punt(node.x, node.y + 1, node, 0));
                        break;
                    case Laberint.AVALL:
                        //System.out.println("Puedo ir a la abajo");
                        expansion.add(new Punt(node.x + 1, node.y, node, 0));
                        break;
                    default:
                        return null;
                }
            }
        }
        return expansion;
        //return expansion.toArray(new Punt[expansion.size()]);

    }

    public Cami CercaEnAmplada(Punt origen, Punt desti) {
        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);
        ArrayList<Punt> closed = new ArrayList<>();
        ArrayList<Punt> open = new ArrayList<>();

        open.add(origen);

        while (open.size() != 0) {
            Punt node = open.get(0);
            open.remove(0);
            closed.add(node);
            if (node.equals(desti)) {

                camiTrobat.afegeix(node); // Final node
                while (node.previ != null) { // All other nodes
                    node = node.previ;
                    camiTrobat.afegeix(node);
                }
                camiTrobat.afegeix(node); // First node


                return camiTrobat;
            }
            add_non_repeated(open, expand_node(node));
            // open.addAll(expand_node(node));
        }
        return null;
    }

    public Cami CercaEnProfunditat(Punt origen, Punt desti) {
        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);
        ArrayList<Punt> closed = new ArrayList<>();
        Stack<Punt> open = new Stack<>();

        open.push(origen);

        while (!open.empty()) {
            Punt node = open.pop();
            closed.add(node);
            if (node.equals(desti)) {

                camiTrobat.afegeix(node); // Final node
                while (node.previ != null) { // All other nodes
                    node = node.previ;
                    camiTrobat.afegeix(node);
                }
                camiTrobat.afegeix(node); // First node


                return camiTrobat;
            }
            add_non_repeated(open, expand_node(node));
            // open.addAll(expand_node(node));
        }
        return null;
    }

    public Cami CercaAmbHeurística(Punt origen, Punt desti, int tipus) {   // Tipus pot ser MANHATTAN o EUCLIDIA
        int i;
        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);

        // Implementa l'algoritme aquí
        camiTrobat.afegeix(desti);

        return camiTrobat;
    }


    public Cami CercaViatjant(Punt origen, Punt desti) {
        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);

        // Implementa l'algoritme aquí
        camiTrobat.afegeix(desti);

        return camiTrobat;
    }
}
