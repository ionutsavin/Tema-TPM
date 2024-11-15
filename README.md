# Tema-TPM

# Exercitul 1
Secventa este linearizabila si putem demonstra prin aceasta ordonare

Thread-ul B face apelul de write(1) -> r=1
Thread-ul A Citeste r=1
------------------------------------------
Thread-ul A Citeste din nou r=1
Thread-ul C face apelul de write(2) -> r=2
Thread-ul B Citeste r=2
------------------------------------------
Thread-ul B Citeste r=2
Thread-ul C face apelul de write(1) -> r=1
Thread-ul A Citeste r=1

Aceasta secventa respecta executia in timp a metodelor din fiecare thread deci este Liniarizabila.


A: 3 apeluri r.read().1
B: r.write(1), r.read():2, r.read():2
C: r.write (2) r.write(2)

Consideram secventa de executie:

 B_r.write(1) -> A_r.read():1 -> A_r.read():1 -> A_r.read():1 -> C_r.write(2) -> B_r.read():2  -> C_r.write(2) -> B_r.read():2

Pentru aceasta executie, secventa data este Consistent secventiala deoarece am respectat ordinea de executie a metodelor din fiecare thread.
