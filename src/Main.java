public class Main {
    public static void main(String[] args) {
        // Elevators are numbered 0 to 6.
        ElevatorSystem es = new ElevatorSystem(7);

        es.addRequest(35, 0, Direction.DOWN);
        es.addRequest(14, 0, Direction.DOWN);
        es.addRequest(0, 14, Direction.UP);

        es.checkAvailableElevators();

        es.addRequest(20, 0, Direction.DOWN);

        try {
            // Let the elevators run for a little while.
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        es.checkAvailableElevators();

        es.addRequest(0, 15, Direction.UP);

        try {
            // Let the elevators run for a little while.
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        es.checkAvailableElevators();

        try {
            es.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
