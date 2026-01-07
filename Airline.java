import java.util.Arrays;

public class Airline {
    Flight head, tail;
    int flightCount = 0;

    public void addFlight(String no, String dest) {
        Flight f = new Flight(no, dest);
        if (head == null) {
            head = tail = f;
        } else {
            tail.next = f;
            f.prev = tail;
            tail = f;
        }
        flightCount++;
    }


     // Sorts flights using the Insertion Sort Algorithm.

    public void sortFlightsByNumber() {
        if (flightCount < 2) return;

        // Convert to array for sorting
        Flight[] arr = toArray();

        // Insertion Sort Logic

        for (int i = 1; i < arr.length; i++) {
            Flight key = arr[i];
            int j = i - 1;

            // Move elements of arr[0..i-1] that are greater than key.flightNo
            // to one position ahead of their current position
            while (j >= 0 && arr[j].flightNo.compareToIgnoreCase(key.flightNo) > 0) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }

        // Re-link the Doubly Linked List pointers after sorting
        head = arr[0];
        head.prev = null;
        for (int i = 0; i < arr.length - 1; i++) {
            arr[i].next = arr[i + 1];
            arr[i + 1].prev = arr[i];
        }
        tail = arr[arr.length - 1];
        tail.next = null;
    }

    public Flight findFlight(String no) {
        Flight temp = head;
        while (temp != null) {
            if (temp.flightNo.equalsIgnoreCase(no)) return temp;
            temp = temp.next;
        }
        return null;
    }

    public Flight[] toArray() {
        Flight[] arr = new Flight[flightCount];
        Flight temp = head;
        for (int i = 0; i < flightCount; i++) {
            arr[i] = temp;
            temp = temp.next;
        }
        return arr;
    }
}