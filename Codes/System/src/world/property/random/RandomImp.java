package world.property.random;

import java.io.Serializable;
import java.util.Random;

import world.property.type.range.sub.FloatRange;
import world.property.type.range.sub.IntRange;


public class RandomImp implements RandomValue, Serializable {
    private Random random;
    final private int MAX_STRING_LENGTH = 50;
    public RandomImp() {
        random = new Random();
    }

    @Override
    public int getRandomDecimalValue(IntRange range) {

        return random.nextInt( (int)range.getTo()- (int)range.getFrom() + 1) + (int)range.getFrom();
    }

    @Override
    public float getRandomFloatValue(FloatRange range) {
        float randomValue = random.nextFloat();
        return (float)range.getFrom()+ randomValue * ((float)range.getTo() - (float)range.getFrom());
    }

    @Override
    public String getRandomStringValue() {

        String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?,_-(). ";
        int stringLength = random.nextInt(MAX_STRING_LENGTH) + 1;

        StringBuilder resultString = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            char randomChar = allowedCharacters.charAt(randomIndex);
            resultString.append(randomChar);
        }

        return resultString.toString();
    }

    @Override
    public boolean getRandomBooleanValue() {
        return random.nextBoolean();
    }
}
