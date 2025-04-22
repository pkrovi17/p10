import java.util.NoSuchElementException;

public class PriorityEvents extends Object{
    private Event[] completed;
    private int completedSize;
    private Event[] heapData;
    private int size;
    private static boolean sortAlphabetically;

    public PriorityEvents(int capacity) throws IllegalArgumentException {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must be non-negative");
        }
        this.completed = new Event[capacity * 2];
        this.completedSize = 0;
        this.heapData = new Event[capacity];
        this.size = 0;
    }
   // Constructor: uses heapify on an existing array
    public PriorityEvents(Event[] events, int size) throws IllegalArgumentException {
        if (events == null || size < 0 || size > events.length)
            throw new IllegalArgumentException("Invalid inputs");
        for (int i = 0; i < size; i++) {
            if (events[i] == null || events[i].isComplete())
                throw new IllegalArgumentException("Invalid or completed Event in array");
            }
        heapData = events.clone(); // Shallow copy, fine for now
        completed = new Event[size * 2];
        this.size = size;
        completedSize = 0;
        // Heapify process (bottom-up)
        for (int i = size / 2 - 1; i >= 0; i--) {
        percolateDown(i);
        }
    }

    public static boolean isSortedAlphabetically () {
        return sortAlphabetically;
    }

    public static void sortAlphabetically () {
        sortAlphabetically = true;
    }

    public static void sortChronologically () {
        sortAlphabetically = false;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public int numCompleted() {
        return completedSize;
    }

    public Event[] clearCompletedEvents() {
        Event[] result = new Event[completedSize];
        deepCopyHelper(result, 0);
        completedSize = 0;
        return result;
    }

    private void deepCopyHelper(Event[] result, int index) {
        if (index >= completedSize) return;
        Event original = completed[index];
        if (original != null) {
            result[index] = new Event(original.getDescription(), original.getDay(),
                                    original.getStartTimeAsString().contains("T") ? 
                                    Integer.parseInt(original.getStartTimeAsString().split("T")[1].split(":")[0]) : 0,
                                    original.getStartTimeAsString().contains("T") ? 
                                    Integer.parseInt(original.getStartTimeAsString().split("T")[1].split(":")[1]) : 0);
            if (original.isComplete()) result[index].markAsComplete();
        }
        completed[index] = null;
        deepCopyHelper(result, index + 1);
    }

    protected Event[] getCompletedEvents() {
        Event[] result = new Event[completedSize];
        copyCompletedHelper(result, 0);
        return result;
    }

    private void copyCompletedHelper(Event[] result, int index) {
        if (index >= completedSize) return;
        Event original = completed[index];
        if (original != null) {
            String[] timeParts = original.getStartTimeAsString().split("T")[1].split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int min = Integer.parseInt(timeParts[1]);
            result[index] = new Event(original.getDescription(), original.getDay(), hour, min);
            if (original.isComplete()) result[index].markAsComplete();
        }
        copyCompletedHelper(result, index + 1);
    }

    public Event peekNextEvent() {
        if (isEmpty()) {
            throw new NoSuchElementException("No events available");
        }
        return heapData[0];
    }

    protected Event[] getHeapData() {
        Event[] result = new Event[heapData.length];
        copyHeapHelper(result, 0);
        return result;
    }

    private void copyHeapHelper(Event[] result, int index) {
    if (index >= heapData.length) return;
    result[index] = heapData[index];
    copyHeapHelper(result, index + 1);
    }

    public void addEvent(Event e) throws IllegalArgumentException, IllegalStateException {
        if (e == null)
            throw new IllegalArgumentException("Event cannot be null");
        if (e.isComplete())
            throw new IllegalArgumentException("Cannot add completed event");
        if (size >= heapData.length)
            throw new IllegalStateException("Heap is full");
        heapData[size] = e;
        percolateUp(size);
        size++;
    }

    protected void percolateUp(int i) {
        if (i == 0) return;
        int parent = (i - 1) / 2;
        if (compare(heapData[i], heapData[parent]) < 0) {
            swap(i, parent);
            percolateUp(parent);
        }
    }
    
    protected void percolateDown(int i) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int smallest = i;
        if (left < size && compare(heapData[left], heapData[smallest]) < 0) {
            smallest = left;
        }
        if (right < size && compare(heapData[right], heapData[smallest]) < 0) {
            smallest = right;
        }
        if (smallest != i) {
            swap(i, smallest);
            percolateDown(smallest);
        }
    }
    
    private void swap(int i, int j) {
        Event temp = heapData[i];
        heapData[i] = heapData[j];
        heapData[j] = temp;
    }

    private int compare(Event a, Event b) {
        if (sortAlphabetically) {
            return a.getDescription().compareToIgnoreCase(b.getDescription());
        } else {
          return a.compareTo(b); // by timestamp
        }
    }

    public void completeEvent() throws IllegalStateException {
        if (isEmpty())
            throw new IllegalStateException("Queue is empty");
        if (completedSize >= completed.length)
            throw new IllegalStateException("Completed array is full");
        Event completedEvent = heapData[0];
        completedEvent.markAsComplete();
        completed[completedSize++] = completedEvent;
        heapData[0] = heapData[size - 1];
        heapData[size - 1] = null;
        size--;
        if (!isEmpty()) {
            percolateDown(0);
        }
    }

    protected PriorityEvents deepCopy() {
        PriorityEvents copy = new PriorityEvents(heapData.length);
        copy.size = this.size;
        copy.completedSize = this.completedSize;
    
        for (int i = 0; i < size; i++) {
            copy.heapData[i] = heapData[i];
        }
        for (int i = 0; i < completedSize; i++) {
            copy.completed[i] = completed[i];
        }
        return copy;
    }

    private Event removeBest() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty");
        Event top = heapData[0];
        heapData[0] = heapData[size - 1];
        heapData[size - 1] = null;
        size--;
        if (!isEmpty()) percolateDown(0);
        return top;
    }
    
    @Override
    public String toString() {
        PriorityEvents copy = this.deepCopy(); // don’t touch the real queue
        String[] lines = new String[copy.size];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = copy.removeBest().toString(); // pull events in sorted order
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            sb.append(lines[i]);
            if (i < lines.length - 1) sb.append("\n"); // no trailing newline
        }
        return sb.toString();
    }
}
