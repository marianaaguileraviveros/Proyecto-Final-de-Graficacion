// Renderizador.java
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.InputStream;

public class Renderizador implements GLEventListener {      

    // Objeto para manejar cámara y proyecciones 3D
    private final GLU glu = new GLU();      

    // Cámara
    public float yaw = 0f;
    public float pitch = 20f;
    public float distance = 12f;
    public float camX = 0f;
    public float camY = 1.5f;
    public float anguloFlor = 0f;

    // Texturas
    private Texture texMariposa;
    private Texture texFondo;

    private Figura_Mariposa mariposa;
    private Figura_Flor flor;
    private Animacion_Mariposa anim;

    private long startTime;

    // Configuraciones iniciales y carga de recursos
    @Override
    public void init(GLAutoDrawable gld) {

        startTime = System.currentTimeMillis();
        GL2 gl = gld.getGL().getGL2();

        gl.glEnable(GL2.GL_DEPTH_TEST);    
        gl.glEnable(GL2.GL_NORMALIZE);     // Normalizar iluminación aun con escalado

        gl.glClearColor(0f, 0f, 0f, 1f);   // Color del fondo (negro)

        // Iluminación
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        float[] amb = {0.2f, 0.2f, 0.2f, 1f};
        float[] dif = {0.95f, 0.95f, 0.9f, 1f};
        float[] pos = {6f, 8f, 6f, 1f};

        // Propiedades de la luz
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, amb, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, dif, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos, 0);

        // Cargar texturas 
        texMariposa = loadTexture("/imagenes/mariposa_ala.png", gl);
        texFondo    = loadTexture("/imagenes/Fondo.png", gl);

        // Crear figuras
        mariposa = new Figura_Mariposa(texMariposa);
        flor     = new Figura_Flor(); 
        anim     = new Animacion_Mariposa();

        gl.glShadeModel(GL2.GL_SMOOTH); 
    }

    // Cargar texturas  
    private Texture loadTexture(String path, GL2 gl) {
        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream == null) {
                System.err.println("ERROR: No se encontró imagen: " + path);
                return null;
            }

            TextureData data = TextureIO.newTextureData(
                    gl.getGLProfile(),
                    stream,
                    false,
                    "png"
            );

            return TextureIO.newTexture(data);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Dibujo general 
    @Override
    public void display(GLAutoDrawable gld) {

        GL2 gl = gld.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        // Diujar fondo
        drawBackground(gl);

        // Camara 3D
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        // Convertir grados a radianes 
        double ry = Math.toRadians(yaw);
        double rp = Math.toRadians(pitch);

        // Calcula la posición de la cámara
        float cx = camX + (float)(distance * Math.cos(rp) * Math.sin(ry));
        float cz = (float)(distance * Math.cos(rp) * Math.cos(ry));
        float cy = camY + (float)(distance * Math.sin(rp));

        glu.gluLookAt(cx, cy, cz, camX, camY, 0f, 0f, 1f, 0f);

        // Flor 3D
        gl.glPushMatrix();
        gl.glTranslatef(0f, -1.5f, 0f);   
        gl.glRotatef(anguloFlor, 0, 1, 0);  
        flor.draw(gl);
        gl.glPopMatrix();

        // Animación mariposa
        float t = (System.currentTimeMillis() - startTime) / 1000f;
        anim.update(t);

        gl.glPushMatrix();
        gl.glTranslatef(anim.x, anim.y, anim.z);
        gl.glRotatef(anim.angleYaw, 0, 1, 0);
        mariposa.draw(gl, anim.wingAngle);
        gl.glPopMatrix();
    }

    // Fondo 2D 
   private void drawBackground(GL2 gl) {

    if (texFondo == null) return;

    // A menor distancia, más zoom del fondo
    float zoom = 12f / distance;

    // Limites 
    if (zoom < 0.55f) zoom = 0.55f;
    if (zoom > 1.60f) zoom = 1.60f;

    // Desactivar iluminación y profundidad para dibujar 2D
    gl.glDisable(GL2.GL_LIGHTING);
    gl.glDisable(GL2.GL_DEPTH_TEST);

    gl.glMatrixMode(GL2.GL_PROJECTION);
    gl.glPushMatrix();
    gl.glLoadIdentity();  

    gl.glOrtho(0, 1, 0, 1, -1, 1);  // Proyección ortográfica

    gl.glMatrixMode(GL2.GL_MODELVIEW);
    gl.glPushMatrix();
    gl.glLoadIdentity();

    texFondo.enable(gl);
    texFondo.bind(gl);

    float cx = 0.5f;
    float cy = 0.5f;

    float w = 0.5f * zoom;
    float h = 0.5f * zoom;

    // Dibujar rectángulo con textura de fondo
    gl.glBegin(GL2.GL_QUADS);
    gl.glTexCoord2f(0, 0); gl.glVertex2f(cx - w, cy - h);
    gl.glTexCoord2f(1, 0); gl.glVertex2f(cx + w, cy - h);
    gl.glTexCoord2f(1, 1); gl.glVertex2f(cx + w, cy + h);
    gl.glTexCoord2f(0, 1); gl.glVertex2f(cx - w, cy + h);
    gl.glEnd();

    texFondo.disable(gl);

    // Restaurar configuración 3D
    gl.glMatrixMode(GL2.GL_PROJECTION);
    gl.glPopMatrix();

    gl.glMatrixMode(GL2.GL_MODELVIEW);
    gl.glPopMatrix();

    gl.glEnable(GL2.GL_DEPTH_TEST);
    gl.glEnable(GL2.GL_LIGHTING);
}

    //  Ajuste de ventana 
    @Override
    public void reshape(GLAutoDrawable gld, int x, int y, int w, int h) {
        GL2 gl = gld.getGL().getGL2();
        if (h == 0) h = 1;

        float asp = (float) w / h;   // Relación de aspecto

        gl.glViewport(0, 0, w, h);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(60, asp, 0.1, 200);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    // Liberar memoria
    @Override
    public void dispose(GLAutoDrawable drawable) {}

    // Restaurar valores iniciales de la cámara
    public void resetCamera() {
        yaw = 0;
        pitch = 20;
        distance = 12;
        camX = 0;
        camY = 1.5f;
    }
}
