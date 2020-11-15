import classes.Cyclist;
import classes.CyclistPhaser;
import classes.ImpatientCyclist;
import classes.TardyCyclist;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int NUMBER_OF_FRIENDS = 10;

    public static void main(String[] args) throws InterruptedException {
        int i;
        CyclistPhaser phaser = new CyclistPhaser();

        for (i = 0; i < NUMBER_OF_FRIENDS; i++) {
            new Thread(new Cyclist("Cyclist #" + i , phaser), "Cyclist #" + i).start();
        }

        new Thread(new ImpatientCyclist("Cyclist #" + i, phaser), "Cyclist #" + i).start();
        i++;

        TimeUnit.SECONDS.sleep(9);
        new Thread(new TardyCyclist("Cylist # " + i, phaser), "Cylist # " + i).start();
        i++;

        TimeUnit.SECONDS.sleep(12);
        new Thread(new TardyCyclist("Cylist # " + i, phaser), "Cylist # " + i).start();
    }
}
