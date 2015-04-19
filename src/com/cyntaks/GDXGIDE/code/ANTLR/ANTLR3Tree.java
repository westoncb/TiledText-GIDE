// $ANTLR 3.4 ANTLR3Tree.g 2012-08-16 23:29:18
package com.cyntaks.GDXGIDE.code.ANTLR;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/** ANTLR v3 tree grammar to walk trees created by ANTLRv3.g */
@SuppressWarnings({"all", "warnings", "unchecked"})
public class ANTLR3Tree extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ACTION", "ACTION_CHAR_LITERAL", "ACTION_ESC", "ACTION_STRING_LITERAL", "ALT", "ARG", "ARGLIST", "ARG_ACTION", "BACKTRACK_SEMPRED", "BANG", "BLOCK", "CHAR_LITERAL", "CHAR_RANGE", "CLOSURE", "COMBINED_GRAMMAR", "DOC_COMMENT", "DOUBLE_ANGLE_STRING_LITERAL", "DOUBLE_QUOTE_STRING_LITERAL", "EOA", "EOB", "EOR", "EPSILON", "ESC", "FRAGMENT", "GATED_SEMPRED", "ID", "INITACTION", "INT", "LABEL", "LEXER", "LEXER_GRAMMAR", "LITERAL_CHAR", "ML_COMMENT", "NESTED_ACTION", "NESTED_ARG_ACTION", "OPTIONAL", "OPTIONS", "PARSER", "PARSER_GRAMMAR", "POSITIVE_CLOSURE", "RANGE", "RET", "REWRITE", "ROOT", "RULE", "RULE_REF", "SCOPE", "SEMPRED", "SL_COMMENT", "SRC", "STRING_LITERAL", "SYNPRED", "SYN_SEMPRED", "TEMPLATE", "TOKENS", "TOKEN_REF", "TREE_BEGIN", "TREE_GRAMMAR", "WS", "WS_LOOP", "XDIGIT", "'$'", "'('", "')'", "'*'", "'+'", "'+='", "','", "'.'", "':'", "'::'", "';'", "'='", "'=>'", "'?'", "'@'", "'catch'", "'finally'", "'grammar'", "'lexer'", "'parser'", "'private'", "'protected'", "'public'", "'returns'", "'throws'", "'tree'", "'|'", "'}'", "'~'"
    };

    public static final int EOF=-1;
    public static final int T__65=65;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int T__73=73;
    public static final int T__74=74;
    public static final int T__75=75;
    public static final int T__76=76;
    public static final int T__77=77;
    public static final int T__78=78;
    public static final int T__79=79;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int T__84=84;
    public static final int T__85=85;
    public static final int T__86=86;
    public static final int T__87=87;
    public static final int T__88=88;
    public static final int T__89=89;
    public static final int T__90=90;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__93=93;
    public static final int ACTION=4;
    public static final int ACTION_CHAR_LITERAL=5;
    public static final int ACTION_ESC=6;
    public static final int ACTION_STRING_LITERAL=7;
    public static final int ALT=8;
    public static final int ARG=9;
    public static final int ARGLIST=10;
    public static final int ARG_ACTION=11;
    public static final int BACKTRACK_SEMPRED=12;
    public static final int BANG=13;
    public static final int BLOCK=14;
    public static final int CHAR_LITERAL=15;
    public static final int CHAR_RANGE=16;
    public static final int CLOSURE=17;
    public static final int COMBINED_GRAMMAR=18;
    public static final int DOC_COMMENT=19;
    public static final int DOUBLE_ANGLE_STRING_LITERAL=20;
    public static final int DOUBLE_QUOTE_STRING_LITERAL=21;
    public static final int EOA=22;
    public static final int EOB=23;
    public static final int EOR=24;
    public static final int EPSILON=25;
    public static final int ESC=26;
    public static final int FRAGMENT=27;
    public static final int GATED_SEMPRED=28;
    public static final int ID=29;
    public static final int INITACTION=30;
    public static final int INT=31;
    public static final int LABEL=32;
    public static final int LEXER=33;
    public static final int LEXER_GRAMMAR=34;
    public static final int LITERAL_CHAR=35;
    public static final int ML_COMMENT=36;
    public static final int NESTED_ACTION=37;
    public static final int NESTED_ARG_ACTION=38;
    public static final int OPTIONAL=39;
    public static final int OPTIONS=40;
    public static final int PARSER=41;
    public static final int PARSER_GRAMMAR=42;
    public static final int POSITIVE_CLOSURE=43;
    public static final int RANGE=44;
    public static final int RET=45;
    public static final int REWRITE=46;
    public static final int ROOT=47;
    public static final int RULE=48;
    public static final int RULE_REF=49;
    public static final int SCOPE=50;
    public static final int SEMPRED=51;
    public static final int SL_COMMENT=52;
    public static final int SRC=53;
    public static final int STRING_LITERAL=54;
    public static final int SYNPRED=55;
    public static final int SYN_SEMPRED=56;
    public static final int TEMPLATE=57;
    public static final int TOKENS=58;
    public static final int TOKEN_REF=59;
    public static final int TREE_BEGIN=60;
    public static final int TREE_GRAMMAR=61;
    public static final int WS=62;
    public static final int WS_LOOP=63;
    public static final int XDIGIT=64;

    // delegates
    public TreeParser[] getDelegates() {
        return new TreeParser[] {};
    }

    // delegators


    public ANTLR3Tree(TreeNodeStream input) {
        this(input, new RecognizerSharedState());
    }
    public ANTLR3Tree(TreeNodeStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return ANTLR3Tree.tokenNames; }
    public String getGrammarFileName() { return "ANTLR3Tree.g"; }



    // $ANTLR start "grammarDef"
    // ANTLR3Tree.g:37:1: grammarDef : ^( grammarType ID ( DOC_COMMENT )? ( optionsSpec )? ( tokensSpec )? ( attrScope )* ( action )* ( rule )+ ) ;
    public final void grammarDef() throws RecognitionException {
        try {
            // ANTLR3Tree.g:38:5: ( ^( grammarType ID ( DOC_COMMENT )? ( optionsSpec )? ( tokensSpec )? ( attrScope )* ( action )* ( rule )+ ) )
            // ANTLR3Tree.g:38:9: ^( grammarType ID ( DOC_COMMENT )? ( optionsSpec )? ( tokensSpec )? ( attrScope )* ( action )* ( rule )+ )
            {
            pushFollow(FOLLOW_grammarType_in_grammarDef46);
            grammarType();

            state._fsp--;


            match(input, Token.DOWN, null); 
            match(input,ID,FOLLOW_ID_in_grammarDef48); 

            // ANTLR3Tree.g:38:27: ( DOC_COMMENT )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==DOC_COMMENT) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // ANTLR3Tree.g:38:27: DOC_COMMENT
                    {
                    match(input,DOC_COMMENT,FOLLOW_DOC_COMMENT_in_grammarDef50); 

                    }
                    break;

            }


            // ANTLR3Tree.g:38:40: ( optionsSpec )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==OPTIONS) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // ANTLR3Tree.g:38:40: optionsSpec
                    {
                    pushFollow(FOLLOW_optionsSpec_in_grammarDef53);
                    optionsSpec();

                    state._fsp--;


                    }
                    break;

            }


            // ANTLR3Tree.g:38:53: ( tokensSpec )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==TOKENS) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // ANTLR3Tree.g:38:53: tokensSpec
                    {
                    pushFollow(FOLLOW_tokensSpec_in_grammarDef56);
                    tokensSpec();

                    state._fsp--;


                    }
                    break;

            }


            // ANTLR3Tree.g:38:65: ( attrScope )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==SCOPE) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // ANTLR3Tree.g:38:65: attrScope
            	    {
            	    pushFollow(FOLLOW_attrScope_in_grammarDef59);
            	    attrScope();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            // ANTLR3Tree.g:38:76: ( action )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==79) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // ANTLR3Tree.g:38:76: action
            	    {
            	    pushFollow(FOLLOW_action_in_grammarDef62);
            	    action();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            // ANTLR3Tree.g:38:84: ( rule )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==RULE) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // ANTLR3Tree.g:38:84: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_grammarDef65);
            	    rule();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "grammarDef"



    // $ANTLR start "grammarType"
    // ANTLR3Tree.g:41:1: grammarType : ( LEXER_GRAMMAR | PARSER_GRAMMAR | TREE_GRAMMAR | COMBINED_GRAMMAR );
    public final void grammarType() throws RecognitionException {
        try {
            // ANTLR3Tree.g:42:2: ( LEXER_GRAMMAR | PARSER_GRAMMAR | TREE_GRAMMAR | COMBINED_GRAMMAR )
            // ANTLR3Tree.g:
            {
            if ( input.LA(1)==COMBINED_GRAMMAR||input.LA(1)==LEXER_GRAMMAR||input.LA(1)==PARSER_GRAMMAR||input.LA(1)==TREE_GRAMMAR ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "grammarType"



    // $ANTLR start "tokensSpec"
    // ANTLR3Tree.g:48:1: tokensSpec : ^( TOKENS ( tokenSpec )+ ) ;
    public final void tokensSpec() throws RecognitionException {
        try {
            // ANTLR3Tree.g:49:2: ( ^( TOKENS ( tokenSpec )+ ) )
            // ANTLR3Tree.g:49:4: ^( TOKENS ( tokenSpec )+ )
            {
            match(input,TOKENS,FOLLOW_TOKENS_in_tokensSpec121); 

            match(input, Token.DOWN, null); 
            // ANTLR3Tree.g:49:13: ( tokenSpec )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==TOKEN_REF||LA7_0==76) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // ANTLR3Tree.g:49:13: tokenSpec
            	    {
            	    pushFollow(FOLLOW_tokenSpec_in_tokensSpec123);
            	    tokenSpec();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "tokensSpec"



    // $ANTLR start "tokenSpec"
    // ANTLR3Tree.g:52:1: tokenSpec : ( ^( '=' TOKEN_REF STRING_LITERAL ) | ^( '=' TOKEN_REF CHAR_LITERAL ) | TOKEN_REF );
    public final void tokenSpec() throws RecognitionException {
        try {
            // ANTLR3Tree.g:53:2: ( ^( '=' TOKEN_REF STRING_LITERAL ) | ^( '=' TOKEN_REF CHAR_LITERAL ) | TOKEN_REF )
            int alt8=3;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==76) ) {
                int LA8_1 = input.LA(2);

                if ( (LA8_1==DOWN) ) {
                    int LA8_3 = input.LA(3);

                    if ( (LA8_3==TOKEN_REF) ) {
                        int LA8_4 = input.LA(4);

                        if ( (LA8_4==STRING_LITERAL) ) {
                            alt8=1;
                        }
                        else if ( (LA8_4==CHAR_LITERAL) ) {
                            alt8=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 8, 4, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 3, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA8_0==TOKEN_REF) ) {
                alt8=3;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }
            switch (alt8) {
                case 1 :
                    // ANTLR3Tree.g:53:4: ^( '=' TOKEN_REF STRING_LITERAL )
                    {
                    match(input,76,FOLLOW_76_in_tokenSpec137); 

                    match(input, Token.DOWN, null); 
                    match(input,TOKEN_REF,FOLLOW_TOKEN_REF_in_tokenSpec139); 

                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_tokenSpec141); 

                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:54:4: ^( '=' TOKEN_REF CHAR_LITERAL )
                    {
                    match(input,76,FOLLOW_76_in_tokenSpec148); 

                    match(input, Token.DOWN, null); 
                    match(input,TOKEN_REF,FOLLOW_TOKEN_REF_in_tokenSpec150); 

                    match(input,CHAR_LITERAL,FOLLOW_CHAR_LITERAL_in_tokenSpec152); 

                    match(input, Token.UP, null); 


                    }
                    break;
                case 3 :
                    // ANTLR3Tree.g:55:4: TOKEN_REF
                    {
                    match(input,TOKEN_REF,FOLLOW_TOKEN_REF_in_tokenSpec158); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "tokenSpec"



    // $ANTLR start "attrScope"
    // ANTLR3Tree.g:58:1: attrScope : ^( 'scope' ID ACTION ) ;
    public final void attrScope() throws RecognitionException {
        try {
            // ANTLR3Tree.g:59:2: ( ^( 'scope' ID ACTION ) )
            // ANTLR3Tree.g:59:4: ^( 'scope' ID ACTION )
            {
            match(input,SCOPE,FOLLOW_SCOPE_in_attrScope170); 

            match(input, Token.DOWN, null); 
            match(input,ID,FOLLOW_ID_in_attrScope172); 

            match(input,ACTION,FOLLOW_ACTION_in_attrScope174); 

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "attrScope"



    // $ANTLR start "action"
    // ANTLR3Tree.g:62:1: action : ( ^( '@' ID ID ACTION ) | ^( '@' ID ACTION ) );
    public final void action() throws RecognitionException {
        try {
            // ANTLR3Tree.g:63:2: ( ^( '@' ID ID ACTION ) | ^( '@' ID ACTION ) )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==79) ) {
                int LA9_1 = input.LA(2);

                if ( (LA9_1==DOWN) ) {
                    int LA9_2 = input.LA(3);

                    if ( (LA9_2==ID) ) {
                        int LA9_3 = input.LA(4);

                        if ( (LA9_3==ID) ) {
                            alt9=1;
                        }
                        else if ( (LA9_3==ACTION) ) {
                            alt9=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 9, 3, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 9, 2, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }
            switch (alt9) {
                case 1 :
                    // ANTLR3Tree.g:63:4: ^( '@' ID ID ACTION )
                    {
                    match(input,79,FOLLOW_79_in_action187); 

                    match(input, Token.DOWN, null); 
                    match(input,ID,FOLLOW_ID_in_action189); 

                    match(input,ID,FOLLOW_ID_in_action191); 

                    match(input,ACTION,FOLLOW_ACTION_in_action193); 

                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:64:4: ^( '@' ID ACTION )
                    {
                    match(input,79,FOLLOW_79_in_action200); 

                    match(input, Token.DOWN, null); 
                    match(input,ID,FOLLOW_ID_in_action202); 

                    match(input,ACTION,FOLLOW_ACTION_in_action204); 

                    match(input, Token.UP, null); 


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "action"



    // $ANTLR start "optionsSpec"
    // ANTLR3Tree.g:67:1: optionsSpec : ^( OPTIONS ( option )+ ) ;
    public final void optionsSpec() throws RecognitionException {
        try {
            // ANTLR3Tree.g:68:2: ( ^( OPTIONS ( option )+ ) )
            // ANTLR3Tree.g:68:4: ^( OPTIONS ( option )+ )
            {
            match(input,OPTIONS,FOLLOW_OPTIONS_in_optionsSpec217); 

            match(input, Token.DOWN, null); 
            // ANTLR3Tree.g:68:14: ( option )+
            int cnt10=0;
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==76) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // ANTLR3Tree.g:68:14: option
            	    {
            	    pushFollow(FOLLOW_option_in_optionsSpec219);
            	    option();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt10 >= 1 ) break loop10;
                        EarlyExitException eee =
                            new EarlyExitException(10, input);
                        throw eee;
                }
                cnt10++;
            } while (true);


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "optionsSpec"



    // $ANTLR start "option"
    // ANTLR3Tree.g:71:1: option : ^( '=' ID optionValue ) ;
    public final void option() throws RecognitionException {
        try {
            // ANTLR3Tree.g:72:5: ( ^( '=' ID optionValue ) )
            // ANTLR3Tree.g:72:9: ^( '=' ID optionValue )
            {
            match(input,76,FOLLOW_76_in_option238); 

            match(input, Token.DOWN, null); 
            match(input,ID,FOLLOW_ID_in_option240); 

            pushFollow(FOLLOW_optionValue_in_option242);
            optionValue();

            state._fsp--;


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "option"



    // $ANTLR start "optionValue"
    // ANTLR3Tree.g:75:1: optionValue : ( ID | STRING_LITERAL | CHAR_LITERAL | INT );
    public final void optionValue() throws RecognitionException {
        try {
            // ANTLR3Tree.g:76:5: ( ID | STRING_LITERAL | CHAR_LITERAL | INT )
            // ANTLR3Tree.g:
            {
            if ( input.LA(1)==CHAR_LITERAL||input.LA(1)==ID||input.LA(1)==INT||input.LA(1)==STRING_LITERAL ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "optionValue"



    // $ANTLR start "rule"
    // ANTLR3Tree.g:82:1: rule : ^( RULE ID ( modifier )? ( ^( ARG ARG_ACTION ) )? ( ^( RET ARG_ACTION ) )? ( optionsSpec )? ( ruleScopeSpec )? ( ruleAction )* altList ( exceptionGroup )? EOR ) ;
    public final void rule() throws RecognitionException {
        try {
            // ANTLR3Tree.g:83:2: ( ^( RULE ID ( modifier )? ( ^( ARG ARG_ACTION ) )? ( ^( RET ARG_ACTION ) )? ( optionsSpec )? ( ruleScopeSpec )? ( ruleAction )* altList ( exceptionGroup )? EOR ) )
            // ANTLR3Tree.g:83:4: ^( RULE ID ( modifier )? ( ^( ARG ARG_ACTION ) )? ( ^( RET ARG_ACTION ) )? ( optionsSpec )? ( ruleScopeSpec )? ( ruleAction )* altList ( exceptionGroup )? EOR )
            {
            match(input,RULE,FOLLOW_RULE_in_rule308); 

            match(input, Token.DOWN, null); 
            match(input,ID,FOLLOW_ID_in_rule310); 

            // ANTLR3Tree.g:83:15: ( modifier )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==FRAGMENT||(LA11_0 >= 85 && LA11_0 <= 87)) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // ANTLR3Tree.g:83:15: modifier
                    {
                    pushFollow(FOLLOW_modifier_in_rule312);
                    modifier();

                    state._fsp--;


                    }
                    break;

            }


            // ANTLR3Tree.g:83:25: ( ^( ARG ARG_ACTION ) )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ARG) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // ANTLR3Tree.g:83:26: ^( ARG ARG_ACTION )
                    {
                    match(input,ARG,FOLLOW_ARG_in_rule317); 

                    match(input, Token.DOWN, null); 
                    match(input,ARG_ACTION,FOLLOW_ARG_ACTION_in_rule319); 

                    match(input, Token.UP, null); 


                    }
                    break;

            }


            // ANTLR3Tree.g:83:46: ( ^( RET ARG_ACTION ) )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==RET) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // ANTLR3Tree.g:83:47: ^( RET ARG_ACTION )
                    {
                    match(input,RET,FOLLOW_RET_in_rule326); 

                    match(input, Token.DOWN, null); 
                    match(input,ARG_ACTION,FOLLOW_ARG_ACTION_in_rule328); 

                    match(input, Token.UP, null); 


                    }
                    break;

            }


            // ANTLR3Tree.g:84:9: ( optionsSpec )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==OPTIONS) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // ANTLR3Tree.g:84:9: optionsSpec
                    {
                    pushFollow(FOLLOW_optionsSpec_in_rule341);
                    optionsSpec();

                    state._fsp--;


                    }
                    break;

            }


            // ANTLR3Tree.g:84:22: ( ruleScopeSpec )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==SCOPE) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // ANTLR3Tree.g:84:22: ruleScopeSpec
                    {
                    pushFollow(FOLLOW_ruleScopeSpec_in_rule344);
                    ruleScopeSpec();

                    state._fsp--;


                    }
                    break;

            }


            // ANTLR3Tree.g:84:37: ( ruleAction )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==79) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // ANTLR3Tree.g:84:37: ruleAction
            	    {
            	    pushFollow(FOLLOW_ruleAction_in_rule347);
            	    ruleAction();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);


            pushFollow(FOLLOW_altList_in_rule358);
            altList();

            state._fsp--;


            // ANTLR3Tree.g:86:9: ( exceptionGroup )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( ((LA17_0 >= 80 && LA17_0 <= 81)) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // ANTLR3Tree.g:86:9: exceptionGroup
                    {
                    pushFollow(FOLLOW_exceptionGroup_in_rule368);
                    exceptionGroup();

                    state._fsp--;


                    }
                    break;

            }


            match(input,EOR,FOLLOW_EOR_in_rule371); 

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rule"



    // $ANTLR start "modifier"
    // ANTLR3Tree.g:90:1: modifier : ( 'protected' | 'public' | 'private' | 'fragment' );
    public final void modifier() throws RecognitionException {
        try {
            // ANTLR3Tree.g:91:2: ( 'protected' | 'public' | 'private' | 'fragment' )
            // ANTLR3Tree.g:
            {
            if ( input.LA(1)==FRAGMENT||(input.LA(1) >= 85 && input.LA(1) <= 87) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "modifier"



    // $ANTLR start "ruleAction"
    // ANTLR3Tree.g:95:1: ruleAction : ^( '@' ID ACTION ) ;
    public final void ruleAction() throws RecognitionException {
        try {
            // ANTLR3Tree.g:96:2: ( ^( '@' ID ACTION ) )
            // ANTLR3Tree.g:96:4: ^( '@' ID ACTION )
            {
            match(input,79,FOLLOW_79_in_ruleAction410); 

            match(input, Token.DOWN, null); 
            match(input,ID,FOLLOW_ID_in_ruleAction412); 

            match(input,ACTION,FOLLOW_ACTION_in_ruleAction414); 

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "ruleAction"



    // $ANTLR start "throwsSpec"
    // ANTLR3Tree.g:99:1: throwsSpec : ^( 'throws' ( ID )+ ) ;
    public final void throwsSpec() throws RecognitionException {
        try {
            // ANTLR3Tree.g:100:2: ( ^( 'throws' ( ID )+ ) )
            // ANTLR3Tree.g:100:4: ^( 'throws' ( ID )+ )
            {
            match(input,89,FOLLOW_89_in_throwsSpec427); 

            match(input, Token.DOWN, null); 
            // ANTLR3Tree.g:100:15: ( ID )+
            int cnt18=0;
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==ID) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // ANTLR3Tree.g:100:15: ID
            	    {
            	    match(input,ID,FOLLOW_ID_in_throwsSpec429); 

            	    }
            	    break;

            	default :
            	    if ( cnt18 >= 1 ) break loop18;
                        EarlyExitException eee =
                            new EarlyExitException(18, input);
                        throw eee;
                }
                cnt18++;
            } while (true);


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "throwsSpec"



    // $ANTLR start "ruleScopeSpec"
    // ANTLR3Tree.g:103:1: ruleScopeSpec : ( ^( 'scope' ACTION ) | ^( 'scope' ACTION ( ID )+ ) | ^( 'scope' ( ID )+ ) );
    public final void ruleScopeSpec() throws RecognitionException {
        try {
            // ANTLR3Tree.g:104:2: ( ^( 'scope' ACTION ) | ^( 'scope' ACTION ( ID )+ ) | ^( 'scope' ( ID )+ ) )
            int alt21=3;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==SCOPE) ) {
                int LA21_1 = input.LA(2);

                if ( (LA21_1==DOWN) ) {
                    int LA21_2 = input.LA(3);

                    if ( (LA21_2==ACTION) ) {
                        int LA21_3 = input.LA(4);

                        if ( (LA21_3==UP) ) {
                            alt21=1;
                        }
                        else if ( (LA21_3==ID) ) {
                            alt21=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 21, 3, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA21_2==ID) ) {
                        alt21=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 21, 2, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;

            }
            switch (alt21) {
                case 1 :
                    // ANTLR3Tree.g:104:4: ^( 'scope' ACTION )
                    {
                    match(input,SCOPE,FOLLOW_SCOPE_in_ruleScopeSpec443); 

                    match(input, Token.DOWN, null); 
                    match(input,ACTION,FOLLOW_ACTION_in_ruleScopeSpec445); 

                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:105:4: ^( 'scope' ACTION ( ID )+ )
                    {
                    match(input,SCOPE,FOLLOW_SCOPE_in_ruleScopeSpec452); 

                    match(input, Token.DOWN, null); 
                    match(input,ACTION,FOLLOW_ACTION_in_ruleScopeSpec454); 

                    // ANTLR3Tree.g:105:21: ( ID )+
                    int cnt19=0;
                    loop19:
                    do {
                        int alt19=2;
                        int LA19_0 = input.LA(1);

                        if ( (LA19_0==ID) ) {
                            alt19=1;
                        }


                        switch (alt19) {
                    	case 1 :
                    	    // ANTLR3Tree.g:105:21: ID
                    	    {
                    	    match(input,ID,FOLLOW_ID_in_ruleScopeSpec456); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt19 >= 1 ) break loop19;
                                EarlyExitException eee =
                                    new EarlyExitException(19, input);
                                throw eee;
                        }
                        cnt19++;
                    } while (true);


                    match(input, Token.UP, null); 


                    }
                    break;
                case 3 :
                    // ANTLR3Tree.g:106:4: ^( 'scope' ( ID )+ )
                    {
                    match(input,SCOPE,FOLLOW_SCOPE_in_ruleScopeSpec464); 

                    match(input, Token.DOWN, null); 
                    // ANTLR3Tree.g:106:14: ( ID )+
                    int cnt20=0;
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( (LA20_0==ID) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // ANTLR3Tree.g:106:14: ID
                    	    {
                    	    match(input,ID,FOLLOW_ID_in_ruleScopeSpec466); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt20 >= 1 ) break loop20;
                                EarlyExitException eee =
                                    new EarlyExitException(20, input);
                                throw eee;
                        }
                        cnt20++;
                    } while (true);


                    match(input, Token.UP, null); 


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "ruleScopeSpec"



    // $ANTLR start "block"
    // ANTLR3Tree.g:109:1: block : ^( BLOCK ( optionsSpec )? ( alternative rewrite )+ EOB ) ;
    public final void block() throws RecognitionException {
        try {
            // ANTLR3Tree.g:110:5: ( ^( BLOCK ( optionsSpec )? ( alternative rewrite )+ EOB ) )
            // ANTLR3Tree.g:110:9: ^( BLOCK ( optionsSpec )? ( alternative rewrite )+ EOB )
            {
            match(input,BLOCK,FOLLOW_BLOCK_in_block486); 

            match(input, Token.DOWN, null); 
            // ANTLR3Tree.g:110:18: ( optionsSpec )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==OPTIONS) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // ANTLR3Tree.g:110:18: optionsSpec
                    {
                    pushFollow(FOLLOW_optionsSpec_in_block488);
                    optionsSpec();

                    state._fsp--;


                    }
                    break;

            }


            // ANTLR3Tree.g:110:31: ( alternative rewrite )+
            int cnt23=0;
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( (LA23_0==ALT) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // ANTLR3Tree.g:110:32: alternative rewrite
            	    {
            	    pushFollow(FOLLOW_alternative_in_block492);
            	    alternative();

            	    state._fsp--;


            	    pushFollow(FOLLOW_rewrite_in_block494);
            	    rewrite();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt23 >= 1 ) break loop23;
                        EarlyExitException eee =
                            new EarlyExitException(23, input);
                        throw eee;
                }
                cnt23++;
            } while (true);


            match(input,EOB,FOLLOW_EOB_in_block498); 

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "block"



    // $ANTLR start "altList"
    // ANTLR3Tree.g:113:1: altList : ^( BLOCK ( alternative rewrite )+ EOB ) ;
    public final void altList() throws RecognitionException {
        try {
            // ANTLR3Tree.g:114:5: ( ^( BLOCK ( alternative rewrite )+ EOB ) )
            // ANTLR3Tree.g:114:9: ^( BLOCK ( alternative rewrite )+ EOB )
            {
            match(input,BLOCK,FOLLOW_BLOCK_in_altList521); 

            match(input, Token.DOWN, null); 
            // ANTLR3Tree.g:114:18: ( alternative rewrite )+
            int cnt24=0;
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==ALT) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // ANTLR3Tree.g:114:19: alternative rewrite
            	    {
            	    pushFollow(FOLLOW_alternative_in_altList524);
            	    alternative();

            	    state._fsp--;


            	    pushFollow(FOLLOW_rewrite_in_altList526);
            	    rewrite();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt24 >= 1 ) break loop24;
                        EarlyExitException eee =
                            new EarlyExitException(24, input);
                        throw eee;
                }
                cnt24++;
            } while (true);


            match(input,EOB,FOLLOW_EOB_in_altList530); 

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "altList"



    // $ANTLR start "alternative"
    // ANTLR3Tree.g:117:1: alternative : ( ^( ALT ( element )+ EOA ) | ^( ALT EPSILON EOA ) );
    public final void alternative() throws RecognitionException {
        try {
            // ANTLR3Tree.g:118:5: ( ^( ALT ( element )+ EOA ) | ^( ALT EPSILON EOA ) )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==ALT) ) {
                int LA26_1 = input.LA(2);

                if ( (LA26_1==DOWN) ) {
                    int LA26_2 = input.LA(3);

                    if ( (LA26_2==EPSILON) ) {
                        alt26=2;
                    }
                    else if ( (LA26_2==ACTION||(LA26_2 >= BANG && LA26_2 <= CLOSURE)||LA26_2==GATED_SEMPRED||LA26_2==OPTIONAL||LA26_2==POSITIVE_CLOSURE||LA26_2==ROOT||LA26_2==RULE_REF||LA26_2==SEMPRED||(LA26_2 >= STRING_LITERAL && LA26_2 <= SYN_SEMPRED)||(LA26_2 >= TOKEN_REF && LA26_2 <= TREE_BEGIN)||LA26_2==70||LA26_2==72||LA26_2==76||LA26_2==93) ) {
                        alt26=1;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 26, 2, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 26, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;

            }
            switch (alt26) {
                case 1 :
                    // ANTLR3Tree.g:118:9: ^( ALT ( element )+ EOA )
                    {
                    match(input,ALT,FOLLOW_ALT_in_alternative552); 

                    match(input, Token.DOWN, null); 
                    // ANTLR3Tree.g:118:15: ( element )+
                    int cnt25=0;
                    loop25:
                    do {
                        int alt25=2;
                        int LA25_0 = input.LA(1);

                        if ( (LA25_0==ACTION||(LA25_0 >= BANG && LA25_0 <= CLOSURE)||LA25_0==GATED_SEMPRED||LA25_0==OPTIONAL||LA25_0==POSITIVE_CLOSURE||LA25_0==ROOT||LA25_0==RULE_REF||LA25_0==SEMPRED||(LA25_0 >= STRING_LITERAL && LA25_0 <= SYN_SEMPRED)||(LA25_0 >= TOKEN_REF && LA25_0 <= TREE_BEGIN)||LA25_0==70||LA25_0==72||LA25_0==76||LA25_0==93) ) {
                            alt25=1;
                        }


                        switch (alt25) {
                    	case 1 :
                    	    // ANTLR3Tree.g:118:15: element
                    	    {
                    	    pushFollow(FOLLOW_element_in_alternative554);
                    	    element();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt25 >= 1 ) break loop25;
                                EarlyExitException eee =
                                    new EarlyExitException(25, input);
                                throw eee;
                        }
                        cnt25++;
                    } while (true);


                    match(input,EOA,FOLLOW_EOA_in_alternative557); 

                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:119:9: ^( ALT EPSILON EOA )
                    {
                    match(input,ALT,FOLLOW_ALT_in_alternative569); 

                    match(input, Token.DOWN, null); 
                    match(input,EPSILON,FOLLOW_EPSILON_in_alternative571); 

                    match(input,EOA,FOLLOW_EOA_in_alternative573); 

                    match(input, Token.UP, null); 


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "alternative"



    // $ANTLR start "exceptionGroup"
    // ANTLR3Tree.g:122:1: exceptionGroup : ( ( exceptionHandler )+ ( finallyClause )? | finallyClause );
    public final void exceptionGroup() throws RecognitionException {
        try {
            // ANTLR3Tree.g:123:2: ( ( exceptionHandler )+ ( finallyClause )? | finallyClause )
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==80) ) {
                alt29=1;
            }
            else if ( (LA29_0==81) ) {
                alt29=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;

            }
            switch (alt29) {
                case 1 :
                    // ANTLR3Tree.g:123:4: ( exceptionHandler )+ ( finallyClause )?
                    {
                    // ANTLR3Tree.g:123:4: ( exceptionHandler )+
                    int cnt27=0;
                    loop27:
                    do {
                        int alt27=2;
                        int LA27_0 = input.LA(1);

                        if ( (LA27_0==80) ) {
                            alt27=1;
                        }


                        switch (alt27) {
                    	case 1 :
                    	    // ANTLR3Tree.g:123:4: exceptionHandler
                    	    {
                    	    pushFollow(FOLLOW_exceptionHandler_in_exceptionGroup588);
                    	    exceptionHandler();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt27 >= 1 ) break loop27;
                                EarlyExitException eee =
                                    new EarlyExitException(27, input);
                                throw eee;
                        }
                        cnt27++;
                    } while (true);


                    // ANTLR3Tree.g:123:22: ( finallyClause )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==81) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // ANTLR3Tree.g:123:22: finallyClause
                            {
                            pushFollow(FOLLOW_finallyClause_in_exceptionGroup591);
                            finallyClause();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:124:4: finallyClause
                    {
                    pushFollow(FOLLOW_finallyClause_in_exceptionGroup597);
                    finallyClause();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "exceptionGroup"



    // $ANTLR start "exceptionHandler"
    // ANTLR3Tree.g:127:1: exceptionHandler : ^( 'catch' ARG_ACTION ACTION ) ;
    public final void exceptionHandler() throws RecognitionException {
        try {
            // ANTLR3Tree.g:128:5: ( ^( 'catch' ARG_ACTION ACTION ) )
            // ANTLR3Tree.g:128:10: ^( 'catch' ARG_ACTION ACTION )
            {
            match(input,80,FOLLOW_80_in_exceptionHandler618); 

            match(input, Token.DOWN, null); 
            match(input,ARG_ACTION,FOLLOW_ARG_ACTION_in_exceptionHandler620); 

            match(input,ACTION,FOLLOW_ACTION_in_exceptionHandler622); 

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "exceptionHandler"



    // $ANTLR start "finallyClause"
    // ANTLR3Tree.g:131:1: finallyClause : ^( 'finally' ACTION ) ;
    public final void finallyClause() throws RecognitionException {
        try {
            // ANTLR3Tree.g:132:5: ( ^( 'finally' ACTION ) )
            // ANTLR3Tree.g:132:10: ^( 'finally' ACTION )
            {
            match(input,81,FOLLOW_81_in_finallyClause644); 

            match(input, Token.DOWN, null); 
            match(input,ACTION,FOLLOW_ACTION_in_finallyClause646); 

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "finallyClause"



    // $ANTLR start "element"
    // ANTLR3Tree.g:135:1: element : elementNoOptionSpec ;
    public final void element() throws RecognitionException {
        try {
            // ANTLR3Tree.g:136:2: ( elementNoOptionSpec )
            // ANTLR3Tree.g:136:4: elementNoOptionSpec
            {
            pushFollow(FOLLOW_elementNoOptionSpec_in_element661);
            elementNoOptionSpec();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "element"



    // $ANTLR start "elementNoOptionSpec"
    // ANTLR3Tree.g:139:1: elementNoOptionSpec : ( ^( ( '=' | '+=' ) ID block ) | ^( ( '=' | '+=' ) ID atom ) | atom | ebnf | ACTION | SEMPRED | GATED_SEMPRED | treeSpec );
    public final void elementNoOptionSpec() throws RecognitionException {
        try {
            // ANTLR3Tree.g:140:2: ( ^( ( '=' | '+=' ) ID block ) | ^( ( '=' | '+=' ) ID atom ) | atom | ebnf | ACTION | SEMPRED | GATED_SEMPRED | treeSpec )
            int alt30=8;
            switch ( input.LA(1) ) {
            case 70:
            case 76:
                {
                int LA30_1 = input.LA(2);

                if ( (LA30_1==DOWN) ) {
                    int LA30_8 = input.LA(3);

                    if ( (LA30_8==ID) ) {
                        int LA30_9 = input.LA(4);

                        if ( (LA30_9==BLOCK) ) {
                            alt30=1;
                        }
                        else if ( (LA30_9==BANG||(LA30_9 >= CHAR_LITERAL && LA30_9 <= CHAR_RANGE)||LA30_9==ROOT||LA30_9==RULE_REF||LA30_9==STRING_LITERAL||LA30_9==TOKEN_REF||LA30_9==72||LA30_9==93) ) {
                            alt30=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 30, 9, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 30, 8, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 30, 1, input);

                    throw nvae;

                }
                }
                break;
            case BANG:
            case CHAR_LITERAL:
            case CHAR_RANGE:
            case ROOT:
            case RULE_REF:
            case STRING_LITERAL:
            case TOKEN_REF:
            case 72:
            case 93:
                {
                alt30=3;
                }
                break;
            case BLOCK:
            case CLOSURE:
            case OPTIONAL:
            case POSITIVE_CLOSURE:
            case SYNPRED:
            case SYN_SEMPRED:
                {
                alt30=4;
                }
                break;
            case ACTION:
                {
                alt30=5;
                }
                break;
            case SEMPRED:
                {
                alt30=6;
                }
                break;
            case GATED_SEMPRED:
                {
                alt30=7;
                }
                break;
            case TREE_BEGIN:
                {
                alt30=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 30, 0, input);

                throw nvae;

            }

            switch (alt30) {
                case 1 :
                    // ANTLR3Tree.g:140:4: ^( ( '=' | '+=' ) ID block )
                    {
                    if ( input.LA(1)==70||input.LA(1)==76 ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    match(input, Token.DOWN, null); 
                    match(input,ID,FOLLOW_ID_in_elementNoOptionSpec679); 

                    pushFollow(FOLLOW_block_in_elementNoOptionSpec681);
                    block();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:141:4: ^( ( '=' | '+=' ) ID atom )
                    {
                    if ( input.LA(1)==70||input.LA(1)==76 ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    match(input, Token.DOWN, null); 
                    match(input,ID,FOLLOW_ID_in_elementNoOptionSpec694); 

                    pushFollow(FOLLOW_atom_in_elementNoOptionSpec696);
                    atom();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    }
                    break;
                case 3 :
                    // ANTLR3Tree.g:142:4: atom
                    {
                    pushFollow(FOLLOW_atom_in_elementNoOptionSpec702);
                    atom();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // ANTLR3Tree.g:143:4: ebnf
                    {
                    pushFollow(FOLLOW_ebnf_in_elementNoOptionSpec707);
                    ebnf();

                    state._fsp--;


                    }
                    break;
                case 5 :
                    // ANTLR3Tree.g:144:6: ACTION
                    {
                    match(input,ACTION,FOLLOW_ACTION_in_elementNoOptionSpec714); 

                    }
                    break;
                case 6 :
                    // ANTLR3Tree.g:145:6: SEMPRED
                    {
                    match(input,SEMPRED,FOLLOW_SEMPRED_in_elementNoOptionSpec721); 

                    }
                    break;
                case 7 :
                    // ANTLR3Tree.g:146:4: GATED_SEMPRED
                    {
                    match(input,GATED_SEMPRED,FOLLOW_GATED_SEMPRED_in_elementNoOptionSpec726); 

                    }
                    break;
                case 8 :
                    // ANTLR3Tree.g:147:6: treeSpec
                    {
                    pushFollow(FOLLOW_treeSpec_in_elementNoOptionSpec733);
                    treeSpec();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "elementNoOptionSpec"



    // $ANTLR start "atom"
    // ANTLR3Tree.g:150:1: atom : ( ^( ( '^' | '!' ) atom ) | range | notSet | ^( RULE_REF ARG_ACTION ) | RULE_REF | terminal );
    public final void atom() throws RecognitionException {
        try {
            // ANTLR3Tree.g:150:5: ( ^( ( '^' | '!' ) atom ) | range | notSet | ^( RULE_REF ARG_ACTION ) | RULE_REF | terminal )
            int alt31=6;
            switch ( input.LA(1) ) {
            case BANG:
            case ROOT:
                {
                alt31=1;
                }
                break;
            case CHAR_RANGE:
                {
                alt31=2;
                }
                break;
            case 93:
                {
                alt31=3;
                }
                break;
            case RULE_REF:
                {
                int LA31_4 = input.LA(2);

                if ( (LA31_4==DOWN) ) {
                    alt31=4;
                }
                else if ( ((LA31_4 >= UP && LA31_4 <= ACTION)||(LA31_4 >= BANG && LA31_4 <= CLOSURE)||LA31_4==EOA||LA31_4==GATED_SEMPRED||LA31_4==OPTIONAL||LA31_4==POSITIVE_CLOSURE||LA31_4==ROOT||LA31_4==RULE_REF||LA31_4==SEMPRED||(LA31_4 >= STRING_LITERAL && LA31_4 <= SYN_SEMPRED)||(LA31_4 >= TOKEN_REF && LA31_4 <= TREE_BEGIN)||LA31_4==70||LA31_4==72||LA31_4==76||LA31_4==93) ) {
                    alt31=5;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 31, 4, input);

                    throw nvae;

                }
                }
                break;
            case CHAR_LITERAL:
            case STRING_LITERAL:
            case TOKEN_REF:
            case 72:
                {
                alt31=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 31, 0, input);

                throw nvae;

            }

            switch (alt31) {
                case 1 :
                    // ANTLR3Tree.g:150:9: ^( ( '^' | '!' ) atom )
                    {
                    if ( input.LA(1)==BANG||input.LA(1)==ROOT ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_atom_in_atom751);
                    atom();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:151:4: range
                    {
                    pushFollow(FOLLOW_range_in_atom757);
                    range();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // ANTLR3Tree.g:152:4: notSet
                    {
                    pushFollow(FOLLOW_notSet_in_atom762);
                    notSet();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // ANTLR3Tree.g:153:7: ^( RULE_REF ARG_ACTION )
                    {
                    match(input,RULE_REF,FOLLOW_RULE_REF_in_atom771); 

                    match(input, Token.DOWN, null); 
                    match(input,ARG_ACTION,FOLLOW_ARG_ACTION_in_atom773); 

                    match(input, Token.UP, null); 


                    }
                    break;
                case 5 :
                    // ANTLR3Tree.g:154:7: RULE_REF
                    {
                    match(input,RULE_REF,FOLLOW_RULE_REF_in_atom782); 

                    }
                    break;
                case 6 :
                    // ANTLR3Tree.g:155:9: terminal
                    {
                    pushFollow(FOLLOW_terminal_in_atom792);
                    terminal();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "atom"



    // $ANTLR start "notSet"
    // ANTLR3Tree.g:158:1: notSet : ( ^( '~' notTerminal ) | ^( '~' block ) );
    public final void notSet() throws RecognitionException {
        try {
            // ANTLR3Tree.g:159:2: ( ^( '~' notTerminal ) | ^( '~' block ) )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==93) ) {
                int LA32_1 = input.LA(2);

                if ( (LA32_1==DOWN) ) {
                    int LA32_2 = input.LA(3);

                    if ( (LA32_2==CHAR_LITERAL||LA32_2==STRING_LITERAL||LA32_2==TOKEN_REF) ) {
                        alt32=1;
                    }
                    else if ( (LA32_2==BLOCK) ) {
                        alt32=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 32, 2, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;

            }
            switch (alt32) {
                case 1 :
                    // ANTLR3Tree.g:159:4: ^( '~' notTerminal )
                    {
                    match(input,93,FOLLOW_93_in_notSet807); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_notTerminal_in_notSet809);
                    notTerminal();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:160:4: ^( '~' block )
                    {
                    match(input,93,FOLLOW_93_in_notSet816); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_block_in_notSet818);
                    block();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "notSet"



    // $ANTLR start "treeSpec"
    // ANTLR3Tree.g:163:1: treeSpec : ^( TREE_BEGIN ( element )+ ) ;
    public final void treeSpec() throws RecognitionException {
        try {
            // ANTLR3Tree.g:164:2: ( ^( TREE_BEGIN ( element )+ ) )
            // ANTLR3Tree.g:164:4: ^( TREE_BEGIN ( element )+ )
            {
            match(input,TREE_BEGIN,FOLLOW_TREE_BEGIN_in_treeSpec831); 

            match(input, Token.DOWN, null); 
            // ANTLR3Tree.g:164:17: ( element )+
            int cnt33=0;
            loop33:
            do {
                int alt33=2;
                int LA33_0 = input.LA(1);

                if ( (LA33_0==ACTION||(LA33_0 >= BANG && LA33_0 <= CLOSURE)||LA33_0==GATED_SEMPRED||LA33_0==OPTIONAL||LA33_0==POSITIVE_CLOSURE||LA33_0==ROOT||LA33_0==RULE_REF||LA33_0==SEMPRED||(LA33_0 >= STRING_LITERAL && LA33_0 <= SYN_SEMPRED)||(LA33_0 >= TOKEN_REF && LA33_0 <= TREE_BEGIN)||LA33_0==70||LA33_0==72||LA33_0==76||LA33_0==93) ) {
                    alt33=1;
                }


                switch (alt33) {
            	case 1 :
            	    // ANTLR3Tree.g:164:17: element
            	    {
            	    pushFollow(FOLLOW_element_in_treeSpec833);
            	    element();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt33 >= 1 ) break loop33;
                        EarlyExitException eee =
                            new EarlyExitException(33, input);
                        throw eee;
                }
                cnt33++;
            } while (true);


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "treeSpec"



    // $ANTLR start "ebnf"
    // ANTLR3Tree.g:168:1: ebnf : ( ^( SYNPRED block ) | SYN_SEMPRED | ^( ebnfSuffix block ) | block );
    public final void ebnf() throws RecognitionException {
        try {
            // ANTLR3Tree.g:169:2: ( ^( SYNPRED block ) | SYN_SEMPRED | ^( ebnfSuffix block ) | block )
            int alt34=4;
            switch ( input.LA(1) ) {
            case SYNPRED:
                {
                alt34=1;
                }
                break;
            case SYN_SEMPRED:
                {
                alt34=2;
                }
                break;
            case CLOSURE:
            case OPTIONAL:
            case POSITIVE_CLOSURE:
                {
                alt34=3;
                }
                break;
            case BLOCK:
                {
                alt34=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 34, 0, input);

                throw nvae;

            }

            switch (alt34) {
                case 1 :
                    // ANTLR3Tree.g:169:4: ^( SYNPRED block )
                    {
                    match(input,SYNPRED,FOLLOW_SYNPRED_in_ebnf849); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_block_in_ebnf851);
                    block();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:170:4: SYN_SEMPRED
                    {
                    match(input,SYN_SEMPRED,FOLLOW_SYN_SEMPRED_in_ebnf857); 

                    }
                    break;
                case 3 :
                    // ANTLR3Tree.g:171:4: ^( ebnfSuffix block )
                    {
                    pushFollow(FOLLOW_ebnfSuffix_in_ebnf863);
                    ebnfSuffix();

                    state._fsp--;


                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_block_in_ebnf865);
                    block();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    }
                    break;
                case 4 :
                    // ANTLR3Tree.g:172:4: block
                    {
                    pushFollow(FOLLOW_block_in_ebnf871);
                    block();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "ebnf"



    // $ANTLR start "range"
    // ANTLR3Tree.g:175:1: range : ^( CHAR_RANGE CHAR_LITERAL CHAR_LITERAL ) ;
    public final void range() throws RecognitionException {
        try {
            // ANTLR3Tree.g:176:2: ( ^( CHAR_RANGE CHAR_LITERAL CHAR_LITERAL ) )
            // ANTLR3Tree.g:176:4: ^( CHAR_RANGE CHAR_LITERAL CHAR_LITERAL )
            {
            match(input,CHAR_RANGE,FOLLOW_CHAR_RANGE_in_range883); 

            match(input, Token.DOWN, null); 
            match(input,CHAR_LITERAL,FOLLOW_CHAR_LITERAL_in_range885); 

            match(input,CHAR_LITERAL,FOLLOW_CHAR_LITERAL_in_range887); 

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "range"



    // $ANTLR start "terminal"
    // ANTLR3Tree.g:179:1: terminal : ( CHAR_LITERAL | TOKEN_REF | STRING_LITERAL | ^( TOKEN_REF ARG_ACTION ) | '.' );
    public final void terminal() throws RecognitionException {
        try {
            // ANTLR3Tree.g:180:5: ( CHAR_LITERAL | TOKEN_REF | STRING_LITERAL | ^( TOKEN_REF ARG_ACTION ) | '.' )
            int alt35=5;
            switch ( input.LA(1) ) {
            case CHAR_LITERAL:
                {
                alt35=1;
                }
                break;
            case TOKEN_REF:
                {
                int LA35_2 = input.LA(2);

                if ( (LA35_2==DOWN) ) {
                    alt35=4;
                }
                else if ( ((LA35_2 >= UP && LA35_2 <= ACTION)||(LA35_2 >= BANG && LA35_2 <= CLOSURE)||LA35_2==EOA||LA35_2==GATED_SEMPRED||LA35_2==OPTIONAL||LA35_2==POSITIVE_CLOSURE||LA35_2==ROOT||LA35_2==RULE_REF||LA35_2==SEMPRED||(LA35_2 >= STRING_LITERAL && LA35_2 <= SYN_SEMPRED)||(LA35_2 >= TOKEN_REF && LA35_2 <= TREE_BEGIN)||LA35_2==70||LA35_2==72||LA35_2==76||LA35_2==93) ) {
                    alt35=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 35, 2, input);

                    throw nvae;

                }
                }
                break;
            case STRING_LITERAL:
                {
                alt35=3;
                }
                break;
            case 72:
                {
                alt35=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 35, 0, input);

                throw nvae;

            }

            switch (alt35) {
                case 1 :
                    // ANTLR3Tree.g:180:9: CHAR_LITERAL
                    {
                    match(input,CHAR_LITERAL,FOLLOW_CHAR_LITERAL_in_terminal904); 

                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:181:7: TOKEN_REF
                    {
                    match(input,TOKEN_REF,FOLLOW_TOKEN_REF_in_terminal912); 

                    }
                    break;
                case 3 :
                    // ANTLR3Tree.g:182:7: STRING_LITERAL
                    {
                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_terminal920); 

                    }
                    break;
                case 4 :
                    // ANTLR3Tree.g:183:7: ^( TOKEN_REF ARG_ACTION )
                    {
                    match(input,TOKEN_REF,FOLLOW_TOKEN_REF_in_terminal929); 

                    match(input, Token.DOWN, null); 
                    match(input,ARG_ACTION,FOLLOW_ARG_ACTION_in_terminal931); 

                    match(input, Token.UP, null); 


                    }
                    break;
                case 5 :
                    // ANTLR3Tree.g:184:7: '.'
                    {
                    match(input,72,FOLLOW_72_in_terminal940); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "terminal"



    // $ANTLR start "notTerminal"
    // ANTLR3Tree.g:187:1: notTerminal : ( CHAR_LITERAL | TOKEN_REF | STRING_LITERAL );
    public final void notTerminal() throws RecognitionException {
        try {
            // ANTLR3Tree.g:188:2: ( CHAR_LITERAL | TOKEN_REF | STRING_LITERAL )
            // ANTLR3Tree.g:
            {
            if ( input.LA(1)==CHAR_LITERAL||input.LA(1)==STRING_LITERAL||input.LA(1)==TOKEN_REF ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "notTerminal"



    // $ANTLR start "ebnfSuffix"
    // ANTLR3Tree.g:193:1: ebnfSuffix : ( OPTIONAL | CLOSURE | POSITIVE_CLOSURE );
    public final void ebnfSuffix() throws RecognitionException {
        try {
            // ANTLR3Tree.g:194:2: ( OPTIONAL | CLOSURE | POSITIVE_CLOSURE )
            // ANTLR3Tree.g:
            {
            if ( input.LA(1)==CLOSURE||input.LA(1)==OPTIONAL||input.LA(1)==POSITIVE_CLOSURE ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "ebnfSuffix"



    // $ANTLR start "rewrite"
    // ANTLR3Tree.g:201:1: rewrite : ( ( ^( '->' SEMPRED rewrite_alternative ) )* ^( '->' rewrite_alternative ) |);
    public final void rewrite() throws RecognitionException {
        try {
            // ANTLR3Tree.g:202:2: ( ( ^( '->' SEMPRED rewrite_alternative ) )* ^( '->' rewrite_alternative ) |)
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==REWRITE) ) {
                alt37=1;
            }
            else if ( (LA37_0==ALT||LA37_0==EOB) ) {
                alt37=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 37, 0, input);

                throw nvae;

            }
            switch (alt37) {
                case 1 :
                    // ANTLR3Tree.g:202:4: ( ^( '->' SEMPRED rewrite_alternative ) )* ^( '->' rewrite_alternative )
                    {
                    // ANTLR3Tree.g:202:4: ( ^( '->' SEMPRED rewrite_alternative ) )*
                    loop36:
                    do {
                        int alt36=2;
                        int LA36_0 = input.LA(1);

                        if ( (LA36_0==REWRITE) ) {
                            int LA36_1 = input.LA(2);

                            if ( (LA36_1==DOWN) ) {
                                int LA36_2 = input.LA(3);

                                if ( (LA36_2==SEMPRED) ) {
                                    alt36=1;
                                }


                            }


                        }


                        switch (alt36) {
                    	case 1 :
                    	    // ANTLR3Tree.g:202:5: ^( '->' SEMPRED rewrite_alternative )
                    	    {
                    	    match(input,REWRITE,FOLLOW_REWRITE_in_rewrite1006); 

                    	    match(input, Token.DOWN, null); 
                    	    match(input,SEMPRED,FOLLOW_SEMPRED_in_rewrite1008); 

                    	    pushFollow(FOLLOW_rewrite_alternative_in_rewrite1010);
                    	    rewrite_alternative();

                    	    state._fsp--;


                    	    match(input, Token.UP, null); 


                    	    }
                    	    break;

                    	default :
                    	    break loop36;
                        }
                    } while (true);


                    match(input,REWRITE,FOLLOW_REWRITE_in_rewrite1016); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_rewrite_alternative_in_rewrite1018);
                    rewrite_alternative();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:204:2: 
                    {
                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rewrite"



    // $ANTLR start "rewrite_alternative"
    // ANTLR3Tree.g:206:1: rewrite_alternative : ( rewrite_template | rewrite_tree_alternative | ^( ALT EPSILON EOA ) );
    public final void rewrite_alternative() throws RecognitionException {
        try {
            // ANTLR3Tree.g:207:2: ( rewrite_template | rewrite_tree_alternative | ^( ALT EPSILON EOA ) )
            int alt38=3;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==ACTION||LA38_0==TEMPLATE) ) {
                alt38=1;
            }
            else if ( (LA38_0==ALT) ) {
                int LA38_2 = input.LA(2);

                if ( (LA38_2==DOWN) ) {
                    int LA38_3 = input.LA(3);

                    if ( (LA38_3==EPSILON) ) {
                        alt38=3;
                    }
                    else if ( (LA38_3==ACTION||(LA38_3 >= BLOCK && LA38_3 <= CHAR_LITERAL)||LA38_3==CLOSURE||LA38_3==LABEL||LA38_3==OPTIONAL||LA38_3==POSITIVE_CLOSURE||LA38_3==RULE_REF||LA38_3==STRING_LITERAL||(LA38_3 >= TOKEN_REF && LA38_3 <= TREE_BEGIN)) ) {
                        alt38=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 38, 3, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 38, 2, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;

            }
            switch (alt38) {
                case 1 :
                    // ANTLR3Tree.g:207:4: rewrite_template
                    {
                    pushFollow(FOLLOW_rewrite_template_in_rewrite_alternative1033);
                    rewrite_template();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:208:4: rewrite_tree_alternative
                    {
                    pushFollow(FOLLOW_rewrite_tree_alternative_in_rewrite_alternative1038);
                    rewrite_tree_alternative();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // ANTLR3Tree.g:209:9: ^( ALT EPSILON EOA )
                    {
                    match(input,ALT,FOLLOW_ALT_in_rewrite_alternative1049); 

                    match(input, Token.DOWN, null); 
                    match(input,EPSILON,FOLLOW_EPSILON_in_rewrite_alternative1051); 

                    match(input,EOA,FOLLOW_EOA_in_rewrite_alternative1053); 

                    match(input, Token.UP, null); 


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rewrite_alternative"



    // $ANTLR start "rewrite_tree_block"
    // ANTLR3Tree.g:212:1: rewrite_tree_block : ^( BLOCK rewrite_tree_alternative EOB ) ;
    public final void rewrite_tree_block() throws RecognitionException {
        try {
            // ANTLR3Tree.g:213:5: ( ^( BLOCK rewrite_tree_alternative EOB ) )
            // ANTLR3Tree.g:213:9: ^( BLOCK rewrite_tree_alternative EOB )
            {
            match(input,BLOCK,FOLLOW_BLOCK_in_rewrite_tree_block1072); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_rewrite_tree_alternative_in_rewrite_tree_block1074);
            rewrite_tree_alternative();

            state._fsp--;


            match(input,EOB,FOLLOW_EOB_in_rewrite_tree_block1076); 

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rewrite_tree_block"



    // $ANTLR start "rewrite_tree_alternative"
    // ANTLR3Tree.g:216:1: rewrite_tree_alternative : ^( ALT ( rewrite_tree_element )+ EOA ) ;
    public final void rewrite_tree_alternative() throws RecognitionException {
        try {
            // ANTLR3Tree.g:217:5: ( ^( ALT ( rewrite_tree_element )+ EOA ) )
            // ANTLR3Tree.g:217:7: ^( ALT ( rewrite_tree_element )+ EOA )
            {
            match(input,ALT,FOLLOW_ALT_in_rewrite_tree_alternative1095); 

            match(input, Token.DOWN, null); 
            // ANTLR3Tree.g:217:13: ( rewrite_tree_element )+
            int cnt39=0;
            loop39:
            do {
                int alt39=2;
                int LA39_0 = input.LA(1);

                if ( (LA39_0==ACTION||(LA39_0 >= BLOCK && LA39_0 <= CHAR_LITERAL)||LA39_0==CLOSURE||LA39_0==LABEL||LA39_0==OPTIONAL||LA39_0==POSITIVE_CLOSURE||LA39_0==RULE_REF||LA39_0==STRING_LITERAL||(LA39_0 >= TOKEN_REF && LA39_0 <= TREE_BEGIN)) ) {
                    alt39=1;
                }


                switch (alt39) {
            	case 1 :
            	    // ANTLR3Tree.g:217:13: rewrite_tree_element
            	    {
            	    pushFollow(FOLLOW_rewrite_tree_element_in_rewrite_tree_alternative1097);
            	    rewrite_tree_element();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt39 >= 1 ) break loop39;
                        EarlyExitException eee =
                            new EarlyExitException(39, input);
                        throw eee;
                }
                cnt39++;
            } while (true);


            match(input,EOA,FOLLOW_EOA_in_rewrite_tree_alternative1100); 

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rewrite_tree_alternative"



    // $ANTLR start "rewrite_tree_element"
    // ANTLR3Tree.g:220:1: rewrite_tree_element : ( rewrite_tree_atom | rewrite_tree | rewrite_tree_block | rewrite_tree_ebnf );
    public final void rewrite_tree_element() throws RecognitionException {
        try {
            // ANTLR3Tree.g:221:2: ( rewrite_tree_atom | rewrite_tree | rewrite_tree_block | rewrite_tree_ebnf )
            int alt40=4;
            switch ( input.LA(1) ) {
            case ACTION:
            case CHAR_LITERAL:
            case LABEL:
            case RULE_REF:
            case STRING_LITERAL:
            case TOKEN_REF:
                {
                alt40=1;
                }
                break;
            case TREE_BEGIN:
                {
                alt40=2;
                }
                break;
            case BLOCK:
                {
                alt40=3;
                }
                break;
            case CLOSURE:
            case OPTIONAL:
            case POSITIVE_CLOSURE:
                {
                alt40=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 40, 0, input);

                throw nvae;

            }

            switch (alt40) {
                case 1 :
                    // ANTLR3Tree.g:221:4: rewrite_tree_atom
                    {
                    pushFollow(FOLLOW_rewrite_tree_atom_in_rewrite_tree_element1115);
                    rewrite_tree_atom();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:222:4: rewrite_tree
                    {
                    pushFollow(FOLLOW_rewrite_tree_in_rewrite_tree_element1120);
                    rewrite_tree();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // ANTLR3Tree.g:223:6: rewrite_tree_block
                    {
                    pushFollow(FOLLOW_rewrite_tree_block_in_rewrite_tree_element1127);
                    rewrite_tree_block();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // ANTLR3Tree.g:224:6: rewrite_tree_ebnf
                    {
                    pushFollow(FOLLOW_rewrite_tree_ebnf_in_rewrite_tree_element1134);
                    rewrite_tree_ebnf();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rewrite_tree_element"



    // $ANTLR start "rewrite_tree_atom"
    // ANTLR3Tree.g:227:1: rewrite_tree_atom : ( CHAR_LITERAL | TOKEN_REF | ^( TOKEN_REF ARG_ACTION ) | RULE_REF | STRING_LITERAL | LABEL | ACTION );
    public final void rewrite_tree_atom() throws RecognitionException {
        try {
            // ANTLR3Tree.g:228:5: ( CHAR_LITERAL | TOKEN_REF | ^( TOKEN_REF ARG_ACTION ) | RULE_REF | STRING_LITERAL | LABEL | ACTION )
            int alt41=7;
            switch ( input.LA(1) ) {
            case CHAR_LITERAL:
                {
                alt41=1;
                }
                break;
            case TOKEN_REF:
                {
                int LA41_2 = input.LA(2);

                if ( (LA41_2==DOWN) ) {
                    alt41=3;
                }
                else if ( ((LA41_2 >= UP && LA41_2 <= ACTION)||(LA41_2 >= BLOCK && LA41_2 <= CHAR_LITERAL)||LA41_2==CLOSURE||LA41_2==EOA||LA41_2==LABEL||LA41_2==OPTIONAL||LA41_2==POSITIVE_CLOSURE||LA41_2==RULE_REF||LA41_2==STRING_LITERAL||(LA41_2 >= TOKEN_REF && LA41_2 <= TREE_BEGIN)) ) {
                    alt41=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 41, 2, input);

                    throw nvae;

                }
                }
                break;
            case RULE_REF:
                {
                alt41=4;
                }
                break;
            case STRING_LITERAL:
                {
                alt41=5;
                }
                break;
            case LABEL:
                {
                alt41=6;
                }
                break;
            case ACTION:
                {
                alt41=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 41, 0, input);

                throw nvae;

            }

            switch (alt41) {
                case 1 :
                    // ANTLR3Tree.g:228:9: CHAR_LITERAL
                    {
                    match(input,CHAR_LITERAL,FOLLOW_CHAR_LITERAL_in_rewrite_tree_atom1150); 

                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:229:6: TOKEN_REF
                    {
                    match(input,TOKEN_REF,FOLLOW_TOKEN_REF_in_rewrite_tree_atom1157); 

                    }
                    break;
                case 3 :
                    // ANTLR3Tree.g:230:6: ^( TOKEN_REF ARG_ACTION )
                    {
                    match(input,TOKEN_REF,FOLLOW_TOKEN_REF_in_rewrite_tree_atom1165); 

                    match(input, Token.DOWN, null); 
                    match(input,ARG_ACTION,FOLLOW_ARG_ACTION_in_rewrite_tree_atom1167); 

                    match(input, Token.UP, null); 


                    }
                    break;
                case 4 :
                    // ANTLR3Tree.g:231:9: RULE_REF
                    {
                    match(input,RULE_REF,FOLLOW_RULE_REF_in_rewrite_tree_atom1179); 

                    }
                    break;
                case 5 :
                    // ANTLR3Tree.g:232:6: STRING_LITERAL
                    {
                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_rewrite_tree_atom1186); 

                    }
                    break;
                case 6 :
                    // ANTLR3Tree.g:233:6: LABEL
                    {
                    match(input,LABEL,FOLLOW_LABEL_in_rewrite_tree_atom1193); 

                    }
                    break;
                case 7 :
                    // ANTLR3Tree.g:234:4: ACTION
                    {
                    match(input,ACTION,FOLLOW_ACTION_in_rewrite_tree_atom1198); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rewrite_tree_atom"



    // $ANTLR start "rewrite_tree_ebnf"
    // ANTLR3Tree.g:237:1: rewrite_tree_ebnf : ^( ebnfSuffix rewrite_tree_block ) ;
    public final void rewrite_tree_ebnf() throws RecognitionException {
        try {
            // ANTLR3Tree.g:238:2: ( ^( ebnfSuffix rewrite_tree_block ) )
            // ANTLR3Tree.g:238:4: ^( ebnfSuffix rewrite_tree_block )
            {
            pushFollow(FOLLOW_ebnfSuffix_in_rewrite_tree_ebnf1210);
            ebnfSuffix();

            state._fsp--;


            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_rewrite_tree_block_in_rewrite_tree_ebnf1212);
            rewrite_tree_block();

            state._fsp--;


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rewrite_tree_ebnf"



    // $ANTLR start "rewrite_tree"
    // ANTLR3Tree.g:241:1: rewrite_tree : ^( TREE_BEGIN rewrite_tree_atom ( rewrite_tree_element )* ) ;
    public final void rewrite_tree() throws RecognitionException {
        try {
            // ANTLR3Tree.g:242:2: ( ^( TREE_BEGIN rewrite_tree_atom ( rewrite_tree_element )* ) )
            // ANTLR3Tree.g:242:4: ^( TREE_BEGIN rewrite_tree_atom ( rewrite_tree_element )* )
            {
            match(input,TREE_BEGIN,FOLLOW_TREE_BEGIN_in_rewrite_tree1226); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_rewrite_tree_atom_in_rewrite_tree1228);
            rewrite_tree_atom();

            state._fsp--;


            // ANTLR3Tree.g:242:35: ( rewrite_tree_element )*
            loop42:
            do {
                int alt42=2;
                int LA42_0 = input.LA(1);

                if ( (LA42_0==ACTION||(LA42_0 >= BLOCK && LA42_0 <= CHAR_LITERAL)||LA42_0==CLOSURE||LA42_0==LABEL||LA42_0==OPTIONAL||LA42_0==POSITIVE_CLOSURE||LA42_0==RULE_REF||LA42_0==STRING_LITERAL||(LA42_0 >= TOKEN_REF && LA42_0 <= TREE_BEGIN)) ) {
                    alt42=1;
                }


                switch (alt42) {
            	case 1 :
            	    // ANTLR3Tree.g:242:35: rewrite_tree_element
            	    {
            	    pushFollow(FOLLOW_rewrite_tree_element_in_rewrite_tree1230);
            	    rewrite_tree_element();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop42;
                }
            } while (true);


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rewrite_tree"



    // $ANTLR start "rewrite_template"
    // ANTLR3Tree.g:245:1: rewrite_template : ( ^( TEMPLATE ID rewrite_template_args ( DOUBLE_QUOTE_STRING_LITERAL | DOUBLE_ANGLE_STRING_LITERAL ) ) | rewrite_template_ref | rewrite_indirect_template_head | ACTION );
    public final void rewrite_template() throws RecognitionException {
        try {
            // ANTLR3Tree.g:246:2: ( ^( TEMPLATE ID rewrite_template_args ( DOUBLE_QUOTE_STRING_LITERAL | DOUBLE_ANGLE_STRING_LITERAL ) ) | rewrite_template_ref | rewrite_indirect_template_head | ACTION )
            int alt43=4;
            alt43 = dfa43.predict(input);
            switch (alt43) {
                case 1 :
                    // ANTLR3Tree.g:246:6: ^( TEMPLATE ID rewrite_template_args ( DOUBLE_QUOTE_STRING_LITERAL | DOUBLE_ANGLE_STRING_LITERAL ) )
                    {
                    match(input,TEMPLATE,FOLLOW_TEMPLATE_in_rewrite_template1248); 

                    match(input, Token.DOWN, null); 
                    match(input,ID,FOLLOW_ID_in_rewrite_template1250); 

                    pushFollow(FOLLOW_rewrite_template_args_in_rewrite_template1252);
                    rewrite_template_args();

                    state._fsp--;


                    if ( (input.LA(1) >= DOUBLE_ANGLE_STRING_LITERAL && input.LA(1) <= DOUBLE_QUOTE_STRING_LITERAL) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:249:4: rewrite_template_ref
                    {
                    pushFollow(FOLLOW_rewrite_template_ref_in_rewrite_template1275);
                    rewrite_template_ref();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // ANTLR3Tree.g:250:4: rewrite_indirect_template_head
                    {
                    pushFollow(FOLLOW_rewrite_indirect_template_head_in_rewrite_template1280);
                    rewrite_indirect_template_head();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // ANTLR3Tree.g:251:4: ACTION
                    {
                    match(input,ACTION,FOLLOW_ACTION_in_rewrite_template1285); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rewrite_template"



    // $ANTLR start "rewrite_template_ref"
    // ANTLR3Tree.g:255:1: rewrite_template_ref : ^( TEMPLATE ID rewrite_template_args ) ;
    public final void rewrite_template_ref() throws RecognitionException {
        try {
            // ANTLR3Tree.g:256:2: ( ^( TEMPLATE ID rewrite_template_args ) )
            // ANTLR3Tree.g:256:4: ^( TEMPLATE ID rewrite_template_args )
            {
            match(input,TEMPLATE,FOLLOW_TEMPLATE_in_rewrite_template_ref1299); 

            match(input, Token.DOWN, null); 
            match(input,ID,FOLLOW_ID_in_rewrite_template_ref1301); 

            pushFollow(FOLLOW_rewrite_template_args_in_rewrite_template_ref1303);
            rewrite_template_args();

            state._fsp--;


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rewrite_template_ref"



    // $ANTLR start "rewrite_indirect_template_head"
    // ANTLR3Tree.g:260:1: rewrite_indirect_template_head : ^( TEMPLATE ACTION rewrite_template_args ) ;
    public final void rewrite_indirect_template_head() throws RecognitionException {
        try {
            // ANTLR3Tree.g:261:2: ( ^( TEMPLATE ACTION rewrite_template_args ) )
            // ANTLR3Tree.g:261:4: ^( TEMPLATE ACTION rewrite_template_args )
            {
            match(input,TEMPLATE,FOLLOW_TEMPLATE_in_rewrite_indirect_template_head1318); 

            match(input, Token.DOWN, null); 
            match(input,ACTION,FOLLOW_ACTION_in_rewrite_indirect_template_head1320); 

            pushFollow(FOLLOW_rewrite_template_args_in_rewrite_indirect_template_head1322);
            rewrite_template_args();

            state._fsp--;


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rewrite_indirect_template_head"



    // $ANTLR start "rewrite_template_args"
    // ANTLR3Tree.g:264:1: rewrite_template_args : ( ^( ARGLIST ( rewrite_template_arg )+ ) | ARGLIST );
    public final void rewrite_template_args() throws RecognitionException {
        try {
            // ANTLR3Tree.g:265:2: ( ^( ARGLIST ( rewrite_template_arg )+ ) | ARGLIST )
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==ARGLIST) ) {
                int LA45_1 = input.LA(2);

                if ( (LA45_1==DOWN) ) {
                    alt45=1;
                }
                else if ( (LA45_1==UP||(LA45_1 >= DOUBLE_ANGLE_STRING_LITERAL && LA45_1 <= DOUBLE_QUOTE_STRING_LITERAL)) ) {
                    alt45=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 45, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 45, 0, input);

                throw nvae;

            }
            switch (alt45) {
                case 1 :
                    // ANTLR3Tree.g:265:4: ^( ARGLIST ( rewrite_template_arg )+ )
                    {
                    match(input,ARGLIST,FOLLOW_ARGLIST_in_rewrite_template_args1335); 

                    match(input, Token.DOWN, null); 
                    // ANTLR3Tree.g:265:14: ( rewrite_template_arg )+
                    int cnt44=0;
                    loop44:
                    do {
                        int alt44=2;
                        int LA44_0 = input.LA(1);

                        if ( (LA44_0==ARG) ) {
                            alt44=1;
                        }


                        switch (alt44) {
                    	case 1 :
                    	    // ANTLR3Tree.g:265:14: rewrite_template_arg
                    	    {
                    	    pushFollow(FOLLOW_rewrite_template_arg_in_rewrite_template_args1337);
                    	    rewrite_template_arg();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt44 >= 1 ) break loop44;
                                EarlyExitException eee =
                                    new EarlyExitException(44, input);
                                throw eee;
                        }
                        cnt44++;
                    } while (true);


                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // ANTLR3Tree.g:266:4: ARGLIST
                    {
                    match(input,ARGLIST,FOLLOW_ARGLIST_in_rewrite_template_args1344); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rewrite_template_args"



    // $ANTLR start "rewrite_template_arg"
    // ANTLR3Tree.g:269:1: rewrite_template_arg : ^( ARG ID ACTION ) ;
    public final void rewrite_template_arg() throws RecognitionException {
        try {
            // ANTLR3Tree.g:270:2: ( ^( ARG ID ACTION ) )
            // ANTLR3Tree.g:270:6: ^( ARG ID ACTION )
            {
            match(input,ARG,FOLLOW_ARG_in_rewrite_template_arg1358); 

            match(input, Token.DOWN, null); 
            match(input,ID,FOLLOW_ID_in_rewrite_template_arg1360); 

            match(input,ACTION,FOLLOW_ACTION_in_rewrite_template_arg1362); 

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "rewrite_template_arg"

    // Delegated rules


    protected DFA43 dfa43 = new DFA43(this);
    static final String DFA43_eotS =
        "\20\uffff";
    static final String DFA43_eofS =
        "\20\uffff";
    static final String DFA43_minS =
        "\1\4\1\2\1\uffff\1\4\1\12\1\uffff\1\2\1\11\2\uffff\1\2\1\35\1\4"+
        "\3\3";
    static final String DFA43_maxS =
        "\1\71\1\2\1\uffff\1\35\1\12\1\uffff\1\25\1\11\2\uffff\1\2\1\35\1"+
        "\4\1\3\1\11\1\25";
    static final String DFA43_acceptS =
        "\2\uffff\1\4\2\uffff\1\3\2\uffff\1\1\1\2\6\uffff";
    static final String DFA43_specialS =
        "\20\uffff}>";
    static final String[] DFA43_transitionS = {
            "\1\2\64\uffff\1\1",
            "\1\3",
            "",
            "\1\5\30\uffff\1\4",
            "\1\6",
            "",
            "\1\7\1\11\20\uffff\2\10",
            "\1\12",
            "",
            "",
            "\1\13",
            "\1\14",
            "\1\15",
            "\1\16",
            "\1\17\5\uffff\1\12",
            "\1\11\20\uffff\2\10"
    };

    static final short[] DFA43_eot = DFA.unpackEncodedString(DFA43_eotS);
    static final short[] DFA43_eof = DFA.unpackEncodedString(DFA43_eofS);
    static final char[] DFA43_min = DFA.unpackEncodedStringToUnsignedChars(DFA43_minS);
    static final char[] DFA43_max = DFA.unpackEncodedStringToUnsignedChars(DFA43_maxS);
    static final short[] DFA43_accept = DFA.unpackEncodedString(DFA43_acceptS);
    static final short[] DFA43_special = DFA.unpackEncodedString(DFA43_specialS);
    static final short[][] DFA43_transition;

    static {
        int numStates = DFA43_transitionS.length;
        DFA43_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA43_transition[i] = DFA.unpackEncodedString(DFA43_transitionS[i]);
        }
    }

    class DFA43 extends DFA {

        public DFA43(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 43;
            this.eot = DFA43_eot;
            this.eof = DFA43_eof;
            this.min = DFA43_min;
            this.max = DFA43_max;
            this.accept = DFA43_accept;
            this.special = DFA43_special;
            this.transition = DFA43_transition;
        }
        public String getDescription() {
            return "245:1: rewrite_template : ( ^( TEMPLATE ID rewrite_template_args ( DOUBLE_QUOTE_STRING_LITERAL | DOUBLE_ANGLE_STRING_LITERAL ) ) | rewrite_template_ref | rewrite_indirect_template_head | ACTION );";
        }
    }
 

    public static final BitSet FOLLOW_grammarType_in_grammarDef46 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_grammarDef48 = new BitSet(new long[]{0x0405010000080000L,0x0000000000008000L});
    public static final BitSet FOLLOW_DOC_COMMENT_in_grammarDef50 = new BitSet(new long[]{0x0405010000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_optionsSpec_in_grammarDef53 = new BitSet(new long[]{0x0405000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_tokensSpec_in_grammarDef56 = new BitSet(new long[]{0x0005000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_attrScope_in_grammarDef59 = new BitSet(new long[]{0x0005000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_action_in_grammarDef62 = new BitSet(new long[]{0x0001000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_rule_in_grammarDef65 = new BitSet(new long[]{0x0001000000000008L});
    public static final BitSet FOLLOW_TOKENS_in_tokensSpec121 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_tokenSpec_in_tokensSpec123 = new BitSet(new long[]{0x0800000000000008L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_tokenSpec137 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_TOKEN_REF_in_tokenSpec139 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_tokenSpec141 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_76_in_tokenSpec148 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_TOKEN_REF_in_tokenSpec150 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_CHAR_LITERAL_in_tokenSpec152 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_TOKEN_REF_in_tokenSpec158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SCOPE_in_attrScope170 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_attrScope172 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_attrScope174 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_79_in_action187 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_action189 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_ID_in_action191 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_action193 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_79_in_action200 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_action202 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_action204 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_OPTIONS_in_optionsSpec217 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_option_in_optionsSpec219 = new BitSet(new long[]{0x0000000000000008L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_option238 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_option240 = new BitSet(new long[]{0x00400000A0008000L});
    public static final BitSet FOLLOW_optionValue_in_option242 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RULE_in_rule308 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_rule310 = new BitSet(new long[]{0x0004210008004200L,0x0000000000E08000L});
    public static final BitSet FOLLOW_modifier_in_rule312 = new BitSet(new long[]{0x0004210000004200L,0x0000000000008000L});
    public static final BitSet FOLLOW_ARG_in_rule317 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ARG_ACTION_in_rule319 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RET_in_rule326 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ARG_ACTION_in_rule328 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_optionsSpec_in_rule341 = new BitSet(new long[]{0x0004000000004000L,0x0000000000008000L});
    public static final BitSet FOLLOW_ruleScopeSpec_in_rule344 = new BitSet(new long[]{0x0000000000004000L,0x0000000000008000L});
    public static final BitSet FOLLOW_ruleAction_in_rule347 = new BitSet(new long[]{0x0000000000004000L,0x0000000000008000L});
    public static final BitSet FOLLOW_altList_in_rule358 = new BitSet(new long[]{0x0000000001000000L,0x0000000000030000L});
    public static final BitSet FOLLOW_exceptionGroup_in_rule368 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_EOR_in_rule371 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_79_in_ruleAction410 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_ruleAction412 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_ruleAction414 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_89_in_throwsSpec427 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_throwsSpec429 = new BitSet(new long[]{0x0000000020000008L});
    public static final BitSet FOLLOW_SCOPE_in_ruleScopeSpec443 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ACTION_in_ruleScopeSpec445 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SCOPE_in_ruleScopeSpec452 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ACTION_in_ruleScopeSpec454 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_ID_in_ruleScopeSpec456 = new BitSet(new long[]{0x0000000020000008L});
    public static final BitSet FOLLOW_SCOPE_in_ruleScopeSpec464 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_ruleScopeSpec466 = new BitSet(new long[]{0x0000000020000008L});
    public static final BitSet FOLLOW_BLOCK_in_block486 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_optionsSpec_in_block488 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_alternative_in_block492 = new BitSet(new long[]{0x0000400000800100L});
    public static final BitSet FOLLOW_rewrite_in_block494 = new BitSet(new long[]{0x0000000000800100L});
    public static final BitSet FOLLOW_EOB_in_block498 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_BLOCK_in_altList521 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_alternative_in_altList524 = new BitSet(new long[]{0x0000400000800100L});
    public static final BitSet FOLLOW_rewrite_in_altList526 = new BitSet(new long[]{0x0000000000800100L});
    public static final BitSet FOLLOW_EOB_in_altList530 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ALT_in_alternative552 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_element_in_alternative554 = new BitSet(new long[]{0x19CA88801043E010L,0x0000000020001140L});
    public static final BitSet FOLLOW_EOA_in_alternative557 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ALT_in_alternative569 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_EPSILON_in_alternative571 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_EOA_in_alternative573 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_exceptionHandler_in_exceptionGroup588 = new BitSet(new long[]{0x0000000000000002L,0x0000000000030000L});
    public static final BitSet FOLLOW_finallyClause_in_exceptionGroup591 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_finallyClause_in_exceptionGroup597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_80_in_exceptionHandler618 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ARG_ACTION_in_exceptionHandler620 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_exceptionHandler622 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_81_in_finallyClause644 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ACTION_in_finallyClause646 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_elementNoOptionSpec_in_element661 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_elementNoOptionSpec673 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_elementNoOptionSpec679 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_block_in_elementNoOptionSpec681 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_set_in_elementNoOptionSpec688 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_elementNoOptionSpec694 = new BitSet(new long[]{0x084280000001A000L,0x0000000020000100L});
    public static final BitSet FOLLOW_atom_in_elementNoOptionSpec696 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_atom_in_elementNoOptionSpec702 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ebnf_in_elementNoOptionSpec707 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACTION_in_elementNoOptionSpec714 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMPRED_in_elementNoOptionSpec721 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GATED_SEMPRED_in_elementNoOptionSpec726 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_treeSpec_in_elementNoOptionSpec733 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_atom745 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_atom_in_atom751 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_range_in_atom757 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_notSet_in_atom762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_REF_in_atom771 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ARG_ACTION_in_atom773 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RULE_REF_in_atom782 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_terminal_in_atom792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_93_in_notSet807 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_notTerminal_in_notSet809 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_93_in_notSet816 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_block_in_notSet818 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_TREE_BEGIN_in_treeSpec831 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_element_in_treeSpec833 = new BitSet(new long[]{0x19CA88801003E018L,0x0000000020001140L});
    public static final BitSet FOLLOW_SYNPRED_in_ebnf849 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_block_in_ebnf851 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SYN_SEMPRED_in_ebnf857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ebnfSuffix_in_ebnf863 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_block_in_ebnf865 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_block_in_ebnf871 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CHAR_RANGE_in_range883 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_CHAR_LITERAL_in_range885 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_CHAR_LITERAL_in_range887 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CHAR_LITERAL_in_terminal904 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_REF_in_terminal912 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_terminal920 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_REF_in_terminal929 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ARG_ACTION_in_terminal931 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_72_in_terminal940 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REWRITE_in_rewrite1006 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_SEMPRED_in_rewrite1008 = new BitSet(new long[]{0x0200000000000110L});
    public static final BitSet FOLLOW_rewrite_alternative_in_rewrite1010 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_REWRITE_in_rewrite1016 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_rewrite_alternative_in_rewrite1018 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_rewrite_template_in_rewrite_alternative1033 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_tree_alternative_in_rewrite_alternative1038 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALT_in_rewrite_alternative1049 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_EPSILON_in_rewrite_alternative1051 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_EOA_in_rewrite_alternative1053 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_BLOCK_in_rewrite_tree_block1072 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_rewrite_tree_alternative_in_rewrite_tree_block1074 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_EOB_in_rewrite_tree_block1076 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ALT_in_rewrite_tree_alternative1095 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_rewrite_tree_element_in_rewrite_tree_alternative1097 = new BitSet(new long[]{0x184208810042C010L});
    public static final BitSet FOLLOW_EOA_in_rewrite_tree_alternative1100 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_rewrite_tree_atom_in_rewrite_tree_element1115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_tree_in_rewrite_tree_element1120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_tree_block_in_rewrite_tree_element1127 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_tree_ebnf_in_rewrite_tree_element1134 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CHAR_LITERAL_in_rewrite_tree_atom1150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_REF_in_rewrite_tree_atom1157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_REF_in_rewrite_tree_atom1165 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ARG_ACTION_in_rewrite_tree_atom1167 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RULE_REF_in_rewrite_tree_atom1179 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_rewrite_tree_atom1186 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LABEL_in_rewrite_tree_atom1193 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACTION_in_rewrite_tree_atom1198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ebnfSuffix_in_rewrite_tree_ebnf1210 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_rewrite_tree_block_in_rewrite_tree_ebnf1212 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_TREE_BEGIN_in_rewrite_tree1226 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_rewrite_tree_atom_in_rewrite_tree1228 = new BitSet(new long[]{0x184208810002C018L});
    public static final BitSet FOLLOW_rewrite_tree_element_in_rewrite_tree1230 = new BitSet(new long[]{0x184208810002C018L});
    public static final BitSet FOLLOW_TEMPLATE_in_rewrite_template1248 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_rewrite_template1250 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_rewrite_template_args_in_rewrite_template1252 = new BitSet(new long[]{0x0000000000300000L});
    public static final BitSet FOLLOW_set_in_rewrite_template1259 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_rewrite_template_ref_in_rewrite_template1275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_indirect_template_head_in_rewrite_template1280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACTION_in_rewrite_template1285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TEMPLATE_in_rewrite_template_ref1299 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_rewrite_template_ref1301 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_rewrite_template_args_in_rewrite_template_ref1303 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_TEMPLATE_in_rewrite_indirect_template_head1318 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ACTION_in_rewrite_indirect_template_head1320 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_rewrite_template_args_in_rewrite_indirect_template_head1322 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ARGLIST_in_rewrite_template_args1335 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_rewrite_template_arg_in_rewrite_template_args1337 = new BitSet(new long[]{0x0000000000000208L});
    public static final BitSet FOLLOW_ARGLIST_in_rewrite_template_args1344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARG_in_rewrite_template_arg1358 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_rewrite_template_arg1360 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_rewrite_template_arg1362 = new BitSet(new long[]{0x0000000000000008L});

}