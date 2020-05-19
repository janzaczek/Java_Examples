import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    static int k;
    static int counter = 1;

    public static void main(String[] args) throws FileNotFoundException {
        List<Iris> lista = loadData("iris_training.txt");
        System.out.print("Wprowadź k: ");
        Scanner s = new Scanner(System.in);
        k = s.nextInt();
        double[][] centroid;
        centroid = new double[k][lista.get(0).getNum().length];
        createCentroids(lista,centroid);
        kMeans(lista,centroid);
    }

    public static List<Iris> loadData(String string) throws FileNotFoundException {
        List<Iris> lista = new ArrayList<>();
        File file = new File(string);
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()){
            String[] tab = scanner.nextLine().replaceAll(",",".").trim().split("\\s+");
            double[] zmienna = new double[tab.length-1];
            for (int i = 0; i < tab.length - 1; i++) {
                zmienna[i] = Double.parseDouble(tab[i]);
            }
            String name = tab[tab.length-1];
            Iris iris = new Iris(zmienna,name);
            lista.add(iris);
        }
        return lista;
    }

    public static void createCentroids(List<Iris> lista, double[][] centro) {
        for(int i=0; i<k; i++) {
            centro[i] = lista.get((int) (Math.random() * lista.size())).getNum();
        }
        if(centro[0]==centro[1]||centro[1]==centro[2]||centro[0]==centro[2])
            createCentroids(lista,centro);
    }

    public static void entropy(List<Iris> lista, int grupa) {
        double e = 0.0;
        int currentG = 0;
        double a = 0, b=0, c=0;

        for(Iris iris : lista) {
            if(iris.getGroup()==grupa)
                currentG++;
            if(iris.getName().equals("Iris-setosa")&&iris.getGroup()==grupa)
                a++;
            else if(iris.getName().equals("Iris-versicolor")&&iris.getGroup()==grupa)
                b++;
            else
                c++;
        }

        List<Double> omega = new ArrayList<>(Arrays.asList(a, b, c));
        int alfa = grupa+1;
        System.out.print("Group: " + alfa);
        if(a==0 && b==0 && c==0 && currentG==0)
            System.err.println(" Blank space");
        else {
            for(int i=0; i<omega.size(); i++)
                omega.set(i,omega.get(i) / currentG);
            for (Double aDouble : omega) {
                if (aDouble != 0 && aDouble != 1)
                    e += log(aDouble, aDouble);
            }
            if (e != 0)
                e *= -1;
            System.out.println("\tEntropy: " + e);
        }
    }

    public static double log(double x, double y) {
        return (Math.log(x)/Math.log(2)) * y;
    }

    public static void kMeans(List<Iris> lista,double[][] centro) {
        while (true) {
            // przypisanie grup
            for(int j=0; j<lista.size(); j++){
                List<Double> zmienna = new ArrayList<>();
                for(int i=0; i<k; i++){
                    zmienna.add(lista.get(j).calculate(centro[i]));
                }
                double kai = Collections.min(zmienna);
                lista.get(j).setGroup(zmienna.indexOf(kai));
            }
            // nowe centroidy = zapisuje sobie starą wartość i tworzymy nową pusta tablice centroidow
            // suma = tablica wielkosci k zliczająca ilosc w danej grp
            double[][] prev = centro;
            centro = new double[k][lista.get(lista.size()-1).getNum().length];
            int[] suma = new int[k];

            for(Iris iris : lista) {
                for (int i = 0; i < iris.getNum().length; i++)
                    centro[iris.getGroup()][i] += iris.getNum()[i]; // sumuje poszczegolne wartsci
                suma[iris.getGroup()]++; //
            }
            for(int i=0; i<centro.length; i++) {
                for (int j = 0; j < centro[i].length; j++)
                    if(suma[i]==0)
                        centro[i][j]=0;
                    else
                        centro[i][j] = centro[i][j]/suma[i];
            }
            //https://www.geeksforgeeks.org/compare-two-arrays-java/
            if(Arrays.deepEquals(prev,centro))
                break;

            // suma odległości
            double[] zmienna = new double[k];
            for(Iris iris : lista)
                zmienna[iris.getGroup()]+=iris.calculate(centro[iris.getGroup()]);

            System.out.println("ITERATION: " + counter);
            counter++;
            for(int i=0; i<k; i++) {
                int alfa = i+1;
                System.out.println("Group: " + alfa + " distance from center: " + zmienna[i]);
            }
            System.out.println("==================================");
        }

        // wyswietlanie odpowiedzi
        List<List<Iris>> res = new ArrayList<>();
        for(int i=0; i<k; i++){
            List<Iris> alfa = new ArrayList<>();
            for(Iris iris : lista){
                if(iris.getGroup()==i)
                    alfa.add(iris);
            }
            res.add(alfa);
        }

        for(int j=0; j<res.size(); j++) {
            int setosa = 0, virgi = 0, versi = 0;
            for (int i = 0; i < res.get(j).size(); i++) {
                if (res.get(j).get(i).getName().equals("Iris-setosa"))
                    setosa++;
                else if (res.get(j).get(i).getName().equals("Iris-virginica"))
                    virgi++;
                else
                    versi++;
            }
            int suma = setosa + virgi + versi;
            int alfa = j+1;
            System.out.println("-------- Group " + alfa + " --------");
            System.out.println("\tSetosa: " + "amount = " + setosa);
            System.out.println("\t\t% of setosa = " + ((double) setosa / (double) suma)*100);
            System.out.println("\tVirginica: " + "amount = " + virgi);
            System.out.println("\t\t% of virginica = " + ((double) virgi / (double) suma)*100);
            System.out.println("\tVersicolor: " + "amount = " + versi);
            System.out.println("\t\t% versicolor = " + ((double) versi / (double) suma)*100);
            System.out.println("---ENTROPY---");
            System.out.print("\t");
            entropy(res.get(j),j);
        }
        System.out.println();
    }
}