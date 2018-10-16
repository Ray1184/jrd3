package Scripts.inv

import Scripts.utils.Env
import org.joml.Vector3f
import org.jrd3.engine.playenv.objects.inventory.InventoryObject
import org.jrd3.engine.playenv.objects.inventory.InventoryObjectAction

class OldLetter implements InventoryObject {

    private int amount = -1

    @Override
    String getName() {
        return "Una Vecchia Lettera"
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

                def text0 = null
                def text1 = "Poveri stolti! Illusi di avermi sconfitto, " +
                        "illusi della vostra vittoria, " +
                        "illusi della vostra stessa vita! " +
                        "Fintanto che i miei resti giaceranno " +
                        "in queste fredde terre il mio spirito " +
                        "rimarra' vivo. Quando vi accorgerete di cio', " +
                        "io avro' guadagnato l'immortalita'!"


                def pages = new ArrayList(1)
                pages.add(text0)
                pages.add(text1)

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
