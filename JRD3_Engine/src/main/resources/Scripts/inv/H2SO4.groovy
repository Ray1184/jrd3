package Scripts.inv

import org.joml.Vector3f
import org.jrd3.engine.playenv.objects.inventory.InventoryObject
import org.jrd3.engine.playenv.objects.inventory.InventoryObjectAction

class H2SO4 implements InventoryObject {

    private int amount = -1

    @Override
    String getName() {
        return "Acido Solforico"
    }

    @Override
    int getAmount() {
        return amount
    }

    @Override
    float getScale() {
        return 0.1f
    }

    @Override
    void setAmount(int amount) {
        this.amount = amount
    }

    @Override
    String getModelName() {
        return "H2SO4.obj"
    }

    @Override
    List<InventoryObjectAction> getActions() {
        InventoryObjectAction act = new InventoryObjectAction() {
            @Override
            String getActionName() {
                return "Usa"
            }

            @Override
            boolean performAction() {
                return false
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
