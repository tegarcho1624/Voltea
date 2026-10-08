# Voltea (APK)
1. Sube TODO el contenido de esta carpeta a un repositorio de GitHub (la carpeta .github debe quedar en la raíz).
2. Ve a Actions > Build APK > Run workflow (o haz un push a main).
3. Al terminar, descarga el artefacto "Voltea-APK" y descomprímelo: dentro está app-debug.apk.
Todo queda en el teléfono. La app solo usa internet para el fondo animado de YouTube (buscar y reproducir un video sin anuncios); sin ese fondo no hace ninguna conexión.

## Fondo animado
- Botón con un triángulo, a la izquierda del botón de fondo de imagen (entre el de volumen y el de imagen).
- Cada pantalla (inicio, carpeta y estudio) tiene su propio fondo animado, igual que el de imagen.
- Se puede elegir un video del dispositivo o buscar/pegar un enlace de YouTube (se reproduce sin anuncios con los mismos servidores que AloTube).
- Opciones: ajuste, bucle, atrasar y adelantar 10 s, pausa y "Quitar fondo animado" (vuelve el fondo de imagen).
- Mientras hay un video de fondo, la música de fondo se detiene. El video no responde a toques.
