package com.benbenlaw.multiblocks.command;

import com.benbenlaw.multiblocks.Multiblocks;
import com.benbenlaw.multiblocks.structure.StructureDiagnostics;
import com.benbenlaw.multiblocks.structure.StructureManager;
import com.benbenlaw.multiblocks.structure.StructurePattern;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Comparator;
import java.util.List;

/**
 * /bblmultiblocks structures: lists the loaded structures.
 * /bblmultiblocks check &lt;structure&gt; [controller pos]: shows what doesn't match around a controller (the block you look at by default).
 */
@EventBusSubscriber(modid = Multiblocks.MOD_ID)
public class MultiblocksCommands {

    private static final int MAX_LISTED = 10;

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(Multiblocks.MOD_ID)
                .then(Commands.literal("structures").executes(MultiblocksCommands::listStructures))
                .then(Commands.literal("check")
                        .then(Commands.argument("structure", IdentifierArgument.id())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggestResource(StructureManager.INSTANCE.all().keySet(), builder, ""))
                                .executes(context -> check(context, lookedAtBlock(context)))
                                .then(Commands.argument("controller", BlockPosArgument.blockPos())
                                        .executes(context -> check(context, BlockPosArgument.getBlockPos(context, "controller")))))));
    }

    private static int listStructures(CommandContext<CommandSourceStack> context) {
        List<Identifier> ids = StructureManager.INSTANCE.all().keySet().stream().sorted(Comparator.comparing(Identifier::toString)).toList();

        context.getSource().sendSuccess(() -> Component.literal(ids.size() + " structures loaded").withStyle(ChatFormatting.GREEN), false);
        for (Identifier id : ids) {
            context.getSource().sendSuccess(() -> Component.literal("- " + id), false);
        }
        return ids.size();
    }

    private static BlockPos lookedAtBlock(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        HitResult hit = player.pick(8.0, 0.0F, false);

        if (hit instanceof BlockHitResult blockHit && blockHit.getType() == HitResult.Type.BLOCK) return blockHit.getBlockPos();
        return player.blockPosition();
    }

    private static int check(CommandContext<CommandSourceStack> context, BlockPos controller) {
        CommandSourceStack source = context.getSource();
        Identifier id = IdentifierArgument.getId(context, "structure");

        StructurePattern pattern = StructureManager.INSTANCE.get(id);
        if (pattern == null) {
            source.sendFailure(Component.literal("No structure called " + id + " is loaded. Use /" + Multiblocks.MOD_ID + " structures to see the loaded ones."));
            return 0;
        }

        StructureDiagnostics.Report report = StructureDiagnostics.check(source.getLevel(), controller, pattern, MAX_LISTED);

        if (report.matches()) {
            source.sendSuccess(() -> Component.literal("All " + report.total() + " blocks of " + id + " match around " + controller.toShortString()).withStyle(ChatFormatting.GREEN), false);
            return 1;
        }

        source.sendSuccess(() -> Component.literal(report.wrong() + " of " + report.total() + " blocks of " + id + " are wrong around " + controller.toShortString()
                + " (the controller is expected there)").withStyle(ChatFormatting.RED), false);

        for (StructureDiagnostics.Mismatch mismatch : report.mismatches()) {
            String note = mismatch.emptyTag() ? "  <- that tag has no blocks, does it exist?" : "";
            source.sendSuccess(() -> Component.literal("- " + mismatch.pos().toShortString() + " (" + mismatch.dx() + ", " + mismatch.dy() + ", " + mismatch.dz()
                    + "): wants " + mismatch.expected() + ", found " + mismatch.found() + note), false);
        }

        if (report.wrong() > report.mismatches().size()) {
            int more = report.wrong() - report.mismatches().size();
            source.sendSuccess(() -> Component.literal("...and " + more + " more").withStyle(ChatFormatting.GRAY), false);
        }
        return 0;
    }
}
