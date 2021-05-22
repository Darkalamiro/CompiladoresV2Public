package compilador.unitri.lexico;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorLexico {
    /*
    PR -> Palavra Reservada
    DE -> Delimitador
    OPA -> Operador Aritmético
    OPC -> Operador Comparação
    OA -> Operador Atribuição

    CLI -> Constante Literal Inteira
    CLR -> Constante Literal Real
    CLT -> Constante Literal Texto
     */

    private static final List<Token> tokens = new ArrayList<>();
    private static final List<Erros> erros = new ArrayList<>();

    public enum Simbolos{
        PR, DE, OPA, OPC, OA, CLI, CLR, CLT, ID, $
    }

    public static void analisar(String imagem, int nrLinhas, int coluna) {
        if(PalavrasEstaticas.isPalavraReservada(imagem))
            tokens.add(new Token(imagem, Simbolos.PR.toString(), -1, nrLinhas, coluna));
        else if(PalavrasEstaticas.isDelimitador(imagem))
            tokens.add(new Token(imagem, Simbolos.DE.toString(), -1, nrLinhas, coluna));
        else if(PalavrasEstaticas.isOperadorAritmetico(imagem))
            tokens.add(new Token(imagem, Simbolos.OPA.toString(), -1, nrLinhas, coluna));
        else if(PalavrasEstaticas.isOperadorComparativo(imagem))
            tokens.add(new Token(imagem, Simbolos.OPC.toString(), -1, nrLinhas, coluna));
        else if(PalavrasEstaticas.isOperadorAtribuicao(imagem))
            tokens.add(new Token(imagem, Simbolos.OA.toString(), -1, nrLinhas, coluna));
        else if(imagem.matches("\\d\\d*"))
            tokens.add(new Token(imagem, Simbolos.CLI.toString(), -1, nrLinhas, coluna));
        else if(imagem.matches("\\d\\d*.\\d\\d*"))
            tokens.add(new Token(imagem, Simbolos.CLR.toString(), -1, nrLinhas, coluna));
        else if(imagem.matches("\\p{Alpha}\\p{Alnum}*"))
            tokens.add(new Token(imagem, Simbolos.ID.toString(), -1, nrLinhas, coluna));
        else erros.add(new Erros(imagem, nrLinhas, coluna));
    }

    public static void adicionaConstanteLiteralTexto(String imagem, int linha, int coluna) {
        tokens.add(new Token(imagem, Simbolos.CLT.toString(), -1, linha, coluna));
    }

    public static void finalCadeia() {
        tokens.add(new Token("$", Simbolos.$.toString(), -1, -1, -1));
    }

    public static void printTokens(){
        System.out.println("Tokens: ");
        for (Token token :
                tokens) {
            System.out.println(token +" - "+ token.getClasse() +"| Linha: "+token.getLinha() +"| Coluna: "+token.getColuna());
        }
    }

    public static void printErros(){
        System.out.println("ERROS: ");
        for (Erros erro :
                erros) {
            System.out.println(erros);
        }
    }

    public static ArrayList<Erros> getErros() {
        return (ArrayList<Erros>) erros;
    }

    public static ArrayList<Token> getTokens() {
        return (ArrayList<Token>) tokens;
    }
}
