import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;



public class Main extends Thread {

    static int counter = 3;
    static boolean alfa = false;
    static boolean beta = false;
    boolean petla = true;

    static String gamma = "Czy się udało ?";

    static String host = "localhost";       // nazwa hosta
    static int port = 55555;                // numer portu
    static Scanner scanner = new Scanner(System.in);    // scanner do czytania nazw i numerów

    static JFrame frame;
    static Thread thread;

    static int szerokosc = 510;
    static int wysokosc = 530;

    static DataOutputStream dos;
    static DataInputStream dis;
    static Socket socket;

    static Maluj maluj;     // klasa do obsługi i malowania

    static ServerSocket serverSocket;

    // tutaj są obrazki do GUI jakie nam się wyświetli
    static BufferedImage redCircle;
    static BufferedImage blueCircle;
    static BufferedImage board;
    static BufferedImage redX;
    static BufferedImage blueX;

    // tablica 3x3 do kółka i krzyżyk
    static String[] tab = new String[9];

    static int miejsceJeden = -1;
    static int miejsceDwa = -1;

    // zmienne wpływające na gre ( czy połączono, czy wygrana/przegrana itp... )
    static boolean komunikacja = false;
    static boolean wygrana = false;
    static boolean przegrana = false;
    static boolean tura = false;
    static boolean kolko = true;
    static boolean ok = false;
    static boolean remis = false;

    static int odstep = 160;
    static int bledy = 0;

    static String napisZwyciesto = "Wygrałeś!";
    static String napisPrzegrana = "Przeciwnik wygrał!";
    static String napisRemis = "Gra skończona - remis";

    static Font f1 = new Font("Rzodkiew", Font.ROMAN_BASELINE, 8);
    static Font f2 = new Font("Rzodkiew", Font.ROMAN_BASELINE, 14);
    static Font f3 = new Font("Rzodkiew", Font.ROMAN_BASELINE, 16);

    static String czekam = "Czekam na drugiego gracza";
    static String polaczenie = "Nie moge połączyć się z przeciwnikiem";


    static int[][] zwyciestwa;


    public Main() {
        System.out.println("Podaj nazwe hosta: ");
        host = scanner.nextLine();
        System.out.println("Podaj numer portu: ");
        port = scanner.nextInt();

        while (port < 1 || port > 65535) {
            System.out.println("Zły port. Podaj inny numer portu! ");
            port = scanner.nextInt();
        }
        //deklarujemy jakie konfiguracje w kółko i krzyżyk są zwycięzkie
        zwyciestwa = new int[][] {{ 1, 4, 7 }, { 0, 1, 2 }, { 3, 4, 5 }, { 2, 5, 8 }, { 6, 7, 8 }, { 0, 4, 8 }, { 2, 4, 6 }, { 0, 3, 6 }};

        zaladujObrazki();

        maluj = new Maluj();
        maluj.setPreferredSize(new Dimension(szerokosc, wysokosc));

        if (!polacz())
            sprawdzSerwer();

        // ustawienia naszego okna gry - nie można rozszerzać, ustawiamy wysokosc i szerokosc....
        frame = new JFrame();
        frame.setTitle("Kółko i Krzyżyk");
        frame.setContentPane(maluj);
        frame.setSize(szerokosc, wysokosc);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        thread = new Thread(this, "KółkoiKrzyżyk");
        thread.start();
    }


    public static void main(String[] args) {
        Main kik = new Main();
    }


    @Override
    public void run() {
        while (petla) {
            zobacz();
            maluj.repaint();
            if (!kolko && !ok)
                nasluchuj();
        }
    }

    public static void odswiez(Graphics g) {
        g.drawImage(board, 0, 0, null);
        if (komunikacja) {
            g.setFont(f1);
            g.setColor(Color.BLACK);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int stringWidth = g2.getFontMetrics().stringWidth(polaczenie);
            g.drawString(polaczenie, szerokosc / 2 - stringWidth / 2, wysokosc / 2);
            return;
        }

        if (ok) {
            for (int i = 0; i < tab.length; i++) {
                if (tab[i] != null) {
                    if (tab[i].equals("X")) {
                        if (kolko)
                            g.drawImage(redX, (i % 3) * odstep + 10 * (i % 3), (int) (i / 3) * odstep + 10 * (int) (i / 3), null);
                         else
                            g.drawImage(blueX, (i % 3) * odstep + 10 * (i % 3), (int) (i / 3) * odstep + 10 * (int) (i / 3), null);
                    } else if (tab[i].equals("O")) {
                        if (kolko)
                            g.drawImage(blueCircle, (i % 3) * odstep + 10 * (i % 3), (int) (i / 3) * odstep + 10 * (int) (i / 3), null);
                        else
                            g.drawImage(redCircle, (i % 3) * odstep + 10 * (i % 3), (int) (i / 3) * odstep + 10 * (int) (i / 3), null);
                    }
                }
            }
            if (wygrana || przegrana) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(10));
//                g.setColor(Color.BLACK);

                g.setFont(f3);
                g.setColor(Color.BLACK);
                if (wygrana) {
                    int stringWidth = g2.getFontMetrics().stringWidth(napisZwyciesto);
                    g.drawString(napisZwyciesto, szerokosc / 2 - stringWidth / 2, wysokosc / 2);
                } else if (przegrana) {
                    int stringWidth = g2.getFontMetrics().stringWidth(napisPrzegrana);
                    g.drawString(napisPrzegrana, szerokosc / 2 - stringWidth / 2, wysokosc / 2);
                }
            }
            if (remis) {
                Graphics2D g2 = (Graphics2D) g;
                g.setFont(f3);
                g.setColor(Color.BLACK);
                int stringWidth = g2.getFontMetrics().stringWidth(napisRemis);
                g.drawString(napisRemis, szerokosc / 2 - stringWidth / 2, wysokosc / 2);
            }
        } else {
            g.setFont(f2);
            g.setColor(Color.BLACK);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int stringWidth = g2.getFontMetrics().stringWidth(czekam);
            g.drawString(czekam, szerokosc / 2 - stringWidth / 2, wysokosc / 2);
        }

    }

    // tutaj następuje nasłuchiwanie na klienta
    public static void nasluchuj() {
        Socket socket;
        try {
            socket = serverSocket.accept();
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            ok = true;
            System.out.println("Client się połączył");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // tutaj następuje połączenie się z klientem
    public static boolean polacz() {
        try {
            socket = new Socket(host, port);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            ok = true;
        } catch (IOException e) {
            System.out.println("Nie można się połączyć z adresem: " + host + ":" + port);
            return false;
        }
        System.out.println("Udało się połączyć z serwerem");
        return true;
    }

    public static void sprawdzSerwer() {
        try {
            serverSocket = new ServerSocket(port, 8, InetAddress.getByName(host));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tura = true;
        kolko = false;
    }

    static void sprawdz(){
        alfa = true;
        beta = true;
        gamma = "Tak";
        counter = 1;
    }

    public static void zobacz() {
        if (bledy >= 10)
            komunikacja = true;

        if (!tura && !komunikacja) {
            try {
                int space = dis.readInt();
                if (kolko) tab[space] = "X";
                else tab[space] = "O";
                sprawdzPrzegrana();
                sprawdzRemis();
                tura = true;
            } catch (IOException e) {
                e.printStackTrace();
                bledy++;
            }
        }
    }

    public static void sprawdzWygrana() {
        for (int i = 0; i < zwyciestwa.length; i++) {
            if (kolko) {
                if (tab[zwyciestwa[i][0]] == "O" && tab[zwyciestwa[i][1]] == "O" && tab[zwyciestwa[i][2]] == "O") {
                    miejsceJeden = zwyciestwa[i][0];
                    miejsceDwa = zwyciestwa[i][2];
                    wygrana = true;
                }
            } else {
                if (tab[zwyciestwa[i][0]] == "X" && tab[zwyciestwa[i][1]] == "X" && tab[zwyciestwa[i][2]] == "X") {
                    miejsceJeden = zwyciestwa[i][0];
                    miejsceDwa = zwyciestwa[i][2];
                    wygrana = true;
                }
            }
        }
    }

    public static void sprawdzPrzegrana() {
        for (int i = 0; i < zwyciestwa.length; i++) {
            if (kolko) {
                if (tab[zwyciestwa[i][0]] == "X" && tab[zwyciestwa[i][1]] == "X" && tab[zwyciestwa[i][2]] == "X") {
                    miejsceJeden = zwyciestwa[i][0];
                    miejsceDwa = zwyciestwa[i][2];
                    przegrana = true;
                }
            } else {
                if (tab[zwyciestwa[i][0]] == "O" && tab[zwyciestwa[i][1]] == "O" && tab[zwyciestwa[i][2]] == "O") {
                    miejsceJeden = zwyciestwa[i][0];
                    miejsceDwa = zwyciestwa[i][2];
                    przegrana = true;
                }
            }
        }
    }

    public static void sprawdzRemis() {
        for (int i = 0; i < tab.length; i++)
            if (tab[i] == null)
                return;
        remis = true;
    }

    public void zaladujObrazki() {
        try {
            board = ImageIO.read(getClass().getResourceAsStream("/board.png"));
	    blueX = ImageIO.read(getClass().getResourceAsStream("/blueX.png"));
            redX = ImageIO.read(getClass().getResourceAsStream("/redX.png"));
	    blueCircle = ImageIO.read(getClass().getResourceAsStream("/blueCircle.png"));
            redCircle = ImageIO.read(getClass().getResourceAsStream("/redCircle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sprawdz();
    }
}