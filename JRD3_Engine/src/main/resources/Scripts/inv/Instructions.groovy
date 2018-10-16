package Scripts.inv

import Scripts.utils.Env
import org.joml.Vector3f
import org.jrd3.engine.playenv.objects.inventory.InventoryObject
import org.jrd3.engine.playenv.objects.inventory.InventoryObjectAction

class Instructions implements InventoryObject {

    private int amount = -1

    @Override
    String getName() {
        return "Istruzioni"
    }

    @Override
    int getAmount() {
        return amount
    }

    @Override
    float getScale() {
        return 0.07f
    }

    @Override
    void setAmount(int amount) {
        this.amount = amount
    }

    @Override
    String getModelName() {
        return "Letter.obj"
    }

    @Override
    List<InventoryObjectAction> getActions() {
        InventoryObjectAction act = new InventoryObjectAction() {
            @Override
            String getActionName() {
                return "Esamina"
            }

            @Override
            boolean performAction() {

                def text0 = "GENERATORE DI CROOKES\n\nISTRUZIONI DI SETTAGGIO"
                def text1 = "Ispirato all'espirimento di Sir " +
                        "William Crookes, il generatore di " +
                        "Crookes permette, se alimentato " +
                        "correttamente, di creare un fascio " +
                        "di elettroni ad alta energia tra " +
                        "un catodo e un anodo posti fino " +
                        "a tre metri di distanza."

                def text2 = "Per funzionare, i condensatori " +
                        "vanno caricati con batterie ad " +
                        "alta capacita', oppure con un " +
                        "potente generatore capacitivo. " +
                        "Per stabilizzare il flusso di " +
                        "elettroni verso lo schermo " +
                        "anodico e' necessario collegare " +
                        "al catodo uno spinterometro."
                def text3 = "In qualsiasi caso collegare lo spinterometro " +
                        "SEMPRE prima dell'inserimento " +
                        "delle batterie nei relativi alloggi."
                def pages = new ArrayList(1)
                pages.add(text0)
                pages.add(text1)
                pages.add(text2)
                pages.add(text3)
                Env.setVar("PAGES_TO_READ", pages)
                Env.setVar("BOOK_TEMPLATE", "/Textures/OldLetter.png")
                return true
            }
        }

        List<InventoryObjectAction> actions = new ArrayList<>()
        actions.add(act)
        return actions
    }

    @Override
    Vector3f spaceOffset() {
        return null
    }

    @Override
    float spaceScale() {
        return 0
    }
}
