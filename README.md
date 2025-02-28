# Medius JobFair 2025

Dobrodošli na programerskem izzivu podjetja Medius na JobFair 2025.

Tokratni izziv je programiranje reševalnika za igro [Peg Solitaire](https://en.wikipedia.org/wiki/Peg_solitaire), katero ste že lahko spoznali v živo na naši stojnici.

## Peg Solitaire

V naši različici igre bomo uporabili angleško različico plošče v obliki križa s 7 vrsticami in stolpci, skupno 33 polji.
Veljavna polja na plošči so označena z 'o':

```
    o o o
    o o o
o o o o o o o
o o o o o o o
o o o o o o o
    o o o
    o o o
```

Igra uporablja 32 figur, ki jih postavimo v polje, eno polje ostane prazno.
Primer začetnega stanja plošče je:
```
    x x x
    x x x
x x x x x x x
x x x o x x x
x x x x x x x
    x x x
    x x x
```
kjer 'x' predstavlja polje s figuro, 'o' pa prazno polje.

Igra se igra, dokler na igralni plošči ne ostane le 1 figura.
Ob vsaki potezi izberemo figuro, ki jo lahko preko polja s figuro ortogonalno premaknemo na prazno polje.
Potem preskočeno figuro umaknemo iz plošče.

Primeri veljavnih potez:
```
x    o      o    x  
x -> o      x -> o      x x o -> o o x      o x x -> x o o      
o    x      x    o
```

### Reševalnik

Vaša naloga je implementirati reševalnik (solver) igre Peg Solitaire v programskem jeziku Java.
Reševalnik naj v čimkrajšem času najde najkrajšo rešitev.
Na voljo imate osnovno knjižnico znotraj `java` paketa, **ostale knjižnice niso dovoljene**.

Stanje plošče bo podano v obliki 49 bitnega števila, ki predstavlja zaporedje polj na plošči.
Vrednost '0' predstavlja prazno polje, brez figure, vrednost '1' pa polje s figuro.

Primer: desetiško število 124141717933596 je v binarnem formatu enako `11100001110011111111110111111111100111000011100`.
Če spredaj pripnemo 2 ničli, da dobimo število dolžine 49, dobimo `0011100001110011111111110111111111100111000011100`.
Nekoliko preuredimo številko, da vsakih 7 števk zapišemo v novo vrstico:
```
0011100
0011100
1111111
1110111
1111111
0011100
0011100
```

kar predstavlja običajno začetno stanje plošče.

Kot vhod v reševalnik boste prejeli začetno in končno stanje igralne plošče obliki številke v podatkovnem tipu `long`.
Vaša naloga je vrniti zaporedje stanj plošče ob vsakem premiku figure, vključno z začetnim in končnim stanjem plošče.

Vašo rešitev implementirajte v novi `.java` datoteki, katere razred implementira vmesnik `IPegSolitaireSolver`.
Znotraj razreda implementirajte metodi `solve` in `personalData`.
Metoda `solve` kot parameter prejme dve števili, ki predstavljata začetno in željeno končno stanje plošče, kot izhod pa pričakuje seznam števil, ki predstavljajo vmesna stanja na plošči.
V primeru, da končno stanje ni dosegljivo, vrnite prazen seznam.
V metodo `personalData` v tabelo nizov napišite vaše ime in priimek, vsakega v svoj niz.

Primer osnovnega reševalnika je na voljo v `/solver/BasicSolver.java`, kjer vsakič izbere naključno veljavno potezo.
Ogrodje vašega reševalnika je na voljo v `solver/TemplateSolver.java`.

### Namigi

Bodite pozorni:
- številka, ki predstavlja stanje plošče, vsebuje tudi neveljavna polja v vsakem kotu, implementirajte ustrezna pravila, da se figure ne premikajo na ta neveljavna polja
- lahko se zgodi, da željeno končno stanje ni dosegljivo s podanim začetnim stanjem plošče, ustrezno pokrijte scenarij, da v primeru nedosegljivega stanja program to prepozna in vrne prazen seznam.

### Testiranje

Testiranje in validiranje vrnjenih potez se preverja v datoteki `/base/PegSolitaireValidation.java`.
Vse, kar morate narediti, da v spremenljivko `solverFileName` zapišete ime svojega razreda in java datoteke in jo odložite v `solver` direktorij.
Nato le poženete program v `PegSolitaireValidation.java` in v konzolo boste dobili poročilo posameznega testa, vključno s časom izvajanja in morebitnimi napakami.

Teste lahko tudi prilagodite oz. napišete nove.
V direktoriju `tests/public` popravite ali dodajte novo `.txt` datoteko, kjer je v 1. vrstici številka (bit), ali je test rešljiv, v 2. vrstici začetno stanje, v 3. vrstici pa končno stanje plošče.
Program `PegSolitaireValidation.java` bo samodejno sprožil teste za vse datoteke v navedenem direktoriju.

### Oddaja rešitve

Rešitev oddajte v nov GitHub repozitorij.
Povezavo do GitHub repozitorija pošljite na email [jobfair@medius.si](mailto:jobfair@medius.si).

Rok za oddajo rešitev je **nedelja, 9.3.2025**.

Vašo rešitev bomo preverili še z dodatnimi testi in jo primerjali z ostalimi.
V primeru uspešnega reševanja Vas bomo povabili na razgovor.

### Želimo vam obilo zabave in sreče pri implementaciji rešitve :)

----

Welcome to the Medius programming challenge JobFair 2025.

This year the challenge is implementing the solver for a game [Peg Solitaire](https://en.wikipedia.org/wiki/Peg_solitaire), which you already had a chance to try in our booth.

### Peg Solitaire

In this version of the game, we use an English style board in the cross shape with 7 rows and 7 columns with 33 fields in total.
Below is a visualization of valid fields that are marked with 'o'

```
    o o o
    o o o
o o o o o o o
o o o o o o o
o o o o o o o
    o o o
    o o o
```

The game uses 32 pegs, that are set up in an initial position, so one field stays empty.
An example of the initial board state is:

```
    x x x
    x x x
x x x x x x x
x x x o x x x
x x x x x x x
    x x x
    x x x
```

where 'x' marks a field with a peg and 'o' a field without a peg.

Game is played until only 1 peg remains on the board.
Each turn we can move a peg over another peg to an empty field orthogonally.
We can then remove the jumped peg from the board.

Examples of valid moves:
```
x    o      o    x  
x -> o      x -> o      x x o -> o o x      o x x -> x o o      
o    x      x    o
```

### Solver

Your task is to implement a solver for the game Peg Solitaire in Java programming language.
The solver should in the quickest possible time find the shortest solution.
You are allowed to use any library inside `java` package, **other libraries are not allowed**.

The initial position of the board is provided in a form of a 49 bit number, which represents the sequence of the fields on the board.
Value '0' represents an empty field while value '1' represent a field with a peg on it.

Example: A decimal number 124141717933596 equals to `11100001110011111111110111111111100111000011100` in binary format.
If we append 2 zeros to the beginning to get a 49 bit number, we get `0011100001110011111111110111111111100111000011100`.
We can put every 7 numbers in its own row and we get:
```
0011100
0011100
1111111
1110111
1111111
0011100
0011100
```

which represent the basic initial state of the board.

The input to the program is an initial and desired final state of the board in the form of a number in datatype `long`.
Your task is to return the sequence of the board state at every move, including the initial and final state of the board.

Implement your solution in a new `.java` file in which your main class must implement the `IPegSolitaireSolver` interface.
Inside the class implement methods `solve` and `personalData`.
Method `solve` accepts 2 numbers as input parameters, representing initial and final state of the board, and expects an array of `long` numbers that represent the state of the board after every move.
If the desired board state is not reachable from the provided initial state, the solver must return an empty array.
Method `personalData` is used to input your name and surname, each in its own string.

An example of a simple solver is available in `/solver/BasicSolver.java` where at every move we select a valid one at random.
A template for your solver is available at `/solver/TemplateSolver.java`.

### Advice

Be careful about:
- number, that represents the board state, also contains invalid fields in each corner, take care of the scenarios, where a peg isn't moved to those invalid fields
- it can happen, that the desired board state is not reachable given the initial state of the board, implement your solver, so it can, in case of no solution, return an empty array and terminates

### Testing

You can test your solution in `PegSolitaireValidation.java`.
Simply write the name of your solver class and java fila in the variable `solverFileName`, put the file in the `solver` directory and run the program.
The program will validate every move and display the time it took your solver to give a solution, including possible information about move validation.

Tests can be adjusted/added.
Simply edit or add a `.txt` file in the `tests/public` directory, where in the first row there is a number (bit) representing whether the test is solvable, in the second row number represent the initial board state and the third row contains final state of the board.
The program `PegSolitaireValidation.java` will automatically run all the tests in the directory.

### Solution submission

Implement and submit your solution to a new GitHub repository.
Include the link to that repository in an email to [jobfair@medius.si](mailto:jobfair@medius.si).

The submission deadline is on **Sunday, 9th March 2025**.

We will test your solution with additional tests and compare it to other solutions.
In case of successful solution we will invite you for a job interview.

### We wish you lots of fun and good luck while implementing your solution :)