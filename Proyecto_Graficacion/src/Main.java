// Main.java
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {

        // Ejecuta el Swing (necesario para interfaces gráficas)
        SwingUtilities.invokeLater(() -> {  
            JFrame frame = new JFrame("Proyecto Mariposa");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1024, 720);
            frame.setLocationRelativeTo(null);  

            GLCanvas canvas = new GLCanvas();
            Renderizador renderer = new Renderizador();
            canvas.addGLEventListener(renderer);

            // Teclado cámara
            canvas.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {  // Detecta la tecla presionada y ajusta la cámara

                        case KeyEvent.VK_LEFT  -> renderer.yaw -= 5;
                        case KeyEvent.VK_RIGHT -> renderer.yaw += 5;

                        case KeyEvent.VK_UP    -> 
                                renderer.pitch = Math.max(-85, renderer.pitch - 5);
                        case KeyEvent.VK_DOWN  -> 
                                renderer.pitch = Math.min(85, renderer.pitch + 5);

                        case KeyEvent.VK_W -> 
                                renderer.distance = Math.max(2f, renderer.distance - 0.5f);
                        case KeyEvent.VK_S -> 
                                renderer.distance += 0.5f;

                        case KeyEvent.VK_R -> renderer.resetCamera();  // Cámara a posición inicial
                    }
                    canvas.display();     // Redibuja escena después de mover cámara
                }
            });

            frame.add(canvas, BorderLayout.CENTER);
            frame.setVisible(true);
            canvas.requestFocusInWindow();   // Dar foco al canvas para que el teclado funcione

            FPSAnimator animator = new FPSAnimator(canvas, 60);
            animator.start();
        });
    }
}
