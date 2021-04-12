import java.util.Random;

public class RandomNumber {
    public static int randomInt(int min, int max){
        boolean inRange = false;
        int randomNumber = 0;
        do{
            int range = max - min;
            randomNumber = min + (int) Math.rint(Math.random() * range);
            if (randomNumber >= min && randomNumber <= max){
                inRange = true;
            }
        } while(!inRange);

        return randomNumber;
    }
}
