# Medius JobFair 2025

Končna rešitev se nahaja v `solver/DFSSolver.java`.

Program temelji na algoritmu DFS, vključuje pa več izboljšav:

- **Predračunane poteze**: Vse možne poteze so predračunane, kar pohitri generiranje novih stanj.
- **Omejena globina iskanja**: Globina je omejena glede na razliko v številu figur med začetnim in končnim stanjem.
- **Izkoriščanje simetričnosti igralnega polja**: Omogoča agresivno rezanje in s tem manjše število rekurzij.
- **Bitne operacije**: Uporaba bitnih mask za učinkovito izvajanje potez.

