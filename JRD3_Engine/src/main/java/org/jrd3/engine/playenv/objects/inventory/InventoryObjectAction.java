package org.jrd3.engine.playenv.objects.inventory;

/**
 * Common interface for generic object action.
 *
 * @author Ray1184
 * @version 1.0
 */
public interface InventoryObjectAction {

    String getActionName();

    boolean performAction();
}
