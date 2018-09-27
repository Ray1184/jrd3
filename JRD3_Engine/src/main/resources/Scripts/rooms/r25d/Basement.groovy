package Scripts.rooms.r25d

import Scripts.utils.Env
import Scripts.utils.Keys
import org.joml.Quaternionf
import org.joml.Vector3f
import org.jrd3.engine.core.graph.SceneLight
import org.jrd3.engine.core.graph.anim.Animation
import org.jrd3.engine.core.items.ModelItem
import org.jrd3.engine.core.items.SceneNode
import org.jrd3.engine.core.loaders.binary.BinaryFileLoader
import org.jrd3.engine.core.sim.AbstractState
import org.jrd3.engine.core.sim.MouseInput
import org.jrd3.engine.core.sim.Window
import org.jrd3.engine.playenv.controllers.HeightAdapterController
import org.jrd3.engine.playenv.controllers.MovableActorController
import org.jrd3.engine.playenv.controllers.MovableAnimController
import org.jrd3.engine.playenv.controllers.SeekerActorController
import org.jrd3.engine.playenv.interaction.Collisor
import org.jrd3.engine.playenv.interaction.map.Sector

import static org.lwjgl.glfw.GLFW.*

sec1 = "S21"

actorInc = 0
actorRot = 0

// debug
x = 0f
y = 0f
z = 0f
rx = 0f
ry = 0f
rz = 0f
ratio = 0.1

animItem = {}

obj5 = {}
model5 = {}

def onInit(AbstractState state) {

    distance = new Vector3f()
    sec1 = "S21"

    Env.setVar("LAST", "Basement")
    Keys.initKeys()

    transformNode = new SceneNode("PL_NODE")
    transformNode2 = new SceneNode("PL_NODE2")
    transformNode3 = new SceneNode("PL_NODE3")
    moveNode = new SceneNode("FT_NODE")
    moveNode2 = new SceneNode("FT_NODE2")
    moveNode3 = new SceneNode("FT_NODE3")



    String fileName = Thread.currentThread().getContextClassLoader()
            .getResource("Models/CrippleAnim.dae").getFile()
    File file = new File(fileName)
    animItem = ModelItem.get(file.getAbsolutePath(), "")
    animItem.setScale(0.20f)
    animItem.setPosition(0, 0, 0)
    animItem.setAnimController(new MovableAnimController())
    animation = animItem.getCurrentAnimation() as Animation
    animation.play()
    animation.setCompleteAfterHalf(true)




    fileName = Thread.currentThread().getContextClassLoader()
            .getResource("Models/UncleAnim.dae").getFile()
    file = new File(fileName)
    animItem2 = ModelItem.get(file.getAbsolutePath(), "")
    animItem2.setScale(0.20) //0.20
    animItem2.setPosition(0, 0, 0)



    state.getScene().addModelItem(animItem)
    state.getScene().addModelItem(animItem2)


    transformNode.attachGeometry(animItem)
    transformNode.setPosition(0, 0, 0f)
    moveNode.addChild(transformNode)
    moveNode.setPosition(0f, 1.1f, 0.0f)
    moveNode.rotate(0, 77, 0)
    state.getScene().getRootNode().addChild(moveNode)

    transformNode2.attachGeometry(animItem2)
    transformNode2.setPosition(0, 0, 0f)
    moveNode2.addChild(transformNode2)
    moveNode2.setPosition(-1f, 1.1f, 0.0f)
    moveNode2.rotate(0, 77, 0)
    state.getScene().getRootNode().addChild(moveNode2)

    //moveNode3.setLocalTransform(hand.getTransformations().get())

    // Setup Lights
    SceneLight globalLight = new SceneLight()
    state.getScene().setSceneLight(globalLight)

    // Ambient Light
    globalLight.setAmbientLight(new Vector3f(1.0f, 1.0f, 1.0f))


    walkmap = BinaryFileLoader.loadWalkMap("/Maps/BasementW.jrd3m")
    viewmap = BinaryFileLoader.loadViewMap("/Maps/BasementV.jrd3m")
    pathsmap = BinaryFileLoader.loadPathsMap("/Maps/BasementP.jrd3m")
    collisor = new Collisor(walkmap)
    boolean stop = false
    controller = new MovableActorController(collisor)
    controller2 = new SeekerActorController(moveNode, pathsmap)
    controller3 = new HeightAdapterController(walkmap, 1.0f)
    controller3b = new HeightAdapterController(walkmap, 1.0f)



    moveNode.addController(controller)
    moveNode.addController(controller3)
    moveNode2.addController(controller2)
    moveNode2.addController(controller3b)

    // Restore
    if (Env.getVar("UPDATE")) {
        moveNode.setPosition(Env.getVar("ACTOR_P"))
        moveNode.setRotation(Env.getVar("ACTOR_R"))
        moveNode2.setPosition(Env.getVar("NPC_P"))
        moveNode2.setRotation(Env.getVar("NPC_R"))
        Env.setVar("UPDATE", false)

    } else {
        if (Env.getVar("ACTOR_S") != null) {
            sec1 = Env.getVar("ACTOR_S")
            moveNode.rotate(0, 180, 0)
        }

        Sector sPlayer = walkmap.getSectorByName().get(sec1)
        Sector sNpc1 = walkmap.getSectorByName().get("S30")
        moveNode.setPosition(sPlayer.triangle.center2f.x, 0, sPlayer.triangle.center2f.y)
        moveNode2.setPosition(sNpc1.triangle.center2f.x, 0, sNpc1.triangle.center2f.y)
    }

    // Pickable objects
    // Instructions [5]
    obj5 = Env.getVar("ALL_ROOM_OBJS")[5]
    if (obj5 != null) {

        fileName = Thread.currentThread().contextClassLoader
                .getResource("Models/Objs/" + obj5.modelName).file
        file = new File(fileName)
        def fileNameTex = Thread.currentThread().contextClassLoader
                .getResource("Textures/Objs/").file
        def fileTex = new File(fileNameTex)
        model5 = ModelItem.get(file.getAbsolutePath(), fileTex.getAbsolutePath())
        model5.scale = 0.04f
        model5.setRotation(new Quaternionf(0f, 0f, Math.toRadians(40) as float, 1f))
        model5.setPosition(-2.5f, 0.7f, 3.5f)
        state.scene.addModelItem(model5)
    }

    Env.initFade(state.scene)


}


def onUpdate(AbstractState state, Float tpf) {


    animation.setSpeed((75 * tpf) as int)
    //transformNode3.setPosition(x as float, y as float, z as float)
    viewmap.updateView(state.getCamera(), moveNode.getPosition().x, moveNode.getPosition().z)
    state.getScene().setBackground(viewmap.getCurrentBackground())
    state.getScene().setDepthMask(viewmap.getCurrentDepthMask())

    currentSec = walkmap.sampleSector(moveNode.getPosition().x, moveNode.getPosition().z, 0.0)


    controller.setMovStep(actorInc)
    controller.setRotStep(actorRot)

    controller2.seeking = false


}

def onInput(AbstractState state, Window window, MouseInput mouseInput) {
    //println "T-" + Texture.debMeshCount
    //println "M-" + Mesh.debMeshCount
    Env.updateFade()
    Keys.updateKeys(window)
    actorInc = 0
    actorRot = 0
    if (window.isKeyPressed(GLFW_KEY_W)) {
        if (animation != null) {
            animation.play()

        }
        actorInc = 1.2f
    } else if (window.isKeyPressed(GLFW_KEY_S)) {
        if (animation != null) {
            animation.play()

        }
        actorInc = -1.2f
    }
    if (window.isKeyPressed(GLFW_KEY_A)) {
        actorRot = 130
    } else if (window.isKeyPressed(GLFW_KEY_D)) {
        actorRot = -130
    }


    if (Keys.enter == 1 && (currentSec.name.equals("S118") || currentSec.name.equals("S122"))) {
        Env.fade({ v ->
            state.app.switchState("Garden")
        })
    }




    if (Keys.invent == 1) {
        saveCurrentVars()
        Env.fade({ v ->
            state.app.switchState("Inventory")
        })
    }

    // Picking
    if (model5 != null) {
        Vector3f pos = animItem.getPosition()
        Vector3f pos5 = model5.getPosition()

        if (Keys.enter == 1 && pos.distance(pos5) < 1) {
            def obj = Env.getVar("ALL_ROOM_OBJS")[5]
            if (obj != null) {
                saveCurrentVars()
                Env.setVar("CURRENT_PICKING", obj)
                Env.fade({ v ->
                    state.app.switchState("Picking")
                })
            }
        }
    }

    //println(x + "-" + y + "-" + z + "\n" + rx + "-" + ry + "-" + rz + "\n\n\n")

}

def onClose(AbstractState state) {

}

def saveCurrentVars() {
    Env.setVar("ACTOR_P", moveNode.getPosition())
    Env.setVar("ACTOR_R", moveNode.getRotation())

    Env.setVar("NPC_P", moveNode2.getPosition())
    Env.setVar("NPC_R", moveNode2.getRotation())
}

this