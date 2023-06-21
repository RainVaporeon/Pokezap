package com.spiritlight.pokezap;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.*;

public class Main {

    public static net.dv8tion.jda.api.JDA jda;

    private static final List<String> names = new LinkedList<>();

    public static void main(String[] args) throws LoginException {
        if(args.length == 0) {
            System.out.println("Please enter a bot token as the argument!");
            System.exit(1);
            return;
        }
        String token = args[0];

        Set<String> set = new HashSet<>(Collector.fetchNames());
        names.addAll(set);

        jda = net.dv8tion.jda.api.JDABuilder.createDefault(token)
                .setEnabledIntents(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES)
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE, CacheFlag.ACTIVITY, CacheFlag.MEMBER_OVERRIDES)
                .setAutoReconnect(true)
                .build();

        jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReceived(@NotNull MessageReceivedEvent event) {
                if(!event.getMessage().getContentRaw().toLowerCase(Locale.ROOT).startsWith("pokezap")) return;
                event.getMessage().reply(
                        "Zap! You've been turned into a " + randomName()
                ).queue();
            }
        });
    }

    private static final Random random = new Random();

    private static String randomName() {
        int idx = random.nextInt(names.size());

        return names.get(idx);
    }
}