import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Cupboard3DRenderer extends JFrame implements GLEventListener, MouseListener, MouseMotionListener {

    private static final long serialVersionUID = 1L;
    private float angleX = 0;
    private float angleY = 0;
    private int prevMouseX, prevMouseY;
    private GLUT glut;
    private float[] color;

    public Cupboard3DRenderer(float[] color) {
        setTitle("3D Cupboard Renderer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.color = color;

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        GLCanvas glcanvas = new GLCanvas(capabilities);
        glcanvas.addGLEventListener(this);
        glcanvas.addMouseListener(this);
        glcanvas.addMouseMotionListener(this);

        getContentPane().add(glcanvas);
        setSize(800, 600);
        setLocationRelativeTo(null);

        glut = new GLUT();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Define color
            float[] brown = {0.5f, 0.35f, 0.05f};
            float[] black = {0.0f, 0.0f, 0.0f};
            float[] darkRed = {0.5f, 0.0f, 0.0f};
            float[] darkBlue = {0.0f, 0.0f, 0.5f};
            float[] darkBrown = {0.3f, 0.2f, 0.1f};
            float[] maroon = {0.5f, 0.0f, 0.0f};

            Cupboard3DRenderer renderer = new Cupboard3DRenderer(maroon);
            renderer.setVisible(true);
        });
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // Background color
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -5); // Move the cupboard away from the camera
        gl.glRotatef(-150, 0, 1, 0); // Rotate slightly for better view
        gl.glRotatef(angleX, 0, 1, 0); // Rotate around Y-axis
        gl.glRotatef(angleY, 1, 0, 0); // Rotate around X-axis

        renderCupboard(gl);

        gl.glFlush();
    }

    private void renderCupboard(GL2 gl) {
        // Set color
        gl.glColor3fv(color, 0); // Set color

        // Render cupboard body
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -1.0f, 0.0f); // Translate to sit on the ground
        gl.glScalef(1.5f, 2.0f, 0.8f); // Scale to adjust dimensions
        gl.glBegin(GL2.GL_QUADS);
        // Front face
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        // Back face
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);
        gl.glVertex3f(0.5f, 0.5f, -0.5f);
        gl.glVertex3f(0.5f, -0.5f, -0.5f);
        // Top face
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glVertex3f(0.5f, 0.5f, -0.5f);
        // Bottom face
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);
        gl.glVertex3f(0.5f, -0.5f, -0.5f);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        // Right face
        gl.glVertex3f(0.5f, -0.5f, -0.5f);
        gl.glVertex3f(0.5f, 0.5f, -0.5f);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        // Left face
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);
        gl.glEnd();
        gl.glPopMatrix();
    }


    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        double aspect = (double) width / (double) height;
        gl.glFrustum(-aspect, aspect, -1.0, 1.0, 2.0, 10.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        prevMouseX = e.getX();
        prevMouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        int deltaX = e.getX() - prevMouseX;
        int deltaY = e.getY() - prevMouseY;

        angleY += deltaX * 0.5f;
        angleX += deltaY * 0.5f;

        prevMouseX = e.getX();
        prevMouseY = e.getY();

        GLCanvas canvas = (GLCanvas) e.getSource();
        canvas.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}
