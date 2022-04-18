package dev.rise.anticheat.check;

import dev.rise.anticheat.check.api.CheckInfo;
import dev.rise.anticheat.data.PlayerData;
import dev.rise.util.InstanceAccess;
import lombok.Getter;
import net.minecraft.network.Packet;

@Getter
public abstract class Check implements InstanceAccess {

    public final PlayerData data;

    private final CheckInfo checkInfo;

    protected double violations, buffer;

    public Check(final PlayerData data) {
        this.data = data;

        if (this.getClass().isAnnotationPresent(CheckInfo.class)) {
            this.checkInfo = this.getClass().getAnnotation(CheckInfo.class);
        } else {
            this.checkInfo = null;
            throw new RuntimeException("CheckInfo annotation not found in " + this.getClass().getSimpleName());
        }
    }

    public abstract void handle(final Packet<?> packet);

    protected final void fail() {
        ++this.violations;

        instance.getAntiCheat().getAlertManager().sendAlert(this);
    }

    public final double increaseBuffer() {
        return buffer = Math.min(10000, buffer + 1);
    }

    public final double increaseBufferBy(final double amount) {
        return buffer = Math.min(10000, buffer + amount);
    }

    public final double decreaseBuffer() {
        return buffer = Math.max(0, buffer - 1);
    }

    public final double decreaseBufferBy(final double amount) {
        return buffer = Math.max(0, buffer - amount);
    }

    public final void resetBuffer() {
        buffer = 0;
    }

    public final void setBuffer(final double amount) {
        buffer = amount;
    }

    public final void multiplyBuffer(final double multiplier) {
        buffer *= multiplier;
    }
}
