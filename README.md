# Post-contenido — Unidad 3: Patrones Estructurales en ConfUDES

## Descripción
Repositorio del post-contenido de la Unidad 3 de Patrones de Diseño
de Software. Un único proyecto Spring Boot (confudes-patrones-
estructurales) que resuelve cuatro necesidades reales del backend
de ConfUDES, una plataforma de gestión de congresos académicos:
registro de asistencia con un proveedor externo, emisión de
certificados, mejoras opcionales sobre el certificado emitido y
control de acceso a la descarga masiva.

---

## Cómo ejecutar
```
$ mvn clean package
$ mvn spring-boot:run
$ mvn test
```
---

## Decisiones de diseño

### Necesidad 1 — Registro de asistencia    
**Patrón Elegido:** Adapter    
ServicioAsistencia está bien como está. El problema es QRCheckClient: pide long donde el sistema tiene String, tiene su propio QRCheckRequest/QRCheckResponse, y responde con 200/401 en vez del resultado que espera ControladorCheckIn. Como no se puede tocar ninguno de los dos lados, hace falta algo en el medio que traduzca, eso es Adapter.    

Se consideró Facade un momento porque técnicamente también se está "envolviendo" algo de afuera, pero Facade resuelve otro problema: cuando el cliente tiene que coordinar varios colaboradores. Acá hay uno solo, y lo que falla es que su contrato no calza con el interno, no que sean muchos. AdaptadorQRCheck implementa ServicioAsistencia, convierte el eventoId, arma el payload con la credencial y traduce la respuesta del proveedor a ResultadoCheckIn. ControladorCheckIn ni se entera de que cambió el proveedor.    

### Necesidad 2 — Emisión de certificados    
**Patrón Elegido:** Facade    
ValidadorAsistencia, GeneradorCertificadoPDF, FirmaDigitalService y EnvioCorreoService funcionan perfecto solos. Lo que estaba mal era que ControladorCertificados los conocía a los cuatro y los orquestaba a mano, entonces tocar la firma digital significaba tocar el controlador también. Eso no es un problema de interfaces, es acoplamiento — por eso Adapter no encajaba acá, no hay nada que traducir.   

ManejadorCertificadosFacade recibe los cuatro servicios y expone emitirYEnviarCertificado() con la misma secuencia de antes (validar, generar, firmar, enviar). El controlador quedó con una sola dependencia y su método en dos líneas.

### Necesidad 3 — Mejoras opcionales del certificado   
**Patron elegido:** Decorator    
El patrón usado es Decorator. Las tres mejoras (marca de agua, código QR, traducción) deben poder combinarse de cualquier forma sin agregar una clase por combinación, así que subclasificar por combinación quedó descartado de entrada: con tres mejoras ya son 8 combinaciones, y si mañana se agregan dos más serían 32 clases solo para cubrirlas. Eso no escala y rompe SRP y OCP.    

La otra opción, banderas booleanas en emitir() (emitir(solicitud, conQR, conMarca, conTraduccion)), tampoco funciona: ensucia la firma del método con detalles de formato y obliga a tocarlo cada vez que aparece una mejora nueva.    

Con Decorator, cada mejora vive en su propia clase (MarcaDeAguaDecorador, CodigoQRDecorador, TraduccionInglesDecorador), todas extendiendo CertificadoDecorador, que implementa ServicioCertificados y delega por defecto en el servicio envuelto. Se anidan en el orden que se quiera sin tocar el colaborador base de la Necesidad 2. 

Proxy, el patrón de la Necesidad 4, no aplica acá: Proxy decide si delega la llamada o no, y estas mejoras necesitan aplicarse siempre. Una "mejora" que decidiera bloquear el certificado no tendría sentido.   

### Necesidad 4 — Control de acceso a la descarga masiva  
**Patron Elegido:** Proxy    
El patrón usado es Proxy. ServicioCertificadosProxy implementa ServicioCertificados igual que el colaborador real, pero antes de delegar consulta el rol actual con ContextoUsuario.rolActual(). Si no es ORGANIZADOR ni ADMIN, lanza una SecurityException ahí mismo — el servicio real nunca llega a invocarse, así que la operación costosa, con el límite de 60 llamadas por minuto del proveedor de firma incluido, no se gasta en una petición que igual iba a rechazarse.    

Estructuralmente se parece a la Necesidad 3: ambos envuelven un ServicioCertificados con otro. La diferencia está en la intención. Decorator siempre delega y construye algo sobre el resultado; Proxy puede cortar la llamada antes de que llegue al objeto real. Cambiarlos sería un problema serio: un decorador llama primero al servicio envuelto para obtener el PDF, así que la firma digital se ejecutaría igual antes de revisar si el usuario tenía permiso.   

---


## Herramientas utilizadas
- Java 17, Spring Boot 3.2, Apache Maven, JUnit 5
- VS Code o IntelliJ IDEA, Git, GitHub  

## Conclusiones   

Lo más difícil de todo el laboratorio no fue decidir entre Adapter y Facade, sino entre Decorator y Proxy: los dos envuelven un ServicioCertificados con otro ServicioCertificados, así que en código lucen casi idénticos. La diferencia real está en la intención — uno siempre delega y transforma el resultado, el otro decide si delega o corta la llamada — y esa distinción solo se ve preguntando qué pasaría si se intercambiaran los patrones, no mirando la firma de los métodos. Adapter y Facade, en cambio, fueron más fáciles de distinguir porque el enunciado ya señalaba si el problema era de compatibilidad de interfaces o de exceso de colaboradores. Al final, la lección de las cuatro necesidades es la misma: el nombre del patrón importa menos que entender exactamente qué síntoma de diseño está resolviendo cada envoltura.



