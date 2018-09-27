package org.jrd3.engine.core.loaders.assimp;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.graph.Material;
import org.jrd3.engine.core.graph.Mesh;
import org.jrd3.engine.core.graph.anim.*;
import org.jrd3.engine.core.items.AnimModelItem;
import org.jrd3.engine.core.utils.CommonUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.lwjgl.assimp.AIVertexWeight.Buffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.assimp.Assimp.*;

public class AnimMeshesLoader extends StaticMeshesLoader {

    private static void buildTransFormationMatrices(AINodeAnim aiNodeAnim, AnimNode animNode) {
        int numFrames = aiNodeAnim.mNumPositionKeys();
        AIVectorKey.Buffer positionKeys = aiNodeAnim.mPositionKeys();
        AIVectorKey.Buffer scalingKeys = aiNodeAnim.mScalingKeys();
        AIQuatKey.Buffer rotationKeys = aiNodeAnim.mRotationKeys();

        for (int i = 0; i < numFrames; i++) {
            AIVectorKey aiVecKey = positionKeys.get(i);
            AIVector3D vec = aiVecKey.mValue();

            Matrix4f transfMat = new Matrix4f().translate(vec.x(), vec.y(), vec.z());

            AIQuatKey quatKey = rotationKeys.get(i);
            AIQuaternion aiQuat = quatKey.mValue();
            Quaternionf quat = new Quaternionf(aiQuat.x(), aiQuat.y(), aiQuat.z(), aiQuat.w());
            transfMat.rotate(quat);

            if (i < aiNodeAnim.mNumScalingKeys()) {
                aiVecKey = scalingKeys.get(i);
                vec = aiVecKey.mValue();
                transfMat.scale(vec.x(), vec.y(), vec.z());
            }

            animNode.addTransformation(transfMat);
        }
    }

    public static AnimModelItem loadAnimModelItem(String resourcePath)
            throws JRD3Exception {
        return AnimMeshesLoader.loadAnimModelItem(resourcePath, "");
    }

    public static AnimModelItem loadAnimModelItem(String resourcePath, String texturesDir)
            throws JRD3Exception {
        return AnimMeshesLoader.loadAnimModelItem(resourcePath, texturesDir,
                aiProcess_GenSmoothNormals | aiProcess_JoinIdenticalVertices | aiProcess_Triangulate
                        | aiProcess_FixInfacingNormals | aiProcess_LimitBoneWeights);
    }

    public static AnimModelItem loadAnimModelItem(String resourcePath, String texturesDir, int flags)
            throws JRD3Exception {

        AIScene aiScene = aiImportFile(resourcePath, flags);

        if (aiScene == null) {
            throw new JRD3Exception("Error loading model");
        }

        int numMaterials = aiScene.mNumMaterials();
        PointerBuffer aiMaterials = aiScene.mMaterials();
        List<Material> materials = new ArrayList<>();
        for (int i = 0; i < numMaterials; i++) {
            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
            StaticMeshesLoader.processMaterial(aiMaterial, materials, texturesDir);
        }

        List<Bone> boneList = new ArrayList<>();
        int numMeshes = aiScene.mNumMeshes();
        PointerBuffer aiMeshes = aiScene.mMeshes();
        Mesh[] meshes = new Mesh[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            Mesh mesh = AnimMeshesLoader.processMesh(aiMesh, materials, boneList);
            meshes[i] = mesh;
        }

        AINode aiRootNode = aiScene.mRootNode();
        Matrix4f rootTransfromation = toMatrix(aiRootNode.mTransformation());
        AnimNode rootAnimNode = AnimMeshesLoader.processNodesHierarchy(aiRootNode, null);
        Map<String, Animation> animations = AnimMeshesLoader.processAnimations(aiScene, boneList, rootAnimNode, rootTransfromation);
        AnimModelItem item = new AnimModelItem(meshes, animations, rootAnimNode);

        return item;
    }

    private static List<AnimatedFrame> buildAnimationFrames(List<Bone> boneList, AnimNode rootAnimNode,
                                                            Matrix4f rootTransformation) {

        int numFrames = rootAnimNode.getAnimationFrames();
        List<AnimatedFrame> frameList = new ArrayList<>();
        for (int i = 0; i < numFrames; i++) {
            AnimatedFrame frame = new AnimatedFrame();
            frameList.add(frame);

            int numBones = boneList.size();
            for (int j = 0; j < numBones; j++) {
                Bone bone = boneList.get(j);
                AnimNode animNode = rootAnimNode.findByNameInternal(bone.getBoneName());
                Matrix4f boneMatrix = AnimNode.getParentTransforms(animNode, i);
                boneMatrix.mul(bone.getOffsetMatrix());
                boneMatrix = new Matrix4f(rootTransformation).mul(boneMatrix);
                frame.setMatrix(j, boneMatrix);
            }
        }

        return frameList;
    }

    private static Map<String, Animation> processAnimations(AIScene aiScene, List<Bone> boneList,
                                                            AnimNode rootAnimNode, Matrix4f rootTransformation) {
        Map<String, Animation> animations = new HashMap<>();

        // Process all animations
        int numAnimations = aiScene.mNumAnimations();
        PointerBuffer aiAnimations = aiScene.mAnimations();
        for (int i = 0; i < numAnimations; i++) {
            AIAnimation aiAnimation = AIAnimation.create(aiAnimations.get(i));

            // Calculate transformation matrices for each node
            int numChanels = aiAnimation.mNumChannels();
            PointerBuffer aiChannels = aiAnimation.mChannels();
            for (int j = 0; j < numChanels; j++) {
                AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get(j));
                String nodeName = aiNodeAnim.mNodeName().dataString();
                AnimNode animNode = rootAnimNode.findByNameInternal(nodeName);
                AnimMeshesLoader.buildTransFormationMatrices(aiNodeAnim, animNode);
            }

            List<AnimatedFrame> frames = AnimMeshesLoader.buildAnimationFrames(boneList, rootAnimNode, rootTransformation);
            Animation animation = new Animation(aiAnimation.mName().dataString(), frames, aiAnimation.mDuration());
            animations.put(animation.getName(), animation);
        }
        return animations;
    }

    private static void processBones(AIMesh aiMesh, List<Bone> boneList, List<Integer> boneIds,
                                     List<Float> weights) {
        Map<Integer, List<VertexWeight>> weightSet = new HashMap<>();
        int numBones = aiMesh.mNumBones();
        PointerBuffer aiBones = aiMesh.mBones();
        for (int i = 0; i < numBones; i++) {
            AIBone aiBone = AIBone.create(aiBones.get(i));
            int id = boneList.size();
            Bone bone = new Bone(id, aiBone.mName().dataString(), AnimMeshesLoader.toMatrix(aiBone.mOffsetMatrix()));
            boneList.add(bone);
            int numWeights = aiBone.mNumWeights();
            Buffer aiWeights = aiBone.mWeights();
            for (int j = 0; j < numWeights; j++) {
                AIVertexWeight aiWeight = aiWeights.get(j);
                VertexWeight vw = new VertexWeight(bone.getBoneId(), aiWeight.mVertexId(),
                        aiWeight.mWeight());
                List<VertexWeight> vertexWeightList = weightSet.get(vw.getVertexId());
                if (vertexWeightList == null) {
                    vertexWeightList = new ArrayList<>();
                    weightSet.put(vw.getVertexId(), vertexWeightList);
                }
                vertexWeightList.add(vw);
            }
        }

        int numVertices = aiMesh.mNumVertices();
        for (int i = 0; i < numVertices; i++) {
            List<VertexWeight> vertexWeightList = weightSet.get(i);
            int size = vertexWeightList != null ? vertexWeightList.size() : 0;
            for (int j = 0; j < Mesh.MAX_WEIGHTS; j++) {
                if (j < size) {
                    VertexWeight vw = vertexWeightList.get(j);
                    weights.add(vw.getWeight());
                    boneIds.add(vw.getBoneId());
                } else {
                    weights.add(0.0f);
                    boneIds.add(0);
                }
            }
        }
    }

    private static Mesh processMesh(AIMesh aiMesh, List<Material> materials, List<Bone> boneList) {
        List<Float> vertices = new ArrayList<>();
        List<Float> textures = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        List<Integer> boneIds = new ArrayList<>();
        List<Float> weights = new ArrayList<>();

        StaticMeshesLoader.processVertices(aiMesh, vertices);
        StaticMeshesLoader.processNormals(aiMesh, normals);
        StaticMeshesLoader.processTextCoords(aiMesh, textures);
        StaticMeshesLoader.processIndices(aiMesh, indices);
        AnimMeshesLoader.processBones(aiMesh, boneList, boneIds, weights);

        // Workaround for severe shading compilation.
        if (textures.size() == 0) {
            textures.add(0.0f);
        }

        Mesh mesh = new Mesh(CommonUtils.listToArray(vertices), CommonUtils.listToArray(textures),
                CommonUtils.listToArray(normals), CommonUtils.listIntToArray(indices),
                CommonUtils.listIntToArray(boneIds), CommonUtils.listToArray(weights));
        Material material;
        int materialIdx = aiMesh.mMaterialIndex();
        if (materialIdx >= 0 && materialIdx < materials.size()) {
            material = materials.get(materialIdx);
        } else {
            material = new Material();
        }
        mesh.setMaterial(material);

        return mesh;
    }

    private static AnimNode processNodesHierarchy(AINode aiNode, AnimNode parentAnimNode) {
        String nodeName = aiNode.mName().dataString();
        AnimNode animNode = new AnimNode(nodeName, parentAnimNode);

        int numChildren = aiNode.mNumChildren();
        PointerBuffer aiChildren = aiNode.mChildren();
        for (int i = 0; i < numChildren; i++) {
            AINode aiChildNode = AINode.create(aiChildren.get(i));
            AnimNode childAnimNode = AnimMeshesLoader.processNodesHierarchy(aiChildNode, animNode);
            animNode.addChild(childAnimNode);
        }

        return animNode;
    }

    private static Matrix4f toMatrix(AIMatrix4x4 aiMatrix4x4) {
        Matrix4f result = new Matrix4f();
        result.m00(aiMatrix4x4.a1());
        result.m10(aiMatrix4x4.a2());
        result.m20(aiMatrix4x4.a3());
        result.m30(aiMatrix4x4.a4());
        result.m01(aiMatrix4x4.b1());
        result.m11(aiMatrix4x4.b2());
        result.m21(aiMatrix4x4.b3());
        result.m31(aiMatrix4x4.b4());
        result.m02(aiMatrix4x4.c1());
        result.m12(aiMatrix4x4.c2());
        result.m22(aiMatrix4x4.c3());
        result.m32(aiMatrix4x4.c4());
        result.m03(aiMatrix4x4.d1());
        result.m13(aiMatrix4x4.d2());
        result.m23(aiMatrix4x4.d3());
        result.m33(aiMatrix4x4.d4());

        return result;
    }
}
