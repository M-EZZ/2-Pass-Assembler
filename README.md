# 2-Pass-Assembler For the SIC/XE machine

## Main functions 

*Translate mnemonic operation codes to their machine language equivalents.

*Assign machine addresses to symbolic labels (e.g., jump labels, variable
names) used by the programmers.

*Use proper addressing modes and formats to build efficient machine instructions.

*Translate data constants into internal machine representations.

*Output the object program and provide other information (e.g., for linker and loader).

*Depend heavily on the source language it translates and the machine language it produces.   
  E.g., the instruction format and addressing modes. 

********************************************************************************

- SIC Format 1 and Format 2 support
- SIC/XE Format 3 and Format 4 support
- Literals support
- SIC/XE Register-to-Register instructions
- Addressing modes supported:
  1. Immediate addressing
  2. Indirect addressing
  3. Base-relative indexing
  4. PC-relative indexing


1.2 - SIC/XE Format Specifications

Instruction format field lengths are specified within {}, and are 1-bit long when
the braces are omitted.

- Format 1

  Length: 1 byte
  Format: [ opcode{8} ]

- Format 2

  Length: 2 bytes
  Format: [ opcode{8} ][ r1{4} ][ r2{4} ]

- Format 3 (SIC upward-compatible)

  Length: 3 bytes
  Format: [ opcode{6} ][n][i][x][b][p][e][ disp{12} ]

- Format 4 (SIC/XE only)

  Length: 4 bytes
  Format: [ opcode{6} ][n][i][x][b][p][e][ target address{20} ]

  Notation:

  Format 4 (extended) instructions are denoted by prefixing the first operand with '+', ie:
  +LDT    #MAXLEN