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
     * @param punto Punto a printear
     * @return String con los contenidos pertinentes del punto
     */
    public String print_punt(Punt punto) {
        return "Punto\tx:" + punto.x + "\ty:" + punto.y;
    }

    /**
     * Añade todos los elementos del segundo parametro al primero. target = target U toAdd
     * @param target
     * @param toAdd Camino a concantenar
     */
    public void concat_paths(Cami target, Cami toAdd){
        for (int i = 1; i < toAdd.longitud; i++){ // Skip first node desti = origen
            target.afegeix(toAdd.cami[i]);
        }
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

    /**
     * Metodo que implemtenta el problema del mercader ambulante (TSM). Mediante llamadas recursivas
     * a la búsqueda por amplitud, este método retornará el camino más eficiente con respecto a
     * longitud.
     * @param from
     * @param to
     * @param path
     * @param city
     * @param min
     * @param visited
     * @return Camino de menor longitud entre todos las "ciudades" y la salida
     */
    public Cami TSM_recursive(Punt from, Punt to, Cami path, int city, int min, ArrayList<Boolean> visited){
        Cami temp_path;
        int current_min = min;
        Cami recur;
        ArrayList<Boolean> temp_visited = new ArrayList<>();
        Cami exit = new Cami(files * columnes);
        int nodes = laberint.nodes;

        if (!visited.contains(false)){ // All visited
            temp_path = CercaEnAmplada(from, to);
            laberint.setNodes(nodes + laberint.nodes);
            concat_paths(path, temp_path);
            return path;
        }

        for (int i = 0; i < city; i++){
            temp_visited.addAll(visited); // reset of visited values
            nodes = laberint.nodes; // reset value of nodes
            if (!temp_visited.get(i)){
                temp_visited.set(i, true); // Count as visited
                temp_path = CercaEnAmplada(from, laberint.getObjecte(i));
                laberint.setNodes(nodes + laberint.nodes);
                if ((path.longitud + (temp_path.longitud-1)) < current_min){
                    concat_paths(path, temp_path);

                    recur = TSM_recursive(laberint.getObjecte(i), to, path, city, current_min, temp_visited);
                    if (recur.longitud < min){
                        current_min = path.longitud;
                        exit = recur;
                    }
                }
            }
        }

        return exit;
    }
    /**
     * Implementación del algoritmo del mercader mediante el algoritmo de busqueda de amplitud (BFS)
     * @param origen Punto inicial
     * @param desti Punto final
     * @return Camino a seguir para ir desde origen hasta destino
     */
    public Cami CercaViatjant(Punt origen, Punt desti) {
        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);

        // Get "city" array
        ArrayList<Punt> city = new ArrayList<>();
        try{
            for (int i = 0; i < Integer.MAX_VALUE; i++){
                city.add(laberint.getObjecte(i));
            }
        }catch(Exception e){
        }

        ArrayList<Boolean> visited = new ArrayList<>();
        for (int i = 0; i < city.size(); i++){ // add as much "cities" as we have
            visited.add(false);
        }

        return TSM_recursive(origen, desti, camiTrobat, city.size(), Integer.MAX_VALUE, visited);
    }
}
