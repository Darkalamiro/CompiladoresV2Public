package compilador.unitri.lexico;

import java.util.Arrays;
import java.util.List;

public class PalavrasEstaticas {

    private static final List<String> pr = initPalavraReservada();

    private static List<String> initPalavraReservada() {
        return Arrays.asList("def", "inteiro", "real", "texto", "nada", "enquanto", "se", "senao", "leia", "escreva", "retorna");
    }

    private static final List<String> de = initDelimitador();

    private static List<String> initDelimitador() {
        return Arrays.asList("(", ")", "{", "}", ":", ";", ",", "[", "]");
    }

    private static final List<String> opa = initOperadorAritmetico();

    private static List<String> initOperadorAritmetico() {
        return Arrays.asList("+", "-", "/", "*");
    }

    private static final List<String> opc = initOperadorComparativo();

    private static List<String> initOperadorComparativo() {
        return Arrays.asList("<", "<=", ">", ">=", "!=", "==");
    }

    private static final List<String> oa = initOperadorAtribuicao();

    private static List<String> initOperadorAtribuicao() {
        return Arrays.asList("=", "+=", "-=", "*=", "/=");
    }

    public static boolean isPalavraReservada(String imagem){
        return pr.contains(imagem);
    }

    public static boolean isDelimitador(String imagem){
        return de.contains(imagem);
    }

    public static boolean isOperadorAritmetico(String imagem){
        return opa.contains(imagem);
    }

    public static boolean isOperadorComparativo(String imagem){
        return opc.contains(imagem);
    }

    public static boolean isOperadorAtribuicao(String imagem){
        return oa.contains(imagem);
    }
}
