/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkduckdevelopers.util;

import com.darkduckdevelopers.components.CollideComponent;
import com.darkduckdevelopers.components.RenderComponent;
import com.darkduckdevelopers.components.TransformComponent;
import com.darkduckdevelopers.objects.Entity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;


/**
 *
 * @author Ethan
 */
public class LevelImporter {
    private static Object temporaryGameEntities;
    
    public static void loadLevel(List<Entity> entitites, String levelFile, Loader loader) {
        BufferedReader reader =  null;
        try {
            InputStreamReader isr = new InputStreamReader(Class.class.getResourceAsStream(levelFile));
            reader = new BufferedReader(isr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String line = "";
        while(line != null) {
            try {
                line = reader.readLine();
            } catch (Exception e) {
                
                e.printStackTrace();
            }
            char[] string_chars = line.toCharArray();
            byte[][] bytes = new byte[string_chars.length/9][9];
            for (int i = 0; i < (string_chars.length/9); i++) {
                byte[] tile = bytes[i];
                float x;
                int x_as_int = (tile[0] & 0xFF) | ((tile[1] & 0xFF) << 8) | ((tile[2] & 0xFF) << 16) | ((tile[3] & 0xFF) << 24);
                x = Float.intBitsToFloat(x_as_int);
                float y;
                int y_as_int = (tile[4] & 0xFF) | ((tile[5] & 0xFF) << 8) | ((tile[6] & 0xFF) << 16) | ((tile[7] & 0xFF) << 24);
                y = Float.intBitsToFloat(y_as_int);
                int id = tile[8] & 0xFF;
                
                Entity e = new Entity();
                TransformComponent transform = new TransformComponent(new Vector2f(0.2f*x, 0.2f*y), 0f, new Vector2f(0.1f, 0.1f));
                
                RenderComponent render = new RenderComponent(renderer, transform, groundTexture, 0, true);
				ground.addComponent(render);
				if (j == 0) {
					CollideComponent collider = new CollideComponent(transform, 0, 0f);
					ground.addComponent(collider);
				}
				temporaryGameEntities.add(ground);
                
            }
        }
    }
    
}
