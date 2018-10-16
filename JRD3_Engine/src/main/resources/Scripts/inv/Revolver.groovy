package Scripts.inv

import Scripts.utils.Env
import org.joml.Vector3f
import org.jrd3.engine.playenv.objects.inventory.InventoryObject
import org.jrd3.engine.playenv.objects.inventory.InventoryObjectAction

class Revolver implements InventoryObject {

    private int amount = 6

    @Override
    String getName() {
        return "Un Revolver"
    }

    @Override
    int getAmount() {
        return amount
    }

    @Override
    float getScale() {
        return 0.3f
    }

    @Override
    void setAmount(int amount) {
        this.amount = amount
    }

    @Override
    String getModelName() {
        return "Revolver.obj"
    }

    @Override
    List<InventoryObjectAction> getActions() {
        InventoryObjectAction act = new InventoryObjectAction() {
            @Override
            String getActionName() {
                return "Equip"
            }

            @Override
            boolean performAction() {
                Env.setVar("PLAYER_ANIM", "CrippleAnimRevolver.dae")
                Env.setVar("SHOOTING_TYPE", Env.SHOOT_TYPE_PISTOL)
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
