package Scripts.rooms.r25d

import Scripts.utils.Env
import Scripts.utils.Keys
import org.joml.Quaternionf
import org.joml.Vector3f
import org.jrd3.engine.core.graph.SceneLight
import org.jrd3.engine.core.graph.anim.Animation
import org.jrd3.engine.core.items.ActorController
import org.jrd3.engine.core.items.AnimModelItem
import org.jrd3.engine.core.items.ModelItem
import org.jrd3.engine.core.items.SceneNode
import org.jrd3.engine.core.sim.AbstractState
import org.jrd3.engine.core.sim.MouseInput
import org.jrd3.engine.core.sim.Window
import org.jrd3.engine.playenv.controllers.HeightAdapterController
import org.jrd3.engine.playenv.controllers.MovableActorController
import org.jrd3.engine.playenv.controllers.MovableAnimController
import org.jrd3.engine.playenv.interaction.Collisor
import org.jrd3.engine.playenv.interaction.map.Sector
import org.jrd3.engine.playenv.interaction.map.ViewMap
import org.jrd3.engine.playenv.interaction.map.WalkMap

import static org.lwjgl.glfw.GLFW.*

actorInc = 0
actorRot = 0
animItem = null as AnimModelItem
animation = null as Animation
fordModel = null as ModelItem
moveNode = null as SceneNode
walkmap = null as WalkMap
viewmap = null as ViewMap
controller = null as ActorController
currentSec = null as Sector

def onInit(AbstractState state) {



    Env.setVar("LAST", "Garden")
    Keys.initKeys()




    animItem = Env.getDaeModel(Env.getVar("PLAYER_ANIM"))
    animItem.setScale(0.2)
    animItem.setAnimController(new MovableAnimController())
    animation = animItem.getCurrentAnimation()
    animation.play()
    animation.setFirst(0)
    animation.setLast(81)
    animation.setCompleteAfterHalf(true)

    state.getScene().addModelItem(animItem)

    fordModel = Env.getOBJModel("Ford_T.obj", "")
    fordModel.setScale(1f)
    fordModel.setPosition(6, 0.7, 2)
    fordModel.setRotation(new Quaternionf().rotateXYZ(0f, Math.toRadians(-140) as float, 0f))
    state.getScene().addModelItem(fordModel)


    moveNode = Env.createTransformAndMoveNode("PlayerNode", animItem)
    moveNode.setPosition(0f, 1.1f, 0.0f)
    moveNode.rotate(0, 77, 0)
    state.getScene().getRootNode().addChild(moveNode)

    // Setup Lights
    SceneLight globalLight = new SceneLight()
    state.getScene().setSceneLight(globalLight)

    // Ambient Light
    globalLight.setAmbientLight(new Vector3f(1.0f, 1.0f, 1.0f))


    walkmap = Env.getWalkMap("GardenW.jrd3m")
    viewmap = Env.getViewMap("GardenV.jrd3m")
    def collisor = new Collisor(walkmap)
    controller = new MovableActorController(collisor)
    def controller3 = new HeightAdapterController(walkmap, 0.0f)

    Sector s1 = walkmap.getSectorByName().get("S57")
    moveNode.setPosition(s1.triangle.center2f.x, 0, s1.triangle.center2f.y)

    moveNode.addController(controller)
    moveNode.addController(controller3)

    // Restore
    if (Env.getVar("UPDATE")) {
        loadCurrentVars()

        Env.setVar("UPDATE", false)
    }


    def obj8 = Env.getVar("ALL_ROOM_OBJS")[8]
    def obj1 = Env.getVar("ALL_ROOM_OBJS")[1]
    def obj2 = Env.getVar("ALL_ROOM_OBJS")[2]
    def obj3 = Env.getVar("ALL_ROOM_OBJS")[3]
    def obj9 = Env.getVar("ALL_ROOM_OBJS")[9]
    def obj10 = Env.getVar("ALL_ROOM_OBJS")[10]
    def action = { o ->
        saveCurrentVars()
        Env.setVar("CURRENT_PICKING", o)
        Env.fade({ v ->
            state.app.switchState("Picking")
        })
    }
    def acid = Env.dropPickeableObject(animItem, obj8, new Vector3f(3.8, -0.0, 3.5), new Quaternionf(0f, 0f, 0f, 1f),
            0.1f, action, 1.5)

    def b1 = Env.dropPickeableObject(animItem, obj1, new Vector3f(4, -0.0, 3.5), new Quaternionf(0f, 0f, 0f, 1f),
            0.0f, action, 1.45)

    def b2 = Env.dropPickeableObject(animItem, obj2, new Vector3f(4, -0.0, 3.5), new Quaternionf(0f, 0f, 0f, 1f),
            0.0f, action, 1.45)

    def b3 = Env.dropPickeableObject(animItem, obj3, new Vector3f(4, -0.0, 3.5), new Quaternionf(0f, 0f, 0f, 1f),
            0.0f, action, 1.45)

    def spark = Env.dropPickeableObject(animItem, obj9, new Vector3f(4, -0.0, 3.5), new Quaternionf(0f, 0f, 0f, 1f),
            0.0f, action, 1.45)

    def shovel = Env.dropPickeableObject(animItem, obj10, new Vector3f(4, -0.0, 3.5), new Quaternionf(0f, 0f, 0f, 1f),
            0.0f, action, 1.45)

    if (acid != null) {
        state.scene.addModelItem(acid)
    }
    if (b1 != null) {
        state.scene.addModelItem(b1)
    }
    if (b2 != null) {
        state.scene.addModelItem(b2)
    }
    if (b3 != null) {
        state.scene.addModelItem(b3)
    }
    if (spark != null) {
        state.scene.addModelItem(spark)
    }
    if (shovel != null) {
        state.scene.addModelItem(shovel)
    }

    def msg = Env.getVar("SHOW_MESSAGE")
    if (msg != null) {
        def time = Env.getVar("MESSAGE_TIME", 4.0)
        def mess = Env.getMessage(msg, 0, 170, time)
        state.scene.addText(mess)
        Env.setVar("SHOW_MESSAGE", null)
    }

    Env.initFade(state.scene)
}


def onUpdate(AbstractState state, Float tpf) {

    animation.setInvSpeed((75 * tpf) as int)
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
        Env.fade({ v ->
            saveCurrentVars()
            state.app.switchState("Inventory")
        })
    }


}

def onClose(AbstractState state) {

}

def saveCurrentVars() {
    Env.setVar("ACTOR_P", moveNode.getPosition())
    Env.setVar("ACTOR_R", moveNode.getRotation())

}

def loadCurrentVars() {
    moveNode.setPosition(Env.getVar("ACTOR_P"))
    moveNode.setRotation(Env.getVar("ACTOR_R"))
}

this