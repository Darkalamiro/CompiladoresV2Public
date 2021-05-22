package compilador.unitri.gerador;

import compilador.unitri.lexico.TabelaSimbolos;
import compilador.unitri.lexico.Token;
import compilador.unitri.sintatico.No;

import java.io.PrintWriter;
import java.util.ArrayList;

public class GeradorCodigo {

    private static No raiz;
    private static PrintWriter out;
    private static int identa = 0;

    public GeradorCodigo(No raiz, PrintWriter out) {
        this.raiz = raiz;
        this.out = out;
    }

    public void gerar(){
        gerarCabecalho();
        analisar(raiz);
    }


    public Object analisar(No no){
        switch (no.getTipo()){
            case NO_LISTDEF: return ListDef(no);
            case NO_DEF: return Def(no);
            case NO_LISTPARAM: return ListParam(no);
            case NO_LISTPARAM2: return ListParam2(no);
            case NO_PARAM: return Param(no);
            case NO_TIPO: return Tipo(no);
            case NO_LISTCOMAN: return ListComan(no);
            case NO_COMAN: return Coman(no);
            case NO_DECL: return Decl(no);
            case NO_LISTID: return ListId(no);
            case NO_LISTID2: return ListId2(no);
            case NO_ATRIB: return Atrib(no);
            case NO_EXPREARIT: return ExpreArit(no);
            case NO_EXPRARIT2: return ExprArit2(no);
            case NO_TERMO: return Termo(no);
            case NO_TERMO2: return Termo2(no);
            case NO_FATOR: return Fator(no);
            case NO_OPERANDO: return Operando(no);
            case NO_OP1: return Op1(no);
            case NO_OP2: return Op2(no);
            case NO_OP3: return Op3(no);
            case NO_LACO: return Laco(no);
            case NO_EXPREL: return ExpRel(no);
            case NO_SE: return Se(no);
            case NO_SENAO: return Senao(no);
            case NO_RET: return Ret(no);
            case NO_ENTRADA: return Entrada(no);
            case NO_SAIDA: return Saida(no);
            case NO_CHAMADA: return Chamada(no);
            case NO_LISTARG: return ListArg(no);
            case NO_LISTARG2: return ListArg2(no);
        }
        return null;
    }

    //x<Fator> ::= <Operando> | <Chamada> | '('<ExprArit>')'
    private Object Fator(No no) {
        if(no.getFilhos().size() == 1){
            Token token = (Token) analisar(no.getFilho(0));
            ArrayList<Token> fator = new ArrayList<>();
            fator.add(token);
            return fator;
        }else{
            out.print(" "+no.getFilho(0).getToken().getImagem());
            ArrayList<Token> tokens = (ArrayList<Token>) analisar(no.getFilho(1));
            out.print(no.getFilho(2).getToken().getImagem());
            return tokens;
        }
    }

    //x<Termo2> ::= <Op2><Termo> |
    private Object Termo2(No no) {
        if(no.getFilhos().isEmpty()){
            return new ArrayList<Token>();
        }else{
            out.print(no.getFilho(0).getFilho(0).getToken().getImagem());
            return analisar(no.getFilho(1));
        }
    }

    //x<Termo> ::= <Fator><Termo2>
    private Object Termo(No no) {
        ArrayList<Token> fator = (ArrayList<Token>) analisar(no.getFilho(0));
        ArrayList<Token> termo2 = (ArrayList<Token>) analisar(no.getFilho(1));

        fator.addAll(termo2);
        return fator;
    }

    //x<ExprArit2> ::= <Op1><ExprArit> |
    private Object ExprArit2(No no) {
        if(no.getFilhos().isEmpty()){
            return new ArrayList<Token>();
        }else{
            out.print(no.getFilho(0).getFilho(0).getToken().getImagem());
            ArrayList<Token> exprArit = (ArrayList<Token>) analisar(no.getFilho(1));
            return exprArit;
        }
    }

    //x<ExprArit> ::= <Termo><ExprArit2>
    private Object ExpreArit(No no) {
        ArrayList<Token> termo = (ArrayList<Token>) analisar(no.getFilho(0));
        ArrayList<Token> expArit2 = (ArrayList<Token>) analisar(no.getFilho(1));
        termo.addAll(expArit2);
        return termo;
    }

    //x<Atrib> ::= id '=' <ExprArit> ';'
    private Object Atrib(No no) {
        out.print("\n");
        formata();
        Token id = no.getFilho(0).getToken();
        out.print("  "+ id + " "+no.getFilho(1));
        String tipo = TabelaSimbolos.getTipo(id);
        analisar(no.getFilho(2));
        out.print(no.getFilho(3).getToken().getImagem()+ "\n");

        return null;
    }

    //x<ListId2> ::= ',' id <ListId2> |
    private Object ListId2(No no) {
        if(no.getFilhos().isEmpty()){
            return new ArrayList<Token>();
        }else{
            Token id = no.getFilho(1).getToken();
            ArrayList<Token>listId2 = (ArrayList<Token>) analisar(no.getFilho(2));
            listId2.add(0, id);
            return listId2;
        }
    }

    //x<ListId> ::= id <ListId2>
    private Object ListId(No no) {
        Token id = no.getFilho(0).getToken();
        ArrayList<Token>listId2 = (ArrayList<Token>) analisar(no.getFilho(1));
        listId2.add(0, id);
        return listId2;
    }

    //x<Decl> ::= <ListId>':'<Tipo> ';'
    private Object Decl(No no) {
        ArrayList<Token> listId = (ArrayList<Token>) analisar(no.getFilho(0));
        String tipo = (String) analisar(no.getFilho(2));

        out.print("\n\t"+ tipo + " "+listId.get(0));
        for (int i = 1; i<listId.size(); i++){
            out.print(", "+listId.get(i));
        }

        out.print(no.getFilho(3).getToken().getImagem()+ "\n");

        return null;
    }

    /*
    x<Coman> ::= <Decl>
            | <Atrib>
            | <Laco>
            | <Se>
            | <Ret>
            | <Entrada>
            | <Saida>
            | <Chamada>';'
            | '{'<ListComan>'}'
     */
    private Object Coman(No no) {
        if(no.getFilhos().size() > 1){
            out.print(no.getFilho(0).getToken().getImagem()+"\n");
            analisar(no.getFilho(1));
            formata();
            out.print(no.getFilho(2).getToken().getImagem()+"\n");
        }else{
            analisar(no.getFilho(0));
        }
        return null;
    }

    //x<ListComan> ::=  | <Coman><ListComan>
    private Object ListComan(No no) {
        if(!no.getFilhos().isEmpty()){
            formata();
            analisar(no.getFilho(0));
            analisar(no.getFilho(1));
        }
        return null;
    }

    private void formata() {
        for (int i = 0; i<identa; i++){
            out.print("\t");
        }
    }

    //x<Tipo> ::= 'inteiro'
    //        | 'real'
    //        | 'texto'
    //        | 'nada'
    private Object Tipo(No no) {
        return PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem());
    }

    //x<Param> ::= id':'<Tipo>
    private Object Param(No no) {
        Token token = no.getFilho(0).getToken();
        String tipo = (String) analisar(no.getFilho(2));
        return null;
    }

    //x<ListParam2> ::= ',' <Param><ListParam2> |
    private Object ListParam2(No no) {
        if(!no.getFilhos().isEmpty()){
            analisar(no.getFilho(1));
            analisar(no.getFilho(2));
        }
        return null;
    }

    //x<ListParam> ::= <Param><ListParam2> |
    private Object ListParam(No no) {
        if(!no.getFilhos().isEmpty()){
            analisar(no.getFilho(0));
            analisar(no.getFilho(1));
        }
        return null;
    }

    // int soma(int, int){....}
    //<Def> ::= 'def' id '(' <ListParam> ')' ':' <Tipo> '{' <ListComan> '}'
    private Object Def(No no) {
        String tipo = (String) analisar(no.getFilho(6));
        Token id = no.getFilho(1).getToken();
        out.print(tipo+ " " + no.getFilho(1) + " "+no.getFilho(2));
        analisar(no.getFilho(3));
        ArrayList<Token> params = TabelaSimbolos.getParamDef(id);

        if(!params.isEmpty()){
            out.print(PalavrasReservadsC.tipo(TabelaSimbolos.getTipo(params.get(0))) + " "+ params.get(0).getImagem());
        }
        for (int i = 1; i < params.size(); i++){
            String tipo1 = PalavrasReservadsC.tipo(TabelaSimbolos.getTipo(params.get(i)));
            out.print(", "+ tipo1 + " " + params.get(i).getClasse());
        }

        out.print(no.getFilho(4)+ " " );
        out.print(no.getFilho(7) + "\n\n");

        TabelaSimbolos.setTipoDef(id, tipo);
        TabelaSimbolos.setTipo(id, tipo);

        analisar(no.getFilho(8));

        out.print("\n" + no.getFilho(9)+ "\n\n") ;
        return  null;

    }

    //<ListDef> ::= <Def><ListDef> |
    private Object ListDef(No no) {
        if(!no.getFilhos().isEmpty()){
            analisar(no.getFilho(0));
            analisar(no.getFilho(1));
        }
        return null;
    }


    private void gerarCabecalho() {
        out.println("#include<stdio.h>");
        out.println("#include<stdlib.h>");
    }


}
