import java.util.LinkedList;
import java.util.Queue;

public class Elevator {
    private int currentFloor = 0;
    private Direction currentDirection = Direction.NONE;
    private final Queue<Integer> tasks = new LinkedList<>();
    private final ElevatorThread thread;

    public Elevator() {
        this.thread = new ElevatorThread();
        this.thread.start();
    }

    public void addTask(int floor, int destFloor, Direction direction) {
        // Don't add if at this floor already
        synchronized (tasks) {
            if (floor != currentFloor) tasks.add(floor);
            tasks.add(destFloor);
        }
    }

    public boolean isImmediatelyAvailable() {
        synchronized (tasks) {
            return tasks.size() == 0;
        }
    }

    public void shutdown() throws InterruptedException {
        thread.interrupt();
        thread.join();
    }

    private class ElevatorThread extends Thread {

        public void run() {
            while (!Thread.interrupted()) {
                System.out.println("Current floor: " + currentFloor + ", Direction: " + currentDirection);

                // If there is something to do
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
                    // Thread got interrupted
                    break;
                }
            }
        }
    }
}
