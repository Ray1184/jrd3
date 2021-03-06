package Scripts.rooms.r25d

import Scripts.utils.Env
import Scripts.utils.Keys
import org.joml.Quaternionf
import org.joml.Vector3f
import org.jrd3.engine.core.graph.SceneLight
import org.jrd3.engine.core.graph.anim.Animation
import org.jrd3.engine.core.items.ActorController
import org.jrd3.engine.core.items.ModelItem
import org.jrd3.engine.core.items.SceneNode
import org.jrd3.engine.core.sim.AbstractState
import org.jrd3.engine.core.sim.MouseInput
import org.jrd3.engine.core.sim.Window
import org.jrd3.engine.playenv.controllers.HeightAdapterController
import org.jrd3.engine.playenv.controllers.MovableActorController
import org.jrd3.engine.playenv.controllers.MovableAnimController
import org.jrd3.engine.playenv.controllers.SeekerActorController
import org.jrd3.engine.playenv.interaction.Collisor
import org.jrd3.engine.playenv.interaction.map.Sector
import org.jrd3.engine.playenv.interaction.map.ViewMap
import org.jrd3.engine.playenv.interaction.map.WalkMap

import static org.lwjgl.glfw.GLFW.*

sec1 = "S21"

actorInc = 0
actorRot = 0

actorInc = 0
actorRot = 0
animItem = {}
animation = null as Animation
animItem2 = {}
animation2 = null as Animation
moveNode = null as SceneNode
moveNode2 = null as SceneNode
walkmap = null as WalkMap
viewmap = null as ViewMap
controller = null as ActorController
controller2 = null as ActorController
currentSec = null as Sector


def onInit(AbstractState state) {

    sec1 = "S21"

    Env.setVar("LAST", "Basement")
    Keys.initKeys()



    animItem = Env.getDaeModel(Env.getVar("PLAYER_ANIM"))
    animItem.setScale(0.2)
    animItem.setAnimController(new MovableAnimController())
    animation = animItem.getCurrentAnimation()

    animation.setFirst(0)
    animation.setLast(81)
    animation.setCompleteAfterHalf(true)




    animItem2 = Env.getDaeModel("UncleAnim.dae")
    animItem2.setScale(0.2)
    animItem2.setPosition(0, 0, 0)
    animItem2.setAnimController(new MovableAnimController())
    animation2 = animItem2.getCurrentAnimation()
    //animation2.play()
    animation2.setCompleteAfterHalf(true)



    state.getScene().addModelItem(animItem)
    state.getScene().addModelItem(animItem2)


    moveNode = Env.createTransformAndMoveNode("PlayerNode", animItem)
    moveNode.setPosition(0f, 1.1f, 3.0f)
    state.getScene().getRootNode().addChild(moveNode)

    moveNode2 = Env.createTransformAndMoveNode("NPC1Node", animItem2)
    moveNode2.setPosition(-1f, 1.1f, 0.0f)
    state.getScene().getRootNode().addChild(moveNode2)

    // Setup Lights
    SceneLight globalLight = new SceneLight()
    state.getScene().setSceneLight(globalLight)

    // Ambient Light
    globalLight.setAmbientLight(new Vector3f(1.0f, 1.0f, 1.0f))


    walkmap = Env.getWalkMap("BasementW.jrd3m")
    viewmap = Env.getViewMap("BasementV.jrd3m")
    def pathsmap = Env.getPathsMap("BasementP.jrd3m")
    def collisor = new Collisor(walkmap)
    def stop = false
    controller = new MovableActorController(collisor)
    controller2 = new SeekerActorController(moveNode, pathsmap)
    def controller3 = new HeightAdapterController(walkmap, 0.0f)
    def controller3b = new HeightAdapterController(walkmap, 1.0f)



    moveNode.addController(controller)
    moveNode.addController(controller3)
    //moveNode2.addController(controller2)
    moveNode2.addController(controller3b)

    // Restore
    if (Env.getVar("UPDATE")) {
        loadCurrentVars()
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

    def obj5 = Env.getVar("ALL_ROOM_OBJS")[5]
    def action = { o ->
        saveCurrentVars()
        Env.setVar("CURRENT_PICKING", o)
        Env.fade({ v ->
            state.app.switchState("Picking")
        })
    }
    def letter = Env.dropPickeableObject(animItem, obj5, new Vector3f(-2.5, 0.7, 3.5), new Quaternionf(0f, 0f, Math.toRadians(40) as float, 1f),
            0.04f, action, 1)
    if (letter != null) {
        state.scene.addModelItem(letter)
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
    controller2.seeking = false

    //transformNode3.setPosition(x as float, y as float, z as float)
    viewmap.updateView(state.getCamera(), moveNode.getPosition().x, moveNode.getPosition().z)
    state.getScene().setBackground(viewmap.getCurrentBackground())
    state.getScene().setDepthMask(viewmap.getCurrentDepthMask())

    currentSec = walkmap.sampleSector(moveNode.getPosition().x, moveNode.getPosition().z, 0.0)


    controller.setMovStep(actorInc)
    controller.setRotStep(actorRot)
    //controller2.setMovStep(0.5f)
    if ((animItem as ModelItem).position.distance(animItem2.position) > 1) {
        //controller2.seeking = true
        //animation2.play()

    } else {
        //controller2.seeking = false
    }
    Env.manageShooting(animation, state.window, tpf)

}

def onInput(AbstractState state, Window window, MouseInput mouseInput) {
    //println "T-" + Texture.debMeshCount
    //println "M-" + Mesh.debMeshCount
    Env.updateFade()
    Keys.updateKeys(window)
    actorInc = 0
    actorRot = 0
    if (window.isKeyPressed(GLFW_KEY_W) && !window.isKeyPressed(GLFW_KEY_SPACE)) {

        actorInc = 1.2f
    } else if (window.isKeyPressed(GLFW_KEY_S) && !window.isKeyPressed(GLFW_KEY_SPACE)) {

        actorInc = -1.2f
    }
    if (window.isKeyPressed(GLFW_KEY_A)) {
        actorRot = 130
    } else if (window.isKeyPressed(GLFW_KEY_D)) {
        actorRot = -130
    }


    if (Keys.enter == 1) {

        if (currentSec.name.equals("S118") || currentSec.name.equals("S122")) {
            Env.fade({ v ->
                state.app.switchState("Garden")
            })
        }
    }



    if (Keys.invent == 1) {
        saveCurrentVars()
        Env.fade({ v ->
            state.app.switchState("Inventory")
        })
    }



}

def onClose(AbstractState state) {

}

def saveCurrentVars() {
    Env.setVar("ACTOR_P", moveNode.getPosition())
    Env.setVar("ACTOR_R", moveNode.getRotation())
    Env.setVar("NPC_P", moveNode2.getPosition())
    Env.setVar("NPC_R", moveNode2.getRotation())
}

def loadCurrentVars() {
    moveNode.setPosition(Env.getVar("ACTOR_P"))
    moveNode.setRotation(Env.getVar("ACTOR_R"))
    moveNode2.setPosition(Env.getVar("NPC_P"))
    moveNode2.setRotation(Env.getVar("NPC_R"))
}

this

