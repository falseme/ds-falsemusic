package net.falseme.discord.falsemusic.bot.musicplayer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

/**
 * Store songs in a LinkedQueue so as to play them using an AudioPlayer
 * 
 * @author Falseme (Fabricio Tomás)
 */
public class AudioPlayList extends AudioEventAdapter implements AudioLoadResultHandler {

	private final AudioPlayer audioPlayer;
	private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();

	/**
	 * Creates a playlist that uses an audio player
	 * 
	 * @param player The Audio Player
	 * @see AudioPlayer
	 */
	public AudioPlayList(AudioPlayer player) {

		audioPlayer = player;

	}

	/**
	 * Adds the song to the playlist queue. If empty, then starts the song without
	 * add to a queue.
	 * 
	 * @param track The song track
	 */
	public void addToQueue(AudioTrack track) {

		if (!audioPlayer.startTrack(track, true))
			queue.offer(track);

	}

	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

	public BlockingQueue<AudioTrack> getQueue() {
		return queue;
	}

	/**
	 * When a song ends, it reproduces the next one in the queue. Used when a song
	 * ends by itself or a user skiped it.
	 */
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		player.startTrack(queue.poll(), false);
	}

	/**
	 * When an Audio Track is loaded it adds it to the queue
	 */
	@Override
	public void trackLoaded(AudioTrack track) {

		addToQueue(track);

	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {

	}

	@Override
	public void noMatches() {

	}

	@Override
	public void loadFailed(FriendlyException exception) {

	}

}
