package ca.tristan.easycommands.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {

    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer;
    private MutableAudioFrame frame;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.buffer = ByteBuffer.allocate(1024);
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(buffer);
    }

    @Override
    public boolean canProvide() {
        return this.audioPlayer.provide(this.frame);
    }

    @Nullable
    @Override
    public ByteBuffer provide20MsAudio() {
        final Buffer tmp = ((Buffer) this.buffer).flip();
        return (ByteBuffer) tmp;
    }

    @Override
    public boolean isOpus() {
        return true;
    }

}
