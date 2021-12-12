package com.example.cerqueslaberint;

import android.content.pm.LauncherApps;

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
     *
     * @param punto Punto a printear
     * @return String con los contenidos pertinentes del punto
     */
    private String print_punt(Punt punto) {
        return "Punto\tx:" + punto.x + "\ty:" + punto.y;
    }

    /**
     * Invierte el Cami pasado por parámetro. Princio -> Final & Final -> Principio
     *
     * @param input Camino a invertir
     * @return Camino invertido
     */
    private Cami invert_path(Cami input) {
        Cami inverted_path = new Cami(files * columnes);
        for (int i = input.longitud - 1; i >= 0; i--) {
            inverted_path.afegeix(input.cami[i]);
        }
        return inverted_path;
    }

    /**
     * Concatena el primer camino con el segundo. Tiene en cuenta que el nodo final y el inicial de los caminos serán iguales
     *
     * @param target Camino objetivo
     * @param toAdd  Camino a concantenar
     * @return Camino concatenado
     */
    private Cami concat_paths(Cami target, Cami toAdd) {
        Cami return_path = new Cami(files * columnes);

        for (int i = 0; i < target.longitud; i++) {
            return_path.afegeix(target.cami[i]);
        }
        if (target.longitud == 0) { // first iteration
            return_path.afegeix(toAdd.cami[toAdd.longitud - 1]);
        }

        for (int i = toAdd.longitud - 2; i >= 0; i--) { // Skip first node desti = origen
            return_path.afegeix(toAdd.cami[i]);
        }
        return return_path;
    }

    /**
     * Devuelve un conjunto de datos del segundo parametro que no estan en el primer parametro
     *
     * @param target Primer conjunto
     * @param toAdd  Segundo conjunto
     * @return Conjunto de datos del segundo parametro que no estan en el primer parametro
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
     * Devuelve la interesccion entre el primer parámetro y el segundo parámetro
     *
     * @param target
     * @param toAdd
     * @return La interesccion entre target y toAdd
     */
    public ArrayList<Punt> non_repeated_heuristica(ArrayList<Punt> target, ArrayList<Punt> toAdd, int tipus, Punt desti) {
        ArrayList<Punt> non_repeated = new ArrayList<>();
        int j;
        for (int i = 0; i < toAdd.size(); i++) {
            boolean found = false;
            for (j = 0; j < target.size(); j++) {
                if (toAdd.get(i).equals(target.get(j))) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                Punt aux = toAdd.get(i);
                aux.distanciaDeLinici++;
                calculaF(aux, desti, tipus);
                non_repeated.add(aux);
            } else if (found) {
                Punt auxTarget = target.get(j);
                Punt auxToAdd = toAdd.get(i);
                auxToAdd.distanciaDeLinici++;

                if (calculaF(auxTarget, desti, tipus) > calculaF(auxToAdd, desti, tipus)) {
                    target.remove(j);
                    target.add(auxToAdd);
                }
            }
        }
        return non_repeated;
    }

    /**
     * Dado un punto, retorna los puntos a los que podemos ir siguiendo un orden predeterminado
     *
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
                        expansion.add(new Punt(node.x, node.y - 1, node, 0));
                        break;
                    case Laberint.AMUNT:
                        expansion.add(new Punt(node.x - 1, node.y, node, 0));
                        break;
                    case Laberint.DRETA:
                        expansion.add(new Punt(node.x, node.y + 1, node, 0));
                        break;
                    case Laberint.AVALL:
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
     * Método que retorna el Menor punto de un arrayList
     *
     * @param open  ArrayList a inspeccionar para encontra el menor punto
     * @param desti Desti del laberinto
     * @param tipus Manhattan / euclidea
     * @return Min punt en términos de f(n) = g(n) + h(n)
     */
    private Punt getLeast(ArrayList open, Punt desti, int tipus) {
        Punt min = (Punt) open.get(0);

        // loop to find minimum from ArrayList
        for (int i = 1; i < open.size(); i++) {
            if (calculaF((Punt) open.get(i), desti, tipus) < calculaF(min, desti, tipus)) {
                min = (Punt) open.get(i);
            }
        }
        return min;
    }

    /**
     * Cálcula f(n) t.q f(n) = g(n) + h(n)
     *
     * @param origen Punto de origen
     * @param desti  Punto de destino
     * @param tipus  Manhattan / euclidea
     * @return f(n)
     */
    private double calculaF(Punt origen, Punt desti, int tipus) {
        if (tipus == 2) {
            origen.distanciaAlFinal = calculaManhattan(origen, desti);
        } else {
            origen.distanciaAlFinal = calculaEuclidea(origen, desti);
        }

        return origen.distanciaAlFinal + origen.distanciaDeLinici;
    }

    /**
     * Cálcula h(n) con fórmula Manhattan
     *
     * @param origen Punto de origen
     * @param desti  Punto de desti
     * @return distancia según fórmula
     */
    private int calculaManhattan(Punt origen, Punt desti) {
        return Math.abs(origen.x - desti.x) + Math.abs(origen.y - desti.y);
    }

    /**
     * Cálcula h(n) con fórmula Euclidea
     *
     * @param origen Punto de origen
     * @param desti  Punto de desti
     * @return distancia según fórmula
     */
    private double calculaEuclidea(Punt origen, Punt desti) {
        return Math.sqrt(Math.pow(origen.x - desti.x, 2) + Math.pow(origen.y - desti.y, 2));
    }

    /**
     * Implementación del algoritmo de busqueda de ampladitud (BFS)
     *
     * @param origen Punto inicial
     * @param desti  Punto final
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

                laberint.setNodes(closed.size()); // Set node count
                return camiTrobat;
            }
            open.addAll(non_repeated(open, expand_node(node)));
        }
        return null; //Error
    }

    /**
     * Implementación del algoritmo de busqueda de profunidad (DFS)
     *
     * @param origen Punto inicial
     * @param desti  Punto final
     * @return Camino a seguir para ir desde origen hasta destino
     */
    public Cami CercaEnProfunditat(Punt origen, Punt desti) {
        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);
        ArrayList<Punt> closed = new ArrayList<>();
        ArrayList<Punt> non_repeated;
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

                laberint.setNodes(closed.size()); // Set node count
                return camiTrobat;
            }
            non_repeated = non_repeated(closed, expand_node(node));

            // Swap order to get prefered order upon pop
            for (int i = non_repeated.size() - 1; i >= 0; i--) {
                open.push(non_repeated.get(i));
            }
        }
        return null; // Error
    }

    /**
     * Implementación del algoritmo de busqueda de profunidad (DFS)
     *
     * @param origen Punto inicial
     * @param desti  Punto final
     * @return Camino a seguir para ir desde origen hasta destino
     */
    public Cami CercaAmbHeurística(Punt origen, Punt desti, int tipus) {   // Tipus pot ser MANHATTAN o EUCLIDIA

        ArrayList<Punt> closed = new ArrayList<>();
        ArrayList<Punt> open = new ArrayList<>();

        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);
        boolean found = false;
        Punt current_punt = origen;

        current_punt.distanciaDeLinici = 0;
        calculaF(current_punt, desti, tipus);

        open.add(current_punt);

        while (open.size() != 0) {
            found = false;
            Punt node = getLeast(open, desti, tipus); // Point with least f(n)
            open.remove(0);

            int i;
            for (i = 0; i < closed.size(); i++) {
                if (closed.get(i).equals(node)) {
                    found = true;
                    break;
                }
            }

            // Repeated point
            if (found) {
                // Leave lest f(n)
                if (calculaF(closed.get(i), desti, tipus) > calculaF(node, desti, tipus)) {
                    closed.remove(i);
                    closed.add(node);
                }

            } else {
                closed.add(node);
            }

            if (node.equals(desti)) {

                camiTrobat.afegeix(node); // Final node
                while (node.previ != null) { // All other nodes
                    node = node.previ;
                    camiTrobat.afegeix(node);
                }

                laberint.setNodes(closed.size()); // Set node count
                break;
            }
            open.addAll(non_repeated_heuristica(open, expand_node(node), tipus, desti));
        }

        return camiTrobat;
    }

    /**
     * Metodo que implemtenta el problema del mercader ambulante (TSM). Mediante llamadas recursivas
     * a la búsqueda por amplitud, este método retornará el camino más eficiente con respecto a
     * longitud.
     *
     * @param from    Punto origen
     * @param to      Punto final
     * @param path    Camino parcial
     * @param min     Minimo actual
     * @param visited Conjunto de "ciudades" visitadas
     * @return Camino de menor longitud entre todos las "ciudades" y la salida
     */
    public Cami TSM_recursive(Punt from, Punt to, Cami path, int min, ArrayList<Boolean> visited) {
        Cami temp_path;
        int current_min = min;
        Cami recur;
        Cami exit = new Cami(files * columnes);
        ArrayList<Boolean> temp_visited;
        int nodes = laberint.nodes;

        if (!visited.contains(false)) { // All visited
            temp_path = CercaEnAmplada(from, to);
            laberint.setNodes(nodes + laberint.nodes);
            return concat_paths(path, temp_path);
        }

        for (int i = 0; i < visited.size(); i++) {
            temp_visited = new ArrayList<>();
            temp_visited.addAll(visited); // reset of visited values
            nodes = laberint.nodes; // reset value of nodes

            if (!temp_visited.get(i)) {
                temp_visited.set(i, true); // Count as visited
                temp_path = CercaEnAmplada(from, laberint.getObjecte(i));
                laberint.setNodes(nodes + laberint.nodes);

                if ((path.longitud + (temp_path.longitud - 1)) < current_min) {
                    recur = TSM_recursive(laberint.getObjecte(i), to, concat_paths(path, temp_path), current_min, temp_visited);

                    if ((recur.longitud < current_min) && (recur.longitud != 0)) {
                        current_min = recur.longitud;
                        exit = recur;
                    }
                }
            }
        }

        return exit;
    }

    /**
     * Implementación del algoritmo del mercader mediante el algoritmo de busqueda de amplitud (BFS)
     *
     * @param origen Punto inicial
     * @param desti  Punto final
     * @return Camino a seguir para ir desde origen hasta destino
     */
    public Cami CercaViatjant(Punt origen, Punt desti) {
        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);

        // Get "city" array
        ArrayList<Punt> city = new ArrayList<>();
        try {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                city.add(laberint.getObjecte(i));
            }
        } catch (Exception e) {
        }

        ArrayList<Boolean> visited = new ArrayList<>();
        for (int i = 0; i < city.size(); i++) { // add as much "cities" as we have
            visited.add(false);
        }

        return invert_path(TSM_recursive(origen, desti, camiTrobat, Integer.MAX_VALUE, visited));
    }
}