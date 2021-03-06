package it.nettiva.codreanu.lft;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Un oggetto della classe NFA rappresenta un automa a stati finiti non
 * deterministico con epsilon transizioni
 */
public class NFA {

    /**
     * Usiamo il carattere nullo per rappresentare una epsilon transizione
     */
    public static final char EPSILON = 'E';

    /**
     * Numero degli stati dell'automa. Ogni stato e` rappresentato da un numero
     * interno non negativo, lo stato con indice 0 e` lo stato iniziale.
     */
    private int numberOfStates;

    /**
     * Insieme degli stati finali dell'automa.
     */
    private HashSet<Integer> finalStates;

    /**
     * Funzione di transizione dell'automa, rappresentata come una mappa da
     * mosse a insiemi di stati di arrivo.
     */
    private HashMap<Move, HashSet<Integer>> transitions;

    /**x
     * Crea un NFA con un dato numero di stati.
     *
     * @param n Il numero di stati dell'automa.
     */
    public NFA(int n) {
        numberOfStates = n;
        finalStates = new HashSet<Integer>();
        transitions = new HashMap<Move, HashSet<Integer>>();
    }

    /**
     * Aggiunge uno stato all'automa.
     *
     * @return L'indice del nuovo stato creato
     */
    public int newState() {
        return numberOfStates++;
    }

    /**
     * Aggiunge uno stato finale.
     *
     * @param p Lo stato che si vuole aggiungere a quelli finali.
     * @return <code>true</code> se lo stato e` valido, <code>false</code>
     * altrimenti.
     */
    public boolean addFinalState(int p) {
        if (validState(p)) {
            finalStates.add(p);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determina se uno stato e` valido oppure no.
     *
     * @param p Lo stato da controllare.
     * @return <code>true</code> se lo stato e` valido, <code>false</code>
     * altrimenti.
     * @see #numberOfStates
     */
    public boolean validState(int p) {
        return (p >= 0 && p < numberOfStates);
    }

    /**
     * Determina se uno stato e` finale oppure no.
     *
     * @param p Lo stato da controllare.
     * @return <code>true</code> se lo stato e` finale, <code>false</code>
     * altrimenti.
     * @see #finalStates
     */
    public boolean finalState(int p) {
        return finalStates.contains(p);
    }

    /**
     * Restituisce il numero di stati dell'automa.
     *
     * @return Numero di stati.
     */
    public int numberOfStates() {
        return numberOfStates;
    }

    /**
     * Aggiunge una transizione all'automa.
     *
     * @param p  Lo stato di partenza della transizione.
     * @param ch Il simbolo che etichetta la transizione.
     * @param q  Lo stato di arrivo della transizione.
     * @return <code>true</code> se lo stato di partenza e lo stato di arrivo
     * sono validi, <code>false</code> altrimenti.
     */
    // TODO Esercizio 5.1
    public boolean addMove(int p, char ch, int q) {
        if (validState(p) && validState(q)) {                    //controllo che i due stati passati per parametro siano validi
            HashSet<Integer> states;                        //HashSet di interi che conterr� gli stati raggiungibili da p leggendo ch
            Move m = new Move(p, ch);                        //creo una nuova istanza di Move con parametri p e ch

            if (transitions.containsKey(m)) {                    //se transitions contiene gi� l'istanza di Move con stato p e carattere ch
                states = transitions.get(m);                    //allora esiste gi� un oggetto ad essa associata
                states.add(q);                                //si aggiunge solamente il nuovo stato q all'insieme degli stati raggiungibili da p leggendo ch
            } else {
                states = new HashSet<Integer>();            //altrimenti creo un nuovo oggetto states
                states.add(q);                                //aggiungo q a states
                transitions.put(m, states);                    //associo la mossa m al nuovo insieme di stati
            }
            return true;
        }
        return false;
    }

    /**
     * Determina se c'e` uno stato finale in un insieme di stati.
     *
     * @param s L'insieme di stati da controllare.
     * @return <code>true</code> se c'e` uno stato finale in <code>s</code>,
     * <code>false</code> altrimenti.
     * @see #finalStates
     */
    private boolean finalState(HashSet<Integer> s) {
        for (int p : s) {
            if (finalState(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Restituisce l'alfabeto dell'automa, ovvero l'insieme di simboli che
     * compaiono come etichette delle transizioni dell'automa. Notare che
     * <code>EPSILON</code> non e` un simbolo.
     *
     * @return L'alfabeto dell'automa.
     */
    // TODO Esercizio 5.1
    public HashSet<Character> alphabet() {
        HashSet<Character> alphabet = new HashSet<>();
        for (Move m : transitions.keySet()) {
            if (m.ch != EPSILON) {
                alphabet.add(m.ch);
            }
        }
        return alphabet;
    }

    /**
     * Esegue una mossa dell'automa.
     *
     * @param p  Stato di partenza prima della transizione.
     * @param ch Simbolo da riconoscere.
     * @return Insieme di stati di arrivo dopo la transizione. Questo insieme
     * puo` essere vuoto.
     */
    // TODO Esercizio 5.1
    public HashSet<Integer> move(int p, char ch) {
        HashSet<Integer> states = new HashSet<>();
        Move             move   = new Move(p, ch);
        if (transitions.containsKey(move)) {
            states = transitions.get(move);
        }
        return states;
    }

    /**
     * Esegue una mossa dell'automa.
     *
     * @param s  Insieme di stati di partenza prima della transizione.
     * @param ch Simbolo da riconoscere.
     * @return Insieme di stati di arrivo dopo la transizione. Questo insieme
     * puo` essere vuoto.
     */
    public HashSet<Integer> move(HashSet<Integer> s, char ch) {
        HashSet<Integer> qset = new HashSet<>();
        for (int p : s) {
            qset.addAll(move(p, ch));
        }
        return qset;
    }

    /**
     * Calcola la epsilon chiusura di un insieme di stati dell'automa.
     *
     * @param s Insieme di stati di cui calcolare l'epsilon chiusura.
     * @return Insieme di stati raggiungibili da quelli contenuti in
     * <code>s</code> per mezzo di zero o piu` epsilon transizioni.
     */
    // TODO Esercizio 5.5
    public HashSet<Integer> epsilonClosure(HashSet<Integer> s) {
        HashSet<Integer> qset = new HashSet<>();
        for (int p : s) {
            qset.addAll(epsilonClosure(p));
        }
        return qset;
    }

    /**
     * Calcola la epsilon chiusura di uno stato dell'automa. E` un caso
     * specifico del metodo precedente.
     *
     * @param q Insieme di cui calcolare l'epsilon chiusura.
     * @return Insieme di stati raggiungibili da <code>p</code> per mezzo di
     * zero o piu` epsilon transizioni.
     * @see #epsilonClosure
     */
    // TODO Esercizio 5.1
    public HashSet<Integer> epsilonClosure(int q) {
        HashSet<Integer>    s          = new HashSet<>();
        LinkedList<Integer> daVisitare = new LinkedList<>();
        if (!validState(q)) {
            return s;
        }
        s.add(q);
        daVisitare.add(q);
        while (!daVisitare.isEmpty()) {
            q = daVisitare.remove(0);
            for (Move m : transitions.keySet()) {
                if (m.start == q && m.ch == EPSILON) {
                    for (int x : transitions.get(m)) {
                        if (!s.contains(x)) {
                            s.add(x);
                            daVisitare.add(x);
                        }
                    }
                }
            }
        }
        return s;
    }

    public HashSet<Integer> epsilonClosureProf(int q) {
        HashSet<Integer> qset          = new HashSet<Integer>();
        HashSet<Integer> checkedStates = new HashSet<Integer>();
        HashSet<Integer> states;
        Move             m;
        boolean          r[]           = new boolean[numberOfStates];        //l'elemento r[i] del vettore serve a indicare se lo stato (con indice) i �
        // raggiungibile da q per mezzo di zero o più epsilon-transizioni.
        for (int i = 0; i < r.length; i++) {
            r[i] = (i == q);                                //lo stato q `e sempre raggiungibile da se stesso per mezzo di zero epsilon-transizioni.
        }
        boolean repeat = true;
        while (repeat) {
            repeat = false;
            for (int i = 0; i < r.length; i++) {
                if (r[i] && !checkedStates.contains(i)) {        //viene controllato che lo stato i non sia gi� stato controllato in precedenza
                    checkedStates.add(i);
                    m = new Move(i, EPSILON);                //creo una nuova istanza di Move dallo stato i con epsilon
                    if (transitions.containsKey(m)) {            //se esiste una transizione da i con epsilon
                        states = transitions.get(m);
                        for (int j : states) //per ogni stato raggiungibile da i con epsilon imposto il corrispondente valore di r a true
                        {
                            repeat = r[j] = true;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < r.length; i++) {
            if (r[i]) {
                qset.add(i);
            }
        }
        return qset;
    }

    /**
     * Calcola l'automa a stati finiti deterministico equivalente.
     *
     * @return DFA equivalente.
     */
    // TODO Esercizio 5.3
    public DFA dfa() {
        // la costruzione del DFA utilizza due tabelle hash per tenere
        // traccia della corrispondenza (biunivoca) tra insiemi di
        // stati del NFA e stati del DFA
        HashMap<HashSet<Integer>, Integer> indexOfSet
                = new HashMap<>();    // NFA -> DFA
        HashMap<Integer, HashSet<Integer>> setOfIndex
                = new HashMap<>();    // DFA -> NFA

        DFA                dfa       = new DFA(1);                            // il DFA
        Stack<Integer>     newStates = new Stack<>(); // nuovi stati del DFA
        HashSet<Character> alphabet  = alphabet();

        indexOfSet.put(epsilonClosure(0), 0); // stati dell'NFA corrisp. a q0
        setOfIndex.put(0, epsilonClosure(0));
        newStates.push(0);                    // nuovo stato da esplorare

        // indexOfSet  NFA -> DFA
        // setOfIndex  DFA -> NFA
        while (!newStates.empty()) { // finche' ci sono nuovi stati da visitare
            final int p = newStates.pop(); // ne considero uno e lo visito
            final HashSet<Integer> pset = setOfIndex.get(p); // stati del NFA corrisp.
            for (char ch : alphabet) { // considero tutte le possibili transizioni
                HashSet<Integer> qset = epsilonClosure(move(pset, ch));
                if (indexOfSet.containsKey(qset)) { // se qset non e` nuovo...
                    final int q = indexOfSet.get(qset); // recupero il suo indice
                    dfa.setMove(p, ch, q);          // aggiungo la transizione
                } else {                            // se invece qset e` nuovo
                    final int q = dfa.newState();   // creo lo stato nel DFA
                    indexOfSet.put(qset, q);        // aggiorno la corrispondenza
                    setOfIndex.put(q, qset);
                    newStates.push(q);              // q e` da visitare
                    dfa.setMove(p, ch, q);          // aggiungo la transizione
                }
            }
        }

        // stabilisco gli stati finali del DFA
        for (int p = 0; p < dfa.numberOfStates(); p++) {
            if (finalState(setOfIndex.get(p))) {
                dfa.addFinalState(p);
            }
        }

        return dfa;
    }

    /**
     * Stampa una rappresentazione testuale dell'automa da visualizzare con
     * <a href="http://www.graphviz.org">GraphViz</a>.
     *
     * @param name Nome dell'automa.
     */
    // TODO Esercizio 5.4
    public void toDOT(String name) {
        HashSet<Integer> states;
        System.out.println("digraph " + name + " {");
        System.out.println("  rankdir=LR;");
        System.out.println("  node [shape = doublecircle];");

        Object[] fs = finalStates.toArray();

        for (int i = 0; i < fs.length; i++) {
            if (i == fs.length - 1) {
                System.out.print("  q" + fs[i].toString());
            } else {
                System.out.print("  q" + fs[i].toString() + ", ");
            }
        }
        System.out.println(";\n  node [shape = circle];");

        HashMap<String, String> res = new HashMap<>();

        for (Move m : transitions.keySet()) {
            for (int x : transitions.get(m)) {
                String from = "q" + m.start + " -> " + "q" + x;
                if (!res.containsKey(from)) {
                    res.put(from, Character.toString(m.ch)); // se il carattere è unico lo lascio cosi
                } else {
                    res.put(from, res.get(from) + "," + m.ch);  //altrimenti lo aggiungo in coda con la virgola
                }
            }

        }
        for (String s : res.keySet()) {
            System.out.println("  " + s + " [ label = \"" + res.get(s) + "\" ];");
        }
        System.out.println("}\n");
    }

    //metodo append per l'unione di più NFA
    /* 
     */
    public int append(NFA a) {
        final int n = numberOfStates;
        numberOfStates += a.numberOfStates();
        for (Move m : a.transitions.keySet()) {
            for (int q : a.transitions.get(m)) {
                addMove(n + m.start, m.ch, n + q);
            }
        }
        return n;
    }

    /* Implementare un metodo statico nth della classe NFA che, dato un numero naturale
     * n, produce l'automa non deterministico avente n + 1 stati che riconosce le stringhe di 0 e 1 tali
     * che l'n-esimo simbolo da destra sia 1.
     */
    public static NFA nth(int n) {
        NFA nfa = new NFA(n + 1);
        nfa.addMove(0, '0', 0);                //le seguenti mosse sono uguali per ogni nfa creato
        nfa.addMove(0, '1', 0);
        nfa.addMove(0, '1', 1);

        for (int i = 1; i < n; i++) {                //si aggiungono le mosse dallo stato i allo stato i+1 leggendo 0 o 1
            nfa.addMove(i, '0', i + 1);
            nfa.addMove(i, '1', i + 1);
        }

        nfa.addFinalState(n);                //l'ultimo stato dell'nfa è lo stato finale

        return nfa;
    }
}
