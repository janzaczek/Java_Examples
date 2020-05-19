import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class Maluj extends JPanel implements MouseListener {

    boolean zmienna = true;

    public Maluj() {
        setFocusable(zmienna);
        requestFocus();
        setBackground(Color.WHITE);
        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Main.odswiez(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (Main.ok) {
            if (Main.tura && !Main.komunikacja && !Main.wygrana && !Main.przegrana) {
                int alfa = e.getX() / Main.odstep;
                int beta = e.getY() / Main.odstep;
                beta *= 3;
                int position = alfa + beta;

                if (Main.tab[position] == null) {
                    if (!Main.kolko)
                        Main.tab[position] = "X";
                    else
                        Main.tab[position] = "O";
                    Main.tura = false;
                    repaint();
                    Toolkit.getDefaultToolkit().sync();

                    try {
                        Main.dos.writeInt(position);
                        Main.dos.flush();
                    } catch (IOException e1) {
                        Main.bledy++;
                        e1.printStackTrace();
                    }

                    System.out.println("To się powinno wypisać jeżeli przesłano dane!");
                    Main.sprawdzWygrana();
                    Main.sprawdzRemis();

                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}