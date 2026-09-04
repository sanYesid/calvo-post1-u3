package com.universidad.confudes.certificados;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MejorasCertificadoTest {

    private final SolicitudCertificado solicitud =
        new SolicitudCertificado("EVT-001", "PART-123", "Ana Ríos", "ana@correo.com");

    @Test
    void emiteSinNingunaMejoraActivada() {
        ServicioCertificados base = sol -> "PDF_BASE".getBytes();
        assertDoesNotThrow(() -> base.emitir(solicitud));
    }

    @Test
    void combinaLasTresMejorasSinCrearUnaClaseNueva() {
        ServicioCertificados base = sol -> "PDF_BASE".getBytes();
        ServicioCertificados conTodo = new TraduccionInglesDecorador(
            new CodigoQRDecorador(
                new MarcaDeAguaDecorador(base, "CONFIDENTIAL"),
                "https://confudes.edu/verify/123"
            )
        );
        assertDoesNotThrow(() -> conTodo.emitir(solicitud));
    }

    @Test
    void unaSolaMejoraFuncionaDeFormaIndependiente() {
        ServicioCertificados base = sol -> "PDF_BASE".getBytes();
        ServicioCertificados soloMarcaDeAgua = new MarcaDeAguaDecorador(base, "CONFIDENTIAL");
        assertDoesNotThrow(() -> soloMarcaDeAgua.emitir(solicitud));
    }
}