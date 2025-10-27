package softcare.game.model;

import android.content.Context;

import java.util.Random;

import softcare.game.R;

public class GameMessage {

    private static final int[] WIN_MESSAGES = {
            R.string.win_message1,
            R.string.win_message2,
            R.string.win_message3,
            R.string.win_message4,
            R.string.win_message5,
            R.string.win_message6,
            R.string.win_message7,
            R.string.win_message8,
            R.string.win_message9,
            R.string.win_message10,
            R.string.win_message11,
            R.string.win_message12,
            R.string.win_message13,
            R.string.win_message14,
            R.string.win_message15,
            R.string.win_message16,
            R.string.win_message17,
            R.string.win_message18,
            R.string.win_message19,
            R.string.win_message20,
            R.string.win_message21,
            R.string.win_message22,
            R.string.win_message23,
            R.string.win_message24,
            R.string.win_message25
    };

    private static final int[] LOSE_MESSAGES = {
            R.string.lose_message1,
            R.string.lose_message2,
            R.string.lose_message3,
            R.string.lose_message4,
            R.string.lose_message5,
            R.string.lose_message6,
            R.string.lose_message7,
            R.string.lose_message8,
            R.string.lose_message9,
            R.string.lose_message10,
            R.string.lose_message11,
            R.string.lose_message12,
            R.string.lose_message13,
            R.string.lose_message14,
            R.string.lose_message15,
            R.string.lose_message16,
            R.string.lose_message17,
            R.string.lose_message18,
            R.string.lose_message19,
            R.string.lose_message20,
            R.string.lose_message21,
            R.string.lose_message22,
            R.string.lose_message23,
            R.string.lose_message24,
            R.string.lose_message25
    };

    public static String getGameMessage(Context context, boolean isWin) {
        Random random = new Random();
        int[] messages = isWin ? WIN_MESSAGES : LOSE_MESSAGES;
        int index = random.nextInt(messages.length);
        return context.getString(messages[index]);
    }


    public static String buildResultMessage(Context context, boolean isWin,
                                            double expectedDistance, double playerDistance, String path) {

        String baseMessage = getGameMessage(context, isWin);
        double difference = playerDistance - expectedDistance;
        StringBuilder sb = new StringBuilder(baseMessage).append("\n\n");

        // use formatted strings from resources
        sb.append(context.getString(R.string.your_total_distance, playerDistance)).append("\n");

        if (isWin) {
            sb.append(context.getString(R.string.optimal_distance, expectedDistance)).append("\n");
        } else {
            sb.append(context.getString(R.string.expected_best, expectedDistance)).append("\n");
            sb.append(context.getString(R.string.went_longer, difference)).append("\n");
        }

        sb.append(context.getString(R.string.path_followed, path));
        return sb.toString();
    }
}