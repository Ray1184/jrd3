package org.jrd3.engine.playenv.objects.inventory;

import org.joml.Vector3f;

import java.util.List;

/**
 * Common interface for usable objects in inventory.
 *
 * @author Ray1184
 * @version 1.0
 */
public interface InventoryObject {

    String getName();

    int getAmount();

    void setAmount(int amount);

    float getScale();

    String getModelName();

    List<InventoryObjectAction> getActions();

    Vector3f spaceOffset();

    float spaceScale();
}
