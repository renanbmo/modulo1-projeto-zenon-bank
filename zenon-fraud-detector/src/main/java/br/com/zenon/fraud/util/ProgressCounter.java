package br.com.zenon.fraud.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Contador estático thread-safe para acompanhar o progresso da ingestão.
 * <p>
 * Pode ser incrementado concorrentemente por várias threads. A cada
 * {@link #PRINT_INTERVAL} registros, imprime o total atingido exatamente uma
 * vez — mesmo que várias threads cruzem o marco "ao mesmo tempo".
 */
public final class ProgressCounter {

    public static final long PRINT_INTERVAL = 500_000;

    private static final AtomicLong COUNT = new AtomicLong();

    private ProgressCounter() {
    }

    /**
     * Soma {@code delta} ao total (ex.: o tamanho de um lote salvo) e imprime o
     * marco caso a soma tenha cruzado um múltiplo de {@link #PRINT_INTERVAL}.
     * Retorna o novo valor.
     */
    public static long add(long delta) {
        long newValue = COUNT.addAndGet(delta);
        long oldValue = newValue - delta;

        // addAndGet é atômico: cada thread recebe a faixa exclusiva (oldValue, newValue],
        // então só a thread que cruzou o marco vê o quociente subir e imprime.
        if (newValue / PRINT_INTERVAL > oldValue / PRINT_INTERVAL)
            IO.println("Processed " + (newValue / PRINT_INTERVAL * PRINT_INTERVAL) + " records");

        return newValue;
    }

    /** Total atual. */
    public static long get() {
        return COUNT.get();
    }
}
