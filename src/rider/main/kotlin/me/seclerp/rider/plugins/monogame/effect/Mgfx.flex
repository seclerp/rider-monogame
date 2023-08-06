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
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

//
// Core
WHITE_SPACE = [ \t\n\r]+
LINE_WHITE_SPACE = [ \t]
ANYTHING = [^\"\n\\]

//
// Keywords
PASS = "pass"
TECHNIQUE = "technique"
SAMPLER = "sampler1D" | "sampler2D" | "sampler3D" | "samplerCUBE" | "SamplerState" | "sampler"
SAMPLER_STATE = "sampler_state"
VERTEX_SHADER = "VertexShader"
PIXEL_SHADER = "PixelShader"
REGISTER = "register"
COMPILE = "compile"

//
// Literals
DIGIT = [0-9]
HEX_DIGIT = [0-9a-fA-F]
BOOLEAN = "true" | "false" | "0" | "1"
NUMBER = ([+-]?) [ ]? [0-9]? "."? [0-9]+ [fF]?
HEX_COLOR = "0x" {HEX_DIGIT}{6} ({HEX_DIGIT}{2})?
IDENTIFIER = [A-Za-z_][A-Za-z0-9_]*

//
// Blocks
LBRACKET = "{"
RBRACKET = "}"
LCOMMENT = "/*"
RCOMMENT = "*/"
COMMENT = "//"
LPAREN = "("
RPAREN = ")"

//
// Operators
EQUALS = "="
COLON = ":"
COMMA = ","
SEMICOLON = ";"
OR = "|"
LESS_THAN = "<"
GREATER_THAN = ">"

//
// Misc
CODE = [\S]+

//
// Sampler states
MIN_FILTER = "MinFilter"
MAG_FILTER = "MagFilter"
MIP_FILTER = "MipFilter"
FILTER = "Filter"
TEXTURE = "Texture"
ADDRESS_U = "AddressU"
ADDRESS_V = "AddressV"
ADDRESS_W = "AddressW"
BORDER_COLOR = "BorderColor"
MAX_ANISOTROPY = "MaxAnisotropy"
MAX_MIP_LEVEL = "MaxMipLevel"
MIP_LOD_BIAS = "MipLodBias"

//
// Address mode
CLAMP = "Clamp"
WRAP = "Wrap"
MIRROR = "Mirror"
BORDER = "Border"

//
// Texture filters
NONE = "None"
LINEAR = "Linear"
POINT = "Point"
ANISOTROPIC = "Anisotropic"

//
// Render states
ALPHA_BLEND_ENABLE = "AlphaBlendEnable"
SRC_BLEND = "SrcBlend"
DEST_BLEND = "DestBlend"
BLEND_OP = "BlendOp"
COLOR_WRITE_ENABLE = "ColorWriteEnable"
Z_ENABLE = "ZEnable"
Z_WRITE_ENABLE = "ZWriteEnable"
Z_FUNC = "ZFunc"
DEPTH_BIAS = ""DepthBias
CULL_MODE = "CullMode"
FILL_MODE = "FillMode"
MULTI_SAMPLE_ANTI_ALIAS = "MultiSampleAntiAlias"
SCISSOR_TEST_ENABLE = "ScissorTestEnable"
SLOPE_SCALE_DEPTH_BIAS = "SlopeScaleDepthBias"
STENCIL_ENABLE = "StencilEnable"
STENCIL_FAIL = "StencilFail"
STENCIL_FUNC = "StencilFunc"
STENCIL_MASK = "StencilMask"
STENCIL_PASS = "StencilPass"
STENCIL_REF = "StencilRef"
STENCIL_WRITE_MASK = "StencilWriteMask"
STENCIL_Z_FAIL = "StencilZFail"

//
// Compare functions
NEVER = "Never"
LESS = "Less"
EQUAL = "Equal"
LESS_EQUAL = "LessEqual"
GREATER = "Greater"
NOT_EQUAL= "NotEqual"
GREATER_EQUAL = "GreaterEqual"
ALWAYS = "Always"

//
// Stencil operations
KEEP = "Keep"
ZERO = "Zero"
REPLACE = "Replace"
INCR_SET = "IncrSat"
DECR_SET = "DecrSat"
INVERT = "Invert"
INCR = "Incr"
DECR = "Decr"

//
// Colors
RED = "Red"
GREEN = "Green"
BLUE = "Blue"
ALPHA = "Alpha"
ALL = "All"

//
// Culling mode
CW = "Cw" // Clock-wise
CCW = "Ccw" // Counter clock-wise

//
// Fill modes
SOLID = "Solid"
WIRE_FRAME = "WireFrame"

//
// Blend functions
ADD = "Add"
SUBTRACT = "Subtract"
REV_SUBTRACT = "RevSubtract"
MIN = "Min"
MAX = "Max"

//
// Blend
ZERO = "Zero"
ONE = "One"
SRC_COLOR = "SrcColor"
INV_SRC_COLOR = "InvSrcColor"
SRC_ALPHA = "SrcAlpha"
INV_SRC_ALPHA = "InvSrcAlpha"
DEST_ALPHA = "DestAlpha"
INV_DEST_ALPHA = "InvDestAlpha"
DEST_COLOR = "DestColor"
INV_DEST_COLOR = "InvDestColor"
SRC_ALPHA_SAT = "SrcAlphaSat"
BLEND_FACTOR = "BlendFactor"
INV_BLEND_FACTOR = "InvBlendFactor"

// #line 123 "file.fx"
LINE_PRAGMA = {LINE_WHITE_SPACE}* "#line" {LINE_WHITE_SPACE}* {DIGIT}+ ({LINE_WHITE_SPACE}* \" {ANYTHING}*\")?\n

SAMPLER = "sampler1D" | "sampler2D" | "sampler3D" | "samplerCUBE" | "SamplerState" | "sampler"

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