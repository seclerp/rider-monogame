/**
* A Flex lexer definition of MGCB .tpg grammar from HLSL2GL translator.
* Please keep in sync with https://github.com/SickheadGames/HL2GLSL/blob/master/parser/hlsl.grammar
*/

package me.seclerp.rider.plugins.monogame.effect;

import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbTypes;
import com.intellij.psi.TokenType;

%%

%{
    public EffectLexer() {
        this((java.io.Reader)null);
    }
%}

%{}
    /**
    * Dedicated storage for starting position of some previously successful
    * match
    */
    private int zzPostponedMarkedPos = -1;

    /**
    * Dedicated nested-comment level counter
    */
    private int zzNestedCommentLevel = 0;
%}

%{
    IElementType imbueBlockComment() {
        assert(zzNestedCommentLevel == 0);
        yybegin(YYINITIAL);

        zzStartRead = zzPostponedMarkedPos;
        zzPostponedMarkedPos = -1;

        if (yylength() >= 3) {
            if (yycharat(2) == '!') {
                return INNER_BLOCK_DOC_COMMENT;
            } else if (yycharat(2) == '*' && (yylength() == 3 || yycharat(3) != '*' && yycharat(3) != '/')) {
                return OUTER_BLOCK_DOC_COMMENT;
            }
        }

        return BLOCK_COMMENT;
    }
%}

%public
%class EffectLexer
%implements FlexLexer
%function advance
%type IElementType
%eof{  return;
%eof}

%caseless
%unicode

//
// Tokens

ADD                          = "+"
SUB                          = "-"
MULT                         = "*"
DIV                          = "/"
MOD                          = "%"

ADD_ADD                      = "++"
SUB_SUB                      = "--"

EQUAL						 = "="
ADD_EQUAL 					 = "+="
SUB_EQUAL 					 = "-="
MULT_EQUAL 					 = "*="
DIV_EQUAL 					 = "/="
MOD_EQUAL 					 = "%="
MINOR_MINOR_EQUAL			 = "<<="
MAJOR_MAJOR_EQUAL			 = ">>="
AND_EQUAL					 = "&="
OR_EQUAL					 = "|="
POT_EQUAL					 = "^="


EQUAL_EQUAL 				 = "=="
DIFF						 = "!="
LESS_EQUAL 					 = "<="
MORE_EQUAL					 = ">="

TIL 						 = "~"
MINOR_MINOR					 = "<<"
MAJOR_MAJOR					 = ">>"
AND							 = "&"
OR							 = "|"
POT							 = "^"
AND_AND						 = "&&"
OR_OR						 = "||"
NOT							 = "!"

MAJOR        	             = ">"
MINOR		                 = "<"
OPEN_PAREN                   = "("
CLOSE_PAREN                  = ")"
OPEN_BREACKET                = "{"
CLOSE_BREACKET               = "}"
OPEN_COLCHETES               = "["
CLOSE_COLCHETES              = "]"
DOUBLE_DOT					 = ":"
DOT_COMMA					 = ";"
COMMA					 	 = ","
DOT							 = "."
NUMBER                       = [0-9]+ | [0-9]* \. [0-9]+ (E[0-9]+)? (f)?
NUMBER_2_4                   = [2-4]
NUMBER_1_4                   = [1-4]
WHITESPACE                   = [ ]+
TAB		                     = [\t]
NEWLINE                      = [\n]
NEWLINE2                     = [\r\n]
NEWLINE3                     = [\n\r]
FORMFEED                     = [\r]

STRING              		 = (".*[^\\]")|('.*[^\\]')
RGBA						 = [rgba]+
XYZW						 = [xyzw]+

// KEYWORDS

ASM 				= "asm"
ASM_FRAGMENT 		= "asm_fragment"
BLENDSTATE 			= "BlendState"
COLUMN_MAJOR 		= "column_major" // column in a single register
COMPILE_FRAGMENT 	= "compile_frgment"
DISCARD 			= "discard"
DECL 				= "decl"
DO 			 		= "do"
ELSE 				= "else"
EXTERN 				= "extern"
END 				= "end"
FALSE 				= "false"
FOR 				= "for"
IF 					= "if"
IN 					= "in"
INLINE 				= "inline"
INOUT 				= "inout"
MATRIX 				= "matrix"
OUT 				= "out"
PACKOFFSET			= "packoffset"
PASS 				= "pass"
PIXELFRAGMENT 		= "pixelfragment"
REGISTER 			= "register"
RETURN 				= "return"
ROW_MAJOR  			= "row_major" // row in a single register
SAMPLER 			= "sampler"
SAMPLER1D 			= "sampler1D"
SAMPLER2D 			= "sampler2D"
SAMPLER3D 			= "sampler3D"
SAMPLERCUBE 		= "samplerCUBE"
SAMPLER_STATE 		= "sampler_state"
SHARED 				= "shared"
STATEBLOCK 			= "stateblock"
STATEBLOCK_STATE 	= "stateblock_state"
STATIC 				= "static"
STRING_TYPE			= "string"
STRUCT 				= "struct"
TECHNIQUE 			= "technique"
TEXTURE 			= "texture"
TEXTURE1D 			= "texture1D"
TEXTURE2D 			= "texture2D"
TEXTURE3D 			= "texture3D"
TEXTURECUBE 		= "textureCUBE"
TRUE 				= "true"
TYPEDEF 			= "typedef"
UNIFORM 			= "uniform"
VARYING				= "varying"
VECTOR 				= "vector"
VERTEXFRAGMENT 		= "vertexfragment"
VOID 				= "void"
VOLATILE 			= "volatile"
WHILE 	 			= "while"

// Other words.

STOP	 			= "stop"

FLATTEN	 			= "flatten"
BRANCH	 			= "branch"

UNROLL				= "unroll"
LOOP				= "loop"

FORCECASE 			= "forcecase"
CALL 				= "call"

// Reserved Words (UNUSED)

AUTO 			 	= "auto"
BREAK 				= "break"
COMPILE				= "compile"
CONST  				= "const"
CHAR  				= "char"
CLASS 				= "class"
CASE 				= "case"
CATCH 				= "catch"
DEFAULT 			= "default"
DELETE 				= "delete"
CONST_CAST  		= "const_cast"
CONTINUE		    = "continue"
EXPLICIT 			= "explicit"
FRIEND	 			= "friend"
DYNAMIC_CAST 		= "dynamic_cast"
ENUM 				= "enum"
MUTABLE 			= "mutable"
NAMESPACE 			= "namespace"
GOTO 				= "goto"
LONG 				= "long"
PRIVATE 			= "private"
PROTECTED 			= "protected"
NEW 				= "new"
OPERATOR 			= "operator"
PUBLIC 				= "public"
REINTERPRET_CAST 	= "reinterpret_cast"
SHORT 				= "short"
STATIC_CAST 		= "static_cast"
SIGNED 				= "signed"
SIZEOF 				= "sizeof"
SWITCH 				= "switch"
TEMPLATE 			= "template"
THIS 				= "this"
THROW 				= "throw"
TRY 				= "try"
TYPENAME 			= "typename"
UNION 				= "union"
UNSIGNED 			= "unsigned"
USING 				= "using"
VIRTUAL 			= "virtual"

// Input Semantical Parameters

BINORMAL 			= "BINORMAL" 	 // Binormal 	float4
BLENDINDICES 		= "BLENDINDICES" // Blend indices 	uint
BLENDWEIGHT 		= "BLENDWEIGHT"  // Blend weights 	float
NORMAL				= "NORMAL" 		 // Normal vector 	float4
POSITIONT			= "POSITIONT" 	 // Transformed vertex position. 	float4
// TANGENT				= "TANGENT"		 // Tangent 	float4
VFACE 				= "VFACE"		 // Floating-point scalar that indicates a back-facing primitive.
VPOS 				= "VPOS" 		 // Contains the current pixel (x,y) location. 	float2

// Output Semantical parameters

FOG					= "FOG"			 //	Vertex fog 	float
TESSFACTOR			= "TESSFACTOR"   // Tessellation factor 	float
DEPTH				= "DEPTH"		 // Output depth

// Input/Output Semantical Parameters

POSITION			= POSITION([0-9])? 	 // Vertex position in object space. 	float4
TEXCOORD			= TEXCOORD([0-9])?	 // Texture coordinates
TEXUNIT 			= TEXUNIT([0-9])?	 // Texture coordinates
COLOR				= COLOR([0-9])? 		 // Diffuse and specular color 	float4
TANGENT				= TANGENT([0-9])?		 // Tangent 	float4
PSIZE 				= "PSIZE"		 // Point size 	float

// System-Value Semantics - they are new to Direct3D 10

//SV_ClipDistance			  = <<SV_ClipDistance([0-9])?>>	//Clip distance data					float
//SV_CullDistance			  = <<SV_CullDistance([0-9])?>>	//Cull distance data					float
//SV_Depth				  = "DEPTH"						//Depth buffer data						float
//SV_IsFrontFace			  = "IsFrontFace"				//A visible primitive					bool
//SV_Position				  = "POSITION"					//Vertex position in homogeneous space	float4
//SV_RenderTargetArrayIndex = "RenderTargetArrayIndex"	//Render-target array index				uint
//SV_Target				  = <<SV_Target([0-7])>>		//A render-target array					float
//SV_ViewportArrayIndex	  = "ViewportArrayIndex"		//Viewport array index					uint
//SV_InstanceID			  = "InstanceID"				//Per-instance identifier				uint
//SV_PrimitiveID			  = "PrimitiveID"				//Per-primitive identifier				uint
//SV_VertexID				  = "VertexID"					//Per-vertex identifier					uint

// Matrixes
WORLD			    = WORLD(I)?(T)?
VIEW			    = VIEW(I)?(T)?
PROJ	    		= PROJ(I)?(T)?
WORLDVIEW		    = WORLDVIEW(I)?(T)?
WORLDPROJ		    = WORLDPROJ(I)?(T)?
VIEWPROJ		    = VIEWPROJ(I)?(T)?
WORLDVIEWPROJ	    = WORLDVIEWPROJ(I)?(T)?


// Matrix Types

/** [row][column]
 * [0][0], [0][1], [0][2], [0][3]
 * [1][0], [1][1], [1][2], [1][3]
 * [2][0], [2][1], [2][2], [2][3]
 * [3][0], [3][1], [3][2], [3][3]
 */


FLOAT				= <<float([2-4](x[2-4])?)?>>
INT					= <<int([2-4](x[2-4])?)?>>
HALF				= <<half([2-4](x[2-4])?)?>>
DOUBLE				= <<double([2-4](x[2-4])?)?>>
BOOL				= <<bool([2-4](x[2-4])?)?>>


BASIC_FLOAT			= "float"
BASIC_INT			= "int"
BASIC_HALF			= "half"
BASIC_BOOL			= "bool"
BASIC_DOUBLE		= "double"
BASIC_UINT			= "uint"

// Pre-Processor Directives

PRE_DEFINE 	= <<#define[^\n\r]*>> 	// This directive is used to declare a new compiler macro.
PRE_IF 		= <<#if[^\n\r]*>>
PRE_ELSEIF 	= <<#elseif[^\n\r]*>>
PRE_ENDIF 	= <<#endif[^\n\r]*>> 	  // This set of directives is used to define a copiler conditional directive.
PRE_IFDEF  	= <<#ifdef[^\n\r]*>>
PRE_IFNDEF 	= <<#ifndef[^\n\r]*>>
PRE_ERROR 	= <<#error[^\n\r]*>>      // This directive is used to force the compiler to emit an error and is
                    	    		  // generally used in conjunction with the conditional directives.
PRE_INCLUDE = <<#include[^\n\r]*>>    // This directive is used to include an external file into the compilation process.
PRE_LINE 	= <<#line[^\n\r]*>>       // This directive is substituted with the current line number at which the directive is included within the source file.
PRE_PRAGMA 	= <<#pragma[^\n\r]*>> 	  // This directive is used to enable and control certain compiler behaviors. They will be discussed later in more details.

// Other Words

NOINTERPOLATION 	= "nointerpolation"

IDENTIFIER					 = <<[A-Za-z_][A-Za-z0-9_]*>>

QUOTED_STRING                = <<"([^"]|"")*+">>
COMMENT                      = <</\*([^*]|\*[^/])*\*/>>
COMMENTCPP                   = <<//[^\n\r]*>>
// COMMENTASM                   = <<;[^\n\r]*>> %ignore%

%s IN_BLOCK_COMMENT

%%

<YYINITIAL> {
    //
    // Keywords
    {PASS} { return PASS; }
    {TECHNIQUE} { return TECHNIQUE; }
    {SAMPLER} { return SAMPLER; }
    {SAMPLER_STATE} { return SAMPLER_STATE; }
    {VERTEX_SHADER} { return VERTEX_SHADER; }
    {PIXEL_SHADER} { return PIXEL_SHADER; }
    {REGISTER} { return REGISTER; }
    {COMPILE} { return COMPILE; }

    //
    // Literals
    {DIGIT} { return DIGIT; }
    {HEX_DIGIT} { return HEX_DIGIT; }
    {BOOLEAN} { return BOOLEAN; }
    {NUMBER} { return NUMBER; }
    {HEX_COLOR} { return HEX_COLOR; }
    {IDENTIFIER} { return IDENTIFIER; }

    //
    // Blocks
    {LBRACKET} { return LBRACKET; }
    {RBRACKET} { return RBRACKET; }
    {LCOMMENT} { return LCOMMENT; }
    {RCOMMENT} { return RCOMMENT; }
    {COMMENT} { return COMMENT; }
    {LPAREN} { return LPAREN; }
    {RPAREN} { return RPAREN; }

    //
    // Operators


}

<IN_BLOCK_COMMENT> {
    "/*" {
        if (zzNestedCommentLevel++ == 0)
            zzPostponedMarkedPos = zzStartRead;
    }

    "*/" {
        if (--zzNestedCommentLevel == 0)
            return imbueBlockComment();
    }

    <<EOF>> {
        zzNestedCommentLevel = 0;
        return imbueBlockComment();
    }

  [^]     { }
}

[^] { return BAD_CHARACTER; }