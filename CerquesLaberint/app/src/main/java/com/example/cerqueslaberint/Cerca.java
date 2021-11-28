package com.example.cerqueslaberint;

import java.util.ArrayList;
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

    /**
     * Printea un NODO. Cabe decir que el valor "x" es la altura, empezando desde la celda mas alta de la izquierda
     * @param punto Punto a printear
     * @return String con los contenidos pertinentes del punto
     */
    public String print_punt(Punt punto) {
        return "Punto\tx:" + punto.x + "\ty:" + punto.y;
    }

    /**
     * Devuelve la interesccion entre el primer parámetro y el segundo parámetro
     * @param target
     * @param toAdd
     * @return La interesccion entre target y toAdd
     */
    public ArrayList<Punt> non_repeated(ArrayList<Punt> target, ArrayList<Punt> toAdd) {
        ArrayList<Punt> non_repeated = new ArrayList<>();
        for (int i = 0; i < toAdd.size(); i++) {
            boolean found = false;
            for (int j = 0; j < target.size(); j++) {
                if (toAdd.get(i).equals(target.get(j))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                non_repeated.add(toAdd.get(i));
            }
        }
        return non_repeated;
    }

    /**
     * Dado un punto, retorna los puntos a los que podemos ir siguiendo un orden predeterminado
     * @param node Punto de origen a expandir
     * @return Conjunto de todos los nodos a los que se puede ir
     */
    public ArrayList<Punt> expand_node(Punt node) {
        ArrayList<Punt> expansion = new ArrayList<>();
        int[] orden = {laberint.ESQUERRA, laberint.AMUNT, laberint.DRETA, laberint.AVALL}; // the order can be changed with this array
        for (int i = 0; i < orden.length; i++) {

            if (laberint.pucAnar(node.x, node.y, orden[i])) {
                switch (orden[i]) {
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
    }

    /**
     * Implementación del algoritmo de busqueda de ampladitud (BFS)
     * @param origen Punto inicial
     * @param desti Punto final
     * @return Camino a seguir para ir desde origen hasta destino
     */
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
                laberint.setNodes(closed.size()); // Set node count
                return camiTrobat;
            }
            open.addAll(non_repeated(open, expand_node(node)));
        }
        // System.err.println("No hay ningun camino!");
        return null; //Error
    }

    /**
     * Implementación del algoritmo de busqueda de profunidad (DFS)
     * @param origen Punto inicial
     * @param desti Punto final
     * @return Camino a seguir para ir desde origen hasta destino
     */
    public Cami CercaEnProfunditat(Punt origen, Punt desti) {
        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);
        ArrayList<Punt> closed = new ArrayList<>();
        ArrayList<Punt> non_repeated = new ArrayList<>();
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

                laberint.setNodes(closed.size()); // Set node count
                return camiTrobat;
            }
            non_repeated = non_repeated(closed, expand_node(node));

            // Swap order to get prefered order upon pop
            for (int i = non_repeated.size()-1; i >= 0; i-- ){
                open.push(non_repeated.get(i));
            }
        }
        // System.err.println("No hay ningun camino!");
        return null; // Error
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
