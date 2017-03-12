package com.cpblack;

import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.TranslationMessage;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.mc.protocol.data.status.handler.ServerPingTimeHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerSetExperiencePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.*;
import com.github.steveice10.mc.protocol.packet.login.server.LoginSuccessPacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;

import java.net.Proxy;
import java.util.Arrays;

/**
 * Created by Caleb on 3/11/2017.
 */
public class Connection2 {
    public static String username = "";
    public static String password = "";
    public static String host = "localhost";
    public static int port = 25565;
    public static MinecraftProtocol protocol;
    public static Client client;
    public static Proxy proxy = Proxy.NO_PROXY;
    public static Proxy AuthProxy = Proxy.NO_PROXY;


    public static void initialize() {
        client.getSession().connect();
    }


    public static void prepare() {
        try {
            protocol = new MinecraftProtocol(username, password, false);
            System.out.println("Successfully authenticated user.");
        } catch (RequestException e) {
            e.printStackTrace();
            return;
        }

        client = new Client(host, port, protocol, new TcpSessionFactory(proxy));
        client.getSession().setFlag(MinecraftConstants.AUTH_PROXY_KEY, AuthProxy);
        client.getSession().addListener(new SessionAdapter() {
            @Override
            public void packetReceived(PacketReceivedEvent event) {
                handlePacketRecievedEvent(event);
            }

            @Override

            public void disconnected(DisconnectedEvent event) {
                handleDisconnectEvent(event);
            }
        });
        client.getSession().setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, new ServerInfoHandler() {
            @Override
            public void handle(Session session, ServerStatusInfo info) {
                handleServerStatusInfo(session, info);
            }
        });

        client.getSession().setFlag(MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY, new ServerPingTimeHandler() {
            @Override
            public void handle(Session session, long pingTime) {
                handleServerPingTime(session, pingTime);
            }
        });


    }


    public static void handleServerPingTime(Session session, long pingTime) {
        System.out.println("Server ping took " + pingTime + "ms");
    }

    public static void handleServerStatusInfo(Session session, ServerStatusInfo info) {
        System.out.println("Version: " + info.getVersionInfo().getVersionName() + ", " + info.getVersionInfo().getProtocolVersion());
        System.out.println("Player Count: " + info.getPlayerInfo().getOnlinePlayers() + " / " + info.getPlayerInfo().getMaxPlayers());
        System.out.println("Players: " + Arrays.toString(info.getPlayerInfo().getPlayers()));
        System.out.println("Description: " + info.getDescription().getText());
        System.out.println("Icon: " + info.getIcon());
    }

    public static void handlePacketRecievedEvent(PacketReceivedEvent event) {
        Packet packet = event.getPacket();
        if (packet instanceof ServerSpawnPositionPacket) {
            ServerSpawnPositionPacket formPacket = (ServerSpawnPositionPacket) packet;

            Position coords = formPacket.getPosition();

            //inGame.player.x = coords.getX();
            //inGame.player.y = coords.getY();
            //inGame.player.z = coords.getZ();
        } else if (packet instanceof ServerPlayerPositionRotationPacket) {
            ServerPlayerPositionRotationPacket formPacket = (ServerPlayerPositionRotationPacket) packet;
            inGame.player.x = formPacket.getX();
            inGame.player.y = formPacket.getY();
            inGame.player.z = formPacket.getZ();
            inGame.player.yaw = formPacket.getYaw();
            inGame.player.pitch = formPacket.getPitch();
            System.out.println("Position Recieved: " + inGame.player.x + ", " + inGame.player.y);
        } else if (packet instanceof ServerChatPacket) {
            ServerChatPacket form = (ServerChatPacket) packet;
            Message message = form.getMessage();

            if (message instanceof TranslationMessage) {
                handleChatArray(((TranslationMessage) message).getTranslationParams());
            } else {
                handleChat(message.getFullText());
            }
        } else if (packet instanceof ServerUpdateTimePacket) {
            ServerUpdateTimePacket form = (ServerUpdateTimePacket) packet;
            inGame.world.time = form.getTime();
        } else if (packet instanceof ServerJoinGamePacket) {
            ServerJoinGamePacket form = (ServerJoinGamePacket) packet;
            inGame.player.gameMode = form.getGameMode();
            inGame.player.entityId = form.getEntityId();
            inGame.world.difficulty = form.getDifficulty();
            inGame.world.dimension = form.getDimension();
            inGame.server.maxPlayers = form.getMaxPlayers();
            inGame.world.worldType = form.getWorldType();
        } else if (packet instanceof ServerPluginMessagePacket) {
        } else if (packet instanceof ServerEntityPositionRotationPacket) {

        } else if (packet instanceof ServerEntityHeadLookPacket) {
        } else if (packet instanceof ServerEntityTeleportPacket) {
            ServerEntityTeleportPacket form = (ServerEntityTeleportPacket) packet;
            inGame.entity tempEnt = inGame.entityList.get(form.getEntityId());
            tempEnt.x = form.getX();
            tempEnt.y = form.getY();
            tempEnt.z = form.getZ();
            tempEnt.yaw = form.getYaw();
            tempEnt.pitch = form.getPitch();
            tempEnt.isGrounded = form.isOnGround();
            inGame.entityList.put(tempEnt.entityId, tempEnt);

            if (form.getEntityId() == inGame.player.entityId) {
                inGame.player.x = form.getX();
                inGame.player.y = form.getY();
                inGame.player.z = form.getZ();
                inGame.player.yaw = form.getYaw();
                inGame.player.pitch = form.getPitch();
                inGame.player.isGrounded = form.isOnGround();
            }
        } else if (packet instanceof ServerEntityStatusPacket) {
        } else if (packet instanceof ServerEntityPositionPacket) {
            ServerEntityPositionPacket form = (ServerEntityPositionPacket) packet;
            inGame.entity fetchEnt = inGame.entityList.get(form.getEntityId());
            fetchEnt.x += form.getMovementX();
            fetchEnt.y += form.getMovementY();
            fetchEnt.z += form.getMovementZ();
            fetchEnt.yaw = form.getYaw();
            fetchEnt.pitch = form.getPitch();
            inGame.entityList.put(fetchEnt.entityId, fetchEnt);
        } else if (packet instanceof ServerEntityRotationPacket) {
        } else if (packet instanceof ServerEntityVelocityPacket) {
        } else if (packet instanceof ServerEntityPropertiesPacket) {
        } else if (packet instanceof ServerEntityMetadataPacket) {
            ServerEntityMetadataPacket form = (ServerEntityMetadataPacket) packet;
            inGame.entity tempEnt = inGame.entityList.get(form.getEntityId());
            tempEnt.metadata = form.getMetadata();
            inGame.entityList.put(form.getEntityId(), tempEnt);
        } else if (packet instanceof ServerSpawnMobPacket) {
            ServerSpawnMobPacket form = (ServerSpawnMobPacket) packet;
            inGame.mobEntity mob = new inGame.mobEntity(form.getEntityId(), form.getType(), form.getX(), form.getY(), form.getZ(), form.getYaw(), form.getPitch());
            mob.metadata = form.getMetadata();
            mob.headYaw = form.getHeadYaw();
            inGame.entityList.put(form.getEntityId(), mob);
        } else if (packet instanceof ServerEntityEquipmentPacket) {
        } else if (packet instanceof ServerChunkDataPacket) {
            ServerChunkDataPacket form = (ServerChunkDataPacket) packet;
            handleChunkData(form.getColumn());
        } else if (packet instanceof ServerSpawnObjectPacket) {
            ServerSpawnObjectPacket form = (ServerSpawnObjectPacket) packet;
            inGame.objectEntity tempObj = new inGame.objectEntity(form.getEntityId(), form.getX(), form.getY(), form.getZ(), form.getYaw(), form.getPitch());
            tempObj.objectData = form.getData();
            tempObj.objectType = form.getType();
        } else if (packet instanceof ServerWorldBorderPacket) {
        } else if (packet instanceof ServerWindowItemsPacket) {
        } else if (packet instanceof ServerPlayerListEntryPacket) {
        } else if (packet instanceof ServerBlockChangePacket) {
            ServerBlockChangePacket form = (ServerBlockChangePacket) packet;
            handleBlockChange(form.getRecord());
        } else if (packet instanceof LoginSuccessPacket) {
        } else if (packet instanceof ServerSetSlotPacket) {
        } else if (packet instanceof ServerPlayEffectPacket) {
        } else if (packet instanceof ServerEntityDestroyPacket) {
            ServerEntityDestroyPacket form = (ServerEntityDestroyPacket) packet;
            int[] deaths = form.getEntityIds();
            for (int entityId : deaths) {
                if (entityId == inGame.player.entityId) {
                    //client.getSession().send(new Client(0));
                }
                if (inGame.world.entityList.containsKey(entityId)) {
                    inGame.world.entityList.remove(entityId);
                }
            }
        } else if (packet instanceof ServerPlayerHealthPacket) {
            ServerPlayerHealthPacket form = (ServerPlayerHealthPacket) packet;
            inGame.player.health = form.getHealth();
            inGame.player.saturation = form.getSaturation();
            inGame.player.food = form.getFood();
        } else if (packet instanceof ServerPlayerSetExperiencePacket) {
            ServerPlayerSetExperiencePacket form = (ServerPlayerSetExperiencePacket) packet;
            inGame.player.level = form.getLevel();
            inGame.player.totalExperience = form.getTotalExperience();
            //form.getSlot();
        } else if (packet instanceof ServerStatisticsPacket) {
            //ServerStatisticsPacket form = (ServerStatisticsPacket)packet;
            //form.getStatistics()
        } else if (packet instanceof ServerSpawnPlayerPacket) {
            ServerSpawnPlayerPacket form = (ServerSpawnPlayerPacket) packet;
            inGame.playerEntity tempPlayer = new inGame.playerEntity(form.getEntityId(), form.getUUID(), form.getX(), form.getY(), form.getY(), form.getYaw(), form.getPitch());
            tempPlayer.metadata = form.getMetadata();
            inGame.entityList.put(tempPlayer.entityId, tempPlayer);

        } else if (packet instanceof ServerPlayerChangeHeldItemPacket) {
            ServerPlayerChangeHeldItemPacket form = (ServerPlayerChangeHeldItemPacket) packet;
            inGame.player.heldSlot = form.getSlot();
        } else if (packet instanceof ServerDifficultyPacket) {
            ServerDifficultyPacket form = (ServerDifficultyPacket) packet;
            inGame.world.difficulty = form.getDifficulty();
        } else {
            if (Main.debug) {
                System.out.println(packet.getClass().toString());
            }
        }
    }

    public static void handleBlockChange(BlockChangeRecord record) {

    }

    public static void handleChunkData(Column data) {

    }

    public static void handleChatArray(Message[] message) {
        if (message.length == 2 && !message[0].toString().equalsIgnoreCase(inGame.player.inGameName)) {
            System.out.println(message[0].toString() + " said: " + message[1].toString());
        } else {
            String output = "";
            for (Message messageBit : message) {
                output += messageBit.getFullText();
            }
            System.out.println(output);
        }
    }

    public static void handleChat(String message) {
        System.out.println("Chat Recieved: " + message);
    }

    public static void handleDisconnectEvent(DisconnectedEvent event) {
        System.out.println("Disconnected from Server");

    }
}
