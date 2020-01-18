public class Kolejka {  // to jest tak naprawde kolejka punktów

    Punkt p;

    Kolejka poprzednik; // dzieki trzymaniu poprzednika bedzie mozna ladnie printowac sciezke

    public Kolejka(Punkt p, Kolejka poprzednik) {
        this.p = p;
        this.poprzednik = poprzednik;
    }

    @Override
    public String toString() {
        return "(" + p.x + "," + p.y + ')';
    }
};