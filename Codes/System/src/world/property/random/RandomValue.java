package world.property.random;

import world.property.type.range.sub.FloatRange;
import world.property.type.range.sub.IntRange;

public interface RandomValue {
    int getRandomDecimalValue(IntRange range);
    float getRandomFloatValue(FloatRange range);
    String getRandomStringValue();
    boolean getRandomBooleanValue();
}
