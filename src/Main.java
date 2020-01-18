import java.util.ArrayDeque;
import java.util.Queue;

public class Main {

    // ruch na 4 kierunki swiata
    static int[] poziomo = {-1, 1, 0, 0}; // lewo i prawo
    static int[] pionowo = {0, 0, -1, 1};   // dol i gora
    static int p1,p2,k1,k2;
    static Punkt start,koniec;

    static boolean czyMiesciSieWMapie(Punkt p){
        return (p.x >= 0) && (p.x < 10) && (p.y >= 0) && (p.y < 10);
    }

    static boolean czyMozePrzejscNaDanePole(Object[][] tab, int[][] odwiedzone, Punkt p){
        return (tab[p.x][p.y].equals(1) || tab[p.x][p.y].equals('K')) && odwiedzone[p.x][p.y] != 3; // == tutaj moze dowolna wartosc byleby rozna od 0,1
    }

    static void findPath(Object[][] tab, Punkt start, Punkt koniec)
    {
        // bedziemy tutaj sledzic odwiedzone msc
        int[][] odwiedzone = new int[10][10];	// to moze byc dowolna tablica np string,char itp.

        Queue<Kolejka> queue = new ArrayDeque<>();  // taki LinkedList tylko szybszy

        // zaznaczam komorke punkt start jako odwiedzony i dodaje go do kolejki
        // wazne - Punkt startowy nie ma poprzednika dlatego dajemy null - (pomoze nam to przy wypisywaniu)
        odwiedzone[start.x][start.y] = 3;
        queue.add(new Kolejka(new Punkt(start.x,start.y), null));

        Kolejka kolejka = null;
        // dopoki nie znajdziemy naszego wezla
        // ew. mozemy dac while true
        while (!queue.isEmpty()) {
            System.out.print(queue.peek() + "->");
            // usuwam poprzedni element z kolejki i przetwarzam go
            //metoda poll zwraca i usuwa element pierwszy, jezeli jest kolejka jest pusta to zwraca null
            kolejka = queue.poll();

            // kolejka nie powinna byc nullem, jezeli bedzie program rzuci mi błąd AssertionError i będę wiedział w którym miejscu wystąpił błąd
            // tip - nie probwac wyłapać tego błądu - on jest właśnie po to żeby pokazać nam w kótrym msc program się krzaczy
            assert kolejka != null : "kolejka jest pusta";
            start.y = kolejka.p.y;
            start.x = kolejka.p.x;

            // jezeli dojedziemy do naszego destination to przerywamy
            if (start.x == koniec.x && start.y == koniec.y)
                break;

            // sprawdzam dla wszystkich 4 kierunkow swiata
            // oraz kolejkuje kazdy mozliwy ruch
            for (int v = 0; v < 4; v++) // idzie gora,dol,lewo,prawo - w tej kolejnosci dodaje
            {
                // sprawdzam czy mozliwe isc na te pozycje w petli tzn gora, dol, lewo, prawo
                if(czyMiesciSieWMapie(new Punkt(start.x + poziomo[v], start.y + pionowo[v]))&&
                        czyMozePrzejscNaDanePole(tab, odwiedzone, new Punkt(start.x + poziomo[v], start.y + pionowo[v])))
                {
                    queue.add(new Kolejka(new Punkt(start.x + poziomo[v], start.y + pionowo[v]), kolejka));
                    // zaznaczam komorke jako odwiedzona
                    odwiedzone[start.x + poziomo[v]][start.y + pionowo[v]] = 3;
                }
            }
        }

        System.out.println();
        // jezeli dotarlismy do konca to wypiszemy sciezke, jesli nie to odpowiedni komunikat
        if (start.x == koniec.x && start.y == koniec.y) {
            System.out.print("Istnieje droga: ");
            pokazTrase(kolejka);
        }
        else
            System.out.println("Nie ma drogi do celu");
    }

    static void pokazTrase(Kolejka kolejka) {
        // sprawdzam rekurencyjnie dopoki jest poprzednik i wyswietlam
        // nie moge w petli gdyz klasa Kolejka nie ma wlasciwosci kolekcji
        if (kolejka == null) {
//            System.err.println("Node == null");
            return;
        }
        pokazTrase(kolejka.poprzednik);
        if(kolejka.p.x==k1&&kolejka.p.y==k2)
            System.out.print(kolejka);
        else{
            System.out.print(kolejka + ",");
        }
    }


    public static void main(String[] args)
    {
        Object[][] tab = new Object[10][10];
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[i].length; j++) {
                tab[i][j] = ((int) (Math.random() * 2));
            }
        }
        p1 = (int)(Math.random()*9);    //S.x
        p2 = (int)(Math.random()*9);    //S.y
        k1 = (int)(Math.random()*9);    //K.x
        k2 = (int)(Math.random()*9);    //K.y

        start = new Punkt(p1,p2);
        koniec = new Punkt(k1,k2);

        while(start.x == koniec.x && start.y == koniec.y){
            int x = (int)(Math.random()*9);
            int y = (int)(Math.random()*9);
            start = new Punkt(x,y);
        }

        tab[start.x][start.y] = 'S';
        tab[koniec.x][koniec.y] = 'K';


        for (Object[] objects : tab) {
            for (Object object : objects) {
                System.out.print(object + " ");
            }
            System.out.println();
        }
        System.out.println("Start: " + start  + ", Koniec: " + koniec);
        findPath(tab,start,koniec);
    }
}