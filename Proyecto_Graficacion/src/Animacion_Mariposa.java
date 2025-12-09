// Animacion_Mariposa
public class Animacion_Mariposa {

    // Posición inicial de la mariposa
    public float x = -6f;
    public float y = 0.0f;
    public float z = 2.5f;

    // Orientación y aleteo
    public float angleYaw = 0f;
    public float wingAngle = 0f;

    // Estados: 0=volando, 1=acercando, 2=posada
    private int state = 0;

    // Posición REAL de la flor (por la traslación en el render)
    private final float flowerX = 0f;
    private final float flowerY = -1.5f;
    private final float flowerZ = 0f;

    //  Posición final guardada
    private boolean landed = false;
    private float landX, landY, landZ, landYaw;

    public void update(float t) {
        // Aleteo en vuelo
        wingAngle = (float) (30 * Math.sin(t * 12));

        switch (state) {
            case 0 -> {      // VUELO LIBRE
                x = (float) (-6 + 4 * Math.sin(t * 0.6) + 2 * Math.cos(t * 0.3));
                z = (float) (2.5 * Math.cos(t * 0.5));
                y = (float) (1.1 + 0.4 * Math.sin(t * 1.6));

                if (Math.hypot(x - flowerX, z - flowerZ) < 2.8f && t > 6f) {
                    state = 1;
                }

                angleYaw = (float) Math.toDegrees(
                        Math.atan2(flowerX - x, flowerZ - z)
                );
            }

            case 1 -> {      // ACERCAMIENTO
                x += (flowerX - x) * 0.02f;
                z += (flowerZ - z) * 0.02f;

                float targetY = flowerY + 0.8f;
                y += (targetY - y) * 0.02f;

                angleYaw = (float) Math.toDegrees(
                        Math.atan2(flowerX - x, flowerZ - z)
                );

                if (Math.hypot(x - flowerX, z - flowerZ) < 0.25f &&
                    Math.abs(y - targetY) < 0.1f) {

                    state = 2;
                }
            }

            case 2 -> {      // POSADA
                // Guardar posición donde aterrizó 
                if (!landed) {
                    landed = true;
                    landX = x;
                    landY = y;
                    landZ = z;
                    landYaw = angleYaw;
                }

                // Mantener esa posición sin moverla 
                x = landX;
                y = landY;
                z = landZ;
                angleYaw = landYaw;

                // Aleteo suave mientras está posada
                wingAngle = (float) (10 * Math.sin(t * 4));
            }
        }
    }

    // Reiniciar la animación
    public void reset() {
        state = 0;
        landed = false;
        x = -6f;
        y = 0f;
        z = 2.5f;
    }
}
