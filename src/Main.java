import java.util.List;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();
        nao.connect("Padme.local", 9559);
        
        nao.say("Hello my friend");
        
        ArrayList<String> rightasnwers = new ArrayList<>();
        rightasnwers.add("all");
        rightasnwers.add("everyone");
        ArrayList<String> falseanswers = new ArrayList<>();
        falseanswers.add("February");
        falseanswers.add("march");
        falseanswers.add("december");
        nao.say("hello how are you?");

        nao.listen(rightasnwers,falseanswers);



        Thread.sleep(5000);

       // nao.scan();
        



    }
}
