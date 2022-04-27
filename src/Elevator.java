import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * An elevator.
 */
public class Elevator {
    private final int elevatorId;
    private int currentFloor = 0;
    private Direction currentDirection = Direction.NONE;
    private final Queue<Integer> tasks = new LinkedList<>();
    private final ElevatorThread thread;
    // Semaphore to track amount of tasks to prevent busy waiting.
    private final Semaphore tasksSem = new Semaphore(0);

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
            if (floor != currentFloor && direction != currentDirection) {
                tasks.add(floor);
                tasksSem.release();
            }
            tasks.add(destFloor);
            tasksSem.release();
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
                // System.out.printf("[%d] Current Floor: %d, Direction: %s\n", elevatorId, currentFloor, currentDirection);

                try {
                    // Blocks if there are no tasks.
                    tasksSem.acquire();
                } catch (InterruptedException e) {
                    break;
                }

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
                            // Task fulfilled, remove it.
                            tasks.remove();
                            currentDirection = Direction.NONE;
                        } else {
                            // Release the Semaphore again since the task hasn't been removed.
                            tasksSem.release();
                        }
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
