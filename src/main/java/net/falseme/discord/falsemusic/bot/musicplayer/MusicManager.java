package net.falseme.discord.falsemusic.bot.musicplayer;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import net.dv8tion.jda.api.entities.Guild;

/**
 * The Music Manager controls everything related to music within the bot
 * features. <br>
 * <br>
 * It has some static members so as work on every Discord Guild/Server.
 * 
 * @author Falseme (Fabricio Tomás)
 */
public class MusicManager {

	private static Map<Long, MusicManager> musicManagers = new HashMap<>();
	private static AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

	private AudioPlayList audioPlayList;
	private AudioHandler audioHandler;

	/**
	 * Builds a Music Manager with a playlist and a handler
	 * 
	 * @see AudioPlayList
	 * @see AudioHandler
	 */
	public MusicManager() {

		AudioPlayer audioPlayer = audioPlayerManager.createPlayer();

		audioPlayList = new AudioPlayList(audioPlayer);
		audioPlayer.addListener(audioPlayList);
		audioHandler = new AudioHandler(audioPlayer);

	}

	/**
	 * Register audio sources
	 */
	public static void load() {

		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		AudioSourceManagers.registerLocalSource(audioPlayerManager);

	}

	/**
	 * Retuns the MusicManager for the given Guild. If does not exist, creates a new
	 * one and adds it to the hashmap after implementing it to the guild using the
	 * AudioHandler.
	 * 
	 * @param guild The discord guild the music manager will be used for.
	 */
	public static MusicManager getMusicManager(Guild guild) {

		if (!musicManagers.containsKey(guild.getIdLong())) {

			MusicManager musicManager = new MusicManager();
			guild.getAudioManager().setSendingHandler(musicManager.getAudioHandler());
			musicManagers.put(guild.getIdLong(), musicManager);

		}

		return musicManagers.get(guild.getIdLong());

	}

	/**
	 * Plays a song by the given URL on a specific Guild.
	 * 
	 * @param guild The Discord Guild
	 * 
	 * @param URL   the song url
	 * 
	 * @see AudioPlayList
	 */
	public static void play(Guild guild, String URL) {

		MusicManager musicManager = getMusicManager(guild);
		audioPlayerManager.loadItemOrdered(musicManager, URL, musicManager.getAudioPlayList());

	}

	public AudioPlayList getAudioPlayList() {
		return audioPlayList;
	}

	public AudioHandler getAudioHandler() {
		return audioHandler;
	}

}
