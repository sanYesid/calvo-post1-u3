# calvo-post1-u3
Post-contenido — Patrones Estructurales aplicados al backend de ConfUDES

### Necesidad 1 — Registro de asistencia    
**Patrón Elegido:** Adapter    
ServicioAsistencia está bien como está. El problema es QRCheckClient: pide long donde el sistema tiene String, tiene su propio QRCheckRequest/QRCheckResponse, y responde con 200/401 en vez del resultado que espera ControladorCheckIn. Como no se puede tocar ninguno de los dos lados, hace falta algo en el medio que traduzca, eso es Adapter.    

Se consideró Facade un momento porque técnicamente también se está "envolviendo" algo de afuera, pero Facade resuelve otro problema: cuando el cliente tiene que coordinar varios colaboradores. Acá hay uno solo, y lo que falla es que su contrato no calza con el interno, no que sean muchos. AdaptadorQRCheck implementa ServicioAsistencia, convierte el eventoId, arma el payload con la credencial y traduce la respuesta del proveedor a ResultadoCheckIn. ControladorCheckIn ni se entera de que cambió el proveedor.    

### Necesidad 2 — Emisión de certificados    
**Patrón Elegido:** Facade    
ValidadorAsistencia, GeneradorCertificadoPDF, FirmaDigitalService y EnvioCorreoService funcionan perfecto solos. Lo que estaba mal era que ControladorCertificados los conocía a los cuatro y los orquestaba a mano, entonces tocar la firma digital significaba tocar el controlador también. Eso no es un problema de interfaces, es acoplamiento — por eso Adapter no encajaba acá, no hay nada que traducir.   

ManejadorCertificadosFacade recibe los cuatro servicios y expone emitirYEnviarCertificado() con la misma secuencia de antes (validar, generar, firmar, enviar). El controlador quedó con una sola dependencia y su método en dos líneas.
