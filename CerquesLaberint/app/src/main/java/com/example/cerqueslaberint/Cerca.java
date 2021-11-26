package com.example.cerqueslaberint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Ramon Mas on 10/10/21.
 * Classe que conté els diferents algorismes de cerca que s'han d'implementar
 */

/**
 *   AUTORS:__________________________________________
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

public class Cerca
{
    static final public int MANHATTAN = 2;
    static final public int EUCLIDEA  = 3;

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
    public String print_punt(Punt punto){
        return "Punto\tx:" + punto.x + "\ty:" + punto.y;
    }
    /*
     * Dado un nodo, retorna la cantidad de nodos a los que podemos ir siguiendo un orden
     */
    public Punt[] expand_node(Punt node){
        ArrayList<Punt> expansion = new ArrayList<>();
        System.out.println(print_punt(node));
        int[] orden = {laberint.ESQUERRA, laberint.AMUNT, laberint.DRETA, laberint.AVALL};
        for (int i = 0; i < orden.length; i++){
            System.out.println(orden[i]);
            if (laberint.pucAnar(node.y, node.x, orden[i])){
                switch (orden[i]){
                    // No se que es este "val" pero nunca se usa
                    case Laberint.ESQUERRA:
                        System.out.println("puedo ir a la izquierda");
                        expansion.add(new Punt(node.x, node.y-1, node, 0));
                        break;
                    case Laberint.AMUNT:
                        System.out.println("puedo ir arriba");
                        expansion.add(new Punt(node.x-1, node.y, node, 0));
                        break;
                    case Laberint.DRETA:
                        System.out.println("puedo ir a la derecha");
                        expansion.add(new Punt(node.x, node.y+1, node, 0));
                        break;
                    case Laberint.AVALL:
                        System.out.println("puedo ir abajo");
                        expansion.add(new Punt(node.x+1, node.y, node, 0));
                        break;
                    default:
                        return null;
                }
            }
        }

        return expansion.toArray(new Punt[expansion.size()]);

    }
    public Cami CercaEnAmplada(Punt origen, Punt desti)
    {
        Cami camiTrobat = new Cami(files*columnes);
        laberint.setNodes(0);
        System.out.println(Arrays.toString(expand_node(origen)));
        // Implementa l'algoritme aquí
        camiTrobat.afegeix(desti);
        return camiTrobat;
    }

    public Cami CercaEnProfunditat(Punt origen, Punt desti)
    {
        Cami camiTrobat = new Cami(files*columnes);
        laberint.setNodes(0);

        // Implementa l'algoritme aquí
        camiTrobat.afegeix(desti);

        return camiTrobat;
    }

    public Cami CercaAmbHeurística(Punt origen, Punt desti, int tipus)
    {   // Tipus pot ser MANHATTAN o EUCLIDIA
        int i;
        Cami camiTrobat = new Cami(files*columnes);
        laberint.setNodes(0);

        // Implementa l'algoritme aquí
        camiTrobat.afegeix(desti);

        return camiTrobat;
    }


    public Cami CercaViatjant(Punt origen, Punt desti)
    {
        Cami camiTrobat = new Cami(files*columnes);
        laberint.setNodes(0);

        // Implementa l'algoritme aquí
        camiTrobat.afegeix(desti);

        return camiTrobat;
    }
}
