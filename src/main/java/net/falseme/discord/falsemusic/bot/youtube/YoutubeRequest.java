package net.falseme.discord.falsemusic.bot.youtube;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

import net.dv8tion.jda.api.entities.Guild;
import net.falseme.discord.falsemusic.env.Env;

/**
 * Manage the youtube requests executing queries by the Youtube Data API and
 * storing the responses inside a hashmap.
 * 
 * @author Falseme (Fabricio Tomás)
 */
public class YoutubeRequest {

	private static YouTube youtubeClient;
	/**
	 * The maximum number of search request that will be made
	 */
	public static final long MAX_VID_SEARCH = 5;
	/**
	 * HashMap where all the GET responses are stored using the GuildID as a Long
	 */
	private static Map<Long, YoutubeResponse> guildYoutubeResponses = new HashMap<>();

	/**
	 * Searches for videos using the query given by the user.
	 * 
	 * @param query The user query
	 * @param guild The discord guild/server the bot was called from
	 * @throws IOException if there was a problem connecting or getting a response
	 * @return A YoutubeResponse with all the song data. Also stores it inside a
	 *         hashmap.
	 */
	public static YoutubeResponse executeQuery(String query, Guild guild) throws IOException {

		if (youtubeClient == null)
			youtubeClient = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
				@Override
				public void initialize(HttpRequest request) throws IOException {
				}
			}).setApplicationName("FalseMusic DSBot").build();

		YouTube.Search.List search = youtubeClient.search().list("id,snippet");
		search.setKey(Env.get("YOUTUBE_API_KEY"));
		search.setQ(query);
		search.setType("video");
		// To increase efficiency, only retrieve the fields that the application uses.
		search.setFields("items(id/kind,id/videoId,snippet/title,snippet/channelTitle,snippet/thumbnails/default/url)");
		search.setMaxResults(MAX_VID_SEARCH);

		SearchListResponse searchResponse = search.execute();
		return guildYoutubeResponses.compute(guild.getIdLong(), (guild_id, guild_responses) -> {
			return new YoutubeResponse(searchResponse.getItems());
		});

	}

	/**
	 * Get the Youtube Response previously stored on a hashmap using the
	 * guild/server id
	 * 
	 * @param guild The discord guild/server
	 * @return the YoutubeResponse of the previos search in the guild/server, or
	 *         null if empty.
	 */
	public static YoutubeResponse get(Guild guild) {

		if (!guildYoutubeResponses.containsKey(guild.getIdLong()))
			return null;

		return guildYoutubeResponses.get(guild.getIdLong());

	}

}
