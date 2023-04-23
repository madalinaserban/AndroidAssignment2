## Android Assignment 2 Serban Elena Madalina

Aceasta este o aplicație Android pentru a seta și opri alarme. 
Aplicația utilizează o listă de alarme pentru a afișa toate alarmele setate și oferă utilizatorului posibilitatea de a adăuga noi alarme sau de a opri alarmele existente.
Utilizam un SurfaceView pentru a vizualiza ceasul sub forma de animatie.

# Funcționalități

Vizualizarea timpului curent sub forma de animatie. Avem 3 linii : pentru ore minute si secunde cu grosimi si lungimi diferite care se rotesc conform timpului curent.
Aplicația utilizează Runnable pentru a actualiza animația.
Adăugare alarme noi prin intermediul unui ecran de adăugare alarmă, care permite utilizatorului să introducă ora și minutul de declanșare a alarmei
Activarea și dezactivarea alarmelor prin intermediul unui comutator de activare/dezactivare, care schimbă starea alarmei și o actualizează în preferințele partajate
Salvarea tuturor alarmelor în Shared Preferences pentru a le păstra chiar și după închiderea aplicației
Pornirea alarmelor la ora și minutul setate, cu un sunet și o notificare
