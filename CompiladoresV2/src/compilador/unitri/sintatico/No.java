package compilador.unitri.sintatico;

import compilador.unitri.lexico.Token;

import java.util.ArrayList;
import java.util.List;

public class No {

    private No pai;
    private List<No> filhos = new ArrayList<>();
    private TipoNo tipo;
    private Token token;

    public No(Token token){
        this.token = token;
        this.tipo = TipoNo.NO_TOKEN;
    }

    public No(TipoNo tipo){
        this.tipo = tipo;
    }

    public void addFilho(No filho){
        if (filho != null){
            filhos.add(filho);
            filho.setPai(this);
        }
    }

    public void setPai(No pai) {
        this.pai = pai;
    }

    public No getPai() {
        return pai;
    }

    public No getFilho(int pos){
        return filhos.get(pos);
    }

    public List<No> getFilhos(){
        return filhos;
    }

    public void setFilhos(List<No> filhos){
        this.filhos = filhos;
    }

    public TipoNo getTipo(){
        return this.tipo;
    }

    public void setTipo(TipoNo tipo) {
        this.tipo = tipo;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        if (tipo == TipoNo.NO_TOKEN){
            return String.valueOf(token);
        }else{
            return tipo.toString();
        }
    }
}
