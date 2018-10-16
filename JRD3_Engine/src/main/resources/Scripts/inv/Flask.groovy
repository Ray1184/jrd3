package Scripts.inv

import Scripts.utils.Env
import org.joml.Vector3f
import org.jrd3.engine.playenv.objects.inventory.InventoryObject
import org.jrd3.engine.playenv.objects.inventory.InventoryObjectAction

class Flask implements InventoryObject {

    private int amount = -1

    @Override
    String getName() {
        return "Una Fiaschetta"
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
        return "Flask.obj"
    }

    @Override
    List<InventoryObjectAction> getActions() {
        InventoryObjectAction act = new InventoryObjectAction() {
            @Override
            String getActionName() {
                return "Mangia/Bevi"
            }

            @Override
            boolean performAction() {
                Env.setVar("PLAYER_LIFE", Env.getVar("PLAYER_LIFE") + 10)
                Env.setVar("SHOW_MESSAGE", "Ahhh... mi sento meglio!")
                Env.setVar("MESSAGE_TIME", 4.0f)
                amount--
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
