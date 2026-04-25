package com.mygame;


import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;



/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    private com.mygame.RenderManager renderManagermg;
    private BlockSelector blockSelector;
    WorldAccess worldAccess;
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        // Your existing init
        
        TestInit.init(rootNode, flyCam, assetManager);

        // 1. Initialize WorldAccess (points to a folder for saving/loading)
        worldAccess = new WorldAccess("worlds/my_world");
        var player = new Player();
        player.setWorldPosition(new Vector3f(1,1,1));
        // 2. Initialize RenderManager
        // Pass the worldAccess, the node to attach chunks to (rootNode), and the assetManager
        
        this.renderManagermg = new com.mygame.RenderManager(worldAccess, rootNode, assetManager, player, this);
        this.blockSelector = new BlockSelector(cam, inputManager, rootNode);

        // Optional: Configure view distance (default was 8)
        // renderManager.setViewDistance(5);
        
        try {
            ChunkPos firstChunk = new ChunkPos(0, 0, 3);
            worldAccess.createChunkAt(firstChunk, 1);
        
            
            // This tells the RenderManager:build the mesh!"
            renderManagermg.markDirty(firstChunk);
        }
        catch (Exception e) { System.out.println("er at mk chunk");
        
        }
        
    }
    @Override
    public void simpleUpdate(float tpf) {
        // 3. Update the RenderManager every frame
        // This calculates which chunks should be loaded based on camera position
        renderManagermg.tick(
            cam.getLocation().x, 
            cam.getLocation().y, 
            cam.getLocation().z
        );
        
        Object[] blockCoords = blockSelector.getBlockCoords();
        
    }


    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    @Override
    public void destroy() {
        // Assuming worldAccess is accessible here
        if (worldAccess != null) {
            worldAccess.saveAll();
        }
        super.destroy(); // Continue with standard shutdown
    }

}

