package PacSim.Graphics;

import PacSim.Game.Game;
import PacSim.Game.Movement;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Display {
    private static long window;
    private Game game;
    private static final int offWidth = 128;
    private static final int offHeight = 128;
    public static int WIDTH = 224 + offWidth;
    public static int HEIGHT = 320 + offHeight;

    private boolean isRunning = false;
    private static Display instance = null;

    private Display() {

    }

    public static Display getSingleton() {
        if (instance == null)
            instance = new Display();

        return instance;
    }

    public void run() {
        if (isRunning)
            return;

        isRunning = true;

        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(WIDTH, HEIGHT, "PacSim", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
        });

        try (MemoryStack stack = stackPush())
        {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        }

        glfwSetWindowSizeCallback(window, (windowHnd, xpos, ypos) -> resizeWindow_Callback(windowHnd, xpos, ypos));
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    public static void resizeWindow(int w, int h)
    {
        WIDTH = w + offWidth;
        HEIGHT = h + offHeight;

        glfwSetWindowSize(window, WIDTH, HEIGHT);
        glViewport(0, 0, WIDTH, HEIGHT);
    }

    private void resizeWindow_Callback(long windowHnd, int xpos, int ypos) {
        try (MemoryStack stack = stackPush())
        {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(windowHnd, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(windowHnd, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        }
    }

    private void loop()
    {
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        GL11.glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);

        GL11.glEnable(GL11.GL_ALPHA);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        game = new Game();

        while (!glfwWindowShouldClose(window))
        {
            if (glfwGetKey(window, GLFW_KEY_W) != GLFW_RELEASE)
                game.movePlayer(Movement.NORTH);

            if (glfwGetKey(window, GLFW_KEY_S) != GLFW_RELEASE)
                game.movePlayer(Movement.SOUTH);

            if (glfwGetKey(window, GLFW_KEY_A) != GLFW_RELEASE)
                game.movePlayer(Movement.WEST);

            if (glfwGetKey(window, GLFW_KEY_D) != GLFW_RELEASE)
                game.movePlayer(Movement.EAST);

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            game.render(64.0f, 64.0f, 0.0f, glfwGetTime());
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        Display.getSingleton().run();
    }
}
