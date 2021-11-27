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

    public Cami CercaEnAmplada(Punt origen, Punt desti) {
        Coa oberts = new Coa();
        Coa tancats = new Coa();

        boolean fin = false;

        Punt current_punt = origen;

        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);

        // Implementa l'algoritme aquí
        oberts.afegeix(current_punt);

        while (!fin) {
            expansioAmplada(current_punt, oberts, tancats);
            current_punt = (Punt) oberts.treu();

            if (current_punt.equals(desti)) {
                fin = true;
            }
        }

        revertirOrdre(current_punt, camiTrobat, oberts, tancats);
        return camiTrobat;
    }

    private void revertirOrdre(Punt current_punt, Cami solucio, Coa q1, Coa q2) {

        buidar(q1);
        buidar(q2);

        pushTot(q1, current_punt);

        while (!q1.buida()) {
            laberint.incNodes();
            solucio.afegeix(pop(q1, q2));
        }


    }

    private void buidar(Coa q) {
        while (!q.buida()) {
            q.treu();
        }
    }

    private void pushTot(Coa q, Punt current_punt) {
        while (current_punt != null) {
            q.afegeix(current_punt);
            current_punt = current_punt.previ;
        }
    }

    private Punt pop(Coa q1, Coa q2) {
        while (q1.elements() > 1) {
            q2.afegeix(q1.treu());
        }
        Punt primer_pila = (Punt) q1.treu();

        q1 = q2;
        buidar(q2);

        return primer_pila;
    }

    private void expansioAmplada(Punt current_punt, Coa oberts, Coa tancats) {
        Punt aux;

        if (laberint.pucAnar(current_punt.x, current_punt.y, laberint.AVALL)) {
            aux = current_punt;
            aux.previ = current_punt;
            aux.x = aux.x + 1;
            oberts.afegeix(aux);

        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, laberint.DRETA)) {
            aux = current_punt;
            aux.previ = current_punt;
            aux.y = aux.y + 1;
            oberts.afegeix(aux);

        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, laberint.AMUNT)) {
            aux = current_punt;
            aux.previ = current_punt;
            aux.x = aux.x - 1;
            oberts.afegeix(aux);

        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, laberint.ESQUERRA)) {
            aux = current_punt;
            aux.previ = current_punt;
            aux.y = aux.y - 1;
            oberts.afegeix(aux);

        }

        tancats.afegeix(oberts.treu());
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
