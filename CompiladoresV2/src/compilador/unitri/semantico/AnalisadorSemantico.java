package compilador.unitri.semantico;

import compilador.unitri.lexico.TabelaSimbolos;
import compilador.unitri.lexico.Token;
import compilador.unitri.sintatico.No;

import java.util.ArrayList;

public class AnalisadorSemantico {

    private static No raiz;
    private static ArrayList<String> erros = new ArrayList<>();
    private static Token defAtual;

    public AnalisadorSemantico(No raiz) {
        this.raiz = raiz;
    }

    public static No getRaiz() {
        return raiz;
    }

    public static void setRaiz(No raiz) {
        AnalisadorSemantico.raiz = raiz;
    }

    public static ArrayList<String> getErros() {
        return erros;
    }

    public void setErros(ArrayList<String> erros) {
        this.erros = erros;
    }

    public static Token getDefAtual() {
        return defAtual;
    }

    public static void setDefAtual(Token defAtual) {
        AnalisadorSemantico.defAtual = defAtual;
    }

    public static void mostraErros(){
        for(String erro: erros){
            System.out.println(erro);
        }
    }

    public void analisar(){
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
    //<ListArg2> ::= ','<Operando><ListArg2> |
    private Object ListArg2(No no) {
        return null;
    }

    //<ListArg> ::= <Operando><ListArg2> |
    private Object ListArg(No no) {
        return null;
    }

    //<Chamada> ::= 'id''('<ListArg>')'';'
    private Object Chamada(No no) {
        Token id = no.getFilho(0).getToken();
        ArrayList<Token> params = TabelaSimbolos.getParamDef(id);
        ArrayList<Token> listArgs = (ArrayList<Token>) analisar(no.getFilho(2));

        if (params.size() == listArgs.size()){
            for (int i = 0; i <  listArgs.size(); i++){
                if (!TabelaSimbolos.getTipo(params.get(i)).equals(listArgs.get(i))){
                    erros.add("Os tipos dos argumentos da chamada da função são incompatíveis com os declarados");
                }
            }
        }else{
            erros.add("Quantidade de argumentos incompatível com a declaração da função");
        }
        return null;
    }

    //<Saida> ::= 'escreva''('<Operando>')'';'
    private Object Saida(No no) {
        return null;
    }

    //<Entrada> ::= 'leia''(' id ')'';'
    private Object Entrada(No no) {
        return null;
    }

    //<Ret> ::= 'retorna' <Fator> ';'
    private Object Ret(No no) {
        String tipoDef = TabelaSimbolos.getTipo(defAtual);
        ArrayList<Token> fator = (ArrayList<Token>) analisar(no.getFilho(1));

        for (Token token :
                fator) {
            if (TabelaSimbolos.getTipo(token) == null) {
                erros.add("Variável retornada não declarada");
            } else if (!TabelaSimbolos.getTipo(token).equals(tipoDef)) {
                erros.add("O retorno da função possui um tipo incompatível com o declarado");
            }

        }

        return null;
    }
    //<Senao> ::= 'senao' <Coman> |
    private Object Senao(No no) {
        if(!no.getFilhos().isEmpty()){
            analisar(no.getFilho(1));
        }
        return null;
    }

    //          0   1     2      3     4       5
    //<Se> ::= 'se''('<ExpreRel>')' <Coman> <Senao>
    private Object Se(No no) {
        analisar(no.getFilho(2));
        analisar(no.getFilho(4));
        analisar(no.getFilho(5));
        return null;

    }

    //                  0        1      2
    //<ExpreRel> ::= <ExprArit><Op3><ExprArit>
    private Object ExpRel(No no) {
        ArrayList<Token> expAritE = (ArrayList<Token>) analisar(no.getFilho(0));
        ArrayList<Token> expAritD = (ArrayList<Token>) analisar(no.getFilho(2));
        
        String tipoE = TabelaSimbolos.getTipo(expAritE.get(0));
        String tipoD = TabelaSimbolos.getTipo(expAritD.get(0));
        
        if(tipoE == null){
            erros.add("Variável do operando do lado esquerdo da expressão relacional não declardo!");
        }else{
            for (Token token :
                    expAritE) {
                if (TabelaSimbolos.getTipo(token) == null){
                    erros.add("A variável não foi declarada: "+token.getImagem());
                }else if (!TabelaSimbolos.getTipo(token).equals(tipoE)){
                    erros.add("Operando com tipo incompatível do lado esquerdo da expressão relacional");
                }
            }
        }

        if(tipoD == null){
            erros.add("Variável do operando do lado direto da expressão relacional não declardo!");
        }else{
            for (Token token :
                    expAritD) {
                if (TabelaSimbolos.getTipo(token) == null){
                    erros.add("A variável não foi declarada: "+token.getImagem());
                }else if (!TabelaSimbolos.getTipo(token).equals(tipoD)){
                    erros.add("Operando com tipo incompatível do lado direito da expressão relacional");
                }
            }
        }
        
        if(tipoE != null && tipoD != null && !tipoE.equals(tipoD)){
            erros.add("Tipo de variáveis incompatíveis na expressão relacional");
        }
        return null;
    }

    //               0       1      2       3     4
    //<Laco> ::= 'enquanto' '(' <ExpreRel> ')' <Coman>
    private Object Laco(No no) {
        analisar(no.getFilho(2));
        analisar(no.getFilho(4));
        return null;
    }

    private Object Op3(No no) {
        return null;
    }

    private Object Op2(No no) {
        return null;
    }

    private Object Op1(No no) {
        return null;
    }

    /*
    <Operando> ::= id
            | cli
            | clr
            | clt
    */
    private Object Operando(No no) {
        return no.getFilho(0).getToken();
    }

    //               0            0        0     1     0
    //<Fator> ::= <Operando> | <Chamada> | '('<ExprArit>')'
    private Object Fator(No no) {
        if(no.getFilhos().size() == 1){
            Token token = (Token) analisar(no.getFilho(0));
            ArrayList<Token> fator = new ArrayList<>();
            fator.add(token);
            return fator;
        }else{
            return analisar(no.getFilho(1));
        }
    }

    //<Termo2> ::= <Op2><Termo> |
    private Object Termo2(No no) {
        if(no.getFilhos().isEmpty()){
            return new ArrayList<Token>();
        }else{
            return analisar(no.getFilho(1));
        }
    }

    //               0      1
    //<Termo> ::= <Fator><Termo2>
    private Object Termo(No no) {
        ArrayList<Token> fator = (ArrayList<Token>) analisar(no.getFilho(0));
        ArrayList<Token> termo2 = (ArrayList<Token>) analisar(no.getFilho(1));
        fator.addAll(termo2);
        return fator;
    }

    //                  0      1
    //<ExprArit2> ::= <Op1><ExprArit> |
    private Object ExprArit2(No no) {
        if(no.getFilhos().isEmpty()){
            return new ArrayList<Token>();
        }else {
            ArrayList<Token> exprArit = (ArrayList<Token>) analisar(no.getFilho(1));
            return exprArit;
        }
    }

    //                  0        1
    //<ExprArit> ::= <Termo><ExprArit2>
    private Object ExpreArit(No no) {
        ArrayList<Token> termo = (ArrayList<Token>) analisar(no.getFilho(0));
        ArrayList<Token> exprArit2 = (ArrayList<Token>) analisar(no.getFilho(1));
        termo.addAll(exprArit2);
        return termo;
    }

    //            0   1      2       3
    //<Atrib> ::= id '=' <ExprArit> ';'
    // var1 = inteiro;
    // var1 = 1 {string, decimal, nada}
    private Object Atrib(No no) {
        Token id = no.getFilho(0).getToken();
        String tipo = TabelaSimbolos.getTipo(id);

        if (tipo != null){
            ArrayList<Token> exprArit = (ArrayList<Token>) analisar(no.getFilho(2));

            for (Token operando :
                    exprArit) {
                String tipoOperando = TabelaSimbolos.getTipo(operando);
                if (tipoOperando == null){
                    erros.add("Variável do lado direito não declarado!");
                }else if (!tipoOperando.equals(tipo)){
                    erros.add("Variável do lado direito incompatível com do tipo declarado " + operando);
                }
            }
        }else{
            erros.add("Variável não declarada!");
        }

        return null;
    }

    //               0   1     2
    //<ListId2> ::= ',' id <ListId2> |
    private Object ListId2(No no) {
        if (no.getFilhos().isEmpty()){
            return new ArrayList<Token>();
        }else{
            Token id = no.getFilho(1).getToken();
            ArrayList<Token> listId2 = (ArrayList<Token>) analisar(no.getFilho(2));
            listId2.add(0, id);
            return listId2;
        }
    }

    //             0      1
    //<ListId> ::= id <ListId2>
    private Object ListId(No no) {
        Token id = no.getFilho(0).getToken();
        ArrayList<Token> listId2 = (ArrayList<Token>) analisar(no.getFilho(1));
        listId2.add(0,id);
        return listId2;
    }

    //               0    1   2     3
    //<Decl> ::= <ListId>':'<Tipo> ';'
    private Object Decl(No no) {
        ArrayList<Token> listId = (ArrayList<Token>) analisar(no.getFilho(0));
        String tipo = (String) analisar(no.getFilho(2));

        for (Token id :
                listId) {
            if (TabelaSimbolos.getTipo(id) != null){
                erros.add("A variável "+ id.getImagem() +" já foi declarada!");
            }else{
                TabelaSimbolos.setTipo(id, tipo);
            }
        }
        return null;
    }


    /*            0
    <Coman> ::= <Decl>
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
            analisar(no.getFilho(1));
        }else{
            analisar(no.getFilho(0));
        }
        return null;   
    }
    //DEBUG AQUI!
    //                      0       1
    //<ListComan> ::=  | <Coman><ListComan>
    private Object ListComan(No no) {
        if (!no.getFilhos().isEmpty()){
            analisar(no.getFilho(0));
            analisar(no.getFilho(1));
        }

        return null;
    }

    /*            0
    <Tipo> ::= 'inteiro'
                | 'real'
                | 'texto'
                | 'nada'
     */
    private Object Tipo(No no) {
        return no.getFilho(0).getToken().getImagem();
    }

    //            0  1   2
    //<Param> ::= id':'<Tipo>
    private Object Param(No no) {
        Token token = no.getFilho(0).getToken();
        String tipo = (String) analisar(no.getFilho(2));
        String tipoId = TabelaSimbolos.getTipo(token);

        if(tipoId == null){
            TabelaSimbolos.setTipo(token, tipo);
            TabelaSimbolos.setParam(token);
        }else{
            erros.add("A variável de parametro já foi declarada! ID: " + token);
        }

        return null;
    }

    //                  0     1        2
    //<ListParam2> ::= ',' <Param><ListParam2> |
    private Object ListParam2(No no) {
        if(!no.getFilhos().isEmpty()){
            analisar(no.getFilho(1));
            analisar(no.getFilho(2));
        }
        return null;
    }

    //                   0        1
    //<ListParam> ::= <Param><ListParam2> |
    private Object ListParam(No no) {
        if(!no.getFilhos().isEmpty()){
            analisar(no.getFilho(0));
            analisar(no.getFilho(1));
        }
        return null;
    }

    //           0    1   2      3       4   5     6     7      8        9
    //<Def> ::= 'def' id '(' <ListParam> ')' ':' <Tipo> '{' <ListComan> '}'
    private Object Def(No no) {
        Token id = no.getFilho(1).getToken();
        String tipo = (String) analisar(no.getFilho(6));
        //TabelaSimbolos.setTipo(id, tipo);
        defAtual = id;

        if (TabelaSimbolos.getTipo(id) != null){
            erros.add("Essa Função já foi declarada: " + id);
        }else{
            TabelaSimbolos.setTipo(id, tipo);
            analisar(no.getFilho(3));
            analisar(no.getFilho(8));
        }
        return null;
    }

    //<ListDef> ::= <Def><ListDef> |
    private Object ListDef(No no) {
        if(!no.getFilhos().isEmpty()){
           analisar(no.getFilho(0));
           analisar(no.getFilho(1));
        }
        return null;
    }

}
