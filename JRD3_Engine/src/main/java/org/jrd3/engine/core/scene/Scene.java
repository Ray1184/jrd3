package org.jrd3.engine.core.scene;

import org.jrd3.engine.core.graph.InstancedMesh;
import org.jrd3.engine.core.graph.Mesh;
import org.jrd3.engine.core.graph.SceneLight;
import org.jrd3.engine.core.graph.anim.AnimController;
import org.jrd3.engine.core.graph.particles.ParticleEmitter;
import org.jrd3.engine.core.items.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

    private static final String ROOT_NODE_NAME = "ROOT_NODE";

    private final Map<Mesh, List<ModelItem>> meshMap;

    private final Map<InstancedMesh, List<ModelItem>> instancedMeshMap;

    private final List<Picture> backPictureList;

    private final List<Picture> forePictureList;

    private final List<Picture> nonCachedPictureList;

    private final List<ModelItem> items;

    private SceneNode rootNode;

    private final Map<Actor, List<ActorController>> actorControllers;

    private final Map<AnimModelItem, AnimController> animControllers;

    private DepthMask depthMask;

    private SceneLight sceneLight;

    private ParticleEmitter[] particleEmitters;

    private float globalAlpha;

    /**
     * Constructs a new Scene.
     */
    public Scene(String rootNodeName) {
        meshMap = new HashMap<>();
        instancedMeshMap = new HashMap<>();
        backPictureList = new ArrayList<>();
        forePictureList = new ArrayList<>();
        actorControllers = new HashMap<>();
        animControllers = new HashMap<>();
        rootNode = new SceneNode(rootNodeName);
        nonCachedPictureList = new ArrayList<>();
        items = new ArrayList<>();
        globalAlpha = 0.0f;
    }

    /**
     * Default constructor.
     */
    public Scene() {
        this(Scene.ROOT_NODE_NAME);
    }

    /**
     * Getter for property 'gameMeshes'.
     *
     * @return Value for property 'gameMeshes'.
     */
    public Map<Mesh, List<ModelItem>> getGameMeshes() {
        return meshMap;
    }

    /**
     * Getter for property 'gameInstancedMeshes'.
     *
     * @return Value for property 'gameInstancedMeshes'.
     */
    public Map<InstancedMesh, List<ModelItem>> getGameInstancedMeshes() {
        return instancedMeshMap;
    }

    /**
     * Getter for property 'backPictureList'.
     *
     * @return Value for property 'backPictureList'.
     */
    public List<Picture> getBackPictureList() {
        return backPictureList;
    }

    /**
     * Getter for property 'forePictureList'.
     *
     * @return Value for property 'forePictureList'.
     */
    public List<Picture> getForePictureList() {
        return forePictureList;
    }

    /**
     * Getter for property 'depthMask'.
     *
     * @return Value for property 'depthMask'.
     */
    public DepthMask getDepthMask() {
        return depthMask;
    }

    /**
     * Setter for property 'depthMask'.
     *
     * @param depthMask Value to set for property 'depthMask'.
     */
    public void setDepthMask(DepthMask depthMask) {
        this.depthMask = depthMask;
    }

    /**
     * Sets background removing all others.
     *
     * @param pic The picture.
     */
    public void setBackground(Picture pic) {
        backPictureList.clear();
        backPictureList.add(pic);
    }

    /**
     * Adds non cached image to draw.
     *
     * @param tmp The temp image.
     */
    public void addTempPicture(Picture tmp) {
        if (!nonCachedPictureList.contains(tmp)) {
            nonCachedPictureList.add(tmp);
            addPicture(tmp);
        }
    }

    /**
     * Remove non cached image to draw.
     *
     * @param tmp The temp image.
     */
    public void removeTempPicture(Picture tmp) {
        nonCachedPictureList.remove(tmp);
        removePicture(tmp);
        tmp.cleanup(true); // FIXME - BUG HERE!!!
    }

    /**
     * Adds text to draw.
     *
     * @param text The text.
     */
    public void addText(TextPicture text) {
        addPicture(text.getPicture());
    }

    /**
     * Remove text to draw.
     *
     * @param text The text.
     */
    public void removeText(TextPicture text) {
        removePicture(text.getPicture());
    }

    /**
     * Adds non cached text to draw.
     *
     * @param text The text.
     */
    public void addTempText(TextPicture text) {
        addTempPicture(text.getPicture());
    }

    /**
     * Remove non cached text to draw.
     *
     * @param text The text.
     */
    public void removeTempText(TextPicture text) {
        removeTempPicture(text.getPicture());
    }

    /**
     * Adds picture to draw.
     *
     * @param pic The picture.
     */
    public void addPicture(Picture pic) {
        if (Picture.Mode.BACKGROUND == pic.getMode()) {
            if (!backPictureList.contains(pic)) {
                backPictureList.add(pic);
            }
        } else {
            if (!forePictureList.contains(pic)) {
                forePictureList.add(pic);
            }
        }

    }

    /**
     * Remove picture to draw.
     *
     * @param pic The picture.
     */
    public void removePicture(Picture pic) {
        if (Picture.Mode.BACKGROUND == pic.getMode()) {
            backPictureList.remove(pic);
        } else {
            forePictureList.remove(pic);
        }

    }

    /**
     * Finds a scene node by name.
     *
     * @param name The node name.
     * @return The node object.
     */
    public SceneNode findNode(String name) {
        SceneGraph.NodeSearcherVisitor visitor = new SceneGraph.NodeSearcherVisitor(name);
        SceneGraph.traverse(rootNode, visitor);
        return visitor.getRes();
    }


    /**
     * Adds model item to draw.
     *
     * @param modelItem The model item.
     */
    public void addModelItem(ModelItem modelItem) {

        if (modelItem.getControllers() != null) {
            actorControllers.put(modelItem, modelItem.getControllers());
        }

        if (modelItem instanceof AnimModelItem) {
            AnimModelItem animItem = (AnimModelItem) modelItem;
            if (animItem.getAnimController() != null) {
                animControllers.put(animItem, animItem.getAnimController());
            }
        }
        Mesh[] meshes = modelItem.getMeshes();
        for (Mesh mesh : meshes) {
            boolean instancedMesh = mesh instanceof InstancedMesh;
            List<ModelItem> list = instancedMesh ? instancedMeshMap.get(mesh) : meshMap.get(mesh);
            if (list == null) {
                list = new ArrayList<>();
                if (instancedMesh) {
                    instancedMeshMap.put((InstancedMesh) mesh, list);
                } else {
                    meshMap.put(mesh, list);
                }
            }
            list.add(modelItem);
        }

        if (!items.contains(modelItem)) {
            items.add(modelItem);
        }

    }


    public void cleanup() {

        // Meshes are now caches, just set all item meshes and textures are not used
        // yet.
        for (ModelItem item : items) {
            for (Mesh mesh : item.getMeshes()) {
                if (mesh.getMaterial() != null && mesh.getMaterial().getTexture() != null) {
                    mesh.getMaterial().getTexture().setCurrentlyUsed(false);
                }

            }
            item.setCurrentlyUsed(false);
        }
        /*
        for (Mesh mesh : meshMap.keySet()) {
            mesh.cleanup();
        }
        for (Mesh mesh : instancedMeshMap.keySet()) {
            mesh.cleanup();
        }
        */

        for (Picture picture : backPictureList) {
            picture.cleanup(false);
        }

        for (Picture picture : forePictureList) {
            picture.cleanup(false);
        }


        for (Picture pic : nonCachedPictureList) {
            pic.cleanup(true);
        }
        if (particleEmitters != null) {
            for (ParticleEmitter particleEmitter : particleEmitters) {
                particleEmitter.cleanup();
            }
        }

        // TODO - Think to do in more performant way.
        rootNode = new SceneNode(Scene.ROOT_NODE_NAME);
    }

    /**
     * Init all controllers and adds also all actor controllers attached to scene node.
     */
    public void initControllers() {
        SceneGraph.traverse(rootNode, node -> actorControllers.put(node, node.getControllers()));
        actorControllers.forEach((actor, controllers) -> controllers.forEach((c) -> c.init(actor)));
        animControllers.forEach((animActor, animController) -> animController.init(animActor.getAnimations()));

    }

    /**
     * Updates all controllers.
     *
     * @param tpf
     */
    public void updateControllers(float tpf) {
        actorControllers.forEach((actor, controllers) -> controllers.forEach((c) -> c.update(actor, tpf)));
        animControllers.forEach((animActor, animController) -> animController.update(animActor.getCurrentAnimation(), tpf));
    }


    /**
     * Getter for property 'sceneLight'.
     *
     * @return Value for property 'sceneLight'.
     */
    public SceneLight getSceneLight() {
        return sceneLight;
    }

    /**
     * Setter for property 'sceneLight'.
     *
     * @param sceneLight Value to set for property 'sceneLight'.
     */
    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }


    /**
     * Getter for property 'particleEmitters'.
     *
     * @return Value for property 'particleEmitters'.
     */
    public ParticleEmitter[] getParticleEmitters() {
        return particleEmitters;
    }

    /**
     * Setter for property 'particleEmitters'.
     *
     * @param particleEmitters Value to set for property 'particleEmitters'.
     */
    public void setParticleEmitters(ParticleEmitter[] particleEmitters) {
        this.particleEmitters = particleEmitters;
    }

    /**
     * Getter for property 'rootNode'.
     *
     * @return Value for property 'rootNode'.
     */
    public SceneNode getRootNode() {
        return rootNode;
    }

    /**
     * Getter for property 'globalAlpha'.
     *
     * @return Value for property 'globalAlpha'.
     */
    public float getGlobalAlpha() {
        return globalAlpha;
    }

    /**
     * Setter for property 'globalAlpha'.
     *
     * @param globalAlpha Value to set for property 'globalAlpha'.
     */
    public void setGlobalAlpha(float globalAlpha) {
        this.globalAlpha = globalAlpha;
    }
}
