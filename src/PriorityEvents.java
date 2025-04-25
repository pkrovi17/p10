
//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title:    Priority Queue of Events
// Course:   CS 300 Spring 2025
//
// Author:   Pranav Krovi
// Email:    pkrovi@wisc.edu
// Lecturer: Mouna Kacem
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
// 
// Partner Name:    x
// Partner Email:   x
// Partner Lecturer's Name: x
// 
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//   x Write-up states that pair programming is allowed for this assignment.
//   x We have both read and understand the course Pair Programming Policy.
//   x We have registered our team prior to the team registration deadline.
//
//////////////////////// ASSISTANCE/HELP CITATIONS ////////////////////////////
//
// Persons:         Mouna Kacem
//                  The diagram present in the project docs that helped me understand the heap structure better
// Online Sources:  GeeksforGeeks
//                  - https://www.geeksforgeeks.org/priority-queue-set-1-introduction/
//
///////////////////////////////////////////////////////////////////////////////
// imports
import java.util.NoSuchElementException;
/*
 * Class PriorityEvents represents a priority queue of Event objects. 
 * It allows adding events, completing them, and retrieving the next event based on priority 
 * (either alphabetically or chronologically)
 * The class also provides methods to clear completed events and check the size of the queue.
 */
public class PriorityEvents extends Object{
    // Instance variables
    // completed array stores completed events
    private Event[] completed;
    // completedSize keeps track of the number of completed events
    private int completedSize;
    // heapData array stores the events in the priority queue
    private Event[] heapData;
    // size keeps track of the number of events in the priority queue
    private int size;
    // sortAlphabetically is a static variable that determines the sorting order of events
    private static boolean sortAlphabetically;

    /*
     * Initializes the PriorityEvents object with a specified capacity.
     * @param capacity, the initial capacity of the priority queue
     * @throws IllegalArgumentException if the capacity is negative
     */
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
   /*
    * Initializes the PriorityEvents object with an array of events and its size.
    * @param events, the array of Event objects
    * @param size, the number of events in the array
    * @throws IllegalArgumentException if the events array is null, size is invalid, or any event is null or completed
    * @throws IllegalStateException if the heap is full
    */
    public PriorityEvents(Event[] events, int size) throws IllegalArgumentException {
        if (events == null || size < 0 || size > events.length)
            throw new IllegalArgumentException("Invalid inputs");
        for (int i = 0; i < size; i++) {
            if (events[i] == null || events[i].isComplete())
                throw new IllegalArgumentException("Invalid or completed Event in array");
            }
        heapData = events.clone(); // copys the array
        // oversize array
        completed = new Event[size * 2];
        this.size = size;
        completedSize = 0;
        // Heapify process (bottom-up)
        for (int i = size / 2 - 1; i >= 0; i--) {
        percolateDown(i);
        }
    }
    /*
     * returns the sorting order of events
     * @return sortAlphabetically, true for alphabetical sorting, false for chronological sorting
     */
    public static boolean isSortedAlphabetically () {
        return sortAlphabetically;
    }
    /*
     * Sets the sorting order of events to be alphabetical.
     */
    public static void sortAlphabetically () {
        sortAlphabetically = true;
    }
    /*
     * Sets the sorting order of events to be chronological.
     */
    public static void sortChronologically () {
        sortAlphabetically = false;
    }
    /*
     * Checks if the priority queue is empty.
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }
    /*
     * Returns the number of events in the priority queue.
     * @return the number of events in the queue
     */
    public int size() {
        return size;
    }
    /*
     * Returns the number of completed events.
     * @return the number of completed events
     */
    public int numCompleted() {
        return completedSize;
    }
    /*
     * Clears all completed events from the completed array and returns them.
     * @return an array of cleared completed events
     */
    public Event[] clearCompletedEvents() {
        Event[] result = new Event[completedSize];
        deepCopyHelper(result, 0);
        completedSize = 0;
        return result;
    }
    /*
     * Helper method to deep copy completed events into a new array. Recursive
     * @param result, the array to store copied events
     * @param index, the current index in the completed array
     */
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
    /*
     * Returns the completed events in the completed array.
     * @return an array of completed events
     */
    protected Event[] getCompletedEvents() {
        Event[] result = new Event[completedSize];
        copyCompletedHelper(result, 0);
        return result;
    }
    /*
     * Helper method to copy completed events into a new array. Recursive
     * @param result, the array to store copied events
     * @param index, the current index in the completed array
     */
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
    /*
     * Returns the next event in the priority queue without removing it.
     * @return the next event in the queue
     * @throws NoSuchElementException if the queue is empty
     */
    public Event peekNextEvent() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("No events available");
        }
        return heapData[0];
    }
    /*
     * Returns the events in the priority queue as an array.
     * @return an array of events in the queue
     */
    protected Event[] getHeapData() {
        Event[] result = new Event[heapData.length];
        copyHeapHelper(result, 0);
        return result;
    }
    /*
     * Helper method to copy events from the heapData array into a new array. Recursive
     * @param result, the array to store copied events
     * @param index, the current index in the heapData array
     */
    private void copyHeapHelper(Event[] result, int index) {
    if (index >= heapData.length) return;
    result[index] = heapData[index];
    copyHeapHelper(result, index + 1);
    }
    /*
     * Adds a new event to the priority queue.
     * @param e, the event to be added
     * @throws IllegalStateException if the queue is full
     * @throws IllegalArgumentException if the event is null or completed
     */
    public void addEvent(Event e) throws IllegalStateException, IllegalArgumentException {
        // Cite GeeksforGeeks
        // https://www.geeksforgeeks.org/priority-queue-set-1-introduction/
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
    /*
     * Percolates an event up the heap to maintain the heap property. Recursive
     * @param i, the index of the event to be percolated up
     */
    protected void percolateUp(int i) {
        // Cite GeeksforGeeks
        // https://www.geeksforgeeks.org/priority-queue-set-1-introduction/
        if (i == 0) return;
        int parent = (i - 1) / 2;
        if (compare(heapData[i], heapData[parent]) < 0) {
            swap(i, parent);
            percolateUp(parent);
        }
    }
    /*
     * Percolates an event down the heap to maintain the heap property. Recursive
     * @param i, the index of the event to be percolated down
     */
    protected void percolateDown(int i) {
        // Cite GeeksforGeeks
        // https://www.geeksforgeeks.org/priority-queue-set-1-introduction/
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
    /*
     * Swaps two events in the heapData array.
     * @param i, the index of the first event
     * @param j, the index of the second event
     */
    private void swap(int i, int j) {
        Event temp = heapData[i];
        heapData[i] = heapData[j];
        heapData[j] = temp;
    }
    /*
     * Compares two events based on the sorting order (alphabetical or chronological).
     * @param a, the first event to compare
     * @param b, the second event to compare
     * @return a negative integer, zero, or a positive integer as the first event is less than, equal to, or greater than the second event
     */
    private int compare(Event a, Event b) {
        if (sortAlphabetically) {
            return a.getDescription().compareToIgnoreCase(b.getDescription());
        } else {
          return a.compareTo(b); // by timestamp
        }
    }
    /*
     * Marks the next event in the priority queue as complete and moves it to the completed array.
     * @throws IllegalStateException if the queue is empty or the completed array is full
     */
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
    /*
     * Returns the completed events in the completed array.
     * @return an array of completed events
     */
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
    /*
     * Returns the next best event in the priority queue and removes it from the queue.
     * @return the next best event in the queue
     * @throws IllegalStateException if the queue is empty
     */
    private Event removeBest() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty");
        Event top = heapData[0];
        heapData[0] = heapData[size - 1];
        heapData[size - 1] = null;
        size--;
        if (!isEmpty()) percolateDown(0);
        return top;
    }
    /*
     * Returns a string representation of the priority queue, with events sorted in the current order.
     * @return a string representation of the priority queue
     * @override toString method from Object class
     */
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