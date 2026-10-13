package PackInterfaces;

import java.util.Random;

public interface Aleatorio {

    /**
     * Gerador de numeros aleatorios.
     * 
     * @param min Minimo numero possivel.
     * @param max
     * @return
     */
    default double numeroAleatorio(double min, double max) {
        Random random = new Random();

        return min + random.nextDouble()*(max-min);
    }
}


