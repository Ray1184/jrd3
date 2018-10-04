package Scripts.gui

import Scripts.utils.Env
import Scripts.utils.Keys
import org.joml.Vector3f
import org.jrd3.engine.core.graph.SceneLight
import org.jrd3.engine.core.items.Picture
import org.jrd3.engine.core.items.TextPicture
import org.jrd3.engine.core.sim.AbstractState
import org.jrd3.engine.core.sim.MouseInput
import org.jrd3.engine.core.sim.Window


def onInit(AbstractState state) {

    Keys.initKeys()

    // Background
    def pick = Picture.get("/Textures/Loader.png", Picture.Mode.BACKGROUND)

    pick.alpha = 0.8
    state.scene.addPicture(pick)

    // Text pictures

    def conf = TextPicture.get("CONFIGURAZIONE TASTIERA", -20, 10)
    def wasd = TextPicture.get("W A S D      Su, Sinistra, Giu, Destra", 30, 50, false)
    def act = TextPicture.get("E               Azione", 30, 70, false)
    def inv = TextPicture.get("I                Inventario", 30, 90, false)
    def esc = TextPicture.get("ESC           Esci", 30, 110, false)
    def text = TextPicture.get("Premi E per tornare al menu'", 0, 170)

    state.scene.addText(conf)
    state.scene.addText(wasd)
    state.scene.addText(act)
    state.scene.addText(inv)
    state.scene.addText(esc)
    state.scene.addText(text)

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

    if (Keys.enter == 1) {
        Env.fade({ v ->
            state.app.switchState("MainMenu")
        })

    }


}


def onClose(AbstractState state) {

}


this