import java.util.LinkedList;
import java.util.Queue;

/**
 * An elevator.
 */
public class Elevator {
    private final int elevatorId;
    private int currentFloor = 0;
    private Direction currentDirection = Direction.NONE;
    private final Queue<Integer> tasks = new LinkedList<>();
    private final ElevatorThread thread;

    /**
     * Creates a new elevator.
     * @param elevatorId ID of the new elevator.
     */
    public Elevator(int elevatorId) {
        this.elevatorId = elevatorId;
        this.thread = new ElevatorThread();
        this.thread.start();
    }

    /**
     * Submits a new request to the elevator.
     * @param floor Starting floor.
     * @param destFloor Destination floor.
     * @param direction Direction.
     */
    public void addRequest(int floor, int destFloor, Direction direction) {
        synchronized (tasks) {
            // Only add the starting floor if the elevator is at the
            if (floor != currentFloor && direction != currentDirection) {
                tasks.add(floor);
            }
            tasks.add(destFloor);
        }
    }

    /**
     * Whether the elevator is immediately available.
     * @return true, if the elevator is immediately available.
     */
    public boolean isImmediatelyAvailable() {
        synchronized (tasks) {
            return tasks.size() == 0;
        }
    }

    /**
     * Returns the amount of tasks the elevator is currently handling.
     * @return Amount of tasks.
     */
    public int amountOfTasks() {
        synchronized (tasks) {
            return tasks.size();
        }
    }

    /**
     * Shutdown the elevator.
     * @throws InterruptedException Thread got interrupted.
     */
    public void shutdown() throws InterruptedException {
        thread.interrupt();
        thread.join();
    }

    /**
     * Elevator thread that simulates the movement of the elevator.
     */
    private class ElevatorThread extends Thread {

        public void run() {
            while (!Thread.interrupted()) {
                synchronized (tasks) {
                    if (tasks.size() > 0) {
                        int taskToHandle = tasks.peek();

                        if (taskToHandle < currentFloor) {
                            currentFloor--;
                            currentDirection = Direction.DOWN;
                        } else {
                            currentFloor++;
                            currentDirection = Direction.UP;
                        }

                        if (taskToHandle == currentFloor) {
                            tasks.remove();
                            currentDirection = Direction.NONE;
                        }
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Thread got interrupted, stop execution
                    break;
                }
            }
        }
    }
}
