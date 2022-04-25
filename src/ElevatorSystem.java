public class ElevatorSystem {
    private final Elevator[] elevators;

    public ElevatorSystem(int amount) {
        elevators = new Elevator[amount];

        for (int i = 0; i < elevators.length; i++) {
            elevators[i] = new Elevator(i);
        }
    }

    public void addTask(int floor, int destFloor, Direction direction) {
        Elevator elevatorWithLeastTasks = elevators[0];

        for (Elevator e : elevators) {
            // If the elevator is immediately available, assign the task to that elevator
            if (e.isImmediatelyAvailable()) {
                e.addTask(floor, destFloor, direction);
                return;
            }

            // Also use this loop find out the elevator with the least amount of tasks, to avoid a second loop.
            // Since the search for the minimum is not atomic (doesn't lock all the tasks queues in the elevators),
            // addTask() is not thread-safe (should be fine for this purpose though)
            if (e.amountOfTasks() < elevatorWithLeastTasks.amountOfTasks()) {
                elevatorWithLeastTasks = e;
            }
        }

        // No elevator immediately available, assign to the least busy elevator
        elevatorWithLeastTasks.addTask(floor, destFloor, direction);
    }

    public void shutdown() throws InterruptedException {
        for (Elevator e : elevators) {
            e.shutdown();
        }
    }

}
