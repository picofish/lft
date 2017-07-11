package it.nettiva.codreanu.lft.RegExp;

import it.nettiva.codreanu.lft.NFA;

public class RegExpEpsilon implements RegExp {
    public NFA compile() {
        NFA a = new NFA(2);
        a.addMove(0, NFA.EPSILON, 1);
        a.addFinalState(1);
        return a;
    }
}