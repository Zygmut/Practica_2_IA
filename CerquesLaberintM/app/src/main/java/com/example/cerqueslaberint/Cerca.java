package com.example.cerqueslaberint;

import java.util.ArrayList;
import java.util.Collections;

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


    public void print(Punt punto) {
        System.out.println("Punto\tx:" + punto.x + "\ty:" + punto.y);
    }

    /**
     * Implementación de amplada
     *
     * @param origen Punt inicial
     * @param desti Punt final
     * @return
     */
    public Cami CercaEnAmplada(Punt origen, Punt desti) {
        Coa oberts = new Coa();
        Coa tancats = new Coa();

        boolean fin = false;

        Punt current_punt = origen;

        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);

        // Implementa l'algoritme aquí
        oberts.afegeix(current_punt);

        //mientras no encontremos el Punt final..
        while (!fin) {
            //estudiamos Punt actual
            expansioAmplada(current_punt, oberts);
            //colocamos Punt actual en Coa tancats
            tancats.afegeix(oberts.treu());

            //miramos si es el Punt final, sino, seguimos
            current_punt = (Punt) oberts.consulta();
            if (current_punt.equals(desti)) {
                fin = true;
            }
        }

        //primer Punt
        camiTrobat.afegeix(current_punt);
        while (current_punt.previ != null) {
            current_punt = current_punt.previ;
            camiTrobat.afegeix(current_punt);
        }
        //ultim Punt
        camiTrobat.afegeix(current_punt);

        return camiTrobat;
    }

    /**
     * Vacíamos la Coa en un arraylist para poder usar el método contains() y
     * luego reestablecemos la Coa
     *
     * @param oberts Coa en la que revisamos si el Punt esta repetido
     * @param current Punt a revisar si esta repetido
     * @return
     */
    private boolean contains(Coa oberts, Punt current) {
        boolean trobat;
        ArrayList<Punt> list = new ArrayList<>();

        //vaciamos coa en list
        while (!oberts.buida()) {
            list.add((Punt) oberts.treu());
        }

        //contiene oberts el nodo current?
        trobat = list.contains(current);

        //reestablecemos coa oberts
        for (int i = 0; i < list.size(); i++) {
            oberts.afegeix(list.get(i));
        }

        return trobat;
    }

    /**
     * Método que expande un Punt abriendo todos sus nodos y colocando los no repetidos
     * en la coa de abiertos
     *
     * @param current_punt Punt que esta siendo estudiado(abriéndose)
     * @param oberts       Coa donde se meterán los nodos nuevos no repetidos
     */
    private void expansioAmplada(Punt current_punt, Coa oberts) {

        //puc anar laberint.DIRECCIO?
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.ESQUERRA)) {
            Punt aux = new Punt(current_punt.x, current_punt.y - 1, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!contains(oberts, aux)) {
                oberts.afegeix(aux);
            }
        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.AMUNT)) {
            Punt aux = new Punt(current_punt.x - 1, current_punt.y, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!contains(oberts, aux)) {
                oberts.afegeix(aux);
            }
        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.DRETA)) {
            Punt aux = new Punt(current_punt.x, current_punt.y + 1, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!contains(oberts, aux)) {
                oberts.afegeix(aux);
            }
        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.AVALL)) {
            Punt aux = new Punt(current_punt.x + 1, current_punt.y, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!contains(oberts, aux)) {
                oberts.afegeix(aux);
            }
        }
    }

    public Cami CercaEnProfunditat(Punt origen, Punt desti) {
        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);

        // Implementa l'algoritme aquí
        camiTrobat.afegeix(desti);

        return camiTrobat;
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
