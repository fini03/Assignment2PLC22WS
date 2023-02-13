import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficControllerFair implements TrafficController {
    private final TrafficRegistrar registrar;
    Lock lock = new ReentrantLock(true);
    private final Condition place_empty = lock.newCondition();
    private boolean empty = true;

    public TrafficControllerFair(TrafficRegistrar registrar) {
        this.registrar = registrar;
    }

    @Override
    public void enterLeft(Vehicle v) {
        lock.lock();
        try {
            while(!empty) {
                place_empty.await();
            }
            registrar.registerLeft(v);
            empty = false;
        } catch (InterruptedException e) {}
    }

    @Override
    public void enterRight(Vehicle v) {
        lock.lock();
        try {
            while(!empty) {
                place_empty.await();
            }
            registrar.registerRight(v);
            empty = false;
        } catch (InterruptedException e) {}
    }

    @Override
    public void leaveLeft(Vehicle v) {
        empty = true;
        registrar.deregisterLeft(v);
        place_empty.signalAll();
        lock.unlock();
    }

    @Override
    public void leaveRight(Vehicle v) {
        empty = true;
        registrar.deregisterRight(v);
        place_empty.signalAll();
        lock.unlock();
    }
}
