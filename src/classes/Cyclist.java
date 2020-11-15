package classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.*;

public class Cyclist implements Runnable{

    private final String name;
    private final Phaser phaser;
    private final DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Cyclist(String name, Phaser phaser){
        Objects.requireNonNull(name);
        Objects.requireNonNull(phaser);
        this.name = name;
        this.phaser = phaser;
    }
    @Override
    public void run() {
        // 1. Sale de casa y se dirige a la gasolinera. Tarda entre 1 y 3 segundos
        try {
            goToPetrolStation();
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while going to the Petrol Station\n", name);
            return;
        }

        // 2. Espere que llegue todo el mundo
        try {
            phaser.awaitAdvanceInterruptibly(phaser.arrive());
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while waiting in the Petrol Station\n", name);
        }

        // 3. Una vez que llegue todo el mundo comienza la etapa.
        //  Llegan a una venta de entre 5 y 10 segundos, lo indica
        try {
            beginsTheStage();
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while waiting in the Disposal\n", name);
        }
        // 4. Espera a que llegue los demás
        try {
            phaser.awaitAdvanceInterruptibly(phaser.arrive());
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while waiting in the Disposal\n", name);
        }

        // 5. Vuelven a la gasolinera. Suelen tardar el mismo tiempo.
        try {
            goBackToThePetrolStation();
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while going back to the Petrol Station\n", name);
        }
        // 6. Espera a que llegue todo el mundo
        try {
            phaser.awaitAdvanceInterruptibly(phaser.arrive());
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while waiting in the Petrol Station\n", name);
        }
        // 7. Etapa finalizada y vuelve para su casa, tarda entre 1 y 3 segundos.
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
