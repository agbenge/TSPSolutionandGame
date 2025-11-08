package softcare.activities;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import softcare.activities.model.Game;
import softcare.activities.model.Tsp;
import softcare.gui.PointXY;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
            Random r= new Random();
            Game game=new Game( );
            List<PointXY> p=  new ArrayList<>();
            List<String> n=  new ArrayList<>();
            for (int i = 0; i<game.getNodes() ;i++){
                int x= 1;
                int y=1;
                PointXY px=new PointXY(x,y);
                if(p.stream().anyMatch(pointXY -> {
                    return (pointXY.x == px.x) && (pointXY.y == px.y);
                })){
                     x= r.nextInt(game.getBound());
                     y= r.nextInt(game.getBound());
                }
                p.add(px);
             //   Log.d(CodeX.tag, i+"---->  x "+x+"  y "+y);
                System.out.println(i+"---->  x "+x+"  y "+y);

            }

    }
    @Test
    public void testList() {
        int  a[] ={6,9,0,21};
    }
    @Test
    public void testGsonTime() {
        long s1= System.currentTimeMillis();
        Gson gson = new Gson();
        System.out.println("----> Init   Gson "+(System.currentTimeMillis()-s1));
        s1= System.currentTimeMillis();
        Tsp tsp= new Tsp();
        Game game = new Game();

        System.out.println("----> Init  Game and TSP "+(System.currentTimeMillis()-s1));
        s1= System.currentTimeMillis();
        String jsonTsp = gson.toJson(tsp);
        String jsonGame = gson.toJson(game);

        System.out.println("----> Class to String  x "+(System.currentTimeMillis()-s1));
        s1= System.currentTimeMillis();
        Tsp tspStored = gson.fromJson(jsonTsp, Tsp.class);
        Game gameStored = gson.fromJson(jsonGame, Game.class);

        System.out.println("----> String to Class x "+(System.currentTimeMillis()-s1));
        s1= System.currentTimeMillis();
    }
}