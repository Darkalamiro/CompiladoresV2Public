import compilador.unitri.lexico.AnalisadorLexico;
import compilador.unitri.lexico.ClassificadorPalavras;
import compilador.unitri.lexico.TabelaSimbolos;
import compilador.unitri.semantico.AnalisadorSemantico;
import compilador.unitri.sintatico.AnalisadorSintaticoGeradorArvore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

    public static void lexico(){
        String linha = null;
        String imagemOriginal = null;
        String novaLinha = null;
        String novaImagem = null;

        try {
            File file = new File("src//compilador//unitri//lexico//programa_teste.l7p");
            BufferedReader br = new BufferedReader(new FileReader(file));
            ClassificadorPalavras classificadorPalavras = new ClassificadorPalavras();

            classificadorPalavras.classifica(br);

            System.out.println("#### ANALISADOR LÉXICO ####");
            if (!AnalisadorLexico.getErros().isEmpty()) {
                System.out.println("->>>> Erros LÉXICOS apresentados <<<<-");
                AnalisadorLexico.printErros();
            }else{
                System.out.println("->>>> Análise Léxica Finalizada com Sucesso! <<<<-");
                AnalisadorLexico.printTokens();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        lexico();
        sintatico();
        semantico();
    }

    private static void semantico() {
        System.out.println("#### ANALISADOR SEMÂNTICO ####");
        AnalisadorSemantico semantico = new AnalisadorSemantico(AnalisadorSintaticoGeradorArvore.getRaiz());
        semantico.analisar();

        if(!AnalisadorSemantico.getErros().isEmpty()){
            System.out.println("->>>> Erros SEMÂNTICOS apresentados  <<<<-");
            AnalisadorSemantico.mostraErros();
        }else{
            System.out.println("->>>> Análise Semântica Finalizada com Sucesso! <<<<-");
            TabelaSimbolos.mostrarSimbolos();
        }
    }


    private static void sintatico() {
        System.out.println("#### ANALISADOR SINTÁTICO ####");
        AnalisadorSintaticoGeradorArvore sintatico = new AnalisadorSintaticoGeradorArvore(AnalisadorLexico.getTokens());
        sintatico.analisar();

        if(!AnalisadorSintaticoGeradorArvore.getErros().isEmpty()){
            System.out.println("->>>> Erros SINTÁTICOS apresentados  <<<<-");
            System.out.println(AnalisadorSintaticoGeradorArvore.getErros());
        }else{
            System.out.println("->>>> Análise Sintática Finalizada com Sucesso! <<<<-");
            AnalisadorSintaticoGeradorArvore.mostraArvore();
        }
    }
}
