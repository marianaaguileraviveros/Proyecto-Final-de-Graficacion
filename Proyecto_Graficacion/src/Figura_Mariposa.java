// Figura_Mariposa
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public class Figura_Mariposa {

    private final Texture texture;

    public Figura_Mariposa(Texture texture) {
        this.texture = texture;
    }

    public void draw(GL2 gl, float wingAngle) {

        // Habilita que el material responda al color 
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
        
        if (texture != null) texture.disable(gl); 

        gl.glPushMatrix();
        gl.glColor3f(0.0f, 0.0f, 0.0f);  // Color de la mariposa
        gl.glScalef(0.12f, 0.12f, 0.50f);
        drawCube(gl);
        gl.glPopMatrix();

        drawAntenas(gl);

        gl.glColor3f(1f, 1f, 1f);

        // Textura de las alas
        if (texture != null) {
            texture.enable(gl);
            texture.bind(gl);
        }

        // Ala derecha
        gl.glPushMatrix();
        gl.glTranslatef(0.14f, 0f, 0f);
        gl.glRotatef(wingAngle, 0f, 0f, 1f);
        drawWing(gl, false);
        gl.glPopMatrix();

        // Ala izquierda
        gl.glPushMatrix();
        gl.glTranslatef(-0.14f, 0f, 0f);
        gl.glRotatef(-wingAngle, 0f, 0f, 1f);
        drawWing(gl, true);
        gl.glPopMatrix();

        if (texture != null) texture.disable(gl);

        gl.glDisable(GL2.GL_COLOR_MATERIAL);
    }

    // Antenas
    private void drawAntenas(GL2 gl) {

        gl.glColor3f(0f, 0f, 0f);  // Color de las antenas 

        // Antena derecha
        gl.glPushMatrix();
        gl.glTranslatef(0.06f, 0.02f, 0.26f);   
        gl.glRotatef(35, 0, 0, 1);
        drawAntena(gl);
        gl.glPopMatrix();

        // Antena izquierda
        gl.glPushMatrix();
        gl.glTranslatef(-0.06f, 0.02f, 0.26f);
        gl.glRotatef(-35, 0, 0, 1);
        drawAntena(gl);
        gl.glPopMatrix();
    }

    private void drawAntena(GL2 gl) {

        gl.glPushMatrix();
        gl.glScalef(0.015f, 0.015f, 0.30f);  // Tamaño del palo de la antena
        drawCube(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, 0.18f);  
        drawSphere(gl, 0.05f, 12, 12);    // Radio pequeño de las antenas 
        gl.glPopMatrix();
    }

    // Ala
    private void drawWing(GL2 gl, boolean left) {

        gl.glDisable(GL2.GL_CULL_FACE);

        gl.glBegin(GL2.GL_QUADS);
        gl.glNormal3f(0, 0, 1);   // Normal hacia el frente para iluminación

        float w = 0.9f;
        float h = 1.4f;

        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(0f, -h * 0.35f, 0f);

        gl.glTexCoord2f(1f, 0f);
        gl.glVertex3f(left ? -w : w, -h * 0.15f, 0f);

        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(left ? -w : w, h * 0.60f, 0f);

        gl.glTexCoord2f(0f, 1f);
        gl.glVertex3f(0f, h * 0.15f, 0f);

        gl.glEnd();

        gl.glEnable(GL2.GL_CULL_FACE);
    }

    // Cubo para cuerpo 
    private void drawCube(GL2 gl) {

        gl.glBegin(GL2.GL_QUADS);

        // frente
        gl.glNormal3f(0,0,1);
        gl.glVertex3f(-0.5f,-0.5f,0.5f);
        gl.glVertex3f(0.5f,-0.5f,0.5f);
        gl.glVertex3f(0.5f,0.5f,0.5f);
        gl.glVertex3f(-0.5f,0.5f,0.5f);

        // atrás
        gl.glNormal3f(0,0,-1);
        gl.glVertex3f(-0.5f,-0.5f,-0.5f);
        gl.glVertex3f(-0.5f,0.5f,-0.5f);
        gl.glVertex3f(0.5f,0.5f,-0.5f);
        gl.glVertex3f(0.5f,-0.5f,-0.5f);

        // izquierda
        gl.glNormal3f(-1,0,0);
        gl.glVertex3f(-0.5f,-0.5f,-0.5f);
        gl.glVertex3f(-0.5f,-0.5f,0.5f);
        gl.glVertex3f(-0.5f,0.5f,0.5f);
        gl.glVertex3f(-0.5f,0.5f,-0.5f);

        // derecha
        gl.glNormal3f(1,0,0);
        gl.glVertex3f(0.5f,-0.5f,-0.5f);
        gl.glVertex3f(0.5f,0.5f,-0.5f);
        gl.glVertex3f(0.5f,0.5f,0.5f);
        gl.glVertex3f(0.5f,-0.5f,0.5f);

        // arriba
        gl.glNormal3f(0,1,0);
        gl.glVertex3f(-0.5f,0.5f,-0.5f);
        gl.glVertex3f(-0.5f,0.5f,0.5f);
        gl.glVertex3f(0.5f,0.5f,0.5f);
        gl.glVertex3f(0.5f,0.5f,-0.5f);

        // abajo
        gl.glNormal3f(0,-1,0);
        gl.glVertex3f(-0.5f,-0.5f,-0.5f);
        gl.glVertex3f(0.5f,-0.5f,-0.5f);
        gl.glVertex3f(0.5f,-0.5f,0.5f);
        gl.glVertex3f(-0.5f,-0.5f,0.5f);

        gl.glEnd();
    }

    // Esfera de la antena 
    private void drawSphere(GL2 gl, float radius, int stacks, int slices) {

        // La esfera se dibuja por bandas (circunferencias)
        for (int i = 0; i < stacks; i++) {

            // Coordenadas polares para latitud
            float lat0 = (float) Math.PI * (-0.5f + (float)(i) / stacks);
            float z0 = (float) Math.sin(lat0);
            float zr0 = (float) Math.cos(lat0);

            float lat1 = (float) Math.PI * (-0.5f + (float)(i+1) / stacks);
            float z1 = (float) Math.sin(lat1);
            float zr1 = (float) Math.cos(lat1);

            // Tira de triángulos más eficiente
            gl.glBegin(GL2.GL_TRIANGLE_STRIP);
            for (int j = 0; j <= slices; j++) {

                float lng = 2f * (float)Math.PI * (float)(j - 1) / slices;
                float x = (float)Math.cos(lng);
                float y = (float)Math.sin(lng);

                // Normal y vértice del anillo inferior
                gl.glNormal3f(x * zr0, y * zr0, z0);
                gl.glVertex3f(radius * x * zr0, radius * y * zr0, radius * z0);

                // Normal y vértice del anillo superior
                gl.glNormal3f(x * zr1, y * zr1, z1);
                gl.glVertex3f(radius * x * zr1, radius * y * zr1, radius * z1);
            }
            gl.glEnd();
        }
    }
}
