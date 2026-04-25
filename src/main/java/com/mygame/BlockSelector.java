package com.mygame;

import com.jme3.collision.CollisionResults;
import com.jme3.collision.CollisionResult;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * BlockSelector is a utility class for mining block detection in a voxel world.
 * It uses raycasting from the camera to determine which block a player is targeting,
 * with different behavior for left-click (place/destroy forward) and right-click (destroy backward).
 * 
 * @author OxillenGlow 
 */
public class BlockSelector {
    private com.jme3.renderer.Camera cam;
    private InputManager inputManager;
    private Node rootNode;

    /**
     * Constructs a BlockSelector with the given camera, input manager, and scene root.
     * 
     * @param cam the jME3 camera to raycast from
     * @param inputManager the input manager for detecting mouse button clicks
     * @param rootNode the root node of the scene to collide rays against
     */
    public BlockSelector(com.jme3.renderer.Camera cam, InputManager inputManager, Node rootNode) {
        this.cam = cam;
        this.inputManager = inputManager;
        this.rootNode = rootNode;
    }

    /**
     * Calculates the 3D coordinates of the block being targeted by the player's camera.
     * 
     * Uses raycasting to detect which block is hit in 3D space:
     * - If left mouse button is pressed: moves the hit point 0.2 units away from camera
     * - If right mouse button is pressed: moves the hit point 0.2 units toward camera
     * 
     * @return an Object array containing: [int x, int y, int z, boolean isLeftClick]
     *         where x, y, and z are the floored block coordinates and isLeftClick indicates
     *         which mouse button was pressed. Returns null if neither button is pressed
     *         or no collision is detected.
     */
    public Object[] getBlockCoords() {
        boolean leftPressed = inputManager.isButtonPressed(MouseInput.BUTTON_LEFT);
        boolean rightPressed = inputManager.isButtonPressed(MouseInput.BUTTON_RIGHT);

        if (!leftPressed && !rightPressed) {
            return null;
        }

        // Create ray from camera
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        CollisionResults results = new CollisionResults();
        rootNode.collideWith(ray, results);

        if (results.size() == 0) {
            return null;
        }

        CollisionResult closest = results.getClosestCollision();
        Vector3f hitPoint = closest.getContactPoint();

        // Direction from cam to hit
        Vector3f direction = hitPoint.subtract(cam.getLocation()).normalize();

        Vector3f adjustedPoint;
        boolean isLeft;
        if (leftPressed) {
            // Move farther from cam by 0.3
            adjustedPoint = hitPoint.add(direction.mult(0.3f));
            isLeft = true;
        } else {
            // Right pressed: move inwards by 0.3
            adjustedPoint = hitPoint.subtract(direction.mult(0.3f));
            isLeft = false;
        }

        // Floor x, y, and z for 3D block coordinates
        int x = (int) Math.floor(adjustedPoint.x);
        int y = (int) Math.floor(adjustedPoint.y);
        int z = (int) Math.floor(adjustedPoint.z);

        return new Object[]{x, y, z, isLeft};
    }
}//
