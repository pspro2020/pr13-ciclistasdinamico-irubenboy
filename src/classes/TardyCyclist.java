package classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static classes.CyclistPhaser.*;

public class TardyCyclist implements Runnable{

    private final String name;
    private final Phaser phaser;
    private final DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm:ss");

    public TardyCyclist(String name, Phaser phaser){
        Objects.requireNonNull(name);
        Objects.requireNonNull(phaser);
        this.name = name;
        this.phaser = phaser;
    }


    @Override
    public void run() {
        // Se controla si no ha terminado la fase
        if(!phaser.isTerminated()){
            int p = phaser.register();
            System.out.printf("%s -> %s has joined friends in phase #d\n",
                    LocalDateTime.now().format(f), name, p);
            //1. Va a la gasolinera
            try {
                goToPetrolStation();
            } catch (InterruptedException e) {
                System.out.printf("%s has been interrupted while going to the Petrol Station\n", name);
                return;
            }

            if(p <= ARRIVE_TO_PETROL_STATION_PHASE){
                try {
                    phaser.awaitAdvanceInterruptibly(phaser.arrive());
                } catch (InterruptedException e) {
                    System.out.printf("I've interrupted while waiting to cyclist in the petrol Station");
                }
            }

            try {
                beginsTheStage();
            } catch (InterruptedException e) {
                System.out.printf("%s has been interrupted while waiting in the Disposal\n", name);
            }

            if(p <= FINISH_PHASER_ONE) {
                try {
                    phaser.awaitAdvanceInterruptibly(phaser.arrive());
                } catch (InterruptedException e) {
                    System.out.printf("%s has been interrupted while waiting in the Disposal\n", name);
                }
            }

            try {
                goBackToThePetrolStation();
            } catch (InterruptedException e) {
                System.out.printf("%s has been interrupted while going back to the Petrol Station\n", name);
            }

            if(p <= FINISH_PHASER_TWO){
                try {
                    phaser.awaitAdvanceInterruptibly(phaser.arrive());
                } catch (InterruptedException e) {
                    System.out.printf("%s has been interrupted while waiting in the Petrol Station\n", name);
                }
            }
            try {
                goBackHome();
            } catch (InterruptedException e) {
                System.out.printf("%s has been interrupted while going back home\n", name);
            }
        } else {
            System.out.printf("%s -> %s called the cyclists too late\n",
                    LocalDateTime.now().format(f), name);
        }
    }

    private void goBackHome() throws InterruptedException {
        System.out.printf("%s -> %s has finalized and is leaving the Petrol Station\n",
                LocalDateTime.now().format(f), name);
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3)+1);
        System.out.printf("%s -> %s is at home.\n", LocalDateTime.now().format(f), name);
    }

    private void goBackToThePetrolStation() throws InterruptedException {
        System.out.printf("%s -> %s goes back to the Petrol Station\n", LocalDateTime.now().format(f), name);
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(6)+1);
        System.out.printf("%s -> %s has arrived to the Petrol Station\n", LocalDateTime.now().format(f), name);
    }

    private void beginsTheStage() throws InterruptedException {
        System.out.printf("%s -> %s has begun the Stage\n", LocalDateTime.now().format(f), name);
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(6)+1);
        System.out.printf("%s -> %s has arrived in the Disposal\n", LocalDateTime.now().format(f), name);
    }

    private void goToPetrolStation() throws InterruptedException {
        System.out.printf("%s -> %s is leaving home...\n",
                LocalDateTime.now().format(f), name);
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3)+1);
        System.out.printf("%s -> %s has arrived in the Petrol Station\n",
                LocalDateTime.now().format(f), name);
    }
}
