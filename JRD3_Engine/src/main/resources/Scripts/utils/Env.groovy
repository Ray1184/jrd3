package Scripts.utils

import org.jrd3.engine.core.items.AnimModelItem
import org.jrd3.engine.core.items.ModelItem
import org.jrd3.engine.core.scene.Scene;


/**
 * Lot of static functions for common issues and user defined functions.
 *
 * @author Ray1184
 * @version 1.0
 */
class Env {

    @FunctionalInterface
    interface Action {
        Void perform(Void v);
    }

    enum FadeType {
        IN,
        OUT
    }

    static float fadeRatio = 0.02f;
    private static final Map<String, Object> VARIABLES = new HashMap<>();
    private static float fadeCounter;
    static Scene currentScene;
    private static FadeType fadeType = FadeType.IN;
    private static Action currentAction;

    static void setVar(String name, Object val) {
        VARIABLES.put(name, val);
    }

    static Object getVar(String name) {
        return VARIABLES.get(name);
    }


    static void initFade(Scene scene) {
        currentScene = scene;
        fadeCounter = 0;
        fadeType = FadeType.IN;
    }

    static void fade(Action action) {
        fadeType = FadeType.OUT;
        currentAction = action;
    }

    static void updateFade() {
        if (currentScene == null) {
            return;
        }
        switch (fadeType) {
            case FadeType.OUT:
                if (fadeCounter > 0) {
                    fadeCounter -= fadeRatio;
                    if (fadeCounter < 0) {
                        fadeCounter = 0;
                    }
                } else {
                    if (currentAction != null) {
                        currentAction.perform(null);
                    }
                    currentAction = null;
                }
                break;
            case FadeType.IN:
                if (fadeCounter < 1) {
                    fadeCounter += fadeRatio;
                    if (fadeCounter > 1) {
                        fadeCounter = 1;
                    }
                }
                break;
            default:
                break;
        }
        currentScene.setGlobalAlpha(fadeCounter);

    }


    // IO functions.

    AnimModelItem getDaeModel(String path) {
        String fileName = Thread.currentThread().getContextClassLoader()
                .getResource("Models/" + path).getFile()
        File file = new File(fileName)
        return ModelItem.get(file.getAbsolutePath(), "")
    }

    AnimModelItem getOBJModel(String path, String textureFolder) {
        String fileName = Thread.currentThread().contextClassLoader
                .getResource("Models/" + path).file
        File file = new File(fileName)
        String fileNameTex = Thread.currentThread().contextClassLoader
                .getResource("Textures/" + textureFolder).file
        File fileTex = new File(fileNameTex)
        return ModelItem.get(file.getAbsolutePath(), fileTex.getAbsolutePath())
    }

    // ADD HERE NEW FUNCTIONS


}
