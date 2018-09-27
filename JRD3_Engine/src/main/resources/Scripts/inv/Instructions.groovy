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
                def text1 = "\nGENERATORE DI CROOKES     \n" +
                        "Ispirato all'espirimento di Sir\n" +
                        "William Crookes, il generatore di\n" +
                        "Crookes permette, se alimentato\n" +
                        "correttamente, di creare un fascio  \n" +
                        "di elettroni ad alta energia tra\n" +
                        "un catodo e un anodo posti fino\n" +
                        "a tre metri di distanza.\n\n" +
                        "   ->"

                def text2 = "\nPer funzionare, i condensatori\n" +
                        "vanno caricati con batterie ad\n" +
                        "alta capacita', oppure con un\n" +
                        "potente generatore capacitivo.\n" +
                        "Per stabilizzare il flusso di  \n" +
                        "elettroni verso lo schermo\n" +
                        "anodico e' necessario collegare\n" +
                        "al catodo uno spinterometro.\n\n" +
                        "<- ->"
                def text3 = "\nATTENZIONE:\n" +
                        "\n" +
                        "Collegare lo spinterometro\n" +
                        "SEMPRE prima dell'inserimento.\n" +
                        "delle batterie nei relativi  \n" +
                        "alloggi.\n" +
                        "\n" +
                        "\n\n" +
                        "<-"

                def pages = new ArrayList(1)
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
