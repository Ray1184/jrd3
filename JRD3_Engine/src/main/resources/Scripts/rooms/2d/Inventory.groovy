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
import org.jrd3.engine.playenv.objects.inventory.InventoryObject
import org.jrd3.engine.playenv.objects.inventory.InventoryObjectAction

OBJECTS = 0
ACTIONS = 1

objId = 0
actId = 0

mode = 0

slotOffset = 0
inventVisibleSlots = []
invObjects = []
inventAllSlots = []
inventAllActions = []
inventAll3D = []
inventAllAmount = []

current3DModel = {}

def onInit(AbstractState state) {

    invObjects = Env.getVar("CURR_INV_OBJS")
    inventAllSlots = []
    inventAllActions = []
    inventAll3D = []
    inventAllAmount = []
    inventVisibleSlots = []
    slotOffset = 0
    actId = 0
    objId = 0
    mode = 0
    step = 0
    Keys.initKeys()

    window = state.getWindow()


    for (InventoryObject obj : invObjects) {

        // Slots
        def descPic = TextPicture.get(obj.name, 0, 8)
        state.scene.addText(descPic)
        inventAllSlots.add(descPic)

        // Actions
        List<TextPicture> pictures = []
        def i = 0
        for (InventoryObjectAction action : obj.actions) {
            def actionsPic = TextPicture.get(action.actionName, 70, 100 + (i++ * 19))
            state.scene.addText(actionsPic)
            pictures.add(actionsPic)
        }
        inventAllActions.add(pictures)

        // Graphics
        def fileName = Thread.currentThread().contextClassLoader
                .getResource("Models/Objs/" + obj.modelName).file
        def file = new File(fileName)
        def fileNameTex = Thread.currentThread().contextClassLoader
                .getResource("Textures/Objs/").file
        def fileTex = new File(fileNameTex)
        def model = ModelItem.get(file.getAbsolutePath(), fileTex.getAbsolutePath())
        model.position = new Vector3f(-1.3, -1.25, -3)
        model.rotation = new Quaternionf().rotateXYZ(0, 0, 0)
        model.scale = 0
        state.scene.addModelItem model
        inventAll3D.add model

        // Amount
        if (obj.getAmount() > -1) {
            def amountPic = TextPicture.get(String.valueOf(obj.getAmount()), -130, 100)
            state.scene.addText(amountPic)
            inventAllAmount.add(amountPic)
        } else {
            inventAllAmount.add(null) // Place holder for keep sorting
        }

    }

    // Background
    backSt = Picture.get("/Textures/Invent.png", Picture.Mode.BACKGROUND)
    backSt.alpha = 1
    state.getScene().addPicture(backSt)

    // Setup Lights
    def globalLight = new SceneLight()
    state.scene.sceneLight = globalLight

    // Ambient Light
    globalLight.setAmbientLight(new Vector3f(1.0f, 1.0f, 1.0f))

    refreshObjList()
    refreshObjSelected()
    refreshActionsList()

    current3DModel = inventAll3D.get(objId)

    Env.initFade(state.scene)

}

def refreshAmount() {
    def i = 0
    for (TextPicture lab : inventAllAmount) {
        if (lab != null) {
            if (objId == i) {
                lab.picture.alpha = 1
            } else {
                lab.picture.alpha = 0
            }
        }

        i++
    }
}

def refresh3DModels(tpf) {
    def new3DModel = inventAll3D.get(objId)
    if (new3DModel != current3DModel) {
        // TODO temporary! Implements and use visibility flag.
        current3DModel.scale = 0
        current3DModel = new3DModel
    }
    current3DModel.scale = invObjects.get(objId).scale
    current3DModel.rotate 0, (120f * tpf as float), 0
}

def refreshObjSelected() {
    def i = 0
    for (TextPicture lab : inventAllSlots) {
        if (i == objId) {
            lab.getPicture().alpha = 1
        } else if (!inventVisibleSlots.contains(lab)) {
            lab.getPicture().alpha = 0
        } else {
            lab.getPicture().alpha = 0.6
        }
        i++
    }
}

def refreshActionsList() {


    def i = 0
    for (List<TextPicture> actions : inventAllActions) {
        if (i == objId) {
            def h = 0
            for (TextPicture lab : actions) {
                if (mode == OBJECTS) {
                    lab.getPicture().alpha = 0.6
                } else if (mode == ACTIONS) {
                    if (h == actId) {
                        lab.getPicture().alpha = 1
                    } else {
                        lab.getPicture().alpha = 0.6
                    }
                    h++
                }
            }

        } else {
            for (TextPicture lab : actions) {
                lab.getPicture().alpha = 0
            }
        }
        i++
    }
}

def refreshObjList() {

    int begIndex, endIndex
    if (inventAllSlots.size() < 4) {
        begIndex = 0
        endIndex = inventAllSlots.size()
    } else {
        begIndex = 0 + slotOffset
        endIndex = 4 + slotOffset
    }

    inventVisibleSlots = inventAllSlots.subList(begIndex, endIndex)

    def i = 0
    for (TextPicture lab : inventVisibleSlots) {
        lab.getPicture().alpha = 0.6
        lab.picture.setPosition(0, i++ * 19)

    }

    for (TextPicture lab : inventAllSlots) {
        if (!inventVisibleSlots.contains(lab)) {
            lab.getPicture().alpha = 0
        }
    }

}

def onUpdate(AbstractState state, Float tpf) {


    refresh3DModels(tpf)
    refreshAmount()
}

def onInput(AbstractState state, Window window, MouseInput mouseInput) {
    //println "T-" + Texture.debMeshCount
    //println "M-" + Mesh.debMeshCount
    Env.updateFade()
    Keys.updateKeys(window)

    if (Keys.right == 1 && mode != ACTIONS) {
        mode = ACTIONS
        refreshActionsList()
    }
    if (Keys.left == 1 && mode != OBJECTS) {
        mode = OBJECTS
        actId = 0
        refreshActionsList()
    }

    if (Keys.down == 1) {
        if (mode == OBJECTS) {
            if (objId < invObjects.size - 1) {
                objId++
                refreshActionsList()

            }
            if (step < 57) {
                step += 19
            } else {
                if (slotOffset < inventAllSlots.size - 4) {
                    slotOffset++
                }
                refreshObjList()
            }
            refreshObjSelected()
        } else if (mode == ACTIONS) {
            def acSize = invObjects.get(objId).actions.size - 1
            if (actId < acSize) {
                actId++
                refreshActionsList()
            }

        }
    }

    if (Keys.up == 1) {
        if (mode == OBJECTS) {
            if (objId > 0) {
                objId--
                refreshActionsList()
            }
            if (step > 0) {
                step -= 19
            } else {
                if (slotOffset > 0) {
                    slotOffset--
                }
                refreshObjList()
            }
            refreshObjSelected()
        } else if (mode == ACTIONS) {
            if (actId > 0) {
                actId--
                refreshActionsList()
            }
        }
    }

    if (Keys.enter == 1) {
        if (mode == ACTIONS) {
            ret = invObjects[objId].actions[actId].performAction()
            if (ret) {
                // Process type of action
                Env.setVar("UPDATE", true)
                if (invObjects[objId].actions[actId].getActionName().equals("Esamina")) {
                    def pages = Env.getVar("PAGES_TO_READ")
                    Env.fade({ v ->
                        state.app.switchState("Book")
                    })
                } else {
                    Env.fade({ v ->
                        // Remove object of amount -2
                        if (invObjects[objId].amount == -2) {
                            invObjects.remove(objId)
                        }
                        state.app.switchState(Env.getVar("LAST"))
                    })
                }
            }
        } else if (mode == OBJECTS) {
            mode = ACTIONS
            refreshActionsList()
        }
    }

    if (Keys.invent == 1) {
        Env.setVar("UPDATE", true)
        Env.fade({ v ->
            state.app.switchState(Env.getVar("LAST"))
        })
    }

}

def onClose(AbstractState state) {

}

this