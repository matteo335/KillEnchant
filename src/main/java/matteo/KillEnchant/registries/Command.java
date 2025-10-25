package matteo.KillEnchant.registries;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;

public class Command {
    public static void command(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("KillEnchant")
                        .requires(command -> command.hasPermission(0))
                        .then(Commands.literal("self")
                                .executes(command -> {
                                    Player player = command.getSource().getPlayer();
                                    CompoundTag data = player.getPersistentData().getCompoundOrEmpty(Player.PERSISTED_NBT_TAG);
                                    int killEnchant = data.getInt("KillEnchant").orElse(30);

                                    command.getSource().getPlayer().displayClientMessage(Component.literal("Your EnchantLevel is " + killEnchant), false);
                                    return 1;
                                }))

                        .then(Commands.literal("help")
                                .executes(command -> {
                                    command.getSource().getPlayer().displayClientMessage(Component.translatable("This mod change the level requirement §oinfluences§r in the enchantment table, but without changing the level or lapis lazuli consumption. This mod is pretty much tied to a event provided by NeoForge and its methods.\n\nThe higher vanila enchantment level is 30, this score will increase or decrease by a default of 6 if you kill/get killed by another player or use the enchant_levelup item, these numbers can be configured in the configs.\n\nYou can go up to 110 and down to 1 with KillEnchant (any higher or lower would break the system), I recommend using BeyondEnchant with this mod.\n\n§oCough Cough§r I quote\nhttps://minecraft.wiki/w/Enchanting_Table\n\nThe level requirement influences the quantity, type, and level of enchantments instilled in the item, with a higher experience level generally resulting in more and/or higher-level enchantments. Nevertheless, there is a significant random factor, and even a level 30 enchantment (the maximum) doesn't guarantee more than one enchantment, or even that enchantments are maximum strength — a level 30 enchantment can still yield Fortune II or Efficiency III alone, for example."), false);
                                    return 1;
                                }))

                        .requires(command -> command.hasPermission(2))
                        .then(Commands.literal("get")
                                .then(Commands.argument("player", EntityArgument.players())
                                .executes(command -> {
                                    Player target = EntityArgument.getPlayer(command, "player");
                                    CompoundTag data = target.getPersistentData().getCompoundOrEmpty(Player.PERSISTED_NBT_TAG);
                                    int killEnchant = data.getInt("KillEnchant").orElse(30);

                                    command.getSource().getPlayer().displayClientMessage(Component.literal( target.getName().getString() + " score is " + killEnchant), false);

                                    return 1;
                                })))


                .then(Commands.literal("set")
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.argument("score", IntegerArgumentType.integer())
                                        .executes(command -> {
                                            Player target = EntityArgument.getPlayer(command, "player");
                                            CompoundTag data = target.getPersistentData().getCompoundOrEmpty(Player.PERSISTED_NBT_TAG);
                                            int score = IntegerArgumentType.getInteger(command, "score");
                                            int killEnchant = data.getInt("KillEnchant").orElse(30);

                                            data.putInt("KillEnchant", score);

                                            target.getPersistentData().put(Player.PERSISTED_NBT_TAG, data);
                                            target.displayClientMessage(Component.literal("The score of " + target.getName().getString()+ "(" + killEnchant + ")" + " is now " + score), false);
                                            return 1;
                                }))))


                .then(Commands.literal("add")
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.argument("add ..Or subtract?", IntegerArgumentType.integer())
                                .executes(command -> {
                                    Player target = EntityArgument.getPlayer(command, "player");
                                    CompoundTag data = target.getPersistentData().getCompoundOrEmpty(Player.PERSISTED_NBT_TAG);
                                    int killEnchant = data.getInt("KillEnchant").orElse(30);

                                    target.displayClientMessage(Component.literal("You added " + IntegerArgumentType.getInteger(command, "add ..Or subtract?") + " to " + target.getName().getString() + "(" + killEnchant + ") making " + (killEnchant + IntegerArgumentType.getInteger(command, "add ..Or subtract?"))), false);

                                    killEnchant = killEnchant + IntegerArgumentType.getInteger(command, "add ..Or subtract?");
                                    data.putInt("KillEnchant", killEnchant);

                                    target.getPersistentData().put(Player.PERSISTED_NBT_TAG, data);
                                    return 1;
                                }))))
                );
    }
}
