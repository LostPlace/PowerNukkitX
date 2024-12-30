package cn.nukkit.entity.ai.executor;

import cn.nukkit.entity.EntityIntelligent;
import cn.nukkit.level.Sound;
import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class PlaySoundExecutor implements IBehaviorExecutor {

    private Sound sound;
    float minPitch, maxPitch;
    float minVolume, maxVolume;

    public PlaySoundExecutor(Sound sound) {
        this.sound = sound;
    }


    @Override
    public boolean execute(EntityIntelligent entity) {
        float volume = minVolume == maxVolume ? minVolume : ThreadLocalRandom.current().nextFloat(minVolume, maxVolume);
        float pitch = minPitch == maxPitch ? minPitch : ThreadLocalRandom.current().nextFloat(minPitch, maxPitch);
        entity.getValidLevel().addSound(entity, sound, volume, pitch);
        return true;
    }
}