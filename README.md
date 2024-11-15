# Tema-TPM

# Exercitul 1
Secventa este linearizabila si putem demonstra prin aceasta ordonare

1. Pasul 1
- Thread-ul B face apelul de write(1) -> r=1
- Thread-ul A Citeste r=1
2. Pasul 2
- Thread-ul A Citeste din nou r=1 
- Thread-ul C face apelul de write(2) -> r=2
- Thread-ul B Citeste r=2
3. Pasul 3
- Thread-ul B Citeste r=2
- Thread-ul C face apelul de write(1) -> r=1
- Thread-ul A Citeste r=1

Aceasta secventa respecta executia in timp a metodelor din fiecare thread deci este Liniarizabila.


* A: 3 apeluri r.read().1
* B: r.write(1), r.read():2, r.read():2
* C: r.write (2) r.write(2)

Consideram secventa de executie:

 B_r.write(1) -> A_r.read():1 -> A_r.read():1 -> A_r.read():1 -> C_r.write(2) -> B_r.read():2  -> C_r.write(2) -> B_r.read():2

Pentru aceasta executie, secventa data este Consistent secventiala deoarece am respectat ordinea de executie a metodelor din fiecare thread.

# Exercitiul 2
Se prefera sa pui lock() inainte de try deoarece este mai sigur. Daca lock() arunca o exceptie (sa zicem ca nu reuseste sa obtina lock-ul), codul din try nu va mai rula deloc conform documentatiei Java, si astfel nu trebuie sa ne facem griji ca unlock() va fi apelat din greseala. In schimb, daca lock() e in try si se obtine o eroare, se va ajunge in finally, unde unlock() va incerca sa elibereze un lock care n-a fost obtinut, ceea ce poate cauza probleme.

# Exercitiul 3
## Explicatie
Algoritmul Bakery asigura o ordine corecta de acces pentru fiecare thread in sectiunea critica.
Indicele (de exemplu "i") reprezinta un id al thread-ului, iar label pe post de eticheta. Comparatia tuplelor poate fi rescrisa astfel:
if label[i] > label[k] or (label[i] == label[k] and i > k). Eticheta label[i] este utilizata pentru a reprezenta ordinea de sosire a fiecarui thread. Cand un thread vrea sa intre in sectiunea critica, isi seteaza label[i] la max(label[0], ..., label[n-1]) + 1. Totusi, este posibil ca doua thread-uri sa obtina aceeasi valoare. Astfel, daca am compara doar etichetele (label-urile) doua thread-uri cu aceiasi eticheta nu are avea o metoda de a decide cine are prioritate, de aceea algoritmul Bakery compara si indicele lor, bazandu-se pe o ordonare lexicografica, pentru a stabili cine are acces la sectiunea critica.

## Exemplu
Fie 2 thread-uri T1 T2.
- T1 atribuie flag[0] = true si label[0] = max(label[0], label[1]) + 1, deci atribuie label[0] = 1
- T2 in acelasi timp atribuie flag[1] = true si citeste valorile curente label[1] = max(label[0], label[1]) + 1, deci atribuie label[1] = 1, astfel T1 si T2 obtin aceiasi eticheta in mod concurent.

# Exercitiul 4
## Punctul a
 Un algoritm este deadlock-free daca, in orice moment, cel putin un thread isi poate continua executia si nu ramane blocat permanent.
Avem 3 thread-uri T1, T2 si T3 care incearca sa obtina lock-ul:
1. T1 intra in prima bucla si seteaza turn = me
2. T1 intra in al doilea loop si verifica used. Avand in vedere ca used este false (fiind primul thread), il va seta pe true si apoi iese din prima bucla turn-ul fiind T1.
3. T2 intra in prima bucla si seteaza turn = me
4. T3 intra in prima bucla si seteaza turn = me
5. T2 intra in al doilea loop si verifica used. Avand in vedere ca used este true (setat de T1), T2 va ramane blocat in bucla.
6. T3 intra in al doilea loop si verifica used. Avand in vedere ca used este true (setat de T2), T3 va ramane blocat in bucla.
7. T1 da release la lock si used va deveni false.
8. T2 iese din al doilea loop si va seta din nou used ca si true, dar va ramane blocat in primul loop deoarece turn = T3.
9. T3 va ramane in al doilea loop blocat deoarece used este true setat de T2 acum.

Astfel, T2 si T3 vor ramane blocate permanent, deci algoritmul nu este deadlock-free.

![image](https://github.com/user-attachments/assets/055baa28-5ecd-4c5a-9ffe-0227a185ab4b)

## Punctul b
Un algoritm este fair daca niciun thread nu poate accesa o sectiune critica protejata de lock mai des ca altele.
Avem 3 thread-uri T1, T2 si T3 care incearca sa acceseze sectiunea critica:
1. T1 incepe sa obtina lock-ul:
- x = 1, y = 0.
- T1 seteaza y = 1 si intra in sectiunea critica.
2. T2 incearca sa obtina lock-ul:
- T2 setează x = 2.
- Asteapta in bucla while (y != 0) deoarece y = 1.
3. T3 incearca sa obtina lock-ul:
- T3 seteaza x = 3.
- Asteapta in bucla while (y != 0).
4. T1 elibereaza lock-ul:
- T1 seteaza y = 0.
- Atat T2, cat si T3 pot concura pentru sectiunea critica.
5. T3 reuseste sa acceseze sectiunea critica (fara fairness):
- T3 seteaza y = 3 si intra in sectiunea critica, desi T2 astepta de mai mult timp.
- T2 continua sa astepte in bucla while (y != 0).
6. T1 incearca sa obtina lock-ul din nou:
- T1 seteaza x = 1.
- Asteapta in bucla while (y != 0).
7. T3 elibereaza lock-ul:
- T3 seteaza y = 0.
- Atat T1, cat si T2 pot concura pentru sectiunea critica.
8. T1 reuseste sa acceseze sectiunea critica (fara fairness):
- T1 seteaza y = 1 si intra in sectiunea critica, desi T2 astepta de mai mult timp.
- T2 continua sa astepte in bucla while (y != 0).

  ![image](https://github.com/user-attachments/assets/de214134-85b7-4579-842c-9d110cabe84c)

## Punctul c
Un trace posibil de executie ar fi:
1. Thread-ul T1 apeleaza primul metoda si atribuie noua valoare variabilei last.
2. Thread-ul T2 apeleaza metoda si atribuie noua valoare variabilei last.
3. Atat thread-urile T1 cat si T2 vor incerca atribuirea ```getWhite = true;``` insa doar unul dintre ele va reusi acest lucru(T1 fiind primul va reusi acest lucru, apoi T2 va fi
nevoit sa se intoarca si sa aleaga white).
4. In acest caz T1 va alege negru deoarece valoarea variabilei last a fost schimbata, dar daca un alt thread nu apuca sa schimbe valoarea variabilei last atunci ar fi ales rosu.

Deci, datorita faptului ca atribuirea ```getWhite = true;``` poate fi facuta de un singur thread, doar acesta va putea alege culoarea rosu, iar restul vor fi nevoite sa aleaga alb. 

![image](https://github.com/user-attachments/assets/d4d241d9-d4c9-4a31-9b1c-140a871124e8)

# Exercitiul 5
![image](https://github.com/user-attachments/assets/5d1b937f-a8b1-4d5b-a80c-7e83d196f5e4)

