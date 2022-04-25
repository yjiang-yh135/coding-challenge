public class Main {
    public static void main(String[] args) {
        ElevatorSystem es = new ElevatorSystem(2);
        es.addTask(10, 5, Direction.DOWN);
        es.addTask(5, 10, Direction.UP);
        es.addTask(2, 5, Direction.UP);

//        try {
//            Thread.sleep(5000);
//            es.shutdown();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
