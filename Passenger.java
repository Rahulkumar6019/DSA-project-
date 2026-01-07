public class Passenger {
    String name;
    int age;
    String passport;
    Passenger next;

    public Passenger(String name, int age, String passport) {
        this.name = name;
        this.age = age;
        this.passport = passport;
        this.next = null;
    }
}
