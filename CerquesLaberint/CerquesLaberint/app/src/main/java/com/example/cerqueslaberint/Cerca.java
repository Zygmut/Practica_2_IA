package com.example.cerqueslaberint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
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


    public void print(Punt punto) {
        System.out.println("Punto\tx:" + punto.x + "\ty:" + punto.y);
    }

    /**
     * Implementación de amplada
     *
     * @param origen Punt inicial
     * @param desti  Punt final
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
            expansioAmplada(current_punt, oberts, tancats);
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

        laberint.setNodes(tancats.elements());

        return camiTrobat;
    }

    /**
     * Vacíamos la Coa en un arraylist para poder usar el método contains() y
     * luego reestablecemos la Coa
     *
     * @param oberts  Coa en la que revisamos si el Punt esta repetido
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
     * @param tancats      Coa para controlar repetidos
     */
    private void expansioAmplada(Punt current_punt, Coa oberts, Coa tancats) {

        //puc anar laberint.DIRECCIO?
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.ESQUERRA)) {
            Punt aux = new Punt(current_punt.x, current_punt.y - 1, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!contains(oberts, aux) && !contains(tancats, aux)) {
                oberts.afegeix(aux);
            }
        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.AMUNT)) {
            Punt aux = new Punt(current_punt.x - 1, current_punt.y, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!contains(oberts, aux) && !contains(tancats, aux)) {
                oberts.afegeix(aux);
            }
        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.DRETA)) {
            Punt aux = new Punt(current_punt.x, current_punt.y + 1, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!contains(oberts, aux) && !contains(tancats, aux)) {
                oberts.afegeix(aux);
            }
        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.AVALL)) {
            Punt aux = new Punt(current_punt.x + 1, current_punt.y, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!contains(oberts, aux) && !contains(tancats, aux)) {
                oberts.afegeix(aux);
            }
        }
    }

    /**
     * Método que expande un Punt abriendo todos sus nodos y colocando los no repetidos
     * en la Pila de abiertos
     *
     * @param current_punt Punt que esta siendo estudiado(abriéndose)
     * @param oberts       Pila donde se meterán los nodos nuevos no repetidos
     * @param tancats      Coa para controlar repetidos
     */
    private void expansioProfundidad(Punt current_punt, Stack oberts, Coa tancats) {

        //puc anar laberint.DIRECCIO?
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.ESQUERRA)) {
            Punt aux = new Punt(current_punt.x, current_punt.y - 1, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!oberts.contains(aux) && !contains(tancats, aux)) {
                oberts.push(aux);
            }
        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.AMUNT)) {
            Punt aux = new Punt(current_punt.x - 1, current_punt.y, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!oberts.contains(aux) && !contains(tancats, aux)) {
                oberts.push(aux);
            }
        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.DRETA)) {
            Punt aux = new Punt(current_punt.x, current_punt.y + 1, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!oberts.contains(aux) && !contains(tancats, aux)) {
                oberts.push(aux);
            }
        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.AVALL)) {
            Punt aux = new Punt(current_punt.x + 1, current_punt.y, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!oberts.contains(aux) && !contains(tancats, aux)) {
                oberts.push(aux);
            }
        }
    }

    public Cami CercaEnProfunditat(Punt origen, Punt desti) {
        Stack<Punt> oberts = new Stack<>();
        Coa tancats = new Coa();
        boolean fin = false;
        Punt current_punt = origen;

        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);

        // Implementa l'algoritme aquí

        oberts.push(current_punt);

        while (!fin) {
            //Punto a tratar
            current_punt = oberts.pop();
            //expandimos, mirando que no este repetido
            expansioProfundidad(current_punt, oberts, tancats);
            //colocamos en lista de cerrados
            tancats.afegeix(current_punt);

            //es el nodo desti?
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
        laberint.setNodes(tancats.elements());
        return camiTrobat;
    }
    private void replace(PriorityQueue coa, Punt punt, Punt desti, int tipus){
        ArrayList<Punt> list = new ArrayList<>();

        //vaciamos en list
        while (!coa.isEmpty()) {
            list.add((Punt) coa.remove());
        }

        if(calculaF(punt,desti,tipus) < calculaF(list.get(list.indexOf(punt)),desti,tipus)){
            list.remove(list.indexOf(punt));
            list.add(punt);
        }

        //reestablecemos coa
        for (int i = 0; i < list.size(); i++) {
            coa.add(list.get(i));
        }


    }
    private void expansioHeuristica(Punt current_punt, PriorityQueue oberts, PriorityQueue tancats, Punt desti, int tipus) {

        //puc anar laberint.DIRECCIO?
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.ESQUERRA)) {
            Punt aux = new Punt(current_punt.x, current_punt.y - 1, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!oberts.contains(aux) && !tancats.contains(aux)) {
                calculaF(aux,desti,tipus);
                oberts.add(aux);
            }
            else{
                //replace(PriorityQueue coa, Punt punt, Punt desti, int tipus)
                if(oberts.contains(aux)){
                    replace(oberts,aux,desti,tipus);
                }
                else{
                    replace(tancats,aux,desti,tipus);
                }
            }
        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.AMUNT)) {
            Punt aux = new Punt(current_punt.x - 1, current_punt.y, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!oberts.contains(aux) && !tancats.contains(aux)) {
                calculaF(aux,desti,tipus);
                oberts.add(aux);
            }
            else{
                //replace(PriorityQueue coa, Punt punt, Punt desti, int tipus)
                if(oberts.contains(aux)){
                    replace(oberts,aux,desti,tipus);
                }
                else{
                    replace(tancats,aux,desti,tipus);
                }
            }
        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.DRETA)) {
            Punt aux = new Punt(current_punt.x, current_punt.y + 1, current_punt, 0);
            print(aux);
            ///Nodo repetido?
            if (!oberts.contains(aux) && !tancats.contains(aux)) {
                calculaF(aux,desti,tipus);
                oberts.add(aux);
            }
            else{
                //replace(PriorityQueue coa, Punt punt, Punt desti, int tipus)
                if(oberts.contains(aux)){
                    replace(oberts,aux,desti,tipus);
                }
                else{
                    replace(tancats,aux,desti,tipus);
                }
            }
        }
        if (laberint.pucAnar(current_punt.x, current_punt.y, Laberint.AVALL)) {
            Punt aux = new Punt(current_punt.x + 1, current_punt.y, current_punt, 0);
            print(aux);
            //Nodo repetido?
            if (!oberts.contains(aux) && !tancats.contains(aux)) {
                calculaF(aux,desti,tipus);
                oberts.add(aux);
            }
            else{
                //replace(PriorityQueue coa, Punt punt, Punt desti, int tipus)
                if(oberts.contains(aux)){
                    replace(oberts,aux,desti,tipus);
                }
                else{
                    replace(tancats,aux,desti,tipus);
                }
            }
        }
    }
    public Cami CercaAmbHeurística(Punt origen, Punt desti, int tipus) {   // Tipus pot ser MANHATTAN o EUCLIDIA
        int i;
        PriorityQueue<Punt> tancats = new PriorityQueue<>();
        PriorityQueue<Punt> oberts = new PriorityQueue<Punt>(new Comparator<Punt>() {
            public int compare(Punt x, Punt y) {
                if (x.distanciaDeLinici+x.distanciaAlFinal < y.distanciaDeLinici+y.distanciaAlFinal) return -1;
                if (x.distanciaDeLinici+x.distanciaAlFinal > y.distanciaDeLinici+y.distanciaAlFinal) return 1;
                return 0;
            }
        });

        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);
        Punt current_punt = origen;
        boolean trobat = false;
        // Implementa l'algoritme aquí
        calculaF(current_punt,desti,tipus);
        oberts.add(current_punt);

        while(!trobat){
            current_punt = oberts.peek();

            if(current_punt == desti){
                trobat=true;
            }
            expansioHeuristica(current_punt,oberts,tancats,desti,tipus);
            oberts.remove(current_punt);
            tancats.add(current_punt);
        }

        //primer Punt
        camiTrobat.afegeix(current_punt);
        while (current_punt.previ != null) {
            current_punt = current_punt.previ;
            camiTrobat.afegeix(current_punt);
        }
        //ultim Punt
        camiTrobat.afegeix(current_punt);
        laberint.setNodes(tancats.size());
        return camiTrobat;
    }

    private int calculaG(Punt origen, Punt desti){

        return CercaEnAmplada(origen, desti).longitud;
    }

    private double calculaF(Punt origen, Punt desti, int tipus){
        origen.distanciaDeLinici = calculaG(origen,desti);

        if(tipus == 2){
            origen.distanciaAlFinal = calculaManhattan(origen,desti);
        }
        else{
            origen.distanciaAlFinal = calculaEuclidea(origen,desti);
        }

        return origen.distanciaAlFinal + origen.distanciaDeLinici;
    }

    private int calculaManhattan(Punt origen, Punt desti){

        return Math.abs(origen.x-desti.x) + Math.abs(origen.y-desti.y);
    }

    private double calculaEuclidea(Punt origen, Punt desti){

        return  Math.sqrt(Math.pow(origen.x-desti.x, 2) + Math.pow(origen.y-desti.y, 2));
    }

    public Cami CercaViatjant(Punt origen, Punt desti) {
        Cami camiTrobat = new Cami(files * columnes);
        laberint.setNodes(0);

        // Implementa l'algoritme aquí
        camiTrobat.afegeix(desti);

        return camiTrobat;
    }
}
