public class Iris
{
    double[] num;
    String name;
    int group;

    public Iris(double[] num, String name) {
        this.num = num;
        this.name = name;
    }

    public double calculate(double[] center) {
        double counter = 0;
        for(int i=0; i<center.length; i++) {
            counter += Math.pow((center[i] - num[i]),2);
        }
        return counter;
    }

    public String getName() {
        return name;
    }

    public double[] getNum() {
        return num;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
    
}