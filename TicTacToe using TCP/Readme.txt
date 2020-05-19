Działanie programu:
Połączenie następuje na serverze o nazwie hosta "localhost" oraz porcie numer 55555. 
Po połączeniu się z serverem i wpisaniu poprawnych danych, powinno nam się wyświetlić 
okno rozgrywki. W oknie nie będziemy mogli nic zrobić dopóki nie połączy się 
'przecwinik'. Gra to pełnoprawne 'Kółko i krzyżyk', gdzie na oknach rozgrywki, będzie można 
śledzić nasze poczynania. GUI dla użytkownika1 oraz użytkownika2 różnią się kolorami
oraz końcowymi napisami gdzie dokładnie można zobaczyć który użytkownik wygrał.


Uruchomienie programu:
1) Rozpakować przesłany plik zip
2) Należy odpalić 2 konsole ( cmd w oknie wyszukiwania )
3) Wpisać w obu konsolach komende :
	'java -jar' - a następenie po spacji, przeciągnąć plik Projekt3.2.jar
		który znajedziemy w -> ( projekt -> folder 'out' -> 
			aritifacts -> Projekt3_2_jar -> Projekt3.2.jar )
4) Następnie zostaniemy poproszeni o wpisanie nazwe hosta oraz numer portu. Wpisać
następujące wartości: 'localhost', '55555'.
5) Kiedy wpiszemy odpowiednie wartości kliknąć enter. Nastąpi połączenie oraz
	wyświetlą nam się 2 okna rozgrywki gdzie będzie można rozpocząć rozgrywke.


Opis:
Aplikacja jednowątkowa działająca na serverze UDP.Jeżeli server połączy się 
poprawnie z klientem, to dzięki UDP server odpowiada na żądanie wolnym portem TCP.
Program analizuje na bieżąco jakie ruchy wykonał przeciwnik, wysyłając odpowiednie 
pakiety. Jeśli konfiguracja zwycięstwa zgadza się ze zwycieskimi konfiguracjami
( zadeklarowanymi w kodzie ) następuje wysłanie komunikatu z wiadomością który
użytkownik wygrał, oraz blokuje plansze do gry. Rozłączyć można się w każdej chwili
zamykając okno GUI (- rozwiązanie problemu LOGOUT).

