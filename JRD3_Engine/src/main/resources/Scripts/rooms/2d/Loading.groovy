package Scripts.gui

import Scripts.inv.*
import Scripts.utils.Env
import Scripts.utils.Keys
import org.joml.Vector3f
import org.jrd3.engine.core.graph.SceneLight
import org.jrd3.engine.core.items.DepthMask
import org.jrd3.engine.core.items.ModelItem
import org.jrd3.engine.core.items.Picture
import org.jrd3.engine.core.items.TextPicture
import org.jrd3.engine.core.sim.AbstractState
import org.jrd3.engine.core.sim.MouseInput
import org.jrd3.engine.core.sim.Window
import org.jrd3.engine.playenv.objects.inventory.InventoryObject
import org.jrd3.engine.playenv.objects.inventory.InventoryObjectAction

loadPercentage = 0
waitCycles = 0
resLoaded = false

def onInit(AbstractState state) {
    Env.fadeRatio = 0.02
    resLoaded = false
    loadPercentage = 0
    waitCycles = 0
    Keys.initKeys()

    def load = Picture.get("/Textures/Loader.png", Picture.Mode.BACKGROUND)
    load.alpha = 1
    state.scene.addTempPicture(load)

    // def text = TextPicture.get("La villa di Harm Street e' stata teatro di\nfatti inspiegabili fin dalla sua costruzione.\nOgni membro della famiglia che l'ha \noccupata e' deceduto di morte orribile.\nLe forme oblunghe e le strane luci\nvisibili dalle finestre della cantina\nsono state per anni oggetto di leggende.\nFrutto della fantasia? Forse...", 0, 0)
    def text = Env.text("La villa di Harm Street e' stata teatro di fatti inspiegabili fin dalla sua costruzione. Ogni membro della famiglia che l'ha  occupata e' deceduto di morte orribile. Le forme oblunghe e le strane luci visibili dalle finestre della cantina sono state per anni oggetto di leggende. Frutto della fantasia? Forse...", 0, 0, Env.GOLD, true, 39)
    text.picture.alpha = 0.8
    state.scene.addTempText(text)

    // Setup Lights
    def globalLight = new SceneLight()
    state.scene.sceneLight = globalLight

    // Ambient Light
    globalLight.setAmbientLight(new Vector3f(1.0f, 1.0f, 1.0f))

    Env.initFade(state.scene)

}


def onUpdate(AbstractState state, Float tpf) {


}

def onInput(AbstractState state, Window window, MouseInput mouseInput) {
    Env.updateFade()
    Keys.updateKeys(window)
    if (Env.currentScene.globalAlpha >= 1) {
        if (!resLoaded) {
            resLoaded = true
            loadResources()
            def text = TextPicture.get("Premi E per continuare", 0, 170)
            state.scene.addTempText(text)

        }

        if (Keys.enter == 1) {
            Env.fade({ v ->
                Env.fadeRatio = 0.1
                state.app.switchState("Basement")
            })
        }

    }

}

def onClose(AbstractState state) {

}

void loadResources() {
    // Loading models
    String fileName = Thread.currentThread().getContextClassLoader()
            .getResource("Models/CrippleAnim.dae").getFile()
    File file = new File(fileName)
    ModelItem.get(file.getAbsolutePath(), "")

    fileName = Thread.currentThread().getContextClassLoader()
            .getResource("Models/UncleAnim.dae").getFile()
    file = new File(fileName)
    ModelItem.get(file.getAbsolutePath(), "")

    // Loading backgrounds and pictures
    Picture.get("/Screens/B01_B.png", Picture.Mode.BACKGROUND)
    Picture.get("/Screens/B02_B.png", Picture.Mode.BACKGROUND)
    Picture.get("/Screens/B03_B.png", Picture.Mode.BACKGROUND)
    Picture.get("/Screens/B04_B.png", Picture.Mode.BACKGROUND)
    Picture.get("/Screens/G01_B.png", Picture.Mode.BACKGROUND)
    Picture.get("/Screens/G02_B.png", Picture.Mode.BACKGROUND)
    Picture.get("/Screens/G03_B.png", Picture.Mode.BACKGROUND)
    Picture.get("/Screens/G04_B.png", Picture.Mode.BACKGROUND)
    Picture.get("/Screens/G05_B.png", Picture.Mode.BACKGROUND)
    DepthMask.get("/Screens/B01_D.png")
    DepthMask.get("/Screens/B02_D.png")
    DepthMask.get("/Screens/B03_D.png")
    DepthMask.get("/Screens/B04_D.png")
    DepthMask.get("/Screens/G01_D.png")
    DepthMask.get("/Screens/G02_D.png")
    DepthMask.get("/Screens/G03_D.png")
    DepthMask.get("/Screens/G04_D.png")
    DepthMask.get("/Screens/G05_D.png")

    // Caching all resources for inventory
    Env.setVar "ALL_ROOM_OBJS", []
    Env.setVar "CURR_INV_OBJS", []
    Env.setVar "SPACE_OBJS", []

    Env.setVar "PLAYER_LIFE", 20


    Env.getVar("ALL_ROOM_OBJS")[0] = new Actions()
    Env.getVar("ALL_ROOM_OBJS")[1] = new Battery()
    Env.getVar("ALL_ROOM_OBJS")[2] = new Battery()
    Env.getVar("ALL_ROOM_OBJS")[3] = new Battery()
    Env.getVar("ALL_ROOM_OBJS")[4] = new Flask()
    Env.getVar("ALL_ROOM_OBJS")[5] = new Instructions()
    Env.getVar("ALL_ROOM_OBJS")[6] = new OldLetter()
    Env.getVar("ALL_ROOM_OBJS")[7] = new Cartridges12()
    Env.getVar("ALL_ROOM_OBJS")[8] = new H2SO4()


    Env.getVar("ALL_ROOM_OBJS").each { InventoryObject obj ->

        // Slots
        TextPicture.get(obj.name, 0, 8)

        // Picking
        TextPicture.get(obj.name + "?", 0, 135)

        // Actions
        def i = 0
        for (InventoryObjectAction action : obj.actions) {
            TextPicture.get(action.actionName, 70, 100 + (i++ * 19))
        }

        // Graphics
        fileName = Thread.currentThread().contextClassLoader
                .getResource("Models/Objs/" + obj.modelName).file
        file = new File(fileName)
        def fileNameTex = Thread.currentThread().contextClassLoader
                .getResource("Textures/Objs/").file
        def fileTex = new File(fileNameTex)
        def model = ModelItem.get(file.getAbsolutePath(), fileTex.getAbsolutePath())
        model.position = new Vector3f(-1.3, -1.25, -3)
        model.scale = 0

        // Amount
        if (obj.getAmount() > -1) {
            TextPicture.get(String.valueOf(obj.getAmount()), -130, 100)
        }

    }
    // Load static messages

    TextPicture.get("Vuoi prendere", 0, 120)
    TextPicture.get("Si", -20, 160)
    TextPicture.get("No", 20,160)

    // Setup inventory

    Env.getVar("CURR_INV_OBJS").add(Env.getVar("ALL_ROOM_OBJS")[0] )
    Env.getVar("CURR_INV_OBJS").add(Env.getVar("ALL_ROOM_OBJS")[4] )
    Env.getVar("CURR_INV_OBJS").add(Env.getVar("ALL_ROOM_OBJS")[8])
}

this