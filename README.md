# Voltea (APK)
1. Sube TODO el contenido de esta carpeta a un repositorio de GitHub (la carpeta .github debe quedar en la raíz).
2. Ve a Actions > Build APK > Run workflow (o haz un push a main).
3. Al terminar, descarga el artefacto "Voltea-APK" y descomprímelo: dentro está app-debug.apk.
La app no pide permiso de internet: todo queda en el teléfono.

## Fondo animado (video del dispositivo)
- Botón con un ícono de cámara de video, en el centro del grupo de ajustes de ambiente (música · video · imagen), abajo a la derecha.
- Cada pantalla (inicio, carpeta y estudio) tiene su propio fondo animado, igual que el de imagen.
- Se elige un video del dispositivo. Opciones: ajuste, bucle, atrasar y adelantar 10 s, pausa, volumen, silenciar y "Quitar fondo animado" (vuelve el fondo de imagen).
- Mientras el video suene, la música de fondo se detiene. Si silencias el video (o bajas su volumen a 0), puedes activar la música de fondo.
- El video no responde a toques. No usa internet.


## Ordenar carpetas
- En el inicio, cada carpeta tiene un asa de seis puntos (⠿) a la derecha. Mantén el dedo sobre ella y arrastra la carpeta al lugar que quieras; las demás se acomodan solas.
- También funciona con una pulsación larga sobre la carpeta (en cualquier parte que no sea un botón).
- Al acercarte al borde de la pantalla, la lista se desplaza sola.
- Con teclado o lector de pantalla: enfoca el asa y usa las flechas ↑ ↓.
- El orden se guarda en el dispositivo y también va dentro de la copia de seguridad (.json).

## Ordenar tarjetas
- En Editar, cada tarjeta tiene también su asa (⠿) a la izquierda: arrástrala para cambiarla de lugar, o mantén pulsado sobre el texto de la tarjeta.
- Funciona igual que con las carpetas: autodesplazamiento en los bordes, flechas ↑ ↓ con teclado, y el orden queda guardado y dentro de la copia de seguridad.

## Lista de reproducción (música de fondo)
- En las opciones de audio (botón de la nota musical), debajo de "Bucle", está la fila "Lista de reproducción" con su botón (ícono de fila de reproducción). Al tocarlo se abre un panel que no ocupa toda la pantalla: si hay más canciones de las que caben, se desliza con el dedo para ver las de abajo.
- Cada pantalla (inicio y cada carpeta) tiene su propia lista. El audio que ya tenías guardado pasa solo a ser la primera canción de la lista.
- **Agregar**: elige una o varias canciones del dispositivo a la vez. **Editar**: muestra un botón para quitar cada canción.
- **Orden**: arrastra el asa (⠿) de cada canción, o mantén pulsado sobre ella. Con teclado, enfoca el asa y usa ↑ ↓. Toca una canción para reproducirla; el botón redondo de la canción actual pausa o reanuda.
- **Aleatorio**: la siguiente canción se elige al azar, sin repetir hasta haber sonado todas.
- **Bucle**: el ciclo no se interrumpe al terminar el audio; continúa con la canción que sigue (o con el aleatorio si está activo) y, al llegar al final, vuelve a empezar. Sin bucle, la lista se detiene al terminar la última.
- "Bucle" de las opciones de audio sigue repitiendo solo la canción actual.
- Con más de una canción aparecen los botones de canción anterior / siguiente junto al reproductor.
