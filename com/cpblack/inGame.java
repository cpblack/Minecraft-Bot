package com.cpblack;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectData;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.github.steveice10.mc.protocol.data.game.world.WorldType;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Caleb on 3/11/2017.
 */
public class inGame {
    public static HashMap<java.lang.Integer, entity> entityList = new HashMap<java.lang.Integer, entity>();
    public static class player {
        public static int heldSlot;
        public static int level;
        public static int totalExperience;
        public static int food;
        public static Float health;
        public static Float saturation;
        public static String inGameName = "L1lith";
        public static int entityId;
        public static GameMode gameMode;
        public static Double x;
        public static Double y;
        public static Double z;
        public static Float yaw;
        public static Float pitch;
        public static boolean isGrounded;
        public static class inventory {
        int items = new int[4][9];
        int equipment = new int[5];
        public int[] getHotbar(){
        return this.items[3];
        }
        public void setHotbar(int[] listIn) {
            this.items[3] = listIn;
        }
    }
    public static class world {
        public static WorldType worldType;
        public static int dimension;
        public static Difficulty difficulty;
        public static Long time;
        public static HashMap<Integer,entity> entityList = new HashMap<Integer,entity>();
        public static class chunks {

        }
    }
    public static class server {
        public static int maxPlayers;
    }
    static class entity {
        public EntityMetadata metadata[];
        public int entityId;
        public float yaw;
        public float pitch;
        public Double x;
        public Double y;
        public Double z;
        public float headYaw;
        public boolean isGrounded;
        entity (int entityIdIn, Double xIn, Double yIn, Double zIn, float yawIn, float pitchIn){
            entityId = entityIdIn;
            x = xIn;
            y = yIn;
            z = zIn;
            yaw = yawIn;
            pitch = pitchIn;
        }
    }
    static class playerEntity extends entity {
        public UUID uuid;
        playerEntity(int entityIdIn,UUID uuidIn, Double xIn, Double yIn, Double zIn, float yawIn, float pitchIn){ super(entityIdIn,xIn,yIn,zIn,yawIn,pitchIn); uuid = uuidIn;}
    }
    static class objectEntity extends entity {
        public static ObjectData objectData;
        public static ObjectType objectType;
        objectEntity(int entityIdIn, Double xIn, Double yIn, Double zIn, float yawIn, float pitchIn){ super(entityIdIn,xIn,yIn,zIn,yawIn,pitchIn);}
    }
    static class mobEntity extends entity {
        public MobType mob;
        mobEntity(int entityIdIn,MobType mobIn, Double xIn, Double yIn, Double zIn, float yawIn, float pitchIn){ super(entityIdIn,xIn,yIn,zIn,yawIn,pitchIn); mob = mobIn;}
    }

}

