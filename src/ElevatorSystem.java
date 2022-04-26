/**
 * An elevator system, that takes requests and distributes them among the elevators.
 */
public class ElevatorSystem {
    private final Elevator[] elevators;

    /**
     * Creates a new elevator system.
     * @param amount Amount of elevators.
     */
    public ElevatorSystem(int amount) {
        elevators = new Elevator[amount];

        for (int i = 0; i < elevators.length; i++) {
            elevators[i] = new Elevator(i);
        }
    }

    /**
     * Add a new request to the elevator system.
     * @param floor Starting floor.
     * @param destFloor Destination floor.
     * @param direction Direction.
     */
    public void addRequest(int floor, int destFloor, Direction direction) {
        Elevator elevatorWithLeastRequests = elevators[0];

        for (Elevator e : elevators) {
            // If the elevator is immediately available, assign the task to that elevator
            if (e.isImmediatelyAvailable()) {
                e.addRequest(floor, destFloor, direction);
                return;
            }

            // Also use this loop find out the elevator with the least amount of tasks, to avoid a second loop.
            // Since the search for the minimum is not atomic (doesn't lock all the tasks queues in the elevators),
            // addTask() is not thread-safe (should be fine for this purpose though)
            if (e.amountOfTasks() < elevatorWithLeastRequests.amountOfTasks()) {
                elevatorWithLeastRequests = e;
            }
        }

        // No elevator immediately available, assign to the least busy elevator
        elevatorWithLeastRequests.addRequest(floor, destFloor, direction);
    }

    /**
     * Prints out all immediately available elevators.
     */
    public void checkAvailableElevators() {
        StringBuilder builder = new StringBuilder("Available Elevators: ");
        boolean first = true;
        for (int i = 0; i < elevators.length; i++) {
            if (elevators[i].isImmediatelyAvailable()) {
                if (!first) {
                    builder.append(", ");
                }
                builder.append(i);
                first = false;
            }
        }
        System.out.println(builder);
    }

    /**
     * Shutdown the elevator system.
     * @throws InterruptedException Thread got interrupted.
     */
    public void shutdown() throws InterruptedException {
        for (Elevator e : elevators) {
            e.shutdown();
        }
    }

}
