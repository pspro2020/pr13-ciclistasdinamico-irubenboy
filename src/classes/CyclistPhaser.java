package classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Phaser;

public class CyclistPhaser extends Phaser {
    public static final int ARRIVE_TO_PEATROL_STATION_PHASE = 0;
    public static final int FINISH_PHASER_ONE = 1;
    public static final int FINISH_PHASER_TWO = 2;

    private final DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm:ss");


    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        switch (phase){
            case ARRIVE_TO_PEATROL_STATION_PHASE:
                System.out.printf("%s -> All %d cyclists arrived to peatrol station (excuted in %s)",
                        LocalDateTime.now().format(f), registeredParties, Thread.currentThread().getName());
                break;
            case FINISH_PHASER_ONE:
                System.out.printf("%s -> All %d cyclists finished their first phase",
                        LocalDateTime.now().format(f), registeredParties, Thread.currentThread().getName());
                break;
            case FINISH_PHASER_TWO:
                System.out.printf("%s -> All %d cyclists finished their second phase",
                        LocalDateTime.now().format(f), registeredParties, Thread.currentThread().getName());
                return true;
        }
        return  super.onAdvance(phase, registeredParties);
    }
}
