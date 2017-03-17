# 2-Pass-Assembler For the SIC/XE machine

/* Depend heavily on the source language it           *
 * translates and the machine language it produces.   *
 * E.g., the instruction format and addressing modes. */

Main functions 

Translate mnemonic operation codes to theirmachine language equivalents.

Assign machine addresses to symbolic labels (e.g., jump labels, variable
names) used by the programmers.

Use proper addressing modes and formats to build efficient machine instructions.

Translate data constants into internal machine representations.

Output the object program and provide other information (e.g., for linker and loader).
