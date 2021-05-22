package compilador.unitri.lexico;

public class Token {

    private int indice;
    private int linha;
    private int coluna;
    private String imagem;
    private String classe;

    public Token(String imagem, String classe, int indice, int linha, int coluna ) {
        this.indice = indice;
        this.linha = linha;
        this.coluna = coluna;
        this.imagem = imagem;
        this.classe = classe;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    @Override
    public String toString() {
        return this.imagem;
    }
}
