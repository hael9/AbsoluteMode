package com.github.hael9.absolutemode.mixin;

import com.github.hael9.absolutemode.AbsoluteMode;
import net.minecraft.util.MouseHelper;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.hael9.absolutemode.AbsoluteMode.lastX;
import static com.github.hael9.absolutemode.AbsoluteMode.lastY;

@Mixin(MouseHelper.class)
public class MixinMouseHelper {
    @Shadow public int deltaX;
    @Shadow public int deltaY;

    private final int thresold = 5;
    int counter = 0;



    @Inject(method = "mouseXYChange", at = @At("HEAD"), cancellable = true)
    public void onMouseXYChange(CallbackInfo ci) {
        if (!AbsoluteMode.toggled) return;

        int dX = (int)((double) Mouse.getDX() * AbsoluteMode.xSensitivity);
        int dY = (int)((double) Mouse.getDY() * AbsoluteMode.ySensitivity);

        // were not moving, 0 - lastPos = some wack number
        if (dX != 0 && lastX != 0) this.deltaX = dX - lastX;
        if (dY != 0 && lastY != 0) this.deltaY = dY - lastY;

        if (dX == 0 && dY == 0) {
            counter++;
        }
        else if (counter > thresold) {
            lastX = dX;
            lastY = dY;
        }
        else counter = 0;

        // these need to get set or else stopping a movement will just keep the cursor moving
        if (dX == 0 || lastX == 0) this.deltaX = 0;
        if (dY == 0 || lastY == 0) this.deltaY = 0;

        // if you actually want absolute mode, don't set this if dX/dY = 0, you'll fail rotation checks though
        lastX = dX;
        lastY = dY;

        // don't want the next part to run even though it probably doesn't matter
        ci.cancel();
    }
}
