public class Flight {
    String flightNo, destination;
    Passenger pHead;
    Flight prev, next;

    public Flight(String flightNo, String destination) {
        this.flightNo = flightNo;
        this.destination = destination;
    }

    public void addPassenger(String name, int age, String passport) {
        Passenger p = new Passenger(name, age, passport);
        if (pHead == null) pHead = p;
        else {
            Passenger temp = pHead;
            while (temp.next != null) temp = temp.next;
            temp.next = p;
        }
    }

    public int getPassengerCount() {
        int count = 0;
        Passenger temp = pHead;
        while (temp != null) { count++; temp = temp.next; }
        return count;
    }

    public String getPassengerList() {
        StringBuilder sb = new StringBuilder();
        Passenger temp = pHead;
        if (temp == null) return "No passengers registered.";
        while (temp != null) {
            sb.append("- ").append(temp.name).append(" (Age: ").append(temp.age).append(", Passport: ").append(temp.passport).append(")\n");
            temp = temp.next;
        }
        return sb.toString();
    }
}