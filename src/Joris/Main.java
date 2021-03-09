package Joris;

import java.util.List;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        NAO nao = new NAO();
        nao.verbind("localhost", 9559);

        List <String> woordenlijst = new ArrayList<>();
        woordenlijst.add("Paard");
        woordenlijst.add("Koe");
        woordenlijst.add("melk");
        woordenlijst.add("pizza");
        woordenlijst.add("vogel");

        nao.luisteren(woordenlijst);

    }
}
