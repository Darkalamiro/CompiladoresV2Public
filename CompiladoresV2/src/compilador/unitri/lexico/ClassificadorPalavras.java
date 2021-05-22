package compilador.unitri.lexico;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ClassificadorPalavras {

    private static int variavelAnterior;
    /*
    def minha_funcao(var:texto)
    def minha_funcao ( var : texto )
    def  definicao ( )
    "essa é um texto é uma String"
     */
    public ArrayList<Token> classifica(BufferedReader texto){
        String linha = null;

        try {
            int nrLinhas = 1;
            while ((linha = texto.readLine()) != null){

                linha = linha.replaceAll("("
                        + "^\\p{Alpha}def^\\p{Alpha}|^\\p{Alpha}inteiro^\\p{Alpha}|^\\p{Alpha}real^\\p{Alpha}|"
                        + "^\\p{Alpha}texto^\\p{Alpha}|^\\p{Alpha}nada^\\p{Alpha}|^\\p{Alpha}senao^\\p{Alpha}|"
                        + "^\\p{Alpha}se^\\p{Alpha}|^\\p{Alpha}enquanto^\\p{Alpha}|^\\p{Alpha}retorna^\\p{Alpha}|"
                        + "^\\p{Alpha}leia^\\p{Alpha}|^\\p{Alpha}escreva^\\p{Alpha}|"
                        + "\\(|\\)|\\{|\\}|\\,|\\;|\\:|"
                        + "\\+|\\-|\\*|\\/|\\%|"
                        + "\\<=|\\>=|\\==|\\!=|\\<|\\>|"
                        + "\\+=|\\-=|\\*=|\\/=|\\\"|\\="
                        + ")", " $1 ");
                linha = linha.replace("  ", " ");

                String aux = linha;
                variavelAnterior = 0;
                StringTokenizer st = new StringTokenizer(linha);
                int coluna = 0;
                while (st.hasMoreTokens()){
                    String imagem = st.nextToken();
                    if(imagem.equals("#")){ //simbolo de comentário
                        break;
                    }
                    if (!imagem.startsWith("\"")){ // analisa uma não string
                        coluna = posicaoColuna(linha, imagem) + 1 - imagem.length();
                        linha = linha.substring(linha.indexOf(imagem) + imagem.length());
                        AnalisadorLexico.analisar(imagem, nrLinhas, coluna);
                    }else{ // analisa a string completa
                        //"essa é um texto é uma String"
                        coluna = posicaoColuna(linha, imagem) + 1 - imagem.length();
                        linha = linha.substring(linha.indexOf(imagem) + imagem.length());
                        if(!imagem.endsWith("\"") || imagem.length() == 1){
                            int init = variavelAnterior - imagem.length() - 1;
                            imagem = st.nextToken();
                            coluna = posicaoColuna(linha, imagem) + 1 - imagem.length();
                            linha = linha.substring(linha.indexOf(imagem)+imagem.length());

                            while(!imagem.endsWith("\"")){
                                imagem = st.nextToken();
                                coluna = posicaoColuna(linha, imagem) + 1 - imagem.length();
                                linha = linha.substring(linha.indexOf(imagem) + imagem.length());
                            }
                            AnalisadorLexico.adicionaConstanteLiteralTexto(aux.substring(aux.indexOf("\"") + 1,variavelAnterior-1), nrLinhas, coluna);
                        }else{
                            AnalisadorLexico.adicionaConstanteLiteralTexto(aux.substring(aux.indexOf("\"") + 1,variavelAnterior-1), nrLinhas, coluna);
                        }

                    }
                }
                nrLinhas++;
            }
            AnalisadorLexico.finalCadeia();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int posicaoColuna(String linha, String imagem) {
        variavelAnterior += linha.indexOf(imagem) + imagem.length();
        return variavelAnterior;
    }
}
