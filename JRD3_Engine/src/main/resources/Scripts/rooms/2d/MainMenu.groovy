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

index = 0

def onInit(AbstractState state) {
    Env.fadeRatio = 0.02
    Keys.initKeys()



    index = 0

    // Background
    def pick = Picture.get("/Textures/MainMenu.png", Picture.Mode.BACKGROUND)

    pick.alpha = 0.8
    state.scene.addPicture(pick)

    // Text pictures

    start = TextPicture.get("Inizio", 0, 110)
    info = TextPicture.get("Istruzioni", 0, 130)
    exit = TextPicture.get("Esci", 0, 150)

    state.scene.addText(start)
    state.scene.addText(info)
    state.scene.addText(exit)

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
        if (index == 0) {
            Env.fade({ v ->
                state.app.switchState("Loading")
            })
        } else if (index == 1) {
            Env.fadeRatio = 0.1
            Env.fade({ v ->
                state.app.switchState("Info")
            })
        } else {
            System.exit(0)
        }

    }

    if (Keys.down == 1) {
        if (index < 2) {
            index++;
        }
    }

    if (Keys.up == 1) {
        if (index > 0) {
            index--;
        }
    }

    if (index == 0) {
        start.picture.alpha = 1
        info.picture.alpha = 0.7
        exit.picture.alpha = 0.7
    }

    if (index == 1) {
        start.picture.alpha = 0.7
        info.picture.alpha = 1
        exit.picture.alpha = 0.7
    }

    if (index == 2) {
        start.picture.alpha = 0.7
        info.picture.alpha = 0.7
        exit.picture.alpha = 1
    }


}

def updatePictures() {
    /*
    for (TextPicture textPic : pagesPict) {
        textPic.picture.alpha = 0
    }

    pagesPict.get(index).picture.alpha = 1
*/

}

def onClose(AbstractState state) {

}


this