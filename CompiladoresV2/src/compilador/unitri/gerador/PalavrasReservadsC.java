package compilador.unitri.gerador;

import java.util.Hashtable;

public class PalavrasReservadsC {
    static Hashtable<String, String> hash = new Hashtable<>();

    static {
        hash.put("inteiro", "int");
        hash.put("texto", "char[250]");
        hash.put("real", "double");
        hash.put("escreve", "printf");
        hash.put("leia", "scanf");
        hash.put("se", "if");
        hash.put("senao", "else");
        hash.put("enquanto", "while");
        hash.put("retorna", "return");
    }

    public static String tipo(String value){
        return hash.get(value);
    }
}
