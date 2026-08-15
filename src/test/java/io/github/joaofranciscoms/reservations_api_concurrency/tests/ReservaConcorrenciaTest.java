package io.github.joaofranciscoms.reservations_api_concurrency.tests;

import io.github.joaofranciscoms.reservations_api_concurrency.model.Assento;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Evento;
import io.github.joaofranciscoms.reservations_api_concurrency.model.Reserva;
import io.github.joaofranciscoms.reservations_api_concurrency.model.StatusAssento;
import io.github.joaofranciscoms.reservations_api_concurrency.repository.AssentoRepository;
import io.github.joaofranciscoms.reservations_api_concurrency.repository.EventoRepository;
import io.github.joaofranciscoms.reservations_api_concurrency.repository.ReservaRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservaConcorrenciaTest {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private AssentoRepository assentoRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @SneakyThrows
    @Test
    void semLock() {
        executarCenario(reservaService::salvarSemLock);
    }

    @Test
    void lockOtimista() throws InterruptedException {
        executarCenario(reservaService::salvarComLockOtimista);
    }

    @Test
    void lockPessimista() throws InterruptedException {
        executarCenario(reservaService::salvarComLockPessimista);
    }

    private void executarCenario(Consumer<Reserva> tipoSalvamento) throws InterruptedException {

        // Mapa thread-safe para agrupar e contar os tipos de erros
        ConcurrentHashMap<String, AtomicInteger> errosAgrupados = new ConcurrentHashMap<>();

        Evento evento = new Evento();
        evento.setNome("Show Teste");
        evento.setLocal("Arena X");
        evento = eventoRepository.save(evento);

        Assento assento = new Assento();
        assento.setCodigo("A1");
        assento.setStatus(StatusAssento.DISPONIVEL);
        assento.setEvento(evento);
        assento = assentoRepository.save(assento);

        UUID assentoId = assento.getId();

        int numeroDeThreads = 50;
        ExecutorService executor = Executors.newFixedThreadPool(numeroDeThreads);
        CountDownLatch largada = new CountDownLatch(1);
        CountDownLatch chegada = new CountDownLatch(numeroDeThreads);

        AtomicInteger sucessos = new AtomicInteger(0);
        AtomicInteger falhas = new AtomicInteger(0);

        for (int i = 0; i < numeroDeThreads; i++) {
            int clienteId = i;
            executor.submit(() -> {
                try {
                    largada.await();

                    Assento assentoRef = new Assento();
                    assentoRef.setId(assentoId);

                    Reserva reserva = new Reserva();
                    reserva.setNomeCliente("Cliente " + clienteId);
                    reserva.setAssento(assentoRef);

                    tipoSalvamento.accept(reserva);
                    sucessos.incrementAndGet();
                }
                catch (Exception e) {
                    falhas.incrementAndGet();

                    // Extrai a causa raiz
                    Throwable rootCause = org.springframework.core.NestedExceptionUtils.getRootCause(e);
                    if (rootCause == null) {
                        rootCause = e;
                    }

                    String erroMensagem;

                    if (e instanceof ObjectOptimisticLockingFailureException || rootCause instanceof org.hibernate.StaleStateException) {

                        erroMensagem = "ObjectOptimisticLockingFailureException: Ops! Outra pessoa reservou este assento milissegundos antes de você";

                    } else {
                        // Mantém o comportamento padrão para as outras exceções (como o AssentoIndisponivelException)
                        erroMensagem = rootCause.getClass().getSimpleName() + ": " + rootCause.getMessage();
                    }

                    // Adiciona ao mapa
                    errosAgrupados.computeIfAbsent(erroMensagem, k -> new AtomicInteger(0)).incrementAndGet();
                }
                finally {
                    chegada.countDown();
                }
            });
        }

        largada.countDown();
        chegada.await();

        System.out.println("Sucessos: " + sucessos.get());
        System.out.println("Falhas: " + falhas.get());
        System.out.println("--- Resumo das Falhas ---");

        errosAgrupados.forEach((erro, quantidade) -> {System.out.println(quantidade.get() + "x -> " + erro);});

        executor.shutdown();

        long reservasDoAssento = reservaRepository.findAll().stream().filter(r -> r.getAssento().getId().equals(assentoId)).count();
        System.out.println("Reservas gravadas no banco: " + reservasDoAssento);
    }
}