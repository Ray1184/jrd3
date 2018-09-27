package Scripts.inv

import Scripts.utils.Env
import org.joml.Vector3f
import org.jrd3.engine.playenv.objects.inventory.InventoryObject
import org.jrd3.engine.playenv.objects.inventory.InventoryObjectAction

class Actions implements InventoryObject {



    @Override
    String getName() {
        return "Azioni"
    }

    @Override
    int getAmount() {
        return Env.getVar("PLAYER_LIFE")
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
        return "Cripple.obj"
    }

    @Override
    List<InventoryObjectAction> getActions() {
        InventoryObjectAction act = new InventoryObjectAction() {
            @Override
            String getActionName() {
                return "Apri/Cerca"
            }

            @Override
            boolean performAction() {
                return true
            }
        }

        InventoryObjectAction act2 = new InventoryObjectAction() {
            @Override
            String getActionName() {
                return "Combatti"
            }

            @Override
            boolean performAction() {
                return true
            }
        }

        InventoryObjectAction act3 = new InventoryObjectAction() {
            @Override
            String getActionName() {
                return "Spingi"
            }

            @Override
            boolean performAction() {
                return true
            }
        }

        List<InventoryObjectAction> actions = new ArrayList<>()
        actions.add(act)
        //actions.add(act2)
        //actions.add(act3)
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
