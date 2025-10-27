package softcare.game;

import android.content.Context;

import java.util.Random;

public class GameMessage {

    private static final int WIN_COUNT = 25;
    private static final int LOSE_COUNT = 25;

    public static String getGameMessage(Context context, boolean isWin) {
        Random random = new Random();
        int index = random.nextInt(isWin ? WIN_COUNT : LOSE_COUNT) + 1;
        String key = (isWin ? "win_message" : "lose_message") + index;
        int resId = context.getResources().getIdentifier(key, "string", context.getPackageName());
        return context.getString(resId);
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