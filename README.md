# Voltea (APK)
1. Sube TODO el contenido de esta carpeta a un repositorio de GitHub (la carpeta .github debe quedar en la raíz).
2. Ve a Actions > Build APK > Run workflow (o haz un push a main).
3. Al terminar, descarga el artefacto "Voltea-APK" y descomprímelo: dentro está app-debug.apk.
La app no pide permiso de internet: todo queda en el teléfono.

## Fondo animado (video del dispositivo)
- Botón con un triángulo, a la izquierda del botón de fondo de imagen (entre el de música y el de imagen).
- Cada pantalla (inicio, carpeta y estudio) tiene su propio fondo animado, igual que el de imagen.
- Se elige un video del dispositivo. Opciones: ajuste, bucle, atrasar y adelantar 10 s, pausa, volumen, silenciar y "Quitar fondo animado" (vuelve el fondo de imagen).
- Mientras el video suene, la música de fondo se detiene. Si silencias el video (o bajas su volumen a 0), puedes activar la música de fondo.
- El video no responde a toques. No usa internet.

