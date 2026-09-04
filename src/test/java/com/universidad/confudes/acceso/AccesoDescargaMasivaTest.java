package com.universidad.confudes.acceso;

import com.universidad.confudes.certificados.ServicioCertificados;
import com.universidad.confudes.certificados.SolicitudCertificado;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

class AccesoDescargaMasivaTest {

    @AfterEach
    void limpiarRol() {
        System.clearProperty("confudes.rol");
    }

    @Test
    void rechazaAParticipanteSinLlegarAEmitir() {
        System.setProperty("confudes.rol", "PARTICIPANTE");
        ServicioCertificados base = sol -> "PDF_BASE".getBytes();
        ServicioCertificados controlado = new ServicioCertificadosProxy(base);
        SolicitudCertificado solicitud = new SolicitudCertificado("EVT-001", "PART-123", "Ana", "ana@correo.com");
        assertThrows(SecurityException.class, () -> controlado.emitir(solicitud));
    }

    @Test
    void permiteAOrganizador() {
        System.setProperty("confudes.rol", "ORGANIZADOR");
        ServicioCertificados base = sol -> "PDF_BASE".getBytes();
        ServicioCertificados controlado = new ServicioCertificadosProxy(base);
        SolicitudCertificado solicitud = new SolicitudCertificado("EVT-001", "PART-123", "Ana", "ana@correo.com");
        assertDoesNotThrow(() -> controlado.emitir(solicitud));
    }
}