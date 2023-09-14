package net.falseme.discord.falsemusic.bot.musicplayer;

import java.nio.ByteBuffer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

/**
 * @author Falseme (Fabricio Tomás)
 */
public class AudioHandler implements AudioSendHandler {

	private final AudioPlayer audioPlayer;
	private final ByteBuffer buffer = ByteBuffer.allocate(1024);
	private final MutableAudioFrame frame = new MutableAudioFrame();

	/**
	 * Builds an Audio Handler for the given Audio Player
	 * 
	 * @param player The Audio Player
	 * 
	 * @see MusicManager
	 */
	public AudioHandler(AudioPlayer player) {

		audioPlayer = player;
		frame.setBuffer(buffer);

	}

	@Override
	public boolean canProvide() {
		return audioPlayer.provide(frame);
	}

	@Override
	public ByteBuffer provide20MsAudio() {
		return buffer.flip();
	}

	@Override
	public boolean isOpus() {
		return true;
	}

}
