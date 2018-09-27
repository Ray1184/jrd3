package org.jrd3.engine.core.sim;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.sim.Window.WindowOptions;
import org.jrd3.engine.core.utils.ErrorHandler;

public class Simulator implements Runnable {

    private static final int TARGET_FPS = 60;

    private static final int TARGET_UPS = 30;

    private final Window window;

    private final Thread gameLoopThread;

    private final Timer timer;

    private final CustomLogic gameLogic;

    private final MouseInput mouseInput;

    private final String windowTitle;

    private double lastFps;

    private byte updateMask;

    private int fps;

    public Simulator(String windowTitle, boolean vSync, WindowOptions opts, CustomLogic gameLogic) throws JRD3Exception {
        this(windowTitle, 0, 0, vSync, opts, gameLogic);
    }

    public Simulator(String windowTitle, int width, int height, boolean vSync, WindowOptions opts, CustomLogic gameLogic) throws JRD3Exception {
        this.windowTitle = windowTitle;
        gameLoopThread = new Thread(this, "MainLoop");
        window = new Window(windowTitle, width, height, vSync, opts);
        mouseInput = new MouseInput();
        this.gameLogic = gameLogic;
        timer = new Timer();
        updateMask = 0;
    }

    public void start() {
        String osName = System.getProperty("os.name");
        if (osName.contains("Mac")) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (JRD3Exception e) {
            ErrorHandler.handleStackTrace(e);
        } finally {
            try {
                cleanup();
            } catch (JRD3Exception e) {
                ErrorHandler.handleStackTrace(e);
            }
        }
    }

    protected void init() throws JRD3Exception {
        window.init();
        timer.init();
        mouseInput.init(window);
        gameLogic.init(window);
        lastFps = timer.getTime();
        fps = 0;
    }

    protected void gameLoop() throws JRD3Exception {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.windowShouldClose()) {

            // First one delegates to logic init.
            update(0);

            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            if (updateMask >= 2) {
                render();
            } else {
                updateMask++;
            }

            if (!window.isvSync()) {
                sync();
            }
        }
    }

    protected void cleanup() throws JRD3Exception {
        gameLogic.cleanup();
    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
            }
        }
    }

    protected void input() throws JRD3Exception {
        mouseInput.input(window);
        gameLogic.input(window, mouseInput);
    }

    protected void update(float interval) throws JRD3Exception {
        gameLogic.update(interval);
    }

    protected void render() throws JRD3Exception {
        if (window.getWindowOptions().showFps && timer.getLastLoopTime() - lastFps > 1) {
            lastFps = timer.getLastLoopTime();
            window.setWindowTitle(windowTitle + " - " + fps + " FPS");
            fps = 0;
        }
        fps++;
        gameLogic.render(window);
        window.update();
    }

}
