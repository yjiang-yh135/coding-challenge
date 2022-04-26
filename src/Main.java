public class Main {
    public static void main(String[] args) {
        ElevatorSystem es = new ElevatorSystem(7);

        es.addRequest(35, 0, Direction.DOWN);
        es.addRequest(14, 0, Direction.UP);
        es.addRequest(0, 14, Direction.UP);

        es.checkAvailableElevators();

        es.addRequest(20, 0, Direction.DOWN);

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        es.checkAvailableElevators();

        es.addRequest(0, 15, Direction.UP);

        es.checkAvailableElevators();

        try {
            es.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
