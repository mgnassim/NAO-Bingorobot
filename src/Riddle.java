import java.util.List;

public class Riddle {
    public NAO nao = new NAO();

    public void sayRiddle(String riddle, List<String> correctAwnsers, List<String> falseAwnsers) throws Exception {
        nao.say(riddle);
        nao.listen(correctAwnsers,falseAwnsers);

    }
}
