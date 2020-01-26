package br.ufpe.cin.if688.jflex;

%%

/* Nao altere as configuracoes a seguir */

%line
%column
%unicode
//%debug
%public
%standalone
%class Minijava
%eofclose


whitespace = [ \n\t\r\f]
Literais_Inteiros = [0-9]+
Identificadores = ([A-Za-z]|[_])([0-9]|[A-Za-z]|[_])*
Operadores = [<]|[==]|[!=]|[!]|&&|[*]|[+]|[-]
Reservado = "boolean" | "class" | "public" | "extends" | "static" | "void" | "main" | "String" | "int" | "while" | "if" | "else" | "return" | "length" | "true" | "false" | "this" | "new" | "System.out.println"
Delimitadores = ";" | "." | "," | "=" | "(" | ")" | "{" | "}" | "[" | "]"

/* Insira as regras lexicas abaixo */

%%

{Reservado}                   {System.out.println("token gerado foi um reservado: '" + yytext() + "' na linha: " + yyline + ", coluna: " + yycolumn); }
{Delimitadores}               {System.out.println("token gerado foi um delimitador: '" + yytext() + "' na linha: " + yyline + ", coluna: " + yycolumn); }
{whitespace}                  {} 
("/*" [^*] ~"*/") | ("//".*)  {}
{Operadores}                  {System.out.println("token gerado foi um operador: '" + yytext() + "' na linha: " + yyline + ", coluna: " + yycolumn); }
{Literais_Inteiros}           {System.out.println("token gerado foi um integer: '" + yytext() + "' na linha: " + yyline + ", coluna: " + yycolumn); }  
{Identificadores}             {System.out.println("token gerado foi um id: '" + yytext() + "' na linha: " + yyline + ", coluna: " + yycolumn); }  


/* Insira as regras lexicas no espaco acima */  
     
. { throw new RuntimeException("Caractere ilegal! " + yytext() + " na linha: " + yyline + ", coluna: " + yycolumn); }