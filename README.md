# Chess-Engine

Implementare in Java a unui engine de sah, compatibil cu XBoard.

## Responsabili proiect:
	PERETE Rareș-Daniel
	STÎRCU Cătălin
	ZBARCEA Andrei

## Documentarea proiectului:

*** Pachetul 'chess' ***
MainClass.java
	Clasa principala a programului. Prezinta un obiect de tip 'Engine' ce
	reprezinta jucatorul AI de sah.

	Este prezenta un map input <-> functie ce decide functia ce se va executa
	asupra jucatorului (new -> new game, force -> force mode etc.)

	Se foloseste un loop infinit in main() ce asteapta sa primeasca o comanda
	de tipul celor antepuse sau o mutare. In orice caz, executia programului
	va continua cu functia corespunzatoare.

	Loop-ul infinit se opreste la intalnirea comenzii 'quit'

Engine.java
	Jucatorul propriu-zis. Sunt prezente campuri de il definesc: culoarea
	pieselor, modul de functionare - forced, si instanta curenta de joc
	asupra careia actioneaza

	makeMove()
		Metoda testeaza, inainte de toate, daca este randul jucatorului de a
		face o mutare. In caz afirmativ, se aplica algoritmul minimax
		(negamax + alphabeta) pentru a gasi o mutare cat mai buna.

	registerMove()
		Este apelata in momentul in care se primeste o mutare de la xboard.
		Aceasta este verificata si, in caz afirmativ, aplicata.

	alphaBeta()
		Algoritmul clasic de negamax + alphabeta. Daca sunt mai multe mutari
		cu exact aceeasi evaluare, se va alege una random dintre ele. Acest
		lucru previne executarea aceleiasi mutari in continuu intr-un caz
		(mai precis prima sau ultima din lista cu aceeasi evaluare)

GameInstance.java
	Un obiect de acest tip reprezinta o stare a jocului. Se cunoaste jucatorul
	care trebuie sa mute, tabla de joc, pionul susceptibil la en passant
	si coordonatele celor doi regi.

	newBoard()
		Populeaza tabla de joc cu piesele in formatia de baza, si pregateste
		tabla pentru joc

	print()
		Doar pentru debug -> printeaza tabla de joc

	relChange()
		O metoda simla pentru a calcula diferenta relativa a doua numere

	eval()
		Metoda ce ofera evaluarea starii

		In cazul in care din perspectiva jucatorului ce trebuie sa mute, acesta
		se afla in sah mat, este cea mai rea evaluare cu putinta. Daca
		adversarul se afla in sah mat, bineinteles este cea mai buna evaluare.

		Dupa aceste teste, se verifica toata tabla de joc dupa campuri
		precalculate de pseudo-generarea mutarilor.

		Prin generarea mutarilor posibile in stare se afla detalii importante
		despre anume coordonate precum controlul acestora. Se itereaza prin
		toate coordonatele pentru a se stabili scoruri bazate pe aceste detalii

		La final, in functie de aceste scoruri si valori fine-tuned din
		Values.java, se obtine evaluarea starii.

	ended()
		Verifica daca starea este considerata sfarsit de joc (sah mat)

	getMoves()
		Returneaza toate mutarile posibile pentru un jucator. Se itereaza
		prin fiecare piesa a acestuia si se genereaza fiecare mutare in
		modul aferent.

	applyMove()
		Actualizeaza tabla de joc cu privire la mutarea primita.

		Daca este o promovare de pion, se schimba pionul cu piesa dorita.

		Daca piesa mutata este un pion (si nu este promovare), se verifica daca
		acesta s-a mutat 2 pozitii in fata (in cazul in care este legal). In
		caz afirmativ, acest pion devine susceptibil la regula enPassant.
		Totodata, daca acest pion a capturat pe o coordonata la care nu se afla
		o piesa, inseamna ca s-a folosit de regula enPassant asupra altui pion.

		Daca piesa mutata este o tura, regele aferent pierde dreptul de rocada
		in directia ei

		Daca piesa este un rege, se testeaza daca mutarea dorita este o rocada.
		In caz afirmativ, se executa. In orice caz, cum regele s-a miscat,
		acesta nu mai poate efectua o rocada in nicio directie pe viitor.

	promotePawn()
		Creeaza o noua piesa folosita la promovarea unui pion

	validateMove()
		Primeste o mutare (se aplica asupra celor de la adversar) si se
		genereaza mutarile posibile cu ajutorul programului nostru. Daca
		mutarea primita se afla printre acestea, inseamna ca este legala
		(Programul genereaza mutari legale)

	checkFor()
		Verifica daca o anumita piesa se afla la anumite coordonate

	getKing()
		Returneaza regele jucatorului dorit

	kingInCheck()
		Verifica daca un rege se afla in sah (metoda definita in King.java)

	clone()
		Creeaza o copie fidela a starii

	pieceAt()
		Returneaza piesa de la o anumita coordonata

	addPiece()
		Creeaza o piesa la o anumita coordonata. Se foloseste in newBoard()


Values.java
	Valori fine-tuned ce servesc la evaluare


*** pieces ***
Piece.java
	Superclasa pieselor

	Sunt prezente metode ce calculeaza valoarea unei piese, in functie de
	valoarea ei de baza si de mutarile pe care le poate efectua, sau mutarile
	altor piese care pot fi efectuate asupra ei (increaseValue, addProtector
	decreaseValue)

	getMoves()
		Toate subclasele trebuie sa implementeze aceasta metoda in functie
		de piesa

	touchSquare()
		Aceasta metoda incearca sa aplice o mutare ce poate fi efectuata de
		aceasta piesa si se verifica coordonata la care aceasta ajunge,
		actualizandu-se campuri ce servesc la evaluare (precum controlul
		unei coordonate)

		Returneaza o pereche de valori boolene ce ajuta alte metode sa isi
		continue executia sau sa ia alte decizii (mai jos)

	getDiagMoves()
		Aceasta metoda porneste de la coordonata piesei in fiecare directie
		pana cand intalneste o alta piesa (exact felul de miscare al nebunului)

		Fiecare mutare posibila (inclusiv cele ce se termina pe o piesa
		de-a adversarului) se adauga intr-o lista

	getGridMoves()
		Similar lui getDiagMoves, doar ca genereaza mutarile pentru miscarea
		turei. Regina se foloseste de ambele metode.

	moveIsLegal()
		Aplica mutarea primita asupra unei clone a instantei curente si
		testeaza daca regele este in sah. In cazul in care nu este, inseamna
		ca mutarea este legala din acest punct de vedere.

	clone()
		Creeaza o copie fidela a piesei


Pentru fiecare piesa de mai jos, in momentul testarii unei pozitii prin care se
itereaza se testeaza daca este in tabla (daca nu a iesit) si daca ajunge pe o
alta piesa (conform lui touchSquare())

Pawn.java
	Clasa pion. Ea este identificata suplimentar printr-un camp hasMoved
	ce decide daca pionul poate avansa una sau doua pozitii

	getMoves()
		In functie de culoarea pionului, se decide directia de avansare.

		In functie de campul hasMoved, se testeaza prima sau primele 2 pozitii
		din directia acestuia de avansare.

		In cazul in care pozitia pe care se ajunge este capat de tabla, are
		loc o promovare. Se considera promovarea la Knight si la Queen.


		Ulterior, se testeaza pozitiile de atac ale pionului. Se verifica
		totodata si utilizarea regulii enPassant. Din nou, se verifica daca
		este posibila o promovare.


Knight.java
	getMoves()
		Se itereaza prin cele 8 pozitii pe care le poate ocupa un cal,
		validandu-se acestea.


Bishop.java
	getMoves()
		Se foloseste de getDiagMoves()


Rook.java
	getMoves()
		Se foloseste de getGridMoves()


Queen.java
	getMoves()
		Se foloseste de getDiagMoves() si getGridMoves()


King.java
	Sunt prezente campuri ce decid daca acest rege are drept de rocada intr-o
	anumita directie

	getMoves()
		Se itereaza prin pozitiile din jurul regelui, validandu-se

		Se verifica totodata posibilitatii de rocada, daca acest rege are drept
		si daca sunt indeplinite toate conditiile din regulament (in ambele
		directii)

	inCheck()
		Verifica, pornind de la rege, daca o piesa care il poate ataca intr-un
		anumit fel chiar il ataca (de exemplu, pentru un cal se verifica
		cele 8 pozitii din jurul regelui in care il poate ataca)


*** units ***
Move
	Un obiect ce reprezinta o mutare, de la o pozitie la alta

Promotion
	O subclasa a lui Move de fineste o promovare de pion la o anumita piesa
	(impreuna cu mutarea lui propriu-zisa)

Position
	O pozitie determinata de un rand si o coloana. Sunt prezente metode
	ce servesc la conversia string <-> pozitie (ambele sensuri)

Square
	O coordonata din tabla de sah, ce poate sau nu sa contina o piesa. Ea
	prezinta campuri ce servesc la evaluarea starii.