// Figura_Flor.java
import com.jogamp.opengl.GL2;

public class Figura_Flor {

    public void draw(GL2 gl) {
        gl.glPushMatrix();

        gl.glDisable(GL2.GL_CULL_FACE);
        gl.glEnable(GL2.GL_NORMALIZE);

        gl.glTranslatef(0f, -1.2f, 0f); // Ubicacion 
        gl.glScalef(0.68f, 0.68f, 0.68f); // Tamaño 

        drawStem(gl);
        drawPetals(gl);
        drawCenter(gl);

        gl.glPopMatrix(); 
    }

    // T A L L O  y  H O J A S
    private void drawStem(GL2 gl) {

        // Colores verdes oscuros
        float[] ambient = {0.06f, 0.28f, 0.06f, 1f};  
        float[] diffuse = {0.12f, 0.68f, 0.12f, 1f};  
        float[] spec    = {0.05f, 0.08f, 0.05f, 1f};  

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, spec, 0);
        gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 8f);

        float h = 2.2f; 
        float r = 0.10f;
        int slices = 36;

        for (int i = 0; i < slices; i++) {

            double th1 = Math.toRadians(i * (360.0 / slices));
            double th2 = Math.toRadians((i + 1) * (360.0 / slices)); 

            float x1 = (float)(r * Math.cos(th1));  // Usa coseno y seno para obtener puntos en la circunferencia.
            float z1 = (float)(r * Math.sin(th1));
            float x2 = (float)(r * Math.cos(th2));
            float z2 = (float)(r * Math.sin(th2));

            gl.glBegin(GL2.GL_QUADS);   // Se dibuja cada parte del tallo (cuadrado).

            gl.glNormal3f(x1, 0, z1);   // Permiten que la luz se refleje.
            gl.glVertex3f(x1, 0, z1);
            gl.glVertex3f(x1, h, z1);

            gl.glNormal3f(x2, 0, z2);   // Permiten que la luz se refleje.
            gl.glVertex3f(x2, h, z2);
            gl.glVertex3f(x2, 0, z2);

            gl.glEnd();
        }

        // Hoja 1
        gl.glPushMatrix();
        gl.glTranslatef(-0.35f, h * 0.40f, 0f);  // Ubicacion de la hoja.
        gl.glRotatef(38, 0, 0, 1);
        gl.glScalef(1f, 1f, 0.9f);
        drawLeaf(gl);
        gl.glPopMatrix();

        // Hoja 2
        gl.glPushMatrix();
        gl.glTranslatef(0.35f, h * 0.55f, 0f);  // Ubicacion de la hoja.
        gl.glRotatef(-38, 0, 0, 1);
        gl.glScalef(1f, 1f, 0.9f);
        drawLeaf(gl);
        gl.glPopMatrix();
    }

    // H O J A  
    private void drawLeaf(GL2 gl) {

        // Verde más brillante que el tallo
        float[] ambient = {0.08f, 0.36f, 0.08f, 1f};
        float[] diffuse = {0.18f, 0.85f, 0.12f, 1f};
        float[] spec    = {0.02f, 0.03f, 0.02f, 1f};

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, spec, 0);
        gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 6f);

        int segments = 28;
        float length = 0.95f;
        float width = 0.45f;

        for (int side = 0; side < 2; side++) {

            float normalY = (side == 0 ? 1f : -1f);
            float normalZ = (side == 0 ? 0.08f : -0.08f);

            for (int i = 0; i < segments; i++) {

                // Cada tirita sigue una curva para simular la forma de la hoja
                float t0 = i / (float)segments;
                float t1 = (i + 1) / (float)segments;

                float y0 = t0 * length;
                float y1 = t1 * length;

                // La hoja se dibuja en el centro
                float w0 = width * (float)Math.sin(Math.PI * t0);
                float w1 = width * (float)Math.sin(Math.PI * t1);

                float z0 = 0.12f * (float)Math.sin(t0 * Math.PI);
                float z1 = 0.12f * (float)Math.sin(t1 * Math.PI);

                gl.glBegin(GL2.GL_QUADS);

                gl.glNormal3f(0f, normalY, normalZ);
                gl.glVertex3f(-w0, y0, z0);
                gl.glVertex3f( w0, y0, z0);
                gl.glVertex3f( w1, y1, z1);
                gl.glVertex3f(-w1, y1, z1);

                gl.glEnd();
            }
        }
    }

    // P É T A L O S
    private void drawPetals(GL2 gl) {

        gl.glPushMatrix();
        gl.glTranslatef(0f, 2.25f, 0f);

        int num = 10;
        float height = 0.40f;
        float width  = 1.60f;
        float stretch = 1.35f;  // Estiramiento para pétalos largos

        for (int i = 0; i < num; i++) {

            gl.glPushMatrix();

            gl.glRotatef((360f / num) * i, 0, 1, 0);
            gl.glRotatef(-32f, 1, 0, 0);
            gl.glScalef(stretch, 1f, 1f);

            drawSinglePetal(gl, height, width);

            gl.glPopMatrix();
        }
        gl.glPopMatrix();
    }

    private void drawSinglePetal(GL2 gl, float height, float width) {

        // Petalos Naranjas
        float[] ambient = {0.55f, 0.23f, 0.07f, 1f};   
        float[] diffuse = {1.00f, 0.55f, 0.18f, 1f};   
        float[] spec    = {0.35f, 0.22f, 0.10f, 1f};   

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, spec, 0);
        gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 12f);

        int segments = 28;  // El pétalo con 28 partes para la curva

        for (int i = 0; i < segments; i++) {

            float t0 = i / (float)segments;
            float t1 = (i + 1) / (float)segments;

            float y0 = t0 * height;
            float y1 = t1 * height;

            // Curvatura hacia abajo (cóncavo)
            float curve0 = (float)Math.sin(t0 * Math.PI);
            float curve1 = (float)Math.sin(t1 * Math.PI);

            float z0 = -0.30f * curve0;
            float z1 = -0.30f * curve1;

            // El ancho disminuye al acercarse a la punta del pétalo
            float w0 = width * (0.4f + 0.6f * (1 - t0));
            float w1 = width * (0.4f + 0.6f * (1 - t1));

            gl.glBegin(GL2.GL_QUADS);

            gl.glNormal3f(0f, 0.85f, 0.28f);
            gl.glVertex3f(-w0, y0, z0);
            gl.glVertex3f( w0, y0, z0);
            gl.glVertex3f( w1, y1, z1);
            gl.glVertex3f(-w1, y1, z1);

            gl.glEnd();
        }
    }

    // C E N T R O 
    private void drawCenter(GL2 gl) {

        gl.glPushMatrix();
        gl.glTranslatef(0f, 2.60f, 0f); // Se coloca encima de todos los pétalos

        // Marrón rojizo brillante
        float[] ambient  = {0.35f, 0.12f, 0.10f, 1f};   
        float[] diffuse  = {0.60f, 0.20f, 0.15f, 1f}; 

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 40f);

        float r = 0.33f;
        int stacks = 20;  // Divisiones verticales
        int slices = 36;  // Divisiones horizontales

        // Se arma el centro con las divisiones 
        for (int i = 0; i < stacks; i++) {

            double lat0 = Math.PI * (-0.5 + (double)i / stacks);
            double lat1 = Math.PI * (-0.5 + (double)(i + 1) / stacks);

            double z0 = Math.sin(lat0);
            double zr0 = Math.cos(lat0);
            double z1 = Math.sin(lat1);
            double zr1 = Math.cos(lat1);

            gl.glBegin(GL2.GL_QUAD_STRIP);

            for (int j = 0; j <= slices; j++) {

                double lng = 2 * Math.PI * j / slices;
                double x = Math.cos(lng);
                double y = Math.sin(lng);

                gl.glNormal3d(x * zr0, y * zr0, z0);
                gl.glVertex3d(r * x * zr0, r * y * zr0, r * z0);

                gl.glNormal3d(x * zr1, y * zr1, z1);
                gl.glVertex3d(r * x * zr1, r * y * zr1, r * z1);
            }
            gl.glEnd();
        }

        float[] noEmission = {0f, 0f, 0f, 1f};
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, noEmission, 0);

        gl.glPopMatrix();
    }
}
