package classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ImpatientCyclist implements Runnable{

    private final String name;
    private final Phaser phaser;
    private DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm:ss");

    public ImpatientCyclist(String name, Phaser phaser){
        Objects.requireNonNull(name);
        Objects.requireNonNull(phaser);
        this.name = name;
        this.phaser = phaser;
    }
    public void run() {
        phaser.register();
        // 1. Sale de casa y se dirige a la gasolinera. Tarda entre 1 y 3 segundos
        try {
            goToPetrolStation();
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while going to the Petrol Station\n", name);
            return;
        }

        // 2. No espera a que llegue todo el mundo
        phaser.arriveAndDeregister();
        // 3. Una vez que llegue todo el mundo comienza la etapa.
        //  Llegan a una venta de entre 5 y 10 segundos, lo indica
        try {
            beginsTheStage();
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while waiting in the Disposal\n", name);
        }

        // 4. Vuelve a la gasolinera. Suelen tardar el mismo tiempo.
        try {
            goBackToThePetrolStation();
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while going back to the Petrol Station\n", name);
        }

        // 5. Etapa finalizada y vuelve para su casa, tarda entre 1 y 3 segundos.
        try {
            goBackHome();
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while going back home\n", name);
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
