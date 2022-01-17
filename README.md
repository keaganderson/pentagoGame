# Pentago
A game of Pentago against an Artificial Intelligence

# Game Rules

This is a link to the official Pentago Rules to understand how the game is played.
https://www.ultraboardgames.com/pentago/game-rules.php

# Instructions

After running java file, program will prompt you for a color. This decides who goes first. It will then prompt you for a move. Enter a move
in the format "4/1 1R" and it will process it.

Take turns until the game is over.

Format is representative of the move in the form "(game board to add piece)/(location in board) (gameboard to rotate)/(direction to rotate)"

# How the Algorithm Works

Implemented a utility function that calculated both what the player's highest in a row, column, or diagonal pieces are on the board,
and what the computer's highest in a row, column, or diagonal pieces are and compare them. This means the utility function incorperates both its own progression and the player's.
This leads to the computer very often blocking the player and not allowing you to win unless you have it trapped in a corner with 2 possible win moves.

It weighs wins heavily so that it avoids the player winning at all cost and prioritizes the AI winning.
