import java.util.concurrent.Semaphore;

public class Avion extends Thread {

    private int id;

    private static final int K_HANGARES = 10;
    private static final int AVIONES = 20;
    private static Semaphore hangar;    //Semaforo que controla los hangares disponibles (inicializado a K_HANGARES)
    private static Semaphore pista;     //Mutex que controla el acceso a la pista

    public Avion(int id) {
        this.id = id;
    }

    public void aterrizar() {
        try{
            hangar.acquire();   //reservamos el hangar antes de intentar reservar la pista
            System.out.println("El avion " + this.id + " reserva hangar. Hangares libres: " + hangar.availablePermits());
            pista.acquire();    //con el hangar reservado, intentamos obtener la pista

            Thread.sleep(250);
            System.out.println("El avion " + this.id + " aterriza");

            pista.release();    //liberamos la pista una vez aterrizamos
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void cargar() {
        try{
            Thread.sleep(100);
            System.out.println("El avion " + this.id + " carga");
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void descargar() {
        try{
            Thread.sleep(100);
            System.out.println("El avion " + this.id + " descarga");

            pista.acquire();    //tratamos de obtener la pista para despegar
            hangar.release();   //una vez obtenemos la pista  podemos liberar el hangar y despegar
            System.out.println("El avion " + this.id + " deja el hangar. Hangares libres: " + hangar.availablePermits());
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void despegar() {
        try{
            Thread.sleep(250);
            System.out.println("El avion " + this.id + " despega");

            pista.release();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        aterrizar();
        cargar();
        descargar();
        despegar();
    }

    public static void main(String args[]) {
        hangar = new Semaphore(K_HANGARES);
        pista = new Semaphore(1);
        Thread threads [] = new Thread[AVIONES];

        for (int i = 0; i < AVIONES; i++) {
            try{
                Thread.sleep((int)Math.random()*200 + 100); //esperamos un tiempo aleatorio para la llegada de los aviones
                Thread thr = new Avion(i);
                thr.start();
                threads[i] = new Thread(thr);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        try{
            for (int i = 0; i < AVIONES; i++) {
                threads[i].join();
            }
        }catch(InterruptedException e) {
            return;
        }
    }
}
