package dev.tr7zw.exordium.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.tr7zw.exordium.ExordiumModBase;
import dev.tr7zw.exordium.components.BufferInstance;
import dev.tr7zw.exordium.components.vanilla.HotbarComponent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
//#if MC >= 12100
import net.minecraft.client.DeltaTracker;
//#endif

@Mixin(Gui.class)
public class GuiHotbarMixin {

    //#if MC >= 12100
    @WrapOperation(method = "renderHotbarAndDecorations", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderItemHotbar(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V") })
    private void renderHotbarWrapper(Gui gui, GuiGraphics guiGraphics, DeltaTracker f,
            final Operation<Void> operation) {
        //#elseif MC >= 12005
        //$$     @WrapOperation(method = "renderHotbarAndDecorations", at = {
        //$$         @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderItemHotbar(Lnet/minecraft/client/gui/GuiGraphics;F)V")})
        //$$ private void renderHotbarWrapper(Gui gui, GuiGraphics guiGraphics, float f, final Operation<Void> operation) {
        //#else
        //$$ @WrapOperation(method = "render", at = {
        //$$         @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHotbar(FLnet/minecraft/client/gui/GuiGraphics;)V"), })
        //$$ private void renderHotbarWrapper(Gui gui, float f, GuiGraphics guiGraphics, final Operation<Void> operation) {
        //#endif
        BufferInstance<Void> buffer = ExordiumModBase.instance.getBufferManager()
                .getBufferInstance(HotbarComponent.getId(), Void.class);
        if (!buffer.renderBuffer(null, guiGraphics)) {
            //#if MC >= 12005
            operation.call(gui, guiGraphics, f);
            //#else
            //$$ operation.call(gui, f, guiGraphics);
            //#endif
        }
        buffer.postRender(null, guiGraphics);
    }

    // FIXME broken
    //    @WrapOperation(method = "renderHotbarAndDecorations", at = {
    //            @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/spectator/SpectatorGui;renderHotbar(Lnet/minecraft/client/gui/GuiGraphics;)V"), })
    //    private void renderHotbarWrapperSpectator(SpectatorGui gui, GuiGraphics guiGraphics,
    //            final Operation<Void> operation) {
    //        BufferInstance<Void> buffer = ExordiumModBase.instance.getBufferManager()
    //                .getBufferInstance(HotbarComponent.getId(), Void.class);
    //        if (!buffer.renderBuffer(0, null)) {
    //            operation.call(gui, guiGraphics);
    //        }
    //        buffer.postRender(null);
    //    }

}
