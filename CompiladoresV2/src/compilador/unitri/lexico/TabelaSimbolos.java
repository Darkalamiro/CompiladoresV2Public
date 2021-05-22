package compilador.unitri.lexico;

import java.util.ArrayList;

public class TabelaSimbolos {

    private static ArrayList<Simbolo> simbolos = new ArrayList<Simbolo>();

    public static void addSimbolo(Token token, String escopo){
        Simbolo simboloBuscado = findSimbolo(token.getImagem(), escopo);

        if(simboloBuscado != null){
            token.setIndice(simbolos.indexOf(simboloBuscado));
        }else{
            simboloBuscado = new Simbolo(token.getImagem(), escopo);
            simbolos.add(simboloBuscado);
            token.setIndice(simbolos.indexOf(simboloBuscado));
        }
    }

    private static Simbolo findSimbolo(String imagem, String escopo) {
        for (Simbolo simbolo: simbolos) {
            if(simbolo.getImagem().equals(imagem) && simbolo.getEscopo().equals(escopo)) {
                return simbolo;
            }
        }
        return null;
    }

    public static String getTipo(Token id){
        if(id.getClasse().equals("ID"))
            return simbolos.get(id.getIndice()).getTipo();
        else if (id.getClasse().equals("CLI"))
            return "inteiro";
        else if (id.getClasse().equals("CLR"))
            return "real";
        else if (id.getClasse().equals("CLT"))
            return "texto";

        return null;
    }

    public static void setParam(Token id){
        String escopo = simbolos.get(id.getIndice()).getEscopo();
        findSimbolo(escopo,escopo).getParams().add(id);
    }

    public static void setTipo(Token token, String tipo){
        simbolos.get(token.getIndice()).setTipo(tipo);
    }

    public static void setTipoDef(Token token, String tipo){
        simbolos.get(token.getIndice()).setNatureza("param");
    }

    public static ArrayList<Token> getParamDef(Token token){
        return simbolos.get(token.getIndice()).getParams();
    }

    public static void printTabelaSimbolos(){
        for (Simbolo simbolo :
                simbolos) {
            System.out.println(simbolo);
        }
    }

    public static void mostrarSimbolos() {
        for (Simbolo simbolo :
                simbolos) {
            System.out.println(simbolo);
        }
    }
}
