package Scripts.gui

import Scripts.utils.Env
import Scripts.utils.Keys
import org.joml.Quaternionf
import org.joml.Vector3f
import org.jrd3.engine.core.graph.SceneLight
import org.jrd3.engine.core.items.ModelItem
import org.jrd3.engine.core.items.Picture
import org.jrd3.engine.core.items.TextPicture
import org.jrd3.engine.core.sim.AbstractState
import org.jrd3.engine.core.sim.MouseInput
import org.jrd3.engine.core.sim.Window

confirm = true
obj = {}
model = {}
yes = {}
no = {}
maxScale = 0
minScale = 0
invert = false

def onInit(AbstractState state) {

    Keys.initKeys()

    confirm = true

    invert = false

    // Show picked object
    obj = Env.getVar("CURRENT_PICKING")
    def fileName = Thread.currentThread().contextClassLoader
            .getResource("Models/Objs/" + obj.modelName).file
    def file = new File(fileName)
    def fileNameTex = Thread.currentThread().contextClassLoader
            .getResource("Textures/Objs/").file
    def fileTex = new File(fileNameTex)
    model = ModelItem.get(file.getAbsolutePath(), fileTex.getAbsolutePath())
    model.scale = obj.scale * 2f
    maxScale = obj.scale * 2f
    minScale = obj.scale;
    model.setPosition(0, -0.5f, -3)
    model.setRotation(new Quaternionf().rotateXYZ(Math.toRadians(15) as float, 0f, 0f))
    state.scene.addModelItem(model)

    // Background
    def pick = Picture.get("/Textures/Picking.png", Picture.Mode.BACKGROUND)
    pick.alpha = 1
    state.scene.addPicture(pick)

    // Labels
    def whouldTake = TextPicture.get("Vuoi prendere", 0, 120)
    whouldTake.getPicture().alpha = 0.7f
    def objName = TextPicture.get(obj.name + "?", 0, 135)
    objName.getPicture().alpha = 0.7f
    yes = TextPicture.get("Si", -20, 160)

    no = TextPicture.get("No", 20, 160)
    no.getPicture().alpha = 0.7f

    state.scene.addText(objName)
    state.scene.addText(whouldTake)
    state.scene.addText(yes)
    state.scene.addText(no)


    // Setup Lights
    def globalLight = new SceneLight()
    state.scene.sceneLight = globalLight

    // Ambient Light
    globalLight.setAmbientLight(new Vector3f(1.0f, 1.0f, 1.0f))

    Env.initFade(state.scene)

}

def refresh3DModels(tpf) {

    model.rotate 0, (250f * tpf as float), 0

    if (model.scale >= maxScale ) {
        invert = false
    } else if (model.scale <= minScale) {
        invert = true
    }

    if (invert) {
        model.scale += 0.1f * tpf as float;
    } else {
        model.scale -= 0.1f * tpf as float;
    }

}

def refreshLabels() {
    if (confirm) {
        yes.getPicture().alpha = 1.0f
        no.getPicture().alpha = 0.6f
    } else {
        yes.getPicture().alpha = 0.6f
        no.getPicture().alpha = 1.0f
    }
}


def onUpdate(AbstractState state, Float tpf) {
    refresh3DModels(tpf)

}

def onInput(AbstractState state, Window window, MouseInput mouseInput) {
    Env.updateFade()
    Keys.updateKeys(window)
    refreshLabels()
    if (Keys.enter == 1) {
        Env.setVar("UPDATE", true)
        Env.fade({ v ->
            if (confirm) {
                Env.getVar("CURR_INV_OBJS").add(obj)
                int index = Env.getVar("ALL_ROOM_OBJS").indexOf(obj)
                Env.getVar("ALL_ROOM_OBJS").set(index, null)
                obj = null
                // Reset model matrix
                model.setPosition(0, 0f, 0)
                model.setRotation(new Quaternionf().rotateXYZ(0, 0, 0))
            }

            state.app.switchState(Env.getVar("LAST"))
        })
    }

    if (Keys.left == 1) {
        confirm = true
    }

    if (Keys.right == 1) {
        confirm = false
    }


}

def onClose(AbstractState state) {

}



this