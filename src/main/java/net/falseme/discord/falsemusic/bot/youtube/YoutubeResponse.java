package net.falseme.discord.falsemusic.bot.youtube;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.api.services.youtube.model.SearchResult;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class YoutubeResponse {

	Map<Integer, Map<String, String>> resultData = new HashMap<>();

	public YoutubeResponse(List<SearchResult> result) {

		Iterator<SearchResult> iterator = result.iterator();
		int index = 0;

		while (iterator.hasNext()) {

			resultData.put(index, new HashMap<String, String>());

			SearchResult searchResult = iterator.next();
			resultData.get(index).put("url",
					String.format("https://www.youtube.com/watch?v=%s", searchResult.getId().getVideoId()));
			resultData.get(index).put("author", searchResult.getSnippet().getChannelTitle());
			resultData.get(index).put("songName", searchResult.getSnippet().getTitle());
			resultData.get(index).put("thumbnail", searchResult.getSnippet().getThumbnails().getDefault().getUrl());

			index++;

		}

	}

	public List<MessageEmbed> getEmbedMessages() {

		List<MessageEmbed> embeds = new LinkedList<>();

		for (int i = 0; i < resultData.size(); i++)
			embeds.add(buildEmbed(i));

		return embeds;

	}

	public MessageEmbed getEmbed(int index) {

		if (!resultData.containsKey(index))
			return null;

		return buildEmbed(index);

	}

	public String getUrl(int index) {

		if (!resultData.containsKey(index))
			return null;

		return resultData.get(index).get("url");

	}

	public String getSongName(int index) {

		if (!resultData.containsKey(index))
			return null;

		return resultData.get(index).get("songName");

	}

	public String getAuthor(int index) {

		if (!resultData.containsKey(index))
			return null;

		return resultData.get(index).get("author");

	}

	public String getThumbnail(int index) {

		if (!resultData.containsKey(index))
			return null;

		return resultData.get(index).get("thumbnail");

	}

	private MessageEmbed buildEmbed(int index) {

		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(new Color(0xfe6906));

		// Use 'index+1' to make it easier for users
		eb.addField(String.format("[%d] %s", index + 1, resultData.get(index).get("songName")),
				"Author: " + resultData.get(index).get("author"), false);
		eb.addField(String.format("Use `/play %d` or `/play [url]`", index + 1), resultData.get(index).get("url"),
				false);
		eb.setThumbnail(resultData.get(index).get("thumbnail"));

		return eb.build();

	}

}
