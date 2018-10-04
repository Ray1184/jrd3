package Scripts.utils

import org.joml.Quaternionf
import org.joml.Vector3f
import org.jrd3.engine.core.items.*
import org.jrd3.engine.core.loaders.binary.BinaryFileLoader
import org.jrd3.engine.core.scene.Scene
import org.jrd3.engine.playenv.interaction.map.WalkMap
import org.jrd3.engine.playenv.objects.inventory.InventoryObject

import java.awt.*

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
    static Scene currentScene;
    static final Color BLACK = new Color(0, 0, 0)
    static final Color GOLD = new Color(250, 200, 40)
    static final Color WHITE = new Color(255, 255, 255)

    private static final Map<String, Object> VARIABLES = new HashMap<>();
    private static float fadeCounter;
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

    // Scene issues and IO functions.

    static ModelItem getDaeModel(String path) {
        String fileName = Thread.currentThread().getContextClassLoader()
                .getResource("Models/" + path).getFile()
        File file = new File(fileName)
        return ModelItem.get(file.getAbsolutePath(), "")
    }

    static ModelItem getOBJModel(String path, String texturePath) {
        String fileName = Thread.currentThread().contextClassLoader
                .getResource("Models/" + path).file
        File file = new File(fileName)
        String fileNameTex = Thread.currentThread().contextClassLoader
                .getResource("Textures/" + texturePath).file
        File fileTex = new File(fileNameTex)
        return ModelItem.get(file.getAbsolutePath(), fileTex.getAbsolutePath())
    }

    static SceneNode createTransformAndMoveNode(String name, ModelItem model) {
        SceneNode tNode = new SceneNode("T_" + name)
        tNode.attachGeometry(model)
        SceneNode mNode = new SceneNode(name)
        mNode.addChild(tNode)
        return mNode
    }

    static Picture getBackPicture(String path, Picture.Mode mode) {
        return Picture.get("/Textures/" + path, mode)
    }

    static TextPicture text(String text, int x, int y, Color color, boolean center) {
        return text(text, x, y, color, center, 0)
    }

    static TextPicture text(String text, int x, int y, Color color, boolean center, int maxLength) {
        StringBuilder sb = new StringBuilder(text);
        if (maxLength > 0) {
            int i = 0;
            while (i + maxLength < sb.length() && (i = sb.lastIndexOf(" ", i + maxLength)) != -1) {
                sb.replace(i, i + 1, "\n");
            }
        }
        return TextPicture.get(sb.toString(), x, y, TextPicture.DEFAULT_FONT, color, center)
    }

    static WalkMap getWalkMap(String path) {
        return BinaryFileLoader.loadWalkMap("/Maps/" + path)
    }

    static WalkMap getViewMap(String path) {
        return BinaryFileLoader.loadViewMap("/Maps/" + path)
    }

    static WalkMap getPathsMap(String path) {
        return BinaryFileLoader.loadPathsMap("/Maps/" + path)
    }

    static ModelItem dropPickeableObject(ModelItem player, InventoryObject object, Vector3f position, Quaternionf rotation, float scale, Closure action) {
        // If null, already picked.
        if (object != null) {
            ModelItem item3d = getOBJModel("Objs/" + object.getModelName(), "Objs/")
            item3d.setPosition(position)
            item3d.setRotation(rotation)
            item3d.setScale(scale)
            item3d.addController([
                    init  : { Actor actor ->

                    },
                    update: { Actor actor, float tpf ->
                        if (actor != null) {
                            Vector3f pos = player.getPosition()
                            Vector3f posItem3d = item3d.getPosition()

                            if (Keys.enter == 1 && pos.distance(posItem3d) < 1) {
                                if (object != null) {
                                    action.call(object)
                                }
                            }
                        }
                    }
            ] as ActorController)
            return item3d

        }
        return null;

    }
    // ADD HERE NEW FUNCTIONS


}
