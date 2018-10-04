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

pagesPict = []

index = 0

def onInit(AbstractState state) {

    Keys.initKeys()

    pagesPict = []

    index = 0

    // Background
    def pick = Picture.get(Env.getVar("BOOK_TEMPLATE"), Picture.Mode.BACKGROUND)

    pick.alpha = 0.8
    state.scene.addPicture(pick)

    // Text pictures
    def pages = Env.getVar("PAGES_TO_READ")
    boolean cover = false;
    for (String text : pages) {
        if (!cover) {
            cover = true
            if (text != null) {
                def textPic = Env.text(text, -10, 30, Env.BLACK, true, 0)
                textPic.picture.alpha = 0
                state.scene.addText(textPic)
                pagesPict.add(textPic)
            }
        } else {
            //def textPic = TextPicture.get(text, 0, 0, TextPicture.DEFAULT_FONT, new Color(0, 0, 0))
            def textPic = Env.text(text, -5, 20, Env.BLACK, true, 32)
            textPic.picture.alpha = 0
            state.scene.addText(textPic)
            pagesPict.add(textPic)
        }
    }





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

    if (Keys.enter == 1 || Keys.right == 1) {
        if (index < pagesPict.size() - 1) {
            index++
        } else {
            Env.fade({ v ->
                state.app.switchState(Env.getVar("LAST"))
            })
        }
    }

    if (Keys.left == 1) {
        if (index > 0) {
            index--
        }
    }

    updatePictures()



}

def updatePictures() {
    for (TextPicture textPic : pagesPict) {
        textPic.picture.alpha = 0
    }

    pagesPict.get(index).picture.alpha = 0.8


}

def onClose(AbstractState state) {

}



this