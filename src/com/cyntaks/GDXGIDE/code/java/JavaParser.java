// $ANTLR 3.4 Java.g 2012-11-07 19:58:10

    package com.cyntaks.GDXGIDE.code.java;
	import java.util.LinkedList;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


/** A Java 1.5 grammar for ANTLR v3 derived from the spec
 *
 *  This is a very close representation of the spec; the changes
 *  are comestic (remove left recursion) and also fixes (the spec
 *  isn't exactly perfect).  I have run this on the 1.4.2 source
 *  and some nasty looking enums from 1.5, but have not really
 *  tested for 1.5 compatibility.
 *
 *  I built this with: java -Xmx100M org.antlr.Tool java.g
 *  and got two errors that are ok (for now):
 *  java.g:691:9: Decision can match input such as
 *    "'0'..'9'{'E', 'e'}{'+', '-'}'0'..'9'{'D', 'F', 'd', 'f'}"
 *    using multiple alternatives: 3, 4
 *  As a result, alternative(s) 4 were disabled for that input
 *  java.g:734:35: Decision can match input such as "{'$', 'A'..'Z',
 *    '_', 'a'..'z', '\u00C0'..'\u00D6', '\u00D8'..'\u00F6',
 *    '\u00F8'..'\u1FFF', '\u3040'..'\u318F', '\u3300'..'\u337F',
 *    '\u3400'..'\u3D2D', '\u4E00'..'\u9FFF', '\uF900'..'\uFAFF'}"
 *    using multiple alternatives: 1, 2
 *  As a result, alternative(s) 2 were disabled for that input
 *
 *  You can turn enum on/off as a keyword :)
 *
 *  Version 1.0 -- initial release July 5, 2006 (requires 3.0b2 or higher)
 *
 *  Primary author: Terence Parr, July 2006
 *
 *  Version 1.0.1 -- corrections by Koen Vanderkimpen & Marko van Dooren,
 *      October 25, 2006;
 *      fixed normalInterfaceDeclaration: now uses typeParameters instead
 *          of typeParameter (according to JLS, 3rd edition)
 *      fixed castExpression: no longer allows expression next to type
 *          (according to semantics in JLS, in contrast with syntax in JLS)
 *
 *  Version 1.0.2 -- Terence Parr, Nov 27, 2006
 *      java spec I built this from had some bizarre for-loop control.
 *          Looked weird and so I looked elsewhere...Yep, it's messed up.
 *          simplified.
 *
 *  Version 1.0.3 -- Chris Hogue, Feb 26, 2007
 *      Factored out an annotationName rule and used it in the annotation rule.
 *          Not sure why, but typeName wasn't recognizing references to inner
 *          annotations (e.g. @InterfaceName.InnerAnnotation())
 *      Factored out the elementValue section of an annotation reference.  Created
 *          elementValuePair and elementValuePairs rules, then used them in the
 *          annotation rule.  Allows it to recognize annotation references with
 *          multiple, comma separated attributes.
 *      Updated elementValueArrayInitializer so that it allows multiple elements.
 *          (It was only allowing 0 or 1 element).
 *      Updated localVariableDeclaration to allow annotations.  Interestingly the JLS
 *          doesn't appear to indicate this is legal, but it does work as of at least
 *          JDK 1.5.0_06.
 *      Moved the Identifier portion of annotationTypeElementRest to annotationMethodRest.
 *          Because annotationConstantRest already references variableDeclarator which
 *          has the Identifier portion in it, the parser would fail on constants in
 *          annotation definitions because it expected two identifiers.
 *      Added optional trailing ';' to the alternatives in annotationTypeElementRest.
 *          Wouldn't handle an inner interface that has a trailing ';'.
 *      Swapped the expression and type rule reference order in castExpression to
 *          make it check for genericized casts first.  It was failing to recognize a
 *          statement like  "Class<Byte> TYPE = (Class<Byte>)...;" because it was seeing
 *          'Class<Byte' in the cast expression as a less than expression, then failing
 *          on the '>'.
 *      Changed createdName to use typeArguments instead of nonWildcardTypeArguments.
 *         
 *      Changed the 'this' alternative in primary to allow 'identifierSuffix' rather than
 *          just 'arguments'.  The case it couldn't handle was a call to an explicit
 *          generic method invocation (e.g. this.<E>doSomething()).  Using identifierSuffix
 *          may be overly aggressive--perhaps should create a more constrained thisSuffix rule?
 *
 *  Version 1.0.4 -- Hiroaki Nakamura, May 3, 2007
 *
 *  Fixed formalParameterDecls, localVariableDeclaration, forInit,
 *  and forVarControl to use variableModifier* not 'final'? (annotation)?
 *
 *  Version 1.0.5 -- Terence, June 21, 2007
 *  --a[i].foo didn't work. Fixed unaryExpression
 *
 *  Version 1.0.6 -- John Ridgway, March 17, 2008
 *      Made "assert" a switchable keyword like "enum".
 *      Fixed compilationUnit to disallow "annotation importDeclaration ...".
 *      Changed "Identifier ('.' Identifier)*" to "qualifiedName" in more
 *          places.
 *      Changed modifier* and/or variableModifier* to classOrInterfaceModifiers,
 *          modifiers or variableModifiers, as appropriate.
 *      Renamed "bound" to "typeBound" to better match language in the JLS.
 *      Added "memberDeclaration" which rewrites to methodDeclaration or
 *      fieldDeclaration and pulled type into memberDeclaration.  So we parse
 *          type and then move on to decide whether we're dealing with a field
 *          or a method.
 *      Modified "constructorDeclaration" to use "constructorBody" instead of
 *          "methodBody".  constructorBody starts with explicitConstructorInvocation,
 *          then goes on to blockStatement*.  Pulling explicitConstructorInvocation
 *          out of expressions allowed me to simplify "primary".
 *      Changed variableDeclarator to simplify it.
 *      Changed type to use classOrInterfaceType, thus simplifying it; of course
 *          I then had to add classOrInterfaceType, but it is used in several
 *          places.
 *      Fixed annotations, old version allowed "@X(y,z)", which is illegal.
 *      Added optional comma to end of "elementValueArrayInitializer"; as per JLS.
 *      Changed annotationTypeElementRest to use normalClassDeclaration and
 *          normalInterfaceDeclaration rather than classDeclaration and
 *          interfaceDeclaration, thus getting rid of a couple of grammar ambiguities.
 *      Split localVariableDeclaration into localVariableDeclarationStatement
 *          (includes the terminating semi-colon) and localVariableDeclaration.
 *          This allowed me to use localVariableDeclaration in "forInit" clauses,
 *           simplifying them.
 *      Changed switchBlockStatementGroup to use multiple labels.  This adds an
 *          ambiguity, but if one uses appropriately greedy parsing it yields the
 *           parse that is closest to the meaning of the switch statement.
 *      Renamed "forVarControl" to "enhancedForControl" -- JLS language.
 *      Added semantic predicates to test for shift operations rather than other
 *          things.  Thus, for instance, the string "< <" will never be treated
 *          as a left-shift operator.
 *      In "creator" we rule out "nonWildcardTypeArguments" on arrayCreation,
 *          which are illegal.
 *      Moved "nonWildcardTypeArguments into innerCreator.
 *      Removed 'super' superSuffix from explicitGenericInvocation, since that
 *          is only used in explicitConstructorInvocation at the beginning of a
 *           constructorBody.  (This is part of the simplification of expressions
 *           mentioned earlier.)
 *      Simplified primary (got rid of those things that are only used in
 *          explicitConstructorInvocation).
 *      Lexer -- removed "Exponent?" from FloatingPointLiteral choice 4, since it
 *          led to an ambiguity.
 *
 *      This grammar successfully parses every .java file in the JDK 1.5 source
 *          tree (excluding those whose file names include '-', which are not
 *          valid Java compilation units).
 *
 *  Known remaining problems:
 *      "Letter" and "JavaIDDigit" are wrong.  The actual specification of
 *      "Letter" should be "a character for which the method
 *      Character.isJavaIdentifierStart(int) returns true."  A "Java
 *      letter-or-digit is a character for which the method
 *      Character.isJavaIdentifierPart(int) returns true."
 */
@SuppressWarnings({"all", "warnings", "unchecked"})
public class JavaParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ABSTRACT", "AMP", "AMPAMP", "AMPEQ", "ASSERT", "BANG", "BANGEQ", "BAR", "BARBAR", "BAREQ", "BOOLEAN", "BREAK", "BYTE", "CARET", "CARETEQ", "CASE", "CATCH", "CHAR", "CHARLITERAL", "CLASS", "COLON", "COMMA", "COMMENT", "CONST", "CONTINUE", "DEFAULT", "DO", "DOT", "DOUBLE", "DOUBLELITERAL", "DoubleSuffix", "ELLIPSIS", "ELSE", "ENUM", "EQ", "EQEQ", "EXTENDS", "EscapeSequence", "Exponent", "FALSE", "FINAL", "FINALLY", "FLOAT", "FLOATLITERAL", "FOR", "FloatSuffix", "GOTO", "GT", "HexDigit", "HexPrefix", "IDENTIFIER", "IF", "IMPLEMENTS", "IMPORT", "INSTANCEOF", "INT", "INTERFACE", "INTLITERAL", "IdentifierPart", "IdentifierStart", "IntegerNumber", "LBRACE", "LBRACKET", "LINE_COMMENT", "LONG", "LONGLITERAL", "LPAREN", "LT", "LongSuffix", "NATIVE", "NEW", "NULL", "NonIntegerNumber", "PACKAGE", "PERCENT", "PERCENTEQ", "PLUS", "PLUSEQ", "PLUSPLUS", "PRIVATE", "PROTECTED", "PUBLIC", "QUES", "RBRACE", "RBRACKET", "RETURN", "RPAREN", "SEMI", "SHORT", "SLASH", "SLASHEQ", "STAR", "STAREQ", "STATIC", "STRICTFP", "STRINGLITERAL", "SUB", "SUBEQ", "SUBSUB", "SUPER", "SWITCH", "SYNCHRONIZED", "SurrogateIdentifer", "THIS", "THROW", "THROWS", "TILDE", "TRANSIENT", "TRUE", "TRY", "VOID", "VOLATILE", "WANNOTATION", "WANNOTATIONBLOCK", "WBLOCKSTATEMENT", "WCLASSCONTAINER", "WCLASSHEAD", "WCONSTRUCTORINVOCATION", "WCONTAINER", "WENUMDECL", "WFIELDDECLARATION", "WHILE", "WIMPORTBLOCK", "WIMPORTDECLARATION", "WINTERFACEDECL", "WMETHODCONTAINER", "WMETHODDECLARATION", "WMETHODHEAD", "WNORMALCLASSDECLARATION", "WPACKAGEDECLARATION", "WS", "WSTATEMENT", "'@'"
    };

    public static final int EOF=-1;
    public static final int T__136=136;
    public static final int ABSTRACT=4;
    public static final int AMP=5;
    public static final int AMPAMP=6;
    public static final int AMPEQ=7;
    public static final int ASSERT=8;
    public static final int BANG=9;
    public static final int BANGEQ=10;
    public static final int BAR=11;
    public static final int BARBAR=12;
    public static final int BAREQ=13;
    public static final int BOOLEAN=14;
    public static final int BREAK=15;
    public static final int BYTE=16;
    public static final int CARET=17;
    public static final int CARETEQ=18;
    public static final int CASE=19;
    public static final int CATCH=20;
    public static final int CHAR=21;
    public static final int CHARLITERAL=22;
    public static final int CLASS=23;
    public static final int COLON=24;
    public static final int COMMA=25;
    public static final int COMMENT=26;
    public static final int CONST=27;
    public static final int CONTINUE=28;
    public static final int DEFAULT=29;
    public static final int DO=30;
    public static final int DOT=31;
    public static final int DOUBLE=32;
    public static final int DOUBLELITERAL=33;
    public static final int DoubleSuffix=34;
    public static final int ELLIPSIS=35;
    public static final int ELSE=36;
    public static final int ENUM=37;
    public static final int EQ=38;
    public static final int EQEQ=39;
    public static final int EXTENDS=40;
    public static final int EscapeSequence=41;
    public static final int Exponent=42;
    public static final int FALSE=43;
    public static final int FINAL=44;
    public static final int FINALLY=45;
    public static final int FLOAT=46;
    public static final int FLOATLITERAL=47;
    public static final int FOR=48;
    public static final int FloatSuffix=49;
    public static final int GOTO=50;
    public static final int GT=51;
    public static final int HexDigit=52;
    public static final int HexPrefix=53;
    public static final int IDENTIFIER=54;
    public static final int IF=55;
    public static final int IMPLEMENTS=56;
    public static final int IMPORT=57;
    public static final int INSTANCEOF=58;
    public static final int INT=59;
    public static final int INTERFACE=60;
    public static final int INTLITERAL=61;
    public static final int IdentifierPart=62;
    public static final int IdentifierStart=63;
    public static final int IntegerNumber=64;
    public static final int LBRACE=65;
    public static final int LBRACKET=66;
    public static final int LINE_COMMENT=67;
    public static final int LONG=68;
    public static final int LONGLITERAL=69;
    public static final int LPAREN=70;
    public static final int LT=71;
    public static final int LongSuffix=72;
    public static final int NATIVE=73;
    public static final int NEW=74;
    public static final int NULL=75;
    public static final int NonIntegerNumber=76;
    public static final int PACKAGE=77;
    public static final int PERCENT=78;
    public static final int PERCENTEQ=79;
    public static final int PLUS=80;
    public static final int PLUSEQ=81;
    public static final int PLUSPLUS=82;
    public static final int PRIVATE=83;
    public static final int PROTECTED=84;
    public static final int PUBLIC=85;
    public static final int QUES=86;
    public static final int RBRACE=87;
    public static final int RBRACKET=88;
    public static final int RETURN=89;
    public static final int RPAREN=90;
    public static final int SEMI=91;
    public static final int SHORT=92;
    public static final int SLASH=93;
    public static final int SLASHEQ=94;
    public static final int STAR=95;
    public static final int STAREQ=96;
    public static final int STATIC=97;
    public static final int STRICTFP=98;
    public static final int STRINGLITERAL=99;
    public static final int SUB=100;
    public static final int SUBEQ=101;
    public static final int SUBSUB=102;
    public static final int SUPER=103;
    public static final int SWITCH=104;
    public static final int SYNCHRONIZED=105;
    public static final int SurrogateIdentifer=106;
    public static final int THIS=107;
    public static final int THROW=108;
    public static final int THROWS=109;
    public static final int TILDE=110;
    public static final int TRANSIENT=111;
    public static final int TRUE=112;
    public static final int TRY=113;
    public static final int VOID=114;
    public static final int VOLATILE=115;
    public static final int WANNOTATION=116;
    public static final int WANNOTATIONBLOCK=117;
    public static final int WBLOCKSTATEMENT=118;
    public static final int WCLASSCONTAINER=119;
    public static final int WCLASSHEAD=120;
    public static final int WCONSTRUCTORINVOCATION=121;
    public static final int WCONTAINER=122;
    public static final int WENUMDECL=123;
    public static final int WFIELDDECLARATION=124;
    public static final int WHILE=125;
    public static final int WIMPORTBLOCK=126;
    public static final int WIMPORTDECLARATION=127;
    public static final int WINTERFACEDECL=128;
    public static final int WMETHODCONTAINER=129;
    public static final int WMETHODDECLARATION=130;
    public static final int WMETHODHEAD=131;
    public static final int WNORMALCLASSDECLARATION=132;
    public static final int WPACKAGEDECLARATION=133;
    public static final int WS=134;
    public static final int WSTATEMENT=135;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public JavaParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public JavaParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
        this.state.ruleMemo = new HashMap[362+1];
         

    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return JavaParser.tokenNames; }
    public String getGrammarFileName() { return "Java.g"; }


        private List<String> errors = new LinkedList<String>();
        public void displayRecognitionError(String[] tokenNames,
                                            RecognitionException e) {
            String hdr = getErrorHeader(e);
            String msg = getErrorMessage(e, tokenNames);
            errors.add(hdr + " " + msg);
        }
        public List<String> getErrors() {
            return errors;
        }


    public static class compilationUnit_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "compilationUnit"
    // Java.g:321:1: compilationUnit : ( ( annotations )? packageDeclaration )? ( importBlock )? ( typeDeclaration )* -> ( ( ^( WANNOTATIONBLOCK annotations ) )? ^( WPACKAGEDECLARATION packageDeclaration ) )? ( ^( WIMPORTBLOCK importBlock ) )? ( typeDeclaration )* ;
    public final JavaParser.compilationUnit_return compilationUnit() throws RecognitionException {
        JavaParser.compilationUnit_return retval = new JavaParser.compilationUnit_return();
        retval.start = input.LT(1);

        int compilationUnit_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.annotations_return annotations1 =null;

        JavaParser.packageDeclaration_return packageDeclaration2 =null;

        JavaParser.importBlock_return importBlock3 =null;

        JavaParser.typeDeclaration_return typeDeclaration4 =null;


        RewriteRuleSubtreeStream stream_packageDeclaration=new RewriteRuleSubtreeStream(adaptor,"rule packageDeclaration");
        RewriteRuleSubtreeStream stream_importBlock=new RewriteRuleSubtreeStream(adaptor,"rule importBlock");
        RewriteRuleSubtreeStream stream_annotations=new RewriteRuleSubtreeStream(adaptor,"rule annotations");
        RewriteRuleSubtreeStream stream_typeDeclaration=new RewriteRuleSubtreeStream(adaptor,"rule typeDeclaration");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 1) ) { return retval; }

            // Java.g:322:5: ( ( ( annotations )? packageDeclaration )? ( importBlock )? ( typeDeclaration )* -> ( ( ^( WANNOTATIONBLOCK annotations ) )? ^( WPACKAGEDECLARATION packageDeclaration ) )? ( ^( WIMPORTBLOCK importBlock ) )? ( typeDeclaration )* )
            // Java.g:322:9: ( ( annotations )? packageDeclaration )? ( importBlock )? ( typeDeclaration )*
            {
            // Java.g:322:9: ( ( annotations )? packageDeclaration )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==136) ) {
                int LA2_1 = input.LA(2);

                if ( (synpred2_Java()) ) {
                    alt2=1;
                }
            }
            else if ( (LA2_0==PACKAGE) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // Java.g:322:13: ( annotations )? packageDeclaration
                    {
                    // Java.g:322:13: ( annotations )?
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==136) ) {
                        alt1=1;
                    }
                    switch (alt1) {
                        case 1 :
                            // Java.g:322:14: annotations
                            {
                            pushFollow(FOLLOW_annotations_in_compilationUnit109);
                            annotations1=annotations();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_annotations.add(annotations1.getTree());

                            }
                            break;

                    }


                    pushFollow(FOLLOW_packageDeclaration_in_compilationUnit138);
                    packageDeclaration2=packageDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_packageDeclaration.add(packageDeclaration2.getTree());

                    }
                    break;

            }


            // Java.g:326:9: ( importBlock )?
            int alt3=2;
            switch ( input.LA(1) ) {
                case IMPORT:
                    {
                    alt3=1;
                    }
                    break;
                case 136:
                    {
                    int LA3_2 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case PUBLIC:
                    {
                    int LA3_3 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case PROTECTED:
                    {
                    int LA3_4 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case PRIVATE:
                    {
                    int LA3_5 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case STATIC:
                    {
                    int LA3_6 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case ABSTRACT:
                    {
                    int LA3_7 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case FINAL:
                    {
                    int LA3_8 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case NATIVE:
                    {
                    int LA3_9 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case SYNCHRONIZED:
                    {
                    int LA3_10 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case TRANSIENT:
                    {
                    int LA3_11 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case VOLATILE:
                    {
                    int LA3_12 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case STRICTFP:
                    {
                    int LA3_13 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case CLASS:
                    {
                    int LA3_14 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case ENUM:
                    {
                    int LA3_15 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case INTERFACE:
                    {
                    int LA3_16 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case SEMI:
                    {
                    int LA3_17 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
                case EOF:
                    {
                    int LA3_18 = input.LA(2);

                    if ( (synpred3_Java()) ) {
                        alt3=1;
                    }
                    }
                    break;
            }

            switch (alt3) {
                case 1 :
                    // Java.g:326:10: importBlock
                    {
                    pushFollow(FOLLOW_importBlock_in_compilationUnit160);
                    importBlock3=importBlock();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_importBlock.add(importBlock3.getTree());

                    }
                    break;

            }


            // Java.g:327:9: ( typeDeclaration )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==ABSTRACT||LA4_0==BOOLEAN||LA4_0==BYTE||LA4_0==CHAR||LA4_0==CLASS||LA4_0==DOUBLE||LA4_0==ENUM||LA4_0==FINAL||LA4_0==FLOAT||LA4_0==IDENTIFIER||(LA4_0 >= INT && LA4_0 <= INTERFACE)||LA4_0==LONG||LA4_0==LT||LA4_0==NATIVE||(LA4_0 >= PRIVATE && LA4_0 <= PUBLIC)||(LA4_0 >= SEMI && LA4_0 <= SHORT)||(LA4_0 >= STATIC && LA4_0 <= STRICTFP)||LA4_0==SYNCHRONIZED||LA4_0==TRANSIENT||(LA4_0 >= VOID && LA4_0 <= VOLATILE)||LA4_0==136) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // Java.g:327:10: typeDeclaration
            	    {
            	    pushFollow(FOLLOW_typeDeclaration_in_compilationUnit173);
            	    typeDeclaration4=typeDeclaration();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_typeDeclaration.add(typeDeclaration4.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            // AST REWRITE
            // elements: packageDeclaration, importBlock, typeDeclaration, annotations
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 329:3: -> ( ( ^( WANNOTATIONBLOCK annotations ) )? ^( WPACKAGEDECLARATION packageDeclaration ) )? ( ^( WIMPORTBLOCK importBlock ) )? ( typeDeclaration )*
            {
                // Java.g:329:6: ( ( ^( WANNOTATIONBLOCK annotations ) )? ^( WPACKAGEDECLARATION packageDeclaration ) )?
                if ( stream_packageDeclaration.hasNext()||stream_annotations.hasNext() ) {
                    // Java.g:329:10: ( ^( WANNOTATIONBLOCK annotations ) )?
                    if ( stream_annotations.hasNext() ) {
                        // Java.g:329:10: ^( WANNOTATIONBLOCK annotations )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WANNOTATIONBLOCK, "WANNOTATIONBLOCK")
                        , root_1);

                        adaptor.addChild(root_1, stream_annotations.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }
                    stream_annotations.reset();

                    // Java.g:331:13: ^( WPACKAGEDECLARATION packageDeclaration )
                    {
                    Object root_1 = (Object)adaptor.nil();
                    root_1 = (Object)adaptor.becomeRoot(
                    (Object)adaptor.create(WPACKAGEDECLARATION, "WPACKAGEDECLARATION")
                    , root_1);

                    adaptor.addChild(root_1, stream_packageDeclaration.nextTree());

                    adaptor.addChild(root_0, root_1);
                    }

                }
                stream_packageDeclaration.reset();
                stream_annotations.reset();

                // Java.g:333:9: ( ^( WIMPORTBLOCK importBlock ) )?
                if ( stream_importBlock.hasNext() ) {
                    // Java.g:333:9: ^( WIMPORTBLOCK importBlock )
                    {
                    Object root_1 = (Object)adaptor.nil();
                    root_1 = (Object)adaptor.becomeRoot(
                    (Object)adaptor.create(WIMPORTBLOCK, "WIMPORTBLOCK")
                    , root_1);

                    adaptor.addChild(root_1, stream_importBlock.nextTree());

                    adaptor.addChild(root_0, root_1);
                    }

                }
                stream_importBlock.reset();

                // Java.g:334:9: ( typeDeclaration )*
                while ( stream_typeDeclaration.hasNext() ) {
                    adaptor.addChild(root_0, stream_typeDeclaration.nextTree());

                }
                stream_typeDeclaration.reset();

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 1, compilationUnit_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "compilationUnit"


    public static class importBlock_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "importBlock"
    // Java.g:338:1: importBlock : ( importDeclaration )* -> ( ^( WIMPORTDECLARATION importDeclaration ) )* ;
    public final JavaParser.importBlock_return importBlock() throws RecognitionException {
        JavaParser.importBlock_return retval = new JavaParser.importBlock_return();
        retval.start = input.LT(1);

        int importBlock_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.importDeclaration_return importDeclaration5 =null;


        RewriteRuleSubtreeStream stream_importDeclaration=new RewriteRuleSubtreeStream(adaptor,"rule importDeclaration");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 2) ) { return retval; }

            // Java.g:339:2: ( ( importDeclaration )* -> ( ^( WIMPORTDECLARATION importDeclaration ) )* )
            // Java.g:339:4: ( importDeclaration )*
            {
            // Java.g:339:4: ( importDeclaration )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==IMPORT) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // Java.g:339:5: importDeclaration
            	    {
            	    pushFollow(FOLLOW_importDeclaration_in_importBlock294);
            	    importDeclaration5=importDeclaration();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_importDeclaration.add(importDeclaration5.getTree());

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            // AST REWRITE
            // elements: importDeclaration
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 339:25: -> ( ^( WIMPORTDECLARATION importDeclaration ) )*
            {
                // Java.g:339:28: ( ^( WIMPORTDECLARATION importDeclaration ) )*
                while ( stream_importDeclaration.hasNext() ) {
                    // Java.g:339:28: ^( WIMPORTDECLARATION importDeclaration )
                    {
                    Object root_1 = (Object)adaptor.nil();
                    root_1 = (Object)adaptor.becomeRoot(
                    (Object)adaptor.create(WIMPORTDECLARATION, "WIMPORTDECLARATION")
                    , root_1);

                    adaptor.addChild(root_1, stream_importDeclaration.nextTree());

                    adaptor.addChild(root_0, root_1);
                    }

                }
                stream_importDeclaration.reset();

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 2, importBlock_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "importBlock"


    public static class packageDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "packageDeclaration"
    // Java.g:342:1: packageDeclaration : 'package' qualifiedName ';' ;
    public final JavaParser.packageDeclaration_return packageDeclaration() throws RecognitionException {
        JavaParser.packageDeclaration_return retval = new JavaParser.packageDeclaration_return();
        retval.start = input.LT(1);

        int packageDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal6=null;
        Token char_literal8=null;
        JavaParser.qualifiedName_return qualifiedName7 =null;


        Object string_literal6_tree=null;
        Object char_literal8_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 3) ) { return retval; }

            // Java.g:343:5: ( 'package' qualifiedName ';' )
            // Java.g:343:9: 'package' qualifiedName ';'
            {
            root_0 = (Object)adaptor.nil();


            string_literal6=(Token)match(input,PACKAGE,FOLLOW_PACKAGE_in_packageDeclaration322); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal6_tree = 
            (Object)adaptor.create(string_literal6)
            ;
            adaptor.addChild(root_0, string_literal6_tree);
            }

            pushFollow(FOLLOW_qualifiedName_in_packageDeclaration324);
            qualifiedName7=qualifiedName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, qualifiedName7.getTree());

            char_literal8=(Token)match(input,SEMI,FOLLOW_SEMI_in_packageDeclaration334); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal8_tree = 
            (Object)adaptor.create(char_literal8)
            ;
            adaptor.addChild(root_0, char_literal8_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 3, packageDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "packageDeclaration"


    public static class importDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "importDeclaration"
    // Java.g:347:1: importDeclaration : ( 'import' ( 'static' )? IDENTIFIER '.' '*' ';' | 'import' ( 'static' )? IDENTIFIER ( '.' IDENTIFIER )+ ( '.' '*' )? ';' );
    public final JavaParser.importDeclaration_return importDeclaration() throws RecognitionException {
        JavaParser.importDeclaration_return retval = new JavaParser.importDeclaration_return();
        retval.start = input.LT(1);

        int importDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal9=null;
        Token string_literal10=null;
        Token IDENTIFIER11=null;
        Token char_literal12=null;
        Token char_literal13=null;
        Token char_literal14=null;
        Token string_literal15=null;
        Token string_literal16=null;
        Token IDENTIFIER17=null;
        Token char_literal18=null;
        Token IDENTIFIER19=null;
        Token char_literal20=null;
        Token char_literal21=null;
        Token char_literal22=null;

        Object string_literal9_tree=null;
        Object string_literal10_tree=null;
        Object IDENTIFIER11_tree=null;
        Object char_literal12_tree=null;
        Object char_literal13_tree=null;
        Object char_literal14_tree=null;
        Object string_literal15_tree=null;
        Object string_literal16_tree=null;
        Object IDENTIFIER17_tree=null;
        Object char_literal18_tree=null;
        Object IDENTIFIER19_tree=null;
        Object char_literal20_tree=null;
        Object char_literal21_tree=null;
        Object char_literal22_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 4) ) { return retval; }

            // Java.g:348:5: ( 'import' ( 'static' )? IDENTIFIER '.' '*' ';' | 'import' ( 'static' )? IDENTIFIER ( '.' IDENTIFIER )+ ( '.' '*' )? ';' )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==IMPORT) ) {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==STATIC) ) {
                    int LA10_2 = input.LA(3);

                    if ( (LA10_2==IDENTIFIER) ) {
                        int LA10_3 = input.LA(4);

                        if ( (LA10_3==DOT) ) {
                            int LA10_4 = input.LA(5);

                            if ( (LA10_4==STAR) ) {
                                alt10=1;
                            }
                            else if ( (LA10_4==IDENTIFIER) ) {
                                alt10=2;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return retval;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 10, 4, input);

                                throw nvae;

                            }
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 10, 3, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 10, 2, input);

                        throw nvae;

                    }
                }
                else if ( (LA10_1==IDENTIFIER) ) {
                    int LA10_3 = input.LA(3);

                    if ( (LA10_3==DOT) ) {
                        int LA10_4 = input.LA(4);

                        if ( (LA10_4==STAR) ) {
                            alt10=1;
                        }
                        else if ( (LA10_4==IDENTIFIER) ) {
                            alt10=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 10, 4, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 10, 3, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }
            switch (alt10) {
                case 1 :
                    // Java.g:348:9: 'import' ( 'static' )? IDENTIFIER '.' '*' ';'
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal9=(Token)match(input,IMPORT,FOLLOW_IMPORT_in_importDeclaration355); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal9_tree = 
                    (Object)adaptor.create(string_literal9)
                    ;
                    adaptor.addChild(root_0, string_literal9_tree);
                    }

                    // Java.g:349:9: ( 'static' )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==STATIC) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // Java.g:349:10: 'static'
                            {
                            string_literal10=(Token)match(input,STATIC,FOLLOW_STATIC_in_importDeclaration367); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal10_tree = 
                            (Object)adaptor.create(string_literal10)
                            ;
                            adaptor.addChild(root_0, string_literal10_tree);
                            }

                            }
                            break;

                    }


                    IDENTIFIER11=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_importDeclaration388); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    IDENTIFIER11_tree = 
                    (Object)adaptor.create(IDENTIFIER11)
                    ;
                    adaptor.addChild(root_0, IDENTIFIER11_tree);
                    }

                    char_literal12=(Token)match(input,DOT,FOLLOW_DOT_in_importDeclaration390); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal12_tree = 
                    (Object)adaptor.create(char_literal12)
                    ;
                    adaptor.addChild(root_0, char_literal12_tree);
                    }

                    char_literal13=(Token)match(input,STAR,FOLLOW_STAR_in_importDeclaration392); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal13_tree = 
                    (Object)adaptor.create(char_literal13)
                    ;
                    adaptor.addChild(root_0, char_literal13_tree);
                    }

                    char_literal14=(Token)match(input,SEMI,FOLLOW_SEMI_in_importDeclaration402); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal14_tree = 
                    (Object)adaptor.create(char_literal14)
                    ;
                    adaptor.addChild(root_0, char_literal14_tree);
                    }

                    }
                    break;
                case 2 :
                    // Java.g:353:9: 'import' ( 'static' )? IDENTIFIER ( '.' IDENTIFIER )+ ( '.' '*' )? ';'
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal15=(Token)match(input,IMPORT,FOLLOW_IMPORT_in_importDeclaration412); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal15_tree = 
                    (Object)adaptor.create(string_literal15)
                    ;
                    adaptor.addChild(root_0, string_literal15_tree);
                    }

                    // Java.g:354:9: ( 'static' )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==STATIC) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // Java.g:354:10: 'static'
                            {
                            string_literal16=(Token)match(input,STATIC,FOLLOW_STATIC_in_importDeclaration424); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal16_tree = 
                            (Object)adaptor.create(string_literal16)
                            ;
                            adaptor.addChild(root_0, string_literal16_tree);
                            }

                            }
                            break;

                    }


                    IDENTIFIER17=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_importDeclaration445); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    IDENTIFIER17_tree = 
                    (Object)adaptor.create(IDENTIFIER17)
                    ;
                    adaptor.addChild(root_0, IDENTIFIER17_tree);
                    }

                    // Java.g:357:9: ( '.' IDENTIFIER )+
                    int cnt8=0;
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0==DOT) ) {
                            int LA8_1 = input.LA(2);

                            if ( (LA8_1==IDENTIFIER) ) {
                                alt8=1;
                            }


                        }


                        switch (alt8) {
                    	case 1 :
                    	    // Java.g:357:10: '.' IDENTIFIER
                    	    {
                    	    char_literal18=(Token)match(input,DOT,FOLLOW_DOT_in_importDeclaration456); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal18_tree = 
                    	    (Object)adaptor.create(char_literal18)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal18_tree);
                    	    }

                    	    IDENTIFIER19=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_importDeclaration458); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    IDENTIFIER19_tree = 
                    	    (Object)adaptor.create(IDENTIFIER19)
                    	    ;
                    	    adaptor.addChild(root_0, IDENTIFIER19_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt8 >= 1 ) break loop8;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(8, input);
                                throw eee;
                        }
                        cnt8++;
                    } while (true);


                    // Java.g:359:9: ( '.' '*' )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==DOT) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // Java.g:359:10: '.' '*'
                            {
                            char_literal20=(Token)match(input,DOT,FOLLOW_DOT_in_importDeclaration480); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            char_literal20_tree = 
                            (Object)adaptor.create(char_literal20)
                            ;
                            adaptor.addChild(root_0, char_literal20_tree);
                            }

                            char_literal21=(Token)match(input,STAR,FOLLOW_STAR_in_importDeclaration482); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            char_literal21_tree = 
                            (Object)adaptor.create(char_literal21)
                            ;
                            adaptor.addChild(root_0, char_literal21_tree);
                            }

                            }
                            break;

                    }


                    char_literal22=(Token)match(input,SEMI,FOLLOW_SEMI_in_importDeclaration503); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal22_tree = 
                    (Object)adaptor.create(char_literal22)
                    ;
                    adaptor.addChild(root_0, char_literal22_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 4, importDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "importDeclaration"


    public static class qualifiedImportName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "qualifiedImportName"
    // Java.g:364:1: qualifiedImportName : IDENTIFIER ( '.' IDENTIFIER )* ;
    public final JavaParser.qualifiedImportName_return qualifiedImportName() throws RecognitionException {
        JavaParser.qualifiedImportName_return retval = new JavaParser.qualifiedImportName_return();
        retval.start = input.LT(1);

        int qualifiedImportName_StartIndex = input.index();

        Object root_0 = null;

        Token IDENTIFIER23=null;
        Token char_literal24=null;
        Token IDENTIFIER25=null;

        Object IDENTIFIER23_tree=null;
        Object char_literal24_tree=null;
        Object IDENTIFIER25_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return retval; }

            // Java.g:365:5: ( IDENTIFIER ( '.' IDENTIFIER )* )
            // Java.g:365:9: IDENTIFIER ( '.' IDENTIFIER )*
            {
            root_0 = (Object)adaptor.nil();


            IDENTIFIER23=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_qualifiedImportName523); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER23_tree = 
            (Object)adaptor.create(IDENTIFIER23)
            ;
            adaptor.addChild(root_0, IDENTIFIER23_tree);
            }

            // Java.g:366:9: ( '.' IDENTIFIER )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==DOT) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // Java.g:366:10: '.' IDENTIFIER
            	    {
            	    char_literal24=(Token)match(input,DOT,FOLLOW_DOT_in_qualifiedImportName534); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal24_tree = 
            	    (Object)adaptor.create(char_literal24)
            	    ;
            	    adaptor.addChild(root_0, char_literal24_tree);
            	    }

            	    IDENTIFIER25=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_qualifiedImportName536); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    IDENTIFIER25_tree = 
            	    (Object)adaptor.create(IDENTIFIER25)
            	    ;
            	    adaptor.addChild(root_0, IDENTIFIER25_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 5, qualifiedImportName_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "qualifiedImportName"


    public static class typeDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "typeDeclaration"
    // Java.g:370:1: typeDeclaration : ( classOrInterfaceDeclaration | ';' );
    public final JavaParser.typeDeclaration_return typeDeclaration() throws RecognitionException {
        JavaParser.typeDeclaration_return retval = new JavaParser.typeDeclaration_return();
        retval.start = input.LT(1);

        int typeDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal27=null;
        JavaParser.classOrInterfaceDeclaration_return classOrInterfaceDeclaration26 =null;


        Object char_literal27_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return retval; }

            // Java.g:371:5: ( classOrInterfaceDeclaration | ';' )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ABSTRACT||LA12_0==BOOLEAN||LA12_0==BYTE||LA12_0==CHAR||LA12_0==CLASS||LA12_0==DOUBLE||LA12_0==ENUM||LA12_0==FINAL||LA12_0==FLOAT||LA12_0==IDENTIFIER||(LA12_0 >= INT && LA12_0 <= INTERFACE)||LA12_0==LONG||LA12_0==LT||LA12_0==NATIVE||(LA12_0 >= PRIVATE && LA12_0 <= PUBLIC)||LA12_0==SHORT||(LA12_0 >= STATIC && LA12_0 <= STRICTFP)||LA12_0==SYNCHRONIZED||LA12_0==TRANSIENT||(LA12_0 >= VOID && LA12_0 <= VOLATILE)||LA12_0==136) ) {
                alt12=1;
            }
            else if ( (LA12_0==SEMI) ) {
                alt12=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;

            }
            switch (alt12) {
                case 1 :
                    // Java.g:371:9: classOrInterfaceDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration567);
                    classOrInterfaceDeclaration26=classOrInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classOrInterfaceDeclaration26.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:372:9: ';'
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal27=(Token)match(input,SEMI,FOLLOW_SEMI_in_typeDeclaration577); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal27_tree = 
                    (Object)adaptor.create(char_literal27)
                    ;
                    adaptor.addChild(root_0, char_literal27_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 6, typeDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeDeclaration"


    public static class classOrInterfaceDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "classOrInterfaceDeclaration"
    // Java.g:375:1: classOrInterfaceDeclaration : ( classDeclaration | interfaceDeclaration );
    public final JavaParser.classOrInterfaceDeclaration_return classOrInterfaceDeclaration() throws RecognitionException {
        JavaParser.classOrInterfaceDeclaration_return retval = new JavaParser.classOrInterfaceDeclaration_return();
        retval.start = input.LT(1);

        int classOrInterfaceDeclaration_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.classDeclaration_return classDeclaration28 =null;

        JavaParser.interfaceDeclaration_return interfaceDeclaration29 =null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return retval; }

            // Java.g:376:5: ( classDeclaration | interfaceDeclaration )
            int alt13=2;
            switch ( input.LA(1) ) {
            case 136:
                {
                int LA13_1 = input.LA(2);

                if ( (synpred13_Java()) ) {
                    alt13=1;
                }
                else if ( (true) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;

                }
                }
                break;
            case PUBLIC:
                {
                int LA13_2 = input.LA(2);

                if ( (synpred13_Java()) ) {
                    alt13=1;
                }
                else if ( (true) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 2, input);

                    throw nvae;

                }
                }
                break;
            case PROTECTED:
                {
                int LA13_3 = input.LA(2);

                if ( (synpred13_Java()) ) {
                    alt13=1;
                }
                else if ( (true) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 3, input);

                    throw nvae;

                }
                }
                break;
            case PRIVATE:
                {
                int LA13_4 = input.LA(2);

                if ( (synpred13_Java()) ) {
                    alt13=1;
                }
                else if ( (true) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 4, input);

                    throw nvae;

                }
                }
                break;
            case STATIC:
                {
                int LA13_5 = input.LA(2);

                if ( (synpred13_Java()) ) {
                    alt13=1;
                }
                else if ( (true) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 5, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
                {
                int LA13_6 = input.LA(2);

                if ( (synpred13_Java()) ) {
                    alt13=1;
                }
                else if ( (true) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 6, input);

                    throw nvae;

                }
                }
                break;
            case FINAL:
                {
                int LA13_7 = input.LA(2);

                if ( (synpred13_Java()) ) {
                    alt13=1;
                }
                else if ( (true) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 7, input);

                    throw nvae;

                }
                }
                break;
            case NATIVE:
                {
                int LA13_8 = input.LA(2);

                if ( (synpred13_Java()) ) {
                    alt13=1;
                }
                else if ( (true) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 8, input);

                    throw nvae;

                }
                }
                break;
            case SYNCHRONIZED:
                {
                int LA13_9 = input.LA(2);

                if ( (synpred13_Java()) ) {
                    alt13=1;
                }
                else if ( (true) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 9, input);

                    throw nvae;

                }
                }
                break;
            case TRANSIENT:
                {
                int LA13_10 = input.LA(2);

                if ( (synpred13_Java()) ) {
                    alt13=1;
                }
                else if ( (true) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 10, input);

                    throw nvae;

                }
                }
                break;
            case VOLATILE:
                {
                int LA13_11 = input.LA(2);

                if ( (synpred13_Java()) ) {
                    alt13=1;
                }
                else if ( (true) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 11, input);

                    throw nvae;

                }
                }
                break;
            case STRICTFP:
                {
                int LA13_12 = input.LA(2);

                if ( (synpred13_Java()) ) {
                    alt13=1;
                }
                else if ( (true) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 12, input);

                    throw nvae;

                }
                }
                break;
            case CLASS:
            case ENUM:
                {
                alt13=1;
                }
                break;
            case INTERFACE:
                {
                alt13=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }

            switch (alt13) {
                case 1 :
                    // Java.g:376:10: classDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_classDeclaration_in_classOrInterfaceDeclaration598);
                    classDeclaration28=classDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classDeclaration28.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:377:9: interfaceDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration608);
                    interfaceDeclaration29=interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, interfaceDeclaration29.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 7, classOrInterfaceDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "classOrInterfaceDeclaration"


    public static class modifiers_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "modifiers"
    // Java.g:381:1: modifiers : ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )* ;
    public final JavaParser.modifiers_return modifiers() throws RecognitionException {
        JavaParser.modifiers_return retval = new JavaParser.modifiers_return();
        retval.start = input.LT(1);

        int modifiers_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal31=null;
        Token string_literal32=null;
        Token string_literal33=null;
        Token string_literal34=null;
        Token string_literal35=null;
        Token string_literal36=null;
        Token string_literal37=null;
        Token string_literal38=null;
        Token string_literal39=null;
        Token string_literal40=null;
        Token string_literal41=null;
        JavaParser.annotation_return annotation30 =null;


        Object string_literal31_tree=null;
        Object string_literal32_tree=null;
        Object string_literal33_tree=null;
        Object string_literal34_tree=null;
        Object string_literal35_tree=null;
        Object string_literal36_tree=null;
        Object string_literal37_tree=null;
        Object string_literal38_tree=null;
        Object string_literal39_tree=null;
        Object string_literal40_tree=null;
        Object string_literal41_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return retval; }

            // Java.g:382:5: ( ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )* )
            // Java.g:383:5: ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )*
            {
            root_0 = (Object)adaptor.nil();


            // Java.g:383:5: ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )*
            loop14:
            do {
                int alt14=13;
                switch ( input.LA(1) ) {
                case 136:
                    {
                    int LA14_2 = input.LA(2);

                    if ( (LA14_2==IDENTIFIER) ) {
                        alt14=1;
                    }


                    }
                    break;
                case PUBLIC:
                    {
                    alt14=2;
                    }
                    break;
                case PROTECTED:
                    {
                    alt14=3;
                    }
                    break;
                case PRIVATE:
                    {
                    alt14=4;
                    }
                    break;
                case STATIC:
                    {
                    alt14=5;
                    }
                    break;
                case ABSTRACT:
                    {
                    alt14=6;
                    }
                    break;
                case FINAL:
                    {
                    alt14=7;
                    }
                    break;
                case NATIVE:
                    {
                    alt14=8;
                    }
                    break;
                case SYNCHRONIZED:
                    {
                    alt14=9;
                    }
                    break;
                case TRANSIENT:
                    {
                    alt14=10;
                    }
                    break;
                case VOLATILE:
                    {
                    alt14=11;
                    }
                    break;
                case STRICTFP:
                    {
                    alt14=12;
                    }
                    break;

                }

                switch (alt14) {
            	case 1 :
            	    // Java.g:383:10: annotation
            	    {
            	    pushFollow(FOLLOW_annotation_in_modifiers643);
            	    annotation30=annotation();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotation30.getTree());

            	    }
            	    break;
            	case 2 :
            	    // Java.g:384:9: 'public'
            	    {
            	    string_literal31=(Token)match(input,PUBLIC,FOLLOW_PUBLIC_in_modifiers653); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal31_tree = 
            	    (Object)adaptor.create(string_literal31)
            	    ;
            	    adaptor.addChild(root_0, string_literal31_tree);
            	    }

            	    }
            	    break;
            	case 3 :
            	    // Java.g:385:9: 'protected'
            	    {
            	    string_literal32=(Token)match(input,PROTECTED,FOLLOW_PROTECTED_in_modifiers663); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal32_tree = 
            	    (Object)adaptor.create(string_literal32)
            	    ;
            	    adaptor.addChild(root_0, string_literal32_tree);
            	    }

            	    }
            	    break;
            	case 4 :
            	    // Java.g:386:9: 'private'
            	    {
            	    string_literal33=(Token)match(input,PRIVATE,FOLLOW_PRIVATE_in_modifiers673); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal33_tree = 
            	    (Object)adaptor.create(string_literal33)
            	    ;
            	    adaptor.addChild(root_0, string_literal33_tree);
            	    }

            	    }
            	    break;
            	case 5 :
            	    // Java.g:387:9: 'static'
            	    {
            	    string_literal34=(Token)match(input,STATIC,FOLLOW_STATIC_in_modifiers683); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal34_tree = 
            	    (Object)adaptor.create(string_literal34)
            	    ;
            	    adaptor.addChild(root_0, string_literal34_tree);
            	    }

            	    }
            	    break;
            	case 6 :
            	    // Java.g:388:9: 'abstract'
            	    {
            	    string_literal35=(Token)match(input,ABSTRACT,FOLLOW_ABSTRACT_in_modifiers693); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal35_tree = 
            	    (Object)adaptor.create(string_literal35)
            	    ;
            	    adaptor.addChild(root_0, string_literal35_tree);
            	    }

            	    }
            	    break;
            	case 7 :
            	    // Java.g:389:9: 'final'
            	    {
            	    string_literal36=(Token)match(input,FINAL,FOLLOW_FINAL_in_modifiers703); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal36_tree = 
            	    (Object)adaptor.create(string_literal36)
            	    ;
            	    adaptor.addChild(root_0, string_literal36_tree);
            	    }

            	    }
            	    break;
            	case 8 :
            	    // Java.g:390:9: 'native'
            	    {
            	    string_literal37=(Token)match(input,NATIVE,FOLLOW_NATIVE_in_modifiers713); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal37_tree = 
            	    (Object)adaptor.create(string_literal37)
            	    ;
            	    adaptor.addChild(root_0, string_literal37_tree);
            	    }

            	    }
            	    break;
            	case 9 :
            	    // Java.g:391:9: 'synchronized'
            	    {
            	    string_literal38=(Token)match(input,SYNCHRONIZED,FOLLOW_SYNCHRONIZED_in_modifiers723); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal38_tree = 
            	    (Object)adaptor.create(string_literal38)
            	    ;
            	    adaptor.addChild(root_0, string_literal38_tree);
            	    }

            	    }
            	    break;
            	case 10 :
            	    // Java.g:392:9: 'transient'
            	    {
            	    string_literal39=(Token)match(input,TRANSIENT,FOLLOW_TRANSIENT_in_modifiers733); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal39_tree = 
            	    (Object)adaptor.create(string_literal39)
            	    ;
            	    adaptor.addChild(root_0, string_literal39_tree);
            	    }

            	    }
            	    break;
            	case 11 :
            	    // Java.g:393:9: 'volatile'
            	    {
            	    string_literal40=(Token)match(input,VOLATILE,FOLLOW_VOLATILE_in_modifiers743); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal40_tree = 
            	    (Object)adaptor.create(string_literal40)
            	    ;
            	    adaptor.addChild(root_0, string_literal40_tree);
            	    }

            	    }
            	    break;
            	case 12 :
            	    // Java.g:394:9: 'strictfp'
            	    {
            	    string_literal41=(Token)match(input,STRICTFP,FOLLOW_STRICTFP_in_modifiers753); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal41_tree = 
            	    (Object)adaptor.create(string_literal41)
            	    ;
            	    adaptor.addChild(root_0, string_literal41_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 8, modifiers_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "modifiers"


    public static class variableModifiers_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "variableModifiers"
    // Java.g:399:1: variableModifiers : ( 'final' | annotation )* ;
    public final JavaParser.variableModifiers_return variableModifiers() throws RecognitionException {
        JavaParser.variableModifiers_return retval = new JavaParser.variableModifiers_return();
        retval.start = input.LT(1);

        int variableModifiers_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal42=null;
        JavaParser.annotation_return annotation43 =null;


        Object string_literal42_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return retval; }

            // Java.g:400:5: ( ( 'final' | annotation )* )
            // Java.g:400:9: ( 'final' | annotation )*
            {
            root_0 = (Object)adaptor.nil();


            // Java.g:400:9: ( 'final' | annotation )*
            loop15:
            do {
                int alt15=3;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==FINAL) ) {
                    alt15=1;
                }
                else if ( (LA15_0==136) ) {
                    alt15=2;
                }


                switch (alt15) {
            	case 1 :
            	    // Java.g:400:13: 'final'
            	    {
            	    string_literal42=(Token)match(input,FINAL,FOLLOW_FINAL_in_variableModifiers785); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal42_tree = 
            	    (Object)adaptor.create(string_literal42)
            	    ;
            	    adaptor.addChild(root_0, string_literal42_tree);
            	    }

            	    }
            	    break;
            	case 2 :
            	    // Java.g:401:13: annotation
            	    {
            	    pushFollow(FOLLOW_annotation_in_variableModifiers799);
            	    annotation43=annotation();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotation43.getTree());

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 9, variableModifiers_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "variableModifiers"


    public static class classDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "classDeclaration"
    // Java.g:406:1: classDeclaration : ( normalClassDeclaration | enumDeclaration );
    public final JavaParser.classDeclaration_return classDeclaration() throws RecognitionException {
        JavaParser.classDeclaration_return retval = new JavaParser.classDeclaration_return();
        retval.start = input.LT(1);

        int classDeclaration_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.normalClassDeclaration_return normalClassDeclaration44 =null;

        JavaParser.enumDeclaration_return enumDeclaration45 =null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 10) ) { return retval; }

            // Java.g:407:5: ( normalClassDeclaration | enumDeclaration )
            int alt16=2;
            switch ( input.LA(1) ) {
            case 136:
                {
                int LA16_1 = input.LA(2);

                if ( (synpred28_Java()) ) {
                    alt16=1;
                }
                else if ( (true) ) {
                    alt16=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    throw nvae;

                }
                }
                break;
            case PUBLIC:
                {
                int LA16_2 = input.LA(2);

                if ( (synpred28_Java()) ) {
                    alt16=1;
                }
                else if ( (true) ) {
                    alt16=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 2, input);

                    throw nvae;

                }
                }
                break;
            case PROTECTED:
                {
                int LA16_3 = input.LA(2);

                if ( (synpred28_Java()) ) {
                    alt16=1;
                }
                else if ( (true) ) {
                    alt16=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 3, input);

                    throw nvae;

                }
                }
                break;
            case PRIVATE:
                {
                int LA16_4 = input.LA(2);

                if ( (synpred28_Java()) ) {
                    alt16=1;
                }
                else if ( (true) ) {
                    alt16=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 4, input);

                    throw nvae;

                }
                }
                break;
            case STATIC:
                {
                int LA16_5 = input.LA(2);

                if ( (synpred28_Java()) ) {
                    alt16=1;
                }
                else if ( (true) ) {
                    alt16=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 5, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
                {
                int LA16_6 = input.LA(2);

                if ( (synpred28_Java()) ) {
                    alt16=1;
                }
                else if ( (true) ) {
                    alt16=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 6, input);

                    throw nvae;

                }
                }
                break;
            case FINAL:
                {
                int LA16_7 = input.LA(2);

                if ( (synpred28_Java()) ) {
                    alt16=1;
                }
                else if ( (true) ) {
                    alt16=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 7, input);

                    throw nvae;

                }
                }
                break;
            case NATIVE:
                {
                int LA16_8 = input.LA(2);

                if ( (synpred28_Java()) ) {
                    alt16=1;
                }
                else if ( (true) ) {
                    alt16=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 8, input);

                    throw nvae;

                }
                }
                break;
            case SYNCHRONIZED:
                {
                int LA16_9 = input.LA(2);

                if ( (synpred28_Java()) ) {
                    alt16=1;
                }
                else if ( (true) ) {
                    alt16=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 9, input);

                    throw nvae;

                }
                }
                break;
            case TRANSIENT:
                {
                int LA16_10 = input.LA(2);

                if ( (synpred28_Java()) ) {
                    alt16=1;
                }
                else if ( (true) ) {
                    alt16=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 10, input);

                    throw nvae;

                }
                }
                break;
            case VOLATILE:
                {
                int LA16_11 = input.LA(2);

                if ( (synpred28_Java()) ) {
                    alt16=1;
                }
                else if ( (true) ) {
                    alt16=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 11, input);

                    throw nvae;

                }
                }
                break;
            case STRICTFP:
                {
                int LA16_12 = input.LA(2);

                if ( (synpred28_Java()) ) {
                    alt16=1;
                }
                else if ( (true) ) {
                    alt16=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 12, input);

                    throw nvae;

                }
                }
                break;
            case CLASS:
                {
                alt16=1;
                }
                break;
            case ENUM:
                {
                alt16=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;

            }

            switch (alt16) {
                case 1 :
                    // Java.g:407:9: normalClassDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_normalClassDeclaration_in_classDeclaration835);
                    normalClassDeclaration44=normalClassDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, normalClassDeclaration44.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:408:9: enumDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_enumDeclaration_in_classDeclaration845);
                    enumDeclaration45=enumDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enumDeclaration45.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 10, classDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "classDeclaration"


    public static class normalClassDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "normalClassDeclaration"
    // Java.g:411:1: normalClassDeclaration : modifiers 'class' IDENTIFIER ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody -> ^( WNORMALCLASSDECLARATION ^( WCLASSHEAD ( modifiers )? 'class' IDENTIFIER ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? ) classBody ) ;
    public final JavaParser.normalClassDeclaration_return normalClassDeclaration() throws RecognitionException {
        JavaParser.normalClassDeclaration_return retval = new JavaParser.normalClassDeclaration_return();
        retval.start = input.LT(1);

        int normalClassDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal47=null;
        Token IDENTIFIER48=null;
        Token string_literal50=null;
        Token string_literal52=null;
        JavaParser.modifiers_return modifiers46 =null;

        JavaParser.typeParameters_return typeParameters49 =null;

        JavaParser.type_return type51 =null;

        JavaParser.typeList_return typeList53 =null;

        JavaParser.classBody_return classBody54 =null;


        Object string_literal47_tree=null;
        Object IDENTIFIER48_tree=null;
        Object string_literal50_tree=null;
        Object string_literal52_tree=null;
        RewriteRuleTokenStream stream_CLASS=new RewriteRuleTokenStream(adaptor,"token CLASS");
        RewriteRuleTokenStream stream_IMPLEMENTS=new RewriteRuleTokenStream(adaptor,"token IMPLEMENTS");
        RewriteRuleTokenStream stream_EXTENDS=new RewriteRuleTokenStream(adaptor,"token EXTENDS");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleSubtreeStream stream_typeParameters=new RewriteRuleSubtreeStream(adaptor,"rule typeParameters");
        RewriteRuleSubtreeStream stream_classBody=new RewriteRuleSubtreeStream(adaptor,"rule classBody");
        RewriteRuleSubtreeStream stream_modifiers=new RewriteRuleSubtreeStream(adaptor,"rule modifiers");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        RewriteRuleSubtreeStream stream_typeList=new RewriteRuleSubtreeStream(adaptor,"rule typeList");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 11) ) { return retval; }

            // Java.g:412:5: ( modifiers 'class' IDENTIFIER ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody -> ^( WNORMALCLASSDECLARATION ^( WCLASSHEAD ( modifiers )? 'class' IDENTIFIER ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? ) classBody ) )
            // Java.g:412:9: modifiers 'class' IDENTIFIER ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody
            {
            pushFollow(FOLLOW_modifiers_in_normalClassDeclaration865);
            modifiers46=modifiers();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_modifiers.add(modifiers46.getTree());

            string_literal47=(Token)match(input,CLASS,FOLLOW_CLASS_in_normalClassDeclaration868); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_CLASS.add(string_literal47);


            IDENTIFIER48=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_normalClassDeclaration870); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER48);


            // Java.g:413:9: ( typeParameters )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==LT) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // Java.g:413:10: typeParameters
                    {
                    pushFollow(FOLLOW_typeParameters_in_normalClassDeclaration881);
                    typeParameters49=typeParameters();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_typeParameters.add(typeParameters49.getTree());

                    }
                    break;

            }


            // Java.g:415:9: ( 'extends' type )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==EXTENDS) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // Java.g:415:10: 'extends' type
                    {
                    string_literal50=(Token)match(input,EXTENDS,FOLLOW_EXTENDS_in_normalClassDeclaration903); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_EXTENDS.add(string_literal50);


                    pushFollow(FOLLOW_type_in_normalClassDeclaration905);
                    type51=type();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_type.add(type51.getTree());

                    }
                    break;

            }


            // Java.g:417:9: ( 'implements' typeList )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==IMPLEMENTS) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // Java.g:417:10: 'implements' typeList
                    {
                    string_literal52=(Token)match(input,IMPLEMENTS,FOLLOW_IMPLEMENTS_in_normalClassDeclaration927); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IMPLEMENTS.add(string_literal52);


                    pushFollow(FOLLOW_typeList_in_normalClassDeclaration929);
                    typeList53=typeList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_typeList.add(typeList53.getTree());

                    }
                    break;

            }


            pushFollow(FOLLOW_classBody_in_normalClassDeclaration962);
            classBody54=classBody();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_classBody.add(classBody54.getTree());

            // AST REWRITE
            // elements: IDENTIFIER, typeParameters, type, classBody, CLASS, typeList, EXTENDS, IMPLEMENTS, modifiers
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 420:3: -> ^( WNORMALCLASSDECLARATION ^( WCLASSHEAD ( modifiers )? 'class' IDENTIFIER ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? ) classBody )
            {
                // Java.g:420:6: ^( WNORMALCLASSDECLARATION ^( WCLASSHEAD ( modifiers )? 'class' IDENTIFIER ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? ) classBody )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(WNORMALCLASSDECLARATION, "WNORMALCLASSDECLARATION")
                , root_1);

                // Java.g:420:32: ^( WCLASSHEAD ( modifiers )? 'class' IDENTIFIER ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? )
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(WCLASSHEAD, "WCLASSHEAD")
                , root_2);

                // Java.g:420:45: ( modifiers )?
                if ( stream_modifiers.hasNext() ) {
                    adaptor.addChild(root_2, stream_modifiers.nextTree());

                }
                stream_modifiers.reset();

                adaptor.addChild(root_2, 
                stream_CLASS.nextNode()
                );

                adaptor.addChild(root_2, 
                stream_IDENTIFIER.nextNode()
                );

                // Java.g:421:9: ( typeParameters )?
                if ( stream_typeParameters.hasNext() ) {
                    adaptor.addChild(root_2, stream_typeParameters.nextTree());

                }
                stream_typeParameters.reset();

                // Java.g:423:9: ( 'extends' type )?
                if ( stream_type.hasNext()||stream_EXTENDS.hasNext() ) {
                    adaptor.addChild(root_2, 
                    stream_EXTENDS.nextNode()
                    );

                    adaptor.addChild(root_2, stream_type.nextTree());

                }
                stream_type.reset();
                stream_EXTENDS.reset();

                // Java.g:425:9: ( 'implements' typeList )?
                if ( stream_typeList.hasNext()||stream_IMPLEMENTS.hasNext() ) {
                    adaptor.addChild(root_2, 
                    stream_IMPLEMENTS.nextNode()
                    );

                    adaptor.addChild(root_2, stream_typeList.nextTree());

                }
                stream_typeList.reset();
                stream_IMPLEMENTS.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_1, stream_classBody.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 11, normalClassDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "normalClassDeclaration"


    public static class typeParameters_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "typeParameters"
    // Java.g:431:1: typeParameters : '<' typeParameter ( ',' typeParameter )* '>' ;
    public final JavaParser.typeParameters_return typeParameters() throws RecognitionException {
        JavaParser.typeParameters_return retval = new JavaParser.typeParameters_return();
        retval.start = input.LT(1);

        int typeParameters_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal55=null;
        Token char_literal57=null;
        Token char_literal59=null;
        JavaParser.typeParameter_return typeParameter56 =null;

        JavaParser.typeParameter_return typeParameter58 =null;


        Object char_literal55_tree=null;
        Object char_literal57_tree=null;
        Object char_literal59_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 12) ) { return retval; }

            // Java.g:432:5: ( '<' typeParameter ( ',' typeParameter )* '>' )
            // Java.g:432:9: '<' typeParameter ( ',' typeParameter )* '>'
            {
            root_0 = (Object)adaptor.nil();


            char_literal55=(Token)match(input,LT,FOLLOW_LT_in_typeParameters1097); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal55_tree = 
            (Object)adaptor.create(char_literal55)
            ;
            adaptor.addChild(root_0, char_literal55_tree);
            }

            pushFollow(FOLLOW_typeParameter_in_typeParameters1111);
            typeParameter56=typeParameter();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, typeParameter56.getTree());

            // Java.g:434:13: ( ',' typeParameter )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==COMMA) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // Java.g:434:14: ',' typeParameter
            	    {
            	    char_literal57=(Token)match(input,COMMA,FOLLOW_COMMA_in_typeParameters1126); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal57_tree = 
            	    (Object)adaptor.create(char_literal57)
            	    ;
            	    adaptor.addChild(root_0, char_literal57_tree);
            	    }

            	    pushFollow(FOLLOW_typeParameter_in_typeParameters1128);
            	    typeParameter58=typeParameter();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeParameter58.getTree());

            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);


            char_literal59=(Token)match(input,GT,FOLLOW_GT_in_typeParameters1153); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal59_tree = 
            (Object)adaptor.create(char_literal59)
            ;
            adaptor.addChild(root_0, char_literal59_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 12, typeParameters_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeParameters"


    public static class typeParameter_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "typeParameter"
    // Java.g:439:1: typeParameter : IDENTIFIER ( 'extends' typeBound )? ;
    public final JavaParser.typeParameter_return typeParameter() throws RecognitionException {
        JavaParser.typeParameter_return retval = new JavaParser.typeParameter_return();
        retval.start = input.LT(1);

        int typeParameter_StartIndex = input.index();

        Object root_0 = null;

        Token IDENTIFIER60=null;
        Token string_literal61=null;
        JavaParser.typeBound_return typeBound62 =null;


        Object IDENTIFIER60_tree=null;
        Object string_literal61_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 13) ) { return retval; }

            // Java.g:440:5: ( IDENTIFIER ( 'extends' typeBound )? )
            // Java.g:440:9: IDENTIFIER ( 'extends' typeBound )?
            {
            root_0 = (Object)adaptor.nil();


            IDENTIFIER60=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_typeParameter1173); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER60_tree = 
            (Object)adaptor.create(IDENTIFIER60)
            ;
            adaptor.addChild(root_0, IDENTIFIER60_tree);
            }

            // Java.g:441:9: ( 'extends' typeBound )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==EXTENDS) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // Java.g:441:10: 'extends' typeBound
                    {
                    string_literal61=(Token)match(input,EXTENDS,FOLLOW_EXTENDS_in_typeParameter1184); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal61_tree = 
                    (Object)adaptor.create(string_literal61)
                    ;
                    adaptor.addChild(root_0, string_literal61_tree);
                    }

                    pushFollow(FOLLOW_typeBound_in_typeParameter1186);
                    typeBound62=typeBound();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeBound62.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 13, typeParameter_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeParameter"


    public static class typeBound_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "typeBound"
    // Java.g:446:1: typeBound : type ( '&' type )* ;
    public final JavaParser.typeBound_return typeBound() throws RecognitionException {
        JavaParser.typeBound_return retval = new JavaParser.typeBound_return();
        retval.start = input.LT(1);

        int typeBound_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal64=null;
        JavaParser.type_return type63 =null;

        JavaParser.type_return type65 =null;


        Object char_literal64_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 14) ) { return retval; }

            // Java.g:447:5: ( type ( '&' type )* )
            // Java.g:447:9: type ( '&' type )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_type_in_typeBound1218);
            type63=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type63.getTree());

            // Java.g:448:9: ( '&' type )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==AMP) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // Java.g:448:10: '&' type
            	    {
            	    char_literal64=(Token)match(input,AMP,FOLLOW_AMP_in_typeBound1229); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal64_tree = 
            	    (Object)adaptor.create(char_literal64)
            	    ;
            	    adaptor.addChild(root_0, char_literal64_tree);
            	    }

            	    pushFollow(FOLLOW_type_in_typeBound1231);
            	    type65=type();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, type65.getTree());

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 14, typeBound_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeBound"


    public static class enumDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "enumDeclaration"
    // Java.g:453:1: enumDeclaration : modifiers ( 'enum' ) IDENTIFIER ( 'implements' typeList )? enumBody -> ^( WENUMDECL ( modifiers )? 'enum' IDENTIFIER ( 'implements' typeList )? enumBody ) ;
    public final JavaParser.enumDeclaration_return enumDeclaration() throws RecognitionException {
        JavaParser.enumDeclaration_return retval = new JavaParser.enumDeclaration_return();
        retval.start = input.LT(1);

        int enumDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal67=null;
        Token IDENTIFIER68=null;
        Token string_literal69=null;
        JavaParser.modifiers_return modifiers66 =null;

        JavaParser.typeList_return typeList70 =null;

        JavaParser.enumBody_return enumBody71 =null;


        Object string_literal67_tree=null;
        Object IDENTIFIER68_tree=null;
        Object string_literal69_tree=null;
        RewriteRuleTokenStream stream_ENUM=new RewriteRuleTokenStream(adaptor,"token ENUM");
        RewriteRuleTokenStream stream_IMPLEMENTS=new RewriteRuleTokenStream(adaptor,"token IMPLEMENTS");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleSubtreeStream stream_enumBody=new RewriteRuleSubtreeStream(adaptor,"rule enumBody");
        RewriteRuleSubtreeStream stream_modifiers=new RewriteRuleSubtreeStream(adaptor,"rule modifiers");
        RewriteRuleSubtreeStream stream_typeList=new RewriteRuleSubtreeStream(adaptor,"rule typeList");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 15) ) { return retval; }

            // Java.g:454:5: ( modifiers ( 'enum' ) IDENTIFIER ( 'implements' typeList )? enumBody -> ^( WENUMDECL ( modifiers )? 'enum' IDENTIFIER ( 'implements' typeList )? enumBody ) )
            // Java.g:454:9: modifiers ( 'enum' ) IDENTIFIER ( 'implements' typeList )? enumBody
            {
            pushFollow(FOLLOW_modifiers_in_enumDeclaration1263);
            modifiers66=modifiers();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_modifiers.add(modifiers66.getTree());

            // Java.g:455:9: ( 'enum' )
            // Java.g:455:10: 'enum'
            {
            string_literal67=(Token)match(input,ENUM,FOLLOW_ENUM_in_enumDeclaration1275); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ENUM.add(string_literal67);


            }


            IDENTIFIER68=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_enumDeclaration1296); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER68);


            // Java.g:458:9: ( 'implements' typeList )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==IMPLEMENTS) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // Java.g:458:10: 'implements' typeList
                    {
                    string_literal69=(Token)match(input,IMPLEMENTS,FOLLOW_IMPLEMENTS_in_enumDeclaration1307); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IMPLEMENTS.add(string_literal69);


                    pushFollow(FOLLOW_typeList_in_enumDeclaration1309);
                    typeList70=typeList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_typeList.add(typeList70.getTree());

                    }
                    break;

            }


            pushFollow(FOLLOW_enumBody_in_enumDeclaration1330);
            enumBody71=enumBody();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_enumBody.add(enumBody71.getTree());

            // AST REWRITE
            // elements: typeList, modifiers, IDENTIFIER, enumBody, ENUM, IMPLEMENTS
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 461:3: -> ^( WENUMDECL ( modifiers )? 'enum' IDENTIFIER ( 'implements' typeList )? enumBody )
            {
                // Java.g:461:6: ^( WENUMDECL ( modifiers )? 'enum' IDENTIFIER ( 'implements' typeList )? enumBody )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(WENUMDECL, "WENUMDECL")
                , root_1);

                // Java.g:461:18: ( modifiers )?
                if ( stream_modifiers.hasNext() ) {
                    adaptor.addChild(root_1, stream_modifiers.nextTree());

                }
                stream_modifiers.reset();

                adaptor.addChild(root_1, 
                stream_ENUM.nextNode()
                );

                adaptor.addChild(root_1, 
                stream_IDENTIFIER.nextNode()
                );

                // Java.g:464:9: ( 'implements' typeList )?
                if ( stream_typeList.hasNext()||stream_IMPLEMENTS.hasNext() ) {
                    adaptor.addChild(root_1, 
                    stream_IMPLEMENTS.nextNode()
                    );

                    adaptor.addChild(root_1, stream_typeList.nextTree());

                }
                stream_typeList.reset();
                stream_IMPLEMENTS.reset();

                adaptor.addChild(root_1, stream_enumBody.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 15, enumDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "enumDeclaration"


    public static class enumBody_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "enumBody"
    // Java.g:470:1: enumBody : '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}' -> '{' ^( WCLASSCONTAINER ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? ) '}' ;
    public final JavaParser.enumBody_return enumBody() throws RecognitionException {
        JavaParser.enumBody_return retval = new JavaParser.enumBody_return();
        retval.start = input.LT(1);

        int enumBody_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal72=null;
        Token char_literal74=null;
        Token char_literal76=null;
        JavaParser.enumConstants_return enumConstants73 =null;

        JavaParser.enumBodyDeclarations_return enumBodyDeclarations75 =null;


        Object char_literal72_tree=null;
        Object char_literal74_tree=null;
        Object char_literal76_tree=null;
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleSubtreeStream stream_enumBodyDeclarations=new RewriteRuleSubtreeStream(adaptor,"rule enumBodyDeclarations");
        RewriteRuleSubtreeStream stream_enumConstants=new RewriteRuleSubtreeStream(adaptor,"rule enumConstants");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 16) ) { return retval; }

            // Java.g:471:5: ( '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}' -> '{' ^( WCLASSCONTAINER ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? ) '}' )
            // Java.g:471:9: '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}'
            {
            char_literal72=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_enumBody1424); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACE.add(char_literal72);


            // Java.g:472:9: ( enumConstants )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==IDENTIFIER||LA24_0==136) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // Java.g:472:10: enumConstants
                    {
                    pushFollow(FOLLOW_enumConstants_in_enumBody1435);
                    enumConstants73=enumConstants();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_enumConstants.add(enumConstants73.getTree());

                    }
                    break;

            }


            // Java.g:474:9: ( ',' )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==COMMA) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // Java.g:474:9: ','
                    {
                    char_literal74=(Token)match(input,COMMA,FOLLOW_COMMA_in_enumBody1457); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COMMA.add(char_literal74);


                    }
                    break;

            }


            // Java.g:475:9: ( enumBodyDeclarations )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==SEMI) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // Java.g:475:10: enumBodyDeclarations
                    {
                    pushFollow(FOLLOW_enumBodyDeclarations_in_enumBody1470);
                    enumBodyDeclarations75=enumBodyDeclarations();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_enumBodyDeclarations.add(enumBodyDeclarations75.getTree());

                    }
                    break;

            }


            char_literal76=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_enumBody1492); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACE.add(char_literal76);


            // AST REWRITE
            // elements: COMMA, enumBodyDeclarations, RBRACE, LBRACE, enumConstants
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 478:3: -> '{' ^( WCLASSCONTAINER ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? ) '}'
            {
                adaptor.addChild(root_0, 
                stream_LBRACE.nextNode()
                );

                // Java.g:478:10: ^( WCLASSCONTAINER ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(WCLASSCONTAINER, "WCLASSCONTAINER")
                , root_1);

                // Java.g:479:9: ( enumConstants )?
                if ( stream_enumConstants.hasNext() ) {
                    adaptor.addChild(root_1, stream_enumConstants.nextTree());

                }
                stream_enumConstants.reset();

                // Java.g:481:9: ( ',' )?
                if ( stream_COMMA.hasNext() ) {
                    adaptor.addChild(root_1, 
                    stream_COMMA.nextNode()
                    );

                }
                stream_COMMA.reset();

                // Java.g:482:9: ( enumBodyDeclarations )?
                if ( stream_enumBodyDeclarations.hasNext() ) {
                    adaptor.addChild(root_1, stream_enumBodyDeclarations.nextTree());

                }
                stream_enumBodyDeclarations.reset();

                adaptor.addChild(root_0, root_1);
                }

                adaptor.addChild(root_0, 
                stream_RBRACE.nextNode()
                );

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 16, enumBody_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "enumBody"


    public static class enumConstants_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "enumConstants"
    // Java.g:487:1: enumConstants : enumConstant ( ',' enumConstant )* ;
    public final JavaParser.enumConstants_return enumConstants() throws RecognitionException {
        JavaParser.enumConstants_return retval = new JavaParser.enumConstants_return();
        retval.start = input.LT(1);

        int enumConstants_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal78=null;
        JavaParser.enumConstant_return enumConstant77 =null;

        JavaParser.enumConstant_return enumConstant79 =null;


        Object char_literal78_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 17) ) { return retval; }

            // Java.g:488:5: ( enumConstant ( ',' enumConstant )* )
            // Java.g:488:9: enumConstant ( ',' enumConstant )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_enumConstant_in_enumConstants1591);
            enumConstant77=enumConstant();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, enumConstant77.getTree());

            // Java.g:489:9: ( ',' enumConstant )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==COMMA) ) {
                    int LA27_1 = input.LA(2);

                    if ( (LA27_1==IDENTIFIER||LA27_1==136) ) {
                        alt27=1;
                    }


                }


                switch (alt27) {
            	case 1 :
            	    // Java.g:489:10: ',' enumConstant
            	    {
            	    char_literal78=(Token)match(input,COMMA,FOLLOW_COMMA_in_enumConstants1602); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal78_tree = 
            	    (Object)adaptor.create(char_literal78)
            	    ;
            	    adaptor.addChild(root_0, char_literal78_tree);
            	    }

            	    pushFollow(FOLLOW_enumConstant_in_enumConstants1604);
            	    enumConstant79=enumConstant();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, enumConstant79.getTree());

            	    }
            	    break;

            	default :
            	    break loop27;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 17, enumConstants_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "enumConstants"


    public static class enumConstant_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "enumConstant"
    // Java.g:497:1: enumConstant : ( annotations )? IDENTIFIER ( arguments )? ( classBody )? ;
    public final JavaParser.enumConstant_return enumConstant() throws RecognitionException {
        JavaParser.enumConstant_return retval = new JavaParser.enumConstant_return();
        retval.start = input.LT(1);

        int enumConstant_StartIndex = input.index();

        Object root_0 = null;

        Token IDENTIFIER81=null;
        JavaParser.annotations_return annotations80 =null;

        JavaParser.arguments_return arguments82 =null;

        JavaParser.classBody_return classBody83 =null;


        Object IDENTIFIER81_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 18) ) { return retval; }

            // Java.g:498:5: ( ( annotations )? IDENTIFIER ( arguments )? ( classBody )? )
            // Java.g:498:9: ( annotations )? IDENTIFIER ( arguments )? ( classBody )?
            {
            root_0 = (Object)adaptor.nil();


            // Java.g:498:9: ( annotations )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==136) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // Java.g:498:10: annotations
                    {
                    pushFollow(FOLLOW_annotations_in_enumConstant1638);
                    annotations80=annotations();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotations80.getTree());

                    }
                    break;

            }


            IDENTIFIER81=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_enumConstant1659); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER81_tree = 
            (Object)adaptor.create(IDENTIFIER81)
            ;
            adaptor.addChild(root_0, IDENTIFIER81_tree);
            }

            // Java.g:501:9: ( arguments )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==LPAREN) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // Java.g:501:10: arguments
                    {
                    pushFollow(FOLLOW_arguments_in_enumConstant1670);
                    arguments82=arguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arguments82.getTree());

                    }
                    break;

            }


            // Java.g:503:9: ( classBody )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==LBRACE) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // Java.g:503:10: classBody
                    {
                    pushFollow(FOLLOW_classBody_in_enumConstant1692);
                    classBody83=classBody();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classBody83.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 18, enumConstant_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "enumConstant"


    public static class enumBodyDeclarations_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "enumBodyDeclarations"
    // Java.g:509:1: enumBodyDeclarations : ';' ( classBodyDeclaration )* ;
    public final JavaParser.enumBodyDeclarations_return enumBodyDeclarations() throws RecognitionException {
        JavaParser.enumBodyDeclarations_return retval = new JavaParser.enumBodyDeclarations_return();
        retval.start = input.LT(1);

        int enumBodyDeclarations_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal84=null;
        JavaParser.classBodyDeclaration_return classBodyDeclaration85 =null;


        Object char_literal84_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 19) ) { return retval; }

            // Java.g:510:5: ( ';' ( classBodyDeclaration )* )
            // Java.g:510:9: ';' ( classBodyDeclaration )*
            {
            root_0 = (Object)adaptor.nil();


            char_literal84=(Token)match(input,SEMI,FOLLOW_SEMI_in_enumBodyDeclarations1733); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal84_tree = 
            (Object)adaptor.create(char_literal84)
            ;
            adaptor.addChild(root_0, char_literal84_tree);
            }

            // Java.g:511:9: ( classBodyDeclaration )*
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( (LA31_0==ABSTRACT||LA31_0==BOOLEAN||LA31_0==BYTE||LA31_0==CHAR||LA31_0==CLASS||LA31_0==DOUBLE||LA31_0==ENUM||LA31_0==FINAL||LA31_0==FLOAT||LA31_0==IDENTIFIER||(LA31_0 >= INT && LA31_0 <= INTERFACE)||LA31_0==LBRACE||LA31_0==LONG||LA31_0==LT||LA31_0==NATIVE||(LA31_0 >= PRIVATE && LA31_0 <= PUBLIC)||(LA31_0 >= SEMI && LA31_0 <= SHORT)||(LA31_0 >= STATIC && LA31_0 <= STRICTFP)||LA31_0==SYNCHRONIZED||LA31_0==TRANSIENT||(LA31_0 >= VOID && LA31_0 <= VOLATILE)||LA31_0==136) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // Java.g:511:10: classBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_classBodyDeclaration_in_enumBodyDeclarations1745);
            	    classBodyDeclaration85=classBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, classBodyDeclaration85.getTree());

            	    }
            	    break;

            	default :
            	    break loop31;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 19, enumBodyDeclarations_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "enumBodyDeclarations"


    public static class interfaceDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "interfaceDeclaration"
    // Java.g:515:1: interfaceDeclaration : ( normalInterfaceDeclaration | annotationTypeDeclaration );
    public final JavaParser.interfaceDeclaration_return interfaceDeclaration() throws RecognitionException {
        JavaParser.interfaceDeclaration_return retval = new JavaParser.interfaceDeclaration_return();
        retval.start = input.LT(1);

        int interfaceDeclaration_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.normalInterfaceDeclaration_return normalInterfaceDeclaration86 =null;

        JavaParser.annotationTypeDeclaration_return annotationTypeDeclaration87 =null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 20) ) { return retval; }

            // Java.g:516:5: ( normalInterfaceDeclaration | annotationTypeDeclaration )
            int alt32=2;
            switch ( input.LA(1) ) {
            case 136:
                {
                int LA32_1 = input.LA(2);

                if ( (synpred44_Java()) ) {
                    alt32=1;
                }
                else if ( (true) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 1, input);

                    throw nvae;

                }
                }
                break;
            case PUBLIC:
                {
                int LA32_2 = input.LA(2);

                if ( (synpred44_Java()) ) {
                    alt32=1;
                }
                else if ( (true) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 2, input);

                    throw nvae;

                }
                }
                break;
            case PROTECTED:
                {
                int LA32_3 = input.LA(2);

                if ( (synpred44_Java()) ) {
                    alt32=1;
                }
                else if ( (true) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 3, input);

                    throw nvae;

                }
                }
                break;
            case PRIVATE:
                {
                int LA32_4 = input.LA(2);

                if ( (synpred44_Java()) ) {
                    alt32=1;
                }
                else if ( (true) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 4, input);

                    throw nvae;

                }
                }
                break;
            case STATIC:
                {
                int LA32_5 = input.LA(2);

                if ( (synpred44_Java()) ) {
                    alt32=1;
                }
                else if ( (true) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 5, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
                {
                int LA32_6 = input.LA(2);

                if ( (synpred44_Java()) ) {
                    alt32=1;
                }
                else if ( (true) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 6, input);

                    throw nvae;

                }
                }
                break;
            case FINAL:
                {
                int LA32_7 = input.LA(2);

                if ( (synpred44_Java()) ) {
                    alt32=1;
                }
                else if ( (true) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 7, input);

                    throw nvae;

                }
                }
                break;
            case NATIVE:
                {
                int LA32_8 = input.LA(2);

                if ( (synpred44_Java()) ) {
                    alt32=1;
                }
                else if ( (true) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 8, input);

                    throw nvae;

                }
                }
                break;
            case SYNCHRONIZED:
                {
                int LA32_9 = input.LA(2);

                if ( (synpred44_Java()) ) {
                    alt32=1;
                }
                else if ( (true) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 9, input);

                    throw nvae;

                }
                }
                break;
            case TRANSIENT:
                {
                int LA32_10 = input.LA(2);

                if ( (synpred44_Java()) ) {
                    alt32=1;
                }
                else if ( (true) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 10, input);

                    throw nvae;

                }
                }
                break;
            case VOLATILE:
                {
                int LA32_11 = input.LA(2);

                if ( (synpred44_Java()) ) {
                    alt32=1;
                }
                else if ( (true) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 11, input);

                    throw nvae;

                }
                }
                break;
            case STRICTFP:
                {
                int LA32_12 = input.LA(2);

                if ( (synpred44_Java()) ) {
                    alt32=1;
                }
                else if ( (true) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 12, input);

                    throw nvae;

                }
                }
                break;
            case INTERFACE:
                {
                alt32=1;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;

            }

            switch (alt32) {
                case 1 :
                    // Java.g:516:9: normalInterfaceDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration1776);
                    normalInterfaceDeclaration86=normalInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, normalInterfaceDeclaration86.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:517:9: annotationTypeDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration1786);
                    annotationTypeDeclaration87=annotationTypeDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotationTypeDeclaration87.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 20, interfaceDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "interfaceDeclaration"


    public static class normalInterfaceDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "normalInterfaceDeclaration"
    // Java.g:520:1: normalInterfaceDeclaration : modifiers 'interface' IDENTIFIER ( typeParameters )? ( 'extends' typeList )? interfaceBody ;
    public final JavaParser.normalInterfaceDeclaration_return normalInterfaceDeclaration() throws RecognitionException {
        JavaParser.normalInterfaceDeclaration_return retval = new JavaParser.normalInterfaceDeclaration_return();
        retval.start = input.LT(1);

        int normalInterfaceDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal89=null;
        Token IDENTIFIER90=null;
        Token string_literal92=null;
        JavaParser.modifiers_return modifiers88 =null;

        JavaParser.typeParameters_return typeParameters91 =null;

        JavaParser.typeList_return typeList93 =null;

        JavaParser.interfaceBody_return interfaceBody94 =null;


        Object string_literal89_tree=null;
        Object IDENTIFIER90_tree=null;
        Object string_literal92_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 21) ) { return retval; }

            // Java.g:521:5: ( modifiers 'interface' IDENTIFIER ( typeParameters )? ( 'extends' typeList )? interfaceBody )
            // Java.g:521:9: modifiers 'interface' IDENTIFIER ( typeParameters )? ( 'extends' typeList )? interfaceBody
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_modifiers_in_normalInterfaceDeclaration1810);
            modifiers88=modifiers();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, modifiers88.getTree());

            string_literal89=(Token)match(input,INTERFACE,FOLLOW_INTERFACE_in_normalInterfaceDeclaration1812); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal89_tree = 
            (Object)adaptor.create(string_literal89)
            ;
            adaptor.addChild(root_0, string_literal89_tree);
            }

            IDENTIFIER90=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_normalInterfaceDeclaration1814); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER90_tree = 
            (Object)adaptor.create(IDENTIFIER90)
            ;
            adaptor.addChild(root_0, IDENTIFIER90_tree);
            }

            // Java.g:522:9: ( typeParameters )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==LT) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // Java.g:522:10: typeParameters
                    {
                    pushFollow(FOLLOW_typeParameters_in_normalInterfaceDeclaration1825);
                    typeParameters91=typeParameters();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeParameters91.getTree());

                    }
                    break;

            }


            // Java.g:524:9: ( 'extends' typeList )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==EXTENDS) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // Java.g:524:10: 'extends' typeList
                    {
                    string_literal92=(Token)match(input,EXTENDS,FOLLOW_EXTENDS_in_normalInterfaceDeclaration1847); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal92_tree = 
                    (Object)adaptor.create(string_literal92)
                    ;
                    adaptor.addChild(root_0, string_literal92_tree);
                    }

                    pushFollow(FOLLOW_typeList_in_normalInterfaceDeclaration1849);
                    typeList93=typeList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeList93.getTree());

                    }
                    break;

            }


            pushFollow(FOLLOW_interfaceBody_in_normalInterfaceDeclaration1870);
            interfaceBody94=interfaceBody();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, interfaceBody94.getTree());

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 21, normalInterfaceDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "normalInterfaceDeclaration"


    public static class typeList_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "typeList"
    // Java.g:529:1: typeList : type ( ',' type )* ;
    public final JavaParser.typeList_return typeList() throws RecognitionException {
        JavaParser.typeList_return retval = new JavaParser.typeList_return();
        retval.start = input.LT(1);

        int typeList_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal96=null;
        JavaParser.type_return type95 =null;

        JavaParser.type_return type97 =null;


        Object char_literal96_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 22) ) { return retval; }

            // Java.g:530:5: ( type ( ',' type )* )
            // Java.g:530:9: type ( ',' type )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_type_in_typeList1890);
            type95=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type95.getTree());

            // Java.g:531:9: ( ',' type )*
            loop35:
            do {
                int alt35=2;
                int LA35_0 = input.LA(1);

                if ( (LA35_0==COMMA) ) {
                    alt35=1;
                }


                switch (alt35) {
            	case 1 :
            	    // Java.g:531:10: ',' type
            	    {
            	    char_literal96=(Token)match(input,COMMA,FOLLOW_COMMA_in_typeList1901); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal96_tree = 
            	    (Object)adaptor.create(char_literal96)
            	    ;
            	    adaptor.addChild(root_0, char_literal96_tree);
            	    }

            	    pushFollow(FOLLOW_type_in_typeList1903);
            	    type97=type();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, type97.getTree());

            	    }
            	    break;

            	default :
            	    break loop35;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 22, typeList_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeList"


    public static class classBody_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "classBody"
    // Java.g:535:1: classBody : '{' ( classBodyDeclaration )* '}' -> '{' ^( WCLASSCONTAINER ( classBodyDeclaration )* ) '}' ;
    public final JavaParser.classBody_return classBody() throws RecognitionException {
        JavaParser.classBody_return retval = new JavaParser.classBody_return();
        retval.start = input.LT(1);

        int classBody_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal98=null;
        Token char_literal100=null;
        JavaParser.classBodyDeclaration_return classBodyDeclaration99 =null;


        Object char_literal98_tree=null;
        Object char_literal100_tree=null;
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleSubtreeStream stream_classBodyDeclaration=new RewriteRuleSubtreeStream(adaptor,"rule classBodyDeclaration");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 23) ) { return retval; }

            // Java.g:536:5: ( '{' ( classBodyDeclaration )* '}' -> '{' ^( WCLASSCONTAINER ( classBodyDeclaration )* ) '}' )
            // Java.g:536:9: '{' ( classBodyDeclaration )* '}'
            {
            char_literal98=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_classBody1934); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACE.add(char_literal98);


            // Java.g:537:9: ( classBodyDeclaration )*
            loop36:
            do {
                int alt36=2;
                int LA36_0 = input.LA(1);

                if ( (LA36_0==ABSTRACT||LA36_0==BOOLEAN||LA36_0==BYTE||LA36_0==CHAR||LA36_0==CLASS||LA36_0==DOUBLE||LA36_0==ENUM||LA36_0==FINAL||LA36_0==FLOAT||LA36_0==IDENTIFIER||(LA36_0 >= INT && LA36_0 <= INTERFACE)||LA36_0==LBRACE||LA36_0==LONG||LA36_0==LT||LA36_0==NATIVE||(LA36_0 >= PRIVATE && LA36_0 <= PUBLIC)||(LA36_0 >= SEMI && LA36_0 <= SHORT)||(LA36_0 >= STATIC && LA36_0 <= STRICTFP)||LA36_0==SYNCHRONIZED||LA36_0==TRANSIENT||(LA36_0 >= VOID && LA36_0 <= VOLATILE)||LA36_0==136) ) {
                    alt36=1;
                }


                switch (alt36) {
            	case 1 :
            	    // Java.g:537:10: classBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_classBodyDeclaration_in_classBody1946);
            	    classBodyDeclaration99=classBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_classBodyDeclaration.add(classBodyDeclaration99.getTree());

            	    }
            	    break;

            	default :
            	    break loop36;
                }
            } while (true);


            char_literal100=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_classBody1968); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACE.add(char_literal100);


            // AST REWRITE
            // elements: LBRACE, RBRACE, classBodyDeclaration
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 540:3: -> '{' ^( WCLASSCONTAINER ( classBodyDeclaration )* ) '}'
            {
                adaptor.addChild(root_0, 
                stream_LBRACE.nextNode()
                );

                // Java.g:540:10: ^( WCLASSCONTAINER ( classBodyDeclaration )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(WCLASSCONTAINER, "WCLASSCONTAINER")
                , root_1);

                // Java.g:541:9: ( classBodyDeclaration )*
                while ( stream_classBodyDeclaration.hasNext() ) {
                    adaptor.addChild(root_1, stream_classBodyDeclaration.nextTree());

                }
                stream_classBodyDeclaration.reset();

                adaptor.addChild(root_0, root_1);
                }

                adaptor.addChild(root_0, 
                stream_RBRACE.nextNode()
                );

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 23, classBody_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "classBody"


    public static class interfaceBody_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "interfaceBody"
    // Java.g:546:1: interfaceBody : '{' ( interfaceBodyDeclaration )* '}' -> '{' ^( WCLASSCONTAINER ( interfaceBodyDeclaration )* ) '}' ;
    public final JavaParser.interfaceBody_return interfaceBody() throws RecognitionException {
        JavaParser.interfaceBody_return retval = new JavaParser.interfaceBody_return();
        retval.start = input.LT(1);

        int interfaceBody_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal101=null;
        Token char_literal103=null;
        JavaParser.interfaceBodyDeclaration_return interfaceBodyDeclaration102 =null;


        Object char_literal101_tree=null;
        Object char_literal103_tree=null;
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleSubtreeStream stream_interfaceBodyDeclaration=new RewriteRuleSubtreeStream(adaptor,"rule interfaceBodyDeclaration");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 24) ) { return retval; }

            // Java.g:547:5: ( '{' ( interfaceBodyDeclaration )* '}' -> '{' ^( WCLASSCONTAINER ( interfaceBodyDeclaration )* ) '}' )
            // Java.g:547:9: '{' ( interfaceBodyDeclaration )* '}'
            {
            char_literal101=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_interfaceBody2032); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACE.add(char_literal101);


            // Java.g:548:9: ( interfaceBodyDeclaration )*
            loop37:
            do {
                int alt37=2;
                int LA37_0 = input.LA(1);

                if ( (LA37_0==ABSTRACT||LA37_0==BOOLEAN||LA37_0==BYTE||LA37_0==CHAR||LA37_0==CLASS||LA37_0==DOUBLE||LA37_0==ENUM||LA37_0==FINAL||LA37_0==FLOAT||LA37_0==IDENTIFIER||(LA37_0 >= INT && LA37_0 <= INTERFACE)||LA37_0==LONG||LA37_0==LT||LA37_0==NATIVE||(LA37_0 >= PRIVATE && LA37_0 <= PUBLIC)||(LA37_0 >= SEMI && LA37_0 <= SHORT)||(LA37_0 >= STATIC && LA37_0 <= STRICTFP)||LA37_0==SYNCHRONIZED||LA37_0==TRANSIENT||(LA37_0 >= VOID && LA37_0 <= VOLATILE)||LA37_0==136) ) {
                    alt37=1;
                }


                switch (alt37) {
            	case 1 :
            	    // Java.g:548:10: interfaceBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_interfaceBodyDeclaration_in_interfaceBody2044);
            	    interfaceBodyDeclaration102=interfaceBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_interfaceBodyDeclaration.add(interfaceBodyDeclaration102.getTree());

            	    }
            	    break;

            	default :
            	    break loop37;
                }
            } while (true);


            char_literal103=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_interfaceBody2066); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACE.add(char_literal103);


            // AST REWRITE
            // elements: RBRACE, interfaceBodyDeclaration, LBRACE
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 551:3: -> '{' ^( WCLASSCONTAINER ( interfaceBodyDeclaration )* ) '}'
            {
                adaptor.addChild(root_0, 
                stream_LBRACE.nextNode()
                );

                // Java.g:551:10: ^( WCLASSCONTAINER ( interfaceBodyDeclaration )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(WCLASSCONTAINER, "WCLASSCONTAINER")
                , root_1);

                // Java.g:552:9: ( interfaceBodyDeclaration )*
                while ( stream_interfaceBodyDeclaration.hasNext() ) {
                    adaptor.addChild(root_1, stream_interfaceBodyDeclaration.nextTree());

                }
                stream_interfaceBodyDeclaration.reset();

                adaptor.addChild(root_0, root_1);
                }

                adaptor.addChild(root_0, 
                stream_RBRACE.nextNode()
                );

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 24, interfaceBody_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "interfaceBody"


    public static class classBodyDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "classBodyDeclaration"
    // Java.g:557:1: classBodyDeclaration : ( ';' | ( 'static' )? block | memberDecl );
    public final JavaParser.classBodyDeclaration_return classBodyDeclaration() throws RecognitionException {
        JavaParser.classBodyDeclaration_return retval = new JavaParser.classBodyDeclaration_return();
        retval.start = input.LT(1);

        int classBodyDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal104=null;
        Token string_literal105=null;
        JavaParser.block_return block106 =null;

        JavaParser.memberDecl_return memberDecl107 =null;


        Object char_literal104_tree=null;
        Object string_literal105_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 25) ) { return retval; }

            // Java.g:558:5: ( ';' | ( 'static' )? block | memberDecl )
            int alt39=3;
            switch ( input.LA(1) ) {
            case SEMI:
                {
                alt39=1;
                }
                break;
            case STATIC:
                {
                int LA39_2 = input.LA(2);

                if ( (LA39_2==LBRACE) ) {
                    alt39=2;
                }
                else if ( (LA39_2==ABSTRACT||LA39_2==BOOLEAN||LA39_2==BYTE||LA39_2==CHAR||LA39_2==CLASS||LA39_2==DOUBLE||LA39_2==ENUM||LA39_2==FINAL||LA39_2==FLOAT||LA39_2==IDENTIFIER||(LA39_2 >= INT && LA39_2 <= INTERFACE)||LA39_2==LONG||LA39_2==LT||LA39_2==NATIVE||(LA39_2 >= PRIVATE && LA39_2 <= PUBLIC)||LA39_2==SHORT||(LA39_2 >= STATIC && LA39_2 <= STRICTFP)||LA39_2==SYNCHRONIZED||LA39_2==TRANSIENT||(LA39_2 >= VOID && LA39_2 <= VOLATILE)||LA39_2==136) ) {
                    alt39=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 2, input);

                    throw nvae;

                }
                }
                break;
            case LBRACE:
                {
                alt39=2;
                }
                break;
            case ABSTRACT:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CLASS:
            case DOUBLE:
            case ENUM:
            case FINAL:
            case FLOAT:
            case IDENTIFIER:
            case INT:
            case INTERFACE:
            case LONG:
            case LT:
            case NATIVE:
            case PRIVATE:
            case PROTECTED:
            case PUBLIC:
            case SHORT:
            case STRICTFP:
            case SYNCHRONIZED:
            case TRANSIENT:
            case VOID:
            case VOLATILE:
            case 136:
                {
                alt39=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;

            }

            switch (alt39) {
                case 1 :
                    // Java.g:558:9: ';'
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal104=(Token)match(input,SEMI,FOLLOW_SEMI_in_classBodyDeclaration2130); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal104_tree = 
                    (Object)adaptor.create(char_literal104)
                    ;
                    adaptor.addChild(root_0, char_literal104_tree);
                    }

                    }
                    break;
                case 2 :
                    // Java.g:559:9: ( 'static' )? block
                    {
                    root_0 = (Object)adaptor.nil();


                    // Java.g:559:9: ( 'static' )?
                    int alt38=2;
                    int LA38_0 = input.LA(1);

                    if ( (LA38_0==STATIC) ) {
                        alt38=1;
                    }
                    switch (alt38) {
                        case 1 :
                            // Java.g:559:10: 'static'
                            {
                            string_literal105=(Token)match(input,STATIC,FOLLOW_STATIC_in_classBodyDeclaration2141); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            string_literal105_tree = 
                            (Object)adaptor.create(string_literal105)
                            ;
                            adaptor.addChild(root_0, string_literal105_tree);
                            }

                            }
                            break;

                    }


                    pushFollow(FOLLOW_block_in_classBodyDeclaration2163);
                    block106=block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, block106.getTree());

                    }
                    break;
                case 3 :
                    // Java.g:562:9: memberDecl
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_memberDecl_in_classBodyDeclaration2173);
                    memberDecl107=memberDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, memberDecl107.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 25, classBodyDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "classBodyDeclaration"


    public static class memberDecl_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "memberDecl"
    // Java.g:565:1: memberDecl : ( fieldDeclaration -> ^( WFIELDDECLARATION fieldDeclaration ) | methodDeclaration -> ^( WMETHODDECLARATION methodDeclaration ) | classDeclaration | interfaceDeclaration );
    public final JavaParser.memberDecl_return memberDecl() throws RecognitionException {
        JavaParser.memberDecl_return retval = new JavaParser.memberDecl_return();
        retval.start = input.LT(1);

        int memberDecl_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.fieldDeclaration_return fieldDeclaration108 =null;

        JavaParser.methodDeclaration_return methodDeclaration109 =null;

        JavaParser.classDeclaration_return classDeclaration110 =null;

        JavaParser.interfaceDeclaration_return interfaceDeclaration111 =null;


        RewriteRuleSubtreeStream stream_fieldDeclaration=new RewriteRuleSubtreeStream(adaptor,"rule fieldDeclaration");
        RewriteRuleSubtreeStream stream_methodDeclaration=new RewriteRuleSubtreeStream(adaptor,"rule methodDeclaration");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 26) ) { return retval; }

            // Java.g:566:5: ( fieldDeclaration -> ^( WFIELDDECLARATION fieldDeclaration ) | methodDeclaration -> ^( WMETHODDECLARATION methodDeclaration ) | classDeclaration | interfaceDeclaration )
            int alt40=4;
            switch ( input.LA(1) ) {
            case 136:
                {
                int LA40_1 = input.LA(2);

                if ( (synpred53_Java()) ) {
                    alt40=1;
                }
                else if ( (synpred54_Java()) ) {
                    alt40=2;
                }
                else if ( (synpred55_Java()) ) {
                    alt40=3;
                }
                else if ( (true) ) {
                    alt40=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 1, input);

                    throw nvae;

                }
                }
                break;
            case PUBLIC:
                {
                int LA40_2 = input.LA(2);

                if ( (synpred53_Java()) ) {
                    alt40=1;
                }
                else if ( (synpred54_Java()) ) {
                    alt40=2;
                }
                else if ( (synpred55_Java()) ) {
                    alt40=3;
                }
                else if ( (true) ) {
                    alt40=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 2, input);

                    throw nvae;

                }
                }
                break;
            case PROTECTED:
                {
                int LA40_3 = input.LA(2);

                if ( (synpred53_Java()) ) {
                    alt40=1;
                }
                else if ( (synpred54_Java()) ) {
                    alt40=2;
                }
                else if ( (synpred55_Java()) ) {
                    alt40=3;
                }
                else if ( (true) ) {
                    alt40=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 3, input);

                    throw nvae;

                }
                }
                break;
            case PRIVATE:
                {
                int LA40_4 = input.LA(2);

                if ( (synpred53_Java()) ) {
                    alt40=1;
                }
                else if ( (synpred54_Java()) ) {
                    alt40=2;
                }
                else if ( (synpred55_Java()) ) {
                    alt40=3;
                }
                else if ( (true) ) {
                    alt40=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 4, input);

                    throw nvae;

                }
                }
                break;
            case STATIC:
                {
                int LA40_5 = input.LA(2);

                if ( (synpred53_Java()) ) {
                    alt40=1;
                }
                else if ( (synpred54_Java()) ) {
                    alt40=2;
                }
                else if ( (synpred55_Java()) ) {
                    alt40=3;
                }
                else if ( (true) ) {
                    alt40=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 5, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
                {
                int LA40_6 = input.LA(2);

                if ( (synpred53_Java()) ) {
                    alt40=1;
                }
                else if ( (synpred54_Java()) ) {
                    alt40=2;
                }
                else if ( (synpred55_Java()) ) {
                    alt40=3;
                }
                else if ( (true) ) {
                    alt40=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 6, input);

                    throw nvae;

                }
                }
                break;
            case FINAL:
                {
                int LA40_7 = input.LA(2);

                if ( (synpred53_Java()) ) {
                    alt40=1;
                }
                else if ( (synpred54_Java()) ) {
                    alt40=2;
                }
                else if ( (synpred55_Java()) ) {
                    alt40=3;
                }
                else if ( (true) ) {
                    alt40=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 7, input);

                    throw nvae;

                }
                }
                break;
            case NATIVE:
                {
                int LA40_8 = input.LA(2);

                if ( (synpred53_Java()) ) {
                    alt40=1;
                }
                else if ( (synpred54_Java()) ) {
                    alt40=2;
                }
                else if ( (synpred55_Java()) ) {
                    alt40=3;
                }
                else if ( (true) ) {
                    alt40=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 8, input);

                    throw nvae;

                }
                }
                break;
            case SYNCHRONIZED:
                {
                int LA40_9 = input.LA(2);

                if ( (synpred53_Java()) ) {
                    alt40=1;
                }
                else if ( (synpred54_Java()) ) {
                    alt40=2;
                }
                else if ( (synpred55_Java()) ) {
                    alt40=3;
                }
                else if ( (true) ) {
                    alt40=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 9, input);

                    throw nvae;

                }
                }
                break;
            case TRANSIENT:
                {
                int LA40_10 = input.LA(2);

                if ( (synpred53_Java()) ) {
                    alt40=1;
                }
                else if ( (synpred54_Java()) ) {
                    alt40=2;
                }
                else if ( (synpred55_Java()) ) {
                    alt40=3;
                }
                else if ( (true) ) {
                    alt40=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 10, input);

                    throw nvae;

                }
                }
                break;
            case VOLATILE:
                {
                int LA40_11 = input.LA(2);

                if ( (synpred53_Java()) ) {
                    alt40=1;
                }
                else if ( (synpred54_Java()) ) {
                    alt40=2;
                }
                else if ( (synpred55_Java()) ) {
                    alt40=3;
                }
                else if ( (true) ) {
                    alt40=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 11, input);

                    throw nvae;

                }
                }
                break;
            case STRICTFP:
                {
                int LA40_12 = input.LA(2);

                if ( (synpred53_Java()) ) {
                    alt40=1;
                }
                else if ( (synpred54_Java()) ) {
                    alt40=2;
                }
                else if ( (synpred55_Java()) ) {
                    alt40=3;
                }
                else if ( (true) ) {
                    alt40=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 12, input);

                    throw nvae;

                }
                }
                break;
            case IDENTIFIER:
                {
                int LA40_13 = input.LA(2);

                if ( (synpred53_Java()) ) {
                    alt40=1;
                }
                else if ( (synpred54_Java()) ) {
                    alt40=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 13, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
                {
                int LA40_14 = input.LA(2);

                if ( (synpred53_Java()) ) {
                    alt40=1;
                }
                else if ( (synpred54_Java()) ) {
                    alt40=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 14, input);

                    throw nvae;

                }
                }
                break;
            case LT:
            case VOID:
                {
                alt40=2;
                }
                break;
            case CLASS:
            case ENUM:
                {
                alt40=3;
                }
                break;
            case INTERFACE:
                {
                alt40=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 40, 0, input);

                throw nvae;

            }

            switch (alt40) {
                case 1 :
                    // Java.g:566:10: fieldDeclaration
                    {
                    pushFollow(FOLLOW_fieldDeclaration_in_memberDecl2194);
                    fieldDeclaration108=fieldDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_fieldDeclaration.add(fieldDeclaration108.getTree());

                    // AST REWRITE
                    // elements: fieldDeclaration
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 566:27: -> ^( WFIELDDECLARATION fieldDeclaration )
                    {
                        // Java.g:566:30: ^( WFIELDDECLARATION fieldDeclaration )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WFIELDDECLARATION, "WFIELDDECLARATION")
                        , root_1);

                        adaptor.addChild(root_1, stream_fieldDeclaration.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // Java.g:567:10: methodDeclaration
                    {
                    pushFollow(FOLLOW_methodDeclaration_in_memberDecl2213);
                    methodDeclaration109=methodDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_methodDeclaration.add(methodDeclaration109.getTree());

                    // AST REWRITE
                    // elements: methodDeclaration
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 567:28: -> ^( WMETHODDECLARATION methodDeclaration )
                    {
                        // Java.g:567:31: ^( WMETHODDECLARATION methodDeclaration )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WMETHODDECLARATION, "WMETHODDECLARATION")
                        , root_1);

                        adaptor.addChild(root_1, stream_methodDeclaration.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 3 :
                    // Java.g:568:10: classDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_classDeclaration_in_memberDecl2232);
                    classDeclaration110=classDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classDeclaration110.getTree());

                    }
                    break;
                case 4 :
                    // Java.g:569:10: interfaceDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_interfaceDeclaration_in_memberDecl2243);
                    interfaceDeclaration111=interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, interfaceDeclaration111.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 26, memberDecl_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "memberDecl"


    public static class methodDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "methodDeclaration"
    // Java.g:573:1: methodDeclaration : ( modifiers ( typeParameters )? IDENTIFIER formalParameters ( 'throws' qualifiedNameList )? '{' ( explicitConstructorInvocation )? ( blockStatement )* '}' -> ^( WMETHODHEAD ( modifiers )? ( typeParameters )? IDENTIFIER formalParameters ( 'throws' qualifiedNameList )? ) '{' ^( WMETHODCONTAINER ( ^( WCONSTRUCTORINVOCATION explicitConstructorInvocation ) )? ( blockStatement )* ) '}' | modifiers ( typeParameters )? ( type | 'void' ) IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( block | ';' ) -> ^( WMETHODHEAD ( modifiers )? ( typeParameters )? ( type )? ( 'void' )? IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ) ( block )? ( ';' )? );
    public final JavaParser.methodDeclaration_return methodDeclaration() throws RecognitionException {
        JavaParser.methodDeclaration_return retval = new JavaParser.methodDeclaration_return();
        retval.start = input.LT(1);

        int methodDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token IDENTIFIER114=null;
        Token string_literal116=null;
        Token char_literal118=null;
        Token char_literal121=null;
        Token string_literal125=null;
        Token IDENTIFIER126=null;
        Token char_literal128=null;
        Token char_literal129=null;
        Token string_literal130=null;
        Token char_literal133=null;
        JavaParser.modifiers_return modifiers112 =null;

        JavaParser.typeParameters_return typeParameters113 =null;

        JavaParser.formalParameters_return formalParameters115 =null;

        JavaParser.qualifiedNameList_return qualifiedNameList117 =null;

        JavaParser.explicitConstructorInvocation_return explicitConstructorInvocation119 =null;

        JavaParser.blockStatement_return blockStatement120 =null;

        JavaParser.modifiers_return modifiers122 =null;

        JavaParser.typeParameters_return typeParameters123 =null;

        JavaParser.type_return type124 =null;

        JavaParser.formalParameters_return formalParameters127 =null;

        JavaParser.qualifiedNameList_return qualifiedNameList131 =null;

        JavaParser.block_return block132 =null;


        Object IDENTIFIER114_tree=null;
        Object string_literal116_tree=null;
        Object char_literal118_tree=null;
        Object char_literal121_tree=null;
        Object string_literal125_tree=null;
        Object IDENTIFIER126_tree=null;
        Object char_literal128_tree=null;
        Object char_literal129_tree=null;
        Object string_literal130_tree=null;
        Object char_literal133_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_THROWS=new RewriteRuleTokenStream(adaptor,"token THROWS");
        RewriteRuleTokenStream stream_VOID=new RewriteRuleTokenStream(adaptor,"token VOID");
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleSubtreeStream stream_explicitConstructorInvocation=new RewriteRuleSubtreeStream(adaptor,"rule explicitConstructorInvocation");
        RewriteRuleSubtreeStream stream_typeParameters=new RewriteRuleSubtreeStream(adaptor,"rule typeParameters");
        RewriteRuleSubtreeStream stream_formalParameters=new RewriteRuleSubtreeStream(adaptor,"rule formalParameters");
        RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");
        RewriteRuleSubtreeStream stream_blockStatement=new RewriteRuleSubtreeStream(adaptor,"rule blockStatement");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        RewriteRuleSubtreeStream stream_modifiers=new RewriteRuleSubtreeStream(adaptor,"rule modifiers");
        RewriteRuleSubtreeStream stream_qualifiedNameList=new RewriteRuleSubtreeStream(adaptor,"rule qualifiedNameList");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 27) ) { return retval; }

            // Java.g:574:5: ( modifiers ( typeParameters )? IDENTIFIER formalParameters ( 'throws' qualifiedNameList )? '{' ( explicitConstructorInvocation )? ( blockStatement )* '}' -> ^( WMETHODHEAD ( modifiers )? ( typeParameters )? IDENTIFIER formalParameters ( 'throws' qualifiedNameList )? ) '{' ^( WMETHODCONTAINER ( ^( WCONSTRUCTORINVOCATION explicitConstructorInvocation ) )? ( blockStatement )* ) '}' | modifiers ( typeParameters )? ( type | 'void' ) IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( block | ';' ) -> ^( WMETHODHEAD ( modifiers )? ( typeParameters )? ( type )? ( 'void' )? IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ) ( block )? ( ';' )? )
            int alt50=2;
            switch ( input.LA(1) ) {
            case 136:
                {
                int LA50_1 = input.LA(2);

                if ( (synpred60_Java()) ) {
                    alt50=1;
                }
                else if ( (true) ) {
                    alt50=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 1, input);

                    throw nvae;

                }
                }
                break;
            case PUBLIC:
                {
                int LA50_2 = input.LA(2);

                if ( (synpred60_Java()) ) {
                    alt50=1;
                }
                else if ( (true) ) {
                    alt50=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 2, input);

                    throw nvae;

                }
                }
                break;
            case PROTECTED:
                {
                int LA50_3 = input.LA(2);

                if ( (synpred60_Java()) ) {
                    alt50=1;
                }
                else if ( (true) ) {
                    alt50=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 3, input);

                    throw nvae;

                }
                }
                break;
            case PRIVATE:
                {
                int LA50_4 = input.LA(2);

                if ( (synpred60_Java()) ) {
                    alt50=1;
                }
                else if ( (true) ) {
                    alt50=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 4, input);

                    throw nvae;

                }
                }
                break;
            case STATIC:
                {
                int LA50_5 = input.LA(2);

                if ( (synpred60_Java()) ) {
                    alt50=1;
                }
                else if ( (true) ) {
                    alt50=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 5, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
                {
                int LA50_6 = input.LA(2);

                if ( (synpred60_Java()) ) {
                    alt50=1;
                }
                else if ( (true) ) {
                    alt50=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 6, input);

                    throw nvae;

                }
                }
                break;
            case FINAL:
                {
                int LA50_7 = input.LA(2);

                if ( (synpred60_Java()) ) {
                    alt50=1;
                }
                else if ( (true) ) {
                    alt50=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 7, input);

                    throw nvae;

                }
                }
                break;
            case NATIVE:
                {
                int LA50_8 = input.LA(2);

                if ( (synpred60_Java()) ) {
                    alt50=1;
                }
                else if ( (true) ) {
                    alt50=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 8, input);

                    throw nvae;

                }
                }
                break;
            case SYNCHRONIZED:
                {
                int LA50_9 = input.LA(2);

                if ( (synpred60_Java()) ) {
                    alt50=1;
                }
                else if ( (true) ) {
                    alt50=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 9, input);

                    throw nvae;

                }
                }
                break;
            case TRANSIENT:
                {
                int LA50_10 = input.LA(2);

                if ( (synpred60_Java()) ) {
                    alt50=1;
                }
                else if ( (true) ) {
                    alt50=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 10, input);

                    throw nvae;

                }
                }
                break;
            case VOLATILE:
                {
                int LA50_11 = input.LA(2);

                if ( (synpred60_Java()) ) {
                    alt50=1;
                }
                else if ( (true) ) {
                    alt50=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 11, input);

                    throw nvae;

                }
                }
                break;
            case STRICTFP:
                {
                int LA50_12 = input.LA(2);

                if ( (synpred60_Java()) ) {
                    alt50=1;
                }
                else if ( (true) ) {
                    alt50=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 12, input);

                    throw nvae;

                }
                }
                break;
            case LT:
                {
                int LA50_13 = input.LA(2);

                if ( (synpred60_Java()) ) {
                    alt50=1;
                }
                else if ( (true) ) {
                    alt50=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 13, input);

                    throw nvae;

                }
                }
                break;
            case IDENTIFIER:
                {
                int LA50_14 = input.LA(2);

                if ( (synpred60_Java()) ) {
                    alt50=1;
                }
                else if ( (true) ) {
                    alt50=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 14, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
            case VOID:
                {
                alt50=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;

            }

            switch (alt50) {
                case 1 :
                    // Java.g:576:10: modifiers ( typeParameters )? IDENTIFIER formalParameters ( 'throws' qualifiedNameList )? '{' ( explicitConstructorInvocation )? ( blockStatement )* '}'
                    {
                    pushFollow(FOLLOW_modifiers_in_methodDeclaration2281);
                    modifiers112=modifiers();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_modifiers.add(modifiers112.getTree());

                    // Java.g:577:9: ( typeParameters )?
                    int alt41=2;
                    int LA41_0 = input.LA(1);

                    if ( (LA41_0==LT) ) {
                        alt41=1;
                    }
                    switch (alt41) {
                        case 1 :
                            // Java.g:577:10: typeParameters
                            {
                            pushFollow(FOLLOW_typeParameters_in_methodDeclaration2292);
                            typeParameters113=typeParameters();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_typeParameters.add(typeParameters113.getTree());

                            }
                            break;

                    }


                    IDENTIFIER114=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_methodDeclaration2313); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER114);


                    pushFollow(FOLLOW_formalParameters_in_methodDeclaration2323);
                    formalParameters115=formalParameters();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_formalParameters.add(formalParameters115.getTree());

                    // Java.g:581:9: ( 'throws' qualifiedNameList )?
                    int alt42=2;
                    int LA42_0 = input.LA(1);

                    if ( (LA42_0==THROWS) ) {
                        alt42=1;
                    }
                    switch (alt42) {
                        case 1 :
                            // Java.g:581:10: 'throws' qualifiedNameList
                            {
                            string_literal116=(Token)match(input,THROWS,FOLLOW_THROWS_in_methodDeclaration2334); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_THROWS.add(string_literal116);


                            pushFollow(FOLLOW_qualifiedNameList_in_methodDeclaration2336);
                            qualifiedNameList117=qualifiedNameList();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_qualifiedNameList.add(qualifiedNameList117.getTree());

                            }
                            break;

                    }


                    char_literal118=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_methodDeclaration2357); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACE.add(char_literal118);


                    // Java.g:584:9: ( explicitConstructorInvocation )?
                    int alt43=2;
                    switch ( input.LA(1) ) {
                        case LT:
                            {
                            alt43=1;
                            }
                            break;
                        case THIS:
                            {
                            int LA43_2 = input.LA(2);

                            if ( (synpred58_Java()) ) {
                                alt43=1;
                            }
                            }
                            break;
                        case LPAREN:
                            {
                            int LA43_3 = input.LA(2);

                            if ( (synpred58_Java()) ) {
                                alt43=1;
                            }
                            }
                            break;
                        case SUPER:
                            {
                            int LA43_4 = input.LA(2);

                            if ( (synpred58_Java()) ) {
                                alt43=1;
                            }
                            }
                            break;
                        case IDENTIFIER:
                            {
                            int LA43_5 = input.LA(2);

                            if ( (synpred58_Java()) ) {
                                alt43=1;
                            }
                            }
                            break;
                        case CHARLITERAL:
                        case DOUBLELITERAL:
                        case FALSE:
                        case FLOATLITERAL:
                        case INTLITERAL:
                        case LONGLITERAL:
                        case NULL:
                        case STRINGLITERAL:
                        case TRUE:
                            {
                            int LA43_6 = input.LA(2);

                            if ( (synpred58_Java()) ) {
                                alt43=1;
                            }
                            }
                            break;
                        case NEW:
                            {
                            int LA43_7 = input.LA(2);

                            if ( (synpred58_Java()) ) {
                                alt43=1;
                            }
                            }
                            break;
                        case BOOLEAN:
                        case BYTE:
                        case CHAR:
                        case DOUBLE:
                        case FLOAT:
                        case INT:
                        case LONG:
                        case SHORT:
                            {
                            int LA43_8 = input.LA(2);

                            if ( (synpred58_Java()) ) {
                                alt43=1;
                            }
                            }
                            break;
                        case VOID:
                            {
                            int LA43_9 = input.LA(2);

                            if ( (synpred58_Java()) ) {
                                alt43=1;
                            }
                            }
                            break;
                    }

                    switch (alt43) {
                        case 1 :
                            // Java.g:584:10: explicitConstructorInvocation
                            {
                            pushFollow(FOLLOW_explicitConstructorInvocation_in_methodDeclaration2369);
                            explicitConstructorInvocation119=explicitConstructorInvocation();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_explicitConstructorInvocation.add(explicitConstructorInvocation119.getTree());

                            }
                            break;

                    }


                    // Java.g:586:9: ( blockStatement )*
                    loop44:
                    do {
                        int alt44=2;
                        int LA44_0 = input.LA(1);

                        if ( (LA44_0==EOF||LA44_0==ABSTRACT||(LA44_0 >= ASSERT && LA44_0 <= BANG)||(LA44_0 >= BOOLEAN && LA44_0 <= BYTE)||(LA44_0 >= CHAR && LA44_0 <= CLASS)||LA44_0==CONTINUE||LA44_0==DO||(LA44_0 >= DOUBLE && LA44_0 <= DOUBLELITERAL)||LA44_0==ENUM||(LA44_0 >= FALSE && LA44_0 <= FINAL)||(LA44_0 >= FLOAT && LA44_0 <= FOR)||(LA44_0 >= IDENTIFIER && LA44_0 <= IF)||(LA44_0 >= INT && LA44_0 <= INTLITERAL)||LA44_0==LBRACE||(LA44_0 >= LONG && LA44_0 <= LT)||(LA44_0 >= NATIVE && LA44_0 <= NULL)||LA44_0==PLUS||(LA44_0 >= PLUSPLUS && LA44_0 <= PUBLIC)||LA44_0==RETURN||(LA44_0 >= SEMI && LA44_0 <= SHORT)||(LA44_0 >= STATIC && LA44_0 <= SUB)||(LA44_0 >= SUBSUB && LA44_0 <= SYNCHRONIZED)||(LA44_0 >= THIS && LA44_0 <= THROW)||(LA44_0 >= TILDE && LA44_0 <= VOLATILE)||LA44_0==WHILE||LA44_0==136) ) {
                            alt44=1;
                        }


                        switch (alt44) {
                    	case 1 :
                    	    // Java.g:586:10: blockStatement
                    	    {
                    	    pushFollow(FOLLOW_blockStatement_in_methodDeclaration2391);
                    	    blockStatement120=blockStatement();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_blockStatement.add(blockStatement120.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop44;
                        }
                    } while (true);


                    char_literal121=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_methodDeclaration2412); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACE.add(char_literal121);


                    // AST REWRITE
                    // elements: modifiers, blockStatement, IDENTIFIER, LBRACE, typeParameters, qualifiedNameList, THROWS, RBRACE, explicitConstructorInvocation, formalParameters
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 589:3: -> ^( WMETHODHEAD ( modifiers )? ( typeParameters )? IDENTIFIER formalParameters ( 'throws' qualifiedNameList )? ) '{' ^( WMETHODCONTAINER ( ^( WCONSTRUCTORINVOCATION explicitConstructorInvocation ) )? ( blockStatement )* ) '}'
                    {
                        // Java.g:589:6: ^( WMETHODHEAD ( modifiers )? ( typeParameters )? IDENTIFIER formalParameters ( 'throws' qualifiedNameList )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WMETHODHEAD, "WMETHODHEAD")
                        , root_1);

                        // Java.g:589:20: ( modifiers )?
                        if ( stream_modifiers.hasNext() ) {
                            adaptor.addChild(root_1, stream_modifiers.nextTree());

                        }
                        stream_modifiers.reset();

                        // Java.g:590:9: ( typeParameters )?
                        if ( stream_typeParameters.hasNext() ) {
                            adaptor.addChild(root_1, stream_typeParameters.nextTree());

                        }
                        stream_typeParameters.reset();

                        adaptor.addChild(root_1, 
                        stream_IDENTIFIER.nextNode()
                        );

                        adaptor.addChild(root_1, stream_formalParameters.nextTree());

                        // Java.g:594:9: ( 'throws' qualifiedNameList )?
                        if ( stream_qualifiedNameList.hasNext()||stream_THROWS.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_THROWS.nextNode()
                            );

                            adaptor.addChild(root_1, stream_qualifiedNameList.nextTree());

                        }
                        stream_qualifiedNameList.reset();
                        stream_THROWS.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                        adaptor.addChild(root_0, 
                        stream_LBRACE.nextNode()
                        );

                        // Java.g:596:13: ^( WMETHODCONTAINER ( ^( WCONSTRUCTORINVOCATION explicitConstructorInvocation ) )? ( blockStatement )* )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WMETHODCONTAINER, "WMETHODCONTAINER")
                        , root_1);

                        // Java.g:597:9: ( ^( WCONSTRUCTORINVOCATION explicitConstructorInvocation ) )?
                        if ( stream_explicitConstructorInvocation.hasNext() ) {
                            // Java.g:597:9: ^( WCONSTRUCTORINVOCATION explicitConstructorInvocation )
                            {
                            Object root_2 = (Object)adaptor.nil();
                            root_2 = (Object)adaptor.becomeRoot(
                            (Object)adaptor.create(WCONSTRUCTORINVOCATION, "WCONSTRUCTORINVOCATION")
                            , root_2);

                            adaptor.addChild(root_2, stream_explicitConstructorInvocation.nextTree());

                            adaptor.addChild(root_1, root_2);
                            }

                        }
                        stream_explicitConstructorInvocation.reset();

                        // Java.g:599:9: ( blockStatement )*
                        while ( stream_blockStatement.hasNext() ) {
                            adaptor.addChild(root_1, stream_blockStatement.nextTree());

                        }
                        stream_blockStatement.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                        adaptor.addChild(root_0, 
                        stream_RBRACE.nextNode()
                        );

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // Java.g:601:9: modifiers ( typeParameters )? ( type | 'void' ) IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( block | ';' )
                    {
                    pushFollow(FOLLOW_modifiers_in_methodDeclaration2564);
                    modifiers122=modifiers();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_modifiers.add(modifiers122.getTree());

                    // Java.g:602:9: ( typeParameters )?
                    int alt45=2;
                    int LA45_0 = input.LA(1);

                    if ( (LA45_0==LT) ) {
                        alt45=1;
                    }
                    switch (alt45) {
                        case 1 :
                            // Java.g:602:10: typeParameters
                            {
                            pushFollow(FOLLOW_typeParameters_in_methodDeclaration2575);
                            typeParameters123=typeParameters();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_typeParameters.add(typeParameters123.getTree());

                            }
                            break;

                    }


                    // Java.g:604:9: ( type | 'void' )
                    int alt46=2;
                    int LA46_0 = input.LA(1);

                    if ( (LA46_0==BOOLEAN||LA46_0==BYTE||LA46_0==CHAR||LA46_0==DOUBLE||LA46_0==FLOAT||LA46_0==IDENTIFIER||LA46_0==INT||LA46_0==LONG||LA46_0==SHORT) ) {
                        alt46=1;
                    }
                    else if ( (LA46_0==VOID) ) {
                        alt46=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 46, 0, input);

                        throw nvae;

                    }
                    switch (alt46) {
                        case 1 :
                            // Java.g:604:10: type
                            {
                            pushFollow(FOLLOW_type_in_methodDeclaration2597);
                            type124=type();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_type.add(type124.getTree());

                            }
                            break;
                        case 2 :
                            // Java.g:605:13: 'void'
                            {
                            string_literal125=(Token)match(input,VOID,FOLLOW_VOID_in_methodDeclaration2611); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_VOID.add(string_literal125);


                            }
                            break;

                    }


                    IDENTIFIER126=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_methodDeclaration2631); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER126);


                    pushFollow(FOLLOW_formalParameters_in_methodDeclaration2641);
                    formalParameters127=formalParameters();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_formalParameters.add(formalParameters127.getTree());

                    // Java.g:609:9: ( '[' ']' )*
                    loop47:
                    do {
                        int alt47=2;
                        int LA47_0 = input.LA(1);

                        if ( (LA47_0==LBRACKET) ) {
                            alt47=1;
                        }


                        switch (alt47) {
                    	case 1 :
                    	    // Java.g:609:10: '[' ']'
                    	    {
                    	    char_literal128=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_methodDeclaration2652); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_LBRACKET.add(char_literal128);


                    	    char_literal129=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_methodDeclaration2654); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_RBRACKET.add(char_literal129);


                    	    }
                    	    break;

                    	default :
                    	    break loop47;
                        }
                    } while (true);


                    // Java.g:611:9: ( 'throws' qualifiedNameList )?
                    int alt48=2;
                    int LA48_0 = input.LA(1);

                    if ( (LA48_0==THROWS) ) {
                        alt48=1;
                    }
                    switch (alt48) {
                        case 1 :
                            // Java.g:611:10: 'throws' qualifiedNameList
                            {
                            string_literal130=(Token)match(input,THROWS,FOLLOW_THROWS_in_methodDeclaration2676); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_THROWS.add(string_literal130);


                            pushFollow(FOLLOW_qualifiedNameList_in_methodDeclaration2678);
                            qualifiedNameList131=qualifiedNameList();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_qualifiedNameList.add(qualifiedNameList131.getTree());

                            }
                            break;

                    }


                    // Java.g:613:9: ( block | ';' )
                    int alt49=2;
                    int LA49_0 = input.LA(1);

                    if ( (LA49_0==LBRACE) ) {
                        alt49=1;
                    }
                    else if ( (LA49_0==SEMI) ) {
                        alt49=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 49, 0, input);

                        throw nvae;

                    }
                    switch (alt49) {
                        case 1 :
                            // Java.g:614:13: block
                            {
                            pushFollow(FOLLOW_block_in_methodDeclaration2733);
                            block132=block();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_block.add(block132.getTree());

                            }
                            break;
                        case 2 :
                            // Java.g:615:13: ';'
                            {
                            char_literal133=(Token)match(input,SEMI,FOLLOW_SEMI_in_methodDeclaration2747); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_SEMI.add(char_literal133);


                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: RBRACKET, LBRACKET, formalParameters, THROWS, VOID, IDENTIFIER, qualifiedNameList, SEMI, type, typeParameters, modifiers, block
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 617:3: -> ^( WMETHODHEAD ( modifiers )? ( typeParameters )? ( type )? ( 'void' )? IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ) ( block )? ( ';' )?
                    {
                        // Java.g:617:6: ^( WMETHODHEAD ( modifiers )? ( typeParameters )? ( type )? ( 'void' )? IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WMETHODHEAD, "WMETHODHEAD")
                        , root_1);

                        // Java.g:617:20: ( modifiers )?
                        if ( stream_modifiers.hasNext() ) {
                            adaptor.addChild(root_1, stream_modifiers.nextTree());

                        }
                        stream_modifiers.reset();

                        // Java.g:618:9: ( typeParameters )?
                        if ( stream_typeParameters.hasNext() ) {
                            adaptor.addChild(root_1, stream_typeParameters.nextTree());

                        }
                        stream_typeParameters.reset();

                        // Java.g:620:9: ( type )?
                        if ( stream_type.hasNext() ) {
                            adaptor.addChild(root_1, stream_type.nextTree());

                        }
                        stream_type.reset();

                        // Java.g:621:9: ( 'void' )?
                        if ( stream_VOID.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_VOID.nextNode()
                            );

                        }
                        stream_VOID.reset();

                        adaptor.addChild(root_1, 
                        stream_IDENTIFIER.nextNode()
                        );

                        adaptor.addChild(root_1, stream_formalParameters.nextTree());

                        // Java.g:624:9: ( '[' ']' )*
                        while ( stream_RBRACKET.hasNext()||stream_LBRACKET.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_LBRACKET.nextNode()
                            );

                            adaptor.addChild(root_1, 
                            stream_RBRACKET.nextNode()
                            );

                        }
                        stream_RBRACKET.reset();
                        stream_LBRACKET.reset();

                        // Java.g:626:9: ( 'throws' qualifiedNameList )?
                        if ( stream_THROWS.hasNext()||stream_qualifiedNameList.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_THROWS.nextNode()
                            );

                            adaptor.addChild(root_1, stream_qualifiedNameList.nextTree());

                        }
                        stream_THROWS.reset();
                        stream_qualifiedNameList.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                        // Java.g:628:9: ( block )?
                        if ( stream_block.hasNext() ) {
                            adaptor.addChild(root_0, stream_block.nextTree());

                        }
                        stream_block.reset();

                        // Java.g:629:9: ( ';' )?
                        if ( stream_SEMI.hasNext() ) {
                            adaptor.addChild(root_0, 
                            stream_SEMI.nextNode()
                            );

                        }
                        stream_SEMI.reset();

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 27, methodDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "methodDeclaration"


    public static class fieldDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "fieldDeclaration"
    // Java.g:633:1: fieldDeclaration : modifiers type variableDeclarator ( ',' variableDeclarator )* ';' ;
    public final JavaParser.fieldDeclaration_return fieldDeclaration() throws RecognitionException {
        JavaParser.fieldDeclaration_return retval = new JavaParser.fieldDeclaration_return();
        retval.start = input.LT(1);

        int fieldDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal137=null;
        Token char_literal139=null;
        JavaParser.modifiers_return modifiers134 =null;

        JavaParser.type_return type135 =null;

        JavaParser.variableDeclarator_return variableDeclarator136 =null;

        JavaParser.variableDeclarator_return variableDeclarator138 =null;


        Object char_literal137_tree=null;
        Object char_literal139_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 28) ) { return retval; }

            // Java.g:634:5: ( modifiers type variableDeclarator ( ',' variableDeclarator )* ';' )
            // Java.g:634:9: modifiers type variableDeclarator ( ',' variableDeclarator )* ';'
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_modifiers_in_fieldDeclaration2946);
            modifiers134=modifiers();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, modifiers134.getTree());

            pushFollow(FOLLOW_type_in_fieldDeclaration2956);
            type135=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type135.getTree());

            pushFollow(FOLLOW_variableDeclarator_in_fieldDeclaration2966);
            variableDeclarator136=variableDeclarator();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, variableDeclarator136.getTree());

            // Java.g:637:9: ( ',' variableDeclarator )*
            loop51:
            do {
                int alt51=2;
                int LA51_0 = input.LA(1);

                if ( (LA51_0==COMMA) ) {
                    alt51=1;
                }


                switch (alt51) {
            	case 1 :
            	    // Java.g:637:10: ',' variableDeclarator
            	    {
            	    char_literal137=(Token)match(input,COMMA,FOLLOW_COMMA_in_fieldDeclaration2977); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal137_tree = 
            	    (Object)adaptor.create(char_literal137)
            	    ;
            	    adaptor.addChild(root_0, char_literal137_tree);
            	    }

            	    pushFollow(FOLLOW_variableDeclarator_in_fieldDeclaration2979);
            	    variableDeclarator138=variableDeclarator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, variableDeclarator138.getTree());

            	    }
            	    break;

            	default :
            	    break loop51;
                }
            } while (true);


            char_literal139=(Token)match(input,SEMI,FOLLOW_SEMI_in_fieldDeclaration3000); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal139_tree = 
            (Object)adaptor.create(char_literal139)
            ;
            adaptor.addChild(root_0, char_literal139_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 28, fieldDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "fieldDeclaration"


    public static class variableDeclarator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "variableDeclarator"
    // Java.g:642:1: variableDeclarator : IDENTIFIER ( '[' ']' )* ( '=' variableInitializer )? ;
    public final JavaParser.variableDeclarator_return variableDeclarator() throws RecognitionException {
        JavaParser.variableDeclarator_return retval = new JavaParser.variableDeclarator_return();
        retval.start = input.LT(1);

        int variableDeclarator_StartIndex = input.index();

        Object root_0 = null;

        Token IDENTIFIER140=null;
        Token char_literal141=null;
        Token char_literal142=null;
        Token char_literal143=null;
        JavaParser.variableInitializer_return variableInitializer144 =null;


        Object IDENTIFIER140_tree=null;
        Object char_literal141_tree=null;
        Object char_literal142_tree=null;
        Object char_literal143_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 29) ) { return retval; }

            // Java.g:643:5: ( IDENTIFIER ( '[' ']' )* ( '=' variableInitializer )? )
            // Java.g:643:9: IDENTIFIER ( '[' ']' )* ( '=' variableInitializer )?
            {
            root_0 = (Object)adaptor.nil();


            IDENTIFIER140=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_variableDeclarator3020); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER140_tree = 
            (Object)adaptor.create(IDENTIFIER140)
            ;
            adaptor.addChild(root_0, IDENTIFIER140_tree);
            }

            // Java.g:644:9: ( '[' ']' )*
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( (LA52_0==LBRACKET) ) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // Java.g:644:10: '[' ']'
            	    {
            	    char_literal141=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_variableDeclarator3031); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal141_tree = 
            	    (Object)adaptor.create(char_literal141)
            	    ;
            	    adaptor.addChild(root_0, char_literal141_tree);
            	    }

            	    char_literal142=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_variableDeclarator3033); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal142_tree = 
            	    (Object)adaptor.create(char_literal142)
            	    ;
            	    adaptor.addChild(root_0, char_literal142_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop52;
                }
            } while (true);


            // Java.g:646:9: ( '=' variableInitializer )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==EQ) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // Java.g:646:10: '=' variableInitializer
                    {
                    char_literal143=(Token)match(input,EQ,FOLLOW_EQ_in_variableDeclarator3055); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal143_tree = 
                    (Object)adaptor.create(char_literal143)
                    ;
                    adaptor.addChild(root_0, char_literal143_tree);
                    }

                    pushFollow(FOLLOW_variableInitializer_in_variableDeclarator3057);
                    variableInitializer144=variableInitializer();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, variableInitializer144.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 29, variableDeclarator_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "variableDeclarator"


    public static class interfaceBodyDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "interfaceBodyDeclaration"
    // Java.g:653:1: interfaceBodyDeclaration : ( interfaceFieldDeclaration | interfaceMethodDeclaration | interfaceDeclaration | classDeclaration | ';' );
    public final JavaParser.interfaceBodyDeclaration_return interfaceBodyDeclaration() throws RecognitionException {
        JavaParser.interfaceBodyDeclaration_return retval = new JavaParser.interfaceBodyDeclaration_return();
        retval.start = input.LT(1);

        int interfaceBodyDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal149=null;
        JavaParser.interfaceFieldDeclaration_return interfaceFieldDeclaration145 =null;

        JavaParser.interfaceMethodDeclaration_return interfaceMethodDeclaration146 =null;

        JavaParser.interfaceDeclaration_return interfaceDeclaration147 =null;

        JavaParser.classDeclaration_return classDeclaration148 =null;


        Object char_literal149_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 30) ) { return retval; }

            // Java.g:654:5: ( interfaceFieldDeclaration | interfaceMethodDeclaration | interfaceDeclaration | classDeclaration | ';' )
            int alt54=5;
            switch ( input.LA(1) ) {
            case 136:
                {
                int LA54_1 = input.LA(2);

                if ( (synpred69_Java()) ) {
                    alt54=1;
                }
                else if ( (synpred70_Java()) ) {
                    alt54=2;
                }
                else if ( (synpred71_Java()) ) {
                    alt54=3;
                }
                else if ( (synpred72_Java()) ) {
                    alt54=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 1, input);

                    throw nvae;

                }
                }
                break;
            case PUBLIC:
                {
                int LA54_2 = input.LA(2);

                if ( (synpred69_Java()) ) {
                    alt54=1;
                }
                else if ( (synpred70_Java()) ) {
                    alt54=2;
                }
                else if ( (synpred71_Java()) ) {
                    alt54=3;
                }
                else if ( (synpred72_Java()) ) {
                    alt54=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 2, input);

                    throw nvae;

                }
                }
                break;
            case PROTECTED:
                {
                int LA54_3 = input.LA(2);

                if ( (synpred69_Java()) ) {
                    alt54=1;
                }
                else if ( (synpred70_Java()) ) {
                    alt54=2;
                }
                else if ( (synpred71_Java()) ) {
                    alt54=3;
                }
                else if ( (synpred72_Java()) ) {
                    alt54=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 3, input);

                    throw nvae;

                }
                }
                break;
            case PRIVATE:
                {
                int LA54_4 = input.LA(2);

                if ( (synpred69_Java()) ) {
                    alt54=1;
                }
                else if ( (synpred70_Java()) ) {
                    alt54=2;
                }
                else if ( (synpred71_Java()) ) {
                    alt54=3;
                }
                else if ( (synpred72_Java()) ) {
                    alt54=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 4, input);

                    throw nvae;

                }
                }
                break;
            case STATIC:
                {
                int LA54_5 = input.LA(2);

                if ( (synpred69_Java()) ) {
                    alt54=1;
                }
                else if ( (synpred70_Java()) ) {
                    alt54=2;
                }
                else if ( (synpred71_Java()) ) {
                    alt54=3;
                }
                else if ( (synpred72_Java()) ) {
                    alt54=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 5, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
                {
                int LA54_6 = input.LA(2);

                if ( (synpred69_Java()) ) {
                    alt54=1;
                }
                else if ( (synpred70_Java()) ) {
                    alt54=2;
                }
                else if ( (synpred71_Java()) ) {
                    alt54=3;
                }
                else if ( (synpred72_Java()) ) {
                    alt54=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 6, input);

                    throw nvae;

                }
                }
                break;
            case FINAL:
                {
                int LA54_7 = input.LA(2);

                if ( (synpred69_Java()) ) {
                    alt54=1;
                }
                else if ( (synpred70_Java()) ) {
                    alt54=2;
                }
                else if ( (synpred71_Java()) ) {
                    alt54=3;
                }
                else if ( (synpred72_Java()) ) {
                    alt54=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 7, input);

                    throw nvae;

                }
                }
                break;
            case NATIVE:
                {
                int LA54_8 = input.LA(2);

                if ( (synpred69_Java()) ) {
                    alt54=1;
                }
                else if ( (synpred70_Java()) ) {
                    alt54=2;
                }
                else if ( (synpred71_Java()) ) {
                    alt54=3;
                }
                else if ( (synpred72_Java()) ) {
                    alt54=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 8, input);

                    throw nvae;

                }
                }
                break;
            case SYNCHRONIZED:
                {
                int LA54_9 = input.LA(2);

                if ( (synpred69_Java()) ) {
                    alt54=1;
                }
                else if ( (synpred70_Java()) ) {
                    alt54=2;
                }
                else if ( (synpred71_Java()) ) {
                    alt54=3;
                }
                else if ( (synpred72_Java()) ) {
                    alt54=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 9, input);

                    throw nvae;

                }
                }
                break;
            case TRANSIENT:
                {
                int LA54_10 = input.LA(2);

                if ( (synpred69_Java()) ) {
                    alt54=1;
                }
                else if ( (synpred70_Java()) ) {
                    alt54=2;
                }
                else if ( (synpred71_Java()) ) {
                    alt54=3;
                }
                else if ( (synpred72_Java()) ) {
                    alt54=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 10, input);

                    throw nvae;

                }
                }
                break;
            case VOLATILE:
                {
                int LA54_11 = input.LA(2);

                if ( (synpred69_Java()) ) {
                    alt54=1;
                }
                else if ( (synpred70_Java()) ) {
                    alt54=2;
                }
                else if ( (synpred71_Java()) ) {
                    alt54=3;
                }
                else if ( (synpred72_Java()) ) {
                    alt54=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 11, input);

                    throw nvae;

                }
                }
                break;
            case STRICTFP:
                {
                int LA54_12 = input.LA(2);

                if ( (synpred69_Java()) ) {
                    alt54=1;
                }
                else if ( (synpred70_Java()) ) {
                    alt54=2;
                }
                else if ( (synpred71_Java()) ) {
                    alt54=3;
                }
                else if ( (synpred72_Java()) ) {
                    alt54=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 12, input);

                    throw nvae;

                }
                }
                break;
            case IDENTIFIER:
                {
                int LA54_13 = input.LA(2);

                if ( (synpred69_Java()) ) {
                    alt54=1;
                }
                else if ( (synpred70_Java()) ) {
                    alt54=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 13, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
                {
                int LA54_14 = input.LA(2);

                if ( (synpred69_Java()) ) {
                    alt54=1;
                }
                else if ( (synpred70_Java()) ) {
                    alt54=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 54, 14, input);

                    throw nvae;

                }
                }
                break;
            case LT:
            case VOID:
                {
                alt54=2;
                }
                break;
            case INTERFACE:
                {
                alt54=3;
                }
                break;
            case CLASS:
            case ENUM:
                {
                alt54=4;
                }
                break;
            case SEMI:
                {
                alt54=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 54, 0, input);

                throw nvae;

            }

            switch (alt54) {
                case 1 :
                    // Java.g:655:9: interfaceFieldDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_interfaceFieldDeclaration_in_interfaceBodyDeclaration3096);
                    interfaceFieldDeclaration145=interfaceFieldDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, interfaceFieldDeclaration145.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:656:9: interfaceMethodDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_interfaceMethodDeclaration_in_interfaceBodyDeclaration3106);
                    interfaceMethodDeclaration146=interfaceMethodDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, interfaceMethodDeclaration146.getTree());

                    }
                    break;
                case 3 :
                    // Java.g:657:9: interfaceDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_interfaceDeclaration_in_interfaceBodyDeclaration3116);
                    interfaceDeclaration147=interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, interfaceDeclaration147.getTree());

                    }
                    break;
                case 4 :
                    // Java.g:658:9: classDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_classDeclaration_in_interfaceBodyDeclaration3126);
                    classDeclaration148=classDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classDeclaration148.getTree());

                    }
                    break;
                case 5 :
                    // Java.g:659:9: ';'
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal149=(Token)match(input,SEMI,FOLLOW_SEMI_in_interfaceBodyDeclaration3136); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal149_tree = 
                    (Object)adaptor.create(char_literal149)
                    ;
                    adaptor.addChild(root_0, char_literal149_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 30, interfaceBodyDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "interfaceBodyDeclaration"


    public static class interfaceMethodDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "interfaceMethodDeclaration"
    // Java.g:662:1: interfaceMethodDeclaration : modifiers ( typeParameters )? ( type | 'void' ) IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';' ;
    public final JavaParser.interfaceMethodDeclaration_return interfaceMethodDeclaration() throws RecognitionException {
        JavaParser.interfaceMethodDeclaration_return retval = new JavaParser.interfaceMethodDeclaration_return();
        retval.start = input.LT(1);

        int interfaceMethodDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal153=null;
        Token IDENTIFIER154=null;
        Token char_literal156=null;
        Token char_literal157=null;
        Token string_literal158=null;
        Token char_literal160=null;
        JavaParser.modifiers_return modifiers150 =null;

        JavaParser.typeParameters_return typeParameters151 =null;

        JavaParser.type_return type152 =null;

        JavaParser.formalParameters_return formalParameters155 =null;

        JavaParser.qualifiedNameList_return qualifiedNameList159 =null;


        Object string_literal153_tree=null;
        Object IDENTIFIER154_tree=null;
        Object char_literal156_tree=null;
        Object char_literal157_tree=null;
        Object string_literal158_tree=null;
        Object char_literal160_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 31) ) { return retval; }

            // Java.g:663:5: ( modifiers ( typeParameters )? ( type | 'void' ) IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';' )
            // Java.g:663:9: modifiers ( typeParameters )? ( type | 'void' ) IDENTIFIER formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';'
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_modifiers_in_interfaceMethodDeclaration3156);
            modifiers150=modifiers();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, modifiers150.getTree());

            // Java.g:664:9: ( typeParameters )?
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==LT) ) {
                alt55=1;
            }
            switch (alt55) {
                case 1 :
                    // Java.g:664:10: typeParameters
                    {
                    pushFollow(FOLLOW_typeParameters_in_interfaceMethodDeclaration3167);
                    typeParameters151=typeParameters();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeParameters151.getTree());

                    }
                    break;

            }


            // Java.g:666:9: ( type | 'void' )
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==BOOLEAN||LA56_0==BYTE||LA56_0==CHAR||LA56_0==DOUBLE||LA56_0==FLOAT||LA56_0==IDENTIFIER||LA56_0==INT||LA56_0==LONG||LA56_0==SHORT) ) {
                alt56=1;
            }
            else if ( (LA56_0==VOID) ) {
                alt56=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;

            }
            switch (alt56) {
                case 1 :
                    // Java.g:666:10: type
                    {
                    pushFollow(FOLLOW_type_in_interfaceMethodDeclaration3189);
                    type152=type();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, type152.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:667:10: 'void'
                    {
                    string_literal153=(Token)match(input,VOID,FOLLOW_VOID_in_interfaceMethodDeclaration3200); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal153_tree = 
                    (Object)adaptor.create(string_literal153)
                    ;
                    adaptor.addChild(root_0, string_literal153_tree);
                    }

                    }
                    break;

            }


            IDENTIFIER154=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_interfaceMethodDeclaration3220); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER154_tree = 
            (Object)adaptor.create(IDENTIFIER154)
            ;
            adaptor.addChild(root_0, IDENTIFIER154_tree);
            }

            pushFollow(FOLLOW_formalParameters_in_interfaceMethodDeclaration3230);
            formalParameters155=formalParameters();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, formalParameters155.getTree());

            // Java.g:671:9: ( '[' ']' )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==LBRACKET) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // Java.g:671:10: '[' ']'
            	    {
            	    char_literal156=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_interfaceMethodDeclaration3241); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal156_tree = 
            	    (Object)adaptor.create(char_literal156)
            	    ;
            	    adaptor.addChild(root_0, char_literal156_tree);
            	    }

            	    char_literal157=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_interfaceMethodDeclaration3243); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal157_tree = 
            	    (Object)adaptor.create(char_literal157)
            	    ;
            	    adaptor.addChild(root_0, char_literal157_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop57;
                }
            } while (true);


            // Java.g:673:9: ( 'throws' qualifiedNameList )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==THROWS) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // Java.g:673:10: 'throws' qualifiedNameList
                    {
                    string_literal158=(Token)match(input,THROWS,FOLLOW_THROWS_in_interfaceMethodDeclaration3265); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal158_tree = 
                    (Object)adaptor.create(string_literal158)
                    ;
                    adaptor.addChild(root_0, string_literal158_tree);
                    }

                    pushFollow(FOLLOW_qualifiedNameList_in_interfaceMethodDeclaration3267);
                    qualifiedNameList159=qualifiedNameList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, qualifiedNameList159.getTree());

                    }
                    break;

            }


            char_literal160=(Token)match(input,SEMI,FOLLOW_SEMI_in_interfaceMethodDeclaration3280); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal160_tree = 
            (Object)adaptor.create(char_literal160)
            ;
            adaptor.addChild(root_0, char_literal160_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 31, interfaceMethodDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "interfaceMethodDeclaration"


    public static class interfaceFieldDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "interfaceFieldDeclaration"
    // Java.g:682:1: interfaceFieldDeclaration : modifiers type variableDeclarator ( ',' variableDeclarator )* ';' ;
    public final JavaParser.interfaceFieldDeclaration_return interfaceFieldDeclaration() throws RecognitionException {
        JavaParser.interfaceFieldDeclaration_return retval = new JavaParser.interfaceFieldDeclaration_return();
        retval.start = input.LT(1);

        int interfaceFieldDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal164=null;
        Token char_literal166=null;
        JavaParser.modifiers_return modifiers161 =null;

        JavaParser.type_return type162 =null;

        JavaParser.variableDeclarator_return variableDeclarator163 =null;

        JavaParser.variableDeclarator_return variableDeclarator165 =null;


        Object char_literal164_tree=null;
        Object char_literal166_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 32) ) { return retval; }

            // Java.g:683:5: ( modifiers type variableDeclarator ( ',' variableDeclarator )* ';' )
            // Java.g:683:9: modifiers type variableDeclarator ( ',' variableDeclarator )* ';'
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_modifiers_in_interfaceFieldDeclaration3302);
            modifiers161=modifiers();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, modifiers161.getTree());

            pushFollow(FOLLOW_type_in_interfaceFieldDeclaration3304);
            type162=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type162.getTree());

            pushFollow(FOLLOW_variableDeclarator_in_interfaceFieldDeclaration3306);
            variableDeclarator163=variableDeclarator();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, variableDeclarator163.getTree());

            // Java.g:684:9: ( ',' variableDeclarator )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==COMMA) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // Java.g:684:10: ',' variableDeclarator
            	    {
            	    char_literal164=(Token)match(input,COMMA,FOLLOW_COMMA_in_interfaceFieldDeclaration3317); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal164_tree = 
            	    (Object)adaptor.create(char_literal164)
            	    ;
            	    adaptor.addChild(root_0, char_literal164_tree);
            	    }

            	    pushFollow(FOLLOW_variableDeclarator_in_interfaceFieldDeclaration3319);
            	    variableDeclarator165=variableDeclarator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, variableDeclarator165.getTree());

            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);


            char_literal166=(Token)match(input,SEMI,FOLLOW_SEMI_in_interfaceFieldDeclaration3340); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal166_tree = 
            (Object)adaptor.create(char_literal166)
            ;
            adaptor.addChild(root_0, char_literal166_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 32, interfaceFieldDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "interfaceFieldDeclaration"


    public static class type_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "type"
    // Java.g:690:1: type : ( classOrInterfaceType ( '[' ']' )* | primitiveType ( '[' ']' )* );
    public final JavaParser.type_return type() throws RecognitionException {
        JavaParser.type_return retval = new JavaParser.type_return();
        retval.start = input.LT(1);

        int type_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal168=null;
        Token char_literal169=null;
        Token char_literal171=null;
        Token char_literal172=null;
        JavaParser.classOrInterfaceType_return classOrInterfaceType167 =null;

        JavaParser.primitiveType_return primitiveType170 =null;


        Object char_literal168_tree=null;
        Object char_literal169_tree=null;
        Object char_literal171_tree=null;
        Object char_literal172_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 33) ) { return retval; }

            // Java.g:691:5: ( classOrInterfaceType ( '[' ']' )* | primitiveType ( '[' ']' )* )
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==IDENTIFIER) ) {
                alt62=1;
            }
            else if ( (LA62_0==BOOLEAN||LA62_0==BYTE||LA62_0==CHAR||LA62_0==DOUBLE||LA62_0==FLOAT||LA62_0==INT||LA62_0==LONG||LA62_0==SHORT) ) {
                alt62=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 62, 0, input);

                throw nvae;

            }
            switch (alt62) {
                case 1 :
                    // Java.g:691:9: classOrInterfaceType ( '[' ']' )*
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_classOrInterfaceType_in_type3361);
                    classOrInterfaceType167=classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classOrInterfaceType167.getTree());

                    // Java.g:692:9: ( '[' ']' )*
                    loop60:
                    do {
                        int alt60=2;
                        int LA60_0 = input.LA(1);

                        if ( (LA60_0==LBRACKET) ) {
                            alt60=1;
                        }


                        switch (alt60) {
                    	case 1 :
                    	    // Java.g:692:10: '[' ']'
                    	    {
                    	    char_literal168=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_type3372); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal168_tree = 
                    	    (Object)adaptor.create(char_literal168)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal168_tree);
                    	    }

                    	    char_literal169=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_type3374); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal169_tree = 
                    	    (Object)adaptor.create(char_literal169)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal169_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop60;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // Java.g:694:9: primitiveType ( '[' ']' )*
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_primitiveType_in_type3395);
                    primitiveType170=primitiveType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, primitiveType170.getTree());

                    // Java.g:695:9: ( '[' ']' )*
                    loop61:
                    do {
                        int alt61=2;
                        int LA61_0 = input.LA(1);

                        if ( (LA61_0==LBRACKET) ) {
                            alt61=1;
                        }


                        switch (alt61) {
                    	case 1 :
                    	    // Java.g:695:10: '[' ']'
                    	    {
                    	    char_literal171=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_type3406); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal171_tree = 
                    	    (Object)adaptor.create(char_literal171)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal171_tree);
                    	    }

                    	    char_literal172=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_type3408); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal172_tree = 
                    	    (Object)adaptor.create(char_literal172)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal172_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop61;
                        }
                    } while (true);


                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 33, type_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "type"


    public static class classOrInterfaceType_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "classOrInterfaceType"
    // Java.g:700:1: classOrInterfaceType : IDENTIFIER ( typeArguments )? ( '.' IDENTIFIER ( typeArguments )? )* ;
    public final JavaParser.classOrInterfaceType_return classOrInterfaceType() throws RecognitionException {
        JavaParser.classOrInterfaceType_return retval = new JavaParser.classOrInterfaceType_return();
        retval.start = input.LT(1);

        int classOrInterfaceType_StartIndex = input.index();

        Object root_0 = null;

        Token IDENTIFIER173=null;
        Token char_literal175=null;
        Token IDENTIFIER176=null;
        JavaParser.typeArguments_return typeArguments174 =null;

        JavaParser.typeArguments_return typeArguments177 =null;


        Object IDENTIFIER173_tree=null;
        Object char_literal175_tree=null;
        Object IDENTIFIER176_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 34) ) { return retval; }

            // Java.g:701:5: ( IDENTIFIER ( typeArguments )? ( '.' IDENTIFIER ( typeArguments )? )* )
            // Java.g:701:9: IDENTIFIER ( typeArguments )? ( '.' IDENTIFIER ( typeArguments )? )*
            {
            root_0 = (Object)adaptor.nil();


            IDENTIFIER173=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_classOrInterfaceType3440); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER173_tree = 
            (Object)adaptor.create(IDENTIFIER173)
            ;
            adaptor.addChild(root_0, IDENTIFIER173_tree);
            }

            // Java.g:702:9: ( typeArguments )?
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==LT) ) {
                int LA63_1 = input.LA(2);

                if ( (LA63_1==BOOLEAN||LA63_1==BYTE||LA63_1==CHAR||LA63_1==DOUBLE||LA63_1==FLOAT||LA63_1==IDENTIFIER||LA63_1==INT||LA63_1==LONG||LA63_1==QUES||LA63_1==SHORT) ) {
                    alt63=1;
                }
            }
            switch (alt63) {
                case 1 :
                    // Java.g:702:10: typeArguments
                    {
                    pushFollow(FOLLOW_typeArguments_in_classOrInterfaceType3451);
                    typeArguments174=typeArguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeArguments174.getTree());

                    }
                    break;

            }


            // Java.g:704:9: ( '.' IDENTIFIER ( typeArguments )? )*
            loop65:
            do {
                int alt65=2;
                int LA65_0 = input.LA(1);

                if ( (LA65_0==DOT) ) {
                    alt65=1;
                }


                switch (alt65) {
            	case 1 :
            	    // Java.g:704:10: '.' IDENTIFIER ( typeArguments )?
            	    {
            	    char_literal175=(Token)match(input,DOT,FOLLOW_DOT_in_classOrInterfaceType3473); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal175_tree = 
            	    (Object)adaptor.create(char_literal175)
            	    ;
            	    adaptor.addChild(root_0, char_literal175_tree);
            	    }

            	    IDENTIFIER176=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_classOrInterfaceType3475); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    IDENTIFIER176_tree = 
            	    (Object)adaptor.create(IDENTIFIER176)
            	    ;
            	    adaptor.addChild(root_0, IDENTIFIER176_tree);
            	    }

            	    // Java.g:705:13: ( typeArguments )?
            	    int alt64=2;
            	    int LA64_0 = input.LA(1);

            	    if ( (LA64_0==LT) ) {
            	        int LA64_1 = input.LA(2);

            	        if ( (LA64_1==BOOLEAN||LA64_1==BYTE||LA64_1==CHAR||LA64_1==DOUBLE||LA64_1==FLOAT||LA64_1==IDENTIFIER||LA64_1==INT||LA64_1==LONG||LA64_1==QUES||LA64_1==SHORT) ) {
            	            alt64=1;
            	        }
            	    }
            	    switch (alt64) {
            	        case 1 :
            	            // Java.g:705:14: typeArguments
            	            {
            	            pushFollow(FOLLOW_typeArguments_in_classOrInterfaceType3490);
            	            typeArguments177=typeArguments();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, typeArguments177.getTree());

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop65;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 34, classOrInterfaceType_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "classOrInterfaceType"


    public static class primitiveType_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "primitiveType"
    // Java.g:710:1: primitiveType : ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' );
    public final JavaParser.primitiveType_return primitiveType() throws RecognitionException {
        JavaParser.primitiveType_return retval = new JavaParser.primitiveType_return();
        retval.start = input.LT(1);

        int primitiveType_StartIndex = input.index();

        Object root_0 = null;

        Token set178=null;

        Object set178_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 35) ) { return retval; }

            // Java.g:711:5: ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' )
            // Java.g:
            {
            root_0 = (Object)adaptor.nil();


            set178=(Token)input.LT(1);

            if ( input.LA(1)==BOOLEAN||input.LA(1)==BYTE||input.LA(1)==CHAR||input.LA(1)==DOUBLE||input.LA(1)==FLOAT||input.LA(1)==INT||input.LA(1)==LONG||input.LA(1)==SHORT ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (Object)adaptor.create(set178)
                );
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 35, primitiveType_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "primitiveType"


    public static class typeArguments_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "typeArguments"
    // Java.g:721:1: typeArguments : '<' typeArgument ( ',' typeArgument )* '>' ;
    public final JavaParser.typeArguments_return typeArguments() throws RecognitionException {
        JavaParser.typeArguments_return retval = new JavaParser.typeArguments_return();
        retval.start = input.LT(1);

        int typeArguments_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal179=null;
        Token char_literal181=null;
        Token char_literal183=null;
        JavaParser.typeArgument_return typeArgument180 =null;

        JavaParser.typeArgument_return typeArgument182 =null;


        Object char_literal179_tree=null;
        Object char_literal181_tree=null;
        Object char_literal183_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 36) ) { return retval; }

            // Java.g:722:5: ( '<' typeArgument ( ',' typeArgument )* '>' )
            // Java.g:722:9: '<' typeArgument ( ',' typeArgument )* '>'
            {
            root_0 = (Object)adaptor.nil();


            char_literal179=(Token)match(input,LT,FOLLOW_LT_in_typeArguments3627); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal179_tree = 
            (Object)adaptor.create(char_literal179)
            ;
            adaptor.addChild(root_0, char_literal179_tree);
            }

            pushFollow(FOLLOW_typeArgument_in_typeArguments3629);
            typeArgument180=typeArgument();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, typeArgument180.getTree());

            // Java.g:723:9: ( ',' typeArgument )*
            loop66:
            do {
                int alt66=2;
                int LA66_0 = input.LA(1);

                if ( (LA66_0==COMMA) ) {
                    alt66=1;
                }


                switch (alt66) {
            	case 1 :
            	    // Java.g:723:10: ',' typeArgument
            	    {
            	    char_literal181=(Token)match(input,COMMA,FOLLOW_COMMA_in_typeArguments3640); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal181_tree = 
            	    (Object)adaptor.create(char_literal181)
            	    ;
            	    adaptor.addChild(root_0, char_literal181_tree);
            	    }

            	    pushFollow(FOLLOW_typeArgument_in_typeArguments3642);
            	    typeArgument182=typeArgument();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeArgument182.getTree());

            	    }
            	    break;

            	default :
            	    break loop66;
                }
            } while (true);


            char_literal183=(Token)match(input,GT,FOLLOW_GT_in_typeArguments3664); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal183_tree = 
            (Object)adaptor.create(char_literal183)
            ;
            adaptor.addChild(root_0, char_literal183_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 36, typeArguments_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeArguments"


    public static class typeArgument_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "typeArgument"
    // Java.g:728:1: typeArgument : ( type | '?' ( ( 'extends' | 'super' ) type )? );
    public final JavaParser.typeArgument_return typeArgument() throws RecognitionException {
        JavaParser.typeArgument_return retval = new JavaParser.typeArgument_return();
        retval.start = input.LT(1);

        int typeArgument_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal185=null;
        Token set186=null;
        JavaParser.type_return type184 =null;

        JavaParser.type_return type187 =null;


        Object char_literal185_tree=null;
        Object set186_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 37) ) { return retval; }

            // Java.g:729:5: ( type | '?' ( ( 'extends' | 'super' ) type )? )
            int alt68=2;
            int LA68_0 = input.LA(1);

            if ( (LA68_0==BOOLEAN||LA68_0==BYTE||LA68_0==CHAR||LA68_0==DOUBLE||LA68_0==FLOAT||LA68_0==IDENTIFIER||LA68_0==INT||LA68_0==LONG||LA68_0==SHORT) ) {
                alt68=1;
            }
            else if ( (LA68_0==QUES) ) {
                alt68=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 68, 0, input);

                throw nvae;

            }
            switch (alt68) {
                case 1 :
                    // Java.g:729:9: type
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_type_in_typeArgument3684);
                    type184=type();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, type184.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:730:9: '?' ( ( 'extends' | 'super' ) type )?
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal185=(Token)match(input,QUES,FOLLOW_QUES_in_typeArgument3694); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal185_tree = 
                    (Object)adaptor.create(char_literal185)
                    ;
                    adaptor.addChild(root_0, char_literal185_tree);
                    }

                    // Java.g:731:9: ( ( 'extends' | 'super' ) type )?
                    int alt67=2;
                    int LA67_0 = input.LA(1);

                    if ( (LA67_0==EXTENDS||LA67_0==SUPER) ) {
                        alt67=1;
                    }
                    switch (alt67) {
                        case 1 :
                            // Java.g:732:13: ( 'extends' | 'super' ) type
                            {
                            set186=(Token)input.LT(1);

                            if ( input.LA(1)==EXTENDS||input.LA(1)==SUPER ) {
                                input.consume();
                                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                                (Object)adaptor.create(set186)
                                );
                                state.errorRecovery=false;
                                state.failed=false;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return retval;}
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            pushFollow(FOLLOW_type_in_typeArgument3762);
                            type187=type();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, type187.getTree());

                            }
                            break;

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 37, typeArgument_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeArgument"


    public static class qualifiedNameList_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "qualifiedNameList"
    // Java.g:739:1: qualifiedNameList : qualifiedName ( ',' qualifiedName )* ;
    public final JavaParser.qualifiedNameList_return qualifiedNameList() throws RecognitionException {
        JavaParser.qualifiedNameList_return retval = new JavaParser.qualifiedNameList_return();
        retval.start = input.LT(1);

        int qualifiedNameList_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal189=null;
        JavaParser.qualifiedName_return qualifiedName188 =null;

        JavaParser.qualifiedName_return qualifiedName190 =null;


        Object char_literal189_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 38) ) { return retval; }

            // Java.g:740:5: ( qualifiedName ( ',' qualifiedName )* )
            // Java.g:740:9: qualifiedName ( ',' qualifiedName )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList3793);
            qualifiedName188=qualifiedName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, qualifiedName188.getTree());

            // Java.g:741:9: ( ',' qualifiedName )*
            loop69:
            do {
                int alt69=2;
                int LA69_0 = input.LA(1);

                if ( (LA69_0==COMMA) ) {
                    alt69=1;
                }


                switch (alt69) {
            	case 1 :
            	    // Java.g:741:10: ',' qualifiedName
            	    {
            	    char_literal189=(Token)match(input,COMMA,FOLLOW_COMMA_in_qualifiedNameList3804); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal189_tree = 
            	    (Object)adaptor.create(char_literal189)
            	    ;
            	    adaptor.addChild(root_0, char_literal189_tree);
            	    }

            	    pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList3806);
            	    qualifiedName190=qualifiedName();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, qualifiedName190.getTree());

            	    }
            	    break;

            	default :
            	    break loop69;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 38, qualifiedNameList_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "qualifiedNameList"


    public static class formalParameters_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "formalParameters"
    // Java.g:745:1: formalParameters : '(' ( formalParameterDecls )? ')' ;
    public final JavaParser.formalParameters_return formalParameters() throws RecognitionException {
        JavaParser.formalParameters_return retval = new JavaParser.formalParameters_return();
        retval.start = input.LT(1);

        int formalParameters_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal191=null;
        Token char_literal193=null;
        JavaParser.formalParameterDecls_return formalParameterDecls192 =null;


        Object char_literal191_tree=null;
        Object char_literal193_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 39) ) { return retval; }

            // Java.g:746:5: ( '(' ( formalParameterDecls )? ')' )
            // Java.g:746:9: '(' ( formalParameterDecls )? ')'
            {
            root_0 = (Object)adaptor.nil();


            char_literal191=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_formalParameters3837); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal191_tree = 
            (Object)adaptor.create(char_literal191)
            ;
            adaptor.addChild(root_0, char_literal191_tree);
            }

            // Java.g:747:9: ( formalParameterDecls )?
            int alt70=2;
            int LA70_0 = input.LA(1);

            if ( (LA70_0==EOF||LA70_0==BOOLEAN||LA70_0==BYTE||LA70_0==CHAR||LA70_0==DOUBLE||LA70_0==FINAL||LA70_0==FLOAT||LA70_0==IDENTIFIER||LA70_0==INT||LA70_0==LONG||LA70_0==SHORT||LA70_0==136) ) {
                alt70=1;
            }
            switch (alt70) {
                case 1 :
                    // Java.g:747:10: formalParameterDecls
                    {
                    pushFollow(FOLLOW_formalParameterDecls_in_formalParameters3848);
                    formalParameterDecls192=formalParameterDecls();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, formalParameterDecls192.getTree());

                    }
                    break;

            }


            char_literal193=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_formalParameters3870); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal193_tree = 
            (Object)adaptor.create(char_literal193)
            ;
            adaptor.addChild(root_0, char_literal193_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 39, formalParameters_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "formalParameters"


    public static class formalParameterDecls_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "formalParameterDecls"
    // Java.g:752:1: formalParameterDecls : ( ellipsisParameterDecl | normalParameterDecl ( ',' normalParameterDecl )* | ( normalParameterDecl ',' )+ ellipsisParameterDecl );
    public final JavaParser.formalParameterDecls_return formalParameterDecls() throws RecognitionException {
        JavaParser.formalParameterDecls_return retval = new JavaParser.formalParameterDecls_return();
        retval.start = input.LT(1);

        int formalParameterDecls_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal196=null;
        Token char_literal199=null;
        JavaParser.ellipsisParameterDecl_return ellipsisParameterDecl194 =null;

        JavaParser.normalParameterDecl_return normalParameterDecl195 =null;

        JavaParser.normalParameterDecl_return normalParameterDecl197 =null;

        JavaParser.normalParameterDecl_return normalParameterDecl198 =null;

        JavaParser.ellipsisParameterDecl_return ellipsisParameterDecl200 =null;


        Object char_literal196_tree=null;
        Object char_literal199_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 40) ) { return retval; }

            // Java.g:753:5: ( ellipsisParameterDecl | normalParameterDecl ( ',' normalParameterDecl )* | ( normalParameterDecl ',' )+ ellipsisParameterDecl )
            int alt73=3;
            switch ( input.LA(1) ) {
            case FINAL:
                {
                int LA73_1 = input.LA(2);

                if ( (synpred97_Java()) ) {
                    alt73=1;
                }
                else if ( (synpred99_Java()) ) {
                    alt73=2;
                }
                else if ( (true) ) {
                    alt73=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 73, 1, input);

                    throw nvae;

                }
                }
                break;
            case 136:
                {
                int LA73_2 = input.LA(2);

                if ( (synpred97_Java()) ) {
                    alt73=1;
                }
                else if ( (synpred99_Java()) ) {
                    alt73=2;
                }
                else if ( (true) ) {
                    alt73=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 73, 2, input);

                    throw nvae;

                }
                }
                break;
            case IDENTIFIER:
                {
                int LA73_3 = input.LA(2);

                if ( (synpred97_Java()) ) {
                    alt73=1;
                }
                else if ( (synpred99_Java()) ) {
                    alt73=2;
                }
                else if ( (true) ) {
                    alt73=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 73, 3, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
                {
                int LA73_4 = input.LA(2);

                if ( (synpred97_Java()) ) {
                    alt73=1;
                }
                else if ( (synpred99_Java()) ) {
                    alt73=2;
                }
                else if ( (true) ) {
                    alt73=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 73, 4, input);

                    throw nvae;

                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 73, 0, input);

                throw nvae;

            }

            switch (alt73) {
                case 1 :
                    // Java.g:753:9: ellipsisParameterDecl
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_ellipsisParameterDecl_in_formalParameterDecls3890);
                    ellipsisParameterDecl194=ellipsisParameterDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, ellipsisParameterDecl194.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:754:9: normalParameterDecl ( ',' normalParameterDecl )*
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_normalParameterDecl_in_formalParameterDecls3900);
                    normalParameterDecl195=normalParameterDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, normalParameterDecl195.getTree());

                    // Java.g:755:9: ( ',' normalParameterDecl )*
                    loop71:
                    do {
                        int alt71=2;
                        int LA71_0 = input.LA(1);

                        if ( (LA71_0==COMMA) ) {
                            alt71=1;
                        }


                        switch (alt71) {
                    	case 1 :
                    	    // Java.g:755:10: ',' normalParameterDecl
                    	    {
                    	    char_literal196=(Token)match(input,COMMA,FOLLOW_COMMA_in_formalParameterDecls3911); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal196_tree = 
                    	    (Object)adaptor.create(char_literal196)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal196_tree);
                    	    }

                    	    pushFollow(FOLLOW_normalParameterDecl_in_formalParameterDecls3913);
                    	    normalParameterDecl197=normalParameterDecl();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, normalParameterDecl197.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop71;
                        }
                    } while (true);


                    }
                    break;
                case 3 :
                    // Java.g:757:9: ( normalParameterDecl ',' )+ ellipsisParameterDecl
                    {
                    root_0 = (Object)adaptor.nil();


                    // Java.g:757:9: ( normalParameterDecl ',' )+
                    int cnt72=0;
                    loop72:
                    do {
                        int alt72=2;
                        switch ( input.LA(1) ) {
                        case FINAL:
                            {
                            int LA72_1 = input.LA(2);

                            if ( (synpred100_Java()) ) {
                                alt72=1;
                            }


                            }
                            break;
                        case 136:
                            {
                            int LA72_2 = input.LA(2);

                            if ( (synpred100_Java()) ) {
                                alt72=1;
                            }


                            }
                            break;
                        case IDENTIFIER:
                            {
                            int LA72_3 = input.LA(2);

                            if ( (synpred100_Java()) ) {
                                alt72=1;
                            }


                            }
                            break;
                        case BOOLEAN:
                        case BYTE:
                        case CHAR:
                        case DOUBLE:
                        case FLOAT:
                        case INT:
                        case LONG:
                        case SHORT:
                            {
                            int LA72_4 = input.LA(2);

                            if ( (synpred100_Java()) ) {
                                alt72=1;
                            }


                            }
                            break;

                        }

                        switch (alt72) {
                    	case 1 :
                    	    // Java.g:757:10: normalParameterDecl ','
                    	    {
                    	    pushFollow(FOLLOW_normalParameterDecl_in_formalParameterDecls3935);
                    	    normalParameterDecl198=normalParameterDecl();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, normalParameterDecl198.getTree());

                    	    char_literal199=(Token)match(input,COMMA,FOLLOW_COMMA_in_formalParameterDecls3945); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal199_tree = 
                    	    (Object)adaptor.create(char_literal199)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal199_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt72 >= 1 ) break loop72;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(72, input);
                                throw eee;
                        }
                        cnt72++;
                    } while (true);


                    pushFollow(FOLLOW_ellipsisParameterDecl_in_formalParameterDecls3967);
                    ellipsisParameterDecl200=ellipsisParameterDecl();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, ellipsisParameterDecl200.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 40, formalParameterDecls_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "formalParameterDecls"


    public static class normalParameterDecl_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "normalParameterDecl"
    // Java.g:763:1: normalParameterDecl : variableModifiers type IDENTIFIER ( '[' ']' )* ;
    public final JavaParser.normalParameterDecl_return normalParameterDecl() throws RecognitionException {
        JavaParser.normalParameterDecl_return retval = new JavaParser.normalParameterDecl_return();
        retval.start = input.LT(1);

        int normalParameterDecl_StartIndex = input.index();

        Object root_0 = null;

        Token IDENTIFIER203=null;
        Token char_literal204=null;
        Token char_literal205=null;
        JavaParser.variableModifiers_return variableModifiers201 =null;

        JavaParser.type_return type202 =null;


        Object IDENTIFIER203_tree=null;
        Object char_literal204_tree=null;
        Object char_literal205_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 41) ) { return retval; }

            // Java.g:764:5: ( variableModifiers type IDENTIFIER ( '[' ']' )* )
            // Java.g:764:9: variableModifiers type IDENTIFIER ( '[' ']' )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_variableModifiers_in_normalParameterDecl3987);
            variableModifiers201=variableModifiers();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, variableModifiers201.getTree());

            pushFollow(FOLLOW_type_in_normalParameterDecl3989);
            type202=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type202.getTree());

            IDENTIFIER203=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_normalParameterDecl3991); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER203_tree = 
            (Object)adaptor.create(IDENTIFIER203)
            ;
            adaptor.addChild(root_0, IDENTIFIER203_tree);
            }

            // Java.g:765:9: ( '[' ']' )*
            loop74:
            do {
                int alt74=2;
                int LA74_0 = input.LA(1);

                if ( (LA74_0==LBRACKET) ) {
                    alt74=1;
                }


                switch (alt74) {
            	case 1 :
            	    // Java.g:765:10: '[' ']'
            	    {
            	    char_literal204=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_normalParameterDecl4002); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal204_tree = 
            	    (Object)adaptor.create(char_literal204)
            	    ;
            	    adaptor.addChild(root_0, char_literal204_tree);
            	    }

            	    char_literal205=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_normalParameterDecl4004); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal205_tree = 
            	    (Object)adaptor.create(char_literal205)
            	    ;
            	    adaptor.addChild(root_0, char_literal205_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop74;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 41, normalParameterDecl_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "normalParameterDecl"


    public static class ellipsisParameterDecl_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "ellipsisParameterDecl"
    // Java.g:769:1: ellipsisParameterDecl : variableModifiers type '...' IDENTIFIER ;
    public final JavaParser.ellipsisParameterDecl_return ellipsisParameterDecl() throws RecognitionException {
        JavaParser.ellipsisParameterDecl_return retval = new JavaParser.ellipsisParameterDecl_return();
        retval.start = input.LT(1);

        int ellipsisParameterDecl_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal208=null;
        Token IDENTIFIER209=null;
        JavaParser.variableModifiers_return variableModifiers206 =null;

        JavaParser.type_return type207 =null;


        Object string_literal208_tree=null;
        Object IDENTIFIER209_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 42) ) { return retval; }

            // Java.g:770:5: ( variableModifiers type '...' IDENTIFIER )
            // Java.g:770:9: variableModifiers type '...' IDENTIFIER
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_variableModifiers_in_ellipsisParameterDecl4035);
            variableModifiers206=variableModifiers();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, variableModifiers206.getTree());

            pushFollow(FOLLOW_type_in_ellipsisParameterDecl4045);
            type207=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type207.getTree());

            string_literal208=(Token)match(input,ELLIPSIS,FOLLOW_ELLIPSIS_in_ellipsisParameterDecl4048); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal208_tree = 
            (Object)adaptor.create(string_literal208)
            ;
            adaptor.addChild(root_0, string_literal208_tree);
            }

            IDENTIFIER209=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_ellipsisParameterDecl4058); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER209_tree = 
            (Object)adaptor.create(IDENTIFIER209)
            ;
            adaptor.addChild(root_0, IDENTIFIER209_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 42, ellipsisParameterDecl_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "ellipsisParameterDecl"


    public static class explicitConstructorInvocation_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "explicitConstructorInvocation"
    // Java.g:776:1: explicitConstructorInvocation : ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';' );
    public final JavaParser.explicitConstructorInvocation_return explicitConstructorInvocation() throws RecognitionException {
        JavaParser.explicitConstructorInvocation_return retval = new JavaParser.explicitConstructorInvocation_return();
        retval.start = input.LT(1);

        int explicitConstructorInvocation_StartIndex = input.index();

        Object root_0 = null;

        Token set211=null;
        Token char_literal213=null;
        Token char_literal215=null;
        Token string_literal217=null;
        Token char_literal219=null;
        JavaParser.nonWildcardTypeArguments_return nonWildcardTypeArguments210 =null;

        JavaParser.arguments_return arguments212 =null;

        JavaParser.primary_return primary214 =null;

        JavaParser.nonWildcardTypeArguments_return nonWildcardTypeArguments216 =null;

        JavaParser.arguments_return arguments218 =null;


        Object set211_tree=null;
        Object char_literal213_tree=null;
        Object char_literal215_tree=null;
        Object string_literal217_tree=null;
        Object char_literal219_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 43) ) { return retval; }

            // Java.g:777:5: ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';' )
            int alt77=2;
            switch ( input.LA(1) ) {
            case LT:
                {
                alt77=1;
                }
                break;
            case THIS:
                {
                int LA77_2 = input.LA(2);

                if ( (synpred104_Java()) ) {
                    alt77=1;
                }
                else if ( (true) ) {
                    alt77=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 77, 2, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CHARLITERAL:
            case DOUBLE:
            case DOUBLELITERAL:
            case FALSE:
            case FLOAT:
            case FLOATLITERAL:
            case IDENTIFIER:
            case INT:
            case INTLITERAL:
            case LONG:
            case LONGLITERAL:
            case LPAREN:
            case NEW:
            case NULL:
            case SHORT:
            case STRINGLITERAL:
            case TRUE:
            case VOID:
                {
                alt77=2;
                }
                break;
            case SUPER:
                {
                int LA77_4 = input.LA(2);

                if ( (synpred104_Java()) ) {
                    alt77=1;
                }
                else if ( (true) ) {
                    alt77=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 77, 4, input);

                    throw nvae;

                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 77, 0, input);

                throw nvae;

            }

            switch (alt77) {
                case 1 :
                    // Java.g:777:9: ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';'
                    {
                    root_0 = (Object)adaptor.nil();


                    // Java.g:777:9: ( nonWildcardTypeArguments )?
                    int alt75=2;
                    int LA75_0 = input.LA(1);

                    if ( (LA75_0==LT) ) {
                        alt75=1;
                    }
                    switch (alt75) {
                        case 1 :
                            // Java.g:777:10: nonWildcardTypeArguments
                            {
                            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation4080);
                            nonWildcardTypeArguments210=nonWildcardTypeArguments();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, nonWildcardTypeArguments210.getTree());

                            }
                            break;

                    }


                    set211=(Token)input.LT(1);

                    if ( input.LA(1)==SUPER||input.LA(1)==THIS ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                        (Object)adaptor.create(set211)
                        );
                        state.errorRecovery=false;
                        state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    pushFollow(FOLLOW_arguments_in_explicitConstructorInvocation4138);
                    arguments212=arguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arguments212.getTree());

                    char_literal213=(Token)match(input,SEMI,FOLLOW_SEMI_in_explicitConstructorInvocation4140); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal213_tree = 
                    (Object)adaptor.create(char_literal213)
                    ;
                    adaptor.addChild(root_0, char_literal213_tree);
                    }

                    }
                    break;
                case 2 :
                    // Java.g:784:9: primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';'
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_primary_in_explicitConstructorInvocation4151);
                    primary214=primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, primary214.getTree());

                    char_literal215=(Token)match(input,DOT,FOLLOW_DOT_in_explicitConstructorInvocation4161); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal215_tree = 
                    (Object)adaptor.create(char_literal215)
                    ;
                    adaptor.addChild(root_0, char_literal215_tree);
                    }

                    // Java.g:786:9: ( nonWildcardTypeArguments )?
                    int alt76=2;
                    int LA76_0 = input.LA(1);

                    if ( (LA76_0==LT) ) {
                        alt76=1;
                    }
                    switch (alt76) {
                        case 1 :
                            // Java.g:786:10: nonWildcardTypeArguments
                            {
                            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation4172);
                            nonWildcardTypeArguments216=nonWildcardTypeArguments();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, nonWildcardTypeArguments216.getTree());

                            }
                            break;

                    }


                    string_literal217=(Token)match(input,SUPER,FOLLOW_SUPER_in_explicitConstructorInvocation4193); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal217_tree = 
                    (Object)adaptor.create(string_literal217)
                    ;
                    adaptor.addChild(root_0, string_literal217_tree);
                    }

                    pushFollow(FOLLOW_arguments_in_explicitConstructorInvocation4203);
                    arguments218=arguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arguments218.getTree());

                    char_literal219=(Token)match(input,SEMI,FOLLOW_SEMI_in_explicitConstructorInvocation4205); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal219_tree = 
                    (Object)adaptor.create(char_literal219)
                    ;
                    adaptor.addChild(root_0, char_literal219_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 43, explicitConstructorInvocation_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "explicitConstructorInvocation"


    public static class qualifiedName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "qualifiedName"
    // Java.g:792:1: qualifiedName : IDENTIFIER ( '.' IDENTIFIER )* ;
    public final JavaParser.qualifiedName_return qualifiedName() throws RecognitionException {
        JavaParser.qualifiedName_return retval = new JavaParser.qualifiedName_return();
        retval.start = input.LT(1);

        int qualifiedName_StartIndex = input.index();

        Object root_0 = null;

        Token IDENTIFIER220=null;
        Token char_literal221=null;
        Token IDENTIFIER222=null;

        Object IDENTIFIER220_tree=null;
        Object char_literal221_tree=null;
        Object IDENTIFIER222_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 44) ) { return retval; }

            // Java.g:793:5: ( IDENTIFIER ( '.' IDENTIFIER )* )
            // Java.g:793:9: IDENTIFIER ( '.' IDENTIFIER )*
            {
            root_0 = (Object)adaptor.nil();


            IDENTIFIER220=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_qualifiedName4225); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER220_tree = 
            (Object)adaptor.create(IDENTIFIER220)
            ;
            adaptor.addChild(root_0, IDENTIFIER220_tree);
            }

            // Java.g:794:9: ( '.' IDENTIFIER )*
            loop78:
            do {
                int alt78=2;
                int LA78_0 = input.LA(1);

                if ( (LA78_0==DOT) ) {
                    alt78=1;
                }


                switch (alt78) {
            	case 1 :
            	    // Java.g:794:10: '.' IDENTIFIER
            	    {
            	    char_literal221=(Token)match(input,DOT,FOLLOW_DOT_in_qualifiedName4236); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal221_tree = 
            	    (Object)adaptor.create(char_literal221)
            	    ;
            	    adaptor.addChild(root_0, char_literal221_tree);
            	    }

            	    IDENTIFIER222=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_qualifiedName4238); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    IDENTIFIER222_tree = 
            	    (Object)adaptor.create(IDENTIFIER222)
            	    ;
            	    adaptor.addChild(root_0, IDENTIFIER222_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop78;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 44, qualifiedName_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "qualifiedName"


    public static class annotations_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "annotations"
    // Java.g:798:1: annotations : ( annotation )+ -> ( ^( WANNOTATION annotation ) )+ ;
    public final JavaParser.annotations_return annotations() throws RecognitionException {
        JavaParser.annotations_return retval = new JavaParser.annotations_return();
        retval.start = input.LT(1);

        int annotations_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.annotation_return annotation223 =null;


        RewriteRuleSubtreeStream stream_annotation=new RewriteRuleSubtreeStream(adaptor,"rule annotation");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 45) ) { return retval; }

            // Java.g:799:5: ( ( annotation )+ -> ( ^( WANNOTATION annotation ) )+ )
            // Java.g:799:9: ( annotation )+
            {
            // Java.g:799:9: ( annotation )+
            int cnt79=0;
            loop79:
            do {
                int alt79=2;
                int LA79_0 = input.LA(1);

                if ( (LA79_0==136) ) {
                    alt79=1;
                }


                switch (alt79) {
            	case 1 :
            	    // Java.g:799:10: annotation
            	    {
            	    pushFollow(FOLLOW_annotation_in_annotations4270);
            	    annotation223=annotation();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_annotation.add(annotation223.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt79 >= 1 ) break loop79;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(79, input);
                        throw eee;
                }
                cnt79++;
            } while (true);


            // AST REWRITE
            // elements: annotation
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 801:3: -> ( ^( WANNOTATION annotation ) )+
            {
                if ( !(stream_annotation.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_annotation.hasNext() ) {
                    // Java.g:801:6: ^( WANNOTATION annotation )
                    {
                    Object root_1 = (Object)adaptor.nil();
                    root_1 = (Object)adaptor.becomeRoot(
                    (Object)adaptor.create(WANNOTATION, "WANNOTATION")
                    , root_1);

                    adaptor.addChild(root_1, stream_annotation.nextTree());

                    adaptor.addChild(root_0, root_1);
                    }

                }
                stream_annotation.reset();

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 45, annotations_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "annotations"


    public static class annotation_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "annotation"
    // Java.g:809:1: annotation : '@' qualifiedName ( '(' ( elementValuePairs | elementValue )? ')' )? ;
    public final JavaParser.annotation_return annotation() throws RecognitionException {
        JavaParser.annotation_return retval = new JavaParser.annotation_return();
        retval.start = input.LT(1);

        int annotation_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal224=null;
        Token char_literal226=null;
        Token char_literal229=null;
        JavaParser.qualifiedName_return qualifiedName225 =null;

        JavaParser.elementValuePairs_return elementValuePairs227 =null;

        JavaParser.elementValue_return elementValue228 =null;


        Object char_literal224_tree=null;
        Object char_literal226_tree=null;
        Object char_literal229_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 46) ) { return retval; }

            // Java.g:810:5: ( '@' qualifiedName ( '(' ( elementValuePairs | elementValue )? ')' )? )
            // Java.g:810:9: '@' qualifiedName ( '(' ( elementValuePairs | elementValue )? ')' )?
            {
            root_0 = (Object)adaptor.nil();


            char_literal224=(Token)match(input,136,FOLLOW_136_in_annotation4323); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal224_tree = 
            (Object)adaptor.create(char_literal224)
            ;
            adaptor.addChild(root_0, char_literal224_tree);
            }

            pushFollow(FOLLOW_qualifiedName_in_annotation4325);
            qualifiedName225=qualifiedName();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, qualifiedName225.getTree());

            // Java.g:811:9: ( '(' ( elementValuePairs | elementValue )? ')' )?
            int alt81=2;
            int LA81_0 = input.LA(1);

            if ( (LA81_0==LPAREN) ) {
                alt81=1;
            }
            switch (alt81) {
                case 1 :
                    // Java.g:811:13: '(' ( elementValuePairs | elementValue )? ')'
                    {
                    char_literal226=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_annotation4339); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal226_tree = 
                    (Object)adaptor.create(char_literal226)
                    ;
                    adaptor.addChild(root_0, char_literal226_tree);
                    }

                    // Java.g:812:19: ( elementValuePairs | elementValue )?
                    int alt80=3;
                    int LA80_0 = input.LA(1);

                    if ( (LA80_0==IDENTIFIER) ) {
                        int LA80_1 = input.LA(2);

                        if ( (LA80_1==EQ) ) {
                            alt80=1;
                        }
                        else if ( ((LA80_1 >= AMP && LA80_1 <= AMPAMP)||(LA80_1 >= BANGEQ && LA80_1 <= BARBAR)||LA80_1==CARET||LA80_1==DOT||LA80_1==EQEQ||LA80_1==GT||LA80_1==INSTANCEOF||LA80_1==LBRACKET||(LA80_1 >= LPAREN && LA80_1 <= LT)||LA80_1==PERCENT||LA80_1==PLUS||LA80_1==PLUSPLUS||LA80_1==QUES||LA80_1==RPAREN||LA80_1==SLASH||LA80_1==STAR||LA80_1==SUB||LA80_1==SUBSUB) ) {
                            alt80=2;
                        }
                    }
                    else if ( (LA80_0==BANG||LA80_0==BOOLEAN||LA80_0==BYTE||(LA80_0 >= CHAR && LA80_0 <= CHARLITERAL)||(LA80_0 >= DOUBLE && LA80_0 <= DOUBLELITERAL)||LA80_0==FALSE||(LA80_0 >= FLOAT && LA80_0 <= FLOATLITERAL)||LA80_0==INT||LA80_0==INTLITERAL||LA80_0==LBRACE||(LA80_0 >= LONG && LA80_0 <= LPAREN)||(LA80_0 >= NEW && LA80_0 <= NULL)||LA80_0==PLUS||LA80_0==PLUSPLUS||LA80_0==SHORT||(LA80_0 >= STRINGLITERAL && LA80_0 <= SUB)||(LA80_0 >= SUBSUB && LA80_0 <= SUPER)||LA80_0==THIS||LA80_0==TILDE||LA80_0==TRUE||LA80_0==VOID||LA80_0==136) ) {
                        alt80=2;
                    }
                    switch (alt80) {
                        case 1 :
                            // Java.g:812:23: elementValuePairs
                            {
                            pushFollow(FOLLOW_elementValuePairs_in_annotation4366);
                            elementValuePairs227=elementValuePairs();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, elementValuePairs227.getTree());

                            }
                            break;
                        case 2 :
                            // Java.g:813:23: elementValue
                            {
                            pushFollow(FOLLOW_elementValue_in_annotation4390);
                            elementValue228=elementValue();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, elementValue228.getTree());

                            }
                            break;

                    }


                    char_literal229=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_annotation4426); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal229_tree = 
                    (Object)adaptor.create(char_literal229)
                    ;
                    adaptor.addChild(root_0, char_literal229_tree);
                    }

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 46, annotation_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "annotation"


    public static class elementValuePairs_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "elementValuePairs"
    // Java.g:819:1: elementValuePairs : elementValuePair ( ',' elementValuePair )* ;
    public final JavaParser.elementValuePairs_return elementValuePairs() throws RecognitionException {
        JavaParser.elementValuePairs_return retval = new JavaParser.elementValuePairs_return();
        retval.start = input.LT(1);

        int elementValuePairs_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal231=null;
        JavaParser.elementValuePair_return elementValuePair230 =null;

        JavaParser.elementValuePair_return elementValuePair232 =null;


        Object char_literal231_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 47) ) { return retval; }

            // Java.g:820:5: ( elementValuePair ( ',' elementValuePair )* )
            // Java.g:820:9: elementValuePair ( ',' elementValuePair )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_elementValuePair_in_elementValuePairs4458);
            elementValuePair230=elementValuePair();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, elementValuePair230.getTree());

            // Java.g:821:9: ( ',' elementValuePair )*
            loop82:
            do {
                int alt82=2;
                int LA82_0 = input.LA(1);

                if ( (LA82_0==COMMA) ) {
                    alt82=1;
                }


                switch (alt82) {
            	case 1 :
            	    // Java.g:821:10: ',' elementValuePair
            	    {
            	    char_literal231=(Token)match(input,COMMA,FOLLOW_COMMA_in_elementValuePairs4469); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal231_tree = 
            	    (Object)adaptor.create(char_literal231)
            	    ;
            	    adaptor.addChild(root_0, char_literal231_tree);
            	    }

            	    pushFollow(FOLLOW_elementValuePair_in_elementValuePairs4471);
            	    elementValuePair232=elementValuePair();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, elementValuePair232.getTree());

            	    }
            	    break;

            	default :
            	    break loop82;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 47, elementValuePairs_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "elementValuePairs"


    public static class elementValuePair_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "elementValuePair"
    // Java.g:825:1: elementValuePair : IDENTIFIER '=' elementValue ;
    public final JavaParser.elementValuePair_return elementValuePair() throws RecognitionException {
        JavaParser.elementValuePair_return retval = new JavaParser.elementValuePair_return();
        retval.start = input.LT(1);

        int elementValuePair_StartIndex = input.index();

        Object root_0 = null;

        Token IDENTIFIER233=null;
        Token char_literal234=null;
        JavaParser.elementValue_return elementValue235 =null;


        Object IDENTIFIER233_tree=null;
        Object char_literal234_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 48) ) { return retval; }

            // Java.g:826:5: ( IDENTIFIER '=' elementValue )
            // Java.g:826:9: IDENTIFIER '=' elementValue
            {
            root_0 = (Object)adaptor.nil();


            IDENTIFIER233=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_elementValuePair4502); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER233_tree = 
            (Object)adaptor.create(IDENTIFIER233)
            ;
            adaptor.addChild(root_0, IDENTIFIER233_tree);
            }

            char_literal234=(Token)match(input,EQ,FOLLOW_EQ_in_elementValuePair4504); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal234_tree = 
            (Object)adaptor.create(char_literal234)
            ;
            adaptor.addChild(root_0, char_literal234_tree);
            }

            pushFollow(FOLLOW_elementValue_in_elementValuePair4506);
            elementValue235=elementValue();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, elementValue235.getTree());

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 48, elementValuePair_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "elementValuePair"


    public static class elementValue_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "elementValue"
    // Java.g:829:1: elementValue : ( conditionalExpression | annotation | elementValueArrayInitializer );
    public final JavaParser.elementValue_return elementValue() throws RecognitionException {
        JavaParser.elementValue_return retval = new JavaParser.elementValue_return();
        retval.start = input.LT(1);

        int elementValue_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.conditionalExpression_return conditionalExpression236 =null;

        JavaParser.annotation_return annotation237 =null;

        JavaParser.elementValueArrayInitializer_return elementValueArrayInitializer238 =null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 49) ) { return retval; }

            // Java.g:830:5: ( conditionalExpression | annotation | elementValueArrayInitializer )
            int alt83=3;
            switch ( input.LA(1) ) {
            case BANG:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CHARLITERAL:
            case DOUBLE:
            case DOUBLELITERAL:
            case FALSE:
            case FLOAT:
            case FLOATLITERAL:
            case IDENTIFIER:
            case INT:
            case INTLITERAL:
            case LONG:
            case LONGLITERAL:
            case LPAREN:
            case NEW:
            case NULL:
            case PLUS:
            case PLUSPLUS:
            case SHORT:
            case STRINGLITERAL:
            case SUB:
            case SUBSUB:
            case SUPER:
            case THIS:
            case TILDE:
            case TRUE:
            case VOID:
                {
                alt83=1;
                }
                break;
            case 136:
                {
                alt83=2;
                }
                break;
            case LBRACE:
                {
                alt83=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 83, 0, input);

                throw nvae;

            }

            switch (alt83) {
                case 1 :
                    // Java.g:830:9: conditionalExpression
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_conditionalExpression_in_elementValue4526);
                    conditionalExpression236=conditionalExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, conditionalExpression236.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:831:9: annotation
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_annotation_in_elementValue4536);
                    annotation237=annotation();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotation237.getTree());

                    }
                    break;
                case 3 :
                    // Java.g:832:9: elementValueArrayInitializer
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_elementValueArrayInitializer_in_elementValue4546);
                    elementValueArrayInitializer238=elementValueArrayInitializer();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, elementValueArrayInitializer238.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 49, elementValue_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "elementValue"


    public static class elementValueArrayInitializer_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "elementValueArrayInitializer"
    // Java.g:835:1: elementValueArrayInitializer : '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}' ;
    public final JavaParser.elementValueArrayInitializer_return elementValueArrayInitializer() throws RecognitionException {
        JavaParser.elementValueArrayInitializer_return retval = new JavaParser.elementValueArrayInitializer_return();
        retval.start = input.LT(1);

        int elementValueArrayInitializer_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal239=null;
        Token char_literal241=null;
        Token char_literal243=null;
        Token char_literal244=null;
        JavaParser.elementValue_return elementValue240 =null;

        JavaParser.elementValue_return elementValue242 =null;


        Object char_literal239_tree=null;
        Object char_literal241_tree=null;
        Object char_literal243_tree=null;
        Object char_literal244_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 50) ) { return retval; }

            // Java.g:836:5: ( '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}' )
            // Java.g:836:9: '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}'
            {
            root_0 = (Object)adaptor.nil();


            char_literal239=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_elementValueArrayInitializer4566); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal239_tree = 
            (Object)adaptor.create(char_literal239)
            ;
            adaptor.addChild(root_0, char_literal239_tree);
            }

            // Java.g:837:9: ( elementValue ( ',' elementValue )* )?
            int alt85=2;
            int LA85_0 = input.LA(1);

            if ( (LA85_0==BANG||LA85_0==BOOLEAN||LA85_0==BYTE||(LA85_0 >= CHAR && LA85_0 <= CHARLITERAL)||(LA85_0 >= DOUBLE && LA85_0 <= DOUBLELITERAL)||LA85_0==FALSE||(LA85_0 >= FLOAT && LA85_0 <= FLOATLITERAL)||LA85_0==IDENTIFIER||LA85_0==INT||LA85_0==INTLITERAL||LA85_0==LBRACE||(LA85_0 >= LONG && LA85_0 <= LPAREN)||(LA85_0 >= NEW && LA85_0 <= NULL)||LA85_0==PLUS||LA85_0==PLUSPLUS||LA85_0==SHORT||(LA85_0 >= STRINGLITERAL && LA85_0 <= SUB)||(LA85_0 >= SUBSUB && LA85_0 <= SUPER)||LA85_0==THIS||LA85_0==TILDE||LA85_0==TRUE||LA85_0==VOID||LA85_0==136) ) {
                alt85=1;
            }
            switch (alt85) {
                case 1 :
                    // Java.g:837:10: elementValue ( ',' elementValue )*
                    {
                    pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer4577);
                    elementValue240=elementValue();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, elementValue240.getTree());

                    // Java.g:838:13: ( ',' elementValue )*
                    loop84:
                    do {
                        int alt84=2;
                        int LA84_0 = input.LA(1);

                        if ( (LA84_0==COMMA) ) {
                            int LA84_1 = input.LA(2);

                            if ( (LA84_1==BANG||LA84_1==BOOLEAN||LA84_1==BYTE||(LA84_1 >= CHAR && LA84_1 <= CHARLITERAL)||(LA84_1 >= DOUBLE && LA84_1 <= DOUBLELITERAL)||LA84_1==FALSE||(LA84_1 >= FLOAT && LA84_1 <= FLOATLITERAL)||LA84_1==IDENTIFIER||LA84_1==INT||LA84_1==INTLITERAL||LA84_1==LBRACE||(LA84_1 >= LONG && LA84_1 <= LPAREN)||(LA84_1 >= NEW && LA84_1 <= NULL)||LA84_1==PLUS||LA84_1==PLUSPLUS||LA84_1==SHORT||(LA84_1 >= STRINGLITERAL && LA84_1 <= SUB)||(LA84_1 >= SUBSUB && LA84_1 <= SUPER)||LA84_1==THIS||LA84_1==TILDE||LA84_1==TRUE||LA84_1==VOID||LA84_1==136) ) {
                                alt84=1;
                            }


                        }


                        switch (alt84) {
                    	case 1 :
                    	    // Java.g:838:14: ',' elementValue
                    	    {
                    	    char_literal241=(Token)match(input,COMMA,FOLLOW_COMMA_in_elementValueArrayInitializer4592); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal241_tree = 
                    	    (Object)adaptor.create(char_literal241)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal241_tree);
                    	    }

                    	    pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer4594);
                    	    elementValue242=elementValue();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, elementValue242.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop84;
                        }
                    } while (true);


                    }
                    break;

            }


            // Java.g:840:12: ( ',' )?
            int alt86=2;
            int LA86_0 = input.LA(1);

            if ( (LA86_0==COMMA) ) {
                alt86=1;
            }
            switch (alt86) {
                case 1 :
                    // Java.g:840:13: ','
                    {
                    char_literal243=(Token)match(input,COMMA,FOLLOW_COMMA_in_elementValueArrayInitializer4623); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal243_tree = 
                    (Object)adaptor.create(char_literal243)
                    ;
                    adaptor.addChild(root_0, char_literal243_tree);
                    }

                    }
                    break;

            }


            char_literal244=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_elementValueArrayInitializer4627); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal244_tree = 
            (Object)adaptor.create(char_literal244)
            ;
            adaptor.addChild(root_0, char_literal244_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 50, elementValueArrayInitializer_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "elementValueArrayInitializer"


    public static class annotationTypeDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "annotationTypeDeclaration"
    // Java.g:847:1: annotationTypeDeclaration : modifiers '@' 'interface' IDENTIFIER annotationTypeBody ;
    public final JavaParser.annotationTypeDeclaration_return annotationTypeDeclaration() throws RecognitionException {
        JavaParser.annotationTypeDeclaration_return retval = new JavaParser.annotationTypeDeclaration_return();
        retval.start = input.LT(1);

        int annotationTypeDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal246=null;
        Token string_literal247=null;
        Token IDENTIFIER248=null;
        JavaParser.modifiers_return modifiers245 =null;

        JavaParser.annotationTypeBody_return annotationTypeBody249 =null;


        Object char_literal246_tree=null;
        Object string_literal247_tree=null;
        Object IDENTIFIER248_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 51) ) { return retval; }

            // Java.g:848:5: ( modifiers '@' 'interface' IDENTIFIER annotationTypeBody )
            // Java.g:848:9: modifiers '@' 'interface' IDENTIFIER annotationTypeBody
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_modifiers_in_annotationTypeDeclaration4650);
            modifiers245=modifiers();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, modifiers245.getTree());

            char_literal246=(Token)match(input,136,FOLLOW_136_in_annotationTypeDeclaration4652); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal246_tree = 
            (Object)adaptor.create(char_literal246)
            ;
            adaptor.addChild(root_0, char_literal246_tree);
            }

            string_literal247=(Token)match(input,INTERFACE,FOLLOW_INTERFACE_in_annotationTypeDeclaration4662); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal247_tree = 
            (Object)adaptor.create(string_literal247)
            ;
            adaptor.addChild(root_0, string_literal247_tree);
            }

            IDENTIFIER248=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_annotationTypeDeclaration4672); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER248_tree = 
            (Object)adaptor.create(IDENTIFIER248)
            ;
            adaptor.addChild(root_0, IDENTIFIER248_tree);
            }

            pushFollow(FOLLOW_annotationTypeBody_in_annotationTypeDeclaration4682);
            annotationTypeBody249=annotationTypeBody();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, annotationTypeBody249.getTree());

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 51, annotationTypeDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "annotationTypeDeclaration"


    public static class annotationTypeBody_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "annotationTypeBody"
    // Java.g:855:1: annotationTypeBody : '{' ( annotationTypeElementDeclaration )* '}' ;
    public final JavaParser.annotationTypeBody_return annotationTypeBody() throws RecognitionException {
        JavaParser.annotationTypeBody_return retval = new JavaParser.annotationTypeBody_return();
        retval.start = input.LT(1);

        int annotationTypeBody_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal250=null;
        Token char_literal252=null;
        JavaParser.annotationTypeElementDeclaration_return annotationTypeElementDeclaration251 =null;


        Object char_literal250_tree=null;
        Object char_literal252_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 52) ) { return retval; }

            // Java.g:856:5: ( '{' ( annotationTypeElementDeclaration )* '}' )
            // Java.g:856:9: '{' ( annotationTypeElementDeclaration )* '}'
            {
            root_0 = (Object)adaptor.nil();


            char_literal250=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_annotationTypeBody4703); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal250_tree = 
            (Object)adaptor.create(char_literal250)
            ;
            adaptor.addChild(root_0, char_literal250_tree);
            }

            // Java.g:857:9: ( annotationTypeElementDeclaration )*
            loop87:
            do {
                int alt87=2;
                int LA87_0 = input.LA(1);

                if ( (LA87_0==ABSTRACT||LA87_0==BOOLEAN||LA87_0==BYTE||LA87_0==CHAR||LA87_0==CLASS||LA87_0==DOUBLE||LA87_0==ENUM||LA87_0==FINAL||LA87_0==FLOAT||LA87_0==IDENTIFIER||(LA87_0 >= INT && LA87_0 <= INTERFACE)||LA87_0==LONG||LA87_0==LT||LA87_0==NATIVE||(LA87_0 >= PRIVATE && LA87_0 <= PUBLIC)||(LA87_0 >= SEMI && LA87_0 <= SHORT)||(LA87_0 >= STATIC && LA87_0 <= STRICTFP)||LA87_0==SYNCHRONIZED||LA87_0==TRANSIENT||(LA87_0 >= VOID && LA87_0 <= VOLATILE)||LA87_0==136) ) {
                    alt87=1;
                }


                switch (alt87) {
            	case 1 :
            	    // Java.g:857:10: annotationTypeElementDeclaration
            	    {
            	    pushFollow(FOLLOW_annotationTypeElementDeclaration_in_annotationTypeBody4715);
            	    annotationTypeElementDeclaration251=annotationTypeElementDeclaration();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotationTypeElementDeclaration251.getTree());

            	    }
            	    break;

            	default :
            	    break loop87;
                }
            } while (true);


            char_literal252=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_annotationTypeBody4737); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal252_tree = 
            (Object)adaptor.create(char_literal252)
            ;
            adaptor.addChild(root_0, char_literal252_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 52, annotationTypeBody_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "annotationTypeBody"


    public static class annotationTypeElementDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "annotationTypeElementDeclaration"
    // Java.g:865:1: annotationTypeElementDeclaration : ( annotationMethodDeclaration | interfaceFieldDeclaration | normalClassDeclaration | normalInterfaceDeclaration | enumDeclaration | annotationTypeDeclaration | ';' );
    public final JavaParser.annotationTypeElementDeclaration_return annotationTypeElementDeclaration() throws RecognitionException {
        JavaParser.annotationTypeElementDeclaration_return retval = new JavaParser.annotationTypeElementDeclaration_return();
        retval.start = input.LT(1);

        int annotationTypeElementDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal259=null;
        JavaParser.annotationMethodDeclaration_return annotationMethodDeclaration253 =null;

        JavaParser.interfaceFieldDeclaration_return interfaceFieldDeclaration254 =null;

        JavaParser.normalClassDeclaration_return normalClassDeclaration255 =null;

        JavaParser.normalInterfaceDeclaration_return normalInterfaceDeclaration256 =null;

        JavaParser.enumDeclaration_return enumDeclaration257 =null;

        JavaParser.annotationTypeDeclaration_return annotationTypeDeclaration258 =null;


        Object char_literal259_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 53) ) { return retval; }

            // Java.g:866:5: ( annotationMethodDeclaration | interfaceFieldDeclaration | normalClassDeclaration | normalInterfaceDeclaration | enumDeclaration | annotationTypeDeclaration | ';' )
            int alt88=7;
            switch ( input.LA(1) ) {
            case 136:
                {
                int LA88_1 = input.LA(2);

                if ( (synpred118_Java()) ) {
                    alt88=1;
                }
                else if ( (synpred119_Java()) ) {
                    alt88=2;
                }
                else if ( (synpred120_Java()) ) {
                    alt88=3;
                }
                else if ( (synpred121_Java()) ) {
                    alt88=4;
                }
                else if ( (synpred122_Java()) ) {
                    alt88=5;
                }
                else if ( (synpred123_Java()) ) {
                    alt88=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 88, 1, input);

                    throw nvae;

                }
                }
                break;
            case PUBLIC:
                {
                int LA88_2 = input.LA(2);

                if ( (synpred118_Java()) ) {
                    alt88=1;
                }
                else if ( (synpred119_Java()) ) {
                    alt88=2;
                }
                else if ( (synpred120_Java()) ) {
                    alt88=3;
                }
                else if ( (synpred121_Java()) ) {
                    alt88=4;
                }
                else if ( (synpred122_Java()) ) {
                    alt88=5;
                }
                else if ( (synpred123_Java()) ) {
                    alt88=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 88, 2, input);

                    throw nvae;

                }
                }
                break;
            case PROTECTED:
                {
                int LA88_3 = input.LA(2);

                if ( (synpred118_Java()) ) {
                    alt88=1;
                }
                else if ( (synpred119_Java()) ) {
                    alt88=2;
                }
                else if ( (synpred120_Java()) ) {
                    alt88=3;
                }
                else if ( (synpred121_Java()) ) {
                    alt88=4;
                }
                else if ( (synpred122_Java()) ) {
                    alt88=5;
                }
                else if ( (synpred123_Java()) ) {
                    alt88=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 88, 3, input);

                    throw nvae;

                }
                }
                break;
            case PRIVATE:
                {
                int LA88_4 = input.LA(2);

                if ( (synpred118_Java()) ) {
                    alt88=1;
                }
                else if ( (synpred119_Java()) ) {
                    alt88=2;
                }
                else if ( (synpred120_Java()) ) {
                    alt88=3;
                }
                else if ( (synpred121_Java()) ) {
                    alt88=4;
                }
                else if ( (synpred122_Java()) ) {
                    alt88=5;
                }
                else if ( (synpred123_Java()) ) {
                    alt88=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 88, 4, input);

                    throw nvae;

                }
                }
                break;
            case STATIC:
                {
                int LA88_5 = input.LA(2);

                if ( (synpred118_Java()) ) {
                    alt88=1;
                }
                else if ( (synpred119_Java()) ) {
                    alt88=2;
                }
                else if ( (synpred120_Java()) ) {
                    alt88=3;
                }
                else if ( (synpred121_Java()) ) {
                    alt88=4;
                }
                else if ( (synpred122_Java()) ) {
                    alt88=5;
                }
                else if ( (synpred123_Java()) ) {
                    alt88=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 88, 5, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
                {
                int LA88_6 = input.LA(2);

                if ( (synpred118_Java()) ) {
                    alt88=1;
                }
                else if ( (synpred119_Java()) ) {
                    alt88=2;
                }
                else if ( (synpred120_Java()) ) {
                    alt88=3;
                }
                else if ( (synpred121_Java()) ) {
                    alt88=4;
                }
                else if ( (synpred122_Java()) ) {
                    alt88=5;
                }
                else if ( (synpred123_Java()) ) {
                    alt88=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 88, 6, input);

                    throw nvae;

                }
                }
                break;
            case FINAL:
                {
                int LA88_7 = input.LA(2);

                if ( (synpred118_Java()) ) {
                    alt88=1;
                }
                else if ( (synpred119_Java()) ) {
                    alt88=2;
                }
                else if ( (synpred120_Java()) ) {
                    alt88=3;
                }
                else if ( (synpred121_Java()) ) {
                    alt88=4;
                }
                else if ( (synpred122_Java()) ) {
                    alt88=5;
                }
                else if ( (synpred123_Java()) ) {
                    alt88=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 88, 7, input);

                    throw nvae;

                }
                }
                break;
            case NATIVE:
                {
                int LA88_8 = input.LA(2);

                if ( (synpred118_Java()) ) {
                    alt88=1;
                }
                else if ( (synpred119_Java()) ) {
                    alt88=2;
                }
                else if ( (synpred120_Java()) ) {
                    alt88=3;
                }
                else if ( (synpred121_Java()) ) {
                    alt88=4;
                }
                else if ( (synpred122_Java()) ) {
                    alt88=5;
                }
                else if ( (synpred123_Java()) ) {
                    alt88=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 88, 8, input);

                    throw nvae;

                }
                }
                break;
            case SYNCHRONIZED:
                {
                int LA88_9 = input.LA(2);

                if ( (synpred118_Java()) ) {
                    alt88=1;
                }
                else if ( (synpred119_Java()) ) {
                    alt88=2;
                }
                else if ( (synpred120_Java()) ) {
                    alt88=3;
                }
                else if ( (synpred121_Java()) ) {
                    alt88=4;
                }
                else if ( (synpred122_Java()) ) {
                    alt88=5;
                }
                else if ( (synpred123_Java()) ) {
                    alt88=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 88, 9, input);

                    throw nvae;

                }
                }
                break;
            case TRANSIENT:
                {
                int LA88_10 = input.LA(2);

                if ( (synpred118_Java()) ) {
                    alt88=1;
                }
                else if ( (synpred119_Java()) ) {
                    alt88=2;
                }
                else if ( (synpred120_Java()) ) {
                    alt88=3;
                }
                else if ( (synpred121_Java()) ) {
                    alt88=4;
                }
                else if ( (synpred122_Java()) ) {
                    alt88=5;
                }
                else if ( (synpred123_Java()) ) {
                    alt88=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 88, 10, input);

                    throw nvae;

                }
                }
                break;
            case VOLATILE:
                {
                int LA88_11 = input.LA(2);

                if ( (synpred118_Java()) ) {
                    alt88=1;
                }
                else if ( (synpred119_Java()) ) {
                    alt88=2;
                }
                else if ( (synpred120_Java()) ) {
                    alt88=3;
                }
                else if ( (synpred121_Java()) ) {
                    alt88=4;
                }
                else if ( (synpred122_Java()) ) {
                    alt88=5;
                }
                else if ( (synpred123_Java()) ) {
                    alt88=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 88, 11, input);

                    throw nvae;

                }
                }
                break;
            case STRICTFP:
                {
                int LA88_12 = input.LA(2);

                if ( (synpred118_Java()) ) {
                    alt88=1;
                }
                else if ( (synpred119_Java()) ) {
                    alt88=2;
                }
                else if ( (synpred120_Java()) ) {
                    alt88=3;
                }
                else if ( (synpred121_Java()) ) {
                    alt88=4;
                }
                else if ( (synpred122_Java()) ) {
                    alt88=5;
                }
                else if ( (synpred123_Java()) ) {
                    alt88=6;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 88, 12, input);

                    throw nvae;

                }
                }
                break;
            case IDENTIFIER:
                {
                int LA88_13 = input.LA(2);

                if ( (synpred118_Java()) ) {
                    alt88=1;
                }
                else if ( (synpred119_Java()) ) {
                    alt88=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 88, 13, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
                {
                int LA88_14 = input.LA(2);

                if ( (synpred118_Java()) ) {
                    alt88=1;
                }
                else if ( (synpred119_Java()) ) {
                    alt88=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 88, 14, input);

                    throw nvae;

                }
                }
                break;
            case CLASS:
                {
                alt88=3;
                }
                break;
            case INTERFACE:
                {
                alt88=4;
                }
                break;
            case ENUM:
                {
                alt88=5;
                }
                break;
            case SEMI:
                {
                alt88=7;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 88, 0, input);

                throw nvae;

            }

            switch (alt88) {
                case 1 :
                    // Java.g:866:9: annotationMethodDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_annotationMethodDeclaration_in_annotationTypeElementDeclaration4759);
                    annotationMethodDeclaration253=annotationMethodDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotationMethodDeclaration253.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:867:9: interfaceFieldDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_interfaceFieldDeclaration_in_annotationTypeElementDeclaration4769);
                    interfaceFieldDeclaration254=interfaceFieldDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, interfaceFieldDeclaration254.getTree());

                    }
                    break;
                case 3 :
                    // Java.g:868:9: normalClassDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_normalClassDeclaration_in_annotationTypeElementDeclaration4779);
                    normalClassDeclaration255=normalClassDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, normalClassDeclaration255.getTree());

                    }
                    break;
                case 4 :
                    // Java.g:869:9: normalInterfaceDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_normalInterfaceDeclaration_in_annotationTypeElementDeclaration4789);
                    normalInterfaceDeclaration256=normalInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, normalInterfaceDeclaration256.getTree());

                    }
                    break;
                case 5 :
                    // Java.g:870:9: enumDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_enumDeclaration_in_annotationTypeElementDeclaration4799);
                    enumDeclaration257=enumDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enumDeclaration257.getTree());

                    }
                    break;
                case 6 :
                    // Java.g:871:9: annotationTypeDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_annotationTypeDeclaration_in_annotationTypeElementDeclaration4809);
                    annotationTypeDeclaration258=annotationTypeDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotationTypeDeclaration258.getTree());

                    }
                    break;
                case 7 :
                    // Java.g:872:9: ';'
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal259=(Token)match(input,SEMI,FOLLOW_SEMI_in_annotationTypeElementDeclaration4819); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal259_tree = 
                    (Object)adaptor.create(char_literal259)
                    ;
                    adaptor.addChild(root_0, char_literal259_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 53, annotationTypeElementDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "annotationTypeElementDeclaration"


    public static class annotationMethodDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "annotationMethodDeclaration"
    // Java.g:875:1: annotationMethodDeclaration : modifiers type IDENTIFIER '(' ')' ( 'default' elementValue )? ';' ;
    public final JavaParser.annotationMethodDeclaration_return annotationMethodDeclaration() throws RecognitionException {
        JavaParser.annotationMethodDeclaration_return retval = new JavaParser.annotationMethodDeclaration_return();
        retval.start = input.LT(1);

        int annotationMethodDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token IDENTIFIER262=null;
        Token char_literal263=null;
        Token char_literal264=null;
        Token string_literal265=null;
        Token char_literal267=null;
        JavaParser.modifiers_return modifiers260 =null;

        JavaParser.type_return type261 =null;

        JavaParser.elementValue_return elementValue266 =null;


        Object IDENTIFIER262_tree=null;
        Object char_literal263_tree=null;
        Object char_literal264_tree=null;
        Object string_literal265_tree=null;
        Object char_literal267_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 54) ) { return retval; }

            // Java.g:876:5: ( modifiers type IDENTIFIER '(' ')' ( 'default' elementValue )? ';' )
            // Java.g:876:9: modifiers type IDENTIFIER '(' ')' ( 'default' elementValue )? ';'
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_modifiers_in_annotationMethodDeclaration4839);
            modifiers260=modifiers();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, modifiers260.getTree());

            pushFollow(FOLLOW_type_in_annotationMethodDeclaration4841);
            type261=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type261.getTree());

            IDENTIFIER262=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_annotationMethodDeclaration4843); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER262_tree = 
            (Object)adaptor.create(IDENTIFIER262)
            ;
            adaptor.addChild(root_0, IDENTIFIER262_tree);
            }

            char_literal263=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_annotationMethodDeclaration4853); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal263_tree = 
            (Object)adaptor.create(char_literal263)
            ;
            adaptor.addChild(root_0, char_literal263_tree);
            }

            char_literal264=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_annotationMethodDeclaration4855); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal264_tree = 
            (Object)adaptor.create(char_literal264)
            ;
            adaptor.addChild(root_0, char_literal264_tree);
            }

            // Java.g:877:17: ( 'default' elementValue )?
            int alt89=2;
            int LA89_0 = input.LA(1);

            if ( (LA89_0==DEFAULT) ) {
                alt89=1;
            }
            switch (alt89) {
                case 1 :
                    // Java.g:877:18: 'default' elementValue
                    {
                    string_literal265=(Token)match(input,DEFAULT,FOLLOW_DEFAULT_in_annotationMethodDeclaration4858); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal265_tree = 
                    (Object)adaptor.create(string_literal265)
                    ;
                    adaptor.addChild(root_0, string_literal265_tree);
                    }

                    pushFollow(FOLLOW_elementValue_in_annotationMethodDeclaration4860);
                    elementValue266=elementValue();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, elementValue266.getTree());

                    }
                    break;

            }


            char_literal267=(Token)match(input,SEMI,FOLLOW_SEMI_in_annotationMethodDeclaration4889); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal267_tree = 
            (Object)adaptor.create(char_literal267)
            ;
            adaptor.addChild(root_0, char_literal267_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 54, annotationMethodDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "annotationMethodDeclaration"


    public static class block_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "block"
    // Java.g:882:1: block : '{' ( blockStatement )* '}' -> '{' ^( WMETHODCONTAINER ( blockStatement )* ) '}' ;
    public final JavaParser.block_return block() throws RecognitionException {
        JavaParser.block_return retval = new JavaParser.block_return();
        retval.start = input.LT(1);

        int block_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal268=null;
        Token char_literal270=null;
        JavaParser.blockStatement_return blockStatement269 =null;


        Object char_literal268_tree=null;
        Object char_literal270_tree=null;
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleSubtreeStream stream_blockStatement=new RewriteRuleSubtreeStream(adaptor,"rule blockStatement");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 55) ) { return retval; }

            // Java.g:883:5: ( '{' ( blockStatement )* '}' -> '{' ^( WMETHODCONTAINER ( blockStatement )* ) '}' )
            // Java.g:883:9: '{' ( blockStatement )* '}'
            {
            char_literal268=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_block4913); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LBRACE.add(char_literal268);


            // Java.g:884:9: ( blockStatement )*
            loop90:
            do {
                int alt90=2;
                int LA90_0 = input.LA(1);

                if ( (LA90_0==EOF||LA90_0==ABSTRACT||(LA90_0 >= ASSERT && LA90_0 <= BANG)||(LA90_0 >= BOOLEAN && LA90_0 <= BYTE)||(LA90_0 >= CHAR && LA90_0 <= CLASS)||LA90_0==CONTINUE||LA90_0==DO||(LA90_0 >= DOUBLE && LA90_0 <= DOUBLELITERAL)||LA90_0==ENUM||(LA90_0 >= FALSE && LA90_0 <= FINAL)||(LA90_0 >= FLOAT && LA90_0 <= FOR)||(LA90_0 >= IDENTIFIER && LA90_0 <= IF)||(LA90_0 >= INT && LA90_0 <= INTLITERAL)||LA90_0==LBRACE||(LA90_0 >= LONG && LA90_0 <= LT)||(LA90_0 >= NATIVE && LA90_0 <= NULL)||LA90_0==PLUS||(LA90_0 >= PLUSPLUS && LA90_0 <= PUBLIC)||LA90_0==RETURN||(LA90_0 >= SEMI && LA90_0 <= SHORT)||(LA90_0 >= STATIC && LA90_0 <= SUB)||(LA90_0 >= SUBSUB && LA90_0 <= SYNCHRONIZED)||(LA90_0 >= THIS && LA90_0 <= THROW)||(LA90_0 >= TILDE && LA90_0 <= VOLATILE)||LA90_0==WHILE||LA90_0==136) ) {
                    alt90=1;
                }


                switch (alt90) {
            	case 1 :
            	    // Java.g:884:10: blockStatement
            	    {
            	    pushFollow(FOLLOW_blockStatement_in_block4924);
            	    blockStatement269=blockStatement();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_blockStatement.add(blockStatement269.getTree());

            	    }
            	    break;

            	default :
            	    break loop90;
                }
            } while (true);


            char_literal270=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_block4945); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RBRACE.add(char_literal270);


            // AST REWRITE
            // elements: LBRACE, RBRACE, blockStatement
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {

            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 887:3: -> '{' ^( WMETHODCONTAINER ( blockStatement )* ) '}'
            {
                adaptor.addChild(root_0, 
                stream_LBRACE.nextNode()
                );

                // Java.g:887:10: ^( WMETHODCONTAINER ( blockStatement )* )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(WMETHODCONTAINER, "WMETHODCONTAINER")
                , root_1);

                // Java.g:888:9: ( blockStatement )*
                while ( stream_blockStatement.hasNext() ) {
                    adaptor.addChild(root_1, stream_blockStatement.nextTree());

                }
                stream_blockStatement.reset();

                adaptor.addChild(root_0, root_1);
                }

                adaptor.addChild(root_0, 
                stream_RBRACE.nextNode()
                );

            }


            retval.tree = root_0;
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 55, block_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "block"


    public static class blockStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "blockStatement"
    // Java.g:917:1: blockStatement : ( localVariableDeclarationStatement -> ^( WSTATEMENT localVariableDeclarationStatement ) | classOrInterfaceDeclaration | statement );
    public final JavaParser.blockStatement_return blockStatement() throws RecognitionException {
        JavaParser.blockStatement_return retval = new JavaParser.blockStatement_return();
        retval.start = input.LT(1);

        int blockStatement_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.localVariableDeclarationStatement_return localVariableDeclarationStatement271 =null;

        JavaParser.classOrInterfaceDeclaration_return classOrInterfaceDeclaration272 =null;

        JavaParser.statement_return statement273 =null;


        RewriteRuleSubtreeStream stream_localVariableDeclarationStatement=new RewriteRuleSubtreeStream(adaptor,"rule localVariableDeclarationStatement");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 56) ) { return retval; }

            // Java.g:918:5: ( localVariableDeclarationStatement -> ^( WSTATEMENT localVariableDeclarationStatement ) | classOrInterfaceDeclaration | statement )
            int alt91=3;
            switch ( input.LA(1) ) {
            case FINAL:
                {
                int LA91_1 = input.LA(2);

                if ( (synpred126_Java()) ) {
                    alt91=1;
                }
                else if ( (synpred127_Java()) ) {
                    alt91=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 91, 1, input);

                    throw nvae;

                }
                }
                break;
            case 136:
                {
                int LA91_2 = input.LA(2);

                if ( (synpred126_Java()) ) {
                    alt91=1;
                }
                else if ( (synpred127_Java()) ) {
                    alt91=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 91, 2, input);

                    throw nvae;

                }
                }
                break;
            case IDENTIFIER:
                {
                int LA91_3 = input.LA(2);

                if ( (synpred126_Java()) ) {
                    alt91=1;
                }
                else if ( (true) ) {
                    alt91=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 91, 3, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
                {
                int LA91_4 = input.LA(2);

                if ( (synpred126_Java()) ) {
                    alt91=1;
                }
                else if ( (true) ) {
                    alt91=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 91, 4, input);

                    throw nvae;

                }
                }
                break;
            case ABSTRACT:
            case CLASS:
            case ENUM:
            case INTERFACE:
            case NATIVE:
            case PRIVATE:
            case PROTECTED:
            case PUBLIC:
            case STATIC:
            case STRICTFP:
            case TRANSIENT:
            case VOLATILE:
                {
                alt91=2;
                }
                break;
            case SYNCHRONIZED:
                {
                int LA91_11 = input.LA(2);

                if ( (synpred127_Java()) ) {
                    alt91=2;
                }
                else if ( (true) ) {
                    alt91=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 91, 11, input);

                    throw nvae;

                }
                }
                break;
            case ASSERT:
            case BANG:
            case BREAK:
            case CHARLITERAL:
            case CONTINUE:
            case DO:
            case DOUBLELITERAL:
            case FALSE:
            case FLOATLITERAL:
            case FOR:
            case IF:
            case INTLITERAL:
            case LBRACE:
            case LONGLITERAL:
            case LPAREN:
            case NEW:
            case NULL:
            case PLUS:
            case PLUSPLUS:
            case RETURN:
            case SEMI:
            case STRINGLITERAL:
            case SUB:
            case SUBSUB:
            case SUPER:
            case SWITCH:
            case THIS:
            case THROW:
            case TILDE:
            case TRUE:
            case TRY:
            case VOID:
            case WHILE:
                {
                alt91=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 91, 0, input);

                throw nvae;

            }

            switch (alt91) {
                case 1 :
                    // Java.g:918:9: localVariableDeclarationStatement
                    {
                    pushFollow(FOLLOW_localVariableDeclarationStatement_in_blockStatement5010);
                    localVariableDeclarationStatement271=localVariableDeclarationStatement();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_localVariableDeclarationStatement.add(localVariableDeclarationStatement271.getTree());

                    // AST REWRITE
                    // elements: localVariableDeclarationStatement
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 918:43: -> ^( WSTATEMENT localVariableDeclarationStatement )
                    {
                        // Java.g:918:46: ^( WSTATEMENT localVariableDeclarationStatement )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, stream_localVariableDeclarationStatement.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // Java.g:919:9: classOrInterfaceDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_blockStatement5028);
                    classOrInterfaceDeclaration272=classOrInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classOrInterfaceDeclaration272.getTree());

                    }
                    break;
                case 3 :
                    // Java.g:920:9: statement
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_statement_in_blockStatement5038);
                    statement273=statement();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, statement273.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 56, blockStatement_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "blockStatement"


    public static class localVariableDeclarationStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "localVariableDeclarationStatement"
    // Java.g:924:1: localVariableDeclarationStatement : localVariableDeclaration ';' ;
    public final JavaParser.localVariableDeclarationStatement_return localVariableDeclarationStatement() throws RecognitionException {
        JavaParser.localVariableDeclarationStatement_return retval = new JavaParser.localVariableDeclarationStatement_return();
        retval.start = input.LT(1);

        int localVariableDeclarationStatement_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal275=null;
        JavaParser.localVariableDeclaration_return localVariableDeclaration274 =null;


        Object char_literal275_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 57) ) { return retval; }

            // Java.g:925:5: ( localVariableDeclaration ';' )
            // Java.g:925:9: localVariableDeclaration ';'
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement5059);
            localVariableDeclaration274=localVariableDeclaration();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, localVariableDeclaration274.getTree());

            char_literal275=(Token)match(input,SEMI,FOLLOW_SEMI_in_localVariableDeclarationStatement5069); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal275_tree = 
            (Object)adaptor.create(char_literal275)
            ;
            adaptor.addChild(root_0, char_literal275_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 57, localVariableDeclarationStatement_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "localVariableDeclarationStatement"


    public static class localVariableDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "localVariableDeclaration"
    // Java.g:929:1: localVariableDeclaration : variableModifiers type variableDeclarator ( ',' variableDeclarator )* ;
    public final JavaParser.localVariableDeclaration_return localVariableDeclaration() throws RecognitionException {
        JavaParser.localVariableDeclaration_return retval = new JavaParser.localVariableDeclaration_return();
        retval.start = input.LT(1);

        int localVariableDeclaration_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal279=null;
        JavaParser.variableModifiers_return variableModifiers276 =null;

        JavaParser.type_return type277 =null;

        JavaParser.variableDeclarator_return variableDeclarator278 =null;

        JavaParser.variableDeclarator_return variableDeclarator280 =null;


        Object char_literal279_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 58) ) { return retval; }

            // Java.g:930:5: ( variableModifiers type variableDeclarator ( ',' variableDeclarator )* )
            // Java.g:930:9: variableModifiers type variableDeclarator ( ',' variableDeclarator )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_variableModifiers_in_localVariableDeclaration5089);
            variableModifiers276=variableModifiers();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, variableModifiers276.getTree());

            pushFollow(FOLLOW_type_in_localVariableDeclaration5091);
            type277=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type277.getTree());

            pushFollow(FOLLOW_variableDeclarator_in_localVariableDeclaration5101);
            variableDeclarator278=variableDeclarator();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, variableDeclarator278.getTree());

            // Java.g:932:9: ( ',' variableDeclarator )*
            loop92:
            do {
                int alt92=2;
                int LA92_0 = input.LA(1);

                if ( (LA92_0==COMMA) ) {
                    alt92=1;
                }


                switch (alt92) {
            	case 1 :
            	    // Java.g:932:10: ',' variableDeclarator
            	    {
            	    char_literal279=(Token)match(input,COMMA,FOLLOW_COMMA_in_localVariableDeclaration5112); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal279_tree = 
            	    (Object)adaptor.create(char_literal279)
            	    ;
            	    adaptor.addChild(root_0, char_literal279_tree);
            	    }

            	    pushFollow(FOLLOW_variableDeclarator_in_localVariableDeclaration5114);
            	    variableDeclarator280=variableDeclarator();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, variableDeclarator280.getTree());

            	    }
            	    break;

            	default :
            	    break loop92;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 58, localVariableDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "localVariableDeclaration"


    public static class statement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "statement"
    // Java.g:936:1: statement : ( block | 'assert' expression ( ':' expression )? ';' -> ^( WSTATEMENT 'assert' expression ( ':' expression )? ';' ) | 'if' parExpression statement ( 'else' statement )? -> ^( WSTATEMENT 'if' parExpression ) statement ( ^( WSTATEMENT 'else' ) statement )? | forstatement | 'while' parExpression statement -> ^( WSTATEMENT 'while' parExpression ) statement | 'do' statement 'while' parExpression ';' -> ^( WSTATEMENT 'do' ) statement ^( WSTATEMENT 'while' parExpression ';' ) | trystatement | 'switch' parExpression '{' switchBlockStatementGroups '}' -> ^( WSTATEMENT 'switch' parExpression ) '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block -> ^( WSTATEMENT 'synchronized' parExpression ) block | 'return' ( expression )? ';' -> ^( WSTATEMENT 'return' ( expression )? ';' ) | 'throw' expression ';' -> ^( WSTATEMENT 'throw' expression ';' ) | 'break' ( IDENTIFIER )? ';' -> ^( WSTATEMENT 'break' ( IDENTIFIER )? ';' ) | 'continue' ( IDENTIFIER )? ';' -> ^( WSTATEMENT 'continue' ( IDENTIFIER )? ';' ) | expression ';' -> ^( WSTATEMENT expression ';' ) | IDENTIFIER ':' statement -> ^( WSTATEMENT IDENTIFIER ':' ) statement | ';' -> ^( WSTATEMENT ';' ) );
    public final JavaParser.statement_return statement() throws RecognitionException {
        JavaParser.statement_return retval = new JavaParser.statement_return();
        retval.start = input.LT(1);

        int statement_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal282=null;
        Token char_literal284=null;
        Token char_literal286=null;
        Token string_literal287=null;
        Token string_literal290=null;
        Token string_literal293=null;
        Token string_literal296=null;
        Token string_literal298=null;
        Token char_literal300=null;
        Token string_literal302=null;
        Token char_literal304=null;
        Token char_literal306=null;
        Token string_literal307=null;
        Token string_literal310=null;
        Token char_literal312=null;
        Token string_literal313=null;
        Token char_literal315=null;
        Token string_literal316=null;
        Token IDENTIFIER317=null;
        Token char_literal318=null;
        Token string_literal319=null;
        Token IDENTIFIER320=null;
        Token char_literal321=null;
        Token char_literal323=null;
        Token IDENTIFIER324=null;
        Token char_literal325=null;
        Token char_literal327=null;
        JavaParser.block_return block281 =null;

        JavaParser.expression_return expression283 =null;

        JavaParser.expression_return expression285 =null;

        JavaParser.parExpression_return parExpression288 =null;

        JavaParser.statement_return statement289 =null;

        JavaParser.statement_return statement291 =null;

        JavaParser.forstatement_return forstatement292 =null;

        JavaParser.parExpression_return parExpression294 =null;

        JavaParser.statement_return statement295 =null;

        JavaParser.statement_return statement297 =null;

        JavaParser.parExpression_return parExpression299 =null;

        JavaParser.trystatement_return trystatement301 =null;

        JavaParser.parExpression_return parExpression303 =null;

        JavaParser.switchBlockStatementGroups_return switchBlockStatementGroups305 =null;

        JavaParser.parExpression_return parExpression308 =null;

        JavaParser.block_return block309 =null;

        JavaParser.expression_return expression311 =null;

        JavaParser.expression_return expression314 =null;

        JavaParser.expression_return expression322 =null;

        JavaParser.statement_return statement326 =null;


        Object string_literal282_tree=null;
        Object char_literal284_tree=null;
        Object char_literal286_tree=null;
        Object string_literal287_tree=null;
        Object string_literal290_tree=null;
        Object string_literal293_tree=null;
        Object string_literal296_tree=null;
        Object string_literal298_tree=null;
        Object char_literal300_tree=null;
        Object string_literal302_tree=null;
        Object char_literal304_tree=null;
        Object char_literal306_tree=null;
        Object string_literal307_tree=null;
        Object string_literal310_tree=null;
        Object char_literal312_tree=null;
        Object string_literal313_tree=null;
        Object char_literal315_tree=null;
        Object string_literal316_tree=null;
        Object IDENTIFIER317_tree=null;
        Object char_literal318_tree=null;
        Object string_literal319_tree=null;
        Object IDENTIFIER320_tree=null;
        Object char_literal321_tree=null;
        Object char_literal323_tree=null;
        Object IDENTIFIER324_tree=null;
        Object char_literal325_tree=null;
        Object char_literal327_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_SYNCHRONIZED=new RewriteRuleTokenStream(adaptor,"token SYNCHRONIZED");
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_WHILE=new RewriteRuleTokenStream(adaptor,"token WHILE");
        RewriteRuleTokenStream stream_CONTINUE=new RewriteRuleTokenStream(adaptor,"token CONTINUE");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_SWITCH=new RewriteRuleTokenStream(adaptor,"token SWITCH");
        RewriteRuleTokenStream stream_ELSE=new RewriteRuleTokenStream(adaptor,"token ELSE");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleTokenStream stream_RETURN=new RewriteRuleTokenStream(adaptor,"token RETURN");
        RewriteRuleTokenStream stream_DO=new RewriteRuleTokenStream(adaptor,"token DO");
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleTokenStream stream_ASSERT=new RewriteRuleTokenStream(adaptor,"token ASSERT");
        RewriteRuleTokenStream stream_BREAK=new RewriteRuleTokenStream(adaptor,"token BREAK");
        RewriteRuleTokenStream stream_THROW=new RewriteRuleTokenStream(adaptor,"token THROW");
        RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
        RewriteRuleSubtreeStream stream_statement=new RewriteRuleSubtreeStream(adaptor,"rule statement");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        RewriteRuleSubtreeStream stream_parExpression=new RewriteRuleSubtreeStream(adaptor,"rule parExpression");
        RewriteRuleSubtreeStream stream_switchBlockStatementGroups=new RewriteRuleSubtreeStream(adaptor,"rule switchBlockStatementGroups");
        RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 59) ) { return retval; }

            // Java.g:937:5: ( block | 'assert' expression ( ':' expression )? ';' -> ^( WSTATEMENT 'assert' expression ( ':' expression )? ';' ) | 'if' parExpression statement ( 'else' statement )? -> ^( WSTATEMENT 'if' parExpression ) statement ( ^( WSTATEMENT 'else' ) statement )? | forstatement | 'while' parExpression statement -> ^( WSTATEMENT 'while' parExpression ) statement | 'do' statement 'while' parExpression ';' -> ^( WSTATEMENT 'do' ) statement ^( WSTATEMENT 'while' parExpression ';' ) | trystatement | 'switch' parExpression '{' switchBlockStatementGroups '}' -> ^( WSTATEMENT 'switch' parExpression ) '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block -> ^( WSTATEMENT 'synchronized' parExpression ) block | 'return' ( expression )? ';' -> ^( WSTATEMENT 'return' ( expression )? ';' ) | 'throw' expression ';' -> ^( WSTATEMENT 'throw' expression ';' ) | 'break' ( IDENTIFIER )? ';' -> ^( WSTATEMENT 'break' ( IDENTIFIER )? ';' ) | 'continue' ( IDENTIFIER )? ';' -> ^( WSTATEMENT 'continue' ( IDENTIFIER )? ';' ) | expression ';' -> ^( WSTATEMENT expression ';' ) | IDENTIFIER ':' statement -> ^( WSTATEMENT IDENTIFIER ':' ) statement | ';' -> ^( WSTATEMENT ';' ) )
            int alt98=16;
            switch ( input.LA(1) ) {
            case LBRACE:
                {
                alt98=1;
                }
                break;
            case ASSERT:
                {
                alt98=2;
                }
                break;
            case IF:
                {
                alt98=3;
                }
                break;
            case FOR:
                {
                alt98=4;
                }
                break;
            case WHILE:
                {
                alt98=5;
                }
                break;
            case DO:
                {
                alt98=6;
                }
                break;
            case TRY:
                {
                alt98=7;
                }
                break;
            case SWITCH:
                {
                alt98=8;
                }
                break;
            case SYNCHRONIZED:
                {
                alt98=9;
                }
                break;
            case RETURN:
                {
                alt98=10;
                }
                break;
            case THROW:
                {
                alt98=11;
                }
                break;
            case BREAK:
                {
                alt98=12;
                }
                break;
            case CONTINUE:
                {
                alt98=13;
                }
                break;
            case BANG:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CHARLITERAL:
            case DOUBLE:
            case DOUBLELITERAL:
            case FALSE:
            case FLOAT:
            case FLOATLITERAL:
            case INT:
            case INTLITERAL:
            case LONG:
            case LONGLITERAL:
            case LPAREN:
            case NEW:
            case NULL:
            case PLUS:
            case PLUSPLUS:
            case SHORT:
            case STRINGLITERAL:
            case SUB:
            case SUBSUB:
            case SUPER:
            case THIS:
            case TILDE:
            case TRUE:
            case VOID:
                {
                alt98=14;
                }
                break;
            case IDENTIFIER:
                {
                int LA98_15 = input.LA(2);

                if ( (LA98_15==COLON) ) {
                    alt98=15;
                }
                else if ( ((LA98_15 >= AMP && LA98_15 <= AMPEQ)||(LA98_15 >= BANGEQ && LA98_15 <= BAREQ)||(LA98_15 >= CARET && LA98_15 <= CARETEQ)||LA98_15==DOT||(LA98_15 >= EQ && LA98_15 <= EQEQ)||LA98_15==GT||LA98_15==INSTANCEOF||LA98_15==LBRACKET||(LA98_15 >= LPAREN && LA98_15 <= LT)||(LA98_15 >= PERCENT && LA98_15 <= PLUSPLUS)||LA98_15==QUES||LA98_15==SEMI||(LA98_15 >= SLASH && LA98_15 <= STAREQ)||(LA98_15 >= SUB && LA98_15 <= SUBSUB)) ) {
                    alt98=14;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 98, 15, input);

                    throw nvae;

                }
                }
                break;
            case SEMI:
                {
                alt98=16;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 98, 0, input);

                throw nvae;

            }

            switch (alt98) {
                case 1 :
                    // Java.g:937:9: block
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_block_in_statement5145);
                    block281=block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, block281.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:938:9: 'assert' expression ( ':' expression )? ';'
                    {
                    string_literal282=(Token)match(input,ASSERT,FOLLOW_ASSERT_in_statement5155); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ASSERT.add(string_literal282);


                    pushFollow(FOLLOW_expression_in_statement5158);
                    expression283=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_expression.add(expression283.getTree());

                    // Java.g:938:30: ( ':' expression )?
                    int alt93=2;
                    int LA93_0 = input.LA(1);

                    if ( (LA93_0==COLON) ) {
                        alt93=1;
                    }
                    switch (alt93) {
                        case 1 :
                            // Java.g:938:31: ':' expression
                            {
                            char_literal284=(Token)match(input,COLON,FOLLOW_COLON_in_statement5161); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_COLON.add(char_literal284);


                            pushFollow(FOLLOW_expression_in_statement5163);
                            expression285=expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_expression.add(expression285.getTree());

                            }
                            break;

                    }


                    char_literal286=(Token)match(input,SEMI,FOLLOW_SEMI_in_statement5167); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEMI.add(char_literal286);


                    // AST REWRITE
                    // elements: COLON, SEMI, ASSERT, expression, expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 938:52: -> ^( WSTATEMENT 'assert' expression ( ':' expression )? ';' )
                    {
                        // Java.g:938:55: ^( WSTATEMENT 'assert' expression ( ':' expression )? ';' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_ASSERT.nextNode()
                        );

                        adaptor.addChild(root_1, stream_expression.nextTree());

                        // Java.g:938:89: ( ':' expression )?
                        if ( stream_COLON.hasNext()||stream_expression.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_COLON.nextNode()
                            );

                            adaptor.addChild(root_1, stream_expression.nextTree());

                        }
                        stream_COLON.reset();
                        stream_expression.reset();

                        adaptor.addChild(root_1, 
                        stream_SEMI.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 3 :
                    // Java.g:939:9: 'if' parExpression statement ( 'else' statement )?
                    {
                    string_literal287=(Token)match(input,IF,FOLLOW_IF_in_statement5197); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IF.add(string_literal287);


                    pushFollow(FOLLOW_parExpression_in_statement5199);
                    parExpression288=parExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_parExpression.add(parExpression288.getTree());

                    pushFollow(FOLLOW_statement_in_statement5201);
                    statement289=statement();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_statement.add(statement289.getTree());

                    // Java.g:939:38: ( 'else' statement )?
                    int alt94=2;
                    int LA94_0 = input.LA(1);

                    if ( (LA94_0==ELSE) ) {
                        int LA94_1 = input.LA(2);

                        if ( (synpred132_Java()) ) {
                            alt94=1;
                        }
                    }
                    switch (alt94) {
                        case 1 :
                            // Java.g:939:39: 'else' statement
                            {
                            string_literal290=(Token)match(input,ELSE,FOLLOW_ELSE_in_statement5204); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_ELSE.add(string_literal290);


                            pushFollow(FOLLOW_statement_in_statement5206);
                            statement291=statement();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_statement.add(statement291.getTree());

                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: IF, statement, statement, parExpression, ELSE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 939:58: -> ^( WSTATEMENT 'if' parExpression ) statement ( ^( WSTATEMENT 'else' ) statement )?
                    {
                        // Java.g:939:61: ^( WSTATEMENT 'if' parExpression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_IF.nextNode()
                        );

                        adaptor.addChild(root_1, stream_parExpression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                        adaptor.addChild(root_0, stream_statement.nextTree());

                        // Java.g:939:104: ( ^( WSTATEMENT 'else' ) statement )?
                        if ( stream_statement.hasNext()||stream_ELSE.hasNext() ) {
                            // Java.g:939:105: ^( WSTATEMENT 'else' )
                            {
                            Object root_1 = (Object)adaptor.nil();
                            root_1 = (Object)adaptor.becomeRoot(
                            (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                            , root_1);

                            adaptor.addChild(root_1, 
                            stream_ELSE.nextNode()
                            );

                            adaptor.addChild(root_0, root_1);
                            }

                            adaptor.addChild(root_0, stream_statement.nextTree());

                        }
                        stream_statement.reset();
                        stream_ELSE.reset();

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 4 :
                    // Java.g:940:9: forstatement
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_forstatement_in_statement5241);
                    forstatement292=forstatement();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, forstatement292.getTree());

                    }
                    break;
                case 5 :
                    // Java.g:941:9: 'while' parExpression statement
                    {
                    string_literal293=(Token)match(input,WHILE,FOLLOW_WHILE_in_statement5251); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHILE.add(string_literal293);


                    pushFollow(FOLLOW_parExpression_in_statement5253);
                    parExpression294=parExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_parExpression.add(parExpression294.getTree());

                    pushFollow(FOLLOW_statement_in_statement5255);
                    statement295=statement();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_statement.add(statement295.getTree());

                    // AST REWRITE
                    // elements: WHILE, statement, parExpression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 941:41: -> ^( WSTATEMENT 'while' parExpression ) statement
                    {
                        // Java.g:941:44: ^( WSTATEMENT 'while' parExpression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_WHILE.nextNode()
                        );

                        adaptor.addChild(root_1, stream_parExpression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                        adaptor.addChild(root_0, stream_statement.nextTree());

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 6 :
                    // Java.g:942:9: 'do' statement 'while' parExpression ';'
                    {
                    string_literal296=(Token)match(input,DO,FOLLOW_DO_in_statement5277); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DO.add(string_literal296);


                    pushFollow(FOLLOW_statement_in_statement5279);
                    statement297=statement();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_statement.add(statement297.getTree());

                    string_literal298=(Token)match(input,WHILE,FOLLOW_WHILE_in_statement5281); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_WHILE.add(string_literal298);


                    pushFollow(FOLLOW_parExpression_in_statement5283);
                    parExpression299=parExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_parExpression.add(parExpression299.getTree());

                    char_literal300=(Token)match(input,SEMI,FOLLOW_SEMI_in_statement5285); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEMI.add(char_literal300);


                    // AST REWRITE
                    // elements: DO, WHILE, statement, SEMI, parExpression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 942:50: -> ^( WSTATEMENT 'do' ) statement ^( WSTATEMENT 'while' parExpression ';' )
                    {
                        // Java.g:942:53: ^( WSTATEMENT 'do' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_DO.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                        adaptor.addChild(root_0, stream_statement.nextTree());

                        // Java.g:942:82: ^( WSTATEMENT 'while' parExpression ';' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_WHILE.nextNode()
                        );

                        adaptor.addChild(root_1, stream_parExpression.nextTree());

                        adaptor.addChild(root_1, 
                        stream_SEMI.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 7 :
                    // Java.g:943:9: trystatement
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_trystatement_in_statement5315);
                    trystatement301=trystatement();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, trystatement301.getTree());

                    }
                    break;
                case 8 :
                    // Java.g:944:9: 'switch' parExpression '{' switchBlockStatementGroups '}'
                    {
                    string_literal302=(Token)match(input,SWITCH,FOLLOW_SWITCH_in_statement5325); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SWITCH.add(string_literal302);


                    pushFollow(FOLLOW_parExpression_in_statement5327);
                    parExpression303=parExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_parExpression.add(parExpression303.getTree());

                    char_literal304=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_statement5329); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LBRACE.add(char_literal304);


                    pushFollow(FOLLOW_switchBlockStatementGroups_in_statement5331);
                    switchBlockStatementGroups305=switchBlockStatementGroups();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_switchBlockStatementGroups.add(switchBlockStatementGroups305.getTree());

                    char_literal306=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_statement5333); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RBRACE.add(char_literal306);


                    // AST REWRITE
                    // elements: RBRACE, LBRACE, parExpression, switchBlockStatementGroups, SWITCH
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 944:67: -> ^( WSTATEMENT 'switch' parExpression ) '{' switchBlockStatementGroups '}'
                    {
                        // Java.g:944:70: ^( WSTATEMENT 'switch' parExpression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_SWITCH.nextNode()
                        );

                        adaptor.addChild(root_1, stream_parExpression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                        adaptor.addChild(root_0, 
                        stream_LBRACE.nextNode()
                        );

                        adaptor.addChild(root_0, stream_switchBlockStatementGroups.nextTree());

                        adaptor.addChild(root_0, 
                        stream_RBRACE.nextNode()
                        );

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 9 :
                    // Java.g:945:9: 'synchronized' parExpression block
                    {
                    string_literal307=(Token)match(input,SYNCHRONIZED,FOLLOW_SYNCHRONIZED_in_statement5359); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SYNCHRONIZED.add(string_literal307);


                    pushFollow(FOLLOW_parExpression_in_statement5361);
                    parExpression308=parExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_parExpression.add(parExpression308.getTree());

                    pushFollow(FOLLOW_block_in_statement5363);
                    block309=block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_block.add(block309.getTree());

                    // AST REWRITE
                    // elements: block, parExpression, SYNCHRONIZED
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 945:44: -> ^( WSTATEMENT 'synchronized' parExpression ) block
                    {
                        // Java.g:945:47: ^( WSTATEMENT 'synchronized' parExpression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_SYNCHRONIZED.nextNode()
                        );

                        adaptor.addChild(root_1, stream_parExpression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                        adaptor.addChild(root_0, stream_block.nextTree());

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 10 :
                    // Java.g:946:9: 'return' ( expression )? ';'
                    {
                    string_literal310=(Token)match(input,RETURN,FOLLOW_RETURN_in_statement5385); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RETURN.add(string_literal310);


                    // Java.g:946:18: ( expression )?
                    int alt95=2;
                    int LA95_0 = input.LA(1);

                    if ( (LA95_0==BANG||LA95_0==BOOLEAN||LA95_0==BYTE||(LA95_0 >= CHAR && LA95_0 <= CHARLITERAL)||(LA95_0 >= DOUBLE && LA95_0 <= DOUBLELITERAL)||LA95_0==FALSE||(LA95_0 >= FLOAT && LA95_0 <= FLOATLITERAL)||LA95_0==IDENTIFIER||LA95_0==INT||LA95_0==INTLITERAL||(LA95_0 >= LONG && LA95_0 <= LPAREN)||(LA95_0 >= NEW && LA95_0 <= NULL)||LA95_0==PLUS||LA95_0==PLUSPLUS||LA95_0==SHORT||(LA95_0 >= STRINGLITERAL && LA95_0 <= SUB)||(LA95_0 >= SUBSUB && LA95_0 <= SUPER)||LA95_0==THIS||LA95_0==TILDE||LA95_0==TRUE||LA95_0==VOID) ) {
                        alt95=1;
                    }
                    switch (alt95) {
                        case 1 :
                            // Java.g:946:19: expression
                            {
                            pushFollow(FOLLOW_expression_in_statement5388);
                            expression311=expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_expression.add(expression311.getTree());

                            }
                            break;

                    }


                    char_literal312=(Token)match(input,SEMI,FOLLOW_SEMI_in_statement5393); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEMI.add(char_literal312);


                    // AST REWRITE
                    // elements: RETURN, SEMI, expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 946:37: -> ^( WSTATEMENT 'return' ( expression )? ';' )
                    {
                        // Java.g:946:40: ^( WSTATEMENT 'return' ( expression )? ';' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_RETURN.nextNode()
                        );

                        // Java.g:946:62: ( expression )?
                        if ( stream_expression.hasNext() ) {
                            adaptor.addChild(root_1, stream_expression.nextTree());

                        }
                        stream_expression.reset();

                        adaptor.addChild(root_1, 
                        stream_SEMI.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 11 :
                    // Java.g:947:9: 'throw' expression ';'
                    {
                    string_literal313=(Token)match(input,THROW,FOLLOW_THROW_in_statement5419); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_THROW.add(string_literal313);


                    pushFollow(FOLLOW_expression_in_statement5421);
                    expression314=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_expression.add(expression314.getTree());

                    char_literal315=(Token)match(input,SEMI,FOLLOW_SEMI_in_statement5423); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEMI.add(char_literal315);


                    // AST REWRITE
                    // elements: THROW, SEMI, expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 947:32: -> ^( WSTATEMENT 'throw' expression ';' )
                    {
                        // Java.g:947:35: ^( WSTATEMENT 'throw' expression ';' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_THROW.nextNode()
                        );

                        adaptor.addChild(root_1, stream_expression.nextTree());

                        adaptor.addChild(root_1, 
                        stream_SEMI.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 12 :
                    // Java.g:948:9: 'break' ( IDENTIFIER )? ';'
                    {
                    string_literal316=(Token)match(input,BREAK,FOLLOW_BREAK_in_statement5445); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BREAK.add(string_literal316);


                    // Java.g:949:13: ( IDENTIFIER )?
                    int alt96=2;
                    int LA96_0 = input.LA(1);

                    if ( (LA96_0==IDENTIFIER) ) {
                        alt96=1;
                    }
                    switch (alt96) {
                        case 1 :
                            // Java.g:949:14: IDENTIFIER
                            {
                            IDENTIFIER317=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_statement5460); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER317);


                            }
                            break;

                    }


                    char_literal318=(Token)match(input,SEMI,FOLLOW_SEMI_in_statement5477); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEMI.add(char_literal318);


                    // AST REWRITE
                    // elements: SEMI, IDENTIFIER, BREAK
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 950:20: -> ^( WSTATEMENT 'break' ( IDENTIFIER )? ';' )
                    {
                        // Java.g:950:23: ^( WSTATEMENT 'break' ( IDENTIFIER )? ';' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_BREAK.nextNode()
                        );

                        // Java.g:951:13: ( IDENTIFIER )?
                        if ( stream_IDENTIFIER.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_IDENTIFIER.nextNode()
                            );

                        }
                        stream_IDENTIFIER.reset();

                        adaptor.addChild(root_1, 
                        stream_SEMI.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 13 :
                    // Java.g:953:9: 'continue' ( IDENTIFIER )? ';'
                    {
                    string_literal319=(Token)match(input,CONTINUE,FOLLOW_CONTINUE_in_statement5527); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CONTINUE.add(string_literal319);


                    // Java.g:954:13: ( IDENTIFIER )?
                    int alt97=2;
                    int LA97_0 = input.LA(1);

                    if ( (LA97_0==IDENTIFIER) ) {
                        alt97=1;
                    }
                    switch (alt97) {
                        case 1 :
                            // Java.g:954:14: IDENTIFIER
                            {
                            IDENTIFIER320=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_statement5542); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER320);


                            }
                            break;

                    }


                    char_literal321=(Token)match(input,SEMI,FOLLOW_SEMI_in_statement5559); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEMI.add(char_literal321);


                    // AST REWRITE
                    // elements: IDENTIFIER, SEMI, CONTINUE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 955:20: -> ^( WSTATEMENT 'continue' ( IDENTIFIER )? ';' )
                    {
                        // Java.g:955:23: ^( WSTATEMENT 'continue' ( IDENTIFIER )? ';' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_CONTINUE.nextNode()
                        );

                        // Java.g:956:13: ( IDENTIFIER )?
                        if ( stream_IDENTIFIER.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_IDENTIFIER.nextNode()
                            );

                        }
                        stream_IDENTIFIER.reset();

                        adaptor.addChild(root_1, 
                        stream_SEMI.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 14 :
                    // Java.g:958:9: expression ';'
                    {
                    pushFollow(FOLLOW_expression_in_statement5609);
                    expression322=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_expression.add(expression322.getTree());

                    char_literal323=(Token)match(input,SEMI,FOLLOW_SEMI_in_statement5612); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEMI.add(char_literal323);


                    // AST REWRITE
                    // elements: expression, SEMI
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 958:25: -> ^( WSTATEMENT expression ';' )
                    {
                        // Java.g:958:28: ^( WSTATEMENT expression ';' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, stream_expression.nextTree());

                        adaptor.addChild(root_1, 
                        stream_SEMI.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 15 :
                    // Java.g:959:9: IDENTIFIER ':' statement
                    {
                    IDENTIFIER324=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_statement5633); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER324);


                    char_literal325=(Token)match(input,COLON,FOLLOW_COLON_in_statement5635); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COLON.add(char_literal325);


                    pushFollow(FOLLOW_statement_in_statement5637);
                    statement326=statement();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_statement.add(statement326.getTree());

                    // AST REWRITE
                    // elements: IDENTIFIER, COLON, statement
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 959:34: -> ^( WSTATEMENT IDENTIFIER ':' ) statement
                    {
                        // Java.g:959:37: ^( WSTATEMENT IDENTIFIER ':' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_IDENTIFIER.nextNode()
                        );

                        adaptor.addChild(root_1, 
                        stream_COLON.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                        adaptor.addChild(root_0, stream_statement.nextTree());

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 16 :
                    // Java.g:960:9: ';'
                    {
                    char_literal327=(Token)match(input,SEMI,FOLLOW_SEMI_in_statement5659); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEMI.add(char_literal327);


                    // AST REWRITE
                    // elements: SEMI
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 960:13: -> ^( WSTATEMENT ';' )
                    {
                        // Java.g:960:16: ^( WSTATEMENT ';' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_SEMI.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 59, statement_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "statement"


    public static class switchBlockStatementGroups_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "switchBlockStatementGroups"
    // Java.g:963:1: switchBlockStatementGroups : ( switchBlockStatementGroup )* ;
    public final JavaParser.switchBlockStatementGroups_return switchBlockStatementGroups() throws RecognitionException {
        JavaParser.switchBlockStatementGroups_return retval = new JavaParser.switchBlockStatementGroups_return();
        retval.start = input.LT(1);

        int switchBlockStatementGroups_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.switchBlockStatementGroup_return switchBlockStatementGroup328 =null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 60) ) { return retval; }

            // Java.g:964:5: ( ( switchBlockStatementGroup )* )
            // Java.g:964:9: ( switchBlockStatementGroup )*
            {
            root_0 = (Object)adaptor.nil();


            // Java.g:964:9: ( switchBlockStatementGroup )*
            loop99:
            do {
                int alt99=2;
                int LA99_0 = input.LA(1);

                if ( (LA99_0==CASE||LA99_0==DEFAULT) ) {
                    alt99=1;
                }


                switch (alt99) {
            	case 1 :
            	    // Java.g:964:10: switchBlockStatementGroup
            	    {
            	    pushFollow(FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups5688);
            	    switchBlockStatementGroup328=switchBlockStatementGroup();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, switchBlockStatementGroup328.getTree());

            	    }
            	    break;

            	default :
            	    break loop99;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 60, switchBlockStatementGroups_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "switchBlockStatementGroups"


    public static class switchBlockStatementGroup_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "switchBlockStatementGroup"
    // Java.g:967:1: switchBlockStatementGroup : switchLabel ( blockStatement )* ;
    public final JavaParser.switchBlockStatementGroup_return switchBlockStatementGroup() throws RecognitionException {
        JavaParser.switchBlockStatementGroup_return retval = new JavaParser.switchBlockStatementGroup_return();
        retval.start = input.LT(1);

        int switchBlockStatementGroup_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.switchLabel_return switchLabel329 =null;

        JavaParser.blockStatement_return blockStatement330 =null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 61) ) { return retval; }

            // Java.g:968:5: ( switchLabel ( blockStatement )* )
            // Java.g:969:9: switchLabel ( blockStatement )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_switchLabel_in_switchBlockStatementGroup5717);
            switchLabel329=switchLabel();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, switchLabel329.getTree());

            // Java.g:970:9: ( blockStatement )*
            loop100:
            do {
                int alt100=2;
                int LA100_0 = input.LA(1);

                if ( (LA100_0==ABSTRACT||(LA100_0 >= ASSERT && LA100_0 <= BANG)||(LA100_0 >= BOOLEAN && LA100_0 <= BYTE)||(LA100_0 >= CHAR && LA100_0 <= CLASS)||LA100_0==CONTINUE||LA100_0==DO||(LA100_0 >= DOUBLE && LA100_0 <= DOUBLELITERAL)||LA100_0==ENUM||(LA100_0 >= FALSE && LA100_0 <= FINAL)||(LA100_0 >= FLOAT && LA100_0 <= FOR)||(LA100_0 >= IDENTIFIER && LA100_0 <= IF)||(LA100_0 >= INT && LA100_0 <= INTLITERAL)||LA100_0==LBRACE||(LA100_0 >= LONG && LA100_0 <= LPAREN)||(LA100_0 >= NATIVE && LA100_0 <= NULL)||LA100_0==PLUS||(LA100_0 >= PLUSPLUS && LA100_0 <= PUBLIC)||LA100_0==RETURN||(LA100_0 >= SEMI && LA100_0 <= SHORT)||(LA100_0 >= STATIC && LA100_0 <= SUB)||(LA100_0 >= SUBSUB && LA100_0 <= SYNCHRONIZED)||(LA100_0 >= THIS && LA100_0 <= THROW)||(LA100_0 >= TILDE && LA100_0 <= VOLATILE)||LA100_0==WHILE||LA100_0==136) ) {
                    alt100=1;
                }


                switch (alt100) {
            	case 1 :
            	    // Java.g:970:10: blockStatement
            	    {
            	    pushFollow(FOLLOW_blockStatement_in_switchBlockStatementGroup5728);
            	    blockStatement330=blockStatement();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, blockStatement330.getTree());

            	    }
            	    break;

            	default :
            	    break loop100;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 61, switchBlockStatementGroup_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "switchBlockStatementGroup"


    public static class switchLabel_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "switchLabel"
    // Java.g:974:1: switchLabel : ( 'case' expression ':' | 'default' ':' );
    public final JavaParser.switchLabel_return switchLabel() throws RecognitionException {
        JavaParser.switchLabel_return retval = new JavaParser.switchLabel_return();
        retval.start = input.LT(1);

        int switchLabel_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal331=null;
        Token char_literal333=null;
        Token string_literal334=null;
        Token char_literal335=null;
        JavaParser.expression_return expression332 =null;


        Object string_literal331_tree=null;
        Object char_literal333_tree=null;
        Object string_literal334_tree=null;
        Object char_literal335_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 62) ) { return retval; }

            // Java.g:975:5: ( 'case' expression ':' | 'default' ':' )
            int alt101=2;
            int LA101_0 = input.LA(1);

            if ( (LA101_0==CASE) ) {
                alt101=1;
            }
            else if ( (LA101_0==DEFAULT) ) {
                alt101=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 101, 0, input);

                throw nvae;

            }
            switch (alt101) {
                case 1 :
                    // Java.g:975:9: 'case' expression ':'
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal331=(Token)match(input,CASE,FOLLOW_CASE_in_switchLabel5759); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal331_tree = 
                    (Object)adaptor.create(string_literal331)
                    ;
                    adaptor.addChild(root_0, string_literal331_tree);
                    }

                    pushFollow(FOLLOW_expression_in_switchLabel5761);
                    expression332=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression332.getTree());

                    char_literal333=(Token)match(input,COLON,FOLLOW_COLON_in_switchLabel5763); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal333_tree = 
                    (Object)adaptor.create(char_literal333)
                    ;
                    adaptor.addChild(root_0, char_literal333_tree);
                    }

                    }
                    break;
                case 2 :
                    // Java.g:976:9: 'default' ':'
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal334=(Token)match(input,DEFAULT,FOLLOW_DEFAULT_in_switchLabel5773); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal334_tree = 
                    (Object)adaptor.create(string_literal334)
                    ;
                    adaptor.addChild(root_0, string_literal334_tree);
                    }

                    char_literal335=(Token)match(input,COLON,FOLLOW_COLON_in_switchLabel5775); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal335_tree = 
                    (Object)adaptor.create(char_literal335)
                    ;
                    adaptor.addChild(root_0, char_literal335_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 62, switchLabel_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "switchLabel"


    public static class trystatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "trystatement"
    // Java.g:980:1: trystatement : 'try' block ( catches 'finally' block | catches | 'finally' block ) ;
    public final JavaParser.trystatement_return trystatement() throws RecognitionException {
        JavaParser.trystatement_return retval = new JavaParser.trystatement_return();
        retval.start = input.LT(1);

        int trystatement_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal336=null;
        Token string_literal339=null;
        Token string_literal342=null;
        JavaParser.block_return block337 =null;

        JavaParser.catches_return catches338 =null;

        JavaParser.block_return block340 =null;

        JavaParser.catches_return catches341 =null;

        JavaParser.block_return block343 =null;


        Object string_literal336_tree=null;
        Object string_literal339_tree=null;
        Object string_literal342_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 63) ) { return retval; }

            // Java.g:981:5: ( 'try' block ( catches 'finally' block | catches | 'finally' block ) )
            // Java.g:981:9: 'try' block ( catches 'finally' block | catches | 'finally' block )
            {
            root_0 = (Object)adaptor.nil();


            string_literal336=(Token)match(input,TRY,FOLLOW_TRY_in_trystatement5796); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal336_tree = 
            (Object)adaptor.create(string_literal336)
            ;
            adaptor.addChild(root_0, string_literal336_tree);
            }

            pushFollow(FOLLOW_block_in_trystatement5798);
            block337=block();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, block337.getTree());

            // Java.g:982:9: ( catches 'finally' block | catches | 'finally' block )
            int alt102=3;
            int LA102_0 = input.LA(1);

            if ( (LA102_0==CATCH) ) {
                int LA102_1 = input.LA(2);

                if ( (synpred152_Java()) ) {
                    alt102=1;
                }
                else if ( (synpred153_Java()) ) {
                    alt102=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 102, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA102_0==FINALLY) ) {
                alt102=3;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 102, 0, input);

                throw nvae;

            }
            switch (alt102) {
                case 1 :
                    // Java.g:982:13: catches 'finally' block
                    {
                    pushFollow(FOLLOW_catches_in_trystatement5812);
                    catches338=catches();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, catches338.getTree());

                    string_literal339=(Token)match(input,FINALLY,FOLLOW_FINALLY_in_trystatement5814); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal339_tree = 
                    (Object)adaptor.create(string_literal339)
                    ;
                    adaptor.addChild(root_0, string_literal339_tree);
                    }

                    pushFollow(FOLLOW_block_in_trystatement5816);
                    block340=block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, block340.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:983:13: catches
                    {
                    pushFollow(FOLLOW_catches_in_trystatement5830);
                    catches341=catches();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, catches341.getTree());

                    }
                    break;
                case 3 :
                    // Java.g:984:13: 'finally' block
                    {
                    string_literal342=(Token)match(input,FINALLY,FOLLOW_FINALLY_in_trystatement5844); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal342_tree = 
                    (Object)adaptor.create(string_literal342)
                    ;
                    adaptor.addChild(root_0, string_literal342_tree);
                    }

                    pushFollow(FOLLOW_block_in_trystatement5846);
                    block343=block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, block343.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 63, trystatement_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "trystatement"


    public static class catches_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "catches"
    // Java.g:988:1: catches : catchClause ( catchClause )* ;
    public final JavaParser.catches_return catches() throws RecognitionException {
        JavaParser.catches_return retval = new JavaParser.catches_return();
        retval.start = input.LT(1);

        int catches_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.catchClause_return catchClause344 =null;

        JavaParser.catchClause_return catchClause345 =null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 64) ) { return retval; }

            // Java.g:989:5: ( catchClause ( catchClause )* )
            // Java.g:989:9: catchClause ( catchClause )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_catchClause_in_catches5877);
            catchClause344=catchClause();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, catchClause344.getTree());

            // Java.g:990:9: ( catchClause )*
            loop103:
            do {
                int alt103=2;
                int LA103_0 = input.LA(1);

                if ( (LA103_0==CATCH) ) {
                    alt103=1;
                }


                switch (alt103) {
            	case 1 :
            	    // Java.g:990:10: catchClause
            	    {
            	    pushFollow(FOLLOW_catchClause_in_catches5888);
            	    catchClause345=catchClause();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, catchClause345.getTree());

            	    }
            	    break;

            	default :
            	    break loop103;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 64, catches_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "catches"


    public static class catchClause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "catchClause"
    // Java.g:994:1: catchClause : 'catch' '(' ( ( variableModifiers )? type '|' )* formalParameter ')' block ;
    public final JavaParser.catchClause_return catchClause() throws RecognitionException {
        JavaParser.catchClause_return retval = new JavaParser.catchClause_return();
        retval.start = input.LT(1);

        int catchClause_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal346=null;
        Token char_literal347=null;
        Token char_literal350=null;
        Token char_literal352=null;
        JavaParser.variableModifiers_return variableModifiers348 =null;

        JavaParser.type_return type349 =null;

        JavaParser.formalParameter_return formalParameter351 =null;

        JavaParser.block_return block353 =null;


        Object string_literal346_tree=null;
        Object char_literal347_tree=null;
        Object char_literal350_tree=null;
        Object char_literal352_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 65) ) { return retval; }

            // Java.g:995:5: ( 'catch' '(' ( ( variableModifiers )? type '|' )* formalParameter ')' block )
            // Java.g:995:9: 'catch' '(' ( ( variableModifiers )? type '|' )* formalParameter ')' block
            {
            root_0 = (Object)adaptor.nil();


            string_literal346=(Token)match(input,CATCH,FOLLOW_CATCH_in_catchClause5919); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal346_tree = 
            (Object)adaptor.create(string_literal346)
            ;
            adaptor.addChild(root_0, string_literal346_tree);
            }

            char_literal347=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_catchClause5921); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal347_tree = 
            (Object)adaptor.create(char_literal347)
            ;
            adaptor.addChild(root_0, char_literal347_tree);
            }

            // Java.g:995:21: ( ( variableModifiers )? type '|' )*
            loop105:
            do {
                int alt105=2;
                switch ( input.LA(1) ) {
                case FINAL:
                    {
                    int LA105_1 = input.LA(2);

                    if ( (synpred156_Java()) ) {
                        alt105=1;
                    }


                    }
                    break;
                case 136:
                    {
                    int LA105_2 = input.LA(2);

                    if ( (synpred156_Java()) ) {
                        alt105=1;
                    }


                    }
                    break;
                case IDENTIFIER:
                    {
                    int LA105_3 = input.LA(2);

                    if ( (synpred156_Java()) ) {
                        alt105=1;
                    }


                    }
                    break;
                case BOOLEAN:
                case BYTE:
                case CHAR:
                case DOUBLE:
                case FLOAT:
                case INT:
                case LONG:
                case SHORT:
                    {
                    int LA105_4 = input.LA(2);

                    if ( (synpred156_Java()) ) {
                        alt105=1;
                    }


                    }
                    break;

                }

                switch (alt105) {
            	case 1 :
            	    // Java.g:995:22: ( variableModifiers )? type '|'
            	    {
            	    // Java.g:995:22: ( variableModifiers )?
            	    int alt104=2;
            	    switch ( input.LA(1) ) {
            	        case FINAL:
            	        case 136:
            	            {
            	            alt104=1;
            	            }
            	            break;
            	        case IDENTIFIER:
            	            {
            	            int LA104_2 = input.LA(2);

            	            if ( (synpred155_Java()) ) {
            	                alt104=1;
            	            }
            	            }
            	            break;
            	        case BOOLEAN:
            	        case BYTE:
            	        case CHAR:
            	        case DOUBLE:
            	        case FLOAT:
            	        case INT:
            	        case LONG:
            	        case SHORT:
            	            {
            	            int LA104_3 = input.LA(2);

            	            if ( (synpred155_Java()) ) {
            	                alt104=1;
            	            }
            	            }
            	            break;
            	    }

            	    switch (alt104) {
            	        case 1 :
            	            // Java.g:995:23: variableModifiers
            	            {
            	            pushFollow(FOLLOW_variableModifiers_in_catchClause5925);
            	            variableModifiers348=variableModifiers();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, variableModifiers348.getTree());

            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_type_in_catchClause5929);
            	    type349=type();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, type349.getTree());

            	    char_literal350=(Token)match(input,BAR,FOLLOW_BAR_in_catchClause5931); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal350_tree = 
            	    (Object)adaptor.create(char_literal350)
            	    ;
            	    adaptor.addChild(root_0, char_literal350_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop105;
                }
            } while (true);


            pushFollow(FOLLOW_formalParameter_in_catchClause5935);
            formalParameter351=formalParameter();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, formalParameter351.getTree());

            char_literal352=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_catchClause5945); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal352_tree = 
            (Object)adaptor.create(char_literal352)
            ;
            adaptor.addChild(root_0, char_literal352_tree);
            }

            pushFollow(FOLLOW_block_in_catchClause5947);
            block353=block();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, block353.getTree());

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 65, catchClause_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "catchClause"


    public static class formalParameter_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "formalParameter"
    // Java.g:999:1: formalParameter : variableModifiers type IDENTIFIER ( '[' ']' )* ;
    public final JavaParser.formalParameter_return formalParameter() throws RecognitionException {
        JavaParser.formalParameter_return retval = new JavaParser.formalParameter_return();
        retval.start = input.LT(1);

        int formalParameter_StartIndex = input.index();

        Object root_0 = null;

        Token IDENTIFIER356=null;
        Token char_literal357=null;
        Token char_literal358=null;
        JavaParser.variableModifiers_return variableModifiers354 =null;

        JavaParser.type_return type355 =null;


        Object IDENTIFIER356_tree=null;
        Object char_literal357_tree=null;
        Object char_literal358_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 66) ) { return retval; }

            // Java.g:1000:5: ( variableModifiers type IDENTIFIER ( '[' ']' )* )
            // Java.g:1000:9: variableModifiers type IDENTIFIER ( '[' ']' )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_variableModifiers_in_formalParameter5968);
            variableModifiers354=variableModifiers();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, variableModifiers354.getTree());

            pushFollow(FOLLOW_type_in_formalParameter5970);
            type355=type();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, type355.getTree());

            IDENTIFIER356=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_formalParameter5972); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER356_tree = 
            (Object)adaptor.create(IDENTIFIER356)
            ;
            adaptor.addChild(root_0, IDENTIFIER356_tree);
            }

            // Java.g:1001:9: ( '[' ']' )*
            loop106:
            do {
                int alt106=2;
                int LA106_0 = input.LA(1);

                if ( (LA106_0==LBRACKET) ) {
                    alt106=1;
                }


                switch (alt106) {
            	case 1 :
            	    // Java.g:1001:10: '[' ']'
            	    {
            	    char_literal357=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_formalParameter5983); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal357_tree = 
            	    (Object)adaptor.create(char_literal357)
            	    ;
            	    adaptor.addChild(root_0, char_literal357_tree);
            	    }

            	    char_literal358=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_formalParameter5985); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal358_tree = 
            	    (Object)adaptor.create(char_literal358)
            	    ;
            	    adaptor.addChild(root_0, char_literal358_tree);
            	    }

            	    }
            	    break;

            	default :
            	    break loop106;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 66, formalParameter_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "formalParameter"


    public static class forstatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "forstatement"
    // Java.g:1005:1: forstatement : ( 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement -> ^( WSTATEMENT 'for' '(' ( variableModifiers )? type IDENTIFIER ':' expression ')' ) statement | 'for' '(' ( forInit )? ';' ( expression )? ';' ( expressionList )? ')' statement -> ^( WSTATEMENT 'for' '(' ( forInit )? ';' ( expression )? ';' ( expressionList )? ')' ) statement );
    public final JavaParser.forstatement_return forstatement() throws RecognitionException {
        JavaParser.forstatement_return retval = new JavaParser.forstatement_return();
        retval.start = input.LT(1);

        int forstatement_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal359=null;
        Token char_literal360=null;
        Token IDENTIFIER363=null;
        Token char_literal364=null;
        Token char_literal366=null;
        Token string_literal368=null;
        Token char_literal369=null;
        Token char_literal371=null;
        Token char_literal373=null;
        Token char_literal375=null;
        JavaParser.variableModifiers_return variableModifiers361 =null;

        JavaParser.type_return type362 =null;

        JavaParser.expression_return expression365 =null;

        JavaParser.statement_return statement367 =null;

        JavaParser.forInit_return forInit370 =null;

        JavaParser.expression_return expression372 =null;

        JavaParser.expressionList_return expressionList374 =null;

        JavaParser.statement_return statement376 =null;


        Object string_literal359_tree=null;
        Object char_literal360_tree=null;
        Object IDENTIFIER363_tree=null;
        Object char_literal364_tree=null;
        Object char_literal366_tree=null;
        Object string_literal368_tree=null;
        Object char_literal369_tree=null;
        Object char_literal371_tree=null;
        Object char_literal373_tree=null;
        Object char_literal375_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_FOR=new RewriteRuleTokenStream(adaptor,"token FOR");
        RewriteRuleTokenStream stream_SEMI=new RewriteRuleTokenStream(adaptor,"token SEMI");
        RewriteRuleTokenStream stream_IDENTIFIER=new RewriteRuleTokenStream(adaptor,"token IDENTIFIER");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        RewriteRuleSubtreeStream stream_statement=new RewriteRuleSubtreeStream(adaptor,"rule statement");
        RewriteRuleSubtreeStream stream_expressionList=new RewriteRuleSubtreeStream(adaptor,"rule expressionList");
        RewriteRuleSubtreeStream stream_variableModifiers=new RewriteRuleSubtreeStream(adaptor,"rule variableModifiers");
        RewriteRuleSubtreeStream stream_forInit=new RewriteRuleSubtreeStream(adaptor,"rule forInit");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 67) ) { return retval; }

            // Java.g:1006:5: ( 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement -> ^( WSTATEMENT 'for' '(' ( variableModifiers )? type IDENTIFIER ':' expression ')' ) statement | 'for' '(' ( forInit )? ';' ( expression )? ';' ( expressionList )? ')' statement -> ^( WSTATEMENT 'for' '(' ( forInit )? ';' ( expression )? ';' ( expressionList )? ')' ) statement )
            int alt110=2;
            int LA110_0 = input.LA(1);

            if ( (LA110_0==FOR) ) {
                int LA110_1 = input.LA(2);

                if ( (synpred158_Java()) ) {
                    alt110=1;
                }
                else if ( (true) ) {
                    alt110=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 110, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 110, 0, input);

                throw nvae;

            }
            switch (alt110) {
                case 1 :
                    // Java.g:1008:9: 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement
                    {
                    string_literal359=(Token)match(input,FOR,FOLLOW_FOR_in_forstatement6034); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_FOR.add(string_literal359);


                    char_literal360=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_forstatement6036); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(char_literal360);


                    pushFollow(FOLLOW_variableModifiers_in_forstatement6038);
                    variableModifiers361=variableModifiers();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_variableModifiers.add(variableModifiers361.getTree());

                    pushFollow(FOLLOW_type_in_forstatement6040);
                    type362=type();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_type.add(type362.getTree());

                    IDENTIFIER363=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_forstatement6042); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_IDENTIFIER.add(IDENTIFIER363);


                    char_literal364=(Token)match(input,COLON,FOLLOW_COLON_in_forstatement6044); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_COLON.add(char_literal364);


                    pushFollow(FOLLOW_expression_in_forstatement6055);
                    expression365=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_expression.add(expression365.getTree());

                    char_literal366=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_forstatement6057); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(char_literal366);


                    pushFollow(FOLLOW_statement_in_forstatement6059);
                    statement367=statement();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_statement.add(statement367.getTree());

                    // AST REWRITE
                    // elements: expression, RPAREN, type, FOR, IDENTIFIER, variableModifiers, statement, LPAREN, COLON
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 1010:3: -> ^( WSTATEMENT 'for' '(' ( variableModifiers )? type IDENTIFIER ':' expression ')' ) statement
                    {
                        // Java.g:1011:3: ^( WSTATEMENT 'for' '(' ( variableModifiers )? type IDENTIFIER ':' expression ')' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_FOR.nextNode()
                        );

                        adaptor.addChild(root_1, 
                        stream_LPAREN.nextNode()
                        );

                        // Java.g:1011:26: ( variableModifiers )?
                        if ( stream_variableModifiers.hasNext() ) {
                            adaptor.addChild(root_1, stream_variableModifiers.nextTree());

                        }
                        stream_variableModifiers.reset();

                        adaptor.addChild(root_1, stream_type.nextTree());

                        adaptor.addChild(root_1, 
                        stream_IDENTIFIER.nextNode()
                        );

                        adaptor.addChild(root_1, 
                        stream_COLON.nextNode()
                        );

                        adaptor.addChild(root_1, stream_expression.nextTree());

                        adaptor.addChild(root_1, 
                        stream_RPAREN.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                        adaptor.addChild(root_0, stream_statement.nextTree());

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;
                case 2 :
                    // Java.g:1014:9: 'for' '(' ( forInit )? ';' ( expression )? ';' ( expressionList )? ')' statement
                    {
                    string_literal368=(Token)match(input,FOR,FOLLOW_FOR_in_forstatement6118); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_FOR.add(string_literal368);


                    char_literal369=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_forstatement6120); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LPAREN.add(char_literal369);


                    // Java.g:1015:17: ( forInit )?
                    int alt107=2;
                    int LA107_0 = input.LA(1);

                    if ( (LA107_0==EOF||LA107_0==BANG||LA107_0==BOOLEAN||LA107_0==BYTE||(LA107_0 >= CHAR && LA107_0 <= CHARLITERAL)||(LA107_0 >= DOUBLE && LA107_0 <= DOUBLELITERAL)||(LA107_0 >= FALSE && LA107_0 <= FINAL)||(LA107_0 >= FLOAT && LA107_0 <= FLOATLITERAL)||LA107_0==IDENTIFIER||LA107_0==INT||LA107_0==INTLITERAL||(LA107_0 >= LONG && LA107_0 <= LPAREN)||(LA107_0 >= NEW && LA107_0 <= NULL)||LA107_0==PLUS||LA107_0==PLUSPLUS||LA107_0==SHORT||(LA107_0 >= STRINGLITERAL && LA107_0 <= SUB)||(LA107_0 >= SUBSUB && LA107_0 <= SUPER)||LA107_0==THIS||LA107_0==TILDE||LA107_0==TRUE||LA107_0==VOID||LA107_0==136) ) {
                        alt107=1;
                    }
                    switch (alt107) {
                        case 1 :
                            // Java.g:1015:18: forInit
                            {
                            pushFollow(FOLLOW_forInit_in_forstatement6140);
                            forInit370=forInit();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_forInit.add(forInit370.getTree());

                            }
                            break;

                    }


                    char_literal371=(Token)match(input,SEMI,FOLLOW_SEMI_in_forstatement6161); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEMI.add(char_literal371);


                    // Java.g:1017:17: ( expression )?
                    int alt108=2;
                    int LA108_0 = input.LA(1);

                    if ( (LA108_0==BANG||LA108_0==BOOLEAN||LA108_0==BYTE||(LA108_0 >= CHAR && LA108_0 <= CHARLITERAL)||(LA108_0 >= DOUBLE && LA108_0 <= DOUBLELITERAL)||LA108_0==FALSE||(LA108_0 >= FLOAT && LA108_0 <= FLOATLITERAL)||LA108_0==IDENTIFIER||LA108_0==INT||LA108_0==INTLITERAL||(LA108_0 >= LONG && LA108_0 <= LPAREN)||(LA108_0 >= NEW && LA108_0 <= NULL)||LA108_0==PLUS||LA108_0==PLUSPLUS||LA108_0==SHORT||(LA108_0 >= STRINGLITERAL && LA108_0 <= SUB)||(LA108_0 >= SUBSUB && LA108_0 <= SUPER)||LA108_0==THIS||LA108_0==TILDE||LA108_0==TRUE||LA108_0==VOID) ) {
                        alt108=1;
                    }
                    switch (alt108) {
                        case 1 :
                            // Java.g:1017:18: expression
                            {
                            pushFollow(FOLLOW_expression_in_forstatement6181);
                            expression372=expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_expression.add(expression372.getTree());

                            }
                            break;

                    }


                    char_literal373=(Token)match(input,SEMI,FOLLOW_SEMI_in_forstatement6202); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_SEMI.add(char_literal373);


                    // Java.g:1019:17: ( expressionList )?
                    int alt109=2;
                    int LA109_0 = input.LA(1);

                    if ( (LA109_0==BANG||LA109_0==BOOLEAN||LA109_0==BYTE||(LA109_0 >= CHAR && LA109_0 <= CHARLITERAL)||(LA109_0 >= DOUBLE && LA109_0 <= DOUBLELITERAL)||LA109_0==FALSE||(LA109_0 >= FLOAT && LA109_0 <= FLOATLITERAL)||LA109_0==IDENTIFIER||LA109_0==INT||LA109_0==INTLITERAL||(LA109_0 >= LONG && LA109_0 <= LPAREN)||(LA109_0 >= NEW && LA109_0 <= NULL)||LA109_0==PLUS||LA109_0==PLUSPLUS||LA109_0==SHORT||(LA109_0 >= STRINGLITERAL && LA109_0 <= SUB)||(LA109_0 >= SUBSUB && LA109_0 <= SUPER)||LA109_0==THIS||LA109_0==TILDE||LA109_0==TRUE||LA109_0==VOID) ) {
                        alt109=1;
                    }
                    switch (alt109) {
                        case 1 :
                            // Java.g:1019:18: expressionList
                            {
                            pushFollow(FOLLOW_expressionList_in_forstatement6222);
                            expressionList374=expressionList();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_expressionList.add(expressionList374.getTree());

                            }
                            break;

                    }


                    char_literal375=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_forstatement6243); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RPAREN.add(char_literal375);


                    pushFollow(FOLLOW_statement_in_forstatement6245);
                    statement376=statement();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_statement.add(statement376.getTree());

                    // AST REWRITE
                    // elements: expressionList, SEMI, statement, LPAREN, RPAREN, forInit, FOR, SEMI, expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {

                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 1021:3: -> ^( WSTATEMENT 'for' '(' ( forInit )? ';' ( expression )? ';' ( expressionList )? ')' ) statement
                    {
                        // Java.g:1022:3: ^( WSTATEMENT 'for' '(' ( forInit )? ';' ( expression )? ';' ( expressionList )? ')' )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(
                        (Object)adaptor.create(WSTATEMENT, "WSTATEMENT")
                        , root_1);

                        adaptor.addChild(root_1, 
                        stream_FOR.nextNode()
                        );

                        adaptor.addChild(root_1, 
                        stream_LPAREN.nextNode()
                        );

                        // Java.g:1023:17: ( forInit )?
                        if ( stream_forInit.hasNext() ) {
                            adaptor.addChild(root_1, stream_forInit.nextTree());

                        }
                        stream_forInit.reset();

                        adaptor.addChild(root_1, 
                        stream_SEMI.nextNode()
                        );

                        // Java.g:1025:17: ( expression )?
                        if ( stream_expression.hasNext() ) {
                            adaptor.addChild(root_1, stream_expression.nextTree());

                        }
                        stream_expression.reset();

                        adaptor.addChild(root_1, 
                        stream_SEMI.nextNode()
                        );

                        // Java.g:1027:17: ( expressionList )?
                        if ( stream_expressionList.hasNext() ) {
                            adaptor.addChild(root_1, stream_expressionList.nextTree());

                        }
                        stream_expressionList.reset();

                        adaptor.addChild(root_1, 
                        stream_RPAREN.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                        adaptor.addChild(root_0, stream_statement.nextTree());

                    }


                    retval.tree = root_0;
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 67, forstatement_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "forstatement"


    public static class forInit_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "forInit"
    // Java.g:1031:1: forInit : ( localVariableDeclaration | expressionList );
    public final JavaParser.forInit_return forInit() throws RecognitionException {
        JavaParser.forInit_return retval = new JavaParser.forInit_return();
        retval.start = input.LT(1);

        int forInit_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.localVariableDeclaration_return localVariableDeclaration377 =null;

        JavaParser.expressionList_return expressionList378 =null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 68) ) { return retval; }

            // Java.g:1032:5: ( localVariableDeclaration | expressionList )
            int alt111=2;
            switch ( input.LA(1) ) {
            case FINAL:
            case 136:
                {
                alt111=1;
                }
                break;
            case IDENTIFIER:
                {
                int LA111_3 = input.LA(2);

                if ( (synpred162_Java()) ) {
                    alt111=1;
                }
                else if ( (true) ) {
                    alt111=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 111, 3, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
                {
                int LA111_4 = input.LA(2);

                if ( (synpred162_Java()) ) {
                    alt111=1;
                }
                else if ( (true) ) {
                    alt111=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 111, 4, input);

                    throw nvae;

                }
                }
                break;
            case BANG:
            case CHARLITERAL:
            case DOUBLELITERAL:
            case FALSE:
            case FLOATLITERAL:
            case INTLITERAL:
            case LONGLITERAL:
            case LPAREN:
            case NEW:
            case NULL:
            case PLUS:
            case PLUSPLUS:
            case STRINGLITERAL:
            case SUB:
            case SUBSUB:
            case SUPER:
            case THIS:
            case TILDE:
            case TRUE:
            case VOID:
                {
                alt111=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 111, 0, input);

                throw nvae;

            }

            switch (alt111) {
                case 1 :
                    // Java.g:1032:9: localVariableDeclaration
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_localVariableDeclaration_in_forInit6404);
                    localVariableDeclaration377=localVariableDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, localVariableDeclaration377.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:1033:9: expressionList
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_expressionList_in_forInit6414);
                    expressionList378=expressionList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expressionList378.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 68, forInit_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "forInit"


    public static class parExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "parExpression"
    // Java.g:1036:1: parExpression : '(' expression ')' ;
    public final JavaParser.parExpression_return parExpression() throws RecognitionException {
        JavaParser.parExpression_return retval = new JavaParser.parExpression_return();
        retval.start = input.LT(1);

        int parExpression_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal379=null;
        Token char_literal381=null;
        JavaParser.expression_return expression380 =null;


        Object char_literal379_tree=null;
        Object char_literal381_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 69) ) { return retval; }

            // Java.g:1037:5: ( '(' expression ')' )
            // Java.g:1037:9: '(' expression ')'
            {
            root_0 = (Object)adaptor.nil();


            char_literal379=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_parExpression6434); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal379_tree = 
            (Object)adaptor.create(char_literal379)
            ;
            adaptor.addChild(root_0, char_literal379_tree);
            }

            pushFollow(FOLLOW_expression_in_parExpression6436);
            expression380=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression380.getTree());

            char_literal381=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_parExpression6438); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal381_tree = 
            (Object)adaptor.create(char_literal381)
            ;
            adaptor.addChild(root_0, char_literal381_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 69, parExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "parExpression"


    public static class expressionList_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "expressionList"
    // Java.g:1040:1: expressionList : expression ( ',' expression )* ;
    public final JavaParser.expressionList_return expressionList() throws RecognitionException {
        JavaParser.expressionList_return retval = new JavaParser.expressionList_return();
        retval.start = input.LT(1);

        int expressionList_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal383=null;
        JavaParser.expression_return expression382 =null;

        JavaParser.expression_return expression384 =null;


        Object char_literal383_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 70) ) { return retval; }

            // Java.g:1041:5: ( expression ( ',' expression )* )
            // Java.g:1041:9: expression ( ',' expression )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_expression_in_expressionList6458);
            expression382=expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, expression382.getTree());

            // Java.g:1042:9: ( ',' expression )*
            loop112:
            do {
                int alt112=2;
                int LA112_0 = input.LA(1);

                if ( (LA112_0==COMMA) ) {
                    alt112=1;
                }


                switch (alt112) {
            	case 1 :
            	    // Java.g:1042:10: ',' expression
            	    {
            	    char_literal383=(Token)match(input,COMMA,FOLLOW_COMMA_in_expressionList6469); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal383_tree = 
            	    (Object)adaptor.create(char_literal383)
            	    ;
            	    adaptor.addChild(root_0, char_literal383_tree);
            	    }

            	    pushFollow(FOLLOW_expression_in_expressionList6471);
            	    expression384=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression384.getTree());

            	    }
            	    break;

            	default :
            	    break loop112;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 70, expressionList_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "expressionList"


    public static class expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "expression"
    // Java.g:1047:1: expression : conditionalExpression ( assignmentOperator expression )? ;
    public final JavaParser.expression_return expression() throws RecognitionException {
        JavaParser.expression_return retval = new JavaParser.expression_return();
        retval.start = input.LT(1);

        int expression_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.conditionalExpression_return conditionalExpression385 =null;

        JavaParser.assignmentOperator_return assignmentOperator386 =null;

        JavaParser.expression_return expression387 =null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 71) ) { return retval; }

            // Java.g:1048:5: ( conditionalExpression ( assignmentOperator expression )? )
            // Java.g:1048:9: conditionalExpression ( assignmentOperator expression )?
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_conditionalExpression_in_expression6503);
            conditionalExpression385=conditionalExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditionalExpression385.getTree());

            // Java.g:1049:9: ( assignmentOperator expression )?
            int alt113=2;
            int LA113_0 = input.LA(1);

            if ( (LA113_0==AMPEQ||LA113_0==BAREQ||LA113_0==CARETEQ||LA113_0==EQ||LA113_0==GT||LA113_0==LT||LA113_0==PERCENTEQ||LA113_0==PLUSEQ||LA113_0==SLASHEQ||LA113_0==STAREQ||LA113_0==SUBEQ) ) {
                alt113=1;
            }
            switch (alt113) {
                case 1 :
                    // Java.g:1049:10: assignmentOperator expression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_expression6514);
                    assignmentOperator386=assignmentOperator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, assignmentOperator386.getTree());

                    pushFollow(FOLLOW_expression_in_expression6516);
                    expression387=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression387.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 71, expression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "expression"


    public static class assignmentOperator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "assignmentOperator"
    // Java.g:1054:1: assignmentOperator : ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | '<' '<' '=' | '>' '>' '>' '=' | '>' '>' '=' );
    public final JavaParser.assignmentOperator_return assignmentOperator() throws RecognitionException {
        JavaParser.assignmentOperator_return retval = new JavaParser.assignmentOperator_return();
        retval.start = input.LT(1);

        int assignmentOperator_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal388=null;
        Token string_literal389=null;
        Token string_literal390=null;
        Token string_literal391=null;
        Token string_literal392=null;
        Token string_literal393=null;
        Token string_literal394=null;
        Token string_literal395=null;
        Token string_literal396=null;
        Token char_literal397=null;
        Token char_literal398=null;
        Token char_literal399=null;
        Token char_literal400=null;
        Token char_literal401=null;
        Token char_literal402=null;
        Token char_literal403=null;
        Token char_literal404=null;
        Token char_literal405=null;
        Token char_literal406=null;

        Object char_literal388_tree=null;
        Object string_literal389_tree=null;
        Object string_literal390_tree=null;
        Object string_literal391_tree=null;
        Object string_literal392_tree=null;
        Object string_literal393_tree=null;
        Object string_literal394_tree=null;
        Object string_literal395_tree=null;
        Object string_literal396_tree=null;
        Object char_literal397_tree=null;
        Object char_literal398_tree=null;
        Object char_literal399_tree=null;
        Object char_literal400_tree=null;
        Object char_literal401_tree=null;
        Object char_literal402_tree=null;
        Object char_literal403_tree=null;
        Object char_literal404_tree=null;
        Object char_literal405_tree=null;
        Object char_literal406_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 72) ) { return retval; }

            // Java.g:1055:5: ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | '<' '<' '=' | '>' '>' '>' '=' | '>' '>' '=' )
            int alt114=12;
            switch ( input.LA(1) ) {
            case EQ:
                {
                alt114=1;
                }
                break;
            case PLUSEQ:
                {
                alt114=2;
                }
                break;
            case SUBEQ:
                {
                alt114=3;
                }
                break;
            case STAREQ:
                {
                alt114=4;
                }
                break;
            case SLASHEQ:
                {
                alt114=5;
                }
                break;
            case AMPEQ:
                {
                alt114=6;
                }
                break;
            case BAREQ:
                {
                alt114=7;
                }
                break;
            case CARETEQ:
                {
                alt114=8;
                }
                break;
            case PERCENTEQ:
                {
                alt114=9;
                }
                break;
            case LT:
                {
                alt114=10;
                }
                break;
            case GT:
                {
                int LA114_11 = input.LA(2);

                if ( (LA114_11==GT) ) {
                    int LA114_12 = input.LA(3);

                    if ( (LA114_12==GT) ) {
                        alt114=11;
                    }
                    else if ( (LA114_12==EQ) ) {
                        alt114=12;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 114, 12, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 114, 11, input);

                    throw nvae;

                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 114, 0, input);

                throw nvae;

            }

            switch (alt114) {
                case 1 :
                    // Java.g:1055:9: '='
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal388=(Token)match(input,EQ,FOLLOW_EQ_in_assignmentOperator6548); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal388_tree = 
                    (Object)adaptor.create(char_literal388)
                    ;
                    adaptor.addChild(root_0, char_literal388_tree);
                    }

                    }
                    break;
                case 2 :
                    // Java.g:1056:9: '+='
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal389=(Token)match(input,PLUSEQ,FOLLOW_PLUSEQ_in_assignmentOperator6558); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal389_tree = 
                    (Object)adaptor.create(string_literal389)
                    ;
                    adaptor.addChild(root_0, string_literal389_tree);
                    }

                    }
                    break;
                case 3 :
                    // Java.g:1057:9: '-='
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal390=(Token)match(input,SUBEQ,FOLLOW_SUBEQ_in_assignmentOperator6568); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal390_tree = 
                    (Object)adaptor.create(string_literal390)
                    ;
                    adaptor.addChild(root_0, string_literal390_tree);
                    }

                    }
                    break;
                case 4 :
                    // Java.g:1058:9: '*='
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal391=(Token)match(input,STAREQ,FOLLOW_STAREQ_in_assignmentOperator6578); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal391_tree = 
                    (Object)adaptor.create(string_literal391)
                    ;
                    adaptor.addChild(root_0, string_literal391_tree);
                    }

                    }
                    break;
                case 5 :
                    // Java.g:1059:9: '/='
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal392=(Token)match(input,SLASHEQ,FOLLOW_SLASHEQ_in_assignmentOperator6588); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal392_tree = 
                    (Object)adaptor.create(string_literal392)
                    ;
                    adaptor.addChild(root_0, string_literal392_tree);
                    }

                    }
                    break;
                case 6 :
                    // Java.g:1060:9: '&='
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal393=(Token)match(input,AMPEQ,FOLLOW_AMPEQ_in_assignmentOperator6598); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal393_tree = 
                    (Object)adaptor.create(string_literal393)
                    ;
                    adaptor.addChild(root_0, string_literal393_tree);
                    }

                    }
                    break;
                case 7 :
                    // Java.g:1061:9: '|='
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal394=(Token)match(input,BAREQ,FOLLOW_BAREQ_in_assignmentOperator6608); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal394_tree = 
                    (Object)adaptor.create(string_literal394)
                    ;
                    adaptor.addChild(root_0, string_literal394_tree);
                    }

                    }
                    break;
                case 8 :
                    // Java.g:1062:9: '^='
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal395=(Token)match(input,CARETEQ,FOLLOW_CARETEQ_in_assignmentOperator6618); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal395_tree = 
                    (Object)adaptor.create(string_literal395)
                    ;
                    adaptor.addChild(root_0, string_literal395_tree);
                    }

                    }
                    break;
                case 9 :
                    // Java.g:1063:9: '%='
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal396=(Token)match(input,PERCENTEQ,FOLLOW_PERCENTEQ_in_assignmentOperator6628); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal396_tree = 
                    (Object)adaptor.create(string_literal396)
                    ;
                    adaptor.addChild(root_0, string_literal396_tree);
                    }

                    }
                    break;
                case 10 :
                    // Java.g:1064:10: '<' '<' '='
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal397=(Token)match(input,LT,FOLLOW_LT_in_assignmentOperator6639); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal397_tree = 
                    (Object)adaptor.create(char_literal397)
                    ;
                    adaptor.addChild(root_0, char_literal397_tree);
                    }

                    char_literal398=(Token)match(input,LT,FOLLOW_LT_in_assignmentOperator6641); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal398_tree = 
                    (Object)adaptor.create(char_literal398)
                    ;
                    adaptor.addChild(root_0, char_literal398_tree);
                    }

                    char_literal399=(Token)match(input,EQ,FOLLOW_EQ_in_assignmentOperator6643); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal399_tree = 
                    (Object)adaptor.create(char_literal399)
                    ;
                    adaptor.addChild(root_0, char_literal399_tree);
                    }

                    }
                    break;
                case 11 :
                    // Java.g:1065:10: '>' '>' '>' '='
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal400=(Token)match(input,GT,FOLLOW_GT_in_assignmentOperator6654); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal400_tree = 
                    (Object)adaptor.create(char_literal400)
                    ;
                    adaptor.addChild(root_0, char_literal400_tree);
                    }

                    char_literal401=(Token)match(input,GT,FOLLOW_GT_in_assignmentOperator6656); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal401_tree = 
                    (Object)adaptor.create(char_literal401)
                    ;
                    adaptor.addChild(root_0, char_literal401_tree);
                    }

                    char_literal402=(Token)match(input,GT,FOLLOW_GT_in_assignmentOperator6658); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal402_tree = 
                    (Object)adaptor.create(char_literal402)
                    ;
                    adaptor.addChild(root_0, char_literal402_tree);
                    }

                    char_literal403=(Token)match(input,EQ,FOLLOW_EQ_in_assignmentOperator6660); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal403_tree = 
                    (Object)adaptor.create(char_literal403)
                    ;
                    adaptor.addChild(root_0, char_literal403_tree);
                    }

                    }
                    break;
                case 12 :
                    // Java.g:1066:10: '>' '>' '='
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal404=(Token)match(input,GT,FOLLOW_GT_in_assignmentOperator6671); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal404_tree = 
                    (Object)adaptor.create(char_literal404)
                    ;
                    adaptor.addChild(root_0, char_literal404_tree);
                    }

                    char_literal405=(Token)match(input,GT,FOLLOW_GT_in_assignmentOperator6673); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal405_tree = 
                    (Object)adaptor.create(char_literal405)
                    ;
                    adaptor.addChild(root_0, char_literal405_tree);
                    }

                    char_literal406=(Token)match(input,EQ,FOLLOW_EQ_in_assignmentOperator6675); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal406_tree = 
                    (Object)adaptor.create(char_literal406)
                    ;
                    adaptor.addChild(root_0, char_literal406_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 72, assignmentOperator_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "assignmentOperator"


    public static class conditionalExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "conditionalExpression"
    // Java.g:1070:1: conditionalExpression : conditionalOrExpression ( '?' expression ':' conditionalExpression )? ;
    public final JavaParser.conditionalExpression_return conditionalExpression() throws RecognitionException {
        JavaParser.conditionalExpression_return retval = new JavaParser.conditionalExpression_return();
        retval.start = input.LT(1);

        int conditionalExpression_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal408=null;
        Token char_literal410=null;
        JavaParser.conditionalOrExpression_return conditionalOrExpression407 =null;

        JavaParser.expression_return expression409 =null;

        JavaParser.conditionalExpression_return conditionalExpression411 =null;


        Object char_literal408_tree=null;
        Object char_literal410_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 73) ) { return retval; }

            // Java.g:1071:5: ( conditionalOrExpression ( '?' expression ':' conditionalExpression )? )
            // Java.g:1071:9: conditionalOrExpression ( '?' expression ':' conditionalExpression )?
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_conditionalOrExpression_in_conditionalExpression6696);
            conditionalOrExpression407=conditionalOrExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditionalOrExpression407.getTree());

            // Java.g:1072:9: ( '?' expression ':' conditionalExpression )?
            int alt115=2;
            int LA115_0 = input.LA(1);

            if ( (LA115_0==QUES) ) {
                alt115=1;
            }
            switch (alt115) {
                case 1 :
                    // Java.g:1072:10: '?' expression ':' conditionalExpression
                    {
                    char_literal408=(Token)match(input,QUES,FOLLOW_QUES_in_conditionalExpression6707); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal408_tree = 
                    (Object)adaptor.create(char_literal408)
                    ;
                    adaptor.addChild(root_0, char_literal408_tree);
                    }

                    pushFollow(FOLLOW_expression_in_conditionalExpression6709);
                    expression409=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression409.getTree());

                    char_literal410=(Token)match(input,COLON,FOLLOW_COLON_in_conditionalExpression6711); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal410_tree = 
                    (Object)adaptor.create(char_literal410)
                    ;
                    adaptor.addChild(root_0, char_literal410_tree);
                    }

                    pushFollow(FOLLOW_conditionalExpression_in_conditionalExpression6713);
                    conditionalExpression411=conditionalExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, conditionalExpression411.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 73, conditionalExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "conditionalExpression"


    public static class conditionalOrExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "conditionalOrExpression"
    // Java.g:1076:1: conditionalOrExpression : conditionalAndExpression ( '||' conditionalAndExpression )* ;
    public final JavaParser.conditionalOrExpression_return conditionalOrExpression() throws RecognitionException {
        JavaParser.conditionalOrExpression_return retval = new JavaParser.conditionalOrExpression_return();
        retval.start = input.LT(1);

        int conditionalOrExpression_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal413=null;
        JavaParser.conditionalAndExpression_return conditionalAndExpression412 =null;

        JavaParser.conditionalAndExpression_return conditionalAndExpression414 =null;


        Object string_literal413_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 74) ) { return retval; }

            // Java.g:1077:5: ( conditionalAndExpression ( '||' conditionalAndExpression )* )
            // Java.g:1077:9: conditionalAndExpression ( '||' conditionalAndExpression )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression6744);
            conditionalAndExpression412=conditionalAndExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, conditionalAndExpression412.getTree());

            // Java.g:1078:9: ( '||' conditionalAndExpression )*
            loop116:
            do {
                int alt116=2;
                int LA116_0 = input.LA(1);

                if ( (LA116_0==BARBAR) ) {
                    alt116=1;
                }


                switch (alt116) {
            	case 1 :
            	    // Java.g:1078:10: '||' conditionalAndExpression
            	    {
            	    string_literal413=(Token)match(input,BARBAR,FOLLOW_BARBAR_in_conditionalOrExpression6755); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal413_tree = 
            	    (Object)adaptor.create(string_literal413)
            	    ;
            	    adaptor.addChild(root_0, string_literal413_tree);
            	    }

            	    pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression6757);
            	    conditionalAndExpression414=conditionalAndExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, conditionalAndExpression414.getTree());

            	    }
            	    break;

            	default :
            	    break loop116;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 74, conditionalOrExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "conditionalOrExpression"


    public static class conditionalAndExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "conditionalAndExpression"
    // Java.g:1082:1: conditionalAndExpression : inclusiveOrExpression ( '&&' inclusiveOrExpression )* ;
    public final JavaParser.conditionalAndExpression_return conditionalAndExpression() throws RecognitionException {
        JavaParser.conditionalAndExpression_return retval = new JavaParser.conditionalAndExpression_return();
        retval.start = input.LT(1);

        int conditionalAndExpression_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal416=null;
        JavaParser.inclusiveOrExpression_return inclusiveOrExpression415 =null;

        JavaParser.inclusiveOrExpression_return inclusiveOrExpression417 =null;


        Object string_literal416_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 75) ) { return retval; }

            // Java.g:1083:5: ( inclusiveOrExpression ( '&&' inclusiveOrExpression )* )
            // Java.g:1083:9: inclusiveOrExpression ( '&&' inclusiveOrExpression )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression6788);
            inclusiveOrExpression415=inclusiveOrExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, inclusiveOrExpression415.getTree());

            // Java.g:1084:9: ( '&&' inclusiveOrExpression )*
            loop117:
            do {
                int alt117=2;
                int LA117_0 = input.LA(1);

                if ( (LA117_0==AMPAMP) ) {
                    alt117=1;
                }


                switch (alt117) {
            	case 1 :
            	    // Java.g:1084:10: '&&' inclusiveOrExpression
            	    {
            	    string_literal416=(Token)match(input,AMPAMP,FOLLOW_AMPAMP_in_conditionalAndExpression6799); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal416_tree = 
            	    (Object)adaptor.create(string_literal416)
            	    ;
            	    adaptor.addChild(root_0, string_literal416_tree);
            	    }

            	    pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression6801);
            	    inclusiveOrExpression417=inclusiveOrExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, inclusiveOrExpression417.getTree());

            	    }
            	    break;

            	default :
            	    break loop117;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 75, conditionalAndExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "conditionalAndExpression"


    public static class inclusiveOrExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "inclusiveOrExpression"
    // Java.g:1088:1: inclusiveOrExpression : exclusiveOrExpression ( '|' exclusiveOrExpression )* ;
    public final JavaParser.inclusiveOrExpression_return inclusiveOrExpression() throws RecognitionException {
        JavaParser.inclusiveOrExpression_return retval = new JavaParser.inclusiveOrExpression_return();
        retval.start = input.LT(1);

        int inclusiveOrExpression_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal419=null;
        JavaParser.exclusiveOrExpression_return exclusiveOrExpression418 =null;

        JavaParser.exclusiveOrExpression_return exclusiveOrExpression420 =null;


        Object char_literal419_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 76) ) { return retval; }

            // Java.g:1089:5: ( exclusiveOrExpression ( '|' exclusiveOrExpression )* )
            // Java.g:1089:9: exclusiveOrExpression ( '|' exclusiveOrExpression )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression6832);
            exclusiveOrExpression418=exclusiveOrExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, exclusiveOrExpression418.getTree());

            // Java.g:1090:9: ( '|' exclusiveOrExpression )*
            loop118:
            do {
                int alt118=2;
                int LA118_0 = input.LA(1);

                if ( (LA118_0==BAR) ) {
                    alt118=1;
                }


                switch (alt118) {
            	case 1 :
            	    // Java.g:1090:10: '|' exclusiveOrExpression
            	    {
            	    char_literal419=(Token)match(input,BAR,FOLLOW_BAR_in_inclusiveOrExpression6843); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal419_tree = 
            	    (Object)adaptor.create(char_literal419)
            	    ;
            	    adaptor.addChild(root_0, char_literal419_tree);
            	    }

            	    pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression6845);
            	    exclusiveOrExpression420=exclusiveOrExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, exclusiveOrExpression420.getTree());

            	    }
            	    break;

            	default :
            	    break loop118;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 76, inclusiveOrExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "inclusiveOrExpression"


    public static class exclusiveOrExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "exclusiveOrExpression"
    // Java.g:1094:1: exclusiveOrExpression : andExpression ( '^' andExpression )* ;
    public final JavaParser.exclusiveOrExpression_return exclusiveOrExpression() throws RecognitionException {
        JavaParser.exclusiveOrExpression_return retval = new JavaParser.exclusiveOrExpression_return();
        retval.start = input.LT(1);

        int exclusiveOrExpression_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal422=null;
        JavaParser.andExpression_return andExpression421 =null;

        JavaParser.andExpression_return andExpression423 =null;


        Object char_literal422_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 77) ) { return retval; }

            // Java.g:1095:5: ( andExpression ( '^' andExpression )* )
            // Java.g:1095:9: andExpression ( '^' andExpression )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression6876);
            andExpression421=andExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, andExpression421.getTree());

            // Java.g:1096:9: ( '^' andExpression )*
            loop119:
            do {
                int alt119=2;
                int LA119_0 = input.LA(1);

                if ( (LA119_0==CARET) ) {
                    alt119=1;
                }


                switch (alt119) {
            	case 1 :
            	    // Java.g:1096:10: '^' andExpression
            	    {
            	    char_literal422=(Token)match(input,CARET,FOLLOW_CARET_in_exclusiveOrExpression6887); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal422_tree = 
            	    (Object)adaptor.create(char_literal422)
            	    ;
            	    adaptor.addChild(root_0, char_literal422_tree);
            	    }

            	    pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression6889);
            	    andExpression423=andExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, andExpression423.getTree());

            	    }
            	    break;

            	default :
            	    break loop119;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 77, exclusiveOrExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "exclusiveOrExpression"


    public static class andExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "andExpression"
    // Java.g:1100:1: andExpression : equalityExpression ( '&' equalityExpression )* ;
    public final JavaParser.andExpression_return andExpression() throws RecognitionException {
        JavaParser.andExpression_return retval = new JavaParser.andExpression_return();
        retval.start = input.LT(1);

        int andExpression_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal425=null;
        JavaParser.equalityExpression_return equalityExpression424 =null;

        JavaParser.equalityExpression_return equalityExpression426 =null;


        Object char_literal425_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 78) ) { return retval; }

            // Java.g:1101:5: ( equalityExpression ( '&' equalityExpression )* )
            // Java.g:1101:9: equalityExpression ( '&' equalityExpression )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_equalityExpression_in_andExpression6920);
            equalityExpression424=equalityExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, equalityExpression424.getTree());

            // Java.g:1102:9: ( '&' equalityExpression )*
            loop120:
            do {
                int alt120=2;
                int LA120_0 = input.LA(1);

                if ( (LA120_0==AMP) ) {
                    alt120=1;
                }


                switch (alt120) {
            	case 1 :
            	    // Java.g:1102:10: '&' equalityExpression
            	    {
            	    char_literal425=(Token)match(input,AMP,FOLLOW_AMP_in_andExpression6931); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    char_literal425_tree = 
            	    (Object)adaptor.create(char_literal425)
            	    ;
            	    adaptor.addChild(root_0, char_literal425_tree);
            	    }

            	    pushFollow(FOLLOW_equalityExpression_in_andExpression6933);
            	    equalityExpression426=equalityExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, equalityExpression426.getTree());

            	    }
            	    break;

            	default :
            	    break loop120;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 78, andExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "andExpression"


    public static class equalityExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "equalityExpression"
    // Java.g:1106:1: equalityExpression : instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )* ;
    public final JavaParser.equalityExpression_return equalityExpression() throws RecognitionException {
        JavaParser.equalityExpression_return retval = new JavaParser.equalityExpression_return();
        retval.start = input.LT(1);

        int equalityExpression_StartIndex = input.index();

        Object root_0 = null;

        Token set428=null;
        JavaParser.instanceOfExpression_return instanceOfExpression427 =null;

        JavaParser.instanceOfExpression_return instanceOfExpression429 =null;


        Object set428_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 79) ) { return retval; }

            // Java.g:1107:5: ( instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )* )
            // Java.g:1107:9: instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression6964);
            instanceOfExpression427=instanceOfExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, instanceOfExpression427.getTree());

            // Java.g:1108:9: ( ( '==' | '!=' ) instanceOfExpression )*
            loop121:
            do {
                int alt121=2;
                int LA121_0 = input.LA(1);

                if ( (LA121_0==BANGEQ||LA121_0==EQEQ) ) {
                    alt121=1;
                }


                switch (alt121) {
            	case 1 :
            	    // Java.g:1109:13: ( '==' | '!=' ) instanceOfExpression
            	    {
            	    set428=(Token)input.LT(1);

            	    if ( input.LA(1)==BANGEQ||input.LA(1)==EQEQ ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, 
            	        (Object)adaptor.create(set428)
            	        );
            	        state.errorRecovery=false;
            	        state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression7041);
            	    instanceOfExpression429=instanceOfExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, instanceOfExpression429.getTree());

            	    }
            	    break;

            	default :
            	    break loop121;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 79, equalityExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "equalityExpression"


    public static class instanceOfExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "instanceOfExpression"
    // Java.g:1116:1: instanceOfExpression : relationalExpression ( 'instanceof' type )? ;
    public final JavaParser.instanceOfExpression_return instanceOfExpression() throws RecognitionException {
        JavaParser.instanceOfExpression_return retval = new JavaParser.instanceOfExpression_return();
        retval.start = input.LT(1);

        int instanceOfExpression_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal431=null;
        JavaParser.relationalExpression_return relationalExpression430 =null;

        JavaParser.type_return type432 =null;


        Object string_literal431_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 80) ) { return retval; }

            // Java.g:1117:5: ( relationalExpression ( 'instanceof' type )? )
            // Java.g:1117:9: relationalExpression ( 'instanceof' type )?
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression7072);
            relationalExpression430=relationalExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, relationalExpression430.getTree());

            // Java.g:1118:9: ( 'instanceof' type )?
            int alt122=2;
            int LA122_0 = input.LA(1);

            if ( (LA122_0==INSTANCEOF) ) {
                alt122=1;
            }
            switch (alt122) {
                case 1 :
                    // Java.g:1118:10: 'instanceof' type
                    {
                    string_literal431=(Token)match(input,INSTANCEOF,FOLLOW_INSTANCEOF_in_instanceOfExpression7083); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal431_tree = 
                    (Object)adaptor.create(string_literal431)
                    ;
                    adaptor.addChild(root_0, string_literal431_tree);
                    }

                    pushFollow(FOLLOW_type_in_instanceOfExpression7085);
                    type432=type();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, type432.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 80, instanceOfExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "instanceOfExpression"


    public static class relationalExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "relationalExpression"
    // Java.g:1122:1: relationalExpression : shiftExpression ( relationalOp shiftExpression )* ;
    public final JavaParser.relationalExpression_return relationalExpression() throws RecognitionException {
        JavaParser.relationalExpression_return retval = new JavaParser.relationalExpression_return();
        retval.start = input.LT(1);

        int relationalExpression_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.shiftExpression_return shiftExpression433 =null;

        JavaParser.relationalOp_return relationalOp434 =null;

        JavaParser.shiftExpression_return shiftExpression435 =null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 81) ) { return retval; }

            // Java.g:1123:5: ( shiftExpression ( relationalOp shiftExpression )* )
            // Java.g:1123:9: shiftExpression ( relationalOp shiftExpression )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_shiftExpression_in_relationalExpression7116);
            shiftExpression433=shiftExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, shiftExpression433.getTree());

            // Java.g:1124:9: ( relationalOp shiftExpression )*
            loop123:
            do {
                int alt123=2;
                int LA123_0 = input.LA(1);

                if ( (LA123_0==LT) ) {
                    int LA123_2 = input.LA(2);

                    if ( (LA123_2==BANG||LA123_2==BOOLEAN||LA123_2==BYTE||(LA123_2 >= CHAR && LA123_2 <= CHARLITERAL)||(LA123_2 >= DOUBLE && LA123_2 <= DOUBLELITERAL)||LA123_2==EQ||LA123_2==FALSE||(LA123_2 >= FLOAT && LA123_2 <= FLOATLITERAL)||LA123_2==IDENTIFIER||LA123_2==INT||LA123_2==INTLITERAL||(LA123_2 >= LONG && LA123_2 <= LPAREN)||(LA123_2 >= NEW && LA123_2 <= NULL)||LA123_2==PLUS||LA123_2==PLUSPLUS||LA123_2==SHORT||(LA123_2 >= STRINGLITERAL && LA123_2 <= SUB)||(LA123_2 >= SUBSUB && LA123_2 <= SUPER)||LA123_2==THIS||LA123_2==TILDE||LA123_2==TRUE||LA123_2==VOID) ) {
                        alt123=1;
                    }


                }
                else if ( (LA123_0==GT) ) {
                    int LA123_3 = input.LA(2);

                    if ( (LA123_3==BANG||LA123_3==BOOLEAN||LA123_3==BYTE||(LA123_3 >= CHAR && LA123_3 <= CHARLITERAL)||(LA123_3 >= DOUBLE && LA123_3 <= DOUBLELITERAL)||LA123_3==EQ||LA123_3==FALSE||(LA123_3 >= FLOAT && LA123_3 <= FLOATLITERAL)||LA123_3==IDENTIFIER||LA123_3==INT||LA123_3==INTLITERAL||(LA123_3 >= LONG && LA123_3 <= LPAREN)||(LA123_3 >= NEW && LA123_3 <= NULL)||LA123_3==PLUS||LA123_3==PLUSPLUS||LA123_3==SHORT||(LA123_3 >= STRINGLITERAL && LA123_3 <= SUB)||(LA123_3 >= SUBSUB && LA123_3 <= SUPER)||LA123_3==THIS||LA123_3==TILDE||LA123_3==TRUE||LA123_3==VOID) ) {
                        alt123=1;
                    }


                }


                switch (alt123) {
            	case 1 :
            	    // Java.g:1124:10: relationalOp shiftExpression
            	    {
            	    pushFollow(FOLLOW_relationalOp_in_relationalExpression7127);
            	    relationalOp434=relationalOp();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, relationalOp434.getTree());

            	    pushFollow(FOLLOW_shiftExpression_in_relationalExpression7129);
            	    shiftExpression435=shiftExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, shiftExpression435.getTree());

            	    }
            	    break;

            	default :
            	    break loop123;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 81, relationalExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "relationalExpression"


    public static class relationalOp_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "relationalOp"
    // Java.g:1128:1: relationalOp : ( '<' '=' | '>' '=' | '<' | '>' );
    public final JavaParser.relationalOp_return relationalOp() throws RecognitionException {
        JavaParser.relationalOp_return retval = new JavaParser.relationalOp_return();
        retval.start = input.LT(1);

        int relationalOp_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal436=null;
        Token char_literal437=null;
        Token char_literal438=null;
        Token char_literal439=null;
        Token char_literal440=null;
        Token char_literal441=null;

        Object char_literal436_tree=null;
        Object char_literal437_tree=null;
        Object char_literal438_tree=null;
        Object char_literal439_tree=null;
        Object char_literal440_tree=null;
        Object char_literal441_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 82) ) { return retval; }

            // Java.g:1129:5: ( '<' '=' | '>' '=' | '<' | '>' )
            int alt124=4;
            int LA124_0 = input.LA(1);

            if ( (LA124_0==LT) ) {
                int LA124_1 = input.LA(2);

                if ( (LA124_1==EQ) ) {
                    alt124=1;
                }
                else if ( (LA124_1==BANG||LA124_1==BOOLEAN||LA124_1==BYTE||(LA124_1 >= CHAR && LA124_1 <= CHARLITERAL)||(LA124_1 >= DOUBLE && LA124_1 <= DOUBLELITERAL)||LA124_1==FALSE||(LA124_1 >= FLOAT && LA124_1 <= FLOATLITERAL)||LA124_1==IDENTIFIER||LA124_1==INT||LA124_1==INTLITERAL||(LA124_1 >= LONG && LA124_1 <= LPAREN)||(LA124_1 >= NEW && LA124_1 <= NULL)||LA124_1==PLUS||LA124_1==PLUSPLUS||LA124_1==SHORT||(LA124_1 >= STRINGLITERAL && LA124_1 <= SUB)||(LA124_1 >= SUBSUB && LA124_1 <= SUPER)||LA124_1==THIS||LA124_1==TILDE||LA124_1==TRUE||LA124_1==VOID) ) {
                    alt124=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 124, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA124_0==GT) ) {
                int LA124_2 = input.LA(2);

                if ( (LA124_2==EQ) ) {
                    alt124=2;
                }
                else if ( (LA124_2==BANG||LA124_2==BOOLEAN||LA124_2==BYTE||(LA124_2 >= CHAR && LA124_2 <= CHARLITERAL)||(LA124_2 >= DOUBLE && LA124_2 <= DOUBLELITERAL)||LA124_2==FALSE||(LA124_2 >= FLOAT && LA124_2 <= FLOATLITERAL)||LA124_2==IDENTIFIER||LA124_2==INT||LA124_2==INTLITERAL||(LA124_2 >= LONG && LA124_2 <= LPAREN)||(LA124_2 >= NEW && LA124_2 <= NULL)||LA124_2==PLUS||LA124_2==PLUSPLUS||LA124_2==SHORT||(LA124_2 >= STRINGLITERAL && LA124_2 <= SUB)||(LA124_2 >= SUBSUB && LA124_2 <= SUPER)||LA124_2==THIS||LA124_2==TILDE||LA124_2==TRUE||LA124_2==VOID) ) {
                    alt124=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 124, 2, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 124, 0, input);

                throw nvae;

            }
            switch (alt124) {
                case 1 :
                    // Java.g:1129:10: '<' '='
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal436=(Token)match(input,LT,FOLLOW_LT_in_relationalOp7161); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal436_tree = 
                    (Object)adaptor.create(char_literal436)
                    ;
                    adaptor.addChild(root_0, char_literal436_tree);
                    }

                    char_literal437=(Token)match(input,EQ,FOLLOW_EQ_in_relationalOp7163); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal437_tree = 
                    (Object)adaptor.create(char_literal437)
                    ;
                    adaptor.addChild(root_0, char_literal437_tree);
                    }

                    }
                    break;
                case 2 :
                    // Java.g:1130:10: '>' '='
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal438=(Token)match(input,GT,FOLLOW_GT_in_relationalOp7174); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal438_tree = 
                    (Object)adaptor.create(char_literal438)
                    ;
                    adaptor.addChild(root_0, char_literal438_tree);
                    }

                    char_literal439=(Token)match(input,EQ,FOLLOW_EQ_in_relationalOp7176); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal439_tree = 
                    (Object)adaptor.create(char_literal439)
                    ;
                    adaptor.addChild(root_0, char_literal439_tree);
                    }

                    }
                    break;
                case 3 :
                    // Java.g:1131:9: '<'
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal440=(Token)match(input,LT,FOLLOW_LT_in_relationalOp7186); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal440_tree = 
                    (Object)adaptor.create(char_literal440)
                    ;
                    adaptor.addChild(root_0, char_literal440_tree);
                    }

                    }
                    break;
                case 4 :
                    // Java.g:1132:9: '>'
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal441=(Token)match(input,GT,FOLLOW_GT_in_relationalOp7196); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal441_tree = 
                    (Object)adaptor.create(char_literal441)
                    ;
                    adaptor.addChild(root_0, char_literal441_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 82, relationalOp_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "relationalOp"


    public static class shiftExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "shiftExpression"
    // Java.g:1135:1: shiftExpression : additiveExpression ( shiftOp additiveExpression )* ;
    public final JavaParser.shiftExpression_return shiftExpression() throws RecognitionException {
        JavaParser.shiftExpression_return retval = new JavaParser.shiftExpression_return();
        retval.start = input.LT(1);

        int shiftExpression_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.additiveExpression_return additiveExpression442 =null;

        JavaParser.shiftOp_return shiftOp443 =null;

        JavaParser.additiveExpression_return additiveExpression444 =null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 83) ) { return retval; }

            // Java.g:1136:5: ( additiveExpression ( shiftOp additiveExpression )* )
            // Java.g:1136:9: additiveExpression ( shiftOp additiveExpression )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_additiveExpression_in_shiftExpression7216);
            additiveExpression442=additiveExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, additiveExpression442.getTree());

            // Java.g:1137:9: ( shiftOp additiveExpression )*
            loop125:
            do {
                int alt125=2;
                int LA125_0 = input.LA(1);

                if ( (LA125_0==LT) ) {
                    int LA125_1 = input.LA(2);

                    if ( (LA125_1==LT) ) {
                        int LA125_4 = input.LA(3);

                        if ( (LA125_4==BANG||LA125_4==BOOLEAN||LA125_4==BYTE||(LA125_4 >= CHAR && LA125_4 <= CHARLITERAL)||(LA125_4 >= DOUBLE && LA125_4 <= DOUBLELITERAL)||LA125_4==FALSE||(LA125_4 >= FLOAT && LA125_4 <= FLOATLITERAL)||LA125_4==IDENTIFIER||LA125_4==INT||LA125_4==INTLITERAL||(LA125_4 >= LONG && LA125_4 <= LPAREN)||(LA125_4 >= NEW && LA125_4 <= NULL)||LA125_4==PLUS||LA125_4==PLUSPLUS||LA125_4==SHORT||(LA125_4 >= STRINGLITERAL && LA125_4 <= SUB)||(LA125_4 >= SUBSUB && LA125_4 <= SUPER)||LA125_4==THIS||LA125_4==TILDE||LA125_4==TRUE||LA125_4==VOID) ) {
                            alt125=1;
                        }


                    }


                }
                else if ( (LA125_0==GT) ) {
                    int LA125_2 = input.LA(2);

                    if ( (LA125_2==GT) ) {
                        int LA125_5 = input.LA(3);

                        if ( (LA125_5==GT) ) {
                            int LA125_7 = input.LA(4);

                            if ( (LA125_7==BANG||LA125_7==BOOLEAN||LA125_7==BYTE||(LA125_7 >= CHAR && LA125_7 <= CHARLITERAL)||(LA125_7 >= DOUBLE && LA125_7 <= DOUBLELITERAL)||LA125_7==FALSE||(LA125_7 >= FLOAT && LA125_7 <= FLOATLITERAL)||LA125_7==IDENTIFIER||LA125_7==INT||LA125_7==INTLITERAL||(LA125_7 >= LONG && LA125_7 <= LPAREN)||(LA125_7 >= NEW && LA125_7 <= NULL)||LA125_7==PLUS||LA125_7==PLUSPLUS||LA125_7==SHORT||(LA125_7 >= STRINGLITERAL && LA125_7 <= SUB)||(LA125_7 >= SUBSUB && LA125_7 <= SUPER)||LA125_7==THIS||LA125_7==TILDE||LA125_7==TRUE||LA125_7==VOID) ) {
                                alt125=1;
                            }


                        }
                        else if ( (LA125_5==BANG||LA125_5==BOOLEAN||LA125_5==BYTE||(LA125_5 >= CHAR && LA125_5 <= CHARLITERAL)||(LA125_5 >= DOUBLE && LA125_5 <= DOUBLELITERAL)||LA125_5==FALSE||(LA125_5 >= FLOAT && LA125_5 <= FLOATLITERAL)||LA125_5==IDENTIFIER||LA125_5==INT||LA125_5==INTLITERAL||(LA125_5 >= LONG && LA125_5 <= LPAREN)||(LA125_5 >= NEW && LA125_5 <= NULL)||LA125_5==PLUS||LA125_5==PLUSPLUS||LA125_5==SHORT||(LA125_5 >= STRINGLITERAL && LA125_5 <= SUB)||(LA125_5 >= SUBSUB && LA125_5 <= SUPER)||LA125_5==THIS||LA125_5==TILDE||LA125_5==TRUE||LA125_5==VOID) ) {
                            alt125=1;
                        }


                    }


                }


                switch (alt125) {
            	case 1 :
            	    // Java.g:1137:10: shiftOp additiveExpression
            	    {
            	    pushFollow(FOLLOW_shiftOp_in_shiftExpression7227);
            	    shiftOp443=shiftOp();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, shiftOp443.getTree());

            	    pushFollow(FOLLOW_additiveExpression_in_shiftExpression7229);
            	    additiveExpression444=additiveExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, additiveExpression444.getTree());

            	    }
            	    break;

            	default :
            	    break loop125;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 83, shiftExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "shiftExpression"


    public static class shiftOp_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "shiftOp"
    // Java.g:1142:1: shiftOp : ( '<' '<' | '>' '>' '>' | '>' '>' );
    public final JavaParser.shiftOp_return shiftOp() throws RecognitionException {
        JavaParser.shiftOp_return retval = new JavaParser.shiftOp_return();
        retval.start = input.LT(1);

        int shiftOp_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal445=null;
        Token char_literal446=null;
        Token char_literal447=null;
        Token char_literal448=null;
        Token char_literal449=null;
        Token char_literal450=null;
        Token char_literal451=null;

        Object char_literal445_tree=null;
        Object char_literal446_tree=null;
        Object char_literal447_tree=null;
        Object char_literal448_tree=null;
        Object char_literal449_tree=null;
        Object char_literal450_tree=null;
        Object char_literal451_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 84) ) { return retval; }

            // Java.g:1143:5: ( '<' '<' | '>' '>' '>' | '>' '>' )
            int alt126=3;
            int LA126_0 = input.LA(1);

            if ( (LA126_0==LT) ) {
                alt126=1;
            }
            else if ( (LA126_0==GT) ) {
                int LA126_2 = input.LA(2);

                if ( (LA126_2==GT) ) {
                    int LA126_3 = input.LA(3);

                    if ( (LA126_3==GT) ) {
                        alt126=2;
                    }
                    else if ( (LA126_3==BANG||LA126_3==BOOLEAN||LA126_3==BYTE||(LA126_3 >= CHAR && LA126_3 <= CHARLITERAL)||(LA126_3 >= DOUBLE && LA126_3 <= DOUBLELITERAL)||LA126_3==FALSE||(LA126_3 >= FLOAT && LA126_3 <= FLOATLITERAL)||LA126_3==IDENTIFIER||LA126_3==INT||LA126_3==INTLITERAL||(LA126_3 >= LONG && LA126_3 <= LPAREN)||(LA126_3 >= NEW && LA126_3 <= NULL)||LA126_3==PLUS||LA126_3==PLUSPLUS||LA126_3==SHORT||(LA126_3 >= STRINGLITERAL && LA126_3 <= SUB)||(LA126_3 >= SUBSUB && LA126_3 <= SUPER)||LA126_3==THIS||LA126_3==TILDE||LA126_3==TRUE||LA126_3==VOID) ) {
                        alt126=3;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 126, 3, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 126, 2, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 126, 0, input);

                throw nvae;

            }
            switch (alt126) {
                case 1 :
                    // Java.g:1143:10: '<' '<'
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal445=(Token)match(input,LT,FOLLOW_LT_in_shiftOp7262); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal445_tree = 
                    (Object)adaptor.create(char_literal445)
                    ;
                    adaptor.addChild(root_0, char_literal445_tree);
                    }

                    char_literal446=(Token)match(input,LT,FOLLOW_LT_in_shiftOp7264); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal446_tree = 
                    (Object)adaptor.create(char_literal446)
                    ;
                    adaptor.addChild(root_0, char_literal446_tree);
                    }

                    }
                    break;
                case 2 :
                    // Java.g:1144:10: '>' '>' '>'
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal447=(Token)match(input,GT,FOLLOW_GT_in_shiftOp7275); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal447_tree = 
                    (Object)adaptor.create(char_literal447)
                    ;
                    adaptor.addChild(root_0, char_literal447_tree);
                    }

                    char_literal448=(Token)match(input,GT,FOLLOW_GT_in_shiftOp7277); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal448_tree = 
                    (Object)adaptor.create(char_literal448)
                    ;
                    adaptor.addChild(root_0, char_literal448_tree);
                    }

                    char_literal449=(Token)match(input,GT,FOLLOW_GT_in_shiftOp7279); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal449_tree = 
                    (Object)adaptor.create(char_literal449)
                    ;
                    adaptor.addChild(root_0, char_literal449_tree);
                    }

                    }
                    break;
                case 3 :
                    // Java.g:1145:10: '>' '>'
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal450=(Token)match(input,GT,FOLLOW_GT_in_shiftOp7290); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal450_tree = 
                    (Object)adaptor.create(char_literal450)
                    ;
                    adaptor.addChild(root_0, char_literal450_tree);
                    }

                    char_literal451=(Token)match(input,GT,FOLLOW_GT_in_shiftOp7292); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal451_tree = 
                    (Object)adaptor.create(char_literal451)
                    ;
                    adaptor.addChild(root_0, char_literal451_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 84, shiftOp_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "shiftOp"


    public static class additiveExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "additiveExpression"
    // Java.g:1149:1: additiveExpression : multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )* ;
    public final JavaParser.additiveExpression_return additiveExpression() throws RecognitionException {
        JavaParser.additiveExpression_return retval = new JavaParser.additiveExpression_return();
        retval.start = input.LT(1);

        int additiveExpression_StartIndex = input.index();

        Object root_0 = null;

        Token set453=null;
        JavaParser.multiplicativeExpression_return multiplicativeExpression452 =null;

        JavaParser.multiplicativeExpression_return multiplicativeExpression454 =null;


        Object set453_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 85) ) { return retval; }

            // Java.g:1150:5: ( multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )* )
            // Java.g:1150:9: multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression7313);
            multiplicativeExpression452=multiplicativeExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, multiplicativeExpression452.getTree());

            // Java.g:1151:9: ( ( '+' | '-' ) multiplicativeExpression )*
            loop127:
            do {
                int alt127=2;
                int LA127_0 = input.LA(1);

                if ( (LA127_0==PLUS||LA127_0==SUB) ) {
                    alt127=1;
                }


                switch (alt127) {
            	case 1 :
            	    // Java.g:1152:13: ( '+' | '-' ) multiplicativeExpression
            	    {
            	    set453=(Token)input.LT(1);

            	    if ( input.LA(1)==PLUS||input.LA(1)==SUB ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, 
            	        (Object)adaptor.create(set453)
            	        );
            	        state.errorRecovery=false;
            	        state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression7390);
            	    multiplicativeExpression454=multiplicativeExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, multiplicativeExpression454.getTree());

            	    }
            	    break;

            	default :
            	    break loop127;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 85, additiveExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "additiveExpression"


    public static class multiplicativeExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "multiplicativeExpression"
    // Java.g:1159:1: multiplicativeExpression : unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* ;
    public final JavaParser.multiplicativeExpression_return multiplicativeExpression() throws RecognitionException {
        JavaParser.multiplicativeExpression_return retval = new JavaParser.multiplicativeExpression_return();
        retval.start = input.LT(1);

        int multiplicativeExpression_StartIndex = input.index();

        Object root_0 = null;

        Token set456=null;
        JavaParser.unaryExpression_return unaryExpression455 =null;

        JavaParser.unaryExpression_return unaryExpression457 =null;


        Object set456_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 86) ) { return retval; }

            // Java.g:1160:5: ( unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* )
            // Java.g:1161:9: unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression7428);
            unaryExpression455=unaryExpression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression455.getTree());

            // Java.g:1162:9: ( ( '*' | '/' | '%' ) unaryExpression )*
            loop128:
            do {
                int alt128=2;
                int LA128_0 = input.LA(1);

                if ( (LA128_0==PERCENT||LA128_0==SLASH||LA128_0==STAR) ) {
                    alt128=1;
                }


                switch (alt128) {
            	case 1 :
            	    // Java.g:1163:13: ( '*' | '/' | '%' ) unaryExpression
            	    {
            	    set456=(Token)input.LT(1);

            	    if ( input.LA(1)==PERCENT||input.LA(1)==SLASH||input.LA(1)==STAR ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) adaptor.addChild(root_0, 
            	        (Object)adaptor.create(set456)
            	        );
            	        state.errorRecovery=false;
            	        state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression7523);
            	    unaryExpression457=unaryExpression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression457.getTree());

            	    }
            	    break;

            	default :
            	    break loop128;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 86, multiplicativeExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "multiplicativeExpression"


    public static class unaryExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "unaryExpression"
    // Java.g:1175:1: unaryExpression : ( '+' unaryExpression | '-' unaryExpression | '++' unaryExpression | '--' unaryExpression | unaryExpressionNotPlusMinus );
    public final JavaParser.unaryExpression_return unaryExpression() throws RecognitionException {
        JavaParser.unaryExpression_return retval = new JavaParser.unaryExpression_return();
        retval.start = input.LT(1);

        int unaryExpression_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal458=null;
        Token char_literal460=null;
        Token string_literal462=null;
        Token string_literal464=null;
        JavaParser.unaryExpression_return unaryExpression459 =null;

        JavaParser.unaryExpression_return unaryExpression461 =null;

        JavaParser.unaryExpression_return unaryExpression463 =null;

        JavaParser.unaryExpression_return unaryExpression465 =null;

        JavaParser.unaryExpressionNotPlusMinus_return unaryExpressionNotPlusMinus466 =null;


        Object char_literal458_tree=null;
        Object char_literal460_tree=null;
        Object string_literal462_tree=null;
        Object string_literal464_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 87) ) { return retval; }

            // Java.g:1176:5: ( '+' unaryExpression | '-' unaryExpression | '++' unaryExpression | '--' unaryExpression | unaryExpressionNotPlusMinus )
            int alt129=5;
            switch ( input.LA(1) ) {
            case PLUS:
                {
                alt129=1;
                }
                break;
            case SUB:
                {
                alt129=2;
                }
                break;
            case PLUSPLUS:
                {
                alt129=3;
                }
                break;
            case SUBSUB:
                {
                alt129=4;
                }
                break;
            case BANG:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CHARLITERAL:
            case DOUBLE:
            case DOUBLELITERAL:
            case FALSE:
            case FLOAT:
            case FLOATLITERAL:
            case IDENTIFIER:
            case INT:
            case INTLITERAL:
            case LONG:
            case LONGLITERAL:
            case LPAREN:
            case NEW:
            case NULL:
            case SHORT:
            case STRINGLITERAL:
            case SUPER:
            case THIS:
            case TILDE:
            case TRUE:
            case VOID:
                {
                alt129=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 129, 0, input);

                throw nvae;

            }

            switch (alt129) {
                case 1 :
                    // Java.g:1176:9: '+' unaryExpression
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal458=(Token)match(input,PLUS,FOLLOW_PLUS_in_unaryExpression7556); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal458_tree = 
                    (Object)adaptor.create(char_literal458)
                    ;
                    adaptor.addChild(root_0, char_literal458_tree);
                    }

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression7559);
                    unaryExpression459=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression459.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:1177:9: '-' unaryExpression
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal460=(Token)match(input,SUB,FOLLOW_SUB_in_unaryExpression7569); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal460_tree = 
                    (Object)adaptor.create(char_literal460)
                    ;
                    adaptor.addChild(root_0, char_literal460_tree);
                    }

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression7571);
                    unaryExpression461=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression461.getTree());

                    }
                    break;
                case 3 :
                    // Java.g:1178:9: '++' unaryExpression
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal462=(Token)match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryExpression7581); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal462_tree = 
                    (Object)adaptor.create(string_literal462)
                    ;
                    adaptor.addChild(root_0, string_literal462_tree);
                    }

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression7583);
                    unaryExpression463=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression463.getTree());

                    }
                    break;
                case 4 :
                    // Java.g:1179:9: '--' unaryExpression
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal464=(Token)match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryExpression7593); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal464_tree = 
                    (Object)adaptor.create(string_literal464)
                    ;
                    adaptor.addChild(root_0, string_literal464_tree);
                    }

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression7595);
                    unaryExpression465=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression465.getTree());

                    }
                    break;
                case 5 :
                    // Java.g:1180:9: unaryExpressionNotPlusMinus
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression7605);
                    unaryExpressionNotPlusMinus466=unaryExpressionNotPlusMinus();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpressionNotPlusMinus466.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 87, unaryExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "unaryExpression"


    public static class unaryExpressionNotPlusMinus_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "unaryExpressionNotPlusMinus"
    // Java.g:1183:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );
    public final JavaParser.unaryExpressionNotPlusMinus_return unaryExpressionNotPlusMinus() throws RecognitionException {
        JavaParser.unaryExpressionNotPlusMinus_return retval = new JavaParser.unaryExpressionNotPlusMinus_return();
        retval.start = input.LT(1);

        int unaryExpressionNotPlusMinus_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal467=null;
        Token char_literal469=null;
        Token set474=null;
        JavaParser.unaryExpression_return unaryExpression468 =null;

        JavaParser.unaryExpression_return unaryExpression470 =null;

        JavaParser.castExpression_return castExpression471 =null;

        JavaParser.primary_return primary472 =null;

        JavaParser.selector_return selector473 =null;


        Object char_literal467_tree=null;
        Object char_literal469_tree=null;
        Object set474_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 88) ) { return retval; }

            // Java.g:1184:5: ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? )
            int alt132=4;
            switch ( input.LA(1) ) {
            case TILDE:
                {
                alt132=1;
                }
                break;
            case BANG:
                {
                alt132=2;
                }
                break;
            case LPAREN:
                {
                int LA132_3 = input.LA(2);

                if ( (synpred203_Java()) ) {
                    alt132=3;
                }
                else if ( (true) ) {
                    alt132=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 132, 3, input);

                    throw nvae;

                }
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CHARLITERAL:
            case DOUBLE:
            case DOUBLELITERAL:
            case FALSE:
            case FLOAT:
            case FLOATLITERAL:
            case IDENTIFIER:
            case INT:
            case INTLITERAL:
            case LONG:
            case LONGLITERAL:
            case NEW:
            case NULL:
            case SHORT:
            case STRINGLITERAL:
            case SUPER:
            case THIS:
            case TRUE:
            case VOID:
                {
                alt132=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 132, 0, input);

                throw nvae;

            }

            switch (alt132) {
                case 1 :
                    // Java.g:1184:9: '~' unaryExpression
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal467=(Token)match(input,TILDE,FOLLOW_TILDE_in_unaryExpressionNotPlusMinus7625); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal467_tree = 
                    (Object)adaptor.create(char_literal467)
                    ;
                    adaptor.addChild(root_0, char_literal467_tree);
                    }

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus7627);
                    unaryExpression468=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression468.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:1185:9: '!' unaryExpression
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal469=(Token)match(input,BANG,FOLLOW_BANG_in_unaryExpressionNotPlusMinus7637); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal469_tree = 
                    (Object)adaptor.create(char_literal469)
                    ;
                    adaptor.addChild(root_0, char_literal469_tree);
                    }

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus7639);
                    unaryExpression470=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression470.getTree());

                    }
                    break;
                case 3 :
                    // Java.g:1186:9: castExpression
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_castExpression_in_unaryExpressionNotPlusMinus7649);
                    castExpression471=castExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, castExpression471.getTree());

                    }
                    break;
                case 4 :
                    // Java.g:1187:9: primary ( selector )* ( '++' | '--' )?
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_primary_in_unaryExpressionNotPlusMinus7659);
                    primary472=primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, primary472.getTree());

                    // Java.g:1188:9: ( selector )*
                    loop130:
                    do {
                        int alt130=2;
                        int LA130_0 = input.LA(1);

                        if ( (LA130_0==DOT||LA130_0==LBRACKET) ) {
                            alt130=1;
                        }


                        switch (alt130) {
                    	case 1 :
                    	    // Java.g:1188:10: selector
                    	    {
                    	    pushFollow(FOLLOW_selector_in_unaryExpressionNotPlusMinus7670);
                    	    selector473=selector();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, selector473.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop130;
                        }
                    } while (true);


                    // Java.g:1190:9: ( '++' | '--' )?
                    int alt131=2;
                    int LA131_0 = input.LA(1);

                    if ( (LA131_0==PLUSPLUS||LA131_0==SUBSUB) ) {
                        alt131=1;
                    }
                    switch (alt131) {
                        case 1 :
                            // Java.g:
                            {
                            set474=(Token)input.LT(1);

                            if ( input.LA(1)==PLUSPLUS||input.LA(1)==SUBSUB ) {
                                input.consume();
                                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                                (Object)adaptor.create(set474)
                                );
                                state.errorRecovery=false;
                                state.failed=false;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return retval;}
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            }
                            break;

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 88, unaryExpressionNotPlusMinus_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "unaryExpressionNotPlusMinus"


    public static class castExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "castExpression"
    // Java.g:1195:1: castExpression : ( '(' primitiveType ')' unaryExpression | '(' type ')' unaryExpressionNotPlusMinus );
    public final JavaParser.castExpression_return castExpression() throws RecognitionException {
        JavaParser.castExpression_return retval = new JavaParser.castExpression_return();
        retval.start = input.LT(1);

        int castExpression_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal475=null;
        Token char_literal477=null;
        Token char_literal479=null;
        Token char_literal481=null;
        JavaParser.primitiveType_return primitiveType476 =null;

        JavaParser.unaryExpression_return unaryExpression478 =null;

        JavaParser.type_return type480 =null;

        JavaParser.unaryExpressionNotPlusMinus_return unaryExpressionNotPlusMinus482 =null;


        Object char_literal475_tree=null;
        Object char_literal477_tree=null;
        Object char_literal479_tree=null;
        Object char_literal481_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 89) ) { return retval; }

            // Java.g:1196:5: ( '(' primitiveType ')' unaryExpression | '(' type ')' unaryExpressionNotPlusMinus )
            int alt133=2;
            int LA133_0 = input.LA(1);

            if ( (LA133_0==LPAREN) ) {
                int LA133_1 = input.LA(2);

                if ( (synpred207_Java()) ) {
                    alt133=1;
                }
                else if ( (true) ) {
                    alt133=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 133, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 133, 0, input);

                throw nvae;

            }
            switch (alt133) {
                case 1 :
                    // Java.g:1196:9: '(' primitiveType ')' unaryExpression
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal475=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_castExpression7740); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal475_tree = 
                    (Object)adaptor.create(char_literal475)
                    ;
                    adaptor.addChild(root_0, char_literal475_tree);
                    }

                    pushFollow(FOLLOW_primitiveType_in_castExpression7742);
                    primitiveType476=primitiveType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, primitiveType476.getTree());

                    char_literal477=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_castExpression7744); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal477_tree = 
                    (Object)adaptor.create(char_literal477)
                    ;
                    adaptor.addChild(root_0, char_literal477_tree);
                    }

                    pushFollow(FOLLOW_unaryExpression_in_castExpression7746);
                    unaryExpression478=unaryExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpression478.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:1197:9: '(' type ')' unaryExpressionNotPlusMinus
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal479=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_castExpression7756); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal479_tree = 
                    (Object)adaptor.create(char_literal479)
                    ;
                    adaptor.addChild(root_0, char_literal479_tree);
                    }

                    pushFollow(FOLLOW_type_in_castExpression7758);
                    type480=type();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, type480.getTree());

                    char_literal481=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_castExpression7760); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal481_tree = 
                    (Object)adaptor.create(char_literal481)
                    ;
                    adaptor.addChild(root_0, char_literal481_tree);
                    }

                    pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_castExpression7762);
                    unaryExpressionNotPlusMinus482=unaryExpressionNotPlusMinus();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, unaryExpressionNotPlusMinus482.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 89, castExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "castExpression"


    public static class primary_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "primary"
    // Java.g:1203:1: primary : ( parExpression | 'this' ( '.' IDENTIFIER )* ( identifierSuffix )? | IDENTIFIER ( '.' IDENTIFIER )* ( identifierSuffix )? | 'super' superSuffix | literal | creator | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' );
    public final JavaParser.primary_return primary() throws RecognitionException {
        JavaParser.primary_return retval = new JavaParser.primary_return();
        retval.start = input.LT(1);

        int primary_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal484=null;
        Token char_literal485=null;
        Token IDENTIFIER486=null;
        Token IDENTIFIER488=null;
        Token char_literal489=null;
        Token IDENTIFIER490=null;
        Token string_literal492=null;
        Token char_literal497=null;
        Token char_literal498=null;
        Token char_literal499=null;
        Token string_literal500=null;
        Token string_literal501=null;
        Token char_literal502=null;
        Token string_literal503=null;
        JavaParser.parExpression_return parExpression483 =null;

        JavaParser.identifierSuffix_return identifierSuffix487 =null;

        JavaParser.identifierSuffix_return identifierSuffix491 =null;

        JavaParser.superSuffix_return superSuffix493 =null;

        JavaParser.literal_return literal494 =null;

        JavaParser.creator_return creator495 =null;

        JavaParser.primitiveType_return primitiveType496 =null;


        Object string_literal484_tree=null;
        Object char_literal485_tree=null;
        Object IDENTIFIER486_tree=null;
        Object IDENTIFIER488_tree=null;
        Object char_literal489_tree=null;
        Object IDENTIFIER490_tree=null;
        Object string_literal492_tree=null;
        Object char_literal497_tree=null;
        Object char_literal498_tree=null;
        Object char_literal499_tree=null;
        Object string_literal500_tree=null;
        Object string_literal501_tree=null;
        Object char_literal502_tree=null;
        Object string_literal503_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 90) ) { return retval; }

            // Java.g:1204:5: ( parExpression | 'this' ( '.' IDENTIFIER )* ( identifierSuffix )? | IDENTIFIER ( '.' IDENTIFIER )* ( identifierSuffix )? | 'super' superSuffix | literal | creator | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' )
            int alt139=8;
            switch ( input.LA(1) ) {
            case LPAREN:
                {
                alt139=1;
                }
                break;
            case THIS:
                {
                alt139=2;
                }
                break;
            case IDENTIFIER:
                {
                alt139=3;
                }
                break;
            case SUPER:
                {
                alt139=4;
                }
                break;
            case CHARLITERAL:
            case DOUBLELITERAL:
            case FALSE:
            case FLOATLITERAL:
            case INTLITERAL:
            case LONGLITERAL:
            case NULL:
            case STRINGLITERAL:
            case TRUE:
                {
                alt139=5;
                }
                break;
            case NEW:
                {
                alt139=6;
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
                {
                alt139=7;
                }
                break;
            case VOID:
                {
                alt139=8;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 139, 0, input);

                throw nvae;

            }

            switch (alt139) {
                case 1 :
                    // Java.g:1204:9: parExpression
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_parExpression_in_primary7784);
                    parExpression483=parExpression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, parExpression483.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:1205:9: 'this' ( '.' IDENTIFIER )* ( identifierSuffix )?
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal484=(Token)match(input,THIS,FOLLOW_THIS_in_primary7806); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal484_tree = 
                    (Object)adaptor.create(string_literal484)
                    ;
                    adaptor.addChild(root_0, string_literal484_tree);
                    }

                    // Java.g:1206:9: ( '.' IDENTIFIER )*
                    loop134:
                    do {
                        int alt134=2;
                        int LA134_0 = input.LA(1);

                        if ( (LA134_0==DOT) ) {
                            int LA134_2 = input.LA(2);

                            if ( (LA134_2==IDENTIFIER) ) {
                                int LA134_3 = input.LA(3);

                                if ( (synpred209_Java()) ) {
                                    alt134=1;
                                }


                            }


                        }


                        switch (alt134) {
                    	case 1 :
                    	    // Java.g:1206:10: '.' IDENTIFIER
                    	    {
                    	    char_literal485=(Token)match(input,DOT,FOLLOW_DOT_in_primary7817); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal485_tree = 
                    	    (Object)adaptor.create(char_literal485)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal485_tree);
                    	    }

                    	    IDENTIFIER486=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_primary7819); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    IDENTIFIER486_tree = 
                    	    (Object)adaptor.create(IDENTIFIER486)
                    	    ;
                    	    adaptor.addChild(root_0, IDENTIFIER486_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop134;
                        }
                    } while (true);


                    // Java.g:1208:9: ( identifierSuffix )?
                    int alt135=2;
                    switch ( input.LA(1) ) {
                        case LBRACKET:
                            {
                            int LA135_1 = input.LA(2);

                            if ( (synpred210_Java()) ) {
                                alt135=1;
                            }
                            }
                            break;
                        case LPAREN:
                            {
                            alt135=1;
                            }
                            break;
                        case DOT:
                            {
                            int LA135_3 = input.LA(2);

                            if ( (synpred210_Java()) ) {
                                alt135=1;
                            }
                            }
                            break;
                    }

                    switch (alt135) {
                        case 1 :
                            // Java.g:1208:10: identifierSuffix
                            {
                            pushFollow(FOLLOW_identifierSuffix_in_primary7841);
                            identifierSuffix487=identifierSuffix();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, identifierSuffix487.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // Java.g:1210:9: IDENTIFIER ( '.' IDENTIFIER )* ( identifierSuffix )?
                    {
                    root_0 = (Object)adaptor.nil();


                    IDENTIFIER488=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_primary7862); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    IDENTIFIER488_tree = 
                    (Object)adaptor.create(IDENTIFIER488)
                    ;
                    adaptor.addChild(root_0, IDENTIFIER488_tree);
                    }

                    // Java.g:1211:9: ( '.' IDENTIFIER )*
                    loop136:
                    do {
                        int alt136=2;
                        int LA136_0 = input.LA(1);

                        if ( (LA136_0==DOT) ) {
                            int LA136_2 = input.LA(2);

                            if ( (LA136_2==IDENTIFIER) ) {
                                int LA136_3 = input.LA(3);

                                if ( (synpred212_Java()) ) {
                                    alt136=1;
                                }


                            }


                        }


                        switch (alt136) {
                    	case 1 :
                    	    // Java.g:1211:10: '.' IDENTIFIER
                    	    {
                    	    char_literal489=(Token)match(input,DOT,FOLLOW_DOT_in_primary7873); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal489_tree = 
                    	    (Object)adaptor.create(char_literal489)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal489_tree);
                    	    }

                    	    IDENTIFIER490=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_primary7875); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    IDENTIFIER490_tree = 
                    	    (Object)adaptor.create(IDENTIFIER490)
                    	    ;
                    	    adaptor.addChild(root_0, IDENTIFIER490_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop136;
                        }
                    } while (true);


                    // Java.g:1213:9: ( identifierSuffix )?
                    int alt137=2;
                    switch ( input.LA(1) ) {
                        case LBRACKET:
                            {
                            int LA137_1 = input.LA(2);

                            if ( (synpred213_Java()) ) {
                                alt137=1;
                            }
                            }
                            break;
                        case LPAREN:
                            {
                            alt137=1;
                            }
                            break;
                        case DOT:
                            {
                            int LA137_3 = input.LA(2);

                            if ( (synpred213_Java()) ) {
                                alt137=1;
                            }
                            }
                            break;
                    }

                    switch (alt137) {
                        case 1 :
                            // Java.g:1213:10: identifierSuffix
                            {
                            pushFollow(FOLLOW_identifierSuffix_in_primary7897);
                            identifierSuffix491=identifierSuffix();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, identifierSuffix491.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // Java.g:1215:9: 'super' superSuffix
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal492=(Token)match(input,SUPER,FOLLOW_SUPER_in_primary7918); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal492_tree = 
                    (Object)adaptor.create(string_literal492)
                    ;
                    adaptor.addChild(root_0, string_literal492_tree);
                    }

                    pushFollow(FOLLOW_superSuffix_in_primary7928);
                    superSuffix493=superSuffix();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, superSuffix493.getTree());

                    }
                    break;
                case 5 :
                    // Java.g:1217:9: literal
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_literal_in_primary7938);
                    literal494=literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, literal494.getTree());

                    }
                    break;
                case 6 :
                    // Java.g:1218:9: creator
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_creator_in_primary7948);
                    creator495=creator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, creator495.getTree());

                    }
                    break;
                case 7 :
                    // Java.g:1219:9: primitiveType ( '[' ']' )* '.' 'class'
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_primitiveType_in_primary7958);
                    primitiveType496=primitiveType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, primitiveType496.getTree());

                    // Java.g:1220:9: ( '[' ']' )*
                    loop138:
                    do {
                        int alt138=2;
                        int LA138_0 = input.LA(1);

                        if ( (LA138_0==LBRACKET) ) {
                            alt138=1;
                        }


                        switch (alt138) {
                    	case 1 :
                    	    // Java.g:1220:10: '[' ']'
                    	    {
                    	    char_literal497=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_primary7969); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal497_tree = 
                    	    (Object)adaptor.create(char_literal497)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal497_tree);
                    	    }

                    	    char_literal498=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_primary7971); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal498_tree = 
                    	    (Object)adaptor.create(char_literal498)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal498_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop138;
                        }
                    } while (true);


                    char_literal499=(Token)match(input,DOT,FOLLOW_DOT_in_primary7992); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal499_tree = 
                    (Object)adaptor.create(char_literal499)
                    ;
                    adaptor.addChild(root_0, char_literal499_tree);
                    }

                    string_literal500=(Token)match(input,CLASS,FOLLOW_CLASS_in_primary7994); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal500_tree = 
                    (Object)adaptor.create(string_literal500)
                    ;
                    adaptor.addChild(root_0, string_literal500_tree);
                    }

                    }
                    break;
                case 8 :
                    // Java.g:1223:9: 'void' '.' 'class'
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal501=(Token)match(input,VOID,FOLLOW_VOID_in_primary8004); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal501_tree = 
                    (Object)adaptor.create(string_literal501)
                    ;
                    adaptor.addChild(root_0, string_literal501_tree);
                    }

                    char_literal502=(Token)match(input,DOT,FOLLOW_DOT_in_primary8006); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal502_tree = 
                    (Object)adaptor.create(char_literal502)
                    ;
                    adaptor.addChild(root_0, char_literal502_tree);
                    }

                    string_literal503=(Token)match(input,CLASS,FOLLOW_CLASS_in_primary8008); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal503_tree = 
                    (Object)adaptor.create(string_literal503)
                    ;
                    adaptor.addChild(root_0, string_literal503_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 90, primary_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "primary"


    public static class superSuffix_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "superSuffix"
    // Java.g:1227:1: superSuffix : ( arguments | '.' ( typeArguments )? IDENTIFIER ( arguments )? );
    public final JavaParser.superSuffix_return superSuffix() throws RecognitionException {
        JavaParser.superSuffix_return retval = new JavaParser.superSuffix_return();
        retval.start = input.LT(1);

        int superSuffix_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal505=null;
        Token IDENTIFIER507=null;
        JavaParser.arguments_return arguments504 =null;

        JavaParser.typeArguments_return typeArguments506 =null;

        JavaParser.arguments_return arguments508 =null;


        Object char_literal505_tree=null;
        Object IDENTIFIER507_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 91) ) { return retval; }

            // Java.g:1228:5: ( arguments | '.' ( typeArguments )? IDENTIFIER ( arguments )? )
            int alt142=2;
            int LA142_0 = input.LA(1);

            if ( (LA142_0==LPAREN) ) {
                alt142=1;
            }
            else if ( (LA142_0==DOT) ) {
                alt142=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 142, 0, input);

                throw nvae;

            }
            switch (alt142) {
                case 1 :
                    // Java.g:1228:9: arguments
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_arguments_in_superSuffix8034);
                    arguments504=arguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arguments504.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:1229:9: '.' ( typeArguments )? IDENTIFIER ( arguments )?
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal505=(Token)match(input,DOT,FOLLOW_DOT_in_superSuffix8044); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal505_tree = 
                    (Object)adaptor.create(char_literal505)
                    ;
                    adaptor.addChild(root_0, char_literal505_tree);
                    }

                    // Java.g:1229:13: ( typeArguments )?
                    int alt140=2;
                    int LA140_0 = input.LA(1);

                    if ( (LA140_0==LT) ) {
                        alt140=1;
                    }
                    switch (alt140) {
                        case 1 :
                            // Java.g:1229:14: typeArguments
                            {
                            pushFollow(FOLLOW_typeArguments_in_superSuffix8047);
                            typeArguments506=typeArguments();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, typeArguments506.getTree());

                            }
                            break;

                    }


                    IDENTIFIER507=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_superSuffix8068); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    IDENTIFIER507_tree = 
                    (Object)adaptor.create(IDENTIFIER507)
                    ;
                    adaptor.addChild(root_0, IDENTIFIER507_tree);
                    }

                    // Java.g:1232:9: ( arguments )?
                    int alt141=2;
                    int LA141_0 = input.LA(1);

                    if ( (LA141_0==LPAREN) ) {
                        alt141=1;
                    }
                    switch (alt141) {
                        case 1 :
                            // Java.g:1232:10: arguments
                            {
                            pushFollow(FOLLOW_arguments_in_superSuffix8079);
                            arguments508=arguments();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, arguments508.getTree());

                            }
                            break;

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 91, superSuffix_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "superSuffix"


    public static class identifierSuffix_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "identifierSuffix"
    // Java.g:1237:1: identifierSuffix : ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' nonWildcardTypeArguments IDENTIFIER arguments | '.' 'this' | '.' 'super' arguments | innerCreator );
    public final JavaParser.identifierSuffix_return identifierSuffix() throws RecognitionException {
        JavaParser.identifierSuffix_return retval = new JavaParser.identifierSuffix_return();
        retval.start = input.LT(1);

        int identifierSuffix_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal509=null;
        Token char_literal510=null;
        Token char_literal511=null;
        Token string_literal512=null;
        Token char_literal513=null;
        Token char_literal515=null;
        Token char_literal517=null;
        Token string_literal518=null;
        Token char_literal519=null;
        Token IDENTIFIER521=null;
        Token char_literal523=null;
        Token string_literal524=null;
        Token char_literal525=null;
        Token string_literal526=null;
        JavaParser.expression_return expression514 =null;

        JavaParser.arguments_return arguments516 =null;

        JavaParser.nonWildcardTypeArguments_return nonWildcardTypeArguments520 =null;

        JavaParser.arguments_return arguments522 =null;

        JavaParser.arguments_return arguments527 =null;

        JavaParser.innerCreator_return innerCreator528 =null;


        Object char_literal509_tree=null;
        Object char_literal510_tree=null;
        Object char_literal511_tree=null;
        Object string_literal512_tree=null;
        Object char_literal513_tree=null;
        Object char_literal515_tree=null;
        Object char_literal517_tree=null;
        Object string_literal518_tree=null;
        Object char_literal519_tree=null;
        Object IDENTIFIER521_tree=null;
        Object char_literal523_tree=null;
        Object string_literal524_tree=null;
        Object char_literal525_tree=null;
        Object string_literal526_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 92) ) { return retval; }

            // Java.g:1238:5: ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' nonWildcardTypeArguments IDENTIFIER arguments | '.' 'this' | '.' 'super' arguments | innerCreator )
            int alt145=8;
            switch ( input.LA(1) ) {
            case LBRACKET:
                {
                int LA145_1 = input.LA(2);

                if ( (LA145_1==RBRACKET) ) {
                    alt145=1;
                }
                else if ( (LA145_1==BANG||LA145_1==BOOLEAN||LA145_1==BYTE||(LA145_1 >= CHAR && LA145_1 <= CHARLITERAL)||(LA145_1 >= DOUBLE && LA145_1 <= DOUBLELITERAL)||LA145_1==FALSE||(LA145_1 >= FLOAT && LA145_1 <= FLOATLITERAL)||LA145_1==IDENTIFIER||LA145_1==INT||LA145_1==INTLITERAL||(LA145_1 >= LONG && LA145_1 <= LPAREN)||(LA145_1 >= NEW && LA145_1 <= NULL)||LA145_1==PLUS||LA145_1==PLUSPLUS||LA145_1==SHORT||(LA145_1 >= STRINGLITERAL && LA145_1 <= SUB)||(LA145_1 >= SUBSUB && LA145_1 <= SUPER)||LA145_1==THIS||LA145_1==TILDE||LA145_1==TRUE||LA145_1==VOID) ) {
                    alt145=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 145, 1, input);

                    throw nvae;

                }
                }
                break;
            case LPAREN:
                {
                alt145=3;
                }
                break;
            case DOT:
                {
                switch ( input.LA(2) ) {
                case CLASS:
                    {
                    alt145=4;
                    }
                    break;
                case THIS:
                    {
                    alt145=6;
                    }
                    break;
                case SUPER:
                    {
                    alt145=7;
                    }
                    break;
                case NEW:
                    {
                    alt145=8;
                    }
                    break;
                case LT:
                    {
                    alt145=5;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 145, 3, input);

                    throw nvae;

                }

                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 145, 0, input);

                throw nvae;

            }

            switch (alt145) {
                case 1 :
                    // Java.g:1238:9: ( '[' ']' )+ '.' 'class'
                    {
                    root_0 = (Object)adaptor.nil();


                    // Java.g:1238:9: ( '[' ']' )+
                    int cnt143=0;
                    loop143:
                    do {
                        int alt143=2;
                        int LA143_0 = input.LA(1);

                        if ( (LA143_0==LBRACKET) ) {
                            alt143=1;
                        }


                        switch (alt143) {
                    	case 1 :
                    	    // Java.g:1238:10: '[' ']'
                    	    {
                    	    char_literal509=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_identifierSuffix8112); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal509_tree = 
                    	    (Object)adaptor.create(char_literal509)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal509_tree);
                    	    }

                    	    char_literal510=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_identifierSuffix8114); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal510_tree = 
                    	    (Object)adaptor.create(char_literal510)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal510_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt143 >= 1 ) break loop143;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(143, input);
                                throw eee;
                        }
                        cnt143++;
                    } while (true);


                    char_literal511=(Token)match(input,DOT,FOLLOW_DOT_in_identifierSuffix8135); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal511_tree = 
                    (Object)adaptor.create(char_literal511)
                    ;
                    adaptor.addChild(root_0, char_literal511_tree);
                    }

                    string_literal512=(Token)match(input,CLASS,FOLLOW_CLASS_in_identifierSuffix8137); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal512_tree = 
                    (Object)adaptor.create(string_literal512)
                    ;
                    adaptor.addChild(root_0, string_literal512_tree);
                    }

                    }
                    break;
                case 2 :
                    // Java.g:1241:9: ( '[' expression ']' )+
                    {
                    root_0 = (Object)adaptor.nil();


                    // Java.g:1241:9: ( '[' expression ']' )+
                    int cnt144=0;
                    loop144:
                    do {
                        int alt144=2;
                        int LA144_0 = input.LA(1);

                        if ( (LA144_0==LBRACKET) ) {
                            int LA144_2 = input.LA(2);

                            if ( (synpred225_Java()) ) {
                                alt144=1;
                            }


                        }


                        switch (alt144) {
                    	case 1 :
                    	    // Java.g:1241:10: '[' expression ']'
                    	    {
                    	    char_literal513=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_identifierSuffix8148); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal513_tree = 
                    	    (Object)adaptor.create(char_literal513)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal513_tree);
                    	    }

                    	    pushFollow(FOLLOW_expression_in_identifierSuffix8150);
                    	    expression514=expression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression514.getTree());

                    	    char_literal515=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_identifierSuffix8152); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal515_tree = 
                    	    (Object)adaptor.create(char_literal515)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal515_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt144 >= 1 ) break loop144;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(144, input);
                                throw eee;
                        }
                        cnt144++;
                    } while (true);


                    }
                    break;
                case 3 :
                    // Java.g:1243:9: arguments
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_arguments_in_identifierSuffix8173);
                    arguments516=arguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arguments516.getTree());

                    }
                    break;
                case 4 :
                    // Java.g:1244:9: '.' 'class'
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal517=(Token)match(input,DOT,FOLLOW_DOT_in_identifierSuffix8183); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal517_tree = 
                    (Object)adaptor.create(char_literal517)
                    ;
                    adaptor.addChild(root_0, char_literal517_tree);
                    }

                    string_literal518=(Token)match(input,CLASS,FOLLOW_CLASS_in_identifierSuffix8185); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal518_tree = 
                    (Object)adaptor.create(string_literal518)
                    ;
                    adaptor.addChild(root_0, string_literal518_tree);
                    }

                    }
                    break;
                case 5 :
                    // Java.g:1245:9: '.' nonWildcardTypeArguments IDENTIFIER arguments
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal519=(Token)match(input,DOT,FOLLOW_DOT_in_identifierSuffix8195); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal519_tree = 
                    (Object)adaptor.create(char_literal519)
                    ;
                    adaptor.addChild(root_0, char_literal519_tree);
                    }

                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_identifierSuffix8197);
                    nonWildcardTypeArguments520=nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, nonWildcardTypeArguments520.getTree());

                    IDENTIFIER521=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_identifierSuffix8199); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    IDENTIFIER521_tree = 
                    (Object)adaptor.create(IDENTIFIER521)
                    ;
                    adaptor.addChild(root_0, IDENTIFIER521_tree);
                    }

                    pushFollow(FOLLOW_arguments_in_identifierSuffix8201);
                    arguments522=arguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arguments522.getTree());

                    }
                    break;
                case 6 :
                    // Java.g:1246:9: '.' 'this'
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal523=(Token)match(input,DOT,FOLLOW_DOT_in_identifierSuffix8211); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal523_tree = 
                    (Object)adaptor.create(char_literal523)
                    ;
                    adaptor.addChild(root_0, char_literal523_tree);
                    }

                    string_literal524=(Token)match(input,THIS,FOLLOW_THIS_in_identifierSuffix8213); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal524_tree = 
                    (Object)adaptor.create(string_literal524)
                    ;
                    adaptor.addChild(root_0, string_literal524_tree);
                    }

                    }
                    break;
                case 7 :
                    // Java.g:1247:9: '.' 'super' arguments
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal525=(Token)match(input,DOT,FOLLOW_DOT_in_identifierSuffix8223); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal525_tree = 
                    (Object)adaptor.create(char_literal525)
                    ;
                    adaptor.addChild(root_0, char_literal525_tree);
                    }

                    string_literal526=(Token)match(input,SUPER,FOLLOW_SUPER_in_identifierSuffix8225); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal526_tree = 
                    (Object)adaptor.create(string_literal526)
                    ;
                    adaptor.addChild(root_0, string_literal526_tree);
                    }

                    pushFollow(FOLLOW_arguments_in_identifierSuffix8227);
                    arguments527=arguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arguments527.getTree());

                    }
                    break;
                case 8 :
                    // Java.g:1248:9: innerCreator
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_innerCreator_in_identifierSuffix8237);
                    innerCreator528=innerCreator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, innerCreator528.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 92, identifierSuffix_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "identifierSuffix"


    public static class selector_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "selector"
    // Java.g:1252:1: selector : ( '.' IDENTIFIER ( arguments )? | '.' 'this' | '.' 'super' superSuffix | innerCreator | '[' expression ']' );
    public final JavaParser.selector_return selector() throws RecognitionException {
        JavaParser.selector_return retval = new JavaParser.selector_return();
        retval.start = input.LT(1);

        int selector_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal529=null;
        Token IDENTIFIER530=null;
        Token char_literal532=null;
        Token string_literal533=null;
        Token char_literal534=null;
        Token string_literal535=null;
        Token char_literal538=null;
        Token char_literal540=null;
        JavaParser.arguments_return arguments531 =null;

        JavaParser.superSuffix_return superSuffix536 =null;

        JavaParser.innerCreator_return innerCreator537 =null;

        JavaParser.expression_return expression539 =null;


        Object char_literal529_tree=null;
        Object IDENTIFIER530_tree=null;
        Object char_literal532_tree=null;
        Object string_literal533_tree=null;
        Object char_literal534_tree=null;
        Object string_literal535_tree=null;
        Object char_literal538_tree=null;
        Object char_literal540_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 93) ) { return retval; }

            // Java.g:1253:5: ( '.' IDENTIFIER ( arguments )? | '.' 'this' | '.' 'super' superSuffix | innerCreator | '[' expression ']' )
            int alt147=5;
            int LA147_0 = input.LA(1);

            if ( (LA147_0==DOT) ) {
                switch ( input.LA(2) ) {
                case IDENTIFIER:
                    {
                    alt147=1;
                    }
                    break;
                case THIS:
                    {
                    alt147=2;
                    }
                    break;
                case SUPER:
                    {
                    alt147=3;
                    }
                    break;
                case NEW:
                    {
                    alt147=4;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 147, 1, input);

                    throw nvae;

                }

            }
            else if ( (LA147_0==LBRACKET) ) {
                alt147=5;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 147, 0, input);

                throw nvae;

            }
            switch (alt147) {
                case 1 :
                    // Java.g:1253:9: '.' IDENTIFIER ( arguments )?
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal529=(Token)match(input,DOT,FOLLOW_DOT_in_selector8259); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal529_tree = 
                    (Object)adaptor.create(char_literal529)
                    ;
                    adaptor.addChild(root_0, char_literal529_tree);
                    }

                    IDENTIFIER530=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_selector8261); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    IDENTIFIER530_tree = 
                    (Object)adaptor.create(IDENTIFIER530)
                    ;
                    adaptor.addChild(root_0, IDENTIFIER530_tree);
                    }

                    // Java.g:1254:9: ( arguments )?
                    int alt146=2;
                    int LA146_0 = input.LA(1);

                    if ( (LA146_0==LPAREN) ) {
                        alt146=1;
                    }
                    switch (alt146) {
                        case 1 :
                            // Java.g:1254:10: arguments
                            {
                            pushFollow(FOLLOW_arguments_in_selector8272);
                            arguments531=arguments();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) adaptor.addChild(root_0, arguments531.getTree());

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // Java.g:1256:9: '.' 'this'
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal532=(Token)match(input,DOT,FOLLOW_DOT_in_selector8293); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal532_tree = 
                    (Object)adaptor.create(char_literal532)
                    ;
                    adaptor.addChild(root_0, char_literal532_tree);
                    }

                    string_literal533=(Token)match(input,THIS,FOLLOW_THIS_in_selector8295); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal533_tree = 
                    (Object)adaptor.create(string_literal533)
                    ;
                    adaptor.addChild(root_0, string_literal533_tree);
                    }

                    }
                    break;
                case 3 :
                    // Java.g:1257:9: '.' 'super' superSuffix
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal534=(Token)match(input,DOT,FOLLOW_DOT_in_selector8305); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal534_tree = 
                    (Object)adaptor.create(char_literal534)
                    ;
                    adaptor.addChild(root_0, char_literal534_tree);
                    }

                    string_literal535=(Token)match(input,SUPER,FOLLOW_SUPER_in_selector8307); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal535_tree = 
                    (Object)adaptor.create(string_literal535)
                    ;
                    adaptor.addChild(root_0, string_literal535_tree);
                    }

                    pushFollow(FOLLOW_superSuffix_in_selector8317);
                    superSuffix536=superSuffix();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, superSuffix536.getTree());

                    }
                    break;
                case 4 :
                    // Java.g:1259:9: innerCreator
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_innerCreator_in_selector8327);
                    innerCreator537=innerCreator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, innerCreator537.getTree());

                    }
                    break;
                case 5 :
                    // Java.g:1260:9: '[' expression ']'
                    {
                    root_0 = (Object)adaptor.nil();


                    char_literal538=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_selector8337); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal538_tree = 
                    (Object)adaptor.create(char_literal538)
                    ;
                    adaptor.addChild(root_0, char_literal538_tree);
                    }

                    pushFollow(FOLLOW_expression_in_selector8339);
                    expression539=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression539.getTree());

                    char_literal540=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_selector8341); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal540_tree = 
                    (Object)adaptor.create(char_literal540)
                    ;
                    adaptor.addChild(root_0, char_literal540_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 93, selector_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "selector"


    public static class creator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "creator"
    // Java.g:1263:1: creator : ( 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest | 'new' classOrInterfaceType classCreatorRest | arrayCreator );
    public final JavaParser.creator_return creator() throws RecognitionException {
        JavaParser.creator_return retval = new JavaParser.creator_return();
        retval.start = input.LT(1);

        int creator_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal541=null;
        Token string_literal545=null;
        JavaParser.nonWildcardTypeArguments_return nonWildcardTypeArguments542 =null;

        JavaParser.classOrInterfaceType_return classOrInterfaceType543 =null;

        JavaParser.classCreatorRest_return classCreatorRest544 =null;

        JavaParser.classOrInterfaceType_return classOrInterfaceType546 =null;

        JavaParser.classCreatorRest_return classCreatorRest547 =null;

        JavaParser.arrayCreator_return arrayCreator548 =null;


        Object string_literal541_tree=null;
        Object string_literal545_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 94) ) { return retval; }

            // Java.g:1264:5: ( 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest | 'new' classOrInterfaceType classCreatorRest | arrayCreator )
            int alt148=3;
            int LA148_0 = input.LA(1);

            if ( (LA148_0==NEW) ) {
                int LA148_1 = input.LA(2);

                if ( (synpred237_Java()) ) {
                    alt148=1;
                }
                else if ( (synpred238_Java()) ) {
                    alt148=2;
                }
                else if ( (true) ) {
                    alt148=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 148, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 148, 0, input);

                throw nvae;

            }
            switch (alt148) {
                case 1 :
                    // Java.g:1264:9: 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal541=(Token)match(input,NEW,FOLLOW_NEW_in_creator8361); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal541_tree = 
                    (Object)adaptor.create(string_literal541)
                    ;
                    adaptor.addChild(root_0, string_literal541_tree);
                    }

                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_creator8363);
                    nonWildcardTypeArguments542=nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, nonWildcardTypeArguments542.getTree());

                    pushFollow(FOLLOW_classOrInterfaceType_in_creator8365);
                    classOrInterfaceType543=classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classOrInterfaceType543.getTree());

                    pushFollow(FOLLOW_classCreatorRest_in_creator8367);
                    classCreatorRest544=classCreatorRest();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classCreatorRest544.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:1265:9: 'new' classOrInterfaceType classCreatorRest
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal545=(Token)match(input,NEW,FOLLOW_NEW_in_creator8377); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal545_tree = 
                    (Object)adaptor.create(string_literal545)
                    ;
                    adaptor.addChild(root_0, string_literal545_tree);
                    }

                    pushFollow(FOLLOW_classOrInterfaceType_in_creator8379);
                    classOrInterfaceType546=classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classOrInterfaceType546.getTree());

                    pushFollow(FOLLOW_classCreatorRest_in_creator8381);
                    classCreatorRest547=classCreatorRest();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classCreatorRest547.getTree());

                    }
                    break;
                case 3 :
                    // Java.g:1266:9: arrayCreator
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_arrayCreator_in_creator8391);
                    arrayCreator548=arrayCreator();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arrayCreator548.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 94, creator_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "creator"


    public static class arrayCreator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "arrayCreator"
    // Java.g:1269:1: arrayCreator : ( 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer | 'new' createdName '[' expression ']' ( '[' expression ']' )* ( '[' ']' )* );
    public final JavaParser.arrayCreator_return arrayCreator() throws RecognitionException {
        JavaParser.arrayCreator_return retval = new JavaParser.arrayCreator_return();
        retval.start = input.LT(1);

        int arrayCreator_StartIndex = input.index();

        Object root_0 = null;

        Token string_literal549=null;
        Token char_literal551=null;
        Token char_literal552=null;
        Token char_literal553=null;
        Token char_literal554=null;
        Token string_literal556=null;
        Token char_literal558=null;
        Token char_literal560=null;
        Token char_literal561=null;
        Token char_literal563=null;
        Token char_literal564=null;
        Token char_literal565=null;
        JavaParser.createdName_return createdName550 =null;

        JavaParser.arrayInitializer_return arrayInitializer555 =null;

        JavaParser.createdName_return createdName557 =null;

        JavaParser.expression_return expression559 =null;

        JavaParser.expression_return expression562 =null;


        Object string_literal549_tree=null;
        Object char_literal551_tree=null;
        Object char_literal552_tree=null;
        Object char_literal553_tree=null;
        Object char_literal554_tree=null;
        Object string_literal556_tree=null;
        Object char_literal558_tree=null;
        Object char_literal560_tree=null;
        Object char_literal561_tree=null;
        Object char_literal563_tree=null;
        Object char_literal564_tree=null;
        Object char_literal565_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 95) ) { return retval; }

            // Java.g:1270:5: ( 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer | 'new' createdName '[' expression ']' ( '[' expression ']' )* ( '[' ']' )* )
            int alt152=2;
            int LA152_0 = input.LA(1);

            if ( (LA152_0==NEW) ) {
                int LA152_1 = input.LA(2);

                if ( (synpred240_Java()) ) {
                    alt152=1;
                }
                else if ( (true) ) {
                    alt152=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 152, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 152, 0, input);

                throw nvae;

            }
            switch (alt152) {
                case 1 :
                    // Java.g:1270:9: 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal549=(Token)match(input,NEW,FOLLOW_NEW_in_arrayCreator8411); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal549_tree = 
                    (Object)adaptor.create(string_literal549)
                    ;
                    adaptor.addChild(root_0, string_literal549_tree);
                    }

                    pushFollow(FOLLOW_createdName_in_arrayCreator8413);
                    createdName550=createdName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, createdName550.getTree());

                    char_literal551=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator8423); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal551_tree = 
                    (Object)adaptor.create(char_literal551)
                    ;
                    adaptor.addChild(root_0, char_literal551_tree);
                    }

                    char_literal552=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator8425); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal552_tree = 
                    (Object)adaptor.create(char_literal552)
                    ;
                    adaptor.addChild(root_0, char_literal552_tree);
                    }

                    // Java.g:1272:9: ( '[' ']' )*
                    loop149:
                    do {
                        int alt149=2;
                        int LA149_0 = input.LA(1);

                        if ( (LA149_0==LBRACKET) ) {
                            alt149=1;
                        }


                        switch (alt149) {
                    	case 1 :
                    	    // Java.g:1272:10: '[' ']'
                    	    {
                    	    char_literal553=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator8436); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal553_tree = 
                    	    (Object)adaptor.create(char_literal553)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal553_tree);
                    	    }

                    	    char_literal554=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator8438); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal554_tree = 
                    	    (Object)adaptor.create(char_literal554)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal554_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop149;
                        }
                    } while (true);


                    pushFollow(FOLLOW_arrayInitializer_in_arrayCreator8459);
                    arrayInitializer555=arrayInitializer();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arrayInitializer555.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:1276:9: 'new' createdName '[' expression ']' ( '[' expression ']' )* ( '[' ']' )*
                    {
                    root_0 = (Object)adaptor.nil();


                    string_literal556=(Token)match(input,NEW,FOLLOW_NEW_in_arrayCreator8470); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal556_tree = 
                    (Object)adaptor.create(string_literal556)
                    ;
                    adaptor.addChild(root_0, string_literal556_tree);
                    }

                    pushFollow(FOLLOW_createdName_in_arrayCreator8472);
                    createdName557=createdName();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, createdName557.getTree());

                    char_literal558=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator8482); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal558_tree = 
                    (Object)adaptor.create(char_literal558)
                    ;
                    adaptor.addChild(root_0, char_literal558_tree);
                    }

                    pushFollow(FOLLOW_expression_in_arrayCreator8484);
                    expression559=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression559.getTree());

                    char_literal560=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator8494); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal560_tree = 
                    (Object)adaptor.create(char_literal560)
                    ;
                    adaptor.addChild(root_0, char_literal560_tree);
                    }

                    // Java.g:1279:9: ( '[' expression ']' )*
                    loop150:
                    do {
                        int alt150=2;
                        int LA150_0 = input.LA(1);

                        if ( (LA150_0==LBRACKET) ) {
                            int LA150_1 = input.LA(2);

                            if ( (synpred241_Java()) ) {
                                alt150=1;
                            }


                        }


                        switch (alt150) {
                    	case 1 :
                    	    // Java.g:1279:13: '[' expression ']'
                    	    {
                    	    char_literal561=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator8508); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal561_tree = 
                    	    (Object)adaptor.create(char_literal561)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal561_tree);
                    	    }

                    	    pushFollow(FOLLOW_expression_in_arrayCreator8510);
                    	    expression562=expression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression562.getTree());

                    	    char_literal563=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator8524); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal563_tree = 
                    	    (Object)adaptor.create(char_literal563)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal563_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop150;
                        }
                    } while (true);


                    // Java.g:1282:9: ( '[' ']' )*
                    loop151:
                    do {
                        int alt151=2;
                        int LA151_0 = input.LA(1);

                        if ( (LA151_0==LBRACKET) ) {
                            int LA151_2 = input.LA(2);

                            if ( (LA151_2==RBRACKET) ) {
                                alt151=1;
                            }


                        }


                        switch (alt151) {
                    	case 1 :
                    	    // Java.g:1282:10: '[' ']'
                    	    {
                    	    char_literal564=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator8546); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal564_tree = 
                    	    (Object)adaptor.create(char_literal564)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal564_tree);
                    	    }

                    	    char_literal565=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator8548); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal565_tree = 
                    	    (Object)adaptor.create(char_literal565)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal565_tree);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop151;
                        }
                    } while (true);


                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 95, arrayCreator_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "arrayCreator"


    public static class variableInitializer_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "variableInitializer"
    // Java.g:1286:1: variableInitializer : ( arrayInitializer | expression );
    public final JavaParser.variableInitializer_return variableInitializer() throws RecognitionException {
        JavaParser.variableInitializer_return retval = new JavaParser.variableInitializer_return();
        retval.start = input.LT(1);

        int variableInitializer_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.arrayInitializer_return arrayInitializer566 =null;

        JavaParser.expression_return expression567 =null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 96) ) { return retval; }

            // Java.g:1287:5: ( arrayInitializer | expression )
            int alt153=2;
            int LA153_0 = input.LA(1);

            if ( (LA153_0==LBRACE) ) {
                alt153=1;
            }
            else if ( (LA153_0==BANG||LA153_0==BOOLEAN||LA153_0==BYTE||(LA153_0 >= CHAR && LA153_0 <= CHARLITERAL)||(LA153_0 >= DOUBLE && LA153_0 <= DOUBLELITERAL)||LA153_0==FALSE||(LA153_0 >= FLOAT && LA153_0 <= FLOATLITERAL)||LA153_0==IDENTIFIER||LA153_0==INT||LA153_0==INTLITERAL||(LA153_0 >= LONG && LA153_0 <= LPAREN)||(LA153_0 >= NEW && LA153_0 <= NULL)||LA153_0==PLUS||LA153_0==PLUSPLUS||LA153_0==SHORT||(LA153_0 >= STRINGLITERAL && LA153_0 <= SUB)||(LA153_0 >= SUBSUB && LA153_0 <= SUPER)||LA153_0==THIS||LA153_0==TILDE||LA153_0==TRUE||LA153_0==VOID) ) {
                alt153=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 153, 0, input);

                throw nvae;

            }
            switch (alt153) {
                case 1 :
                    // Java.g:1287:9: arrayInitializer
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_arrayInitializer_in_variableInitializer8579);
                    arrayInitializer566=arrayInitializer();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, arrayInitializer566.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:1288:9: expression
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_expression_in_variableInitializer8589);
                    expression567=expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression567.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 96, variableInitializer_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "variableInitializer"


    public static class arrayInitializer_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "arrayInitializer"
    // Java.g:1291:1: arrayInitializer : '{' ( variableInitializer ( ',' variableInitializer )* )? ( ',' )? '}' ;
    public final JavaParser.arrayInitializer_return arrayInitializer() throws RecognitionException {
        JavaParser.arrayInitializer_return retval = new JavaParser.arrayInitializer_return();
        retval.start = input.LT(1);

        int arrayInitializer_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal568=null;
        Token char_literal570=null;
        Token char_literal572=null;
        Token char_literal573=null;
        JavaParser.variableInitializer_return variableInitializer569 =null;

        JavaParser.variableInitializer_return variableInitializer571 =null;


        Object char_literal568_tree=null;
        Object char_literal570_tree=null;
        Object char_literal572_tree=null;
        Object char_literal573_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 97) ) { return retval; }

            // Java.g:1292:5: ( '{' ( variableInitializer ( ',' variableInitializer )* )? ( ',' )? '}' )
            // Java.g:1292:9: '{' ( variableInitializer ( ',' variableInitializer )* )? ( ',' )? '}'
            {
            root_0 = (Object)adaptor.nil();


            char_literal568=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_arrayInitializer8609); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal568_tree = 
            (Object)adaptor.create(char_literal568)
            ;
            adaptor.addChild(root_0, char_literal568_tree);
            }

            // Java.g:1293:13: ( variableInitializer ( ',' variableInitializer )* )?
            int alt155=2;
            int LA155_0 = input.LA(1);

            if ( (LA155_0==BANG||LA155_0==BOOLEAN||LA155_0==BYTE||(LA155_0 >= CHAR && LA155_0 <= CHARLITERAL)||(LA155_0 >= DOUBLE && LA155_0 <= DOUBLELITERAL)||LA155_0==FALSE||(LA155_0 >= FLOAT && LA155_0 <= FLOATLITERAL)||LA155_0==IDENTIFIER||LA155_0==INT||LA155_0==INTLITERAL||LA155_0==LBRACE||(LA155_0 >= LONG && LA155_0 <= LPAREN)||(LA155_0 >= NEW && LA155_0 <= NULL)||LA155_0==PLUS||LA155_0==PLUSPLUS||LA155_0==SHORT||(LA155_0 >= STRINGLITERAL && LA155_0 <= SUB)||(LA155_0 >= SUBSUB && LA155_0 <= SUPER)||LA155_0==THIS||LA155_0==TILDE||LA155_0==TRUE||LA155_0==VOID) ) {
                alt155=1;
            }
            switch (alt155) {
                case 1 :
                    // Java.g:1293:14: variableInitializer ( ',' variableInitializer )*
                    {
                    pushFollow(FOLLOW_variableInitializer_in_arrayInitializer8625);
                    variableInitializer569=variableInitializer();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, variableInitializer569.getTree());

                    // Java.g:1294:17: ( ',' variableInitializer )*
                    loop154:
                    do {
                        int alt154=2;
                        int LA154_0 = input.LA(1);

                        if ( (LA154_0==COMMA) ) {
                            int LA154_1 = input.LA(2);

                            if ( (LA154_1==BANG||LA154_1==BOOLEAN||LA154_1==BYTE||(LA154_1 >= CHAR && LA154_1 <= CHARLITERAL)||(LA154_1 >= DOUBLE && LA154_1 <= DOUBLELITERAL)||LA154_1==FALSE||(LA154_1 >= FLOAT && LA154_1 <= FLOATLITERAL)||LA154_1==IDENTIFIER||LA154_1==INT||LA154_1==INTLITERAL||LA154_1==LBRACE||(LA154_1 >= LONG && LA154_1 <= LPAREN)||(LA154_1 >= NEW && LA154_1 <= NULL)||LA154_1==PLUS||LA154_1==PLUSPLUS||LA154_1==SHORT||(LA154_1 >= STRINGLITERAL && LA154_1 <= SUB)||(LA154_1 >= SUBSUB && LA154_1 <= SUPER)||LA154_1==THIS||LA154_1==TILDE||LA154_1==TRUE||LA154_1==VOID) ) {
                                alt154=1;
                            }


                        }


                        switch (alt154) {
                    	case 1 :
                    	    // Java.g:1294:18: ',' variableInitializer
                    	    {
                    	    char_literal570=(Token)match(input,COMMA,FOLLOW_COMMA_in_arrayInitializer8644); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    char_literal570_tree = 
                    	    (Object)adaptor.create(char_literal570)
                    	    ;
                    	    adaptor.addChild(root_0, char_literal570_tree);
                    	    }

                    	    pushFollow(FOLLOW_variableInitializer_in_arrayInitializer8646);
                    	    variableInitializer571=variableInitializer();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, variableInitializer571.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop154;
                        }
                    } while (true);


                    }
                    break;

            }


            // Java.g:1297:13: ( ',' )?
            int alt156=2;
            int LA156_0 = input.LA(1);

            if ( (LA156_0==COMMA) ) {
                alt156=1;
            }
            switch (alt156) {
                case 1 :
                    // Java.g:1297:14: ','
                    {
                    char_literal572=(Token)match(input,COMMA,FOLLOW_COMMA_in_arrayInitializer8696); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal572_tree = 
                    (Object)adaptor.create(char_literal572)
                    ;
                    adaptor.addChild(root_0, char_literal572_tree);
                    }

                    }
                    break;

            }


            char_literal573=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_arrayInitializer8709); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal573_tree = 
            (Object)adaptor.create(char_literal573)
            ;
            adaptor.addChild(root_0, char_literal573_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 97, arrayInitializer_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "arrayInitializer"


    public static class createdName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "createdName"
    // Java.g:1302:1: createdName : ( classOrInterfaceType | primitiveType );
    public final JavaParser.createdName_return createdName() throws RecognitionException {
        JavaParser.createdName_return retval = new JavaParser.createdName_return();
        retval.start = input.LT(1);

        int createdName_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.classOrInterfaceType_return classOrInterfaceType574 =null;

        JavaParser.primitiveType_return primitiveType575 =null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 98) ) { return retval; }

            // Java.g:1303:5: ( classOrInterfaceType | primitiveType )
            int alt157=2;
            int LA157_0 = input.LA(1);

            if ( (LA157_0==IDENTIFIER) ) {
                alt157=1;
            }
            else if ( (LA157_0==BOOLEAN||LA157_0==BYTE||LA157_0==CHAR||LA157_0==DOUBLE||LA157_0==FLOAT||LA157_0==INT||LA157_0==LONG||LA157_0==SHORT) ) {
                alt157=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 157, 0, input);

                throw nvae;

            }
            switch (alt157) {
                case 1 :
                    // Java.g:1303:9: classOrInterfaceType
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_classOrInterfaceType_in_createdName8743);
                    classOrInterfaceType574=classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classOrInterfaceType574.getTree());

                    }
                    break;
                case 2 :
                    // Java.g:1304:9: primitiveType
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_primitiveType_in_createdName8753);
                    primitiveType575=primitiveType();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, primitiveType575.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 98, createdName_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "createdName"


    public static class innerCreator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "innerCreator"
    // Java.g:1307:1: innerCreator : '.' 'new' ( nonWildcardTypeArguments )? IDENTIFIER ( typeArguments )? classCreatorRest ;
    public final JavaParser.innerCreator_return innerCreator() throws RecognitionException {
        JavaParser.innerCreator_return retval = new JavaParser.innerCreator_return();
        retval.start = input.LT(1);

        int innerCreator_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal576=null;
        Token string_literal577=null;
        Token IDENTIFIER579=null;
        JavaParser.nonWildcardTypeArguments_return nonWildcardTypeArguments578 =null;

        JavaParser.typeArguments_return typeArguments580 =null;

        JavaParser.classCreatorRest_return classCreatorRest581 =null;


        Object char_literal576_tree=null;
        Object string_literal577_tree=null;
        Object IDENTIFIER579_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 99) ) { return retval; }

            // Java.g:1308:5: ( '.' 'new' ( nonWildcardTypeArguments )? IDENTIFIER ( typeArguments )? classCreatorRest )
            // Java.g:1308:9: '.' 'new' ( nonWildcardTypeArguments )? IDENTIFIER ( typeArguments )? classCreatorRest
            {
            root_0 = (Object)adaptor.nil();


            char_literal576=(Token)match(input,DOT,FOLLOW_DOT_in_innerCreator8774); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal576_tree = 
            (Object)adaptor.create(char_literal576)
            ;
            adaptor.addChild(root_0, char_literal576_tree);
            }

            string_literal577=(Token)match(input,NEW,FOLLOW_NEW_in_innerCreator8776); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal577_tree = 
            (Object)adaptor.create(string_literal577)
            ;
            adaptor.addChild(root_0, string_literal577_tree);
            }

            // Java.g:1309:9: ( nonWildcardTypeArguments )?
            int alt158=2;
            int LA158_0 = input.LA(1);

            if ( (LA158_0==LT) ) {
                alt158=1;
            }
            switch (alt158) {
                case 1 :
                    // Java.g:1309:10: nonWildcardTypeArguments
                    {
                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_innerCreator8787);
                    nonWildcardTypeArguments578=nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, nonWildcardTypeArguments578.getTree());

                    }
                    break;

            }


            IDENTIFIER579=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_innerCreator8808); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IDENTIFIER579_tree = 
            (Object)adaptor.create(IDENTIFIER579)
            ;
            adaptor.addChild(root_0, IDENTIFIER579_tree);
            }

            // Java.g:1312:9: ( typeArguments )?
            int alt159=2;
            int LA159_0 = input.LA(1);

            if ( (LA159_0==LT) ) {
                alt159=1;
            }
            switch (alt159) {
                case 1 :
                    // Java.g:1312:10: typeArguments
                    {
                    pushFollow(FOLLOW_typeArguments_in_innerCreator8819);
                    typeArguments580=typeArguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, typeArguments580.getTree());

                    }
                    break;

            }


            pushFollow(FOLLOW_classCreatorRest_in_innerCreator8840);
            classCreatorRest581=classCreatorRest();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, classCreatorRest581.getTree());

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 99, innerCreator_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "innerCreator"


    public static class classCreatorRest_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "classCreatorRest"
    // Java.g:1318:1: classCreatorRest : arguments ( classBody )? ;
    public final JavaParser.classCreatorRest_return classCreatorRest() throws RecognitionException {
        JavaParser.classCreatorRest_return retval = new JavaParser.classCreatorRest_return();
        retval.start = input.LT(1);

        int classCreatorRest_StartIndex = input.index();

        Object root_0 = null;

        JavaParser.arguments_return arguments582 =null;

        JavaParser.classBody_return classBody583 =null;



        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 100) ) { return retval; }

            // Java.g:1319:5: ( arguments ( classBody )? )
            // Java.g:1319:9: arguments ( classBody )?
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_arguments_in_classCreatorRest8861);
            arguments582=arguments();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, arguments582.getTree());

            // Java.g:1320:9: ( classBody )?
            int alt160=2;
            int LA160_0 = input.LA(1);

            if ( (LA160_0==LBRACE) ) {
                alt160=1;
            }
            switch (alt160) {
                case 1 :
                    // Java.g:1320:10: classBody
                    {
                    pushFollow(FOLLOW_classBody_in_classCreatorRest8872);
                    classBody583=classBody();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, classBody583.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 100, classCreatorRest_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "classCreatorRest"


    public static class nonWildcardTypeArguments_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "nonWildcardTypeArguments"
    // Java.g:1325:1: nonWildcardTypeArguments : '<' typeList '>' ;
    public final JavaParser.nonWildcardTypeArguments_return nonWildcardTypeArguments() throws RecognitionException {
        JavaParser.nonWildcardTypeArguments_return retval = new JavaParser.nonWildcardTypeArguments_return();
        retval.start = input.LT(1);

        int nonWildcardTypeArguments_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal584=null;
        Token char_literal586=null;
        JavaParser.typeList_return typeList585 =null;


        Object char_literal584_tree=null;
        Object char_literal586_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 101) ) { return retval; }

            // Java.g:1326:5: ( '<' typeList '>' )
            // Java.g:1326:9: '<' typeList '>'
            {
            root_0 = (Object)adaptor.nil();


            char_literal584=(Token)match(input,LT,FOLLOW_LT_in_nonWildcardTypeArguments8904); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal584_tree = 
            (Object)adaptor.create(char_literal584)
            ;
            adaptor.addChild(root_0, char_literal584_tree);
            }

            pushFollow(FOLLOW_typeList_in_nonWildcardTypeArguments8906);
            typeList585=typeList();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, typeList585.getTree());

            char_literal586=(Token)match(input,GT,FOLLOW_GT_in_nonWildcardTypeArguments8916); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal586_tree = 
            (Object)adaptor.create(char_literal586)
            ;
            adaptor.addChild(root_0, char_literal586_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 101, nonWildcardTypeArguments_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "nonWildcardTypeArguments"


    public static class arguments_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "arguments"
    // Java.g:1330:1: arguments : '(' ( expressionList )? ')' ;
    public final JavaParser.arguments_return arguments() throws RecognitionException {
        JavaParser.arguments_return retval = new JavaParser.arguments_return();
        retval.start = input.LT(1);

        int arguments_StartIndex = input.index();

        Object root_0 = null;

        Token char_literal587=null;
        Token char_literal589=null;
        JavaParser.expressionList_return expressionList588 =null;


        Object char_literal587_tree=null;
        Object char_literal589_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 102) ) { return retval; }

            // Java.g:1331:5: ( '(' ( expressionList )? ')' )
            // Java.g:1331:9: '(' ( expressionList )? ')'
            {
            root_0 = (Object)adaptor.nil();


            char_literal587=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arguments8936); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal587_tree = 
            (Object)adaptor.create(char_literal587)
            ;
            adaptor.addChild(root_0, char_literal587_tree);
            }

            // Java.g:1331:13: ( expressionList )?
            int alt161=2;
            int LA161_0 = input.LA(1);

            if ( (LA161_0==BANG||LA161_0==BOOLEAN||LA161_0==BYTE||(LA161_0 >= CHAR && LA161_0 <= CHARLITERAL)||(LA161_0 >= DOUBLE && LA161_0 <= DOUBLELITERAL)||LA161_0==FALSE||(LA161_0 >= FLOAT && LA161_0 <= FLOATLITERAL)||LA161_0==IDENTIFIER||LA161_0==INT||LA161_0==INTLITERAL||(LA161_0 >= LONG && LA161_0 <= LPAREN)||(LA161_0 >= NEW && LA161_0 <= NULL)||LA161_0==PLUS||LA161_0==PLUSPLUS||LA161_0==SHORT||(LA161_0 >= STRINGLITERAL && LA161_0 <= SUB)||(LA161_0 >= SUBSUB && LA161_0 <= SUPER)||LA161_0==THIS||LA161_0==TILDE||LA161_0==TRUE||LA161_0==VOID) ) {
                alt161=1;
            }
            switch (alt161) {
                case 1 :
                    // Java.g:1331:14: expressionList
                    {
                    pushFollow(FOLLOW_expressionList_in_arguments8939);
                    expressionList588=expressionList();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, expressionList588.getTree());

                    }
                    break;

            }


            char_literal589=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arguments8952); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            char_literal589_tree = 
            (Object)adaptor.create(char_literal589)
            ;
            adaptor.addChild(root_0, char_literal589_tree);
            }

            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 102, arguments_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "arguments"


    public static class literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "literal"
    // Java.g:1335:1: literal : ( INTLITERAL | LONGLITERAL | FLOATLITERAL | DOUBLELITERAL | CHARLITERAL | STRINGLITERAL | TRUE | FALSE | NULL );
    public final JavaParser.literal_return literal() throws RecognitionException {
        JavaParser.literal_return retval = new JavaParser.literal_return();
        retval.start = input.LT(1);

        int literal_StartIndex = input.index();

        Object root_0 = null;

        Token set590=null;

        Object set590_tree=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 103) ) { return retval; }

            // Java.g:1336:5: ( INTLITERAL | LONGLITERAL | FLOATLITERAL | DOUBLELITERAL | CHARLITERAL | STRINGLITERAL | TRUE | FALSE | NULL )
            // Java.g:
            {
            root_0 = (Object)adaptor.nil();


            set590=(Token)input.LT(1);

            if ( input.LA(1)==CHARLITERAL||input.LA(1)==DOUBLELITERAL||input.LA(1)==FALSE||input.LA(1)==FLOATLITERAL||input.LA(1)==INTLITERAL||input.LA(1)==LONGLITERAL||input.LA(1)==NULL||input.LA(1)==STRINGLITERAL||input.LA(1)==TRUE ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, 
                (Object)adaptor.create(set590)
                );
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 103, literal_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "literal"

    // $ANTLR start synpred2_Java
    public final void synpred2_Java_fragment() throws RecognitionException {
        // Java.g:322:13: ( ( annotations )? packageDeclaration )
        // Java.g:322:13: ( annotations )? packageDeclaration
        {
        // Java.g:322:13: ( annotations )?
        int alt162=2;
        int LA162_0 = input.LA(1);

        if ( (LA162_0==136) ) {
            alt162=1;
        }
        switch (alt162) {
            case 1 :
                // Java.g:322:14: annotations
                {
                pushFollow(FOLLOW_annotations_in_synpred2_Java109);
                annotations();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        pushFollow(FOLLOW_packageDeclaration_in_synpred2_Java138);
        packageDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred2_Java

    // $ANTLR start synpred3_Java
    public final void synpred3_Java_fragment() throws RecognitionException {
        // Java.g:326:10: ( importBlock )
        // Java.g:326:10: importBlock
        {
        pushFollow(FOLLOW_importBlock_in_synpred3_Java160);
        importBlock();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred3_Java

    // $ANTLR start synpred13_Java
    public final void synpred13_Java_fragment() throws RecognitionException {
        // Java.g:376:10: ( classDeclaration )
        // Java.g:376:10: classDeclaration
        {
        pushFollow(FOLLOW_classDeclaration_in_synpred13_Java598);
        classDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred13_Java

    // $ANTLR start synpred28_Java
    public final void synpred28_Java_fragment() throws RecognitionException {
        // Java.g:407:9: ( normalClassDeclaration )
        // Java.g:407:9: normalClassDeclaration
        {
        pushFollow(FOLLOW_normalClassDeclaration_in_synpred28_Java835);
        normalClassDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred28_Java

    // $ANTLR start synpred44_Java
    public final void synpred44_Java_fragment() throws RecognitionException {
        // Java.g:516:9: ( normalInterfaceDeclaration )
        // Java.g:516:9: normalInterfaceDeclaration
        {
        pushFollow(FOLLOW_normalInterfaceDeclaration_in_synpred44_Java1776);
        normalInterfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred44_Java

    // $ANTLR start synpred53_Java
    public final void synpred53_Java_fragment() throws RecognitionException {
        // Java.g:566:10: ( fieldDeclaration )
        // Java.g:566:10: fieldDeclaration
        {
        pushFollow(FOLLOW_fieldDeclaration_in_synpred53_Java2194);
        fieldDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred53_Java

    // $ANTLR start synpred54_Java
    public final void synpred54_Java_fragment() throws RecognitionException {
        // Java.g:567:10: ( methodDeclaration )
        // Java.g:567:10: methodDeclaration
        {
        pushFollow(FOLLOW_methodDeclaration_in_synpred54_Java2213);
        methodDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred54_Java

    // $ANTLR start synpred55_Java
    public final void synpred55_Java_fragment() throws RecognitionException {
        // Java.g:568:10: ( classDeclaration )
        // Java.g:568:10: classDeclaration
        {
        pushFollow(FOLLOW_classDeclaration_in_synpred55_Java2232);
        classDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred55_Java

    // $ANTLR start synpred58_Java
    public final void synpred58_Java_fragment() throws RecognitionException {
        // Java.g:584:10: ( explicitConstructorInvocation )
        // Java.g:584:10: explicitConstructorInvocation
        {
        pushFollow(FOLLOW_explicitConstructorInvocation_in_synpred58_Java2369);
        explicitConstructorInvocation();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred58_Java

    // $ANTLR start synpred60_Java
    public final void synpred60_Java_fragment() throws RecognitionException {
        // Java.g:576:10: ( modifiers ( typeParameters )? IDENTIFIER formalParameters ( 'throws' qualifiedNameList )? '{' ( explicitConstructorInvocation )? ( blockStatement )* '}' )
        // Java.g:576:10: modifiers ( typeParameters )? IDENTIFIER formalParameters ( 'throws' qualifiedNameList )? '{' ( explicitConstructorInvocation )? ( blockStatement )* '}'
        {
        pushFollow(FOLLOW_modifiers_in_synpred60_Java2281);
        modifiers();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:577:9: ( typeParameters )?
        int alt165=2;
        int LA165_0 = input.LA(1);

        if ( (LA165_0==LT) ) {
            alt165=1;
        }
        switch (alt165) {
            case 1 :
                // Java.g:577:10: typeParameters
                {
                pushFollow(FOLLOW_typeParameters_in_synpred60_Java2292);
                typeParameters();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred60_Java2313); if (state.failed) return ;

        pushFollow(FOLLOW_formalParameters_in_synpred60_Java2323);
        formalParameters();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:581:9: ( 'throws' qualifiedNameList )?
        int alt166=2;
        int LA166_0 = input.LA(1);

        if ( (LA166_0==THROWS) ) {
            alt166=1;
        }
        switch (alt166) {
            case 1 :
                // Java.g:581:10: 'throws' qualifiedNameList
                {
                match(input,THROWS,FOLLOW_THROWS_in_synpred60_Java2334); if (state.failed) return ;

                pushFollow(FOLLOW_qualifiedNameList_in_synpred60_Java2336);
                qualifiedNameList();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        match(input,LBRACE,FOLLOW_LBRACE_in_synpred60_Java2357); if (state.failed) return ;

        // Java.g:584:9: ( explicitConstructorInvocation )?
        int alt167=2;
        switch ( input.LA(1) ) {
            case LT:
                {
                alt167=1;
                }
                break;
            case THIS:
                {
                int LA167_2 = input.LA(2);

                if ( (synpred58_Java()) ) {
                    alt167=1;
                }
                }
                break;
            case LPAREN:
                {
                int LA167_3 = input.LA(2);

                if ( (synpred58_Java()) ) {
                    alt167=1;
                }
                }
                break;
            case SUPER:
                {
                int LA167_4 = input.LA(2);

                if ( (synpred58_Java()) ) {
                    alt167=1;
                }
                }
                break;
            case IDENTIFIER:
                {
                int LA167_5 = input.LA(2);

                if ( (synpred58_Java()) ) {
                    alt167=1;
                }
                }
                break;
            case CHARLITERAL:
            case DOUBLELITERAL:
            case FALSE:
            case FLOATLITERAL:
            case INTLITERAL:
            case LONGLITERAL:
            case NULL:
            case STRINGLITERAL:
            case TRUE:
                {
                int LA167_6 = input.LA(2);

                if ( (synpred58_Java()) ) {
                    alt167=1;
                }
                }
                break;
            case NEW:
                {
                int LA167_7 = input.LA(2);

                if ( (synpred58_Java()) ) {
                    alt167=1;
                }
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
                {
                int LA167_8 = input.LA(2);

                if ( (synpred58_Java()) ) {
                    alt167=1;
                }
                }
                break;
            case VOID:
                {
                int LA167_9 = input.LA(2);

                if ( (synpred58_Java()) ) {
                    alt167=1;
                }
                }
                break;
        }

        switch (alt167) {
            case 1 :
                // Java.g:584:10: explicitConstructorInvocation
                {
                pushFollow(FOLLOW_explicitConstructorInvocation_in_synpred60_Java2369);
                explicitConstructorInvocation();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        // Java.g:586:9: ( blockStatement )*
        loop168:
        do {
            int alt168=2;
            int LA168_0 = input.LA(1);

            if ( (LA168_0==EOF||LA168_0==ABSTRACT||(LA168_0 >= ASSERT && LA168_0 <= BANG)||(LA168_0 >= BOOLEAN && LA168_0 <= BYTE)||(LA168_0 >= CHAR && LA168_0 <= CLASS)||LA168_0==CONTINUE||LA168_0==DO||(LA168_0 >= DOUBLE && LA168_0 <= DOUBLELITERAL)||LA168_0==ENUM||(LA168_0 >= FALSE && LA168_0 <= FINAL)||(LA168_0 >= FLOAT && LA168_0 <= FOR)||(LA168_0 >= IDENTIFIER && LA168_0 <= IF)||(LA168_0 >= INT && LA168_0 <= INTLITERAL)||LA168_0==LBRACE||(LA168_0 >= LONG && LA168_0 <= LT)||(LA168_0 >= NATIVE && LA168_0 <= NULL)||LA168_0==PLUS||(LA168_0 >= PLUSPLUS && LA168_0 <= PUBLIC)||LA168_0==RETURN||(LA168_0 >= SEMI && LA168_0 <= SHORT)||(LA168_0 >= STATIC && LA168_0 <= SUB)||(LA168_0 >= SUBSUB && LA168_0 <= SYNCHRONIZED)||(LA168_0 >= THIS && LA168_0 <= THROW)||(LA168_0 >= TILDE && LA168_0 <= VOLATILE)||LA168_0==WHILE||LA168_0==136) ) {
                alt168=1;
            }


            switch (alt168) {
        	case 1 :
        	    // Java.g:586:10: blockStatement
        	    {
        	    pushFollow(FOLLOW_blockStatement_in_synpred60_Java2391);
        	    blockStatement();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop168;
            }
        } while (true);


        match(input,RBRACE,FOLLOW_RBRACE_in_synpred60_Java2412); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred60_Java

    // $ANTLR start synpred69_Java
    public final void synpred69_Java_fragment() throws RecognitionException {
        // Java.g:655:9: ( interfaceFieldDeclaration )
        // Java.g:655:9: interfaceFieldDeclaration
        {
        pushFollow(FOLLOW_interfaceFieldDeclaration_in_synpred69_Java3096);
        interfaceFieldDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred69_Java

    // $ANTLR start synpred70_Java
    public final void synpred70_Java_fragment() throws RecognitionException {
        // Java.g:656:9: ( interfaceMethodDeclaration )
        // Java.g:656:9: interfaceMethodDeclaration
        {
        pushFollow(FOLLOW_interfaceMethodDeclaration_in_synpred70_Java3106);
        interfaceMethodDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred70_Java

    // $ANTLR start synpred71_Java
    public final void synpred71_Java_fragment() throws RecognitionException {
        // Java.g:657:9: ( interfaceDeclaration )
        // Java.g:657:9: interfaceDeclaration
        {
        pushFollow(FOLLOW_interfaceDeclaration_in_synpred71_Java3116);
        interfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred71_Java

    // $ANTLR start synpred72_Java
    public final void synpred72_Java_fragment() throws RecognitionException {
        // Java.g:658:9: ( classDeclaration )
        // Java.g:658:9: classDeclaration
        {
        pushFollow(FOLLOW_classDeclaration_in_synpred72_Java3126);
        classDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred72_Java

    // $ANTLR start synpred97_Java
    public final void synpred97_Java_fragment() throws RecognitionException {
        // Java.g:753:9: ( ellipsisParameterDecl )
        // Java.g:753:9: ellipsisParameterDecl
        {
        pushFollow(FOLLOW_ellipsisParameterDecl_in_synpred97_Java3890);
        ellipsisParameterDecl();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred97_Java

    // $ANTLR start synpred99_Java
    public final void synpred99_Java_fragment() throws RecognitionException {
        // Java.g:754:9: ( normalParameterDecl ( ',' normalParameterDecl )* )
        // Java.g:754:9: normalParameterDecl ( ',' normalParameterDecl )*
        {
        pushFollow(FOLLOW_normalParameterDecl_in_synpred99_Java3900);
        normalParameterDecl();

        state._fsp--;
        if (state.failed) return ;

        // Java.g:755:9: ( ',' normalParameterDecl )*
        loop171:
        do {
            int alt171=2;
            int LA171_0 = input.LA(1);

            if ( (LA171_0==COMMA) ) {
                alt171=1;
            }


            switch (alt171) {
        	case 1 :
        	    // Java.g:755:10: ',' normalParameterDecl
        	    {
        	    match(input,COMMA,FOLLOW_COMMA_in_synpred99_Java3911); if (state.failed) return ;

        	    pushFollow(FOLLOW_normalParameterDecl_in_synpred99_Java3913);
        	    normalParameterDecl();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop171;
            }
        } while (true);


        }

    }
    // $ANTLR end synpred99_Java

    // $ANTLR start synpred100_Java
    public final void synpred100_Java_fragment() throws RecognitionException {
        // Java.g:757:10: ( normalParameterDecl ',' )
        // Java.g:757:10: normalParameterDecl ','
        {
        pushFollow(FOLLOW_normalParameterDecl_in_synpred100_Java3935);
        normalParameterDecl();

        state._fsp--;
        if (state.failed) return ;

        match(input,COMMA,FOLLOW_COMMA_in_synpred100_Java3945); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred100_Java

    // $ANTLR start synpred104_Java
    public final void synpred104_Java_fragment() throws RecognitionException {
        // Java.g:777:9: ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' )
        // Java.g:777:9: ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';'
        {
        // Java.g:777:9: ( nonWildcardTypeArguments )?
        int alt172=2;
        int LA172_0 = input.LA(1);

        if ( (LA172_0==LT) ) {
            alt172=1;
        }
        switch (alt172) {
            case 1 :
                // Java.g:777:10: nonWildcardTypeArguments
                {
                pushFollow(FOLLOW_nonWildcardTypeArguments_in_synpred104_Java4080);
                nonWildcardTypeArguments();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        if ( input.LA(1)==SUPER||input.LA(1)==THIS ) {
            input.consume();
            state.errorRecovery=false;
            state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        pushFollow(FOLLOW_arguments_in_synpred104_Java4138);
        arguments();

        state._fsp--;
        if (state.failed) return ;

        match(input,SEMI,FOLLOW_SEMI_in_synpred104_Java4140); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred104_Java

    // $ANTLR start synpred118_Java
    public final void synpred118_Java_fragment() throws RecognitionException {
        // Java.g:866:9: ( annotationMethodDeclaration )
        // Java.g:866:9: annotationMethodDeclaration
        {
        pushFollow(FOLLOW_annotationMethodDeclaration_in_synpred118_Java4759);
        annotationMethodDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred118_Java

    // $ANTLR start synpred119_Java
    public final void synpred119_Java_fragment() throws RecognitionException {
        // Java.g:867:9: ( interfaceFieldDeclaration )
        // Java.g:867:9: interfaceFieldDeclaration
        {
        pushFollow(FOLLOW_interfaceFieldDeclaration_in_synpred119_Java4769);
        interfaceFieldDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred119_Java

    // $ANTLR start synpred120_Java
    public final void synpred120_Java_fragment() throws RecognitionException {
        // Java.g:868:9: ( normalClassDeclaration )
        // Java.g:868:9: normalClassDeclaration
        {
        pushFollow(FOLLOW_normalClassDeclaration_in_synpred120_Java4779);
        normalClassDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred120_Java

    // $ANTLR start synpred121_Java
    public final void synpred121_Java_fragment() throws RecognitionException {
        // Java.g:869:9: ( normalInterfaceDeclaration )
        // Java.g:869:9: normalInterfaceDeclaration
        {
        pushFollow(FOLLOW_normalInterfaceDeclaration_in_synpred121_Java4789);
        normalInterfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred121_Java

    // $ANTLR start synpred122_Java
    public final void synpred122_Java_fragment() throws RecognitionException {
        // Java.g:870:9: ( enumDeclaration )
        // Java.g:870:9: enumDeclaration
        {
        pushFollow(FOLLOW_enumDeclaration_in_synpred122_Java4799);
        enumDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred122_Java

    // $ANTLR start synpred123_Java
    public final void synpred123_Java_fragment() throws RecognitionException {
        // Java.g:871:9: ( annotationTypeDeclaration )
        // Java.g:871:9: annotationTypeDeclaration
        {
        pushFollow(FOLLOW_annotationTypeDeclaration_in_synpred123_Java4809);
        annotationTypeDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred123_Java

    // $ANTLR start synpred126_Java
    public final void synpred126_Java_fragment() throws RecognitionException {
        // Java.g:918:9: ( localVariableDeclarationStatement )
        // Java.g:918:9: localVariableDeclarationStatement
        {
        pushFollow(FOLLOW_localVariableDeclarationStatement_in_synpred126_Java5010);
        localVariableDeclarationStatement();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred126_Java

    // $ANTLR start synpred127_Java
    public final void synpred127_Java_fragment() throws RecognitionException {
        // Java.g:919:9: ( classOrInterfaceDeclaration )
        // Java.g:919:9: classOrInterfaceDeclaration
        {
        pushFollow(FOLLOW_classOrInterfaceDeclaration_in_synpred127_Java5028);
        classOrInterfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred127_Java

    // $ANTLR start synpred132_Java
    public final void synpred132_Java_fragment() throws RecognitionException {
        // Java.g:939:39: ( 'else' statement )
        // Java.g:939:39: 'else' statement
        {
        match(input,ELSE,FOLLOW_ELSE_in_synpred132_Java5204); if (state.failed) return ;

        pushFollow(FOLLOW_statement_in_synpred132_Java5206);
        statement();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred132_Java

    // $ANTLR start synpred152_Java
    public final void synpred152_Java_fragment() throws RecognitionException {
        // Java.g:982:13: ( catches 'finally' block )
        // Java.g:982:13: catches 'finally' block
        {
        pushFollow(FOLLOW_catches_in_synpred152_Java5812);
        catches();

        state._fsp--;
        if (state.failed) return ;

        match(input,FINALLY,FOLLOW_FINALLY_in_synpred152_Java5814); if (state.failed) return ;

        pushFollow(FOLLOW_block_in_synpred152_Java5816);
        block();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred152_Java

    // $ANTLR start synpred153_Java
    public final void synpred153_Java_fragment() throws RecognitionException {
        // Java.g:983:13: ( catches )
        // Java.g:983:13: catches
        {
        pushFollow(FOLLOW_catches_in_synpred153_Java5830);
        catches();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred153_Java

    // $ANTLR start synpred155_Java
    public final void synpred155_Java_fragment() throws RecognitionException {
        // Java.g:995:23: ( variableModifiers )
        // Java.g:995:23: variableModifiers
        {
        pushFollow(FOLLOW_variableModifiers_in_synpred155_Java5925);
        variableModifiers();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred155_Java

    // $ANTLR start synpred156_Java
    public final void synpred156_Java_fragment() throws RecognitionException {
        // Java.g:995:22: ( ( variableModifiers )? type '|' )
        // Java.g:995:22: ( variableModifiers )? type '|'
        {
        // Java.g:995:22: ( variableModifiers )?
        int alt180=2;
        switch ( input.LA(1) ) {
            case FINAL:
            case 136:
                {
                alt180=1;
                }
                break;
            case IDENTIFIER:
                {
                int LA180_2 = input.LA(2);

                if ( (synpred155_Java()) ) {
                    alt180=1;
                }
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
                {
                int LA180_3 = input.LA(2);

                if ( (synpred155_Java()) ) {
                    alt180=1;
                }
                }
                break;
        }

        switch (alt180) {
            case 1 :
                // Java.g:995:23: variableModifiers
                {
                pushFollow(FOLLOW_variableModifiers_in_synpred156_Java5925);
                variableModifiers();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        pushFollow(FOLLOW_type_in_synpred156_Java5929);
        type();

        state._fsp--;
        if (state.failed) return ;

        match(input,BAR,FOLLOW_BAR_in_synpred156_Java5931); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred156_Java

    // $ANTLR start synpred158_Java
    public final void synpred158_Java_fragment() throws RecognitionException {
        // Java.g:1008:9: ( 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement )
        // Java.g:1008:9: 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement
        {
        match(input,FOR,FOLLOW_FOR_in_synpred158_Java6034); if (state.failed) return ;

        match(input,LPAREN,FOLLOW_LPAREN_in_synpred158_Java6036); if (state.failed) return ;

        pushFollow(FOLLOW_variableModifiers_in_synpred158_Java6038);
        variableModifiers();

        state._fsp--;
        if (state.failed) return ;

        pushFollow(FOLLOW_type_in_synpred158_Java6040);
        type();

        state._fsp--;
        if (state.failed) return ;

        match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred158_Java6042); if (state.failed) return ;

        match(input,COLON,FOLLOW_COLON_in_synpred158_Java6044); if (state.failed) return ;

        pushFollow(FOLLOW_expression_in_synpred158_Java6055);
        expression();

        state._fsp--;
        if (state.failed) return ;

        match(input,RPAREN,FOLLOW_RPAREN_in_synpred158_Java6057); if (state.failed) return ;

        pushFollow(FOLLOW_statement_in_synpred158_Java6059);
        statement();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred158_Java

    // $ANTLR start synpred162_Java
    public final void synpred162_Java_fragment() throws RecognitionException {
        // Java.g:1032:9: ( localVariableDeclaration )
        // Java.g:1032:9: localVariableDeclaration
        {
        pushFollow(FOLLOW_localVariableDeclaration_in_synpred162_Java6404);
        localVariableDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred162_Java

    // $ANTLR start synpred203_Java
    public final void synpred203_Java_fragment() throws RecognitionException {
        // Java.g:1186:9: ( castExpression )
        // Java.g:1186:9: castExpression
        {
        pushFollow(FOLLOW_castExpression_in_synpred203_Java7649);
        castExpression();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred203_Java

    // $ANTLR start synpred207_Java
    public final void synpred207_Java_fragment() throws RecognitionException {
        // Java.g:1196:9: ( '(' primitiveType ')' unaryExpression )
        // Java.g:1196:9: '(' primitiveType ')' unaryExpression
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred207_Java7740); if (state.failed) return ;

        pushFollow(FOLLOW_primitiveType_in_synpred207_Java7742);
        primitiveType();

        state._fsp--;
        if (state.failed) return ;

        match(input,RPAREN,FOLLOW_RPAREN_in_synpred207_Java7744); if (state.failed) return ;

        pushFollow(FOLLOW_unaryExpression_in_synpred207_Java7746);
        unaryExpression();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred207_Java

    // $ANTLR start synpred209_Java
    public final void synpred209_Java_fragment() throws RecognitionException {
        // Java.g:1206:10: ( '.' IDENTIFIER )
        // Java.g:1206:10: '.' IDENTIFIER
        {
        match(input,DOT,FOLLOW_DOT_in_synpred209_Java7817); if (state.failed) return ;

        match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred209_Java7819); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred209_Java

    // $ANTLR start synpred210_Java
    public final void synpred210_Java_fragment() throws RecognitionException {
        // Java.g:1208:10: ( identifierSuffix )
        // Java.g:1208:10: identifierSuffix
        {
        pushFollow(FOLLOW_identifierSuffix_in_synpred210_Java7841);
        identifierSuffix();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred210_Java

    // $ANTLR start synpred212_Java
    public final void synpred212_Java_fragment() throws RecognitionException {
        // Java.g:1211:10: ( '.' IDENTIFIER )
        // Java.g:1211:10: '.' IDENTIFIER
        {
        match(input,DOT,FOLLOW_DOT_in_synpred212_Java7873); if (state.failed) return ;

        match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred212_Java7875); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred212_Java

    // $ANTLR start synpred213_Java
    public final void synpred213_Java_fragment() throws RecognitionException {
        // Java.g:1213:10: ( identifierSuffix )
        // Java.g:1213:10: identifierSuffix
        {
        pushFollow(FOLLOW_identifierSuffix_in_synpred213_Java7897);
        identifierSuffix();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred213_Java

    // $ANTLR start synpred225_Java
    public final void synpred225_Java_fragment() throws RecognitionException {
        // Java.g:1241:10: ( '[' expression ']' )
        // Java.g:1241:10: '[' expression ']'
        {
        match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred225_Java8148); if (state.failed) return ;

        pushFollow(FOLLOW_expression_in_synpred225_Java8150);
        expression();

        state._fsp--;
        if (state.failed) return ;

        match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred225_Java8152); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred225_Java

    // $ANTLR start synpred237_Java
    public final void synpred237_Java_fragment() throws RecognitionException {
        // Java.g:1264:9: ( 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest )
        // Java.g:1264:9: 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest
        {
        match(input,NEW,FOLLOW_NEW_in_synpred237_Java8361); if (state.failed) return ;

        pushFollow(FOLLOW_nonWildcardTypeArguments_in_synpred237_Java8363);
        nonWildcardTypeArguments();

        state._fsp--;
        if (state.failed) return ;

        pushFollow(FOLLOW_classOrInterfaceType_in_synpred237_Java8365);
        classOrInterfaceType();

        state._fsp--;
        if (state.failed) return ;

        pushFollow(FOLLOW_classCreatorRest_in_synpred237_Java8367);
        classCreatorRest();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred237_Java

    // $ANTLR start synpred238_Java
    public final void synpred238_Java_fragment() throws RecognitionException {
        // Java.g:1265:9: ( 'new' classOrInterfaceType classCreatorRest )
        // Java.g:1265:9: 'new' classOrInterfaceType classCreatorRest
        {
        match(input,NEW,FOLLOW_NEW_in_synpred238_Java8377); if (state.failed) return ;

        pushFollow(FOLLOW_classOrInterfaceType_in_synpred238_Java8379);
        classOrInterfaceType();

        state._fsp--;
        if (state.failed) return ;

        pushFollow(FOLLOW_classCreatorRest_in_synpred238_Java8381);
        classCreatorRest();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred238_Java

    // $ANTLR start synpred240_Java
    public final void synpred240_Java_fragment() throws RecognitionException {
        // Java.g:1270:9: ( 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer )
        // Java.g:1270:9: 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer
        {
        match(input,NEW,FOLLOW_NEW_in_synpred240_Java8411); if (state.failed) return ;

        pushFollow(FOLLOW_createdName_in_synpred240_Java8413);
        createdName();

        state._fsp--;
        if (state.failed) return ;

        match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred240_Java8423); if (state.failed) return ;

        match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred240_Java8425); if (state.failed) return ;

        // Java.g:1272:9: ( '[' ']' )*
        loop189:
        do {
            int alt189=2;
            int LA189_0 = input.LA(1);

            if ( (LA189_0==LBRACKET) ) {
                alt189=1;
            }


            switch (alt189) {
        	case 1 :
        	    // Java.g:1272:10: '[' ']'
        	    {
        	    match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred240_Java8436); if (state.failed) return ;

        	    match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred240_Java8438); if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop189;
            }
        } while (true);


        pushFollow(FOLLOW_arrayInitializer_in_synpred240_Java8459);
        arrayInitializer();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred240_Java

    // $ANTLR start synpred241_Java
    public final void synpred241_Java_fragment() throws RecognitionException {
        // Java.g:1279:13: ( '[' expression ']' )
        // Java.g:1279:13: '[' expression ']'
        {
        match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred241_Java8508); if (state.failed) return ;

        pushFollow(FOLLOW_expression_in_synpred241_Java8510);
        expression();

        state._fsp--;
        if (state.failed) return ;

        match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred241_Java8524); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred241_Java

    // Delegated rules

    public final boolean synpred207_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred207_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred121_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred121_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred69_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred69_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred71_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred71_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred210_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred210_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred97_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred97_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred104_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred104_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred132_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred132_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred119_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred119_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred54_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred54_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred55_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred55_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred238_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred238_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred162_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred162_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred126_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred126_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred212_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred212_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred209_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred209_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred155_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred155_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred152_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred152_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred100_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred100_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred213_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred213_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred53_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred53_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred225_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred225_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred156_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred156_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred158_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred158_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred123_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred123_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred13_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred13_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred120_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred120_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred122_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred122_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred240_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred240_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred241_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred241_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred70_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred70_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred127_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred127_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred58_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred58_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred153_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred153_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred203_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred203_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred99_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred99_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred28_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred28_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred60_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred60_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred72_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred72_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred237_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred237_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred44_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred44_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred118_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred118_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_annotations_in_compilationUnit109 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_packageDeclaration_in_compilationUnit138 = new BitSet(new long[]{0x1200102000800010L,0x0008820608380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_importBlock_in_compilationUnit160 = new BitSet(new long[]{0x1000102000800012L,0x0008820608380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_typeDeclaration_in_compilationUnit173 = new BitSet(new long[]{0x1000102000800012L,0x0008820608380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_importDeclaration_in_importBlock294 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_PACKAGE_in_packageDeclaration322 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_qualifiedName_in_packageDeclaration324 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_packageDeclaration334 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDeclaration355 = new BitSet(new long[]{0x0040000000000000L,0x0000000200000000L});
    public static final BitSet FOLLOW_STATIC_in_importDeclaration367 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_importDeclaration388 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_DOT_in_importDeclaration390 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_STAR_in_importDeclaration392 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_importDeclaration402 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDeclaration412 = new BitSet(new long[]{0x0040000000000000L,0x0000000200000000L});
    public static final BitSet FOLLOW_STATIC_in_importDeclaration424 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_importDeclaration445 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_DOT_in_importDeclaration456 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_importDeclaration458 = new BitSet(new long[]{0x0000000080000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_DOT_in_importDeclaration480 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
    public static final BitSet FOLLOW_STAR_in_importDeclaration482 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_importDeclaration503 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedImportName523 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_DOT_in_qualifiedImportName534 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedImportName536 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_typeDeclaration577 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_classOrInterfaceDeclaration598 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_modifiers643 = new BitSet(new long[]{0x0000100000000012L,0x0008820600380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_PUBLIC_in_modifiers653 = new BitSet(new long[]{0x0000100000000012L,0x0008820600380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_PROTECTED_in_modifiers663 = new BitSet(new long[]{0x0000100000000012L,0x0008820600380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_PRIVATE_in_modifiers673 = new BitSet(new long[]{0x0000100000000012L,0x0008820600380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_STATIC_in_modifiers683 = new BitSet(new long[]{0x0000100000000012L,0x0008820600380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_ABSTRACT_in_modifiers693 = new BitSet(new long[]{0x0000100000000012L,0x0008820600380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_FINAL_in_modifiers703 = new BitSet(new long[]{0x0000100000000012L,0x0008820600380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_NATIVE_in_modifiers713 = new BitSet(new long[]{0x0000100000000012L,0x0008820600380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_SYNCHRONIZED_in_modifiers723 = new BitSet(new long[]{0x0000100000000012L,0x0008820600380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_TRANSIENT_in_modifiers733 = new BitSet(new long[]{0x0000100000000012L,0x0008820600380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_VOLATILE_in_modifiers743 = new BitSet(new long[]{0x0000100000000012L,0x0008820600380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_STRICTFP_in_modifiers753 = new BitSet(new long[]{0x0000100000000012L,0x0008820600380200L,0x0000000000000100L});
    public static final BitSet FOLLOW_FINAL_in_variableModifiers785 = new BitSet(new long[]{0x0000100000000002L,0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_annotation_in_variableModifiers799 = new BitSet(new long[]{0x0000100000000002L,0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_classDeclaration835 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_classDeclaration845 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_normalClassDeclaration865 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_CLASS_in_normalClassDeclaration868 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_normalClassDeclaration870 = new BitSet(new long[]{0x0100010000000000L,0x0000000000000082L});
    public static final BitSet FOLLOW_typeParameters_in_normalClassDeclaration881 = new BitSet(new long[]{0x0100010000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_normalClassDeclaration903 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_normalClassDeclaration905 = new BitSet(new long[]{0x0100000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_IMPLEMENTS_in_normalClassDeclaration927 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_typeList_in_normalClassDeclaration929 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_classBody_in_normalClassDeclaration962 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_typeParameters1097 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_typeParameter_in_typeParameters1111 = new BitSet(new long[]{0x0008000002000000L});
    public static final BitSet FOLLOW_COMMA_in_typeParameters1126 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_typeParameter_in_typeParameters1128 = new BitSet(new long[]{0x0008000002000000L});
    public static final BitSet FOLLOW_GT_in_typeParameters1153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_typeParameter1173 = new BitSet(new long[]{0x0000010000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_typeParameter1184 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_typeBound_in_typeParameter1186 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeBound1218 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_AMP_in_typeBound1229 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_typeBound1231 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_modifiers_in_enumDeclaration1263 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_ENUM_in_enumDeclaration1275 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_enumDeclaration1296 = new BitSet(new long[]{0x0100000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_IMPLEMENTS_in_enumDeclaration1307 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_typeList_in_enumDeclaration1309 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_enumBody_in_enumDeclaration1330 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_enumBody1424 = new BitSet(new long[]{0x0040000002000000L,0x0000000008800000L,0x0000000000000100L});
    public static final BitSet FOLLOW_enumConstants_in_enumBody1435 = new BitSet(new long[]{0x0000000002000000L,0x0000000008800000L});
    public static final BitSet FOLLOW_COMMA_in_enumBody1457 = new BitSet(new long[]{0x0000000000000000L,0x0000000008800000L});
    public static final BitSet FOLLOW_enumBodyDeclarations_in_enumBody1470 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_RBRACE_in_enumBody1492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumConstant_in_enumConstants1591 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_COMMA_in_enumConstants1602 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_enumConstant_in_enumConstants1604 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_annotations_in_enumConstant1638 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_enumConstant1659 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000042L});
    public static final BitSet FOLLOW_arguments_in_enumConstant1670 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_classBody_in_enumConstant1692 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_enumBodyDeclarations1733 = new BitSet(new long[]{0x1840502100A14012L,0x000C820618380292L,0x0000000000000100L});
    public static final BitSet FOLLOW_classBodyDeclaration_in_enumBodyDeclarations1745 = new BitSet(new long[]{0x1840502100A14012L,0x000C820618380292L,0x0000000000000100L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration1776 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration1786 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_normalInterfaceDeclaration1810 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_INTERFACE_in_normalInterfaceDeclaration1812 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_normalInterfaceDeclaration1814 = new BitSet(new long[]{0x0000010000000000L,0x0000000000000082L});
    public static final BitSet FOLLOW_typeParameters_in_normalInterfaceDeclaration1825 = new BitSet(new long[]{0x0000010000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_normalInterfaceDeclaration1847 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_typeList_in_normalInterfaceDeclaration1849 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceBody_in_normalInterfaceDeclaration1870 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeList1890 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_COMMA_in_typeList1901 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_typeList1903 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_LBRACE_in_classBody1934 = new BitSet(new long[]{0x1840502100A14010L,0x000C820618B80292L,0x0000000000000100L});
    public static final BitSet FOLLOW_classBodyDeclaration_in_classBody1946 = new BitSet(new long[]{0x1840502100A14010L,0x000C820618B80292L,0x0000000000000100L});
    public static final BitSet FOLLOW_RBRACE_in_classBody1968 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_interfaceBody2032 = new BitSet(new long[]{0x1840502100A14010L,0x000C820618B80290L,0x0000000000000100L});
    public static final BitSet FOLLOW_interfaceBodyDeclaration_in_interfaceBody2044 = new BitSet(new long[]{0x1840502100A14010L,0x000C820618B80290L,0x0000000000000100L});
    public static final BitSet FOLLOW_RBRACE_in_interfaceBody2066 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_classBodyDeclaration2130 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STATIC_in_classBodyDeclaration2141 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_classBodyDeclaration2163 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberDecl_in_classBodyDeclaration2173 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldDeclaration_in_memberDecl2194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_methodDeclaration_in_memberDecl2213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_memberDecl2232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_memberDecl2243 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_methodDeclaration2281 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_typeParameters_in_methodDeclaration2292 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_methodDeclaration2313 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_formalParameters_in_methodDeclaration2323 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000002L});
    public static final BitSet FOLLOW_THROWS_in_methodDeclaration2334 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_methodDeclaration2336 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_methodDeclaration2357 = new BitSet(new long[]{0x38C1D82350E1C310L,0x200FDBDE1ABD0EF2L,0x0000000000000100L});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_methodDeclaration2369 = new BitSet(new long[]{0x38C1D82350E1C310L,0x200FDBDE1ABD0E72L,0x0000000000000100L});
    public static final BitSet FOLLOW_blockStatement_in_methodDeclaration2391 = new BitSet(new long[]{0x38C1D82350E1C310L,0x200FDBDE1ABD0E72L,0x0000000000000100L});
    public static final BitSet FOLLOW_RBRACE_in_methodDeclaration2412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_methodDeclaration2564 = new BitSet(new long[]{0x0840400100214000L,0x0004000010000090L});
    public static final BitSet FOLLOW_typeParameters_in_methodDeclaration2575 = new BitSet(new long[]{0x0840400100214000L,0x0004000010000010L});
    public static final BitSet FOLLOW_type_in_methodDeclaration2597 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_VOID_in_methodDeclaration2611 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_methodDeclaration2631 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_formalParameters_in_methodDeclaration2641 = new BitSet(new long[]{0x0000000000000000L,0x0000200008000006L});
    public static final BitSet FOLLOW_LBRACKET_in_methodDeclaration2652 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_methodDeclaration2654 = new BitSet(new long[]{0x0000000000000000L,0x0000200008000006L});
    public static final BitSet FOLLOW_THROWS_in_methodDeclaration2676 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_methodDeclaration2678 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000002L});
    public static final BitSet FOLLOW_block_in_methodDeclaration2733 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_methodDeclaration2747 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_fieldDeclaration2946 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_fieldDeclaration2956 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_variableDeclarator_in_fieldDeclaration2966 = new BitSet(new long[]{0x0000000002000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_COMMA_in_fieldDeclaration2977 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_variableDeclarator_in_fieldDeclaration2979 = new BitSet(new long[]{0x0000000002000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_fieldDeclaration3000 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_variableDeclarator3020 = new BitSet(new long[]{0x0000004000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_LBRACKET_in_variableDeclarator3031 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_variableDeclarator3033 = new BitSet(new long[]{0x0000004000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_EQ_in_variableDeclarator3055 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C72L});
    public static final BitSet FOLLOW_variableInitializer_in_variableDeclarator3057 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceFieldDeclaration_in_interfaceBodyDeclaration3096 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodDeclaration_in_interfaceBodyDeclaration3106 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_interfaceBodyDeclaration3116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_interfaceBodyDeclaration3126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_interfaceBodyDeclaration3136 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_interfaceMethodDeclaration3156 = new BitSet(new long[]{0x0840400100214000L,0x0004000010000090L});
    public static final BitSet FOLLOW_typeParameters_in_interfaceMethodDeclaration3167 = new BitSet(new long[]{0x0840400100214000L,0x0004000010000010L});
    public static final BitSet FOLLOW_type_in_interfaceMethodDeclaration3189 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_VOID_in_interfaceMethodDeclaration3200 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_interfaceMethodDeclaration3220 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_formalParameters_in_interfaceMethodDeclaration3230 = new BitSet(new long[]{0x0000000000000000L,0x0000200008000004L});
    public static final BitSet FOLLOW_LBRACKET_in_interfaceMethodDeclaration3241 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_interfaceMethodDeclaration3243 = new BitSet(new long[]{0x0000000000000000L,0x0000200008000004L});
    public static final BitSet FOLLOW_THROWS_in_interfaceMethodDeclaration3265 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_interfaceMethodDeclaration3267 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_interfaceMethodDeclaration3280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_interfaceFieldDeclaration3302 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_interfaceFieldDeclaration3304 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_variableDeclarator_in_interfaceFieldDeclaration3306 = new BitSet(new long[]{0x0000000002000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_COMMA_in_interfaceFieldDeclaration3317 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_variableDeclarator_in_interfaceFieldDeclaration3319 = new BitSet(new long[]{0x0000000002000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_interfaceFieldDeclaration3340 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_type3361 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_LBRACKET_in_type3372 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_type3374 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_primitiveType_in_type3395 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_LBRACKET_in_type3406 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_type3408 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_IDENTIFIER_in_classOrInterfaceType3440 = new BitSet(new long[]{0x0000000080000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_typeArguments_in_classOrInterfaceType3451 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_DOT_in_classOrInterfaceType3473 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_classOrInterfaceType3475 = new BitSet(new long[]{0x0000000080000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_typeArguments_in_classOrInterfaceType3490 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_LT_in_typeArguments3627 = new BitSet(new long[]{0x0840400100214000L,0x0000000010400010L});
    public static final BitSet FOLLOW_typeArgument_in_typeArguments3629 = new BitSet(new long[]{0x0008000002000000L});
    public static final BitSet FOLLOW_COMMA_in_typeArguments3640 = new BitSet(new long[]{0x0840400100214000L,0x0000000010400010L});
    public static final BitSet FOLLOW_typeArgument_in_typeArguments3642 = new BitSet(new long[]{0x0008000002000000L});
    public static final BitSet FOLLOW_GT_in_typeArguments3664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeArgument3684 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_typeArgument3694 = new BitSet(new long[]{0x0000010000000002L,0x0000008000000000L});
    public static final BitSet FOLLOW_set_in_typeArgument3718 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_typeArgument3762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList3793 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_COMMA_in_qualifiedNameList3804 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList3806 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_LPAREN_in_formalParameters3837 = new BitSet(new long[]{0x0840500100214000L,0x0000000014000010L,0x0000000000000100L});
    public static final BitSet FOLLOW_formalParameterDecls_in_formalParameters3848 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RPAREN_in_formalParameters3870 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ellipsisParameterDecl_in_formalParameterDecls3890 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalParameterDecl_in_formalParameterDecls3900 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_COMMA_in_formalParameterDecls3911 = new BitSet(new long[]{0x0840500100214000L,0x0000000010000010L,0x0000000000000100L});
    public static final BitSet FOLLOW_normalParameterDecl_in_formalParameterDecls3913 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_normalParameterDecl_in_formalParameterDecls3935 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_COMMA_in_formalParameterDecls3945 = new BitSet(new long[]{0x0840500100214000L,0x0000000010000010L,0x0000000000000100L});
    public static final BitSet FOLLOW_ellipsisParameterDecl_in_formalParameterDecls3967 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_normalParameterDecl3987 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_normalParameterDecl3989 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_normalParameterDecl3991 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_LBRACKET_in_normalParameterDecl4002 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_normalParameterDecl4004 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_variableModifiers_in_ellipsisParameterDecl4035 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_ellipsisParameterDecl4045 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ELLIPSIS_in_ellipsisParameterDecl4048 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_ellipsisParameterDecl4058 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation4080 = new BitSet(new long[]{0x0000000000000000L,0x0000088000000000L});
    public static final BitSet FOLLOW_set_in_explicitConstructorInvocation4106 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_arguments_in_explicitConstructorInvocation4138 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_explicitConstructorInvocation4140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_in_explicitConstructorInvocation4151 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_DOT_in_explicitConstructorInvocation4161 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000080L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation4172 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_SUPER_in_explicitConstructorInvocation4193 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_arguments_in_explicitConstructorInvocation4203 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_explicitConstructorInvocation4205 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedName4225 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_DOT_in_qualifiedName4236 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedName4238 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_annotation_in_annotations4270 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_136_in_annotation4323 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_qualifiedName_in_annotation4325 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000040L});
    public static final BitSet FOLLOW_LPAREN_in_annotation4339 = new BitSet(new long[]{0x2840C80300614200L,0x000548D814050C72L,0x0000000000000100L});
    public static final BitSet FOLLOW_elementValuePairs_in_annotation4366 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_elementValue_in_annotation4390 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RPAREN_in_annotation4426 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs4458 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_COMMA_in_elementValuePairs4469 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs4471 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_elementValuePair4502 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_EQ_in_elementValuePair4504 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C72L,0x0000000000000100L});
    public static final BitSet FOLLOW_elementValue_in_elementValuePair4506 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalExpression_in_elementValue4526 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_elementValue4536 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_elementValueArrayInitializer_in_elementValue4546 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_elementValueArrayInitializer4566 = new BitSet(new long[]{0x2840C80302614200L,0x000548D810850C72L,0x0000000000000100L});
    public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer4577 = new BitSet(new long[]{0x0000000002000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_COMMA_in_elementValueArrayInitializer4592 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C72L,0x0000000000000100L});
    public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer4594 = new BitSet(new long[]{0x0000000002000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_COMMA_in_elementValueArrayInitializer4623 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_RBRACE_in_elementValueArrayInitializer4627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_annotationTypeDeclaration4650 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000100L});
    public static final BitSet FOLLOW_136_in_annotationTypeDeclaration4652 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_INTERFACE_in_annotationTypeDeclaration4662 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_annotationTypeDeclaration4672 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeBody_in_annotationTypeDeclaration4682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_annotationTypeBody4703 = new BitSet(new long[]{0x1840502100A14010L,0x0008820618B80210L,0x0000000000000100L});
    public static final BitSet FOLLOW_annotationTypeElementDeclaration_in_annotationTypeBody4715 = new BitSet(new long[]{0x1840502100A14010L,0x0008820618B80210L,0x0000000000000100L});
    public static final BitSet FOLLOW_RBRACE_in_annotationTypeBody4737 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationMethodDeclaration_in_annotationTypeElementDeclaration4759 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceFieldDeclaration_in_annotationTypeElementDeclaration4769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_annotationTypeElementDeclaration4779 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_annotationTypeElementDeclaration4789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_annotationTypeElementDeclaration4799 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_annotationTypeElementDeclaration4809 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_annotationTypeElementDeclaration4819 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_annotationMethodDeclaration4839 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_annotationMethodDeclaration4841 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_annotationMethodDeclaration4843 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_LPAREN_in_annotationMethodDeclaration4853 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RPAREN_in_annotationMethodDeclaration4855 = new BitSet(new long[]{0x0000000020000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_DEFAULT_in_annotationMethodDeclaration4858 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C72L,0x0000000000000100L});
    public static final BitSet FOLLOW_elementValue_in_annotationMethodDeclaration4860 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_annotationMethodDeclaration4889 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block4913 = new BitSet(new long[]{0x38C1D82350E1C310L,0x200FDBDE1ABD0E72L,0x0000000000000100L});
    public static final BitSet FOLLOW_blockStatement_in_block4924 = new BitSet(new long[]{0x38C1D82350E1C310L,0x200FDBDE1ABD0E72L,0x0000000000000100L});
    public static final BitSet FOLLOW_RBRACE_in_block4945 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclarationStatement_in_blockStatement5010 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_blockStatement5028 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_blockStatement5038 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement5059 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_localVariableDeclarationStatement5069 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_localVariableDeclaration5089 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_localVariableDeclaration5091 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_variableDeclarator_in_localVariableDeclaration5101 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_COMMA_in_localVariableDeclaration5112 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_variableDeclarator_in_localVariableDeclaration5114 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_block_in_statement5145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_statement5155 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_statement5158 = new BitSet(new long[]{0x0000000001000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_COLON_in_statement5161 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_statement5163 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5167 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_statement5197 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_parExpression_in_statement5199 = new BitSet(new long[]{0x28C1C8035061C300L,0x20075BD81A050C72L});
    public static final BitSet FOLLOW_statement_in_statement5201 = new BitSet(new long[]{0x0000001000000002L});
    public static final BitSet FOLLOW_ELSE_in_statement5204 = new BitSet(new long[]{0x28C1C8035061C300L,0x20075BD81A050C72L});
    public static final BitSet FOLLOW_statement_in_statement5206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forstatement_in_statement5241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_statement5251 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_parExpression_in_statement5253 = new BitSet(new long[]{0x28C1C8035061C300L,0x20075BD81A050C72L});
    public static final BitSet FOLLOW_statement_in_statement5255 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DO_in_statement5277 = new BitSet(new long[]{0x28C1C8035061C300L,0x20075BD81A050C72L});
    public static final BitSet FOLLOW_statement_in_statement5279 = new BitSet(new long[]{0x0000000000000000L,0x2000000000000000L});
    public static final BitSet FOLLOW_WHILE_in_statement5281 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_parExpression_in_statement5283 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_trystatement_in_statement5315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SWITCH_in_statement5325 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_parExpression_in_statement5327 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_statement5329 = new BitSet(new long[]{0x0000000020080000L,0x0000000000800000L});
    public static final BitSet FOLLOW_switchBlockStatementGroups_in_statement5331 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_RBRACE_in_statement5333 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SYNCHRONIZED_in_statement5359 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_parExpression_in_statement5361 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_statement5363 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_statement5385 = new BitSet(new long[]{0x2840C80300614200L,0x000548D818050C70L});
    public static final BitSet FOLLOW_expression_in_statement5388 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5393 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THROW_in_statement5419 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_statement5421 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_statement5445 = new BitSet(new long[]{0x0040000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_statement5460 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_statement5527 = new BitSet(new long[]{0x0040000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_statement5542 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statement5609 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_statement5612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_statement5633 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_COLON_in_statement5635 = new BitSet(new long[]{0x28C1C8035061C300L,0x20075BD81A050C72L});
    public static final BitSet FOLLOW_statement_in_statement5637 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_statement5659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups5688 = new BitSet(new long[]{0x0000000020080002L});
    public static final BitSet FOLLOW_switchLabel_in_switchBlockStatementGroup5717 = new BitSet(new long[]{0x38C1D82350E1C312L,0x200FDBDE1A3D0E72L,0x0000000000000100L});
    public static final BitSet FOLLOW_blockStatement_in_switchBlockStatementGroup5728 = new BitSet(new long[]{0x38C1D82350E1C312L,0x200FDBDE1A3D0E72L,0x0000000000000100L});
    public static final BitSet FOLLOW_CASE_in_switchLabel5759 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_switchLabel5761 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_COLON_in_switchLabel5763 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEFAULT_in_switchLabel5773 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_COLON_in_switchLabel5775 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRY_in_trystatement5796 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_trystatement5798 = new BitSet(new long[]{0x0000200000100000L});
    public static final BitSet FOLLOW_catches_in_trystatement5812 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_FINALLY_in_trystatement5814 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_trystatement5816 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_trystatement5830 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FINALLY_in_trystatement5844 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_trystatement5846 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_catches5877 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_catchClause_in_catches5888 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_CATCH_in_catchClause5919 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_LPAREN_in_catchClause5921 = new BitSet(new long[]{0x0840500100214000L,0x0000000010000010L,0x0000000000000100L});
    public static final BitSet FOLLOW_variableModifiers_in_catchClause5925 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_catchClause5929 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_BAR_in_catchClause5931 = new BitSet(new long[]{0x0840500100214000L,0x0000000010000010L,0x0000000000000100L});
    public static final BitSet FOLLOW_formalParameter_in_catchClause5935 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RPAREN_in_catchClause5945 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_catchClause5947 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_formalParameter5968 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_formalParameter5970 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_formalParameter5972 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_LBRACKET_in_formalParameter5983 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_formalParameter5985 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_FOR_in_forstatement6034 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_LPAREN_in_forstatement6036 = new BitSet(new long[]{0x0840500100214000L,0x0000000010000010L,0x0000000000000100L});
    public static final BitSet FOLLOW_variableModifiers_in_forstatement6038 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_forstatement6040 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_forstatement6042 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_COLON_in_forstatement6044 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_forstatement6055 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RPAREN_in_forstatement6057 = new BitSet(new long[]{0x28C1C8035061C300L,0x20075BD81A050C72L});
    public static final BitSet FOLLOW_statement_in_forstatement6059 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_forstatement6118 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_LPAREN_in_forstatement6120 = new BitSet(new long[]{0x2840D80300614200L,0x000548D818050C70L,0x0000000000000100L});
    public static final BitSet FOLLOW_forInit_in_forstatement6140 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_forstatement6161 = new BitSet(new long[]{0x2840C80300614200L,0x000548D818050C70L});
    public static final BitSet FOLLOW_expression_in_forstatement6181 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_forstatement6202 = new BitSet(new long[]{0x2840C80300614200L,0x000548D814050C70L});
    public static final BitSet FOLLOW_expressionList_in_forstatement6222 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RPAREN_in_forstatement6243 = new BitSet(new long[]{0x28C1C8035061C300L,0x20075BD81A050C72L});
    public static final BitSet FOLLOW_statement_in_forstatement6245 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_forInit6404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_forInit6414 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_parExpression6434 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_parExpression6436 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RPAREN_in_parExpression6438 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionList6458 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_COMMA_in_expressionList6469 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_expressionList6471 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_conditionalExpression_in_expression6503 = new BitSet(new long[]{0x0008004000042082L,0x0000002140028080L});
    public static final BitSet FOLLOW_assignmentOperator_in_expression6514 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_expression6516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQ_in_assignmentOperator6548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSEQ_in_assignmentOperator6558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBEQ_in_assignmentOperator6568 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAREQ_in_assignmentOperator6578 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SLASHEQ_in_assignmentOperator6588 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AMPEQ_in_assignmentOperator6598 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BAREQ_in_assignmentOperator6608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARETEQ_in_assignmentOperator6618 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENTEQ_in_assignmentOperator6628 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_assignmentOperator6639 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_LT_in_assignmentOperator6641 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_EQ_in_assignmentOperator6643 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator6654 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator6656 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator6658 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_EQ_in_assignmentOperator6660 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator6671 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator6673 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_EQ_in_assignmentOperator6675 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalOrExpression_in_conditionalExpression6696 = new BitSet(new long[]{0x0000000000000002L,0x0000000000400000L});
    public static final BitSet FOLLOW_QUES_in_conditionalExpression6707 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_conditionalExpression6709 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_COLON_in_conditionalExpression6711 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_conditionalExpression_in_conditionalExpression6713 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression6744 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_BARBAR_in_conditionalOrExpression6755 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression6757 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression6788 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_AMPAMP_in_conditionalAndExpression6799 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression6801 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression6832 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_BAR_in_inclusiveOrExpression6843 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression6845 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression6876 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_CARET_in_exclusiveOrExpression6887 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression6889 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_equalityExpression_in_andExpression6920 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_AMP_in_andExpression6931 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_equalityExpression_in_andExpression6933 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression6964 = new BitSet(new long[]{0x0000008000000402L});
    public static final BitSet FOLLOW_set_in_equalityExpression6991 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression7041 = new BitSet(new long[]{0x0000008000000402L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression7072 = new BitSet(new long[]{0x0400000000000002L});
    public static final BitSet FOLLOW_INSTANCEOF_in_instanceOfExpression7083 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_instanceOfExpression7085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression7116 = new BitSet(new long[]{0x0008000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_relationalOp_in_relationalExpression7127 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression7129 = new BitSet(new long[]{0x0008000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_LT_in_relationalOp7161 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_EQ_in_relationalOp7163 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_relationalOp7174 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_EQ_in_relationalOp7176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_relationalOp7186 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_relationalOp7196 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression7216 = new BitSet(new long[]{0x0008000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_shiftOp_in_shiftExpression7227 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression7229 = new BitSet(new long[]{0x0008000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_LT_in_shiftOp7262 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_LT_in_shiftOp7264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_shiftOp7275 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_GT_in_shiftOp7277 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_GT_in_shiftOp7279 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_shiftOp7290 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_GT_in_shiftOp7292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression7313 = new BitSet(new long[]{0x0000000000000002L,0x0000001000010000L});
    public static final BitSet FOLLOW_set_in_additiveExpression7340 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression7390 = new BitSet(new long[]{0x0000000000000002L,0x0000001000010000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression7428 = new BitSet(new long[]{0x0000000000000002L,0x00000000A0004000L});
    public static final BitSet FOLLOW_set_in_multiplicativeExpression7455 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression7523 = new BitSet(new long[]{0x0000000000000002L,0x00000000A0004000L});
    public static final BitSet FOLLOW_PLUS_in_unaryExpression7556 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression7559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_unaryExpression7569 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression7571 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_unaryExpression7581 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression7583 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_unaryExpression7593 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression7595 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression7605 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_unaryExpressionNotPlusMinus7625 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus7627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BANG_in_unaryExpressionNotPlusMinus7637 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus7639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_unaryExpressionNotPlusMinus7649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_in_unaryExpressionNotPlusMinus7659 = new BitSet(new long[]{0x0000000080000002L,0x0000004000040004L});
    public static final BitSet FOLLOW_selector_in_unaryExpressionNotPlusMinus7670 = new BitSet(new long[]{0x0000000080000002L,0x0000004000040004L});
    public static final BitSet FOLLOW_LPAREN_in_castExpression7740 = new BitSet(new long[]{0x0800400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_primitiveType_in_castExpression7742 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RPAREN_in_castExpression7744 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_unaryExpression_in_castExpression7746 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_castExpression7756 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_castExpression7758 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RPAREN_in_castExpression7760 = new BitSet(new long[]{0x2840C80300614200L,0x0005488810000C70L});
    public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_castExpression7762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_primary7784 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primary7806 = new BitSet(new long[]{0x0000000080000002L,0x0000000000000044L});
    public static final BitSet FOLLOW_DOT_in_primary7817 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_primary7819 = new BitSet(new long[]{0x0000000080000002L,0x0000000000000044L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary7841 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_primary7862 = new BitSet(new long[]{0x0000000080000002L,0x0000000000000044L});
    public static final BitSet FOLLOW_DOT_in_primary7873 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_primary7875 = new BitSet(new long[]{0x0000000080000002L,0x0000000000000044L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary7897 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_primary7918 = new BitSet(new long[]{0x0000000080000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_superSuffix_in_primary7928 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primary7938 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_creator_in_primary7948 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_primary7958 = new BitSet(new long[]{0x0000000080000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_LBRACKET_in_primary7969 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_primary7971 = new BitSet(new long[]{0x0000000080000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_DOT_in_primary7992 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_CLASS_in_primary7994 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VOID_in_primary8004 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_DOT_in_primary8006 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_CLASS_in_primary8008 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_superSuffix8034 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_superSuffix8044 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_typeArguments_in_superSuffix8047 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_superSuffix8068 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000040L});
    public static final BitSet FOLLOW_arguments_in_superSuffix8079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_identifierSuffix8112 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_identifierSuffix8114 = new BitSet(new long[]{0x0000000080000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix8135 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_CLASS_in_identifierSuffix8137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_identifierSuffix8148 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_identifierSuffix8150 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_identifierSuffix8152 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix8173 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix8183 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_CLASS_in_identifierSuffix8185 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix8195 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_identifierSuffix8197 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_identifierSuffix8199 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix8201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix8211 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_THIS_in_identifierSuffix8213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix8223 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_SUPER_in_identifierSuffix8225 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix8227 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_innerCreator_in_identifierSuffix8237 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_selector8259 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_selector8261 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000040L});
    public static final BitSet FOLLOW_arguments_in_selector8272 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_selector8293 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_THIS_in_selector8295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_selector8305 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_SUPER_in_selector8307 = new BitSet(new long[]{0x0000000080000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_superSuffix_in_selector8317 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_innerCreator_in_selector8327 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_selector8337 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_selector8339 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_selector8341 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_creator8361 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_creator8363 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_creator8365 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_classCreatorRest_in_creator8367 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_creator8377 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_creator8379 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_classCreatorRest_in_creator8381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arrayCreator_in_creator8391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_arrayCreator8411 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_createdName_in_arrayCreator8413 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator8423 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator8425 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator8436 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator8438 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
    public static final BitSet FOLLOW_arrayInitializer_in_arrayCreator8459 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_arrayCreator8470 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_createdName_in_arrayCreator8472 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator8482 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_arrayCreator8484 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator8494 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator8508 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_arrayCreator8510 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator8524 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator8546 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator8548 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000004L});
    public static final BitSet FOLLOW_arrayInitializer_in_variableInitializer8579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_variableInitializer8589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_arrayInitializer8609 = new BitSet(new long[]{0x2840C80302614200L,0x000548D810850C72L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer8625 = new BitSet(new long[]{0x0000000002000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_COMMA_in_arrayInitializer8644 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C72L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer8646 = new BitSet(new long[]{0x0000000002000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_COMMA_in_arrayInitializer8696 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
    public static final BitSet FOLLOW_RBRACE_in_arrayInitializer8709 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_createdName8743 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_createdName8753 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_innerCreator8774 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_NEW_in_innerCreator8776 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_innerCreator8787 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_innerCreator8808 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000C0L});
    public static final BitSet FOLLOW_typeArguments_in_innerCreator8819 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_classCreatorRest_in_innerCreator8840 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_classCreatorRest8861 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_classBody_in_classCreatorRest8872 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_nonWildcardTypeArguments8904 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_typeList_in_nonWildcardTypeArguments8906 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_GT_in_nonWildcardTypeArguments8916 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arguments8936 = new BitSet(new long[]{0x2840C80300614200L,0x000548D814050C70L});
    public static final BitSet FOLLOW_expressionList_in_arguments8939 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RPAREN_in_arguments8952 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotations_in_synpred2_Java109 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_packageDeclaration_in_synpred2_Java138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_importBlock_in_synpred3_Java160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_synpred13_Java598 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_synpred28_Java835 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_synpred44_Java1776 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldDeclaration_in_synpred53_Java2194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_methodDeclaration_in_synpred54_Java2213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_synpred55_Java2232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_synpred58_Java2369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_synpred60_Java2281 = new BitSet(new long[]{0x0040000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_typeParameters_in_synpred60_Java2292 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred60_Java2313 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_formalParameters_in_synpred60_Java2323 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000002L});
    public static final BitSet FOLLOW_THROWS_in_synpred60_Java2334 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_synpred60_Java2336 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_synpred60_Java2357 = new BitSet(new long[]{0x38C1D82350E1C310L,0x200FDBDE1ABD0EF2L,0x0000000000000100L});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_synpred60_Java2369 = new BitSet(new long[]{0x38C1D82350E1C310L,0x200FDBDE1ABD0E72L,0x0000000000000100L});
    public static final BitSet FOLLOW_blockStatement_in_synpred60_Java2391 = new BitSet(new long[]{0x38C1D82350E1C310L,0x200FDBDE1ABD0E72L,0x0000000000000100L});
    public static final BitSet FOLLOW_RBRACE_in_synpred60_Java2412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceFieldDeclaration_in_synpred69_Java3096 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodDeclaration_in_synpred70_Java3106 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_synpred71_Java3116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_synpred72_Java3126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ellipsisParameterDecl_in_synpred97_Java3890 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalParameterDecl_in_synpred99_Java3900 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_COMMA_in_synpred99_Java3911 = new BitSet(new long[]{0x0840500100214000L,0x0000000010000010L,0x0000000000000100L});
    public static final BitSet FOLLOW_normalParameterDecl_in_synpred99_Java3913 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_normalParameterDecl_in_synpred100_Java3935 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_COMMA_in_synpred100_Java3945 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_synpred104_Java4080 = new BitSet(new long[]{0x0000000000000000L,0x0000088000000000L});
    public static final BitSet FOLLOW_set_in_synpred104_Java4106 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_arguments_in_synpred104_Java4138 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_SEMI_in_synpred104_Java4140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationMethodDeclaration_in_synpred118_Java4759 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceFieldDeclaration_in_synpred119_Java4769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_synpred120_Java4779 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_synpred121_Java4789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_synpred122_Java4799 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_synpred123_Java4809 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclarationStatement_in_synpred126_Java5010 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_synpred127_Java5028 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ELSE_in_synpred132_Java5204 = new BitSet(new long[]{0x28C1C8035061C300L,0x20075BD81A050C72L});
    public static final BitSet FOLLOW_statement_in_synpred132_Java5206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_synpred152_Java5812 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_FINALLY_in_synpred152_Java5814 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_synpred152_Java5816 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_synpred153_Java5830 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_synpred155_Java5925 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_synpred156_Java5925 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_synpred156_Java5929 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_BAR_in_synpred156_Java5931 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_synpred158_Java6034 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_LPAREN_in_synpred158_Java6036 = new BitSet(new long[]{0x0840500100214000L,0x0000000010000010L,0x0000000000000100L});
    public static final BitSet FOLLOW_variableModifiers_in_synpred158_Java6038 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_type_in_synpred158_Java6040 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred158_Java6042 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_COLON_in_synpred158_Java6044 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_synpred158_Java6055 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred158_Java6057 = new BitSet(new long[]{0x28C1C8035061C300L,0x20075BD81A050C72L});
    public static final BitSet FOLLOW_statement_in_synpred158_Java6059 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_synpred162_Java6404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_synpred203_Java7649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred207_Java7740 = new BitSet(new long[]{0x0800400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_primitiveType_in_synpred207_Java7742 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred207_Java7744 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_unaryExpression_in_synpred207_Java7746 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_synpred209_Java7817 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred209_Java7819 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred210_Java7841 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_synpred212_Java7873 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred212_Java7875 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred213_Java7897 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred225_Java8148 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_synpred225_Java8150 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred225_Java8152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_synpred237_Java8361 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_synpred237_Java8363 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_synpred237_Java8365 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_classCreatorRest_in_synpred237_Java8367 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_synpred238_Java8377 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_synpred238_Java8379 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_classCreatorRest_in_synpred238_Java8381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_synpred240_Java8411 = new BitSet(new long[]{0x0840400100214000L,0x0000000010000010L});
    public static final BitSet FOLLOW_createdName_in_synpred240_Java8413 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred240_Java8423 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred240_Java8425 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred240_Java8436 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred240_Java8438 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000006L});
    public static final BitSet FOLLOW_arrayInitializer_in_synpred240_Java8459 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred241_Java8508 = new BitSet(new long[]{0x2840C80300614200L,0x000548D810050C70L});
    public static final BitSet FOLLOW_expression_in_synpred241_Java8510 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred241_Java8524 = new BitSet(new long[]{0x0000000000000002L});

}