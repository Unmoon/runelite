/*
 * Copyright (c) 2020, Unmoon <https://github.com/Unmoon>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.runedex;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
@Singleton
public class RunedexClient
{
	private static final String RUNEDEX_BASE = "http://unmoon.com:5000/";
	private static final String RUNEDEX_AUTH_HEADER = "RUNEDEX-AUTH";
	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static final Gson GSON = new Gson();
	private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
		.pingInterval(30, TimeUnit.SECONDS)
		.build();

	private Set<Runemon> previouslySubmitted;

	protected void submit(Set<Runemon> runedex, String authentication_hash)
	{
		if (runedex == null || runedex.isEmpty() || runedex.equals(previouslySubmitted)) return;

		Set<Runemon> diff = new HashSet<>(runedex);
		if (previouslySubmitted != null) diff.removeAll(previouslySubmitted);

		Request request = new Request.Builder()
			.header(RUNEDEX_AUTH_HEADER, authentication_hash)
			.url(RUNEDEX_BASE + "submit")
			.post(RequestBody.create(JSON, GSON.toJson(diff)))
			.build();

		try (Response response = CLIENT.newCall(request).execute())
		{
			if (response.isSuccessful())
			{
				log.info("Successfully submitted {} new Runemon", diff.size());
				previouslySubmitted = new HashSet<>(runedex);
			}
			else
			{
				log.error("Error submitting Runedex changes: {}", response.toString());
			}
		}
		catch (IOException e)
		{
			log.error("IOException: {}", e.getMessage());
		}
	}

	protected Set<Runemon> fetch(String authentication_hash)
	{
		Request request = new Request.Builder()
			.header(RUNEDEX_AUTH_HEADER, authentication_hash)
			.url(RUNEDEX_BASE + "fetch")
			.build();

		try (Response response = CLIENT.newCall(request).execute())
		{
			if (response.isSuccessful())
			{
				previouslySubmitted = GSON.fromJson(response.body().string(), new TypeToken<HashSet<Runemon>>(){}.getType()); // CHECKSTYLE:OFF
				log.info("Successfully fetched Runedex with {} Runemon", previouslySubmitted.size());
				return new HashSet<>(previouslySubmitted);
			}
			else
			{
				log.error("Error fetching Runedex: {}", response.body().toString());
			}
		}
		catch (IOException e)
		{
			log.error("IOException: {}", e.getMessage());
		}
		return null;
	}
}
