package Scripts.utils

import org.joml.Quaternionf
import org.joml.Vector3f
import org.jrd3.engine.core.graph.anim.Animation
import org.jrd3.engine.core.items.*
import org.jrd3.engine.core.loaders.binary.BinaryFileLoader
import org.jrd3.engine.core.scene.Scene
import org.jrd3.engine.playenv.interaction.map.WalkMap
import org.jrd3.engine.playenv.objects.inventory.InventoryObject

import java.awt.*

import static org.lwjgl.glfw.GLFW.*

/**
 * Lot of static functions for common issues and user defined functions.
 *
 * @author Ray1184
 * @version 1.0
 */
class Env {

    @FunctionalInterface
    interface Action {
        Void perform(Void v)
    }

    enum FadeType {
        IN,
        OUT
    }

    static float fadeRatio = 0.02f
    static float textRatio = 0.02f
    static float textFade = 4f
    static Scene currentScene
    static final Color BLACK = new Color(0, 0, 0)
    static final Color GOLD = new Color(250, 200, 40)
    static final Color WHITE = new Color(255, 255, 255)

    static final int SHOOT_TYPE_NONE = 0
    static final int SHOOT_TYPE_PISTOL = 1
    static final int SHOOT_TYPE_RIFLE = 2
    static final int SHOOT_TYPE_THROW = 3

    private static boolean aiming = false

    private static final Map<String, Object> VARIABLES = new HashMap<>()
    private static float fadeCounter
    private static FadeType fadeType = FadeType.IN
    private static Action currentAction

    private static TextPicture currentText


    static void setVar(String name, Object val) {
        VARIABLES.put(name, val)
    }

    static Object getVar(String name) {
        return VARIABLES.get(name)
    }

    static Object getVar(String name, Object defaultValue) {
        Object val = VARIABLES.get(name)
        if (val != null) {
            return val
        } else {
            return defaultValue
        }
    }


    static void initFade(Scene scene) {
        currentScene = scene
        fadeCounter = 0
        fadeType = FadeType.IN
    }

    static void fade(Action action) {
        fadeType = FadeType.OUT
        currentAction = action
    }

    static void updateFade() {
        if (currentText != null) {
            if (textFade > 0) {
                textFade -= textRatio
                currentText.picture.alpha = textFade
            }
        }
        if (currentScene == null) {
            return
        }
        switch (fadeType) {
            case FadeType.OUT:
                if (fadeCounter > 0) {
                    fadeCounter -= fadeRatio
                    if (fadeCounter < 0) {
                        fadeCounter = 0
                    }
                } else {
                    if (currentAction != null) {
                        currentAction.perform(null)
                    }
                    currentAction = null
                }
                break
            case FadeType.IN:
                if (fadeCounter < 1) {
                    fadeCounter += fadeRatio
                    if (fadeCounter > 1) {
                        fadeCounter = 1
                    }
                }
                break
            default:
                break
        }
        currentScene.setGlobalAlpha(fadeCounter)

    }

    // Scene issues and IO functions.
    static TextPicture getMessage(String message, int x, int y, float fadeTime) {
        textFade = fadeTime
        currentText = text(message, x, y, GOLD, true, 40)
        return currentText
    }

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
        StringBuilder sb = new StringBuilder(text)
        if (maxLength > 0) {
            int i = 0
            while (i + maxLength < sb.length() && (i = sb.lastIndexOf(" ", i + maxLength)) != -1) {
                sb.replace(i, i + 1, "\n")
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

    static ModelItem dropPickeableObject(ModelItem player, InventoryObject object, Vector3f position, Quaternionf rotation, float scale, Closure action, float distance) {
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

                            if (Keys.enter == 1 && pos.distance(posItem3d) < distance) {
                                if (object != null) {
                                    action.call(object)
                                }
                            }
                        }
                    }
            ] as ActorController)
            return item3d

        }
        return null

    }

    static void manageShooting(Animation animation, org.jrd3.engine.core.sim.Window window, float tpf) {
        int shootType = Env.getVar("SHOOTING_TYPE", SHOOT_TYPE_NONE)


        if (window.isKeyPressed(GLFW_KEY_W) && !window.isKeyPressed(GLFW_KEY_SPACE)) {
            if (animation != null) {
                animation.setFirst(0)
                animation.setLast(81)
                animation.setCompleteAfterHalf(true)
                animation.setInvSpeed(2)
                animation.play()

            }

        } else if (window.isKeyPressed(GLFW_KEY_S) && !window.isKeyPressed(GLFW_KEY_SPACE)) {
            if (animation != null) {

                animation.setFirst(0)
                animation.setLast(81)
                animation.setCompleteAfterHalf(true)
                animation.setInvSpeed(2)
                animation.play()


            }

        }


        switch (shootType) {

            case SHOOT_TYPE_PISTOL:

                if (window.isKeyPressed(GLFW_KEY_SPACE) && !aiming) {
                    if (Keys.space == 1) {
                        println animation.getCurrentFrameIndex()
                        animation.setCurrentFrameIndex(81)
                        animation.setFirst(81)
                        animation.setLast(88)
                        animation.setCompleteAfterHalf(false)
                        animation.setInvSpeed(3)
                        //animation.setCycleCompleted(false)


                    }
                    //System.out.println(animation.currentFrameIndex);
                    if (animation.getCurrentFrameIndex() < 87) {
                        animation.play()

                    } else {
                        aiming = true

                    }


                } else if (window.isKeyRelease(GLFW_KEY_SPACE) && aiming) {

                    if (Keys.spaceReleased == 1) {
                        animation.setCurrentFrameIndex(88)
                        animation.setFirst(88)
                        animation.setLast(94)
                        animation.setCompleteAfterHalf(false)
                        animation.setInvSpeed(3)


                    }
                    if (animation.getCurrentFrameIndex() >= 87 && animation.getCurrentFrameIndex() < 93) {
                        animation.play()

                    } else {
                        aiming = false
                        animation.setFirst(0)
                        animation.setLast(81)
                        animation.setCompleteAfterHalf(true)
                        animation.setInvSpeed(2)
                        animation.play()

                    }
                }

                /*if (animation.getCurrentFrameIndex() >= 92) {
                    animation.setCurrentFrameIndex(0)
                    animation.setFirst(0)
                    animation.setLast(81)
                    animation.setCompleteAfterHalf(true)
                }*/


                break


        }

    }

    // ADD HERE NEW FUNCTIONS


}
