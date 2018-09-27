package Scripts.rooms.r25d

import Scripts.utils.Env
import Scripts.utils.Keys
import org.joml.Quaternionf
import org.joml.Vector3f
import org.jrd3.engine.core.graph.SceneLight
import org.jrd3.engine.core.items.ModelItem
import org.jrd3.engine.core.items.SceneNode
import org.jrd3.engine.core.loaders.binary.BinaryFileLoader
import org.jrd3.engine.core.sim.AbstractState
import org.jrd3.engine.core.sim.MouseInput
import org.jrd3.engine.core.sim.Window
import org.jrd3.engine.playenv.controllers.HeightAdapterController
import org.jrd3.engine.playenv.controllers.MovableActorController
import org.jrd3.engine.playenv.controllers.MovableAnimController
import org.jrd3.engine.playenv.interaction.Collisor
import org.jrd3.engine.playenv.interaction.map.Sector

import static org.lwjgl.glfw.GLFW.*

actorInc = 0
actorRot = 0

def onInit(AbstractState state) {



    Env.setVar("LAST", "Garden")
    Keys.initKeys()

    transformNode = new SceneNode("PL_NODE")
    moveNode = new SceneNode("FT_NODE")



    String fileName = Thread.currentThread().getContextClassLoader()
            .getResource("Models/CrippleAnim.dae").getFile()
    File file = new File(fileName)
    animItem = ModelItem.get(file.getAbsolutePath())
    animItem.setScale(0.20f)
    animItem.setPosition(0, 0, 0)
    animItem.setAnimController(new MovableAnimController())
    animation = animItem.getCurrentAnimation()
    state.getScene().addModelItem(animItem)

    fileName = Thread.currentThread().getContextClassLoader()
            .getResource("Models/Ford_T.obj").getFile()
    file = new File(fileName)
    fordModel = ModelItem.get(file.getAbsolutePath())
    fordModel.setScale(1f)
    fordModel.setPosition(6, 0.7, 2)
    fordModel.setRotation(new Quaternionf().rotateXYZ(0f, Math.toRadians(-140) as float, 0f))
    state.getScene().addModelItem(fordModel)


    transformNode.attachGeometry(animItem)
    transformNode.setPosition(0, 0, 0f)
    moveNode.addChild(transformNode)
    moveNode.setPosition(0f, 1.1f, 0.0f)
    moveNode.rotate(0, 77, 0)
    state.getScene().getRootNode().addChild(moveNode)

    // Setup Lights
    SceneLight globalLight = new SceneLight()
    state.getScene().setSceneLight(globalLight)

    // Ambient Light
    globalLight.setAmbientLight(new Vector3f(1.0f, 1.0f, 1.0f))


    walkmap = BinaryFileLoader.loadWalkMap("/Maps/GardenW.jrd3m")
    viewmap = BinaryFileLoader.loadViewMap("/Maps/GardenV.jrd3m")
    pathsmap = BinaryFileLoader.loadPathsMap("/Maps/GardenP.jrd3m")
    collisor = new Collisor(walkmap)
    boolean stop = false
    controller = new MovableActorController(collisor)
    controller3 = new HeightAdapterController(walkmap, 1.0f)

    Sector s1 = walkmap.getSectorByName().get("S57")
    moveNode.setPosition(s1.triangle.center2f.x, 0, s1.triangle.center2f.y)

    moveNode.addController(controller)
    moveNode.addController(controller3)

    // Restore
    if (Env.getVar("UPDATE")) {
        moveNode.setPosition(Env.getVar("ACTOR_P"))
        moveNode.setRotation(Env.getVar("ACTOR_R"))

        Env.setVar("UPDATE", false)
    }

    Env.initFade(state.scene)
}


def onUpdate(AbstractState state, Float tpf) {

    animation.setSpeed((75 * tpf) as int)
    viewmap.updateView(state.getCamera(), moveNode.getPosition().x, moveNode.getPosition().z)
    state.getScene().setBackground(viewmap.getCurrentBackground())
    state.getScene().setDepthMask(viewmap.getCurrentDepthMask())


    currentSec = walkmap.sampleSector(moveNode.getPosition().x, moveNode.getPosition().z, 0.0)

    controller.setMovStep(actorInc)
    controller.setRotStep(actorRot)


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



    if (Keys.enter == 1 && (currentSec.name.equals("S53") || currentSec.name.equals("S58"))) {
        Env.setVar("ACTOR_S", "S121")
        Env.fade({ v ->
            state.app.switchState("Basement")
        })
    }



    if (Keys.invent == 1) {
        Env.setVar("ACTOR_P", moveNode.getPosition())
        Env.setVar("ACTOR_R", moveNode.getRotation())
        Env.fade({ v ->
            state.app.switchState("Inventory")
        })
    }


}

def onClose(AbstractState state) {

}

this