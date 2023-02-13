public class TrafficControllerSimple implements TrafficController {
    private final TrafficRegistrar registrar;
    private boolean empty = true;
    
    public TrafficControllerSimple(TrafficRegistrar registrar) {
        this.registrar = registrar;
    }

    private void enter() {
        while(!empty) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }
        empty = true;
        notifyAll();
    }

    private void leave() {
        empty = true;
        notifyAll();
    }

    @Override
    public synchronized void enterLeft(Vehicle v) {
        enter();
        registrar.registerLeft(v);
    }

    @Override
    public synchronized void enterRight(Vehicle v) {
        enter();
        registrar.registerRight(v);
    }

    @Override
    public synchronized void leaveLeft(Vehicle v) {
        leave();
        registrar.deregisterLeft(v);
    }

    @Override
    public synchronized void leaveRight(Vehicle v) {
        leave();
        registrar.deregisterRight(v);
    }
}
